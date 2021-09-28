package net.sourceforge.plantuml.test.approval;

import static net.sourceforge.plantuml.test.approval.ApprovalTestingImpl.BUFFERED_IMAGE;
import static net.sourceforge.plantuml.test.approval.ApprovalTestingImpl.STRING;

import java.awt.image.BufferedImage;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.extension.ExtensionContext;


public class ApprovalTestingDsl {

	//
	// Public
	//

	public ApprovalTestingDsl withExtension(String extensionWithDot) {
		ApprovalTestingDsl copy = new ApprovalTestingDsl(this);
		copy.extensionWithDot = extensionWithDot;
		return copy;
	}

	public ApprovalTestingDsl withSuffix(String suffix) {
		ApprovalTestingDsl copy = new ApprovalTestingDsl(this);
		copy.suffix = suffix;
		return copy;
	}

	public void approveImage(BufferedImage value) {
		BUFFERED_IMAGE.approve(this, value);
	}

	public void approveString(String value) {
		STRING.approve(this, value);
	}

	//
	// Internals
	//

	private final Set<String> approvedFilesUsed;
	private String className;
	private String displayName;
	private String extensionWithDot;
	private String methodName;
	private String suffix;

	ApprovalTestingDsl() {
		approvedFilesUsed = new HashSet<>();
		suffix = "";
	}

	ApprovalTestingDsl(ApprovalTestingDsl other) {
		this.approvedFilesUsed = other.approvedFilesUsed;
		this.className = other.className;
		this.displayName = other.displayName;
		this.extensionWithDot = other.extensionWithDot;
		this.suffix = other.suffix;
		this.methodName = other.methodName;
	}

	void configureForTest(ExtensionContext context) {
		this.className = context.getRequiredTestClass().getName();
		this.displayName = context.getDisplayName();
		this.methodName = context.getRequiredTestMethod().getName();
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
