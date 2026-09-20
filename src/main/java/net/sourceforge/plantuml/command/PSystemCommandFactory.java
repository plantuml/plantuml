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
package net.sourceforge.plantuml.command;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import net.sourceforge.plantuml.EmbeddedDiagram;
import net.sourceforge.plantuml.ErrorUml;
import net.sourceforge.plantuml.ErrorUmlType;
import net.sourceforge.plantuml.Previous;
import net.sourceforge.plantuml.core.AbstractDiagram;
import net.sourceforge.plantuml.core.DiagramType;
import net.sourceforge.plantuml.core.UmlSource;
import net.sourceforge.plantuml.error.PSystemError;
import net.sourceforge.plantuml.error.PSystemErrorUtils;
import net.sourceforge.plantuml.explain.Explanation;
import net.sourceforge.plantuml.nio.PathSystem;
import net.sourceforge.plantuml.preproc.PreprocessingArtifact;
import net.sourceforge.plantuml.teavm.TeaVM;
import net.sourceforge.plantuml.teavm.browser.BrowserLog;
import net.sourceforge.plantuml.text.StringLocated;
import net.sourceforge.plantuml.utils.BlocLines;
import net.sourceforge.plantuml.utils.Log;
import net.sourceforge.plantuml.utils.StartUtils;
import net.sourceforge.plantuml.version.IteratorCounter2;

public abstract class PSystemCommandFactory extends PSystemAbstractFactory {

	private final List<Command> cmds = new ArrayList<>();

	// What each command of cmds declared, at the same index: its first tokens, or null for "any
	// line". Asked once, when the commands are built, and never changed afterwards.
	private List<Collection<String>> declared;

	// Every token at least one command declared: the only tokens that get a bucket of their own.
	private final Set<String> declaredTokens = new HashSet<>();

	// Every command that can start with a given first token, in registration order -- first match
	// wins, so an index may skip candidates but never reorder them. A bucket is only built the
	// first time a line starts with its token (see getBucket), and a diagram uses a handful of the
	// hundred or so tokens a factory declares: most buckets are never built at all. That is what
	// keeps a single run -- PlantUML launched from the command line for one diagram -- from paying
	// for all of them, with nothing afterwards to amortize the cost.
	private final ConcurrentHashMap<String, List<Command>> cmdsByFirstToken = new ConcurrentHashMap<>();

	// The commands that declared no first token at all, and so can match a line whichever one it
	// starts with. This is the list to try when a line's first token is one no command claimed:
	// every other command has said it cannot match such a line, which is exactly the answer
	// asking each of them one by one used to produce -- some fifty thousand times per render of a
	// thousand-line diagram.
	private final List<Command> cmdsWithoutFirstToken = new ArrayList<>();

	// Written last inside the synchronized block below, read first outside it. One factory is
	// shared by every thread (PSystemBuilder keeps a static singleton holding one of each), and
	// none of cmds, declared, declaredTokens and cmdsWithoutFirstToken changes once built, so the
	// lock is only needed for the very first parse -- this flag is what keeps every later line from
	// taking it. Reading it as true happens-after the write, which happens-after everything the
	// block filled in, so those collections are fully visible without holding anything. The buckets
	// themselves are built later, by whichever thread first needs one: the ConcurrentHashMap is
	// what makes that safe.
	private volatile boolean commandsReady;

	protected abstract void initCommandsList(List<Command> cmds);

	public abstract AbstractDiagram createEmptyDiagram(PathSystem pathSystem, UmlSource source, Previous previous,
			PreprocessingArtifact preprocessing);

	protected PSystemCommandFactory(DiagramType type) {
		super(type);
	}

	@Override
	public List<Explanation> explain(PathSystem pathSystem, UmlSource source, Previous previous,
			PreprocessingArtifact preprocessing) {
		final List<Explanation> result = new ArrayList<>();
		AbstractDiagram sys = createEmptyDiagram(pathSystem, source, previous, preprocessing);

		IteratorCounter2 it = source.iterator2();
		final StringLocated startLine = it.next();
		if (StartUtils.isStartDirective(startLine.getString()) == false)
			throw new UnsupportedOperationException();

		final Set<ParserPass> requiredPass = sys.getRequiredPass();

		for (ParserPass pass : requiredPass) {
			while (it.hasNext()) {
				if (StartUtils.isEndDirective(it.peek().getString())) {
					it = source.iterator2();
					it.next();
					// For next pass
					break;
				}
				final Explanation explanation = explainFewLines(sys, source, it, pass, preprocessing);
				if (explanation != null)
					result.add(explanation);
			}

		}

		return result;

	}

	@Override
	final public AbstractDiagram createSystem(PathSystem pathSystem, UmlSource source, Previous previous,
			PreprocessingArtifact preprocessing) {

		BrowserLog.consoleLog(getClass(), "createSystem");

		IteratorCounter2 it = source.iterator2();
		final StringLocated startLine = it.next();
		if (StartUtils.isStartDirective(startLine.getString()) == false)
			throw new UnsupportedOperationException();

		if (source.isEmpty()) {
			if (it.hasNext())
				it.next();

			return buildEmptyError(source, startLine, it.getTrace(), preprocessing);
		}
		AbstractDiagram sys = createEmptyDiagram(pathSystem, source, previous, preprocessing);

		final Set<ParserPass> requiredPass = sys.getRequiredPass();

		for (ParserPass pass : requiredPass) {
			sys.startingPass(pass);
			while (it.hasNext()) {
				if (StartUtils.isEndDirective(it.peek().getString())) {
					it = source.iterator2();
					it.next();
					// For next pass
					break;
				}
				sys = executeFewLines(sys, source, it, pass, preprocessing);
				if (sys instanceof PSystemError)
					return sys;
			}
		}
		return finalizeDiagram(sys, source, it, preprocessing);

	}

	private AbstractDiagram finalizeDiagram(AbstractDiagram sys, UmlSource source, IteratorCounter2 it,
			PreprocessingArtifact preprocessing) {
		if (sys == null)
			return null;

		final String err = sys.checkFinalError();
		if (err != null) {
			final StringLocated location = it.next();
			return buildExecutionError(source, err, location, it.getTrace(), preprocessing);
		}
		if (source.getTotalLineCount() == 2) {
			final StringLocated location = it.next();
			return buildEmptyError(source, location, it.getTrace(), preprocessing);
		}

		sys.makeDiagramReady();
		if (sys.isIncomplete())
			return null;

		return sys;
	}

	private AbstractDiagram executeFewLines(AbstractDiagram sys, UmlSource source, final IteratorCounter2 it,
			ParserPass currentPass, PreprocessingArtifact preprocessing) {
		final Step step = getCandidate(it);
		if (step == null) {
			final ErrorUml err = new ErrorUml(ErrorUmlType.SYNTAX_ERROR, "Syntax Error?", 0, it.peek(),
					getDiagramType());
			it.next();
			return PSystemErrorUtils.buildV2(source, err, null, it.getTrace(), preprocessing);
		}

		if (step.command.isEligibleFor(currentPass) == false)
			return sys;

		final CommandExecutionResult result = sys.executeCommand(step.command, step.blocLines, currentPass);
		if (result.isOk() == false) {
			final StringLocated line = (StringLocated) step.blocLines.getFirst();
			final ErrorUml err = new ErrorUml(ErrorUmlType.EXECUTION_ERROR, result.getError(), result.getScore(), line,
					getDiagramType());
			sys = PSystemErrorUtils.buildV2(source, err, result.getDebugLines(), it.getTrace(), preprocessing,
					result.getRootCause());
		}
		if (result.getNewDiagram() != null)
			sys = result.getNewDiagram();

		return sys;

	}

	private Explanation explainFewLines(AbstractDiagram sys, UmlSource source, final IteratorCounter2 it,
			ParserPass currentPass, PreprocessingArtifact preprocessing) {
		final Step step = getCandidate(it);
		if (step == null)
			return null;

		if (step.command.isEligibleFor(currentPass) == false)
			return null;

		final String explain = sys.explain(step.command, step.blocLines);
		if (explain == null || explain.isEmpty())
			return null;

		return new Explanation(step.blocLines, explain);

	}

	static class Step {
		final Command command;
		final BlocLines blocLines;

		Step(Command command, BlocLines blocLines) {
			this.command = command;
			this.blocLines = blocLines;
		}

	}

	/**
	 * The commands to try on a line whose first token is {@code token}, in registration order.
	 *
	 * A token no command declared -- a participant name, most of the time -- has no bucket of its
	 * own: only the commands that claim every line can match it. Those tokens are deliberately not
	 * cached, or the map would keep every name ever met, and grow for as long as a server runs.
	 */
	private List<Command> getBucket(String token) {
		if (declaredTokens.contains(token) == false)
			return cmdsWithoutFirstToken;

		return cmdsByFirstToken.computeIfAbsent(token, this::buildBucket);
	}

	/**
	 * The commands that declared {@code token}, or declared nothing at all, in registration order.
	 * Only ever called once per token, by the ConcurrentHashMap, and it reads nothing but lists
	 * that no longer change.
	 */
	private List<Command> buildBucket(String token) {
		final List<Command> result = new ArrayList<>();
		for (int i = 0; i < cmds.size(); i++)
			if (declared.get(i) == null || declared.get(i).contains(token))
				result.add(cmds.get(i));

		return result;
	}

	private Step getCandidate(final IteratorCounter2 it) {
		final StringLocated firstLine = it.peek();
		final BlocLines single = BlocLines.single(firstLine);
		if (commandsReady == false)
			synchronized (cmds) {
				if (commandsReady == false) {
					initCommandsList(cmds);

					// What each command declared, asked once here rather than once per line. A
					// command returning null goes to cmdsWithoutFirstToken, and will go into every
					// bucket as well, since it can match any line.
					declared = new ArrayList<>(cmds.size());
					for (Command cmd : cmds) {
						final Collection<String> own = cmd.mandatoryFirstTokens();
						declared.add(own);
						if (own == null)
							cmdsWithoutFirstToken.add(cmd);
						else
							declaredTokens.addAll(own);
					}

					// How much the index actually narrows things down for this factory: the
					// commands left in cmdsWithoutFirstToken are the ones FirstTokens could not
					// read, and so the next patterns worth teaching it.
					Log.info(() -> getClass().getSimpleName() + ": " + cmdsWithoutFirstToken.size() + "/"
							+ cmds.size() + " commands without first token, " + declaredTokens.size()
							+ " tokens indexed");

					commandsReady = true;
				}
			}

		// Both lists are already the answer: a bucket holds the commands that claimed this token
		// plus those that claim every line, and cmdsWithoutFirstToken holds the latter alone, for
		// a token nobody claimed. Nothing is left to ask at this point.
		for (Command cmd : getBucket(firstLine.getFirstToken())) {

			final CommandControl result = safeIsValid(cmd, single);
			if (result == CommandControl.OK) {
				it.next();
				return new Step(cmd, single);
			}
			if (result == CommandControl.OK_PARTIAL) {
				final IteratorCounter2 cloned = it.cloneMe();
				final BlocLines lines = isMultilineCommandOk(cloned, cmd);
				if (lines == null)
					continue;

				it.copyStateFrom(cloned);
				return new Step(cmd, lines);
			}
		}
		return null;
	}

	/**
	 * Safely evaluates a command against input lines. In TeaVM (browser JS),
	 * complex regex patterns on long lines (e.g. base64-encoded images) can cause a
	 * stack overflow in the transpiled regex engine. This method catches such
	 * errors and treats them as NOT_OK, allowing parsing to continue with the next
	 * command.
	 */
	private static CommandControl safeIsValid(Command cmd, BlocLines single) {
		try {
			return cmd.isValid(single);
		} catch (Exception e) {
			if (TeaVM.isTeaVM()) {
				BrowserLog.consoleLog(cmd.getClass(), "Big exception " + e);
				return CommandControl.NOT_OK;
			}
			throw e;
		}
	}

	private BlocLines isMultilineCommandOk(IteratorCounter2 it, Command cmd) {
		BlocLines lines = BlocLines.create();
		int nb = 0;
		while (it.hasNext()) {
			lines = addOneSingleLineManageEmbedded2(it, lines);
			final CommandControl result = cmd.isValid(lines);
			if (result == CommandControl.NOT_OK)
				return null;

			if (result == CommandControl.OK)
				return lines;

			nb++;
			if (cmd instanceof CommandDecoratorMultine && nb > ((CommandDecoratorMultine) cmd).getNbMaxLines())
				return null;

		}
		return null;
	}

	private static BlocLines addOneSingleLineManageEmbedded2(IteratorCounter2 it, BlocLines lines) {
		final StringLocated linetoBeAdded = it.next();
		lines = lines.add(linetoBeAdded);
		if (EmbeddedDiagram.getEmbeddedType(linetoBeAdded.getString()) != null) {
			int nested = 1;
			while (it.hasNext()) {
				final StringLocated s = it.next();
				lines = lines.add(s);
				if (EmbeddedDiagram.getEmbeddedType(s.getString()) != null)
					nested++;
				else if (s.getTrimmed().getString().equals(EmbeddedDiagram.EMBEDDED_END)) {
					nested--;
					if (nested == 0)
						return lines;
				}
			}
		}

		return lines;
	}

}
