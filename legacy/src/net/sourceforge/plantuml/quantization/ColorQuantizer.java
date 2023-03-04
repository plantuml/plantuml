package net.sourceforge.plantuml.quantization;

import java.util.Set;

public interface ColorQuantizer {
	/**
	 * Quantize the given set of colors, returning a set no larger than
	 * {@code maxColors}.
	 *
	 * <p>
	 * The intent is to pick a set of colors which are representative of the
	 * original color set, but no specific guarantees are made.
	 *
	 * @param originalColors the colors in the original image
	 * @param maxColorCount  the maximum number of colors to allow
	 * @return a quantized collection of colors no larger than {@code maxColors}
	 */
	public Set<QColor> quantize(Multiset<QColor> originalColors, int maxColorCount);
}
