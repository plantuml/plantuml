package net.sourceforge.plantuml.approvaltesting;

import java.awt.image.BufferedImage;
import java.nio.file.Path;

public interface ApprovalTesting {

	interface FailCallback {
		void call(ApprovalTesting approvalTesting) throws Exception;
	}

	ApprovalTesting approve(BufferedImage value);

	ApprovalTesting approve(String value);

	ApprovalTesting fail(FailCallback callback);

	void rethrow(Throwable t);

	Path getDir();

	Path getPathForApproved(String suffix, String extensionWithDot);

	Path getPathForFailed(String suffix, String extensionWithDot);

	ApprovalTesting withExtension(String extensionWithDot);

	ApprovalTesting withMaxFailures(int maxFailures);

	ApprovalTesting withSuffix(String suffix);
}
