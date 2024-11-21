package net.sourceforge.plantuml.nassidiagram.element;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.klimt.shape.UEllipse;
import net.sourceforge.plantuml.klimt.shape.UText;
import net.sourceforge.plantuml.klimt.UTranslate;
import net.sourceforge.plantuml.klimt.font.FontConfiguration;
import net.sourceforge.plantuml.klimt.font.UFont;
import net.sourceforge.plantuml.nassidiagram.NassiElement;

public class NassiConnector extends NassiElement {
    private static final int DIAMETER = 40;
    
    public NassiConnector(String text) {
        super(text);
    }

    @Override
    public void computeDimension(Graphics2D g2d) {
        dimension = new Rectangle2D.Double(0, 0, DIAMETER, DIAMETER);
    }

    @Override
    public void draw(UGraphic ug) {
        // Draw circle
        UEllipse circle = UEllipse.build(DIAMETER, DIAMETER);
        ug.draw(circle);

        // Draw text
        FontConfiguration fontConfig = FontConfiguration.blackBlueTrue(UFont.monospaced(14));
        UText txt = UText.build(text, fontConfig);
        double textX = (DIAMETER - txt.calculateDimension(ug.getStringBounder()).getWidth()) / 2;
        double textY = DIAMETER/2 + 5;
        ug.apply(new UTranslate(textX, textY)).draw(txt);
    }
} 