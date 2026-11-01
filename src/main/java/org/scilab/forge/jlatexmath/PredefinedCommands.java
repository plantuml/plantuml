/* PredefinedCommands.java
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

final class PredefinedCommands {

    PredefinedCommands() { }

    static {
        MacroInfo.Commands.put("newcommand", new PredefMacroInfo(0, 2, 2));
        MacroInfo.Commands.put("renewcommand", new PredefMacroInfo(1, 2, 2));
        MacroInfo.Commands.put("rule", new PredefMacroInfo(2, 2, 1));
        MacroInfo.Commands.put("hspace", new PredefMacroInfo(3, 1));
        MacroInfo.Commands.put("vspace", new PredefMacroInfo(4, 1));
        MacroInfo.Commands.put("llap", new PredefMacroInfo(5, 1));
        MacroInfo.Commands.put("rlap", new PredefMacroInfo(6, 1));
        MacroInfo.Commands.put("clap", new PredefMacroInfo(7, 1));
        MacroInfo.Commands.put("mathllap", new PredefMacroInfo(8, 1));
        MacroInfo.Commands.put("mathrlap", new PredefMacroInfo(9, 1));
        MacroInfo.Commands.put("mathclap", new PredefMacroInfo(10, 1));
        MacroInfo.Commands.put("includegraphics", new PredefMacroInfo(11, 1, 1));
        MacroInfo.Commands.put("cfrac", new PredefMacroInfo(12, 2, 1));
        MacroInfo.Commands.put("frac", new PredefMacroInfo(13, 2));
        MacroInfo.Commands.put("sfrac", new PredefMacroInfo(14, 2));
        MacroInfo.Commands.put("genfrac", new PredefMacroInfo(15, 6));
        MacroInfo.Commands.put("over", new PredefMacroInfo(16, 0));
        MacroInfo.Commands.put("overwithdelims", new PredefMacroInfo(17, 2));
        MacroInfo.Commands.put("atop", new PredefMacroInfo(18, 0));
        MacroInfo.Commands.put("atopwithdelims", new PredefMacroInfo(19, 2));
        MacroInfo.Commands.put("choose", new PredefMacroInfo(20, 0));
        MacroInfo.Commands.put("underscore", new PredefMacroInfo(21, 0));
        MacroInfo.Commands.put("mbox", new PredefMacroInfo(22, 1));
        MacroInfo.Commands.put("text", new PredefMacroInfo(23, 1));
        MacroInfo.Commands.put("intertext", new PredefMacroInfo(24, 1));
        MacroInfo.Commands.put("binom", new PredefMacroInfo(25, 2));
        MacroInfo.Commands.put("mathbf", new PredefMacroInfo(26, 1));
        MacroInfo.Commands.put("bf", new PredefMacroInfo(27, 0));
        MacroInfo.Commands.put("mathbb", new PredefMacroInfo(28, 1));
        MacroInfo.Commands.put("mathcal", new PredefMacroInfo(29, 1));
        MacroInfo.Commands.put("cal", new PredefMacroInfo(30, 1));
        MacroInfo.Commands.put("mathit", new PredefMacroInfo(31, 1));
        MacroInfo.Commands.put("it", new PredefMacroInfo(32, 0));
        MacroInfo.Commands.put("mathrm", new PredefMacroInfo(33, 1));
        MacroInfo.Commands.put("rm", new PredefMacroInfo(34, 0));
        MacroInfo.Commands.put("mathscr", new PredefMacroInfo(35, 1));
        MacroInfo.Commands.put("mathsf", new PredefMacroInfo(36, 1));
        MacroInfo.Commands.put("sf", new PredefMacroInfo(37, 0));
        MacroInfo.Commands.put("mathtt", new PredefMacroInfo(38, 1));
        MacroInfo.Commands.put("tt", new PredefMacroInfo(39, 0));
        MacroInfo.Commands.put("mathfrak", new PredefMacroInfo(40, 1));
        MacroInfo.Commands.put("mathds", new PredefMacroInfo(41, 1));
        MacroInfo.Commands.put("frak", new PredefMacroInfo(42, 1));
        MacroInfo.Commands.put("Bbb", new PredefMacroInfo(43, 1));
        MacroInfo.Commands.put("oldstylenums", new PredefMacroInfo(44, 1));
        MacroInfo.Commands.put("bold", new PredefMacroInfo(45, 1));
        MacroInfo.Commands.put("^", new PredefMacroInfo(46, 1));
        MacroInfo.Commands.put("\'", new PredefMacroInfo(47, 1));
        MacroInfo.Commands.put("\"", new PredefMacroInfo(48, 1));
        MacroInfo.Commands.put("`", new PredefMacroInfo(49, 1));
        MacroInfo.Commands.put("=", new PredefMacroInfo(50, 1));
        MacroInfo.Commands.put(".", new PredefMacroInfo(51, 1));
        MacroInfo.Commands.put("~", new PredefMacroInfo(52, 1));
        MacroInfo.Commands.put("u", new PredefMacroInfo(53, 1));
        MacroInfo.Commands.put("v", new PredefMacroInfo(54, 1));
        MacroInfo.Commands.put("H", new PredefMacroInfo(55, 1));
        MacroInfo.Commands.put("r", new PredefMacroInfo(56, 1));
        MacroInfo.Commands.put("U", new PredefMacroInfo(57, 1));
        MacroInfo.Commands.put("T", new PredefMacroInfo(58, 1));
        MacroInfo.Commands.put("t", new PredefMacroInfo(59, 1));
        MacroInfo.Commands.put("accent", new PredefMacroInfo(60, 2));
        MacroInfo.Commands.put("grkaccent", new PredefMacroInfo(61, 2));
        MacroInfo.Commands.put("hat", new PredefMacroInfo(62, 1));
        MacroInfo.Commands.put("widehat", new PredefMacroInfo(63, 1));
        MacroInfo.Commands.put("tilde", new PredefMacroInfo(64, 1));
        MacroInfo.Commands.put("acute", new PredefMacroInfo(65, 1));
        MacroInfo.Commands.put("grave", new PredefMacroInfo(66, 1));
        MacroInfo.Commands.put("ddot", new PredefMacroInfo(67, 1));
        MacroInfo.Commands.put("cyrddot", new PredefMacroInfo(68, 1));
        MacroInfo.Commands.put("mathring", new PredefMacroInfo(69, 1));
        MacroInfo.Commands.put("bar", new PredefMacroInfo(70, 1));
        MacroInfo.Commands.put("breve", new PredefMacroInfo(71, 1));
        MacroInfo.Commands.put("check", new PredefMacroInfo(72, 1));
        MacroInfo.Commands.put("vec", new PredefMacroInfo(73, 1));
        MacroInfo.Commands.put("dot", new PredefMacroInfo(74, 1));
        MacroInfo.Commands.put("widetilde", new PredefMacroInfo(75, 1));
        MacroInfo.Commands.put("nbsp", new PredefMacroInfo(76, 0));
        MacroInfo.Commands.put("smallmatrix@@env", new PredefMacroInfo(77, 1));
        MacroInfo.Commands.put("matrix@@env", new PredefMacroInfo(78, 1));
        MacroInfo.Commands.put("overrightarrow", new PredefMacroInfo(79, 1));
        MacroInfo.Commands.put("overleftarrow", new PredefMacroInfo(80, 1));
        MacroInfo.Commands.put("overleftrightarrow", new PredefMacroInfo(81, 1));
        MacroInfo.Commands.put("underrightarrow", new PredefMacroInfo(82, 1));
        MacroInfo.Commands.put("underleftarrow", new PredefMacroInfo(83, 1));
        MacroInfo.Commands.put("underleftrightarrow", new PredefMacroInfo(84, 1));
        MacroInfo.Commands.put("xleftarrow", new PredefMacroInfo(85, 1, 1));
        MacroInfo.Commands.put("xrightarrow", new PredefMacroInfo(86, 1, 1));
        MacroInfo.Commands.put("underbrace", new PredefMacroInfo(87, 1));
        MacroInfo.Commands.put("overbrace", new PredefMacroInfo(88, 1));
        MacroInfo.Commands.put("underbrack", new PredefMacroInfo(89, 1));
        MacroInfo.Commands.put("overbrack", new PredefMacroInfo(90, 1));
        MacroInfo.Commands.put("underparen", new PredefMacroInfo(91, 1));
        MacroInfo.Commands.put("overparen", new PredefMacroInfo(92, 1));
        MacroInfo.Commands.put("sqrt", new PredefMacroInfo(93, 1, 1));
        MacroInfo.Commands.put("sqrtsign", new PredefMacroInfo(94, 1));
        MacroInfo.Commands.put("overline", new PredefMacroInfo(95, 1));
        MacroInfo.Commands.put("underline", new PredefMacroInfo(96, 1));
        MacroInfo.Commands.put("mathop", new PredefMacroInfo(97, 1));
        MacroInfo.Commands.put("mathpunct", new PredefMacroInfo(98, 1));
        MacroInfo.Commands.put("mathord", new PredefMacroInfo(99, 1));
        MacroInfo.Commands.put("mathrel", new PredefMacroInfo(100, 1));
        MacroInfo.Commands.put("mathinner", new PredefMacroInfo(101, 1));
        MacroInfo.Commands.put("mathbin", new PredefMacroInfo(102, 1));
        MacroInfo.Commands.put("mathopen", new PredefMacroInfo(103, 1));
        MacroInfo.Commands.put("mathclose", new PredefMacroInfo(104, 1));
        MacroInfo.Commands.put("joinrel", new PredefMacroInfo(105, 0));
        MacroInfo.Commands.put("smash", new PredefMacroInfo(106, 1, 1));
        MacroInfo.Commands.put("vdots", new PredefMacroInfo(107, 0));
        MacroInfo.Commands.put("ddots", new PredefMacroInfo(108, 0));
        MacroInfo.Commands.put("iddots", new PredefMacroInfo(109, 0));
        MacroInfo.Commands.put("nolimits", new PredefMacroInfo(110, 0));
        MacroInfo.Commands.put("limits", new PredefMacroInfo(111, 0));
        MacroInfo.Commands.put("normal", new PredefMacroInfo(112, 0));
        MacroInfo.Commands.put("(", new PredefMacroInfo(113, 0));
        MacroInfo.Commands.put("[", new PredefMacroInfo(114, 0));
        MacroInfo.Commands.put("left", new PredefMacroInfo(115, 1));
        MacroInfo.Commands.put("middle", new PredefMacroInfo(116, 1));
        MacroInfo.Commands.put("cr", new PredefMacroInfo(117, 0));
        MacroInfo.Commands.put("multicolumn", new PredefMacroInfo(118, 3));
        MacroInfo.Commands.put("hdotsfor", new PredefMacroInfo(119, 1, 1));
        MacroInfo.Commands.put("array@@env", new PredefMacroInfo(120, 2));
        MacroInfo.Commands.put("align@@env", new PredefMacroInfo(121, 2));
        MacroInfo.Commands.put("aligned@@env", new PredefMacroInfo(122, 2));
        MacroInfo.Commands.put("flalign@@env", new PredefMacroInfo(123, 2));
        MacroInfo.Commands.put("alignat@@env", new PredefMacroInfo(124, 2));
        MacroInfo.Commands.put("alignedat@@env", new PredefMacroInfo(125, 2));
        MacroInfo.Commands.put("multline@@env", new PredefMacroInfo(126, 2));
        MacroInfo.Commands.put("gather@@env", new PredefMacroInfo(127, 2));
        MacroInfo.Commands.put("gathered@@env", new PredefMacroInfo(128, 2));
        MacroInfo.Commands.put("shoveright", new PredefMacroInfo(129, 1));
        MacroInfo.Commands.put("shoveleft", new PredefMacroInfo(130, 1));
        MacroInfo.Commands.put("\\", new PredefMacroInfo(131, 0));
        MacroInfo.Commands.put("newenvironment", new PredefMacroInfo(132, 3));
        MacroInfo.Commands.put("renewenvironment", new PredefMacroInfo(133, 3));
        MacroInfo.Commands.put("makeatletter", new PredefMacroInfo(134, 0));
        MacroInfo.Commands.put("makeatother", new PredefMacroInfo(135, 0));
        MacroInfo.Commands.put("fbox", new PredefMacroInfo(136, 1));
        MacroInfo.Commands.put("boxed", new PredefMacroInfo(137, 1));
        MacroInfo.Commands.put("stackrel", new PredefMacroInfo(138, 2, 1));
        MacroInfo.Commands.put("stackbin", new PredefMacroInfo(139, 2, 1));
        MacroInfo.Commands.put("accentset", new PredefMacroInfo(140, 2));
        MacroInfo.Commands.put("underaccent", new PredefMacroInfo(141, 2));
        MacroInfo.Commands.put("undertilde", new PredefMacroInfo(142, 1));
        MacroInfo.Commands.put("overset", new PredefMacroInfo(143, 2));
        MacroInfo.Commands.put("Braket", new PredefMacroInfo(144, 1));
        MacroInfo.Commands.put("Set", new PredefMacroInfo(145, 1));
        MacroInfo.Commands.put("underset", new PredefMacroInfo(146, 2));
        MacroInfo.Commands.put("boldsymbol", new PredefMacroInfo(147, 1));
        MacroInfo.Commands.put("LaTeX", new PredefMacroInfo(148, 0));
        MacroInfo.Commands.put("GeoGebra", new PredefMacroInfo(149, 0));
        MacroInfo.Commands.put("big", new PredefMacroInfo(150, 1));
        MacroInfo.Commands.put("Big", new PredefMacroInfo(151, 1));
        MacroInfo.Commands.put("bigg", new PredefMacroInfo(152, 1));
        MacroInfo.Commands.put("Bigg", new PredefMacroInfo(153, 1));
        MacroInfo.Commands.put("bigl", new PredefMacroInfo(154, 1));
        MacroInfo.Commands.put("Bigl", new PredefMacroInfo(155, 1));
        MacroInfo.Commands.put("biggl", new PredefMacroInfo(156, 1));
        MacroInfo.Commands.put("Biggl", new PredefMacroInfo(157, 1));
        MacroInfo.Commands.put("bigr", new PredefMacroInfo(158, 1));
        MacroInfo.Commands.put("Bigr", new PredefMacroInfo(159, 1));
        MacroInfo.Commands.put("biggr", new PredefMacroInfo(160, 1));
        MacroInfo.Commands.put("Biggr", new PredefMacroInfo(161, 1));
        MacroInfo.Commands.put("displaystyle", new PredefMacroInfo(162, 0));
        MacroInfo.Commands.put("textstyle", new PredefMacroInfo(163, 0));
        MacroInfo.Commands.put("scriptstyle", new PredefMacroInfo(164, 0));
        MacroInfo.Commands.put("scriptscriptstyle", new PredefMacroInfo(165, 0));
        MacroInfo.Commands.put("sideset", new PredefMacroInfo(166, 3));
        MacroInfo.Commands.put("prescript", new PredefMacroInfo(167, 3));
        MacroInfo.Commands.put("rotatebox", new PredefMacroInfo(168, 2, 1));
        MacroInfo.Commands.put("reflectbox", new PredefMacroInfo(169, 1));
        MacroInfo.Commands.put("scalebox", new PredefMacroInfo(170, 2, 2));
        MacroInfo.Commands.put("resizebox", new PredefMacroInfo(171, 3));
        MacroInfo.Commands.put("raisebox", new PredefMacroInfo(172, 2, 2));
        MacroInfo.Commands.put("shadowbox", new PredefMacroInfo(173, 1));
        MacroInfo.Commands.put("ovalbox", new PredefMacroInfo(174, 1));
        MacroInfo.Commands.put("doublebox", new PredefMacroInfo(175, 1));
        MacroInfo.Commands.put("phantom", new PredefMacroInfo(176, 1));
        MacroInfo.Commands.put("hphantom", new PredefMacroInfo(177, 1));
        MacroInfo.Commands.put("vphantom", new PredefMacroInfo(178, 1));
        MacroInfo.Commands.put("sp@breve", new PredefMacroInfo(179, 0));
        MacroInfo.Commands.put("sp@hat", new PredefMacroInfo(180, 0));
        MacroInfo.Commands.put("definecolor", new PredefMacroInfo(181, 3));
        MacroInfo.Commands.put("textcolor", new PredefMacroInfo(182, 2));
        MacroInfo.Commands.put("fgcolor", new PredefMacroInfo(183, 2));
        MacroInfo.Commands.put("bgcolor", new PredefMacroInfo(184, 2));
        MacroInfo.Commands.put("colorbox", new PredefMacroInfo(185, 2));
        MacroInfo.Commands.put("fcolorbox", new PredefMacroInfo(186, 3));
        MacroInfo.Commands.put("c", new PredefMacroInfo(187, 1));
        MacroInfo.Commands.put("IJ", new PredefMacroInfo(188, 0));
        MacroInfo.Commands.put("ij", new PredefMacroInfo(189, 0));
        MacroInfo.Commands.put("TStroke", new PredefMacroInfo(190, 0));
        MacroInfo.Commands.put("tStroke", new PredefMacroInfo(191, 0));
        MacroInfo.Commands.put("Lcaron", new PredefMacroInfo(192, 0));
        MacroInfo.Commands.put("tcaron", new PredefMacroInfo(193, 0));
        MacroInfo.Commands.put("lcaron", new PredefMacroInfo(194, 0));
        MacroInfo.Commands.put("k", new PredefMacroInfo(195, 1));
        MacroInfo.Commands.put("cong", new PredefMacroInfo(196, 0));
        MacroInfo.Commands.put("doteq", new PredefMacroInfo(197, 0));
        MacroInfo.Commands.put("jlmDynamic", new PredefMacroInfo(198, 1, 1));
        MacroInfo.Commands.put("jlmExternalFont", new PredefMacroInfo(199, 1));
        MacroInfo.Commands.put("jlmText", new PredefMacroInfo(200, 1));
        MacroInfo.Commands.put("jlmTextit", new PredefMacroInfo(201, 1));
        MacroInfo.Commands.put("jlmTextbf", new PredefMacroInfo(202, 1));
        MacroInfo.Commands.put("jlmTextitbf", new PredefMacroInfo(203, 1));
        MacroInfo.Commands.put("DeclareMathSizes", new PredefMacroInfo(204, 4));
        MacroInfo.Commands.put("magnification", new PredefMacroInfo(205, 1));
        MacroInfo.Commands.put("hline", new PredefMacroInfo(206, 0));
        MacroInfo.Commands.put("tiny", new PredefMacroInfo(207, 0));
        MacroInfo.Commands.put("scriptsize", new PredefMacroInfo(208, 0));
        MacroInfo.Commands.put("footnotesize", new PredefMacroInfo(209, 0));
        MacroInfo.Commands.put("small", new PredefMacroInfo(210, 0));
        MacroInfo.Commands.put("normalsize", new PredefMacroInfo(211, 0));
        MacroInfo.Commands.put("large", new PredefMacroInfo(212, 0));
        MacroInfo.Commands.put("Large", new PredefMacroInfo(213, 0));
        MacroInfo.Commands.put("LARGE", new PredefMacroInfo(214, 0));
        MacroInfo.Commands.put("huge", new PredefMacroInfo(215, 0));
        MacroInfo.Commands.put("Huge", new PredefMacroInfo(216, 0));
        MacroInfo.Commands.put("jlatexmathcumsup", new PredefMacroInfo(217, 1));
        MacroInfo.Commands.put("jlatexmathcumsub", new PredefMacroInfo(218, 1));
        MacroInfo.Commands.put("hstrok", new PredefMacroInfo(219, 0));
        MacroInfo.Commands.put("Hstrok", new PredefMacroInfo(220, 0));
        MacroInfo.Commands.put("dstrok", new PredefMacroInfo(221, 0));
        MacroInfo.Commands.put("Dstrok", new PredefMacroInfo(222, 0));
        MacroInfo.Commands.put("dotminus", new PredefMacroInfo(223, 0));
        MacroInfo.Commands.put("ratio", new PredefMacroInfo(224, 0));
        MacroInfo.Commands.put("smallfrowneq", new PredefMacroInfo(225, 0));
        MacroInfo.Commands.put("geoprop", new PredefMacroInfo(226, 0));
        MacroInfo.Commands.put("minuscolon", new PredefMacroInfo(227, 0));
        MacroInfo.Commands.put("minuscoloncolon", new PredefMacroInfo(228, 0));
        MacroInfo.Commands.put("simcolon", new PredefMacroInfo(229, 0));
        MacroInfo.Commands.put("simcoloncolon", new PredefMacroInfo(230, 0));
        MacroInfo.Commands.put("approxcolon", new PredefMacroInfo(231, 0));
        MacroInfo.Commands.put("approxcoloncolon", new PredefMacroInfo(232, 0));
        MacroInfo.Commands.put("coloncolon", new PredefMacroInfo(233, 0));
        MacroInfo.Commands.put("equalscolon", new PredefMacroInfo(234, 0));
        MacroInfo.Commands.put("equalscoloncolon", new PredefMacroInfo(235, 0));
        MacroInfo.Commands.put("colonminus", new PredefMacroInfo(236, 0));
        MacroInfo.Commands.put("coloncolonminus", new PredefMacroInfo(237, 0));
        MacroInfo.Commands.put("colonequals", new PredefMacroInfo(238, 0));
        MacroInfo.Commands.put("coloncolonequals", new PredefMacroInfo(239, 0));
        MacroInfo.Commands.put("colonsim", new PredefMacroInfo(240, 0));
        MacroInfo.Commands.put("coloncolonsim", new PredefMacroInfo(241, 0));
        MacroInfo.Commands.put("colonapprox", new PredefMacroInfo(242, 0));
        MacroInfo.Commands.put("coloncolonapprox", new PredefMacroInfo(243, 0));
        MacroInfo.Commands.put("kern", new PredefMacroInfo(244, 1));
        MacroInfo.Commands.put("char", new PredefMacroInfo(245, 1));
        MacroInfo.Commands.put("roman", new PredefMacroInfo(246, 1));
        MacroInfo.Commands.put("Roman", new PredefMacroInfo(247, 1));
        MacroInfo.Commands.put("textcircled", new PredefMacroInfo(248, 1));
        MacroInfo.Commands.put("textsc", new PredefMacroInfo(249, 1));
        MacroInfo.Commands.put("sc", new PredefMacroInfo(250, 0));
        MacroInfo.Commands.put(",", new PredefMacroInfo(251, 0));
        MacroInfo.Commands.put(":", new PredefMacroInfo(252, 0));
        MacroInfo.Commands.put(";", new PredefMacroInfo(253, 0));
        MacroInfo.Commands.put("thinspace", new PredefMacroInfo(254, 0));
        MacroInfo.Commands.put("medspace", new PredefMacroInfo(255, 0));
        MacroInfo.Commands.put("thickspace", new PredefMacroInfo(256, 0));
        MacroInfo.Commands.put("!", new PredefMacroInfo(257, 0));
        MacroInfo.Commands.put("negthinspace", new PredefMacroInfo(258, 0));
        MacroInfo.Commands.put("negmedspace", new PredefMacroInfo(259, 0));
        MacroInfo.Commands.put("negthickspace", new PredefMacroInfo(260, 0));
        MacroInfo.Commands.put("quad", new PredefMacroInfo(261, 0));
        MacroInfo.Commands.put("surd", new PredefMacroInfo(262, 0));
        MacroInfo.Commands.put("iint", new PredefMacroInfo(263, 0));
        MacroInfo.Commands.put("iiint", new PredefMacroInfo(264, 0));
        MacroInfo.Commands.put("iiiint", new PredefMacroInfo(265, 0));
        MacroInfo.Commands.put("idotsint", new PredefMacroInfo(266, 0));
        MacroInfo.Commands.put("int", new PredefMacroInfo(267, 0));
        MacroInfo.Commands.put("oint", new PredefMacroInfo(268, 0));
        MacroInfo.Commands.put("lmoustache", new PredefMacroInfo(269, 0));
        MacroInfo.Commands.put("rmoustache", new PredefMacroInfo(270, 0));
        MacroInfo.Commands.put("-", new PredefMacroInfo(271, 0));
        MacroInfo.Commands.put("jlmXML", new PredefMacroInfo(272, 1));
        MacroInfo.Commands.put("above", new PredefMacroInfo(273, 0));
        MacroInfo.Commands.put("abovewithdelims", new PredefMacroInfo(274, 2));
        MacroInfo.Commands.put("st", new PredefMacroInfo(275, 1));
        MacroInfo.Commands.put("fcscore", new PredefMacroInfo(276, 1));
        MacroInfo.Commands.put("mathnormal", new PredefMacroInfo(277, 1));
        MacroInfo.Commands.put("qquad", new PredefMacroInfo(278, 0));
        MacroInfo.Commands.put("longdiv", new PredefMacroInfo(279, 2));
        MacroInfo.Commands.put("questeq", new PredefMacroInfo(280, 0));
        MacroInfo.Commands.put("bangle", new PredefMacroInfo(281, 0));
        MacroInfo.Commands.put("brace", new PredefMacroInfo(282, 0));
        MacroInfo.Commands.put("brack", new PredefMacroInfo(283, 0));
    }
}
