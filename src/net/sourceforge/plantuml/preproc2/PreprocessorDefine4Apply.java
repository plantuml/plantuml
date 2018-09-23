/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2017, Arnaud Roques
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
package net.sourceforge.plantuml.preproc2;

import java.io.IOException;
import java.util.List;

import net.sourceforge.plantuml.CharSequence2;
import net.sourceforge.plantuml.CharSequence2Impl;
import net.sourceforge.plantuml.preproc.Defines;
import net.sourceforge.plantuml.preproc.ReadLine;
import net.sourceforge.plantuml.preproc.ReadLineList;

public class PreprocessorDefine4Apply implements ReadFilter {

	private final Defines defines;

	public PreprocessorDefine4Apply(Defines defines) throws IOException {
		this.defines = defines;
	}

	public ReadLine applyFilter(final ReadLine source) {
		return new Inner(source);
	}

	class Inner extends ReadLineInsertable {

		final ReadLine source;

		Inner(ReadLine source) {
			this.source = source;
		}

		@Override
		void closeInternal() throws IOException {
			source.close();
		}

		@Override
		CharSequence2 readLineInternal() throws IOException {
			final CharSequence2 s = this.source.readLine();
			if (s == null || s.getPreprocessorError() != null) {
				return s;
			}
			if (PreprocessorDefine3Learner.isLearningLine(s)) {
				return s;
			}
			final List<String> result = defines.applyDefines(s.toString2());
			if (result.size() > 1) {
				insert(new ReadLineList(result, s.getLocation()));
				return readLine();
			}
			String tmp = result.get(0);
			return new CharSequence2Impl(tmp, s.getLocation(), s.getPreprocessorError());
		}

	}

	// private final ReadLine2 source;
	// // private final List<CharSequence2> result = new ArrayList<CharSequence2>();
	// // private final PreprocessorDefine2 preprocDefines;
	// private final Defines defines;
	//
	// private final PreprocessorInclude2 include;
	//
	// // private final SubPreprocessor2 subPreprocessor2;
	//
	// public PreprocessorDefine4Apply(List<String> config, ReadLine reader, String charset, Defines defines, File
	// newCurrentDir,
	// DefinitionsContainer definitionsContainer) throws IOException {
	// // final IfManager tmp1 = new IfManager(new ReadLineQuoteComment(reader), defines);
	// final ReadFilter addConfig = new ReadLineAddConfig2(config);
	// final ReadFilter filterComment = new ReadLineQuoteComment2();
	// final ReadFilter filterIf = new IfManagerFilter(defines);
	// final ReadFilter filterSub = new SubPreprocessor2(charset, defines, definitionsContainer);
	// final ReadFilter filterLearnDefine = new PreprocessorDefine3Learner(defines);
	// this.source = new ReadLine2Adapter(new ReadFilterAnd(addConfig, filterIf).applyFilter(reader),
	// new ReadFilterAnd(filterSub, filterComment, filterLearnDefine));
	// this.defines = defines;
	// // this.preprocDefines = new PreprocessorDefine2(defines);
	// this.include = new PreprocessorInclude2(charset);
	// // this.subPreprocessor2 = new SubPreprocessor2(charset, defines, definitionsContainer);
	// }
	//
	// public CharSequence2 readLine() throws IOException {
	// while (true) {
	// // this.preprocDefines.learnDefinition(this.source);
	// if (this.include.learnInclude(this.source)) {
	// continue;
	// }
	// // if (this.subPreprocessor2.learn(this.source)) {
	// // continue;
	// // }
	//
	// final CharSequence2 s = this.source.readLine();
	// if (s == null) {
	// return null;
	// }
	// String tmp = manageDefines(s, source);
	// if (tmp != null) {
	// return new CharSequence2Impl(tmp, s.getLocation(), s.getPreprocessorError());
	// }
	// }
	// }
	// public void close() throws IOException {
	// this.source.close();
	// }
	//
	// public Set<FileWithSuffix> getFilesUsed() {
	// System.err.println("************************** WARNING **************************");
	// return Collections.emptySet();
	// }
}