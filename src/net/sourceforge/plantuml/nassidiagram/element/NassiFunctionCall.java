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
import net.sourceforge.plantuml.nassidiagram.util.NassiDrawingUtil;

public class NassiFunctionCall extends NassiElement {
    private static final int BORDER_GAP = 3;
    private static final int HEIGHT = 40;
    
    public NassiFunctionCall(String text) {
        super(text);
    }

    @Override
    public void computeDimension(Graphics2D g2d) {
        java.awt.FontMetrics fm = g2d.getFontMetrics();
        double width = Math.max(NassiDrawingUtil.MIN_WIDTH, 
                              fm.stringWidth(text) + 40);
        dimension = new Rectangle2D.Double(0, 0, width, HEIGHT);
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
        FontConfiguration fontConfig = FontConfiguration.blackBlueTrue(UFont.sansSerif(12));
        UText txt = UText.build("Call: " + text, fontConfig);
        XDimension2D textDim = txt.calculateDimension(ug.getStringBounder());
        
        double textX = (width - textDim.getWidth()) / 2;
        double textY = (height + textDim.getHeight()) / 2;
        ug.apply(new UTranslate(textX, textY)).draw(txt);
    }
} 