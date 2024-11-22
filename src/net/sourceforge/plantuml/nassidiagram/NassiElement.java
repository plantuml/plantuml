package net.sourceforge.plantuml.nassidiagram;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.image.BufferedImage;
import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.nassidiagram.util.NassiDrawingUtil;

public abstract class NassiElement {
    protected String text;
    protected Rectangle2D dimension;
    protected NassiElement parent;
    protected double forcedWidth = -1;
    protected double minWidth = NassiDrawingUtil.MIN_WIDTH;
    protected double padding = NassiDrawingUtil.PADDING;
    protected static final Font DEFAULT_FONT = new Font("SansSerif", Font.PLAIN, 12);
    protected static final Font BOLD_FONT = new Font("SansSerif", Font.BOLD, 12);

    public NassiElement(String text) {
        this.text = text;
    }

    public abstract void computeDimension(Graphics2D g2d);
    public abstract void draw(UGraphic ug);

    public Rectangle2D getDimension() {
        if (dimension == null) {
            computeDimension(createTemporaryGraphics());
        }
        return dimension;
    }

    public void setParent(NassiElement parent) {
        this.parent = parent;
        // Recompute dimensions when parent changes
        if (dimension != null) {
            computeDimension(createTemporaryGraphics());
        }
    }

    public NassiElement getParent() {
        return parent;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
        // Recompute dimensions when text changes
        if (dimension != null) {
            computeDimension(createTemporaryGraphics());
        }
    }

    public void setWidth(double width) {
        if (this.forcedWidth != width) {
            this.forcedWidth = width;
            // Recompute dimensions when width changes
            if (dimension != null) {
                computeDimension(createTemporaryGraphics());
            }
        }
    }

    public double getNestedWidth() {
        return dimension != null ? dimension.getWidth() : 0;
    }

    protected NassiElement getRoot() {
        NassiElement current = this;
        while (current.getParent() != null) {
            current = current.getParent();
        }
        return current;
    }

    protected double getEffectiveWidth() {
        if (forcedWidth > 0) {
            return forcedWidth;
        }
        return dimension != null ? Math.max(minWidth, dimension.getWidth()) : minWidth;
    }

    protected Graphics2D createTemporaryGraphics() {
        BufferedImage img = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = img.createGraphics();
        g2d.setFont(DEFAULT_FONT);
        return g2d;
    }

    protected double calculateTextWidth(Graphics2D g2d, String prefix) {
        FontMetrics fm = g2d.getFontMetrics();
        String displayText = prefix != null ? prefix + text : text;
        return fm.stringWidth(displayText) + 2 * padding;
    }

    protected double getTextHeight(Graphics2D g2d) {
        return g2d.getFontMetrics().getHeight() + 2 * padding;
    }

    protected void setMinWidth(double width) {
        if (this.minWidth != width) {
            this.minWidth = width;
            // Recompute dimensions when minimum width changes
            if (dimension != null) {
                computeDimension(createTemporaryGraphics());
            }
        }
    }

    protected boolean isInsideParentType(Class<?> parentType) {
        NassiElement current = getParent();
        while (current != null) {
            if (parentType.isInstance(current)) {
                return true;
            }
            current = current.getParent();
        }
        return false;
    }

    protected double getMaxChildWidth() {
        return getEffectiveWidth();
    }

    protected void invalidateDimensions() {
        dimension = null;
    }

    protected boolean isRoot() {
        return parent == null;
    }

    protected double getParentWidth() {
        return parent != null ? parent.getEffectiveWidth() : getEffectiveWidth();
    }

    protected double calculateMinimumWidth(Graphics2D g2d) {
        return Math.max(calculateTextWidth(g2d, null), minWidth);
    }

    protected void validateDimensions() {
        if (dimension == null) {
            computeDimension(createTemporaryGraphics());
        }
    }

    protected boolean shouldRecomputeDimensions() {
        return dimension == null || 
               (forcedWidth > 0 && forcedWidth != dimension.getWidth());
    }

    protected void propagateWidthChange() {
        NassiElement current = this;
        while (current != null) {
            current.invalidateDimensions();
            current = current.getParent();
        }
    }
}