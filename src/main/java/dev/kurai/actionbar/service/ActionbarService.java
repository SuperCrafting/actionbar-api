package dev.kurai.actionbar.service;

import dev.kurai.actionbar.Actionbar;
import java.util.UUID;
import java.util.function.Function;
import net.kyori.adventure.audience.Audience;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.Contract;

/**
 * Manages per-player {@link Actionbar} instances and drives the recurring update cycle that sends
 * action-bar packets to online players.
 *
 * <p>Obtain an instance via {@link #create(Plugin, Function)}. The returned service immediately
 * schedules an asynchronous repeating task that refreshes every player's action bar each tick.
 *
 * <p>Example usage:
 *
 * <pre>{@code
 * ActionbarService service = ActionbarService.create(plugin, player -> BukkitAudiences.of(plugin).player(player));
 * service.actionbar(player.getUniqueId()).registerEntry(Key.key("myplugin", "health"), healthComponent);
 * }</pre>
 */
public sealed interface ActionbarService permits ActionbarServiceImpl {

  /**
   * Creates a new {@code ActionbarService} and starts the asynchronous tick loop.
   *
   * @param plugin the owning plugin used to schedule the task
   * @param audienceProvider a function that maps a {@link Player} to the {@link Audience} that
   *     receives action-bar packets
   * @return a running {@code ActionbarService} instance
   */
  @Contract(value = "_, _ -> new", pure = true)
  static ActionbarService create(
      final Plugin plugin, final Function<Player, Audience> audienceProvider) {
    return new ActionbarServiceImpl(plugin, audienceProvider);
  }

  /**
   * Returns the {@link Actionbar} associated with {@code holder}, creating one lazily if none
   * exists yet.
   *
   * @param holder the player's unique ID
   * @return the player's action bar; never {@code null}
   */
  Actionbar actionbar(final UUID holder);
}
