package net.sourceforge.plantuml.creole.rosetta;

import java.util.List;

public interface ReaderWiki {

	public static final String REGEX_HTTP = "https?://[^\\s/$.?#][^()\\[\\]\\s]*";

	public List<String> transform(List<String> data);

}
