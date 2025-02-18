package nonreg.svg;

import net.sourceforge.plantuml.TitledDiagram;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(GraphvizRequiredTestFilter.class)
public class SvekSvgTest extends SvgTest {

	private boolean originalForceSmetanaSetting;

	@BeforeEach
	void setup() {
		// Some tests (e.g. `nonreg/simple/BasicTest`) set `FORCE_SMETANA = true`,
		// which prevents some Svek functionality from being exercised.
		originalForceSmetanaSetting = TitledDiagram.FORCE_SMETANA;
		TitledDiagram.FORCE_SMETANA = false;
	}

	@AfterEach
	void tearDown() {
		TitledDiagram.FORCE_SMETANA = originalForceSmetanaSetting;
	}
}