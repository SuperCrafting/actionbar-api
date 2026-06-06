package dev.kurai.actionbar;

import dev.kurai.actionbar.style.ActionbarStyle;
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
 * <p>Obtain an instance via {@link #actionbarService(Plugin, Function)}. The returned service
 * immediately schedules an asynchronous repeating task that refreshes every player's action bar
 * each tick.
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
  static ActionbarService actionbarService(
      final Plugin plugin, final Function<Player, Audience> audienceProvider) {
    return actionbarService(plugin, audienceProvider, ActionbarStyle.DEFAULT);
  }

  /**
   * Creates a new {@code ActionbarService} with a custom {@link ActionbarStyle} and starts the
   * asynchronous tick loop.
   *
   * @param plugin the owning plugin used to schedule the task
   * @param audienceProvider a function that maps a {@link Player} to the {@link Audience} that
   *     receives action-bar packets
   * @param style the visual actionbarStyle applied when rendering entries
   * @return a running {@code ActionbarService} instance
   */
  @Contract(value = "_, _, _ -> new", pure = true)
  static ActionbarService actionbarService(
      final Plugin plugin,
      final Function<Player, Audience> audienceProvider,
      final ActionbarStyle style) {
    return new ActionbarServiceImpl(plugin, audienceProvider, style);
  }

  /**
   * Returns the {@link ActionbarStyle} used when rendering entries for all players.
   *
   * @return the active actionbarStyle; never {@code null}
   */
  ActionbarStyle actionbarStyle();

  /**
   * Returns the {@link Actionbar} associated with the holder id, creating one lazily if none exists
   * yet.
   *
   * @param holderId the player's unique ID
   * @return the player's action bar; never {@code null}
   */
  Actionbar actionbar(final UUID holderId);
}
