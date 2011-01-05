/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009, Arnaud Roques
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
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public
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
 * Revision $Revision: 5775 $
 *
 */
package net.sourceforge.plantuml.cucadiagram.dot;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import net.sourceforge.plantuml.ISkinParam;

public class StaticFilesMap {

	private final Map<String, StaticFiles> map = new HashMap<String, StaticFiles>();
	private final ISkinParam param;
	private final double dpiFactor;

	public StaticFilesMap(ISkinParam param, double dpiFactor) {
		this.param = param;
		this.dpiFactor = dpiFactor;
	}

	public StaticFiles getStaticFiles(String stereotype) throws IOException {
		StaticFiles result = map.get(stereotype);
		if (result == null) {
			result = new StaticFiles(param, stereotype, dpiFactor);
			map.put(stereotype, result);
		}
		return result;
	}

	public DrawFile getDrawFile(String href) throws IOException {
		for (StaticFiles staticFiles : map.values()) {
			final DrawFile drawFile = staticFiles.getDrawFile(href);
			if (drawFile != null) {
				return drawFile;
			}
		}
		return null;
	}

}
