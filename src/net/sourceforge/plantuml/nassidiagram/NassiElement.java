package net.sourceforge.plantuml.nassidiagram;

import java.awt.Graphics2D;
import java.awt.geom.Dimension2D;
import java.awt.geom.Rectangle2D;
import net.sourceforge.plantuml.ugraphic.UGraphic;

public abstract class NassiElement {
    protected String text;
    protected Rectangle2D dimension;

    public NassiElement(String text) {
        this.text = text;
    }

    public abstract void computeDimension(Graphics2D g2d);
    public abstract void draw(UGraphic ug);

    public Rectangle2D getDimension() {
        return dimension;
    }
}