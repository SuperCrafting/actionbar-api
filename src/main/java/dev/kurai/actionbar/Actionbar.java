package dev.kurai.actionbar;

import dev.kurai.actionbar.entry.ActionbarEntry;
import java.time.Duration;
import java.util.Collection;
import java.util.function.Predicate;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.UnmodifiableView;

/**
 * Represents a collection of {@link ActionbarEntry entries} displayed together in a player's action
 * bar. Entries are keyed by an Adventure {@link Key} so they can be individually updated or removed
 * at any time.
 *
 * <p>Use {@link #actionbar()} to get a new instance, or retrieve a player-scoped one through {@link
 * ActionbarService#actionbar(java.util.UUID)}.
 */
public sealed interface Actionbar permits ActionbarImpl {

  /**
   * Creates a new, empty {@code Actionbar}.
   *
   * @return a fresh {@code Actionbar} instance
   */
  @Contract(value = " -> new", pure = true)
  static Actionbar actionbar() {
    return new ActionbarImpl();
  }

  /**
   * Returns all currently registered entries.
   *
   * @return a live, mutable view of the entries
   */
  @UnmodifiableView
  Collection<ActionbarEntry> actionbarEntries();

  /**
   * Registers a persistent entry (no expiry) with the given key and text component.
   *
   * @param key the unique identifier for this entry
   * @param valueComponent the text component to display
   */
  default void registerActionbarEntry(final Key key, final Component valueComponent) {
    this.registerActionbarEntry(new ActionbarEntry(key, valueComponent));
  }

  /**
   * Registers a time-limited entry that expires after {@code duration} has elapsed.
   *
   * @param key the unique identifier for this entry
   * @param valueComponent the text component to display
   * @param duration how long the entry should remain visible; must be positive
   */
  default void registerActionbarEntry(
      final Key key, final Component valueComponent, final Duration duration) {
    this.registerActionbarEntry(new ActionbarEntry(key, valueComponent, duration));
  }

  /**
   * Registers a pre-built {@link ActionbarEntry}. If an entry with the same key already exists it
   * is replaced.
   *
   * @param actionbarEntry the entry to register; must not be {@code null}
   */
  void registerActionbarEntry(final ActionbarEntry actionbarEntry);

  /**
   * Removes the entry identified by {@code key}, if present.
   *
   * @param key the key of the entry to remove
   */
  void unregisterActionbarEntry(final Key key);

  /**
   * Removes the entries matching the given filter.
   *
   * @param actionbarEntryFilter the filter to apply; must not be {@code null}
   */
  void unregisterActionbarEntriesIf(final Predicate<ActionbarEntry> actionbarEntryFilter);

  /**
   * Returns the entry associated with {@code key}, or {@code null} if none is registered.
   *
   * @param key the key to look up
   * @return the matching entry, or {@code null}
   */
  ActionbarEntry actionbarEntry(final Key key);
}
