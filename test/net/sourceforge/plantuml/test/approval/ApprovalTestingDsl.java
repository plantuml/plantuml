package net.sourceforge.plantuml.test.approval;

import static net.sourceforge.plantuml.test.approval.ApprovalTestingImpl.BUFFERED_IMAGE;
import static net.sourceforge.plantuml.test.approval.ApprovalTestingImpl.STRING;

import java.awt.image.BufferedImage;
import java.nio.file.Path;
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
	private String baseName;
	private Path dir;
	private String suffix;

	ApprovalTestingDsl() {
		approvedFilesUsed = new HashSet<>();
		suffix = "";
	}

	ApprovalTestingDsl(ApprovalTestingDsl other) {
		this.approvedFilesUsed = other.approvedFilesUsed;
		this.baseName = other.baseName;
		this.dir = other.dir;
		this.suffix = other.suffix;
	}

	void configureForTest(String baseName, Path dir) {
		this.baseName = baseName;
		this.dir = dir;
	}

	Set<String> getApprovedFilesUsed() {
		return approvedFilesUsed;
	}

	String getBaseName() {
		return baseName;
	}

	Path getDir() {
		return dir;
	}

	String getSuffix() {
		return suffix;
	}
}
