package net.sourceforge.plantuml.test.approval;

import java.awt.image.BufferedImage;
import java.nio.file.Path;

public interface ApprovalTesting {
	
	ApprovalTesting approve(BufferedImage value);

	ApprovalTesting approve(String value);
	
	Path getDir();

	ApprovalTesting withExtension(String extensionWithDot);

	ApprovalTesting withSuffix(String suffix);
}
