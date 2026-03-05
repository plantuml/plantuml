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
package net.sourceforge.plantuml.core;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Collection;
import java.util.EnumSet;
import java.util.Objects;
import java.util.Set;

import net.sourceforge.plantuml.FileFormat;
import net.sourceforge.plantuml.Scale;
import net.sourceforge.plantuml.abel.DisplayPositioned;
import net.sourceforge.plantuml.command.Command;
import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.command.ParserPass;
import net.sourceforge.plantuml.command.ProtectedCommand;
import net.sourceforge.plantuml.klimt.color.ColorMapper;
import net.sourceforge.plantuml.klimt.color.HColor;
import net.sourceforge.plantuml.klimt.color.NoSuchColorException;
import net.sourceforge.plantuml.klimt.creole.Display;
import net.sourceforge.plantuml.klimt.geom.HorizontalAlignment;
import net.sourceforge.plantuml.klimt.geom.VerticalAlignment;
import net.sourceforge.plantuml.nio.PathSystem;
import net.sourceforge.plantuml.preproc.PreprocessingArtifact;
import net.sourceforge.plantuml.style.ClockwiseTopRightBottomLeft;
import net.sourceforge.plantuml.text.BackSlash;
import net.sourceforge.plantuml.utils.BlocLines;
import net.sourceforge.plantuml.version.Version;
import net.sourceforge.plantuml.warning.Warning;

public abstract class AbstractDiagram implements Diagram {

	private final UmlSource source;
	private Scale scale;
	private int splitPagesHorizontal = 1;
	private int splitPagesVertical = 1;

	private final PreprocessingArtifact preprocessing;

	public AbstractDiagram(UmlSource source, PreprocessingArtifact preprocessing) {
		this.source = Objects.requireNonNull(source);
		this.preprocessing = preprocessing;
	}

	public PathSystem getPathSystem() {
		return PathSystem.fetch();
	}

	private String getVersion() {
		return Version.versionString();
	}

	@Override
	final public String getMetadata() {
		if (source == null)
			return getVersion();

		final String rawString = source.getRawString("\n");
		final String plainString = source.getPlainString("\n");
		if (rawString != null && rawString.equals(plainString))
			return rawString + BackSlash.NEWLINE + getVersion();

		return rawString + BackSlash.NEWLINE + plainString + BackSlash.NEWLINE + getVersion();
	}

	@Override
	final public UmlSource getSource() {
		return source;
	}

	public final String getFlashData() {
		final UmlSource source = getSource();
		if (source == null)
			return "";

		return source.getPlainString("\n");
	}

	final public long seed() {
		if (source == null)
			return 42;

		return getSource().seed();
	}

	@Override
	public int getNbImages() {
		return 1;
	}

	public int getSplitPagesHorizontal() {
		return splitPagesHorizontal;
	}

	public void setSplitPagesHorizontal(int splitPagesHorizontal) {
		this.splitPagesHorizontal = splitPagesHorizontal;
	}

	public int getSplitPagesVertical() {
		return splitPagesVertical;
	}

	public void setSplitPagesVertical(int splitPagesVertical) {
		this.splitPagesVertical = splitPagesVertical;
	}

	public DisplayPositioned getTitle() {
		if (source == null)
			return DisplayPositioned.single(Display.empty(), HorizontalAlignment.CENTER, VerticalAlignment.TOP);

		return DisplayPositioned.single(source.getTitle(), HorizontalAlignment.CENTER, VerticalAlignment.TOP);
	}

	@Override
	public String getWarningOrError() {
		return null;
	}

	@Override
	public String checkFinalError() {
		return null;
	}

	public void makeDiagramReady() {
	}

	public boolean isIncomplete() {
		return false;
	}

	public CommandExecutionResult executeCommand(Command cmd, BlocLines lines, ParserPass currentPass) {
		cmd = new ProtectedCommand(cmd);
		try {
			return cmd.execute(this, lines, currentPass);
		} catch (NoSuchColorException e) {
			return CommandExecutionResult.badColor();
		}
	}

	@Override
	public boolean hasUrl() {
		return false;
	}

	public HColor calculateBackColor() {
		return null;
	}

	final public void setScale(Scale scale) {
		this.scale = scale;
	}

	final public Scale getScale() {
		return scale;
	}

	protected ColorMapper muteColorMapper(ColorMapper init) {
		return init;
	}

	/**
	 * Exports this diagram in XMI format. Returns {@code null} if this diagram type
	 * does not support XMI, allowing the caller to fall back to the standard
	 * TextBlock export path.
	 */
	protected ImageData exportXmi(OutputStream os, FileFormat fileFormat) throws IOException {
		return null;
	}

	/** @see #exportXmi */
	protected ImageData exportScxml(OutputStream os) throws IOException {
		return null;
	}

	/** @see #exportXmi */
	protected ImageData exportGraphml(OutputStream os) throws IOException {
		return null;
	}

	/** @see #exportXmi */
	protected ImageData exportTxt(OutputStream os, int index, FileFormat fileFormat) throws IOException {
		return null;
	}

	public ClockwiseTopRightBottomLeft getDefaultMargins() {
		return ClockwiseTopRightBottomLeft.same(0);
	}

	@Override
	public Display getTitleDisplay() {
		return Display.NULL;
	}

	@Override
	public Set<ParserPass> getRequiredPass() {
		return EnumSet.of(ParserPass.ONE);
	}

	public void startingPass(ParserPass pass) {
	}

	final public PreprocessingArtifact getPreprocessingArtifact() {
		return preprocessing;
	}

	@Override
	public void addWarning(Warning warning) {
		throw new UnsupportedOperationException();

	}

	@Override
	public Collection<Warning> getWarnings() {
		return preprocessing.getWarnings();
	}

	@Override
	public InstallationRequirement getInstallationRequirement() {
		return InstallationRequirement.NONE;
	}

}
