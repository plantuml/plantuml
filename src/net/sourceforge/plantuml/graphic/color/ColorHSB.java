package net.sourceforge.plantuml.graphic.color;

import java.awt.Color;

/**
 * {@link Color} with hue, saturation and brightness.
 */
public class ColorHSB extends Color {
	final float hue;
	final float saturation;
	final float brightness;

	public ColorHSB(int rgba) {
		super(rgba, true);
		final float[] hsb = Color.RGBtoHSB(getRed(), getGreen(), getBlue(), null);
		hue = hsb[0];
		saturation = hsb[1];
		brightness = hsb[2];
	}

	public ColorHSB(Color other) {
		this(other.getRGB());
	}

	public float getHue() {
		return hue;
	}

	public float getSaturation() {
		return saturation;
	}

	public float getBrightness() {
		return brightness;
	}

	@Override
	public String toString() {
		return String.format("%s[a=%02X r=%02X g=%02X b=%02X / h=%f s=%f b=%f]",
				getClass().getSimpleName(), getAlpha(), getRed(), getGreen(), getBlue(), getHue(), getSaturation(), getBrightness()
		);
	}
}
