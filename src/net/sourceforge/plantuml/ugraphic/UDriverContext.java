package net.sourceforge.plantuml.ugraphic;

import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.ugraphic.color.ColorMapper;
import net.sourceforge.plantuml.ugraphic.color.HColor;

public interface UDriverContext {

	public HColor getBackcolor();

	public HColor getColor();
	
	public double getDpiFactor();
	
	public ColorMapper getColorMapper();

	public UPattern getPattern();

	public StringBounder getStringBounder();

	public UStroke getStroke();
	
	public boolean isHidden();

	public void ensureVisible(double x, double y);
}
