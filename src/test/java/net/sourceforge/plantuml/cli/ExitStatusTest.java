package net.sourceforge.plantuml.cli;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

// https://github.com/plantuml/plantuml/issues/258
// GraphViz/dot failing during rendering (a crash, a timeout, garbage output, ...) is
// reported through the very same status convention as PicoWebServer.httpReturnCode:
// 0 (never explicitly set) or 200 both mean "the image is fine", anything else is an
// error. isErrorStatus() is the single place that convention is decided, so that the
// file/-pipe exit-code wiring in Run/Pipe and the HTTP status in PicoWebServer agree.
class ExitStatusTest {

	@Test
	void zeroIsNotAnError() {
		assertFalse(ExitStatus.isErrorStatus(0));
	}

	@Test
	void twoHundredIsNotAnError() {
		assertFalse(ExitStatus.isErrorStatus(200));
	}

	@Test
	void fourHundredIsAnError() {
		// FileImageData.ERROR
		assertTrue(ExitStatus.isErrorStatus(400));
	}

	@Test
	void fiveHundredThreeIsAnError() {
		// FileImageData.CRASH, ImageDataSimple.error(...): what a GraphViz/dot crash
		// during rendering is tagged with.
		assertTrue(ExitStatus.isErrorStatus(503));
	}

	@Test
	void anyOtherNonZeroNonTwoHundredIsAnError() {
		assertTrue(ExitStatus.isErrorStatus(1));
		assertTrue(ExitStatus.isErrorStatus(404));
		assertTrue(ExitStatus.isErrorStatus(500));
	}

}
