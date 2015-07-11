/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2014, Arnaud Roques
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

	private static final List<String> quotes = Arrays
			.asList(//
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
					"Big mistake!", //
					"How many UML designers does it take to change a lightbulb ?", //
					"Do you like movies about gladiators ?", //
					"The spirit of learning is a lasting frontier.", //
					"It is curious for sailors this need for making sentences.", //
					"Hoping for the best, but expecting the worst", //
					"The will to go on when I'm hurt deep inside.", //
					"If it bleeds, we can kill it.", //
					"Houston, I have a bad feeling about this mission.", //
					"Mama always said life was like a box of chocolates. You never know what you're gonna get.", //
					"By the way, is there anyone on board who knows how to fly a plane?", //
					"Dave, this conversation can serve no purpose anymore. Goodbye.", //
					"It can only be attributable to human error.", //
					"Looks like I picked the wrong week to quit smoking.", //
					"You humans act so strange. Everything you create is used to destroy.", //
					"Where did you learn how to negotiate like that?", //
					"Sir, are you classified as human?", //
					"We're not gonna make it, are we?", //
					"It's in your nature to destroy yourselves.", //
					"The more contact I have with humans, the more I learn.", //
					"Would it save you a lot of time if I just gave up and went mad now?", //
					"Reality is frequently inaccurate.", //
					"Don't believe anything you read on the net. Except this. Well, including this, I suppose.", //
					"A cup of tea would restore my normality.", //
					"Anything that thinks logically can be fooled by something else that thinks at least as logically as it does.", //
					"In an infinite Universe anything can happen.", //
					"Sometimes if you received an answer, the question might be taken away.", //
					"Please call me Eddie if it will help you to relax.", //
					"I don't believe it. Prove it to me and I still won't believe it.", //
					"Totally mad, utter nonsense. But we'll do it because it's brilliant nonsense.", //
					"This sentence is not true.", //
					"I would rather die standing than live on my knees.", //
					"You are being watched.", //
					"Did you feed them after midnight?", //
					"How do you explain school to higher intelligence?", //
					"People sometimes make mistakes.", //
					"Look, I don't have time for a conversation right now.", //
					"All problems in computer science can be solved by another level of indirection", //
					"...except for the problem of too many levels of indirection", //
					"I know because I built it", //
					"Even the smallest person can change the course of the future.", //
					"If you are a friend, you speak the password, and the doors will open.", //
					"You Shall Not Pass", //
					"73.6% Of All Statistics Are Made Up", //
					"We can neither confirm nor deny that this is crashing", //
					"When the beating of your heart echoes the beating of the drums", //
					"Never trust a computer you can't throw out a window", //
					"Yeah, I'm calm. I'm a calm person. Is there some reason I shouldn't be calm?", //
					"Everybody just stay calm. The situation is under control.", //
					"Hippy, you think everything is a conspiracy.", //
					"These guys are about as much fun as a tax audit.", //
					"There is something down there! Something not us.", //
					"I saw a glimpse of my future and everything's changed for me now.", //
					"In space no one can hear you scream", //
					"I can't lie to you about your chances, but... you have my sympathies.", // 
					"There is an explanation for this, you know.", //
					"Bishop: I'm afraid I have some bad news.", //
					"Do me a favour. Disconnect me. I could be reworked, but I'll never be top of the line again.", //
					"Take it easy, don't push the little button on the joystick!", //
					"I'm a very private person.", //
					"To sculpt an elephant from a big block of marble, just knock away all the bits that don't look like an elephant.", //
					"Who said you could talk to me? Have I got something on my face ?"
			);
	private QuoteUtils() {
	}

	public static String getSomeQuote() {
		final int v = (int) (System.currentTimeMillis() / 1000L);
		return quotes.get(v % quotes.size());
	}
}
