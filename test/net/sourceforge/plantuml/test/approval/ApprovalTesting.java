package net.sourceforge.plantuml.test.approval;

import java.awt.image.BufferedImage;

public interface ApprovalTesting {
	
	ApprovalTesting approve(BufferedImage value);

	ApprovalTesting approve(String value);

	ApprovalTesting withExtension(String extensionWithDot);

	ApprovalTesting withSuffix(String suffix);
}
