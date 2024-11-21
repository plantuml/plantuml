package net.sourceforge.plantuml.nassidiagram.element;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.klimt.shape.URectangle;
import net.sourceforge.plantuml.klimt.shape.UPolygon;
import net.sourceforge.plantuml.klimt.shape.ULine;
import net.sourceforge.plantuml.klimt.shape.UText;
import net.sourceforge.plantuml.klimt.UTranslate;
import net.sourceforge.plantuml.klimt.font.FontConfiguration;
import net.sourceforge.plantuml.klimt.font.UFont;
import net.sourceforge.plantuml.klimt.color.HColors;
import net.sourceforge.plantuml.klimt.geom.XDimension2D;
import net.sourceforge.plantuml.nassidiagram.NassiElement;

public class NassiIf extends NassiElement {
    private final NassiElement thenBranch;
    private final NassiElement elseBranch;
    private static final int HEADER_HEIGHT = 30;
    private static final int MIN_WIDTH = 200;

    public NassiIf(String condition, NassiElement thenBranch, NassiElement elseBranch) {
        super(condition);
        this.thenBranch = thenBranch;
        this.elseBranch = elseBranch;
    }

    @Override
    public void computeDimension(Graphics2D g2d) {
        thenBranch.computeDimension(g2d);
        elseBranch.computeDimension(g2d);

        // Calculate total width and height
        double branchWidth = Math.max(MIN_WIDTH/2, 
            Math.max(thenBranch.getDimension().getWidth(), 
                    elseBranch.getDimension().getWidth()));
        
        double totalWidth = branchWidth * 2;  // Equal width for both branches
        double branchHeight = Math.max(thenBranch.getDimension().getHeight(),
                                     elseBranch.getDimension().getHeight());
        
        dimension = new Rectangle2D.Double(0, 0, totalWidth, HEADER_HEIGHT + branchHeight);
    }

    @Override
    public void draw(UGraphic ug) {
        FontConfiguration fontConfig = FontConfiguration.blackBlueTrue(UFont.monospaced(14));
        
        // Draw condition header
        URectangle header = URectangle.build(dimension.getWidth(), HEADER_HEIGHT);
        ug.draw(header);

        // Draw condition text
        UText conditionText = UText.build(text, fontConfig);
        XDimension2D textDim = conditionText.calculateDimension(ug.getStringBounder());
        double textX = (dimension.getWidth() - textDim.getWidth()) / 2;
        double textY = HEADER_HEIGHT/2 + textDim.getHeight()/3;
        ug.apply(new UTranslate(textX, textY)).draw(conditionText);

        // Draw diagonal divider line
        UPolygon divider = new UPolygon();
        divider.addPoint(dimension.getWidth()/2, HEADER_HEIGHT);
        divider.addPoint(0, dimension.getHeight());
        divider.addPoint(dimension.getWidth(), dimension.getHeight());
        divider.addPoint(dimension.getWidth()/2, HEADER_HEIGHT);
        ug.apply(HColors.none().bg()).draw(divider);

        // Draw vertical divider
        ULine verticalLine = ULine.vline(dimension.getHeight() - HEADER_HEIGHT);
        ug.apply(new UTranslate(dimension.getWidth()/2, HEADER_HEIGHT)).draw(verticalLine);

        // Draw branches
        double branchY = HEADER_HEIGHT;
        double branchWidth = dimension.getWidth()/2;
        
        // Then branch (left)
        UGraphic thenUg = ug.apply(UTranslate.dy(branchY));
        thenBranch.draw(thenUg);
        
        // Else branch (right)
        UGraphic elseUg = ug.apply(UTranslate.dy(branchY))
                           .apply(UTranslate.dx(branchWidth));
        elseBranch.draw(elseUg);
    }
}