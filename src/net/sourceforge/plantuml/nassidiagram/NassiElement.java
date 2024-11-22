package net.sourceforge.plantuml.nassidiagram;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import net.sourceforge.plantuml.klimt.drawing.UGraphic;

public abstract class NassiElement {
    protected String text;
    protected Rectangle2D dimension;
    protected NassiElement parent;
    protected double forcedWidth = -1;  // For enforcing consistent widths

    public NassiElement(String text) {
        this.text = text;
    }

    public abstract void computeDimension(Graphics2D g2d);
    public abstract void draw(UGraphic ug);

    public Rectangle2D getDimension() {
        return dimension;
    }

    public void setParent(NassiElement parent) {
        this.parent = parent;
    }

    public NassiElement getParent() {
        return parent;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    // For width consistency across elements
    public void setWidth(double width) {
        this.forcedWidth = width;
    }

    // For nested elements to report their width requirements
    public double getNestedWidth() {
        return dimension != null ? dimension.getWidth() : 0;
    }

    // Helper method to find the root element (diagram)
    protected NassiElement getRoot() {
        NassiElement current = this;
        while (current.getParent() != null) {
            current = current.getParent();
        }
        return current;
    }

    // Helper method to get the effective width (forced or computed)
    protected double getEffectiveWidth() {
        return forcedWidth > 0 ? forcedWidth : 
               (dimension != null ? dimension.getWidth() : 0);
    }
}