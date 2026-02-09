package net.sourceforge.plantuml.klimt.awt;

public class Color {

	public static final Color BLACK = new Color(0, 0, 0);
	public static final Color WHITE = new Color(255, 255, 255);
	public static final Color RED = new Color(255, 0, 0);
	public static final Color BLUE = new Color(0, 0, 255);

	private final int red;
	private final int green;
	private final int blue;
	private final int alpha;

	public Color(int r, int g, int b) {
		this(r, g, b, 255);
	}

	public Color(int r, int g, int b, int a) {
		this.red = r & 0xFF;
		this.green = g & 0xFF;
		this.blue = b & 0xFF;
		this.alpha = a & 0xFF;
	}

	public Color(int rgb) {
		this.alpha = (rgb >> 24) & 0xFF;
		this.red = (rgb >> 16) & 0xFF;
		this.green = (rgb >> 8) & 0xFF;
		this.blue = rgb & 0xFF;
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

	public java.awt.Color toAwtColor() {
		return new java.awt.Color(red, green, blue, alpha);
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
		Color other = (Color) obj;
		return red == other.red && green == other.green && blue == other.blue && alpha == other.alpha;
	}

	@Override
	public String toString() {
		return "[r=" + red + ",g=" + green + ",b=" + blue + ",a=" + alpha + "]";
	}

}
