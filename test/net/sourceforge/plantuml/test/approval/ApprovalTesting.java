package net.sourceforge.plantuml.test.approval;

import java.awt.image.BufferedImage;

interface ApprovalTesting {
	
	ApprovalTesting approveImage(BufferedImage value);

	ApprovalTesting approveString(String value);

	ApprovalTesting withExtension(String extensionWithDot);

	ApprovalTesting withSuffix(String suffix);
}
