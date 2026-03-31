package test.vega;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Path;

public class VegaCheckerGraphml extends VegaChecker {

	@Override
	public Path checkOutput(VegaInputFile data, ByteArrayOutputStream baos, String suffix, int nbImages, int imageIndex)
			throws IOException {
		return checkTextOutput(data, baos, suffix, ".graphml", "GRAPHML");

	}

}
