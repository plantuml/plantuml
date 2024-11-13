package net.sourceforge.plantuml.nassidiagram.element;

import net.sourceforge.plantuml.nassidiagram.NassiElement;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.URectangle;
import net.sourceforge.plantuml.ugraphic.UText;

public class NassiBlock extends NassiElement {
    public NassiBlock(String text) {
        super(text);
    }

    @Override
    public void computeDimension(Graphics2D g2d) {
        // Calculate text dimensions and add padding
        FontMetrics fm = g2d.getFontMetrics();
        double width = fm.stringWidth(text) + 20;
        double height = fm.getHeight() + 10;
        dimension = new Rectangle2D.Double(0, 0, width, height);
    }

    @Override
    public void draw(UGraphic ug) {
        // Draw rectangle
        ug.draw(new URectangle(dimension.getWidth(), dimension.getHeight()));
        // Draw text centered
        ug.draw(new UText(text));
    }
}