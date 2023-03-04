package net.sourceforge.plantuml.quantization;

import java.util.Set;

public final class FloydSteinbergDitherer implements Ditherer {
	public static final FloydSteinbergDitherer INSTANCE = new FloydSteinbergDitherer();

	private static final ErrorComponent[] ERROR_DISTRIBUTION = { new ErrorComponent(1, 0, 7.0 / 16.0),
			new ErrorComponent(-1, 1, 3.0 / 16.0), new ErrorComponent(0, 1, 5.0 / 16.0),
			new ErrorComponent(1, 1, 1.0 / 16.0) };

	private FloydSteinbergDitherer() {
	}

	@Override
	public QImage dither(QImage image, Set<QColor> newColors) {
		final int width = image.getWidth();
		final int height = image.getHeight();
		QColor[][] colors = new QColor[height][width];
		for (int y = 0; y < height; ++y)
			for (int x = 0; x < width; ++x)
				colors[y][x] = image.getColor(x, y);

		for (int y = 0; y < height; ++y)
			for (int x = 0; x < width; ++x) {
				final QColor originalColor = colors[y][x];
				final QColor replacementColor = originalColor.getNearestColor(newColors);
				colors[y][x] = replacementColor;
				final QColor error = originalColor.minus(replacementColor);

				for (ErrorComponent component : ERROR_DISTRIBUTION) {
					int siblingX = x + component.deltaX, siblingY = y + component.deltaY;
					if (siblingX >= 0 && siblingY >= 0 && siblingX < width && siblingY < height) {
						QColor errorComponent = error.scaled(component.errorFraction);
						colors[siblingY][siblingX] = colors[siblingY][siblingX].plus(errorComponent);
					}
				}
			}

		return QImage.fromColors(colors);
	}

	private static final class ErrorComponent {
		final int deltaX, deltaY;
		final double errorFraction;

		ErrorComponent(int deltaX, int deltaY, double errorFraction) {
			this.deltaX = deltaX;
			this.deltaY = deltaY;
			this.errorFraction = errorFraction;
		}
	}
}
