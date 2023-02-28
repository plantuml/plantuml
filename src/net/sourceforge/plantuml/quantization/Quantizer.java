package net.sourceforge.plantuml.quantization;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Set;

import net.sourceforge.plantuml.klimt.color.ColorMapper;

public final class Quantizer {
	// ::remove folder when __CORE__
	private static final int MAX_COLOR_COUNT = 256;

	private static QImage quantizeNow(QImage image) throws IOException {

		Multiset<QColor> originalColors = image.getColors();
		Set<QColor> distinctColors = originalColors.getDistinctElements();
		if (distinctColors.size() > MAX_COLOR_COUNT) {
			// distinctColors = KMeansQuantizer.INSTANCE.quantize(originalColors,
			// MAX_COLOR_COUNT);
			distinctColors = MedianCutQuantizer.INSTANCE.quantize(originalColors, MAX_COLOR_COUNT);
			image = FloydSteinbergDitherer.INSTANCE.dither(image, distinctColors);
		}
		return image;
	}

	public static BufferedImage quantizeNow(ColorMapper mapper, BufferedImage orig) throws IOException {
		final QImage raw = QImage.fromBufferedImage(mapper, orig);
		final QImage result = quantizeNow(raw);

		if (orig.getType() == BufferedImage.TYPE_INT_RGB)
			return result.toBufferedImage();
		else if (orig.getType() == BufferedImage.TYPE_INT_ARGB)
			return result.toBufferedImageKeepTransparency(orig);
		else
			throw new IllegalArgumentException();

	}
}
