package net.sourceforge.plantuml.awt.geom;

import net.sourceforge.plantuml.awt.geom.Point2D.Double;

public class AffineTransform {

	public AffineTransform(AffineTransform at) {
		// TODO Auto-generated constructor stub
	}

	public AffineTransform(double[] ds) {
		// TODO Auto-generated constructor stub
	}

	public AffineTransform(int i, int j, int k, int l, int m, int n) {
		// TODO Auto-generated constructor stub
	}

	public AffineTransform() {
		// TODO Auto-generated constructor stub
	}

	public double getScaleX() {
		// TODO Auto-generated method stub
		return 0;
	}

	public double getScaleY() {
		// TODO Auto-generated method stub
		return 0;
	}

	public double getTranslateX() {
		// TODO Auto-generated method stub
		return 0;
	}

	public double getTranslateY() {
		// TODO Auto-generated method stub
		return 0;
	}

	public void translate(double x, double y) {
		// TODO Auto-generated method stub
		
	}

	public void concatenate(AffineTransform affineTransform) {
		// TODO Auto-generated method stub
		
	}

	public static AffineTransform getScaleInstance(double scale, double scale2) {
		// TODO Auto-generated method stub
		return null;
	}

	public void setToShear(double coef, double coef2) {
		// TODO Auto-generated method stub
		
	}

	public static AffineTransform getTranslateInstance(double tx, double ty) {
		// TODO Auto-generated method stub
		return null;
	}

	public static AffineTransform getShearInstance(double shx, double shy) {
		// TODO Auto-generated method stub
		return null;
	}

	public static AffineTransform getRotateInstance(double d) {
		// TODO Auto-generated method stub
		return null;
	}

	public Point2D transform(Point2D src, Point2D dest) {
		// TODO Auto-generated method stub
		return null;
	}

	public void scale(double changex, double changey) {
		// TODO Auto-generated method stub
		
	}

}
