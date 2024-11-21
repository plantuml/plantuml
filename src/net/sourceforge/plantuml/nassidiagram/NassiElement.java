package net.sourceforge.plantuml.nassidiagram;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import net.sourceforge.plantuml.klimt.drawing.UGraphic;

public abstract class NassiElement {
    protected String text;
    protected Rectangle2D dimension;
    protected NassiElement parent;

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

    // Helper method to find the root element (diagram)
    protected NassiElement getRoot() {
        NassiElement current = this;
        while (current.getParent() != null) {
            current = current.getParent();
        }
        return current;
    }
}