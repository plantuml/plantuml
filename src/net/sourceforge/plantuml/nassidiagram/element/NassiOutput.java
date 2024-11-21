package net.sourceforge.plantuml.nassidiagram.element;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.klimt.shape.UPolygon;
import net.sourceforge.plantuml.klimt.shape.UText;
import net.sourceforge.plantuml.klimt.UTranslate;
import net.sourceforge.plantuml.klimt.font.FontConfiguration;
import net.sourceforge.plantuml.klimt.font.UFont;
import net.sourceforge.plantuml.nassidiagram.NassiElement;

public class NassiOutput extends NassiElement {
    private static final int SLANT = 15;
    private static final int HEIGHT = 40;
    
    public NassiOutput(String text) {
        super(text);
    }

    @Override
    public void computeDimension(Graphics2D g2d) {
        java.awt.FontMetrics fm = g2d.getFontMetrics();
        double width = fm.stringWidth(text) + 2 * SLANT + 20;
        dimension = new Rectangle2D.Double(0, 0, width, HEIGHT);
    }

    @Override
    public void draw(UGraphic ug) {
        // Draw parallelogram (opposite slant from input)
        UPolygon parallelogram = new UPolygon();
        parallelogram.addPoint(0, 0);
        parallelogram.addPoint(dimension.getWidth() - SLANT, 0);
        parallelogram.addPoint(dimension.getWidth(), HEIGHT);
        parallelogram.addPoint(SLANT, HEIGHT);
        
        ug.draw(parallelogram);

        // Draw text
        FontConfiguration fontConfig = FontConfiguration.blackBlueTrue(UFont.monospaced(14));
        UText txt = UText.build(text, fontConfig);
        double textX = SLANT + (dimension.getWidth() - 2*SLANT - txt.calculateDimension(ug.getStringBounder()).getWidth()) / 2;
        double textY = HEIGHT/2 + 5;
        ug.apply(new UTranslate(textX, textY)).draw(txt);
    }
} 