/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2024, Arnaud Roques
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
 * Original Author:  Arnaud Roques
 *
 */
package com.plantuml.ubrex;

import java.util.List;

public class ChallengeResult {

	private final int fullCaptureLength;
	private final int onlyNameLength;
	private final Capture capture;
	private final Capture nonMergeableCapture;

	@Override
	public String toString() {
		return "fullCaptureLength=" + fullCaptureLength + " onlyNameLength=" + onlyNameLength + " " + capture;
	}

	public ChallengeResult(int result) {
		this(result, Capture.EMPTY);
	}

	public ChallengeResult(int fullCaptureLength, Capture capture) {
		this.fullCaptureLength = fullCaptureLength;
		this.onlyNameLength = fullCaptureLength;
		this.capture = capture;
		this.nonMergeableCapture = Capture.EMPTY;
	}

	private ChallengeResult(int onlyNameLength, int fullCaptureLength, Capture capture, Capture nonMergeableCapture) {
		this.onlyNameLength = onlyNameLength;
		this.fullCaptureLength = fullCaptureLength;
		this.capture = capture;
		this.nonMergeableCapture = nonMergeableCapture;
	}

	public ChallengeResult withNameLength(int onlyNameLength, Capture nonMergeableCapture) {
		return new ChallengeResult(onlyNameLength, fullCaptureLength, capture, nonMergeableCapture);
	}

//	public ChallengeResult withNonMergeableCapture(Capture nonMergeableCapture) {
//		return new ChallengeResult(onlyNameLength, fullCaptureLength, capture, nonMergeableCapture);
//	}

	public int getFullCaptureLength() {
		return fullCaptureLength;
	}

	public List<String> findValuesByKey(String path) {
		return capture.findValuesByKey(path);
	}

	public List<String> getKeysToBeRefactored() {
		return capture.getKeysToBeRefactored();
	}


	public Capture getCapture() {
		return capture;
	}

	public int getOnlyNameLength() {
		return onlyNameLength;
	}

	public Capture getNonMergeableCapture() {
		return nonMergeableCapture;
	}

}
