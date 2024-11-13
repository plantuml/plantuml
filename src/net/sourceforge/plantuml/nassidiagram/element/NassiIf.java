package net.sourceforge.plantuml.nassidiagram.element;

import net.sourceforge.plantuml.nassidiagram.NassiElement;

public class NassiIf extends NassiElement {
    private final NassiElement thenBranch;
    private final NassiElement elseBranch;

    public NassiIf(String condition, NassiElement thenBranch, NassiElement elseBranch) {
        super(condition);
        this.thenBranch = thenBranch;
        this.elseBranch = elseBranch;
    }

    @Override
    public void computeDimension(Graphics2D g2d) {
        // Calculate dimensions considering both branches
        thenBranch.computeDimension(g2d);
        elseBranch.computeDimension(g2d);

        double width = Math.max(thenBranch.getDimension().getWidth(),
                              elseBranch.getDimension().getWidth()) * 2;
        double height = Math.max(thenBranch.getDimension().getHeight(),
                               elseBranch.getDimension().getHeight()) + 30;

        dimension = new Rectangle2D.Double(0, 0, width, height);
    }

    @Override
    public void draw(UGraphic ug) {
        // Draw condition box
        // Draw diagonal separator
        // Draw then branch
        // Draw else branch
    }
}