package net.sourceforge.plantuml.nassidiagram.element;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.klimt.shape.URectangle;
import net.sourceforge.plantuml.klimt.shape.UText;
import net.sourceforge.plantuml.klimt.UTranslate;
import net.sourceforge.plantuml.klimt.font.FontConfiguration;
import net.sourceforge.plantuml.klimt.font.UFont;
import net.sourceforge.plantuml.klimt.color.HColors;
import net.sourceforge.plantuml.klimt.geom.XDimension2D;
import net.sourceforge.plantuml.nassidiagram.NassiElement;
import java.util.List;
import java.util.ArrayList;

public class NassiWhile extends NassiElement {
    private final List<NassiElement> body = new ArrayList<>();
    private static final int HEADER_HEIGHT = 30;
    
    public NassiWhile(String condition) {
        super(condition);
    }

    public void addBodyElement(NassiElement element) {
        body.add(element);
    }

    @Override
    public void computeDimension(Graphics2D g2d) {
        double width = 0;
        double bodyHeight = 0;
        
        for (NassiElement element : body) {
            element.computeDimension(g2d);
            width = Math.max(width, element.getDimension().getWidth());
            bodyHeight += element.getDimension().getHeight();
        }
        
        width = Math.max(width, 200); // Minimum width
        dimension = new Rectangle2D.Double(0, 0, width, HEADER_HEIGHT + bodyHeight);
    }

    @Override
    public void draw(UGraphic ug) {
        // Draw condition header
        URectangle header = URectangle.build(dimension.getWidth(), HEADER_HEIGHT);
        ug.draw(header);

        // Draw condition text
        FontConfiguration fontConfig = FontConfiguration.blackBlueTrue(UFont.monospaced(14));
        UText conditionText = UText.build("while " + text, fontConfig);
        XDimension2D textDim = conditionText.calculateDimension(ug.getStringBounder());
        double textX = (dimension.getWidth() - textDim.getWidth()) / 2;
        double textY = HEADER_HEIGHT/2 + textDim.getHeight()/3;
        ug.apply(new UTranslate(textX, textY)).draw(conditionText);

        // Draw body
        double y = HEADER_HEIGHT;
        for (NassiElement element : body) {
            element.draw(ug.apply(UTranslate.dy(y)));
            y += element.getDimension().getHeight();
        }
    }
} 