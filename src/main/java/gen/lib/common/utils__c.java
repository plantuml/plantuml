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
import static gen.lib.cgraph.attr__c.agget;
import static gen.lib.cgraph.attr__c.agxget;
import static gen.lib.cgraph.edge__c.aghead;
import static gen.lib.cgraph.edge__c.agtail;
import static gen.lib.cgraph.obj__c.agraphof;
import static gen.lib.cgraph.refstr__c.aghtmlstr;
import static gen.lib.common.labels__c.make_label;
import static gen.lib.common.shapes__c.bind_shape;
import static gen.lib.common.shapes__c.shapeOf;
import static gen.lib.common.strcasecmp__c.strcasecmp;
import static smetana.core.JUtils.EQ_ARRAY;
import static smetana.core.JUtils.EQ_CSTRING;
import static smetana.core.JUtils.atoi;
import static smetana.core.JUtils.isdigit;
import static smetana.core.JUtils.strchr;
import static smetana.core.JUtils.strcmp;
import static smetana.core.JUtils.strtod;
import static smetana.core.JUtils.strtol;
import static smetana.core.Macro.DEFAULT_FONTSIZE;
import static smetana.core.Macro.DEFAULT_NODEHEIGHT;
import static smetana.core.Macro.DEFAULT_NODESHAPE;
import static smetana.core.Macro.DEFAULT_NODEWIDTH;
import static smetana.core.Macro.DIST2;
import static smetana.core.Macro.EDGE_LABEL;
import static smetana.core.Macro.ED_head_label;
import static smetana.core.Macro.ED_head_port;
import static smetana.core.Macro.ED_label;
import static smetana.core.Macro.ED_label_ontop;
import static smetana.core.Macro.ED_tail_label;
import static smetana.core.Macro.ED_tail_port;
import static smetana.core.Macro.ET_NONE;
import static smetana.core.Macro.GD_bb;
import static smetana.core.Macro.GD_flags;
import static smetana.core.Macro.GD_flip;
import static smetana.core.Macro.GD_has_labels;
import static smetana.core.Macro.HEAD_ID;
import static smetana.core.Macro.HEAD_LABEL;
import static smetana.core.Macro.INCH2PS;
import static smetana.core.Macro.LT_HTML;
import static smetana.core.Macro.LT_NONE;
import static smetana.core.Macro.LT_RECD;
import static smetana.core.Macro.MIN_FONTSIZE;
import static smetana.core.Macro.MIN_NODEHEIGHT;
import static smetana.core.Macro.MIN_NODEWIDTH;
import static smetana.core.Macro.ND_UF_parent;
import static smetana.core.Macro.ND_UF_size;
import static smetana.core.Macro.ND_has_port;
import static smetana.core.Macro.ND_height;
import static smetana.core.Macro.ND_ht;
import static smetana.core.Macro.ND_id;
import static smetana.core.Macro.ND_label;
import static smetana.core.Macro.ND_lw;
import static smetana.core.Macro.ND_ranktype;
import static smetana.core.Macro.ND_rw;
import static smetana.core.Macro.ND_shape;
import static smetana.core.Macro.ND_showboxes;
import static smetana.core.Macro.ND_width;
import static smetana.core.Macro.NORMAL;
import static smetana.core.Macro.TAIL_ID;
import static smetana.core.Macro.TAIL_LABEL;
import static smetana.core.Macro.UNSUPPORTED;
import static smetana.core.debug.SmetanaDebug.ENTERING;
import static smetana.core.debug.SmetanaDebug.LEAVING;

import gen.annotation.Doc;
import gen.annotation.Original;
import gen.annotation.Reviewed;
import gen.annotation.Unused;
import h.EN_shape_kind;
import h.ST_Agedge_s;
import h.ST_Agnode_s;
import h.ST_Agraph_s;
import h.ST_Agsym_s;
import h.ST_bezier;
import h.ST_boxf;
import h.ST_fontinfo;
import h.ST_nodequeue;
import h.ST_pointf;
import h.ST_port;
import h.ST_splines;
import h.ST_textlabel_t;
import smetana.core.CArray;
import smetana.core.CArrayOfStar;
import smetana.core.CFunction;
import smetana.core.CString;
import smetana.core.Globals;
import smetana.core.Memory;
import smetana.core.ZType;
import smetana.core.__ptr__;

public class utils__c {






/*
 *  a queue of nodes
 */
@Reviewed(when = "15/11/2020")
@Original(version="2.38.0", path="lib/common/utils.c", name="", key="c7cptalfn8k75wyfirbnptnav", definition="nodequeue *new_queue(int sz)")
public static ST_nodequeue new_queue(int sz) {
ENTERING("c7cptalfn8k75wyfirbnptnav","new_queue");
try {
    ST_nodequeue q = new ST_nodequeue();
    
    if (sz <= 1)
	sz = 2;
    q.head = q.tail = q.store = CArrayOfStar.<ST_Agnode_s>ALLOC(sz, ZType.ST_Agnode_s);
	q.limit = q.store.plus_(sz);
    return q;
} finally {
LEAVING("c7cptalfn8k75wyfirbnptnav","new_queue");
}
}




@Reviewed(when = "15/11/2020")
@Original(version="2.38.0", path="lib/common/utils.c", name="free_queue", key="1uj5nmdvwmuklnpd3v5py547f", definition="void free_queue(nodequeue * q)")
public static void free_queue(ST_nodequeue q) {
ENTERING("1uj5nmdvwmuklnpd3v5py547f","free_queue");
try {
    Memory.free(q.store);
    Memory.free(q);
} finally {
LEAVING("1uj5nmdvwmuklnpd3v5py547f","free_queue");
}
}




@Reviewed(when = "15/11/2020")
@Original(version="2.38.0", path="lib/common/utils.c", name="enqueue", key="20pwd6i141q3o25lfvrdqytot", definition="void enqueue(nodequeue * q, node_t * n)")
public static void enqueue(ST_nodequeue q, ST_Agnode_s n) {
ENTERING("20pwd6i141q3o25lfvrdqytot","enqueue");
try {
    q.tail.set_(0, n);
    q.tail =q.tail.plus_(1);
    if (q.tail.comparePointer_(q.limit) >= 0)
	q.tail = q.store;
} finally {
LEAVING("20pwd6i141q3o25lfvrdqytot","enqueue");
}
}




@Reviewed(when = "15/11/2020")
@Original(version="2.38.0", path="lib/common/utils.c", name="", key="b612nmtf16au96ztbs8pike9r", definition="node_t *dequeue(nodequeue * q)")
public static ST_Agnode_s dequeue(ST_nodequeue q) {
ENTERING("b612nmtf16au96ztbs8pike9r","dequeue");
try {
    ST_Agnode_s n;
    if (EQ_ARRAY(q.head, q.tail))
	n = null;
    else {
	n = q.head.get_(0);
	q.head = q.head.plus_(1);
	if (q.head.comparePointer_(q.limit) >= 0)
	    q.head = q.store;
    }
    return n;
} finally {
LEAVING("b612nmtf16au96ztbs8pike9r","dequeue");
}
}





@Reviewed(when = "12/11/2020")
@Original(version="2.38.0", path="lib/common/utils.c", name="late_int", key="6nydxv4f2m7jcfh8ljs0neu0x", definition="int late_int(void *obj, attrsym_t * attr, int def, int low)")
public static int late_int(__ptr__ obj, ST_Agsym_s attr, int def, int low) {
ENTERING("6nydxv4f2m7jcfh8ljs0neu0x","late_int");
try {
    CString p;
    CString endp[] = new CString[1];
    int rv;
    if (attr == null)
	return def;
    p = agxget(obj,attr);
    if ((p) == null || p.length()==0)
	return def;
    rv = strtol (p, endp, 10);
    if (EQ_CSTRING(p, endp[0])) return def;  /* invalid int format */
    if (rv < low) return low;
    else return rv;
} finally {
LEAVING("6nydxv4f2m7jcfh8ljs0neu0x","late_int");
}
}




@Reviewed(when = "12/11/2020")
@Original(version="2.38.0", path="lib/common/utils.c", name="late_double", key="d68314e4f20r79tt0cnmxugme", definition="double late_double(void *obj, attrsym_t * attr, double def, double low)")
public static double late_double(__ptr__ obj, ST_Agsym_s attr, double def, double low) {
ENTERING("d68314e4f20r79tt0cnmxugme","late_double");
try {
    CString p;
    CString endp[] = new CString[1];
    double rv;
    
    if ((attr) == null || (obj) == null)
	return def;
    p = agxget(obj,attr);
    if ((p) == null || p.charAt(0) == '\0')
	return def;
    rv = strtod (p, endp);
    if (p == endp[0]) return def;  /* invalid double format */
    if (rv < low) return low;
    else return rv;
} finally {
LEAVING("d68314e4f20r79tt0cnmxugme","late_double");
}
}




@Reviewed(when = "12/11/2020")
@Original(version="2.38.0", path="lib/common/utils.c", name="late_string", key="83xm6yc9q5h1bzufhsnv0v2up", definition="char *late_string(void *obj, attrsym_t * attr, char *def)")
public static CString late_string(__ptr__ obj, ST_Agsym_s attr, CString def) {
ENTERING("83xm6yc9q5h1bzufhsnv0v2up","late_string");
try {
    if ((attr) == null || (obj) == null)
	return def;
    return agxget(obj, attr);
} finally {
LEAVING("83xm6yc9q5h1bzufhsnv0v2up","late_string");
}
}



@Reviewed(when = "12/11/2020")
@Original(version="2.38.0", path="lib/common/utils.c", name="", key="8oon4q1mrublaru177xfntqgd", definition="char *late_nnstring(void *obj, attrsym_t * attr, char *def)")
public static CString late_nnstring(__ptr__ obj, ST_Agsym_s attr, CString def) {
ENTERING("8oon4q1mrublaru177xfntqgd","late_nnstring");
try {
    CString rv = late_string(obj, attr, def);
    if ((rv) == null || (rv.charAt(0) == '\0'))
	rv = def;
    return rv;
} finally {
LEAVING("8oon4q1mrublaru177xfntqgd","late_nnstring");
}
}




@Reviewed(when = "13/11/2020")
@Original(version="2.38.0", path="lib/common/utils.c", name="late_bool", key="87ifze04q7qzigjj1fb9y9by2", definition="boolean late_bool(void *obj, attrsym_t * attr, int def)")
public static boolean late_bool(__ptr__ obj, ST_Agsym_s attr, int def) {
ENTERING("87ifze04q7qzigjj1fb9y9by2","late_bool");
try {
if (attr == null)
	return def!=0;
UNSUPPORTED("a0kh1y5n8u59z0xo7mag3zmt6"); //     return mapbool(agxget(obj, attr));
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
} finally {
LEAVING("87ifze04q7qzigjj1fb9y9by2","late_bool");
}
}




/* union-find */
@Reviewed(when = "13/11/2020")
@Original(version="2.38.0", path="lib/common/utils.c", name="", key="aeq0acpkhfv3gd5jx8op4jf18", definition="node_t *UF_find(node_t * n)")
public static ST_Agnode_s UF_find(ST_Agnode_s n) {
ENTERING("aeq0acpkhfv3gd5jx8op4jf18","UF_find");
try {
    while (ND_UF_parent(n)!=null && (ND_UF_parent(n) != n)) {
	if (ND_UF_parent(ND_UF_parent(n))!=null)
	    ND_UF_parent(n, ND_UF_parent(ND_UF_parent(n)));
	n = ND_UF_parent(n);
    }
    return n;
} finally {
LEAVING("aeq0acpkhfv3gd5jx8op4jf18","UF_find");
}
}




@Reviewed(when = "14/11/2020")
@Original(version="2.38.0", path="lib/common/utils.c", name="", key="9ldxwfr4vvijrvfcvs1hvdzrt", definition="node_t *UF_union(node_t * u, node_t * v)")
public static ST_Agnode_s UF_union(ST_Agnode_s u, ST_Agnode_s v) {
ENTERING("9ldxwfr4vvijrvfcvs1hvdzrt","UF_union");
try {
    if (u == v)
	return u;
    if (ND_UF_parent(u) == null) {
	ND_UF_parent(u, u);
	ND_UF_size(u, 1);
    } else
    UNSUPPORTED("35c97tyk6tzw1g527j6rp6xoo"); // 	u = UF_find(u);
    if (ND_UF_parent(v) == null) {
	ND_UF_parent(v, v);
	ND_UF_size(v, 1);
    } else
	v = UF_find(v);
    if (ND_id(u) > ND_id(v)) {
	UNSUPPORTED("2igr3ntnkm6svji4pbnjlp54e"); // 	ND_UF_parent(u) = v;
	UNSUPPORTED("3lht90i6tvxbr10meir8nvcic"); // 	ND_UF_size(v) += ND_UF_size(u);
    } else {
	ND_UF_parent(v, u);
	ND_UF_size(u, ND_UF_size(u) + ND_UF_size(v));
	v = u;
    }
    return v;
} finally {
LEAVING("9ldxwfr4vvijrvfcvs1hvdzrt","UF_union");
}
}




@Reviewed(when = "14/11/2020")
@Original(version="2.38.0", path="lib/common/utils.c", name="UF_singleton", key="22k0u1imxyw06k9rizqlfz153", definition="void UF_singleton(node_t * u)")
public static void UF_singleton(ST_Agnode_s u) {
ENTERING("22k0u1imxyw06k9rizqlfz153","UF_singleton");
try {
    ND_UF_size(u, 1);
    ND_UF_parent(u, null);
    ND_ranktype(u, NORMAL);
} finally {
LEAVING("22k0u1imxyw06k9rizqlfz153","UF_singleton");
}
}




@Reviewed(when = "14/11/2020")
@Original(version="2.38.0", path="lib/common/utils.c", name="UF_setname", key="e0fn8xuzkdt0q8xoofl6j1txb", definition="void UF_setname(node_t * u, node_t * v)")
public static void UF_setname(ST_Agnode_s u, ST_Agnode_s v) {
ENTERING("e0fn8xuzkdt0q8xoofl6j1txb","UF_setname");
try {
    assert(u == UF_find(u));
    ND_UF_parent(u, v);
    ND_UF_size(v, ND_UF_size(v) + ND_UF_size(u));
} finally {
LEAVING("e0fn8xuzkdt0q8xoofl6j1txb","UF_setname");
}
}


/* from Glassner's Graphics Gems */
private static final int W_DEGREE = 5;

/*
 *  Bezier : 
 *	Evaluate a Bezier curve at a particular parameter value
 *      Fill in control points for resulting sub-curves if "Left" and
 *	"Right" are non-null.
 * 
 */
//3 6p0ey2c2ujk2o7h221p0b4xon
// pointf Bezier(pointf * V, int degree, double t, pointf * Left, pointf * Right) 
@Unused
@Original(version="2.38.0", path="lib/common/utils.c", name="Bezier", key="6p0ey2c2ujk2o7h221p0b4xon", definition="pointf Bezier(pointf * V, int degree, double t, pointf * Left, pointf * Right)")
public static ST_pointf Bezier(CArray<ST_pointf> V, int degree, double t, CArray<ST_pointf> Left, CArray<ST_pointf> Right) {
// WARNING!! STRUCT
return Bezier_w_(V, degree, t, Left, Right).copy();
}
private static ST_pointf Bezier_w_(CArray<ST_pointf> V, int degree, double t, CArray<ST_pointf> Left, CArray<ST_pointf> Right) {
ENTERING("6p0ey2c2ujk2o7h221p0b4xon","Bezier");
try {
    int i, j;			/* Index variables      */
    final CArray<ST_pointf> Vtemp[] = new CArray[] { CArray.ALLOC__(W_DEGREE+1,ZType.ST_pointf),
    		CArray.ALLOC__(W_DEGREE+1,ZType.ST_pointf),
    		CArray.ALLOC__(W_DEGREE+1,ZType.ST_pointf),
    		CArray.ALLOC__(W_DEGREE+1,ZType.ST_pointf),
    		CArray.ALLOC__(W_DEGREE+1,ZType.ST_pointf),
    		CArray.ALLOC__(W_DEGREE+1,ZType.ST_pointf)};

    /* Copy control points  */
    for (j = 0; j <= degree; j++) {
	Vtemp[0].get__(j).___(V.get__(j));
    }
    
    /* Triangle computation */
    for (i = 1; i <= degree; i++) {
	for (j = 0; j <= degree - i; j++) {
	    Vtemp[i].get__(j).x =
		(1.0 - t) * Vtemp[i - 1].get__(j).x + t * Vtemp[i - 1].get__(j + 1).x;
	    Vtemp[i].get__(j).y =
		(1.0 - t) * Vtemp[i - 1].get__(j).y + t * Vtemp[i - 1].get__(j + 1).y;
	}
    }
    if (Left != null)
	for (j = 0; j <= degree; j++)
	    Left.get__(j).___(Vtemp[j].get__(0));
    if (Right != null)
	for (j = 0; j <= degree; j++)
	    Right.get__(j).___(Vtemp[degree - j].get__(j));
    return (Vtemp[degree].get__(0));
} finally {
LEAVING("6p0ey2c2ujk2o7h221p0b4xon","Bezier");
}
}




@Reviewed(when = "12/11/2020")
@Original(version="2.38.0", path="lib/common/utils.c", name="", key="3xll2b0v9nthwvx9dafq49t8s", definition="const char *safefile(const char *filename)")
public static CString safefile(CString filename) {
ENTERING("3xll2b0v9nthwvx9dafq49t8s","safefile");
try {
	return null;
} finally {
LEAVING("3xll2b0v9nthwvx9dafq49t8s","safefile");
}
}




@Reviewed(when = "12/11/2020")
@Original(version="2.38.0", path="lib/common/utils.c", name="maptoken", key="2ihv17oajyaaaycirwsbgz1m7", definition="int maptoken(char *p, char **name, int *val)")
public static int maptoken(CString p, CString name[], int val[]) {
ENTERING("2ihv17oajyaaaycirwsbgz1m7","maptoken");
try {
    int i;
    CString q;
    for (i = 0; (q = name[i]) != null; i++)
	if (p!=null && (strcmp(p,q) == 0))
	    break;
    return val[i];
} finally {
LEAVING("2ihv17oajyaaaycirwsbgz1m7","maptoken");
}
}




@Reviewed(when = "12/11/2020")
@Original(version="2.38.0", path="lib/common/utils.c", name="mapBool", key="4esyuq2yqdaqoddgfqs24m5m3", definition="boolean mapBool(char *p, boolean dflt)")
public static boolean mapBool(CString p, boolean dflt) {
ENTERING("4esyuq2yqdaqoddgfqs24m5m3","mapBool");
try {
    if ((p) == null || (p.charAt(0) == '\0'))
	return dflt;
    if (strcasecmp(p, new CString("false")) == 0)
	return false;
    if (strcasecmp(p, new CString("no")) == 0)
	return false;
    if (strcasecmp(p, new CString("true")) == 0)
	return (true);
    if (strcasecmp(p, new CString("yes")) == 0)
	return (true);
    if (isdigit(p.charAt(0)))
	return atoi(p)!=0;
    else
	return dflt;
} finally {
LEAVING("4esyuq2yqdaqoddgfqs24m5m3","mapBool");
}
}




@Reviewed(when = "12/11/2020")
@Original(version="2.38.0", path="lib/common/utils.c", name="mapbool", key="ehkvqh6bwf4jw3mj1w5p7a8m8", definition="boolean mapbool(char *p)")
public static boolean mapbool(CString p) {
ENTERING("ehkvqh6bwf4jw3mj1w5p7a8m8","mapbool");
try {
    return mapBool (p, false);
} finally {
LEAVING("ehkvqh6bwf4jw3mj1w5p7a8m8","mapbool");
}
}




//3 37hgj44s94wf9bmz16he85pgq
// pointf dotneato_closest(splines * spl, pointf pt) 
@Unused
@Original(version="2.38.0", path="lib/common/utils.c", name="dotneato_closest", key="37hgj44s94wf9bmz16he85pgq", definition="pointf dotneato_closest(splines * spl, pointf pt)")
public static ST_pointf dotneato_closest(ST_splines spl, final ST_pointf pt) {
ENTERING("37hgj44s94wf9bmz16he85pgq","dotneato_closest");
try {
    return dotneato_closest_ (spl, pt.copy()).copy();
} finally {
LEAVING("37hgj44s94wf9bmz16he85pgq","dotneato_closest");
}	
}
private static ST_pointf dotneato_closest_(ST_splines spl, final ST_pointf pt) {
     int i, j, k, besti, bestj;
     double bestdist2, d2, dlow2, dhigh2; /* squares of distances */
     double low, high, t;
     // final ST_pointf c[] = new ST_pointf[] {new ST_pointf(),new ST_pointf(),new ST_pointf(),new ST_pointf()};
	 final CArray<ST_pointf> c = CArray.<ST_pointf>ALLOC__(4, ZType.ST_pointf);
     final ST_pointf pt2 = new ST_pointf();
     final ST_bezier bz = new ST_bezier();
     besti = bestj = -1;
     bestdist2 = 1e+38;
     for (i = 0; i < spl.size; i++) {
 	bz.___(spl.list.get__(i));
 	for (j = 0; j < bz.size; j++) {
 	    final ST_pointf b = new ST_pointf();
 	    b.x = bz.list.get__(j).x;
 	    b.y = bz.list.get__(j).y;
 	    d2 = DIST2(b, pt);
 	    if ((bestj == -1) || (d2 < bestdist2)) {
 		besti = i;
 		bestj = j;
 		bestdist2 = d2;
 	    }
 	}
     }
  	bz.___(spl.list.get__(besti));
     /* Pick best Bezier. If bestj is the last point in the B-spline, decrement.
      * Then set j to be the first point in the corresponding Bezier by dividing
      * then multiplying be 3. Thus, 0,1,2 => 0; 3,4,5 => 3, etc.
      */
     if (bestj == bz.size-1)
 	bestj--;
     j = 3*(bestj / 3);
     for (k = 0; k < 4; k++) {
 	  	c.get__(k).x = bz.list.get__(j + k).x;
 	  	c.get__(k).y = bz.list.get__(j + k).y;
     }
     low = 0.0;
     high = 1.0;
     dlow2 = DIST2(c.get__(0), pt);
     dhigh2 = DIST2(c.get__(3), pt);
     do {
 	t = (low + high) / 2.0;
 	pt2.___(Bezier(c, 3, t, null, null));
 	if (Math.abs(dlow2 - dhigh2) < 1.0)
 	    break;
 	if (Math.abs(high - low) < .00001)
 	    break;
UNSUPPORTED("6apa9aoby9j8a0eanbfhy5mn2"); // 	if (dlow2 < dhigh2) {
UNSUPPORTED("6jttyuryfaxa193mme86dqf58"); // 	    high = t;
UNSUPPORTED("6avwplrum9i9qi3g9wl6yvz04"); // 	    dhigh2 = DIST2(pt2, pt);
UNSUPPORTED("7yhr8hn3r6wohafwxrt85b2j2"); // 	} else {
UNSUPPORTED("556vs5i22602clhs1p3htz7vk"); // 	    low = t;
UNSUPPORTED("507tgq81szei2ge3miiak4b1v"); // 	    dlow2 = DIST2(pt2, pt);
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
     } while (true);
      return pt2;
}






@Reviewed(when = "12/11/2020")
@Original(version="2.38.0", path="lib/common/utils.c", name="common_init_node", key="cr81drt18h5feqzxyh3jb0u49", definition="void common_init_node(node_t * n)")
public static void common_init_node(Globals zz, ST_Agnode_s n) {
ENTERING("cr81drt18h5feqzxyh3jb0u49","common_init_node");
try {
	ST_fontinfo fi = new ST_fontinfo();
    CString str;
    ND_width(n,
	late_double(n, zz.N_width, DEFAULT_NODEWIDTH, MIN_NODEWIDTH));
    ND_height(n,
	late_double(n, zz.N_height, DEFAULT_NODEHEIGHT, MIN_NODEHEIGHT));
    ND_shape(n,
	bind_shape(zz, late_nnstring(n, zz.N_shape, new CString(DEFAULT_NODESHAPE)), n));
    str = agxget(n, zz.N_label);
    fi.fontsize = late_double(n, zz.N_fontsize, DEFAULT_FONTSIZE, MIN_FONTSIZE);
    fi.fontname = late_nnstring(n, zz.N_fontname, new CString("Times-Roman"));
    fi.fontcolor = late_nnstring(n, zz.N_fontcolor, new CString("black"));
    ND_label(n, make_label(zz, n, str,
	        ((aghtmlstr(zz, str)!=0 ? LT_HTML : LT_NONE) | ( (shapeOf(n) == EN_shape_kind.SH_RECORD) ? LT_RECD : LT_NONE)),
		fi.fontsize, fi.fontname, fi.fontcolor));
    if (zz.N_xlabel!=null && (str = agxget(n, zz.N_xlabel))!=null && (str.charAt(0)!='\0')) {
UNSUPPORTED("4ua9vld76wpovsm1celv2ff6e"); // 	ND_xlabel(n) = make_label((void*)n, str, (aghtmlstr(str) ? (1 << 1) : (0 << 1)),
UNSUPPORTED("b0zm6fkpjlt9jacykbgugjodg"); // 				fi.fontsize, fi.fontname, fi.fontcolor);
UNSUPPORTED("ail0d4qmxj2aqh2q721inwgqu"); // 	GD_has_labels(agraphof(n)) |= (1 << 4);
    }
    
    ND_showboxes(n, late_int(n, zz.N_showboxes, 0, 0));
    ND_shape(n).fns.initfn.exe(zz, n);
} finally {
LEAVING("cr81drt18h5feqzxyh3jb0u49","common_init_node");
}
}




@Reviewed(when = "13/11/2020")
@Original(version="2.38.0", path="lib/common/utils.c", name="initFontEdgeAttr", key="d2v8l80y27ue2fag5c0qplah8", definition="static void initFontEdgeAttr(edge_t * e, struct fontinfo *fi)")
public static void initFontEdgeAttr(Globals zz, ST_Agedge_s e, ST_fontinfo fi) {
ENTERING("d2v8l80y27ue2fag5c0qplah8","initFontEdgeAttr");
try {
    fi.fontsize = late_double(e, zz.E_fontsize, DEFAULT_FONTSIZE, MIN_FONTSIZE);
    fi.fontname = late_nnstring(e, zz.E_fontname, new CString("Times-Roman"));
    fi.fontcolor = late_nnstring(e, zz.E_fontcolor, new CString("black"));
} finally {
LEAVING("d2v8l80y27ue2fag5c0qplah8","initFontEdgeAttr");
}
}




@Reviewed(when = "13/11/2020")
@Original(version="2.38.0", path="lib/common/utils.c", name="initFontLabelEdgeAttr", key="ak3pxrdrq900wymudwnjmbito", definition="static void initFontLabelEdgeAttr(edge_t * e, struct fontinfo *fi, 		      struct fontinfo *lfi)")
public static void initFontLabelEdgeAttr(Globals zz, ST_Agedge_s e, ST_fontinfo fi, ST_fontinfo lfi) {
ENTERING("ak3pxrdrq900wymudwnjmbito","initFontLabelEdgeAttr");
try {
	if ((fi.fontname) == null) initFontEdgeAttr(zz, e, fi);
    lfi.fontsize = late_double(e, zz.E_labelfontsize, fi.fontsize, MIN_FONTSIZE);
    lfi.fontname = late_nnstring(e, zz.E_labelfontname, fi.fontname);
    lfi.fontcolor = late_nnstring(e, zz.E_labelfontcolor, fi.fontcolor);
} finally {
LEAVING("ak3pxrdrq900wymudwnjmbito","initFontLabelEdgeAttr");
}
}




/* noClip:
 * Return true if head/tail end of edge should not be clipped
 * to node.
 */
@Reviewed(when = "13/11/2020")
@Original(version="2.38.0", path="lib/common/utils.c", name="noClip", key="bgnk1zwht9rwx6thmly98iofb", definition="static boolean  noClip(edge_t *e, attrsym_t* sym)")
public static boolean noClip(ST_Agedge_s e, ST_Agsym_s sym) {
ENTERING("bgnk1zwht9rwx6thmly98iofb","noClip");
try {
    CString str;
    boolean		rv = false;
    
    if (sym!=null) {	/* mapbool isn't a good fit, because we want "" to mean true */
	str = agxget(e,sym);
	if (str!=null && str.charAt(0)!='\0') rv = !mapbool(str);
	else rv = false;
    }
    return rv;
} finally {
LEAVING("bgnk1zwht9rwx6thmly98iofb","noClip");
}
}




@Reviewed(when = "13/11/2020")
@Original(version="2.38.0", path="lib/common/utils.c", name="chkPort", key="9vnr1bc7p533acazoxbhbfmx3", definition="static port chkPort (port (*pf)(node_t*, char*, char*), node_t* n, char* s)")
public static ST_port chkPort(Globals zz, CFunction pf, ST_Agnode_s n, CString s) {
// WARNING!! STRUCT
return chkPort_w_(zz, pf, n, s).copy();
}
private static ST_port chkPort_w_(Globals zz, CFunction pf, ST_Agnode_s n, CString s) {
ENTERING("9vnr1bc7p533acazoxbhbfmx3","chkPort");
try {
    final ST_port pt = new ST_port();
	CString cp=null;
	if(s!=null)
		cp= strchr(s,':');
    if (cp!=null) {
	UNSUPPORTED("cbuf05ko7kaxq2n9zw35l5v2h"); // 	*cp = '\0';
	UNSUPPORTED("7ofc3q8txvlvus6qwefbnbaxu"); // 	pt = pf(n, s, cp+1);
	UNSUPPORTED("971i954brvgqb35cftazlqhon"); // 	*cp = ':';
	UNSUPPORTED("2o9oidtrr5gspl1dh6vnz7mlz"); // 	pt.name = cp+1;
    }
    else
	pt.___((ST_port) pf.exe(zz, n, s, null));
	pt.name = s;
    return pt;
} finally {
LEAVING("9vnr1bc7p533acazoxbhbfmx3","chkPort");
}
}



@Reviewed(when = "13/11/2020")
@Original(version="2.38.0", path="lib/common/utils.c", name="common_init_edge", key="3aqh64lxwv4da2snfe7fvr45b", definition="int common_init_edge(edge_t * e)")
public static int common_init_edge(Globals zz, ST_Agedge_s e) {
ENTERING("3aqh64lxwv4da2snfe7fvr45b","common_init_edge");
try {
    CString str;
    int r = 0;
    final ST_fontinfo fi = new ST_fontinfo();
    final ST_fontinfo lfi = new ST_fontinfo();
    ST_Agraph_s sg = agraphof(agtail(e));
    
    fi.fontname = null;
    lfi.fontname = null;
    if (zz.E_label!=null && (str = agxget(e, zz.E_label))!=null && (str.charAt(0)!='\0')) {
	r = 1;
	initFontEdgeAttr(zz, e, fi);
	ED_label(e, make_label(zz, e, str, (aghtmlstr(zz, str)!=0 ? LT_HTML : LT_NONE),
				fi.fontsize, fi.fontname, fi.fontcolor));
	GD_has_labels(sg, GD_has_labels(sg) | EDGE_LABEL);
	ED_label_ontop(e,
	    mapbool(late_string(e, zz.E_label_float, new CString("false"))));
    }
    
    
    if (zz.E_xlabel!=null && (str = agxget(e, zz.E_xlabel))!=null && (str.charAt(0)!='\0')) {
UNSUPPORTED("1j3mhgq7abuh3n19q2jtjddbc"); // 	if (!fi.fontname)
UNSUPPORTED("bmqo2g5g107quod3h31r8iudr"); // 	    initFontEdgeAttr(e, &fi);
UNSUPPORTED("3s7kg9x748riuy3tm697s6e8t"); // 	(((Agedgeinfo_t*)(((Agobj_t*)(e))->data))->xlabel) = make_label((void*)e, str, (aghtmlstr(str) ? (1 << 1) : (0 << 1)),
UNSUPPORTED("b0zm6fkpjlt9jacykbgugjodg"); // 				fi.fontsize, fi.fontname, fi.fontcolor);
UNSUPPORTED("c078bypfszv0nsvp1nc0x28wx"); // 	(((Agraphinfo_t*)(((Agobj_t*)(sg))->data))->has_labels) |= (1 << 5);
    }
    
    
    /* vladimir */
    if (zz.E_headlabel!=null && (str = agxget(e, zz.E_headlabel))!=null && (str.charAt(0)!='\0')) {
    	initFontLabelEdgeAttr(zz, e, fi, lfi);
    	ED_head_label(e, make_label(zz, e, str, (aghtmlstr(zz, str)!=0 ? LT_HTML : LT_NONE),
				lfi.fontsize, lfi.fontname, lfi.fontcolor));
    	GD_has_labels(sg, GD_has_labels(sg) | HEAD_LABEL);
    }
    if (zz.E_taillabel!=null && (str = agxget(e, zz.E_taillabel))!=null && (str.charAt(0)!='\0')) {
    	initFontLabelEdgeAttr(zz, e, fi, lfi);
    	ED_tail_label(e, make_label(zz, e, str, (aghtmlstr(zz, str)!=0 ? LT_HTML : LT_NONE),
				lfi.fontsize, lfi.fontname, lfi.fontcolor));
    	GD_has_labels(sg, GD_has_labels(sg) | TAIL_LABEL);
    }
    /* end vladimir */
    
    /* We still accept ports beginning with colons but this is deprecated 
     * That is, we allow tailport = ":abc" as well as the preferred 
     * tailport = "abc".
     */
    str = agget(zz, e, TAIL_ID);
    /* libgraph always defines tailport/headport; libcgraph doesn't */
    if ((str) == null) str = new CString("");
    if (str!=null && str.charAt(0)!='\0')
    ND_has_port(agtail(e), true);
    ED_tail_port(e, chkPort (zz, ND_shape(agtail(e)).fns.portfn, agtail(e), str));
    if (noClip(e, zz.E_tailclip))
    UNSUPPORTED("cg4z67u0dm6h9nrcx8kkalnlt"); // 	ED_tail_port(e).clip = FALSE;
    str = agget(zz, e, HEAD_ID);
    
    /* libgraph always defines tailport/headport; libcgraph doesn't */
    if ((str) == null) str = new CString("");
    if (str!=null && str.charAt(0)!='\0')
    ND_has_port(aghead(e), true);
    ED_head_port(e, chkPort(zz, ND_shape(aghead(e)).fns.portfn, aghead(e), str));
    if (noClip(e, zz.E_headclip))
    UNSUPPORTED("ayqscz30ekhcje94wh4ib1hcu"); // 	ED_head_port(e).clip = FALSE;
    return r;
} finally {
LEAVING("3aqh64lxwv4da2snfe7fvr45b","common_init_edge");
}
}




//3 3mkqvtbyq9j8ktzil6t7vakg5
// static boxf addLabelBB(boxf bb, textlabel_t * lp, boolean flipxy) 
@Unused
@Original(version="2.38.0", path="lib/common/utils.c", name="addLabelBB", key="3mkqvtbyq9j8ktzil6t7vakg5", definition="static boxf addLabelBB(boxf bb, textlabel_t * lp, boolean flipxy)")
public static ST_boxf addLabelBB(final ST_boxf bb, ST_textlabel_t lp, boolean flipxy) {
// WARNING!! STRUCT
return addLabelBB_w_(bb.copy(), lp, flipxy).copy();
}
private static ST_boxf addLabelBB_w_(final ST_boxf bb, ST_textlabel_t lp, boolean flipxy) {
ENTERING("3mkqvtbyq9j8ktzil6t7vakg5","addLabelBB");
try {
    double width, height;
    final ST_pointf p = new ST_pointf();
    p.___(lp.pos);
    double min, max;
    if (flipxy) {
	height = lp.dimen.x;
	width = lp.dimen.y;
    }
    else {
	width = lp.dimen.x;
	height = lp.dimen.y;
    }
    min = p.x - width / 2.;
    max = p.x + width / 2.;
    if (min < bb.LL.x)
	bb.LL.x = min;
    if (max > bb.UR.x)
	bb.UR.x = max;
    min = p.y - height / 2.;
    max = p.y + height / 2.;
    if (min < bb.LL.y)
	bb.LL.y = min;
    if (max > bb.UR.y)
	bb.UR.y = max;
    return bb;
} finally {
LEAVING("3mkqvtbyq9j8ktzil6t7vakg5","addLabelBB");
}
}




//3 bz7kjecium6a7xa39qfobwwnc
// void updateBB(graph_t * g, textlabel_t * lp) 
@Unused
@Original(version="2.38.0", path="lib/common/utils.c", name="updateBB", key="bz7kjecium6a7xa39qfobwwnc", definition="void updateBB(graph_t * g, textlabel_t * lp)")
public static void updateBB(ST_Agraph_s g, ST_textlabel_t lp) {
ENTERING("bz7kjecium6a7xa39qfobwwnc","updateBB");
try {
    GD_bb(g).___(addLabelBB(GD_bb(g), lp, GD_flip(g)));
} finally {
LEAVING("bz7kjecium6a7xa39qfobwwnc","updateBB");
}
}








/* htmlEntityUTF8:
 * substitute html entities like: &#123; and: &amp; with the UTF8 equivalents
 * check for invalid utf8. If found, treat a single byte as Latin-1, convert it to
 * utf8 and warn the user.
 */
@Reviewed(when = "12/11/2020")
@Original(version="2.38.0", path="lib/common/utils.c", name="htmlEntityUTF8", key="9yungx7uxqkmzfh2ub6gs9l48", definition="char* htmlEntityUTF8 (char* s, graph_t* g)")
public static CString htmlEntityUTF8(CString s, ST_Agraph_s g) {
ENTERING("9yungx7uxqkmzfh2ub6gs9l48","htmlEntityUTF8");
try {
if (s!=null) return s.duplicate();
UNSUPPORTED("1xtgr84lklglr4gz1i1m3t30"); // char* htmlEntityUTF8 (char* s, graph_t* g)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("a86rc8dlb4bgtjxjhv43cnmup"); //     static graph_t* lastg;
UNSUPPORTED("1gn88eczuyt54egtiulqe7y7h"); //     static boolean warned;
UNSUPPORTED("3rzf3h52xn02xrxie111286a0"); //     char*  ns;
UNSUPPORTED("9gou5otj6s39l2cbyc8i5i5lq"); //     agxbuf xb;
UNSUPPORTED("esg3s800dx899v69pkng2kavv"); //     unsigned char buf[BUFSIZ];
UNSUPPORTED("10sir32iwi5l2jyfgp65pihto"); //     unsigned char c;
UNSUPPORTED("4urrp9tny84a3cm8ycya896x3"); //     unsigned int v;
UNSUPPORTED("d5druw9z4e87khtgyeivjngvc"); //     int ignored;
UNSUPPORTED("4fymyfhfc3ddededhxw7cs671"); //     int uc;
UNSUPPORTED("d6z43cxggqxq7iq4puyluzkfn"); //     int ui;
UNSUPPORTED("4pgl4pn1cad2whf242bntmjre"); //     (void) ignored;
UNSUPPORTED("t65eqheg8dxzi237a648t66j"); //     if (lastg != g) {
UNSUPPORTED("emyoumradju26mhebq2bewtva"); // 	lastg = g;
UNSUPPORTED("9ys85d2ctjb1a9ra0n11o2a2r"); // 	warned = 0;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("3jwm77zyv02ukrvjv9jppejf7"); //     agxbinit(&xb, BUFSIZ, buf);
UNSUPPORTED("11zj3p308ttro04hrzowx0vrh"); //     while ((c = *(unsigned char*)s++)) {
UNSUPPORTED("3xqp191v0egfea0z3ds5p1mjt"); //         if (c < 0xC0)
UNSUPPORTED("4j200801m87vnfrkblygi6ucj"); // 	    /*
UNSUPPORTED("1egwnjpl995mff91kquf9ikvl"); // 	     * Handles properly formed UTF-8 characters between
UNSUPPORTED("8rfd02x8qlye0oo8ro9u9g8ya"); // 	     * 0x01 and 0x7F.  Also treats \0 and naked trail
UNSUPPORTED("49xkviec8w4s3zvlq13991yqh"); // 	     * bytes 0x80 to 0xBF as valid characters representing
UNSUPPORTED("bg2026u05g8jo9nm9pr39cknl"); // 	     * themselves.
UNSUPPORTED("20m1lc1moer8x00tx9ceto0iw"); // 	     */
UNSUPPORTED("6g3zbtp7zrl9i7jz1if5yi7rj"); //             uc = 0;
UNSUPPORTED("5ks80mtyizjvlnrh1bwebqrx7"); //         else if (c < 0xE0)
UNSUPPORTED("2gr59wt9ibszrzwii40dqyd5b"); //             uc = 1;
UNSUPPORTED("2su1o4swg92stlgi53k4ydm5u"); //         else if (c < 0xF0)
UNSUPPORTED("c0zrulbhqoupyvbwpwapfpc70"); //             uc = 2;
UNSUPPORTED("dveaae8p8nhz8gosmtiftudrz"); //         else if (c < 0xF8)
UNSUPPORTED("a4c1bzq46y652vgwpxsruptth"); //             uc = 3;
UNSUPPORTED("3jir07ymknf0hmb9pv9x4dr3o"); //         else {
UNSUPPORTED("ar52jlyh4qqazbcbvntg2wet6"); //             uc = -1;
UNSUPPORTED("ame11lb7ylv3rp1nhtuq383du"); //             if (!warned) {
UNSUPPORTED("7acv020k6kt3q8tholp3ex0qa"); //                 agerr(AGWARN, "UTF8 codes > 4 bytes are not currently supported (graph %s) - treated as Latin-1. Perhaps \"-Gcharset=latin1\" is needed?\n", agnameof(g));
UNSUPPORTED("qr4o1w9xvn1ayc52y0f4c8bh"); //                 warned = 1;
UNSUPPORTED("7g94ubxa48a1yi3mf9v521b7c"); //             }
UNSUPPORTED("ejqmh8ox9uoy02anzqhcxcrro"); //             c = cvtAndAppend (c, &xb);
UNSUPPORTED("4mhlpjofolwivhm0tl8cxznly"); //         }
UNSUPPORTED("8n9ts84r09bm29qdb7v2og087"); // 	    if (uc == 0 && c == '&') {
UNSUPPORTED("3cziqu94ggcy3m9kpbpdefqgk"); // 		/* replace html entity sequences like: &amp;
UNSUPPORTED("1c1fqb40szvpdlekj4as5qcqq"); // 		 * and: &#123; with their UTF8 equivalents */
UNSUPPORTED("buyl72klnvh12cf4m578joukc"); // 	        v = htmlEntity (&s);
UNSUPPORTED("1fj0k3ba7vy9tjmjnc4d4mujb"); // 	        if (v) {
UNSUPPORTED("aso1t4v0cars70ngqafalmoeq"); // 		    if (v < 0x7F) /* entity needs 1 byte in UTF8 */
UNSUPPORTED("777bke4pyf77uol5s5d6qk0i6"); // 			c = v;
UNSUPPORTED("4wqu3fuzmckazc7eb1vvoxspn"); // 		    else if (v < 0x07FF) { /* entity needs 2 bytes in UTF8 */
UNSUPPORTED("2b9x6g0k8a00ty06llcyp6cqu"); // 			ignored = ((((&xb)->ptr >= (&xb)->eptr) ? agxbmore(&xb,1) : 0), (int)(*(&xb)->ptr++ = ((unsigned char)(v >> 6) | 0xC0)));
UNSUPPORTED("el4sow483b296l5o1hy6oqkzp"); // 			c = (v & 0x3F) | 0x80;
UNSUPPORTED("dkxvw03k2gg9anv4dbze06axd"); // 		    }
UNSUPPORTED("163d4s8voz31qrt0e4c8ysn9e"); // 		    else { /* entity needs 3 bytes in UTF8 */
UNSUPPORTED("4tpuwv4i0wslspyymoqhdxsvd"); // 			ignored = ((((&xb)->ptr >= (&xb)->eptr) ? agxbmore(&xb,1) : 0), (int)(*(&xb)->ptr++ = ((unsigned char)(v >> 12) | 0xE0)));
UNSUPPORTED("8unuta6ydloexb267kdf96wi"); // 			ignored = ((((&xb)->ptr >= (&xb)->eptr) ? agxbmore(&xb,1) : 0), (int)(*(&xb)->ptr++ = ((unsigned char)((v >> 6) & 0x3F) | 0x80)));
UNSUPPORTED("el4sow483b296l5o1hy6oqkzp"); // 			c = (v & 0x3F) | 0x80;
UNSUPPORTED("dkxvw03k2gg9anv4dbze06axd"); // 		    }
UNSUPPORTED("dkxvw03k2gg9anv4dbze06axd"); // 		    }
UNSUPPORTED("4mhlpjofolwivhm0tl8cxznly"); //         }
UNSUPPORTED("atwq5cvh75c4mpqr6f8bepwkd"); //         else /* copy n byte UTF8 characters */
UNSUPPORTED("75h3d3pcb66cff2xszmbwxny0"); //             for (ui = 0; ui < uc; ++ui)
UNSUPPORTED("75m8b2zfuuay6o25xl08y168m"); //                 if ((*s & 0xC0) == 0x80) {
UNSUPPORTED("2bav6kbg19gemwcsyepjeg52u"); //                     ignored = ((((&xb)->ptr >= (&xb)->eptr) ? agxbmore(&xb,1) : 0), (int)(*(&xb)->ptr++ = ((unsigned char)c)));
UNSUPPORTED("5kkh713qn8pc4dhd3omuop8qk"); //                     c = *(unsigned char*)s++;
UNSUPPORTED("7nxu74undh30brb8laojud3f9"); //                 }
UNSUPPORTED("69mmu86j5iw8x34fdfo0k59ff"); //                 else { 
UNSUPPORTED("cjh6htddtrrxjuyqzavdlw01o"); // 		            if (!warned) {
UNSUPPORTED("8ljhi9erpokpqsnveckujskly"); // 		                agerr(AGWARN, "Invalid %d-byte UTF8 found in input of graph %s - treated as Latin-1. Perhaps \"-Gcharset=latin1\" is needed?\n", uc + 1, agnameof(g));
UNSUPPORTED("8sgutsruuu83a337z05bvytk0"); // 		                warned = 1;
UNSUPPORTED("3d2mow5zy6q4vrtc38f78ucgh"); // 		            }
UNSUPPORTED("8jh5xw3y1bjy4poswq4h2wk4n"); // 		            c = cvtAndAppend (c, &xb);
UNSUPPORTED("ctqmerohp1f69mb1v1t20jx33"); //                     break;
UNSUPPORTED("g2y6e9pld3899aejuqyr2x25"); // 	            }
UNSUPPORTED("28mab50dtpxfjz5h216ox1q6w"); // 	    ignored = ((((&xb)->ptr >= (&xb)->eptr) ? agxbmore(&xb,1) : 0), (int)(*(&xb)->ptr++ = ((unsigned char)c)));
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("3g7d9dp3ny3ggno64pwi7nmkg"); //     ns = strdup ((((((&xb)->ptr >= (&xb)->eptr) ? agxbmore(&xb,1) : 0), (int)(*(&xb)->ptr++ = ((unsigned char)'\0'))),(char*)((&xb)->ptr = (&xb)->buf)));
UNSUPPORTED("1at5m9ctjn3ukv5gqtfswik02"); //     agxbfree(&xb);
UNSUPPORTED("98aa6ybsfiu5u7r3j6fsv3snz"); //     return ns;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
} finally {
LEAVING("9yungx7uxqkmzfh2ub6gs9l48","htmlEntityUTF8");
}
}




//3 ckavkcnz5rcrqs17lleds1uxu
// int edgeType (char* s, int dflt) 
@Unused
@Original(version="2.38.0", path="lib/common/utils.c", name="edgeType", key="ckavkcnz5rcrqs17lleds1uxu", definition="int edgeType (char* s, int dflt)")
public static int edgeType(CString s, int dflt) {
ENTERING("ckavkcnz5rcrqs17lleds1uxu","edgeType");
try {
	UNSUPPORTED("h9kzapvoxea4esxgom157wc0"); // int edgeType (char* s, int dflt)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("26e08yupzx95a4pzp1af0t6og"); //     int et;
UNSUPPORTED("73z43mn6ha09hbnvzynnbkvqg"); //     if (!s || (*s == '\0')) return dflt;
UNSUPPORTED("527zd48lq0ay6p16b2whyuafo"); //     et = 0 << 1;
UNSUPPORTED("1ctayzw7ya308i4wpppul6b9o"); //     switch (*s) {
UNSUPPORTED("acwxya6p4cjrbqeuf7gymcmx2"); //     case '0' :    /* false */
UNSUPPORTED("18fcibo027r3vczxrvtju3nah"); // 	et = 1 << 1;
UNSUPPORTED("6aw91xzjmqvmtdvt1di23af8y"); // 	break;
UNSUPPORTED("no8amccag0mew2zmsprfyekp"); //     case '1' :    /* true */
UNSUPPORTED("8to0pat5o6zmyuqjfl01xs9xc"); //     case '2' :
UNSUPPORTED("c7icptpasun232whn2nn5gydx"); //     case '3' :
UNSUPPORTED("44xov6gwt91mlesh02z3zvxx"); //     case '4' :
UNSUPPORTED("4dwlps5sjcl550fvks2ibv2fi"); //     case '5' :
UNSUPPORTED("cr0jhqsceb5y1hcmvtjd1ttgu"); //     case '6' :
UNSUPPORTED("8jq47j7ezu18niwotmuj92cz3"); //     case '7' :
UNSUPPORTED("ami8xk8243o5ku0cyeqxoeiut"); //     case '8' :
UNSUPPORTED("3onv8t8a6v1tmfaz8y7hk9lvv"); //     case '9' :
UNSUPPORTED("8m599inlx0lbuns9r3iiokwxw"); // 	et = 5 << 1;
UNSUPPORTED("6aw91xzjmqvmtdvt1di23af8y"); // 	break;
UNSUPPORTED("vwxe2prs0tywhf20ycwdwa8o"); //     case 'c' :
UNSUPPORTED("e2ux7lqsbmsyyrououuijooiy"); //     case 'C' :
UNSUPPORTED("8zxim9f3q8qdl919cv1v3jf8e"); // 	if (!strcasecmp (s+1, "urved"))
UNSUPPORTED("azc7d85av8k7f1to3mr59m3mz"); // 	    et = 2 << 1;
UNSUPPORTED("b7i0q9ysed6zrjftn8ilgtn0a"); // 	else if (!strcasecmp (s+1, "ompound"))
UNSUPPORTED("aihlhslp3nd26f10vuyjlnb3q"); // 	    et = 6 << 1;
UNSUPPORTED("6aw91xzjmqvmtdvt1di23af8y"); // 	break;
UNSUPPORTED("2ix1d2vw6unhjetclv9vkaw1p"); //     case 'f' :
UNSUPPORTED("2chzjgs8kmwelk00c6469lpx2"); //     case 'F' :
UNSUPPORTED("42jngi39nkk27q16s1sa7sftl"); // 	if (!strcasecmp (s+1, "alse"))
UNSUPPORTED("7xut5zuu25vrpn9gt0f3kc5hz"); // 	    et = 1 << 1;
UNSUPPORTED("6aw91xzjmqvmtdvt1di23af8y"); // 	break;
UNSUPPORTED("7ozigs1hjxmhvwgapx2in25cy"); //     case 'l' :
UNSUPPORTED("c2gttjqnkmx1rnuyjknw7segb"); //     case 'L' :
UNSUPPORTED("96lnofxeiqa1g3g7s02b86h6z"); // 	if (!strcasecmp (s+1, "ine"))
UNSUPPORTED("7xut5zuu25vrpn9gt0f3kc5hz"); // 	    et = 1 << 1;
UNSUPPORTED("6aw91xzjmqvmtdvt1di23af8y"); // 	break;
UNSUPPORTED("5o5i90c7m363f5yyxamxuzok6"); //     case 'n' :
UNSUPPORTED("3ttrfea54jmrshv2796w3a9h2"); //     case 'N' :
UNSUPPORTED("6qibxt06dimtp2r5spwgriorn"); // 	if (!strcasecmp (s+1, "one")) return et;
UNSUPPORTED("bqi51jfycttyx733ls9qw2c18"); // 	if (!strcasecmp (s+1, "o")) return (1 << 1);
UNSUPPORTED("6aw91xzjmqvmtdvt1di23af8y"); // 	break;
UNSUPPORTED("18t59gw7hrgsezibz7bbm0ng3"); //     case 'o' :
UNSUPPORTED("4q6jdsek20d4i9sc5ftmm3mdl"); //     case 'O' :
UNSUPPORTED("8scb0vjws7o3davin33k87o2p"); // 	if (!strcasecmp (s+1, "rtho"))
UNSUPPORTED("48rqxx6odtdnqf676ffe1ll7g"); // 	    et = 4 << 1;
UNSUPPORTED("6aw91xzjmqvmtdvt1di23af8y"); // 	break;
UNSUPPORTED("5cc40qlotkkym6enwcv916835"); //     case 'p' :
UNSUPPORTED("al1clonjqyw2bo1z0li974ijp"); //     case 'P' :
UNSUPPORTED("68l1a5153ouil03qaammm1zty"); // 	if (!strcasecmp (s+1, "olyline"))
UNSUPPORTED("5ytop08aei3hhllfd12904hh7"); // 	    et = 3 << 1;
UNSUPPORTED("6aw91xzjmqvmtdvt1di23af8y"); // 	break;
UNSUPPORTED("20wayzvdomwexzhjzj4wojf4d"); //     case 's' :
UNSUPPORTED("boxft69fzv6rof5elda0zs33z"); //     case 'S' :
UNSUPPORTED("3qs8m2esm62d50tk701b8m0xz"); // 	if (!strcasecmp (s+1, "pline"))
UNSUPPORTED("5l4kd6c21h4bjm98grnqqwra6"); // 	    et = 5 << 1;
UNSUPPORTED("6aw91xzjmqvmtdvt1di23af8y"); // 	break;
UNSUPPORTED("ce41quxcxpj3oi50zybc75b9r"); //     case 't' :
UNSUPPORTED("8drchetff3h6zpsu3m08rqi0q"); //     case 'T' :
UNSUPPORTED("7ln0pymv14hb45h3ypy5955nk"); // 	if (!strcasecmp (s+1, "rue"))
UNSUPPORTED("5l4kd6c21h4bjm98grnqqwra6"); // 	    et = 5 << 1;
UNSUPPORTED("6aw91xzjmqvmtdvt1di23af8y"); // 	break;
UNSUPPORTED("3d55ucqxr8xg0otty2j39hkgz"); //     case 'y' :
UNSUPPORTED("7oihco3xpq1kek2q2dnrfxmcx"); //     case 'Y' :
UNSUPPORTED("679wmbnx0dakltwkxx2svg5ex"); // 	if (!strcasecmp (s+1, "es"))
UNSUPPORTED("5l4kd6c21h4bjm98grnqqwra6"); // 	    et = 5 << 1;
UNSUPPORTED("6aw91xzjmqvmtdvt1di23af8y"); // 	break;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("ckjgbybnvrshn8g32qqpy0ppd"); //     if (!et) {
UNSUPPORTED("79f40sxqwmzmgk4ktfha59mxf"); // 	agerr(AGWARN, "Unknown \"splines\" value: \"%s\" - ignored\n", s);
UNSUPPORTED("mjiefsvltip3uasxic0uipa9"); // 	et = dflt;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("68yadra75shcc0tia9wr9acr4"); //     return et;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
} finally {
LEAVING("ckavkcnz5rcrqs17lleds1uxu","edgeType");
}
}




/* setEdgeType:
 * Sets graph's edge type based on the "splines" attribute.
 * If the attribute is not defined, use default.
 * If the attribute is "", use NONE.
 * If attribute value matches (case indepedent), use match.
 *   ortho => ET_ORTHO
 *   none => ET_NONE
 *   line => ET_LINE
 *   polyline => ET_PLINE
 *   spline => ET_SPLINE
 * If attribute is boolean, true means ET_SPLINE, false means ET_LINE.
 * Else warn and use default.
 */
@Reviewed(when = "12/11/2020")
@Original(version="2.38.0", path="lib/common/utils.c", name="setEdgeType", key="13cpqbf2ztcjdfz4a6v7nv00u", definition="void setEdgeType (graph_t* g, int dflt)")
public static void setEdgeType(Globals zz, ST_Agraph_s g, int dflt) {
ENTERING("13cpqbf2ztcjdfz4a6v7nv00u","setEdgeType");
try {
    CString s = agget(zz, g, new CString("splines"));
    int et;
    
    if ((s) == null) {
	et = dflt;
    }
    else if (s.charAt(0) == '\0') {
	et = ET_NONE;
    }
    else et = edgeType (s, dflt);
    GD_flags(g, GD_flags(g) | et);
} finally {
LEAVING("13cpqbf2ztcjdfz4a6v7nv00u","setEdgeType");
}
}





@Reviewed(when = "13/11/2020")
@Doc("Store size of a node from inch")
@Original(version="2.38.0", path="lib/common/utils.c", name="gv_nodesize", key="80q488y0eqojtsm7osnfydmo5", definition="void gv_nodesize(node_t * n, boolean flip)")
public static void gv_nodesize(ST_Agnode_s n, boolean flip) {
ENTERING("80q488y0eqojtsm7osnfydmo5","gv_nodesize");
try {
    double w;
    
    if (flip) {
        w = INCH2PS(ND_height(n));
        ND_rw(n, w / 2);
        ND_lw(n, w / 2);
        ND_ht(n, INCH2PS(ND_width(n)));
    } 
    else {
        w = INCH2PS(ND_width(n));
        ND_rw(n, w / 2);
        ND_lw(n, w / 2);
        ND_ht(n, INCH2PS(ND_height(n)));
    }
} finally {
LEAVING("80q488y0eqojtsm7osnfydmo5","gv_nodesize");
}
}



}
