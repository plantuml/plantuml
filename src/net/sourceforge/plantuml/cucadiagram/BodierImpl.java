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
import java.util.Set;

import net.sourceforge.plantuml.FontParam;
import net.sourceforge.plantuml.ISkinParam;
import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.graphic.HorizontalAlignment;
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.graphic.TextBlockLineBefore;
import net.sourceforge.plantuml.graphic.TextBlockUtils;
import net.sourceforge.plantuml.skin.VisibilityModifier;

public class BodierImpl implements Bodier {

	private final List<String> rawBody = new ArrayList<String>();
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

	public BodierImpl(LeafType type, Set<VisibilityModifier> hides) {
		if (type == LeafType.MAP) {
			throw new IllegalArgumentException();
		}
		this.hides = hides;
		this.type = type;
		this.manageModifier = type == null ? false : type.manageModifier();
	}

	public void setLeaf(ILeaf leaf) {
		if (leaf == null) {
			throw new IllegalArgumentException();
		}
		this.leaf = leaf;

	}

	public void addFieldOrMethod(String s) {
//		if (leaf == null) {
//			throw new IllegalArgumentException();
//		}
		// Empty cache
		methodsToDisplay = null;
		fieldsToDisplay = null;
		rawBody.add(s);
//		if (leaf instanceof ILeaf) {
//			if (this.leaf != null && this.leaf != leaf) {
//				throw new IllegalArgumentException();
//			}
//			this.leaf = (ILeaf) leaf;
//		}
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
		if (type == LeafType.ANNOTATION || type == LeafType.ABSTRACT_CLASS || type == LeafType.CLASS
				|| type == LeafType.INTERFACE || type == LeafType.ENUM) {
			return Member.isMethod(s);
		}
		return false;
	}

	public List<Member> getMethodsToDisplay() {
		if (methodsToDisplay == null) {
			methodsToDisplay = new ArrayList<Member>();
			for (int i = 0; i < rawBody.size(); i++) {
				final String s = rawBody.get(i);
				if (isMethod(i, rawBody) == false) {
					continue;
				}
				if (s.length() == 0 && methodsToDisplay.size() == 0) {
					continue;
				}
				final Member m = new Member(s, true, manageModifier);
				if (hides == null || hides.contains(m.getVisibilityModifier()) == false) {
					methodsToDisplay.add(m);
				}
			}
			removeFinalEmptyMembers(methodsToDisplay);
		}
		return Collections.unmodifiableList(methodsToDisplay);
	}

	private boolean isMethod(int i, List<String> rawBody) {
		if (i > 0 && i < rawBody.size() - 1 && rawBody.get(i).length() == 0 && isMethod(rawBody.get(i - 1))
				&& isMethod(rawBody.get(i + 1))) {
			return true;
		}
		return isMethod(rawBody.get(i));
	}

	public List<Member> getFieldsToDisplay() {
		if (fieldsToDisplay == null) {
			fieldsToDisplay = new ArrayList<Member>();
			for (String s : rawBody) {
				if (isMethod(s) == true) {
					continue;
				}
				if (s.length() == 0 && fieldsToDisplay.size() == 0) {
					continue;
				}
				final Member m = new Member(s, false, manageModifier);
				if (hides == null || hides.contains(m.getVisibilityModifier()) == false) {
					fieldsToDisplay.add(m);
				}
			}
			removeFinalEmptyMembers(fieldsToDisplay);
		}
		return Collections.unmodifiableList(fieldsToDisplay);
	}

	private void removeFinalEmptyMembers(List<Member> result) {
		while (result.size() > 0 && StringUtils.trin(result.get(result.size() - 1).getDisplay(false)).length() == 0) {
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
		if (hides == null || hides.size() == 0) {
			return rawBody;
		}
		final List<String> result = new ArrayList<String>();
		for (String s : rawBody) {
			final Member m = new Member(s, isMethod(s), manageModifier);
			if (hides.contains(m.getVisibilityModifier()) == false) {
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
		if (leaf == null) {
			throw new IllegalStateException();
		}
		final MethodsOrFieldsArea fields = new MethodsOrFieldsArea(getFieldsToDisplay(), fontParam, skinParam,
				stereotype, leaf);
		if (type == LeafType.OBJECT) {
			if (showFields == false) {
				return new TextBlockLineBefore(TextBlockUtils.empty(0, 0));
			}
			return fields.asBlockMemberImpl();
		}
		if (type.isLikeClass() == false) {
			throw new UnsupportedOperationException();
		}
		final MethodsOrFieldsArea methods = new MethodsOrFieldsArea(getMethodsToDisplay(), fontParam, skinParam,
				stereotype, leaf);
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

	public List<String> getRawBody() {
		return Collections.unmodifiableList(rawBody);
	}

}
