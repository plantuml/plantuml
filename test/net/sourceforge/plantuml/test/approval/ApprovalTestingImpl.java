package net.sourceforge.plantuml.test.approval;

import static net.sourceforge.plantuml.test.approval.ApprovalTestingStrategy.BUFFERED_IMAGE;
import static net.sourceforge.plantuml.test.approval.ApprovalTestingStrategy.STRING;

import java.awt.image.BufferedImage;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.extension.ExtensionContext;


public class ApprovalTestingImpl implements ApprovalTesting {

	//
	// Implement ApprovalTesting
	//

	public ApprovalTestingImpl withExtension(String extensionWithDot) {
		final ApprovalTestingImpl copy = new ApprovalTestingImpl(this);
		copy.extensionWithDot = extensionWithDot;
		return copy;
	}

	public ApprovalTestingImpl withSuffix(String suffix) {
		final ApprovalTestingImpl copy = new ApprovalTestingImpl(this);
		copy.suffix = suffix;
		return copy;
	}

	public ApprovalTestingImpl approveImage(BufferedImage value) {
		BUFFERED_IMAGE.approve(this, value);
		return this;
	}

	public ApprovalTestingImpl approveString(String value) {
		STRING.approve(this, value);
		return this;
	}

	//
	// Internals
	//

	private final Set<String> approvedFilesUsed;
	private final String className;
	private final String displayName;
	private String extensionWithDot;
	private final String methodName;
	private String suffix;

	ApprovalTestingImpl(ApprovalTestingImpl other) {
		this.approvedFilesUsed = other.approvedFilesUsed;
		this.className = other.className;
		this.displayName = other.displayName;
		this.extensionWithDot = other.extensionWithDot;
		this.methodName = other.methodName;
		this.suffix = other.suffix;
	}

	ApprovalTestingImpl(ExtensionContext context) {
		this.approvedFilesUsed = new HashSet<>();
		this.className = context.getRequiredTestClass().getName();
		this.displayName = context.getDisplayName();
		this.extensionWithDot = null;
		this.methodName = context.getRequiredTestMethod().getName();
		this.suffix = "";
	}

	Set<String> getApprovedFilesUsed() {
		return approvedFilesUsed;
	}

	String getClassName() {
		return className;
	}

	String getDisplayName() {
		return displayName;
	}

	Optional<String> getExtensionWithDot() {
		return Optional.ofNullable(extensionWithDot);
	}

	String getSuffix() {
		return suffix;
	}

	String getMethodName() {
		return methodName;
	}
}
