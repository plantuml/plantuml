package net.sourceforge.plantuml.nassidiagram.element;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.klimt.shape.URectangle;
import net.sourceforge.plantuml.klimt.shape.ULine;
import net.sourceforge.plantuml.klimt.shape.UText;
import net.sourceforge.plantuml.klimt.UTranslate;
import net.sourceforge.plantuml.klimt.font.FontConfiguration;
import net.sourceforge.plantuml.klimt.font.UFont;
import net.sourceforge.plantuml.klimt.color.HColors;
import net.sourceforge.plantuml.nassidiagram.NassiElement;
import net.sourceforge.plantuml.nassidiagram.util.NassiDrawingUtil;
import net.sourceforge.plantuml.klimt.geom.XDimension2D;

public class NassiBreak extends NassiElement {
    private static final int HEIGHT = 40;
    private static final int DIAGONAL_GAP = 10;
    
    public NassiBreak(String text) {
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

        // Draw main rectangle
        URectangle rect = URectangle.build(width, height);
        ug.apply(HColors.WHITE.bg())
          .apply(HColors.BLACK)
          .draw(rect);

        // Draw diagonal lines in corners
        ULine diagLine1 = new ULine(DIAGONAL_GAP, DIAGONAL_GAP);
        ULine diagLine2 = new ULine(DIAGONAL_GAP, -DIAGONAL_GAP);
        
        // Top-left and bottom-right corners
        ug.apply(HColors.BLACK).draw(diagLine1);
        ug.apply(new UTranslate(width - DIAGONAL_GAP, height - DIAGONAL_GAP))
          .apply(HColors.BLACK)
          .draw(diagLine1);
          
        // Top-right and bottom-left corners
        ug.apply(new UTranslate(width - DIAGONAL_GAP, 0))
          .apply(HColors.BLACK)
          .draw(diagLine2);
        ug.apply(new UTranslate(0, height))
          .apply(HColors.BLACK)
          .draw(diagLine2);

        // Draw centered text
        FontConfiguration fontConfig = FontConfiguration.blackBlueTrue(UFont.sansSerif(12));
        UText txt = UText.build("Break: " + text, fontConfig);
        XDimension2D textDim = txt.calculateDimension(ug.getStringBounder());
        
        double textX = (width - textDim.getWidth()) / 2;
        double textY = (height + textDim.getHeight()) / 2;
        ug.apply(new UTranslate(textX, textY)).draw(txt);
    }
} 