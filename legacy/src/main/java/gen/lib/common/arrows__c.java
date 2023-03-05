/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * Project Info:  https://plantuml.com
 * 
 * If you like this project or if you find it useful, you can support us at:
 * 
 * https://plantuml.com/patreon (only 1$ per month!)
 * https://plantuml.com/paypal
 * 
 * This file is part of Smetana.
 * Smetana is a partial translation of Graphviz/Dot sources from C to Java.
 *
 * (C) Copyright 2009-2022, Arnaud Roques
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
package gen.lib.common;
import static gen.lib.cgraph.attr__c.agxget;
import static gen.lib.cgraph.graph__c.agisdirected;
import static gen.lib.cgraph.obj__c.agraphof;
import static gen.lib.common.splines__c.bezier_clip;
import static gen.lib.common.utils__c.late_double;
import static smetana.core.JUtils.EQ_CSTRING;
import static smetana.core.JUtils.strncmp;
import static smetana.core.Macro.ARR_TYPE_GAP;
import static smetana.core.Macro.ARR_TYPE_NONE;
import static smetana.core.Macro.ARR_TYPE_NORM;
import static smetana.core.Macro.BITS_PER_ARROW;
import static smetana.core.Macro.BITS_PER_ARROW_TYPE;
import static smetana.core.Macro.DIST2;
import static smetana.core.Macro.ED_conc_opp_flag;
import static smetana.core.Macro.UNSUPPORTED;
import static smetana.core.debug.SmetanaDebug.ENTERING;
import static smetana.core.debug.SmetanaDebug.LEAVING;

import gen.annotation.Doc;
import gen.annotation.Original;
import gen.annotation.Todo;
import gen.annotation.Unused;
import h.ST_Agedge_s;
import h.ST_arrowdir_t;
import h.ST_arrowname_t;
import h.ST_bezier;
import h.ST_inside_t;
import h.ST_pointf;
import smetana.core.CArray;
import smetana.core.CFunction;
import smetana.core.CFunctionAbstract;
import smetana.core.CString;
import smetana.core.Globals;
import smetana.core.ZType;

public class arrows__c {

//3 3apnay8wumntfkvud64ov7fcf
// static char *arrow_match_name_frag(char *name, arrowname_t * arrownames, int *flag) 
@Unused
@Original(version="2.38.0", path="lib/common/arrows.c", name="", key="3apnay8wumntfkvud64ov7fcf", definition="static char *arrow_match_name_frag(char *name, arrowname_t * arrownames, int *flag)")
public static CString arrow_match_name_frag(CString name, ST_arrowname_t[] arrownames, int flag[]) {
ENTERING("3apnay8wumntfkvud64ov7fcf","arrow_match_name_frag");
try {
    int arrowname;
    int namelen = 0;
    CString rest = name;

    for (arrowname = 0; arrownames[arrowname].name!=null; arrowname++) {
	namelen = arrownames[arrowname].name.length();
	if (strncmp(name, arrownames[arrowname].name, namelen) == 0) {
	    flag[0] |= arrownames[arrowname].type;
	    rest =  rest.plus_(namelen);
	    break;
	}
    }
    return rest;
} finally {
LEAVING("3apnay8wumntfkvud64ov7fcf","arrow_match_name_frag");
}
}




//3 b669zec8aznq4obnil98j5lby
// static char *arrow_match_shape(char *name, int *flag) 
@Unused
@Original(version="2.38.0", path="lib/common/arrows.c", name="", key="b669zec8aznq4obnil98j5lby", definition="static char *arrow_match_shape(char *name, int *flag)")
public static CString arrow_match_shape(Globals zz, CString name, int flag[]) {
ENTERING("b669zec8aznq4obnil98j5lby","arrow_match_shape");
try {
    CString next=null, rest=null;
    int f[] = new int[] {0};
    rest = arrow_match_name_frag(name, zz.Arrowsynonyms, f);
    if (EQ_CSTRING(rest, name)) {
	do {
	    next = rest;
	    rest = arrow_match_name_frag(next, zz.Arrowmods, f);
	} while (!EQ_CSTRING(next,rest));
	rest = arrow_match_name_frag(rest, zz.Arrownames, f);
    }
    if (f[0]!=0 && (f[0] & ((1 << BITS_PER_ARROW_TYPE) - 1)) == 0)
UNSUPPORTED("2mly07gipiope02mgflzcie3e"); // 	f |= 1;
    flag[0] |= f[0];
    return rest;
} finally {
LEAVING("b669zec8aznq4obnil98j5lby","arrow_match_shape");
}
}




private static final int NUMB_OF_ARROW_HEADS = 4;

//3 2pveqb5qcgfxcqp410ub942eg
// static void arrow_match_name(char *name, int *flag) 
@Unused
@Doc("update flags for arrow. Warning: implementation changed in Java")
@Todo(what = "Check why C is strange")
@Original(version="2.38.0", path="lib/common/arrows.c", name="arrow_match_name", key="2pveqb5qcgfxcqp410ub942eg", definition="static void arrow_match_name(char *name, int *flag)")
public static void arrow_match_name(Globals zz, CString name, int flag[]) {
ENTERING("2pveqb5qcgfxcqp410ub942eg","arrow_match_name");
try {
    CString rest = name;
    CString next;
    int i; int f[] = new int[] {0};
    flag[0] = 0;
    for (i = 0; rest.charAt(0) != '\0' && i < NUMB_OF_ARROW_HEADS; ) {
	f[0] = ARR_TYPE_NONE;
	next = rest;
        rest = arrow_match_shape(zz, next, f);
	if (f[0] == ARR_TYPE_NONE) {
	    System.err.println("Arrow type \"%s\" unknown - ignoring\n");
	    return;
	}
	if (f[0] == ARR_TYPE_GAP && i == (NUMB_OF_ARROW_HEADS -1))
	    f[0] = ARR_TYPE_NONE;
	if ((f[0] == ARR_TYPE_GAP) && (i == 0) && (rest.charAt(0) == '\0'))
	    f[0] = ARR_TYPE_NONE;
	if (f[0] != ARR_TYPE_NONE)
	    flag[0] |= (f[0]);
        // flag[0] |= (f[0] << (i++ * BITS_PER_ARROW));
    }
} finally {
LEAVING("2pveqb5qcgfxcqp410ub942eg","arrow_match_name");
}
}




//3 2szgwtfieaw58pea2ohjyu8ea
// void arrow_flags(Agedge_t * e, int *sflag, int *eflag) 
@Unused
@Doc("update flags for arrow. Warning: implementation changed in Java")
@Todo(what = "Check why C is strange")
@Original(version="2.38.0", path="lib/common/arrows.c", name="arrow_flags", key="2szgwtfieaw58pea2ohjyu8ea", definition="void arrow_flags(Agedge_t * e, int *sflag, int *eflag)")
public static void arrow_flags(Globals zz, ST_Agedge_s e, int sflag[], int eflag[]) {
ENTERING("2szgwtfieaw58pea2ohjyu8ea","arrow_flags");
try {
    CString attr;
    ST_arrowdir_t arrowdir;
    sflag[0] = ARR_TYPE_NONE;
    eflag[0] = agisdirected(agraphof(e)) ? ARR_TYPE_NORM : ARR_TYPE_NONE;

    sflag[0] = ARR_TYPE_NORM;
    eflag[0] = ARR_TYPE_NORM;

    if (zz.E_dir!=null && ((attr = agxget(e, zz.E_dir))).charAt(0)!='\0') {
UNSUPPORTED("em7x45v09orjeey5u06gf9b4s"); // 	for (arrowdir = Arrowdirs; arrowdir->dir; arrowdir++) {
UNSUPPORTED("dhaookuw0a1xqmh07lldcvlgi"); // 	    if ((*(attr)==*(arrowdir->dir)&&!strcmp(attr,arrowdir->dir))) {
UNSUPPORTED("1d32qbc447n7nmmvedj3bnhr4"); // 		*sflag = arrowdir->sflag;
UNSUPPORTED("4bwlkonvn34iwi5ea1o8zov3o"); // 		*eflag = arrowdir->eflag;
UNSUPPORTED("9ekmvj13iaml5ndszqyxa8eq"); // 		break;
UNSUPPORTED("6t98dcecgbvbvtpycwiq2ynnj"); // 	    }
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
    }
    if (zz.E_arrowhead!=null && (eflag[0] == ARR_TYPE_NORM) && ((attr = agxget(e,zz.E_arrowhead))).charAt(0)!='\0')
	arrow_match_name(zz, attr, eflag);
    if (zz.E_arrowtail!=null && (sflag[0] == ARR_TYPE_NORM) && ((attr = agxget(e, zz.E_arrowtail))).charAt(0)!='\0')
	arrow_match_name(zz, attr, sflag);
    if (ED_conc_opp_flag(e)) {
UNSUPPORTED("1p2usipxeqlorwroqo37t3yfy"); // 	edge_t *f;
UNSUPPORTED("6ne3pu2bnhx6tyx81t4td4up6"); // 	int s0, e0;
UNSUPPORTED("8vccx8sm1c228dqm7l1jm2hfs"); // 	/* pick up arrowhead of opposing edge */
UNSUPPORTED("cu21qrxyz93ly7l96af2gcsle"); // 	f = (agedge(agraphof(aghead(e)),aghead(e),agtail(e),NULL,0));
UNSUPPORTED("9u6scg3h7baww90tcykvjhajo"); // 	arrow_flags(f, &s0, &e0);
UNSUPPORTED("157il4mnbenpon7knxfdb4fwb"); // 	*eflag = *eflag | s0;
UNSUPPORTED("7wucod5xwp24vblpcbjbmmcq1"); // 	*sflag = *sflag | e0;
    }
} finally {
LEAVING("2szgwtfieaw58pea2ohjyu8ea","arrow_flags");
}
}




//3 1yk5wl46i7rlzcern0tefd24s
// double arrow_length(edge_t * e, int flag) 
@Unused
@Original(version="2.38.0", path="lib/common/arrows.c", name="arrow_length", key="1yk5wl46i7rlzcern0tefd24s", definition="double arrow_length(edge_t * e, int flag)")
public static double arrow_length(Globals zz, ST_Agedge_s e, int flag) {
ENTERING("1yk5wl46i7rlzcern0tefd24s","arrow_length");
try {
    double lenfact = 0.0;
    int f, i;
    for (i = 0; i < NUMB_OF_ARROW_HEADS; i++) {
        /* we don't simply index with flag because arrowtypes are not necessarily sorted */
        f = (flag >> (i * BITS_PER_ARROW)) & ((1 << BITS_PER_ARROW_TYPE) - 1);
        for (int arrowtype = 0; zz.Arrowtypes[arrowtype].gen!=null; arrowtype++) {
	    if (f == zz.Arrowtypes[arrowtype].type) {
	        lenfact += zz.Arrowtypes[arrowtype].lenfact;
	        break;
	    }
        }
    }
    /* The original was missing the factor E_arrowsz, but I believe it
       should be here for correct arrow clipping */
    return 10. * lenfact * late_double(e, zz.E_arrowsz, 1.0, 0.0);
} finally {
LEAVING("1yk5wl46i7rlzcern0tefd24s","arrow_length");
}
}




public static CFunction inside = new CFunctionAbstract("inside") {
	
	public Object exe(Globals zz, Object... args) {
		return inside((ST_inside_t)args[0], (ST_pointf)args[1]);
	}};

@Unused
@Original(version="2.38.0", path="lib/common/arrows.c", name="inside", key="7ymcsnwqkr1crisrga0kezh1f", definition="static boolean inside(inside_t * inside_context, pointf p)")
public static boolean inside(ST_inside_t inside_context, final ST_pointf p) {
// WARNING!! STRUCT
return inside_w_(inside_context, p.copy());
}
private static boolean inside_w_(ST_inside_t inside_context, final ST_pointf p) {
ENTERING("7ymcsnwqkr1crisrga0kezh1f","inside");
try {
    return DIST2(p, inside_context.a_p.get__(0)) <= inside_context.a_r[0];
} finally {
LEAVING("7ymcsnwqkr1crisrga0kezh1f","inside");
}
}




//3 9eellwhg4gsa2pdszpeqihs2d
// int arrowEndClip(edge_t* e, pointf * ps, int startp, 		 int endp, bezier * spl, int eflag) 
@Unused
@Original(version="2.38.0", path="lib/common/arrows.c", name="arrowEndClip", key="9eellwhg4gsa2pdszpeqihs2d", definition="int arrowEndClip(edge_t* e, pointf * ps, int startp, 		 int endp, bezier * spl, int eflag)")
public static int arrowEndClip(Globals zz, ST_Agedge_s e, CArray<ST_pointf> ps, int startp, int endp, ST_bezier spl, int eflag) {
ENTERING("9eellwhg4gsa2pdszpeqihs2d","arrowEndClip");
try {
    final ST_inside_t inside_context = new ST_inside_t();
    final CArray<ST_pointf> sp = CArray.<ST_pointf>ALLOC__(4, ZType.ST_pointf);
    double elen;
    final double elen2[] = new double[] {0};
    
    elen = arrow_length(zz, e, eflag);
    elen2[0] =elen * elen;
    spl.eflag = eflag;
    spl.ep.___(ps.get__(endp + 3));
    if (endp > startp && DIST2(ps.get__(endp), ps.get__(endp + 3)) < elen2[0]) {
	endp -= 3;
    }
    sp.get__(3).___(ps.get__(endp));
    sp.get__(2).___(ps.get__(endp+1));
    sp.get__(1).___(ps.get__(endp+2));
    sp.get__(0).___(spl.ep); /* ensure endpoint starts inside */
    
    inside_context.a_p = sp;
    inside_context.a_r = elen2;
    bezier_clip(zz, inside_context, arrows__c.inside, sp, true);
    
    ps.get__(endp).___(sp.get__(3));
    ps.get__(endp+1).___(sp.get__(2));
    ps.get__(endp+2).___(sp.get__(1));
    ps.get__(endp+3).___(sp.get__(0));
    return endp;
} finally {
LEAVING("9eellwhg4gsa2pdszpeqihs2d","arrowEndClip");
}
}




//3 q7y4oxn0paexbgynmtg2zmiv
// int arrowStartClip(edge_t* e, pointf * ps, int startp, 		   int endp, bezier * spl, int sflag) 
@Unused
@Original(version="2.38.0", path="lib/common/arrows.c", name="arrowStartClip", key="q7y4oxn0paexbgynmtg2zmiv", definition="int arrowStartClip(edge_t* e, pointf * ps, int startp, 		   int endp, bezier * spl, int sflag)")
public static int arrowStartClip(Globals zz, ST_Agedge_s e, CArray<ST_pointf> ps, int startp, int endp, ST_bezier spl, int sflag) {
ENTERING("q7y4oxn0paexbgynmtg2zmiv","arrowStartClip");
try {
    final ST_inside_t inside_context = new ST_inside_t();
    final CArray<ST_pointf> sp = CArray.<ST_pointf>ALLOC__(4, ZType.ST_pointf);
    double slen;
    
    final double[] slen2 = new double[] {0};
    slen = arrow_length(zz, e, sflag);
    slen2[0] = slen * slen;
    spl.sflag = sflag;
    spl.sp.___(ps.get__(startp));
    if (endp > startp && DIST2(ps.get__(startp), ps.get__(startp + 3)) < slen2[0]) {
    	startp += 3;
    }
    sp.get__(0).___(ps.get__(startp+3));
    sp.get__(1).___(ps.get__(startp+2));
    sp.get__(2).___(ps.get__(startp+1));
    sp.get__(3).___(spl.sp);    /* ensure endpoint starts inside */
    
    inside_context.a_p = sp.plus_(3);
    inside_context.a_r = slen2;
    bezier_clip(zz, inside_context, arrows__c.inside, sp, false);
    
    ps.get__(startp).___(sp.get__(3));
    ps.get__(startp+1).___(sp.get__(2));
    ps.get__(startp+2).___(sp.get__(1));
    ps.get__(startp+3).___(sp.get__(0));
    return startp;
} finally {
LEAVING("9eellwhg4gsa2pdszpeqihs2d","arrowEndClip");
}
}




//3 5i0vg914q5v5dzz5vo7rg9omc
// void arrowOrthoClip(edge_t* e, pointf* ps, int startp, int endp, bezier* spl, int sflag, int eflag) 
@Unused
@Original(version="2.38.0", path="lib/common/arrows.c", name="arrowOrthoClip", key="5i0vg914q5v5dzz5vo7rg9omc", definition="void arrowOrthoClip(edge_t* e, pointf* ps, int startp, int endp, bezier* spl, int sflag, int eflag)")
public static Object arrowOrthoClip(Object... arg) {
UNSUPPORTED("5cmga0193q90gs5y2r0l9ekgq"); // void arrowOrthoClip(edge_t* e, pointf* ps, int startp, int endp, bezier* spl, int sflag, int eflag)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("y7yudkjc31udfiam6z6lkpyz"); //     pointf p, q, r, s, t;
UNSUPPORTED("3kkc3p6yj8romhqyooa86wcf7"); //     double d, tlen, hlen, maxd;
UNSUPPORTED("c69aoxg5blb5c27rwb7uvguna"); //     if (sflag && eflag && (endp == startp)) { /* handle special case of two arrows on a single segment */
UNSUPPORTED("eb6qp4f6c1liqz5gv8yr4nt2u"); // 	p = ps[endp];
UNSUPPORTED("ecphms6syi9sh7jtisdvhb8hr"); // 	q = ps[endp+3];
UNSUPPORTED("2pzsi9r63yv2o8qeounzv6cny"); // 	tlen = arrow_length (e, sflag);
UNSUPPORTED("f4d86okjchj0qyg2roq13hufh"); // 	hlen = arrow_length (e, eflag);
UNSUPPORTED("3sbhjktcu1u1avngc5ej62mw4"); //         d = DIST(p, q);
UNSUPPORTED("bsdcbs5e8tkm1802lidu0jtw8"); // 	if (hlen + tlen >= d) {
UNSUPPORTED("8gpoj60hh2teibwc83s0ii79w"); // 	    hlen = tlen = d/3.0;
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("6xtkcedj7la7fqplc0unqj0wx"); // 	if (p.y == q.y) { /* horz segment */
UNSUPPORTED("9n0q5j1nqa19z0zoz3mpmwpdv"); // 	    s.y = t.y = p.y;
UNSUPPORTED("c2tle7mztwggexoad4drqjw0a"); // 	    if (p.x < q.x) {
UNSUPPORTED("183kgzstrmgynznfkfj0jl3df"); // 		t.x = q.x - hlen;
UNSUPPORTED("7cugpgpm4lyr66kkhauqj5qvy"); // 		s.x = p.x + tlen;
UNSUPPORTED("6t98dcecgbvbvtpycwiq2ynnj"); // 	    }
UNSUPPORTED("6q044im7742qhglc4553noina"); // 	    else {
UNSUPPORTED("a8lrkw50xbjo3ntsv0r1mz5i9"); // 		t.x = q.x + hlen;
UNSUPPORTED("37zp6lexzsbm2vomf22x7i5r"); // 		s.x = p.x - tlen;
UNSUPPORTED("6t98dcecgbvbvtpycwiq2ynnj"); // 	    }
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("4l6yfu81669thfh19tmcn44pu"); // 	else {            /* vert segment */
UNSUPPORTED("bc0n1oxhmb3wgphgm1w4n9dz1"); // 	    s.x = t.x = p.x;
UNSUPPORTED("d2pzq44lkkxam6rx01xnozquf"); // 	    if (p.y < q.y) {
UNSUPPORTED("5k5qyffqi7gacnu4jwl6efngx"); // 		t.y = q.y - hlen;
UNSUPPORTED("7ppaznbfc8awmm6e9d9qzw4ms"); // 		s.y = p.y + tlen;
UNSUPPORTED("6t98dcecgbvbvtpycwiq2ynnj"); // 	    }
UNSUPPORTED("6q044im7742qhglc4553noina"); // 	    else {
UNSUPPORTED("8ohhvcqa5v7oor1gbpznb6faq"); // 		t.y = q.y + hlen;
UNSUPPORTED("4j6guu6e5ddqobe77kt7sbmjq"); // 		s.y = p.y - tlen;
UNSUPPORTED("6t98dcecgbvbvtpycwiq2ynnj"); // 	    }
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("a4d4fgiq4l3sbeb9ud8dowkby"); // 	ps[endp] = ps[endp + 1] = s;
UNSUPPORTED("db740uoo9pfyknnmi2yx0glgb"); // 	ps[endp + 2] = ps[endp + 3] = t;
UNSUPPORTED("ewajj4utlr95mfmaswtc9yeiv"); // 	spl->eflag = eflag, spl->ep = p;
UNSUPPORTED("9bgf1pn9yx1vlolgcjos2emsl"); // 	spl->sflag = sflag, spl->sp = q;
UNSUPPORTED("a7fgam0j0jm7bar0mblsv3no4"); // 	return;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("5zse8mf1iyqtzzlq2txqcym6x"); //     if (eflag) {
UNSUPPORTED("bpf9snlwegftq8d78l9hsz76b"); // 	hlen = arrow_length(e, eflag);
UNSUPPORTED("eb6qp4f6c1liqz5gv8yr4nt2u"); // 	p = ps[endp];
UNSUPPORTED("ecphms6syi9sh7jtisdvhb8hr"); // 	q = ps[endp+3];
UNSUPPORTED("3sbhjktcu1u1avngc5ej62mw4"); //         d = DIST(p, q);
UNSUPPORTED("9b0ae4jocdkvqt8r3iw39yf5d"); // 	maxd = 0.9*d;
UNSUPPORTED("bwzkrhk431iwhs6c467tb0yh9"); // 	if (hlen >= maxd) {   /* arrow too long */
UNSUPPORTED("23uwvl5a8msik1u1crb262nqj"); // 	    hlen = maxd;
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("6xtkcedj7la7fqplc0unqj0wx"); // 	if (p.y == q.y) { /* horz segment */
UNSUPPORTED("a851ewci39wssny4nn99f4nmr"); // 	    r.y = p.y;
UNSUPPORTED("a2dyb0em7hwd4qdx1u0tuc8pl"); // 	    if (p.x < q.x) r.x = q.x - hlen;
UNSUPPORTED("90ksto8lyojedi0p77l4zm7x"); // 	    else r.x = q.x + hlen;
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("4l6yfu81669thfh19tmcn44pu"); // 	else {            /* vert segment */
UNSUPPORTED("4rh9lai2dcsutwg48bb2qljyg"); // 	    r.x = p.x;
UNSUPPORTED("6gnp9tso58zn1rn4j7jv3i1y0"); // 	    if (p.y < q.y) r.y = q.y - hlen;
UNSUPPORTED("3wd6fw8km4tp6a1p9ijk343ih"); // 	    else r.y = q.y + hlen;
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("bxxlk6noh1kzyi93fptcz29j4"); // 	ps[endp + 1] = p;
UNSUPPORTED("d3kionq4ycqr87orc5vkdnse0"); // 	ps[endp + 2] = ps[endp + 3] = r;
UNSUPPORTED("4uwxjmxybnuriwua5xoo17bfa"); // 	spl->eflag = eflag;
UNSUPPORTED("25oo9o1uy5fisoodt43sio6zx"); // 	spl->ep = q;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("3297fx8lk8bjyg998l9ujeeph"); //     if (sflag) {
UNSUPPORTED("5slqgq5wsgplyy9uj9mg5pkrc"); // 	tlen = arrow_length(e, sflag);
UNSUPPORTED("ayxhimnpo6p08kshlux75qpcu"); // 	p = ps[startp];
UNSUPPORTED("2ydx1urmjnn1tgx6ffzsvwimx"); // 	q = ps[startp+3];
UNSUPPORTED("3sbhjktcu1u1avngc5ej62mw4"); //         d = DIST(p, q);
UNSUPPORTED("9b0ae4jocdkvqt8r3iw39yf5d"); // 	maxd = 0.9*d;
UNSUPPORTED("1uya1cfbkj8b6j38zbvdxmgrq"); // 	if (tlen >= maxd) {   /* arrow too long */
UNSUPPORTED("3ydle9u127f7saxiibosc2lxs"); // 	    tlen = maxd;
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("6xtkcedj7la7fqplc0unqj0wx"); // 	if (p.y == q.y) { /* horz segment */
UNSUPPORTED("a851ewci39wssny4nn99f4nmr"); // 	    r.y = p.y;
UNSUPPORTED("7xq2f46jfu6rsd83fqyr71z26"); // 	    if (p.x < q.x) r.x = p.x + tlen;
UNSUPPORTED("8gtrjqabiq8x8jl0j2eveiugg"); // 	    else r.x = p.x - tlen;
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("4l6yfu81669thfh19tmcn44pu"); // 	else {            /* vert segment */
UNSUPPORTED("4rh9lai2dcsutwg48bb2qljyg"); // 	    r.x = p.x;
UNSUPPORTED("es4i2rg7sahthpreieu5hcwl7"); // 	    if (p.y < q.y) r.y = p.y + tlen;
UNSUPPORTED("26o5nwhklplaxveikjpxzxoom"); // 	    else r.y = p.y - tlen;
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("5pnx91gtz1gahdprf0syc4yk5"); // 	ps[startp] = ps[startp + 1] = r;
UNSUPPORTED("3e3iux8uecaf6eu2s9q46clr5"); // 	ps[startp + 2] = q;
UNSUPPORTED("bmeeipd0o72kslox40628z9gj"); // 	spl->sflag = sflag;
UNSUPPORTED("dwq656v3u8zmbqdetlo0wmyeb"); // 	spl->sp = p;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




public static CFunction arrow_type_normal = new CFunctionAbstract("arrow_type_normal") {
	
	public Object exe(Globals zz, Object... args) {
		return arrow_type_normal(args);
	}};
@Unused
@Original(version="2.38.0", path="lib/common/arrows.c", name="arrow_type_normal", key="b7nm38od2nxotpyzxg0ychqdb", definition="static void arrow_type_normal(GVJ_t * job, pointf p, pointf u, double arrowsize, double penwidth, int flag)")
public static Object arrow_type_normal(Object... arg_) {
UNSUPPORTED("bk3aihjbdtkitpdvvtmzbt2zu"); // static void arrow_type_normal(GVJ_t * job, pointf p, pointf u, double arrowsize, double penwidth, int flag)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("4z3ehq6q8ofvyjy4r4mrg86cl"); //     pointf q, v, a[5];
UNSUPPORTED("5qr741c2e1pdivv0bbjcr6cik"); //     double arrowwidth;
UNSUPPORTED("9gahmv6psjnccqesq7xt6q6hx"); //     arrowwidth = 0.35;
UNSUPPORTED("bih7b1ef9lfxy4uy4o07ae3bk"); //     if (penwidth > 4)
UNSUPPORTED("o3yn0730zlzconddqarivq05"); //         arrowwidth *= penwidth / 4;
UNSUPPORTED("949z3586m713okvlu1ymyw1us"); //     v.x = -u.y * arrowwidth;
UNSUPPORTED("c8xqr3hfd006yf1uuh47ndi71"); //     v.y = u.x * arrowwidth;
UNSUPPORTED("eh1wtktxp1goqc7akjgyqf00j"); //     q.x = p.x + u.x;
UNSUPPORTED("3op9xoxcy2m0v7q790ta7f9hl"); //     q.y = p.y + u.y;
UNSUPPORTED("eh6fh3kco3kvywuta8d8yb5v0"); //     if (flag & (1<<(4+1))) {
UNSUPPORTED("3by9fwhdnq30ll5nt8qbfwx0p"); // 	a[0] = a[4] = p;
UNSUPPORTED("4ut2bmvsgdsemxiv8urham7m3"); // 	a[1].x = p.x - v.x;
UNSUPPORTED("asa1ffrrxd6dkzm1sdamgofuu"); // 	a[1].y = p.y - v.y;
UNSUPPORTED("60oc061ln68pvrg7zp8s3ncog"); // 	a[2] = q;
UNSUPPORTED("591i8kbz6r8bskar4gy0vpsus"); // 	a[3].x = p.x + v.x;
UNSUPPORTED("7i9sj4qz5f52w1wiz47bo6dv2"); // 	a[3].y = p.y + v.y;
UNSUPPORTED("c07up7zvrnu2vhzy6d7zcu94g"); //     } else {
UNSUPPORTED("6ofxgqmmh2ikk8818bf8aw2mw"); // 	a[0] = a[4] = q;
UNSUPPORTED("4l3g4pagkn0dto3bwi2e0bukd"); // 	a[1].x = q.x - v.x;
UNSUPPORTED("9wv7w8vdiedhkfehzqwyyv897"); // 	a[1].y = q.y - v.y;
UNSUPPORTED("d0mui3zxt1cx6mx5wfax35iah"); // 	a[2] = p;
UNSUPPORTED("b5arwmpck5jtms8g1zgnojj5o"); // 	a[3].x = q.x + v.x;
UNSUPPORTED("3viyneb6qkp0alwghd7mo06cc"); // 	a[3].y = q.y + v.y;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("3kac8n2z06u2z5pqkm7c4z4nl"); //     if (flag & (1<<(4+2)))
UNSUPPORTED("52punwd4fhhq1arhez8cuwvam"); // 	gvrender_polygon(job, a, 3, !(flag & (1<<(4+0))));
UNSUPPORTED("9i420px3t1z2sosclutiev22e"); //     else if (flag & (1<<(4+3)))
UNSUPPORTED("gu9pg6c0d0uzlztdiq2o96zh"); // 	gvrender_polygon(job, &a[2], 3, !(flag & (1<<(4+0))));
UNSUPPORTED("div10atae09n36x269sl208r1"); //     else
UNSUPPORTED("7c6e70h4efa2wpa155lfy3er5"); // 	gvrender_polygon(job, &a[1], 3, !(flag & (1<<(4+0))));
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




public static CFunction arrow_type_crow = new CFunctionAbstract("arrow_type_crow") {
	
	public Object exe(Globals zz, Object... args) {
		return arrow_type_crow(args);
	}};
@Unused
@Original(version="2.38.0", path="lib/common/arrows.c", name="arrow_type_crow", key="b6y46a44yguy7zuhgxukxnq79", definition="static void arrow_type_crow(GVJ_t * job, pointf p, pointf u, double arrowsize, double penwidth, int flag)")
public static Object arrow_type_crow(Object... arg) {
UNSUPPORTED("6rtaogz992ixfhc4qfzpl9pw8"); // static void arrow_type_crow(GVJ_t * job, pointf p, pointf u, double arrowsize, double penwidth, int flag)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("1hutab8l2bf18ywdh16qls7ix"); //     pointf m, q, v, w, a[9];
UNSUPPORTED("9riilhm03el22oazbpvsntuyd"); //     double arrowwidth, shaftwidth;
UNSUPPORTED("3pjnxs55moxekjdkzcgmes60h"); //     arrowwidth = 0.45;
UNSUPPORTED("7k8ulzafyheqq2wcj2ued1mll"); //     if (penwidth > (4 * arrowsize) && (flag & (1<<(4+1))))
UNSUPPORTED("85aamjzbblwnk2bp9jvnetpmf"); //         arrowwidth *= penwidth / (4 * arrowsize);
UNSUPPORTED("ykk0490b4cs8a1v9gpqx95we"); //     shaftwidth = 0;
UNSUPPORTED("61i60vknj2akb0ojgfdcnjjyh"); //     if (penwidth > 1 && (flag & (1<<(4+1))))
UNSUPPORTED("ak0a6438e5b0b2nu2wl572f6d"); // 	shaftwidth = 0.05 * (penwidth - 1) / arrowsize;   /* arrowsize to cancel the arrowsize term already in u */
UNSUPPORTED("949z3586m713okvlu1ymyw1us"); //     v.x = -u.y * arrowwidth;
UNSUPPORTED("c8xqr3hfd006yf1uuh47ndi71"); //     v.y = u.x * arrowwidth;
UNSUPPORTED("56jq7ic46net8ort3ve4st2tw"); //     w.x = -u.y * shaftwidth;
UNSUPPORTED("ampfe31k0mn8vsflzy5cx4lgv"); //     w.y = u.x * shaftwidth;
UNSUPPORTED("eh1wtktxp1goqc7akjgyqf00j"); //     q.x = p.x + u.x;
UNSUPPORTED("3op9xoxcy2m0v7q790ta7f9hl"); //     q.y = p.y + u.y;
UNSUPPORTED("cfk5fnc73y4ey68iwqxlsr7u9"); //     m.x = p.x + u.x * 0.5;
UNSUPPORTED("2mtpxqz0h7jsb7bmao9g1vkar"); //     m.y = p.y + u.y * 0.5;
UNSUPPORTED("5rymactsr9099qy69qf1bkwdr"); //     if (flag & (1<<(4+1))) {  /* vee */
UNSUPPORTED("4s8pxkc9kbvhabi2gbpxic5sm"); // 	a[0] = a[8] = p;
UNSUPPORTED("4l3g4pagkn0dto3bwi2e0bukd"); // 	a[1].x = q.x - v.x;
UNSUPPORTED("9wv7w8vdiedhkfehzqwyyv897"); // 	a[1].y = q.y - v.y;
UNSUPPORTED("6rqmfaf4g98cg0t1qaax08e69"); // 	a[2].x = m.x - w.x;
UNSUPPORTED("1nwyu905ao88kvq8pgnfwerf5"); // 	a[2].y = m.y - w.y;
UNSUPPORTED("9z1frd2w5h9zvfo4pszqlzp30"); // 	a[3].x = q.x - w.x;
UNSUPPORTED("1s86b9xef6phy3gt70ojgn6ip"); // 	a[3].y = q.y - w.y;
UNSUPPORTED("4zd6xleeq5n1l2zznquvnlw7v"); // 	a[4] = q;
UNSUPPORTED("2wb1104b1x08j7tecfcwk25uj"); // 	a[5].x = q.x + w.x;
UNSUPPORTED("6tq7rnp0h0p9xtxibo13g8v3t"); // 	a[5].y = q.y + w.y;
UNSUPPORTED("7byonl28yipw4lk1syuuj51it"); // 	a[6].x = m.x + w.x;
UNSUPPORTED("lwb6vnlr2dq5ysijij21wbgl"); // 	a[6].y = m.y + w.y;
UNSUPPORTED("eh4rkk1h3ciybh0u0hgehkdxx"); // 	a[7].x = q.x + v.x;
UNSUPPORTED("bxhyoes561jf42tw73tjj33sj"); // 	a[7].y = q.y + v.y;
UNSUPPORTED("54abbljqrd361peswxjtohjg0"); //     } else {                     /* crow */
UNSUPPORTED("5i9r1mehhwkkn8ojo8csm0piw"); // 	a[0] = a[8] = q;
UNSUPPORTED("4ut2bmvsgdsemxiv8urham7m3"); // 	a[1].x = p.x - v.x;
UNSUPPORTED("asa1ffrrxd6dkzm1sdamgofuu"); // 	a[1].y = p.y - v.y;
UNSUPPORTED("6rqmfaf4g98cg0t1qaax08e69"); // 	a[2].x = m.x - w.x;
UNSUPPORTED("1nwyu905ao88kvq8pgnfwerf5"); // 	a[2].y = m.y - w.y;
UNSUPPORTED("5yhpyznqsxb2ga5si6phvakqg"); // 	a[3].x = p.x;
UNSUPPORTED("3eko75yr046fkm2yulawhw236"); // 	a[3].y = p.y;
UNSUPPORTED("7nofpsbtwg78gooeo8makz5bb"); // 	a[4] = p;
UNSUPPORTED("dl1jq9xglce6cfpe03mzu3p22"); // 	a[5].x = p.x;
UNSUPPORTED("8kqrguezr1d2awpazfz88cw5"); // 	a[5].y = p.y;
UNSUPPORTED("7byonl28yipw4lk1syuuj51it"); // 	a[6].x = m.x + w.x;
UNSUPPORTED("lwb6vnlr2dq5ysijij21wbgl"); // 	a[6].y = m.y + w.y;
UNSUPPORTED("egqlf021ldci9s31jrpk2m1pk"); // 	a[7].x = p.x + v.x;
UNSUPPORTED("60b7k5jzqw1ndaxnese0cnx2t"); // 	a[7].y = p.y + v.y;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("3kac8n2z06u2z5pqkm7c4z4nl"); //     if (flag & (1<<(4+2)))
UNSUPPORTED("8vyuq79k664wims1n1ltnudbt"); // 	gvrender_polygon(job, a, 6, 1);
UNSUPPORTED("9i420px3t1z2sosclutiev22e"); //     else if (flag & (1<<(4+3)))
UNSUPPORTED("ap1vbm2kxsjtcp3pezwq2jwln"); // 	gvrender_polygon(job, &a[3], 6, 1);
UNSUPPORTED("div10atae09n36x269sl208r1"); //     else
UNSUPPORTED("d0npn4cmn6nd0ytaww7u7ghrw"); // 	gvrender_polygon(job, a, 9, 1);
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




public static CFunction arrow_type_gap = new CFunctionAbstract("arrow_type_gap") {
	
	public Object exe(Globals zz, Object... args) {
		return arrow_type_gap(args);
	}};
@Unused
@Original(version="2.38.0", path="lib/common/arrows.c", name="arrow_type_gap", key="e8w54seijyii7km6zl3sivjpu", definition="static void arrow_type_gap(GVJ_t * job, pointf p, pointf u, double arrowsize, double penwidth, int flag)")
public static Object arrow_type_gap(Object... arg) {
UNSUPPORTED("anlswsxb36i1znu2805bu47t2"); // static void arrow_type_gap(GVJ_t * job, pointf p, pointf u, double arrowsize, double penwidth, int flag)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("brf7jajkh244o41ekaw2tyora"); //     pointf q, a[2];
UNSUPPORTED("eh1wtktxp1goqc7akjgyqf00j"); //     q.x = p.x + u.x;
UNSUPPORTED("3op9xoxcy2m0v7q790ta7f9hl"); //     q.y = p.y + u.y;
UNSUPPORTED("dhfgavaa2js7qt2ciwujmmrpv"); //     a[0] = p;
UNSUPPORTED("7b2bztb06255tydz21zauq8qq"); //     a[1] = q;
UNSUPPORTED("9hbag2bcttyxj9vas0kvof5qp"); //     gvrender_polyline(job, a, 2);
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




public static CFunction arrow_type_tee = new CFunctionAbstract("arrow_type_tee") {
	
	public Object exe(Globals zz, Object... args) {
		return arrow_type_tee(args);
	}};
@Unused
@Original(version="2.38.0", path="lib/common/arrows.c", name="arrow_type_tee", key="eg7sgk8umcqfthbo1t0plohbt", definition="static void arrow_type_tee(GVJ_t * job, pointf p, pointf u, double arrowsize, double penwidth, int flag)")
public static Object arrow_type_tee(Object... arg) {
UNSUPPORTED("9u6pwrzl9t5i0kfvnwn7uufrp"); // static void arrow_type_tee(GVJ_t * job, pointf p, pointf u, double arrowsize, double penwidth, int flag)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("1hwyzphi1ihi2lx3engtta1qa"); //     pointf m, n, q, v, a[4];
UNSUPPORTED("e8sltpxlu9ln6k3zn49u46zau"); //     v.x = -u.y;
UNSUPPORTED("1wxxf464go09wrecpyo3y2k25"); //     v.y = u.x;
UNSUPPORTED("eh1wtktxp1goqc7akjgyqf00j"); //     q.x = p.x + u.x;
UNSUPPORTED("3op9xoxcy2m0v7q790ta7f9hl"); //     q.y = p.y + u.y;
UNSUPPORTED("61cj5vg96f8j145swk5v2nz5a"); //     m.x = p.x + u.x * 0.2;
UNSUPPORTED("25zmgst5hb9ya664up64k51tt"); //     m.y = p.y + u.y * 0.2;
UNSUPPORTED("9wrt18toae33nd3tmifyahyt8"); //     n.x = p.x + u.x * 0.6;
UNSUPPORTED("1151hzer2rx55qz715on0gexs"); //     n.y = p.y + u.y * 0.6;
UNSUPPORTED("4ihm8x9khys2bcoivyqzf4dth"); //     a[0].x = m.x + v.x;
UNSUPPORTED("941h30wfi9u1c17vhabko438l"); //     a[0].y = m.y + v.y;
UNSUPPORTED("7qtkiyjyg6pzhhoyslv8pmp1q"); //     a[1].x = m.x - v.x;
UNSUPPORTED("24iwwf6paxfgux41w93obhwp4"); //     a[1].y = m.y - v.y;
UNSUPPORTED("314g2i9mvbz4um5y7oiyuldvw"); //     a[2].x = n.x - v.x;
UNSUPPORTED("7ebhg7671hxu0useo7ewi26kn"); //     a[2].y = n.y - v.y;
UNSUPPORTED("8mc60oc3vsykq69a5zb6h72u4"); //     a[3].x = n.x + v.x;
UNSUPPORTED("6q0rldgbg1rfr4skqzq0v099f"); //     a[3].y = n.y + v.y;
UNSUPPORTED("9u73bl75ej5xy9pe46nac6ih5"); //     if (flag & (1<<(4+2))) {
UNSUPPORTED("6omh7vjmab159riw0fejjpwk0"); // 	a[0] = m;
UNSUPPORTED("3w139dwzvrzrghf5w8hox1qen"); // 	a[3] = n;
UNSUPPORTED("2u9qb4zvcio06wzd2nb3bjrs4"); //     } else if (flag & (1<<(4+3))) {
UNSUPPORTED("7g1lyxw6yo0ycv1l688mehedr"); // 	a[1] = m;
UNSUPPORTED("72r460jtb2id4k8ri9sdwuqy4"); // 	a[2] = n;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("15b4dosag4vcx31fnicxczw81"); //     gvrender_polygon(job, a, 4, 1);
UNSUPPORTED("dhfgavaa2js7qt2ciwujmmrpv"); //     a[0] = p;
UNSUPPORTED("7b2bztb06255tydz21zauq8qq"); //     a[1] = q;
UNSUPPORTED("9hbag2bcttyxj9vas0kvof5qp"); //     gvrender_polyline(job, a, 2);
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




public static CFunction arrow_type_box = new CFunctionAbstract("arrow_type_box") {
	
	public Object exe(Globals zz, Object... args) {
		return arrow_type_box(args);
	}};
@Unused
@Original(version="2.38.0", path="lib/common/arrows.c", name="arrow_type_box", key="3hdgy0baje1akb7fjw9yovjwz", definition="static void arrow_type_box(GVJ_t * job, pointf p, pointf u, double arrowsize, double penwidth, int flag)")
public static Object arrow_type_box(Object... arg) {
UNSUPPORTED("4u7yj9rhqxdonlyd5taprxs28"); // static void arrow_type_box(GVJ_t * job, pointf p, pointf u, double arrowsize, double penwidth, int flag)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("5lmvyh791uemv9zn9qfakp4qb"); //     pointf m, q, v, a[4];
UNSUPPORTED("ep2toe83b4kkoeb00wqwru73t"); //     v.x = -u.y * 0.4;
UNSUPPORTED("8c686kltb5ewlt4qkf7ljmmc8"); //     v.y = u.x * 0.4;
UNSUPPORTED("66oe3r3caie8ddr9mgkqkk0r3"); //     m.x = p.x + u.x * 0.8;
UNSUPPORTED("1mowxzv768r12lzc4blbkl873"); //     m.y = p.y + u.y * 0.8;
UNSUPPORTED("eh1wtktxp1goqc7akjgyqf00j"); //     q.x = p.x + u.x;
UNSUPPORTED("3op9xoxcy2m0v7q790ta7f9hl"); //     q.y = p.y + u.y;
UNSUPPORTED("apz7b1n6lnsr7u47d661qkf7r"); //     a[0].x = p.x + v.x;
UNSUPPORTED("eqdvtg0j9avxbrpfozktn2358"); //     a[0].y = p.y + v.y;
UNSUPPORTED("f0vjjdkq6txr0nrc7xjvcxpa9"); //     a[1].x = p.x - v.x;
UNSUPPORTED("4e5dina8t36xvh3bfr8y98a9m"); //     a[1].y = p.y - v.y;
UNSUPPORTED("4npxatuz83si51hcyaeshl3x3"); //     a[2].x = m.x - v.x;
UNSUPPORTED("br2saagm87ysykkosh9e0xjab"); //     a[2].y = m.y - v.y;
UNSUPPORTED("8kb1ee4fjdywi21l0xydfefb4"); //     a[3].x = m.x + v.x;
UNSUPPORTED("coalhyxqy4kj07zi50yoc48fy"); //     a[3].y = m.y + v.y;
UNSUPPORTED("9u73bl75ej5xy9pe46nac6ih5"); //     if (flag & (1<<(4+2))) {
UNSUPPORTED("5fbtaluh9dcnwehl25ff3obkc"); // 	a[0] = p;
UNSUPPORTED("9bjq8dynp7r5d7sbwtodjucxc"); // 	a[3] = m;
UNSUPPORTED("2u9qb4zvcio06wzd2nb3bjrs4"); //     } else if (flag & (1<<(4+3))) {
UNSUPPORTED("e2cqpiig8ac96q5ovh3nyr7t1"); // 	a[1] = p;
UNSUPPORTED("4uekqgdzqn4bhxlllod39w27b"); // 	a[2] = m;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("7y7z0oxvzng5clk605r3p4zz8"); //     gvrender_polygon(job, a, 4, !(flag & (1<<(4+0))));
UNSUPPORTED("1p9lzfwbik1778u5gdeqpxm31"); //     a[0] = m;
UNSUPPORTED("7b2bztb06255tydz21zauq8qq"); //     a[1] = q;
UNSUPPORTED("9hbag2bcttyxj9vas0kvof5qp"); //     gvrender_polyline(job, a, 2);
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




public static CFunction arrow_type_diamond = new CFunctionAbstract("arrow_type_diamond") {
	
	public Object exe(Globals zz, Object... args) {
		return arrow_type_diamond(args);
	}};
@Unused
@Original(version="2.38.0", path="lib/common/arrows.c", name="arrow_type_diamond", key="equc1q4r6wcoe2pwwnk2u01og", definition="static void arrow_type_diamond(GVJ_t * job, pointf p, pointf u, double arrowsize, double penwidth, int flag)")
public static Object arrow_type_diamond(Object... arg) {
UNSUPPORTED("4wg2b1eyit9ve72uqrds41jk2"); // static void arrow_type_diamond(GVJ_t * job, pointf p, pointf u, double arrowsize, double penwidth, int flag)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("7ww1c4ncz55av20zgtif7choz"); //     pointf q, r, v, a[5];
UNSUPPORTED("1xsjvcsxdwie2v666ry6ihghq"); //     v.x = -u.y / 3.;
UNSUPPORTED("9xea2lahdijfjgz0skow4ps5m"); //     v.y = u.x / 3.;
UNSUPPORTED("4p3vbsracdkwmi6dly3odymvb"); //     r.x = p.x + u.x / 2.;
UNSUPPORTED("7y5j7ecroy4fci5jzh6mk2sl9"); //     r.y = p.y + u.y / 2.;
UNSUPPORTED("eh1wtktxp1goqc7akjgyqf00j"); //     q.x = p.x + u.x;
UNSUPPORTED("3op9xoxcy2m0v7q790ta7f9hl"); //     q.y = p.y + u.y;
UNSUPPORTED("8n36qnwzpe2hugs30s3am1zsg"); //     a[0] = a[4] = q;
UNSUPPORTED("e97x8nztsokegzu9u2tgwssgi"); //     a[1].x = r.x + v.x;
UNSUPPORTED("3yv64xfvqgiyturtkz9pnkbtd"); //     a[1].y = r.y + v.y;
UNSUPPORTED("3isw1pofpfafh53xdqo1gmbrr"); //     a[2] = p;
UNSUPPORTED("655732redrvi9o3a3dyjh90af"); //     a[3].x = r.x - v.x;
UNSUPPORTED("55u2biopicz70n81k5ml5lzjp"); //     a[3].y = r.y - v.y;
UNSUPPORTED("3kac8n2z06u2z5pqkm7c4z4nl"); //     if (flag & (1<<(4+2)))
UNSUPPORTED("gu9pg6c0d0uzlztdiq2o96zh"); // 	gvrender_polygon(job, &a[2], 3, !(flag & (1<<(4+0))));
UNSUPPORTED("9i420px3t1z2sosclutiev22e"); //     else if (flag & (1<<(4+3)))
UNSUPPORTED("52punwd4fhhq1arhez8cuwvam"); // 	gvrender_polygon(job, a, 3, !(flag & (1<<(4+0))));
UNSUPPORTED("div10atae09n36x269sl208r1"); //     else
UNSUPPORTED("6qac6zmwtg6rwpmn73fq6poje"); // 	gvrender_polygon(job, a, 4, !(flag & (1<<(4+0))));
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




public static CFunction arrow_type_dot = new CFunctionAbstract("arrow_type_dot") {
	
	public Object exe(Globals zz, Object... args) {
		return arrow_type_dot(args);
	}};
@Unused
@Original(version="2.38.0", path="lib/common/arrows.c", name="arrow_type_dot", key="dxl50r7ooipvtkyjb0sleittd", definition="static void arrow_type_dot(GVJ_t * job, pointf p, pointf u, double arrowsize, double penwidth, int flag)")
public static Object arrow_type_dot(Object... arg) {
UNSUPPORTED("bsrxktb5cvoy4qewxrb3z3ht"); // static void arrow_type_dot(GVJ_t * job, pointf p, pointf u, double arrowsize, double penwidth, int flag)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("e077smjlsfuq1ptdwqpn7lcf0"); //     double r;
UNSUPPORTED("c1555k4n8zggr5m5nozuhequ8"); //     pointf AF[2];
UNSUPPORTED("9nadrv289g5ravobyhu05u9or"); //     r = sqrt(u.x * u.x + u.y * u.y) / 2.;
UNSUPPORTED("bxxvjt8g03vytuyx531n55b1g"); //     AF[0].x = p.x + u.x / 2. - r;
UNSUPPORTED("8lbe9l6h0hqnth5j5skwrhxx1"); //     AF[0].y = p.y + u.y / 2. - r;
UNSUPPORTED("1krebba2swwkp12jt15xqkjqd"); //     AF[1].x = p.x + u.x / 2. + r;
UNSUPPORTED("btubzlppo1x1284g7zu99lk03"); //     AF[1].y = p.y + u.y / 2. + r;
UNSUPPORTED("7mu57g14vmt1bdc523s4wiy22"); //     gvrender_ellipse(job, AF, 2, !(flag & (1<<(4+0))));
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




public static CFunction arrow_type_curve = new CFunctionAbstract("arrow_type_curve") {
	
	public Object exe(Globals zz, Object... args) {
		return arrow_type_curve(args);
	}};
@Unused
@Original(version="2.38.0", path="lib/common/arrows.c", name="arrow_type_curve", key="5oioemwdl3g1maj3ikzleo0nm", definition="static void arrow_type_curve(GVJ_t* job, pointf p, pointf u, double arrowsize, double penwidth, int flag)")
public static Object arrow_type_curve(Object... arg) {
UNSUPPORTED("2rt93fe18qb092yomrw5l6mko"); // static void arrow_type_curve(GVJ_t* job, pointf p, pointf u, double arrowsize, double penwidth, int flag)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("7127v6bnaxe4g216ue7fn3fyp"); //     double arrowwidth = penwidth > 4 ? 0.5 * penwidth / 4 : 0.5;
UNSUPPORTED("euvu8ayo5swdxux8kyjmrywr5"); //     pointf q, v, w;
UNSUPPORTED("9jc1l9d43c00gutvyto6snss3"); //     pointf AF[4], a[2];
UNSUPPORTED("eh1wtktxp1goqc7akjgyqf00j"); //     q.x = p.x + u.x;
UNSUPPORTED("4qmtmdrrceo9h0mka0fkj5xci"); //     q.y = p.y + u.y; 
UNSUPPORTED("bvibgke8hnjr9rlyzfdumk5w"); //     v.x = -u.y * arrowwidth; 
UNSUPPORTED("c8xqr3hfd006yf1uuh47ndi71"); //     v.y = u.x * arrowwidth;
UNSUPPORTED("duqsnax25spvj38rnd9v32b4d"); //     w.x = v.y; // same direction as u, same magnitude as v.
UNSUPPORTED("4srsifkr8qc1viu2xijhnt66s"); //     w.y = -v.x;
UNSUPPORTED("dhfgavaa2js7qt2ciwujmmrpv"); //     a[0] = p;
UNSUPPORTED("7b2bztb06255tydz21zauq8qq"); //     a[1] = q;
UNSUPPORTED("11fvrbnzwbuar9l5gc9wurbor"); //     AF[0].x = p.x + v.x + w.x;
UNSUPPORTED("34xjxb9aaiworzwsfodegww0g"); //     AF[0].y = p.y + v.y + w.y;
UNSUPPORTED("73ax76wsls8wr2c86mm6umxkl"); //     AF[3].x = p.x - v.x + w.x;
UNSUPPORTED("7q0ly8njmscobyx3v5u7xb59c"); //     AF[3].y = p.y - v.y + w.y;
UNSUPPORTED("9qmj4a7f67dltmu1pte8pzqox"); //     AF[1].x = p.x + 0.95 * v.x + w.x - w.x * 4.0 / 3.0;
UNSUPPORTED("5spg9wtj9dwqh43yhk0dqfija"); //     AF[1].y = AF[0].y - w.y * 4.0 / 3.0;
UNSUPPORTED("ab3yikrvtlncw10ivdxvr4a52"); //     AF[2].x = p.x - 0.95 * v.x + w.x - w.x * 4.0 / 3.0;
UNSUPPORTED("45ok1xa7ia1ugs2o8ediwmd5p"); //     AF[2].y = AF[3].y - w.y * 4.0 / 3.0;
UNSUPPORTED("9hbag2bcttyxj9vas0kvof5qp"); //     gvrender_polyline(job, a, 2);
UNSUPPORTED("3kac8n2z06u2z5pqkm7c4z4nl"); //     if (flag & (1<<(4+2)))
UNSUPPORTED("58oum3y30wqa0bofgze20unn7"); // 	Bezier(AF, 3, 0.5, NULL, AF);
UNSUPPORTED("9i420px3t1z2sosclutiev22e"); //     else if (flag & (1<<(4+3)))
UNSUPPORTED("8dngt8wih972oouawx4wska6k"); // 	Bezier(AF, 3, 0.5, AF, NULL);
UNSUPPORTED("78tsqk66ihei67oy2ptr6t6f3"); //     gvrender_beziercurve(job, AF, sizeof(AF) / sizeof(pointf), 0, 0, 0);
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 ruebmb0rzoin79tmkp4o357x
// static pointf arrow_gen_type(GVJ_t * job, pointf p, pointf u, double arrowsize, double penwidth, int flag) 
@Unused
@Original(version="2.38.0", path="lib/common/arrows.c", name="arrow_gen_type", key="ruebmb0rzoin79tmkp4o357x", definition="static pointf arrow_gen_type(GVJ_t * job, pointf p, pointf u, double arrowsize, double penwidth, int flag)")
public static Object arrow_gen_type(Object... arg) {
UNSUPPORTED("6eekmrou08qiz0zielzyhyn4g"); // static pointf arrow_gen_type(GVJ_t * job, pointf p, pointf u, double arrowsize, double penwidth, int flag)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("9rrbht5x8qg377l1khejt2as2"); //     int f;
UNSUPPORTED("aed0rb6bb02eluj3o0ugcfqv9"); //     arrowtype_t *arrowtype;
UNSUPPORTED("ml2ttdehp7agi83yijbgk49r"); //     f = flag & ((1 << 4) - 1);
UNSUPPORTED("f036frj7aawxz98ctbodsj666"); //     for (arrowtype = Arrowtypes; arrowtype->type; arrowtype++) {
UNSUPPORTED("6qf8zxk5crelbhxfi42gd00l3"); // 	if (f == arrowtype->type) {
UNSUPPORTED("epoo24e6zcp2uaje5ukce1yvh"); // 	    u.x *= arrowtype->lenfact * arrowsize;
UNSUPPORTED("bcfjvd5s3jub6wo9roe0xmn0g"); // 	    u.y *= arrowtype->lenfact * arrowsize;
UNSUPPORTED("5wc1a7bb8k1d528kxw2uchm7c"); // 	    (arrowtype->gen) (job, p, u, arrowsize, penwidth, flag);
UNSUPPORTED("3wwns14fz356e6p4s8byp3d6i"); // 	    p.x = p.x + u.x;
UNSUPPORTED("3rzld1v7nkscibpukz3bdox3v"); // 	    p.y = p.y + u.y;
UNSUPPORTED("ai3czg6gaaxspsmndknpyvuiu"); // 	    break;
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("91xduilalb406jjyw2g1i07th"); //     return p;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 2u4vcl57jl62dmf8fy80ioppm
// boxf arrow_bb(pointf p, pointf u, double arrowsize, int flag) 
@Unused
@Original(version="2.38.0", path="lib/common/arrows.c", name="arrow_bb", key="2u4vcl57jl62dmf8fy80ioppm", definition="boxf arrow_bb(pointf p, pointf u, double arrowsize, int flag)")
public static Object arrow_bb(Object... arg) {
UNSUPPORTED("67tfc7x1j056na7s6itymoeol"); // boxf arrow_bb(pointf p, pointf u, double arrowsize, int flag)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("2fvgbj82ham8m0frx5hn9dyi"); //     double s;
UNSUPPORTED("2lzsl1e035wt5epd1h8f4bn8m"); //     boxf bb;
UNSUPPORTED("94ds3z1i0vt5rbv13ja90fdfp"); //     double ax,ay,bx,by,cx,cy,dx,dy;
UNSUPPORTED("6r1gp4hfea5imwnuiyfuxzh6k"); //     double ux2, uy2;
UNSUPPORTED("d5vh8if7unojun6hmulj4il7u"); //     /* generate arrowhead vector */
UNSUPPORTED("5yc3jb0utnnay4x88h644puhz"); //     u.x -= p.x;
UNSUPPORTED("egh8lzpdfrza6k11lopupxykp"); //     u.y -= p.y;
UNSUPPORTED("bh7ueu6dokefdmej3xz79c7ty"); //     /* the EPSILONs are to keep this stable as length of u approaches 0.0 */
UNSUPPORTED("3oao4fejxee2cop1fhd4m8tae"); //     s = 10. * arrowsize / (sqrt(u.x * u.x + u.y * u.y) + .0001);
UNSUPPORTED("8qxmhdlg9d49yg9gxkjw043"); //     u.x += (u.x >= 0.0) ? .0001 : -.0001;
UNSUPPORTED("4vxtvwh3x5b3i33sdyppe3trq"); //     u.y += (u.y >= 0.0) ? .0001 : -.0001;
UNSUPPORTED("bwi3f8xk8t2nbzy5tjtgeewjl"); //     u.x *= s;
UNSUPPORTED("do56zsbrbn95ovnoqu6zzjjmw"); //     u.y *= s;
UNSUPPORTED("alix1e6k9ywov3xxcwxcgo1hh"); //     /* compute all 4 corners of rotated arrowhead bounding box */
UNSUPPORTED("9bdmzamsx8jasbcfy2mk0v7yt"); //     ux2 = u.x / 2.;
UNSUPPORTED("3k8htwk7cas9gfe4j797zk3b"); //     uy2 = u.y / 2.;
UNSUPPORTED("ar2s2pmmxun5v6p0d4mlij1ro"); //     ax = p.x - uy2;
UNSUPPORTED("d9cpq1pbscjxjhkyi57s76o4r"); //     ay = p.y - ux2;
UNSUPPORTED("7m3bdjur8btdn3q1dzd4o751s"); //     bx = p.x + uy2;
UNSUPPORTED("bhn3rg0stek17iytsy7bgbwqw"); //     by = p.y + ux2;
UNSUPPORTED("ai8hjx4uwhzow4nolep1478xn"); //     cx = ax + u.x;
UNSUPPORTED("15l0cqg7njm4ebimncczi9uho"); //     cy = ay + u.y;
UNSUPPORTED("29117dcz6pcm4ibiebo4cemeh"); //     dx = bx + u.x;
UNSUPPORTED("7s3y5imd0u3woy1d0q58g1wlh"); //     dy = by + u.y;
UNSUPPORTED("7lzozmdnkd5c06cyxy2skrar5"); //     /* compute a right bb */
UNSUPPORTED("4shnxc3z5z4wj3l0pl7tml625"); //     bb.UR.x = MAX(ax, MAX(bx, MAX(cx, dx)));
UNSUPPORTED("2igw3asrvk13qlfbw4sgn7vxt"); //     bb.UR.y = MAX(ay, MAX(by, MAX(cy, dy)));
UNSUPPORTED("7fz9fiabx9i87t8t6bgjeso5a"); //     bb.LL.x = MIN(ax, MIN(bx, MIN(cx, dx)));
UNSUPPORTED("c6v20rdx0lfdvypx8l4tomnri"); //     bb.LL.y = MIN(ay, MIN(by, MIN(cy, dy)));
UNSUPPORTED("5v5hh30squmit8o2i5hs25eig"); //     return bb;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 8ss8m9a0p5v0yx2oqggh0rx57
// void arrow_gen(GVJ_t * job, emit_state_t emit_state, pointf p, pointf u, double arrowsize, double penwidth, int flag) 
@Unused
@Original(version="2.38.0", path="lib/common/arrows.c", name="arrow_gen", key="8ss8m9a0p5v0yx2oqggh0rx57", definition="void arrow_gen(GVJ_t * job, emit_state_t emit_state, pointf p, pointf u, double arrowsize, double penwidth, int flag)")
public static Object arrow_gen(Object... arg) {
UNSUPPORTED("ag73i6wbc5lb0d46ul40euyur"); // void arrow_gen(GVJ_t * job, emit_state_t emit_state, pointf p, pointf u, double arrowsize, double penwidth, int flag)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("84llcpxtvxaggx841n2t03850"); //     obj_state_t *obj = job->obj;
UNSUPPORTED("2fvgbj82ham8m0frx5hn9dyi"); //     double s;
UNSUPPORTED("66oipfshtfj4imr4j2x2drib0"); //     int i, f;
UNSUPPORTED("ecr1y7qy0ikxkidkdfvwv88ir"); //     emit_state_t old_emit_state;
UNSUPPORTED("c3lqudp40feg72zp97ngqkww9"); //     old_emit_state = obj->emit_state;
UNSUPPORTED("3ook7gsw0rr7b6uwm9f5a5dtx"); //     obj->emit_state = emit_state;
UNSUPPORTED("exvy7jlggpvu1zhz08fo1jbvi"); //     /* Dotted and dashed styles on the arrowhead are ugly (dds) */
UNSUPPORTED("em34eidklzv0dobtybvgz9gwu"); //     /* linewidth needs to be reset */
UNSUPPORTED("4g8oyutwebzej18aaiz74zb9k"); //     gvrender_set_style(job, job->gvc->defaultlinestyle);
UNSUPPORTED("eertb1vvqryb308a1uuff8s0"); //     gvrender_set_penwidth(job, penwidth);
UNSUPPORTED("d5vh8if7unojun6hmulj4il7u"); //     /* generate arrowhead vector */
UNSUPPORTED("5yc3jb0utnnay4x88h644puhz"); //     u.x -= p.x;
UNSUPPORTED("egh8lzpdfrza6k11lopupxykp"); //     u.y -= p.y;
UNSUPPORTED("bh7ueu6dokefdmej3xz79c7ty"); //     /* the EPSILONs are to keep this stable as length of u approaches 0.0 */
UNSUPPORTED("9s182w6wdwxo0pwu9hljlyofe"); //     s = 10. / (sqrt(u.x * u.x + u.y * u.y) + .0001);
UNSUPPORTED("8qxmhdlg9d49yg9gxkjw043"); //     u.x += (u.x >= 0.0) ? .0001 : -.0001;
UNSUPPORTED("4vxtvwh3x5b3i33sdyppe3trq"); //     u.y += (u.y >= 0.0) ? .0001 : -.0001;
UNSUPPORTED("bwi3f8xk8t2nbzy5tjtgeewjl"); //     u.x *= s;
UNSUPPORTED("do56zsbrbn95ovnoqu6zzjjmw"); //     u.y *= s;
UNSUPPORTED("3zei0bi63grn37qiuxn09n7hz"); //     /* the first arrow head - closest to node */
UNSUPPORTED("a2n8aqfq0cqpx8elstmfn9oq6"); //     for (i = 0; i < 4; i++) {
UNSUPPORTED("8sgyt5ym5jt73oknb4tdj2zpl"); //         f = (flag >> (i * 8)) & ((1 << 8) - 1);
UNSUPPORTED("5vg3retgvi5ekir9xbw8j4zoq"); // 	if (f == (0))
UNSUPPORTED("ai3czg6gaaxspsmndknpyvuiu"); // 	    break;
UNSUPPORTED("biq7xz2uj7ksjrqn6tqr9glzj"); //         p = arrow_gen_type(job, p, u, arrowsize, penwidth, f);
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("b1bkq4eyrmepbxyb3qiuhi8b8"); //     obj->emit_state = old_emit_state;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}


}
