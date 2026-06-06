package dev.kurai.actionbar.style;

import lombok.Builder;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

/**
 * Defines the visual framing used when rendering a player's action bar.
 *
 * <p>Three components control the layout:
 *
 * <ul>
 *   <li>{@link #prefix} — rendered once at the very start (default: {@code »})
 *   <li>{@link #separator} — rendered between adjacent entries (default: {@code ❘})
 *   <li>{@link #suffix} — rendered once at the very end (default: {@code «})
 * </ul>
 *
 * <p>Use {@link #DEFAULT} for the built-in dark-gray style, or build a custom one:
 *
 * <pre>{@code
 * ActionbarStyle style = ActionbarStyle.builder()
 *     .prefix(Component.text("[", NamedTextColor.GOLD))
 *     .separator(Component.text("|", NamedTextColor.GRAY))
 *     .suffix(Component.text("]", NamedTextColor.GOLD))
 *     .build();
 * }</pre>
 */
@Builder
@Getter
public final class ActionbarStyle {

  /** The default style: {@code » entry1 ❘ entry2 «} rendered in dark gray. */
  public static final ActionbarStyle DEFAULT = ActionbarStyle.builder().build();

  /** Component placed before the first entry. Defaults to {@code »} in dark gray. */
  @lombok.Builder.Default
  private final Component prefix = Component.text('»', NamedTextColor.DARK_GRAY);

  /** Component placed between consecutive entries. Defaults to {@code ❘} in dark gray. */
  @lombok.Builder.Default
  private final Component separator = Component.text('❘', NamedTextColor.DARK_GRAY);

  /** Component placed after the last entry. Defaults to {@code «} in dark gray. */
  @lombok.Builder.Default
  private final Component suffix = Component.text('«', NamedTextColor.DARK_GRAY);
}
