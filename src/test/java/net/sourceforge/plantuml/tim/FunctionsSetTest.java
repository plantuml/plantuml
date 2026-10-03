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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

import net.sourceforge.plantuml.text.StringLocated;
import net.sourceforge.plantuml.tim.builtin.Strlen;
import net.sourceforge.plantuml.tim.expression.TValue;

class FunctionsSetTest {

	private static final TFunctionSignature STRLEN = new TFunctionSignature("%strlen", 1);

	private static class FakeStrlen extends Strlen {
		@Override
		public TValue executeReturnFunction(TContext context, TMemory memory, StringLocated location,
				List<TValue> values, Map<String, TValue> named) {
			return TValue.fromInt(-1);
		}
	}

	@Test
	void standardFunctionsAreSharedByAllContexts() {
		final FunctionsSet a = new FunctionsSet(StandardFunctions.get());
		final FunctionsSet b = new FunctionsSet(StandardFunctions.get());
		assertNotNull(a.getFunctionSmart(STRLEN));
		assertSame(a.getFunctionSmart(STRLEN), b.getFunctionSmart(STRLEN));
		assertTrue(a.doesFunctionExist("%strlen"));
		assertFalse(a.doesFunctionExist("%unknown"));
		assertNull(a.getFunctionSmart(new TFunctionSignature("%unknown", 1)));
	}

	@Test
	void trieSeesTheStandardLayer() {
		final FunctionsSet a = new FunctionsSet(StandardFunctions.get());
		assertEquals("%strlen(", a.getLonguestMatchStartingIn("x %strlen(abc)", 2));
		assertEquals("", a.getLonguestMatchStartingIn("x %strlen(abc)", 1));
		assertEquals("", a.getLonguestMatchStartingIn("%nothing(abc)", 0));
	}

	@Test
	void userFunctionShadowsStandardOneWithoutTouchingTheSharedLayer() {
		final FunctionsSet a = new FunctionsSet(StandardFunctions.get());
		final TFunction mine = new FakeStrlen();
		a.addFunction(mine);

		assertSame(mine, a.getFunctionSmart(STRLEN));
		int count = 0;
		for (TFunction f : a.getFunctionsByName("%strlen")) {
			assertSame(mine, f);
			count++;
		}
		assertEquals(1, count);
		assertEquals("%strlen(", a.getLonguestMatchStartingIn("%strlen(a)", 0));

		final FunctionsSet other = new FunctionsSet(StandardFunctions.get());
		assertFalse(other.getFunctionSmart(STRLEN) instanceof FakeStrlen);
	}

	@Test
	void standardFunctionsHoldNoState() throws Exception {
		for (TFunction f : StandardFunctions.get().getFunctionsByName("%strlen"))
			assertNoInstanceField(f);
		for (String name : new String[] { "%random", "%bool", "%date", "%now", "%getenv", "%filename", "%eval",
				"%upper", "%get_json_key", "%call_user_func", "%invoke_procedure" })
			for (TFunction f : StandardFunctions.get().getFunctionsByName(name))
				assertNoInstanceField(f);
	}

	private static void assertNoInstanceField(TFunction f) {
		for (Class<?> c = f.getClass(); c != null && c != Object.class; c = c.getSuperclass())
			for (Field field : c.getDeclaredFields())
				assertTrue(Modifier.isStatic(field.getModifiers()), c.getName() + "." + field.getName());
	}

}
