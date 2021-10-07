package net.sourceforge.plantuml.test;

import static java.lang.Math.abs;

import java.util.Comparator;

import net.sourceforge.plantuml.graphic.color.ColorHSB;

public class ColorComparators {

	public static Comparator<ColorHSB> COMPARE_PIXEL_EXACT = new Comparator<ColorHSB>() {
		@Override
		public int compare(ColorHSB expected, ColorHSB actual) {
			return expected.getRGB() - actual.getRGB();
		}

		@Override
		public String toString() {
			return "COMPARE_PIXEL_EXACT";
		}
	};

	public static Comparator<ColorHSB> comparePixelWithSBTolerance(float sTolerance, float bTolerance) {
		return new Comparator<ColorHSB>() {
			@Override
			public int compare(ColorHSB expected, ColorHSB actual) {
				return (
						abs(expected.getAlpha() - actual.getAlpha()) > 0
								|| abs(expected.getHue() - actual.getHue()) > 0
								|| abs(expected.getSaturation() - actual.getSaturation()) > sTolerance
								|| abs(expected.getBrightness() - actual.getBrightness()) > bTolerance
				) ? 1 : 0;
			}

			@Override
			public String toString() {
				return String.format("[sTolerance=%f, bTolerance=%f]", sTolerance, bTolerance);
			}
		};
	}
}
