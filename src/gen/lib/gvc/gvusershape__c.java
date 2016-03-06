/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * Project Info:  http://plantuml.com
 * 
 * This file is part of Smetana.
 * Smetana is a partial translation of Graphviz/Dot sources from C to Java.
 *
 * (C) Copyright 2009-2017, Arnaud Roques
 *
 * This translation is distributed under the same Licence as the original C program:
 * 
 *************************************************************************
 * Copyright (c) 2011 AT&T Intellectual Property 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors: See CVS logs. Details at http://www.graphviz.org/
 *************************************************************************
 *
 * THE ACCOMPANYING PROGRAM IS PROVIDED UNDER THE TERMS OF THIS ECLIPSE PUBLIC
 * LICENSE ("AGREEMENT"). [Eclipse Public License - v 1.0]
 * 
 * ANY USE, REPRODUCTION OR DISTRIBUTION OF THE PROGRAM CONSTITUTES
 * RECIPIENT'S ACCEPTANCE OF THIS AGREEMENT.
 * 
 * You may obtain a copy of the License at
 * 
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package gen.lib.gvc;
import static smetana.core.Macro.UNSUPPORTED;

public class gvusershape__c {
//1 2digov3edok6d5srhgtlmrycs
// extern lt_symlist_t lt_preloaded_symbols[]


//1 baedz5i9est5csw3epz3cv7z
// typedef Ppoly_t Ppolyline_t


//1 9k44uhd5foylaeoekf3llonjq
// extern Dtmethod_t* 	Dtset


//1 1ahfywsmzcpcig2oxm7pt9ihj
// extern Dtmethod_t* 	Dtbag


//1 anhghfj3k7dmkudy2n7rvt31v
// extern Dtmethod_t* 	Dtoset


//1 5l6oj1ux946zjwvir94ykejbc
// extern Dtmethod_t* 	Dtobag


//1 2wtf222ak6cui8cfjnw6w377z
// extern Dtmethod_t*	Dtlist


//1 d1s1s6ibtcsmst88e3057u9r7
// extern Dtmethod_t*	Dtstack


//1 axa7mflo824p6fspjn1rdk0mt
// extern Dtmethod_t*	Dtqueue


//1 ega812utobm4xx9oa9w9ayij6
// extern Dtmethod_t*	Dtdeque


//1 cyfr996ur43045jv1tjbelzmj
// extern Dtmethod_t*	Dtorder


//1 wlofoiftbjgrrabzb2brkycg
// extern Dtmethod_t*	Dttree


//1 12bds94t7voj7ulwpcvgf6agr
// extern Dtmethod_t*	Dthash


//1 9lqknzty480cy7zsubmabkk8h
// extern Dtmethod_t	_Dttree


//1 bvn6zkbcp8vjdhkccqo1xrkrb
// extern Dtmethod_t	_Dthash


//1 9lidhtd6nsmmv3e7vjv9e10gw
// extern Dtmethod_t	_Dtlist


//1 34ujfamjxo7xn89u90oh2k6f8
// extern Dtmethod_t	_Dtqueue


//1 3jy4aceckzkdv950h89p4wjc8
// extern Dtmethod_t	_Dtstack


//1 8dfqgf3u1v830qzcjqh9o8ha7
// extern Agmemdisc_t AgMemDisc


//1 18k2oh2t6llfsdc5x0wlcnby8
// extern Agiddisc_t AgIdDisc


//1 a4r7hi80gdxtsv4hdoqpyiivn
// extern Agiodisc_t AgIoDisc


//1 bnzt5syjb7mgeru19114vd6xx
// extern Agdisc_t AgDefaultDisc


//1 35y2gbegsdjilegaribes00mg
// extern Agdesc_t Agdirected, Agstrictdirected, Agundirected,     Agstrictundirected


//1 c2rygslq6bcuka3awmvy2b3ow
// typedef Agsubnode_t	Agnoderef_t


//1 xam6yv0dcsx57dtg44igpbzn
// typedef Dtlink_t	Agedgeref_t


//1 9jp96pa73kseya3w6sulxzok6
// extern char *Gvimagepath


//1 c6f8whijgjwwagjigmxlwz3gb
// extern char *HTTPServerEnVar


//1 9brvabw9vk27d2nrq4p8tf971
// static Dict_t *ImageDict


//1 9pk2f9zg2k7nxk2pay6uw1ecz
// static knowntype_t knowntypes[] = 




//3 afsdkf8ez5ohl6dd2prle0bgt
// static int imagetype (usershape_t *us) 
public static Object imagetype(Object... arg) {
UNSUPPORTED("7vhzi3i3q0uu1byz03i34arpi"); // static int imagetype (usershape_t *us)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("4xkzi87btn54hoh901yqje0t2"); //     char header[20];
UNSUPPORTED("34btb8w1kvws7rrywxitht2dt"); //     char line[200];
UNSUPPORTED("b17di9c7wgtqm51bvsyxz6e2f"); //     int i;
UNSUPPORTED("ayo4w7poqzsosr46zq587rive"); //     if (us->f && fread(header, 1, 20, us->f) == 20) {
UNSUPPORTED("4f6p9x65zgxhi7g72c6rr85ez"); //         for (i = 0; i < sizeof(knowntypes) / sizeof(knowntype_t); i++) {
UNSUPPORTED("ken197schvdfnlj4tbd1ajsg"); // 	    if (!memcmp (header, knowntypes[i].template, knowntypes[i].size)) {
UNSUPPORTED("boxmes4yvhbhxz864uev9jrkn"); // 	        us->stringtype = knowntypes[i].stringtype;
UNSUPPORTED("ot0inunld2mj1q1v9ubb1meg"); // 		us->type = knowntypes[i].type;
UNSUPPORTED("itn78hlno7a8ug22ta41xds5"); // 		if (us->type == FT_XML) {
UNSUPPORTED("152itmr8zxm9bdyohwofdarej"); // 		    /* check for SVG in case of XML */
UNSUPPORTED("3hr9t6mkxbajyo66bbwwo671x"); // 		    while (fgets(line, sizeof(line), us->f) != (void *)0) {
UNSUPPORTED("3t6168qdbfqfv25fv7zx4xfms"); // 		        if (!memcmp(line, "<svg", sizeof("<svg")-1)) {
UNSUPPORTED("4n0qtaiukl38cr3i4ghl56e29"); //     			    us->stringtype = "svg";
UNSUPPORTED("ano6zabqu54wu0a40t35lj2q8"); // 			    return (us->type = FT_SVG);
UNSUPPORTED("3rk33w1xf0jaka2h033sgnx06"); // 		        }
UNSUPPORTED("dkxvw03k2gg9anv4dbze06axd"); // 		    }
UNSUPPORTED("6eq5kf0bj692bokt0bixy1ixh"); // 		}
UNSUPPORTED("f1kxll0fswekbe2gwilmen2m7"); // 	    	else if (us->type == FT_RIFF) {
UNSUPPORTED("cm5s3kh98e66k97v45ofiyv82"); // 		    /* check for WEBP in case of RIFF */
UNSUPPORTED("4ma0gwqxr9j5qek6xkrskevsl"); // 		    if (!memcmp(header+8, "WEBP", sizeof("WEBP")-1)) {
UNSUPPORTED("3zasc2uxnxlgetbuyifmr97bf"); //     			us->stringtype = "webp";
UNSUPPORTED("cgno6et27of87vw0o38rnarxx"); // 			return (us->type = FT_WEBP);
UNSUPPORTED("dkxvw03k2gg9anv4dbze06axd"); // 		    }
UNSUPPORTED("6eq5kf0bj692bokt0bixy1ixh"); // 		}
UNSUPPORTED("60vq78txdp1vk965f5jgtlboc"); // 		return us->type;
UNSUPPORTED("6t98dcecgbvbvtpycwiq2ynnj"); // 	    }
UNSUPPORTED("4mhlpjofolwivhm0tl8cxznly"); //         }
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("775efiysp2o60ingiihr8o6kq"); //     us->stringtype = "(lib)";
UNSUPPORTED("acr9g7qhtz1yhdxvbzfapnlad"); //     us->type = FT_NULL;
UNSUPPORTED("7yivuk2g5w938mbst7quo3jxa"); //     return FT_NULL;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 bnikn3k3s008dmhsa2qu6lh34
// static boolean get_int_lsb_first (FILE *f, unsigned int sz, unsigned int *val) 
public static Object get_int_lsb_first(Object... arg) {
UNSUPPORTED("cj03ffwnafffjxcznknyyp45b"); // static boolean get_int_lsb_first (FILE *f, unsigned int sz, unsigned int *val)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("70g3qubiio6uxhg2gxt141bff"); //     int ch, i;
UNSUPPORTED("f3wi4z18bhg0unx5i47wp28hb"); //     *val = 0;
UNSUPPORTED("6p1fgfnk4itt60e21fa48roiq"); //     for (i = 0; i < sz; i++) {
UNSUPPORTED("7zywfbf1jevwjemy5yzpfiiza"); // 	ch = fgetc(f);
UNSUPPORTED("1t7v5vi7ec6r3mhla23o1v3w2"); // 	if (feof(f))
UNSUPPORTED("6f1138i13x0xz1bf1thxgjgka"); // 	    return 0;
UNSUPPORTED("1ewc3hwvqjod1e2ou7c4u02h1"); // 	*val |= (ch << 8*i);
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("4si0cf97a5sfd9ozuunds9goz"); //     return (!(0));
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 87ifzytv1xgex4vf4ffa1fx28
// static boolean get_int_msb_first (FILE *f, unsigned int sz, unsigned int *val) 
public static Object get_int_msb_first(Object... arg) {
UNSUPPORTED("ad8pamcrkweeq9xmkgw9docv9"); // static boolean get_int_msb_first (FILE *f, unsigned int sz, unsigned int *val)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("70g3qubiio6uxhg2gxt141bff"); //     int ch, i;
UNSUPPORTED("f3wi4z18bhg0unx5i47wp28hb"); //     *val = 0;
UNSUPPORTED("6p1fgfnk4itt60e21fa48roiq"); //     for (i = 0; i < sz; i++) {
UNSUPPORTED("7zywfbf1jevwjemy5yzpfiiza"); // 	ch = fgetc(f);
UNSUPPORTED("1t7v5vi7ec6r3mhla23o1v3w2"); // 	if (feof(f))
UNSUPPORTED("6f1138i13x0xz1bf1thxgjgka"); // 	    return 0;
UNSUPPORTED("ac2fcmmhoviu6vu7od03xd2fz"); //         *val <<= 8;
UNSUPPORTED("2aeq31k5bdbmozogiex8thool"); // 	*val |= ch;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("4si0cf97a5sfd9ozuunds9goz"); //     return (!(0));
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 e6zw0qebmgir30wjyodre1w3q
// static unsigned int svg_units_convert(double n, char *u) 
public static Object svg_units_convert(Object... arg) {
UNSUPPORTED("1q167s10zcp2ohq3mqsaucn6q"); // static unsigned int svg_units_convert(double n, char *u)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("as83qujnkumxgvltowl9k84pa"); //     if (strcmp(u, "in") == 0)
UNSUPPORTED("cz9p5u3tzm0vtfcypzsqjjznz"); // 	return ROUND(n * 72);
UNSUPPORTED("a3ea8buut0466me8422yze7nk"); //     if (strcmp(u, "px") == 0)
UNSUPPORTED("17k340tdedf8kyw8jjau617ji"); //         return ROUND(n * 72 / 96);
UNSUPPORTED("e1voxws30sz7bauvt2nz3e43i"); //     if (strcmp(u, "pc") == 0)
UNSUPPORTED("dgfrr8ul58nbz256i7zg3dfnv"); //         return ROUND(n * 72 / 6); 
UNSUPPORTED("3zt4017at03z9nmfg4b6j33nl"); //     if (strcmp(u, "pt") == 0 || strcmp(u, "\"") == 0)   /* ugly!!  - if there are no inits then the %2s get the trailing '"' */
UNSUPPORTED("3bme0piy1kqu5q9y4eqbrr5fm"); //         return ROUND(n);
UNSUPPORTED("4kvfi7mn3m1cvs8748w6apqe6"); //     if (strcmp(u, "cm") == 0)
UNSUPPORTED("40fcy0h4yq1mhgc6sig41xkiz"); //         return ROUND(n * ((double)72 * 0.393700787));
UNSUPPORTED("795vmm17p8fultyx81btq2qj7"); //     if (strcmp(u, "mm") == 0)
UNSUPPORTED("2p58445vgby41tw5y37sb952f"); //         return ROUND(n * ((double)72 * 0.0393700787));
UNSUPPORTED("5oxhd3fvp0gfmrmz12vndnjt"); //     return 0;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}


//1 48cx4t8oc5776gxxsx3wj1459
// static char* svg_attr_value_re = 


//1 urpt4udx4umgv15x10ssv67m
// static regex_t re, *pre = (void *)0




//3 di4bxm03m9srcwmfu82mzqtu
// static void svg_size (usershape_t *us) 
public static Object svg_size(Object... arg) {
UNSUPPORTED("3bpommb3a197xaax86ebpnapc"); // static void svg_size (usershape_t *us)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("16qpk89sms5wm32xxhdjsrhj1"); //     unsigned int w = 0, h = 0;
UNSUPPORTED("3bfgwfzonzqutm2m3s241ggmq"); //     double n, x0, y0, x1, y1;
UNSUPPORTED("5urpczqwzlgz812axw5w07irp"); //     char u[10];
UNSUPPORTED("e7xkcvvhzn89z17vzxp5d9wqf"); //     char *attribute, *value, *re_string;
UNSUPPORTED("34btb8w1kvws7rrywxitht2dt"); //     char line[200];
UNSUPPORTED("1kfdqzk554dccavlk1sr8hc6k"); //     boolean wFlag = 0, hFlag = 0;
UNSUPPORTED("txcitt9410z35lsx2a1qr3zb"); //     regmatch_t re_pmatch[4];
UNSUPPORTED("2elnezsihato03euhhcjmczz6"); //     /* compile on first use */
UNSUPPORTED("b3qk4y1nm971zossf7ba67c39"); //     if (! pre) {
UNSUPPORTED("4k0rhf891tka3e5fomw7gygix"); //         if (regcomp(&re, svg_attr_value_re, REG_EXTENDED) != 0) {
UNSUPPORTED("bvbawrzsgnxy8a4de3iuwaeqb"); // 	    agerr(AGERR,"cannot compile regular expression %s", svg_attr_value_re);
UNSUPPORTED("klxoy56t7b20wxnwqm0qoofz"); //     	}
UNSUPPORTED("9let8u6jhsriwmr05bp3gwoy5"); // 	pre = &re;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("7iygf89yc0kwp5pocixawdaed"); //     fseek(us->f, 0, SEEK_SET);
UNSUPPORTED("40jwwwlrum66g8u94aha46ac6"); //     while (fgets(line, sizeof(line), us->f) != (void *)0 && (!wFlag || !hFlag)) {
UNSUPPORTED("1d8ii0hljelb18gmpwwrtvano"); // 	re_string = line;
UNSUPPORTED("5bh0ae9np7zl19gk8h3h9317e"); // 	while (regexec(&re, re_string, 4, re_pmatch, 0) == 0) {
UNSUPPORTED("b355fkvcamz9jmg27def9b8x3"); // 	    re_string[re_pmatch[1].rm_eo] = '\0';
UNSUPPORTED("aef3m3p3zi96h0zt0sjw03lj4"); // 	    re_string[re_pmatch[2].rm_eo] = '\0';
UNSUPPORTED("eipkd5535u39a7ub3xbstkeu2"); // 	    attribute = re_string + re_pmatch[1].rm_so;
UNSUPPORTED("bqe4861o77yhd2c4hm2q9ryqg"); // 	    value = re_string + re_pmatch[2].rm_so;
UNSUPPORTED("c9y0ykkvqmbho7jwq1hq2v8rg"); //     	    re_string += re_pmatch[0].rm_eo + 1;
UNSUPPORTED("4x0vivqfmvb5kxu3yv8zlj0z8"); // 	    if (strcmp(attribute,"width") == 0) {
UNSUPPORTED("cjoilrknjlp3580a79aeduafs"); // 	        if (sscanf(value, "%lf%2s", &n, u) == 2) {
UNSUPPORTED("5804xn01sb0nsqc4oand73j71"); // 	            w = svg_units_convert(n, u);
UNSUPPORTED("boxr36cof383w0qjpos68wr3p"); // 	            wFlag = (!(0));
UNSUPPORTED("6eq5kf0bj692bokt0bixy1ixh"); // 		}
UNSUPPORTED("coem1appm7al4by2xogxcf79v"); // 		else if (sscanf(value, "%lf", &n) == 1) {
UNSUPPORTED("d20bsvecsbla4zfrgr8vs0axk"); // 	            w = svg_units_convert(n, "pt");
UNSUPPORTED("boxr36cof383w0qjpos68wr3p"); // 	            wFlag = (!(0));
UNSUPPORTED("6eq5kf0bj692bokt0bixy1ixh"); // 		}
UNSUPPORTED("bokpcg0aj7i7sody2nk55sn1m"); // 		if (hFlag)
UNSUPPORTED("czyohktf9bkx4udfqhx42f4lu"); // 		    break;
UNSUPPORTED("6t98dcecgbvbvtpycwiq2ynnj"); // 	    }
UNSUPPORTED("349wmcj9pjfzzogsgqytbecep"); // 	    else if (strcmp(attribute,"height") == 0) {
UNSUPPORTED("cjoilrknjlp3580a79aeduafs"); // 	        if (sscanf(value, "%lf%2s", &n, u) == 2) {
UNSUPPORTED("4e8rpne5p90nqhwswzkila8du"); // 	            h = svg_units_convert(n, u);
UNSUPPORTED("75lsnpofiu7aq6tvk1bytezfx"); // 	            hFlag = (!(0));
UNSUPPORTED("6eq5kf0bj692bokt0bixy1ixh"); // 		}
UNSUPPORTED("318wbwzbtyt293618awt8oi7j"); // 	        else if (sscanf(value, "%lf", &n) == 1) {
UNSUPPORTED("8roq2ozo7ghuukkquxg4vi3o8"); // 	            h = svg_units_convert(n, "pt");
UNSUPPORTED("75lsnpofiu7aq6tvk1bytezfx"); // 	            hFlag = (!(0));
UNSUPPORTED("6eq5kf0bj692bokt0bixy1ixh"); // 		}
UNSUPPORTED("3f72eveov59pnntffs33fh1o9"); //                 if (wFlag)
UNSUPPORTED("czyohktf9bkx4udfqhx42f4lu"); // 		    break;
UNSUPPORTED("6t98dcecgbvbvtpycwiq2ynnj"); // 	    }
UNSUPPORTED("evrgwvbtefbhi8c4vpurqyncz"); // 	    else if (strcmp(attribute,"viewBox") == 0
UNSUPPORTED("7knynrz6icv4twtp5re2u2tyu"); // 	      && sscanf(value, "%lf %lf %lf %lf", &x0,&y0,&x1,&y1) == 4) {
UNSUPPORTED("b75jl7dtq9qf4ike38i53sb5b"); // 		w = x1 - x0 + 1;
UNSUPPORTED("ccjsgjzpqovbgplagso2d6wvk"); // 		h = y1 - y0 + 1;
UNSUPPORTED("agh2p3816w463hvgmt7pji00p"); // 	        wFlag = (!(0));
UNSUPPORTED("b0kbr8vydi49ibjics3e3mlip"); // 	        hFlag = (!(0));
UNSUPPORTED("7f9nr53m374zf5jnncru7yfn6"); // 	        break;
UNSUPPORTED("6t98dcecgbvbvtpycwiq2ynnj"); // 	    }
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("7337vyphkrv1lekq0dncy8j6s"); //     us->dpi = 0;
UNSUPPORTED("avyq4e4me4o2pteunxjmfehds"); //     us->w = w;
UNSUPPORTED("evkuhplkb5xfea6ln3h8f6ly2"); //     us->h = h;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 2phgl4coa8enkvltpf0938lit
// static void png_size (usershape_t *us) 
public static Object png_size(Object... arg) {
UNSUPPORTED("5qra9bwfkjw5syv0odudm6gam"); // static void png_size (usershape_t *us)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("8l3f9beos057zjxgk0cdj323o"); //     unsigned int w, h;
UNSUPPORTED("7337vyphkrv1lekq0dncy8j6s"); //     us->dpi = 0;
UNSUPPORTED("3135bp24afhqdzs2uj5l7sjr1"); //     fseek(us->f, 16, SEEK_SET);
UNSUPPORTED("1luxqut1mknrn8qp0yakx9czw"); //     if (get_int_msb_first(us->f, 4, &w) && get_int_msb_first(us->f, 4, &h)) {
UNSUPPORTED("73axoa9apyrx2829eqsezmfik"); //         us->w = w;
UNSUPPORTED("993qcme7jlmcsi918hu491ld5"); //         us->h = h;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 lqow6tkjqnnbqz6msa6cmzlk
// static void ico_size (usershape_t *us) 
public static Object ico_size(Object... arg) {
UNSUPPORTED("6eqck7knu0bxd3sib3fpca2hf"); // static void ico_size (usershape_t *us)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("8l3f9beos057zjxgk0cdj323o"); //     unsigned int w, h;
UNSUPPORTED("7337vyphkrv1lekq0dncy8j6s"); //     us->dpi = 0;
UNSUPPORTED("bunso4je5hrb29m4984fw7bh6"); //     fseek(us->f, 6, SEEK_SET);
UNSUPPORTED("875b5zt62o9flpi731becymv4"); //     if (get_int_msb_first(us->f, 1, &w) && get_int_msb_first(us->f, 1, &h)) {
UNSUPPORTED("73axoa9apyrx2829eqsezmfik"); //         us->w = w;
UNSUPPORTED("993qcme7jlmcsi918hu491ld5"); //         us->h = h;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 dleljswqxf1oa4gzr8l8fomcl
// static void webp_size (usershape_t *us) 
public static Object webp_size(Object... arg) {
UNSUPPORTED("dl59ajg8cu2fj1vuagj6pumce"); // static void webp_size (usershape_t *us)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("8l3f9beos057zjxgk0cdj323o"); //     unsigned int w, h;
UNSUPPORTED("7337vyphkrv1lekq0dncy8j6s"); //     us->dpi = 0;
UNSUPPORTED("3mt62dnri0kt6p9g6nmf6x78i"); //     fseek(us->f, 15, SEEK_SET);
UNSUPPORTED("bro476e5tdpqu4kqzjhadepr8"); //     if (fgetc(us->f) == 'X') { //VP8X
UNSUPPORTED("ciyvs7xrf1sc336ipavk7gjbs"); //         fseek(us->f, 24, SEEK_SET);
UNSUPPORTED("77gjxe5g6u7c6371ziwedefxf"); //         if (get_int_lsb_first(us->f, 4, &w) && get_int_lsb_first(us->f, 4, &h)) {
UNSUPPORTED("9htwijlhm4ad9uo70xn5d5tvl"); //             us->w = w;
UNSUPPORTED("3hh8nl3qystuq8wcwsm411fej"); //             us->h = h;
UNSUPPORTED("4mhlpjofolwivhm0tl8cxznly"); //         }
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("bwt2eurukj3m7fsbr441rf1pj"); //     else { //VP8
UNSUPPORTED("5fthwx7kwgddogs4n4fsbb0o8"); //         fseek(us->f, 26, SEEK_SET);
UNSUPPORTED("621fvdyd12vnbfyxpw6j0z7cr"); //         if (get_int_lsb_first(us->f, 2, &w) && get_int_lsb_first(us->f, 2, &h)) {
UNSUPPORTED("9htwijlhm4ad9uo70xn5d5tvl"); //             us->w = w;
UNSUPPORTED("3hh8nl3qystuq8wcwsm411fej"); //             us->h = h;
UNSUPPORTED("4mhlpjofolwivhm0tl8cxznly"); //         }
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 2s3ms72agp5gyfqz15zbpp3u1
// static void gif_size (usershape_t *us) 
public static Object gif_size(Object... arg) {
UNSUPPORTED("ac8be5d24wrrx24tnddiukvl7"); // static void gif_size (usershape_t *us)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("8l3f9beos057zjxgk0cdj323o"); //     unsigned int w, h;
UNSUPPORTED("7337vyphkrv1lekq0dncy8j6s"); //     us->dpi = 0;
UNSUPPORTED("bunso4je5hrb29m4984fw7bh6"); //     fseek(us->f, 6, SEEK_SET);
UNSUPPORTED("ezkdvuggkyj11n6ca6xrhm72g"); //     if (get_int_lsb_first(us->f, 2, &w) && get_int_lsb_first(us->f, 2, &h)) {
UNSUPPORTED("73axoa9apyrx2829eqsezmfik"); //         us->w = w;
UNSUPPORTED("993qcme7jlmcsi918hu491ld5"); //         us->h = h;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 bpjeya6em7jp6m6l86m6t4u4a
// static void bmp_size (usershape_t *us) 
public static Object bmp_size(Object... arg) {
UNSUPPORTED("ba2pylwi2xu10asnonw92pxw1"); // static void bmp_size (usershape_t *us) {
UNSUPPORTED("ct1272vz7j34sqkcwzdiq2x0i"); //     unsigned int size_x_msw, size_x_lsw, size_y_msw, size_y_lsw;
UNSUPPORTED("7337vyphkrv1lekq0dncy8j6s"); //     us->dpi = 0;
UNSUPPORTED("9b418veiss4t0o7s8cqq28owo"); //     fseek (us->f, 16, SEEK_SET);
UNSUPPORTED("1q9kmvi2ajod9wlev8s5p50rv"); //     if ( get_int_lsb_first (us->f, 2, &size_x_msw) &&
UNSUPPORTED("5f5k6cp1ota9f8a2ciircybdh"); //          get_int_lsb_first (us->f, 2, &size_x_lsw) &&
UNSUPPORTED("9n6cqmhzmbkf1vt9fyu6zw0kp"); //          get_int_lsb_first (us->f, 2, &size_y_msw) &&
UNSUPPORTED("cid929pl3ogyk2kecod7opvz0"); //          get_int_lsb_first (us->f, 2, &size_y_lsw) ) {
UNSUPPORTED("a6g25f4k8ew6jja5m9rnccf5o"); //         us->w = size_x_msw << 16 | size_x_lsw;
UNSUPPORTED("8exqj264llut5d629ydwne5k2"); //         us->h = size_y_msw << 16 | size_y_lsw;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 c7yvo5wd816hjqxejnluaol0a
// static void jpeg_size (usershape_t *us) 
public static Object jpeg_size(Object... arg) {
UNSUPPORTED("c8uakwctlakoj06f07juq8r2a"); // static void jpeg_size (usershape_t *us) {
UNSUPPORTED("3o57tczyjy6z9uvry23b9xkw5"); //     unsigned int marker, length, size_x, size_y, junk;
UNSUPPORTED("4ylph99ph3r2v7uhxyd2hq0io"); //     /* These are the markers that follow 0xff in the file.
UNSUPPORTED("1pzctl0feh1bhiqnxtmawvxg4"); //      * Other markers implicitly have a 2-byte length field that follows.
UNSUPPORTED("795vpnc8yojryr8b46aidsu69"); //      */
UNSUPPORTED("1lgv3mem6buwpyuef4b9h8rzp"); //     static unsigned char standalone_markers [] = {
UNSUPPORTED("e00djw6cjtdaxejgnewp8nus6"); //         0x01,                       /* Temporary */
UNSUPPORTED("1urwngq6m3z6m5mr3egwgq9mr"); //         0xd0, 0xd1, 0xd2, 0xd3,     /* Reset */
UNSUPPORTED("e1vwc4842h5c84ojr8w3onuno"); //             0xd4, 0xd5, 0xd6,
UNSUPPORTED("1uig2kz9nbo8j6vj3r7493n8"); //             0xd7,
UNSUPPORTED("927bzm317ui2xj8ugbwhm5xb7"); //         0xd8,                       /* Start of image */
UNSUPPORTED("9ybi9mcsggfw3zbh6zn9hotef"); //         0xd9,                       /* End of image */
UNSUPPORTED("fbagrxdnmyjkvt7zaukczscg"); //         0
UNSUPPORTED("9u8qqu9tw95qjbv3cxv3hj4bd"); //     };
UNSUPPORTED("7337vyphkrv1lekq0dncy8j6s"); //     us->dpi = 0;
UNSUPPORTED("5owjshc6gmd9o9lmekzcl6rvg"); //     while ((!(0))) {
UNSUPPORTED("7kq4b2hkhf3vcx97hwoelgcxz"); //         /* Now we must be at a 0xff or at a series of 0xff's.
UNSUPPORTED("cga1geam6con9djkcofzmw0ko"); //          * If that is not the case, or if we're at EOF, then there's
UNSUPPORTED("exr8s1ongonmgsucjdf4zsmb4"); //          * a parsing error.
UNSUPPORTED("3vesx4cskuo1q42jvwmoum2xs"); //          */
UNSUPPORTED("79jgtuw1xg9dgik49e3hx1068"); //         if (! get_int_msb_first (us->f, 1, &marker))
UNSUPPORTED("3zyp4vhegf9dl2g8a4ffmxah8"); //             return;
UNSUPPORTED("e7cevpg68ih107a2ggfgkq1la"); //         if (marker == 0xff)
UNSUPPORTED("9j8yk6fxm4tnpr3yrev0f9bhm"); //             continue;
UNSUPPORTED("99hdbrpbwvb3lwfyhz43xnoxf"); //         /* Ok.. marker now read. If it is not a stand-alone marker,
UNSUPPORTED("b7dntxmllxk3wzmg9mclxs71r"); //          * then continue. If it's a Start Of Frame (0xc?), then we're there.
UNSUPPORTED("8aramua11q89c43osky6tgha5"); //          * If it's another marker with a length field, then skip ahead
UNSUPPORTED("5w0fy7m3152rri519xzsydb2w"); //          * over that length field.
UNSUPPORTED("3vesx4cskuo1q42jvwmoum2xs"); //          */
UNSUPPORTED("7itrbaa4e2u9uh86wydx2r1wq"); //         /* A stand-alone... */
UNSUPPORTED("7m1dttkv67ixwryn1ukl94hel"); //         if (strchr ((char*)standalone_markers, marker))
UNSUPPORTED("9j8yk6fxm4tnpr3yrev0f9bhm"); //             continue;
UNSUPPORTED("sb3rjk9k59soh7qvm0qpkzoh"); //         /* Incase of a 0xc0 marker: */
UNSUPPORTED("6u3lv1x78jskc1qjam25s00h1"); //         if (marker == 0xc0) {
UNSUPPORTED("4kucipi8uw6ujeuh489csoowf"); //             /* Skip length and 2 lengths. */
UNSUPPORTED("30gcyw02iuybxoyj28nhlbwbl"); //             if ( get_int_msb_first (us->f, 3, &junk)   &&
UNSUPPORTED("b6ypup1v81rta7mfh68arftfd"); //                  get_int_msb_first (us->f, 2, &size_x) &&
UNSUPPORTED("f2mbg2ezlyocn5228wkhy658f"); //                  get_int_msb_first (us->f, 2, &size_y) ) {
UNSUPPORTED("eps17wh894vxn0oojbitsn1lr"); //             /* Store length. */
UNSUPPORTED("danamcguxf0st13b0euh47y8v"); //                 us->h = size_x;
UNSUPPORTED("gva1bl1ygb6xzrrjwpma3dpp"); //                 us->w = size_y;
UNSUPPORTED("7g94ubxa48a1yi3mf9v521b7c"); //             }
UNSUPPORTED("6cprbghvenu9ldc0ez1ifc63q"); // 	    return;
UNSUPPORTED("4mhlpjofolwivhm0tl8cxznly"); //         }
UNSUPPORTED("7jc4v9i136pgfvwakfui3aupm"); //         /* Incase of a 0xc2 marker: */
UNSUPPORTED("a1ww3q31w3xsq697vt32phj9d"); //         if (marker == 0xc2) {
UNSUPPORTED("7xwfuqyvolr45lp6gdcnmqyna"); //             /* Skip length and one more byte */
UNSUPPORTED("dhyz02dvhk6z01p9nh8knpdnp"); //             if (! get_int_msb_first (us->f, 3, &junk))
UNSUPPORTED("6an8ocqq0sjru42k4aathe94m"); //                 return;
UNSUPPORTED("7b106vc3jzqz2eyjlo758xw2n"); //             /* Get length and store. */
UNSUPPORTED("akbj7dadm0nq5ujmuwiw50ukl"); //             if ( get_int_msb_first (us->f, 2, &size_x) &&
UNSUPPORTED("f2mbg2ezlyocn5228wkhy658f"); //                  get_int_msb_first (us->f, 2, &size_y) ) {
UNSUPPORTED("danamcguxf0st13b0euh47y8v"); //                 us->h = size_x;
UNSUPPORTED("gva1bl1ygb6xzrrjwpma3dpp"); //                 us->w = size_y;
UNSUPPORTED("7g94ubxa48a1yi3mf9v521b7c"); //             }
UNSUPPORTED("6cprbghvenu9ldc0ez1ifc63q"); // 	    return;
UNSUPPORTED("4mhlpjofolwivhm0tl8cxznly"); //         }
UNSUPPORTED("9qz6tc2665rtirz7egtertjyl"); //         /* Any other marker is assumed to be followed by 2 bytes length. */
UNSUPPORTED("5xvajayqclvcxxqkozzorzbdi"); //         if (! get_int_msb_first (us->f, 2, &length))
UNSUPPORTED("3zyp4vhegf9dl2g8a4ffmxah8"); //             return;
UNSUPPORTED("cv5sg5k6hy1kwgiinb4i5xr6"); //         fseek (us->f, length - 2, SEEK_CUR);
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 9vi0gtfjnp35ie6bx15grm4gp
// static void ps_size (usershape_t *us) 
public static Object ps_size(Object... arg) {
UNSUPPORTED("8kctdt9asc26knxxydik2v8ug"); // static void ps_size (usershape_t *us)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("18vi5vqd0r4k0kxsl3h3rwa4y"); //     char line[BUFSIZ];
UNSUPPORTED("4qk2wsv83bxm1lchsnd6splfr"); //     boolean saw_bb;
UNSUPPORTED("5rcgzsa5rlhtyjz964tcthts2"); //     int lx, ly, ux, uy;
UNSUPPORTED("5fewhg0dt0vbugqk2bmyfr34l"); //     char* linep;
UNSUPPORTED("891wob6d9fi1zsdfvumw7tx5y"); //     us->dpi = 72;
UNSUPPORTED("7iygf89yc0kwp5pocixawdaed"); //     fseek(us->f, 0, SEEK_SET);
UNSUPPORTED("w6asn9e9wasakvoc3kn19lwt"); //     saw_bb = 0;
UNSUPPORTED("cpypiyiyiq875wcq5pix66en2"); //     while (fgets(line, sizeof(line), us->f)) {
UNSUPPORTED("7vmkqwijdn7elebqrq3q3qqlv"); // 	/* PostScript accepts \r as EOL, so using fgets () and looking for a
UNSUPPORTED("ccqysd4ji4ezs2u937a1tq93k"); // 	 * bounding box comment at the beginning doesn't work in this case. 
UNSUPPORTED("a548nncd0y8n2a01c5z3akmhg"); // 	 * As a heuristic, we first search for a bounding box comment in line.
UNSUPPORTED("djrf0gim2rh71i0skg41uw6zn"); // 	 * This obviously fails if not all of the numbers make it into the
UNSUPPORTED("x3dg0lfzyp7yc2hldpo3cbge"); // 	 * current buffer. This shouldn't be a problem, as the comment is
UNSUPPORTED("4uvbwtwj71pylu4bfe3pu79le"); // 	 * typically near the beginning, and so should be read within the first
UNSUPPORTED("426auswwt3iw0kmtesztdy9ff"); // 	 * BUFSIZ bytes (even on Windows where this is 512).
UNSUPPORTED("62wb43w2xc6ex6hootjubbx22"); // 	 */
UNSUPPORTED("8iwr2d3ooqy0tw5eisixpectn"); // 	if (!(linep = strstr (line, "%%BoundingBox:")))
UNSUPPORTED("6hqli9m8yickz1ox1qfgtdbnd"); // 	    continue;
UNSUPPORTED("2s519tr2mhoj177f79vzllnyq"); //         if (sscanf (linep, "%%%%BoundingBox: %d %d %d %d", &lx, &ly, &ux, &uy) == 4) {
UNSUPPORTED("6dekl3sp6brld4uq5ep5859l3"); //             saw_bb = (!(0));
UNSUPPORTED("ai3czg6gaaxspsmndknpyvuiu"); // 	    break;
UNSUPPORTED("4mhlpjofolwivhm0tl8cxznly"); //         }
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("8p3jjm43yrzygpor71hdkfs1g"); //     if (saw_bb) {
UNSUPPORTED("5dym7sjpxsgdgav57jvyuohe8"); // 	us->x = lx;
UNSUPPORTED("da04q03gf0w4upkkrsn74w1dc"); // 	us->y = ly;
UNSUPPORTED("6mpj1u4n7cyggpme1vsa6je8t"); //         us->w = ux - lx;
UNSUPPORTED("dqnhfatraydhv6fdx3gsi22ao"); //         us->h = uy - ly;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 87i5xtm6lbxo4asqfnu0v9rz1
// static unsigned char nxtc (stream_t* str) 
public static Object nxtc(Object... arg) {
UNSUPPORTED("at0aua2ntxsp0j1h4yidmr4si"); // static unsigned char
UNSUPPORTED("9rrtdlhn8kugpol3lky8rgp5n"); // nxtc (stream_t* str)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("9jyosg56ecd165ua22kq89ggj"); //     if (fgets(str->buf, BUFSIZ, str->fp)) {
UNSUPPORTED("bo88dnmxymbe4e1rppc0ex9ve"); // 	str->s = str->buf;
UNSUPPORTED("8tdhptj2ed5dv8sew624q4jec"); // 	return *(str->s);
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("278hcy3fayv5vclqcgtpjbzdi"); //     return '\0';
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 2fq3qaeuu4gjr89xmwwy7i4xe
// static void skipWS (stream_t* str) 
public static Object skipWS(Object... arg) {
UNSUPPORTED("e2z2o5ybnr5tgpkt8ty7hwan1"); // static void
UNSUPPORTED("cactz8998uk8ou1c5zwtwg601"); // skipWS (stream_t* str)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("10sir32iwi5l2jyfgp65pihto"); //     unsigned char c;
UNSUPPORTED("91zm214vvm6477gtei1ggjkef"); //     while ((c = (*(str->s)?*(str->s):nxtc(str)))) {
UNSUPPORTED("dilzs0pemy12rvmpnko0iel59"); // 	if (isspace(c)) (str->s++);
UNSUPPORTED("9wafzg86cpce49qfuv8wsl6in"); // 	else return;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 dwmuhk2xjfndhxozxfrdz5nk8
// static int scanNum (char* tok, double* dp) 
public static Object scanNum(Object... arg) {
UNSUPPORTED("eyp5xkiyummcoc88ul2b6tkeg"); // static int
UNSUPPORTED("eq0ltrbhzwt422rffdlm4d20e"); // scanNum (char* tok, double* dp)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("bkz9mqmemp1ljxdwdbu8xv3e9"); //     char* endp;
UNSUPPORTED("avrsd77b596s5g291ozzdpxf8"); //     double d = strtod(tok, &endp);
UNSUPPORTED("2qoq2nf2jpfoyc68njwm2ajkq"); //     if (tok == endp) return 1;
UNSUPPORTED("636uh4i1x8w844yxwd2u322d1"); //     *dp = d;
UNSUPPORTED("5oxhd3fvp0gfmrmz12vndnjt"); //     return 0;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 dhjmc8hrpmiohrejyu72mrhno
// static void getNum (stream_t* str, char* buf) 
public static Object getNum(Object... arg) {
UNSUPPORTED("e2z2o5ybnr5tgpkt8ty7hwan1"); // static void
UNSUPPORTED("doymhclp9a2fukna3vd3r7xf8"); // getNum (stream_t* str, char* buf)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("bsj3r19ko668lwj5fqk8tlbhc"); //     int len = 0;
UNSUPPORTED("wrvu9u7a8j6i6y6552zncxfk"); //     char c;
UNSUPPORTED("936voey3mecmlo6hla9hyrmun"); //     skipWS(str);
UNSUPPORTED("tuyymjn2lfs038e1rm6hbw8w"); //     while ((c = (*(str->s)?*(str->s):nxtc(str))) && (isdigit(c) || (c == '.'))) {
UNSUPPORTED("4rvasxp4ert1zz5jaasrqeft1"); // 	buf[len++] = c;
UNSUPPORTED("8vm8y5ewr5ra87d4f8exah65d"); // 	(str->s++);
UNSUPPORTED("5zs9wc8ip6zkde4o33aadh30c"); // 	if (len == BUFSIZ-1) break;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("3w01p3l63zt07scumxz12foyc"); //     buf[len] = '\0';
UNSUPPORTED("b9185t6i77ez1ac587ul8ndnc"); //     return;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 6kdhce4shbkw92wtfgufyqecs
// static int boxof (stream_t* str, boxf* bp) 
public static Object boxof(Object... arg) {
UNSUPPORTED("eyp5xkiyummcoc88ul2b6tkeg"); // static int
UNSUPPORTED("d810hoc5e8uu0odpm097odpfu"); // boxof (stream_t* str, boxf* bp)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("1z7b1t0srtne632sd3ma9jbp7"); // 	char tok[BUFSIZ];
UNSUPPORTED("araz8ffm0d5v424qzgyh7bsvy"); // 	skipWS(str);
UNSUPPORTED("1uwfhdftjs4loqhg7ecaermjd"); // 	if ((*(str->s)?*(str->s):nxtc(str)) != '[') return 1;
UNSUPPORTED("8vm8y5ewr5ra87d4f8exah65d"); // 	(str->s++);
UNSUPPORTED("6jh13u8w5qiy4ij2ebx6zmqwa"); // 	getNum(str, tok);
UNSUPPORTED("1m5wgiebygd9j5o6m68etvwh0"); // 	if (scanNum(tok,&bp->LL.x)) return 1;
UNSUPPORTED("6jh13u8w5qiy4ij2ebx6zmqwa"); // 	getNum(str, tok);
UNSUPPORTED("9bcg5vhxd4fqd8ykkbivfm3d4"); // 	if (scanNum(tok,&bp->LL.y)) return 1;
UNSUPPORTED("6jh13u8w5qiy4ij2ebx6zmqwa"); // 	getNum(str, tok);
UNSUPPORTED("1z0q9nrhj393zbs1mok3h84tf"); // 	if (scanNum(tok,&bp->UR.x)) return 1;
UNSUPPORTED("6jh13u8w5qiy4ij2ebx6zmqwa"); // 	getNum(str, tok);
UNSUPPORTED("247082ftkwl5jzjorvyt3sgcl"); // 	if (scanNum(tok,&bp->UR.y)) return 1;
UNSUPPORTED("c9ckhc8veujmwcw0ar3u3zld4"); // 	return 0;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 dedr09vl6y9ax357arh6tayi
// static int bboxPDF (FILE* fp, boxf* bp) 
public static Object bboxPDF(Object... arg) {
UNSUPPORTED("eyp5xkiyummcoc88ul2b6tkeg"); // static int
UNSUPPORTED("9icxkxwqm6flp3sl9ulkp4fyd"); // bboxPDF (FILE* fp, boxf* bp)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("f3gl6562biaxa4139uus0d370"); // 	stream_t str;
UNSUPPORTED("5pjh3c3u4lxh1jdyyk966intb"); // 	char* s;
UNSUPPORTED("8c7x8di5w36ib05qan9z4sl9"); // 	char buf[BUFSIZ];
UNSUPPORTED("8w39jbbwqjx63enrf5fdvcm9r"); // 	while (fgets(buf, BUFSIZ, fp)) {
UNSUPPORTED("b7wioadlc5j3p2q8w7tsq1gam"); // 		if ((s = strstr(buf,"/MediaBox"))) {
UNSUPPORTED("ecmojjd7fnaeah045va2t5eg8"); // 			str.buf = buf;
UNSUPPORTED("5k89yh945d2crbir8397vkavv"); // 			str.s = s+(sizeof("/MediaBox")-1);
UNSUPPORTED("dj08qw9n5j8nlqx752hipu46u"); // 			str.fp = fp;
UNSUPPORTED("uhua7rc3vef3nawzjyh3w1k5"); // 			return boxof(&str,bp);
UNSUPPORTED("yoqd73ulpi5tn8snro7dt3jc"); // 		} 
UNSUPPORTED("8nzcpbtoi924xzu8ze3z6dbft"); // 	} 
UNSUPPORTED("eleqpc2p2r3hvma6tipoy7tr"); // 	return 1;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 3fdvwmb09jpyu6h40pj206vv3
// static void pdf_size (usershape_t *us) 
public static Object pdf_size(Object... arg) {
UNSUPPORTED("97swmsrhwped1e1n72fyev1ai"); // static void pdf_size (usershape_t *us)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("2lzsl1e035wt5epd1h8f4bn8m"); //     boxf bb;
UNSUPPORTED("7337vyphkrv1lekq0dncy8j6s"); //     us->dpi = 0;
UNSUPPORTED("7iygf89yc0kwp5pocixawdaed"); //     fseek(us->f, 0, SEEK_SET);
UNSUPPORTED("cu1kld68jk8qm12c9f0n61vyd"); //     if ( ! bboxPDF (us->f, &bb)) {
UNSUPPORTED("14kgzu9x57imt127uyibumhif"); // 	us->x = bb.LL.x;
UNSUPPORTED("51rq1d6stvtvjgfta18hxoiyg"); // 	us->y = bb.LL.y;
UNSUPPORTED("encm75ymc6wgridj70fk0jf47"); //         us->w = bb.UR.x - bb.LL.x;
UNSUPPORTED("dy52i81burd516zy265e83wel"); //         us->h = bb.UR.y - bb.LL.y;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 cxhbj2es2sky90cnd8qf89prs
// static void usershape_close (Dict_t * dict, void * p, Dtdisc_t * disc) 
public static Object usershape_close(Object... arg) {
UNSUPPORTED("b7or8sf6zjmnj6wjf08brm0r"); // static void usershape_close (Dict_t * dict, void * p, Dtdisc_t * disc)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("58pwyr0d4numnoai5v987l5ml"); //     usershape_t *us = (usershape_t *)p;
UNSUPPORTED("ad2wgtudijnm3bp4549lrva3l"); //     if (us->f)
UNSUPPORTED("13i1v3mxl0sjnx4rgaden880d"); // 	fclose(us->f);
UNSUPPORTED("e5bs4fy1a1v5ptm2bcrm2m9ai"); //     if (us->data && us->datafree)
UNSUPPORTED("1wod8ffcoluizmoc72db2htxb"); // 	us->datafree(us);
UNSUPPORTED("73p2w162ujnuoboekx5udlion"); //     free (us);
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}


//1 es7jtx4lx7bg26412fcjit8ri
// static Dtdisc_t ImageDictDisc = 




//3 9msk7qh9auq8w21r32k0ffcsa
// usershape_t *gvusershape_find(char *name) 
public static Object gvusershape_find(Object... arg) {
UNSUPPORTED("a9366bkr4gt4nvcj5bnkjrtrd"); // usershape_t *gvusershape_find(char *name)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("exneoozy5g8al0a8y6fxb88zv"); //     usershape_t *us;
UNSUPPORTED("265kxn69043hh3vmr1ma8pbpg"); //     assert(name);
UNSUPPORTED("1av8we70pcc0ni7489zk2ttcg"); //     assert(name[0]);
UNSUPPORTED("4qhyd93m9v57fwb1anljrxfd3"); //     if (!ImageDict)
UNSUPPORTED("45tfw7tcm68298aro2tdiv8pc"); // 	return (void *)0;
UNSUPPORTED("dzt5b4nn4x2oidxk4g4wzt8et"); //     us = (*(((Dt_t*)(ImageDict))->searchf))((ImageDict),(void*)(name),0001000);
UNSUPPORTED("2iqrgjc97xskxvw6791yf0sl7"); //     return us;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 3cgay03dg8pq23kln9kuc0ym5
// boolean gvusershape_file_access(usershape_t *us) 
public static Object gvusershape_file_access(Object... arg) {
UNSUPPORTED("dm8po5i2hu4arliyfc2ea2a47"); // boolean gvusershape_file_access(usershape_t *us)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("8u0o5i27i1no60qjs9r4os0f9"); //     static int usershape_files_open_cnt;
UNSUPPORTED("vadeh8lzzwz0ugk6azcwaayu"); //     const char *fn;
UNSUPPORTED("7gqi0ait1bhc4v9zl8rnn0wbs"); //     assert(us);
UNSUPPORTED("xlffgd4fqdckxg2nbs0mxtlj"); //     assert(us->name);
UNSUPPORTED("fk4kdjctgb13swd2h14t0yvh"); //     assert(us->name[0]);
UNSUPPORTED("ad2wgtudijnm3bp4549lrva3l"); //     if (us->f)
UNSUPPORTED("1te90msr6mhllwn4y3ghlahc0"); // 	fseek(us->f, 0, SEEK_SET);
UNSUPPORTED("1nyzbeonram6636b1w955bypn"); //     else {
UNSUPPORTED("d6n6b94e02ntmefeqcoo0ueon"); //         if (! (fn = safefile(us->name))) {
UNSUPPORTED("778vwup2lml5qa9eyxd88416u"); // 	    agerr(AGWARN, "Filename \"%s\" is unsafe\n", us->name);
UNSUPPORTED("6f1138i13x0xz1bf1thxgjgka"); // 	    return 0;
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("31rnydpzhmzclqs3q4flw9ce2"); // 	us->f = fopen(fn, "r");
UNSUPPORTED("cmb2imwpnm4tgu1dm9r5tgtj5"); // 	if (us->f == (void *)0) {
UNSUPPORTED("ebzon8h2oywxvasmfmyhnw5pq"); // 	    agerr(AGWARN, "%s while opening %s\n", strerror(errno), fn);
UNSUPPORTED("6f1138i13x0xz1bf1thxgjgka"); // 	    return 0;
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("e17a7n92m8ieuby8sa8jm1r3o"); // 	if (usershape_files_open_cnt >= 50)
UNSUPPORTED("c9jo9gbhp1on7wctnk1o7q8vu"); // 	    us->nocache = (!(0));
UNSUPPORTED("9352ql3e58qs4fzapgjfrms2s"); // 	else
UNSUPPORTED("81eb64f0supcvkt4drj4djmby"); // 	    usershape_files_open_cnt++;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("3ebwn2weruwgdz4tfaxj2htek"); //     assert(us->f);
UNSUPPORTED("4si0cf97a5sfd9ozuunds9goz"); //     return (!(0));
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 9y4wcbk4jp03lbul2i25xzmp6
// void gvusershape_file_release(usershape_t *us) 
public static Object gvusershape_file_release(Object... arg) {
UNSUPPORTED("6i3sc3o39zrjf80hb57rmbe1t"); // void gvusershape_file_release(usershape_t *us)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("5a8j5zrw7vbgoyy4ogtlsywui"); //     if (us->nocache) {
UNSUPPORTED("47xp2oczqdec6db0z36u2n84r"); // 	if (us->f) {
UNSUPPORTED("bzvrsl2iafc3pw0qughvm761m"); // 	    fclose(us->f);
UNSUPPORTED("amwbxewujlnkm7a19q6zl4xq0"); // 	    us->f = (void *)0;
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 8gvu7lmp0zomg3yy0a76n8pjh
// static usershape_t *gvusershape_open (char *name) 
public static Object gvusershape_open(Object... arg) {
UNSUPPORTED("5kyko9vxn3aqt1awhp5hd8c6a"); // static usershape_t *gvusershape_open (char *name)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("exneoozy5g8al0a8y6fxb88zv"); //     usershape_t *us;
UNSUPPORTED("265kxn69043hh3vmr1ma8pbpg"); //     assert(name);
UNSUPPORTED("4qhyd93m9v57fwb1anljrxfd3"); //     if (!ImageDict)
UNSUPPORTED("7tvne0duj1fh44ndje8hlq8nt"); //         ImageDict = dtopen(&ImageDictDisc, Dttree);
UNSUPPORTED("be9pwrhxzz4ia299kongm87uq"); //     if (! (us = gvusershape_find(name))) {
UNSUPPORTED("9suqlrwjg77jwdm7tlgzsc0by"); //         if (! (us = zmalloc(sizeof(usershape_t))))
UNSUPPORTED("7f8nrnyuywny79cpzv6a49d8v"); // 	    return (void *)0;
UNSUPPORTED("6viwgw3bdyvknckol3dsh1s5v"); // 	us->name = agstrdup (0, name);
UNSUPPORTED("ewc238znlsx5axoejodocqoyp"); // 	if (!gvusershape_file_access(us)) 
UNSUPPORTED("7f8nrnyuywny79cpzv6a49d8v"); // 	    return (void *)0;
UNSUPPORTED("4i933mk1cyf6xcljbpn69na20"); // 	assert(us->f);
UNSUPPORTED("43qnzsg978s9yom42c0n33wx3"); //         switch(imagetype(us)) {
UNSUPPORTED("7eenfnk5yzoj21g2agitce6kx"); // 	    case FT_NULL:
UNSUPPORTED("azdoxaxgz4fzzdoeyruqj36jk"); // 		if (!(us->data = (void*)find_user_shape(us->name)))
UNSUPPORTED("9mepm6hu7uqhpdwcg60h6pc3o"); // 		    agerr(AGWARN, "\"%s\" was not found as a file or as a shape library member\n", us->name);
UNSUPPORTED("9bu5332flxhc1ivhmzuiqcjdh"); // 		    free(us);
UNSUPPORTED("6948880uywn56ppo1fuqbm2rt"); // 		    return (void *)0;
UNSUPPORTED("9ekmvj13iaml5ndszqyxa8eq"); // 		break;
UNSUPPORTED("4p4ayucon7vqli1ylni5hjrfb"); // 	    case FT_GIF:
UNSUPPORTED("c68gkmazl6aratmtonsx4fan9"); // 	        gif_size(us);
UNSUPPORTED("7f9nr53m374zf5jnncru7yfn6"); // 	        break;
UNSUPPORTED("7da7e6aavwlcxg7eglorxy23g"); // 	    case FT_PNG:
UNSUPPORTED("ubixbe52lmq7uzqie7h12582"); // 	        png_size(us);
UNSUPPORTED("7f9nr53m374zf5jnncru7yfn6"); // 	        break;
UNSUPPORTED("9ysbcxl45mbfo0lhfihqfby4d"); // 	    case FT_BMP:
UNSUPPORTED("9xqkpupuhywfcx0rkky4pnu5q"); // 	        bmp_size(us);
UNSUPPORTED("7f9nr53m374zf5jnncru7yfn6"); // 	        break;
UNSUPPORTED("bhf95qvvxhf47ek76yf2a45l0"); // 	    case FT_JPEG:
UNSUPPORTED("bq0stf6n5x5tzvjir50smcifq"); // 	        jpeg_size(us);
UNSUPPORTED("7f9nr53m374zf5jnncru7yfn6"); // 	        break;
UNSUPPORTED("bazpzxo4uq0pwp7j4k94ngrtz"); // 	    case FT_PS:
UNSUPPORTED("7bs1bijtavu2jceudpors0jbp"); // 	        ps_size(us);
UNSUPPORTED("7f9nr53m374zf5jnncru7yfn6"); // 	        break;
UNSUPPORTED("8xv8jf3eindz5gftkzluercwh"); // 	    case FT_WEBP:
UNSUPPORTED("91d89xyv6dtk8a479p0cfwyy7"); // 	        webp_size(us);
UNSUPPORTED("7f9nr53m374zf5jnncru7yfn6"); // 	        break;
UNSUPPORTED("c8km9iw5nmks5lp6jl9j5s5q2"); // 	    case FT_SVG:
UNSUPPORTED("a147xv5hgfkmevlpr3jb7fzci"); // 	        svg_size(us);
UNSUPPORTED("7f9nr53m374zf5jnncru7yfn6"); // 	        break;
UNSUPPORTED("242ztnyfnn9hx8sb3u5z3omun"); // 	    case FT_PDF:
UNSUPPORTED("9ef2eux36pp9nf00d1ijmfbhm"); // 		pdf_size(us);
UNSUPPORTED("9ekmvj13iaml5ndszqyxa8eq"); // 		break;
UNSUPPORTED("3h2lioytqam297xdu0a7l0y6w"); // 	    case FT_ICO:
UNSUPPORTED("dj7xjql9twvpafx4qoyb07ra8"); // 		ico_size(us);
UNSUPPORTED("9ekmvj13iaml5ndszqyxa8eq"); // 		break;
UNSUPPORTED("90yyikfl7j30g6npbi9n0jxyw"); // //	    case FT_TIFF:
UNSUPPORTED("72cnoy8x3q67t6323yzwcdim8"); // //		tiff_size(us);
UNSUPPORTED("1sbah8d4teszwq6tgzsohjpba"); // //		break;
UNSUPPORTED("2hkvwieff33mlzyovvk76tpzn"); // 	    case FT_EPS:   /* no eps_size code available */
UNSUPPORTED("bt2g0yhsy3c7keqyftf3c98ut"); // 	    default:
UNSUPPORTED("7f9nr53m374zf5jnncru7yfn6"); // 	        break;
UNSUPPORTED("4mhlpjofolwivhm0tl8cxznly"); //         }
UNSUPPORTED("1ju3i4hja8fc5wwdkk9ed70t0"); //         gvusershape_file_release(us);
UNSUPPORTED("6ovs68xjm9urfsh1qtosv57gi"); //         (*(((Dt_t*)(ImageDict))->searchf))((ImageDict),(void*)(us),0000001);
UNSUPPORTED("e7wyuaoyszuqilaizfjv1mnp9"); //         return us;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("7xahvc82av8lspjlm80i6ycqx"); //     gvusershape_file_release(us);
UNSUPPORTED("2iqrgjc97xskxvw6791yf0sl7"); //     return us;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 1uc6t1ylgvem0d3xb73wkh6k5
// point  gvusershape_size_dpi (usershape_t* us, pointf dpi) 
public static Object gvusershape_size_dpi(Object... arg) {
UNSUPPORTED("5vjx3ewef6ht20ld1et4ghr4y"); // point 
UNSUPPORTED("5mxbu6cb95ay6dfs0i5n8icbs"); // gvusershape_size_dpi (usershape_t* us, pointf dpi)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("e5r3mj8btrkw973m7l0rritko"); //     point rv;
UNSUPPORTED("6n6sowowfpx2bppltl0zu5v7v"); //     if (!us) {
UNSUPPORTED("4zusj5p9eg9pazvp0px1fxvhy"); //         rv.x = rv.y = -1;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("1nyzbeonram6636b1w955bypn"); //     else {
UNSUPPORTED("8egqtg148lkga2f8fnti0xikl"); // 	if (us->dpi != 0) {
UNSUPPORTED("appsmczgq79si2bttb9rrlf1n"); // 	    dpi.x = dpi.y = us->dpi;
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("8z1a50vlhe7l1e7lnn7h5r90w"); // 	rv.x = us->w * 72 / dpi.x;
UNSUPPORTED("b8wlsi5z8klt27ehe7vr9ue61"); // 	rv.y = us->h * 72 / dpi.y;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("v7vqc9l7ge2bfdwnw11z7rzi"); //     return rv;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 1vqiwf4q9iqmxqvd9e5qa1vo7
// point gvusershape_size(graph_t * g, char *name) 
public static Object gvusershape_size(Object... arg) {
UNSUPPORTED("3a8kgtxwhe9qi1f5wde0npt4d"); // point gvusershape_size(graph_t * g, char *name)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("e5r3mj8btrkw973m7l0rritko"); //     point rv;
UNSUPPORTED("aj7pw6lpv6z6nan96bx4pdfu2"); //     pointf dpi;
UNSUPPORTED("114dkd9d0mrkmtgl0ncfy2y88"); //     static char* oldpath;
UNSUPPORTED("709e07k0mmaqqdi3botr9zj87"); //     usershape_t* us;
UNSUPPORTED("d5ucq7enfbvarib2hwpi6y9qi"); //     /* no shape file, no shape size */
UNSUPPORTED("crs7cps8buaugcwtbezaqxd0q"); //     if (!name || (*name == '\0')) {
UNSUPPORTED("4zusj5p9eg9pazvp0px1fxvhy"); //         rv.x = rv.y = -1;
UNSUPPORTED("cs1ejkx9cw658cl8cagdzqrzm"); // 	return rv;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("24me4s4cte11xoie4uxxwajbs"); //     if (!HTTPServerEnVar && (oldpath != Gvimagepath)) {
UNSUPPORTED("ba3zx115acxf5hiob55lfwvhd"); // 	oldpath = Gvimagepath;
UNSUPPORTED("63hyc3s3c95h5g1lp6aypzd8k"); // 	if (ImageDict) {
UNSUPPORTED("50x4kwt6y5jkwbeqmhvki188q"); // 	    dtclose(ImageDict);
UNSUPPORTED("75t9qfgcu2fz0o59mjqh5rruv"); // 	    ImageDict = (void *)0;
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("d77jz2hp035qgnn72uyy7evkj"); //     if ((dpi.y = GD_drawing(g)->dpi) >= 1.0)
UNSUPPORTED("8kboj4mypdab20ax7qfuq8oyp"); // 	dpi.x = dpi.y;
UNSUPPORTED("div10atae09n36x269sl208r1"); //     else
UNSUPPORTED("6buslnzh8a0nuwlk9wkfaytt8"); // 	dpi.x = dpi.y = (double)DEFAULT_DPI;
UNSUPPORTED("16h3qy8ek57tauhtrw8xkodar"); //     us = gvusershape_open (name);
UNSUPPORTED("6tq0lkl6cmjltetu68liok738"); //     rv = gvusershape_size_dpi (us, dpi);
UNSUPPORTED("v7vqc9l7ge2bfdwnw11z7rzi"); //     return rv;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}


}
