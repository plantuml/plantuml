package net.sourceforge.plantuml.nassidiagram.element;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.klimt.shape.URectangle;
import net.sourceforge.plantuml.klimt.shape.UText;
import net.sourceforge.plantuml.klimt.UTranslate;
import net.sourceforge.plantuml.klimt.font.FontConfiguration;
import net.sourceforge.plantuml.klimt.font.UFont;
import net.sourceforge.plantuml.nassidiagram.NassiElement;

public class NassiFunctionCall extends NassiElement {
    private static final int MARGIN = 10;
    private static final int INNER_MARGIN = 5;
    
    public NassiFunctionCall(String text) {
        super(text);
    }

    @Override
    public void computeDimension(Graphics2D g2d) {
        java.awt.FontMetrics fm = g2d.getFontMetrics();
        double width = fm.stringWidth(text) + 2 * MARGIN;
        double height = fm.getHeight() + 2 * MARGIN;
        dimension = new Rectangle2D.Double(0, 0, width, height);
    }

    @Override
    public void draw(UGraphic ug) {
        // Draw outer rectangle
        URectangle outer = URectangle.build(dimension.getWidth(), dimension.getHeight());
        ug.draw(outer);
        
        // Draw inner rectangle
        URectangle inner = URectangle.build(
            dimension.getWidth() - 2*INNER_MARGIN, 
            dimension.getHeight() - 2*INNER_MARGIN
        );
        ug.apply(new UTranslate(INNER_MARGIN, INNER_MARGIN)).draw(inner);

        // Draw text
        FontConfiguration fontConfig = FontConfiguration.blackBlueTrue(UFont.monospaced(14));
        UText txt = UText.build(text, fontConfig);
        double textX = (dimension.getWidth() - txt.calculateDimension(ug.getStringBounder()).getWidth()) / 2;
        double textY = dimension.getHeight()/2 + 5;
        ug.apply(new UTranslate(textX, textY)).draw(txt);
    }
} 