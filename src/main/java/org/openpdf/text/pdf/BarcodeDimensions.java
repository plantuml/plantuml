package org.openpdf.text.pdf;

public class BarcodeDimensions {

    private int height;
    private int width;
    private int border;

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getBorder() {
        return border;
    }

    public void setBorder(int border) {
        this.border = border;
    }

    public BarcodeDimensions(int width, int height, int border) {
        this.width = width;
        this.height = height;
        this.border = border;
    }

    public BarcodeDimensions() {
        this(0, 0, 0);
    }


}