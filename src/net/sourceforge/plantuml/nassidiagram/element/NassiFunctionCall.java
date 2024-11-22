package net.sourceforge.plantuml.nassidiagram.element;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.klimt.shape.URectangle;
import net.sourceforge.plantuml.klimt.UTranslate;
import net.sourceforge.plantuml.klimt.color.HColors;
import net.sourceforge.plantuml.nassidiagram.NassiElement;
import net.sourceforge.plantuml.nassidiagram.util.NassiDrawingUtil;

public class NassiFunctionCall extends NassiElement {
    private static final int BORDER_GAP = 3;
    
    public NassiFunctionCall(String text) {
        super(text);
    }

    @Override
    public void computeDimension(Graphics2D g2d) {
        java.awt.FontMetrics fm = g2d.getFontMetrics();
        // Account for "Call: " prefix and double border
        double contentWidth = fm.stringWidth("Call: " + text) + 4 * NassiDrawingUtil.PADDING;
        double width = Math.max(NassiDrawingUtil.MIN_WIDTH, contentWidth);
        double height = Math.max(NassiDrawingUtil.MIN_HEIGHT, 
                               fm.getHeight() + 2 * NassiDrawingUtil.PADDING);
        dimension = new Rectangle2D.Double(0, 0, width, height);
    }

    @Override
    public void draw(UGraphic ug) {
        double width = dimension.getWidth();
        double height = dimension.getHeight();

        // Draw outer rectangle
        URectangle outer = URectangle.build(width, height);
        ug.apply(HColors.WHITE.bg())
          .apply(HColors.BLACK)
          .draw(outer);

        // Draw inner rectangle
        URectangle inner = URectangle.build(
            width - 2*BORDER_GAP, 
            height - 2*BORDER_GAP
        );
        ug.apply(new UTranslate(BORDER_GAP, BORDER_GAP))
          .apply(HColors.WHITE.bg())
          .apply(HColors.BLACK)
          .draw(inner);

        // Draw centered text
        NassiDrawingUtil.drawCenteredText(ug, 0, 0, width, height, "Call: " + text);
    }
} 