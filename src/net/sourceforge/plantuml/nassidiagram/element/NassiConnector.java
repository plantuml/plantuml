package net.sourceforge.plantuml.nassidiagram.element;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.klimt.shape.UEllipse;
import net.sourceforge.plantuml.klimt.UTranslate;
import net.sourceforge.plantuml.klimt.color.HColors;
import net.sourceforge.plantuml.nassidiagram.NassiElement;
import net.sourceforge.plantuml.nassidiagram.util.NassiDrawingUtil;
import net.sourceforge.plantuml.klimt.geom.XDimension2D;

public class NassiConnector extends NassiElement {
    private static final int HEIGHT = 40;
    private static final double CIRCLE_RATIO = 0.8; // Inner circle is 80% of outer circle
    
    public NassiConnector(String text) {
        super(text);
    }

    @Override
    public void computeDimension(Graphics2D g2d) {
        java.awt.FontMetrics fm = g2d.getFontMetrics();
        // Calculate width based on text and circle size
        double textWidth = fm.stringWidth(text) + 2 * NassiDrawingUtil.PADDING;
        double width = Math.max(HEIGHT, textWidth); // Make it square if text is shorter than height
        dimension = new Rectangle2D.Double(0, 0, width, HEIGHT);
    }

    @Override
    public void draw(UGraphic ug) {
        double width = dimension.getWidth();
        double height = dimension.getHeight();
        double diameter = Math.min(width, height);
        double x = (width - diameter) / 2;
        double y = (height - diameter) / 2;

        // Draw outer circle
        UEllipse outer = UEllipse.build(diameter, diameter);
        ug.apply(new UTranslate(x, y))
          .apply(HColors.WHITE.bg())
          .apply(HColors.BLACK)
          .draw(outer);

        // Draw inner circle
        double innerDiameter = diameter * CIRCLE_RATIO;
        double offset = (diameter - innerDiameter) / 2;
        UEllipse inner = UEllipse.build(innerDiameter, innerDiameter);
        ug.apply(new UTranslate(x + offset, y + offset))
          .apply(HColors.WHITE.bg())
          .apply(HColors.BLACK)
          .draw(inner);

        // Draw centered text
        NassiDrawingUtil.drawCenteredText(ug, 0, 0, width, height, text);
    }
} 