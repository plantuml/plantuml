/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2013, Arnaud Roques
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
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public
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
 * Revision $Revision: 7755 $
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
import net.sourceforge.plantuml.graphic.TextBlock;
import net.sourceforge.plantuml.skin.VisibilityModifier;

public class Bodier {

	private final List<String> rawBody = new ArrayList<String>();
	private final Set<VisibilityModifier> hides;
	private final LeafType type;
	private List<Member> methodsToDisplay;
	private List<Member> fieldsToDisplay;
	private final boolean manageModifier;

	public Bodier(LeafType type, Set<VisibilityModifier> hides) {
		this.hides = hides;
		this.type = type;
		this.manageModifier = type == null ? false : type.manageModifier();
	}

	public void addFieldOrMethod(String s) {
		// Empty cache
		methodsToDisplay = null;
		fieldsToDisplay = null;
		rawBody.add(s);
	}

	public boolean isBodyEnhanced() {
		for (String s : rawBody) {
			if (BodyEnhanced.isBlockSeparator(s)) {
				return true;
			}
		}
		return false;
	}

	public BlockMember getBodyEnhanced() {
		return new BlockMember() {
			public TextBlock asTextBlock(FontParam fontParam, ISkinParam skinParam) {
				final BodyEnhanced result = new BodyEnhanced(rawBody, fontParam, skinParam, manageModifier);
				return result;
			}
		};
	}

	private LeafType getEntityType() {
		return type;
	}

	private boolean isMethod(String s) {
		if (getEntityType() == LeafType.ANNOTATION || getEntityType() == LeafType.ABSTRACT_CLASS
				|| getEntityType() == LeafType.CLASS || getEntityType() == LeafType.INTERFACE
				|| getEntityType() == LeafType.ENUM) {
			return StringUtils.isMethod(s);
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
				final Member m = new MemberImpl(s, true, manageModifier);
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
				final Member m = new MemberImpl(s, false, manageModifier);
				if (hides == null || hides.contains(m.getVisibilityModifier()) == false) {
					fieldsToDisplay.add(m);
				}
			}
			removeFinalEmptyMembers(fieldsToDisplay);
		}
		return Collections.unmodifiableList(fieldsToDisplay);
	}

	private void removeFinalEmptyMembers(List<Member> result) {
		while (result.size() > 0 && result.get(result.size() - 1).getDisplay(false).trim().length() == 0) {
			result.remove(result.size() - 1);
		}
	}
}
