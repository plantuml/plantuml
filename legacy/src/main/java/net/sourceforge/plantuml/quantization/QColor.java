package net.sourceforge.plantuml.quantization;

import java.util.Collection;

import net.sourceforge.plantuml.klimt.color.ColorMapper;

/**
 * An RGB representation of a color, which stores each component as a double in
 * the range [0, 1]. Values outside of [0, 1] are permitted though, as this is
 * convenient e.g. for representing color deltas.
 */
public final class QColor {
	public static final QColor BLACK = new QColor(0, 0, 0);
	public static final QColor WHITE = new QColor(1, 1, 1);
	public static final QColor RED = new QColor(1, 0, 0);
	public static final QColor GREEN = new QColor(0, 1, 0);
	public static final QColor BLUE = new QColor(0, 0, 1);

	private final double red;
	private final double green;
	private final double blue;

	public QColor(double red, double green, double blue) {
		this.red = red;
		this.green = green;
		this.blue = blue;
	}

	public static QColor fromArgbInt(ColorMapper mapper, int rgb) {
		final double alpha = (rgb >>> 24 & 0xFF) / 255.0;
		double red = (rgb >>> 16 & 0xFF) / 255.0;
		double green = (rgb >>> 8 & 0xFF) / 255.0;
		double blue = (rgb & 0xFF) / 255.0;

		if (mapper == ColorMapper.DARK_MODE) {
			red = alpha * red;
			green = alpha * green;
			blue = alpha * blue;
		} else {
			red = 1 - alpha * (1 - red);
			green = 1 - alpha * (1 - green);
			blue = 1 - alpha * (1 - blue);
		}
		return new QColor(red, green, blue);
	}

	public static QColor fromRgbInt(int rgb) {
		final double red = (rgb >>> 16 & 0xFF) / 255.0;
		final double green = (rgb >>> 8 & 0xFF) / 255.0;
		final double blue = (rgb & 0xFF) / 255.0;
		return new QColor(red, green, blue);
	}

	public static QColor getCentroid(Multiset<QColor> colors) {
		QColor sum = QColor.BLACK;
		for (QColor color : colors.getDistinctElements()) {
			int weight = colors.count(color);
			sum = sum.plus(color.scaled(weight));
		}
		return sum.scaled(1.0 / colors.size());
	}

	public double getComponent(int index) {
		switch (index) {
		case 0:
			return red;
		case 1:
			return green;
		case 2:
			return blue;
		default:
			throw new IllegalArgumentException("Unexpected component index: " + index);
		}
	}

	public QColor scaled(double s) {
		return new QColor(s * red, s * green, s * blue);
	}

	public QColor plus(QColor that) {
		return new QColor(this.red + that.red, this.green + that.green, this.blue + that.blue);
	}

	public QColor minus(QColor that) {
		return new QColor(this.red - that.red, this.green - that.green, this.blue - that.blue);
	}

	public double getEuclideanDistanceTo(QColor that) {
		final QColor d = this.minus(that);
		final double sumOfSquares = d.red * d.red + d.green * d.green + d.blue * d.blue;
		return Math.sqrt(sumOfSquares);
	}

	/**
	 * Find this color's nearest neighbor, based on Euclidean distance, among some
	 * set of colors.
	 */
	public QColor getNearestColor(Collection<QColor> colors) {
		QColor nearestCentroid = null;
		double nearestCentroidDistance = Double.POSITIVE_INFINITY;
		for (QColor color : colors) {
			final double distance = getEuclideanDistanceTo(color);
			if (distance < nearestCentroidDistance) {
				nearestCentroid = color;
				nearestCentroidDistance = distance;
			}
		}
		return nearestCentroid;
	}

	public int getRgbInt() {
		final int redComponent = (int) (red * 255);
		final int greenComponent = (int) (green * 255);
		final int blueComponent = (int) (blue * 255);
		return redComponent << 16 | greenComponent << 8 | blueComponent;
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof QColor))
			return false;
		final QColor that = (QColor) o;
		return this.red == that.red && this.green == that.green && this.blue == that.blue;
	}

	@Override
	public int hashCode() {
		int result;
		long temp;
		temp = Double.doubleToLongBits(red);
		result = (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(green);
		result = 31 * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(blue);
		result = 31 * result + (int) (temp ^ (temp >>> 32));
		return result;
	}

	@Override
	public String toString() {
		return String.format("Color[%f, %f, %f]", red, green, blue);
	}
}
