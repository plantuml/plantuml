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
        double width = fm.stringWidth(text) + 2 * SLANT + 20;
        dimension = new Rectangle2D.Double(0, 0, width, HEIGHT);
    }

    @Override
    public void draw(UGraphic ug) {
        // Draw parallelogram
        UPolygon shape = new UPolygon();
        if (isInput) {
            shape.addPoint(SLANT, 0);
            shape.addPoint(dimension.getWidth(), 0);
            shape.addPoint(dimension.getWidth() - SLANT, dimension.getHeight());
            shape.addPoint(0, dimension.getHeight());
        } else {
            shape.addPoint(0, 0);
            shape.addPoint(dimension.getWidth() - SLANT, 0);
            shape.addPoint(dimension.getWidth(), dimension.getHeight());
            shape.addPoint(SLANT, dimension.getHeight());
        }
        
        ug.apply(HColors.WHITE.bg()).apply(HColors.BLACK).draw(shape);

        // Draw text
        FontConfiguration fontConfig = FontConfiguration.blackBlueTrue(UFont.sansSerif(12));
        UText txt = UText.build(text, fontConfig);
        double textX = (dimension.getWidth() - txt.calculateDimension(ug.getStringBounder()).getWidth()) / 2;
        double textY = dimension.getHeight()/2 + txt.calculateDimension(ug.getStringBounder()).getHeight()/3;
        ug.apply(new UTranslate(textX, textY)).draw(txt);
    }
} 