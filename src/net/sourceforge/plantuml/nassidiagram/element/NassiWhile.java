package net.sourceforge.plantuml.nassidiagram.element;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.klimt.UTranslate;
import net.sourceforge.plantuml.klimt.shape.URectangle;
import net.sourceforge.plantuml.klimt.shape.ULine;
import net.sourceforge.plantuml.klimt.color.HColors;
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
        
        // Use forced width if set
        if (forcedWidth > 0) {
            width = forcedWidth;
        }

        double bodyHeight = 0;
        
        // Consider body elements for width and height
        for (NassiElement element : body) {
            element.setWidth(width);  // Pass down the width to maintain consistency
            element.computeDimension(g2d);
            Rectangle2D elementDim = element.getDimension();
            if (elementDim != null) {
                bodyHeight += elementDim.getHeight();
            }
        }
        
        dimension = new Rectangle2D.Double(0, 0, width, HEADER_HEIGHT + bodyHeight);
    }

    @Override
    public double getNestedWidth() {
        double maxWidth = getEffectiveWidth();
        
        // Check width requirements of nested elements
        for (NassiElement element : body) {
            maxWidth = Math.max(maxWidth, element.getNestedWidth());
        }
        
        return maxWidth;
    }

    @Override
    public void draw(UGraphic ug) {
        double width = dimension.getWidth();
        double height = dimension.getHeight();

        // Draw main rectangle
        URectangle rect = URectangle.build(width, height);
        ug.apply(HColors.WHITE.bg())
          .apply(HColors.BLACK)
          .draw(rect);

        // Draw while header with horizontal divider
        NassiDrawingUtil.drawWhileHeader(ug, 0, 0, width, HEADER_HEIGHT, text);

        // Draw body elements vertically stacked
        double currentY = HEADER_HEIGHT;
        for (NassiElement element : body) {
            element.draw(ug.apply(new UTranslate(0, currentY)));
            currentY += element.getDimension().getHeight();
        }
    }
} 