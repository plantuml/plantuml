package net.sourceforge.plantuml.utils;

import java.io.IOException;
import java.util.Iterator;

import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

import net.sourceforge.plantuml.security.ImageIO;

public class ImageIOUtils {

	public static ImageReader createImageReader(ImageInputStream iis) throws IOException {
		final Iterator<ImageReader> readers = ImageIO.getImageReaders(iis);

		if (!readers.hasNext()) {
			throw new IllegalStateException("No ImageReader");
		}
		
		final ImageReader reader = readers.next();
		reader.setInput(iis, true);
		return reader;
	}
}
