package net.sourceforge.plantuml.nassidiagram.element;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.klimt.UTranslate;
import net.sourceforge.plantuml.nassidiagram.NassiElement;
import net.sourceforge.plantuml.nassidiagram.util.NassiDrawingUtil;
import java.util.List;
import java.util.ArrayList;

public class NassiWhile extends NassiElement {
    private final List<NassiElement> body = new ArrayList<>();
    private static final int HEADER_HEIGHT = 40;
    
    public NassiWhile(String condition) {
        super(condition);
    }

    public void addBodyElement(NassiElement element) {
        body.add(element);
    }

    @Override
    public void computeDimension(Graphics2D g2d) {
        double width = NassiDrawingUtil.MIN_WIDTH;
        double bodyHeight = 0;
        
        for (NassiElement element : body) {
            element.computeDimension(g2d);
            width = Math.max(width, element.getDimension().getWidth());
            bodyHeight += element.getDimension().getHeight();
        }
        
        dimension = new Rectangle2D.Double(0, 0, width, HEADER_HEIGHT + bodyHeight);
    }

    @Override
    public void draw(UGraphic ug) {
        double width = dimension.getWidth();
        double height = dimension.getHeight();

        // Draw while header with trapezoid shape
        NassiDrawingUtil.drawWhileHeader(ug, 0, 0, width, HEADER_HEIGHT, text);

        // Draw body elements vertically stacked
        double currentY = HEADER_HEIGHT;
        for (NassiElement element : body) {
            element.draw(ug.apply(new UTranslate(0, currentY)));
            currentY += element.getDimension().getHeight();
        }
    }
} 