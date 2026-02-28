/**
 * SVG sprite parsing support.
 *
 * <p><strong>Parser selection:</strong> Nano is the default parser. Use a pragma
 * to explicitly select SAX parsing in a diagram:
 * <pre>
 * !pragma svgparser sax
 * </pre>
 * or to reassert Nano:
 * <pre>
 * !pragma svgparser nano
 * </pre>
 *
 * <p><strong>Nano parser constraints:</strong>
 * <ul>
 *   <li>Legacy, regex-based parsing with a limited SVG subset.</li>
 *   <li>No CSS <code>&lt;style&gt;</code> blocks or class selectors.</li>
 *   <li>Definitions and references (such as <code>&lt;defs&gt;</code> and <code>&lt;use&gt;</code>)
 *   are not parsed.</li>
 *   <li>SVG gradients are not parsed.</li>
 * </ul>
 *
 * <p><strong>SAX parser constraints:</strong>
 * <ul>
 *   <li>The SAX-based parser supports only inline attributes.</li>
 *   <li>CSS <code>&lt;style&gt;</code> blocks and class selectors are not parsed.</li>
 *   <li>The SAX parser honors <code>text-anchor</code> for <code>&lt;text&gt;</code>
 *   elements; positioning can differ from the Nano parser when anchors are used.</li>
 *   <li>Embedded raster images via data URIs (PNG/JPEG) are supported; external URLs
 *   and embedded SVG images are not.</li>
 * </ul>
 *
 * <p><strong>SAX parser additional features:</strong>
 * <ul>
 *   <li>Broader SVG element coverage including rect, circle, ellipse, line, polyline,
 *   polygon, path, text, image, group, defs, symbol, and use.</li>
 *   <li>Transform parsing for translate, rotate, scale, and matrix.</li>
 *   <li>Consistent inline styling across supported elements.</li>
 *   <li>Linear gradient fills (with simplified stop handling).</li>
 * </ul>
 *
 * <p><strong>SAX gradient constraints:</strong>
 * <ul>
 *   <li>Linear gradients are supported, but only the first and last stops are used.</li>
 *   <li>Intermediate stops, offsets, and <code>stop-opacity</code> are ignored.</li>
 *   <li>Gradient direction is approximated to horizontal, vertical, or diagonal.</li>
 *   <li>Radial gradients are not supported.</li>
 * </ul>
 */
package net.sourceforge.plantuml.svg.parser;
