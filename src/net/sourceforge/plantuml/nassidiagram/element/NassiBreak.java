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

public class NassiBreak extends NassiElement {
    private static final int HEIGHT = 40;
    private static final int SIDE_MARGIN = 20;
    
    public NassiBreak(String text) {
        super(text);
    }

    @Override
    public void computeDimension(Graphics2D g2d) {
        java.awt.FontMetrics fm = g2d.getFontMetrics();
        double width = fm.stringWidth(text) + 2 * SIDE_MARGIN;
        dimension = new Rectangle2D.Double(0, 0, width, HEIGHT);
    }

    @Override
    public void draw(UGraphic ug) {
        // Draw hexagon
        UPolygon hexagon = new UPolygon();
        double w = dimension.getWidth();
        double h = dimension.getHeight();
        hexagon.addPoint(SIDE_MARGIN, 0);
        hexagon.addPoint(w - SIDE_MARGIN, 0);
        hexagon.addPoint(w, h/2);
        hexagon.addPoint(w - SIDE_MARGIN, h);
        hexagon.addPoint(SIDE_MARGIN, h);
        hexagon.addPoint(0, h/2);
        
        ug.draw(hexagon);

        // Draw text
        FontConfiguration fontConfig = FontConfiguration.blackBlueTrue(UFont.monospaced(14));
        UText txt = UText.build(text, fontConfig);
        double textX = (dimension.getWidth() - txt.calculateDimension(ug.getStringBounder()).getWidth()) / 2;
        double textY = HEIGHT/2 + 5;
        ug.apply(new UTranslate(textX, textY)).draw(txt);
    }
} 