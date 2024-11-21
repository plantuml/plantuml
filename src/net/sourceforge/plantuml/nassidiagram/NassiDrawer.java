package net.sourceforge.plantuml.nassidiagram;

import java.util.List;
import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.klimt.font.StringBounder;
import net.sourceforge.plantuml.klimt.geom.XDimension2D;
import net.sourceforge.plantuml.klimt.shape.AbstractTextBlock;
import net.sourceforge.plantuml.klimt.shape.TextBlock;
import net.sourceforge.plantuml.klimt.UTranslate;
import net.sourceforge.plantuml.style.ISkinParam;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class NassiDrawer extends AbstractTextBlock implements TextBlock {
    private final List<NassiElement> elements;
    private final ISkinParam skinParam;
    private static final int DEFAULT_WIDTH = 200;
    private static final int DEFAULT_HEIGHT = 50;

    public NassiDrawer(List<NassiElement> elements, ISkinParam skinParam) {
        this.elements = elements;
        this.skinParam = skinParam;
        initializeDimensions();
    }

    private void initializeDimensions() {
        // Create a temporary graphics context to compute dimensions
        BufferedImage img = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = img.createGraphics();
        
        for (NassiElement element : elements) {
            element.computeDimension(g2d);
        }
        
        g2d.dispose();
    }

    @Override
    public void drawU(UGraphic ug) {
        double y = 0;
        for (NassiElement element : elements) {
            element.draw(ug.apply(UTranslate.dy(y)));
            y += element.getDimension().getHeight() + 10; // Add 10px spacing between elements
        }
    }

    @Override
    public XDimension2D calculateDimension(StringBounder stringBounder) {
        double width = DEFAULT_WIDTH;
        double height = 0;
        
        for (NassiElement element : elements) {
            if (element.getDimension() != null) {
                width = Math.max(width, element.getDimension().getWidth());
                height += element.getDimension().getHeight() + 10; // Add 10px spacing
            } else {
                height += DEFAULT_HEIGHT;
            }
        }
        
        return new XDimension2D(width, Math.max(height, DEFAULT_HEIGHT));
    }
}