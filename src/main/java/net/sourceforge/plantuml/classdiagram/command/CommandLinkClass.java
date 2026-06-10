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
 * Contribution :  Hisashi Miyashita
 *
 *
 */
package net.sourceforge.plantuml.classdiagram.command;

import net.sourceforge.plantuml.StringUtils;
import net.sourceforge.plantuml.abel.Entity;
import net.sourceforge.plantuml.abel.LeafType;
import net.sourceforge.plantuml.abel.Link;
import net.sourceforge.plantuml.abel.LinkArg;
import net.sourceforge.plantuml.annotation.Explain;
import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.command.ParserPass;
import net.sourceforge.plantuml.command.SingleLineCommand2;
import net.sourceforge.plantuml.core.DiagramType;
import net.sourceforge.plantuml.decoration.LinkDecor;
import net.sourceforge.plantuml.decoration.LinkType;
import net.sourceforge.plantuml.descdiagram.command.CommandLinkElement;
import net.sourceforge.plantuml.descdiagram.command.Labels;
import net.sourceforge.plantuml.gantt.Failable;
import net.sourceforge.plantuml.klimt.color.ColorParser;
import net.sourceforge.plantuml.klimt.color.ColorType;
import net.sourceforge.plantuml.klimt.color.NoSuchColorException;
import net.sourceforge.plantuml.klimt.creole.Display;
import net.sourceforge.plantuml.objectdiagram.AbstractClassOrObjectDiagram;
import net.sourceforge.plantuml.plasma.Quark;
import net.sourceforge.plantuml.regex.RegexConcat;
import net.sourceforge.plantuml.regex.RegexLeaf;
import net.sourceforge.plantuml.regex.RegexOptional;
import net.sourceforge.plantuml.regex.RegexOr;
import net.sourceforge.plantuml.regex.RegexResult;
import net.sourceforge.plantuml.stereo.Stereotype;
import net.sourceforge.plantuml.stereo.StereotypePattern;
import net.sourceforge.plantuml.url.Url;
import net.sourceforge.plantuml.url.UrlBuilder;
import net.sourceforge.plantuml.url.UrlMode;
import net.sourceforge.plantuml.utils.Direction;
import net.sourceforge.plantuml.utils.LineLocation;

final public class CommandLinkClass extends SingleLineCommand2<AbstractClassOrObjectDiagram> {

	// Issue #2685: a separator is any single character that is not part of an
	// identifier (letter, digit, _, $) and not a PlantUML syntactic
	// character (space, #, :, {},<>,").

	// This lets 'set separator' accept !, -, ~, @, |, /, etc. anything an author
	// might want while keeping the parsing of class names and relations
	// unambiguous.

	// Note:
	// - '#' is excluded because it introduces colors (#red, #FF0000).
	// - ':' is excluded because it introduces relation labels (A --> B : label) and
	// is already handled as a separator only in its doubled form '::' below.
	private static final String SEPARATOR_CHAR_SINGLE = "[^%pLN%s_$#\\:{}<>%g]";

	// Two-character separators must be listed explicitly
	// Only '::' (legacy UML namespace) and '\\'
	private static final String SEPARATOR_CHAR_DOUBLE = "[\\\\]{2}|::";

	public static String getSeparator() {
		return "(?:" + SEPARATOR_CHAR_SINGLE + "|" + SEPARATOR_CHAR_DOUBLE + ")";
	}

	private static final String SINGLE = getSeparator() + "?[%pLN_]+(?:" + getSeparator() + "[%pLN_]+)*";
	private static final String SINGLE_GUILLEMENT = "[%g]" + getSeparator() + "?[%pLN_]+(?:" + getSeparator()
			+ "[%pLN_]+)*[%g]";
	private static final String SINGLE2 = "(?:" + SINGLE + "|" + SINGLE_GUILLEMENT + ")";
	private static final String COUPLE = "\\([%s]*(" + SINGLE2 + ")[%s]*,[%s]*(" + SINGLE2 + ")[%s]*\\)";

	public CommandLinkClass(DiagramType diagramType) {
		super(getRegexConcat(diagramType));
	}

	static private RegexConcat getRegexConcat(DiagramType diagramType) {
		return RegexConcat.build(CommandLinkClass.class.getName() + diagramType, RegexLeaf.start(), //
				new RegexOptional( //
						new RegexConcat( //
								new RegexLeaf(1, "HEADER", "@([\\d.]+)"), //
								RegexLeaf.spaceOneOrMore() //
						)), new RegexOr( //
								new RegexLeaf(1, "ENT1", getClassIdentifier()), //
								new RegexLeaf(2, "COUPLE1", COUPLE)), //

				RegexLeaf.spaceZeroOrMore(), //

				new RegexOptional(new RegexConcat( //
						RegexLeaf.spaceOneOrMore(), //
						new RegexLeaf("[\\[]"), //
						new RegexLeaf(1, "QUALIFIER1", "([^\\[\\]]+)"), //
						new RegexLeaf("[\\]]"), //
						RegexLeaf.spaceOneOrMore() //
				)), //
				new RegexOptional(new RegexLeaf(1, "FIRST_LABEL", "[%g]([^%g]+)[%g]")), //
				new RegexOptional(new RegexLeaf(1, "FIRST_ROLE", "/([^%s]+|[%g][^%g]+[%g])")), //

				RegexLeaf.spaceZeroOrMore(), //

				new RegexConcat(//
						new RegexLeaf(1, "ARROW_HEAD1", LinkDecor.getRegexDecors1()), //
						new RegexLeaf(1, "ARROW_BODY1", "([-=.]+)"), //
						new RegexLeaf(1, "ARROW_STYLE1", "(?:\\[(" + CommandLinkElement.LINE_STYLE + ")\\])?"), //
						new RegexLeaf(1, "ARROW_DIRECTION", "(left|right|up|down|le?|ri?|up?|do?)?"), //
						new RegexOptional(new RegexLeaf(1, "INSIDE", "(0|\\(0\\)|\\(0|0\\))(?=[-=.~])")), //
						new RegexLeaf(1, "ARROW_STYLE2", "(?:\\[(" + CommandLinkElement.LINE_STYLE + ")\\])?"), //
						new RegexLeaf(1, "ARROW_BODY2", "([-=.]*)"), //
						new RegexLeaf(1, "ARROW_HEAD2", LinkDecor.getRegexDecors2())), //

				RegexLeaf.spaceZeroOrMore(), //

				new RegexOptional(new RegexLeaf(1, "SECOND_LABEL", "[%g]([^%g]+)[%g]")), //
				new RegexOptional(new RegexLeaf(1, "SECOND_ROLE", "/([^%s]+|[%g][^%g]+[%g])")), //
				new RegexOptional(new RegexConcat( //
						RegexLeaf.spaceOneOrMore(), //
						new RegexLeaf("[\\[]"), //
						new RegexLeaf(1, "QUALIFIER2", "([^\\[\\]]+)"), //
						new RegexLeaf("[\\]]"), //
						RegexLeaf.spaceOneOrMore() //
				)), //

				RegexLeaf.spaceZeroOrMore(), //

				new RegexOr( //
						new RegexLeaf(1, "ENT2", getClassIdentifier()), //
						new RegexLeaf(2, "COUPLE2", COUPLE)), //
				RegexLeaf.spaceZeroOrMore(), //
				color().getRegex(), //
				RegexLeaf.spaceZeroOrMore(), //
				UrlBuilder.OPTIONAL, //
				RegexLeaf.spaceZeroOrMore(), //
				StereotypePattern.optional("STEREOTYPE"), //
				new RegexOptional( //
						new RegexConcat( //
								new RegexLeaf(":"), //
								RegexLeaf.spaceZeroOrMore(), //
								new RegexLeaf(1, "LABEL_LINK", "(.+)") //
						)), RegexLeaf.end());
	}

	private static ColorParser color() {
		return ColorParser.simpleColor(ColorType.LINE);
	}

	public static String getClassIdentifier() {
		return "(" + getSeparator() + "?[%pLN_$]+(?:" + getSeparator() + "[%pLN_$]+)*|[%g][^%g]+[%g])";
	}

	@Override
	@Explain
	protected String explainArg(LineLocation location, RegexResult arg) {
		final StringBuilder sb = new StringBuilder();

		// The relation kind is deduced from the arrow decorations, like in
		// getLinkType(); an endpoint may be a plain entity or a '(A, B)'
		// couple targeting an existing association (association class syntax,
		// see the executeArgSpecial methods).
		sb.append("Drawing ").append(describeRelation(getArrowHead1(arg), getArrowHead2(arg))).append(" between ")
				.append(describeEndpoint(arg, "ENT1", "COUPLE1")).append(" and ")
				.append(describeEndpoint(arg, "ENT2", "COUPLE2"));

		if (notNull(arg.get("ARROW_BODY1", 0)).contains(".") || notNull(arg.get("ARROW_BODY2", 0)).contains("."))
			sb.append(", dashed");

		final String direction = arg.get("ARROW_DIRECTION", 0);
		if (direction != null && direction.isEmpty() == false)
			sb.append(", oriented ").append(describeDirection(direction));

		if (arg.get("INSIDE", 0) != null)
			sb.append(", with a middle circle");

		final String style = arg.getLazzy("ARROW_STYLE", 0);
		if (style != null)
			sb.append(", with style '").append(style).append("'");

		final String qualifier1 = arg.get("QUALIFIER1", 0);
		if (qualifier1 != null)
			sb.append(", qualifier \"").append(qualifier1).append("\" on the first end");

		final String qualifier2 = arg.get("QUALIFIER2", 0);
		if (qualifier2 != null)
			sb.append(", qualifier \"").append(qualifier2).append("\" on the second end");

		final String firstLabel = arg.get("FIRST_LABEL", 0);
		if (firstLabel != null)
			sb.append(", cardinality \"").append(firstLabel).append("\" on the first end");

		final String secondLabel = arg.get("SECOND_LABEL", 0);
		if (secondLabel != null)
			sb.append(", cardinality \"").append(secondLabel).append("\" on the second end");

		final String firstRole = arg.get("FIRST_ROLE", 0);
		if (firstRole != null)
			sb.append(", role \"").append(StringUtils.eventuallyRemoveStartingAndEndingDoubleQuote(firstRole))
					.append("\" on the first end");

		final String secondRole = arg.get("SECOND_ROLE", 0);
		if (secondRole != null)
			sb.append(", role \"").append(StringUtils.eventuallyRemoveStartingAndEndingDoubleQuote(secondRole))
					.append("\" on the second end");

		final String label = arg.get("LABEL_LINK", 0);
		if (label != null)
			sb.append(", labelled \"").append(label).append("\"");

		if (arg.get("STEREOTYPE", 0) != null)
			sb.append(", stereotype ").append(arg.get("STEREOTYPE", 0));

		if (arg.get("COLOR", 0) != null)
			sb.append(", line color ").append(arg.get("COLOR", 0));

		if (arg.get(UrlBuilder.URL_KEY, 0) != null)
			sb.append(", with a URL link");

		final String weight = arg.get("HEADER", 0);
		if (weight != null)
			sb.append(", weight ").append(weight);

		return sb.toString();
	}

	private String describeRelation(String head1, String head2) {
		// Keep the most significant decoration: '<|' wins over '<', and so on.
		final String heads = head1 + head2;
		if (heads.contains("|"))
			return "an extension (inheritance) link";
		if (heads.contains("*"))
			return "a composition link";
		if (heads.contains("o"))
			return "an aggregation link";
		if (heads.contains("<") || heads.contains(">"))
			return "a directed association";
		if (heads.isEmpty())
			return "an association";
		return "a link";
	}

	private String describeEndpoint(RegexResult arg, String entKey, String coupleKey) {
		final String ent = arg.get(entKey, 0);
		if (ent != null)
			return "'" + StringUtils.eventuallyRemoveStartingAndEndingDoubleQuote(ent) + "'";

		return "the association between '"
				+ StringUtils.eventuallyRemoveStartingAndEndingDoubleQuote(arg.get(coupleKey, 0)) + "' and '"
				+ StringUtils.eventuallyRemoveStartingAndEndingDoubleQuote(arg.get(coupleKey, 1)) + "'";
	}

	private String describeDirection(String direction) {
		switch (StringUtils.goLowerCase(direction).charAt(0)) {
		case 'l':
			return "to the left";
		case 'r':
			return "to the right";
		case 'u':
			return "upwards";
		default:
			return "downwards";
		}
	}

	@Override
	protected CommandExecutionResult executeArg(AbstractClassOrObjectDiagram diagram, LineLocation location,
			RegexResult arg, ParserPass currentPass) throws NoSuchColorException {
		String ent1String = diagram.cleanId(arg.get("ENT1", 0));
		String ent2String = diagram.cleanId(arg.get("ENT2", 0));
		if (ent1String == null && ent2String == null)
			return executeArgSpecial3(location, diagram, arg);

		if (ent1String == null)
			return executeArgSpecial1(location, diagram, arg);

		if (ent2String == null)
			return executeArgSpecial2(location, diagram, arg);

		String port1 = null;
		String port2 = null;
		final LinkType linkType = getLinkType(arg);
		if (ent1String.contains("::") && diagram.firstWithName(ent1String) == null) {
			port1 = diagram.getPortId(ent1String);
			ent1String = diagram.removePortId(ent1String);
		}

		if (ent2String.contains("::") && diagram.firstWithName(ent2String) == null) {
			port2 = diagram.getPortId(ent2String);
			ent2String = diagram.removePortId(ent2String);
		}

		final Failable<Quark<Entity>> quark1 = diagram.quarkInContextSafe(true, ent1String);
		if (quark1.isFail())
			return CommandExecutionResult.error(quark1.getError());
		final Failable<Quark<Entity>> quark2 = diagram.quarkInContextSafe(true, ent2String);
		if (quark2.isFail())
			return CommandExecutionResult.error(quark2.getError());

		Entity cl1 = quark1.get().getData();
		if (cl1 == null)
			cl1 = diagram.reallyCreateLeaf(location, quark1.get(),
					Display.getWithNewlines(diagram.getPragma(), quark1.get().getName()), LeafType.CLASS, null);
		Entity cl2 = quark2.get().getData();
		if (cl2 == null)
			cl2 = diagram.reallyCreateLeaf(location, quark2.get(),
					Display.getWithNewlines(diagram.getPragma(), quark2.get().getName()), LeafType.CLASS, null);

		final Direction dir = getDirection(arg);
		final int queue;
		if (dir == Direction.LEFT || dir == Direction.RIGHT)
			queue = 1;
		else
			queue = getQueueLength(arg);

		final Labels labels = new Labels(arg);

		final String kal1 = arg.get("QUALIFIER1", 0);
		final String kal2 = arg.get("QUALIFIER2", 0);

		final LinkArg linkArg = LinkArg
				.build(labels.getDisplay(diagram.getPragma()), queue,
						diagram.getSkinParam().classAttributeIconSize() > 0)
				.withQuantifier(labels.getFirstLabel(), labels.getSecondLabel())
				.withRole(labels.getFirstRole(), labels.getSecondRole())
				.withDistanceAngle(diagram.getLabeldistance(), diagram.getLabelangle()).withKal(kal1, kal2);

		Link link = new Link(location, diagram, diagram.getSkinParam().getCurrentStyleBuilder(), cl1, cl2, linkType,
				linkArg);
		if (arg.get(UrlBuilder.URL_KEY, 0) != null) {
			final UrlBuilder urlBuilder = new UrlBuilder(diagram.getSkinParam().getValue("topurl"), UrlMode.STRICT);
			final Url url = urlBuilder.getUrl(arg.get(UrlBuilder.URL_KEY, 0));
			link.setUrl(url);
		}
		link.setPortMembers(port1, port2);

		if (dir == Direction.LEFT || dir == Direction.UP)
			link = link.getInv();

		link.setLinkArrow(labels.getLinkArrow());
		link.setColors(color().getColor(arg, diagram.getSkinParam().getIHtmlColorSet()));
		link.applyStyle(arg.getLazzy("ARROW_STYLE", 0));
		if (arg.get("STEREOTYPE", 0) != null) {
			final Stereotype stereotype = Stereotype.build(arg.get("STEREOTYPE", 0));
			link.setStereotype(stereotype);
		}
		link.setCodeLine(location);

		addLink(diagram, link, arg.get("HEADER", 0));

		return CommandExecutionResult.ok();
	}

	private void addLink(AbstractClassOrObjectDiagram diagram, Link link, String weight) {
		diagram.addLink(link);
		if (weight != null)
			link.setWeight(Double.parseDouble(weight));

	}

	private CommandExecutionResult executeArgSpecial1(LineLocation location, AbstractClassOrObjectDiagram diagram,
			RegexResult arg) {
		final String name1A = StringUtils.eventuallyRemoveStartingAndEndingDoubleQuote(arg.get("COUPLE1", 0));
		final String name1B = StringUtils.eventuallyRemoveStartingAndEndingDoubleQuote(arg.get("COUPLE1", 1));

		final Quark<Entity> quark1A = diagram.quarkInContext(true, name1A);
		final Quark<Entity> quark1B = diagram.quarkInContext(true, name1B);

		if (quark1A.getData() != null == false)
			return CommandExecutionResult.error("No class " + name1A);

		if (quark1B.getData() != null == false)
			return CommandExecutionResult.error("No class " + name1B);

		final Entity cl1A = quark1A.getData();
		final Entity cl1B = quark1B.getData();

		final String id2 = StringUtils.eventuallyRemoveStartingAndEndingDoubleQuote(arg.get("ENT2", 0), "\"");
		final Quark<Entity> ent2 = diagram.quarkInContext(true, id2);

		Entity cl2 = ent2.getData();
		if (cl2 == null)
			cl2 = diagram.reallyCreateLeaf(location, ent2, Display.getWithNewlines(diagram.getPragma(), ent2.getName()),
					LeafType.CLASS, null);

		final LinkType linkType = getLinkType(arg);
		final Display label = Display.getWithNewlines(diagram.getPragma(), arg.get("LABEL_LINK", 0));

		final boolean result = diagram.associationClass(location, 1, cl1A, cl1B, cl2, linkType, label);
		if (result == false)
			return CommandExecutionResult.error("Cannot have more than 2 assocications");

		return CommandExecutionResult.ok();
	}

	private CommandExecutionResult executeArgSpecial2(LineLocation location, AbstractClassOrObjectDiagram diagram,
			RegexResult arg) {
		final String name2A = StringUtils.eventuallyRemoveStartingAndEndingDoubleQuote(arg.get("COUPLE2", 0));
		final String name2B = StringUtils.eventuallyRemoveStartingAndEndingDoubleQuote(arg.get("COUPLE2", 1));

		final Quark<Entity> quark2A = diagram.quarkInContext(true, name2A);
		final Quark<Entity> quark2B = diagram.quarkInContext(true, name2B);

		if (quark2A.getData() != null == false)
			return CommandExecutionResult.error("No class " + name2A);

		if (quark2B.getData() != null == false)
			return CommandExecutionResult.error("No class " + name2B);

		final Entity cl2A = quark2A.getData();
		final Entity cl2B = quark2B.getData();

		final String id1 = StringUtils.eventuallyRemoveStartingAndEndingDoubleQuote(arg.get("ENT1", 0), "\"");
		final Quark<Entity> ent1 = diagram.quarkInContext(true, id1);

		Entity cl1 = (Entity) ent1.getData();
		if (cl1 == null)
			cl1 = diagram.reallyCreateLeaf(location, ent1, Display.getWithNewlines(diagram.getPragma(), ent1.getName()),
					LeafType.CLASS, null);

		final LinkType linkType = getLinkType(arg);
		final Display label = Display.getWithNewlines(diagram.getPragma(), arg.get("LABEL_LINK", 0));

		final boolean result = diagram.associationClass(location, 2, cl2A, cl2B, cl1, linkType, label);
		if (result == false)
			return CommandExecutionResult.error("Cannot have more than 2 assocications");

		return CommandExecutionResult.ok();
	}

	private CommandExecutionResult executeArgSpecial3(LineLocation location, AbstractClassOrObjectDiagram diagram,
			RegexResult arg) {

		final String name1A = StringUtils.eventuallyRemoveStartingAndEndingDoubleQuote(arg.get("COUPLE1", 0));
		final String name1B = StringUtils.eventuallyRemoveStartingAndEndingDoubleQuote(arg.get("COUPLE1", 1));
		final String name2A = StringUtils.eventuallyRemoveStartingAndEndingDoubleQuote(arg.get("COUPLE2", 0));
		final String name2B = StringUtils.eventuallyRemoveStartingAndEndingDoubleQuote(arg.get("COUPLE2", 1));

		final Quark<Entity> quark1A = diagram.quarkInContext(true, name1A);
		final Quark<Entity> quark1B = diagram.quarkInContext(true, name1B);
		final Quark<Entity> quark2A = diagram.quarkInContext(true, name2A);
		final Quark<Entity> quark2B = diagram.quarkInContext(true, name2B);

		if (quark1A.getData() != null == false)
			return CommandExecutionResult.error("No class " + name1A);
		if (quark1B.getData() != null == false)
			return CommandExecutionResult.error("No class " + name1B);
		if (quark2A.getData() != null == false)
			return CommandExecutionResult.error("No class " + name2A);
		if (quark2B.getData() != null == false)
			return CommandExecutionResult.error("No class " + name2B);

		final Entity cl1A = (Entity) quark1A.getData();
		final Entity cl1B = (Entity) quark1B.getData();
		final Entity cl2A = (Entity) quark2A.getData();
		final Entity cl2B = (Entity) quark2B.getData();

		final LinkType linkType = getLinkType(arg);
		final Display label = Display.getWithNewlines(diagram.getPragma(), arg.get("LABEL_LINK", 0));

		return diagram.associationClass(location, cl1A, cl1B, cl2A, cl2B, linkType, label);
	}

	private LinkType getLinkType(RegexResult arg) {
		final LinkDecor decors1 = LinkDecor.lookupDecors1(getArrowHead1(arg));
		final LinkDecor decors2 = LinkDecor.lookupDecors2(getArrowHead2(arg));

		LinkType result = new LinkType(decors2, decors1);
		if (arg.get("ARROW_BODY1", 0).contains(".") || arg.get("ARROW_BODY2", 0).contains("."))
			result = result.goDashed();

		final String middle = arg.get("INSIDE", 0);
		if ("0".equals(middle))
			result = result.withMiddleCircle();
		else if ("0)".equals(middle))
			result = result.withMiddleCircleCircled1();
		else if ("(0".equals(middle))
			result = result.withMiddleCircleCircled2();
		else if ("(0)".equals(middle))
			result = result.withMiddleCircleCircled();

		return result;
	}

	private int getQueueLength(RegexResult arg) {
		String s = getFullArrow(arg);
		s = s.replaceAll("[^-.=]", "");
		return s.length();
	}

	private Direction getDirection(RegexResult arg) {
		String s = getFullArrow(arg);
		s = s.replaceAll("[^-.=\\w]", "");
		if (s.startsWith("o"))
			s = s.substring(1);

		if (s.endsWith("o"))
			s = s.substring(0, s.length() - 1);

		return StringUtils.getQueueDirection(s);
	}

	private String getArrowHead1(RegexResult arg) {
		return getArrowHead(arg, "ARROW_HEAD1");
	}

	private String getArrowHead2(RegexResult arg) {
		return getArrowHead(arg, "ARROW_HEAD2");
	}

	private String getArrowHead(RegexResult arg, final String key) {
		return notNull(arg.get(key, 0)).replaceAll("_", "");
	}

	private String getFullArrow(RegexResult arg) {
		return getArrowHead1(arg) + notNull(arg.get("ARROW_BODY1", 0)) + notNull(arg.get("ARROW_DIRECTION", 0))
				+ notNull(arg.get("ARROW_BODY2", 0)) + getArrowHead2(arg);
	}

	public static String notNull(String s) {
		if (s == null)
			return "";
		return s;
	}

}
