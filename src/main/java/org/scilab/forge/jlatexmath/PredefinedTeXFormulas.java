/* PredefinedTeXFormulas.java
 * =========================================================================
 * This file is part of the JLaTeXMath Library - http://forge.scilab.org/jlatexmath
 *
 * Copyright (C) 2011 DENIZET Calixte
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or (at
 * your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * General Public License for more details.
 *
 * A copy of the GNU General Public License can be found in the file
 * LICENSE.txt provided with the source distribution of this program (see
 * the META-INF directory in the source jar). This license can also be
 * found on the GNU website at http://www.gnu.org/licenses/gpl.html.
 *
 * If you did not receive a copy of the GNU General Public License along
 * with this program, contact the lead developer, or write to the Free
 * Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA
 * 02110-1301, USA.
 *
 * Linking this library statically or dynamically with other modules
 * is making a combined work based on this library. Thus, the terms
 * and conditions of the GNU General Public License cover the whole
 * combination.
 *
 * As a special exception, the copyright holders of this library give you
 * permission to link this library with independent modules to produce
 * an executable, regardless of the license terms of these independent
 * modules, and to copy and distribute the resulting executable under terms
 * of your choice, provided that you also meet, for each linked independent
 * module, the terms and conditions of the license of that module.
 * An independent module is a module which is not derived from or based
 * on this library. If you modify this library, you may extend this exception
 * to your version of the library, but you are not obliged to do so.
 * If you do not wish to do so, delete this exception statement from your
 * version.
 *
 */

package org.scilab.forge.jlatexmath;

final class PredefinedTeXFormulas {

    PredefinedTeXFormulas() { }

    static {
        TeXFormula.predefinedTeXFormulasAsString.put("qquad", "\\quad\\quad");
        TeXFormula.predefinedTeXFormulasAsString.put(" ", "\\nbsp");
        TeXFormula.predefinedTeXFormulasAsString.put("ne", "\\not\\equals");
        TeXFormula.predefinedTeXFormulasAsString.put("neq", "\\not\\equals");
        TeXFormula.predefinedTeXFormulasAsString.put("ldots", "\\mathinner{\\ldotp\\ldotp\\ldotp}");
        TeXFormula.predefinedTeXFormulasAsString.put("dotsc", "\\ldots");
        TeXFormula.predefinedTeXFormulasAsString.put("dots", "\\ldots");
        TeXFormula.predefinedTeXFormulasAsString.put("cdots", "\\mathinner{\\cdotp\\cdotp\\cdotp}");
        TeXFormula.predefinedTeXFormulasAsString.put("dotsb", "\\cdots");
        TeXFormula.predefinedTeXFormulasAsString.put("dotso", "\\ldots");
        TeXFormula.predefinedTeXFormulasAsString.put("dotsi", "\\!\\cdots");
        TeXFormula.predefinedTeXFormulasAsString.put("bowtie", "\\mathrel\\triangleright\\joinrel\\mathrel\\triangleleft");
        TeXFormula.predefinedTeXFormulasAsString.put("models", "\\mathrel|\\joinrel\\equals");
        TeXFormula.predefinedTeXFormulasAsString.put("Doteq", "\\doteqdot");
        TeXFormula.predefinedTeXFormulasAsString.put("{", "\\lbrace");
        TeXFormula.predefinedTeXFormulasAsString.put("}", "\\rbrace");
        TeXFormula.predefinedTeXFormulasAsString.put("|", "\\Vert");
        TeXFormula.predefinedTeXFormulasAsString.put("&", "\\textampersand");
        TeXFormula.predefinedTeXFormulasAsString.put("%", "\\textpercent");
        TeXFormula.predefinedTeXFormulasAsString.put("_", "\\underscore");
        TeXFormula.predefinedTeXFormulasAsString.put("$", "\\textdollar");
        TeXFormula.predefinedTeXFormulasAsString.put("@", "\\jlatexmatharobase");
        TeXFormula.predefinedTeXFormulasAsString.put("#", "\\jlatexmathsharp");
        TeXFormula.predefinedTeXFormulasAsString.put("relbar", "\\mathrel{\\smash-}");
        TeXFormula.predefinedTeXFormulasAsString.put("hookrightarrow", "\\lhook\\joinrel\\joinrel\\joinrel\\rightarrow");
        TeXFormula.predefinedTeXFormulasAsString.put("hookleftarrow", "\\leftarrow\\joinrel\\joinrel\\joinrel\\rhook");
        TeXFormula.predefinedTeXFormulasAsString.put("Longrightarrow", "\\Relbar\\joinrel\\Rightarrow");
        TeXFormula.predefinedTeXFormulasAsString.put("longrightarrow", "\\relbar\\joinrel\\rightarrow");
        TeXFormula.predefinedTeXFormulasAsString.put("Longleftarrow", "\\Leftarrow\\joinrel\\Relbar");
        TeXFormula.predefinedTeXFormulasAsString.put("longleftarrow", "\\leftarrow\\joinrel\\relbar");
        TeXFormula.predefinedTeXFormulasAsString.put("Longleftrightarrow", "\\Leftarrow\\joinrel\\Rightarrow");
        TeXFormula.predefinedTeXFormulasAsString.put("longleftrightarrow", "\\leftarrow\\joinrel\\rightarrow");
        TeXFormula.predefinedTeXFormulasAsString.put("iff", "\\;\\Longleftrightarrow\\;");
        TeXFormula.predefinedTeXFormulasAsString.put("implies", "\\;\\Longrightarrow\\;");
        TeXFormula.predefinedTeXFormulasAsString.put("impliedby", "\\;\\Longleftarrow\\;");
        TeXFormula.predefinedTeXFormulasAsString.put("mapsto", "\\mapstochar\\rightarrow");
        TeXFormula.predefinedTeXFormulasAsString.put("longmapsto", "\\mapstochar\\longrightarrow");
        TeXFormula.predefinedTeXFormulasAsString.put("log", "\\mathop{\\mathrm{log}}\\nolimits");
        TeXFormula.predefinedTeXFormulasAsString.put("lg", "\\mathop{\\mathrm{lg}}\\nolimits");
        TeXFormula.predefinedTeXFormulasAsString.put("ln", "\\mathop{\\mathrm{ln}}\\nolimits");
        TeXFormula.predefinedTeXFormulasAsString.put("ln", "\\mathop{\\mathrm{ln}}\\nolimits");
        TeXFormula.predefinedTeXFormulasAsString.put("lim", "\\mathop{\\mathrm{lim}}");
        TeXFormula.predefinedTeXFormulasAsString.put("limsup", "\\mathop{\\mathrm{lim\\,sup}}");
        TeXFormula.predefinedTeXFormulasAsString.put("liminf", "\\mathop{\\mathrm{lim\\,inf}}");
        TeXFormula.predefinedTeXFormulasAsString.put("injlim", "\\mathop{\\mathrm{inj\\,lim}}");
        TeXFormula.predefinedTeXFormulasAsString.put("projlim", "\\mathop{\\mathrm{proj\\,lim}}");
        TeXFormula.predefinedTeXFormulasAsString.put("varinjlim", "\\mathop{\\underrightarrow{\\mathrm{lim}}}");
        TeXFormula.predefinedTeXFormulasAsString.put("varprojlim", "\\mathop{\\underleftarrow{\\mathrm{lim}}}");
        TeXFormula.predefinedTeXFormulasAsString.put("varliminf", "\\mathop{\\underline{\\mathrm{lim}}}");
        TeXFormula.predefinedTeXFormulasAsString.put("varlimsup", "\\mathop{\\overline{\\mathrm{lim}}}");
        TeXFormula.predefinedTeXFormulasAsString.put("sin", "\\mathop{\\mathrm{sin}}\\nolimits");
        TeXFormula.predefinedTeXFormulasAsString.put("arcsin", "\\mathop{\\mathrm{arcsin}}\\nolimits");
        TeXFormula.predefinedTeXFormulasAsString.put("sinh", "\\mathop{\\mathrm{sinh}}\\nolimits");
        TeXFormula.predefinedTeXFormulasAsString.put("cos", "\\mathop{\\mathrm{cos}}\\nolimits");
        TeXFormula.predefinedTeXFormulasAsString.put("arccos", "\\mathop{\\mathrm{arccos}}\\nolimits");
        TeXFormula.predefinedTeXFormulasAsString.put("cot", "\\mathop{\\mathrm{cot}}\\nolimits");
        TeXFormula.predefinedTeXFormulasAsString.put("arccot", "\\mathop{\\mathrm{arccot}}\\nolimits");
        TeXFormula.predefinedTeXFormulasAsString.put("cosh", "\\mathop{\\mathrm{cosh}}\\nolimits");
        TeXFormula.predefinedTeXFormulasAsString.put("tan", "\\mathop{\\mathrm{tan}}\\nolimits");
        TeXFormula.predefinedTeXFormulasAsString.put("arctan", "\\mathop{\\mathrm{arctan}}\\nolimits");
        TeXFormula.predefinedTeXFormulasAsString.put("tanh", "\\mathop{\\mathrm{tanh}}\\nolimits");
        TeXFormula.predefinedTeXFormulasAsString.put("coth", "\\mathop{\\mathrm{coth}}\\nolimits");
        TeXFormula.predefinedTeXFormulasAsString.put("sec", "\\mathop{\\mathrm{sec}}\\nolimits");
        TeXFormula.predefinedTeXFormulasAsString.put("arcsec", "\\mathop{\\mathrm{arcsec}}\\nolimits");
        TeXFormula.predefinedTeXFormulasAsString.put("arccsc", "\\mathop{\\mathrm{arccsc}}\\nolimits");
        TeXFormula.predefinedTeXFormulasAsString.put("sech", "\\mathop{\\mathrm{sech}}\\nolimits");
        TeXFormula.predefinedTeXFormulasAsString.put("csc", "\\mathop{\\mathrm{csc}}\\nolimits");
        TeXFormula.predefinedTeXFormulasAsString.put("csch", "\\mathop{\\mathrm{csch}}\\nolimits");
        TeXFormula.predefinedTeXFormulasAsString.put("max", "\\mathop{\\mathrm{max}}");
        TeXFormula.predefinedTeXFormulasAsString.put("min", "\\mathop{\\mathrm{min}}");
        TeXFormula.predefinedTeXFormulasAsString.put("sup", "\\mathop{\\mathrm{sup}}");
        TeXFormula.predefinedTeXFormulasAsString.put("inf", "\\mathop{\\mathrm{inf}}");
        TeXFormula.predefinedTeXFormulasAsString.put("arg", "\\mathop{\\mathrm{arg}}\\nolimits");
        TeXFormula.predefinedTeXFormulasAsString.put("ker", "\\mathop{\\mathrm{ker}}\\nolimits");
        TeXFormula.predefinedTeXFormulasAsString.put("dim", "\\mathop{\\mathrm{dim}}\\nolimits");
        TeXFormula.predefinedTeXFormulasAsString.put("hom", "\\mathop{\\mathrm{hom}}\\nolimits");
        TeXFormula.predefinedTeXFormulasAsString.put("det", "\\mathop{\\mathrm{det}}");
        TeXFormula.predefinedTeXFormulasAsString.put("exp", "\\mathop{\\mathrm{exp}}\\nolimits");
        TeXFormula.predefinedTeXFormulasAsString.put("Pr", "\\mathop{\\mathrm{Pr}}");
        TeXFormula.predefinedTeXFormulasAsString.put("gcd", "\\mathop{\\mathrm{gcd}}");
        TeXFormula.predefinedTeXFormulasAsString.put("deg", "\\mathop{\\mathrm{deg}}\\nolimits");
        TeXFormula.predefinedTeXFormulasAsString.put("bmod", "\\:\\mathbin{\\mathrm{mod}}\\:");
        TeXFormula.predefinedTeXFormulasAsString.put("JLaTeXMath", "\\mathbb{J}\\LaTeX Math");
        TeXFormula.predefinedTeXFormulasAsString.put("Mapsto", "\\Mapstochar\\Rightarrow");
        TeXFormula.predefinedTeXFormulasAsString.put("mapsfrom", "\\leftarrow\\mapsfromchar");
        TeXFormula.predefinedTeXFormulasAsString.put("Mapsfrom", "\\Leftarrow\\Mapsfromchar");
        TeXFormula.predefinedTeXFormulasAsString.put("Longmapsto", "\\Mapstochar\\Longrightarrow");
        TeXFormula.predefinedTeXFormulasAsString.put("longmapsfrom", "\\longleftarrow\\mapsfromchar");
        TeXFormula.predefinedTeXFormulasAsString.put("Longmapsfrom", "\\Longleftarrow\\Mapsfromchar");
        TeXFormula.predefinedTeXFormulasAsString.put("arrowvert", "\\vert");
        TeXFormula.predefinedTeXFormulasAsString.put("Arrowvert", "\\Vert");
        TeXFormula.predefinedTeXFormulasAsString.put("aa", "\\mathring{a}");
        TeXFormula.predefinedTeXFormulasAsString.put("AA", "\\mathring{A}");
        TeXFormula.predefinedTeXFormulasAsString.put("ddag", "\\ddagger");
        TeXFormula.predefinedTeXFormulasAsString.put("dag", "\\dagger");
        TeXFormula.predefinedTeXFormulasAsString.put("Doteq", "\\doteqdot");
        TeXFormula.predefinedTeXFormulasAsString.put("doublecup", "\\Cup");
        TeXFormula.predefinedTeXFormulasAsString.put("doublecap", "\\Cap");
        TeXFormula.predefinedTeXFormulasAsString.put("llless", "\\lll");
        TeXFormula.predefinedTeXFormulasAsString.put("gggtr", "\\ggg");
        TeXFormula.predefinedTeXFormulasAsString.put("Alpha", "\\mathord{\\mathrm{A}}");
        TeXFormula.predefinedTeXFormulasAsString.put("Beta", "\\mathord{\\mathrm{B}}");
        TeXFormula.predefinedTeXFormulasAsString.put("Epsilon", "\\mathord{\\mathrm{E}}");
        TeXFormula.predefinedTeXFormulasAsString.put("Zeta", "\\mathord{\\mathrm{Z}}");
        TeXFormula.predefinedTeXFormulasAsString.put("Eta", "\\mathord{\\mathrm{H}}");
        TeXFormula.predefinedTeXFormulasAsString.put("Iota", "\\mathord{\\mathrm{I}}");
        TeXFormula.predefinedTeXFormulasAsString.put("Kappa", "\\mathord{\\mathrm{K}}");
        TeXFormula.predefinedTeXFormulasAsString.put("Mu", "\\mathord{\\mathrm{M}}");
        TeXFormula.predefinedTeXFormulasAsString.put("Nu", "\\mathord{\\mathrm{N}}");
        TeXFormula.predefinedTeXFormulasAsString.put("Omicron", "\\mathord{\\mathrm{O}}");
        TeXFormula.predefinedTeXFormulasAsString.put("Rho", "\\mathord{\\mathrm{P}}");
        TeXFormula.predefinedTeXFormulasAsString.put("Tau", "\\mathord{\\mathrm{T}}");
        TeXFormula.predefinedTeXFormulasAsString.put("Chi", "\\mathord{\\mathrm{X}}");
        TeXFormula.predefinedTeXFormulasAsString.put("hdots", "\\ldots");
        TeXFormula.predefinedTeXFormulasAsString.put("restriction", "\\upharpoonright");
        TeXFormula.predefinedTeXFormulasAsString.put("celsius", "\\mathord{{}^\\circ\\mathrm{C}}");
        TeXFormula.predefinedTeXFormulasAsString.put("micro", "\\textmu");
        TeXFormula.predefinedTeXFormulasAsString.put("marker", "\\kern{0.25ex}\\rule{0.5ex}{1.2ex}\\kern{0.25ex}");
        TeXFormula.predefinedTeXFormulasAsString.put("hybull", "\\rule[0.6ex]{1ex}{0.2ex}");
        TeXFormula.predefinedTeXFormulasAsString.put("block", "\\rule{1ex}{1.2ex}");
        TeXFormula.predefinedTeXFormulasAsString.put("uhblk", "\\rule[0.6ex]{1ex}{0.6ex}");
        TeXFormula.predefinedTeXFormulasAsString.put("lhblk", "\\rule{1ex}{0.6ex}");
        TeXFormula.predefinedTeXFormulasAsString.put("notin", "\\not\\in");
        TeXFormula.predefinedTeXFormulasAsString.put("rVert", "\\Vert");
        TeXFormula.predefinedTeXFormulasAsString.put("lVert", "\\Vert");
    }
}
