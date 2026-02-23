package net.sourceforge.plantuml.klimt.awt;

import java.awt.image.BufferedImage;

import net.sourceforge.plantuml.teavm.TeaVM;

public class PortableImageFactory {

	public static PortableImage build(int width, int height, int imageType) {
		if (!TeaVM.isTeaVM())
			return new PortableImageAwt(width, height, imageType);
		else
			// ::revert when JAVA8
			return new PortableImageTeaVM(width, height, imageType);
		// return new PortableImageAwt(width, height, imageType);
		// ::done
	}

	public static PortableImage build(BufferedImage image) {
		if (!TeaVM.isTeaVM())
			return new PortableImageAwt(image);
		else
			throw new UnsupportedOperationException("TEAVM92734");
	}

}
