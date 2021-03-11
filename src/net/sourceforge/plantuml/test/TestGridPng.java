package net.sourceforge.plantuml.test;

import net.sourceforge.plantuml.security.SFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import static java.lang.Math.ceil;
import static java.lang.System.arraycopy;
import static net.sourceforge.plantuml.test.TestUtils.log;
import static net.sourceforge.plantuml.test.TestUtils.logException;

public class TestGridPng {

	private static final int CELL_MARGIN = 3;
	private static final float FONT_SIZE = 20;

	public static void writeGridAsPng(TestGrid grid, SFile outputFile) throws IOException {
		log("--- Rendering %s ---", outputFile.getPath());
		outputFile.getParentFile().mkdirs();
		final BufferedImage image = step1_prepareCells(grid);
		ImageIO.write(image, "png", outputFile.conv());
	}

	private static BufferedImage step1_prepareCells(TestGrid grid) {
		final Object[][] cells = new Object[grid.rowNames.length + 1][grid.colNames.length + 1];

		for (int row = 0; row < grid.rowNames.length; row++) {
			cells[row + 1][0] = grid.rowNames[row];
		}

		arraycopy(grid.colNames, 0, cells[0], 1, grid.colNames.length);

		for (int row = 0; row < grid.rowNames.length; row++) {
			arraycopy(grid.files[row], 0, cells[row + 1], 1, grid.colNames.length);
		}

		return step2_calculateRowAndColumnSizes(cells);
	}

	private static BufferedImage step2_calculateRowAndColumnSizes(final Object[][] cells) {
		final int[] columnWidths = new int[cells[0].length];
		final int[] rowHeights = new int[cells.length];

		final Graphics2D g2d = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB).createGraphics();
		g2d.setFont(g2d.getFont().deriveFont(FONT_SIZE));
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		final FontMetrics fontMetrics = g2d.getFontMetrics();

		for (int row = 0; row < rowHeights.length; row++) {
			for (int col = 0; col < columnWidths.length; col++) {
				final Object value = cells[row][col];
				final int width;
				final int height;

				if (value instanceof String) {
					Rectangle2D size = fontMetrics.getStringBounds((String) value, g2d);
					width = (int) ceil(size.getWidth());
					height = (int) ceil(size.getHeight());
				} else if (value instanceof File) {
					// Keeping all images in memory might cause a problem so they are re-read later when drawing the grid
					final BufferedImage image = readImage((File) value);
					if (image == null) continue;
					width = image.getWidth();
					height = image.getHeight();
				} else {
					continue;
				}

				if (width > columnWidths[col]) {
					columnWidths[col] = width;
				}
				if (height > rowHeights[row]) {
					rowHeights[row] = height;
				}
			}
		}

		return step3_drawCells(cells, columnWidths, rowHeights);
	}

	private static BufferedImage step3_drawCells(Object[][] cells, int[] columnWidths, int[] rowHeights) {
		final int width = sum(columnWidths)
				+ (CELL_MARGIN * (columnWidths.length + 1)) // gap between frames
				+ (2 * columnWidths.length); // frames

		final int height = sum(rowHeights)
				+ (CELL_MARGIN * (rowHeights.length + 1)) // gap between frames
				+ (2 * rowHeights.length); // frames

		final BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

		final Graphics2D g2d = image.createGraphics();
		g2d.setColor(Color.BLACK);
		g2d.setFont(g2d.getFont().deriveFont(FONT_SIZE));
		g2d.setStroke(new BasicStroke());

		int y = CELL_MARGIN;
		for (int row = 0; row < rowHeights.length; row++) {
			int x = CELL_MARGIN;
			for (int col = 0; col < columnWidths.length; col++) {
				drawCell(g2d, cells[row][col], x, y);
				x += columnWidths[col] + CELL_MARGIN + 2;
			}
			y += rowHeights[row] + CELL_MARGIN + 2;
		}

		return image;
	}

	private static void drawCell(Graphics2D g2d, Object value, int x, int y) {
		if (value == null) return;

		if (value instanceof String) {
			drawString(g2d, (String) value, x, y);
		} else if (value instanceof File) {
			drawImage(g2d, (File) value, y, x);
		} else {
			log("Dont know how to draw %s", value.getClass());
		}
	}

	private static void drawImage(Graphics2D g2d, File value, int y, int x) {
		final BufferedImage image = readImage(value);
		if (image == null) return;
		g2d.drawRect(x, y, image.getWidth() + 1, image.getHeight() + 1);
		g2d.drawImage(image, x + 1, y + 1, null);
	}

	private static void drawString(Graphics2D g2d, String string, int x, int y) {
		final Color oldColor = g2d.getColor();
		final Object oldAntialiasing = g2d.getRenderingHint(RenderingHints.KEY_ANTIALIASING);

		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		final FontMetrics fontMetrics = g2d.getFontMetrics();
		final Rectangle2D rect = fontMetrics.getStringBounds(string, g2d);
		g2d.setColor(Color.WHITE);
		g2d.fillRect(x, y, (int) rect.getWidth(), (int) rect.getHeight());

		g2d.setColor(Color.BLACK);
		g2d.drawString(string, x, y + fontMetrics.getAscent());

		g2d.setColor(oldColor);
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, oldAntialiasing);
	}

	private static BufferedImage readImage(File file) {
		if (!file.exists()) return null;

		try {
			return ImageIO.read(file);
		} catch (IOException e) {
			logException(e, "Error reading '%s'", file);
			return null;
		}
	}

	private static int sum(int[] values) {
		int result = 0;
		for (int v : values) result += v;
		return result;
	}
}
