package dev.kurai.actionbar;

import com.google.common.collect.Maps;
import java.util.Collection;
import java.util.Map;

import dev.kurai.actionbar.entry.ActionbarEntry;
import net.kyori.adventure.key.Key;

/**
 * Default {@link Actionbar} implementation backed by a {@link java.util.HashMap}.
 * Entries are stored and retrieved by their {@link Key}.
 */
final class ActionbarImpl implements Actionbar {

  private final Map<Key, ActionbarEntry> entries;

  /** Creates a new {@code ActionbarImpl} with an empty entry map. */
  public ActionbarImpl() {
    this.entries = Maps.newHashMap();
  }

  /** {@inheritDoc} */
  @Override
  public Collection<ActionbarEntry> entries() {
    return this.entries.values();
  }

  /** {@inheritDoc} */
  @Override
  public void registerEntry(final ActionbarEntry entry) {
    this.entries.put(entry.key(), entry);
  }

  /** {@inheritDoc} */
  @Override
  public void unregisterEntry(final Key key) {
    this.entries.remove(key);
  }

  /** {@inheritDoc} */
  @Override
  public ActionbarEntry entry(final Key key) {
    return this.entries.get(key);
  }
}
