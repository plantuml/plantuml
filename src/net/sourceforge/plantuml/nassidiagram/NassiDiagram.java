package net.sourceforge.plantuml.nassidiagram;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.UmlDiagram;
import net.sourceforge.plantuml.core.DiagramDescription;
import net.sourceforge.plantuml.core.ImageData;
import net.sourceforge.plantuml.core.UmlSource;
import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.klimt.UTranslate;
import net.sourceforge.plantuml.klimt.color.HColors;
import net.sourceforge.plantuml.skin.UmlDiagramType;
import net.sourceforge.plantuml.nassidiagram.util.NassiDrawingUtil;
import net.sourceforge.plantuml.klimt.creole.Display;
import net.sourceforge.plantuml.klimt.shape.TextBlock;
import net.sourceforge.plantuml.klimt.shape.AbstractTextBlock;
import net.sourceforge.plantuml.klimt.font.FontConfiguration;
import net.sourceforge.plantuml.klimt.font.UFont;
import net.sourceforge.plantuml.klimt.geom.HorizontalAlignment;
import net.sourceforge.plantuml.klimt.geom.XDimension2D;
import net.sourceforge.plantuml.nassidiagram.element.NassiIf;
import net.sourceforge.plantuml.nassidiagram.element.NassiWhile;
import net.sourceforge.plantuml.klimt.font.StringBounder;

public class NassiDiagram extends UmlDiagram {
    private final List<NassiElement> elements = new ArrayList<>();
    private NassiElement currentControlStructure = null;
    private static final int PADDING = 20;
    private boolean dimensionsComputed = false;

    public NassiDiagram(UmlSource source, Map<String, String> skinParam) {
        super(source, UmlDiagramType.NASSI, skinParam);
    }

    private void computeAllDimensions(Graphics2D g2d) {
        if (!dimensionsComputed) {
            for (NassiElement element : elements) {
                element.computeDimension(g2d);
            }
            dimensionsComputed = true;
        }
    }

    @Override
    protected ImageData exportDiagramInternal(OutputStream os, int index, FileFormatOption fileFormatOption)
        throws IOException {
        return createImageBuilder(fileFormatOption)
            .drawable(new AbstractTextBlock() {
                @Override
                public void drawU(UGraphic ug) {
                    drawDiagram(ug);
                }

                @Override
                public XDimension2D calculateDimension(StringBounder stringBounder) {
                    double totalWidth = PADDING * 2;
                    double totalHeight = PADDING * 2;
                    double maxWidth = 0;

                    Graphics2D g2d = createGraphics();
                    computeAllDimensions(g2d);

                    for (NassiElement element : elements) {
                        Rectangle2D dim = element.getDimension();
                        if (dim != null) {
                            maxWidth = Math.max(maxWidth, dim.getWidth());
                            totalHeight += dim.getHeight();
                        }
                    }
                    totalWidth += maxWidth;

                    return new XDimension2D(totalWidth, totalHeight);
                }
            })
            .write(os);
    }

    private void drawDiagram(UGraphic ug) {
        // Set background color
        ug = ug.apply(HColors.WHITE.bg());

        // Ensure dimensions are computed
        Graphics2D g2d = createGraphics();
        computeAllDimensions(g2d);

        // Draw elements
        double currentY = PADDING;
        double maxWidth = 0;

        // First pass: compute max width
        for (NassiElement element : elements) {
            Rectangle2D dim = element.getDimension();
            if (dim != null) {
                maxWidth = Math.max(maxWidth, dim.getWidth());
            }
        }

        // Second pass: draw elements
        for (NassiElement element : elements) {
            Rectangle2D dim = element.getDimension();
            if (dim != null) {
                double elementWidth = dim.getWidth();
                double xOffset = PADDING + (maxWidth - elementWidth) / 2;
                element.draw(ug.apply(new UTranslate(xOffset, currentY)));
                currentY += dim.getHeight();
            }
        }
    }

    private Graphics2D createGraphics() {
        return new java.awt.image.BufferedImage(1, 1, java.awt.image.BufferedImage.TYPE_INT_RGB)
            .createGraphics();
    }

    @Override
    public DiagramDescription getDescription() {
        return new DiagramDescription("Nassi-Shneiderman diagram");
    }

    @Override
    protected TextBlock getTextMainBlock(FileFormatOption fileFormatOption) {
        FontConfiguration fontConfig = FontConfiguration.blackBlueTrue(UFont.sansSerif(12));
        return Display.create("").create(fontConfig, HorizontalAlignment.LEFT, getSkinParam());
    }

    public void addElement(NassiElement element) {
        if (element instanceof NassiIf || element instanceof NassiWhile) {
            // If we're adding a new control structure
            if (currentControlStructure != null) {
                // Nest it under current control structure if one exists
                if (currentControlStructure instanceof NassiIf) {
                    ((NassiIf) currentControlStructure).addToCurrentBranch(element);
                } else if (currentControlStructure instanceof NassiWhile) {
                    ((NassiWhile) currentControlStructure).addBodyElement(element);
                }
            } else {
                // Add to main list if no current control structure
                elements.add(element);
            }
            currentControlStructure = element;
        } else {
            // For non-control structure elements
            if (currentControlStructure != null) {
                if (currentControlStructure instanceof NassiIf) {
                    ((NassiIf) currentControlStructure).addToCurrentBranch(element);
                } else if (currentControlStructure instanceof NassiWhile) {
                    ((NassiWhile) currentControlStructure).addBodyElement(element);
                }
            } else {
                elements.add(element);
            }
        }
    }

    public void endControlStructure() {
        if (currentControlStructure != null) {
            currentControlStructure = currentControlStructure.getParent();
        }
    }

    public NassiElement getCurrentControlStructure() {
        return currentControlStructure;
    }

    public NassiElement getLastElement() {
        return elements.isEmpty() ? null : elements.get(elements.size() - 1);
    }

    public void removeLastElement() {
        if (!elements.isEmpty()) {
            elements.remove(elements.size() - 1);
        }
    }
}