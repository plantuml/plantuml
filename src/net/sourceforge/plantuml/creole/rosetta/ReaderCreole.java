package net.sourceforge.plantuml.creole.rosetta;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ReaderCreole extends ReaderAbstractWiki implements ReaderWiki {

	@Override
	protected String singleLineFormat(String wiki) {

		// Legacy HTML
		wiki = wiki.replace("<b>", WikiLanguage.UNICODE.tag("strong"));
		wiki = wiki.replace("</b>", WikiLanguage.UNICODE.slashTag("strong"));
		wiki = wiki.replace("<i>", WikiLanguage.UNICODE.tag("em"));
		wiki = wiki.replace("</i>", WikiLanguage.UNICODE.slashTag("em"));

		// Em & Strong
		wiki = wiki.replaceAll("\\*\\*(.+?)\\*\\*",
				WikiLanguage.UNICODE.tag("strong") + "$1" + WikiLanguage.UNICODE.slashTag("strong"));
		wiki = wiki.replaceAll("//(.+?)//",
				WikiLanguage.UNICODE.tag("em") + "$1" + WikiLanguage.UNICODE.slashTag("em"));

		// Strike
		wiki = wiki.replaceAll("--([^-]+?)--",
				WikiLanguage.UNICODE.tag("strike") + "$1" + WikiLanguage.UNICODE.slashTag("strike"));

		return wiki;
	}

	public List<String> transform(List<String> raw) {
		final List<String> uhtml = new ArrayList<String>();
		for (int i = 0; i < raw.size(); i++) {
			String current = raw.get(i);
			uhtml.add(singleLineFormat(current));
		}
		return Collections.unmodifiableList(uhtml);
	}

}
