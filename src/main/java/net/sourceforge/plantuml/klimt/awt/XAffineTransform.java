package net.sourceforge.plantuml.klimt.awt;

import net.sourceforge.plantuml.klimt.geom.XPoint2D;

/**
 * Affine transformation matrix implementation compatible with both standard JVM
 * and TeaVM environments.
 * 
 * <p>
 * The transformation matrix is represented as:
 * </p>
 * 
 * <pre>
 * [ m00  m01  m02 ]   [ x ]   [ m00*x + m01*y + m02 ]
 * [ m10  m11  m12 ] * [ y ] = [ m10*x + m11*y + m12 ]
 * [  0    0    1  ]   [ 1 ]   [          1          ]
 * </pre>
 * 
 * <p>
 * Where:
 * </p>
 * <ul>
 * <li>m00, m11 = scale factors (scaleX, scaleY)</li>
 * <li>m01, m10 = shear/rotation factors</li>
 * <li>m02, m12 = translation factors (translateX, translateY)</li>
 * </ul>
 */
public class XAffineTransform {

	private double m00;
	private double m10;
	private double m01;
	private double m11;
	private double m02;
	private double m12;

	// ------------------------------------------------------------------------
	// Constructors
	// ------------------------------------------------------------------------

	/**
	 * Creates a transform from the 6 specified values.
	 *
	 * @param m00 the X coordinate scaling element
	 * @param m10 the Y coordinate shearing element
	 * @param m01 the X coordinate shearing element
	 * @param m11 the Y coordinate scaling element
	 * @param m02 the X coordinate translation element
	 * @param m12 the Y coordinate translation element
	 */
	public XAffineTransform(double m00, double m10, double m01, double m11, double m02, double m12) {
		this.m00 = m00;
		this.m10 = m10;
		this.m01 = m01;
		this.m11 = m11;
		this.m02 = m02;
		this.m12 = m12;
	}

	/**
	 * Copy constructor.
	 *
	 * @param other the transform to copy
	 */
	public XAffineTransform(XAffineTransform other) {
		this.m00 = other.m00;
		this.m10 = other.m10;
		this.m01 = other.m01;
		this.m11 = other.m11;
		this.m02 = other.m02;
		this.m12 = other.m12;
	}

	/**
	 * Creates a transform from a 6-element array.
	 *
	 * @param flatMatrix array containing [m00, m10, m01, m11, m02, m12]
	 */
	public XAffineTransform(double[] flatMatrix) {
		this.m00 = flatMatrix[0];
		this.m10 = flatMatrix[1];
		this.m01 = flatMatrix[2];
		this.m11 = flatMatrix[3];
		this.m02 = flatMatrix[4];
		this.m12 = flatMatrix[5];
	}

	/**
	 * Creates an identity transform.
	 */
	public XAffineTransform() {
		this.m00 = 1.0;
		this.m10 = 0.0;
		this.m01 = 0.0;
		this.m11 = 1.0;
		this.m02 = 0.0;
		this.m12 = 0.0;
	}

	// ------------------------------------------------------------------------
	// Factory methods
	// ------------------------------------------------------------------------

	/**
	 * Creates a rotation transform.
	 *
	 * @param thetaRadians the angle of rotation in radians
	 * @return a new rotation transform
	 */
	public static XAffineTransform getRotateInstance(double thetaRadians) {
		final double cos = Math.cos(thetaRadians);
		final double sin = Math.sin(thetaRadians);
		return new XAffineTransform(cos, sin, -sin, cos, 0.0, 0.0);
	}

	/**
	 * Creates a translation transform.
	 *
	 * @param tx the X translation
	 * @param ty the Y translation
	 * @return a new translation transform
	 */
	public static XAffineTransform getTranslateInstance(double tx, double ty) {
		return new XAffineTransform(1.0, 0.0, 0.0, 1.0, tx, ty);
	}

	/**
	 * Creates a scaling transform.
	 *
	 * @param sx the X scale factor
	 * @param sy the Y scale factor
	 * @return a new scaling transform
	 */
	public static XAffineTransform getScaleInstance(double sx, double sy) {
		return new XAffineTransform(sx, 0.0, 0.0, sy, 0.0, 0.0);
	}

	// ------------------------------------------------------------------------
	// Accessors
	// ------------------------------------------------------------------------

	/**
	 * Returns the X coordinate scaling element (m00).
	 *
	 * @return the X scaling factor
	 */
	public double getScaleX() {
		return m00;
	}

	/**
	 * Returns the Y coordinate scaling element (m11).
	 *
	 * @return the Y scaling factor
	 */
	public double getScaleY() {
		return m11;
	}

	/**
	 * Returns the X coordinate translation element (m02).
	 *
	 * @return the X translation
	 */
	public double getTranslateX() {
		return m02;
	}

	/**
	 * Returns the Y coordinate translation element (m12).
	 *
	 * @return the Y translation
	 */
	public double getTranslateY() {
		return m12;
	}

	// ------------------------------------------------------------------------
	// Mutators / operations
	// ------------------------------------------------------------------------

	/**
	 * Concatenates a scaling transformation to this transform.
	 *
	 * @param sx the X scale factor
	 * @param sy the Y scale factor
	 */
	public void scale(double sx, double sy) {
		m00 *= sx;
		m01 *= sx;
		m02 *= sx;
		m10 *= sy;
		m11 *= sy;
		m12 *= sy;
	}

	/**
	 * Transforms the specified point.
	 *
	 * @param src the source point
	 * @return the transformed point
	 */
	public XPoint2D transform(XPoint2D src) {
		final double x = m00 * src.x + m01 * src.y + m02;
		final double y = m10 * src.x + m11 * src.y + m12;
		return new XPoint2D(x, y);
	}

	/**
	 * Concatenates another transform to this transform.
	 * <p>
	 * This is equivalent to: [this] = [this] * [other]
	 * </p>
	 *
	 * @param other the transform to concatenate
	 */
	public void concatenate(XAffineTransform other) {
		final double n00 = m00 * other.m00 + m01 * other.m10;
		final double n01 = m00 * other.m01 + m01 * other.m11;
		final double n02 = m00 * other.m02 + m01 * other.m12 + m02;
		final double n10 = m10 * other.m00 + m11 * other.m10;
		final double n11 = m10 * other.m01 + m11 * other.m11;
		final double n12 = m10 * other.m02 + m11 * other.m12 + m12;

		m00 = n00;
		m01 = n01;
		m02 = n02;
		m10 = n10;
		m11 = n11;
		m12 = n12;
	}

	/**
	 * Concatenates a translation transformation to this transform.
	 *
	 * @param tx the X translation
	 * @param ty the Y translation
	 */
	public void translate(double tx, double ty) {
		m02 += m00 * tx + m01 * ty;
		m12 += m10 * tx + m11 * ty;
	}

	/**
	 * Concatenates a rotation transformation around the specified anchor point.
	 *
	 * @param thetaRadians the angle of rotation in radians
	 * @param anchorX      the X coordinate of the rotation anchor
	 * @param anchorY      the Y coordinate of the rotation anchor
	 */
	public void rotate(double thetaRadians, double anchorX, double anchorY) {
		translate(anchorX, anchorY);
		final double cos = Math.cos(thetaRadians);
		final double sin = Math.sin(thetaRadians);
		final double n00 = m00 * cos + m01 * sin;
		final double n01 = m00 * -sin + m01 * cos;
		final double n10 = m10 * cos + m11 * sin;
		final double n11 = m10 * -sin + m11 * cos;
		m00 = n00;
		m01 = n01;
		m10 = n10;
		m11 = n11;
		translate(-anchorX, -anchorY);
	}

	// ------------------------------------------------------------------------
	// Conversion
	// ------------------------------------------------------------------------

	// ::comment when __TEAVM__
	/**
	 * Creates a new {@link java.awt.geom.AffineTransform} with the same matrix
	 * values.
	 *
	 * @return a new AffineTransform instance
	 */
	public java.awt.geom.AffineTransform toAffineTransform() {
		return new java.awt.geom.AffineTransform(m00, m10, m01, m11, m02, m12);
	}
	// ::done

	@Override
	public String toString() {
		return "XAffineTransform[[" + m00 + ", " + m01 + ", " + m02 + "], [" + m10 + ", " + m11 + ", " + m12 + "]]";
	}

}
