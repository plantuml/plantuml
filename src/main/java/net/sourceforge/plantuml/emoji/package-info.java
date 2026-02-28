/**
 * Provides classes used to manage the
 * <a href="https://plantuml.com/en/creole#68305e25f5788db0" target="_top">
 * PlantUML Emoji</a> icon set.
 *
 * <p>Key responsibilities:
 * <ul>
 *   <li>{@link net.sourceforge.plantuml.emoji.Emoji} loads emoji SVG data and
 *   exposes draw helpers for sprites and shortcuts.</li>
 *   <li>{@link net.sourceforge.plantuml.emoji.ColorResolver} resolves color and
 *   monochrome transformations using {@link net.sourceforge.plantuml.emoji.GrayLevelRange}.</li>
 *   <li>{@link net.sourceforge.plantuml.emoji.UGraphicWithScale} provides a scaled
 *   drawing context for emoji rendering.</li>
 * </ul>
 *
 * <p>Sub-package {@code net.sourceforge.plantuml.emoji.data} contains bundled
 * emoji resources (such as the emoji list and SVG files) and a marker class
 * used for resource loading.
 *
 * @see net.sourceforge.plantuml.openiconic
 */
package net.sourceforge.plantuml.emoji;
