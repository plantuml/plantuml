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
package gen.lib.label;
import static smetana.core.Macro.UNSUPPORTED;

public class rectangle__c {
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


//1 9mnpt3mibocomreg5h1kk860g
// extern Rect_t CoverAll




//3 1wtvfzwbzj03e6w5rw4k7qdax
// void InitRect(Rect_t * r) 
public static Object InitRect(Object... arg) {
UNSUPPORTED("bfynnbut17s29886tfi4hmp71"); // void InitRect(Rect_t * r)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("pp6gyv6pecd6kik4hoguluwp"); //     register int i;
UNSUPPORTED("6v26zqmzay64h92bxd4qt6qgs"); //     for (i = 0; i < 2*2; i++)
UNSUPPORTED("d3uknh6sy0xxecd62dbcemd5p"); // 	r->boundary[i] = 0;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 bvazxgli5q4yxvzl5kn1vjqpm
// Rect_t NullRect() 
public static Object NullRect(Object... arg) {
UNSUPPORTED("7bfeg4qbgfa72qeao4zmwznat"); // Rect_t NullRect()
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("9jotn4njsd13qx406m9otorg4"); //     Rect_t r;
UNSUPPORTED("pp6gyv6pecd6kik4hoguluwp"); //     register int i;
UNSUPPORTED("4cmj0swptez35tqafqf86bskl"); //     r.boundary[0] = 1;
UNSUPPORTED("6lzrqh8r1olplqcbtz1n5dow7"); //     r.boundary[2] = -1;
UNSUPPORTED("ol4wmdbmn9kjw3hpmp2gwavz"); //     for (i = 1; i < 2; i++)
UNSUPPORTED("9xr8ijrn07laqlacrzelzczxa"); // 	r.boundary[i] = r.boundary[i + 2] = 0;
UNSUPPORTED("a2hk6w52njqjx48nq3nnn2e5i"); //     return r;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 1ijarur71gcahchxz8vqf69na
// unsigned int RectArea(Rect_t * r) 
public static Object RectArea(Object... arg) {
UNSUPPORTED("dt9366zeifsgcei4onz0fdt4i"); // unsigned int RectArea(Rect_t * r)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("9cyat6ft4tmgpumn70l9fwydy"); //   register int i;
UNSUPPORTED("eep6ne1fnwvxrzmao6aq2e80t"); //   unsigned int area=1, a=1;
UNSUPPORTED("bxtlpefe142w9pb81aa0gkkcj"); //   assert(r);
UNSUPPORTED("7xe9zz3f2fwhfptig6esqvb1t"); //     if (((r)->boundary[0] > (r)->boundary[2])) return 0;
UNSUPPORTED("9gsgfs2guis9c3c3oi57mxpq2"); //     /*
UNSUPPORTED("asaz8qrby7qugc5m3ylnjg6o7"); //      * XXX add overflow checks
UNSUPPORTED("795vpnc8yojryr8b46aidsu69"); //      */
UNSUPPORTED("17o3f4aat9tkp17wsngm29nst"); //     area = 1;
UNSUPPORTED("6xp61z8h2baoxnlm757q289e3"); //     for (i = 0; i < 2; i++) {
UNSUPPORTED("6g0ik6vssf9px33quo2z9ferr"); //       unsigned int b = r->boundary[i + 2] - r->boundary[i];
UNSUPPORTED("7tqqzmxu3tsfxccs53evs54me"); //       a *= b;
UNSUPPORTED("3u7h4981b69nu4w80bhv3s4q"); //       if( (a / b ) != area) {
UNSUPPORTED("6qc59bm54jy4hv9gw8a50rk0u"); // 	agerr (AGERR, "label: area too large for rtree\n");
UNSUPPORTED("awx87c59fwl0w8r64jfd86jrd"); // 	return UINT_MAX;
UNSUPPORTED("dquo3qofk56ds5xl95lhvcthf"); //       }
UNSUPPORTED("b34qh5cr4ie1y00nbl91sn2km"); //       area = a;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("9ww3ox2wqjgbtsin4e26qgoyx"); //     return area;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 tgmhi1wshyhqky2pappb6w6w
// Rect_t CombineRect(Rect_t * r, Rect_t * rr) 
public static Object CombineRect(Object... arg) {
UNSUPPORTED("18ebi8xfcz225jqpfk5vtp9hf"); // Rect_t CombineRect(Rect_t * r, Rect_t * rr)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("7gcww32g3xx6yjb9m2tgq8f7c"); //     register int i, j;
UNSUPPORTED("ep1c21oj5vdbkci5nklq26u6d"); //     Rect_t new;
UNSUPPORTED("8woqwn01hzllzlrb8an6apviw"); //     assert(r && rr);
UNSUPPORTED("61el74qdlszr9b7htgajgnncw"); //     if (((r)->boundary[0] > (r)->boundary[2]))
UNSUPPORTED("26gbreijuodtzexgobqd73u1p"); // 	return *rr;
UNSUPPORTED("9c5jzil5a7hm4bfzytn0e7aww"); //     if (((rr)->boundary[0] > (rr)->boundary[2]))
UNSUPPORTED("h0psmi4ydywx720mvhp33x5g"); // 	return *r;
UNSUPPORTED("6xp61z8h2baoxnlm757q289e3"); //     for (i = 0; i < 2; i++) {
UNSUPPORTED("1p51ro3v4iw4nogctzk3y0bts"); // 	new.boundary[i] = MIN(r->boundary[i], rr->boundary[i]);
UNSUPPORTED("2h0ee6s8hk7srb6xqmnfluf52"); // 	j = i + 2;
UNSUPPORTED("3cljcok8kw06fphxnu0183g4"); // 	new.boundary[j] = MAX(r->boundary[j], rr->boundary[j]);
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("4b8pgvy3c1dhlanxwmafau4pt"); //     return new;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 9glce34jzknoqj98agg96k03o
// int Overlap(Rect_t * r, Rect_t * s) 
public static Object Overlap(Object... arg) {
UNSUPPORTED("75f545jos6v3su84hbt728wxr"); // int Overlap(Rect_t * r, Rect_t * s)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("7gcww32g3xx6yjb9m2tgq8f7c"); //     register int i, j;
UNSUPPORTED("3ilt6jfw7dcaebocva6xawiui"); //     assert(r && s);
UNSUPPORTED("6xp61z8h2baoxnlm757q289e3"); //     for (i = 0; i < 2; i++) {
UNSUPPORTED("71gqfx1xze9ccjzy9ids9x8cj"); // 	j = i + 2;	/* index for high sides */
UNSUPPORTED("b41yx5qm8jqsnyojyaztg6wy1"); // 	if (r->boundary[i] > s->boundary[j]
UNSUPPORTED("cjb3lqf10yggej4b0uh9acc72"); // 	    || s->boundary[i] > r->boundary[j])
UNSUPPORTED("8tnlb2txucdutal7665h0v68g"); // 	    return (0);
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("4si0cf97a5sfd9ozuunds9goz"); //     return (!(0));
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 b2epj09d2wxyndbn4bgdsxr2q
// int Contained(Rect_t * r, Rect_t * s) 
public static Object Contained(Object... arg) {
UNSUPPORTED("733zc41b58sdh3p9oeu9fj9l4"); // int Contained(Rect_t * r, Rect_t * s)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("9awzjr4cb2zjgf6gos90frckl"); //     register int i, j, result;
UNSUPPORTED("3ilt6jfw7dcaebocva6xawiui"); //     assert(r && s);
UNSUPPORTED("ef38prhqw1mt7m82qf23llthz"); //     /* undefined rect is contained in any other */
UNSUPPORTED("61el74qdlszr9b7htgajgnncw"); //     if (((r)->boundary[0] > (r)->boundary[2]))
UNSUPPORTED("3adr32h5e1fehu4g7j2u24asz"); // 	return (!(0));
UNSUPPORTED("97iyq13885wpg4001abrkjhuk"); //     /* no rect (except an undefined one) is contained in an undef rect */
UNSUPPORTED("ex7jjwy9foumvka1ks3pzhm6u"); //     if (((s)->boundary[0] > (s)->boundary[2]))
UNSUPPORTED("e6i1m837bi402fjvy87uxgyf"); // 	return (0);
UNSUPPORTED("41048mrirr0adiwxs12neylq8"); //     result = (!(0));
UNSUPPORTED("6xp61z8h2baoxnlm757q289e3"); //     for (i = 0; i < 2; i++) {
UNSUPPORTED("71gqfx1xze9ccjzy9ids9x8cj"); // 	j = i + 2;	/* index for high sides */
UNSUPPORTED("59et7tzqav7nffmkllv6u1kbt"); // 	result = result && r->boundary[i] >= s->boundary[i]
UNSUPPORTED("4tdt6ca6zi1rw9kklpgicedap"); // 	    && r->boundary[j] <= s->boundary[j];
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("e73y2609z2557xahrcvzmcb8e"); //     return result;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}


}
