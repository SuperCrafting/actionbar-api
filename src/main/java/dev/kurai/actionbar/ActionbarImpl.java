package dev.kurai.actionbar;

import static java.util.Objects.requireNonNull;

import dev.kurai.actionbar.entry.ActionbarEntry;
import java.util.Collections;
import java.util.SequencedCollection;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.function.Predicate;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.UnmodifiableView;

/**
 * Default {@link Actionbar} implementation backed by a concurrent, key-ordered map. Entries are
 * stored and retrieved by their {@link Key}.
 */
@ApiStatus.Internal
final class ActionbarImpl implements Actionbar {

  private static final String KEY_CANNOT_BE_NULL = "Key cannot be null";

  private final ConcurrentNavigableMap<Key, ActionbarEntry> actionbarEntries =
      new ConcurrentSkipListMap<>();
  private final SequencedCollection<ActionbarEntry> actionbarEntriesView =
      Collections.unmodifiableSequencedCollection(this.actionbarEntries.sequencedValues());

  /** {@inheritDoc} */
  @Override
  @UnmodifiableView
  public SequencedCollection<ActionbarEntry> actionbarEntries() {
    return this.actionbarEntriesView;
  }

  /** {@inheritDoc} */
  @Override
  public void registerActionbarEntry(final ActionbarEntry actionbarEntry) {
    requireNonNull(actionbarEntry, "Actionbar entry cannot be null");

    this.actionbarEntries.put(actionbarEntry.key(), actionbarEntry);
  }

  /** {@inheritDoc} */
  @Override
  public void unregisterActionbarEntry(final Key key) {
    this.actionbarEntries.remove(requireNonNull(key, KEY_CANNOT_BE_NULL));
  }

  /** {@inheritDoc} */
  @Override
  public void unregisterActionbarEntriesIf(final Predicate<ActionbarEntry> actionbarEntryFilter) {
    this.actionbarEntries
        .values()
        .removeIf(requireNonNull(actionbarEntryFilter, "Actionbar entry filter cannot be null"));
  }

  /** {@inheritDoc} */
  @Override
  public ActionbarEntry actionbarEntry(final Key key) {
    return this.actionbarEntries.get(requireNonNull(key, KEY_CANNOT_BE_NULL));
  }
}
