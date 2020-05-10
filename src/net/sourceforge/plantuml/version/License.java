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
 */
package net.sourceforge.plantuml.version;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.sourceforge.plantuml.OptionFlags;

public enum License {

	GPL, GPLV2, LGPL, APACHE, EPL, MIT, BSD;

	public static License getCurrent() {
		return GPL;
	}

	private void addMit(final LicenseInfo licenseInfo, final List<String> text) {
		text.add("PlantUML is free software; you can redistribute it and/or modify it");
		text.add("under the terms of the MIT License.");
		text.add(" ");
		text.add("See http://opensource.org/licenses/MIT");
		text.add(" ");
		text.add("Permission is hereby granted, free of charge, to any person obtaining");
		text.add("a copy of this software and associated documentation files (the \"Software\"),");
		text.add("to deal in the Software without restriction, including without limitation");
		text.add("the rights to use, copy, modify, merge, publish, distribute, sublicense,");
		text.add("and/or sell copies of the Software, and to permit persons to whom the");
		text.add("Software is furnished to do so, subject to the following conditions:");
		text.add(" ");
		text.add("The above copyright notice and this permission notice shall be included");
		text.add("in all copies or substantial portions of the Software.");
		text.add(" ");
		text.add("THE SOFTWARE IS PROVIDED \"AS IS\", WITHOUT WARRANTY OF ANY KIND, EXPRESS");
		text.add("OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,");
		text.add("FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE");
		text.add("AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,");
		text.add("WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR");
		text.add("IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.");
		text.add(" ");
		addSupplementary(licenseInfo, text);
		text.add("the MIT License.");
		text.add(" ");
		text.add("The generated images can then be used without any reference to the MIT License.");
		text.add("It is not even necessary to stipulate that they have been generated with PlantUML,");
		text.add("also this will be appreciate by PlantUML team.");
		text.add(" ");
		text.add("There is an exception : if the textual description in PlantUML language is also covered");
		text.add("by a license (like the MIT), then the generated images are logically covered");
		text.add("by the very same license.");
	}

	private void addEpl(final LicenseInfo licenseInfo, final List<String> text) {
		text.add("PlantUML is free software; you can redistribute it and/or modify it");
		text.add("under the terms of the Eclipse Public License.");
		text.add(" ");
		text.add("THE ACCOMPANYING PROGRAM IS PROVIDED UNDER THE TERMS OF THIS ECLIPSE PUBLIC");
		text.add("LICENSE (\"AGREEMENT\"). [Eclipse Public License - v 1.0]");
		text.add(" ");
		text.add("ANY USE, REPRODUCTION OR DISTRIBUTION OF THE PROGRAM CONSTITUTES");
		text.add("RECIPIENT'S ACCEPTANCE OF THIS AGREEMENT.");
		text.add(" ");
		text.add("You may obtain a copy of the License at");
		text.add(" ");
		text.add("http://www.eclipse.org/legal/epl-v10.html");
		text.add(" ");
		text.add("Unless required by applicable law or agreed to in writing, software");
		text.add("distributed under the License is distributed on an \"AS IS\" BASIS,");
		text.add("WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.");
		text.add("See the License for the specific language governing permissions and");
		text.add("limitations under the License.");
		text.add(" ");
		addSupplementary(licenseInfo, text);
		text.add("the Eclipse Public License.");
		text.add(" ");
		text.add("The generated images can then be used without any reference to the Eclipse Public License.");
		text.add("It is not even necessary to stipulate that they have been generated with PlantUML,");
		text.add("also this will be appreciate by PlantUML team.");
		text.add(" ");
		text.add("There is an exception : if the textual description in PlantUML language is also covered");
		text.add("by a license (like the EPL), then the generated images are logically covered");
		text.add("by the very same license.");
	}

	private void addBsd(final LicenseInfo licenseInfo, final List<String> text) {
		text.add("PlantUML is free software; you can redistribute it and/or modify it");
		text.add("under the terms of the Revised BSD License.");
		text.add(" ");
		text.add("All rights reserved.");
		text.add("Redistribution and use in source and binary forms, with or without");
		text.add("modification, are permitted provided that the following conditions are met:");
		text.add(" ");
		text.add("* Redistributions of source code must retain the above copyright");
		text.add("  notice, this list of conditions and the following disclaimer.");
		text.add("* Redistributions in binary form must reproduce the above copyright");
		text.add("  notice, this list of conditions and the following disclaimer in the");
		text.add("  documentation and/or other materials provided with the distribution.");
		text.add("* Neither the name of the University of California, Berkeley nor the");
		text.add("  names of its contributors may be used to endorse or promote products");
		text.add("  derived from this software without specific prior written permission.");
		text.add(" ");
		text.add("THIS SOFTWARE IS PROVIDED BY THE REGENTS AND CONTRIBUTORS ``AS IS'' AND ANY");
		text.add("EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED");
		text.add("WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE");
		text.add("DISCLAIMED. IN NO EVENT SHALL THE REGENTS AND CONTRIBUTORS BE LIABLE FOR ANY");
		text.add("DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES");
		text.add("(INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;");
		text.add("LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND");
		text.add("ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT");
		text.add("(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS");
		text.add("SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.");
		text.add(" ");
		addSupplementary(licenseInfo, text);
		text.add("the Eclipse Public License.");
		text.add(" ");
		text.add("The generated images can then be used without any reference to the Eclipse Public License.");
		text.add("It is not even necessary to stipulate that they have been generated with PlantUML,");
		text.add("also this will be appreciate by PlantUML team.");
		text.add(" ");
		text.add("There is an exception : if the textual description in PlantUML language is also covered");
		text.add("by a license (like the BSD), then the generated images are logically covered");
		text.add("by the very same license.");
	}

	private void addApache(final LicenseInfo licenseInfo, final List<String> text) {
		text.add("PlantUML is free software; you can redistribute it and/or modify it");
		text.add("under the terms of the Apache Software License.");
		text.add(" ");
		text.add("Licensed under the Apache License, Version 2.0 (the \"License\");");
		text.add("you may not use this file except in compliance with the License.");
		text.add("You may obtain a copy of the License at");
		text.add(" ");
		text.add("http://www.apache.org/licenses/LICENSE-2.0");
		text.add(" ");
		text.add("Unless required by applicable law or agreed to in writing, software");
		text.add("distributed under the License is distributed on an \"AS IS\" BASIS,");
		text.add("WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.");
		text.add("See the License for the specific language governing permissions and");
		text.add("limitations under the License.");
		text.add(" ");
		addSupplementary(licenseInfo, text);
		text.add("the Apache license.");
		text.add(" ");
		text.add("The generated images can then be used without any reference to the Apache license.");
		text.add("It is not even necessary to stipulate that they have been generated with PlantUML,");
		text.add("also this will be appreciate by PlantUML team.");
		text.add(" ");
		text.add("There is an exception : if the textual description in PlantUML language is also covered");
		text.add("by a license (like the Apache), then the generated images are logically covered");
		text.add("by the very same license.");
	}

	private void addGpl(final LicenseInfo licenseInfo, final List<String> text) {
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
		text.add("You should have received a copy of the GNU General Public");
		text.add("License along with this library; if not, write to the Free Software");
		text.add("Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301,");
		text.add("USA.");
		text.add(" ");
		addSupplementary(licenseInfo, text);
		text.add("the GPL license.");
		text.add(" ");
		text.add("The generated images can then be used without any reference to the GPL license.");
		text.add("It is not even necessary to stipulate that they have been generated with PlantUML,");
		text.add("also this will be appreciate by PlantUML team.");
		text.add(" ");
		text.add("There is an exception : if the textual description in PlantUML language is also covered");
		text.add("by a license (like the GPL), then the generated images are logically covered");
		text.add("by the very same license.");
	}

	private void addGplV2(final LicenseInfo licenseInfo, final List<String> text) {
		text.add("PlantUML is free software; you can redistribute it and/or modify it");
		text.add("under the terms of the GNU General Public License as published by");
		text.add("the Free Software Foundation, either version 2 of the License, or");
		text.add("(at your option) any later version.");
		text.add(" ");
		text.add("PlantUML distributed in the hope that it will be useful, but");
		text.add("WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY");
		text.add("or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public");
		text.add("License for more details.");
		text.add(" ");
		text.add("You should have received a copy of the GNU General Public");
		text.add("License along with this library; if not, write to the Free Software");
		text.add("Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301,");
		text.add("USA.");
		text.add(" ");
		addSupplementary(licenseInfo, text);
		text.add("the GPL license.");
		text.add(" ");
		text.add("The generated images can then be used without any reference to the GPL license.");
		text.add("It is not even necessary to stipulate that they have been generated with PlantUML,");
		text.add("also this will be appreciate by PlantUML team.");
		text.add(" ");
		text.add("There is an exception : if the textual description in PlantUML language is also covered");
		text.add("by a license (like the GPL), then the generated images are logically covered");
		text.add("by the very same license.");
	}

	private void addLgpl(final LicenseInfo licenseInfo, final List<String> text) {
		text.add("PlantUML is free software; you can redistribute it and/or modify it");
		text.add("under the terms of the GNU Lesser General Public License as published by");
		text.add("the Free Software Foundation, either version 3 of the License, or");
		text.add("(at your option) any later version.");
		text.add(" ");
		text.add("PlantUML distributed in the hope that it will be useful, but");
		text.add("WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY");
		text.add("or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public");
		text.add("License for more details.");
		text.add(" ");
		text.add("You should have received a copy of the GNU Lesser General Public");
		text.add("License along with this library; if not, write to the Free Software");
		text.add("Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301,");
		text.add("USA.");
		text.add(" ");
		addSupplementary(licenseInfo, text);
		text.add("the LGPL license.");
		text.add(" ");
		text.add("The generated images can then be used without any reference to the LGPL license.");
		text.add("It is not even necessary to stipulate that they have been generated with PlantUML,");
		text.add("although this will be appreciate by PlantUML team.");
		text.add(" ");
		text.add("There is an exception : if the textual description in PlantUML language is also covered");
		text.add("by a license (like the LGPL), then the generated images are logically covered");
		text.add("by the very same license.");
	}

	private void addSupplementary(final LicenseInfo licenseInfo, final List<String> text) {
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
	}

	private void header1(final List<String> text, LicenseInfo licenseInfo) {
		if (licenseInfo.isNone()) {
			text.add("+=======================================================================");
			text.add("| ");
			text.add("|      PlantUML : a free UML diagram generator");
			text.add("| ");
			text.add("+=======================================================================");
		} else {
			text.add("+=======================================================================");
			text.add("| ");
			text.add("|      PlantUML Professional Edition");
			text.add("| ");
			text.add("+=======================================================================");
			addLicenseInfo(text, licenseInfo);
			text.add("+=======================================================================");
		}
	}

	private void header2(final List<String> text, LicenseInfo licenseInfo, boolean withQrcode) {
		text.add(" ");
		text.add("(C) Copyright 2009-2020, Arnaud Roques");
		text.add(" ");
		text.add("Project Info:  https://plantuml.com");
		text.add(" ");

		if (licenseInfo.isValid() == false) {
			text.add("If you like this project or if you find it useful, you can support us at:");
			text.add(" ");
			text.add("https://plantuml.com/patreon (only 1$ per month!)");
			text.add("https://plantuml.com/liberapay (only 1\u20ac per month!)");
			text.add("https://plantuml.com/paypal");
			if (withQrcode) {
				text.add(
						"\t<qrcode:http://plantuml.com/patreon>\t\t<qrcode:http://plantuml.com/lp>\t\t<qrcode:http://plantuml.com/paypal>");
			} else {
				text.add("");
				text.add(" ");
			}
		}
	}

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

	public List<String> getJavaHeader(List<String> contributors) {
		final List<String> h = new ArrayList<String>();
		h.add("/* ========================================================================");
		h.add(" * PlantUML : a free UML diagram generator");
		h.add(" * ========================================================================");
		h.add(" *");
		h.add(" * (C) Copyright 2009-2020, Arnaud Roques");
		h.add(" *");
		h.add(" * Project Info:  https://plantuml.com");
		h.add(" * ");
		h.add(" * If you like this project or if you find it useful, you can support us at:");
		h.add(" * ");
		h.add(" * https://plantuml.com/patreon (only 1$ per month!)");
		h.add(" * https://plantuml.com/paypal");
		h.add(" * ");
		h.add(" * This file is part of PlantUML.");
		h.add(" *");
		if (this == License.LGPL) {
			h.add(" * PlantUML is free software; you can redistribute it and/or modify it");
			h.add(" * under the terms of the GNU Lesser General Public License as published by");
			h.add(" * the Free Software Foundation, either version 3 of the License, or");
			h.add(" * (at your option) any later version.");
			h.add(" *");
			h.add(" * PlantUML distributed in the hope that it will be useful, but");
			h.add(" * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY");
			h.add(" * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public");
			h.add(" * License for more details.");
			h.add(" *");
			h.add(" * You should have received a copy of the GNU Lesser General Public");
			h.add(" * License along with this library; if not, write to the Free Software");
			h.add(" * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301,");
			h.add(" * USA.");
			h.add(" *");
		} else if (this == License.GPLV2) {
			h.add(" * PlantUML is free software; you can redistribute it and/or modify it");
			h.add(" * under the terms of the GNU General Public License as published by");
			h.add(" * the Free Software Foundation, either version 2 of the License, or");
			h.add(" * (at your option) any later version.");
			h.add(" *");
			h.add(" * PlantUML distributed in the hope that it will be useful, but");
			h.add(" * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY");
			h.add(" * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public");
			h.add(" * License for more details.");
			h.add(" *");
			h.add(" * You should have received a copy of the GNU General Public");
			h.add(" * License along with this library; if not, write to the Free Software");
			h.add(" * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301,");
			h.add(" * USA.");
			h.add(" *");
		} else if (this == License.APACHE) {
			h.add(" * Licensed under the Apache License, Version 2.0 (the \"License\");");
			h.add(" * you may not use this file except in compliance with the License.");
			h.add(" * You may obtain a copy of the License at");
			h.add(" * ");
			h.add(" * http://www.apache.org/licenses/LICENSE-2.0");
			h.add(" * ");
			h.add(" * Unless required by applicable law or agreed to in writing, software");
			h.add(" * distributed under the License is distributed on an \"AS IS\" BASIS,");
			h.add(" * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.");
			h.add(" * See the License for the specific language governing permissions and");
			h.add(" * limitations under the License.");
			h.add(" *");
		} else if (this == License.EPL) {
			h.add(" * THE ACCOMPANYING PROGRAM IS PROVIDED UNDER THE TERMS OF THIS ECLIPSE PUBLIC");
			h.add(" * LICENSE (\"AGREEMENT\"). [Eclipse Public License - v 1.0]");
			h.add(" * ");
			h.add(" * ANY USE, REPRODUCTION OR DISTRIBUTION OF THE PROGRAM CONSTITUTES");
			h.add(" * RECIPIENT'S ACCEPTANCE OF THIS AGREEMENT.");
			h.add(" * ");
			h.add(" * You may obtain a copy of the License at");
			h.add(" * ");
			h.add(" * http://www.eclipse.org/legal/epl-v10.html");
			h.add(" * ");
			h.add(" * Unless required by applicable law or agreed to in writing, software");
			h.add(" * distributed under the License is distributed on an \"AS IS\" BASIS,");
			h.add(" * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.");
			h.add(" * See the License for the specific language governing permissions and");
			h.add(" * limitations under the License.");
			h.add(" * ");
		} else if (this == License.BSD) {
			h.add(" * Licensed under the Revised BSD License (the Revised Berkeley Software Distribution)");
			h.add(" * ");
			h.add(" * Redistribution and use in source and binary forms, with or without");
			h.add(" * modification, are permitted provided that the following conditions are met:");
			h.add(" * ");
			h.add(" * * Redistributions of source code must retain the above copyright");
			h.add(" *   notice, this list of conditions and the following disclaimer.");
			h.add(" * * Redistributions in binary form must reproduce the above copyright");
			h.add(" *   notice, this list of conditions and the following disclaimer in the");
			h.add(" *   documentation and/or other materials provided with the distribution.");
			h.add(" * * Neither the name of the University of California, Berkeley nor the");
			h.add(" *   names of its contributors may be used to endorse or promote products");
			h.add(" *   derived from this software without specific prior written permission.");
			h.add(" * ");
			h.add(" * THIS SOFTWARE IS PROVIDED BY THE REGENTS AND CONTRIBUTORS ``AS IS'' AND ANY");
			h.add(" * EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED");
			h.add(" * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE");
			h.add(" * DISCLAIMED. IN NO EVENT SHALL THE REGENTS AND CONTRIBUTORS BE LIABLE FOR ANY");
			h.add(" * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES");
			h.add(" * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;");
			h.add(" * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND");
			h.add(" * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT");
			h.add(" * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS");
			h.add(" * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.");
			h.add(" * ");
		} else if (this == License.MIT) {
			h.add(" * Licensed under The MIT License (Massachusetts Institute of Technology License)");
			h.add(" * ");
			h.add(" * See http://opensource.org/licenses/MIT");
			h.add(" * ");
			h.add(" * Permission is hereby granted, free of charge, to any person obtaining");
			h.add(" * a copy of this software and associated documentation files (the \"Software\"),");
			h.add(" * to deal in the Software without restriction, including without limitation");
			h.add(" * the rights to use, copy, modify, merge, publish, distribute, sublicense,");
			h.add(" * and/or sell copies of the Software, and to permit persons to whom the");
			h.add(" * Software is furnished to do so, subject to the following conditions:");
			h.add(" * ");
			h.add(" * The above copyright notice and this permission notice shall be included");
			h.add(" * in all copies or substantial portions of the Software.");
			h.add(" * ");
			h.add(" * THE SOFTWARE IS PROVIDED \"AS IS\", WITHOUT WARRANTY OF ANY KIND, EXPRESS");
			h.add(" * OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,");
			h.add(" * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE");
			h.add(" * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,");
			h.add(" * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR");
			h.add(" * IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.");
			h.add(" * ");
		}
		h.add(" *");
		h.add(" * Original Author:  Arnaud Roques");
		h.addAll(contributors);
		h.add(" */");
		return Collections.unmodifiableList(h);
	}

	public List<String> getTextFull() {
		final LicenseInfo licenseInfo = LicenseInfo.retrieveQuick();
		final List<String> text = new ArrayList<String>();
		header1(text, licenseInfo);
		header2(text, licenseInfo, false);
		end3(text, licenseInfo);
		return text;
	}

	public List<String> getText1(LicenseInfo licenseInfo) {
		final List<String> text = new ArrayList<String>();
		header1(text, licenseInfo);
		return text;
	}

	public List<String> getText2(LicenseInfo licenseInfo) {
		final List<String> text = new ArrayList<String>();
		header2(text, licenseInfo, true);
		end3(text, licenseInfo);
		return text;
	}

	private void end3(final List<String> text, final LicenseInfo licenseInfo) {
		if (this == License.GPL) {
			addGpl(licenseInfo, text);
		} else if (this == License.GPLV2) {
			addGplV2(licenseInfo, text);
		} else if (this == License.MIT) {
			addMit(licenseInfo, text);
		} else if (this == License.EPL) {
			addEpl(licenseInfo, text);
		} else if (this == License.BSD) {
			addBsd(licenseInfo, text);
		} else if (this == License.APACHE) {
			addApache(licenseInfo, text);
		} else if (this == License.LGPL) {
			addLgpl(licenseInfo, text);
		} else {
			throw new IllegalStateException();
		}
		if (OptionFlags.getInstance().isEnableStats()) {
			text.add(" ");
			text.add("This version of PlantUML records general local statistics about usage.");
			text.add("(more info on https://plantuml.com/statistics-report)");
		}
		text.add(" ");
		text.add("Icons provided by OpenIconic :  https://useiconic.com/open");
		text.add("Archimate sprites provided by Archi :  http://www.archimatetool.com");
		text.add("Stdlib AWS provided by https://github.com/milo-minderbinder/AWS-PlantUML");
		text.add("Stdlib Icons provided https://github.com/tupadr3/plantuml-icon-font-sprites");
		text.add("ASCIIMathML (c) Peter Jipsen http://www.chapman.edu/~jipsen");
		text.add("ASCIIMathML (c) David Lippman http://www.pierce.ctc.edu/dlippman");
		text.add("CafeUndZopfli ported by Eugene Klyuchnikov https://github.com/eustas/CafeUndZopfli");
		text.add("Brotli (c) by the Brotli Authors https://github.com/google/brotli");
		text.add(" ");
	}
}
