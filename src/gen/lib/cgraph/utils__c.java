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
import static gen.lib.cdt.dtdisc__c.dtdisc;
import static gen.lib.cdt.dtopen__c.dtopen;
import static smetana.core.JUtils.NEQ;
import static smetana.core.JUtils.function;
import static smetana.core.JUtilsDebug.ENTERING;
import static smetana.core.JUtilsDebug.LEAVING;
import static smetana.core.Macro.UNSUPPORTED;

import gen.annotation.Original;
import gen.annotation.Reviewed;
import gen.annotation.Unused;
import h.Dtmemory_f;
import h.ST_Agraph_s;
import h.ST_dt_s;
import h.ST_dtdisc_s;
import h.ST_dtmethod_s;
import smetana.core.CFunction;
import smetana.core.Z;
import smetana.core.__ptr__;
import smetana.core.size_t;

public class utils__c {

@Reviewed(when = "11/11/2020")
@Original(version="2.38.0", path="lib/cgraph/utils.c", name="agdictobjmem", key="7dkudp41c9byhicatk2sxtxqk", definition="void *agdictobjmem(Dict_t * dict, void * p, size_t size, Dtdisc_t * disc)")
public static Object agdictobjmem(ST_dt_s dict, Object p, size_t size, ST_dtdisc_s disc) {
ENTERING("7dkudp41c9byhicatk2sxtxqk","agdictobjmem");
try {
	return size.malloc();
//	ST_Agraph_s g;
//    g = Z.z().Ag_dictop_G;
//    if (g!=null) {
//	if (p!=null)
//	    agfree(g, p);
//	else
//	    return agalloc(g, size);
//    } else {
//	if (p!=null)
//	    Memory.free(p);
//	else
//	    return size.malloc();
//    }
//    return null;
} finally {
LEAVING("7dkudp41c9byhicatk2sxtxqk","agdictobjmem");
}
}




@Unused
@Original(version="2.38.0", path="lib/cgraph/utils.c", name="agdictobjfree", key="5xdfwxth4q1dm3180qzuf51sn", definition="void agdictobjfree(Dict_t * dict, void * p, Dtdisc_t * disc)")
public static Object agdictobjfree(Object... arg) {
UNSUPPORTED("bsemnw6m2qx5a4hk13xep80nh"); // void agdictobjfree(Dict_t * dict, void * p, Dtdisc_t * disc)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("1dbyk58q3r4fyfxxo7ovemkpu"); //     Agraph_t *g;
UNSUPPORTED("nzl57bfl5onx7q9ge19n2k9i"); //     (void) dict;
UNSUPPORTED("8l8wg6vltx6d7vc9dzqb6n3wi"); //     (void) disc;
UNSUPPORTED("ah8bddgh4pyntvi4o9xx91fkm"); //     g = Ag_dictop_G;
UNSUPPORTED("5skdegxoz3mwfvm59pbtvqtiq"); //     if (g)
UNSUPPORTED("akbizhd9c9w4syt1qpirn7xjy"); // 	agfree(g, p);
UNSUPPORTED("div10atae09n36x269sl208r1"); //     else
UNSUPPORTED("cy5x5dma0v4hiepir7lrfuo17"); // 	free(p);
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




@Original(version="2.38.0", path="lib/cgraph/utils.c", name="agdtopen", key="48ox0bg1qmasrer8np51uwsyk", definition="Dict_t *agdtopen(Agraph_t * g, Dtdisc_t * disc, Dtmethod_t * method)")
public static ST_dt_s agdtopen(ST_Agraph_s g, ST_dtdisc_s disc, ST_dtmethod_s method) {
ENTERING("48ox0bg1qmasrer8np51uwsyk","agdtopen");
try {
    Dtmemory_f memf;
    ST_dt_s d;
    memf = (Dtmemory_f) disc.memoryf;
    disc.memoryf = function(utils__c.class, "agdictobjmem");
    Z.z().Ag_dictop_G = g;
    d = dtopen(disc, method);
    disc.memoryf = (CFunction) memf;
    Z.z().Ag_dictop_G = null;
    return d;
} finally {
LEAVING("48ox0bg1qmasrer8np51uwsyk","agdtopen");
}
}




@Unused
@Original(version="2.38.0", path="lib/cgraph/utils.c", name="agdtdelete", key="6pbz2fsmebq8iy7if4way3ct2", definition="long agdtdelete(Agraph_t * g, Dict_t * dict, void *obj)")
public static int agdtdelete(ST_Agraph_s g, ST_dt_s disc, __ptr__ obj) {
UNSUPPORTED("216ju3s3n4ltlcsntcuo0fg5p"); // long agdtdelete(Agraph_t * g, Dict_t * dict, void *obj)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("id2cse8d1e37coxkbocjgjt4"); //     Ag_dictop_G = g;
UNSUPPORTED("1ii7n9w3quq15wnwynuuwg395"); //     return (long) (*(((Dt_t*)(dict))->searchf))((dict),(void*)(obj),0000002);
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }
throw new UnsupportedOperationException();
}




@Unused
@Original(version="2.38.0", path="lib/cgraph/utils.c", name="agobjfinalize", key="8a6i39x23joa467bqbo4b25ng", definition="int agobjfinalize(void * obj)")
public static Object agobjfinalize(Object... arg) {
UNSUPPORTED("74745f6w9shsg8hps5dn9cunv"); // int agobjfinalize(void * obj)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("dnx10u35ynobkoiuo7v5k2u49"); //     agfree(Ag_dictop_G, obj);
UNSUPPORTED("5oxhd3fvp0gfmrmz12vndnjt"); //     return 0;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




@Unused
@Original(version="2.38.0", path="lib/cgraph/utils.c", name="agdtclose", key="4x6nykawwls34vi6jc9gk1y29", definition="int agdtclose(Agraph_t * g, Dict_t * dict)")
public static Object agdtclose(Object... arg) {
UNSUPPORTED("8snkovnbu003p9w5cgbn12c73"); // int agdtclose(Agraph_t * g, Dict_t * dict)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("ac1z24z48td8pa6zl9az4bqpd"); //     Dtmemory_f memf;
UNSUPPORTED("cawsyfy3xa5ktlemxuyyhd376"); //     Dtdisc_t *disc;
UNSUPPORTED("95hw7dddoneu0z8zzcnb1uslv"); //     disc = dtdisc(dict, ((Dtdisc_t *)0), 0);
UNSUPPORTED("9grut2uf2ma1mtzeq2sfg7p3f"); //     memf = disc->memoryf;
UNSUPPORTED("1jsr5jo041ushyjrdwyazntu7"); //     disc->memoryf = agdictobjmem;
UNSUPPORTED("id2cse8d1e37coxkbocjgjt4"); //     Ag_dictop_G = g;
UNSUPPORTED("32zal75ij4wrh56hfv01bu7as"); //     if (dtclose(dict))
UNSUPPORTED("eleqpc2p2r3hvma6tipoy7tr"); // 	return 1;
UNSUPPORTED("9qqgn587jlvxto7mbtmfh3o00"); //     disc->memoryf = memf;
UNSUPPORTED("dgvsuq3309uz08ww03zstdg8g"); //     Ag_dictop_G = ((Agraph_t*)0);
UNSUPPORTED("5oxhd3fvp0gfmrmz12vndnjt"); //     return 0;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




@Original(version="2.38.0", path="lib/cgraph/utils.c", name="agdtdisc", key="cym72wvu6zffc0vzoa93zha8", definition="void agdtdisc(Agraph_t * g, Dict_t * dict, Dtdisc_t * disc)")
public static void agdtdisc(ST_Agraph_s g, ST_dt_s dict, ST_dtdisc_s disc) {
ENTERING("cym72wvu6zffc0vzoa93zha8","agdtdisc");
try {
    if (disc!=null && NEQ(dtdisc(dict, null, 0), disc)) {
	dtdisc(dict, disc, 0);
    }
    /* else unchanged, disc is same as old disc */
} finally {
LEAVING("cym72wvu6zffc0vzoa93zha8","agdtdisc");
}
}


}
