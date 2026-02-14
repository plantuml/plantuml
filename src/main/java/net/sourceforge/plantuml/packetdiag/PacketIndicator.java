package net.sourceforge.plantuml.packetdiag;

import java.util.Collections;

import net.sourceforge.plantuml.klimt.LineBreakStrategy;
import net.sourceforge.plantuml.klimt.UStroke;
import net.sourceforge.plantuml.klimt.UTranslate;
import net.sourceforge.plantuml.klimt.color.HColor;
import net.sourceforge.plantuml.klimt.creole.CreoleMode;
import net.sourceforge.plantuml.klimt.creole.Display;
import net.sourceforge.plantuml.klimt.drawing.UGraphic;
import net.sourceforge.plantuml.klimt.font.FontConfiguration;
import net.sourceforge.plantuml.klimt.font.StringBounder;
import net.sourceforge.plantuml.klimt.geom.HorizontalAlignment;
import net.sourceforge.plantuml.klimt.geom.XDimension2D;
import net.sourceforge.plantuml.klimt.shape.AbstractTextBlock;
import net.sourceforge.plantuml.klimt.shape.TextBlock;
import net.sourceforge.plantuml.nwdiag.VerticalLine;
import net.sourceforge.plantuml.style.ISkinParam;
import net.sourceforge.plantuml.style.Style;

public class PacketIndicator extends AbstractTextBlock {

	public static final double V_LINE_FULL = 32D;

	public static final double V_LINE_SHORT = V_LINE_FULL / 2;

	private final boolean full;

	private final boolean numbered;

	private final String bitNumber;

	private final Style style;

	private final ISkinParam skinParam;

	public PacketIndicator(boolean full, boolean numbered, int bitNumber, Style style, ISkinParam skinParam) {
		this.full = full;
		this.numbered = numbered;
		this.style = style;
		this.bitNumber = bitNumber + "";
		this.skinParam = skinParam;
	}

	@Override
	public void drawU(UGraphic ug) {
		TextBlock numberTb = numberTb();
		// number dimension is always calculated for consistent padding
		XDimension2D numberDim = numberTb.calculateDimension(ug.getStringBounder());

		double lineOffsetY = full ? 0 : V_LINE_SHORT;
		double lineLength = full ? V_LINE_FULL : V_LINE_SHORT;
		if (numbered) {
			double numberYOffset = full ? 0 : V_LINE_SHORT;
			numberTb.drawU(ug.apply(new UTranslate(-numberDim.getWidth() / 2, numberYOffset)));
		}
		TextBlock vLineTb = vLineTb(lineOffsetY, lineLength);
		vLineTb.drawU(ug.apply(UTranslate.dy(numberDim.getHeight())));
	}

	@Override
	public XDimension2D calculateDimension(StringBounder stringBounder) {
		XDimension2D numberDim = numberTb().calculateDimension(stringBounder);
		double vLineHeight = full ? V_LINE_FULL : V_LINE_SHORT;
		return numberDim.mergeTB(new XDimension2D(0D, vLineHeight));
	}

	TextBlock numberTb() {
		FontConfiguration fg = style.getFontConfiguration(skinParam.getIHtmlColorSet());
		return createNumberTextBlock(fg, skinParam, bitNumber);
	}

	TextBlock vLineTb(double y, double height) {
		return createVLineTextBlock(style, skinParam, y, height);
	}

	public static TextBlock createNumberTextBlock(FontConfiguration fg, ISkinParam skinParam, CharSequence... s) {
		return Display.create(s).create8(
						fg,
						HorizontalAlignment.LEFT,
						skinParam,
						CreoleMode.NO_CREOLE,
						LineBreakStrategy.NONE
		);
	}

	public static TextBlock createVLineTextBlock(Style style, ISkinParam skinParam, double y, double height) {
		final HColor lineColor = style.getSymbolContext(skinParam.getIHtmlColorSet()).getForeColor();
		final UStroke stroke = style.getStroke();
		return new AbstractTextBlock() {
			@Override
			public XDimension2D calculateDimension(StringBounder stringBounder) {
				return new XDimension2D(stroke.getThickness(), height);
			}

			@Override
			public void drawU(UGraphic ug) {
				ug = ug.apply(lineColor).apply(stroke);
				VerticalLine vLine = new VerticalLine(y, y + height, Collections.emptySet());
				vLine.drawU(ug);
			}
		};
	}
}
