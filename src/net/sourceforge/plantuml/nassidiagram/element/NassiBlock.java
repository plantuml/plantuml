package net.sourceforge.plantuml.nassidiagram.element;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.klimt.shape.URectangle;
import net.sourceforge.plantuml.klimt.shape.UText;
import net.sourceforge.plantuml.klimt.UTranslate;
import net.sourceforge.plantuml.klimt.font.FontConfiguration;
import net.sourceforge.plantuml.klimt.font.UFont;
import net.sourceforge.plantuml.klimt.color.HColors;
import net.sourceforge.plantuml.nassidiagram.NassiElement;

public class NassiBlock extends NassiElement {
    private static final int PADDING = 10;
    
    public NassiBlock(String text) {
        super(text);
    }

    @Override
    public void computeDimension(Graphics2D g2d) {
        java.awt.FontMetrics fm = g2d.getFontMetrics();
        double width = fm.stringWidth(text) + 2 * PADDING;
        double height = fm.getHeight() + 2 * PADDING;
        dimension = new Rectangle2D.Double(0, 0, width, height);
    }

    @Override
    public void draw(UGraphic ug) {
        // Draw filled rectangle with border
        URectangle rect = URectangle.build(dimension.getWidth(), dimension.getHeight());
        ug.apply(HColors.WHITE.bg()).apply(HColors.BLACK).draw(rect);

        // Draw text with proper font configuration
        FontConfiguration fontConfig = FontConfiguration.blackBlueTrue(UFont.sansSerif(12));
        UText txt = UText.build(text, fontConfig);
        
        // Center text in block
        double textWidth = txt.calculateDimension(ug.getStringBounder()).getWidth();
        double textHeight = txt.calculateDimension(ug.getStringBounder()).getHeight();
        double textX = (dimension.getWidth() - textWidth) / 2;
        double textY = (dimension.getHeight() + textHeight) / 2 - 2;
        
        ug.apply(new UTranslate(textX, textY)).draw(txt);
    }
}