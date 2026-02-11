package net.sourceforge.plantuml.klimt.awt;

import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;

import net.sourceforge.plantuml.klimt.geom.XPoint2D;

/**
 * Thin wrapper around {@link java.awt.geom.AffineTransform}. Delegates all
 * operations to an internal AffineTransform instance.
 */
public class XAffineTransform {

	// ::comment when __TEAVM__
	private final AffineTransform delegate;
	// ::done

	// ------------------------------------------------------------------------
	// Constructors (aligned with java.awt.geom.AffineTransform)
	// ------------------------------------------------------------------------

	/**
	 * Creates a transform from the 6 specified values.
	 *
	 * In {@link AffineTransform}: (m00, m10, m01, m11, m02, m12)
	 */
	public XAffineTransform(double m00, double m10, double m01, double m11, double m02, double m12) {
		// ::comment when __TEAVM__
		this.delegate = new AffineTransform(m00, m10, m01, m11, m02, m12);
		// ::done
	}

	/** Copy constructor. */
	public XAffineTransform(XAffineTransform other) {
		// ::comment when __TEAVM__
		this.delegate = new AffineTransform(other.delegate);
		// ::done
	}

	/**
	 * Creates a transform from a 6-element array (m00, m10, m01, m11, m02, m12).
	 */
	public XAffineTransform(double[] flatMatrix) {
		// ::comment when __TEAVM__
		this.delegate = new AffineTransform(flatMatrix);
		// ::done
	}

	/** Creates an identity transform. */
	public XAffineTransform() {
		// ::comment when __TEAVM__
		this.delegate = new AffineTransform();
		// ::done
	}

	// ------------------------------------------------------------------------
	// Factory methods
	// ------------------------------------------------------------------------

	public static XAffineTransform getRotateInstance(double thetaRadians) {
		// ::revert when __TEAVM__
		final XAffineTransform result = new XAffineTransform();
		result.delegate.setToRotation(thetaRadians);
		return result;
		// return new XAffineTransform();
		// ::done
	}

	public static XAffineTransform getTranslateInstance(double tx, double ty) {
		// ::revert when __TEAVM__
		final XAffineTransform result = new XAffineTransform();
		result.delegate.setToTranslation(tx, ty);
		return result;
		// return new XAffineTransform();
		// ::done
	}

	public static XAffineTransform getScaleInstance(double sx, double sy) {
		// ::revert when __TEAVM__
		final XAffineTransform result = new XAffineTransform();
		result.delegate.setToScale(sx, sy);
		return result;
		// return new XAffineTransform();
		// ::done
	}

	// ------------------------------------------------------------------------
	// Accessors
	// ------------------------------------------------------------------------

	public double getScaleX() {
		// ::revert when __TEAVM__
		return delegate.getScaleX();
		// return 0;
		// ::done
	}

	public double getScaleY() {
		// ::revert when __TEAVM__
		return delegate.getScaleY();
		// return 0;
		// ::done
	}

	public double getTranslateX() {
		// ::revert when __TEAVM__
		return delegate.getTranslateX();
		// return 0;
		// ::done
	}

	public double getTranslateY() {
		// ::revert when __TEAVM__
		return delegate.getTranslateY();
		// return 0;
		// ::done
	}

	// ------------------------------------------------------------------------
	// Mutators / operations
	// ------------------------------------------------------------------------

	public void scale(double sx, double sy) {
		// ::comment when __TEAVM__
		delegate.scale(sx, sy);
		// ::done
	}

	public XPoint2D transform(XPoint2D src) {
		// ::revert when __TEAVM__
		final Point2D dst = delegate.transform(new Point2D.Double(src.x, src.y), null);
		return new XPoint2D(dst.getX(), dst.getY());
		// return src;
		// ::done
	}

	public void concatenate(XAffineTransform other) {
		// ::comment when __TEAVM__
		delegate.concatenate(other.delegate);
		// ::done
	}

	public void translate(double tx, double ty) {
		// ::comment when __TEAVM__
		delegate.translate(tx, ty);
		// ::done
	}

	public void rotate(double thetaRadians, double anchorX, double anchorY) {
		// ::comment when __TEAVM__
		delegate.rotate(thetaRadians, anchorX, anchorY);
		// ::done
	}

	// ------------------------------------------------------------------------
	// Optional: expose the delegate when needed internally
	// ------------------------------------------------------------------------

	// ::comment when __TEAVM__
	public AffineTransform toAffineTransform() {
		return new AffineTransform(delegate);
	}

	@Override
	public String toString() {
		return delegate.toString();
	}
	// ::done
}
