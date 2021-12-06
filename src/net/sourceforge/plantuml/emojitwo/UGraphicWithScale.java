package net.sourceforge.plantuml.emojitwo;

import java.awt.geom.AffineTransform;

import net.sourceforge.plantuml.ugraphic.UChange;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UShape;

public class UGraphicWithScale {

	final private UGraphic ug;
	final private AffineTransform at;

	public UGraphicWithScale(UGraphic ug, double scale) {
		this(ug, AffineTransform.getScaleInstance(scale, scale));
	}

	private UGraphicWithScale(UGraphic ug, AffineTransform at) {
		this.ug = ug;
		this.at = at;
	}

	public UGraphic getUg() {
		return ug;
	}

	public double getScaleX() {
		return at.getScaleX();
	}

	public double getScaleY() {
		return at.getScaleY();
	}

	public UGraphicWithScale apply(UChange change) {
		return new UGraphicWithScale(ug.apply(change), at);
	}

	public UGraphicWithScale applyScale(double changex, double changey) {
		final AffineTransform copy = new AffineTransform(at);
		copy.scale(changex, changey);
		return new UGraphicWithScale(ug, copy);
	}

	public void draw(UShape shape) {
		ug.draw(shape);
	}

	public UGraphicWithScale applyRotate(double angle, double x, double y) {
		final AffineTransform copy = new AffineTransform(at);
		copy.rotate(angle * Math.PI / 180, x, y);
		return new UGraphicWithScale(ug, copy);
	}

	public UGraphicWithScale applyTranslate(double x, double y) {
		final AffineTransform copy = new AffineTransform(at);
		copy.translate(x, y);
		return new UGraphicWithScale(ug, copy);
	}

	public AffineTransform getAffineTransform() {
		return at;
	}

	public UGraphicWithScale applyMatrix(double v1, double v2, double v3, double v4, double v5, double v6) {
		final AffineTransform copy = new AffineTransform(at);
		copy.concatenate(new AffineTransform(new double[] { v1, v2, v3, v4, v5, v6 }));
		return new UGraphicWithScale(ug, copy);
	}

}
