package dev.kurai.actionbar.entry;

import static java.util.Objects.requireNonNull;

import java.time.Duration;
import java.time.Instant;
import lombok.Getter;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.key.Keyed;
import net.kyori.adventure.text.Component;

/**
 * An immutable entry in an action bar, consisting of a unique {@link Key}, a display {@link
 * Component}, and an optional lifetime {@link Duration}.
 *
 * <p>Entries created without a duration (or with {@link Duration#ZERO}) never expire. Entries
 * created with a positive duration expire once {@code creationTime + duration} is in the past, as
 * determined by {@link #expired()}.
 *
 * <p>All fields are exposed via Lombok's {@code @Getter}.
 */
@Getter
public final class ActionbarEntry implements Keyed {

  /** Unique identifier used to register, look up, and sort this entry. */
  private final Key key;

  /** Text component rendered in the action bar for this entry. */
  private final Component value;

  /** The wall-clock time at which this entry was constructed. */
  private final Instant creationTime;

  /** How long this entry lives. {@link Duration#ZERO} means the entry never expires. */
  private final Duration duration;

  /**
   * Creates a persistent entry that never expires.
   *
   * @param key the unique identifier for this entry
   * @param value the text component to display
   */
  public ActionbarEntry(final Key key, final Component value) {
    this(key, value, Duration.ZERO);
  }

  /**
   * Creates a time-limited entry that expires after {@code duration} has elapsed.
   *
   * @param key the unique identifier for this entry
   * @param value the text component to display
   * @param duration the lifetime of this entry; use {@link Duration#ZERO} for no expiry
   */
  public ActionbarEntry(final Key key, final Component value, final Duration duration) {
    this.key = requireNonNull(key, "Key cannot be null");
    this.value = requireNonNull(value, "Value component cannot be null");

    this.creationTime = Instant.now();

    this.duration = requireNonNull(duration, "Duration cannot be null");
  }

  /**
   * Returns {@code true} if this entry has a non-zero duration and its lifetime has elapsed.
   *
   * @return {@code true} if expired, {@code false} if still valid or persistent
   */
  public boolean expired() {
    return !this.duration.isZero() && Instant.now().isAfter(this.creationTime.plus(this.duration));
  }
}
