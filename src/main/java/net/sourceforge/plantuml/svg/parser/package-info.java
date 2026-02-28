/**
 * SVG sprite parsing support.
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
