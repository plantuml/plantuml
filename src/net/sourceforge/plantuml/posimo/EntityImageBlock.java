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
 * Revision $Revision: 4236 $
 * 
 */
package net.sourceforge.plantuml.posimo;

import java.awt.Color;
import java.awt.geom.Dimension2D;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import net.sourceforge.plantuml.ColorParam;
import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.FontParam;
import net.sourceforge.plantuml.ISkinParam;
import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.cucadiagram.EntityType;
import net.sourceforge.plantuml.cucadiagram.IEntity;
import net.sourceforge.plantuml.cucadiagram.Link;
import net.sourceforge.plantuml.cucadiagram.dot.PlayField;
import net.sourceforge.plantuml.graphic.HorizontalAlignement;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.graphic.TextBlockUtils;
import net.sourceforge.plantuml.skin.rose.Rose;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UGraphicUtils;
import net.sourceforge.plantuml.ugraphic.URectangle;

public class EntityImageBlock implements IEntityImageBlock {

	private final IEntity entity;
	private final ISkinParam param;
	private final Rose rose;
	private final int margin = 6;
	private final TextBlock name;
	private final Collection<Link> links;

	private PlayField playField;
	private Frame frame;

	public EntityImageBlock(IEntity entity, Rose rose, ISkinParam param, Collection<Link> links) {
		this.entity = entity;
		this.param = param;
		this.rose = rose;
		this.links = links;
		this.name = TextBlockUtils.create(StringUtils.getWithNewlines(entity.getDisplay()), param
				.getFont(FontParam.CLASS), Color.BLACK, HorizontalAlignement.CENTER);

	}

	public Dimension2D getDimension(StringBounder stringBounder) {
		initPlayField(stringBounder);
		Dimension2D dim;
		if (playField == null) {
			dim = name.calculateDimension(stringBounder);
		} else {
			try {
				dim = playField.solve();
				// final double frameWidth =
				// frame.getPreferredWidth(stringBounder);
				final double frameHeight = frame.getPreferredHeight(stringBounder);
				dim = Dimension2DDouble.delta(dim, 0, frameHeight);
			} catch (IOException e) {
				e.printStackTrace();
				throw new RuntimeException();
			} catch (InterruptedException e) {
				e.printStackTrace();
				throw new RuntimeException();
			}
		}
		return Dimension2DDouble.delta(dim, margin * 2);
	}

	private void initPlayField(StringBounder stringBounder) {
		if (playField != null || entity.getParent() == null || entity.getType() != EntityType.GROUP) {
			return;
		}
		this.playField = new PlayField(param);
		final Collection<IEntity> entities = new ArrayList<IEntity>();
		for (IEntity ent : entity.getParent().entities().values()) {
			//entities.add(EntityUtils.withNoParent(ent));
			entities.add(ent);
		}
		playField.initInternal(entities, links, stringBounder);

		this.frame = new Frame(StringUtils.getWithNewlines(entity.getDisplay()), Color.BLACK, param
				.getFont(FontParam.CLASS), rose.getHtmlColor(param, ColorParam.classBorder).getColor());

	}

	public void drawU(UGraphic ug, double xTheoricalPosition, double yTheoricalPosition, double marginWidth,
			double marginHeight) {
		final Dimension2D dim = getDimension(ug.getStringBounder());

		final double widthTotal = dim.getWidth() + 2 * marginWidth;
		final double heightTotal = dim.getHeight() + 2 * marginHeight;
		final URectangle rect = new URectangle(widthTotal, heightTotal);

		//if (entity.getParent() == null) {
		if (entity.getType() != EntityType.GROUP) {
			ug.getParam().setBackcolor(rose.getHtmlColor(param, ColorParam.classBackground).getColor());
			ug.getParam().setColor(rose.getHtmlColor(param, ColorParam.classBorder).getColor());
			ug.draw(xTheoricalPosition - marginWidth, yTheoricalPosition - marginHeight, rect);
			name.drawU(ug, xTheoricalPosition + margin, yTheoricalPosition + margin);
		} else {
			final Frame frame = new Frame(StringUtils.getWithNewlines(entity.getDisplay()), Color.BLACK, param
					.getFont(FontParam.CLASS), rose.getHtmlColor(param, ColorParam.classBorder).getColor());

			ug.getParam().setBackcolor(rose.getHtmlColor(param, ColorParam.background).getColor());
			ug.getParam().setColor(null);
			ug.draw(xTheoricalPosition - marginWidth, yTheoricalPosition - marginWidth, rect);

			final double oldX = ug.getTranslateX();
			final double oldY = ug.getTranslateY();
			ug.translate(xTheoricalPosition - marginWidth, yTheoricalPosition - marginHeight);
			frame.drawU(ug, new Dimension2DDouble(widthTotal, heightTotal), null);
			// ug.translate(-xTheoricalPosition + marginWidth,
			// -yTheoricalPosition + marginHeight);
			ug.setTranslate(oldX, oldY);

			playField.drawInternal(UGraphicUtils.translate(ug, xTheoricalPosition + margin, yTheoricalPosition + margin
					+ frame.getPreferredHeight(ug.getStringBounder())));
		}
	}
}
