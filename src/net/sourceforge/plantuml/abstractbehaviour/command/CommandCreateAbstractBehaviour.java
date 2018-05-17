package net.sourceforge.plantuml.abstractbehaviour.command;
/** 
 * Author:  Yijun Yu
 */
import net.sourceforge.plantuml.FontParam;
import net.sourceforge.plantuml.Url;
import net.sourceforge.plantuml.UrlBuilder;
import net.sourceforge.plantuml.UrlBuilder.ModeUrl;
import net.sourceforge.plantuml.abstractbehaviour.AbstractBehaviourDiagram;
import net.sourceforge.plantuml.classdiagram.command.GenericRegexProducer;
import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.command.SingleLineCommand2;
import net.sourceforge.plantuml.command.regex.RegexConcat;
import net.sourceforge.plantuml.command.regex.RegexLeaf;
import net.sourceforge.plantuml.command.regex.RegexResult;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.cucadiagram.Stereotype;
import net.sourceforge.plantuml.graphic.USymbol;
import net.sourceforge.plantuml.graphic.color.ColorType;
import net.sourceforge.plantuml.sequencediagram.Participant;
import net.sourceforge.plantuml.sequencediagram.ParticipantType;

public class CommandCreateAbstractBehaviour extends SingleLineCommand2<AbstractBehaviourDiagram> {
	public static final String DISPLAY_WITH_GENERIC = "[%g](.+?)(?:\\<(" + GenericRegexProducer.PATTERN + ")\\>)?[%g]";
	public static final String CODE = "[^%s{}%g<>]+";

	public CommandCreateAbstractBehaviour() {
		super(getRegexConcat());
	}

	private static RegexConcat getRegexConcat() {
		return new RegexConcat(new RegexLeaf("^"), //
				new RegexLeaf("TYPE", // TODO yy 
						"(req|dom)[%s]+"), //
				new RegexLeaf("DISPLAY", DISPLAY_WITH_GENERIC),
				new RegexLeaf("[%s]+as[%s]+"), //
				new RegexLeaf("CODE", "([a-zA-Z0-9]+)"),
				new RegexLeaf("[%s]*"),
				new RegexLeaf("STEREO", "(\\<\\<.+\\>\\>)?"), //
				//	domain: lexical, causal, biddable
				//	requirement: FR, NFR, quality
				new RegexLeaf("[%s]*"), //
				new RegexLeaf("$"));
	}

	@Override
	protected CommandExecutionResult executeArg(AbstractBehaviourDiagram diagram, RegexResult arg) {
		final String type = arg.get("TYPE", 0);
		String display = arg.getLazzy("DISPLAY", 0);
		String code = arg.getLazzy("CODE", 0);
		if (code == null) {
			code = display;
		}
		final String genericOption = arg.getLazzy("DISPLAY", 1);
		final String generic = genericOption != null ? genericOption : arg.get("GENERIC", 0);

		final String stereotype = arg.get("STEREO", 0);
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
//		if (type.equals("req")) // skip requirements
//			return CommandExecutionResult.ok();
		Participant entity = diagram.createNewParticipant(
			type.equals("req")? ParticipantType.REQUIREMENT: ParticipantType.PARTICIPANT, code, d, 0);
		entity.setSpecificColorTOBEREMOVED(ColorType.BACK, diagram.getSkinParam().getIHtmlColorSet().getColorIfValid(arg.get("COLOR", 0)));
		return CommandExecutionResult.ok();
	}

}
