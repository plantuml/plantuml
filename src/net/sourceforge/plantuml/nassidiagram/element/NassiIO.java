package net.sourceforge.plantuml.nassidiagram.element;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.klimt.shape.UPolygon;
import net.sourceforge.plantuml.klimt.UTranslate;
import net.sourceforge.plantuml.klimt.color.HColors;
import net.sourceforge.plantuml.nassidiagram.NassiElement;
import net.sourceforge.plantuml.nassidiagram.util.NassiDrawingUtil;

public class NassiIO extends NassiElement {
    private final boolean isInput;
    private static final int HEIGHT = 40;
    private static final double SLANT_RATIO = 0.2; // Slant as a ratio of height
    
    public NassiIO(String text, boolean isInput) {
        super(text);
        this.isInput = isInput;
    }

    @Override
    public void computeDimension(Graphics2D g2d) {
        java.awt.FontMetrics fm = g2d.getFontMetrics();
        // Calculate width based on text and slant
        String displayText = (isInput ? "READ " : "WRITE ") + text;
        double contentWidth = fm.stringWidth(displayText) + 4 * NassiDrawingUtil.PADDING;
        double width = Math.max(NassiDrawingUtil.MIN_WIDTH, contentWidth);
        dimension = new Rectangle2D.Double(0, 0, width, HEIGHT);
    }

    @Override
    public void draw(UGraphic ug) {
        double width = dimension.getWidth();
        double height = dimension.getHeight();
        double slant = height * SLANT_RATIO;

        // Create parallelogram shape
        UPolygon parallelogram = new UPolygon();
        if (isInput) {
            // Input parallelogram (slanted left)
            parallelogram.addPoint(slant, 0);
            parallelogram.addPoint(width, 0);
            parallelogram.addPoint(width - slant, height);
            parallelogram.addPoint(0, height);
        } else {
            // Output parallelogram (slanted right)
            parallelogram.addPoint(0, 0);
            parallelogram.addPoint(width - slant, 0);
            parallelogram.addPoint(width, height);
            parallelogram.addPoint(slant, height);
        }
        
        // Draw parallelogram
        ug.apply(HColors.WHITE.bg())
          .apply(HColors.BLACK)
          .draw(parallelogram);

        // Draw centered text with prefix
        NassiDrawingUtil.drawCenteredText(ug, 0, 0, width, height, 
            (isInput ? "READ " : "WRITE ") + text);
    }
} 