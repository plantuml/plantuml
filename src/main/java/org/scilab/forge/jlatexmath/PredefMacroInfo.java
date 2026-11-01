/* PredefMacroInfo.java
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

/**
 * Class to load the predefined commands. Mainly wrote to avoid the use of the Java reflection.
 */
class PredefMacroInfo extends MacroInfo {

    private int id;

    public PredefMacroInfo(int id, int nbArgs, int posOpts) {
        super(nbArgs, posOpts);
        this.id = id;
    }

    public PredefMacroInfo(int id, int nbArgs) {
        super(nbArgs);
        this.id = id;
    }

    public Object invoke(final TeXParser tp, final String[] args) throws ParseException {
        return invokeID(id, tp, args);
    }

    private static final Object invokeID(final int id, final TeXParser tp, final String[] args) throws ParseException {
        try {
            switch (id) {
            case 0:
                return PredefMacros.newcommand_macro(tp, args);
            case 1:
                return PredefMacros.renewcommand_macro(tp, args);
            case 2:
                return PredefMacros.rule_macro(tp, args);
            case 3:
            case 4:
                return PredefMacros.hvspace_macro(tp, args);
            case 5:
            case 6:
            case 7:
                return PredefMacros.clrlap_macro(tp, args);
            case 8:
            case 9:
            case 10:
                return PredefMacros.mathclrlap_macro(tp, args);
            case 11:
                return PredefMacros.includegraphics_macro(tp, args);
            case 12:
                return PredefMacros.cfrac_macro(tp, args);
            case 13:
                return PredefMacros.frac_macro(tp, args);
            case 14:
                return PredefMacros.sfrac_macro(tp, args);
            case 15:
                return PredefMacros.genfrac_macro(tp, args);
            case 16:
                return PredefMacros.over_macro(tp, args);
            case 17:
                return PredefMacros.overwithdelims_macro(tp, args);
            case 18:
                return PredefMacros.atop_macro(tp, args);
            case 19:
                return PredefMacros.atopwithdelims_macro(tp, args);
            case 20:
                return PredefMacros.choose_macro(tp, args);
            case 21:
                return PredefMacros.underscore_macro(tp, args);
            case 22:
                return PredefMacros.mbox_macro(tp, args);
            case 23:
                return PredefMacros.text_macro(tp, args);
            case 24:
                return PredefMacros.intertext_macro(tp, args);
            case 25:
                return PredefMacros.binom_macro(tp, args);
            case 26:
                return PredefMacros.mathbf_macro(tp, args);
            case 27:
                return PredefMacros.bf_macro(tp, args);
            case 28:
                return PredefMacros.textstyle_macros(tp, args);
            case 29:
                return PredefMacros.textstyle_macros(tp, args);
            case 30:
                return PredefMacros.textstyle_macros(tp, args);
            case 31:
                return PredefMacros.mathit_macro(tp, args);
            case 32:
                return PredefMacros.it_macro(tp, args);
            case 33:
                return PredefMacros.mathrm_macro(tp, args);
            case 34:
                return PredefMacros.rm_macro(tp, args);
            case 35:
                return PredefMacros.textstyle_macros(tp, args);
            case 36:
                return PredefMacros.mathsf_macro(tp, args);
            case 37:
                return PredefMacros.sf_macro(tp, args);
            case 38:
                return PredefMacros.mathtt_macro(tp, args);
            case 39:
                return PredefMacros.tt_macro(tp, args);
            case 40:
            case 41:
            case 42:
            case 43:
            case 44:
            case 45:
                return PredefMacros.textstyle_macros(tp, args);
            case 46:
            case 47:
            case 48:
            case 49:
            case 50:
            case 51:
            case 52:
            case 53:
            case 54:
            case 55:
            case 56:
            case 57:
                return PredefMacros.accentbis_macros(tp, args);
            case 58:
                return PredefMacros.T_macro(tp, args);
            case 59:
                return PredefMacros.accentbis_macros(tp, args);
            case 60:
                return PredefMacros.accent_macro(tp, args);
            case 61:
                return PredefMacros.grkaccent_macro(tp, args);
            case 62:
            case 63:
            case 64:
            case 65:
            case 66:
            case 67:
            case 68:
            case 69:
            case 70:
            case 71:
            case 72:
            case 73:
            case 74:
            case 75:
                return PredefMacros.accent_macros(tp, args);
            case 76:
                return PredefMacros.nbsp_macro(tp, args);
            case 77:
                return PredefMacros.smallmatrixATATenv_macro(tp, args);
            case 78:
                return PredefMacros.matrixATATenv_macro(tp, args);
            case 79:
                return PredefMacros.overrightarrow_macro(tp, args);
            case 80:
                return PredefMacros.overleftarrow_macro(tp, args);
            case 81:
                return PredefMacros.overleftrightarrow_macro(tp, args);
            case 82:
                return PredefMacros.underrightarrow_macro(tp, args);
            case 83:
                return PredefMacros.underleftarrow_macro(tp, args);
            case 84:
                return PredefMacros.underleftrightarrow_macro(tp, args);
            case 85:
                return PredefMacros.xleftarrow_macro(tp, args);
            case 86:
                return PredefMacros.xrightarrow_macro(tp, args);
            case 87:
                return PredefMacros.underbrace_macro(tp, args);
            case 88:
                return PredefMacros.overbrace_macro(tp, args);
            case 89:
                return PredefMacros.underbrack_macro(tp, args);
            case 90:
                return PredefMacros.overbrack_macro(tp, args);
            case 91:
                return PredefMacros.underparen_macro(tp, args);
            case 92:
                return PredefMacros.overparen_macro(tp, args);
            case 93:
            case 94:
                return PredefMacros.sqrt_macro(tp, args);
            case 95:
                return PredefMacros.overline_macro(tp, args);
            case 96:
                return PredefMacros.underline_macro(tp, args);
            case 97:
                return PredefMacros.mathop_macro(tp, args);
            case 98:
                return PredefMacros.mathpunct_macro(tp, args);
            case 99:
                return PredefMacros.mathord_macro(tp, args);
            case 100:
                return PredefMacros.mathrel_macro(tp, args);
            case 101:
                return PredefMacros.mathinner_macro(tp, args);
            case 102:
                return PredefMacros.mathbin_macro(tp, args);
            case 103:
                return PredefMacros.mathopen_macro(tp, args);
            case 104:
                return PredefMacros.mathclose_macro(tp, args);
            case 105:
                return PredefMacros.joinrel_macro(tp, args);
            case 106:
                return PredefMacros.smash_macro(tp, args);
            case 107:
                return PredefMacros.vdots_macro(tp, args);
            case 108:
                return PredefMacros.ddots_macro(tp, args);
            case 109:
                return PredefMacros.iddots_macro(tp, args);
            case 110:
                return PredefMacros.nolimits_macro(tp, args);
            case 111:
                return PredefMacros.limits_macro(tp, args);
            case 112:
                return PredefMacros.normal_macro(tp, args);
            case 113:
                return PredefMacros.leftparenthesis_macro(tp, args);
            case 114:
                return PredefMacros.leftbracket_macro(tp, args);
            case 115:
                return PredefMacros.left_macro(tp, args);
            case 116:
                return PredefMacros.middle_macro(tp, args);
            case 117:
                return PredefMacros.cr_macro(tp, args);
            case 118:
                return PredefMacros.multicolumn_macro(tp, args);
            case 119:
                return PredefMacros.hdotsfor_macro(tp, args);
            case 120:
                return PredefMacros.arrayATATenv_macro(tp, args);
            case 121:
                return PredefMacros.alignATATenv_macro(tp, args);
            case 122:
                return PredefMacros.alignedATATenv_macro(tp, args);
            case 123:
                return PredefMacros.flalignATATenv_macro(tp, args);
            case 124:
                return PredefMacros.alignatATATenv_macro(tp, args);
            case 125:
                return PredefMacros.alignedatATATenv_macro(tp, args);
            case 126:
                return PredefMacros.multlineATATenv_macro(tp, args);
            case 127:
                return PredefMacros.gatherATATenv_macro(tp, args);
            case 128:
                return PredefMacros.gatheredATATenv_macro(tp, args);
            case 129:
                return PredefMacros.shoveright_macro(tp, args);
            case 130:
                return PredefMacros.shoveleft_macro(tp, args);
            case 131:
                return PredefMacros.backslashcr_macro(tp, args);
            case 132:
                return PredefMacros.newenvironment_macro(tp, args);
            case 133:
                return PredefMacros.renewenvironment_macro(tp, args);
            case 134:
                return PredefMacros.makeatletter_macro(tp, args);
            case 135:
                return PredefMacros.makeatother_macro(tp, args);
            case 136:
            case 137:
                return PredefMacros.fbox_macro(tp, args);
            case 138:
                return PredefMacros.stackrel_macro(tp, args);
            case 139:
                return PredefMacros.stackbin_macro(tp, args);
            case 140:
                return PredefMacros.accentset_macro(tp, args);
            case 141:
                return PredefMacros.underaccent_macro(tp, args);
            case 142:
                return PredefMacros.undertilde_macro(tp, args);
            case 143:
                return PredefMacros.overset_macro(tp, args);
            case 144:
                return PredefMacros.Braket_macro(tp, args);
            case 145:
                return PredefMacros.Set_macro(tp, args);
            case 146:
                return PredefMacros.underset_macro(tp, args);
            case 147:
                return PredefMacros.boldsymbol_macro(tp, args);
            case 148:
                return PredefMacros.LaTeX_macro(tp, args);
            case 149:
                return PredefMacros.GeoGebra_macro(tp, args);
            case 150:
                return PredefMacros.big_macro(tp, args);
            case 151:
                return PredefMacros.Big_macro(tp, args);
            case 152:
                return PredefMacros.bigg_macro(tp, args);
            case 153:
                return PredefMacros.Bigg_macro(tp, args);
            case 154:
                return PredefMacros.bigl_macro(tp, args);
            case 155:
                return PredefMacros.Bigl_macro(tp, args);
            case 156:
                return PredefMacros.biggl_macro(tp, args);
            case 157:
                return PredefMacros.Biggl_macro(tp, args);
            case 158:
                return PredefMacros.bigr_macro(tp, args);
            case 159:
                return PredefMacros.Bigr_macro(tp, args);
            case 160:
                return PredefMacros.biggr_macro(tp, args);
            case 161:
                return PredefMacros.Biggr_macro(tp, args);
            case 162:
                return PredefMacros.displaystyle_macro(tp, args);
            case 163:
                return PredefMacros.textstyle_macro(tp, args);
            case 164:
                return PredefMacros.scriptstyle_macro(tp, args);
            case 165:
                return PredefMacros.scriptscriptstyle_macro(tp, args);
            case 166:
                return PredefMacros.sideset_macro(tp, args);
            case 167:
                return PredefMacros.prescript_macro(tp, args);
            case 168:
                return PredefMacros.rotatebox_macro(tp, args);
            case 169:
                return PredefMacros.reflectbox_macro(tp, args);
            case 170:
                return PredefMacros.scalebox_macro(tp, args);
            case 171:
                return PredefMacros.resizebox_macro(tp, args);
            case 172:
                return PredefMacros.raisebox_macro(tp, args);
            case 173:
                return PredefMacros.shadowbox_macro(tp, args);
            case 174:
                return PredefMacros.ovalbox_macro(tp, args);
            case 175:
                return PredefMacros.doublebox_macro(tp, args);
            case 176:
                return PredefMacros.phantom_macro(tp, args);
            case 177:
                return PredefMacros.hphantom_macro(tp, args);
            case 178:
                return PredefMacros.vphantom_macro(tp, args);
            case 179:
                return PredefMacros.spATbreve_macro(tp, args);
            case 180:
                return PredefMacros.spAThat_macro(tp, args);
            case 181:
                return PredefMacros.definecolor_macro(tp, args);
            case 182:
                return PredefMacros.textcolor_macro(tp, args);
            case 183:
                return PredefMacros.fgcolor_macro(tp, args);
            case 184:
                return PredefMacros.bgcolor_macro(tp, args);
            case 185:
                return PredefMacros.colorbox_macro(tp, args);
            case 186:
                return PredefMacros.fcolorbox_macro(tp, args);
            case 187:
                return PredefMacros.cedilla_macro(tp, args);
            case 188:
                return PredefMacros.IJ_macro(tp, args);
            case 189:
                return PredefMacros.IJ_macro(tp, args);
            case 190:
                return PredefMacros.TStroke_macro(tp, args);
            case 191:
                return PredefMacros.TStroke_macro(tp, args);
            case 192:
                return PredefMacros.LCaron_macro(tp, args);
            case 193:
                return PredefMacros.tcaron_macro(tp, args);
            case 194:
                return PredefMacros.LCaron_macro(tp, args);
            case 195:
                return PredefMacros.ogonek_macro(tp, args);
            case 196:
                return PredefMacros.cong_macro(tp, args);
            case 197:
                return PredefMacros.doteq_macro(tp, args);
            case 198:
                return PredefMacros.jlmDynamic_macro(tp, args);
            case 199:
                return PredefMacros.jlmExternalFont_macro(tp, args);
            case 200:
                return PredefMacros.jlmText_macro(tp, args);
            case 201:
                return PredefMacros.jlmTextit_macro(tp, args);
            case 202:
                return PredefMacros.jlmTextbf_macro(tp, args);
            case 203:
                return PredefMacros.jlmTextitbf_macro(tp, args);
            case 204:
                return PredefMacros.DeclareMathSizes_macro(tp, args);
            case 205:
                return PredefMacros.magnification_macro(tp, args);
            case 206:
                return PredefMacros.hline_macro(tp, args);
            case 207:
            case 208:
            case 209:
            case 210:
            case 211:
            case 212:
            case 213:
            case 214:
            case 215:
            case 216:
                return PredefMacros.size_macros(tp, args);
            case 217:
                return PredefMacros.jlatexmathcumsup_macro(tp, args);
            case 218:
                return PredefMacros.jlatexmathcumsub_macro(tp, args);
            case 219:
                return PredefMacros.hstrok_macro(tp, args);
            case 220:
                return PredefMacros.Hstrok_macro(tp, args);
            case 221:
                return PredefMacros.dstrok_macro(tp, args);
            case 222:
                return PredefMacros.Dstrok_macro(tp, args);
            case 223:
                return PredefMacros.dotminus_macro(tp, args);
            case 224:
                return PredefMacros.ratio_macro(tp, args);
            case 225:
                return PredefMacros.smallfrowneq_macro(tp, args);
            case 226:
                return PredefMacros.geoprop_macro(tp, args);
            case 227:
                return PredefMacros.minuscolon_macro(tp, args);
            case 228:
                return PredefMacros.minuscoloncolon_macro(tp, args);
            case 229:
                return PredefMacros.simcolon_macro(tp, args);
            case 230:
                return PredefMacros.simcoloncolon_macro(tp, args);
            case 231:
                return PredefMacros.approxcolon_macro(tp, args);
            case 232:
                return PredefMacros.approxcoloncolon_macro(tp, args);
            case 233:
                return PredefMacros.coloncolon_macro(tp, args);
            case 234:
                return PredefMacros.equalscolon_macro(tp, args);
            case 235:
                return PredefMacros.equalscoloncolon_macro(tp, args);
            case 236:
                return PredefMacros.colonminus_macro(tp, args);
            case 237:
                return PredefMacros.coloncolonminus_macro(tp, args);
            case 238:
                return PredefMacros.colonequals_macro(tp, args);
            case 239:
                return PredefMacros.coloncolonequals_macro(tp, args);
            case 240:
                return PredefMacros.colonsim_macro(tp, args);
            case 241:
                return PredefMacros.coloncolonsim_macro(tp, args);
            case 242:
                return PredefMacros.colonapprox_macro(tp, args);
            case 243:
                return PredefMacros.coloncolonapprox_macro(tp, args);
            case 244:
                return PredefMacros.kern_macro(tp, args);
            case 245:
                return PredefMacros.char_macro(tp, args);
            case 246:
            case 247:
                return PredefMacros.romannumeral_macro(tp, args);
            case 248:
                return PredefMacros.textcircled_macro(tp, args);
            case 249:
                return PredefMacros.textsc_macro(tp, args);
            case 250:
                return PredefMacros.sc_macro(tp, args);
            case 251:
            case 252:
            case 253:
            case 254:
            case 255:
            case 256:
            case 257:
            case 258:
            case 259:
            case 260:
                return PredefMacros.muskip_macros(tp, args);
            case 261:
                return PredefMacros.quad_macro(tp, args);
            case 262:
                return PredefMacros.surd_macro(tp, args);
            case 263:
                return PredefMacros.iint_macro(tp, args);
            case 264:
                return PredefMacros.iiint_macro(tp, args);
            case 265:
                return PredefMacros.iiiint_macro(tp, args);
            case 266:
                return PredefMacros.idotsint_macro(tp, args);
            case 267:
                return PredefMacros.int_macro(tp, args);
            case 268:
                return PredefMacros.oint_macro(tp, args);
            case 269:
                return PredefMacros.lmoustache_macro(tp, args);
            case 270:
                return PredefMacros.rmoustache_macro(tp, args);
            case 271:
                return PredefMacros.insertBreakMark_macro(tp, args);
            case 272:
                return PredefMacros.jlmXML_macro(tp, args);
            case 273:
                return PredefMacros.above_macro(tp, args);
            case 274:
                return PredefMacros.abovewithdelims_macro(tp, args);
            case 275:
                return PredefMacros.st_macro(tp, args);
            case 276:
                return PredefMacros.fcscore_macro(tp, args);
            case 277:
                return PredefMacros.textstyle_macros(tp, args);
            case 278:
                return PredefMacros.qquad_macro(tp, args);
            case 279:
                return PredefMacros.longdiv_macro(tp, args);
            case 280:
                return PredefMacros.questeq_macro(tp, args);
            case 281:
                return PredefMacros.bangle_macro(tp, args);
            case 282:
                return PredefMacros.brace_macro(tp, args);
            case 283:
                return PredefMacros.brack_macro(tp, args);
            default:
                return null;
            }
        } catch (Exception e) {
            throw new ParseException("Problem with command " + args[0] + " at position " + tp.getLine() + ":" + tp.getCol() + "\n" + e.getMessage());
        }
    }
}
