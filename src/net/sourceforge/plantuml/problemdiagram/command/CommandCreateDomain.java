package net.sourceforge.plantuml.problemdiagram.command;
/** 
 * Author:  Yijun Yu
 */
import net.sourceforge.plantuml.FontParam;
import net.sourceforge.plantuml.Url;
import net.sourceforge.plantuml.UrlBuilder;
import net.sourceforge.plantuml.UrlBuilder.ModeUrl;
import net.sourceforge.plantuml.classdiagram.command.GenericRegexProducer;
import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.command.SingleLineCommand2;
import net.sourceforge.plantuml.command.regex.RegexConcat;
import net.sourceforge.plantuml.command.regex.RegexLeaf;
import net.sourceforge.plantuml.command.regex.RegexOr;
import net.sourceforge.plantuml.command.regex.RegexResult;
import net.sourceforge.plantuml.cucadiagram.Code;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.cucadiagram.GroupType;
import net.sourceforge.plantuml.cucadiagram.IEntity;
import net.sourceforge.plantuml.cucadiagram.IGroup;
import net.sourceforge.plantuml.cucadiagram.LeafType;
import net.sourceforge.plantuml.cucadiagram.NamespaceStrategy;
import net.sourceforge.plantuml.cucadiagram.Stereotype;
import net.sourceforge.plantuml.graphic.USymbol;
import net.sourceforge.plantuml.graphic.color.ColorParser;
import net.sourceforge.plantuml.graphic.color.ColorType;
import net.sourceforge.plantuml.problemdiagram.ProblemDiagram;
import net.sourceforge.plantuml.statediagram.command.CommandCreateState;

public class CommandCreateDomain extends SingleLineCommand2<ProblemDiagram> {
	public static final String DISPLAY_WITH_GENERIC = "[%g](.+?)(?:\\<(" + GenericRegexProducer.PATTERN + ")\\>)?[%g]";
	public static final String CODE = "[^%s{}%g<>]+";

	public CommandCreateDomain() {
		super(getRegexConcat());
	}
	
	private static RegexConcat getRegexConcat() {
		return new RegexConcat(new RegexLeaf("^"), //
				new RegexLeaf("TYPE", // TODO yy 
						"(requirement|domain)[%s]+"), //
				new RegexLeaf("DISPLAY", DISPLAY_WITH_GENERIC),
				new RegexLeaf("[%s]+as[%s]+"), //
				new RegexLeaf("CODE", "([a-zA-Z0-9]+)"),
				new RegexLeaf("[%s]*"),
				new RegexLeaf("STEREO", "(\\<\\<.+\\>\\>)?"), //
				//	domain: lexical, causal, biddable
				//	requirement: FR, NFR, quality
				new RegexLeaf("[%s]*"), //
				new RegexLeaf("GROUP", "(\\{)?"), //
				new RegexLeaf("$"));
	}

	@Override
	protected CommandExecutionResult executeArg(ProblemDiagram diagram, RegexResult arg) {
		String type = arg.get("TYPE", 0);
		String display = arg.getLazzy("DISPLAY", 0);
		String code = arg.getLazzy("CODE", 0);
		if (code == null) {
			code = display;
		}
		final String genericOption = arg.getLazzy("DISPLAY", 1);
		final String generic = genericOption != null ? genericOption : arg.get("GENERIC", 0);

		final String stereotype = arg.get("STEREO", 0);

		if (diagram.leafExist(Code.of(code))) {
			return CommandExecutionResult.error("Object already exists : " + code);
		}
		if (stereotype!=null && stereotype.startsWith("<<L")) {
			display = display + "\n<size:8> <&circle-x></size>";
		}
		if (stereotype!=null && stereotype.startsWith("<<B")) {
			display = display + "\n<size:8> <&bold></size>";
		}
		if (stereotype!=null && stereotype.startsWith("<<C")) {
			display = display + "\n<size:12> <&cog></size>";
		}
		Display d = Display.getWithNewlines(display);
		final String urlString = arg.get("URL", 0);
		final String group = arg.get("GROUP", 0);
		IEntity entity;
		if (group!=null) {
			final IGroup currentGroup = diagram.getCurrentGroup();
			diagram.gotoGroup2(Code.of(code), d, type.equalsIgnoreCase("domain") 
					? GroupType.DOMAIN: GroupType.REQUIREMENT, 
					currentGroup, NamespaceStrategy.SINGLE);
			entity = diagram.getCurrentGroup();
		} else {		
			entity = diagram.createLeaf(Code.of(code), d, 
				type.equalsIgnoreCase("domain")
					? LeafType.DOMAIN: LeafType.REQUIREMENT, null);
		}
		if (stereotype != null) {
			entity.setStereotype(new Stereotype(stereotype, diagram.getSkinParam().getCircledCharacterRadius(), diagram
					.getSkinParam().getFont(null, false, FontParam.CIRCLED_CHARACTER), diagram.getSkinParam()
					.getIHtmlColorSet()));
		}
		if (urlString != null) {
			final UrlBuilder urlBuilder = new UrlBuilder(diagram.getSkinParam().getValue("topurl"), ModeUrl.STRICT);
			final Url url = urlBuilder.getUrl(urlString);
			entity.addUrl(url);
		}
		entity.setSpecificColorTOBEREMOVED(ColorType.BACK, diagram.getSkinParam().getIHtmlColorSet().getColorIfValid(arg.get("COLOR", 0)));
		if (type.equalsIgnoreCase("domain")) {
			if (stereotype!=null && stereotype.equalsIgnoreCase("<<Machine>>")) {
				type = "machine";
			}
		}
		USymbol usymbol = USymbol.getFromString(type, 
				diagram.getSkinParam().useUml2ForComponent());
		entity.setUSymbol(usymbol);
		return CommandExecutionResult.ok();
	}

}
