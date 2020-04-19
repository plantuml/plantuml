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
package net.sourceforge.plantuml.tim.iterator;

import java.util.Collections;
import java.util.Map;

import net.sourceforge.plantuml.StringLocated;
import net.sourceforge.plantuml.preproc.Sub;
import net.sourceforge.plantuml.tim.EaterException;
import net.sourceforge.plantuml.tim.EaterExceptionLocated;
import net.sourceforge.plantuml.tim.EaterStartsub;
import net.sourceforge.plantuml.tim.TContext;
import net.sourceforge.plantuml.tim.TLineType;
import net.sourceforge.plantuml.tim.TMemory;

public class CodeIteratorSub extends AbstractCodeIterator {

	private final Map<String, Sub> subs;

	private CodeIterator readingInProgress;

	private final TMemory memory;
	private final TContext context;

	public CodeIteratorSub(CodeIterator source, Map<String, Sub> subs, TContext context, TMemory memory) {
		super(source);
		this.context = context;
		this.memory = memory;
		this.subs = subs;
	}

	public Map<String, Sub> getSubs() {
		return Collections.unmodifiableMap(subs);
	}

	public StringLocated peek() throws EaterException, EaterExceptionLocated {
		StringLocated result = source.peek();
		if (result == null) {
			return null;
		}
		if (result.getType() == TLineType.STARTSUB) {
			final EaterStartsub eater = new EaterStartsub(result.getTrimmed());
			eater.analyze(context, memory);
			final Sub created = new Sub(eater.getSubname());
			this.subs.put(eater.getSubname(), created);
			source.next();
			StringLocated s = null;
			while ((s = source.peek()) != null) {
				if (s.getType() == TLineType.STARTSUB) {
					throw EaterException.located("Cannot nest sub", s);
				} else if (s.getType() == TLineType.ENDSUB) {
					source.next();
					readingInProgress = new CodeIteratorImpl(created.lines());
					break;
				} else {
					created.add(s);
					source.next();
				}
			}
		}
		if (readingInProgress != null) {
			return readingInProgress.peek();
		}
		return result;
	}

	@Override
	public void next() throws EaterException, EaterExceptionLocated {
		if (readingInProgress == null) {
			source.next();
			return;
		}
		readingInProgress.next();
		if (readingInProgress.peek() == null) {
			readingInProgress = null;
		}
	}

}
