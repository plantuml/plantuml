package net.sourceforge.plantuml.nassidiagram.util;

import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.klimt.shape.URectangle;
import net.sourceforge.plantuml.klimt.shape.UPolygon;
import net.sourceforge.plantuml.klimt.shape.ULine;
import net.sourceforge.plantuml.klimt.shape.UText;
import net.sourceforge.plantuml.klimt.UTranslate;
import net.sourceforge.plantuml.klimt.font.FontConfiguration;
import net.sourceforge.plantuml.klimt.font.UFont;
import net.sourceforge.plantuml.klimt.color.HColors;
import net.sourceforge.plantuml.klimt.geom.XDimension2D;

public class NassiDrawingUtil {
    public static final int MIN_WIDTH = 200;
    public static final int MIN_HEIGHT = 40;
    
    public static void drawBlock(UGraphic ug, double x, double y, double width, double height, String text) {
        // Draw rectangle
        URectangle rect = URectangle.build(width, height);
        ug.apply(new UTranslate(x, y))
          .apply(HColors.WHITE.bg())
          .apply(HColors.BLACK)
          .draw(rect);

        // Draw centered text
        FontConfiguration fontConfig = FontConfiguration.blackBlueTrue(UFont.sansSerif(12));
        UText txt = UText.build(text, fontConfig);
        XDimension2D textDim = txt.calculateDimension(ug.getStringBounder());
        
        double textX = x + (width - textDim.getWidth()) / 2;
        double textY = y + (height + textDim.getHeight()) / 2;
        ug.apply(new UTranslate(textX, textY)).draw(txt);
    }

    public static void drawIfHeader(UGraphic ug, double x, double y, double width, double height, String condition) {
        // Draw diamond shape for condition
        UPolygon diamond = new UPolygon();
        diamond.addPoint(x + width/2, y);
        diamond.addPoint(x + width, y + height/2);
        diamond.addPoint(x + width/2, y + height);
        diamond.addPoint(x, y + height/2);
        
        ug.apply(HColors.WHITE.bg())
          .apply(HColors.BLACK)
          .draw(diamond);

        // Draw centered condition text
        FontConfiguration fontConfig = FontConfiguration.blackBlueTrue(UFont.sansSerif(12));
        UText txt = UText.build(condition, fontConfig);
        XDimension2D textDim = txt.calculateDimension(ug.getStringBounder());
        
        double textX = x + (width - textDim.getWidth()) / 2;
        double textY = y + (height + textDim.getHeight()) / 2;
        ug.apply(new UTranslate(textX, textY)).draw(txt);
    }

    public static void drawWhileHeader(UGraphic ug, double x, double y, double width, double height, String condition) {
        // Draw trapezoid for while condition
        UPolygon trapezoid = new UPolygon();
        trapezoid.addPoint(x, y);
        trapezoid.addPoint(x + width, y);
        trapezoid.addPoint(x + width - 20, y + height);
        trapezoid.addPoint(x + 20, y + height);
        
        ug.apply(HColors.WHITE.bg())
          .apply(HColors.BLACK)
          .draw(trapezoid);

        // Draw centered condition text
        FontConfiguration fontConfig = FontConfiguration.blackBlueTrue(UFont.sansSerif(12));
        UText txt = UText.build("while " + condition, fontConfig);
        XDimension2D textDim = txt.calculateDimension(ug.getStringBounder());
        
        double textX = x + (width - textDim.getWidth()) / 2;
        double textY = y + (height + textDim.getHeight()) / 2;
        ug.apply(new UTranslate(textX, textY)).draw(txt);
    }

    public static void drawVerticalDivider(UGraphic ug, double x, double y, double height) {
        ULine line = new ULine(0, height);
        ug.apply(new UTranslate(x, y))
          .apply(HColors.BLACK)
          .draw(line);
    }
} 