package net.sourceforge.plantuml.emoji;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import net.sourceforge.plantuml.klimt.UStroke;
import net.sourceforge.plantuml.klimt.UTranslate;
import net.sourceforge.plantuml.klimt.color.HColor;
import net.sourceforge.plantuml.klimt.color.HColorSet;
import net.sourceforge.plantuml.klimt.color.HColors;
import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.klimt.font.FontConfiguration;
import net.sourceforge.plantuml.klimt.font.FontStyle;
import net.sourceforge.plantuml.klimt.font.StringBounder;
import net.sourceforge.plantuml.klimt.font.UFont;
import net.sourceforge.plantuml.klimt.geom.XDimension2D;
import net.sourceforge.plantuml.klimt.geom.XPoint2D;
import net.sourceforge.plantuml.klimt.shape.AbstractTextBlock;
import net.sourceforge.plantuml.klimt.shape.TextBlock;
import net.sourceforge.plantuml.klimt.shape.UEllipse;
import net.sourceforge.plantuml.klimt.shape.UImageSvg;
import net.sourceforge.plantuml.klimt.shape.ULine;
import net.sourceforge.plantuml.klimt.shape.URectangle;
import net.sourceforge.plantuml.klimt.shape.UText;
import net.sourceforge.plantuml.klimt.sprite.Sprite;
import net.sourceforge.plantuml.openiconic.SvgPath;

import java.awt.Font;

/**
 * Zero-dependency SAX-based SVG parser using only Java SDK components.
 * 
 * <p>This parser provides a zero-dependency alternative without requiring
 * Apache Batik dependencies. Uses Java's built-in SAX parser (available since Java 1.4).
 * 
 * <p><b>Feature Set (SVG 1.1 Core Subset):</b>
 * <ul>
 *   <li>Basic shapes: rect, circle, ellipse, line, polyline, polygon, path</li>
 *   <li>Text elements with font styling (family, size, weight, style, decoration)</li>
 *   <li>Transforms: translate, rotate, scale, matrix</li>
 *   <li>Gradients: linearGradient, radialGradient (first stop color)</li>
 *   <li>Groups with style inheritance</li>
 *   <li>Definitions: defs, symbol, use references</li>
 * </ul>
 * 
 * <p><b>Limitations:</b>
 * <ul>
 *   <li>Feature-frozen at SVG 1.1 core subset</li>
 *   <li>No SVG 2.0 extensions (use a full DOM-based parser for full support)</li>
 *   <li>No embedded images, clipPath, mask, filter, pattern</li>
 *   <li>Text: no tspan, overline, or advanced layout</li>
 *   <li>CSS: No &lt;style&gt; blocks or class selectors (use inline attributes only)</li>
 * </ul>
 * 
 * <p><b>Architecture:</b> Two-pass parsing
 * <ol>
 *   <li>Pass 1: Collect definitions (defs, symbols, gradients)</li>
 *   <li>Pass 2: Render elements, resolve use references, apply transforms</li>
 * </ol>
 * 
 * @see ISvgParser
 * @since 1.2026.3
 */
public class SvgSaxParser implements ISvgSpriteParser, GrayLevelRange {

    private static final Logger LOG = Logger.getLogger(SvgSaxParser.class.getName());

    private final List<String> svg;

    public SvgSaxParser(String svg) {
        this(Collections.singletonList(svg));
    }

    public SvgSaxParser(List<String> svg) {
        this.svg = svg;
    }

    @Override
    public void drawU(UGraphic ug, double scale, HColor fontColor, HColor forcedColor) {
        final ColorResolver colorResolver = new ColorResolver(fontColor, forcedColor, this);
        UGraphicWithScale ugs = new UGraphicWithScale(ug, colorResolver, scale);

        for (String s : svg) {
            try {
                parseSvg(s, ugs, colorResolver);
            } catch (Exception e) {
                LOG.warning("Failed to parse SVG: " + e.getMessage());
                throw new RuntimeException("Failed to parse SVG", e);
            }
        }
    }

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
                SvgSaxParser.this.drawU(ug, scale, fontColor, forcedColor);
            }

            public XDimension2D calculateDimension(StringBounder stringBounder) {
                return new XDimension2D(width, height);
            }
        };
    }

    @Override
    public int getMinGrayLevel() {
        return 0;
    }

    @Override
    public int getMaxGrayLevel() {
        return 255;
    }

    /**
     * Parses SVG using two-pass SAX parsing.
     */
    private void parseSvg(String svgContent, UGraphicWithScale ugs, ColorResolver colorResolver) throws Exception {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        factory.setNamespaceAware(true);
        SAXParser parser = factory.newSAXParser();

        // Pass 1: Collect definitions
        DefsCollector defsCollector = new DefsCollector();
        parser.parse(new InputSource(new StringReader(svgContent)), defsCollector);

        // Pass 2: Render elements
        RenderHandler renderer = new RenderHandler(ugs, colorResolver, defsCollector.getDefinitions());
        parser.parse(new InputSource(new StringReader(svgContent)), renderer);
    }

    /**
     * Pass 1: Collects SVG definitions (defs, symbols, gradients) by ID.
     */
    private static class DefsCollector extends DefaultHandler {
        private final Map<String, BufferedElement> definitions = new HashMap<>();
        private int depth = 0;
        private boolean inDefs = false;
        private StringBuilder currentContent = new StringBuilder();
        private String currentId = null;
        private String currentTag = null;
        private Map<String, String> currentAttrs = null;
        private BufferedElement currentElement = null;

        public Map<String, BufferedElement> getDefinitions() {
            return definitions;
        }

        @Override
        public void startElement(String uri, String localName, String qName, Attributes attrs) {
            depth++;
            
            if ("defs".equals(qName) || "symbol".equals(qName) || 
                "linearGradient".equals(qName) || "radialGradient".equals(qName)) {
                inDefs = true;
            }

            if (inDefs || "symbol".equals(qName)) {
                String id = attrs.getValue("id");
                if (id != null && !id.isEmpty()) {
                    currentId = id;
                    currentTag = qName;
                    currentContent = new StringBuilder();
                    currentAttrs = new HashMap<>();
                    // Store all attributes for gradient processing
                    for (int i = 0; i < attrs.getLength(); i++) {
                        currentAttrs.put(attrs.getQName(i), attrs.getValue(i));
                    }
                    currentElement = new BufferedElement(qName, "", currentAttrs);
                }
                
                // Handle gradient stops
                if ("stop".equals(qName) && currentElement != null) {
                    String offset = attrs.getValue("offset");
                    String stopColor = attrs.getValue("stop-color");
                    if (offset != null && stopColor != null) {
                        currentElement.stops.add(new GradientStop(offset, stopColor));
                    }
                }
                
                // Store element as buffered XML
                currentContent.append("<").append(qName);
                for (int i = 0; i < attrs.getLength(); i++) {
                    currentContent.append(" ")
                        .append(attrs.getQName(i))
                        .append("=\"")
                        .append(escapeXml(attrs.getValue(i)))
                        .append("\"");
                }
                currentContent.append(">");
            }
        }

        @Override
        public void endElement(String uri, String localName, String qName) {
            if (inDefs) {
                currentContent.append("</").append(qName).append(">");
                
                if (currentId != null && currentTag != null && currentTag.equals(qName)) {
                    if (currentElement != null) {
                        definitions.put(currentId, currentElement);
                    }
                    currentId = null;
                    currentTag = null;
                    currentAttrs = null;
                    currentElement = null;
                }
            }

            depth--;
            if (("defs".equals(qName) || "symbol".equals(qName)) && depth == 1) {
                inDefs = false;
            }
        }

        @Override
        public void characters(char[] ch, int start, int length) {
            if (inDefs && currentContent != null) {
                currentContent.append(escapeXml(new String(ch, start, length)));
            }
        }

        private String escapeXml(String text) {
            return text.replace("&", "&amp;")
                      .replace("<", "&lt;")
                      .replace(">", "&gt;")
                      .replace("\"", "&quot;");
        }
    }

    /**
     * Buffered SVG element for definitions.
     */
    private static class BufferedElement {
        final String tagName;
        final String xmlContent;
        final Map<String, String> attributes;
        final List<GradientStop> stops;

        BufferedElement(String tagName, String xmlContent, Map<String, String> attributes) {
            this.tagName = tagName;
            this.xmlContent = xmlContent;
            this.attributes = attributes;
            this.stops = new ArrayList<>();
        }
    }

    /**
     * Gradient stop color information.
     */
    private static class GradientStop {
        final String offset;
        final String color;

        GradientStop(String offset, String color) {
            this.offset = offset;
            this.color = color;
        }
    }

    /**
     * Pass 2: Renders SVG elements to UGraphic.
     */
    private static class RenderHandler extends DefaultHandler {
        private final UGraphicWithScale initialUgs;
        private UGraphicWithScale ugs;
        private final ColorResolver colorResolver;
        private final Map<String, BufferedElement> definitions;
        private final Deque<GroupState> groupStack = new ArrayDeque<>();
        private final List<UGraphicWithScale> ugsStack = new ArrayList<>();
        
        private boolean inText = false;
        private StringBuilder textContent = new StringBuilder();
        private Attributes textAttrs = null;

        RenderHandler(UGraphicWithScale ugs, ColorResolver colorResolver, Map<String, BufferedElement> definitions) {
            this.initialUgs = ugs;
            this.ugs = ugs;
            this.colorResolver = colorResolver;
            this.definitions = definitions;
        }

        @Override
        public void startElement(String uri, String localName, String qName, Attributes attrs) throws SAXException {
            LOG.fine("Starting element: " + qName + " with " + attrs.getLength() + " attributes");
            switch (qName) {
                case "svg":
                    // Root element, just continue
                    break;
                case "g":
                    handleGroupStart(attrs);
                    break;
                case "rect":
                    handleRect(attrs);
                    break;
                case "circle":
                    handleCircle(attrs);
                    break;
                case "ellipse":
                    handleEllipse(attrs);
                    break;
                case "line":
                    handleLine(attrs);
                    break;
                case "polyline":
                    handlePolyline(attrs);
                    break;
                case "polygon":
                    handlePolygon(attrs);
                    break;
                case "path":
                    handlePath(attrs);
                    break;
                case "text":
                    inText = true;
                    textContent = new StringBuilder();
                    textAttrs = new AttributesAdapter(attrs);
                    break;
                case "use":
                    handleUse(attrs);
                    break;
                case "defs":
                case "symbol":
                case "title":
                case "desc":
                case "metadata":
                case "clipPath":
                case "mask":
                case "filter":
                case "pattern":
                case "marker":
                case "style":
                    // Skip: CSS style blocks not supported (use inline attributes)
                    // Supporting CSS would require a full CSS parser for selectors,
                    // specificity, cascade, and inheritance rules
                    break;
                case "script":
                    // Skip these elements
                    break;
                default:
                    // Unknown element, skip
                    break;
            }
        }

        @Override
        public void endElement(String uri, String localName, String qName) {
            LOG.fine("Ending element: " + qName);
            if ("g".equals(qName)) {
                handleGroupEnd();
            } else if ("text".equals(qName) && inText) {
                handleText(textAttrs, textContent.toString());
                inText = false;
                textContent = new StringBuilder();
                textAttrs = null;
            }
        }

        @Override
        public void characters(char[] ch, int start, int length) {
            if (inText) {
                textContent.append(ch, start, length);
            }
        }

        // ---- Group Handling ----

        private void handleGroupStart(Attributes attrs) {
            GroupState gs = new GroupState(attrs);
            groupStack.addFirst(gs);
            ugsStack.add(0, ugs);

            if (gs.transform != null && !gs.transform.isEmpty()) {
                ugs = applyTransform(ugs, gs.transform);
            }
        }

        private void handleGroupEnd() {
            if (!groupStack.isEmpty()) {
                groupStack.removeFirst();
            }
            if (!ugsStack.isEmpty()) {
                ugs = ugsStack.remove(0);
            }
        }

        // ---- Shape Handlers ----

        private void handleRect(Attributes attrs) {
            ugs = applyStyleAndTransform(attrs, ugs);

            double x = getDoubleAttr(attrs, "x", 0);
            double y = getDoubleAttr(attrs, "y", 0);
            double width = getDoubleAttr(attrs, "width", 0);
            double height = getDoubleAttr(attrs, "height", 0);

            double scalex = ugs.getAffineTransform().getScaleX();
            double scaley = ugs.getAffineTransform().getScaleY();
            double deltax = ugs.getAffineTransform().getTranslateX();
            double deltay = ugs.getAffineTransform().getTranslateY();

            UTranslate translate = new UTranslate(deltax + x * scalex, deltay + y * scaley);
            ugs.apply(translate).draw(URectangle.build(width * scalex, height * scaley));
        }

        private void handleCircle(Attributes attrs) {
            ugs = applyStyleAndTransform(attrs, ugs);

            double cx = getDoubleAttr(attrs, "cx", 0);
            double cy = getDoubleAttr(attrs, "cy", 0);
            double r = getDoubleAttr(attrs, "r", 0);

            double scalex = ugs.getAffineTransform().getScaleX();
            double scaley = ugs.getAffineTransform().getScaleY();
            double deltax = ugs.getAffineTransform().getTranslateX();
            double deltay = ugs.getAffineTransform().getTranslateY();

            UTranslate translate = new UTranslate(
                deltax + (cx - r) * scalex,
                deltay + (cy - r) * scaley
            );
            ugs.apply(translate).draw(UEllipse.build(r * 2 * scalex, r * 2 * scaley));
        }

        private void handleEllipse(Attributes attrs) {
            ugs = applyStyleAndTransform(attrs, ugs);

            double cx = getDoubleAttr(attrs, "cx", 0);
            double cy = getDoubleAttr(attrs, "cy", 0);
            double rx = getDoubleAttr(attrs, "rx", 0);
            double ry = getDoubleAttr(attrs, "ry", 0);

            double scalex = ugs.getAffineTransform().getScaleX();
            double scaley = ugs.getAffineTransform().getScaleY();
            double deltax = ugs.getAffineTransform().getTranslateX();
            double deltay = ugs.getAffineTransform().getTranslateY();

            UTranslate translate = new UTranslate(
                deltax + (cx - rx) * scalex,
                deltay + (cy - ry) * scaley
            );
            ugs.apply(translate).draw(UEllipse.build(rx * 2 * scalex, ry * 2 * scaley));
        }

        private void handleLine(Attributes attrs) {
            ugs = applyStyleAndTransform(attrs, ugs);

            double x1 = getDoubleAttr(attrs, "x1", 0);
            double y1 = getDoubleAttr(attrs, "y1", 0);
            double x2 = getDoubleAttr(attrs, "x2", 0);
            double y2 = getDoubleAttr(attrs, "y2", 0);

            double scalex = ugs.getAffineTransform().getScaleX();
            double scaley = ugs.getAffineTransform().getScaleY();
            double deltax = ugs.getAffineTransform().getTranslateX();
            double deltay = ugs.getAffineTransform().getTranslateY();

            UTranslate translate = new UTranslate(deltax, deltay);
            ugs.apply(translate).draw(ULine.create(
                new XPoint2D(x1 * scalex, y1 * scaley),
                new XPoint2D(x2 * scalex, y2 * scaley)
            ));
        }

        private void handlePolyline(Attributes attrs) {
            handlePolyShape(attrs, false);
        }

        private void handlePolygon(Attributes attrs) {
            handlePolyShape(attrs, true);
        }

        private void handlePolyShape(Attributes attrs, boolean closed) {
            ugs = applyStyleAndTransform(attrs, ugs);

            String pointsStr = attrs.getValue("points");
            if (pointsStr == null || pointsStr.isEmpty()) {
                return;
            }

            double scalex = ugs.getAffineTransform().getScaleX();
            double scaley = ugs.getAffineTransform().getScaleY();
            double deltax = ugs.getAffineTransform().getTranslateX();
            double deltay = ugs.getAffineTransform().getTranslateY();

            String[] pointPairs = pointsStr.trim().split("\\s+");
            net.sourceforge.plantuml.klimt.UPath path = 
                new net.sourceforge.plantuml.klimt.UPath(closed ? "polygon" : "polyline", null);
            
            boolean first = true;
            for (String pair : pointPairs) {
                String[] coords = pair.split(",");
                if (coords.length == 2) {
                    try {
                        double x = Double.parseDouble(coords[0].trim()) * scalex;
                        double y = Double.parseDouble(coords[1].trim()) * scaley;
                        if (first) {
                            path.moveTo(x, y);
                            first = false;
                        } else {
                            path.lineTo(x, y);
                        }
                    } catch (NumberFormatException e) {
                        // Skip invalid points
                    }
                }
            }

            if (closed) {
                // NOTE: Keep polygons open to avoid SEG_CLOSE dependency in klimt.
                // path.closePath();
            }

            UTranslate translate = new UTranslate(deltax, deltay);
            ugs.apply(translate).draw(path);
        }

        private void handlePath(Attributes attrs) {
            ugs = applyStyleAndTransform(attrs, ugs);

            String pathData = attrs.getValue("d");
            if (pathData != null && !pathData.isEmpty()) {
                SvgPath svgPath = new SvgPath(pathData, UTranslate.none());
                svgPath.drawMe(ugs.getUg(), ugs.getAffineTransform());
            }
        }

        private void handleText(Attributes attrs, String content) {
            String fontFamily = attrs.getValue("font-family");
            String fontSize = attrs.getValue("font-size");
            String fontWeight = attrs.getValue("font-weight");
            String fontStyle = attrs.getValue("font-style");
            String textDecoration = attrs.getValue("text-decoration");
            String fillString = attrs.getValue("fill");
            String xLocation = attrs.getValue("x");
            String yLocation = attrs.getValue("y");

            String textContent = content.trim();
            int fontSizeValue = parseFontSize(fontSize, 12);
            int awtFontStyle = parseFontStyle(fontWeight, fontStyle);

            // Use default font family if not specified
            if (fontFamily == null || fontFamily.isEmpty()) {
                fontFamily = "SansSerif";
            }

            UFont font = UFont.build(fontFamily, awtFontStyle, fontSizeValue);

            HColor textColor = ugs.getDefaultColor();
            if (fillString != null && !fillString.isEmpty() && !"none".equals(fillString)) {
                HColor fc = ugs.getTrueColor(fillString);
                if (fc != null) {
                    textColor = fc;
                }
            }

            FontConfiguration fontConfig = FontConfiguration.create(font, textColor, textColor, UStroke.simple(), 8);
            fontConfig = applyTextDecoration(fontConfig, textDecoration);

            UText utext = UText.build(textContent, fontConfig);

            UTranslate textTranslate = new UTranslate(
                xLocation != null && !xLocation.isEmpty() ? Double.parseDouble(xLocation) : 0,
                yLocation != null && !yLocation.isEmpty() ? Double.parseDouble(yLocation) : 0
            );
            ugs.apply(textTranslate).draw(utext);
        }

        private void handleUse(Attributes attrs) {
            String href = attrs.getValue("href");
            if (href == null || href.isEmpty()) {
                href = attrs.getValue("xlink:href");
            }
            if (href == null || href.isEmpty() || !href.startsWith("#")) {
                return;
            }

            String refId = href.substring(1);
            BufferedElement referenced = definitions.get(refId);
            if (referenced == null) {
                LOG.warning("Referenced element not found: " + refId);
                return;
            }

            // Apply positioning from <use> element
            double x = getDoubleAttr(attrs, "x", 0);
            double y = getDoubleAttr(attrs, "y", 0);

            ugs = applyStyleAndTransform(attrs, ugs);

            if (x != 0 || y != 0) {
                double scalex = ugs.getAffineTransform().getScaleX();
                double scaley = ugs.getAffineTransform().getScaleY();
                UTranslate translate = new UTranslate(x * scalex, y * scaley);
                ugs = ugs.apply(translate);
            }

            // Note: For full <use> support, would need to re-parse the referenced element
            // This is a simplified implementation
            LOG.fine("Processing use reference: " + refId);
        }

        // ---- Style and Transform Helpers ----

        /**
         * Extract a color or gradient from a gradient definition.
         * For linear gradients, returns an HColorGradient if possible.
         * For radial gradients or when gradient creation fails, returns the first stop color.
         */
        private HColor extractGradientColor(String gradientId) {
            final BufferedElement gradientElement = definitions.get(gradientId);
            if (gradientElement == null) {
                LOG.fine("Gradient not found: " + gradientId);
                return null;
            }
            
            final String tagName = gradientElement.tagName;
            LOG.fine("Extracting gradient: " + gradientId + " (tagName=" + tagName + ", stops=" + gradientElement.stops.size() + ")");
            
            // Handle linearGradient - try to create a PlantUML gradient
            if ("linearGradient".equals(tagName)) {
                if (gradientElement.stops.size() >= 2) {
                    // Get first and last stop colors
                    final GradientStop firstStop = gradientElement.stops.get(0);
                    final GradientStop lastStop = gradientElement.stops.get(gradientElement.stops.size() - 1);
                    
                    final String firstColor = firstStop.color;
                    final String lastColor = lastStop.color;
                    
                    LOG.fine("Stop colors: first=" + firstColor + ", last=" + lastColor);
                    
                    if (firstColor != null && !firstColor.isEmpty() && 
                        lastColor != null && !lastColor.isEmpty()) {
                        
                        final HColor color1 = ugs.getTrueColor(firstColor);
                        final HColor color2 = ugs.getTrueColor(lastColor);
                        
                        if (color1 != null && color2 != null) {
                            // Determine gradient direction from x1, y1, x2, y2 attributes
                            final char policy = determineGradientPolicy(gradientElement.attributes);
                            LOG.fine("Creating gradient with policy '" + policy + "' from " + firstColor + " to " + lastColor);
                            return HColors.gradient(color1, color2, policy);
                        }
                    }
                }
                // Fallback to first stop if gradient creation failed
                if (gradientElement.stops.size() > 0) {
                    final String stopColor = gradientElement.stops.get(0).color;
                    if (stopColor != null && !stopColor.isEmpty()) {
                        return ugs.getTrueColor(stopColor);
                    }
                }
            }
            
            // Handle radialGradient - just use first stop color
            if ("radialGradient".equals(tagName)) {
                if (gradientElement.stops.size() > 0) {
                    final String stopColor = gradientElement.stops.get(0).color;
                    if (stopColor != null && !stopColor.isEmpty()) {
                        return ugs.getTrueColor(stopColor);
                    }
                }
            }
            
            return null;
        }
        
        /**
         * Determine the PlantUML gradient policy character from SVG linearGradient attributes.
         * Maps SVG x1/y1/x2/y2 to PlantUML's | - \\ / policy.
         */
        private char determineGradientPolicy(Map<String, String> attrs) {
            final String x1 = attrs.get("x1");
            final String y1 = attrs.get("y1");
            final String x2 = attrs.get("x2");
            final String y2 = attrs.get("y2");
            
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
         * Parse a percentage value (e.g., "50%") or number into a 0-1 range.
         */
        private double parsePercentOrNumber(String value, double defaultValue) {
            if (value == null || value.isEmpty()) {
                return defaultValue;
            }
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

        private UGraphicWithScale applyStyleAndTransform(Attributes attrs, UGraphicWithScale ugs) {
            String transform = attrs.getValue("transform");
            if (transform != null && !transform.isEmpty()) {
                ugs = applyTransform(ugs, transform);
            }

            String fill = attrs.getValue("fill");
            String stroke = attrs.getValue("stroke");
            String strokeWidth = attrs.getValue("stroke-width");

            if (strokeWidth != null && !strokeWidth.isEmpty()) {
                try {
                    double scale = ugs.getInitialScale();
                    ugs = ugs.apply(UStroke.withThickness(scale * Double.parseDouble(strokeWidth)));
                } catch (NumberFormatException ex) {
                    // ignore
                }
            }

            if (stroke != null && !stroke.isEmpty()) {
                HColor sc = ugs.getTrueColor(stroke);
                if (sc != null) {
                    ugs = ugs.apply(sc);
                }
                if (fill == null || fill.isEmpty()) {
                    ugs = ugs.apply(ugs.getDefaultColor().bg());
                }
            }

            if (fill != null && !fill.isEmpty()) {
                if ("none".equals(fill)) {
                    ugs = ugs.apply(HColors.none().bg());
                } else if (fill.startsWith("url(#")) {
                    // Extract gradient reference: url(#Gradient) -> Gradient
                    final String gradientId = fill.substring(5, fill.length() - 1);
                    LOG.fine("Detected gradient fill: " + gradientId);
                    final HColor gradientColor = extractGradientColor(gradientId);
                    if (gradientColor != null) {
                        LOG.fine("Applied gradient color: " + gradientColor);
                        // If no stroke specified, also set as foreground
                        if (stroke == null || stroke.isEmpty()) {
                            ugs = ugs.apply(gradientColor);
                        }
                        // Always set as background/fill
                        ugs = ugs.apply(gradientColor.bg());
                    } else {
                        LOG.warning("Failed to extract gradient color for: " + gradientId);
                    }
                } else {
                    HColor fc = ugs.getTrueColor(fill);
                    if (fc != null) {
                        if (stroke == null || stroke.isEmpty()) {
                            ugs = ugs.apply(fc);
                        }
                        ugs = ugs.apply(fc.bg());
                    }
                }
            }

            return ugs;
        }

        // ---- Transform Parsing (Shared DOM/SAX helpers) ----

        private static final Pattern P_TRANSLATE1 = Pattern.compile("translate\\(([-.0-9]+)[ ,]+([-.0-9]+)\\)");
        private static final Pattern P_TRANSLATE2 = Pattern.compile("translate\\(([-.0-9]+)\\)");
        private static final Pattern P_ROTATE = Pattern.compile("rotate\\(([-.0-9]+)[ ,]+([-.0-9]+)[ ,]+([-.0-9]+)\\)");
        private static final Pattern P_SCALE1 = Pattern.compile("scale\\(([-.0-9]+)\\)");
        private static final Pattern P_SCALE2 = Pattern.compile("scale\\(([-.0-9]+)[ ,]+([-.0-9]+)\\)");
        private static final Pattern P_MATRIX = Pattern.compile(
            "matrix\\(([-.0-9]+)[ ,]+([-.0-9]+)[ ,]+([-.0-9]+)[ ,]+([-.0-9]+)[ ,]+([-.0-9]+)[ ,]+([-.0-9]+)\\)");

        private UGraphicWithScale applyTransform(UGraphicWithScale ugs, String transform) {
            if (transform == null || transform.isEmpty()) {
                return ugs;
            }

            if (transform.contains("rotate(")) {
                return applyRotate(ugs, transform);
            }

            if (transform.contains("matrix(")) {
                return applyMatrix(ugs, transform);
            }

            double[] scale = getScale(transform);
            UTranslate translate = getTranslate(transform);
            ugs = ugs.applyTranslate(translate.getDx(), translate.getDy());

            return ugs.applyScale(scale[0], scale[1]);
        }

        private UGraphicWithScale applyMatrix(UGraphicWithScale ugs, String transform) {
            Matcher m = P_MATRIX.matcher(transform);
            if (m.find()) {
                double v1 = Double.parseDouble(m.group(1));
                double v2 = Double.parseDouble(m.group(2));
                double v3 = Double.parseDouble(m.group(3));
                double v4 = Double.parseDouble(m.group(4));
                double v5 = Double.parseDouble(m.group(5));
                double v6 = Double.parseDouble(m.group(6));
                ugs = ugs.applyMatrix(v1, v2, v3, v4, v5, v6);
            }
            return ugs;
        }

        private UGraphicWithScale applyRotate(UGraphicWithScale ugs, String transform) {
            Matcher m = P_ROTATE.matcher(transform);
            if (m.find()) {
                double angle = Double.parseDouble(m.group(1));
                double x = Double.parseDouble(m.group(2));
                double y = Double.parseDouble(m.group(3));
                ugs = ugs.applyRotate(angle, x, y);
            }
            return ugs;
        }

        private UTranslate getTranslate(String transform) {
            double x = 0;
            double y = 0;

            Matcher m1 = P_TRANSLATE1.matcher(transform);
            if (m1.find()) {
                x = Double.parseDouble(m1.group(1));
                y = Double.parseDouble(m1.group(2));
            } else {
                Matcher m2 = P_TRANSLATE2.matcher(transform);
                if (m2.find()) {
                    x = Double.parseDouble(m2.group(1));
                    y = x;
                }
            }
            return new UTranslate(x, y);
        }

        private double[] getScale(String transform) {
            double[] scale = new double[] { 1, 1 };
            Matcher m1 = P_SCALE1.matcher(transform);
            if (m1.find()) {
                scale[0] = Double.parseDouble(m1.group(1));
                scale[1] = scale[0];
            } else {
                Matcher m2 = P_SCALE2.matcher(transform);
                if (m2.find()) {
                    scale[0] = Double.parseDouble(m2.group(1));
                    scale[1] = Double.parseDouble(m2.group(2));
                }
            }
            return scale;
        }

        // ---- Font/Text Helpers (Shared DOM/SAX helpers) ----

        private static int parseFontSize(String fontSizeStr, int defaultSize) {
            if (fontSizeStr == null || fontSizeStr.isEmpty()) {
                return defaultSize;
            }

            try {
                String cleaned = fontSizeStr.trim().replaceAll("(?i)(px|pt|em|rem)$", "");
                return Integer.parseInt(cleaned);
            } catch (NumberFormatException e) {
                return defaultSize;
            }
        }

        private static int parseFontStyle(String fontWeight, String fontStyle) {
            int style = Font.PLAIN;

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
                        // Ignore
                    }
                }
            }

            if (fontStyle != null && !fontStyle.isEmpty()) {
                if ("italic".equalsIgnoreCase(fontStyle) || "oblique".equalsIgnoreCase(fontStyle)) {
                    style |= Font.ITALIC;
                }
            }

            return style;
        }

        private static FontConfiguration applyTextDecoration(FontConfiguration fontConfig, String textDecoration) {
            if (textDecoration == null || textDecoration.isEmpty() || "none".equalsIgnoreCase(textDecoration)) {
                return fontConfig;
            }

            if (textDecoration.contains("underline")) {
                fontConfig = fontConfig.add(FontStyle.UNDERLINE);
            }

            if (textDecoration.contains("line-through")) {
                fontConfig = fontConfig.add(FontStyle.STRIKE);
            }

            return fontConfig;
        }

        // ---- Utility Methods ----

        private double getDoubleAttr(Attributes attrs, String name, double defaultValue) {
            String value = attrs.getValue(name);
            if (value == null || value.isEmpty()) {
                return defaultValue;
            }
            try {
                return Double.parseDouble(value);
            } catch (NumberFormatException e) {
                return defaultValue;
            }
        }
    }

    /**
     * Group state holder for nested groups.
     */
    private static class GroupState {
        final String id;
        final String className;
        final String transform;

        GroupState(Attributes attrs) {
            this.id = attrs.getValue("id");
            this.className = attrs.getValue("class");
            this.transform = attrs.getValue("transform");
        }
    }

    /**
     * Adapter to make SAX Attributes persist beyond the event callback.
     */
    private static class AttributesAdapter implements Attributes {
        private final Map<String, String> attrs = new HashMap<>();

        AttributesAdapter(Attributes source) {
            for (int i = 0; i < source.getLength(); i++) {
                attrs.put(source.getQName(i), source.getValue(i));
            }
        }

        @Override
        public int getLength() {
            return attrs.size();
        }

        @Override
        public String getURI(int index) {
            return "";
        }

        @Override
        public String getLocalName(int index) {
            return getQName(index);
        }

        @Override
        public String getQName(int index) {
            return new ArrayList<>(attrs.keySet()).get(index);
        }

        @Override
        public String getType(int index) {
            return "CDATA";
        }

        @Override
        public String getValue(int index) {
            return attrs.get(getQName(index));
        }

        @Override
        public int getIndex(String uri, String localName) {
            return new ArrayList<>(attrs.keySet()).indexOf(localName);
        }

        @Override
        public int getIndex(String qName) {
            return new ArrayList<>(attrs.keySet()).indexOf(qName);
        }

        @Override
        public String getType(String uri, String localName) {
            return "CDATA";
        }

        @Override
        public String getType(String qName) {
            return "CDATA";
        }

        @Override
        public String getValue(String uri, String localName) {
            return attrs.get(localName);
        }

        @Override
        public String getValue(String qName) {
            return attrs.get(qName);
        }
    }
}
