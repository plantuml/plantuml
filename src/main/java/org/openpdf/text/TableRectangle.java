package org.openpdf.text;

public class TableRectangle extends Rectangle {

    public TableRectangle(float llx, float lly, float urx, float ury) {
        super(llx, lly, urx, ury);
    }

    public TableRectangle(float urx, float ury) {
        super(urx, ury);
    }

    public TableRectangle(float llx, float lly, float urx, float ury, int rotation) {
        super(llx, lly, urx, ury, rotation);
    }

    public TableRectangle(float urx, float ury, int rotation) {
        super(urx, ury, rotation);
    }

    public TableRectangle(Rectangle rect) {
        super(rect);
    }
}
