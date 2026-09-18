/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2026, Jamison Jiang
 *
 * Project Info:  https://plantuml.com
 *
 * If you like this project or if you find it useful, you can support us at:
 *
 * https://plantuml.com/patreon (only 1$ per month!)
 * https://plantuml.com/paypal
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
 * Original Author:  Jamison Jiang
 *
 *
 */
package net.sourceforge.plantuml.svek.layout;

import java.util.Objects;

public final class SvekLayoutResponse {
	public final SvekLayoutResult result;
	public final String declineCode;
	public final String declineMessage;

	private SvekLayoutResponse(SvekLayoutResult result, String declineCode, String declineMessage) {
		this.result = result;
		this.declineCode = declineCode;
		this.declineMessage = declineMessage;
	}

	public static SvekLayoutResponse success(SvekLayoutResult result) {
		return new SvekLayoutResponse(Objects.requireNonNull(result), null, null);
	}

	public static SvekLayoutResponse declined(String code, String message) {
		return new SvekLayoutResponse(null, Objects.requireNonNull(code), message);
	}

	public boolean isSuccess() {
		return result != null;
	}
}
