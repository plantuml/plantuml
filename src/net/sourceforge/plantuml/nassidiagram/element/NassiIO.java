package net.sourceforge.plantuml.nassidiagram.element;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.klimt.shape.UPolygon;
import net.sourceforge.plantuml.klimt.shape.UText;
import net.sourceforge.plantuml.klimt.UTranslate;
import net.sourceforge.plantuml.klimt.font.FontConfiguration;
import net.sourceforge.plantuml.klimt.font.UFont;
import net.sourceforge.plantuml.klimt.color.HColors;
import net.sourceforge.plantuml.nassidiagram.NassiElement;
import net.sourceforge.plantuml.nassidiagram.util.NassiDrawingUtil;
import net.sourceforge.plantuml.klimt.geom.XDimension2D;

public class NassiIO extends NassiElement {
    private final boolean isInput;
    private static final int SLANT = 20;
    private static final int HEIGHT = 40;
    
    public NassiIO(String text, boolean isInput) {
        super(text);
        this.isInput = isInput;
    }

    @Override
    public void computeDimension(Graphics2D g2d) {
        java.awt.FontMetrics fm = g2d.getFontMetrics();
        double width = Math.max(NassiDrawingUtil.MIN_WIDTH, 
                              fm.stringWidth(text) + 2 * SLANT + 20);
        dimension = new Rectangle2D.Double(0, 0, width, HEIGHT);
    }

    @Override
    public void draw(UGraphic ug) {
        double width = dimension.getWidth();
        double height = dimension.getHeight();

        // Draw parallelogram - slanted left for input, right for output
        UPolygon parallelogram = new UPolygon();
        if (isInput) {
            parallelogram.addPoint(SLANT, 0);
            parallelogram.addPoint(width, 0);
            parallelogram.addPoint(width - SLANT, height);
            parallelogram.addPoint(0, height);
        } else {
            parallelogram.addPoint(0, 0);
            parallelogram.addPoint(width - SLANT, 0);
            parallelogram.addPoint(width, height);
            parallelogram.addPoint(SLANT, height);
        }
        
        ug.apply(HColors.WHITE.bg())
          .apply(HColors.BLACK)
          .draw(parallelogram);

        // Draw centered text
        FontConfiguration fontConfig = FontConfiguration.blackBlueTrue(UFont.sansSerif(12));
        UText txt = UText.build((isInput ? "Input: " : "Output: ") + text, fontConfig);
        XDimension2D textDim = txt.calculateDimension(ug.getStringBounder());
        
        double textX = (width - textDim.getWidth()) / 2;
        double textY = (height + textDim.getHeight()) / 2;
        ug.apply(new UTranslate(textX, textY)).draw(txt);
    }
} 