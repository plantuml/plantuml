package net.sourceforge.plantuml.creole.rosetta;

import java.util.ArrayList;
import java.util.List;

public class WriterWiki {

	private final WikiLanguage syntaxDestination;

	public WriterWiki(WikiLanguage syntaxDestination) {
		this.syntaxDestination = syntaxDestination;
		if (syntaxDestination != WikiLanguage.HTML_DEBUG && syntaxDestination != WikiLanguage.UNICODE) {
			throw new IllegalArgumentException(syntaxDestination.toString());
		}
	}

	public List<String> transform(List<String> data) {
		if (syntaxDestination == WikiLanguage.UNICODE) {
			return data;
		}
		final List<String> result = new ArrayList<String>();
		for (String s : data) {
			result.add(WikiLanguage.toHtmlDebug(s));
		}
		return result;
	}

}
