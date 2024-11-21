package net.sourceforge.plantuml.nassidiagram.element;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.klimt.UTranslate;
import net.sourceforge.plantuml.nassidiagram.NassiElement;
import net.sourceforge.plantuml.nassidiagram.util.NassiDrawingUtil;

public class NassiIf extends NassiElement {
    private final NassiElement thenBranch;
    private final NassiElement elseBranch;
    private static final int HEADER_HEIGHT = 40;

    public NassiIf(String condition, NassiElement thenBranch, NassiElement elseBranch) {
        super(condition);
        this.thenBranch = thenBranch;
        this.elseBranch = elseBranch;
    }

    @Override
    public void computeDimension(Graphics2D g2d) {
        thenBranch.computeDimension(g2d);
        elseBranch.computeDimension(g2d);

        double branchWidth = Math.max(NassiDrawingUtil.MIN_WIDTH/2, 
            Math.max(thenBranch.getDimension().getWidth(), 
                    elseBranch.getDimension().getWidth()));
        
        double totalWidth = branchWidth * 2;
        double branchHeight = Math.max(thenBranch.getDimension().getHeight(),
                                     elseBranch.getDimension().getHeight());
        
        dimension = new Rectangle2D.Double(0, 0, totalWidth, HEADER_HEIGHT + branchHeight);
    }

    @Override
    public void draw(UGraphic ug) {
        double width = dimension.getWidth();
        double height = dimension.getHeight();
        double branchWidth = width / 2;

        // Draw condition header
        NassiDrawingUtil.drawIfHeader(ug, 0, 0, width, HEADER_HEIGHT, text);

        // Draw vertical divider
        NassiDrawingUtil.drawVerticalDivider(ug, branchWidth, HEADER_HEIGHT, 
                                           height - HEADER_HEIGHT);

        // Draw branches
        thenBranch.draw(ug.apply(new UTranslate(0, HEADER_HEIGHT)));
        elseBranch.draw(ug.apply(new UTranslate(branchWidth, HEADER_HEIGHT)));
    }
}