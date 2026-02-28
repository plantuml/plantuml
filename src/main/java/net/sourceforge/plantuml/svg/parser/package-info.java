/**
 * SVG sprite parsing support.
 *
 * <p><strong>Parser constraints:</strong>
 * <ul>
 *   <li>The SAX-based parser supports only inline attributes.</li>
 *   <li>CSS <code>&lt;style&gt;</code> blocks and class selectors are not parsed.</li>
 *   <li>The SAX parser honors <code>text-anchor</code> for <code>&lt;text&gt;</code>
 *   elements; positioning can differ from the Nano parser when anchors are used.</li>
 *   <li>Embedded raster images via data URIs (PNG/JPEG) are supported; external URLs
 *   and embedded SVG images are not.</li>
 * </ul>
 *
 * <p><strong>Gradient constraints:</strong>
 * <ul>
 *   <li>Linear gradients are supported, but only the first and last stops are used.</li>
 *   <li>Intermediate stops, offsets, and <code>stop-opacity</code> are ignored.</li>
 *   <li>Gradient direction is approximated to horizontal, vertical, or diagonal.</li>
 *   <li>Radial gradients are not supported.</li>
 * </ul>
 */
package net.sourceforge.plantuml.svg.parser;
