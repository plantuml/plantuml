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
 * This translation is distributed under the same Licence as the original C program.
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

package smetana.core;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import h.Agedge_s;
import h.Agedgeinfo_t;
import h.Agnode_s;
import h.Agnodeinfo_t;
import h.Agobj_s;
import h.Agraph_s;
import h.Agraphinfo_t;
import h.Agtag_s;
import h.GVC_s;
import h.boxf;
import h.elist;
import h.nlist_t;
// http://docs.oracle.com/javase/specs/jls/se5.0/html/expressions.html#15.7.4
// http://www.jbox.dk/sanos/source/lib/string.c.html
import h.pointf;
import h.port;
import h.splines;
import h.textlabel_t;

public class Macro {

	public static <O> O F(O result, Object... dummy) {
		return result;
	}

	public static int ASINT(boolean v) {
		return v ? 1 : 0;
	}

	public static boolean N(boolean v) {
		return v == false;
	}

	public static boolean N(int i) {
		return i == 0;
	}

	public static boolean N(char c) {
		return c == 0;
	}

	public static boolean N(Object o) {
		if (o instanceof Boolean) {
			throw new IllegalArgumentException();
		}
		if (o instanceof Integer) {
			throw new IllegalArgumentException();
		}
		return o == null;
	}

	public static boolean NOT(boolean v) {
		return v == false;
	}

	public static boolean NOT(int i) {
		return i == 0;
	}

	public static boolean NOT(char c) {
		return c == 0;
	}

	public static boolean NOT(Object o) {
		return o == null;
	}

	public static void TRACE(String functionName) {
		// System.err.println(functionName);
	}

	public static __ptr__ UNSUPPORTED(String comment) {
		throw new UnsupportedOperationException(comment);
	}

	public static int UNSUPPORTED_INT(String comment) {
		throw new UnsupportedOperationException(comment);
	}

	// Graphviz

	// #define AGRAPH 0 /* can't exceed 2 bits. see Agtag_t. */
	// #define AGNODE 1
	// #define AGOUTEDGE 2
	// #define AGINEDGE 3 /* (1 << 1) indicates an edge tag. */
	// #define AGEDGE AGOUTEDGE /* synonym in object kind args */
	public final static int AGRAPH = 0;
	public final static int AGNODE = 1;
	public final static int AGOUTEDGE = 2;
	public final static int AGINEDGE = 3;
	public final static int AGEDGE = AGOUTEDGE;

	// #define AGTAG(obj) (((Agobj_t*)(obj))->tag)
	public static __struct__<Agtag_s> AGTAG(__ptr__ obj) {
		return obj.castTo(Agobj_s.class).getStruct("tag");
	}

	// #define AGTYPE(obj) (AGTAG(obj).objtype)
	public static int AGTYPE(__ptr__ obj) {
		return AGTAG(obj).getInt("objtype");
	}

	public static void AGTYPE(__ptr__ obj, int v) {
		AGTAG(obj).setInt("objtype", v);
	}

	// #define AGID(obj) (AGTAG(obj).id)
	public static int AGID(__ptr__ obj) {
		return AGTAG(obj).getInt("id");
	}

	public static void AGID(__ptr__ obj, int v) {
		AGTAG(obj).setInt("id", v);
	}

	// #define AGSEQ(obj) (AGTAG(obj).seq)
	public static int AGSEQ(__ptr__ obj) {
		return AGTAG(obj).getInt("seq");
	}

	public static void AGSEQ(__ptr__ obj, int v) {
		AGTAG(obj).setInt("seq", v);
	}

	// #define AGATTRWF(obj) (AGTAG(obj).attrwf)
	// #define AGDATA(obj) (((Agobj_t*)(obj))->data)
	public static __ptr__ AGDATA(__ptr__ obj) {
		return obj.castTo(Agobj_s.class).getPtr("data");
	}

	public static void AGDATA(__ptr__ obj, __ptr__ v) {
		obj.castTo(Agobj_s.class).setPtr("data", v);
	}

	// #define AGIN2OUT(e) ((e)-1)
	public static __ptr__ AGIN2OUT(__ptr__ e) {
		return e.plus(-1);
	}

	// #define AGOUT2IN(e) ((e)+1)
	public static __ptr__ AGOUT2IN(__ptr__ e) {
		return e.plus(1);
	}

	// #define AGOPP(e) ((AGTYPE(e)==AGINEDGE)?AGIN2OUT(e):AGOUT2IN(e))
	public static Agedge_s AGOPP(Agedge_s e) {
		return (Agedge_s) (AGTYPE(e) == AGINEDGE ? AGIN2OUT(e) : AGOUT2IN(e));
	}

	// #define AGMKOUT(e) (AGTYPE(e) == AGOUTEDGE? (e): AGIN2OUT(e))
	public static Agedge_s AGMKOUT(__ptr__ e) {
		return (Agedge_s) (AGTYPE(e) == AGOUTEDGE ? (e) : AGIN2OUT(e));
	}

	// #define AGMKIN(e) (AGTYPE(e) == AGINEDGE? (e): AGOUT2IN(e))
	public static Agedge_s AGMKIN(__ptr__ e) {
		return (Agedge_s) (AGTYPE(e) == AGINEDGE ? (e) : AGOUT2IN(e));
	}

	// #define AGTAIL(e) (AGMKIN(e)->node)
	public static Agnode_s AGTAIL(__ptr__ e) {
		return (Agnode_s) AGMKIN(e).getPtr("node");
	}

	public static Agnode_s agtail(__ptr__ e) {
		return (Agnode_s) AGMKIN(e).getPtr("node");
	}

	public static void agtail(Agedge_s e, __ptr__ v) {
		AGMKIN(e).setPtr("node", v);
	}

	// #define AGHEAD(e) (AGMKOUT(e)->node)
	public static Agnode_s AGHEAD(__ptr__ e) {
		return (Agnode_s) AGMKOUT(e).getPtr("node");
	}

	private static Agnode_s aghead(Agedge_s e) {
		return (Agnode_s) AGMKOUT(e).getPtr("node");
	}

	public static void aghead(Agedge_s e, __ptr__ v) {
		AGMKOUT(e).setPtr("node", v);
	}

	// #define agtail(e) AGTAIL(e)
	// #define aghead(e) AGHEAD(e)
	// #define agopp(e) AGOPP(e)
	// #define ageqedge(e,f) (AGMKOUT(e) == AGMKOUT(f))

	// #define AGHEADPOINTER(g) ((Agnoderef_t*)(g->n_seq->data->hh._head))
	// #define AGRIGHTPOINTER(rep) ((Agnoderef_t*)((rep)->seq_link.right?((void*)((rep)->seq_link.right) -
	// offsetof(Agsubnode_t,seq_link)):0))
	// #define AGLEFTPOINTER(rep) ((Agnoderef_t*)((rep)->seq_link.hl._left?((void*)((rep)->seq_link.hl._left) -
	// offsetof(Agsubnode_t,seq_link)):0))
	//
	// #define FIRSTNREF(g) (agflatten(g,1), AGHEADPOINTER(g))
	//
	// #define NEXTNREF(g,rep) (AGRIGHTPOINTER(rep) == AGHEADPOINTER(g)?0:AGRIGHTPOINTER(rep))
	//
	// #define PREVNREF(g,rep) (((rep)==AGHEADPOINTER(g))?0:(AGLEFTPOINTER(rep)))
	//
	// #define LASTNREF(g) (agflatten(g,1), AGHEADPOINTER(g)?AGLEFTPOINTER(AGHEADPOINTER(g)):0)
	// #define NODEOF(rep) ((rep)->node)
	//
	// #define FIRSTOUTREF(g,sn) (agflatten(g,1), (sn)->out_seq)
	// #define LASTOUTREF(g,sn) (agflatten(g,1), (Agedgeref_t*)dtlast(sn->out_seq))
	// #define FIRSTINREF(g,sn) (agflatten(g,1), (sn)->in_seq)
	// #define NEXTEREF(g,rep) ((rep)->right)
	// #define PREVEREF(g,rep) ((rep)->hl._left)
	// /* this is expedient but a bit slimey because it "knows" that dict entries of both nodes
	// and edges are embedded in main graph objects but allocated separately in subgraphs */
	// #define AGSNMAIN(sn) ((sn)==(&((sn)->node->mainsub)))
	// #define EDGEOF(sn,rep) (AGSNMAIN(sn)?((Agedge_t*)((unsigned char*)(rep) - offsetof(Agedge_t,seq_link))) :
	// ((Dthold_t*)(rep))->obj)

	// #define LENGTH(e) (ND_rank(aghead(e)) - ND_rank(agtail(e)))
	public static int LENGTH(Agedge_s e) {
		return ND_rank(aghead(e)) - ND_rank(agtail(e));
	}

	// #define SLACK(e) (LENGTH(e) - ED_minlen(e))
	public static int SLACK(Agedge_s e) {
		return LENGTH(e) - ED_minlen(e);
	}

	// #define SEQ(a,b,c) (((a) <= (b)) && ((b) <= (c)))
	public static boolean SEQ(int a, int b, int c) {
		return (((a) <= (b)) && ((b) <= (c)));
	}

	// #define TREE_EDGE(e) (ED_tree_index(e) >= 0)
	public static boolean TREE_EDGE(Agedge_s e) {
		return ED_tree_index(e) >= 0;
	}

	// #define GD_parent(g) (((Agraphinfo_t*)AGDATA(g))->parent)
	public static __ptr__ GD_parent(Agraph_s g) {
		return AGDATA(g).castTo(Agraphinfo_t.class).getPtr("parent");
	}
	public static void GD_parent(Agraph_s g, __ptr__ v) {
		AGDATA(g).castTo(Agraphinfo_t.class).setPtr("parent", v);
	}
	
	// #define GD_level(g) (((Agraphinfo_t*)AGDATA(g))->level)
	// #define GD_drawing(g) (((Agraphinfo_t*)AGDATA(g))->drawing)
	public static __ptr__ GD_drawing(Agraph_s g) {
		return AGDATA(g).castTo(Agraphinfo_t.class).getPtr("drawing");
	}

	public static void GD_drawing(Agraph_s g, __ptr__ v) {
		AGDATA(g).castTo(Agraphinfo_t.class).setPtr("drawing", v);
	}

	// #define GD_bb(g) (((Agraphinfo_t*)AGDATA(g))->bb)
	public static __struct__<boxf> GD_bb(__ptr__ g) {
		return AGDATA(g).castTo(Agraphinfo_t.class).getStruct("bb");
	}

	// #define GD_gvc(g) (((Agraphinfo_t*)AGDATA(g))->gvc)
	public static GVC_s GD_gvc(Agraph_s g) {
		return (GVC_s) AGDATA(g).castTo(Agraphinfo_t.class).getPtr("gvc");
	}

	public static void GD_gvc(Agraph_s g, GVC_s v) {
		AGDATA(g).castTo(Agraphinfo_t.class).setPtr("gvc", v);
	}

	// #define GD_cleanup(g) (((Agraphinfo_t*)AGDATA(g))->cleanup)
	public static __ptr__ GD_cleanup(Agraph_s g) {
		return AGDATA(g).castTo(Agraphinfo_t.class).getPtr("cleanup");
	}

	// #define GD_dist(g) (((Agraphinfo_t*)AGDATA(g))->dist)
	// #define GD_alg(g) (((Agraphinfo_t*)AGDATA(g))->alg)
	// #define GD_border(g) (((Agraphinfo_t*)AGDATA(g))->border)
	public static __array_of_struct__ GD_border(Agraph_s g) {
		return AGDATA(g).castTo(Agraphinfo_t.class).getArrayOfStruct("border");
	}

	// #define GD_cl_cnt(g) (((Agraphinfo_t*)AGDATA(g))->cl_nt)
	// #define GD_clust(g) (((Agraphinfo_t*)AGDATA(g))->clust)
	public static __ptr__ GD_clust(Agraph_s g) {
		return AGDATA(g).castTo(Agraphinfo_t.class).getPtr("clust");
	}
	public static void GD_clust(Agraph_s g, __ptr__ v) {
		AGDATA(g).castTo(Agraphinfo_t.class).setPtr("clust", v);
	}

	// #define GD_dotroot(g) (((Agraphinfo_t*)AGDATA(g))->dotroot)
	public static Agraph_s GD_dotroot(Agraph_s g) {
		return (Agraph_s) AGDATA(g).castTo(Agraphinfo_t.class).getPtr("dotroot");
	}

	public static void GD_dotroot(Agraph_s g, __ptr__ v) {
		AGDATA(g).castTo(Agraphinfo_t.class).setPtr("dotroot", v);
	}

	// #define GD_comp(g) (((Agraphinfo_t*)AGDATA(g))->comp)
	public static __struct__<nlist_t> GD_comp(Agraph_s g) {
		return AGDATA(g).castTo(Agraphinfo_t.class).getStruct("comp");
	}

	// #define GD_exact_ranksep(g) (((Agraphinfo_t*)AGDATA(g))->exact_ranksep)
	public static int GD_exact_ranksep(Agraph_s g) {
		return AGDATA(g).castTo(Agraphinfo_t.class).getInt("exact_ranksep");
	}

	public static void GD_exact_ranksep(Agraph_s g, int v) {
		AGDATA(g).castTo(Agraphinfo_t.class).setInt("exact_ranksep", v);
	}

	// #define GD_expanded(g) (((Agraphinfo_t*)AGDATA(g))->expanded)
	public static boolean GD_expanded(Agraph_s g) {
		return AGDATA(g).castTo(Agraphinfo_t.class).getBoolean("expanded");
	}
	public static void GD_expanded(Agraph_s g, boolean v) {
		AGDATA(g).castTo(Agraphinfo_t.class).setBoolean("expanded", v);
	}
	
	// #define GD_flags(g) (((Agraphinfo_t*)AGDATA(g))->flags)
	public static int GD_flags(Agraph_s g) {
		return AGDATA(g).castTo(Agraphinfo_t.class).getInt("flags");
	}

	public static void GD_flags(Agraph_s g, int v) {
		AGDATA(g).castTo(Agraphinfo_t.class).setInt("flags", v);
	}

	// #define GD_gui_state(g) (((Agraphinfo_t*)AGDATA(g))->gui_state)
	// #define GD_charset(g) (((Agraphinfo_t*)AGDATA(g))->charset)
	public static int GD_charset(Agraph_s g) {
		return AGDATA(g).castTo(Agraphinfo_t.class).getInt("charset");
	}

	public static void GD_charset(Agraph_s g, int v) {
		AGDATA(g).castTo(Agraphinfo_t.class).setInt("charset", v);
	}

	// #define GD_has_labels(g) (((Agraphinfo_t*)AGDATA(g))->has_labels)
	public static int GD_has_labels(Agraph_s g) {
		return AGDATA(g).castTo(Agraphinfo_t.class).getInt("has_labels");
	}

	public static void GD_has_labels(Agraph_s g, int v) {
		AGDATA(g).castTo(Agraphinfo_t.class).setInt("has_labels", v);
	}

	// #define GD_has_images(g) (((Agraphinfo_t*)AGDATA(g))->has_images)
	// #define GD_has_flat_edges(g) (((Agraphinfo_t*)AGDATA(g))->has_flat_edges)
	public static int GD_has_flat_edges(Agraph_s g) {
		return AGDATA(g).castTo(Agraphinfo_t.class).getInt("has_flat_edges");
	}
	public static void GD_has_flat_edges(Agraph_s g, boolean v) {
		AGDATA(g).castTo(Agraphinfo_t.class).setBoolean("has_flat_edges", v);
	}

	// #define GD_has_sourcerank(g) (((Agraphinfo_t*)AGDATA(g))->has_sourcerank)
	// #define GD_has_sinkrank(g) (((Agraphinfo_t*)AGDATA(g))->has_sinkrank)
	// #define GD_ht1(g) (((Agraphinfo_t*)AGDATA(g))->ht1)
	public static double GD_ht1(Agraph_s g) {
		return AGDATA(g).castTo(Agraphinfo_t.class).getDouble("ht1");
	}

	public static void GD_ht1(Agraph_s g, double v) {
		AGDATA(g).castTo(Agraphinfo_t.class).setDouble("ht1", v);
	}

	// #define GD_ht2(g) (((Agraphinfo_t*)AGDATA(g))->ht2)
	public static double GD_ht2(Agraph_s g) {
		return AGDATA(g).castTo(Agraphinfo_t.class).getDouble("ht2");
	}

	public static void GD_ht2(Agraph_s g, double v) {
		AGDATA(g).castTo(Agraphinfo_t.class).setDouble("ht2", v);
	}

	// #define GD_inleaf(g) (((Agraphinfo_t*)AGDATA(g))->inleaf)
	// #define GD_installed(g) (((Agraphinfo_t*)AGDATA(g))->installed)
	public static int GD_installed(Agraph_s g) {
		return AGDATA(g).castTo(Agraphinfo_t.class).getInt("installed");
	}
	public static void GD_installed(Agraph_s g, int v) {
		AGDATA(g).castTo(Agraphinfo_t.class).setInt("installed", v);
	}

	// #define GD_label(g) (((Agraphinfo_t*)AGDATA(g))->label)
	public static __ptr__ GD_label(Agraph_s g) {
		return AGDATA(g).castTo(Agraphinfo_t.class).getPtr("label");
	}

	// #define GD_leader(g) (((Agraphinfo_t*)AGDATA(g))->leader)
	public static Agnode_s GD_leader(Agraph_s g) {
		return (Agnode_s) AGDATA(g).castTo(Agraphinfo_t.class).getPtr("leader");
	}
	public static void GD_leader(Agraph_s g, __ptr__ v) {
		AGDATA(g).castTo(Agraphinfo_t.class).setPtr("leader", v);
	}
	
	// #define GD_rankdir2(g) (((Agraphinfo_t*)AGDATA(g))->rankdir)
	public static int GD_rankdir2(Agraph_s g) {
		return AGDATA(g).castTo(Agraphinfo_t.class).getInt("rankdir");
	}

	public static void GD_rankdir2(Agraph_s g, int v) {
		AGDATA(g).castTo(Agraphinfo_t.class).setInt("rankdir", v);
	}

	// #define GD_rankdir(g) (((Agraphinfo_t*)AGDATA(g))->rankdir & 0x3)
	public static int GD_rankdir(Agraph_s g) {
		return AGDATA(g).castTo(Agraphinfo_t.class).getInt("rankdir") & 0x3;
	}

	// #define GD_flip(g) (GD_rankdir(g) & 1)
	public static int GD_flip(Agraph_s g) {
		return GD_rankdir(g) & 1;
	}

	// #define GD_realrankdir(g) ((((Agraphinfo_t*)AGDATA(g))->rankdir) >> 2)
	// #define GD_realflip(g) (GD_realrankdir(g) & 1)
	// #define GD_ln(g) (((Agraphinfo_t*)AGDATA(g))->ln)
	public static Agnode_s GD_ln(Agraph_s g) {
		return (Agnode_s) AGDATA(g).castTo(Agraphinfo_t.class).getPtr("ln");
	}
	public static void GD_ln(Agraph_s g, __ptr__ v) {
		AGDATA(g).castTo(Agraphinfo_t.class).setPtr("ln", v);
	}
	
	// #define GD_maxrank(g) (((Agraphinfo_t*)AGDATA(g))->maxrank)
	public static int GD_maxrank(Agraph_s g) {
		return AGDATA(g).castTo(Agraphinfo_t.class).getInt("maxrank");
	}

	public static void GD_maxrank(Agraph_s g, int v) {
		AGDATA(g).castTo(Agraphinfo_t.class).setInt("maxrank", v);
	}

	// #define GD_maxset(g) (((Agraphinfo_t*)AGDATA(g))->maxset)
	public static __ptr__ GD_maxset(Agraph_s g) {
		return AGDATA(g).castTo(Agraphinfo_t.class).getPtr("maxset");
	}

	// #define GD_minrank(g) (((Agraphinfo_t*)AGDATA(g))->minrank)
	public static int GD_minrank(Agraph_s g) {
		return AGDATA(g).castTo(Agraphinfo_t.class).getInt("minrank");
	}

	public static void GD_minrank(Agraph_s g, int v) {
		AGDATA(g).castTo(Agraphinfo_t.class).setInt("minrank", v);
	}

	// #define GD_minset(g) (((Agraphinfo_t*)AGDATA(g))->minset)
	public static __ptr__ GD_minset(Agraph_s g) {
		return AGDATA(g).castTo(Agraphinfo_t.class).getPtr("minset");
	}

	// #define GD_minrep(g) (((Agraphinfo_t*)AGDATA(g))->minrep)
	// #define GD_maxrep(g) (((Agraphinfo_t*)AGDATA(g))->maxrep)
	// #define GD_move(g) (((Agraphinfo_t*)AGDATA(g))->move)
	// #define GD_n_cluster(g) (((Agraphinfo_t*)AGDATA(g))->n_cluster)
	public static int GD_n_cluster(Agraph_s g) {
		return AGDATA(g).castTo(Agraphinfo_t.class).getInt("n_cluster");
	}
	public static void GD_n_cluster(Agraph_s g, int v) {
		AGDATA(g).castTo(Agraphinfo_t.class).setInt("n_cluster", v);
	}

	// #define GD_n_nodes(g) (((Agraphinfo_t*)AGDATA(g))->n_nodes)
	public static int GD_n_nodes(Agraph_s g) {
		return AGDATA(g).castTo(Agraphinfo_t.class).getInt("n_nodes");
	}

	public static void GD_n_nodes(Agraph_s g, int v) {
		AGDATA(g).castTo(Agraphinfo_t.class).setInt("n_nodes", v);
	}

	// #define GD_ndim(g) (((Agraphinfo_t*)AGDATA(g))->ndim)
	// #define GD_odim(g) (((Agraphinfo_t*)AGDATA(g))->odim)
	// #define GD_neato_nlist(g) (((Agraphinfo_t*)AGDATA(g))->neato_nlist)
	// #define GD_nlist(g) (((Agraphinfo_t*)AGDATA(g))->nlist)
	public static Agnode_s GD_nlist(Agraph_s g) {
		return (Agnode_s) AGDATA(g).castTo(Agraphinfo_t.class).getPtr("nlist");
	}

	public static void GD_nlist(Agraph_s g, __ptr__ v) {
		AGDATA(g).castTo(Agraphinfo_t.class).setPtr("nlist", v);
	}

	// #define GD_nodesep(g) (((Agraphinfo_t*)AGDATA(g))->nodesep)
	public static int GD_nodesep(Agraph_s g) {
		return AGDATA(g).castTo(Agraphinfo_t.class).getInt("nodesep");
	}

	public static void GD_nodesep(Agraph_s g, int v) {
		AGDATA(g).castTo(Agraphinfo_t.class).setInt("nodesep", v);
	}

	// #define GD_outleaf(g) (((Agraphinfo_t*)AGDATA(g))->outleaf)
	// #define GD_rank(g) (((Agraphinfo_t*)AGDATA(g))->rank)
	public static __ptr__ GD_rank(Agraph_s g) {
		return AGDATA(g).castTo(Agraphinfo_t.class).getPtr("rank");
	}

	public static void GD_rank(Agraph_s g, __ptr__ v) {
		AGDATA(g).castTo(Agraphinfo_t.class).setPtr("rank", v);
	}

	// #define GD_rankleader(g) (((Agraphinfo_t*)AGDATA(g))->rankleader)
	public static __ptr__ GD_rankleader(Agraph_s g) {
		return AGDATA(g).castTo(Agraphinfo_t.class).getPtr("rankleader");
	}
	public static void GD_rankleader(Agraph_s g, __ptr__ v) {
		AGDATA(g).castTo(Agraphinfo_t.class).setPtr("rankleader", v);
	}

	// #define GD_ranksep(g) (((Agraphinfo_t*)AGDATA(g))->ranksep)
	public static int GD_ranksep(Agraph_s g) {
		return AGDATA(g).castTo(Agraphinfo_t.class).getInt("ranksep");
	}

	public static void GD_ranksep(Agraph_s g, int v) {
		AGDATA(g).castTo(Agraphinfo_t.class).setInt("ranksep", v);
	}

	// #define GD_rn(g) (((Agraphinfo_t*)AGDATA(g))->rn)
	public static Agnode_s GD_rn(Agraph_s g) {
		return (Agnode_s) AGDATA(g).castTo(Agraphinfo_t.class).getPtr("rn");
	}
	public static void GD_rn(Agraph_s g, __ptr__ v) {
		AGDATA(g).castTo(Agraphinfo_t.class).setPtr("rn", v);
	}

	// #define GD_set_type(g) (((Agraphinfo_t*)AGDATA(g))->set_type)
	public static int GD_set_type(Agraph_s g) {
		return AGDATA(g).castTo(Agraphinfo_t.class).getInt("set_type");
	}
	public static void GD_set_type(Agraph_s g, int v) {
		AGDATA(g).castTo(Agraphinfo_t.class).setInt("set_type", v);
	}
	
	
	// #define GD_label_pos(g) (((Agraphinfo_t*)AGDATA(g))->label_pos)
	// #define GD_showboxes(g) (((Agraphinfo_t*)AGDATA(g))->showboxes)
	public static int GD_showboxes(Agraph_s g) {
		return AGDATA(g).castTo(Agraphinfo_t.class).getInt("showboxes");
	}

	public static void GD_showboxes(Agraph_s g, int v) {
		AGDATA(g).castTo(Agraphinfo_t.class).setInt("showboxes", v);
	}

	// #define GD_fontnames(g) (((Agraphinfo_t*)AGDATA(g))->fontnames)
	public static int GD_fontnames(Agraph_s g) {
		return AGDATA(g).castTo(Agraphinfo_t.class).getInt("fontnames");
	}

	public static void GD_fontnames(Agraph_s g, int v) {
		AGDATA(g).castTo(Agraphinfo_t.class).setInt("fontnames", v);
	}

	// #define GD_spring(g) (((Agraphinfo_t*)AGDATA(g))->spring)
	// #define GD_sum_t(g) (((Agraphinfo_t*)AGDATA(g))->sum_t)
	// #define GD_t(g) (((Agraphinfo_t*)AGDATA(g))->t)

	// #define ND_id(n) (((Agnodeinfo_t*)AGDATA(n))->id)
	public static int ND_id(Agnode_s n) {
		return AGDATA(n).castTo(Agnodeinfo_t.class).getInt("id");
	}
	
	// #define ND_alg(n) (((Agnodeinfo_t*)AGDATA(n))->alg)
	public static __ptr__ ND_alg(Agnode_s n) {
		return AGDATA(n).castTo(Agnodeinfo_t.class).getPtr("alg");
	}

	// #define ND_UF_parent(n) (((Agnodeinfo_t*)AGDATA(n))->UF_parent)
	public static Agnode_s ND_UF_parent(__ptr__ n) {
		return (Agnode_s) AGDATA(n).castTo(Agnodeinfo_t.class).getPtr("UF_parent");
	}
	public static void ND_UF_parent(__ptr__ n, __ptr__ v) {
		AGDATA(n).castTo(Agnodeinfo_t.class).setPtr("UF_parent", v);
	}

	// #define ND_set(n) (((Agnodeinfo_t*)AGDATA(n))->set)
	// #define ND_UF_size(n) (((Agnodeinfo_t*)AGDATA(n))->UF_size)
	public static int ND_UF_size(Agnode_s n) {
		return AGDATA(n).castTo(Agnodeinfo_t.class).getInt("UF_size");
	}
	public static void ND_UF_size(Agnode_s n, int v) {
		AGDATA(n).castTo(Agnodeinfo_t.class).setInt("UF_size", v);
	}

	// #define ND_bb(n) (((Agnodeinfo_t*)AGDATA(n))->bb)
	// #define ND_clust(n) (((Agnodeinfo_t*)AGDATA(n))->clust)
	public static Agraph_s ND_clust(__ptr__ n) {
		return (Agraph_s) AGDATA(n).castTo(Agnodeinfo_t.class).getPtr("clust");
	}

	public static void ND_clust(Agnode_s n, __ptr__ v) {
		AGDATA(n).castTo(Agnodeinfo_t.class).setPtr("clust", v);
	}

	// #define ND_coord(n) (((Agnodeinfo_t*)AGDATA(n))->coord)
	public static __struct__<pointf> ND_coord(__ptr__ n) {
		return AGDATA(n).castTo(Agnodeinfo_t.class).getStruct("coord");
	}

	// #define ND_dist(n) (((Agnodeinfo_t*)AGDATA(n))->dist)

	// #define ND_flat_in(n) (((Agnodeinfo_t*)AGDATA(n))->flat_in)
	public static __struct__<elist> ND_flat_in(Agnode_s n) {
		return AGDATA(n).castTo(Agnodeinfo_t.class).getStruct("flat_in");
	}

	// #define ND_flat_out(n) (((Agnodeinfo_t*)AGDATA(n))->flat_out)
	public static __struct__<elist> ND_flat_out(Agnode_s n) {
		return AGDATA(n).castTo(Agnodeinfo_t.class).getStruct("flat_out");
	}

	// #define ND_gui_state(n) (((Agnodeinfo_t*)AGDATA(n))->gui_state)
	// #define ND_has_port(n) (((Agnodeinfo_t*)AGDATA(n))->has_port)
	public static boolean ND_has_port(Agnode_s n) {
		return AGDATA(n).castTo(Agnodeinfo_t.class).getBoolean("has_port");
	}

	// #define ND_rep(n) (((Agnodeinfo_t*)AGDATA(n))->rep)
	// #define ND_heapindex(n) (((Agnodeinfo_t*)AGDATA(n))->heapindex)
	// #define ND_height(n) (((Agnodeinfo_t*)AGDATA(n))->height)
	public static double ND_height(__ptr__ n) {
		return AGDATA(n).castTo(Agnodeinfo_t.class).getDouble("height");
	}

	public static void ND_height(Agnode_s n, double v) {
		AGDATA(n).castTo(Agnodeinfo_t.class).setDouble("height", v);
	}

	// #define ND_hops(n) (((Agnodeinfo_t*)AGDATA(n))->hops)
	// #define ND_ht(n) (((Agnodeinfo_t*)AGDATA(n))->ht)
	public static double ND_ht(__ptr__ n) {
		return AGDATA(n).castTo(Agnodeinfo_t.class).getDouble("ht");
	}

	public static void ND_ht(Agnode_s n, double v) {
		AGDATA(n).castTo(Agnodeinfo_t.class).setDouble("ht", v);
	}

	// #define ND_in(n) (((Agnodeinfo_t*)AGDATA(n))->in)
	public static __struct__<elist> ND_in(Agnode_s n) {
		return AGDATA(n).castTo(Agnodeinfo_t.class).getStruct("in");
	}

	public static void ND_in(__ptr__ n, __struct__<elist> v) {
		AGDATA(n).castTo(Agnodeinfo_t.class).setStruct("in", v);
	}

	// #define ND_inleaf(n) (((Agnodeinfo_t*)AGDATA(n))->inleaf)
	public static __ptr__ ND_inleaf(Agnode_s n) {
		return AGDATA(n).castTo(Agnodeinfo_t.class).getPtr("inleaf");
	}

	// #define ND_label(n) (((Agnodeinfo_t*)AGDATA(n))->label)
	public static textlabel_t ND_label(Agnode_s n) {
		return (textlabel_t) AGDATA(n).castTo(Agnodeinfo_t.class).getPtr("label");
	}

	public static void ND_label(Agnode_s n, __ptr__ v) {
		AGDATA(n).castTo(Agnodeinfo_t.class).setPtr("label", v);
	}

	// #define ND_xlabel(n) (((Agnodeinfo_t*)AGDATA(n))->xlabel)
	public static __ptr__ ND_xlabel(Agnode_s n) {
		return AGDATA(n).castTo(Agnodeinfo_t.class).getPtr("xlabel");
	}

	// #define ND_lim(n) (((Agnodeinfo_t*)AGDATA(n))->lim)
	public static int ND_lim(Agnode_s n) {
		return AGDATA(n).castTo(Agnodeinfo_t.class).getInt("lim");
	}

	public static void ND_lim(Agnode_s n, int v) {
		AGDATA(n).castTo(Agnodeinfo_t.class).setInt("lim", v);
	}

	// #define ND_low(n) (((Agnodeinfo_t*)AGDATA(n))->low)
	public static int ND_low(Agnode_s n) {
		return AGDATA(n).castTo(Agnodeinfo_t.class).getInt("low");
	}

	public static void ND_low(Agnode_s n, int v) {
		AGDATA(n).castTo(Agnodeinfo_t.class).setInt("low", v);
	}

	// #define ND_lw(n) (((Agnodeinfo_t*)AGDATA(n))->lw)
	public static double ND_lw(__ptr__ n) {
		return AGDATA(n).castTo(Agnodeinfo_t.class).getDouble("lw");
	}

	public static void ND_lw(Agnode_s n, double v) {
		AGDATA(n).castTo(Agnodeinfo_t.class).setDouble("lw", v);
	}

	// #define ND_mark(n) (((Agnodeinfo_t*)AGDATA(n))->mark)
	public static int ND_mark(__ptr__ n) {
		return AGDATA(n).castTo(Agnodeinfo_t.class).getInt("mark");
	}
	public static void ND_mark(__ptr__ n, int v) {
		AGDATA(n).castTo(Agnodeinfo_t.class).setInt("mark", v);
	}
	public static void ND_mark(__ptr__ n, boolean v) {
		AGDATA(n).castTo(Agnodeinfo_t.class).setBoolean("mark", v);
	}

	// #define ND_mval(n) (((Agnodeinfo_t*)AGDATA(n))->mval)
	public static double ND_mval(__ptr__ n) {
		return AGDATA(n).castTo(Agnodeinfo_t.class).getDouble("mval");
	}
	public static void ND_mval(Agnode_s n, double v) {
		AGDATA(n).castTo(Agnodeinfo_t.class).setDouble("mval", v);
	}

	// #define ND_n_cluster(n) (((Agnodeinfo_t*)AGDATA(n))->n_cluster)
	// #define ND_next(n) (((Agnodeinfo_t*)AGDATA(n))->next)
	public static Agnode_s ND_next(Agnode_s n) {
		return (Agnode_s) AGDATA(n).castTo(Agnodeinfo_t.class).getPtr("next");
	}
	public static void ND_next(__ptr__ n, __ptr__ v) {
		AGDATA(n).castTo(Agnodeinfo_t.class).setPtr("next", v);
	}

	// #define ND_node_type(n) (((Agnodeinfo_t*)AGDATA(n))->node_type)
	public static int ND_node_type(Agnode_s n) {
		return AGDATA(n).castTo(Agnodeinfo_t.class).getInt("node_type");
	}
	public static void ND_node_type(Agnode_s n, int v) {
		AGDATA(n).castTo(Agnodeinfo_t.class).setInt("node_type", v);
	}

	// #define ND_onstack(n) (((Agnodeinfo_t*)AGDATA(n))->onstack)
	public static boolean ND_onstack(Agnode_s n) {
		return AGDATA(n).castTo(Agnodeinfo_t.class).getBoolean("onstack");
	}
	public static void ND_onstack(Agnode_s n, int v) {
		AGDATA(n).castTo(Agnodeinfo_t.class).setInt("onstack", v);
	}
	public static void ND_onstack(Agnode_s n, boolean v) {
		AGDATA(n).castTo(Agnodeinfo_t.class).setBoolean("onstack", v);
	}

	// #define ND_order(n) (((Agnodeinfo_t*)AGDATA(n))->order)
	public static int ND_order(__ptr__ n) {
		return AGDATA(n).castTo(Agnodeinfo_t.class).getInt("order");
	}
	public static void ND_order(Agnode_s n, int v) {
		AGDATA(n).castTo(Agnodeinfo_t.class).setInt("order", v);
	}

	// #define ND_other(n) (((Agnodeinfo_t*)AGDATA(n))->other)
	public static __struct__<elist> ND_other(Agnode_s n) {
		return AGDATA(n).castTo(Agnodeinfo_t.class).getStruct("other");
	}

	// #define ND_out(n) (((Agnodeinfo_t*)AGDATA(n))->out)
	public static __struct__<elist> ND_out(__ptr__ n) {
		return AGDATA(n).castTo(Agnodeinfo_t.class).getStruct("out");
	}
	public static void ND_out(__ptr__ n, __struct__<elist> v) {
		AGDATA(n).castTo(Agnodeinfo_t.class).setStruct("out", v);
	}

	// #define ND_outleaf(n) (((Agnodeinfo_t*)AGDATA(n))->outleaf)
	public static __ptr__ ND_outleaf(Agnode_s n) {
		return AGDATA(n).castTo(Agnodeinfo_t.class).getPtr("outleaf");
	}

	// #define ND_par(n) (((Agnodeinfo_t*)AGDATA(n))->par)
	public static Agedge_s ND_par(Agnode_s n) {
		return (Agedge_s) AGDATA(n).castTo(Agnodeinfo_t.class).getPtr("par");
	}
	public static void ND_par(Agnode_s n, __ptr__ v) {
		AGDATA(n).castTo(Agnodeinfo_t.class).setPtr("par", v);
	}

	// #define ND_pinned(n) (((Agnodeinfo_t*)AGDATA(n))->pinned)
	// #define ND_pos(n) (((Agnodeinfo_t*)AGDATA(n))->pos)
	// #define ND_prev(n) (((Agnodeinfo_t*)AGDATA(n))->prev)
	public static __ptr__ ND_prev(Agnode_s n) {
		return AGDATA(n).castTo(Agnodeinfo_t.class).getPtr("prev");
	}
	public static void ND_prev(Agnode_s n, __ptr__ v) {
		AGDATA(n).castTo(Agnodeinfo_t.class).setPtr("prev", v);
	}

	// #define ND_priority(n) (((Agnodeinfo_t*)AGDATA(n))->priority)
	public static int ND_priority(Agnode_s n) {
		return AGDATA(n).castTo(Agnodeinfo_t.class).getInt("priority");
	}
	public static void ND_priority(Agnode_s n, int v) {
		AGDATA(n).castTo(Agnodeinfo_t.class).setInt("priority", v);
	}

	// #define ND_rank(n) (((Agnodeinfo_t*)AGDATA(n))->rank)
	public static int ND_rank(__ptr__ n) {
		return AGDATA(n).castTo(Agnodeinfo_t.class).getInt("rank");
	}
	public static void ND_rank(__ptr__ n, int v) {
		AGDATA(n).castTo(Agnodeinfo_t.class).setInt("rank", v);
	}

	// #define ND_ranktype(n) (((Agnodeinfo_t*)AGDATA(n))->ranktype)
	public static int ND_ranktype(Agnode_s n) {
		return AGDATA(n).castTo(Agnodeinfo_t.class).getInt("ranktype");
	}
	public static void ND_ranktype(Agnode_s n, int v) {
		AGDATA(n).castTo(Agnodeinfo_t.class).setInt("ranktype", v);
	}

	// #define ND_rw(n) (((Agnodeinfo_t*)AGDATA(n))->rw)
	public static double ND_rw(__ptr__ n) {
		return AGDATA(n).castTo(Agnodeinfo_t.class).getDouble("rw");
	}
	public static void ND_rw(Agnode_s n, double v) {
		AGDATA(n).castTo(Agnodeinfo_t.class).setDouble("rw", v);
	}

	// #define ND_save_in(n) (((Agnodeinfo_t*)AGDATA(n))->save_in)
	public static __struct__<elist> ND_save_in(Agnode_s n) {
		return AGDATA(n).castTo(Agnodeinfo_t.class).getStruct("save_in");
	}

	public static void ND_save_in(Agnode_s n, __struct__<elist> v) {
		AGDATA(n).castTo(Agnodeinfo_t.class).setStruct("save_in", v);
	}

	// #define ND_save_out(n) (((Agnodeinfo_t*)AGDATA(n))->save_out)
	public static __struct__<elist> ND_save_out(Agnode_s n) {
		return AGDATA(n).castTo(Agnodeinfo_t.class).getStruct("save_out");
	}
	public static void ND_save_out(Agnode_s n, __struct__<elist> v) {
		AGDATA(n).castTo(Agnodeinfo_t.class).setStruct("save_out", v);
	}

	// #define ND_shape(n) (((Agnodeinfo_t*)AGDATA(n))->shape)
	public static __ptr__ ND_shape(Agnode_s n) {
		return AGDATA(n).castTo(Agnodeinfo_t.class).getPtr("shape");
	}
	public static void ND_shape(Agnode_s n, __ptr__ v) {
		AGDATA(n).castTo(Agnodeinfo_t.class).setPtr("shape", v);
	}

	// #define ND_shape_info(n) (((Agnodeinfo_t*)AGDATA(n))->shape_info)
	public static __ptr__ ND_shape_info(__ptr__ n) {
		return AGDATA(n).castTo(Agnodeinfo_t.class).getPtr("shape_info");
	}

	// #define ND_showboxes(n) (((Agnodeinfo_t*)AGDATA(n))->showboxes)
	public static int ND_showboxes(Agnode_s n) {
		return AGDATA(n).castTo(Agnodeinfo_t.class).getInt("showboxes");
	}
	public static void ND_showboxes(Agnode_s n, int v) {
		AGDATA(n).castTo(Agnodeinfo_t.class).setInt("showboxes", v);
	}

	// #define ND_state(n) (((Agnodeinfo_t*)AGDATA(n))->state)
	// #define ND_clustnode(n) (((Agnodeinfo_t*)AGDATA(n))->clustnode)
	// #define ND_tree_in(n) (((Agnodeinfo_t*)AGDATA(n))->tree_in)
	public static __struct__<elist> ND_tree_in(Agnode_s n) {
		return AGDATA(n).castTo(Agnodeinfo_t.class).getStruct("tree_in");
	}

	// #define ND_tree_out(n) (((Agnodeinfo_t*)AGDATA(n))->tree_out)
	public static __struct__<elist> ND_tree_out(Agnode_s n) {
		return AGDATA(n).castTo(Agnodeinfo_t.class).getStruct("tree_out");
	}

	// #define ND_weight_class(n) (((Agnodeinfo_t*)AGDATA(n))->weight_class)
	public static int ND_weight_class(Agnode_s n) {
		return AGDATA(n).castTo(Agnodeinfo_t.class).getInt("weight_class");
	}
	public static void ND_weight_class(Agnode_s n, int v) {
		AGDATA(n).castTo(Agnodeinfo_t.class).setInt("weight_class", v);
	}

	// #define ND_width(n) (((Agnodeinfo_t*)AGDATA(n))->width)
	public static double ND_width(__ptr__ n) {
		return AGDATA(n).castTo(Agnodeinfo_t.class).getDouble("width");
	}
	public static void ND_width(Agnode_s n, double v) {
		AGDATA(n).castTo(Agnodeinfo_t.class).setDouble("width", v);
	}

	// #define ND_xsize(n) (ND_lw(n)+ND_rw(n))
	// #define ND_ysize(n) (ND_ht(n))

	// #define ED_alg(e) (((Agedgeinfo_t*)AGDATA(e))->alg)
	// #define ED_conc_opp_flag(e) (((Agedgeinfo_t*)AGDATA(e))->conc_opp_flag)
	public static boolean ED_conc_opp_flag(Agedge_s e) {
		return AGDATA(e).castTo(Agedgeinfo_t.class).getBoolean("conc_opp_flag");
	}
	public static void ED_conc_opp_flag(Agedge_s e, boolean v) {
		AGDATA(e).castTo(Agedgeinfo_t.class).setInt("conc_opp_flag", v ? 1 : 0);
	}

	// #define ED_count(e) (((Agedgeinfo_t*)AGDATA(e))->count)
	public static int ED_count(__ptr__ e) {
		return AGDATA(e).castTo(Agedgeinfo_t.class).getInt("count");
	}
	public static void ED_count(__ptr__ e, int v) {
		AGDATA(e).castTo(Agedgeinfo_t.class).setInt("count", v);
	}

	// #define ED_cutvalue(e) (((Agedgeinfo_t*)AGDATA(e))->cutvalue)
	public static int ED_cutvalue(Agedge_s e) {
		return AGDATA(e).castTo(Agedgeinfo_t.class).getInt("cutvalue");
	}
	public static void ED_cutvalue(Agedge_s e, int v) {
		AGDATA(e).castTo(Agedgeinfo_t.class).setInt("cutvalue", v);
	}

	// #define ED_edge_type(e) (((Agedgeinfo_t*)AGDATA(e))->edge_type)
	// #define ED_adjacent(e) (((Agedgeinfo_t*)AGDATA(e))->adjacent)
	public static int ED_adjacent(__ptr__ e) {
		return AGDATA(e).castTo(Agedgeinfo_t.class).getInt("adjacent");
	}
	public static void ED_adjacent(Agedge_s e, int v) {
		AGDATA(e).castTo(Agedgeinfo_t.class).setInt("adjacent", v);
	}

	// #define ED_factor(e) (((Agedgeinfo_t*)AGDATA(e))->factor)
	// #define ED_gui_state(e) (((Agedgeinfo_t*)AGDATA(e))->gui_state)
	// #define ED_head_label(e) (((Agedgeinfo_t*)AGDATA(e))->head_label)
	public static __ptr__ ED_head_label(Agedge_s e) {
		return AGDATA(e).castTo(Agedgeinfo_t.class).getPtr("head_label");
	}

	// #define ED_head_port(e) (((Agedgeinfo_t*)AGDATA(e))->head_port)
	public static __struct__<port> ED_head_port(__ptr__ e) {
		return (__struct__<port>) AGDATA(e).castTo(Agedgeinfo_t.class).getStruct("head_port");
	}
	public static void ED_head_port(Agedge_s e, __struct__<port> v) {
		AGDATA(e).castTo(Agedgeinfo_t.class).setStruct("head_port", v);
	}

	// #define ED_label(e) (((Agedgeinfo_t*)AGDATA(e))->label)
	public static textlabel_t ED_label(__ptr__ e) {
		return (textlabel_t) AGDATA(e).castTo(Agedgeinfo_t.class).getPtr("label");
	}
	public static void ED_label(Agedge_s e, __ptr__ v) {
		AGDATA(e).castTo(Agedgeinfo_t.class).setPtr("label", v);
	}

	// #define ED_xlabel(e) (((Agedgeinfo_t*)AGDATA(e))->xlabel)
	public static __ptr__ ED_xlabel(Agedge_s e) {
		return AGDATA(e).castTo(Agedgeinfo_t.class).getPtr("xlabel");
	}

	// #define ED_label_ontop(e) (((Agedgeinfo_t*)AGDATA(e))->label_ontop)
	public static boolean ED_label_ontop(Agedge_s e) {
		return AGDATA(e).castTo(Agedgeinfo_t.class).getBoolean("label_ontop");
	}
	public static void ED_label_ontop(Agedge_s e, boolean v) {
		AGDATA(e).castTo(Agedgeinfo_t.class).setBoolean("label_ontop", v);
	}

	// #define ED_minlen(e) (((Agedgeinfo_t*)AGDATA(e))->minlen)
	public static int ED_minlen(Agedge_s e) {
		return AGDATA(e).castTo(Agedgeinfo_t.class).getInt("minlen");
	}
	public static void ED_minlen(Agedge_s e, int v) {
		AGDATA(e).castTo(Agedgeinfo_t.class).setInt("minlen", v);
	}

	// #define ED_path(e) (((Agedgeinfo_t*)AGDATA(e))->path)
	// #define ED_showboxes(e) (((Agedgeinfo_t*)AGDATA(e))->showboxes)
	public static int ED_showboxes(Agedge_s e) {
		return AGDATA(e).castTo(Agedgeinfo_t.class).getInt("showboxes");
	}
	public static void ED_showboxes(Agedge_s e, int v) {
		AGDATA(e).castTo(Agedgeinfo_t.class).setInt("showboxes", v);
	}

	// #define ED_spl(e) (((Agedgeinfo_t*)AGDATA(e))->spl)
	public static splines ED_spl(Agedge_s e) {
		return (splines) AGDATA(e).castTo(Agedgeinfo_t.class).getPtr("spl");
	}
	public static void ED_spl(Agedge_s e, __ptr__ v) {
		AGDATA(e).castTo(Agedgeinfo_t.class).setPtr("spl", v);
	}

	// #define ED_tail_label(e) (((Agedgeinfo_t*)AGDATA(e))->tail_label)
	public static __ptr__ ED_tail_label(Agedge_s e) {
		return AGDATA(e).castTo(Agedgeinfo_t.class).getPtr("tail_label");
	}

	// #define ED_tail_port(e) (((Agedgeinfo_t*)AGDATA(e))->tail_port)
	public static __struct__<port> ED_tail_port(__ptr__ e) {
		return (__struct__<port>) AGDATA(e).castTo(Agedgeinfo_t.class).getStruct("tail_port");
	}
	public static void ED_tail_port(Agedge_s e, __struct__<port> v) {
		AGDATA(e).castTo(Agedgeinfo_t.class).setStruct("tail_port", v);
	}

	// #define ED_to_orig(e) (((Agedgeinfo_t*)AGDATA(e))->to_orig)
	public static Agedge_s ED_to_orig(__ptr__ e) {
		return (Agedge_s) AGDATA(e).castTo(Agedgeinfo_t.class).getPtr("to_orig");
	}
	public static void ED_to_orig(Agedge_s e, __ptr__ v) {
		AGDATA(e).castTo(Agedgeinfo_t.class).setPtr("to_orig", v);
	}

	// #define ED_to_virt(e) (((Agedgeinfo_t*)AGDATA(e))->to_virt)
	public static Agedge_s ED_to_virt(Agedge_s e) {
		return (Agedge_s) AGDATA(e).castTo(Agedgeinfo_t.class).getPtr("to_virt");
	}
	public static void ED_to_virt(Agedge_s e, __ptr__ v) {
		AGDATA(e).castTo(Agedgeinfo_t.class).setPtr("to_virt", v);
	}

	// #define ED_tree_index(e) (((Agedgeinfo_t*)AGDATA(e))->tree_index)
	public static int ED_tree_index(__ptr__ e) {
		return AGDATA(e).castTo(Agedgeinfo_t.class).getInt("tree_index");
	}
	public static void ED_tree_index(__ptr__ e, int v) {
		AGDATA(e).castTo(Agedgeinfo_t.class).setInt("tree_index", v);
	}

	// #define ED_xpenalty(e) (((Agedgeinfo_t*)AGDATA(e))->xpenalty)
	public static int ED_xpenalty(__ptr__ e) {
		return AGDATA(e).castTo(Agedgeinfo_t.class).getInt("xpenalty");
	}
	public static void ED_xpenalty(Agedge_s e, int v) {
		AGDATA(e).castTo(Agedgeinfo_t.class).setInt("xpenalty", v);
	}

	// #define ED_dist(e) (((Agedgeinfo_t*)AGDATA(e))->dist)
	public static double ED_dist(Agedge_s e) {
		return AGDATA(e).castTo(Agedgeinfo_t.class).getDouble("dist");
	}
	public static void ED_dist(Agedge_s e, double v) {
		AGDATA(e).castTo(Agedgeinfo_t.class).setDouble("dist", v);
	}

	// #define ED_weight(e) (((Agedgeinfo_t*)AGDATA(e))->weight)
	public static int ED_weight(Agedge_s e) {
		return AGDATA(e).castTo(Agedgeinfo_t.class).getInt("weight");
	}
	public static void ED_weight(Agedge_s e, int v) {
		AGDATA(e).castTo(Agedgeinfo_t.class).setInt("weight", v);
	}

	//
	// #define ED_alg(e) (((Agedgeinfo_t*)AGDATA(e))->alg)
	// #define ED_conc_opp_flag(e) (((Agedgeinfo_t*)AGDATA(e))->conc_opp_flag)
	// #define ED_count(e) (((Agedgeinfo_t*)AGDATA(e))->count)
	// #define ED_cutvalue(e) (((Agedgeinfo_t*)AGDATA(e))->cutvalue)
	// #define ED_edge_type(e) (((Agedgeinfo_t*)AGDATA(e))->edge_type)
	public static int ED_edge_type(Agedge_s e) {
		return AGDATA(e).castTo(Agedgeinfo_t.class).getInt("edge_type");
	}
	public static void ED_edge_type(Agedge_s e, int v) {
		AGDATA(e).castTo(Agedgeinfo_t.class).setInt("edge_type", v);
	}

	// #define ED_adjacent(e) (((Agedgeinfo_t*)AGDATA(e))->adjacent)
	// #define ED_factor(e) (((Agedgeinfo_t*)AGDATA(e))->factor)
	// #define ED_gui_state(e) (((Agedgeinfo_t*)AGDATA(e))->gui_state)
	// #define ED_head_label(e) (((Agedgeinfo_t*)AGDATA(e))->head_label)
	// #define ED_head_port(e) (((Agedgeinfo_t*)AGDATA(e))->head_port)
	// #define ED_label(e) (((Agedgeinfo_t*)AGDATA(e))->label)
	// #define ED_xlabel(e) (((Agedgeinfo_t*)AGDATA(e))->xlabel)
	// #define ED_label_ontop(e) (((Agedgeinfo_t*)AGDATA(e))->label_ontop)
	// #define ED_minlen(e) (((Agedgeinfo_t*)AGDATA(e))->minlen)
	// #define ED_path(e) (((Agedgeinfo_t*)AGDATA(e))->path)
	// #define ED_showboxes(e) (((Agedgeinfo_t*)AGDATA(e))->showboxes)
	// #define ED_spl(e) (((Agedgeinfo_t*)AGDATA(e))->spl)
	// #define ED_tail_label(e) (((Agedgeinfo_t*)AGDATA(e))->tail_label)
	// #define ED_tail_port(e) (((Agedgeinfo_t*)AGDATA(e))->tail_port)
	// #define ED_to_orig(e) (((Agedgeinfo_t*)AGDATA(e))->to_orig)
	// #define ED_to_virt(e) (((Agedgeinfo_t*)AGDATA(e))->to_virt)
	// #define ED_tree_index(e) (((Agedgeinfo_t*)AGDATA(e))->tree_index)
	// #define ED_xpenalty(e) (((Agedgeinfo_t*)AGDATA(e))->xpenalty)
	// #define ED_dist(e) (((Agedgeinfo_t*)AGDATA(e))->dist)
	// #define ED_weight(e) (((Agedgeinfo_t*)AGDATA(e))->weight)

	// #define elist_fastapp(item,L) do {L.list[L.size++] = item; L.list[L.size] = NULL;} while(0)

	// #define ALLOC(size,ptr,type) (ptr? (type*)realloc(ptr,(size)*sizeof(type)):(type*)malloc((size)*sizeof(type)))
	public static __ptr__ ALLOC_empty(int size, __ptr__ ptr, Class type) {
		return (__ptr__) (ptr != null ? JUtils.sizeof_starstar_empty(type, size).realloc(ptr) : JUtils
				.sizeof_starstar_empty(type, size).malloc());
	}

	public static __ptr__ ALLOC_allocated2(int size, __ptr__ ptr, Class type) {
		return (__ptr__) (ptr != null ? JUtils.sizeof(type, size).realloc(ptr) : JUtils.sizeof(type, size).malloc());
	}

	public static __ptr__ ALLOC_INT(int size, __ptr__ ptr) {
		return (__ptr__) (ptr != null ? JUtils.size_t_array_of_integer(size).realloc(ptr) : JUtils
				.size_t_array_of_integer(size).malloc());
	}

	// #define RALLOC(size,ptr,type) ((type*)realloc(ptr,(size)*sizeof(type)))
	public static __ptr__ RALLOC(int nb, __ptr__ ptr, Class type) {
		throw new UnsupportedOperationException();
	}

	public static __ptr__ ALLOC(int nb, __ptr__ ptr, Class type) {
		if (ptr == null) {
			return (__ptr__) JUtils.sizeof(type, nb).malloc();
		}
		return (__ptr__) JUtils.sizeof(type, nb).malloc();
	}

	// #define elist_append(item,L) do {L.list = ALLOC(L.size + 2,L.list,edge_t*); L.list[L.size++] = item;
	// L.list[L.size] = NULL;} while(0)
	public static void elist_append(__ptr__ item, __struct__ L) {
		L.setPtr("list", ALLOC_empty(L.getInt("size") + 2, L.getPtr("list"), Agedge_s.class));
		L.getArrayOfPtr("list").plus(L.getInt("size")).setPtr(item);
		L.setInt("size", 1 + L.getInt("size"));
		L.getArrayOfPtr("list").plus(L.getInt("size")).setPtr(null);
	}

	// #define alloc_elist(n,L) do {L.size = 0; L.list = N_NEW(n + 1,edge_t*); } while (0)
	public static void alloc_elist(int n, __struct__ L, Class cl) {
		L.setInt("size", 0);
		L.setPtr("list", (__ptr__) JUtils.sizeof_starstar_empty(cl, n + 1).malloc());
	}

	// #define free_list(L) do {if (L.list) free(L.list);} while (0)
	public static void free_list(__struct__ L) {
		if (L.getPtr("list") != null)
			Memory.free(L.getPtr("list"));
	}

	public static double ABS(double a) {
		return Math.abs(a);
	}

	public static int ABS(int a) {
		return Math.abs(a);
	}

	public static double MAX(double a, double b) {
		return Math.max(a, b);
	}

	public static int MAX(int a, int b) {
		return Math.max(a, b);
	}

	public static double MIN(double a, double b) {
		return Math.min(a, b);
	}

	public static int MIN(int a, int b) {
		return Math.min(a, b);
	}

	// #define BETWEEN(a,b,c) (((a) <= (b)) && ((b) <= (c)))
	public static boolean BETWEEN(double a, double b, double c) {
		return (((a) <= (b)) && ((b) <= (c)));
	}

	public static boolean BETWEEN(int a, int b, int c) {
		return (((a) <= (b)) && ((b) <= (c)));
	}

	// #define ROUND(f) ((f>=0)?(int)(f + .5):(int)(f - .5))
	public static int ROUND(double f) {
		return ((f >= 0) ? (int) (f + .5) : (int) (f - .5));
	}

	// #define MAKEFWDEDGE(new, old) { \
	// edge_t *newp; \
	// Agedgeinfo_t *info; \
	// newp = new; \
	// info = (Agedgeinfo_t*)newp->base.data; \
	// *info = *(Agedgeinfo_t*)old->base.data; \
	// *newp = *old; \
	// newp->base.data = (Agrec_t*)info; \
	// AGTAIL(newp) = AGHEAD(old); \
	// AGHEAD(newp) = AGTAIL(old); \
	// ED_tail_port(newp) = ED_head_port(old); \
	// ED_head_port(newp) = ED_tail_port(old); \
	// ED_edge_type(newp) = VIRTUAL; \
	// ED_to_orig(newp) = old; \

	public static void MAKEFWDEDGE(__ptr__ new_, __ptr__ old) {
		Agedge_s newp;
		Agedgeinfo_t info;
		newp = (Agedge_s) new_;
		info = (Agedgeinfo_t) newp.getStruct("base").getPtr("data");
		info.copyDataFrom(old.getStruct("base").getPtr("data").castTo(Agedgeinfo_t.class).getStruct());
		newp.copyDataFrom(old);
		newp.getStruct("base").setPtr("data", info);
		agtail(newp, AGHEAD(old));
		aghead(newp, AGTAIL(old));
		ED_tail_port(newp, ED_head_port(old));
		ED_head_port(newp, ED_tail_port(old));
		ED_edge_type(newp, VIRTUAL);
		ED_to_orig(newp, old);
	}

	// #define VIRTUAL 1 /* virtual nodes in long edge chains */
	public static final int VIRTUAL = 1;

	// #define ZALLOC(size,ptr,type,osize) (ptr?
	// (type*)zrealloc(ptr,size,sizeof(type),osize):(type*)zmalloc((size)*sizeof(type)))
	public static __ptr__ ZALLOC(int size, __ptr__ ptr, Class type, int osize) {
		if (ptr != null) {
			throw new UnsupportedOperationException();
		}
		return (__ptr__) JUtils.sizeof(type, size).malloc();
	}

	public static final int MAXSHORT = 0x7fff;

	public static final int INT_MAX = Integer.MAX_VALUE;
	// #define INT_MIN (-INT_MAX - 1)
	public static final int INT_MIN = Integer.MIN_VALUE;
	static {
		if (INT_MIN != -INT_MAX - 1) {
			throw new IllegalStateException();
		}
	}

	public static final double HUGE_VAL = Double.POSITIVE_INFINITY;

	public static double fabs(double x) {
		return Math.abs(x);
	}

	public static double hypot(double x, double y) {
		double t;
		x = Math.abs(x);
		y = Math.abs(y);
		t = Math.min(x, y);
		x = Math.max(x, y);
		t = t / x;
		return x * Math.sqrt(1 + t * t);
	}

	// #define SQR(a) ((a) * (a))
	public static double SQR(double a) {
		return a * a;
	}

	// #define MILLIPOINT .001
	// #define MICROPOINT .000001
	public static double MILLIPOINT = .001;
	public static double MICROPOINT = .000001;

	// #define APPROXEQPT(p,q,tol) (DIST2((p),(q)) < SQR(tol))
	public static boolean APPROXEQPT(__ptr__ p, __ptr__ q, double tol) {
		return (DIST2((p), (q)) < SQR(tol));
	}

	// #define LEN2(a,b) (SQR(a) + SQR(b))
	public static double LEN2(double a, double b) {
		return (SQR(a) + SQR(b));
	}

	// #define LEN(a,b) (sqrt(LEN2((a),(b))))
	public static double LEN(double a, double b) {
		return (Math.sqrt(LEN2((a), (b))));
	}

	//
	// #define DIST2(p,q) (LEN2(((p).x - (q).x),((p).y - (q).y)))
	public static double DIST2(__ptr__ p, __ptr__ q) {
		return (LEN2(((p).getDouble("x") - (q).getDouble("x")), ((p).getDouble("y") - (q).getDouble("y"))));
	}

	public static double DIST2(__struct__ p, __ptr__ q) {
		return (LEN2(((p).getDouble("x") - (q).getDouble("x")), ((p).getDouble("y") - (q).getDouble("y"))));
	}

	// #define DIST(p,q) (sqrt(DIST2((p),(q))))

	// #define INSIDE(p,b) (BETWEEN((b).LL.x,(p).x,(b).UR.x) && BETWEEN((b).LL.y,(p).y,(b).UR.y))
	public static boolean INSIDE(__struct__ b, __struct__ p) {
		return (BETWEEN(b.getStruct("LL").getDouble("x"), p.getDouble("x"), b.getStruct("UR").getDouble("x")) && BETWEEN(
				b.getStruct("LL").getDouble("y"), p.getDouble("y"), b.getStruct("UR").getDouble("y")));
	}

	public static final double M_PI = Math.PI;
	// #define SQRT2 1.41421356237309504880
	public static final double SQRT2 = Math.sqrt(2);

	// #define RADIANS(deg) ((deg)/180.0 * M_PI)
	public static double RADIANS(double deg) {
		return ((deg) / 180.0 * M_PI);
	}

	// #define DISTSQ(a, b) ( \
	// (((a).x - (b).x) * ((a).x - (b).x)) + (((a).y - (b).y) * ((a).y - (b).y)) \
	// )

	public static double DISTSQ(__struct__ a, __struct__ b) {
		return (((a).getDouble("x") - (b).getDouble("x")) * ((a).getDouble("x") - (b).getDouble("x")))
				+ (((a).getDouble("y") - (b).getDouble("y")) * ((a).getDouble("y") - (b).getDouble("y")));
	}

	public static void hackInitDimensionFromLabel(__struct__<pointf> size, String label) {
		if (label.matches("_dim_\\d+_\\d+_")) {
			Pattern p = Pattern.compile("_dim_(\\d+)_(\\d+)_");
			Matcher m = p.matcher(label);
			if (m.matches() == false) {
				throw new IllegalStateException();
			}
			int ww = Integer.parseInt(m.group(1));
			int hh = Integer.parseInt(m.group(2));
			size.setDouble("x", ww);
			size.setDouble("y", hh);
			System.err.println("Hacking dimension to width=" + ww + " height=" + hh);
		}
	}
	
	public static CString createHackInitDimensionFromLabel(int width, int height) {
		return new CString("_dim_" + width + "_" + height + "_");
	}
}
