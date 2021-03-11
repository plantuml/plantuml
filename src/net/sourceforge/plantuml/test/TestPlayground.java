package net.sourceforge.plantuml.test;

import static net.sourceforge.plantuml.FileFormat.PNG;
import static net.sourceforge.plantuml.security.SecurityProfile.UNSECURE;
import static net.sourceforge.plantuml.security.SecurityUtils.getSecurityProfile;
import static net.sourceforge.plantuml.test.TestGroup.ALL;
import static net.sourceforge.plantuml.test.TestGroup.ALL_ALLOWING_SKINPARAM;
import static net.sourceforge.plantuml.test.TestUtils.log;

/**
 * A space for developers to experiment with different config settings across different diagram types.
 */
public class TestPlayground {
	public static void main(String[] args) throws Exception {

		System.setProperty("java.awt.headless", "true");

		if (getSecurityProfile() != UNSECURE) {
			log("*** BEWARE some diagram types need env var PLANTUML_SECURITY_PROFILE=UNSECURE ***");
		}

		// For every diagram type
		ALL
				.render(PNG, "no_style")      // Make PNG files in the "testoutput/no_style" dir
				.gridHorizontal("no_style")
				.markdown("no_style")         // Make "testoutput/no_style.md" which shows all the PNGs
				.png("no_style");             // Make "testoutput/no_style.png" which also shows all the PNGs


		// For all diagrams that allow "skinparam" commands
		ALL_ALLOWING_SKINPARAM

				// Make PNG files in the "testoutput/styled" dir
				.render(PNG, "styled",
						"skinparam DiagramBorderColor red",
						"skinparam BackgroundColor blue"
				)

				// And "testoutput/comparison.png" which has a grid comparing PNGs from the no_style & styled dirs.
				// (Currently it tells us that DiagramBorderColor & BackgroundColor work for most diagrams but not all)
				.pngHorizontal("comparison", "no_style", "styled");


		// TestGroup provides simple set operations e.g.
		log("These do not support skinparam: %s", ALL.subtract(ALL_ALLOWING_SKINPARAM));
	}
}
