package net.sourceforge.plantuml.nassidiagram.element;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.klimt.shape.URectangle;
import net.sourceforge.plantuml.klimt.shape.UText;
import net.sourceforge.plantuml.klimt.UTranslate;
import net.sourceforge.plantuml.klimt.font.FontConfiguration;
import net.sourceforge.plantuml.klimt.font.UFont;
import net.sourceforge.plantuml.klimt.geom.XDimension2D;
import net.sourceforge.plantuml.nassidiagram.NassiElement;

public class NassiBlock extends NassiElement {
    private static final int MARGIN = 10;
    
    public NassiBlock(String text) {
        super(text);
        // Initialize with default dimension
        dimension = new Rectangle2D.Double(0, 0, 100, 40);
    }

    @Override
    public void computeDimension(Graphics2D g2d) {
        if (g2d == null) {
            // Provide default dimensions if Graphics2D is not available
            dimension = new Rectangle2D.Double(0, 0, 100, 40);
            return;
        }
        
        java.awt.FontMetrics fm = g2d.getFontMetrics();
        double width = fm.stringWidth(text) + 2 * MARGIN;
        double height = fm.getHeight() + 2 * MARGIN;
        dimension = new Rectangle2D.Double(0, 0, width, height);
    }

    @Override
    public void draw(UGraphic ug) {
        // Ensure dimension is initialized
        if (dimension == null) {
            dimension = new Rectangle2D.Double(0, 0, 100, 40);
        }
        
        // Create rectangle
        URectangle rect = URectangle.build(dimension.getWidth(), dimension.getHeight());
        ug.draw(rect);
        
        // Create and configure text
        FontConfiguration fontConfig = FontConfiguration.blackBlueTrue(UFont.monospaced(14));
        UText txt = UText.build(text, fontConfig);
        
        // Calculate text position
        XDimension2D dimText = txt.calculateDimension(ug.getStringBounder());
        double textX = (dimension.getWidth() - dimText.getWidth()) / 2;
        double textY = (dimension.getHeight() + dimText.getHeight()) / 2 - MARGIN;
        
        // Draw text
        ug.apply(new UTranslate(textX, textY)).draw(txt);
    }
}