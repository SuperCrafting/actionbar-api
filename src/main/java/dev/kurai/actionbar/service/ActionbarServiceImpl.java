package dev.kurai.actionbar.service;

import com.google.common.collect.Maps;
import dev.kurai.actionbar.Actionbar;
import dev.kurai.actionbar.task.ActionbarUpdaterTask;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;

import net.kyori.adventure.audience.Audience;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

/**
 * Default {@link ActionbarService} implementation that stores per-player {@link Actionbar}
 * instances in a {@link java.util.HashMap} and schedules
 * {@link ActionbarUpdaterTask} to run asynchronously every tick (period = 1).
 */
final class ActionbarServiceImpl implements ActionbarService {

  /** Stores each online player's actionbar, keyed by their unique ID. */
  private final Map<UUID, Actionbar> actionbars = Maps.newHashMap();

  /**
   * Constructs the service and immediately schedules the async update task.
   *
   * @param plugin           the plugin that owns the scheduled task
   * @param audienceProvider maps a {@link Player} to the target {@link Audience}
   */
  public ActionbarServiceImpl(
      final Plugin plugin, final Function<Player, Audience> audienceProvider) {
    Bukkit.getScheduler()
        .runTaskTimerAsynchronously(
            plugin, new ActionbarUpdaterTask(this, audienceProvider), 0L, 1L);
  }

  /**
   * {@inheritDoc}
   *
   * <p>Creates a new empty {@link Actionbar} on first access for the given {@code holder}.
   */
  @Override
  public Actionbar actionbar(final UUID holder) {
    return this.actionbars.computeIfAbsent(holder, _ -> Actionbar.create());
  }
}
