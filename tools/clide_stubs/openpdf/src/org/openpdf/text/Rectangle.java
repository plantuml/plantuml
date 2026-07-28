package org.openpdf.text;

/**
 * Compile-only stub of OpenPDF's {@code Rectangle}. PlantUML's OpenPDF driver
 * only ever constructs and passes these around (page size, bounding boxes,
 * link regions) - it never calls a method on an instance - so unlike the
 * other classes in this jar, there is no "real work" to prevent here: this
 * is left as a harmless data holder rather than a throwing stub
 * (see clide_stubs/openpdf/README.md).
 */
public class Rectangle {

	public final float llx;
	public final float lly;
	public final float urx;
	public final float ury;

	public Rectangle(final float urx, final float ury) {
		this(0f, 0f, urx, ury);
	}

	public Rectangle(final float llx, final float lly, final float urx, final float ury) {
		this.llx = llx;
		this.lly = lly;
		this.urx = urx;
		this.ury = ury;
	}

}
