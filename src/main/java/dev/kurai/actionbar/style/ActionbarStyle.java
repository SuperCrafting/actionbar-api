package dev.kurai.actionbar.style;

import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.DARK_GRAY;

import lombok.Builder;
import lombok.Builder.Default;
import lombok.Getter;
import net.kyori.adventure.text.Component;

/**
 * Defines the visual framing used when rendering a player's action bar.
 *
 * <p>Three components control the layout:
 *
 * <ul>
 *   <li>{@link #prefixComponent} — rendered once at the very start (default: {@code »})
 *   <li>{@link #separatorComponent} — rendered between adjacent entries (default: {@code ❘})
 *   <li>{@link #suffixComponent} — rendered once at the very end (default: {@code «})
 * </ul>
 *
 * <p>Use {@link #DEFAULT} for the built-in dark-gray style, or build a custom one:
 *
 * <pre>{@code
 * ActionbarStyle style = ActionbarStyle.builder()
 *     .prefixComponent(Component.text("[", NamedTextColor.GOLD))
 *     .separatorComponent(Component.text("|", NamedTextColor.GRAY))
 *     .suffixComponent(Component.text("]", NamedTextColor.GOLD))
 *     .build();
 * }</pre>
 */
@Builder
@Getter
public final class ActionbarStyle {

  /** The default style: {@code » entry1 ❘ entry2 «} rendered in dark gray. */
  public static final ActionbarStyle DEFAULT = ActionbarStyle.builder().build();

  /** Component placed before the first entry. Defaults to {@code »} in dark gray. */
  @Default private final Component prefixComponent = text('»', DARK_GRAY);

  /** Component placed between consecutive entries. Defaults to {@code ❘} in dark gray. */
  @Default private final Component separatorComponent = text('❘', DARK_GRAY);

  /** Component placed after the last entry. Defaults to {@code «} in dark gray. */
  @Default private final Component suffixComponent = text('«', DARK_GRAY);
}
