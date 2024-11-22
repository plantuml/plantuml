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

public class NassiBreak extends NassiElement {
    private static final int HEIGHT = 40;
    private static final int DIAGONAL_SIZE = 8;  // Smaller diagonal marks
    
    public NassiBreak(String text) {
        super(text);
    }

    @Override
    public void computeDimension(Graphics2D g2d) {
        java.awt.FontMetrics fm = g2d.getFontMetrics();
        // Calculate width based on text with "Break: " prefix
        double contentWidth = fm.stringWidth("Break: " + text) + 4 * NassiDrawingUtil.PADDING;
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

        // Draw diagonal marks in corners
        // Top-left corner
        drawCornerMark(ug, 0, 0, DIAGONAL_SIZE, DIAGONAL_SIZE);
        // Top-right corner
        drawCornerMark(ug, width - DIAGONAL_SIZE, 0, -DIAGONAL_SIZE, DIAGONAL_SIZE);
        // Bottom-left corner
        drawCornerMark(ug, 0, height - DIAGONAL_SIZE, DIAGONAL_SIZE, -DIAGONAL_SIZE);
        // Bottom-right corner
        drawCornerMark(ug, width - DIAGONAL_SIZE, height - DIAGONAL_SIZE, -DIAGONAL_SIZE, -DIAGONAL_SIZE);

        // Draw centered text
        NassiDrawingUtil.drawCenteredText(ug, 0, 0, width, height, "Break: " + text);
    }

    private void drawCornerMark(UGraphic ug, double x, double y, double dx, double dy) {
        ULine diagonal = new ULine(dx, dy);
        ug.apply(new UTranslate(x, y))
          .apply(HColors.BLACK)
          .draw(diagonal);
    }
} 