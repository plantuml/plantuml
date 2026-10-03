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
package net.sourceforge.plantuml.tim;

import net.sourceforge.plantuml.teavm.TeaVM;
import net.sourceforge.plantuml.tim.builtin.AlwaysFalse;
import net.sourceforge.plantuml.tim.builtin.AlwaysTrue;
import net.sourceforge.plantuml.tim.builtin.Backslash;
import net.sourceforge.plantuml.tim.builtin.BoolVal;
import net.sourceforge.plantuml.tim.builtin.Breakline;
import net.sourceforge.plantuml.tim.builtin.CallUserFunction;
import net.sourceforge.plantuml.tim.builtin.Chr;
import net.sourceforge.plantuml.tim.builtin.Darken;
import net.sourceforge.plantuml.tim.builtin.DateFunction;
import net.sourceforge.plantuml.tim.builtin.Dec2hex;
import net.sourceforge.plantuml.tim.builtin.Dirpath;
import net.sourceforge.plantuml.tim.builtin.Dollar;
import net.sourceforge.plantuml.tim.builtin.Eval;
import net.sourceforge.plantuml.tim.builtin.Feature;
import net.sourceforge.plantuml.tim.builtin.FileExists;
import net.sourceforge.plantuml.tim.builtin.Filedate;
import net.sourceforge.plantuml.tim.builtin.Filename;
import net.sourceforge.plantuml.tim.builtin.FilenameNoExtension;
import net.sourceforge.plantuml.tim.builtin.FunctionExists;
import net.sourceforge.plantuml.tim.builtin.GetAllStdlib;
import net.sourceforge.plantuml.tim.builtin.GetAllTheme;
import net.sourceforge.plantuml.tim.builtin.GetCurrentTheme;
import net.sourceforge.plantuml.tim.builtin.GetJsonKey;
import net.sourceforge.plantuml.tim.builtin.GetJsonType;
import net.sourceforge.plantuml.tim.builtin.GetStdlib;
import net.sourceforge.plantuml.tim.builtin.GetVariableValue;
import net.sourceforge.plantuml.tim.builtin.GetVersion;
import net.sourceforge.plantuml.tim.builtin.Getenv;
import net.sourceforge.plantuml.tim.builtin.Hex2dec;
import net.sourceforge.plantuml.tim.builtin.HslColor;
import net.sourceforge.plantuml.tim.builtin.IntVal;
import net.sourceforge.plantuml.tim.builtin.InvokeProcedure;
import net.sourceforge.plantuml.tim.builtin.IsDark;
import net.sourceforge.plantuml.tim.builtin.IsLight;
import net.sourceforge.plantuml.tim.builtin.JsonAdd;
import net.sourceforge.plantuml.tim.builtin.JsonKeyExists;
import net.sourceforge.plantuml.tim.builtin.JsonMerge;
import net.sourceforge.plantuml.tim.builtin.JsonRemove;
import net.sourceforge.plantuml.tim.builtin.JsonSet;
import net.sourceforge.plantuml.tim.builtin.LeftAlign;
import net.sourceforge.plantuml.tim.builtin.Lighten;
import net.sourceforge.plantuml.tim.builtin.LoadJson;
import net.sourceforge.plantuml.tim.builtin.LogicalAnd;
import net.sourceforge.plantuml.tim.builtin.LogicalNand;
import net.sourceforge.plantuml.tim.builtin.LogicalNor;
import net.sourceforge.plantuml.tim.builtin.LogicalNot;
import net.sourceforge.plantuml.tim.builtin.LogicalNxor;
import net.sourceforge.plantuml.tim.builtin.LogicalOr;
import net.sourceforge.plantuml.tim.builtin.LogicalXor;
import net.sourceforge.plantuml.tim.builtin.Lower;
import net.sourceforge.plantuml.tim.builtin.Modulo;
import net.sourceforge.plantuml.tim.builtin.Newline;
import net.sourceforge.plantuml.tim.builtin.NewlineShort;
import net.sourceforge.plantuml.tim.builtin.Now;
import net.sourceforge.plantuml.tim.builtin.Ord;
import net.sourceforge.plantuml.tim.builtin.Percent;
import net.sourceforge.plantuml.tim.builtin.RandomFunction;
import net.sourceforge.plantuml.tim.builtin.RetrieveProcedure;
import net.sourceforge.plantuml.tim.builtin.ReverseColor;
import net.sourceforge.plantuml.tim.builtin.ReverseHsluvColor;
import net.sourceforge.plantuml.tim.builtin.RightAlign;
import net.sourceforge.plantuml.tim.builtin.SetVariableValue;
import net.sourceforge.plantuml.tim.builtin.Size;
import net.sourceforge.plantuml.tim.builtin.SplitStr;
import net.sourceforge.plantuml.tim.builtin.SplitStrRegex;
import net.sourceforge.plantuml.tim.builtin.Str2Json;
import net.sourceforge.plantuml.tim.builtin.StringFunction;
import net.sourceforge.plantuml.tim.builtin.Strlen;
import net.sourceforge.plantuml.tim.builtin.Strpos;
import net.sourceforge.plantuml.tim.builtin.Substr;
import net.sourceforge.plantuml.tim.builtin.Tabulation;
import net.sourceforge.plantuml.tim.builtin.Upper;
import net.sourceforge.plantuml.tim.builtin.VariableExists;
import net.sourceforge.plantuml.tim.builtin.Xargs;

/**
 * The functions that every diagram knows, built once and shared by all the
 * {@link TContext}: they hold no state, so they can be used concurrently.
 */
public final class StandardFunctions {

	private static final FunctionsSet INSTANCE = build();

	private StandardFunctions() {
	}

	/**
	 * Returns the shared registry. It must never be modified.
	 */
	static FunctionsSet get() {
		return INSTANCE;
	}

	private static FunctionsSet build() {
		final FunctionsSet functionsSet = new FunctionsSet();
		functionsSet.addFunction(new AlwaysFalse());
		functionsSet.addFunction(new AlwaysTrue());
		functionsSet.addFunction(new Backslash());
		functionsSet.addFunction(new BoolVal());
		functionsSet.addFunction(new Breakline());
		functionsSet.addFunction(new CallUserFunction());
		functionsSet.addFunction(new Chr());
		functionsSet.addFunction(new Darken());
		functionsSet.addFunction(new DateFunction());
		functionsSet.addFunction(new Dec2hex());
		functionsSet.addFunction(new Dirpath());
		functionsSet.addFunction(new Dollar());
		functionsSet.addFunction(new Eval());
		functionsSet.addFunction(new Feature());
		functionsSet.addFunction(new Filedate());
		functionsSet.addFunction(new FileExists());
		functionsSet.addFunction(new Filename());
		functionsSet.addFunction(new FilenameNoExtension());
		functionsSet.addFunction(new FunctionExists());
		if (!TeaVM.isTeaVM()) {
			functionsSet.addFunction(new GetAllStdlib());
		}
		functionsSet.addFunction(new GetAllTheme());
		functionsSet.addFunction(new GetCurrentTheme());
		functionsSet.addFunction(new GetJsonKey());
		functionsSet.addFunction(new GetJsonType());
		if (!TeaVM.isTeaVM()) {
			functionsSet.addFunction(new GetStdlib());
		}
		functionsSet.addFunction(new GetVariableValue());
		functionsSet.addFunction(new GetVersion());
		functionsSet.addFunction(new Getenv());
		functionsSet.addFunction(new Hex2dec());
		functionsSet.addFunction(new HslColor());
		functionsSet.addFunction(new IntVal());
		functionsSet.addFunction(new InvokeProcedure());
		functionsSet.addFunction(new IsDark());
		functionsSet.addFunction(new IsLight());
		functionsSet.addFunction(new JsonAdd());
		functionsSet.addFunction(new JsonKeyExists());
		functionsSet.addFunction(new JsonMerge());
		functionsSet.addFunction(new JsonRemove());
		functionsSet.addFunction(new JsonSet());
		functionsSet.addFunction(new LeftAlign());
		functionsSet.addFunction(new Lighten());
		functionsSet.addFunction(new LoadJson());
		// functionsSet.addFunction(new LoadJsonLegacy());
		functionsSet.addFunction(new LogicalAnd());
		functionsSet.addFunction(new LogicalNand());
		functionsSet.addFunction(new LogicalNor());
		functionsSet.addFunction(new LogicalNot());
		functionsSet.addFunction(new LogicalNxor());
		functionsSet.addFunction(new LogicalOr());
		functionsSet.addFunction(new LogicalXor());
		functionsSet.addFunction(new Lower());
		functionsSet.addFunction(new Modulo());
		functionsSet.addFunction(new Newline());
		functionsSet.addFunction(new NewlineShort());
		functionsSet.addFunction(new Now());
		functionsSet.addFunction(new Ord());
		functionsSet.addFunction(new Percent());
		functionsSet.addFunction(new RandomFunction());
		functionsSet.addFunction(new RetrieveProcedure());
		functionsSet.addFunction(new ReverseColor());
		functionsSet.addFunction(new ReverseHsluvColor());
		functionsSet.addFunction(new RightAlign());
		functionsSet.addFunction(new SetVariableValue());
		functionsSet.addFunction(new Size());
		functionsSet.addFunction(new SplitStr());
		functionsSet.addFunction(new SplitStrRegex());
		functionsSet.addFunction(new Str2Json());
		functionsSet.addFunction(new StringFunction());
		functionsSet.addFunction(new Strlen());
		functionsSet.addFunction(new Strpos());
		functionsSet.addFunction(new Substr());
		functionsSet.addFunction(new Tabulation());
		functionsSet.addFunction(new Upper());
		functionsSet.addFunction(new VariableExists());
		functionsSet.addFunction(new Xargs());
		// %standard_exists_function
		// %str_replace
		// !exit
		// !log
		// %min
		// %max
		// Regexp
		// %time
		// %trim
		return functionsSet;
	}

}
