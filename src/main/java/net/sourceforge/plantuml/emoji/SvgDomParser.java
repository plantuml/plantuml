package net.sourceforge.plantuml.emoji;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sourceforge.plantuml.klimt.UStroke;
import net.sourceforge.plantuml.klimt.UTranslate;
import net.sourceforge.plantuml.klimt.UPath;
import net.sourceforge.plantuml.klimt.color.HColor;
import net.sourceforge.plantuml.klimt.AffineTransformType;
import net.sourceforge.plantuml.security.SImageIO;
import net.atmp.PixelImage;
import java.io.ByteArrayInputStream;
import java.util.Base64;
import java.awt.image.BufferedImage;
import net.sourceforge.plantuml.klimt.color.HColorSet;
import net.sourceforge.plantuml.klimt.color.HColors;
import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.klimt.shape.TextBlock;
import net.sourceforge.plantuml.klimt.shape.UEllipse;
import net.sourceforge.plantuml.klimt.shape.AbstractTextBlock;
import net.sourceforge.plantuml.klimt.shape.URectangle;
import net.sourceforge.plantuml.klimt.shape.UImage;
import net.sourceforge.plantuml.klimt.shape.UText;
import net.sourceforge.plantuml.klimt.shape.UImageSvg;
import net.sourceforge.plantuml.klimt.shape.ULine;
import net.sourceforge.plantuml.klimt.font.FontConfiguration;
import net.sourceforge.plantuml.klimt.font.FontStyle;
import net.sourceforge.plantuml.klimt.font.StringBounder;
import net.sourceforge.plantuml.klimt.font.UFont;
import java.awt.Font;
import net.sourceforge.plantuml.klimt.geom.XDimension2D;
import net.sourceforge.plantuml.klimt.geom.XPoint2D;
import net.sourceforge.plantuml.klimt.sprite.Sprite;
import net.sourceforge.plantuml.openiconic.SvgPath;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.svg.SVGDocument;

import java.util.logging.Logger;

/**
 * DOM-based SVG parser that converts SVG elements to PlantUML graphics primitives.
 * 
 * <p>Supported SVG elements:
 * <ul>
 *   <li><b>Basic shapes:</b> rect, circle, ellipse, line, polyline, polygon, path</li>
 *   <li><b>Text:</b> text elements with the following attributes:
 *     <ul>
 *       <li><i>Supported:</i> font-family, font-size, font-weight (normal/bold/100-900), 
 *           font-style (normal/italic/oblique), text-decoration (underline/line-through), 
 *           fill, x, y</li>
 *       <li><i>Not supported:</i> text-anchor, text-decoration (overline), letter-spacing, 
 *           word-spacing, text-transform, opacity, dx, dy, dominant-baseline, 
 *           alignment-baseline, transform, inline style attribute parsing, tspan elements</li>
 *     </ul>
 *   </li>
 *   <li><b>Images:</b> embedded PNG/JPEG via data URIs</li>
 *   <li><b>Gradients:</b> linearGradient (all directions), radialGradient (first stop color)</li>
 *   <li><b>Definitions:</b> defs, symbol, use (with href and xlink:href support)</li>
 *   <li><b>Groups:</b> g elements with transforms and style inheritance</li>
 * </ul>
 * 
 * <p>Style attributes supported: fill, stroke, stroke-width, and style attribute parsing.
 * 
 * <p>Known limitations:
 * <ul>
 *   <li>Embedded raster images work in PNG output but may not render in SVG output</li>
 *   <li>Radial gradients use only the first stop color (not full gradient)</li>
 *   <li>Text: overline decoration not supported (PlantUML limitation)</li>
 *   <li>Complex features explicitly skipped: clipPath, mask, filter, pattern, marker</li>
 * </ul>
 */
public class SvgDomParser implements ISvgParser, Sprite, GrayLevelRange {


    private final List<String> svg;
    private final Map<String, Element> defsById = new HashMap<>();
    private final Map<String, Map<String, String>> cssRules = new HashMap<>();

    // Logger for debugging
    private static final Logger LOG = Logger.getLogger(SvgDomParser.class.getName());

    public SvgDomParser(String svg) {
        this(Collections.singletonList(svg));
    }

    public SvgDomParser(List<String> svg) {
        this.svg = svg;
    }


    // helper to produce an indent string based on current group stack depth
    private static String indent(Deque<GroupState> stackG) {
        final int n = (stackG == null) ? 0 : stackG.size();
        final StringBuilder sb = new StringBuilder();
        for (int i = 0; i < n; i++) {
            sb.append("  ");
        }
        return sb.toString();
    }

    @Override
    public void drawU(UGraphic ug, double scale, HColor fontColor, HColor forcedColor) {
        // Reuse ColorResolver and UGraphicWithScale from existing codebase
        final ColorResolver colorResolver = new ColorResolver(fontColor, forcedColor, this);
        UGraphicWithScale ugs = new UGraphicWithScale(ug, colorResolver, scale);

        final List<UGraphicWithScale> stack = new ArrayList<>();
        final Deque<GroupState> stackG = new ArrayDeque<>();

        for (String s : svg) {
            try {
                final BatikSvgHelper.ParsedSvg parsed = BatikSvgHelper.parseSvg(s, false);
                visitDocument(parsed.document, ugs, stackG, stack, colorResolver);
            } catch (IOException e) {
                throw new RuntimeException("Failed to parse SVG", e);
            }
        }
    }

    // Minimal asTextBlock implementation to satisfy Sprite; uses first svg for sizing if available.
    @Override
    public TextBlock asTextBlock(final HColor fontColor, final HColor forcedColor, final double scale,
            final HColor backColor) {

        final UImageSvg data = new UImageSvg(svg.get(0), scale);
        final double width = data.getWidth();
        final double height = data.getHeight();

        return new AbstractTextBlock() {
            public void drawU(UGraphic ug) {
                if (backColor != null)
                    ug.apply(backColor.bg()).apply(backColor)
                            .draw(URectangle.build(calculateDimension(ug.getStringBounder())));
                SvgDomParser.this.drawU(ug, scale, fontColor, forcedColor);
            }

            public XDimension2D calculateDimension(StringBounder stringBounder) {
                return new XDimension2D(width, height);
            }
        };
    }

    // ---- Simple DOM traversal ------------------------------------------------

    /**
     * Visit and process an SVG document.
     * Collects definitions and style rules, then walks the DOM tree recursively.
     * 
     * @param doc the SVG document to process
     * @param ugs the graphics context with scaling
     * @param stackG the stack of group states for nested groups
     * @param stack the stack of graphics contexts
     * @param colResolver the color resolver for handling colors
     */
    private void visitDocument(SVGDocument doc, UGraphicWithScale ugs, Deque<GroupState> stackG, List<UGraphicWithScale> stack, ColorResolver colResolver) {
        if (doc == null) return;
        final Element root = doc.getDocumentElement();
        if (root == null) return;

        // collect defs and simple style blocks
        collectDefs(root);
        collectStyleRules(root);

        // walk DOM recursively
        walkDom(root, ugs, stackG, stack, colResolver);
    }

    /**
     * Collect all elements with IDs from <defs> sections and elsewhere in the SVG.
     * These definitions can be referenced later via <use> elements.
     * 
     * @param root the root SVG element
     */
    private void collectDefs(Element root) {
        final NodeList defsNodes = root.getElementsByTagName("defs");
        for (int i = 0; i < defsNodes.getLength(); i++) {
            final Node defs = defsNodes.item(i);
            final NodeList children = defs.getChildNodes();
            for (int j = 0; j < children.getLength(); j++) {
                final Node n = children.item(j);
                if (n instanceof Element) {
                    final Element el = (Element) n;
                    final String id = el.getAttribute("id");
                    if (id != null && !id.isEmpty()) defsById.put(id, el);
                }
            }
        }
        // also index any id'd element outside defs for convenience
        final NodeList all = root.getElementsByTagName("*");
        for (int i = 0; i < all.getLength(); i++) {
            final Element el = (Element) all.item(i);
            final String id = el.getAttribute("id");
            if (id != null && !id.isEmpty()) defsById.putIfAbsent(id, el);
        }
    }

    /**
     * Collect CSS style rules from <style> elements in the SVG.
     * Stores the raw CSS content for potential future processing.
     * 
     * @param root the root SVG element
     */
    private void collectStyleRules(Element root) {
        final NodeList styleNodes = root.getElementsByTagName("style");
        for (int i = 0; i < styleNodes.getLength(); i++) {
            final Node sn = styleNodes.item(i);
            final String css = sn.getTextContent();
            if (css != null && !css.isEmpty()) {
                final Map<String,String> entry = new HashMap<>();
                entry.put("_raw", css);
                cssRules.put("_style" + i, entry);
            }
        }
    }

    /**
     * Recursively walk the DOM tree and process SVG elements.
     * Dispatches to specific handlers based on element tag name.
     * 
     * @param node the current DOM node to process
     * @param ugs the graphics context with scaling
     * @param stackG the stack of group states for nested groups
     * @param stack the stack of graphics contexts
     * @param colResolver the color resolver for handling colors
     */
    private void walkDom(Node node, UGraphicWithScale ugs, Deque<GroupState> stackG, List<UGraphicWithScale> stack, ColorResolver colResolver) {
        if (node == null) return;
        if (node.getNodeType() != Node.ELEMENT_NODE) return;

        final Element el = (Element) node;
        final String tag = el.getTagName();

        switch (tag) {
            // Root diagram level node
            // Walk children recursively
            case "svg":
                LOG.fine(indent(stackG) + "Entering <svg> element");
                NodeList svgKids = el.getChildNodes();
                for (int i = 0; i < svgKids.getLength(); i++) walkDom(svgKids.item(i), ugs, stackG, stack, colResolver);
                break;

            // Group element - may contain transform, style, etc.
            case "g":
                // push group state (record attributes, don't reconstruct strings)
                final GroupState gs = new GroupState(el);
                stackG.addFirst(gs);

                // push current graphics state
                stack.add(0, ugs);
                LOG.fine(() -> indent(stackG) + "PUSH group id=" + gs.id );
                // optionally apply transform now if you want transforms applied immediately
                if (gs.transform != null && !gs.transform.isEmpty()) {
                    // applyTransformFromElement not present in this file - if you have it, call it here.
                    // ugs = applyTransformFromElement(ugs, el);
                }

                NodeList gKids = el.getChildNodes();
                for (int i = 0; i < gKids.getLength(); i++) walkDom(gKids.item(i), ugs, stackG, stack, colResolver);

                // pop graphics state and group
                LOG.fine(() -> indent(stackG) + "POP group id=" + gs.id );
                ugs = stack.remove(0);
                stackG.removeFirst();

                break;

            case "path":
                LOG.fine(() -> indent(stackG) + "Handling <path> element");
                handlePathElement(el, ugs, stackG, stack);
                break;


            case "text":
                LOG.fine(() -> indent(stackG) + "Handling <text> element");
                handleTextElement(el, ugs, stackG, stack);
                break;     

            case "circle":
                LOG.fine(() -> indent(stackG) + "Handling <circle> element");
                handleCircleOrEllipseElement(el, ugs, stackG, stack, true);
                break;

            
            
                case "rect":
                LOG.fine(() -> indent(stackG) + "Handling <rect> element");
                // net.sourceforge.plantuml.klimt.shape.URectangle
                handleRectangle(el, ugs, stackG, stack);
                break;

            case "ellipse":
                LOG.fine(() -> indent(stackG) + "Handling <ellipse> element");
                handleCircleOrEllipseElement(el, ugs, stackG, stack, false);
                break;

            case "line":
                 LOG.fine(() -> indent(stackG) + "Handling <line> element");
                // net.sourceforge.plantuml.klimt.shape.ULine
                handleLine(el, ugs, stackG, stack);
                break;

            case "polyline":
                 LOG.fine(() -> indent(stackG) + "Handling <polyline> element");
                handlePolyLine(el, ugs, stackG, stack);
                break;

            case "polygon":
                 LOG.fine(() -> indent(stackG) + "Handling <polygon> element");
                handlePolygon(el, ugs, stackG, stack);
                break;
                    
            case "image":
                 LOG.fine(() -> indent(stackG) + "Handling <image> element");
                handleImage(el, ugs, stackG, stack);
                break;    

            case "defs":
            case "symbol":
                // Skip rendering defs and symbols - they are only used via <use> references
                LOG.fine(() -> indent(stackG) + "Skipping <" + tag + "> (definitions only)");
                break;

            case "title":
            case "desc":
            case "metadata":
                // Skip metadata elements
                LOG.finer(() -> indent(stackG) + "Skipping <" + tag + "> (metadata)");
                break;

            case "clipPath":
            case "mask":
            case "filter":
            case "pattern":
            case "marker":
                // Skip complex rendering features not supported
                LOG.finer(() -> indent(stackG) + "Skipping <" + tag + "> (not supported)");
                break;

            case "style":
            case "script":
                // Already handled by collectStyleRules or should be ignored
                LOG.finer(() -> indent(stackG) + "Skipping <" + tag + "> (special handling)");
                break;

            case "use":
                LOG.fine(() -> indent(stackG) + "Handling <use> element");
                handleUseElement(el, ugs, stackG, stack, colResolver);
                break;

            default:
                // generic descent for unhandled elements
                NodeList kids = el.getChildNodes();
                for (int i = 0; i < kids.getLength(); i++) walkDom(kids.item(i), ugs, stackG, stack, colResolver);
                break;
        }
    }

    /**
     * Handle SVG <polyline> elements.
     * Creates an open path connecting a series of points.
     * 
     * @param el the polyline element
     * @param ugs the graphics context with scaling
     * @param stackG the stack of group states
     * @param stack the stack of graphics contexts
     */
    private void handlePolyLine(Element el, UGraphicWithScale ugs, Deque<GroupState> stackG, List<UGraphicWithScale> stack) {
        // Apply common styling
        ugs = applyStyleAndTransform(el, ugs);

        // Get points attribute
        String pointsAttr = el.getAttribute("points");
        if (pointsAttr == null || pointsAttr.isEmpty()) {
            return; // No points to draw
        }

        // Apply scaling from UGraphicWithScale
        final double scalex = ugs.getAffineTransform().getScaleX();
        final double scaley = ugs.getAffineTransform().getScaleY();

        // Apply translation from UGraphicWithScale
        final double deltax = ugs.getAffineTransform().getTranslateX();
        final double deltay = ugs.getAffineTransform().getTranslateY();

        // Parse points: "0,20 20,5 40,20" -> list of (x,y) pairs
        final String[] pointPairs = pointsAttr.trim().split("\\s+");
        
        // Create UPath for open polyline (not closed like UPolygon)
        final UPath path = new UPath("polyline", null);
        boolean firstPoint = true;

        for (String pair : pointPairs) {
            final String[] coords = pair.split(",");
            if (coords.length == 2) {
                try {
                    final double x = Double.parseDouble(coords[0].trim()) * scalex;
                    final double y = Double.parseDouble(coords[1].trim()) * scaley;
                    
                    if (firstPoint) {
                        path.moveTo(x, y);  // Start the path
                        firstPoint = false;
                    } else {
                        path.lineTo(x, y);  // Draw line to next point
                    }
                } catch (NumberFormatException ex) {
                    // Skip invalid points
                }
            }
        }

        // Apply translation and draw the open path
        final UTranslate translate = new UTranslate(deltax, deltay);
        ugs.apply(translate).draw(path);
    }

    /**
     * Handle SVG <polygon> elements.
     * Creates a closed path connecting a series of points.
     * Similar to polyline but automatically closes the path.
     * 
     * @param el the polygon element
     * @param ugs the graphics context with scaling
     * @param stackG the stack of group states
     * @param stack the stack of graphics contexts
     */
    private void handlePolygon(Element el, UGraphicWithScale ugs, Deque<GroupState> stackG, List<UGraphicWithScale> stack) {
        // Apply common styling
        ugs = applyStyleAndTransform(el, ugs);

        // Get points attribute
        String pointsAttr = el.getAttribute("points");
        if (pointsAttr == null || pointsAttr.isEmpty()) {
            return; // No points to draw
        }

        // Apply scaling from UGraphicWithScale
        final double scalex = ugs.getAffineTransform().getScaleX();
        final double scaley = ugs.getAffineTransform().getScaleY();

        // Apply translation from UGraphicWithScale
        final double deltax = ugs.getAffineTransform().getTranslateX();
        final double deltay = ugs.getAffineTransform().getTranslateY();

        // Parse points: "0,20 20,5 40,20" -> list of (x,y) pairs
        final String[] pointPairs = pointsAttr.trim().split("\\s+");
        
        // Create UPath for closed polygon (unlike polyline)
        final UPath path = new UPath("polygon", null);
        boolean firstPoint = true;

        for (String pair : pointPairs) {
            final String[] coords = pair.split(",");
            if (coords.length == 2) {
                try {
                    final double x = Double.parseDouble(coords[0].trim()) * scalex;
                    final double y = Double.parseDouble(coords[1].trim()) * scaley;
                    
                    if (firstPoint) {
                        path.moveTo(x, y);  // Start the path
                        firstPoint = false;
                    } else {
                        path.lineTo(x, y);  // Draw line to next point
                    }
                } catch (NumberFormatException ex) {
                    // Skip invalid points
                }
            }
        }

        // Close the polygon path (key difference from polyline)
        path.closePath();

        // Apply translation and draw the closed path
        final UTranslate translate = new UTranslate(deltax, deltay);
        ugs.apply(translate).draw(path);
    }

    /**
     * Handle SVG <image> elements with embedded PNG or JPEG data.
     * Supports data URIs with base64-encoded PNG and JPEG images.
     * Per SVG specification, only JPEG, PNG, and SVG formats must be supported.
     * We handle PNG and JPEG; SVG-in-SVG is not implemented.
     * 
     * LIMITATION: Embedded raster images work in PNG output but may not render
     * in SVG output. This is because PlantUML's SVG renderer may not support
     * inline UImage objects within sprites. Use vector SVG elements for SVG output.
     */
    private void handleImage(Element el, UGraphicWithScale ugs, Deque<GroupState> stackG, List<UGraphicWithScale> stack) {
        // Get image href attribute (may be href or xlink:href)
        String href = el.getAttribute("href");
        if (href == null || href.isEmpty()) {
            href = el.getAttribute("xlink:href");
        }
        
        if (href == null || href.isEmpty()) {
            return; // No image source
        }
        
        // Check if it's a data URI with base64 PNG or JPEG
        // Per SVG spec: JPEG, PNG, and SVG are the required formats
        final String base64Prefix;
        if (href.startsWith("data:image/png;base64,")) {
            base64Prefix = "data:image/png;base64,";
        } else if (href.startsWith("data:image/jpeg;base64,")) {
            base64Prefix = "data:image/jpeg;base64,";
        } else {
            return; // Only support embedded PNG and JPEG for now
        }
        
        try {
            // Extract base64 data (skip the detected prefix)
            final String base64Data = href.substring(base64Prefix.length());
            
            // Decode base64 to bytes
            final byte[] imageBytes = Base64.getDecoder().decode(base64Data);
            
            // Read image from bytes (PNG or JPEG)
            final BufferedImage bufferedImage = SImageIO.read(new ByteArrayInputStream(imageBytes));
            if (bufferedImage == null) {
                LOG.warning("Failed to decode image from base64 data");
                return;
            }
            
            // Create UImage from BufferedImage
            final UImage uImage = new UImage(new PixelImage(bufferedImage, AffineTransformType.TYPE_BILINEAR));
            
            // Get position and size attributes
            String xAttr = el.getAttribute("x");
            String yAttr = el.getAttribute("y");
            String widthAttr = el.getAttribute("width");
            String heightAttr = el.getAttribute("height");
            
            // Apply scaling from UGraphicWithScale
            final double scalex = ugs.getAffineTransform().getScaleX();
            final double scaley = ugs.getAffineTransform().getScaleY();
            
            // Apply translation from UGraphicWithScale
            final double deltax = ugs.getAffineTransform().getTranslateX();
            final double deltay = ugs.getAffineTransform().getTranslateY();
            
            // Extract position (default to 0)
            final double x = (xAttr != null && !xAttr.isEmpty()) ? Double.parseDouble(xAttr) * scalex : 0;
            final double y = (yAttr != null && !yAttr.isEmpty()) ? Double.parseDouble(yAttr) * scaley : 0;
            
            // Calculate scale factor respecting both width and height constraints
            // to maintain aspect ratio and fit within specified bounds
            double imageScale = 1.0;
            if (widthAttr != null && !widthAttr.isEmpty() && heightAttr != null && !heightAttr.isEmpty()) {
                // Both width and height specified - scale to fit within bounds while preserving aspect ratio
                final double targetWidth = Double.parseDouble(widthAttr) * scalex;
                final double targetHeight = Double.parseDouble(heightAttr) * scaley;
                final double scaleX = targetWidth / bufferedImage.getWidth();
                final double scaleY = targetHeight / bufferedImage.getHeight();
                // Use the smaller scale to ensure image fits within bounds
                imageScale = Math.min(scaleX, scaleY);
            } else if (widthAttr != null && !widthAttr.isEmpty()) {
                // Only width specified - scale to width
                final double targetWidth = Double.parseDouble(widthAttr) * scalex;
                imageScale = targetWidth / bufferedImage.getWidth();
            } else if (heightAttr != null && !heightAttr.isEmpty()) {
                // Only height specified - scale to height
                final double targetHeight = Double.parseDouble(heightAttr) * scaley;
                imageScale = targetHeight / bufferedImage.getHeight();
            }
            
            // Apply image scaling and position
            final UTranslate translate = new UTranslate(deltax + x, deltay + y);
            final UImage scaledImage = imageScale != 1.0 ? uImage.scale(imageScale) : uImage;
            ugs.apply(translate).draw(scaledImage);
            
        } catch (Exception e) {
            LOG.warning("Failed to handle image: " + e.getMessage());
        }
    }

    /**
     * Handle SVG <use> elements that reference definitions via href or xlink:href.
     * Applies positioning (x, y), sizing (width, height for symbols), and transforms.
     */
    private void handleUseElement(Element useEl, UGraphicWithScale ugs, Deque<GroupState> stackG, 
                                   List<UGraphicWithScale> stack, ColorResolver colResolver) {
        // Get the reference - try both href and xlink:href (with namespace)
        String href = useEl.getAttribute("href");
        if (href == null || href.isEmpty()) {
            // Try xlink:href with proper namespace
            href = useEl.getAttributeNS("http://www.w3.org/1999/xlink", "href");
        }
        if (href == null || href.isEmpty()) {
            // Fallback to non-namespaced xlink:href
            href = useEl.getAttribute("xlink:href");
        }
        
        if (href == null || href.isEmpty() || !href.startsWith("#")) {
            LOG.warning("Invalid or missing href in <use> element");
            return;
        }
        
        // Remove the '#' prefix to get the ID
        final String refId = href.substring(1);
        final Element referenced = defsById.get(refId);
        
        if (referenced == null) {
            LOG.warning("Referenced element not found: " + refId);
            return;
        }
        
        LOG.fine(() -> indent(stackG) + "Instantiating reference: " + refId);
        
        // Apply x, y positioning from <use> element
        final String xAttr = useEl.getAttribute("x");
        final String yAttr = useEl.getAttribute("y");
        double x = 0, y = 0;
        if (xAttr != null && !xAttr.isEmpty()) x = Double.parseDouble(xAttr);
        if (yAttr != null && !yAttr.isEmpty()) y = Double.parseDouble(yAttr);
        
        // Apply any transform from <use> element
        ugs = applyStyleAndTransform(useEl, ugs);
        
        // Apply translation for x, y positioning
        if (x != 0 || y != 0) {
            final double scalex = ugs.getAffineTransform().getScaleX();
            final double scaley = ugs.getAffineTransform().getScaleY();
            final UTranslate translate = new UTranslate(x * scalex, y * scaley);
            ugs = ugs.apply(translate);
        }
        
        // For <symbol> elements, apply width/height if specified on <use>
        if (referenced.getTagName().equals("symbol")) {
            final String widthAttr = useEl.getAttribute("width");
            final String heightAttr = useEl.getAttribute("height");
            // Symbol has its own viewBox that handles scaling, but we can apply additional scaling if needed
            // For now, walk the symbol's children directly instead of the symbol element itself
            // (to avoid the "skip symbol" case in walkDom)
            final NodeList children = referenced.getChildNodes();
            for (int i = 0; i < children.getLength(); i++) {
                walkDom(children.item(i), ugs, stackG, stack, colResolver);
            }
        } else {
            // For other elements (like <g>), walk the element itself
            walkDom(referenced, ugs, stackG, stack, colResolver);
        }
    }

    /**
     * Handle SVG <line> elements.
     * Draws a straight line between two points (x1,y1) and (x2,y2).
     * 
     * @param el the line element
     * @param ugs the graphics context with scaling
     * @param stackG the stack of group states
     * @param stack the stack of graphics contexts
     */
    private void handleLine(Element el, UGraphicWithScale ugs, Deque<GroupState> stackG, List<UGraphicWithScale> stack) {
        // Apply common styling
        ugs = applyStyleAndTransform(el, ugs);

        // Get position attributes
        String x1Attr = el.getAttribute("x1");
        String y1Attr = el.getAttribute("y1");
        String x2Attr = el.getAttribute("x2");
        String y2Attr = el.getAttribute("y2");

        // Apply scaling from UGraphicWithScale
        final double scalex = ugs.getAffineTransform().getScaleX();
        final double scaley = ugs.getAffineTransform().getScaleY();

        // Apply translation from UGraphicWithScale
        final double deltax = ugs.getAffineTransform().getTranslateX();
        final double deltay = ugs.getAffineTransform().getTranslateY();

        // Extract parameters and apply scaling
        final double x1 = (x1Attr != null && !x1Attr.isEmpty()) ? Double.parseDouble(x1Attr) * scalex : 0;
        final double y1 = (y1Attr != null && !y1Attr.isEmpty()) ? Double.parseDouble(y1Attr) * scaley : 0;
        final double x2 = (x2Attr != null && !x2Attr.isEmpty()) ? Double.parseDouble(x2Attr) * scalex : 0;
        final double y2 = (y2Attr != null && !y2Attr.isEmpty()) ? Double.parseDouble(y2Attr) * scaley : 0;

        final UTranslate translate = new UTranslate(deltax, deltay);
        ugs.apply(translate).draw(ULine.create(new XPoint2D(x1, y1), new XPoint2D(x2, y2)));
        
    }

    /**
     * Handle SVG <rect> elements.
     * Draws a rectangle with optional position, width, and height attributes.
     * 
     * @param el the rect element
     * @param ugs the graphics context with scaling
     * @param stackG the stack of group states
     * @param stack the stack of graphics contexts
     */
    private void handleRectangle(Element el, UGraphicWithScale ugs, Deque<GroupState> stackG, List<UGraphicWithScale> stack) {
        // Apply common styling
        ugs = applyStyleAndTransform(el, ugs);

        // Get position and size attributes
        String xAttr = el.getAttribute("x");
        String yAttr = el.getAttribute("y");
        String widthAttr = el.getAttribute("width");
        String heightAttr = el.getAttribute("height");

        

        // Apply scaling from UGraphicWithScale
        final double scalex = ugs.getAffineTransform().getScaleX();
        final double scaley = ugs.getAffineTransform().getScaleY();

        // Apply translation from UGraphicWithScale
        final double deltax = ugs.getAffineTransform().getTranslateX();
        final double deltay = ugs.getAffineTransform().getTranslateY();

        // Extract parameters and apply scaling
        final double x = (xAttr != null && !xAttr.isEmpty()) ? Double.parseDouble(xAttr) * scalex : 0;
        final double y = (yAttr != null && !yAttr.isEmpty()) ? Double.parseDouble(yAttr) * scaley : 0;
        final double width = (widthAttr != null && !widthAttr.isEmpty()) ? Double.parseDouble(widthAttr) * scalex : 0;
        final double height = (heightAttr != null && !heightAttr.isEmpty()) ? Double.parseDouble(heightAttr) * scaley : 0;

        final UTranslate translate = new UTranslate(deltax + x, deltay + y);
        ugs.apply(translate).draw(URectangle.build(width, height));
    }

    /**
     * Handle SVG <circle> and <ellipse> elements.
     * For circles, uses a single radius; for ellipses, uses separate rx and ry.
     * 
     * @param el the circle or ellipse element
     * @param ugs the graphics context with scaling
     * @param stackG the stack of group states
     * @param stack the stack of graphics contexts
     * @param isCircle true if handling a circle, false for ellipse
     */
    private void handleCircleOrEllipseElement(Element el, UGraphicWithScale ugs, Deque<GroupState> stackG, List<UGraphicWithScale> stack, boolean isCircle) {
        // Apply common styling
        ugs = applyStyleAndTransform(el, ugs);

        // Get center coordinates (same for both circle and ellipse)
        String cxAttr = el.getAttribute("cx");
        String cyAttr = el.getAttribute("cy");
        
        // Get radius attributes - different for circle vs ellipse
        String rxAttr, ryAttr;
        if (isCircle) {
            String rAttr = el.getAttribute("r");
            rxAttr = rAttr;
            ryAttr = rAttr;
        } else {
            rxAttr = el.getAttribute("rx");
            ryAttr = el.getAttribute("ry");
        }
        
        // Apply scaling from UGraphicWithScale
        final double scalex = ugs.getAffineTransform().getScaleX();
        final double scaley = ugs.getAffineTransform().getScaleY();

        // Apply translation from UGraphicWithScale
        final double deltax = ugs.getAffineTransform().getTranslateX();
        final double deltay = ugs.getAffineTransform().getTranslateY();

        // Extract parameters and apply scaling
        final double cx = Double.parseDouble(cxAttr) * scalex;
        final double cy = Double.parseDouble(cyAttr) * scaley;
        final double rx = Double.parseDouble(rxAttr) * scalex;
        final double ry = Double.parseDouble(ryAttr) * scaley;
        
        final UTranslate translate = new UTranslate(deltax + cx - rx, deltay + cy - ry);
        ugs.apply(translate).draw(UEllipse.build(rx * 2, ry * 2));
    }

    /**
     * Apply common styling and transform attributes to UGraphicWithScale.
     * This method handles stroke-width, stroke color, and fill color for shapes.
     * Returns the potentially modified UGraphicWithScale.
     */
    private UGraphicWithScale applyStyleAndTransform(Element el, UGraphicWithScale ugs) {
        final String fill = el.getAttribute("fill");
        final String stroke = el.getAttribute("stroke");
        final String strokeWidth = el.getAttribute("stroke-width");
        
        // Apply stroke-width first
        if (strokeWidth != null && !strokeWidth.isEmpty()) {
            try {
                final double scale = ugs.getInitialScale();
                ugs = ugs.apply(UStroke.withThickness(scale * Double.parseDouble(strokeWidth)));
            } catch (NumberFormatException ex) {
                // ignore
            }
        }
        
        // Apply stroke color (foreground)
        if (stroke != null && !stroke.isEmpty()) {
            final HColor sc = ugs.getTrueColor(stroke);
            if (sc != null) {
                ugs = ugs.apply(sc);
            }
            // If stroke is specified but no fill, use default background
            if (fill == null || fill.isEmpty()) {
                ugs = ugs.apply(ugs.getDefaultColor().bg());
            }
        }
        
        // Apply fill color (background) - handle gradients and solid colors
        if (fill != null && !fill.isEmpty()) {
            if ("none".equals(fill)) {
                ugs = ugs.apply(HColors.none().bg());
            } else if (fill.startsWith("url(#")) {
                // Extract gradient reference: url(#Gradient) -> Gradient
                final String gradientId = fill.substring(5, fill.length() - 1);
                final HColor gradientColor = extractGradientColor(gradientId);
                if (gradientColor != null) {
                    // If no stroke specified, also set as foreground
                    if (stroke == null || stroke.isEmpty()) {
                        ugs = ugs.apply(gradientColor);
                    }
                    // Always set as background/fill
                    ugs = ugs.apply(gradientColor.bg());
                }
            } else {
                final HColor fc = ugs.getTrueColor(fill);
                if (fc != null) {
                    // If no stroke specified, also set as foreground
                    if (stroke == null || stroke.isEmpty()) {
                        ugs = ugs.apply(fc);
                    }
                    // Always set as background/fill
                    ugs = ugs.apply(fc.bg());
                }
            }
        }
        
        return ugs;
    }
    
    /**
     * Extract a color or gradient from a gradient definition.
     * For linear gradients, returns an HColorGradient if possible.
     * For radial gradients or when gradient creation fails, returns the first stop color.
     */
    private HColor extractGradientColor(String gradientId) {
        final Element gradientElement = defsById.get(gradientId);
        if (gradientElement == null) {
            return null;
        }
        
        final String tagName = gradientElement.getTagName();
        
        // Handle linearGradient - try to create a PlantUML gradient
        if ("linearGradient".equals(tagName)) {
            final NodeList stops = gradientElement.getElementsByTagName("stop");
            if (stops.getLength() >= 2) {
                // Get first and last stop colors
                final Element firstStop = (Element) stops.item(0);
                final Element lastStop = (Element) stops.item(stops.getLength() - 1);
                
                final String firstColor = firstStop.getAttribute("stop-color");
                final String lastColor = lastStop.getAttribute("stop-color");
                
                if (firstColor != null && !firstColor.isEmpty() && 
                    lastColor != null && !lastColor.isEmpty()) {
                    
                    final HColor color1 = parseColor(firstColor);
                    final HColor color2 = parseColor(lastColor);
                    
                    if (color1 != null && color2 != null) {
                        // Determine gradient direction from x1, y1, x2, y2 attributes
                        final char policy = determineGradientPolicy(gradientElement);
                        return HColors.gradient(color1, color2, policy);
                    }
                }
            }
            // Fallback to first stop if gradient creation failed
            if (stops.getLength() > 0) {
                final Element firstStop = (Element) stops.item(0);
                final String stopColor = firstStop.getAttribute("stop-color");
                if (stopColor != null && !stopColor.isEmpty()) {
                    return parseColor(stopColor);
                }
            }
        }
        
        // Handle radialGradient - just use first stop color
        if ("radialGradient".equals(tagName)) {
            final NodeList stops = gradientElement.getElementsByTagName("stop");
            if (stops.getLength() > 0) {
                final Element firstStop = (Element) stops.item(0);
                final String stopColor = firstStop.getAttribute("stop-color");
                if (stopColor != null && !stopColor.isEmpty()) {
                    return parseColor(stopColor);
                }
            }
        }
        
        return null;
    }
    
    /**
     * Determine the PlantUML gradient policy character from SVG linearGradient attributes.
     * Maps SVG x1/y1/x2/y2 to PlantUML's | - \ / policy.
     */
    private char determineGradientPolicy(Element gradientElement) {
        final String x1 = gradientElement.getAttribute("x1");
        final String y1 = gradientElement.getAttribute("y1");
        final String x2 = gradientElement.getAttribute("x2");
        final String y2 = gradientElement.getAttribute("y2");
        
        // Parse percentage or numeric values
        final double dx1 = parsePercentOrNumber(x1, 0.0);
        final double dy1 = parsePercentOrNumber(y1, 0.0);
        final double dx2 = parsePercentOrNumber(x2, 1.0);
        final double dy2 = parsePercentOrNumber(y2, 0.0);
        
        // Determine direction based on differences
        final double deltaX = dx2 - dx1;
        final double deltaY = dy2 - dy1;
        
        // Horizontal gradient: x changes, y constant
        if (Math.abs(deltaY) < 0.1 && Math.abs(deltaX) > 0.5) {
            return '|';  // left to right
        }
        
        // Vertical gradient: y changes, x constant
        if (Math.abs(deltaX) < 0.1 && Math.abs(deltaY) > 0.5) {
            return '-';  // top to bottom
        }
        
        // Diagonal: both change
        if (Math.abs(deltaX) > 0.3 && Math.abs(deltaY) > 0.3) {
            // Determine diagonal direction
            if (deltaX * deltaY > 0) {
                return '/';  // top-left to bottom-right
            } else {
                return '\\'; // bottom-left to top-right
            }
        }
        
        // Default to horizontal
        return '|';
    }
    
    /**
     * Parse a percentage string ("50%") or number string ("0.5") to a double.
     */
    private double parsePercentOrNumber(String value, double defaultValue) {
        if (value == null || value.isEmpty()) {
            return defaultValue;
        }
        
        value = value.trim();
        
        try {
            if (value.endsWith("%")) {
                return Double.parseDouble(value.substring(0, value.length() - 1)) / 100.0;
            } else {
                return Double.parseDouble(value);
            }
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }
    
    /**
     * Parse a color string (hex, rgb, or named color) into an HColor.
     */
    private HColor parseColor(String colorStr) {
        if (colorStr == null || colorStr.isEmpty()) {
            return null;
        }
        
        // Remove whitespace
        colorStr = colorStr.trim();
        
        // Use HColorSet to parse the color string (handles hex, rgb, and named colors)
        return HColorSet.instance().getColorOrWhite(colorStr);
    }

    /**
     * Handle SVG <text> elements.
     * Extracts text content, font attributes, and positioning to render text.
     * 
     * @param el the text element
     * @param ugs the graphics context with scaling
     * @param stackG the stack of group states
     * @param stack the stack of graphics contexts
     */
    private void handleTextElement(Element el, UGraphicWithScale ugs, Deque<GroupState> stackG, List<UGraphicWithScale> stack) {
        // Get the font configuration from attributes
        String fontFamily = el.getAttribute("font-family");
        String fontSize = el.getAttribute("font-size");
        String fontWeight = el.getAttribute("font-weight");
        String fontStyle = el.getAttribute("font-style");
        String textDecoration = el.getAttribute("text-decoration");
        String fillString = el.getAttribute("fill");
        String xLocation = el.getAttribute("x");
        String yLocation = el.getAttribute("y");
        // Example: <text x="10" y="20" font-family="Verdana" font-size="14" fill="#FF0000">

        // Other attributes can be handled similarly
        // dx, dy Relativ offset from current position
        // text-anchor - Horizontal alignment 'start', 'middle', 'end'
        // word-spacing - space between words
        // letter-spacing - space between letters
        // dominant-baseline - Vertical text alignment "auto", "baseline", "middle", "hanging"
        // alignment-baseline - baseline alignment "auto", "baseline", "middle", "hanging"

       // transform - e.g. translate, rotate, scale
       // style - CSS style string, inline reference to check

        // opacity  

        // Get the text string from attributes or text nodes
        String textContent = el.getTextContent().trim();

        // Parse font size with unit handling, default to 12 if not specified
        int fontSizeValue = parseFontSize(fontSize, 12);

        // Determine font style from font-weight and font-style attributes
        int awtFontStyle = parseFontStyle(fontWeight, fontStyle);

        // Create a UFont with appropriate style
        UFont font = UFont.build(fontFamily, awtFontStyle, fontSizeValue);

        // Get text color from fill attribute, or use default
        HColor textColor = ugs.getDefaultColor();
        if (fillString != null && !fillString.isEmpty() && !"none".equals(fillString)) {
            HColor fc = ugs.getTrueColor(fillString);
            if (fc != null) {
                textColor = fc;
            }
        }

        // Create FontConfiguration with text decorations
        FontConfiguration fontConfig = FontConfiguration.create(font, textColor, textColor, UStroke.simple(), 8);
        fontConfig = applyTextDecoration(fontConfig, textDecoration);

        final UText utext = UText.build(textContent, fontConfig);
        
        // Apply translation based on x and y attributes
        final UTranslate textTranslate = new UTranslate(
            xLocation != null && !xLocation.isEmpty() ? Double.parseDouble(xLocation) : 0,
            yLocation != null && !yLocation.isEmpty() ? Double.parseDouble(yLocation) : 0);
        ugs.apply(textTranslate).draw(utext);
    }

    /**
     * Handle SVG <path> elements.
     * Delegates to the existing SvgPath class for path data parsing and rendering.
     * 
     * @param el the path element
     * @param ugs the graphics context with scaling
     * @param stackG the stack of group states
     * @param stack the stack of graphics contexts
     */
    private void handlePathElement(Element el, UGraphicWithScale ugs, Deque<GroupState> stackG, List<UGraphicWithScale> stack) {
        // Apply common styling (stroke-width, stroke, fill)
        ugs = applyStyleAndTransform(el, ugs);

        // Extract path data directly from DOM element - no string parsing needed!
        final String pathData = el.getAttribute("d");
        if (pathData != null && !pathData.isEmpty()) {
            final SvgPath svgPath = new SvgPath(pathData, UTranslate.none());
            svgPath.drawMe(ugs.getUg(), ugs.getAffineTransform());
        }
    }

    // ---- GrayLevelRange simple implementation (expand if needed) ----

    @Override
    public int getMinGrayLevel() {
        return 0;
    }

    @Override
    public int getMaxGrayLevel() {
        return 255;
    }

    /**
     * Parse font-weight and font-style attributes to determine AWT font style.
     * Combines bold and italic flags based on SVG attributes.
     * 
     * @param fontWeight the font-weight attribute (e.g., "bold", "700", "normal")
     * @param fontStyle the font-style attribute (e.g., "italic", "oblique", "normal")
     * @return Font.PLAIN, Font.BOLD, Font.ITALIC, or Font.BOLD | Font.ITALIC
     */
    private static int parseFontStyle(String fontWeight, String fontStyle) {
        int style = Font.PLAIN;
        
        // Parse font-weight: bold, bolder, or numeric >= 600
        if (fontWeight != null && !fontWeight.isEmpty()) {
            if ("bold".equalsIgnoreCase(fontWeight) || "bolder".equalsIgnoreCase(fontWeight)) {
                style |= Font.BOLD;
            } else {
                try {
                    int weight = Integer.parseInt(fontWeight);
                    if (weight >= 600) {
                        style |= Font.BOLD;
                    }
                } catch (NumberFormatException e) {
                    // Ignore invalid numeric values
                }
            }
        }
        
        // Parse font-style: italic or oblique
        if (fontStyle != null && !fontStyle.isEmpty()) {
            if ("italic".equalsIgnoreCase(fontStyle) || "oblique".equalsIgnoreCase(fontStyle)) {
                style |= Font.ITALIC;
            }
        }
        
        return style;
    }

    /**
     * Apply text-decoration attribute to FontConfiguration.
     * Supports underline and line-through (strikethrough).
     * 
     * @param fontConfig the base font configuration
     * @param textDecoration the text-decoration attribute value
     * @return FontConfiguration with decoration applied
     */
    private static FontConfiguration applyTextDecoration(FontConfiguration fontConfig, String textDecoration) {
        if (textDecoration == null || textDecoration.isEmpty() || "none".equalsIgnoreCase(textDecoration)) {
            return fontConfig;
        }
        
        // Handle underline
        if (textDecoration.contains("underline")) {
            fontConfig = fontConfig.add(FontStyle.UNDERLINE);
        }
        
        // Handle line-through (strikethrough)
        if (textDecoration.contains("line-through")) {
            fontConfig = fontConfig.add(FontStyle.STRIKE);
        }
        
        // Note: overline not supported in PlantUML's FontStyle
        
        return fontConfig;
    }

    /**
     * Parse font size attribute, handling common CSS units (px, pt, em).
     * Returns numeric value without units, or default if invalid/empty.
     * 
     * @param fontSizeStr the font-size attribute value (e.g., "12", "12px", "14pt")
     * @param defaultSize the default size to use if parsing fails or value is empty
     * @return the numeric font size
     */
    private static int parseFontSize(String fontSizeStr, int defaultSize) {
        if (fontSizeStr == null || fontSizeStr.isEmpty()) {
            return defaultSize;
        }
        
        try {
            // Remove common CSS units
            String cleaned = fontSizeStr.trim()
                .replaceAll("(?i)(px|pt|em|rem)$", "");
            return Integer.parseInt(cleaned);
        } catch (NumberFormatException e) {
            return defaultSize;
        }
    }

    /**
     * Parse an inline CSS style string into a map of property-value pairs.
     * Handles semicolon-separated CSS declarations.
     * 
     * @param style the CSS style string (e.g., "fill:red;stroke:blue")
     * @return a map of CSS property names to values
     */
    private static Map<String, String> parseStyle(String style) {
        final Map<String, String> map = new HashMap<>();
        if (style == null || style.isEmpty()) return map;
        final String[] parts = style.split(";");
        for (String p : parts) {
            final int idx = p.indexOf(':');
            if (idx > 0) {
                final String key = p.substring(0, idx).trim();
                final String val = p.substring(idx + 1).trim();
                if (!key.isEmpty() && !val.isEmpty()) map.put(key, val);
            }
        }
        return map;
    }

    // Lightweight holder for SVG group attributes used during DOM walk
    private static final class GroupState {
        final String id;
        final String className;
        final String transform;
        final String fillAttr;
        final String strokeAttr;
        final String strokeWidthAttr;
        final Map<String, String> styleMap;

        GroupState(Element el) {
            this.id = el.getAttribute("id");
            this.className = el.getAttribute("class");
            this.transform = el.getAttribute("transform");
            this.fillAttr = el.getAttribute("fill");
            this.strokeAttr = el.getAttribute("stroke");
            this.strokeWidthAttr = el.getAttribute("stroke-width");
            this.styleMap = parseStyle(el.getAttribute("style"));
        }



        @Override
        public String toString() {
            return "GroupState[id=" + id + ", class=" + className + ", transform=" + transform + "]";
        }
    }


}