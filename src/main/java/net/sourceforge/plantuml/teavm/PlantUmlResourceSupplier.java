package net.sourceforge.plantuml.teavm;

import org.teavm.classlib.ResourceSupplier;
import org.teavm.classlib.ResourceSupplierContext;

/**
 * Supplies resources to be bundled in TeaVM JavaScript output.
 * This is necessary for getResourceAsStream() to work in TeaVM.
 */
public class PlantUmlResourceSupplier implements ResourceSupplier {

	@Override
	public String[] supplyResources(ResourceSupplierContext context) {
		// List all resources that should be bundled in the JavaScript
		return new String[] {
			"skin/plantuml.skin",
			// Add more resources here as needed
		};
	}
}
