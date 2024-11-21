/**
 * Provides classes for creating Nassi-Shneiderman diagrams (also known as structograms).
 * 
 * A Nassi-Shneiderman diagram is a structured flowchart that shows control flow using
 * nested boxes with standardized shapes for different control structures:
 * 
 * <ul>
 * <li>Process blocks: Simple rectangles</li>
 * <li>If-then-else: Diamond header with vertical split for branches</li>
 * <li>While loops: Trapezoid header with body below</li>
 * <li>Input/Output: Parallelograms (slanted left for input, right for output)</li>
 * <li>Function calls: Double-bordered rectangles</li>
 * <li>Break statements: Rectangles with diagonal corner marks</li>
 * <li>Connectors: Concentric circles with labels</li>
 * </ul>
 * 
 * The diagram elements are laid out vertically and maintain proper nesting of
 * control structures through parent-child relationships.
 * 
 * Key classes:
 * <ul>
 * <li>{@link NassiDiagram}: Main diagram class</li>
 * <li>{@link NassiElement}: Base class for all diagram elements</li>
 * <li>{@link NassiDrawingUtil}: Utility class for common drawing operations</li>
 * </ul>
 * 
 * @see <a href="https://en.wikipedia.org/wiki/Nassi–Shneiderman_diagram">Wikipedia: Nassi-Shneiderman diagram</a>
 */
package net.sourceforge.plantuml.nassidiagram;

import net.sourceforge.plantuml.nassidiagram.util.NassiDrawingUtil; 