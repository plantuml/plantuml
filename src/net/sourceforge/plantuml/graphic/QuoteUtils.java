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
 * Revision $Revision: 10930 $
 *
 */
package net.sourceforge.plantuml.graphic;

import java.util.Arrays;
import java.util.List;

public class QuoteUtils {

	private static final List<String> quotes = Arrays.asList(//
			"He's dead, Jim.", //
			"By Grabthar's hammer, by the sons of Worvan, you shall be avenged.", //
			"Roads? Where we're going, we don't need roads.", //
			"The time is out of joint.", //
			"C'est curieux chez les marins ce besoin de faire des phrases.", //
			"I'm talking about the other Peter, the one on the other side.", //
			"May the Force be with you!", //
			"Never give up, never surrender...", //
			"Hasta la vista, baby.", //
			"Hey, Doc, we better back up. We don't have enough road to get up to 88.", //
			"Greetings, Professor Falken. Shall we play a game?", //
			"I can't change the law of physics!", //
			"A strange game. The only winning move is not to play.", //
			"I'm the Gatekeeper, are you the Keymaster?", //
			"I am the Master Control Program. No one User wrote me.", //
			"Life? Don't talk to me about life.", //
			"I always thought something was fundamentally wrong with the universe.", //
			"A robot may not injure a human being or, through inaction, allow a human being to come to harm.", //
			"Surrender may be our only option.", //
			"Six by nine. Forty two.", //
			"It's life, Jim, but not as we know it.", //
			"Don't Panic!", //
			"What do you mean? An African or European swallow?", //
			"You forgot to say please...", //
			"You have died of dysentery.", //
			"Wouldn't you prefer a nice game of chess?", //
			"When you have eliminated the impossible, whatever remains, however improbable, must be the truth.", //
			"I know now why you cry. But it's something I can never do.", //
			"Resistance is futile. You will be assimilated.", //
			"Anything different is good.", //
			"Cracked by Aldo Reset and Laurent Rueil.", //
			"I'm both. I'm a celebrity in an emergency.", //
			"Do you know this great great polish actor, Joseph Tura?", //
			"To infinity and beyond!", //
			"Space: the final frontier...", //
			"Sur mon billet, tenez, y a ecrit Saint-Lazare, c'est mes yeux ou quoi ?", //
			"The boy is important. He has to live.", //
			"Once upon a time in a galaxy far, far away...", //
			"And you know there's a long long way ahead of you...", //
			"An allergy to oxygen? Elm blight?", //
			"But alors you are French!", //
			"N'ai-je donc tant vecu que pour cette infamie?", //
			"Something is rotten in the State of Denmark.", //
			"Hey, what do you want? Miracles?", //
			"1.21 gigawatts! 1.21 gigawatts. Great Scott! ", //
			"What the hell is a gigawatt?", //
			"I need a vacation.", //
			"On devrait jamais quitter Montauban.", //
			"My force is a platform that you can climb on...", //
			"There's something weird, and it don't look good...", //
			"Et rien vraiment ne change mais tout est different", //
			"Beam me up, Scotty.", //
			"There is no spoon.", //
			"Follow the white rabbit.", //
			"Never send a human to do a machine's job.", //
			"Guru meditation. Press left mouse button to continue.", //
			"I don't think we're in Kansas anymore.", //
			"Luke, I am your father.", //
			"Blood, Sweat and Tears", //
			"Houston, we have a problem.", //
			"Boot failure, press any key to continue", //
			"Big mistake!");
	// Creativity : how many? 
	// How many folksingers does it take to change a lightbulb ?

	private QuoteUtils() {
	}

	public static String getSomeQuote() {
		final int v = (int) (System.currentTimeMillis() / 1000L);
		return quotes.get(v % quotes.size());
	}
}
