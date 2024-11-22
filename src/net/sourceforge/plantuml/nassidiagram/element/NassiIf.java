package net.sourceforge.plantuml.nassidiagram.element;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.klimt.UTranslate;
import net.sourceforge.plantuml.klimt.shape.URectangle;
import net.sourceforge.plantuml.klimt.shape.ULine;
import net.sourceforge.plantuml.klimt.color.HColors;
import net.sourceforge.plantuml.nassidiagram.NassiElement;
import net.sourceforge.plantuml.nassidiagram.util.NassiDrawingUtil;
import java.util.ArrayList;
import java.util.List;

public class NassiIf extends NassiElement {
    private final List<NassiElement> thenBranch = new ArrayList<>();
    private final List<NassiElement> elseBranch = new ArrayList<>();
    private boolean inElseBranch = false;
    private static final int HEADER_HEIGHT = 40;

    public NassiIf(String condition) {
        super(condition);
    }

    public void addToCurrentBranch(NassiElement element) {
        if (inElseBranch) {
            elseBranch.add(element);
            element.setParent(this);
        } else {
            thenBranch.add(element);
            element.setParent(this);
        }
    }

    public void switchToElseBranch() {
        inElseBranch = true;
    }

    public boolean isInElseBranch() {
        return inElseBranch;
    }

    @Override
    public void computeDimension(Graphics2D g2d) {
        java.awt.FontMetrics fm = g2d.getFontMetrics();
        // Calculate minimum width based on condition text
        double contentWidth = fm.stringWidth(text) + 4 * NassiDrawingUtil.PADDING;
        double width = Math.max(NassiDrawingUtil.MIN_WIDTH, contentWidth);
        
        // Use forced width if set
        if (forcedWidth > 0) {
            width = forcedWidth;
        }

        double thenHeight = 0;
        double elseHeight = 0;

        // Compute branch dimensions
        for (NassiElement element : thenBranch) {
            element.setWidth(width / 2);  // Each branch gets half the width
            element.computeDimension(g2d);
            thenHeight += element.getDimension().getHeight();
        }

        for (NassiElement element : elseBranch) {
            element.setWidth(width / 2);  // Each branch gets half the width
            element.computeDimension(g2d);
            elseHeight += element.getDimension().getHeight();
        }

        double branchHeight = Math.max(thenHeight, elseHeight);
        dimension = new Rectangle2D.Double(0, 0, width, HEADER_HEIGHT + branchHeight);
    }

    @Override
    public double getNestedWidth() {
        double maxWidth = getEffectiveWidth();
        
        // Check width requirements of nested elements
        for (NassiElement element : thenBranch) {
            maxWidth = Math.max(maxWidth, element.getNestedWidth() * 2); // Multiply by 2 because of split
        }
        for (NassiElement element : elseBranch) {
            maxWidth = Math.max(maxWidth, element.getNestedWidth() * 2);
        }
        
        return maxWidth;
    }

    @Override
    public void draw(UGraphic ug) {
        double width = dimension.getWidth();
        double height = dimension.getHeight();

        // Draw main rectangle
        URectangle rect = URectangle.build(width, height);
        ug.apply(HColors.WHITE.bg())
          .apply(HColors.BLACK)
          .draw(rect);

        // Draw diagonal line for condition
        ULine diagonal = new ULine(width, HEADER_HEIGHT);
        ug.apply(new UTranslate(0, 0))
          .apply(HColors.BLACK)
          .draw(diagonal);

        // Draw condition text
        drawConditionText(ug, text, 0, 0, width, HEADER_HEIGHT);

        // Draw vertical divider
        double branchWidth = width / 2;
        ULine verticalDivider = new ULine(0, height - HEADER_HEIGHT);
        ug.apply(new UTranslate(branchWidth, HEADER_HEIGHT))
          .apply(HColors.BLACK)
          .draw(verticalDivider);

        // Draw then branch
        double yThen = HEADER_HEIGHT;
        for (NassiElement element : thenBranch) {
            element.draw(ug.apply(new UTranslate(0, yThen)));
            yThen += element.getDimension().getHeight();
        }

        // Draw else branch
        double yElse = HEADER_HEIGHT;
        for (NassiElement element : elseBranch) {
            element.draw(ug.apply(new UTranslate(branchWidth, yElse)));
            yElse += element.getDimension().getHeight();
        }

        // Draw True/False labels
        drawBranchLabels(ug, 0, HEADER_HEIGHT/2, width);
    }

    private void drawConditionText(UGraphic ug, String text, double x, double y, double width, double height) {
        NassiDrawingUtil.drawText(ug, x + 10, y + height/2, text);
    }

    private void drawBranchLabels(UGraphic ug, double x, double y, double width) {
        double branchWidth = width / 2;
        NassiDrawingUtil.drawText(ug, x + branchWidth/2 - 15, y + 10, "True");
        NassiDrawingUtil.drawText(ug, x + branchWidth + branchWidth/2 - 15, y + 10, "False");
    }
}