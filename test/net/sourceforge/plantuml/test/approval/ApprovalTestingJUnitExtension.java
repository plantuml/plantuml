package net.sourceforge.plantuml.test.approval;

import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.Extension;
import org.junit.jupiter.api.extension.ExtensionContext;

public class ApprovalTestingJUnitExtension extends ApprovalTestingDsl implements Extension, BeforeEachCallback {

	@Override
	public void beforeEach(ExtensionContext context) {
		configureForTest(context.getRequiredTestClass().getName(), context.getDisplayName());
	}
}
