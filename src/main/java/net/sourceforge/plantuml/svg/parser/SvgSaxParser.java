package net.sourceforge.plantuml.svg.parser;

import java.awt.Font;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import net.atmp.PixelImage;
import net.sourceforge.plantuml.emoji.ColorResolver;
import net.sourceforge.plantuml.emoji.GrayLevelRange;
import net.sourceforge.plantuml.emoji.UGraphicWithScale;
import net.sourceforge.plantuml.klimt.AffineTransformType;
import net.sourceforge.plantuml.klimt.UStroke;
import net.sourceforge.plantuml.klimt.UTranslate;
import net.sourceforge.plantuml.klimt.awt.PortableImage;
import net.sourceforge.plantuml.klimt.color.HColor;
import net.sourceforge.plantuml.klimt.color.HColors;
import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.klimt.font.FontConfiguration;
import net.sourceforge.plantuml.klimt.font.FontStyle;
import net.sourceforge.plantuml.klimt.font.StringBounder;
import net.sourceforge.plantuml.klimt.font.UFont;
import net.sourceforge.plantuml.klimt.font.UFontFactory;
import net.sourceforge.plantuml.klimt.geom.XDimension2D;
import net.sourceforge.plantuml.klimt.shape.AbstractTextBlock;
import net.sourceforge.plantuml.klimt.shape.TextBlock;
import net.sourceforge.plantuml.klimt.shape.UImage;
import net.sourceforge.plantuml.klimt.shape.UImageSvg;
import net.sourceforge.plantuml.klimt.shape.URectangle;
import net.sourceforge.plantuml.klimt.shape.UText;
import net.sourceforge.plantuml.openiconic.SvgPath;
import net.sourceforge.plantuml.security.SImageIO;
import net.sourceforge.plantuml.utils.Base64Coder;

/**
 * Zero-dependency SAX-based SVG parser using only Java SDK components.
 * 
 * <p>This parser provides a zero-dependency alternative without requiring
 * Apache Batik dependencies. Uses Java's built-in SAX parser (available since Java 1.4).
 * 
 * <p><b>Feature Set (SVG 1.1 Core Subset):</b>
 * <ul>
 *   <li>Basic shapes: rect, circle, ellipse, line, polyline, polygon, path</li>
 *   <li>Text elements with font styling (family, size, weight, style, decoration) and text-anchor alignment</li>
 *   <li>Transforms: translate, rotate, scale, matrix</li>
 *   <li>Gradients: linearGradient (core renderer does not support radial gradients)</li>
 *   <li>Groups with style inheritance</li>
 *   <li>Definitions: defs, symbol, use references</li>
 * </ul>
 * 
 * <p><b>Limitations:</b>
 * <ul>
 *   <li>Feature-frozen at SVG 1.1 core subset</li>
 *   <li>No SVG 2.0 extensions (use a full DOM-based parser for full support)</li>
 *   <li>Radial gradients are not supported (not in core PlantUML renderer)</li>
 *   <li>Linear gradients use only the first/last stops; intermediate stops, offsets, and stop-opacity are ignored</li>
 *   <li>Gradient direction is approximated to horizontal, vertical, or diagonal; arbitrary angles are not preserved</li>
 *   <li>Embedded raster images via data URIs only (PNG/JPEG); no external URLs or embedded SVG</li>
 *   <li>No clipPath, mask, filter, pattern</li>
 *   <li>Text: no tspan, overline, or advanced layout</li>
 *   <li>Numeric font-weight values are reduced to bold/normal only</li>
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
    private static final AtomicBoolean WARNED_NUMERIC_WEIGHT = new AtomicBoolean(false);

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
     *
     * <p>Stores the raw XML for referenced elements so <code>&lt;use&gt;</code>
     * can re-parse them during render. Gradient stops and definition attributes
     * are also captured for gradient resolution.</p>
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
                        final BufferedElement buffered = new BufferedElement(currentTag, currentContent.toString(), currentAttrs);
                        buffered.stops.addAll(currentElement.stops);
                        definitions.put(currentId, buffered);
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
     *
     * <p>Consumes SAX events from the SVG stream and translates supported
     * elements into PlantUML drawing primitives. Definition blocks are skipped
     * during this pass and <code>&lt;use&gt;</code> references are resolved by
     * re-parsing the buffered definition XML with the current transform and
     * inherited styling applied.</p>
     */
    private static class RenderHandler extends DefaultHandler {
        private final UGraphicWithScale initialUgs;
        private UGraphicWithScale ugs;
        private final ColorResolver colorResolver;
        private final Map<String, BufferedElement> definitions;
        private final Deque<GroupState> groupStack = new ArrayDeque<>();
        private final List<UGraphicWithScale> ugsStack = new ArrayList<>();
        private int defsDepth = 0;
        
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
            if ("defs".equals(qName)) {
                defsDepth++;
                return;
            }
            if (defsDepth > 0) {
                return;
            }
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
                case "image":
                    handleImage(attrs);
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
            if ("defs".equals(qName) && defsDepth > 0) {
                defsDepth--;
                return;
            }
            if (defsDepth > 0) {
                return;
            }
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
            UGraphicWithScale elementUgs = applyStyleAndTransform(attrs, ugs);

            double x = getDoubleAttr(attrs, "x", 0);
            double y = getDoubleAttr(attrs, "y", 0);
            double width = getDoubleAttr(attrs, "width", 0);
            double height = getDoubleAttr(attrs, "height", 0);

            net.sourceforge.plantuml.klimt.UPath path = net.sourceforge.plantuml.klimt.UPath.none();
            path.moveTo(x, y);
            path.lineTo(x + width, y);
            path.lineTo(x + width, y + height);
            path.lineTo(x, y + height);
            path.lineTo(x, y);

            path = path.affine(elementUgs.getAffineTransform(), elementUgs.getAngle(), elementUgs.getInitialScale());
            elementUgs.draw(path);
        }

        private void handleCircle(Attributes attrs) {
            UGraphicWithScale elementUgs = applyStyleAndTransform(attrs, ugs);

            double cx = getDoubleAttr(attrs, "cx", 0);
            double cy = getDoubleAttr(attrs, "cy", 0);
            double r = getDoubleAttr(attrs, "r", 0);

            net.sourceforge.plantuml.klimt.UPath path = buildEllipsePath(cx, cy, r, r);
            path = path.affine(elementUgs.getAffineTransform(), elementUgs.getAngle(), elementUgs.getInitialScale());
            elementUgs.draw(path);
        }

        private void handleEllipse(Attributes attrs) {
            UGraphicWithScale elementUgs = applyStyleAndTransform(attrs, ugs);

            double cx = getDoubleAttr(attrs, "cx", 0);
            double cy = getDoubleAttr(attrs, "cy", 0);
            double rx = getDoubleAttr(attrs, "rx", 0);
            double ry = getDoubleAttr(attrs, "ry", 0);

            net.sourceforge.plantuml.klimt.UPath path = buildEllipsePath(cx, cy, rx, ry);
            path = path.affine(elementUgs.getAffineTransform(), elementUgs.getAngle(), elementUgs.getInitialScale());
            elementUgs.draw(path);
        }

        private void handleLine(Attributes attrs) {
            UGraphicWithScale elementUgs = applyStyleAndTransform(attrs, ugs);

            double x1 = getDoubleAttr(attrs, "x1", 0);
            double y1 = getDoubleAttr(attrs, "y1", 0);
            double x2 = getDoubleAttr(attrs, "x2", 0);
            double y2 = getDoubleAttr(attrs, "y2", 0);

            net.sourceforge.plantuml.klimt.UPath path = net.sourceforge.plantuml.klimt.UPath.none();
            path.moveTo(x1, y1);
            path.lineTo(x2, y2);

            path = path.affine(elementUgs.getAffineTransform(), elementUgs.getAngle(), elementUgs.getInitialScale());
            elementUgs.draw(path);
        }

        private void handlePolyline(Attributes attrs) {
            handlePolyShape(attrs, false);
        }

        private void handlePolygon(Attributes attrs) {
            handlePolyShape(attrs, true);
        }

        private void handlePolyShape(Attributes attrs, boolean closed) {
            final UGraphicWithScale elementUgs = applyStyleAndTransform(attrs, ugs);

            String pointsStr = attrs.getValue("points");
            if (pointsStr == null || pointsStr.isEmpty()) {
                return;
            }

            String[] pointPairs = pointsStr.trim().split("\\s+");
            net.sourceforge.plantuml.klimt.UPath path = 
                new net.sourceforge.plantuml.klimt.UPath(closed ? "polygon" : "polyline", null);
            
            boolean first = true;
            double firstX = 0;
            double firstY = 0;
            for (String pair : pointPairs) {
                String[] coords = pair.split(",");
                if (coords.length == 2) {
                    try {
                        double x = Double.parseDouble(coords[0].trim());
                        double y = Double.parseDouble(coords[1].trim());
                        if (first) {
                            path.moveTo(x, y);
                            firstX = x;
                            firstY = y;
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
                if (first == false)
                    path.lineTo(firstX, firstY);
            }

            path = path.affine(elementUgs.getAffineTransform(), elementUgs.getAngle(), elementUgs.getInitialScale());
            elementUgs.draw(path);
        }

        private net.sourceforge.plantuml.klimt.UPath buildEllipsePath(double cx, double cy, double rx, double ry) {
            net.sourceforge.plantuml.klimt.UPath path = net.sourceforge.plantuml.klimt.UPath.none();
            path.moveTo(0, ry);
            path.arcTo(rx, ry, 0, 0, 1, rx, 0);
            path.arcTo(rx, ry, 0, 0, 1, 2 * rx, ry);
            path.arcTo(rx, ry, 0, 0, 1, rx, 2 * ry);
            path.arcTo(rx, ry, 0, 0, 1, 0, ry);
            path.lineTo(0, ry);

            return path.translate(cx - rx, cy - ry);
        }

        private void handlePath(Attributes attrs) {
            final UGraphicWithScale elementUgs = applyStyleAndTransform(attrs, ugs);

            String pathData = attrs.getValue("d");
            if (pathData != null && !pathData.isEmpty()) {
                SvgPath svgPath = new SvgPath(pathData, UTranslate.none());
                svgPath.drawMe(elementUgs.getUg(), elementUgs.getAffineTransform());
            }
        }

        private void handleText(Attributes attrs, String content) {
            final UGraphicWithScale elementUgs = applyStyleAndTransform(attrs, ugs);
            String fontFamily = getAttrOrStyle(attrs, "font-family", "font-family");
            String fontSize = getAttrOrStyle(attrs, "font-size", "font-size");
            String fontWeight = getAttrOrStyle(attrs, "font-weight", "font-weight");
            String fontStyle = getAttrOrStyle(attrs, "font-style", "font-style");
            String textDecoration = getAttrOrStyle(attrs, "text-decoration", "text-decoration");
            String fillString = getAttrOrStyle(attrs, "fill", "fill");
            String textAnchor = attrs.getValue("text-anchor");
            String xLocation = attrs.getValue("x");
            String yLocation = attrs.getValue("y");

            String textContent = content.trim();
            int fontSizeValue = parseFontSize(fontSize, 12);
            int awtFontStyle = parseFontStyle(fontWeight, fontStyle);

            // Use default font family if not specified
            if (fontFamily == null || fontFamily.isEmpty()) {
                fontFamily = "SansSerif";
            }

            UFont font = UFontFactory.build(fontFamily, awtFontStyle, fontSizeValue);

            HColor textColor = elementUgs.getDefaultColor();
            if (fillString != null && !fillString.isEmpty() && !"none".equals(fillString)) {
                HColor fc = elementUgs.getTrueColor(fillString);
                if (fc != null) {
                    textColor = fc;
                }
            }

            FontConfiguration fontConfig = FontConfiguration.create(font, textColor, textColor, UStroke.simple(), 8);
            fontConfig = applyTextDecoration(fontConfig, textDecoration);

            UText utext = UText.build(textContent, fontConfig);

            double x = xLocation != null && !xLocation.isEmpty() ? Double.parseDouble(xLocation) : 0;
            double y = yLocation != null && !yLocation.isEmpty() ? Double.parseDouble(yLocation) : 0;
            double anchorShift = 0;
            if (textAnchor != null && textAnchor.isEmpty() == false) {
                double textWidth = utext.calculateDimension(elementUgs.getUg().getStringBounder()).getWidth();
                if ("middle".equalsIgnoreCase(textAnchor)) {
                    anchorShift = -textWidth / 2.0;
                } else if ("end".equalsIgnoreCase(textAnchor)) {
                    anchorShift = -textWidth;
                }
            }

            UTranslate textTranslate = new UTranslate(x + anchorShift, y);
            elementUgs.apply(textTranslate).draw(utext);
        }

        private void handleImage(Attributes attrs) {
            final String href = getHref(attrs);
            if (href == null || href.isEmpty())
                return;

            final DataImage dataImage = decodeDataImage(href);
            if (dataImage == null)
                return;

            final PortableImage image = dataImage.image;
            if (image == null)
                return;

            final UGraphicWithScale elementUgs = applyStyleAndTransform(attrs, ugs);
            final double x = getDoubleAttr(attrs, "x", 0);
            final double y = getDoubleAttr(attrs, "y", 0);

            final String widthAttr = attrs.getValue("width");
            final String heightAttr = attrs.getValue("height");
            final double width = getDoubleAttr(attrs, "width", image.getWidth());
            final double height = getDoubleAttr(attrs, "height", image.getHeight());

            if (width <= 0 || height <= 0)
                return;

            final double scale = resolveImageScale(widthAttr, heightAttr, width, height, image.getWidth(), image.getHeight());
            if (scale <= 0)
                return;

            if (elementUgs.getUg().matchesProperty("SVG")) {
                final String svgImage = buildSvgImage(href, width, height);
                final UImageSvg imageSvg = new UImageSvg(svgImage, 1.0);
                elementUgs.apply(new UTranslate(x, y)).draw(imageSvg);
                return;
            }

            UImage uimage = new UImage(new PixelImage(image, AffineTransformType.TYPE_BILINEAR));
            if (scale != 1.0) {
                final PortableImage scaled = uimage.getImage(scale);
                uimage = new UImage(new PixelImage(scaled, AffineTransformType.TYPE_BILINEAR));
            }

            elementUgs.apply(new UTranslate(x, y)).draw(uimage);
        }

        private String getHref(Attributes attrs) {
            String href = attrs.getValue("href");
            if (href == null || href.isEmpty())
                href = attrs.getValue("xlink:href");

            return href;
        }

        private double resolveImageScale(String widthAttr, String heightAttr, double width, double height,
                int imageWidth, int imageHeight) {
            if (imageWidth <= 0 || imageHeight <= 0)
                return 1.0;

            final double scaleX = width / imageWidth;
            final double scaleY = height / imageHeight;

            if (widthAttr == null && heightAttr == null)
                return 1.0;

            if (Math.abs(scaleX - scaleY) < 0.0001)
                return scaleX;

            if (widthAttr != null)
                return scaleX;

            return scaleY;
        }

        private DataImage decodeDataImage(String href) {
            final String lowerHref = href.toLowerCase();
            if (lowerHref.startsWith("data:image/svg+xml;base64,")) {
                LOG.fine("Skipping embedded SVG image data URI");
                return null;
            }

            final String prefix = getDataImagePrefix(lowerHref);
            if (prefix == null) {
                LOG.fine("Skipping non-data image href");
                return null;
            }

            final String data = href.substring(prefix.length());
            final byte[] bytes;
            try {
                bytes = Base64Coder.decode(data);
            } catch (IllegalArgumentException e) {
                LOG.warning("Failed to decode embedded image: " + e.getMessage());
                return null;
            }

            try {
                final PortableImage image = SImageIO.read(bytes);
                if (image == null)
                    return null;

                return new DataImage(image);
            } catch (IOException e) {
                LOG.warning("Failed to read embedded image: " + e.getMessage());
                return null;
            }
        }

        private String getDataImagePrefix(String lowerHref) {
            if (lowerHref.startsWith("data:image/png;base64,"))
                return "data:image/png;base64,";

            if (lowerHref.startsWith("data:image/jpeg;base64,"))
                return "data:image/jpeg;base64,";

            if (lowerHref.startsWith("data:image/jpg;base64,"))
                return "data:image/jpg;base64,";

            return null;
        }

        private String buildSvgImage(String href, double width, double height) {
            final String safeHref = href.replace("&", "&amp;").replace("\"", "&quot;");
            return "<svg xmlns=\"http://www.w3.org/2000/svg\" xmlns:xlink=\"http://www.w3.org/1999/xlink\" "
                    + "width=\"" + formatNumber(width) + "\" height=\"" + formatNumber(height) + "\">"
                    + "<image x=\"0\" y=\"0\" width=\"" + formatNumber(width) + "\" height=\""
                    + formatNumber(height) + "\" xlink:href=\"" + safeHref + "\"/>"
                    + "</svg>";
        }

        private String formatNumber(double value) {
            if (value == Math.rint(value))
                return Long.toString(Math.round(value));

            return Double.toString(value);
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
            double width = getDoubleAttr(attrs, "width", -1);
            double height = getDoubleAttr(attrs, "height", -1);

            UGraphicWithScale elementUgs = applyStyleAndTransform(attrs, ugs);

            if (x != 0 || y != 0) {
                double scalex = elementUgs.getAffineTransform().getScaleX();
                double scaley = elementUgs.getAffineTransform().getScaleY();
                UTranslate translate = new UTranslate(x * scalex, y * scaley);
                elementUgs = elementUgs.apply(translate);
            }

            renderUseReference(refId, referenced, elementUgs, width, height);
        }

        private void renderUseReference(String refId, BufferedElement referenced, UGraphicWithScale elementUgs,
                double width, double height) {
            if (referenced.xmlContent == null || referenced.xmlContent.isEmpty()) {
                LOG.fine("Referenced element has no buffered content: " + refId);
                return;
            }

            if ("symbol".equals(referenced.tagName)) {
                elementUgs = applySymbolViewport(referenced, elementUgs, width, height);
            }

            final String wrapped = wrapSvgFragment(referenced.xmlContent);
            parseSvgFragment(wrapped, elementUgs);
        }

        private UGraphicWithScale applySymbolViewport(BufferedElement referenced, UGraphicWithScale elementUgs,
                double width, double height) {
            final String viewBox = referenced.attributes.get("viewBox");
            if (viewBox == null || viewBox.isEmpty()) {
                return elementUgs;
            }

            final double[] values = parseNumberList(viewBox);
            if (values.length < 4) {
                return elementUgs;
            }

            final double minX = values[0];
            final double minY = values[1];
            final double vbWidth = values[2];
            final double vbHeight = values[3];

            if (vbWidth == 0 || vbHeight == 0) {
                return elementUgs;
            }

            final double scaleX = width > 0 ? width / vbWidth : 1.0;
            final double scaleY = height > 0 ? height / vbHeight : 1.0;

            elementUgs = elementUgs.applyTranslate(-minX, -minY);
            elementUgs = elementUgs.applyScale(scaleX, scaleY);
            return elementUgs;
        }

        private String wrapSvgFragment(String fragment) {
            return "<svg xmlns=\"http://www.w3.org/2000/svg\">" + fragment + "</svg>";
        }

        private void parseSvgFragment(String svgFragment, UGraphicWithScale elementUgs) {
            try {
                SAXParserFactory factory = SAXParserFactory.newInstance();
                factory.setNamespaceAware(true);
                SAXParser parser = factory.newSAXParser();
                RenderHandler renderer = new RenderHandler(elementUgs, colorResolver, definitions);
                parser.parse(new InputSource(new StringReader(svgFragment)), renderer);
            } catch (Exception e) {
                LOG.warning("Failed to parse <use> reference: " + e.getMessage());
            }
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

            String fill = getAttrOrStyle(attrs, "fill", "fill");
            String stroke = getAttrOrStyle(attrs, "stroke", "stroke");
            String strokeWidth = getAttrOrStyle(attrs, "stroke-width", "stroke-width");

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

        private String getAttrOrStyle(Attributes attrs, String attrName, String styleKey) {
            final String styleValue = getStyleValue(attrs, styleKey);
            if (styleValue != null && styleValue.isEmpty() == false)
                return styleValue;

            return attrs.getValue(attrName);
        }

        private String getStyleValue(Attributes attrs, String key) {
            final String style = attrs.getValue("style");
            if (style == null || style.isEmpty())
                return null;

            final String[] parts = style.split(";");
            for (String part : parts) {
                final int idx = part.indexOf(':');
                if (idx <= 0)
                    continue;

                final String name = part.substring(0, idx).trim();
                if (name.equalsIgnoreCase(key))
                    return part.substring(idx + 1).trim();
            }
            return null;
        }

        // ---- Transform Parsing (Shared DOM/SAX helpers) ----

        private static final Pattern P_TRANSFORM_OP = Pattern.compile("(translate|rotate|scale|matrix)\\s*\\(([^)]*)\\)");

        private UGraphicWithScale applyTransform(UGraphicWithScale ugs, String transform) {
            if (transform == null || transform.isEmpty()) {
                return ugs;
            }

            Matcher matcher = P_TRANSFORM_OP.matcher(transform);
            while (matcher.find()) {
                String op = matcher.group(1);
                double[] values = parseNumberList(matcher.group(2));

                if ("translate".equals(op)) {
                    double tx = values.length > 0 ? values[0] : 0;
                    double ty = values.length > 1 ? values[1] : 0;
                    ugs = ugs.applyTranslate(tx, ty);
                } else if ("scale".equals(op)) {
                    double sx = values.length > 0 ? values[0] : 1;
                    double sy = values.length > 1 ? values[1] : sx;
                    if (sx == sy) {
                        ugs = ugs.applyScale(sx, sy);
                    } else {
                        ugs = ugs.applyMatrix(sx, 0, 0, sy, 0, 0);
                    }
                } else if ("rotate".equals(op)) {
                    double angle = values.length > 0 ? values[0] : 0;
                    double cx = values.length > 2 ? values[1] : 0;
                    double cy = values.length > 2 ? values[2] : 0;
                    ugs = ugs.applyRotate(angle, cx, cy);
                } else if ("matrix".equals(op)) {
                    if (values.length >= 6) {
                        ugs = ugs.applyMatrix(values[0], values[1], values[2], values[3], values[4], values[5]);
                    }
                }
            }

            return ugs;
        }

        private double[] parseNumberList(String raw) {
            if (raw == null || raw.trim().isEmpty()) {
                return new double[0];
            }
            String[] parts = raw.trim().split("[,\\s]+");
            double[] values = new double[parts.length];
            int idx = 0;
            for (String part : parts) {
                if (part.isEmpty()) {
                    continue;
                }
                try {
                    values[idx++] = Double.parseDouble(part);
                } catch (NumberFormatException e) {
                    // ignore invalid numbers
                }
            }
            if (idx == values.length) {
                return values;
            }
            double[] trimmed = new double[idx];
            System.arraycopy(values, 0, trimmed, 0, idx);
            return trimmed;
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
                        if (WARNED_NUMERIC_WEIGHT.compareAndSet(false, true)) {
                            LOG.warning("SVG font-weight numeric values are reduced to bold/normal; "
                                    + "intermediate weights are not supported.");
                        }
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

    private static class DataImage {
        private final PortableImage image;

        private DataImage(PortableImage image) {
            this.image = image;
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
