package net.sourceforge.plantuml.nassidiagram.element;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.klimt.UTranslate;
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

    @Override
    public void computeDimension(Graphics2D g2d) {
        double thenWidth = 0;
        double elseWidth = 0;
        double thenHeight = 0;
        double elseHeight = 0;

        // Compute then branch dimensions
        for (NassiElement element : thenBranch) {
            element.computeDimension(g2d);
            thenWidth = Math.max(thenWidth, element.getDimension().getWidth());
            thenHeight += element.getDimension().getHeight();
        }

        // Compute else branch dimensions
        for (NassiElement element : elseBranch) {
            element.computeDimension(g2d);
            elseWidth = Math.max(elseWidth, element.getDimension().getWidth());
            elseHeight += element.getDimension().getHeight();
        }

        double branchWidth = Math.max(NassiDrawingUtil.MIN_WIDTH/2, 
            Math.max(thenWidth, elseWidth));
        double totalWidth = branchWidth * 2;
        double branchHeight = Math.max(thenHeight, elseHeight);
        
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
    }
}