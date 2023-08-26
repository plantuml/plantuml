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
package net.sourceforge.plantuml.cheneer.command;

import net.sourceforge.plantuml.abel.Entity;
import net.sourceforge.plantuml.abel.Link;
import net.sourceforge.plantuml.abel.LinkArg;
import net.sourceforge.plantuml.cheneer.ChenEerDiagram;
import net.sourceforge.plantuml.command.CommandExecutionResult;
import net.sourceforge.plantuml.command.SingleLineCommand2;
import net.sourceforge.plantuml.decoration.LinkDecor;
import net.sourceforge.plantuml.decoration.LinkType;
import net.sourceforge.plantuml.klimt.color.NoSuchColorException;
import net.sourceforge.plantuml.klimt.creole.Display;
import net.sourceforge.plantuml.plasma.Quark;
import net.sourceforge.plantuml.regex.IRegex;
import net.sourceforge.plantuml.regex.RegexConcat;
import net.sourceforge.plantuml.regex.RegexLeaf;
import net.sourceforge.plantuml.regex.RegexOptional;
import net.sourceforge.plantuml.regex.RegexResult;
import net.sourceforge.plantuml.utils.LineLocation;

public class CommandAssociateRelationship extends SingleLineCommand2<ChenEerDiagram> {

  public CommandAssociateRelationship() {
    super(getRegexConcat());
  }

  private static IRegex getRegexConcat() {
    return RegexConcat.build(CommandCreateEntity.class.getName(), RegexLeaf.start(), //
        RegexLeaf.spaceZeroOrMore(), //
        new RegexLeaf("PARTICIPATION", "([-=])"), //
        new RegexOptional( //
            new RegexLeaf("CARDINALITY", "(\\w+|\\(\\w+,[%s]*\\w+\\))")), //
        RegexLeaf.spaceOneOrMore(), //
        new RegexLeaf("NAME", "([\\w-]+)"), //
        RegexLeaf.end());
  }

  @Override
  protected CommandExecutionResult executeArg(ChenEerDiagram diagram, LineLocation location, RegexResult arg)
      throws NoSuchColorException {
    final Entity relationship = diagram.peekOwner();
    if (relationship == null) {
      return CommandExecutionResult.error("Can only associate from a relationship");
    }

    final String entityName = diagram.cleanId(arg.get("NAME", 0));

    final Quark<Entity> entityQuark = diagram.quarkInContext(true, entityName);
    final Entity entity = entityQuark.getData();
    if (entity == null) {
      return CommandExecutionResult.error("No such entity: " + entityName);
    }

    final LinkType linkType = new LinkType(LinkDecor.NONE, LinkDecor.NONE);
    final Link link = new Link(diagram.getEntityFactory(), diagram.getCurrentStyleBuilder(), relationship, entity,
        linkType,
        // TODO: Cardinality
        LinkArg.build(Display.NULL, 1));
    diagram.addLink(link);

    return CommandExecutionResult.ok();
  }

}
