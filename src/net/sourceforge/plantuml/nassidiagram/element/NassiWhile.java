package net.sourceforge.plantuml.nassidiagram.element;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.klimt.UTranslate;
import net.sourceforge.plantuml.nassidiagram.NassiElement;
import net.sourceforge.plantuml.nassidiagram.util.NassiDrawingUtil;
import java.util.ArrayList;
import java.util.List;

public class NassiWhile extends NassiElement {
    private final List<NassiElement> body = new ArrayList<>();
    private static final int HEADER_HEIGHT = 40;
    
    public NassiWhile(String condition) {
        super(condition);
    }

    public void addBodyElement(NassiElement element) {
        body.add(element);
        element.setParent(this);
    }

    @Override
    public void computeDimension(Graphics2D g2d) {
        java.awt.FontMetrics fm = g2d.getFontMetrics();
        // Calculate width based on condition text and body elements
        double contentWidth = fm.stringWidth(text) + 4 * NassiDrawingUtil.PADDING;
        double width = Math.max(NassiDrawingUtil.MIN_WIDTH, contentWidth);
        double bodyHeight = 0;
        
        // Consider body elements for width and height
        for (NassiElement element : body) {
            element.computeDimension(g2d);
            Rectangle2D elementDim = element.getDimension();
            if (elementDim != null) {
                width = Math.max(width, elementDim.getWidth());
                bodyHeight += elementDim.getHeight();
            }
        }
        
        dimension = new Rectangle2D.Double(0, 0, width, HEADER_HEIGHT + bodyHeight);
    }

    @Override
    public void draw(UGraphic ug) {
        double width = dimension.getWidth();
        double height = dimension.getHeight();

        // Draw while header
        NassiDrawingUtil.drawWhileHeader(ug, 0, 0, width, HEADER_HEIGHT, text);

        // Draw body elements vertically stacked
        double currentY = HEADER_HEIGHT;
        for (NassiElement element : body) {
            element.draw(ug.apply(new UTranslate(0, currentY)));
            currentY += element.getDimension().getHeight();
        }
    }
} 