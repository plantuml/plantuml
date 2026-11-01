package org.scilab.forge.jlatexmath;

import java.awt.Color;

/** A canvas that draws nothing, for the tests: they override what they look at. */
class NoopGraphics implements TeXGraphics {

	public Color getColor() {
		return Color.BLACK;
	}

	public void setColor(Color color) {
	}

	public void setLineWidth(float width) {
	}

	public void saveStroke() {
	}

	public void restoreStroke() {
	}

	public void fillRect(float x, float y, float width, float height) {
	}

	public void drawRect(float x, float y, float width, float height) {
	}

	public void drawRoundRect(float x, float y, float width, float height, float arcWidth, float arcHeight) {
	}

	public void drawLine(float x1, float y1, float x2, float y2) {
	}

	public void drawChar(int fontId, char c) {
	}

	public void drawText(String text, double fontSize) {
	}

	public void translate(double tx, double ty) {
	}

	public void scale(double sx, double sy) {
	}

	public void rotate(double theta) {
	}

	public void rotate(double theta, double x, double y) {
	}

	public double getScaleX() {
		return 1;
	}

	public void saveTransform() {
	}

	public void restoreTransform() {
	}

}
