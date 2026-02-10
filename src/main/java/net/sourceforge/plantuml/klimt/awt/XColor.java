package net.sourceforge.plantuml.klimt.awt;

import java.awt.Color;

public class XColor {

	public static final XColor BLACK = new XColor(0, 0, 0);
	public static final XColor WHITE = new XColor(255, 255, 255);
	public static final XColor RED = new XColor(255, 0, 0);
	public static final XColor BLUE = new XColor(0, 0, 255);

	private final int red;
	private final int green;
	private final int blue;
	private final int alpha;

	public XColor(int r, int g, int b) {
		this(r, g, b, 255);
	}

	public XColor(int r, int g, int b, int a) {
		this.red = r & 0xFF;
		this.green = g & 0xFF;
		this.blue = b & 0xFF;
		this.alpha = a & 0xFF;
	}

	public static XColor from(int rgb) {
		final int alpha = 255;
		final int red = (rgb >> 16) & 0xFF;
		final int green = (rgb >> 8) & 0xFF;
		final int blue = rgb & 0xFF;
		return new XColor(red, green, blue, alpha);
	}

	public static XColor fromFloat(float r, float g, float b, float a) {
		if (!Float.isFinite(r) || !Float.isFinite(g) || !Float.isFinite(b) || !Float.isFinite(a) || r < 0.0f || r > 1.0f
				|| g < 0.0f || g > 1.0f || b < 0.0f || b > 1.0f || a < 0.0f || a > 1.0f) {
			throw new IllegalArgumentException();
		}
		final int red = Math.round(r * 255.0f) & 0xFF;
		final int green = Math.round(g * 255.0f) & 0xFF;
		final int blue = Math.round(b * 255.0f) & 0xFF;
		final int alpha = Math.round(a * 255.0f) & 0xFF;
		return new XColor(red, green, blue, alpha);
	}

	public int getAlpha() {
		return alpha;
	}

	public int getRed() {
		return red;
	}

	public int getBlue() {
		return blue;
	}

	public int getGreen() {
		return green;
	}

	public int getRGB() {
		return ((alpha & 0xFF) << 24) | ((red & 0xFF) << 16) | ((green & 0xFF) << 8) | (blue & 0xFF);
	}

	public float[] getRGBColorComponents(Object object) {
		float[] result;
		if (object == null)
			result = new float[3];
		else
			result = (float[]) object;

		result[0] = red / 255.0f;
		result[1] = green / 255.0f;
		result[2] = blue / 255.0f;
		return result;
	}

	public Color toAwtColor() {
		return new Color(red, green, blue, alpha);
	}

	@Override
	public int hashCode() {
		return getRGB();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		XColor other = (XColor) obj;
		return red == other.red && green == other.green && blue == other.blue && alpha == other.alpha;
	}

	@Override
	public String toString() {
		return "[r=" + red + ",g=" + green + ",b=" + blue + ",a=" + alpha + "]";
	}

	public String toSvg() {
		if (alpha == 0)
			return "#00000000";

		if (alpha == 255)
			return String.format("#%02X%02X%02X", red, green, blue);

		return String.format("#%02X%02X%02X%02X", red, green, blue, alpha);
	}

	public static String toHexRGBColor(int rgb) {
		return String.format("#%06X", rgb & 0xFFFFFF);
	}

}
