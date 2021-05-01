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
package net.sourceforge.plantuml.dedication;

import java.awt.image.BufferedImage;
import java.math.BigInteger;

public interface Dedication {

	public static final int SIZE = 512;

	static public final BigInteger E = new BigInteger("47");
	static public final BigInteger N = new BigInteger(
			"64194259632025692228025828504368542164501926620236990850309916606915924860847417702229807236946186163298479808527077315212362810246237044147835839820235668271044023359607622658694578433933680627840319408427732468918341837133798296090069295727323673222224923200718714534955390633175683720810506099934813509605263799234445827953809462431871169282281822048299576307847441008670575692934434087522877910989584374673170522742162366773143807761599862833698229067475807108264396251702152180676841544743258182370105404479387062985271422237607462447989728490398294623785717593446941673706569352249533885603771123718557406286501161336667835919957553680522213067630956498293529840163155604109185561515875171125161872265975088797712442352939352686113608345330266855433849127812528823634773975825170679786399199082599910532761710473383280738663105826045325480095451410448217715495894688594898541182351588505292424154550388343455540760277051977859647543838445735549451966254020972172982014944475678385523833120793348365125754234511467512831686599126674298367512469557219326026525667529348508876650236597163509336304607610284488623800062157659286940214435134423619711736992281071131245654755167288438258292694799131521268600284444731890784171372171309");

	public BufferedImage getImage(TinyHashableString sentence);

}
