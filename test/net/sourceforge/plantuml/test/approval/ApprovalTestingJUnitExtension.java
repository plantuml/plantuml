package net.sourceforge.plantuml.test.approval;

import static net.sourceforge.plantuml.StringUtils.substringBefore;

import java.nio.file.Paths;

import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.Extension;
import org.junit.jupiter.api.extension.ExtensionContext;

public class ApprovalTestingJUnitExtension extends ApprovalTestingDsl implements Extension, BeforeEachCallback {

	@Override
	public void beforeEach(ExtensionContext context) {
		final Class<?> klass = context.getRequiredTestClass();
		configureForTest(
				klass.getSimpleName() + "." + simplifyTestName(context.getDisplayName()),
				Paths.get("test", klass.getPackage().getName().split("\\."))
		);
	}

	private static String simplifyTestName(String name) {
		return substringBefore(name, '(')
				.replaceAll("^[ \\p{Punct}]+", "")
				.replaceAll("[ \\p{Punct}]+", "_");
	}
}
