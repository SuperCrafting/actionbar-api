package dev.kurai.actionbar;

import static java.util.Objects.requireNonNull;

import com.google.common.collect.Maps;
import dev.kurai.actionbar.style.ActionbarStyle;
import dev.kurai.actionbar.task.ActionbarUpdaterTask;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import lombok.Getter;
import net.kyori.adventure.audience.Audience;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

/**
 * Default {@link ActionbarService} implementation that stores per-player {@link Actionbar}
 * instances in a {@link java.util.concurrent.ConcurrentHashMap} and schedules {@link
 * ActionbarUpdaterTask} to run asynchronously every tick (period = 1).
 */
final class ActionbarServiceImpl implements ActionbarService {

  @Getter private final ActionbarStyle actionbarStyle;

  /** Stores each online player's actionbar, keyed by their unique ID. */
  private final Map<UUID, Actionbar> actionbars;

  /**
   * Constructs the service and immediately schedules the async update task.
   *
   * @param plugin the plugin that owns the scheduled task
   * @param audienceProvider maps a {@link Player} to the target {@link Audience}
   * @param actionbarStyle the visual style to use when rendering entries
   */
  public ActionbarServiceImpl(
      final Plugin plugin,
      final Function<Player, Audience> audienceProvider,
      final ActionbarStyle actionbarStyle) {
    requireNonNull(plugin, "Plugin cannot be null");
    requireNonNull(audienceProvider, "Audience provider cannot be null");

    this.actionbarStyle = requireNonNull(actionbarStyle, "Actionbar style cannot be null");

    this.actionbars = Maps.newConcurrentMap();

    Bukkit.getScheduler()
        .runTaskTimerAsynchronously(
            plugin, new ActionbarUpdaterTask(this, audienceProvider), 0L, 1L);
  }

  /** {@inheritDoc} */
  @Override
  public Actionbar actionbar(final UUID holderId) {
    return this.actionbars.computeIfAbsent(
        requireNonNull(holderId, "Holder ID cannot be null"), _ -> Actionbar.actionbar());
  }
}
