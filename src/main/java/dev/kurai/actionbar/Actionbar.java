package dev.kurai.actionbar;

import dev.kurai.actionbar.entry.ActionbarEntry;
import java.time.Duration;
import java.util.Collection;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.NonNull;

/**
 * Represents a collection of {@link ActionbarEntry entries} displayed together in a player's
 * action bar. Entries are keyed by an Adventure {@link Key} so they can be individually updated or
 * removed at any time.
 *
 * <p>Use {@link #create()} to obtain a new instance, or retrieve a player-scoped one through
 * {@link dev.kurai.actionbar.service.ActionbarService#actionbar(java.util.UUID)}.
 */
public sealed interface Actionbar permits ActionbarImpl {

  /**
   * Creates a new, empty {@code Actionbar}.
   *
   * @return a fresh {@code Actionbar} instance
   */
  @Contract(value = " -> new", pure = true)
  static @NonNull Actionbar create() {
    return new ActionbarImpl();
  }

  /**
   * Returns all currently registered entries.
   *
   * @return a live, mutable view of the entries
   */
  Collection<ActionbarEntry> entries();

  /**
   * Registers a persistent entry (no expiry) with the given key and text component.
   *
   * @param key   the unique identifier for this entry
   * @param value the text component to display
   */
  default void registerEntry(final Key key, final Component value) {
    this.registerEntry(new ActionbarEntry(key, value));
  }

  /**
   * Registers a time-limited entry that expires after {@code duration} has elapsed.
   *
   * @param key      the unique identifier for this entry
   * @param value    the text component to display
   * @param duration how long the entry should remain visible; must be positive
   */
  default void registerEntry(final Key key, final Component value, final Duration duration) {
    this.registerEntry(new ActionbarEntry(key, value, duration));
  }

  /**
   * Registers a pre-built {@link ActionbarEntry}. If an entry with the same key already exists
   * it is replaced.
   *
   * @param entry the entry to register; must not be {@code null}
   */
  void registerEntry(final ActionbarEntry entry);

  /**
   * Removes the entry identified by {@code key}, if present.
   *
   * @param key the key of the entry to remove
   */
  void unregisterEntry(final Key key);

  /**
   * Returns the entry associated with {@code key}, or {@code null} if none is registered.
   *
   * @param key the key to look up
   * @return the matching entry, or {@code null}
   */
  ActionbarEntry entry(final Key key);
}
