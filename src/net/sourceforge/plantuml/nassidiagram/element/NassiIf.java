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

        double branchWidth = Math.max(MIN_WIDTH/2, 
            Math.max(thenBranch.getDimension().getWidth(), 
                    elseBranch.getDimension().getWidth()));
        
        double totalWidth = branchWidth * 2;
        double branchHeight = Math.max(thenBranch.getDimension().getHeight(),
                                     elseBranch.getDimension().getHeight());
        
        dimension = new Rectangle2D.Double(0, 0, totalWidth, HEADER_HEIGHT + branchHeight);
    }

    @Override
    public void draw(UGraphic ug) {
        // Draw main container
        URectangle mainRect = URectangle.build(dimension.getWidth(), dimension.getHeight());
        ug.apply(HColors.WHITE.bg()).apply(HColors.BLACK).draw(mainRect);

        // Draw condition header
        UPolygon header = new UPolygon();
        header.addPoint(0, 0);
        header.addPoint(dimension.getWidth(), 0);
        header.addPoint(dimension.getWidth(), HEADER_HEIGHT);
        header.addPoint(0, HEADER_HEIGHT);
        ug.apply(HColors.WHITE.bg()).draw(header);

        // Draw condition text
        FontConfiguration fontConfig = FontConfiguration.blackBlueTrue(UFont.sansSerif(12));
        UText conditionText = UText.build(text, fontConfig);
        XDimension2D textDim = conditionText.calculateDimension(ug.getStringBounder());
        double textX = (dimension.getWidth() - textDim.getWidth()) / 2;
        double textY = HEADER_HEIGHT/2 + textDim.getHeight()/3;
        ug.apply(new UTranslate(textX, textY)).draw(conditionText);

        // Draw vertical divider
        double branchWidth = dimension.getWidth()/2;
        ug.apply(new UTranslate(branchWidth, HEADER_HEIGHT))
          .apply(HColors.BLACK)
          .draw(new ULine(0, dimension.getHeight() - HEADER_HEIGHT));

        // Draw True/False labels
        UText trueLabel = UText.build("True", fontConfig);
        UText falseLabel = UText.build("False", fontConfig);
        ug.apply(new UTranslate(branchWidth/4, HEADER_HEIGHT + 15)).draw(trueLabel);
        ug.apply(new UTranslate(branchWidth*1.25, HEADER_HEIGHT + 15)).draw(falseLabel);

        // Draw branches directly below header
        double y = HEADER_HEIGHT;
        thenBranch.draw(ug.apply(new UTranslate(0, y)));
        elseBranch.draw(ug.apply(new UTranslate(branchWidth, y)));
    }
}