package net.sourceforge.plantuml.nassidiagram.element;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.klimt.shape.UEllipse;
import net.sourceforge.plantuml.klimt.shape.UText;
import net.sourceforge.plantuml.klimt.UTranslate;
import net.sourceforge.plantuml.klimt.font.FontConfiguration;
import net.sourceforge.plantuml.klimt.font.UFont;
import net.sourceforge.plantuml.klimt.color.HColors;
import net.sourceforge.plantuml.nassidiagram.NassiElement;
import net.sourceforge.plantuml.nassidiagram.util.NassiDrawingUtil;

public class NassiConnector extends NassiElement {
    private static final int DIAMETER = 40;
    private static final int INNER_DIAMETER = 30;
    
    public NassiConnector(String text) {
        super(text);
    }

    @Override
    public void computeDimension(Graphics2D g2d) {
        dimension = new Rectangle2D.Double(0, 0, DIAMETER, DIAMETER);
    }

    @Override
    public void draw(UGraphic ug) {
        // Draw outer circle
        UEllipse outer = UEllipse.build(DIAMETER, DIAMETER);
        ug.apply(HColors.WHITE.bg())
          .apply(HColors.BLACK)
          .draw(outer);

        // Draw inner circle
        UEllipse inner = UEllipse.build(INNER_DIAMETER, INNER_DIAMETER);
        double offset = (DIAMETER - INNER_DIAMETER) / 2.0;
        ug.apply(new UTranslate(offset, offset))
          .apply(HColors.WHITE.bg())
          .apply(HColors.BLACK)
          .draw(inner);

        // Draw centered text
        FontConfiguration fontConfig = FontConfiguration.blackBlueTrue(UFont.sansSerif(10));
        UText txt = UText.build(text, fontConfig);
        XDimension2D textDim = txt.calculateDimension(ug.getStringBounder());
        
        double textX = (DIAMETER - textDim.getWidth()) / 2;
        double textY = (DIAMETER + textDim.getHeight()) / 2;
        ug.apply(new UTranslate(textX, textY)).draw(txt);
    }
} 