package net.sourceforge.plantuml.math;

import net.sourceforge.plantuml.klimt.color.HColor;
import net.sourceforge.plantuml.klimt.shape.TextBlock;

/**
 * A diagram that is one formula (@startmath, @startlatex). It is exported by
 * {@code exportDiagram}, as every {@code DirectOsDiagram}; this gives the
 * drawing itself to the renderers that have their own output, such as the
 * browser (SVG built by {@code PlantUMLBrowser}).
 */
public interface FormulaDiagram {

	/** The formula, in its color. */
	TextBlock getFormulaBlock();

	/** The color behind the formula. */
	HColor getFormulaBackground();

	double getFormulaScale();

}
