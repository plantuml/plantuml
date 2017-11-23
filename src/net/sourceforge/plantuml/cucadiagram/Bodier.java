/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2017, Arnaud Roques
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

import net.sourceforge.plantuml.FontParam;
import net.sourceforge.plantuml.ISkinParam;
import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.graphic.HorizontalAlignment;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.graphic.TextBlockLineBefore;
import net.sourceforge.plantuml.graphic.TextBlockUtils;
import net.sourceforge.plantuml.skin.VisibilityModifier;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class Bodier {

	private final List<String> rawBody = new ArrayList<>();
	private final Set<VisibilityModifier> hides;
	private LeafType type;
	private List<Member> methodsToDisplay;
	private List<Member> fieldsToDisplay;
	private final boolean manageModifier;
	private ILeaf leaf;

	public void muteClassToObject() {
		methodsToDisplay = null;
		fieldsToDisplay = null;
		type = LeafType.OBJECT;
	}

	public Bodier(LeafType type, Set<VisibilityModifier> hides) {
		this.hides = hides;
		this.type = type;
		this.manageModifier = type != null && type.manageModifier();
	}

	public void addFieldOrMethod(String s, IEntity leaf) {
		if (leaf == null) {
			throw new IllegalArgumentException();
		}
		// Empty cache
		methodsToDisplay = null;
		fieldsToDisplay = null;
		rawBody.add(s);
		if (leaf instanceof ILeaf) {
			if (this.leaf != null && this.leaf != leaf) {
				throw new IllegalArgumentException();
			}
			this.leaf = (ILeaf) leaf;
		}
	}

	private boolean isBodyEnhanced() {
		for (String s : rawBody) {
			if (BodyEnhanced.isBlockSeparator(s)) {
				return true;
			}
		}
		return false;
	}

	private boolean isMethod(String s) {
		return (type == LeafType.ANNOTATION
			|| type == LeafType.ABSTRACT_CLASS
			|| type == LeafType.CLASS
			|| type == LeafType.INTERFACE
			|| type == LeafType.ENUM)
			&& MemberImpl.isMethod(s);
	}

	public List<Member> getMethodsToDisplay() {
		if (methodsToDisplay == null) {
			methodsToDisplay = new ArrayList<>();
			for (int i = 0; i < rawBody.size(); i++) {
				final String s = rawBody.get(i);
				if (!isMethod(i, rawBody)) {
					continue;
				}
				if (s.isEmpty() && methodsToDisplay.isEmpty()) {
					continue;
				}
				final Member m = new MemberImpl(s, true, manageModifier);
				if (hides == null || !hides.contains(m.getVisibilityModifier())) {
					methodsToDisplay.add(m);
				}
			}
			removeFinalEmptyMembers(methodsToDisplay);
		}
		return Collections.unmodifiableList(methodsToDisplay);
	}

	private boolean isMethod(int i, List<String> rawBody) {
		return i > 0
			&& i < rawBody.size() - 1
			&& rawBody.get(i).isEmpty()
			&& isMethod(rawBody.get(i - 1))
			&& isMethod(rawBody.get(i + 1))
			|| isMethod(rawBody.get(i));
	}

	public List<Member> getFieldsToDisplay() {
		if (fieldsToDisplay == null) {
			fieldsToDisplay = new ArrayList<>();
			for (String s : rawBody) {
				if (isMethod(s)) {
					continue;
				}
				if (s.isEmpty() && fieldsToDisplay.isEmpty()) {
					continue;
				}
				final Member m = new MemberImpl(s, false, manageModifier);
				if (hides == null || !hides.contains(m.getVisibilityModifier())) {
					fieldsToDisplay.add(m);
				}
			}
			removeFinalEmptyMembers(fieldsToDisplay);
		}
		return Collections.unmodifiableList(fieldsToDisplay);
	}

	private void removeFinalEmptyMembers(List<Member> result) {
		while (!result.isEmpty() && StringUtils.trin(result.get(result.size() - 1).getDisplay(false)).isEmpty()) {
			result.remove(result.size() - 1);
		}
	}

	public boolean hasUrl() {
		for (Member m : getFieldsToDisplay()) {
			if (m.hasUrl()) {
				return true;
			}
		}
		for (Member m : getMethodsToDisplay()) {
			if (m.hasUrl()) {
				return true;
			}
		}
		return false;
	}

	private List<String> rawBodyWithoutHidden() {
		if (hides == null || hides.isEmpty()) {
			return rawBody;
		}
		final List<String> result = new ArrayList<>();
		for (String s : rawBody) {
			final Member m = new MemberImpl(s, isMethod(s), manageModifier);
			if (!hides.contains(m.getVisibilityModifier())) {
				result.add(s);
			}

		}
		return result;
	}

	public TextBlock getBody(final FontParam fontParam, final ISkinParam skinParam, final boolean showMethods,
			final boolean showFields, Stereotype stereotype) {
		if (type.isLikeClass() && isBodyEnhanced()) {
			if (showMethods || showFields) {
				return new BodyEnhanced(rawBodyWithoutHidden(), fontParam, skinParam, manageModifier, stereotype, leaf);
			}
			return null;
		}
		final MethodsOrFieldsArea fields = new MethodsOrFieldsArea(getFieldsToDisplay(), fontParam, skinParam,
				stereotype, leaf);
		if (type == LeafType.OBJECT) {
			if (!showFields) {
				return new TextBlockLineBefore(TextBlockUtils.empty(0, 0));
			}
			return fields.asBlockMemberImpl();
		}
		if (!type.isLikeClass()) {
			throw new UnsupportedOperationException();
		}
		final MethodsOrFieldsArea methods = new MethodsOrFieldsArea(getMethodsToDisplay(), fontParam, skinParam,
				stereotype, leaf);
		if (showFields && !showMethods) {
			return fields.asBlockMemberImpl();
		} else if (showMethods && !showFields) {
			return methods.asBlockMemberImpl();
		} else if (!showFields && !showMethods) {
			return TextBlockUtils.empty(0, 0);
		}

		final TextBlock bb1 = fields.asBlockMemberImpl();
		final TextBlock bb2 = methods.asBlockMemberImpl();
		return TextBlockUtils.mergeTB(bb1, bb2, HorizontalAlignment.LEFT);
	}

	public List<String> getRawBody() {
		return Collections.unmodifiableList(rawBody);
	}

}
