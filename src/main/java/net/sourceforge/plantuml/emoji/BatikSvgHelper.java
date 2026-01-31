package net.sourceforge.plantuml.emoji;

import java.io.IOException;
import java.io.StringReader;

import org.apache.batik.anim.dom.SAXSVGDocumentFactory;
import org.apache.batik.bridge.BridgeContext;
import org.apache.batik.bridge.DocumentLoader;
import org.apache.batik.bridge.GVTBuilder;
import org.apache.batik.bridge.UserAgentAdapter;
import org.apache.batik.gvt.GraphicsNode;
import org.apache.batik.util.XMLResourceDescriptor;
import org.w3c.dom.svg.SVGDocument;

/**
 * Helper class for parsing SVG content using Apache Batik.
 * Provides shared functionality for both SvgNanoParser and SvgDomParser.
 */
public class BatikSvgHelper {

    /**
     * Container for parsed SVG document and optional GVT tree.
     */
    public static class ParsedSvg {
        public final SVGDocument document;
        public final GraphicsNode gvt;

        public ParsedSvg(SVGDocument document, GraphicsNode gvt) {
            this.document = document;
            this.gvt = gvt;
        }
    }

    /**
     * Parse SVG content string into a document and optionally build GVT tree.
     *
     * @param svgContent the SVG content as a string
     * @param buildGvt whether to build the GVT (Graphics Vector Toolkit) tree for rendering
     * @return ParsedSvg containing the document and optional GVT
     * @throws IOException if parsing fails
     */
    public static ParsedSvg parseSvg(String svgContent, boolean buildGvt) throws IOException {
        // ensure a parser is available
        final String parser = XMLResourceDescriptor.getXMLParserClassName();
        final SAXSVGDocumentFactory factory = new SAXSVGDocumentFactory(parser);

        // create SVGDocument from String - use SVG namespace URI
        final SVGDocument doc = (SVGDocument) factory.createDocument("http://www.w3.org/2000/svg", new StringReader(svgContent));

        GraphicsNode gvt = null;
        if (buildGvt) {
            // Build the GVT tree for rendering/traversal
            final UserAgentAdapter userAgent = new UserAgentAdapter();
            final DocumentLoader loader = new DocumentLoader(userAgent);
            final BridgeContext ctx = new BridgeContext(userAgent, loader);
            ctx.setDynamicState(BridgeContext.DYNAMIC);
            final GVTBuilder builder = new GVTBuilder();
            gvt = builder.build(ctx, doc);
        }

        return new ParsedSvg(doc, gvt);
    }
}
