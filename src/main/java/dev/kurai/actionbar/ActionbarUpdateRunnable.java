package dev.kurai.actionbar;

import static java.util.Objects.requireNonNull;

import dev.kurai.actionbar.entry.ActionbarEntry;
import java.util.function.Function;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.JoinConfiguration;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.ApiStatus;

/**
 * Runnable task that runs every tick to refresh action bars for online players with active entries.
 *
 * <p>On each invocation it:
 *
 * <ol>
 *   <li>Evicts expired {@link ActionbarEntry entries} from each player's {@link Actionbar}.
 *   <li>Composes the remaining entries using the service's {@link JoinConfiguration}.
 *   <li>Sends the resulting component via the configured audience provider.
 * </ol>
 *
 * <p>Players with no entries are skipped — no packet is sent.
 */
@ApiStatus.Internal
final class ActionbarUpdateRunnable implements Runnable {

  private final ActionbarService actionbarService;
  private final Function<Player, Audience> audienceProvider;

  ActionbarUpdateRunnable(
      final ActionbarService actionbarService, final Function<Player, Audience> audienceProvider) {
    this.actionbarService = requireNonNull(actionbarService, "Actionbar service cannot be null");
    this.audienceProvider = requireNonNull(audienceProvider, "Audience provider cannot be null");
  }

  @Override
  public void run() {
    final JoinConfiguration joinConfiguration = this.actionbarService.joinConfiguration();
    for (final Player player : Bukkit.getOnlinePlayers()) {
      final Actionbar actionbar = this.actionbarService.actionbar(player.getUniqueId());
      actionbar.unregisterActionbarEntriesIf(ActionbarEntry::expired);

      final var actionbarEntries = actionbar.actionbarEntries();
      if (!actionbarEntries.isEmpty()) {
        this.audienceProvider
            .apply(player)
            .sendActionBar(Component.join(joinConfiguration, actionbarEntries));
      }
    }
  }
}
