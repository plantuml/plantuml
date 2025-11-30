/**
 * Provides the core domain model for the New Gantt Model (NGM).
 *
 * <p>This package contains the model layer (in the MVC sense) for Gantt-style
 * scheduling, reworked to use the {@code java.time} API and to better handle
 * workload concepts (load, workload, duration).</p>
 *
 * <p>The NGM model is intentionally kept independent from the rest of PlantUML:
 * it must not depend on rendering, diagram classes, or the legacy Gantt
 * implementation. The goal is to have a clean, reusable scheduling core that
 * can later be integrated or tested in isolation.</p>
 */
package net.sourceforge.plantuml.project.ngm;
