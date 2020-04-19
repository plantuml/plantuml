/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2020, Arnaud Roques
 *
 * Project Info:  http://plantuml.com
 * 
 * If you like this project or if you find it useful, you can support us at:
 * 
 * http://plantuml.com/patreon (only 1$ per month!)
 * http://plantuml.com/paypal
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
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public
 * License for more details.
 *
 * You should have received a copy of the GNU General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301,
 * USA.
 *
 *
 * Original Author:  Arnaud Roques
 * 
 *
 */
package net.sourceforge.plantuml.sequencediagram.graphic;

import java.awt.geom.Dimension2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sourceforge.plantuml.Dimension2DDouble;
import net.sourceforge.plantuml.ISkinParam;
import net.sourceforge.plantuml.LineParam;
import net.sourceforge.plantuml.SkinParamBackcolored;
import net.sourceforge.plantuml.Url;
import net.sourceforge.plantuml.graphic.InnerStrategy;
import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.sequencediagram.Englober;
import net.sourceforge.plantuml.sequencediagram.Event;
import net.sourceforge.plantuml.sequencediagram.Newpage;
import net.sourceforge.plantuml.sequencediagram.Participant;
import net.sourceforge.plantuml.sequencediagram.ParticipantEnglober;
import net.sourceforge.plantuml.skin.Area;
import net.sourceforge.plantuml.skin.Component;
import net.sourceforge.plantuml.skin.ComponentType;
import net.sourceforge.plantuml.skin.Context2D;
import net.sourceforge.plantuml.skin.SimpleContext2D;
import net.sourceforge.plantuml.skin.rose.Rose;
import net.sourceforge.plantuml.ugraphic.MinMax;
import net.sourceforge.plantuml.ugraphic.UClip;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UStroke;
import net.sourceforge.plantuml.ugraphic.UTranslate;
import net.sourceforge.plantuml.ugraphic.txt.UGraphicTxt;

public class DrawableSet {

	private final Map<Participant, LivingParticipantBox> participants = new LinkedHashMap<Participant, LivingParticipantBox>();
	private final Map<Event, GraphicalElement> events = new HashMap<Event, GraphicalElement>();
	private final Map<Participant, ParticipantEnglober> participantEnglobers2 = new LinkedHashMap<Participant, ParticipantEnglober>();

	private final List<Event> eventsList = new ArrayList<Event>();
	private final Rose skin;
	private final ISkinParam skinParam;
	private Dimension2D dimension;
	private double topStartingY;

	DrawableSet(Rose skin, ISkinParam skinParam) {
		if (skin == null) {
			throw new IllegalArgumentException();
		}
		if (skinParam == null) {
			throw new IllegalArgumentException();
		}
		this.skin = skin;
		this.skinParam = skinParam;
	}

	public ParticipantBox getVeryfirst() {
		return participants.values().iterator().next().getParticipantBox();
	}

	public final Rose getSkin() {
		return skin;
	}

	public final ISkinParam getSkinParam() {
		return skinParam;
	}

	public Collection<Event> getAllEvents() {
		return Collections.unmodifiableCollection(eventsList);
	}

	public Set<Participant> getAllParticipants() {
		return Collections.unmodifiableSet(participants.keySet());
	}

	public Collection<LivingParticipantBox> getAllLivingParticipantBox() {
		return Collections.unmodifiableCollection(participants.values());
	}

	public Collection<GraphicalElement> getAllGraphicalElements() {
		final Collection<GraphicalElement> result = new ArrayList<GraphicalElement>();
		for (Event ev : eventsList) {
			result.add(events.get(ev));
		}
		return Collections.unmodifiableCollection(result);
	}

	public LivingParticipantBox getLivingParticipantBox(Participant p) {
		return participants.get(p);
	}

	public GraphicalElement getEvent(Event ev) {
		return events.get(ev);
	}

	public double getHeadHeight(StringBounder stringBounder) {
		double r = 0;
		for (Participant p : participants.keySet()) {
			final double y = getHeadAndEngloberHeight(p, stringBounder);
			r = Math.max(r, y);
		}
		return r;
	}

	public double getHeadAndEngloberHeight(Participant p, StringBounder stringBounder) {
		final LivingParticipantBox box = participants.get(p);
		final double height = box.getParticipantBox().getHeadHeight(stringBounder);
		final Englober englober = getParticipantEnglober(p, stringBounder);
		if (englober == null) {
			return height;
		}
		final Component comp = skin.createComponent(englober.getUsedStyles(), ComponentType.ENGLOBER, null, skinParam,
				englober.getParticipantEnglober().getTitle());
		final double heightEnglober = comp.getPreferredHeight(stringBounder);
		return height + heightEnglober;
	}

	public List<Englober> getExistingParticipantEnglober(StringBounder stringBounder) {
		final List<Englober> result = new ArrayList<Englober>();
		Englober pending = null;
		for (Map.Entry<Participant, ParticipantEnglober> ent : participantEnglobers2.entrySet()) {
			final ParticipantEnglober englober = ent.getValue();
			if (englober == null) {
				pending = null;
				continue;
			}
			assert englober != null;
			if (pending != null && englober == pending.getParticipantEnglober()) {
				pending.add(ent.getKey());
				continue;
			}
			pending = Englober.createPuma(englober, ent.getKey(), getSkinParam(), skin, stringBounder,
					skinParam.getCurrentStyleBuilder());
			result.add(pending);
		}
		return Collections.unmodifiableList(result);
	}

	public double getOffsetForEnglobers(StringBounder stringBounder) {
		double result = 0;
		for (Englober englober : getExistingParticipantEnglober(stringBounder)) {
			final Component comp = skin.createComponent(null, ComponentType.ENGLOBER, null, skinParam, englober
					.getParticipantEnglober().getTitle());
			final double height = comp.getPreferredHeight(stringBounder);
			if (height > result) {
				result = height;
			}
		}
		return result;
	}

	static private final int MARGIN_FOR_ENGLOBERS = 4;
	static private final int MARGIN_FOR_ENGLOBERS1 = 2;

	public double getTailHeight(StringBounder stringBounder, boolean showTail) {
		final double marginForEnglobers = getExistingParticipantEnglober(stringBounder).size() > 0 ? MARGIN_FOR_ENGLOBERS
				: 0;

		if (showTail == false) {
			return 1 + marginForEnglobers;
		}
		double r = 0;
		for (LivingParticipantBox livingParticipantBox : participants.values()) {
			final double y = livingParticipantBox.getParticipantBox().getTailHeight(stringBounder);
			r = Math.max(r, y);
		}
		return r + marginForEnglobers;
	}

	public void addParticipant(Participant p, ParticipantEnglober participantEnglober) {
		participants.put(p, null);
		participantEnglobers2.put(p, participantEnglober);
	}

	public void setLivingParticipantBox(Participant p, LivingParticipantBox box) {
		if (participants.containsKey(p) == false) {
			throw new IllegalArgumentException();
		}
		participants.put(p, box);
	}

	public void addEvent(Event event, GraphicalElement object) {
		if (events.keySet().contains(event) == false) {
			eventsList.add(event);
		}
		events.put(event, object);
	}

	public void addEvent(Newpage newpage, GraphicalNewpage object, Event justBefore) {
		final int idx = eventsList.indexOf(justBefore);
		if (idx == -1) {
			throw new IllegalArgumentException();
		}
		eventsList.add(idx, newpage);
		events.put(newpage, object);
		assert events.size() == eventsList.size();
	}

	void setDimension(Dimension2D dim) {
		if (dimension != null) {
			throw new IllegalStateException();
		}
		this.dimension = dim;
	}

	public Dimension2D getDimension() {
		return dimension;
	}

	TextBlock asTextBlock(final double delta, final double width, final Page page, final boolean showTail) {
		return new TextBlock() {

			public void drawU(UGraphic ug) {
				drawU22(ug, delta, width, page, showTail);
			}

			public Dimension2D calculateDimension(StringBounder stringBounder) {
				final double height = page.getHeight();
				return new Dimension2DDouble(width, height);
			}

			public MinMax getMinMax(StringBounder stringBounder) {
				throw new UnsupportedOperationException();
			}

			public Rectangle2D getInnerPosition(String member, StringBounder stringBounder, InnerStrategy strategy) {
				throw new UnsupportedOperationException();
			}

		};

	}

	void drawU22(final UGraphic ug, final double delta, double width, Page page, boolean showTail) {
		final double height = page.getHeight();

		final UGraphic ugTranslated = clipAndTranslate2(delta, width, page, ug);
		final SimpleContext2D context = new SimpleContext2D(true);
		this.drawEnglobers(ug, height - MARGIN_FOR_ENGLOBERS1, context);
		this.drawPlaygroundU(ugTranslated, context);

		this.drawLineU22(ug, showTail, page);
		this.drawHeadTailU(ug, page, showTail ? height - getTailHeight(ug.getStringBounder(), true) : 0);

		this.drawPlaygroundU(ugTranslated, new SimpleContext2D(false));
	}

	private UGraphic clipAndTranslate2(final double delta, double width, Page p, UGraphic ug) {
		ug = ug.apply(new UClip(0, p.getBodyRelativePosition(), width, p.getBodyHeight() + 1));
		ug = ug.apply(getTranslate4(delta));
		return ug;
	}

	private UTranslate getTranslate4(final double delta) {
		if (delta > 0) {
			return UTranslate.dy(-delta);
		}
		return new UTranslate();
	}

	private void drawLineU22(UGraphic ug, boolean showTail, Page page) {
		// http://plantuml.sourceforge.net/qa/?qa=4826/lifelines-broken-for-txt-seq-diagrams-when-create-is-used
		final boolean isTxt = ug instanceof UGraphicTxt;
		for (LivingParticipantBox box : getAllLivingParticipantBox()) {
			final double create = box.getCreate();
			final double startMin = page.getBodyRelativePosition() - box.magicMargin(ug.getStringBounder());
			final double endMax = startMin + page.getBodyHeight() + 2 * box.magicMargin(ug.getStringBounder());
			double start = startMin;
			if (create > 0) {
				if (create > page.getNewpage2()) {
					continue;
				}
				if (create >= page.getNewpage1() && create < page.getNewpage2()) {
					if (isTxt) {
						start = (int) create;
					} else {
						start += create - page.getNewpage1() + 2 * box.magicMargin(ug.getStringBounder());
					}
				}
			}
			final double myDelta = page.getNewpage1() - page.getHeaderHeight();
			box.drawLineU22(ug, start, endMax, showTail, myDelta);
		}
	}

	private void drawHeadTailU(UGraphic ug, Page page, double positionTail) {
		for (Map.Entry<Participant, LivingParticipantBox> ent : participants.entrySet()) {
			final Participant p = ent.getKey();
			final LivingParticipantBox box = ent.getValue();
			final double create = box.getCreate();
			boolean showHead = true;
			if (create > 0) {
				if (create > page.getNewpage2()) {
					continue;
				}
				if (create >= page.getNewpage1() && create < page.getNewpage2()) {
					showHead = false;
				}
			}
			final Url url = p.getUrl();
			if (url != null) {
				ug.startUrl(url);
			}
			box.getParticipantBox().drawHeadTailU(ug, topStartingY, showHead, positionTail);
			if (url != null) {
				ug.closeAction();
			}
		}
	}

	private double getMaxX() {
		return dimension.getWidth();
	}

	private double getMaxY() {
		return dimension.getHeight();
	}

	private void drawPlaygroundU(UGraphic ug, Context2D context) {
		for (Participant p : getAllParticipants()) {
			drawLifeLineU(ug, p);
		}

		for (GraphicalElement element : getAllGraphicalElements()) {
			element.drawU(ug, getMaxX(), context);
		}
	}

	private void drawEnglobers(UGraphic ug, double height, Context2D context) {
		for (Englober englober : getExistingParticipantEnglober(ug.getStringBounder())) {
			double x1 = getX1(englober);
			final double x2 = getX2(ug.getStringBounder(), englober);

			final Component comp = getEngloberComponent(englober);

			final double width = x2 - x1;
			final double preferedWidth = getEngloberPreferedWidth(ug.getStringBounder(), englober);
			if (preferedWidth > width) {
				// if (englober.getFirst2() == englober.getLast2()) {
				x1 -= (preferedWidth - width) / 2;
				// }
				final Dimension2DDouble dim = new Dimension2DDouble(preferedWidth, height);
				comp.drawU(ug.apply(new UTranslate(x1, 1)), new Area(dim), context);
			} else {
				final Dimension2DDouble dim = new Dimension2DDouble(width, height);
				comp.drawU(ug.apply(new UTranslate(x1, 1)), new Area(dim), context);
			}
		}
	}

	public double getEngloberPreferedWidth(StringBounder stringBounder, Englober englober) {
		return getEngloberComponent(englober).getPreferredWidth(stringBounder);
	}

	private Component getEngloberComponent(Englober englober) {
		final ParticipantEnglober participantEnglober = englober.getParticipantEnglober();
		final ISkinParam s = participantEnglober.getBoxColor() == null ? skinParam : new SkinParamBackcolored(
				skinParam, participantEnglober.getBoxColor());
		return skin.createComponent(englober.getUsedStyles(), ComponentType.ENGLOBER, null, s,
				participantEnglober.getTitle());
	}

	public double getX1(Englober englober) {
		final Participant first = englober.getFirst2TOBEPRIVATE();
		final ParticipantBox firstBox = participants.get(first).getParticipantBox();
		return firstBox.getStartingX() + 1;
	}

	public double getX2(StringBounder stringBounder, Englober englober) {
		final Participant last = englober.getLast2TOBEPRIVATE();
		final ParticipantBox lastBox = participants.get(last).getParticipantBox();
		return lastBox.getMaxX(stringBounder) - 1;
	}

	private void drawLifeLineU(UGraphic ug, Participant p) {
		final LifeLine line = getLivingParticipantBox(p).getLifeLine();

		line.finish(getMaxY());
		line.drawU(ug, getSkin(), skinParam);
	}

	private Englober getParticipantEnglober(Participant p, StringBounder stringBounder) {
		for (Englober pe : getExistingParticipantEnglober(stringBounder)) {
			if (pe.contains(p)) {
				return pe;
			}
		}
		return null;
	}

	public void setTopStartingY(double topStartingY) {
		this.topStartingY = topStartingY;
	}

	Participant getFirst(Collection<Participant> someParticipants) {
		final List<Participant> list = new ArrayList<Participant>(participants.keySet());
		int min = -1;
		for (Participant p : someParticipants) {
			final int n = list.indexOf(p);
			assert n != -1;
			if (min == -1 || min > n) {
				min = n;
			}
		}
		return list.get(min);
	}

	Participant getLast(Collection<Participant> someParticipants) {
		final List<Participant> list = new ArrayList<Participant>(participants.keySet());
		int max = -1;
		for (Participant p : someParticipants) {
			final int n = list.indexOf(p);
			assert n != -1;
			if (max == -1 || max < n) {
				max = n;
			}
		}
		return list.get(max);
	}

	public double getArrowThickness() {
		final UStroke result = skinParam.getThickness(LineParam.sequenceArrow, null);
		if (result == null) {
			return 1;
		}
		return result.getThickness();
	}

}
