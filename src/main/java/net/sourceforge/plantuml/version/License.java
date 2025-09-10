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
package net.sourceforge.plantuml.version;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.plantuml.cli.GlobalConfig;
import net.sourceforge.plantuml.cli.GlobalConfigKey;
import net.sourceforge.plantuml.windowsdot.WindowsDotArchive;

public class License {

	public static void main(String[] args) {
		List<String> lines = License.getCurrent().getTextFull();
		for (int line = 0; line < lines.size(); line++) {
			if (line == 0) {
				System.out.print("/* ");
			} else {
				System.out.print(" * ");
			}
			System.out.println(lines.get(line));
		}
		System.out.println(" */");
	}

	@Override
	public String toString() {
		// ::comment when __CORE__ or __MIT__ or __EPL__ or __BSD__ or __ASL__ or __LGPL__ or __GPLV2__
		return "GPL";
		// ::done
		// ::uncomment when __CORE__ or __MIT__
		// return "MIT";
		// ::done
		// ::uncomment when __EPL__
		// return "EPL";
		// ::done
		// ::uncomment when __BSD__
		// return "BSD";
		// ::done
		// ::uncomment when __ASL__
		// return "APACHE";
		// ::done
		// ::uncomment when __LGPL__
		// return "LGPL";
		// ::done
		// ::uncomment when __GPLV2__
		// return "GPLv2";
		// ::done
	}

	public static License getCurrent() {
		return new License();
	}

	public List<String> getTextFull() {
		final List<String> text = new ArrayList<>();
		final LicenseInfo licenseInfo = LicenseInfo.retrieveQuick();
		header1(text, licenseInfo);
		header2(text, licenseInfo, false);
		end3(licenseInfo, text);
		return text;
	}

	public List<String> getText1(LicenseInfo licenseInfo) {
		final List<String> text = new ArrayList<>();
		header1(text, licenseInfo);
		return text;
	}

	public List<String> getText2(LicenseInfo licenseInfo) {
		final List<String> text = new ArrayList<>();
		header2(text, licenseInfo, true);
		end3(licenseInfo, text);
		return text;
	}

	private void header1(final List<String> text, LicenseInfo licenseInfo) {

		// ::comment when __CORE__
		if (licenseInfo.isNone()) {
			// ::done
			text.add("+=======================================================================");
			text.add("| ");
			text.add("|      PlantUML : a free UML diagram generator");
			text.add("| ");
			text.add("+=======================================================================");
			// ::comment when __CORE__
		} else {
			text.add("+=======================================================================");
			text.add("| ");
			text.add("|      PlantUML Professional Edition");
			text.add("| ");
			text.add("+=======================================================================");
			addLicenseInfo(text, licenseInfo);
			text.add("+=======================================================================");
		}
		// ::done
	}

	private void header2(final List<String> text, LicenseInfo licenseInfo, boolean withQrcode) {
		text.add(" ");
		text.add("(C) Copyright 2009-2024, Arnaud Roques");
		text.add(" ");
		text.add("Project Info:  https://plantuml.com");
		text.add(" ");

		if (licenseInfo.isValid() == false) {
			text.add("If you like this project or if you find it useful, you can support us at:");
			text.add(" ");
			text.add("https://plantuml.com/patreon (only 1$ per month!)");
			text.add("https://plantuml.com/liberapay (only 1\u20ac per month!)");
			text.add("https://plantuml.com/paypal");
			// ::comment when __CORE__
			if (withQrcode) {
				text.add(
						"\t<qrcode:https://plantuml.com/patreon>\t\t<qrcode:https://plantuml.com/lp>\t\t<qrcode:https://plantuml.com/paypal>");
			} else {
				// ::done
				text.add("");
				text.add(" ");
				// ::comment when __CORE__
			}
			// ::done
		}
	}

	private void end3(final LicenseInfo licenseInfo, final List<String> text) {
		// ::comment when __CORE__ or __MIT__ or __EPL__ or __BSD__ or __ASL__ or __LGPL__ or __GPLV2__
		text.add("PlantUML is free software; you can redistribute it and/or modify it");
		text.add("under the terms of the GNU General Public License as published by");
		text.add("the Free Software Foundation, either version 3 of the License, or");
		text.add("(at your option) any later version.");
		text.add(" ");
		text.add("PlantUML distributed in the hope that it will be useful, but");
		text.add("WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY");
		text.add("or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public");
		text.add("License for more details.");
		text.add(" ");
		text.add("You should have received a copy of the GNU General Public License");
		text.add("along with this library.  If not, see <https://www.gnu.org/licenses/>.");
		// ::done

		// ::uncomment when __CORE__
		// addCore(licenseInfo, text);
		// ::done
		// ::uncomment when __MIT__
		// addMit(licenseInfo, text);
		// ::done
		// ::uncomment when __EPL__
		// addEpl(licenseInfo, text);
		// ::done
		// ::uncomment when __BSD__
		// addBsd(licenseInfo, text);
		// ::done
		// ::uncomment when __ASL__
		// addApache(licenseInfo, text);
		// ::done
		// ::uncomment when __LGPL__
		// addLgpl(licenseInfo, text);
		// ::done
		// ::uncomment when __GPLV2__
		// addGplv2(licenseInfo, text);
		// ::done

		text.add(" ");
		if (licenseInfo.isValid() == false) {
			text.add("PlantUML can occasionally display sponsored or advertising messages. Those");
			text.add("messages are usually generated on welcome or error images and never on");
			text.add("functional diagrams.");
			text.add("See https://plantuml.com/professional if you want to remove them");
			text.add(" ");
		}

		text.add("Images (whatever their format : PNG, SVG, EPS...) generated by running PlantUML");
		text.add("are owned by the author of their corresponding sources code (that is, their");
		text.add("textual description in PlantUML language). Those images are not covered by");
		text.add("this " + this + " license.");
		text.add(" ");
		text.add("The generated images can then be used without any reference to the " + this + " license.");
		text.add("It is not even necessary to stipulate that they have been generated with PlantUML,");
		text.add("although this will be appreciated by the PlantUML team.");
		text.add(" ");
		text.add("There is an exception : if the textual description in PlantUML language is also covered");
		text.add("by any license, then the generated images are logically covered");
		text.add("by the very same license.");

		// ::comment when __CORE__
		if (GlobalConfig.getInstance().boolValue(GlobalConfigKey.ENABLE_STATS)) {
			text.add(" ");
			text.add("This version of PlantUML records general local statistics about usage.");
			text.add("(more info on https://plantuml.com/statistics-report)");
		}
		text.add(" ");
		if (WindowsDotArchive.getInstance().isThereArchive()) {
			text.add("This distribution bundles a minimal set of GraphViz files and may install them");
			text.add(" if needed in the local temporary directory.");
		} else {
			text.add("This is the IGY distribution (Install GraphViz by Yourself).");
			text.add("You have to install GraphViz and to setup the GRAPHVIZ_DOT environment variable");
		}
		text.add("(see https://plantuml.com/graphviz-dot )");
		// ::done
		text.add(" ");
		text.add("Icons provided by OpenIconic :  https://useiconic.com/open");
		text.add("Archimate sprites provided by Archi :  http://www.archimatetool.com");
		text.add("Stdlib AWS provided by https://github.com/milo-minderbinder/AWS-PlantUML");
		text.add("Stdlib Icons provided https://github.com/tupadr3/plantuml-icon-font-sprites");
		text.add("ASCIIMathML (c) Peter Jipsen http://www.chapman.edu/~jipsen");
		text.add("ASCIIMathML (c) David Lippman http://www.pierce.ctc.edu/dlippman");
		text.add("CafeUndZopfli ported by Eugene Klyuchnikov https://github.com/eustas/CafeUndZopfli");
		text.add("Brotli (c) by the Brotli Authors https://github.com/google/brotli");
		text.add("Themes (c) by Brett Schwarz https://github.com/bschwarz/puml-themes");
		text.add("Twemoji (c) by Twitter at https://twemoji.twitter.com/");
		text.add(" ");
	}

// ::uncomment when __CORE__
//	private void addCore(final LicenseInfo licenseInfo, final List<String> text) {
//		text.add("Powered by CheerpJ, a Leaning Technologies Java tool.");
//		text.add("This library is running using CheerpJ for PlantUML License provided by Leaning Technologies Limited.");
//		text.add(" ");
//		text.add(" ");
//		text.add("PlantUML is free software; you can redistribute it and/or modify it");
//		text.add("under the terms of the MIT License.");
//		text.add(" ");
//		text.add("See http://opensource.org/licenses/MIT");
//		text.add(" ");
//		text.add("Permission is hereby granted, free of charge, to any person obtaining");
//		text.add("a copy of this software and associated documentation files (the \"Software\"),");
//		text.add("to deal in the Software without restriction, including without limitation");
//		text.add("the rights to use, copy, modify, merge, publish, distribute, sublicense,");
//		text.add("and/or sell copies of the Software, and to permit persons to whom the");
//		text.add("Software is furnished to do so, subject to the following conditions:");
//		text.add(" ");
//		text.add("The above copyright notice and this permission notice shall be included");
//		text.add("in all copies or substantial portions of the Software.");
//		text.add(" ");
//		text.add("THE SOFTWARE IS PROVIDED \"AS IS\", WITHOUT WARRANTY OF ANY KIND, EXPRESS");
//		text.add("OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,");
//		text.add("FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE");
//		text.add("AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,");
//		text.add("WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR");
//		text.add("IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.");
//	}
// ::done

// ::uncomment when __MIT__
//	private void addMit(final LicenseInfo licenseInfo, final List<String> text) {
//		text.add("PlantUML is free software; you can redistribute it and/or modify it");
//		text.add("under the terms of the MIT License.");
//		text.add(" ");
//		text.add("See http://opensource.org/licenses/MIT");
//		text.add(" ");
//		text.add("Permission is hereby granted, free of charge, to any person obtaining");
//		text.add("a copy of this software and associated documentation files (the \"Software\"),");
//		text.add("to deal in the Software without restriction, including without limitation");
//		text.add("the rights to use, copy, modify, merge, publish, distribute, sublicense,");
//		text.add("and/or sell copies of the Software, and to permit persons to whom the");
//		text.add("Software is furnished to do so, subject to the following conditions:");
//		text.add(" ");
//		text.add("The above copyright notice and this permission notice shall be included");
//		text.add("in all copies or substantial portions of the Software.");
//		text.add(" ");
//		text.add("THE SOFTWARE IS PROVIDED \"AS IS\", WITHOUT WARRANTY OF ANY KIND, EXPRESS");
//		text.add("OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,");
//		text.add("FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE");
//		text.add("AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,");
//		text.add("WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR");
//		text.add("IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.");
//	}
// ::done

// ::uncomment when __EPL__
//	private void addEpl(final LicenseInfo licenseInfo, final List<String> text) {
//		text.add("PlantUML is free software; you can redistribute it and/or modify it");
//		text.add("under the terms of the Eclipse Public License.");
//		text.add(" ");
//		text.add("THE ACCOMPANYING PROGRAM IS PROVIDED UNDER THE TERMS OF THIS ECLIPSE PUBLIC");
//		text.add("LICENSE (\"AGREEMENT\"). [Eclipse Public License - v 1.0]");
//		text.add(" ");
//		text.add("ANY USE, REPRODUCTION OR DISTRIBUTION OF THE PROGRAM CONSTITUTES");
//		text.add("RECIPIENT'S ACCEPTANCE OF THIS AGREEMENT.");
//		text.add(" ");
//		text.add("You may obtain a copy of the License at");
//		text.add(" ");
//		text.add("http://www.eclipse.org/legal/epl-v10.html");
//		text.add(" ");
//		text.add("Unless required by applicable law or agreed to in writing, software");
//		text.add("distributed under the License is distributed on an \"AS IS\" BASIS,");
//		text.add("WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.");
//		text.add("See the License for the specific language governing permissions and");
//		text.add("limitations under the License.");
//	}
// ::done

// ::uncomment when __BSD__
//	private void addBsd(final LicenseInfo licenseInfo, final List<String> text) {
//		text.add("PlantUML is free software; you can redistribute it and/or modify it");
//		text.add("under the terms of the Revised BSD License.");
//		text.add(" ");
//		text.add("All rights reserved.");
//		text.add("Redistribution and use in source and binary forms, with or without");
//		text.add("modification, are permitted provided that the following conditions are met:");
//		text.add(" ");
//		text.add("* Redistributions of source code must retain the above copyright");
//		text.add("  notice, this list of conditions and the following disclaimer.");
//		text.add("* Redistributions in binary form must reproduce the above copyright");
//		text.add("  notice, this list of conditions and the following disclaimer in the");
//		text.add("  documentation and/or other materials provided with the distribution.");
//		text.add("* Neither the name of the University of California, Berkeley nor the");
//		text.add("  names of its contributors may be used to endorse or promote products");
//		text.add("  derived from this software without specific prior written permission.");
//		text.add(" ");
//		text.add("THIS SOFTWARE IS PROVIDED BY THE REGENTS AND CONTRIBUTORS ``AS IS'' AND ANY");
//		text.add("EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED");
//		text.add("WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE");
//		text.add("DISCLAIMED. IN NO EVENT SHALL THE REGENTS AND CONTRIBUTORS BE LIABLE FOR ANY");
//		text.add("DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES");
//		text.add("(INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;");
//		text.add("LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND");
//		text.add("ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT");
//		text.add("(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS");
//		text.add("SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.");
//	}
// ::done

// ::uncomment when __ASL__
//	private void addApache(final LicenseInfo licenseInfo, final List<String> text) {
//		text.add("PlantUML is free software; you can redistribute it and/or modify it");
//		text.add("under the terms of the Apache Software License.");
//		text.add(" ");
//		text.add("Licensed under the Apache License, Version 2.0 (the \"License\");");
//		text.add("you may not use this file except in compliance with the License.");
//		text.add("You may obtain a copy of the License at");
//		text.add(" ");
//		text.add("http://www.apache.org/licenses/LICENSE-2.0");
//		text.add(" ");
//		text.add("Unless required by applicable law or agreed to in writing, software");
//		text.add("distributed under the License is distributed on an \"AS IS\" BASIS,");
//		text.add("WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.");
//		text.add("See the License for the specific language governing permissions and");
//		text.add("limitations under the License.");
//	}
// ::done

	// ::uncomment when __LGPL__
//		private void addLgpl(final LicenseInfo licenseInfo, final List<String> text) {
//			text.add("PlantUML is free software; you can redistribute it and/or modify it");
//			text.add("under the terms of the GNU Lesser General Public License as published by");
//			text.add("the Free Software Foundation, either version 3 of the License, or");
//			text.add("(at your option) any later version.");
//			text.add(" ");
//			text.add("PlantUML distributed in the hope that it will be useful, but");
//			text.add("WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY");
//			text.add("or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public");
//			text.add("License for more details.");
//			text.add(" ");
//			text.add("You should have received a copy of the GNU Lesser General Public License");
//			text.add("along with this library.  If not, see <https://www.gnu.org/licenses/>.");
//		}
	// ::done

	// ::uncomment when __GPLV2__
//		private void addGplv2(final LicenseInfo licenseInfo, final List<String> text) {
//			text.add("PlantUML is free software; you can redistribute it and/or modify it");
//			text.add("under the terms of the GNU General Public License V2 as published by");
//			text.add("the Free Software Foundation, either version 2 of the License, or");
//			text.add("(at your option) any later version.");
//			text.add(" ");
//			text.add("PlantUML distributed in the hope that it will be useful, but");
//			text.add("WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY");
//			text.add("or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public");
//			text.add("License for more details.");
//			text.add(" ");
//			text.add("You should have received a copy of the GNU Lesser General Public License");
//			text.add("along with this library.  If not, see <https://www.gnu.org/licenses/old-licenses/gpl-2.0.html>.");
//		}
	// ::done

	public static void addLicenseInfo(final List<String> text, LicenseInfo licenseInfo) {
		if (licenseInfo.getLicenseType() == LicenseType.NAMED) {
			text.add("| ");
			text.add("|      LICENSED TO : " + licenseInfo.getOwner());
			text.add(
					"|      EXPIRATION DATE : " + DateFormat.getDateInstance().format(licenseInfo.getExpirationDate()));
			text.add("|  ");
		} else if (licenseInfo.getLicenseType() == LicenseType.DISTRIBUTOR) {
			text.add("|  ");
			text.add("|      DISTRIBUTED BY : " + licenseInfo.getOwner());
			text.add("|  ");
		}
		if (licenseInfo.getLicenseType() != LicenseType.UNKNOWN && licenseInfo.hasExpired()) {
			text.add("|      <i>Warning: Your license has expired.");
			text.add("|  ");
		}
	}

}
