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
package gen.lib.cgraph;
import static gen.lib.cdt.dtextract__c.dtextract;
import static gen.lib.cdt.dtrestore__c.dtrestore;
import static gen.lib.cdt.dtsize__c.dtsize_;
import static gen.lib.cgraph.attr__c.agraphattr_init;
import static gen.lib.cgraph.edge__c.agsubrep;
import static gen.lib.cgraph.id__c.agmapnametoid;
import static gen.lib.cgraph.id__c.agregister;
import static gen.lib.cgraph.node__c.agfstnode;
import static gen.lib.cgraph.node__c.agnxtnode;
import static gen.lib.cgraph.obj__c.agmethod_init;
import static gen.lib.cgraph.obj__c.agroot;
import static gen.lib.cgraph.subg__c.agparent;
import static gen.lib.cgraph.utils__c.agdtopen;
import static smetana.core.Macro.AGID;
import static smetana.core.Macro.AGRAPH;
import static smetana.core.Macro.AGSEQ;
import static smetana.core.Macro.AGTYPE;
import static smetana.core.debug.SmetanaDebug.ENTERING;
import static smetana.core.debug.SmetanaDebug.LEAVING;

import gen.annotation.Difficult;
import gen.annotation.Original;
import gen.annotation.Reviewed;
import gen.annotation.Unused;
import h.ST_Agclos_s;
import h.ST_Agdesc_s;
import h.ST_Agdisc_s;
import h.ST_Agmemdisc_s;
import h.ST_Agnode_s;
import h.ST_Agraph_s;
import h.ST_Agsubnode_s;
import h.ST_dt_s;
import h.ST_dtdisc_s;
import h.ST_dtlink_s;
import smetana.core.CFunction;
import smetana.core.CFunctionAbstract;
import smetana.core.CStarStar;
import smetana.core.CString;
import smetana.core.Globals;
import smetana.core.ZType;
import smetana.core.__ptr__;
import smetana.core.size_t;

public class graph__c {


//3 bb2bu9iyqx0u6xx44l282vmch
// static Agclos_t *agclos(Agdisc_t * proto) 
@Unused
@Original(version="2.38.0", path="lib/cgraph/graph.c", name="", key="bb2bu9iyqx0u6xx44l282vmch", definition="static Agclos_t *agclos(Agdisc_t * proto)")
public static ST_Agclos_s agclos(Globals zz, ST_Agdisc_s proto) {
ENTERING("bb2bu9iyqx0u6xx44l282vmch","agclos");
try {
	ST_Agmemdisc_s memdisc;
		__ptr__  memclosure;
		ST_Agclos_s rv;
		/* establish an allocation arena */
		memdisc = (ST_Agmemdisc_s) ((proto != null && proto.mem != null) ? proto.mem : zz.AgMemDisc);
		memclosure = (__ptr__) memdisc.open.exe(zz, proto);
		rv = (ST_Agclos_s) memdisc.alloc.exe(zz, memclosure, new size_t(ZType.ST_Agclos_s));
		rv.disc.mem = memdisc;
		rv.state.mem = memclosure;
		rv.disc.id = ((proto != null && proto.id != null) ? proto.id : zz.AgIdDisc);
		// Translation bug in next line: should be AgIoDisc and not AgIdDisc
		// rv.disc.io = ((proto != null && proto.getPtr("io") != null) ? proto.getPtr("io") : Z.z().AgIoDisc));
		rv.callbacks_enabled = (true);
		return rv;
} finally {
LEAVING("bb2bu9iyqx0u6xx44l282vmch","agclos");
}
}




//3 d5yqn56yii8cdoahswt4n6bug
// Agraph_t *agopen(char *name, Agdesc_t desc, Agdisc_t * arg_disc) 
@Unused
@Original(version="2.38.0", path="lib/cgraph/graph.c", name="", key="d5yqn56yii8cdoahswt4n6bug", definition="Agraph_t *agopen(char *name, Agdesc_t desc, Agdisc_t * arg_disc)")
public static ST_Agraph_s agopen(Globals zz, CString name, final ST_Agdesc_s desc, ST_Agdisc_s arg_disc) {
// WARNING!! STRUCT
return agopen_w_(zz, name, (ST_Agdesc_s) desc.copy(), arg_disc);
}
private static ST_Agraph_s agopen_w_(Globals zz, CString name, final ST_Agdesc_s desc, ST_Agdisc_s arg_disc) {
ENTERING("d5yqn56yii8cdoahswt4n6bug","agopen");
try {
		ST_Agraph_s g;
		ST_Agclos_s clos;
		int gid[] = new int[1];
		clos = agclos(zz, arg_disc);
		g = (ST_Agraph_s) clos.disc.mem.alloc.exe(zz, clos.state.mem, new size_t(ZType.ST_Agraph_s));
    	AGTYPE(g, AGRAPH);
		g.clos = clos;
		g.desc.___(desc);
		((ST_Agdesc_s)g.desc).maingraph = (true) ? 1 : 0;
		g.root = g;
		g.clos.state.id = (__ptr__) g.clos.disc.id.open.exe(zz, g, arg_disc);
		 if (agmapnametoid(zz, g, AGRAPH, name, gid, (true))!=0)
		   AGID(g, gid[0]);
		// /* else AGID(g) = 0 because we have no alternatives */
		g = agopen1(zz, g);
		agregister(zz, g, AGRAPH, g);
		return g;
} finally {
LEAVING("d5yqn56yii8cdoahswt4n6bug","agopen");
}
}




//3 8jyhwfdfm0a877qfz8cjlb8rk
// Agraph_t *agopen1(Agraph_t * g) 
@Unused
@Original(version="2.38.0", path="lib/cgraph/graph.c", name="", key="8jyhwfdfm0a877qfz8cjlb8rk", definition="Agraph_t *agopen1(Agraph_t * g)")
public static ST_Agraph_s agopen1(Globals zz, ST_Agraph_s g) {
ENTERING("8jyhwfdfm0a877qfz8cjlb8rk","agopen1");
try {
    ST_Agraph_s par;
    g.n_seq = agdtopen(zz, g, zz.Ag_subnode_seq_disc, zz.Dttree);
    g.n_id = agdtopen(zz, g, zz.Ag_subnode_id_disc, zz.Dttree);
    g.e_seq = agdtopen(zz, g, g == agroot(g)? zz.Ag_mainedge_seq_disc : zz.Ag_subedge_seq_disc, zz.Dttree);
    g.e_id = agdtopen(zz, g, g == agroot(g)? zz.Ag_mainedge_id_disc : zz.Ag_subedge_id_disc, zz.Dttree);
    g.g_dict = agdtopen(zz, g, zz.Ag_subgraph_id_disc, zz.Dttree);
    par = agparent(g);
    if (par!=null) {
	AGSEQ(g, agnextseq(par, AGRAPH));
  	par.g_dict.searchf.exe(zz, par.g_dict,g,0000001);
    }				/* else AGSEQ=0 */
    if ((par) == null || ((ST_Agdesc_s)par.desc).has_attrs!=0)
	agraphattr_init(zz, g);
    agmethod_init(g, g);
    return g;
} finally {
LEAVING("8jyhwfdfm0a877qfz8cjlb8rk","agopen1");
}
}




//3 axmdmml95l55vlp1vqmh0v5sn
// unsigned long agnextseq(Agraph_t * g, int objtype) 
@Unused
@Original(version="2.38.0", path="lib/cgraph/graph.c", name="agnextseq", key="axmdmml95l55vlp1vqmh0v5sn", definition="unsigned long agnextseq(Agraph_t * g, int objtype)")
public static int agnextseq(ST_Agraph_s g, int objtype) {
ENTERING("axmdmml95l55vlp1vqmh0v5sn","agnextseq");
try {
	int tmp = g.clos.seq[objtype];
	tmp++;
	g.clos.seq[objtype]=tmp;
	return tmp;
} finally {
LEAVING("axmdmml95l55vlp1vqmh0v5sn","agnextseq");
}
}




@Reviewed(when = "12/11/2020")
@Original(version="2.38.0", path="lib/cgraph/graph.c", name="agnnodes", key="688euygrkbl10cveflgwalo2n", definition="int agnnodes(Agraph_t * g)")
public static int agnnodes(ST_Agraph_s g) {
ENTERING("688euygrkbl10cveflgwalo2n","agnnodes");
try {
    return dtsize_((ST_dt_s)g.n_id);
} finally {
LEAVING("688euygrkbl10cveflgwalo2n","agnnodes");
}
}




@Reviewed(when = "14/11/2020")
@Original(version="2.38.0", path="lib/cgraph/graph.c", name="agnedges", key="8zjne7uv8rfpmbv5t96zhnr4u", definition="int agnedges(Agraph_t * g)")
public static int agnedges(Globals zz, ST_Agraph_s g) {
ENTERING("8zjne7uv8rfpmbv5t96zhnr4u","agnedges");
try {
    ST_Agnode_s n;
    int rv = 0;
    
    for (n = agfstnode(zz, g); n!=null; n = agnxtnode(zz, g, n))
	rv += agdegree(zz, g, n, false, true);	/* must use OUT to get self-arcs */
    return rv;
} finally {
LEAVING("8zjne7uv8rfpmbv5t96zhnr4u","agnedges");
}
}




@Reviewed(when = "13/11/2020")
@Original(version="2.38.0", path="lib/cgraph/graph.c", name="agisdirected", key="blvn1w3v0icnucu5m5xvbrba1", definition="int agisdirected(Agraph_t * g)")
public static boolean agisdirected(ST_Agraph_s g) {
ENTERING("blvn1w3v0icnucu5m5xvbrba1","agisdirected");
try {
    return g.desc.directed!=0;
} finally {
LEAVING("blvn1w3v0icnucu5m5xvbrba1","agisdirected");
}
}




@Reviewed(when = "13/11/2020")
@Original(version="2.38.0", path="lib/cgraph/graph.c", name="agisundirected", key="8thgds4eioot64flko26m8ns0", definition="int agisundirected(Agraph_t * g)")
public static boolean agisundirected(ST_Agraph_s g) {
ENTERING("8thgds4eioot64flko26m8ns0","agisundirected");
try {
    return !agisdirected(g);
} finally {
LEAVING("8thgds4eioot64flko26m8ns0","agisundirected");
}
}




@Reviewed(when = "13/11/2020")
@Original(version="2.38.0", path="lib/cgraph/graph.c", name="agisstrict", key="9qgdebmdfrcfjm394bg59a7y5", definition="int agisstrict(Agraph_t * g)")
public static boolean agisstrict(ST_Agraph_s g) {
ENTERING("9qgdebmdfrcfjm394bg59a7y5","agisstrict");
try {
    return g.desc.strict!=0;
} finally {
LEAVING("9qgdebmdfrcfjm394bg59a7y5","agisstrict");
}
}




@Reviewed(when = "14/11/2020")
@Original(version="2.38.0", path="lib/cgraph/graph.c", name="cnt", key="abaldeo2ie6zi60cazxp7rv47", definition="static int cnt(Dict_t * d, Dtlink_t ** set)")
public static int cnt(ST_dt_s d, CStarStar<ST_dtlink_s> set) {
ENTERING("abaldeo2ie6zi60cazxp7rv47","cnt");
try {
	int rv;
    dtrestore(d, set.star());
    rv = dtsize_(d);
    set.star(dtextract(d));
	return rv;
} finally {
LEAVING("abaldeo2ie6zi60cazxp7rv47","cnt");
}
}



@Difficult
@Reviewed(when = "14/11/2020")
@Original(version="2.38.0", path="lib/cgraph/graph.c", name="agdegree", key="2bz40qf0qo7pd6er1ut25gthp", definition="int agdegree(Agraph_t * g, Agnode_t * n, int want_in, int want_out)")
public static int agdegree(Globals zz, ST_Agraph_s g, ST_Agnode_s n, boolean want_in, boolean want_out) {
ENTERING("2bz40qf0qo7pd6er1ut25gthp","agdegree");
try {
	ST_Agsubnode_s sn;
    int rv = 0;
    
    sn = agsubrep(zz, g, n);
    if (sn!=null) {
	if (want_out) rv += cnt(g.e_seq, sn.out_seq_AMP());
	if (want_in) rv += cnt(g.e_seq, sn.in_seq_AMP());
    }
	return rv;
} finally {
LEAVING("2bz40qf0qo7pd6er1ut25gthp","agdegree");
}
}



public static CFunction agraphidcmpf = new CFunctionAbstract("agraphidcmpf") {
	
	public Object exe(Globals zz, Object... args) {
		return agraphidcmpf((ST_dt_s)args[0], (ST_Agraph_s)args[1], (ST_Agraph_s)args[2], (ST_dtdisc_s)args[3]);
	}};
	
//3 dhbtfzzp8n5yygqmhmluo9bxl
// int agraphidcmpf(Dict_t * d, void *arg0, void *arg1, Dtdisc_t * disc) 
@Unused
@Original(version="2.38.0", path="lib/cgraph/graph.c", name="agraphidcmpf", key="dhbtfzzp8n5yygqmhmluo9bxl", definition="int agraphidcmpf(Dict_t * d, void *arg0, void *arg1, Dtdisc_t * disc)")
public static int agraphidcmpf(ST_dt_s d, ST_Agraph_s arg0, ST_Agraph_s arg1, ST_dtdisc_s disc) {
ENTERING("dhbtfzzp8n5yygqmhmluo9bxl","agraphidcmpf");
try {
    int v;
    ST_Agraph_s sg0, sg1;
    sg0 = (ST_Agraph_s) arg0;
    sg1 = (ST_Agraph_s) arg1;
    v = (sg0.tag.id - sg1.tag.id);
    return ((v==0)?0:(v<0?-1:1));
} finally {
LEAVING("dhbtfzzp8n5yygqmhmluo9bxl","agraphidcmpf");
}
}


}
