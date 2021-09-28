package net.sourceforge.plantuml.test.approval;

import static net.sourceforge.plantuml.test.approval.ApprovalTestingImpl.BUFFERED_IMAGE;
import static net.sourceforge.plantuml.test.approval.ApprovalTestingImpl.STRING;

import java.awt.image.BufferedImage;
import java.util.HashSet;
import java.util.Set;


public class ApprovalTestingDsl {

	//
	// Public
	//

	public ApprovalTestingDsl withSuffix(String suffix) {
		ApprovalTestingDsl copy = new ApprovalTestingDsl(this);
		copy.suffix = suffix;
		return copy;
	}

	public void approve(BufferedImage value) {
		BUFFERED_IMAGE.approve(this, value);
	}

	public void approve(String value) {
		STRING.approve(this, value);
	}

	//
	// Internals
	//

	private final Set<String> approvedFilesUsed;
	private String className;
	private String suffix;
	private String testName;

	ApprovalTestingDsl() {
		approvedFilesUsed = new HashSet<>();
		suffix = "";
	}

	ApprovalTestingDsl(ApprovalTestingDsl other) {
		this.approvedFilesUsed = other.approvedFilesUsed;
		this.className = other.className;
		this.suffix = other.suffix;
		this.testName = other.testName;
	}

	void configureForTest(String className, String methodName) {
		this.className = className;
		this.testName = methodName;
	}

	Set<String> getApprovedFilesUsed() {
		return approvedFilesUsed;
	}

	public String getClassName() {
		return className;
	}

	String getSuffix() {
		return suffix;
	}

	public String getTestName() {
		return testName;
	}
}
