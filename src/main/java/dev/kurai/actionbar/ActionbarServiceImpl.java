package dev.kurai.actionbar;

import static java.util.Objects.requireNonNull;

import com.google.common.collect.Maps;
import dev.kurai.actionbar.update.configuration.ActionbarUpdateConfiguration;
import java.util.UUID;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Function;
import lombok.Getter;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.JoinConfiguration;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.ApiStatus;

/**
 * Default {@link ActionbarService} implementation that stores per-holder {@link Actionbar}
 * instances in a concurrent map and schedules an {@link ActionbarUpdateRunnable} asynchronously
 * with a one-tick period.
 */
@ApiStatus.Internal
final class ActionbarServiceImpl implements ActionbarService {

  /**
   * Thanks to <a href="https://github.com/SuperCrafting/">Super_Crafting</a> for the suggestion.
   */
  @Getter private final JoinConfiguration joinConfiguration;

  private final ConcurrentMap<UUID, Actionbar> actionbars;

  /**
   * Constructs a service and immediately schedules its asynchronous recurring update task.
   *
   * @param plugin the plugin that owns the scheduled task
   * @param audienceProvider a function that maps each {@link Player} to its target {@link Audience}
   * @param joinConfiguration the join configuration used to compose action-bar entries
   */
  ActionbarServiceImpl(
      final Plugin plugin,
      final Function<Player, Audience> audienceProvider,
      final JoinConfiguration joinConfiguration,
      final ActionbarUpdateConfiguration actionbarUpdateConfiguration) {
    requireNonNull(plugin, "Plugin cannot be null");
    requireNonNull(audienceProvider, "Audience provider cannot be null");
    requireNonNull(joinConfiguration, "Join configuration cannot be null");
    requireNonNull(actionbarUpdateConfiguration, "Actionbar update configuration cannot be null");

    final long initialActionbarUpdateTaskDelay = actionbarUpdateConfiguration.initialDelay();
    if (initialActionbarUpdateTaskDelay < 0L) {
      throw new IllegalArgumentException(
          "Initial actionbar update task delay cannot be less than zero");
    }

    final long actionbarUpdateTaskPeriod = actionbarUpdateConfiguration.period();
    if (actionbarUpdateTaskPeriod < 1L) {
      throw new IllegalArgumentException("Actionbar update task period cannot be less than 1");
    }

    this.joinConfiguration = joinConfiguration;

    this.actionbars = Maps.newConcurrentMap();

    Bukkit.getScheduler()
        .runTaskTimerAsynchronously(
            plugin,
            new ActionbarUpdateRunnable(this, audienceProvider),
            initialActionbarUpdateTaskDelay,
            actionbarUpdateTaskPeriod);
  }

  /** {@inheritDoc} */
  @Override
  public Actionbar actionbar(final UUID holderId) {
    return this.actionbars.computeIfAbsent(
        requireNonNull(holderId, "Holder ID cannot be null"), _ -> Actionbar.actionbar());
  }
}
