package net.sourceforge.plantuml.test;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.util.stream.Stream;

public class PathUtils {

	public static Stream<Path> glob(Path dir, String globPattern) throws IOException {
		final PathMatcher matcher = FileSystems.getDefault().getPathMatcher("glob:" + globPattern);
		return Files
				.walk(dir)
				.filter(matcher::matches);
	}
}
