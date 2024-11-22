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
    public static final int MIN_WIDTH = 400;
    public static final int MIN_HEIGHT = 40;
    
    public static void drawOuterBox(UGraphic ug, double x, double y, double width, double height, String title) {
        // Draw outer container box
        URectangle outerBox = URectangle.build(width, height);
        ug.apply(new UTranslate(x, y))
          .apply(HColors.WHITE.bg())
          .apply(HColors.BLACK)
          .draw(outerBox);

        // Draw title if provided
        if (title != null && !title.isEmpty()) {
            FontConfiguration fontConfig = FontConfiguration.blackBlueTrue(UFont.sansSerif(14).bold());
            UText txt = UText.build(title, fontConfig);
            XDimension2D textDim = txt.calculateDimension(ug.getStringBounder());
            double textX = x + (width - textDim.getWidth()) / 2;
            double textY = y + textDim.getHeight() + 5;
            ug.apply(new UTranslate(textX, textY)).draw(txt);
        }
    }

    public static void drawBlock(UGraphic ug, double x, double y, double width, double height, String text) {
        // Draw full-width rectangle
        URectangle rect = URectangle.build(width, height);
        ug.apply(new UTranslate(x, y))
          .apply(HColors.WHITE.bg())
          .apply(HColors.BLACK)
          .draw(rect);

        drawCenteredText(ug, x, y, width, height, text);
    }

    public static void drawIfHeader(UGraphic ug, double x, double y, double width, double height, String condition) {
        // Draw full-width rectangle
        URectangle rect = URectangle.build(width, height);
        ug.apply(new UTranslate(x, y))
          .apply(HColors.WHITE.bg())
          .apply(HColors.BLACK)
          .draw(rect);

        // Draw diagonal divider from top-left to bottom-right
        ULine diagonal = new ULine(width, height);
        ug.apply(new UTranslate(x, y))
          .apply(HColors.BLACK)
          .draw(diagonal);

        // Draw condition text in top-left area
        drawText(ug, x + 10, y + height/2, condition);
        
        // Draw true/false labels
        drawText(ug, x + width/4, y + height*3/4, "True");
        drawText(ug, x + width*3/4, y + height*3/4, "False");
    }

    public static void drawWhileHeader(UGraphic ug, double x, double y, double width, double height, String condition) {
        // Draw full-width rectangle
        URectangle rect = URectangle.build(width, height);
        ug.apply(new UTranslate(x, y))
          .apply(HColors.WHITE.bg())
          .apply(HColors.BLACK)
          .draw(rect);

        // Draw horizontal divider
        ULine divider = new ULine(width, 0);
        ug.apply(new UTranslate(x, y + height/2))
          .apply(HColors.BLACK)
          .draw(divider);

        // Draw condition text
        drawCenteredText(ug, x, y, width, height/2, condition);
        drawCenteredText(ug, x, y + height/2, width, height/2, "REPEAT");
    }

    public static void drawCase(UGraphic ug, double x, double y, double width, double height, 
                              String condition, String[] cases) {
        // Draw main rectangle
        URectangle rect = URectangle.build(width, height);
        ug.apply(new UTranslate(x, y))
          .apply(HColors.WHITE.bg())
          .apply(HColors.BLACK)
          .draw(rect);

        // Draw condition at top
        double headerHeight = height / (cases.length + 1);
        drawCenteredText(ug, x, y, width, headerHeight, condition);

        // Draw diagonal dividers for cases
        double caseWidth = width / cases.length;
        for (int i = 0; i < cases.length - 1; i++) {
            ULine diagonal = new ULine(caseWidth, height - headerHeight);
            ug.apply(new UTranslate(x + caseWidth * i, y + headerHeight))
              .apply(HColors.BLACK)
              .draw(diagonal);
            
            // Draw case label
            drawCenteredText(ug, x + caseWidth * i, y + headerHeight, 
                           caseWidth, height - headerHeight, cases[i]);
        }
    }

    public static void drawVerticalDivider(UGraphic ug, double x, double y, double height) {
        ULine line = new ULine(0, height);
        ug.apply(new UTranslate(x, y))
          .apply(HColors.BLACK)
          .draw(line);
    }

    private static void drawCenteredText(UGraphic ug, double x, double y, double width, double height, String text) {
        FontConfiguration fontConfig = FontConfiguration.blackBlueTrue(UFont.sansSerif(12));
        UText txt = UText.build(text, fontConfig);
        XDimension2D textDim = txt.calculateDimension(ug.getStringBounder());
        
        double textX = x + (width - textDim.getWidth()) / 2;
        double textY = y + (height + textDim.getHeight()) / 2;
        ug.apply(new UTranslate(textX, textY)).draw(txt);
    }

    private static void drawText(UGraphic ug, double x, double y, String text) {
        FontConfiguration fontConfig = FontConfiguration.blackBlueTrue(UFont.sansSerif(12));
        UText txt = UText.build(text, fontConfig);
        ug.apply(new UTranslate(x, y)).draw(txt);
    }
} 