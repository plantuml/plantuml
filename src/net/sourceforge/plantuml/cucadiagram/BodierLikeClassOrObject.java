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
package net.sourceforge.plantuml.cucadiagram;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import net.sourceforge.plantuml.FontParam;
import net.sourceforge.plantuml.ISkinParam;
import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.UrlBuilder;
import net.sourceforge.plantuml.creole.legacy.CreoleParser;
import net.sourceforge.plantuml.graphic.FontConfiguration;
import net.sourceforge.plantuml.graphic.HorizontalAlignment;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.graphic.TextBlockLineBefore;
import net.sourceforge.plantuml.graphic.TextBlockUtils;
import net.sourceforge.plantuml.skin.VisibilityModifier;
import net.sourceforge.plantuml.style.Style;

public class BodierLikeClassOrObject implements Bodier {

	private final List<CharSequence> rawBody = new ArrayList<>();
	private final Set<VisibilityModifier> hides;
	private LeafType type;
	private List<Member> methodsToDisplay;
	private List<Member> fieldsToDisplay;
	private ILeaf leaf;

	@Override
	public void muteClassToObject() {
		methodsToDisplay = null;
		fieldsToDisplay = null;
		type = LeafType.OBJECT;
	}

	BodierLikeClassOrObject(LeafType type, Set<VisibilityModifier> hides) {
		if (type == LeafType.MAP) {
			throw new IllegalArgumentException();
		}
		this.type = Objects.requireNonNull(type);
		assert type.isLikeClass() || type == LeafType.OBJECT;
		this.hides = hides;
	}

	@Override
	public void setLeaf(ILeaf leaf) {
		this.leaf = Objects.requireNonNull(leaf);

	}

	@Override
	public void addFieldOrMethod(String s) {
		// Empty cache
		methodsToDisplay = null;
		fieldsToDisplay = null;
		rawBody.add(s);
	}

	private boolean isBodyEnhanced() {
		for (CharSequence s : rawBody) {
			if (BodyEnhanced1.isBlockSeparator(s) || CreoleParser.isTableLine(s.toString())) {
				return true;
			}
		}
		return false;
	}

	private boolean isMethod(CharSequence s) {
		final String purged = s.toString().replaceAll(UrlBuilder.getRegexp(), "");
		if (purged.contains("{method}")) {
			return true;
		}
		if (purged.contains("{field}")) {
			return false;
		}
		return purged.contains("(") || purged.contains(")");
	}

	@Override
	public Display getMethodsToDisplay() {
		if (methodsToDisplay == null) {
			methodsToDisplay = new ArrayList<>();
			for (int i = 0; i < rawBody.size(); i++) {
				final CharSequence s = rawBody.get(i);
				if (isMethod(i, rawBody) == false) {
					continue;
				}
				if (s.length() == 0 && methodsToDisplay.size() == 0) {
					continue;
				}
				final Member m = Member.method(s);
				if (hides == null || hides.contains(m.getVisibilityModifier()) == false) {
					methodsToDisplay.add(m);
				}
			}
			removeFinalEmptyMembers(methodsToDisplay);
		}
		return Display.create(methodsToDisplay);
	}

	private boolean isMethod(int i, List<CharSequence> rawBody) {
		if (i > 0 && i < rawBody.size() - 1 && rawBody.get(i).length() == 0 && isMethod(rawBody.get(i - 1))
				&& isMethod(rawBody.get(i + 1))) {
			return true;
		}
		return isMethod(rawBody.get(i));
	}

	@Override
	public Display getFieldsToDisplay() {
		if (fieldsToDisplay == null) {
			fieldsToDisplay = new ArrayList<>();
			for (CharSequence s : rawBody) {
				if (type != LeafType.OBJECT && isMethod(s) == true) {
					continue;
				}
				if (s.length() == 0 && fieldsToDisplay.size() == 0) {
					continue;
				}
				final Member m = Member.field(s);
				if (hides == null || hides.contains(m.getVisibilityModifier()) == false) {
					fieldsToDisplay.add(m);
				}
			}
			removeFinalEmptyMembers(fieldsToDisplay);
		}
		return Display.create(fieldsToDisplay);
	}

	private void removeFinalEmptyMembers(List<Member> result) {
		while (result.size() > 0 && StringUtils.trin(result.get(result.size() - 1).getDisplay(false)).length() == 0) {
			result.remove(result.size() - 1);
		}
	}

	@Override
	public boolean hasUrl() {
		for (CharSequence cs : getFieldsToDisplay()) {
			if (cs instanceof Member) {
				final Member m = (Member) cs;
				if (m.hasUrl()) {
					return true;
				}
			}
		}
		for (CharSequence cs : getMethodsToDisplay()) {
			if (cs instanceof Member) {
				final Member m = (Member) cs;
				if (m.hasUrl()) {
					return true;
				}
			}
		}
		return false;
	}

	private List<CharSequence> rawBodyWithoutHidden() {
		final List<CharSequence> result = new ArrayList<>();
		for (CharSequence s : rawBody) {
			final Member m;
			if (isMethod(s)) {
				m = Member.method(s);
			} else {
				m = Member.field(s);
			}
			if (hides.contains(m.getVisibilityModifier()) == false) {
				result.add(m);
			}

		}
		return result;
	}

	@Override
	public TextBlock getBody(FontParam fontParam, ISkinParam skinParam, boolean showMethods, boolean showFields,
			Stereotype stereotype, Style style, FontConfiguration fontConfiguration) {

		if (BodyFactory.BODY3) {
			return new Body3(rawBody, fontParam, skinParam, stereotype, style);
		}

		if (type.isLikeClass() && isBodyEnhanced()) {
			if (showMethods || showFields) {
				return BodyFactory.create1(skinParam.getDefaultTextAlignment(HorizontalAlignment.LEFT),
						rawBodyWithoutHidden(), fontParam, skinParam, stereotype, leaf, style);
			}
			return null;
		}
		if (leaf == null) {
			throw new IllegalStateException();
		}
		if (type == LeafType.OBJECT) {
			if (showFields == false) {
				return new TextBlockLineBefore(TextBlockUtils.empty(0, 0));
			}
			return BodyFactory.create1(skinParam.getDefaultTextAlignment(HorizontalAlignment.LEFT),
					rawBodyWithoutHidden(), fontParam, skinParam, stereotype, leaf, style);
		}
		assert type.isLikeClass();

		final MethodsOrFieldsArea fields = new MethodsOrFieldsArea(getFieldsToDisplay(), fontParam, skinParam,
				stereotype, leaf, style);

		final MethodsOrFieldsArea methods = new MethodsOrFieldsArea(getMethodsToDisplay(), fontParam, skinParam,
				stereotype, leaf, style);
		if (showFields && showMethods == false) {
			return fields.asBlockMemberImpl();
		} else if (showMethods && showFields == false) {
			return methods.asBlockMemberImpl();
		} else if (showFields == false && showMethods == false) {
			return TextBlockUtils.empty(0, 0);
		}

		final TextBlock bb1 = fields.asBlockMemberImpl();
		final TextBlock bb2 = methods.asBlockMemberImpl();
		return TextBlockUtils.mergeTB(bb1, bb2, HorizontalAlignment.LEFT);
	}

	@Override
	public List<CharSequence> getRawBody() {
		return Collections.unmodifiableList(rawBody);
	}

}
