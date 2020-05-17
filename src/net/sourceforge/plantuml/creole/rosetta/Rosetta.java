package net.sourceforge.plantuml.creole.rosetta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import net.sourceforge.plantuml.cucadiagram.Display;

public class Rosetta {

	private final List<String> unicodeHtml;

	public static Rosetta fromUnicodeHtml(List<String> lines) {
		return new Rosetta(lines);
	}

	public static Rosetta fromSyntax(WikiLanguage syntaxSource, String... wiki) {
		return new Rosetta(syntaxSource, Arrays.asList(wiki));
	}

	public static Rosetta fromSyntax(WikiLanguage syntaxSource, List<String> wiki) {
		return new Rosetta(syntaxSource, wiki);
	}

	public static Rosetta fromSyntax(WikiLanguage syntaxSource, Display display) {
		return new Rosetta(syntaxSource, from(display));
	}

	private static List<String> from(Display display) {
		final List<String> result = new ArrayList<String>();
		for (CharSequence cs : display) {
			result.add(cs.toString());
		}
		return result;
	}

	private Rosetta(List<String> lines) {
		this.unicodeHtml = new ArrayList<String>(lines);
	}

	private Rosetta(WikiLanguage syntaxSource, List<String> wiki) {
		final ReaderWiki reader;
		if (syntaxSource == WikiLanguage.DOKUWIKI) {
			reader = new ReaderDokuwiki();
		} else if (syntaxSource == WikiLanguage.CREOLE) {
			reader = new ReaderCreole();
//			} else if (syntaxSource == WikiLanguage.MARKDOWN) {
//			reader = new ReaderMarkdown();
//		} else if (syntaxSource == WikiLanguage.ASCIIDOC) {
//			reader = new ReaderAsciidoc();
		} else {
			throw new UnsupportedOperationException();
		}
		this.unicodeHtml = reader.transform(wiki);
	}

	public List<String> translateTo(WikiLanguage syntaxDestination) {
		final List<String> html = new ArrayList<String>();
		final WriterWiki writer = new WriterWiki(syntaxDestination);
		html.addAll(writer.transform(unicodeHtml));
		return Collections.unmodifiableList(html);
	}

}
