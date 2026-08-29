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
package net.sourceforge.plantuml.style.parser2;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

/**
 * Pins {@link LevelConstraint#matches(LevelConstraint, LevelConstraint)} against the exact
 * truth table read off {@code StyleSignatureBasic#matchAllImpl} in the legacy code: a
 * declaration's {@code level}/{@code star} pair against a query's.
 */
class LevelConstraintTest {

	@Test
	void declarationWithNoLevelAlwaysPassesTheLevelTest() {
		final LevelConstraint declaration = LevelConstraint.none();
		assertTrue(LevelConstraint.matches(declaration, LevelConstraint.of(0, false)));
		assertTrue(LevelConstraint.matches(declaration, LevelConstraint.of(41, false)));
		assertTrue(LevelConstraint.matches(declaration, LevelConstraint.none()));
	}

	@Test
	void nonStarredDeclarationRequiresAnExactLevelMatch() {
		final LevelConstraint declaration = LevelConstraint.of(3, false);
		assertTrue(LevelConstraint.matches(declaration, LevelConstraint.of(3, false)));
		assertFalse(LevelConstraint.matches(declaration, LevelConstraint.of(2, false)));
		assertFalse(LevelConstraint.matches(declaration, LevelConstraint.of(4, false)));
		// A query with no level at all never satisfies a declaration that requires one.
		assertFalse(LevelConstraint.matches(declaration, LevelConstraint.none()));
	}

	@Test
	void starredDeclarationMatchesItsLevelOrAnyDeeperOne() {
		final LevelConstraint declaration = LevelConstraint.of(2, true);
		assertFalse(LevelConstraint.matches(declaration, LevelConstraint.of(1, false)));
		assertTrue(LevelConstraint.matches(declaration, LevelConstraint.of(2, false)));
		assertTrue(LevelConstraint.matches(declaration, LevelConstraint.of(3, false)));
		assertTrue(LevelConstraint.matches(declaration, LevelConstraint.of(63, false)));
		assertFalse(LevelConstraint.matches(declaration, LevelConstraint.none()));
	}

	@Test
	void starredQueryOnlyMatchesStarredDeclarations() {
		// This is the ancestor-inheritance lookup: only a catch-all (starred) declaration can
		// satisfy it, whether or not it also constrains the level.
		final LevelConstraint starQuery = LevelConstraint.of(5, true);

		assertTrue(LevelConstraint.matches(LevelConstraint.of(5, true), starQuery));
		assertTrue(LevelConstraint.matches(LevelConstraint.of(2, true), starQuery));
		assertFalse(LevelConstraint.matches(LevelConstraint.of(6, true), starQuery));

		// No level at all, but starred: still always passes a starred query.
		assertTrue(LevelConstraint.matches(LevelConstraint.of(LevelConstraint.NO_LEVEL, true), starQuery));

		// Not starred: fails a starred query even with a level that would otherwise match.
		assertFalse(LevelConstraint.matches(LevelConstraint.of(5, false), starQuery));
		assertFalse(LevelConstraint.matches(LevelConstraint.none(), starQuery));
	}

	@Test
	void nonStarredQueryIgnoresWhetherTheDeclarationIsStarred() {
		final LevelConstraint query = LevelConstraint.of(5, false);
		assertTrue(LevelConstraint.matches(LevelConstraint.of(5, false), query));
		assertTrue(LevelConstraint.matches(LevelConstraint.of(5, true), query));
		assertTrue(LevelConstraint.matches(LevelConstraint.none(), query));
	}

	@Test
	void level63IsAcceptedWithNoShiftWraparound() {
		final LevelConstraint declaration = LevelConstraint.of(63, true);
		assertTrue(LevelConstraint.matches(declaration, LevelConstraint.of(63, false)));
		assertFalse(LevelConstraint.matches(declaration, LevelConstraint.of(62, false)));
	}

	@Test
	void levelOutOfRangeIsRejected() {
		assertThrows(IllegalArgumentException.class, new org.junit.jupiter.api.function.Executable() {
			@Override
			public void execute() {
				LevelConstraint.of(64, false);
			}
		});
		assertThrows(IllegalArgumentException.class, new org.junit.jupiter.api.function.Executable() {
			@Override
			public void execute() {
				LevelConstraint.of(-2, false);
			}
		});
	}

	@Test
	void equalsAndHashCodeAreValueBased() {
		assertEquals(LevelConstraint.of(4, true), LevelConstraint.of(4, true));
		assertEquals(LevelConstraint.of(4, true).hashCode(), LevelConstraint.of(4, true).hashCode());
		assertFalse(LevelConstraint.of(4, true).equals(LevelConstraint.of(4, false)));
	}

}
