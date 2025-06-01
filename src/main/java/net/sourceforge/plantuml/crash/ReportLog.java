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
 *
 */
package net.sourceforge.plantuml.crash;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.plantuml.OptionPrint;
import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.eggs.QuoteUtils;
import net.sourceforge.plantuml.text.BackSlash;
import net.sourceforge.plantuml.version.Version;

public class ReportLog implements Iterable<String> {

	private final List<String> strings = new ArrayList<>();

	public void add(String s) {
		this.strings.add(s);
	}

	public void addAll(Collection<String> list) {
		for (String s : list)
			add(s);

	}

	@Override
	public Iterator<String> iterator() {
		return Collections.unmodifiableList(strings).iterator();
	}

	public List<String> asList() {
		return Collections.unmodifiableList(strings);
	}

	public void youShouldSendThisDiagram() {
		add("You should send this diagram and this image to <b>plantuml@gmail.com</b> or");
		add("post to <b>https://plantuml.com/qa</b> to solve this issue.");
		add("You can try to turn around this issue by simplifing your diagram.");
	}

	public void checkOldVersionWarning() {
		checkOldVersionWarning("<b>");
	}

	public void checkOldVersionWarningRaw() {
		checkOldVersionWarning("");
	}

	public void checkOldVersionWarning(String lineFormating) {
		if (Version.versionString().contains("beta"))
			return;

		final long days = (System.currentTimeMillis() - Version.compileTime()) / 1000L / 3600 / 24;
		if (days >= 140) {
			addEmptyLine();
			add(lineFormating + "This version of PlantUML is " + days + " days old, so you should");
			add(lineFormating + "consider upgrading from https://plantuml.com/download");
		}
	}

	public void addEmptyLine() {
		add(" ");
	}

	public void anErrorHasOccured(Throwable exception, String fullDiagramText) {
		
		if (exception == null)
			add("An error has occured!");
		else
			add("An error has occured : " + exception);
		final String quote = StringUtils.rot(QuoteUtils.getSomeQuote());
		add("<i>" + quote);
		addEmptyLine();

		add("PlantUML (" + Version.versionString() + ") has crashed.");
		addEmptyLine();
		checkOldVersionWarning();
		add("Diagram size: " + lines(fullDiagramText) + " lines / " + fullDiagramText.length() + " characters.");
		addEmptyLine();
	}

	private int lines(String text) {
		int result = 0;
		for (int i = 0; i < text.length(); i++)
			if (text.charAt(i) == BackSlash.CHAR_NEWLINE)
				result++;

		return result;
	}

	public void pleaseCheckYourGraphVizVersion() {
		addEmptyLine();
		add("Please go to https://plantuml.com/graphviz-dot to check your GraphViz version.");
		addEmptyLine();
	}

	public void thisMayBeCaused() {
		add("This may be caused by :");
		add(" - a bug in PlantUML");
		add(" - a problem in GraphViz");
	}

	public void addDecodeHint() {
		addEmptyLine();
		add(" Diagram source: (Use http://zxing.org/w/decode.jspx to decode the qrcode)");
	}

	public void addProperties() {
		// ::comment when __CORE__
		addAll(OptionPrint.interestingProperties());
		addAll(OptionPrint.interestingValues());
		// ::done
	}

}
