/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * Project Info:  http://plantuml.com
 * 
 * If you like this project or if you find it useful, you can support us at:
 * 
 * http://plantuml.com/patreon (only 1$ per month!)
 * http://plantuml.com/paypal
 * 
 * This file is part of Smetana.
 * Smetana is a partial translation of Graphviz/Dot sources from C to Java.
 *
 * (C) Copyright 2009-2020, Arnaud Roques
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
import static smetana.core.JUtilsDebug.ENTERING;
import static smetana.core.JUtilsDebug.LEAVING;
import static smetana.core.Macro.N;
import static smetana.core.Macro.UNSUPPORTED;
import h.ST_Rect_t;

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
public static void InitRect(ST_Rect_t r) {
ENTERING("1wtvfzwbzj03e6w5rw4k7qdax","InitRect");
try {
     int i;
     for (i = 0; i < 2*2; i++)
    	 r.boundary[i]=0;
} finally {
LEAVING("1wtvfzwbzj03e6w5rw4k7qdax","InitRect");
}
}




//3 bvazxgli5q4yxvzl5kn1vjqpm
// Rect_t NullRect() 
public static ST_Rect_t NullRect() {
ENTERING("bvazxgli5q4yxvzl5kn1vjqpm","NullRect");
try {
     ST_Rect_t r = new ST_Rect_t();
     int i;
     r.boundary[0]=1;
     r.boundary[2]=-1;
     for (i = 1; i < 2; i++) {
         r.boundary[i]=0;
         r.boundary[i+2]=0;
     }
     return r;
} finally {
LEAVING("bvazxgli5q4yxvzl5kn1vjqpm","NullRect");
}
}




//3 1ijarur71gcahchxz8vqf69na
// unsigned int RectArea(Rect_t * r) 
public static int RectArea(ST_Rect_t r) {
ENTERING("1ijarur71gcahchxz8vqf69na","RectArea");
try {
   int i;
   int area=1, a=1;
//   assert(r);
     if (r.boundary[0] > r.boundary[2]) return 0;
     /*
      * XXX add overflow checks
      */
     area = 1;
     for (i = 0; i < 2; i++) {
       int b = r.boundary[i+2] - r.boundary[i];
       a *= b;
       if( (a / b ) != area) {
UNSUPPORTED("6qc59bm54jy4hv9gw8a50rk0u"); // 	agerr (AGERR, "label: area too large for rtree\n");
UNSUPPORTED("awx87c59fwl0w8r64jfd86jrd"); // 	return UINT_MAX;
       }
       area = a;
     }
     return area;
} finally {
LEAVING("1ijarur71gcahchxz8vqf69na","RectArea");
}
}




//3 tgmhi1wshyhqky2pappb6w6w
// Rect_t CombineRect(Rect_t * r, Rect_t * rr) 
public static ST_Rect_t CombineRect(ST_Rect_t r, ST_Rect_t rr) {
ENTERING("tgmhi1wshyhqky2pappb6w6w","CombineRect");
try {
     int i, j;
     ST_Rect_t new_ = new ST_Rect_t();
     //     assert(r && rr);
     if (r.boundary[0] > r.boundary[2])
 	return (ST_Rect_t) rr.copy();
     if (rr.boundary[0] > rr.boundary[2])
 	return (ST_Rect_t) r.copy();
     for (i = 0; i < 2; i++) {
 	new_.boundary[i] = Math.min(r.boundary[i], rr.boundary[i]);
 	j = i + 2;
 	new_.boundary[j] = Math.max(r.boundary[j], rr.boundary[j]);
     }
     return new_;
} finally {
LEAVING("tgmhi1wshyhqky2pappb6w6w","CombineRect");
}
}




//3 9glce34jzknoqj98agg96k03o
// int Overlap(Rect_t * r, Rect_t * s) 
public static boolean Overlap(ST_Rect_t r, ST_Rect_t s) {
ENTERING("9glce34jzknoqj98agg96k03o","Overlap");
try {
     int i, j;
//     assert(r && s);
     for (i = 0; i < 2; i++) {
 	j = i + 2;	/* index for high sides */
 	if (r.boundary[i] > s.boundary[j]
 	    || s.boundary[i] > r.boundary[j])
 	    return false;
     }
     return (N(0));
} finally {
LEAVING("9glce34jzknoqj98agg96k03o","Overlap");
}
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
