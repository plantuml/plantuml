package net.sourceforge.plantuml.approvaltesting;

import static org.junit.platform.commons.util.ReflectionUtils.HierarchyTraversalMode.TOP_DOWN;
import static org.junit.platform.commons.util.ReflectionUtils.findFields;
import static org.junit.platform.commons.util.ReflectionUtils.makeAccessible;

import java.lang.reflect.Field;
import java.util.function.Predicate;

import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

public class ApprovalTestingJUnitExtension implements BeforeEachCallback {

	private final ApprovalTestingImpl approvalTestingImpl = new ApprovalTestingImpl();

	/**
	 * Injects {@link ApprovalTesting} fields;
	 */
	@Override
	public void beforeEach(ExtensionContext context) {

		final Predicate<Field> filter = field -> field.getType().isAssignableFrom(ApprovalTesting.class);

		findFields(context.getRequiredTestClass(), filter, TOP_DOWN).forEach(field -> {
			try {
				makeAccessible(field)
						.set(context.getRequiredTestInstance(), approvalTestingImpl.forExtensionContext(context));
			} catch (Throwable t) {
				throw new RuntimeException(t);
			}
		});
	}
}
