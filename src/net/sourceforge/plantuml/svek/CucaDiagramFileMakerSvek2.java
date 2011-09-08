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
 * Revision $Revision: 6711 $
 *
 */
package net.sourceforge.plantuml.svek;

import java.awt.Color;
import java.awt.geom.Dimension2D;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sourceforge.plantuml.ColorParam;
import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.EmptyImageBuilder;
import net.sourceforge.plantuml.FontParam;
import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.UmlDiagramType;
import net.sourceforge.plantuml.cucadiagram.Entity;
import net.sourceforge.plantuml.cucadiagram.EntityType;
import net.sourceforge.plantuml.cucadiagram.Group;
import net.sourceforge.plantuml.cucadiagram.GroupType;
import net.sourceforge.plantuml.cucadiagram.IEntity;
import net.sourceforge.plantuml.cucadiagram.Link;
import net.sourceforge.plantuml.cucadiagram.dot.DotData;
import net.sourceforge.plantuml.graphic.FontConfiguration;
import net.sourceforge.plantuml.graphic.GraphicStrings;
import net.sourceforge.plantuml.graphic.HorizontalAlignement;
import net.sourceforge.plantuml.graphic.HtmlColor;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.StringBounderUtils;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.graphic.TextBlockUtils;
import net.sourceforge.plantuml.skin.rose.Rose;
import net.sourceforge.plantuml.ugraphic.UFont;

public final class CucaDiagramFileMakerSvek2 {

	private final ColorSequence colorSequence = new ColorSequence();

	private final DotData dotData;

	static private final StringBounder stringBounder;

	static {
		final EmptyImageBuilder builder = new EmptyImageBuilder(10, 10, Color.WHITE);
		stringBounder = StringBounderUtils.asStringBounder(builder.getGraphics2D());
	}

	public CucaDiagramFileMakerSvek2(DotData dotData) {
		this.dotData = dotData;
	}

	protected UFont getFont(FontParam fontParam) {
		return getData().getSkinParam().getFont(fontParam, null);
	}

	private DotStringFactory dotStringFactory;
	private Map<IEntity, Shape> shapeMap;

	private String getShapeUid(IEntity ent) {
		final Shape result = shapeMap.get(ent);
		if (result == null && ent.getType() == EntityType.GROUP) {
			for (IEntity i : shapeMap.keySet()) {
				if (ent.getParent().getCode().equals(i.getCode())) {
					return shapeMap.get(i).getUid();
				}
			}
			if (result == null) {
				return "za" + ent.getParent().getUid2();
			}
		}
		String uid = result.getUid();
		if (result.isShielded()) {
			uid = uid + ":h";
		}
		return uid;
	}

	public Shape getShape(IEntity ent) {
		return shapeMap.get(ent);
	}

	public IEntityImage createFile(String... dotStrings) throws IOException, InterruptedException {

		dotStringFactory = new DotStringFactory(colorSequence, stringBounder, dotData.getUmlDiagramType());
		shapeMap = new HashMap<IEntity, Shape>();

		printGroups(null);
		printEntities(getUnpackagedEntities());

		// final Map<Link, Line> lineMap = new HashMap<Link, Line>();

		for (Link link : dotData.getLinks()) {
			final String shapeUid1 = getShapeUid(link.getEntity1());
			final String shapeUid2 = getShapeUid(link.getEntity2());

			String ltail = null;
			if (shapeUid1.startsWith("za")) {
				ltail = getCluster(link.getEntity1().getParent()).getClusterId();
			}
			String lhead = null;
			if (shapeUid2.startsWith("za")) {
				lhead = getCluster(link.getEntity2().getParent()).getClusterId();
			}
			final FontConfiguration labelFont = new FontConfiguration(dotData.getSkinParam().getFont(
					FontParam.ACTIVITY_ARROW, null), HtmlColor.BLACK);

			final Line line = new Line(shapeUid1, shapeUid2, link, colorSequence, ltail, lhead, dotData.getSkinParam(),
					stringBounder, labelFont);
			// lineMap.put(link, line);
			dotStringFactory.addLine(line);

			if (link.getEntity1().getType() == EntityType.NOTE && onlyOneLink(link.getEntity1())) {
				final Shape shape = shapeMap.get(link.getEntity1());
				((EntityImageNote) shape.getImage()).setOpaleLine(line, shape);
				line.setOpale(true);
			} else if (link.getEntity2().getType() == EntityType.NOTE && onlyOneLink(link.getEntity2())) {
				final Shape shape = shapeMap.get(link.getEntity2());
				((EntityImageNote) shape.getImage()).setOpaleLine(line, shape);
				line.setOpale(true);
			}
		}

		if (dotStringFactory.illegalDotExe()) {
			return error(dotStringFactory.getDotExe());
		}

		final Dimension2D dim = Dimension2DDouble.delta(dotStringFactory.solve(dotStrings), 10);
		final HtmlColor border;
		if (getData().getUmlDiagramType() == UmlDiagramType.STATE) {
			border = getColor(ColorParam.stateBorder, null);
		} else {
			border = getColor(ColorParam.packageBorder, null);
		}
		final SvekResult result = new SvekResult(dim, dotData, dotStringFactory, border);
		result.moveSvek(6, 0);
		return result;

	}

	private boolean onlyOneLink(IEntity ent) {
		return Link.onlyOneLink(ent, dotData.getLinks());
	}

	protected final HtmlColor getColor(ColorParam colorParam, String stereo) {
		return new Rose().getHtmlColor(dotData.getSkinParam(), colorParam, stereo);
	}

	private Cluster getCluster(Group g) {
		for (Cluster cl : dotStringFactory.getAllSubCluster()) {
			if (cl.getGroup() == g) {
				return cl;
			}
		}
		throw new IllegalArgumentException(g.toString());
	}

	private IEntityImage error(File dotExe) {

		final List<String> msg = new ArrayList<String>();
		msg.add("Dot Executable: " + dotExe);
		if (dotExe != null) {
			if (dotExe.exists() == false) {
				msg.add("File does not exist");
			} else if (dotExe.isDirectory()) {
				msg.add("It should be an executable, not a directory");
			} else if (dotExe.isFile() == false) {
				msg.add("Not a valid file");
			} else if (dotExe.canRead() == false) {
				msg.add("File cannot be read");
			}
		}
		msg.add("Cannot find Graphviz. You should try");
		msg.add(" ");
		msg.add("@startuml");
		msg.add("testdot");
		msg.add("@enduml");
		msg.add(" ");
		msg.add(" or ");
		msg.add(" ");
		msg.add("java -jar plantuml.jar -testdot");
		return new GraphicStrings(msg);
	}

	private void printEntities(Collection<? extends IEntity> entities2) {
		for (IEntity ent : entities2) {
			printEntity(ent);
		}
	}

	private void printEntity(IEntity ent) {
		final IEntityImage image = Shape.printEntity(ent, dotData);
		final Dimension2D dim = image.getDimension(stringBounder);
		final Shape shape = new Shape(image, image.getShapeType(), dim.getWidth(), dim.getHeight(), colorSequence, ent
				.isTop(), image.getShield());
		dotStringFactory.addShape(shape);
		shapeMap.put(ent, shape);
	}

	private Collection<IEntity> getUnpackagedEntities() {
		final List<IEntity> result = new ArrayList<IEntity>();
		for (IEntity ent : dotData.getEntities().values()) {
			if (ent.getParent() == dotData.getTopParent()) {
				result.add(ent);
			}
		}
		return result;
	}

	private void printGroups(Group parent) throws IOException {
		for (Group g : dotData.getGroupHierarchy().getChildrenGroups(parent)) {
			if (dotData.isEmpty(g) && g.getType() == GroupType.PACKAGE) {
				final IEntity folder = new Entity(g.getUid1(), g.getUid2(), g.getCode(), g.getDisplay(),
						EntityType.EMPTY_PACKAGE, null, null);
				printEntity(folder);
			} else {
				printGroup(g);
			}
		}
	}

	private void printGroup(Group g) throws IOException {
		if (g.getType() == GroupType.CONCURRENT_STATE) {
			return;
		}
		// final String stereo = g.getStereotype();

		int titleWidth = 0;
		int titleHeight = 0;

		final String label = g.getDisplay();
		TextBlock title = null;
		if (label != null) {
			final UFont font = getFont(g.getType() == GroupType.STATE ? FontParam.STATE : FontParam.PACKAGE);
			title = TextBlockUtils.create(StringUtils.getWithNewlines(label), new FontConfiguration(font,
					HtmlColor.BLACK), HorizontalAlignement.CENTER);
			final Dimension2D dimLabel = title.calculateDimension(stringBounder);
			titleWidth = (int) dimLabel.getWidth();
			titleHeight = (int) dimLabel.getHeight();
		}

		dotStringFactory.openCluster(g, titleWidth, titleHeight, title, isSpecialGroup(g));
		this.printEntities(g.entities().values());

		// sb.append("subgraph " + g.getUid() + " {");
		//
		// final UFont font =
		// getData().getSkinParam().getFont(getFontParamForGroup(), stereo);
		// sb.append("fontsize=\"" + font.getSize() + "\";");
		// final String fontFamily = font.getFamily(null);
		// if (fontFamily != null) {
		// sb.append("fontname=\"" + fontFamily + "\";");
		// }
		//
		// if (g.getDisplay() != null) {
		// sb.append("label=<" + manageHtmlIB(g.getDisplay(),
		// getFontParamForGroup(), stereo) + ">;");
		// }
		// final String fontColor =
		// getAsHtml(getData().getSkinParam().getFontHtmlColor(getFontParamForGroup(),
		// stereo));
		// sb.append("fontcolor=\"" + fontColor + "\";");
		//
		// if (getGroupBackColor(g) != null) {
		// sb.append("fillcolor=\"" + getAsHtml(getGroupBackColor(g)) + "\";");
		// }
		//
		// if (g.getType() == GroupType.STATE) {
		// sb.append("color=" + getColorString(ColorParam.stateBorder, stereo) +
		// ";");
		// } else {
		// sb.append("color=" + getColorString(ColorParam.packageBorder, stereo)
		// + ";");
		// }
		// sb.append("style=\"" + getStyle(g) + "\";");
		//
		printGroups(g);
		//
		// this.printEntities(sb, g.entities().values());
		// for (Link link : getData().getLinks()) {
		// eventuallySameRank(sb, g, link);
		// }
		// sb.append("}");

		dotStringFactory.closeCluster();
	}

	private boolean isSpecialGroup(Group g) {
		if (g.getType() == GroupType.STATE) {
			return true;
		}
		if (g.getType() == GroupType.CONCURRENT_STATE) {
			throw new IllegalStateException();
		}
		if (getData().isThereLink(g)) {
			return true;
		}
		return false;
	}

	private DotData getData() {
		return dotData;
	}

	private HtmlColor getGroupBackColor(Group g) {
		HtmlColor value = g.getBackColor();
		if (value == null) {
			value = getData().getSkinParam().getHtmlColor(ColorParam.packageBackground, null);
		}
		return value;
	}

}
