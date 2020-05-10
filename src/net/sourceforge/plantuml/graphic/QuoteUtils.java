/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2020, Arnaud Roques
 *
 * Project Info:  http://plantuml.com
 * 
 * If you like this project or if you find it useful, you can support us at:
 * 
 * http://plantuml.com/patreon (only 1$ per month!)
 * http://plantuml.com/paypal
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
package net.sourceforge.plantuml.graphic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class QuoteUtils {

	static private final List<String> quotes = Arrays.asList("Ur'f qrnq, Wvz.",
			"Ol Tenogune'f unzzre, ol gur fbaf bs Jbeina, lbh funyy or niratrq.",
			"Ebnqf? Jurer jr'er tbvat, jr qba'g arrq ebnqf.", "Gur gvzr vf bhg bs wbvag.",
			"P'rfg phevrhk purm yrf znevaf pr orfbva qr snver qrf cuenfrf.",
			"V'z gnyxvat nobhg gur bgure Crgre, gur bar ba gur bgure fvqr.", "Znl gur Sbepr or jvgu lbh!",
			"Arire tvir hc, arire fheeraqre...", "Unfgn yn ivfgn, onol.",
			"Url, Qbp, jr orggre onpx hc. Jr qba'g unir rabhtu ebnq gb trg hc gb 88.",
			"Terrgvatf, Cebsrffbe Snyxra. Funyy jr cynl n tnzr?", "V pna'g punatr gur ynj bs culfvpf!",
			"N fgenatr tnzr. Gur bayl jvaavat zbir vf abg gb cynl.", "V'z gur Tngrxrrcre, ner lbh gur Xrlznfgre?",
			"V nz gur Znfgre Pbageby Cebtenz. Ab bar Hfre jebgr zr.", "Yvsr? Qba'g gnyx gb zr nobhg yvsr.",
			"V nyjnlf gubhtug fbzrguvat jnf shaqnzragnyyl jebat jvgu gur havirefr.",
			"N ebobg znl abg vawher n uhzna orvat be, guebhtu vanpgvba, nyybj n uhzna orvat gb pbzr gb unez.",
			"Fheeraqre znl or bhe bayl bcgvba.", "Fvk ol avar. Sbegl gjb.", "Vg'f yvsr, Wvz, ohg abg nf jr xabj vg.",
			"Qba'g Cnavp!", "Jung qb lbh zrna? Na Nsevpna be Rhebcrna fjnyybj?",
			"V arrq lbhe obbgf lbhe pybgurf naq lbhe zbgbeplpyr", "Lbh sbetbg gb fnl cyrnfr...",
			"Lbh unir qvrq bs qlfragrel.", "Jbhyqa'g lbh cersre n avpr tnzr bs purff?",
			"Jura lbh unir ryvzvangrq gur vzcbffvoyr, jungrire erznvaf, ubjrire vzcebonoyr, zhfg or gur gehgu.",
			"V xabj abj jul lbh pel. Ohg vg'f fbzrguvat V pna arire qb.",
			"Erfvfgnapr vf shgvyr. Lbh jvyy or nffvzvyngrq.", "Nalguvat qvssrerag vf tbbq.",
			"Penpxrq ol Nyqb Erfrg naq Ynherag Ehrvy.", "V'z obgu. V'z n pryroevgl va na rzretrapl.",
			"Qb lbh xabj guvf terng terng cbyvfu npgbe, Wbfrcu Ghen?", "Gb vasvavgl naq orlbaq!",
			"Fcnpr: gur svany sebagvre...", "Fhe zba ovyyrg, grarm, l n rpevg Fnvag-Ynmner, p'rfg zrf lrhk bh dhbv ?",
			"Gur obl vf vzcbegnag. Ur unf gb yvir.", "Bapr hcba n gvzr va n tnynkl sne, sne njnl...",
			"Naq lbh xabj gurer'f n ybat ybat jnl nurnq bs lbh...", "Na nyyretl gb bkltra? Ryz oyvtug?",
			"Ohg nybef lbh ner Serapu!", "A'nv-wr qbap gnag irph dhr cbhe prggr vasnzvr?",
			"Fbzrguvat vf ebggra va gur Fgngr bs Qraznex.", "Url, jung qb lbh jnag? Zvenpyrf?",
			"1.21 tvtnjnggf! 1.21 tvtnjnggf. Terng Fpbgg! ", "Jung gur uryy vf n tvtnjngg?", "V arrq n inpngvba.",
			"Ba qrienvg wnznvf dhvggre Zbagnhona.", "Zl sbepr vf n cyngsbez gung lbh pna pyvzo ba...",
			"Gurer'f fbzrguvat jrveq, naq vg qba'g ybbx tbbq...", "Rg evra ienvzrag ar punatr znvf gbhg rfg qvssrerag",
			"Ornz zr hc, Fpbggl.", "Gurer vf ab fcbba.", "Sbyybj gur juvgr enoovg.",
			"Arire fraq n uhzna gb qb n znpuvar'f wbo.", "Theh zrqvgngvba. Cerff yrsg zbhfr ohggba gb pbagvahr.",
			"V qba'g guvax jr'er va Xnafnf nalzber.", "Yhxr, V nz lbhe sngure.", "Oybbq, Fjrng naq Grnef",
			"Ubhfgba, jr unir n ceboyrz.", "Xrlobneq snvyher, cerff nal xrl gb pbagvahr", "Ovt zvfgnxr!",
			"Ubj znal HZY qrfvtaref qbrf vg gnxr gb punatr n yvtugohyo ?", "Qb lbh yvxr zbivrf nobhg tynqvngbef ?",
			"Gur fcvevg bs yrneavat vf n ynfgvat sebagvre.",
			"Vg vf phevbhf sbe fnvybef guvf arrq sbe znxvat fragraprf.", "Ubcvat sbe gur orfg, ohg rkcrpgvat gur jbefg",
			"Gur jvyy gb tb ba jura V'z uheg qrrc vafvqr.", "Vs vg oyrrqf, jr pna xvyy vg.",
			"Ubhfgba, V unir n onq srryvat nobhg guvf zvffvba.",
			"Znzn nyjnlf fnvq yvsr jnf yvxr n obk bs pubpbyngrf. Lbh arire xabj jung lbh'er tbaan trg.",
			"Ol gur jnl, vf gurer nalbar ba obneq jub xabjf ubj gb syl n cynar?",
			"Qnir, guvf pbairefngvba pna freir ab checbfr nalzber. Tbbqolr.",
			"Vg pna bayl or nggevohgnoyr gb uhzna reebe.", "Ybbxf yvxr V cvpxrq gur jebat jrrx gb dhvg fzbxvat.",
			"Lbh uhznaf npg fb fgenatr. Rirelguvat lbh perngr vf hfrq gb qrfgebl.",
			"Jurer qvq lbh yrnea ubj gb artbgvngr yvxr gung?", "Fve, ner lbh pynffvsvrq nf uhzna?",
			"Jr'er abg tbaan znxr vg, ner jr?", "Vg'f va lbhe angher gb qrfgebl lbhefryirf.",
			"Gur zber pbagnpg V unir jvgu uhznaf, gur zber V yrnea.",
			"Jbhyq vg fnir lbh n ybg bs gvzr vs V whfg tnir hc naq jrag znq abj?", "Ernyvgl vf serdhragyl vanpphengr.",
			"Qba'g oryvrir nalguvat lbh ernq ba gur arg. Rkprcg guvf. Jryy, vapyhqvat guvf, V fhccbfr.",
			"N phc bs grn jbhyq erfgber zl abeznyvgl.",
			"Nalguvat gung guvaxf ybtvpnyyl pna or sbbyrq ol fbzrguvat ryfr gung guvaxf ng yrnfg nf ybtvpnyyl nf vg qbrf.",
			"Va na vasvavgr Havirefr nalguvat pna unccra.",
			"Fbzrgvzrf vs lbh erprvirq na nafjre, gur dhrfgvba zvtug or gnxra njnl.",
			"Cyrnfr pnyy zr Rqqvr vs vg jvyy uryc lbh gb erynk.",
			"V qba'g oryvrir vg. Cebir vg gb zr naq V fgvyy jba'g oryvrir vg.",
			"Gbgnyyl znq, hggre abafrafr. Ohg jr'yy qb vg orpnhfr vg'f oevyyvnag abafrafr.",
			"Guvf fragrapr vf abg gehr.", "V jbhyq engure qvr fgnaqvat guna yvir ba zl xarrf.",
			"Lbh ner orvat jngpurq.", "Qvq lbh srrq gurz nsgre zvqavtug?",
			"Ubj qb lbh rkcynva fpubby gb uvture vagryyvtrapr?", "Crbcyr fbzrgvzrf znxr zvfgnxrf.",
			"Ybbx, V qba'g unir gvzr sbe n pbairefngvba evtug abj.",
			"Nyy ceboyrzf va pbzchgre fpvrapr pna or fbyirq ol nabgure yriry bs vaqverpgvba",
			"...rkprcg sbe gur ceboyrz bs gbb znal yriryf bs vaqverpgvba", "V xabj orpnhfr V ohvyg vg",
			"Rira gur fznyyrfg crefba pna punatr gur pbhefr bs gur shgher.",
			"Vs lbh ner n sevraq, lbh fcrnx gur cnffjbeq, naq gur qbbef jvyy bcra.", "Lbh Funyy Abg Cnff",
			"73.6% Bs Nyy Fgngvfgvpf Ner Znqr Hc", "Jr pna arvgure pbasvez abe qral gung guvf vf penfuvat",
			"Jura gur orngvat bs lbhe urneg rpubrf gur orngvat bs gur qehzf",
			"Arire gehfg n pbzchgre lbh pna'g guebj bhg n jvaqbj",
			"Lrnu, V'z pnyz. V'z n pnyz crefba. Vf gurer fbzr ernfba V fubhyqa'g or pnyz?",
			"Rirelobql whfg fgnl pnyz. Gur fvghngvba vf haqre pbageby.", "Uvccl, lbh guvax rirelguvat vf n pbafcvenpl.",
			"Gurfr thlf ner nobhg nf zhpu sha nf n gnk nhqvg.", "Gurer vf fbzrguvat qbja gurer! Fbzrguvat abg hf.",
			"V fnj n tyvzcfr bs zl shgher naq rirelguvat'f punatrq sbe zr abj.", "Va fcnpr ab bar pna urne lbh fpernz",
			"V pna'g yvr gb lbh nobhg lbhe punaprf, ohg... lbh unir zl flzcnguvrf.",
			"Gurer vf na rkcynangvba sbe guvf, lbh xabj.", "V'z nsenvq V unir fbzr onq arjf.",
			"Qb zr n snibhe. Qvfpbaarpg zr. V pbhyq or erjbexrq, ohg V'yy arire or gbc bs gur yvar ntnva.",
			"Gnxr vg rnfl, qba'g chfu gur yvggyr ohggba ba gur wblfgvpx!", "V'z n irel cevingr crefba.",
			"Gb fphycg na ryrcunag sebz n ovt oybpx bs zneoyr, whfg xabpx njnl nyy gur ovgf gung qba'g ybbx yvxr na ryrcunag.",
			"Jub fnvq lbh pbhyq gnyx gb zr? Unir V tbg fbzrguvat ba zl snpr ?", "Jr'ir orra guebhtu jbefg",
			"Havgrq jr fgnaq", "Jr funyy arire fheeraqre",
			"Nofbyhgr ubarfgl vfa'g nyjnlf gur zbfg qvcybzngvp abe gur fnsrfg sbez bs pbzzhavpngvba jvgu rzbgvbany orvatf.",
			"Vg'f... pbzcyvpngrq.", "Qb abg bcra hagvy 1985", "V fgvyy zrff hc ohg V'yy whfg fgneg ntnva",
			"V jba'g tvir hc, ab V jba'g tvir va; Gvyy V ernpu gur raq; Naq gura V'yy fgneg ntnva",
			"V jnaan gel rira gubhtu V pbhyq snvy", "Fbzrgvzrf jr pbzr ynfg ohg jr qvq bhe orfg",
			"Vs lbh frr fbzrguvat, fnl fbzrguvat",
			"Va gurbel gurer vf ab qvssrerapr orgjrra gurbel naq cenpgvpr. Ohg, va cenpgvpr, gurer vf.",
			"Vs V pnaabg oevat lbh pbzsbeg gura ng yrnfg V oevat lbh ubcr",
			"Jr nyy zhfg yrnea sebz fznyy zvfsbeghar, pbhag gur oyrffvatf gung ner erny",
			"Cercner Guerr Frnyrq Rairybcrf...", "Lbh xabj gung guvat lbh whfg qvq? Qba'g qb gung",
			"Vg gbbx zr n ybat gvzr gb haqrefgnaq gung vs lbh jnag gb qb guvf wbo jryy lbh unir gb fgnl qrgnpurq.",
			"Qb lbh yvxr lbhe zbeavat grn jrnx be fgebat ?", "Jvagre vf pbzvat", "Jung sbbyf gurfr zbegnyf or!",
			"Fbzrguvat jvpxrq guvf jnl pbzrf.", "V guvax V trg vg, jung jnf vg? Cbxre Avtug? Onpurybe Cnegl?",
			"Vg'f nyevtug gb or fpnerq. Erzrzore, gurer vf ab pbhentr jvgubhg srne.",
			"Guebhtu ernqvarff naq qvfpvcyvar jr ner znfgref bs bhe sngr.",
			"Jvgu terng cbjre pbzrf terng erfcbafvovyvgl",
			"Vs n znpuvar pna yrnea gur inyhr bs uhzna yvsr, znlor jr pna gbb ?",
			"Bayl tbvat sbejneq 'pnhfr jr pna'g svaq erirefr.",
			"Jr'er abg tbaan fvg va fvyrapr, jr'er abg tbaan yvir jvgu srne",
			"Oba, qnaf qvk zvahgrf wr abhf pbafvqrer pbzzr qrsvavgvirzrag creqhf.",
			"Pn fren fherzrag ovra dhnaq pn fren svav.",
			"Vg'f gur ynfg cvrpr bs gur chmmyr ohg lbh whfg pna'g znxr vg svg",
			"Qbpgbe fnlf lbh'er pherq ohg lbh fgvyy srry gur cnva",
			"Vf negvsvpvny vagryyvtrapr gur rknpg bccbfvgr bs angheny fghcvqvgl ?",
			"Sbeprzrag, pn qrcraq, pn qrcnffr...", "Gurer'f orra n cnggrea bs vafhobeqvangr orunivbe erpragyl.",
			"Ab. Jr ner abg na rssrpgvir grnz.", "Bhe wbo vf abg gb erzrzore... erzrzore?",
			"Guvf vf zvffvba pbageby. Ubj ner lbh nyy qbvat guvf ybiryl zbeavat?",
			"Vs lbh pbhyq frr lbhe jubyr yvsr ynvq bhg va sebag bs lbh, jbhyq lbh punatr guvatf?",
			"Vf guvf n aba-mreb-fhz tnzr?", "Abj gung'f n cebcre vagebqhpgvba.",
			"Rirelguvat unf punatrq naq vg jba'g fgbc punatvat nalgvzr fbba.",
			"Jung znxrf lbh qvssrerag znxrf lbh qnatrebhf", "Qviretrapr vf rkgerzryl qnatrebhf",
			"V'z Qviretrag. Naq V pna'g or pbagebyyrq", "Znl gur bqqf or rire va lbhe snibe",
			"Ab WninFpevcg senzrjbexf jrer perngrq qhevat gur jevgvat bs guvf zrffntr.",
			"P'rfg cerffr-cherr dhv g'nf vagreebtr ?", "Ybbx, nygreangvir snpgf ner abg snpgf. Gurl'er snyfrubbqf",
			"Guvf vf abg n penfu, guvf vf zber bs na nygreangvir erfhyg.",
			"Lbh yrnearq gb cebtenz va SBEGENA qvqa'g lbh?",
			"Guvf oht vf n srngher nf qrfpevorq ol gur znexrgvat qrcnegzrag.",
			"Abg rirelobql haqrefgnaqf gur uhzbe bs cebtenzzref.",
			"Vs lbh yvir na beqvanel yvsr, nyy lbh'yy unir ner beqvanel fgbevrf.", "Pbzr jvgu zr vs lbh jnag gb yvir",
			"Gh y'nf gebhir bh pryhv-yn ?", "Qb lbh ernyyl guvax lbh unir n punapr ntnvafg hf, Ze. Pbjobl?",
			"Nggragvba, jubrire lbh ner, guvf punaary vf erfreirq sbe rzretrapl pnyyf bayl.",
			"Qbrf vg fbhaq yvxr V'z beqrevat n cvmmn? ", "Jr'er tbaan arrq fbzr zber SOV thlf, V thrff.",
			"Trg ernql sbe ehfu ubhe",
			"V unir gb jnea lbh, V'ir urneq eryngvbafuvcf onfrq ba vagrafr rkcrevraprf arire jbex.",
			"Nalguvat ryfr ohg gur onfrzrag gung'yy xrrc guvf ryringbe sebz snyyvat?",
			"Vf guvf grfgvat jurgure V'z n ercyvpnag be n yrfovna, Ze. Qrpxneq? ", "V'ir qbar... dhrfgvbanoyr guvatf",
			"Jbhyq lbh... yvxr gb or hctenqrq?", "Snhg erpbaanvger... p'rfg qh oehgny!",
			"Fv ba oevpbynvg cyhf fbhirag, ba nhenvg zbvaf yn grgr nhk orgvfrf.",
			"Wr invf yhv snver har beqbaanapr, rg har frirer...",
			"Znvf vy pbaanvg cnf Enbhy, pr zrp! vy in nibve ha erirvy cravoyr.",
			"W'nv ibhyh rger qvcybzngr n pnhfr qr ibhf gbhf, rivgre dhr yr fnat pbhyr.",
			"Vtabenapr oevatf punbf, abg xabjyrqtr.", "Yrneavat vf nyjnlf n cnvashy cebprff.",
			"V'z fbeel, ner lbh sebz gur cnfg ?", "Unir lbh gevrq gheavat vg bss naq ba ntnva ?",
			"Vs lbh qba'g xabj jurer lbh ner tbvat nal ebnq pna gnxr lbh gurer", "Xrrc pnyz naq cerff Pgey-Nyg-Qry",
			"Vs lbh glcr Tbbtyr vagb Tbbtyr, lbh pna oernx gur Vagrearg.", "V unir cneg bs n cyna.",
			"V'z cerggl fher gur nafjre vf: V nz Tebbg.", "Nalguvat gung pna cbffvoyl tb jebat, qbrf",
			"Cyhf pn engr, cyhf ba n qr punapr dhr pn znepur", "Fb V thrff gur sbeghar gryyre'f evtug...",
			"Jura rirelguvat'f tbar jebat fbzrubj...", "V qba'g jnag gb yvir ba guvf cynarg nalzber",
			"Oba, p'rfg y'urher bh yrf fbhiravef fr enzrarag...",
			"Fhpprff pbafvfgf bs tbvat sebz snvyher gb snvyher jvgubhg ybff bs raguhfvnfz",
			"Vs lbh'er tbvat guebhtu uryy, xrrc tbvat",
			"Jre xnzcsg, xnaa ireyvrera. Jre avpug xnzcsg, ung fpuba ireybera.",
			"P'rfg nh cvrq qh zhe dhr y'ba ibvg yr zvrhk yr zhe.",
			"Jr xabj ubj uhznaf jbex. Gurl ner nyy fb cerqvpgnoyr.", "Pyrneyl, lbh unir arire zrg n jbzna.",
			"Gur qbtznf bs gur dhvrg cnfg ner vanqrdhngr gb gur fgbezl cerfrag",
			"Ab jnl gb cerirag guvf, fnlf bayl angvba jurer guvf erthyneyl unccraf",
			"L'n dhrydhrf dhrfgvbaf dhv erfgrag fbhf fvyrapr...", "Vs gurer vf ab fbyhgvba, gurer vf ab ceboyrz",
			"V unir zrzbevrf, ohg V pna'g gryy vs gurl'er erny.", "L n pbzzr ha tbhg nzre ra abhf",
			"L n qrf fvyraprf dhv qvfrag ornhpbhc", "V frr lbh'ir unq fbzr qvfpvcyvanel ceboyrzf va gur cnfg.",
			"Cercnengvba vf gur xrl gb fhpprffshy, vapbafcvphbhf gvzr geniry.",
			"Vg'f arire gbb yngr gb or jub lbh zvtug unir orra.",
			"Rg cbhe nyyre purm Zvpxrl vyf ibag fherzrag cnf pbafgehver har tner gbhf yrf 100 zrgerf",
			"Nyy lbhe onfr ner orybat gb hf", "Znqr ba Rnegu ol uhznaf", "Jvaaref Qba'g Hfr Qehtf",
			"Lbh xabj jung fhecevfrq zr gur zbfg? Vg jnfa'g zrrgvat gurz. Vg jnf zrrgvat lbh.",
			"Va jne gurer ner ab jvaaref, bayl jvqbjf",
			"Vs lbh guvax guvf Havirefr vf onq, lbh fubhyq frr fbzr bs gur bguref", "Cnp-Zna'f n onq thl?",
			"Zl ernyvgl vf whfg qvssrerag guna lbhef", "L'ra n dh'bag rffnlr, vyf bag rh qrf ceboyrzrf",
			"Gb ree vf uhzna, ohg gb ernyyl sbhy guvatf hc erdhverf n pbzchgre.",
			"Vs lbh oryvrir rirelguvat lbh ernq, lbh orggre abg ernq",
			"Gurer vf ab ceboyrz fb onq lbh pna'g znxr vg jbefr", "Pn p'rfg qh ybheq... Ha gehp qr znynqr.",
			"V qb abg guvax, gung V guvax.. V guvax.", "Gurer ner cynprf ybjre guna gur onfrzrag",
			"Gurer ner 10 glcrf bs crbcyr: gubfr jub haqrefgnaq ovanel, naq gubfr jub qba'g.",
			"Cyrnfr zvaq gur tnc orgjrra gur genva naq gur cyngsbez", "Nfuvzbgb av tb-puhv xhqnfnv",
			"Vs lbh'er erprvivat guvf genafzvffvba, znxr ab nggrzcg gb pbzr gb vgf cbvag bs bevtva.",
			"Obl, qb V ungr orvat evtug nyy gur gvzr!",
			"Jub funirf gur oneore jub funirf nyy gur zra jub qba'g funir gurzfryirf?",
			"V haqrefgnaq uhzna rzbgvbaf, nygubhtu V qb abg srry gurz zlfrys.", "Lbh qvqa'g fnl gur zntvp jbeq!",
			"Gurer vf ab ceboyrz gung n ynpx bs fbyhgvba jba'g svanyyl erfbyir.",
			"V unir n inthr srryvat bs vzcraqvat zvfsbeghar.", "Jura lbh'er va gebhoyr, pbashfr rirelguvat",
			"Jung gur uryy vf Cebwrpg Ryebaq?", "Qba'g qvt hc gur ovt obk bs cyhgbavhz, Znex",
			"Uryc vf bayl 140 zvyyvba zvyrf njnl.",
			"P unf gur fcrrq naq rssvpvrapl bs nffrzoyl ynathntr pbzovarq jvgu ernqnovyvgl bs nffrzoyl ynathntr",
			"Crey vf gur bayl ynathntr gung ybbxf gur fnzr orsber naq nsgre EFN rapelcgvba",
			"Gur zber vg snvyf, gur zber yvxryl vg vf gung vg jvyy jbex",
			"V ubcr V qvqa'g gnxr hc gbb zhpu bs lbhe gvzr", "Lbh'er tbaan arrq n ovttre obng",
			"Dhnaq ibhf rgrf rzorgrf, rzoebhvyyrm gbhg", "Gurer nva'g ab phevat jung'f jebat jvgu gung guvat",
			"Vs lbh cevpx hf, qb jr abg oyrrq?", "V qvq lbhe wbo bapr - V jnf tbbq ng vg.",
			"Vyf cbheenvrag snver har fryrpgvba nh fgnaqneq...", "Gung'f ab jnl gb gerng n sevraq.",
			"Ubjrire ornhgvshy gur fgengrtl, lbh fubhyq bppnfvbanyyl ybbx ng gur erfhygf",
			"Qba'g svk vg vs vg'f abg oebxra",
			"Fhqqrayl V'z gnxvat fhttrfgvbaf sebz fbzr fgebat-nez zna jvgu na VD bs zvahf 50.",
			"Fur ehvaf rirelguvat fur'f va. Fur ehvaf guvatf fur'f abg rira va.",
			"Byvir, V guvax lbh fubhyq xabj guvf: lbh'er n ubeevoyr npgerff.",
			"Lbh'er yngr! Qb lbh unir ab pbaprcg bs gvzr ?", "P'zba zna, yrg'f qb fbzrguvat gung ernyyl pbbxf.",
			"Nh sbaq, znvagranag, yrf qvcybzngrf ceraqenvrag cyhgbg yr cnf fhe yrf ubzzrf q'npgvba.",
			"Whfg zragnyyl gubhtu... ner lbh bxnl ?", "Jr xabj fur'f bxnl orpnhfr fur'f oybaq", "Gh oyhssrf Znegbav",
			"Lbh thlf ner trggvat cnvq?",
			"Gurer'f n erq guvatl zbivat gbjneqf gur terra guvatl... V guvax jr'er gur terra guvatl",
			"Nyy flfgrzf ner shapgvbavat Pbzznaqre", "V erzrzore gung fbhaq! Gung'f n onq fbhaq!",
			"V xabj vg'f uneq sbe lbh gb haqrefgnaq rira fubeg fragraprf",
			"Gur havirefr vf shyy bs vagryyvtrag yvsr. Vg'f whfg orra gbb vagryyvtrag gb pbzr urer.",
			"Uryyb, jbhyq lbh yvxr gb urne n GPC wbxr ?", "Rirel snvel gnyr arrqf n tbbq byq-snfuvbarq ivyynva",
			"Fvapr gurer'f ab pbapyhfvba gb gur cnenqbk gurbel, gurer'f ab jnl gb cebir gurer'f ab cnenqbk.",
			"Gnxr zr guebhtu gur qnexarff gb gur oernx bs gur qnl",
			"Vg znxrf gur gehgu rira zber vapbzcerurafvoyr orpnhfr rirelguvat vf arj",
			"V qba'g xabj ubj ohg V fhqqrayl ybfr pbageby", "Gurer ner zbzragf jura V guvax V'z tbvat penml",
			"Jbhyq gung vg jrer fb fvzcyr", "Pnyy gung n xavsr? Guvf vf n xavsr.", "Fhe y'bprna qh grzcf dhv tyvffr...",
			"Snvyher serr bcrengvbaf erdhver rkcrevrapr jvgu snvyher.", "Pngnfgebcur vf nyjnlf whfg nebhaq gur pbeare.",
			"Orjner bs ohtf va gur nobir pbqr; V unir bayl cebirq vg pbeerpg, abg gevrq vg",
			"V'z abg n erny cebtenzzre. V guebj gbtrgure guvatf hagvy vg jbexf gura V zbir ba",
			"#qrsvar DHRFGVBA ((oo)||!(oo))", "Pbzchgref ner hfryrff. Gurl pna bayl tvir lbh nafjref",
			"Gur Vagrearg? Vf gung guvat fgvyy nebhaq?",
			"Va beqre gb haqrefgnaq erphefvba, bar zhfg svefg haqrefgnaq erphefvba",
			"Obhagl Uhagvat vf n pbzcyvpngrq cebsrffvba.",
			"Znahsnpghere'f cebgbpby qvpgngrf V pna abg or pncgherq. V zhfg frys-qrfgehpg.", "Snvyher vf abg na bcgvba",
			"Rirelguvat snvyf nyy gur gvzr", "Gur orfg jnl gb nibvq snvyher vf gb snvy pbafgnagyl",
			"Cerzngher bcgvzvmngvba vf gur ebbg bs nyy rivy", "Chgnva, w'ra nv zneer q'nibve gbhwbhef envfba",
			"Guvf vf gur jnl", "Cngvrapr lbh zhfg unir zl lbhat cnqnjna", "V gubhtug lbh jrer ba zl fvqr",
			"Qba'g cnavp. Abar bs lbh cnavp. Nofbyhgryl ab ernfba gb cnavp.",
			"Gung jnf hacyrnfnag. V'z fbeel lbh unq gb frr gung.", "Gur neg bs fvzcyvpvgl vf n chmmyr bs pbzcyrkvgl.",
			"Xrrc pnyz naq fgnl ng ubzr",
			"Gur terngrfg fubegpbzvat bs gur uhzna enpr vf bhe vanovyvgl gb haqrefgnaq gur rkcbaragvny shapgvba",
			"Vs gur Znegvnaf vainqrq rnegu, bhe svefg erfcbafr jbhyq or gb ybjre vagrerfg engrf.",
			"Rnfl gvzrf qba'g sbetr punenpgre. Vg'f gur gbhtu gvzrf gung sbetr punenpgre",
			"Fbzrgvzrf va beqre gb qb gur evtug guvat, lbh unir gb qb gur jebat guvat.",
			"Gur fgeratgu bs WninFpevcg vf gung lbh pna qb nalguvat. Gur jrnxarff vf gung lbh jvyy.",
			"Vg'f tbvat gb qvfnccrne. Bar qnl vg'f yvxr n zvenpyr, vg jvyy qvfnccrne. Cebonoyl ol Rnfgre, BX?",
			"V'ir sryg vg jnf n oht ybat orsber vg jnf pnyyrq n oht",
			"Yrg'f frr guvf nf n pbyyngreny qnzntr sebz fbzr ureq vzzhavgl pybja'f gurbel.",
			"Lbh'q unir gb or eryngviryl vagryyvtrag gb ernyvmr ubj fghcvq lbh ner",
			"Guvf rkcynvaf abg whfg Ubyyljbbq, ohg nyzbfg gur ragvergl bs Sbk Arjf",
			"Gur terngrfg rarzl bs xabjyrqtr vf abg vtabenapr; vg vf gur vyyhfvba bs xabjyrqtr",
			"N fghql sbhaq gung vtabenag crbcyr ner zber yvxryl gb oryvrir gurl'er oevyyvnag",
			"Dh'rfg-pr dh'ba n qbap n fbaare pbzzr pn, pr zngva ?", "Ernyvgl vf gur bayl guvat gung'f erny",
			"V jbhyq tvir zlfrys na N+", "Lbh rire unir frpbaq gubhtugf nobhg fbzrguvat ?",
			"V oryvrir vg'f n ebhtu fvghngvba bire gurer", "Whfg fb lbh xabj, V tbg zl yvprafr fhfcraqrq",
			"Gunax lbh sbe pubbfvat Bprnavp Nveyvarf", "4-8-15-16-23-42",
			"...naq gnk phgf. Gung'yy fubj gurz znegvnaf.",
			"Jurarire V srry gur arrq gb rkrepvfr, V yvr qbja hagvy vg tbrf njnl", "Ernyvgl pbagvahrf gb ehva zl yvsr",
			"Vs lbh gvpxyr hf, qb jr abg ynhtu?", "V xabj n HQC wbxr, ohg lbh zvtug abg trg vg",
			"Chvfdhr prf zlfgrerf abhf qrcnffrag, srvtabaf q'ra rger y'betnavfngrhe.");

	private QuoteUtils() {
	}

	static private final List<String> tmp = new ArrayList<String>();

	public static String getSomeQuote() {
		synchronized (tmp) {
			if (tmp.size() == 0) {
				tmp.addAll(quotes);
				Collections.shuffle(tmp);
			}
			final int size = tmp.size();
			final String result = tmp.get(size - 1);
			tmp.remove(size - 1);
			return result;
		}
	}
}
