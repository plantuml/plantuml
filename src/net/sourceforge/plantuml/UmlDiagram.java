/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009, Arnaud Roques
 *
 * Project Info:  http://plantuml.sourceforge.net
 * 
 * This file is part of PlantUML.
 *
 * PlantUML is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * PlantUML distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public
 * License for more details.
 *
 * You should have received a copy of the GNU General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301,
 * USA.
 *
 * [Java is a trademark or registered trademark of Sun Microsystems, Inc.
 * in the United States and other countries.]
 *
 * Original Author:  Arnaud Roques
 *
 * Revision $Revision: 5811 $
 *
 */
package net.sourceforge.plantuml;

import java.util.List;

import net.sourceforge.plantuml.graphic.HorizontalAlignement;

public abstract class UmlDiagram extends AbstractPSystem implements PSystem {

	private boolean rotation;

	private int minwidth = Integer.MAX_VALUE;

	private List<String> title;
	private List<String> header;
	private List<String> footer;
	private HorizontalAlignement headerAlignement = HorizontalAlignement.RIGHT;
	private HorizontalAlignement footerAlignement = HorizontalAlignement.CENTER;
	private final Pragma pragma = new Pragma();
	private Scale scale;

	private final SkinParam skinParam = new SkinParam();

	final public void setTitle(List<String> strings) {
		this.title = strings;
	}

	final public List<String> getTitle() {
		return title;
	}

	final public int getMinwidth() {
		return minwidth;
	}

	final public void setMinwidth(int minwidth) {
		this.minwidth = minwidth;
	}

	final public boolean isRotation() {
		return rotation;
	}

	final public void setRotation(boolean rotation) {
		this.rotation = rotation;
	}

	public final ISkinParam getSkinParam() {
		return skinParam;
	}

	public void setParam(String key, String value) {
		skinParam.setParam(key.toLowerCase(), value);
	}

	public final List<String> getHeader() {
		return header;
	}

	public final void setHeader(List<String> header) {
		this.header = header;
	}

	public final List<String> getFooter() {
		return footer;
	}

	public final void setFooter(List<String> footer) {
		this.footer = footer;
	}

	public final HorizontalAlignement getHeaderAlignement() {
		return headerAlignement;
	}

	public final void setHeaderAlignement(HorizontalAlignement headerAlignement) {
		this.headerAlignement = headerAlignement;
	}

	public final HorizontalAlignement getFooterAlignement() {
		return footerAlignement;
	}

	public final void setFooterAlignement(HorizontalAlignement footerAlignement) {
		this.footerAlignement = footerAlignement;
	}

	abstract public UmlDiagramType getUmlDiagramType();

	public Pragma getPragma() {
		return pragma;
	}

	final public void setScale(Scale scale) {
		this.scale = scale;
	}

	final public Scale getScale() {
		return scale;
	}

	public final double getDpiFactor(FileFormatOption fileFormatOption) {
		if (getSkinParam().getDpi() == 96) {
			return 1.0;
		}
		return getSkinParam().getDpi() / 96.0;
	}

	public final int getDpi(FileFormatOption fileFormatOption) {
		return getSkinParam().getDpi();
	}

}
