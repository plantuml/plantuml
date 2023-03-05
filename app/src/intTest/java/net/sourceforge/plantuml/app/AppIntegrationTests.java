package net.sourceforge.plantuml.app;

import static java.nio.file.Files.createDirectories;
import static java.nio.file.Files.createTempDirectory;
import static java.util.Objects.requireNonNull;

import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import javax.imageio.ImageIO;

import org.assertj.core.api.Assertions;
import org.assertj.core.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.github.romankh3.image.comparison.ImageComparison;
import com.github.romankh3.image.comparison.model.ImageComparisonResult;
import com.github.romankh3.image.comparison.model.ImageComparisonState;
import com.github.stefanbirkner.systemlambda.SystemLambda;
import net.sourceforge.plantuml.Run;

class AppIntegrationTests {

	public static final String INT_TEST_RESOURCES_DIRECTORY = "intTest.resources.directory";
	public static final String INT_TEST_OUTPUT_DIRECTORY = "intTest.output.directory";

	private Path resourceDir;
	private Path outputDir;

	@BeforeEach
	public void setupOutputDirectory() throws IOException {
		this.resourceDir = Path.of(requireNonNull(System.getProperty(INT_TEST_RESOURCES_DIRECTORY)));
		Path rootOutputDir = createDirectories(Path.of(requireNonNull(System.getProperty(INT_TEST_OUTPUT_DIRECTORY))));
		this.outputDir = createTempDirectory(rootOutputDir, this.getClass().getSimpleName());
	}

	@Test
	void testRunCli() throws Exception {
		//given
		Path sampleFile = this.resourceDir.resolve("sample.puml").toAbsolutePath();
		Path expectedFile = this.resourceDir.resolve("sample.png").toAbsolutePath();
		Path outputDir = this.outputDir.toAbsolutePath();
		Path actualFile = this.outputDir.resolve("sample.png");
		//when
		int statusCode = SystemLambda.catchSystemExit(() -> {
			Run.main((String[])Arrays.array(new String[]{sampleFile.toString(), "-output", outputDir.toString()}));
		});
		//then
		assertImageContentMach(expectedFile, actualFile);
		Assertions.assertThat(statusCode).isEqualTo(0);
		//delete on success only
		Files.delete(actualFile);
		Files.delete(outputDir);
	}

	private static void assertImageContentMach(Path expectedImagePath, Path actualImagePath) throws IOException {
		BufferedImage expectedImage = ImageIO.read(new FileInputStream(expectedImagePath.toFile()));
		BufferedImage actualImage = ImageIO.read(new FileInputStream(actualImagePath.toFile()));
		assertImageContentMach(expectedImage, actualImage);
	}

	private static void assertImageContentMach(BufferedImage expectedImage, BufferedImage actualImage) {
		ImageComparisonResult imageComparisonResult = (new ImageComparison(expectedImage, actualImage)).compareImages();
		Assertions.assertThat(imageComparisonResult.getImageComparisonState()).isEqualTo(ImageComparisonState.MATCH);
	}
}
