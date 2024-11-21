package net.sourceforge.plantuml.nassidiagram.element;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.nassidiagram.NassiElement;
import net.sourceforge.plantuml.nassidiagram.util.NassiDrawingUtil;

public class NassiBlock extends NassiElement {
    public NassiBlock(String text) {
        super(text);
    }

    @Override
    public void computeDimension(Graphics2D g2d) {
        java.awt.FontMetrics fm = g2d.getFontMetrics();
        double width = Math.max(NassiDrawingUtil.MIN_WIDTH, 
                              fm.stringWidth(text) + 2 * 20);
        double height = Math.max(NassiDrawingUtil.MIN_HEIGHT, 
                               fm.getHeight() + 2 * 10);
        dimension = new Rectangle2D.Double(0, 0, width, height);
    }

    @Override
    public void draw(UGraphic ug) {
        NassiDrawingUtil.drawBlock(ug, 0, 0, dimension.getWidth(), dimension.getHeight(), text);
    }
}