package dev.kurai.actionbar;

import static java.util.Objects.requireNonNull;

import com.google.common.collect.Maps;
import dev.kurai.actionbar.entry.ActionbarEntry;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.function.Predicate;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.UnmodifiableView;

/**
 * Default {@link Actionbar} implementation backed by a {@link java.util.HashMap}. Entries are
 * stored and retrieved by their {@link Key}.
 */
final class ActionbarImpl implements Actionbar {

  private static final String KEY_CANNOT_BE_NULL = "Key cannot be null";

  private final Map<Key, ActionbarEntry> entries = Maps.newConcurrentMap();
  private final Collection<ActionbarEntry> entriesView =
      Collections.unmodifiableCollection(this.entries.values());

  /** {@inheritDoc} */
  @Override
  @UnmodifiableView
  public Collection<ActionbarEntry> entries() {
    return this.entriesView;
  }

  /** {@inheritDoc} */
  @Override
  public void registerEntry(final ActionbarEntry entry) {
    requireNonNull(entry, "Actionbar entry cannot be null");

    this.entries.put(entry.key(), entry);
  }

  /** {@inheritDoc} */
  @Override
  public void unregisterEntry(final Key key) {
    this.entries.remove(requireNonNull(key, KEY_CANNOT_BE_NULL));
  }

  @Override
  public void unregisterEntriesIf(final Predicate<ActionbarEntry> actionbarEntryFilter) {
    this.entries
        .values()
        .removeIf(requireNonNull(actionbarEntryFilter, "Actionbar entry filter cannot be null"));
  }

  /** {@inheritDoc} */
  @Override
  public ActionbarEntry entry(final Key key) {
    return this.entries.get(requireNonNull(key, KEY_CANNOT_BE_NULL));
  }
}
