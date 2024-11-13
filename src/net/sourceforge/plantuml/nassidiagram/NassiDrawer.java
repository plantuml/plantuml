package net.sourceforge.plantuml.nassidiagram;

import java.awt.geom.Dimension2D;
import java.util.List;

import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.ISkinParam;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UTranslate;

public class NassiDrawer implements TextBlock {
    private final List<NassiElement> elements;
    private final ISkinParam skinParam;

    public NassiDrawer(List<NassiElement> elements, ISkinParam skinParam) {
        this.elements = elements;
        this.skinParam = skinParam;
    }

    @Override
    public void drawU(UGraphic ug) {
        double y = 0;
        for (NassiElement element : elements) {
            element.draw(ug.apply(new UTranslate(0, y)));
            y += element.getDimension().getHeight();
        }
    }

    @Override
    public Dimension2D calculateDimension(StringBounder stringBounder) {
        double width = 0;
        double height = 0;
        for (NassiElement element : elements) {
            width = Math.max(width, element.getDimension().getWidth());
            height += element.getDimension().getHeight();
        }
        return new Dimension2DDouble(width, height);
    }
}