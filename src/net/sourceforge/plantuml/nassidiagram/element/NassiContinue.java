package net.sourceforge.plantuml.nassidiagram.element;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.klimt.shape.URectangle;
import net.sourceforge.plantuml.klimt.shape.ULine;
import net.sourceforge.plantuml.klimt.UTranslate;
import net.sourceforge.plantuml.klimt.color.HColors;
import net.sourceforge.plantuml.nassidiagram.NassiElement;
import net.sourceforge.plantuml.nassidiagram.util.NassiDrawingUtil;

public class NassiContinue extends NassiElement {
    private static final int HEIGHT = 40;
    private static final int ARROW_SIZE = 10;  // Size of the continue arrow
    
    public NassiContinue(String text) {
        super(text);
    }

    @Override
    public void computeDimension(Graphics2D g2d) {
        java.awt.FontMetrics fm = g2d.getFontMetrics();
        // Calculate width based on text with "Continue: " prefix
        double contentWidth = fm.stringWidth("Continue: " + text) + 4 * NassiDrawingUtil.PADDING;
        double width = Math.max(NassiDrawingUtil.MIN_WIDTH, contentWidth);
        dimension = new Rectangle2D.Double(0, 0, width, HEIGHT);
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

        // Draw circular arrow in corners to indicate continue
        drawContinueArrow(ug, ARROW_SIZE, ARROW_SIZE);                    // Top-left
        drawContinueArrow(ug, width - ARROW_SIZE, ARROW_SIZE);           // Top-right
        drawContinueArrow(ug, ARROW_SIZE, height - ARROW_SIZE);          // Bottom-left
        drawContinueArrow(ug, width - ARROW_SIZE, height - ARROW_SIZE);  // Bottom-right

        // Draw centered text
        NassiDrawingUtil.drawCenteredText(ug, 0, 0, width, height, "Continue: " + text);
    }

    private void drawContinueArrow(UGraphic ug, double x, double y) {
        // Draw a small circular arrow using three lines
        double size = ARROW_SIZE / 2.0;
        
        // Draw three sides of a square
        ULine top = new ULine(size, 0);
        ULine side = new ULine(0, size);
        ULine bottom = new ULine(-size, 0);
        
        // Draw arrow head
        ULine arrowHead1 = new ULine(size/3, -size/3);
        ULine arrowHead2 = new ULine(size/3, size/3);

        ug.apply(new UTranslate(x - size/2, y - size/2))
          .apply(HColors.BLACK)
          .draw(top);
        
        ug.apply(new UTranslate(x + size/2, y - size/2))
          .apply(HColors.BLACK)
          .draw(side);
        
        ug.apply(new UTranslate(x + size/2, y + size/2))
          .apply(HColors.BLACK)
          .draw(bottom);
        
        // Draw arrow head
        ug.apply(new UTranslate(x - size/2, y - size/2))
          .apply(HColors.BLACK)
          .draw(arrowHead1);
        
        ug.apply(new UTranslate(x - size/2, y - size/2))
          .apply(HColors.BLACK)
          .draw(arrowHead2);
    }
} 