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
 * (C) Copyright 2009-2024, Arnaud Roques
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

import static gen.lib.cdt.dtrestore__c.dtrestore;
import static gen.lib.cgraph.attr__c.agattr;
import static gen.lib.cgraph.edge__c.agedge;
import static smetana.core.JUtils.strcmp;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import h.SHAPE_INFO;
import h.ST_Agedge_s;
import h.ST_Agedgeinfo_t;
import h.ST_Agiddisc_s;
import h.ST_Agiodisc_s;
import h.ST_Agmemdisc_s;
import h.ST_Agnode_s;
import h.ST_Agnodeinfo_t;
import h.ST_Agobj_s;
import h.ST_Agraph_s;
import h.ST_Agraphinfo_t;
import h.ST_Agrec_s;
import h.ST_Agsym_s;
import h.ST_GVC_s;
import h.ST_boxf;
import h.ST_dt_s;
import h.ST_dtdisc_s;
import h.ST_dtlink_s;
import h.ST_elist;
import h.ST_layout_t;
import h.ST_nlist_t;
import h.ST_point;
import h.ST_pointf;
import h.ST_port;
import h.ST_rank_t;
import h.ST_shape_desc;
import h.ST_splines;
import h.ST_subtree_t;
import h.ST_textlabel_t;
import smetana.core.debug.SmetanaDebug;

final public class Macro {
	// ::remove folder when __HAXE__

	public static void UNSURE_ABOUT(String comment) {
		System.err.println("UNSURE_ABOUT: " + comment);
	}

	public static __ptr__ UNSUPPORTED(String comment) {
		SmetanaDebug.LIST_METHODS();
		throw new UnsupportedOperationException(comment);
	}

	public static int UNSUPPORTED_INT(String comment) {
		throw new UnsupportedOperationException(comment);
	}

	// Graphviz

	public final static int AGRAPH = 0;
	public final static int AGNODE = 1;
	public final static int AGOUTEDGE = 2;
	public final static int AGINEDGE = 3;
	public final static int AGEDGE = AGOUTEDGE;

	public static void AGTYPE(ST_Agobj_s obj, int v) {
		obj.tag.objtype = v;
	}

	public static void AGID(ST_Agobj_s obj, int v) {
		obj.tag.id = v;
	}

	public static void AGSEQ(ST_Agobj_s obj, int v) {
		obj.tag.seq = v;
	}

	public static void AGDATA(ST_Agobj_s obj, ST_Agrec_s v) {
		obj.data = v;
	}

	// #define AGIN2OUT(e) ((e)-1)
	public static ST_Agedge_s AGIN2OUT(ST_Agedge_s e) {
		return e.plus_(-1);
	}

	// #define AGOUT2IN(e) ((e)+1)
	public static ST_Agedge_s AGOUT2IN(ST_Agedge_s e) {
		return e.plus_(1);
	}

	// #define AGOPP(e) ((AGTYPE(e)==AGINEDGE)?AGIN2OUT(e):AGOUT2IN(e))
	public static ST_Agedge_s AGOPP(ST_Agedge_s e) {
		return e.tag.objtype == AGINEDGE ? AGIN2OUT(e) : AGOUT2IN(e);
	}

	// #define AGMKOUT(e) (AGTYPE(e) == AGOUTEDGE? (e): AGIN2OUT(e))
	public static ST_Agedge_s AGMKOUT(ST_Agedge_s e) {
		return e.tag.objtype == AGOUTEDGE ? e : AGIN2OUT(e);
	}

	// #define AGMKIN(e) (AGTYPE(e) == AGINEDGE? (e): AGOUT2IN(e))
	public static ST_Agedge_s AGMKIN(ST_Agedge_s e) {
		return e.tag.objtype == AGINEDGE ? e : AGOUT2IN(e);
	}

	// #define AGTAIL(e) (AGMKIN(e)->node)
	public static ST_Agnode_s AGTAIL(ST_Agedge_s e) {
		return AGMKIN(e).node;
	}

	public static ST_Agnode_s M_agtail(ST_Agedge_s e) {
		return AGMKIN(e).node;
	}

	public static void M_agtail(ST_Agedge_s e, ST_Agnode_s v) {
		AGMKIN(e).node = v;
	}

	// #define AGHEAD(e) (AGMKOUT(e)->node)
	public static ST_Agnode_s AGHEAD(ST_Agedge_s e) {
		return AGMKOUT(e).node;
	}

	private static ST_Agnode_s M_aghead(ST_Agedge_s e) {
		return AGMKOUT(e).node;
	}

	public static void M_aghead(ST_Agedge_s e, ST_Agnode_s v) {
		AGMKOUT(e).node = v;
	}

	public static int LENGTH(ST_Agedge_s e) {
		return ND_rank(M_aghead(e)) - ND_rank(M_agtail(e));
	}

	public static int SLACK(ST_Agedge_s e) {
		return LENGTH(e) - ED_minlen(e);
	}

	public static boolean SEQ(int a, int b, int c) {
		return ((a) <= (b)) && ((b) <= (c));
	}

	public static boolean TREE_EDGE(ST_Agedge_s e) {
		return ED_tree_index(e) >= 0;
	}

	// #define GD_parent(g) (((Agraphinfo_t*)AGDATA(g))->parent)
	public static __ptr__ GD_parent(ST_Agraph_s g) {
		return ((ST_Agraphinfo_t) g.data).parent;
	}

	public static void GD_parent(ST_Agraph_s g, ST_Agraph_s v) {
		((ST_Agraphinfo_t) g.data).parent = v;
	}

	// #define GD_level(g) (((Agraphinfo_t*)AGDATA(g))->level)
	// #define GD_drawing(g) (((Agraphinfo_t*)AGDATA(g))->drawing)
	public static ST_layout_t GD_drawing(ST_Agraph_s g) {
		return ((ST_Agraphinfo_t) g.data).drawing;
	}

	public static void GD_drawing(ST_Agraph_s g, ST_layout_t v) {
		((ST_Agraphinfo_t) g.data).drawing = v;
	}

	// #define GD_bb(g) (((Agraphinfo_t*)AGDATA(g))->bb)
	public static ST_boxf GD_bb(ST_Agraph_s g) {
		return ((ST_Agraphinfo_t) g.data).bb;
	}

	// #define GD_gvc(g) (((Agraphinfo_t*)AGDATA(g))->gvc)
	public static ST_GVC_s GD_gvc(ST_Agraph_s g) {
		return ((ST_Agraphinfo_t) g.data).gvc;
	}

	public static void GD_gvc(ST_Agraph_s g, ST_GVC_s v) {
		((ST_Agraphinfo_t) g.data).gvc = v;
	}

	// #define GD_cleanup(g) (((Agraphinfo_t*)AGDATA(g))->cleanup)
	public static __ptr__ GD_cleanup(ST_Agraph_s g) {
		return ((ST_Agraphinfo_t) g.data).cleanup;
	}

	public static void GD_cleanup(ST_Agraph_s g, CFunction data) {
		((ST_Agraphinfo_t) g.data).cleanup = data;
	}

	// #define GD_dist(g) (((Agraphinfo_t*)AGDATA(g))->dist)
	// #define GD_alg(g) (((Agraphinfo_t*)AGDATA(g))->alg)
	// #define GD_border(g) (((Agraphinfo_t*)AGDATA(g))->border)
	public static ST_pointf[] GD_border(ST_Agraph_s g) {
		return ((ST_Agraphinfo_t) g.data).border;
	}

	// #define GD_cl_cnt(g) (((Agraphinfo_t*)AGDATA(g))->cl_nt)
	// #define GD_clust(g) (((Agraphinfo_t*)AGDATA(g))->clust)
	public static CArrayOfStar<ST_Agraph_s> GD_clust(ST_Agraph_s g) {
		return ((ST_Agraphinfo_t) g.data).clust;
	}

	public static void GD_clust(ST_Agraph_s g, CArrayOfStar<ST_Agraph_s> v) {
		((ST_Agraphinfo_t) g.data).clust = v;
	}

	// #define GD_dotroot(g) (((Agraphinfo_t*)AGDATA(g))->dotroot)
	public static ST_Agraph_s GD_dotroot(ST_Agraph_s g) {
		return ((ST_Agraphinfo_t) g.data).dotroot;
	}

	public static void GD_dotroot(ST_Agraph_s g, ST_Agraph_s v) {
		((ST_Agraphinfo_t) g.data).dotroot = v;
	}

	// #define GD_comp(g) (((Agraphinfo_t*)AGDATA(g))->comp)
	public static ST_nlist_t GD_comp(ST_Agraph_s g) {
		return ((ST_Agraphinfo_t) g.data).comp;
	}

	// #define GD_exact_ranksep(g) (((Agraphinfo_t*)AGDATA(g))->exact_ranksep)
	public static int GD_exact_ranksep(ST_Agraph_s g) {
		return ((ST_Agraphinfo_t) g.data).exact_ranksep;
	}

	public static void GD_exact_ranksep(ST_Agraph_s g, int v) {
		((ST_Agraphinfo_t) g.data).exact_ranksep = v;
	}

	// #define GD_expanded(g) (((Agraphinfo_t*)AGDATA(g))->expanded)
	public static boolean GD_expanded(ST_Agraph_s g) {
		return ((ST_Agraphinfo_t) g.data).expanded;
	}

	public static void GD_expanded(ST_Agraph_s g, boolean v) {
		((ST_Agraphinfo_t) g.data).expanded = v;
	}

	// #define GD_flags(g) (((Agraphinfo_t*)AGDATA(g))->flags)
	public static int GD_flags(ST_Agraph_s g) {
		return ((ST_Agraphinfo_t) g.data).flags;
	}

	public static void GD_flags(ST_Agraph_s g, int v) {
		((ST_Agraphinfo_t) g.data).flags = v;
	}

	// #define GD_gui_state(g) (((Agraphinfo_t*)AGDATA(g))->gui_state)
	// #define GD_charset(g) (((Agraphinfo_t*)AGDATA(g))->charset)
	public static int GD_charset(ST_Agraph_s g) {
		return ((ST_Agraphinfo_t) g.data).charset;
	}

	public static void GD_charset(ST_Agraph_s g, int v) {
		((ST_Agraphinfo_t) g.data).charset = v;
	}

	// #define GD_has_labels(g) (((Agraphinfo_t*)AGDATA(g))->has_labels)
	public static int GD_has_labels(ST_Agraph_s g) {
		return ((ST_Agraphinfo_t) g.data).has_labels;
	}

	public static void GD_has_labels(ST_Agraph_s g, int v) {
		((ST_Agraphinfo_t) g.data).has_labels = v;
	}

	// #define GD_has_images(g) (((Agraphinfo_t*)AGDATA(g))->has_images)
	// #define GD_has_flat_edges(g) (((Agraphinfo_t*)AGDATA(g))->has_flat_edges)
	public static int GD_has_flat_edges(ST_Agraph_s g) {
		return ((ST_Agraphinfo_t) g.data).has_flat_edges;
	}

	public static void GD_has_flat_edges(ST_Agraph_s g, boolean v) {
		((ST_Agraphinfo_t) g.data).has_flat_edges = v ? 1 : 0;
	}

	// #define GD_has_sourcerank(g) (((Agraphinfo_t*)AGDATA(g))->has_sourcerank)
	// #define GD_has_sinkrank(g) (((Agraphinfo_t*)AGDATA(g))->has_sinkrank)
	// #define GD_ht1(g) (((Agraphinfo_t*)AGDATA(g))->ht1)
	public static double GD_ht1(ST_Agraph_s g) {
		return ((ST_Agraphinfo_t) g.data).ht1;
	}

	public static void GD_ht1(ST_Agraph_s g, double v) {
		((ST_Agraphinfo_t) g.data).ht1 = v;
	}

	// #define GD_ht2(g) (((Agraphinfo_t*)AGDATA(g))->ht2)
	public static double GD_ht2(ST_Agraph_s g) {
		return ((ST_Agraphinfo_t) g.data).ht2;
	}

	public static void GD_ht2(ST_Agraph_s g, double v) {
		((ST_Agraphinfo_t) g.data).ht2 = v;
	}

	// #define GD_inleaf(g) (((Agraphinfo_t*)AGDATA(g))->inleaf)
	// #define GD_installed(g) (((Agraphinfo_t*)AGDATA(g))->installed)
	public static int GD_installed(ST_Agraph_s g) {
		return ((ST_Agraphinfo_t) g.data).installed;
	}

	public static void GD_installed(ST_Agraph_s g, int v) {
		((ST_Agraphinfo_t) g.data).installed = v;
	}

	// #define GD_label(g) (((Agraphinfo_t*)AGDATA(g))->label)
	public static ST_textlabel_t GD_label(ST_Agraph_s g) {
		return ((ST_Agraphinfo_t) g.data).label;
	}

	public static void GD_label(ST_Agraph_s g, ST_textlabel_t v) {
		((ST_Agraphinfo_t) g.data).label = v;
	}

	// #define GD_leader(g) (((Agraphinfo_t*)AGDATA(g))->leader)
	public static ST_Agnode_s GD_leader(ST_Agraph_s g) {
		return ((ST_Agraphinfo_t) g.data).leader;
	}

	public static void GD_leader(ST_Agraph_s g, ST_Agnode_s v) {
		((ST_Agraphinfo_t) g.data).leader = v;
	}

	// #define GD_rankdir2(g) (((Agraphinfo_t*)AGDATA(g))->rankdir)
	public static int GD_rankdir2(ST_Agraph_s g) {
		return ((ST_Agraphinfo_t) g.data).rankdir;
	}

	public static void GD_rankdir2(ST_Agraph_s g, int v) {
		((ST_Agraphinfo_t) g.data).rankdir = v;
	}

	// #define GD_rankdir(g) (((Agraphinfo_t*)AGDATA(g))->rankdir & 0x3)
	public static int GD_rankdir(ST_Agraph_s g) {
		return ((ST_Agraphinfo_t) g.data).rankdir & 0x3;
	}

	// #define GD_flip(g) (GD_rankdir(g) & 1)
	public static boolean GD_flip(ST_Agraph_s g) {
		return (GD_rankdir(g) & 1) != 0;
	}

	// #define GD_realrankdir(g) ((((Agraphinfo_t*)AGDATA(g))->rankdir) >> 2)
	public static int GD_realrankdir(ST_Agraph_s g) {
		return ((ST_Agraphinfo_t) g.data).rankdir >> 2;
	}

	// #define GD_realflip(g) (GD_realrankdir(g) & 1)
	public static int GD_realflip(ST_Agraph_s g) {
		return GD_realrankdir(g) & 1;
	}

	// #define GD_ln(g) (((Agraphinfo_t*)AGDATA(g))->ln)
	public static ST_Agnode_s GD_ln(ST_Agraph_s g) {
		return ((ST_Agraphinfo_t) g.data).ln;
	}

	public static void GD_ln(ST_Agraph_s g, ST_Agnode_s v) {
		((ST_Agraphinfo_t) g.data).ln = v;
	}

	// #define GD_maxrank(g) (((Agraphinfo_t*)AGDATA(g))->maxrank)
	public static int GD_maxrank(ST_Agraph_s g) {
		return ((ST_Agraphinfo_t) g.data).maxrank;
	}

	public static void GD_maxrank(ST_Agraph_s g, int v) {
		((ST_Agraphinfo_t) g.data).maxrank = v;
	}

	// #define GD_maxset(g) (((Agraphinfo_t*)AGDATA(g))->maxset)
	public static __ptr__ GD_maxset(ST_Agraph_s g) {
		return ((ST_Agraphinfo_t) g.data).maxset;
	}

	// #define GD_minrank(g) (((Agraphinfo_t*)AGDATA(g))->minrank)
	public static int GD_minrank(ST_Agraph_s g) {
		return ((ST_Agraphinfo_t) g.data).minrank;
	}

	public static void GD_minrank(ST_Agraph_s g, int v) {
		((ST_Agraphinfo_t) g.data).minrank = v;
	}

	// #define GD_minset(g) (((Agraphinfo_t*)AGDATA(g))->minset)
	public static __ptr__ GD_minset(ST_Agraph_s g) {
		return ((ST_Agraphinfo_t) g.data).minset;
	}

	// #define GD_minrep(g) (((Agraphinfo_t*)AGDATA(g))->minrep)
	// #define GD_maxrep(g) (((Agraphinfo_t*)AGDATA(g))->maxrep)
	// #define GD_move(g) (((Agraphinfo_t*)AGDATA(g))->move)
	// #define GD_n_cluster(g) (((Agraphinfo_t*)AGDATA(g))->n_cluster)
	public static int GD_n_cluster(ST_Agraph_s g) {
		return ((ST_Agraphinfo_t) g.data).n_cluster;
	}

	public static void GD_n_cluster(ST_Agraph_s g, int v) {
		((ST_Agraphinfo_t) g.data).n_cluster = v;
	}

	// #define GD_n_nodes(g) (((Agraphinfo_t*)AGDATA(g))->n_nodes)
	public static int GD_n_nodes(ST_Agraph_s g) {
		return ((ST_Agraphinfo_t) g.data).n_nodes;
	}

	public static void GD_n_nodes(ST_Agraph_s g, int v) {
		((ST_Agraphinfo_t) g.data).n_nodes = v;
	}

	// #define GD_ndim(g) (((Agraphinfo_t*)AGDATA(g))->ndim)
	// #define GD_odim(g) (((Agraphinfo_t*)AGDATA(g))->odim)
	// #define GD_neato_nlist(g) (((Agraphinfo_t*)AGDATA(g))->neato_nlist)
	// #define GD_nlist(g) (((Agraphinfo_t*)AGDATA(g))->nlist)
	public static ST_Agnode_s GD_nlist(ST_Agraph_s g) {
		return ((ST_Agraphinfo_t) g.data).nlist;
	}

	public static void GD_nlist(ST_Agraph_s g, ST_Agnode_s v) {
		((ST_Agraphinfo_t) g.data).nlist = v;
	}

	// #define GD_nodesep(g) (((Agraphinfo_t*)AGDATA(g))->nodesep)
	public static int GD_nodesep(ST_Agraph_s g) {
		return ((ST_Agraphinfo_t) g.data).nodesep;
	}

	public static void GD_nodesep(ST_Agraph_s g, int v) {
		((ST_Agraphinfo_t) g.data).nodesep = v;
	}

	// #define GD_outleaf(g) (((Agraphinfo_t*)AGDATA(g))->outleaf)
	// #define GD_rank(g) (((Agraphinfo_t*)AGDATA(g))->rank)
	public static CArray<ST_rank_t> GD_rank(ST_Agraph_s g) {
//		JUtilsDebug.LOG("## get GD_rank for " + g);
//		JUtilsDebug.LOG("" + ((ST_Agraphinfo_t) AGDATA(g)).rank);
//		JUtilsDebug.LOG("");
		return ((ST_Agraphinfo_t) g.data).rank;
	}

	public static void GD_rank(ST_Agraph_s g, CArray<ST_rank_t> v) {
//		JUtilsDebug.LOG("!! set GD_rank for " + g);
//		JUtilsDebug.LOG("" + v);
//		JUtilsDebug.LOG("");
		((ST_Agraphinfo_t) g.data).rank = v;
	}

	// #define GD_rankleader(g) (((Agraphinfo_t*)AGDATA(g))->rankleader)
	public static CArrayOfStar<ST_Agnode_s> GD_rankleader(ST_Agraph_s g) {
		return ((ST_Agraphinfo_t) g.data).rankleader;
	}

	public static void GD_rankleader(ST_Agraph_s g, CArrayOfStar<ST_Agnode_s> v) {
		((ST_Agraphinfo_t) g.data).rankleader = v;
	}

	// #define GD_ranksep(g) (((Agraphinfo_t*)AGDATA(g))->ranksep)
	public static int GD_ranksep(ST_Agraph_s g) {
		return ((ST_Agraphinfo_t) g.data).ranksep;
	}

	public static void GD_ranksep(ST_Agraph_s g, int v) {
		((ST_Agraphinfo_t) g.data).ranksep = v;
	}

	// #define GD_rn(g) (((Agraphinfo_t*)AGDATA(g))->rn)
	public static ST_Agnode_s GD_rn(ST_Agraph_s g) {
		return ((ST_Agraphinfo_t) g.data).rn;
	}

	public static void GD_rn(ST_Agraph_s g, ST_Agnode_s v) {
		((ST_Agraphinfo_t) g.data).rn = v;
	}

	// #define GD_set_type(g) (((Agraphinfo_t*)AGDATA(g))->set_type)
	public static int GD_set_type(ST_Agraph_s g) {
		return ((ST_Agraphinfo_t) g.data).set_type;
	}

	public static void GD_set_type(ST_Agraph_s g, int v) {
		((ST_Agraphinfo_t) g.data).set_type = v;
	}

	// #define GD_label_pos(g) (((Agraphinfo_t*)AGDATA(g))->label_pos)
	public static int GD_label_pos(ST_Agraph_s g) {
		return ((ST_Agraphinfo_t) g.data).label_pos;
	}

	public static void GD_label_pos(ST_Agraph_s g, int v) {
		((ST_Agraphinfo_t) g.data).label_pos = v;
	}

	// #define GD_showboxes(g) (((Agraphinfo_t*)AGDATA(g))->showboxes)
	public static int GD_showboxes(ST_Agraph_s g) {
		return ((ST_Agraphinfo_t) g.data).showboxes;
	}

	public static void GD_showboxes(ST_Agraph_s g, int v) {
		((ST_Agraphinfo_t) g.data).showboxes = v;
	}

	// #define GD_fontnames(g) (((Agraphinfo_t*)AGDATA(g))->fontnames)
	public static int GD_fontnames(ST_Agraph_s g) {
		return ((ST_Agraphinfo_t) g.data).fontnames;
	}

	public static void GD_fontnames(ST_Agraph_s g, int v) {
		((ST_Agraphinfo_t) g.data).fontnames = v;
	}

	// #define GD_spring(g) (((Agraphinfo_t*)AGDATA(g))->spring)
	// #define GD_sum_t(g) (((Agraphinfo_t*)AGDATA(g))->sum_t)
	// #define GD_t(g) (((Agraphinfo_t*)AGDATA(g))->t)

	// #define ND_id(n) (((Agnodeinfo_t*)AGDATA(n))->id)
	public static int ND_id(ST_Agnode_s n) {
		return ((ST_Agnodeinfo_t) n.data).id;
	}

	// #define ND_alg(n) (((Agnodeinfo_t*)AGDATA(n))->alg)
	public static __ptr__ ND_alg(ST_Agnode_s n) {
		return ((ST_Agnodeinfo_t) n.data).alg;
	}

	public static void ND_alg(ST_Agnode_s n, ST_Agedge_s value) {
		((ST_Agnodeinfo_t) n.data).alg = value;
	}

	// #define ND_UF_parent(n) (((Agnodeinfo_t*)AGDATA(n))->UF_parent)
	public static ST_Agnode_s ND_UF_parent(ST_Agobj_s n) {
		return ((ST_Agnodeinfo_t) n.data).UF_parent;
	}

	public static void ND_UF_parent(ST_Agobj_s n, ST_Agnode_s v) {
		((ST_Agnodeinfo_t) n.data).UF_parent = v;
	}

	// #define ND_set(n) (((Agnodeinfo_t*)AGDATA(n))->set)
	// #define ND_UF_size(n) (((Agnodeinfo_t*)AGDATA(n))->UF_size)
	public static int ND_UF_size(ST_Agnode_s n) {
		return ((ST_Agnodeinfo_t) n.data).UF_size;
	}

	public static void ND_UF_size(ST_Agnode_s n, int v) {
		((ST_Agnodeinfo_t) n.data).UF_size = v;
	}

	// #define ND_bb(n) (((Agnodeinfo_t*)AGDATA(n))->bb)
	// #define ND_clust(n) (((Agnodeinfo_t*)AGDATA(n))->clust)
	public static ST_Agraph_s ND_clust(ST_Agnode_s n) {
		return ((ST_Agnodeinfo_t) n.data).clust;
	}

	public static void ND_clust(ST_Agnode_s n, ST_Agraph_s v) {
		((ST_Agnodeinfo_t) n.data).clust = v;
	}

	// #define ND_coord(n) (((Agnodeinfo_t*)AGDATA(n))->coord)
	public static ST_pointf ND_coord(ST_Agnode_s n) {
		return ((ST_Agnodeinfo_t) n.data).coord;
	}

	// #define ND_dist(n) (((Agnodeinfo_t*)AGDATA(n))->dist)

	// #define ND_flat_in(n) (((Agnodeinfo_t*)AGDATA(n))->flat_in)
	public static ST_elist ND_flat_in(ST_Agnode_s n) {
		return ((ST_Agnodeinfo_t) n.data).flat_in;
	}

	// #define ND_flat_out(n) (((Agnodeinfo_t*)AGDATA(n))->flat_out)
	public static ST_elist ND_flat_out(ST_Agnode_s n) {
		return ((ST_Agnodeinfo_t) n.data).flat_out;
	}

	// #define ND_gui_state(n) (((Agnodeinfo_t*)AGDATA(n))->gui_state)
	// #define ND_has_port(n) (((Agnodeinfo_t*)AGDATA(n))->has_port)
	public static boolean ND_has_port(ST_Agnode_s n) {
		return ((ST_Agnodeinfo_t) n.data).has_port;
	}

	public static void ND_has_port(ST_Agnode_s n, boolean v) {
		((ST_Agnodeinfo_t) n.data).has_port = v;
	}

	// #define ND_rep(n) (((Agnodeinfo_t*)AGDATA(n))->rep)
	// #define ND_heapindex(n) (((Agnodeinfo_t*)AGDATA(n))->heapindex)
	// #define ND_height(n) (((Agnodeinfo_t*)AGDATA(n))->height)
	public static double ND_height(ST_Agnode_s n) {
		return ((ST_Agnodeinfo_t) n.data).height;
	}

	public static void ND_height(ST_Agnode_s n, double v) {
		((ST_Agnodeinfo_t) n.data).height = v;
	}

	// #define ND_hops(n) (((Agnodeinfo_t*)AGDATA(n))->hops)
	// #define ND_ht(n) (((Agnodeinfo_t*)AGDATA(n))->ht)
	public static double ND_ht(ST_Agnode_s n) {
		return ((ST_Agnodeinfo_t) n.data).ht;
	}

	public static void ND_ht(ST_Agnode_s n, double v) {
		((ST_Agnodeinfo_t) n.data).ht = v;
	}

	// #define ND_in(n) (((Agnodeinfo_t*)AGDATA(n))->in)
	public static ST_elist ND_in(ST_Agnode_s n) {
		return ((ST_Agnodeinfo_t) n.data).in;
	}

	public static void ND_in(ST_Agnode_s n, __struct__<ST_elist> v) {
		((ST_Agnodeinfo_t) n.data).in.___(v);
	}

	// #define ND_inleaf(n) (((Agnodeinfo_t*)AGDATA(n))->inleaf)
	public static __ptr__ ND_inleaf(ST_Agnode_s n) {
		return ((ST_Agnodeinfo_t) n.data).inleaf;
	}

	// #define ND_label(n) (((Agnodeinfo_t*)AGDATA(n))->label)
	public static ST_textlabel_t ND_label(ST_Agnode_s n) {
		return (ST_textlabel_t) ((ST_Agnodeinfo_t) n.data).label;
	}

	public static void ND_label(ST_Agnode_s n, ST_textlabel_t v) {
		((ST_Agnodeinfo_t) n.data).label = v;
	}

	// #define ND_xlabel(n) (((Agnodeinfo_t*)AGDATA(n))->xlabel)
	public static ST_textlabel_t ND_xlabel(ST_Agnode_s n) {
		return ((ST_Agnodeinfo_t) n.data).xlabel;
	}

	// #define ND_lim(n) (((Agnodeinfo_t*)AGDATA(n))->lim)
	public static int ND_lim(ST_Agnode_s n) {
		return ((ST_Agnodeinfo_t) n.data).lim;
	}

	public static void ND_lim(ST_Agnode_s n, int v) {
		((ST_Agnodeinfo_t) n.data).lim = v;
	}

	// #define ND_low(n) (((Agnodeinfo_t*)AGDATA(n))->low)
	public static int ND_low(ST_Agnode_s n) {
		return ((ST_Agnodeinfo_t) n.data).low;
	}

	public static void ND_low(ST_Agnode_s n, int v) {
		((ST_Agnodeinfo_t) n.data).low = v;
	}

	// #define ND_lw(n) (((Agnodeinfo_t*)AGDATA(n))->lw)
	public static double ND_lw(ST_Agnode_s n) {
		return ((ST_Agnodeinfo_t) n.data).lw;
	}

	public static void ND_lw(ST_Agnode_s n, double v) {
		((ST_Agnodeinfo_t) n.data).lw = v;
	}

	// #define ND_mark(n) (((Agnodeinfo_t*)AGDATA(n))->mark)
	public static int ND_mark(ST_Agnode_s n) {
		return ((ST_Agnodeinfo_t) n.data).mark;
	}

	public static void ND_mark(ST_Agnode_s n, int v) {
		((ST_Agnodeinfo_t) n.data).mark = v;
	}

	public static void ND_mark(ST_Agnode_s n, boolean v) {
		((ST_Agnodeinfo_t) n.data).mark = v ? 1 : 0;
	}

	// #define ND_mval(n) (((Agnodeinfo_t*)AGDATA(n))->mval)
	public static double ND_mval(ST_Agnode_s n) {
		return ((ST_Agnodeinfo_t) n.data).mval;
	}

	public static void ND_mval(ST_Agnode_s n, double v) {
		((ST_Agnodeinfo_t) n.data).mval = v;
	}

	// #define ND_n_cluster(n) (((Agnodeinfo_t*)AGDATA(n))->n_cluster)
	// #define ND_next(n) (((Agnodeinfo_t*)AGDATA(n))->next)
	public static ST_Agnode_s ND_next(ST_Agnode_s n) {
		return ((ST_Agnodeinfo_t) n.data).next;
	}

	public static void ND_next(ST_Agnode_s n, ST_Agnode_s v) {
		((ST_Agnodeinfo_t) n.data).next = v;
	}

	// #define ND_node_type(n) (((Agnodeinfo_t*)AGDATA(n))->node_type)
	public static int ND_node_type(ST_Agnode_s n) {
		return ((ST_Agnodeinfo_t) n.data).node_type;
	}

	public static void ND_node_type(ST_Agnode_s n, int v) {
		((ST_Agnodeinfo_t) n.data).node_type = v;
	}

	// #define ND_onstack(n) (((Agnodeinfo_t*)AGDATA(n))->onstack)
	public static boolean ND_onstack(ST_Agnode_s n) {
		return ((ST_Agnodeinfo_t) n.data).onstack != 0;
	}

	public static void ND_onstack(ST_Agnode_s n, int v) {
		((ST_Agnodeinfo_t) n.data).onstack = v;
	}

	public static void ND_onstack(ST_Agnode_s n, boolean v) {
		((ST_Agnodeinfo_t) n.data).onstack = v ? 1 : 0;
	}

	// #define ND_order(n) (((Agnodeinfo_t*)AGDATA(n))->order)
	public static int ND_order(ST_Agnode_s n) {
		return ((ST_Agnodeinfo_t) n.data).order;
	}

	public static void ND_order(ST_Agnode_s n, int v) {
		((ST_Agnodeinfo_t) n.data).order = v;
	}

	// #define ND_other(n) (((Agnodeinfo_t*)AGDATA(n))->other)
	public static ST_elist ND_other(ST_Agnode_s n) {
		return ((ST_Agnodeinfo_t) n.data).other;
	}

	// #define ND_out(n) (((Agnodeinfo_t*)AGDATA(n))->out)
	public static ST_elist ND_out(ST_Agnode_s n) {
		return ((ST_Agnodeinfo_t) n.data).out;
	}

	public static void ND_out(ST_Agnode_s n, __struct__<ST_elist> v) {
		((ST_Agnodeinfo_t) n.data).out.___(v);
	}

	// #define ND_outleaf(n) (((Agnodeinfo_t*)AGDATA(n))->outleaf)
	public static __ptr__ ND_outleaf(ST_Agnode_s n) {
		return ((ST_Agnodeinfo_t) n.data).outleaf;
	}

	// #define ND_par(n) (((Agnodeinfo_t*)AGDATA(n))->par)
	public static ST_Agedge_s ND_par(ST_Agnode_s n) {
		return ((ST_Agnodeinfo_t) n.data).par;
	}

	public static void ND_par(ST_Agnode_s n, ST_Agedge_s v) {
		((ST_Agnodeinfo_t) n.data).par = v;
	}

	// #define ND_pinned(n) (((Agnodeinfo_t*)AGDATA(n))->pinned)
	// #define ND_pos(n) (((Agnodeinfo_t*)AGDATA(n))->pos)
	// #define ND_prev(n) (((Agnodeinfo_t*)AGDATA(n))->prev)
	public static ST_Agnode_s ND_prev(ST_Agnode_s n) {
		return ((ST_Agnodeinfo_t) n.data).prev;
	}

	public static void ND_prev(ST_Agnode_s n, ST_Agnode_s v) {
		((ST_Agnodeinfo_t) n.data).prev = v;
	}

	// #define ND_priority(n) (((Agnodeinfo_t*)AGDATA(n))->priority)
	public static int ND_priority(ST_Agnode_s n) {
		return ((ST_Agnodeinfo_t) n.data).priority;
	}

	public static void ND_priority(ST_Agnode_s n, int v) {
		((ST_Agnodeinfo_t) n.data).priority = v;
	}

	// #define ND_rank(n) (((Agnodeinfo_t*)AGDATA(n))->rank)
	public static int ND_rank(ST_Agnode_s n) {
		return ((ST_Agnodeinfo_t) n.data).rank;
	}

	public static void ND_rank(ST_Agnode_s n, int v) {
		((ST_Agnodeinfo_t) n.data).rank = v;
	}

	// #define ND_ranktype(n) (((Agnodeinfo_t*)AGDATA(n))->ranktype)
	public static int ND_ranktype(ST_Agnode_s n) {
		return ((ST_Agnodeinfo_t) n.data).ranktype;
	}

	public static void ND_ranktype(ST_Agnode_s n, int v) {
		((ST_Agnodeinfo_t) n.data).ranktype = v;
	}

	// #define ND_rw(n) (((Agnodeinfo_t*)AGDATA(n))->rw)
	public static double ND_rw(ST_Agnode_s n) {
		return ((ST_Agnodeinfo_t) n.data).rw;
	}

	public static void ND_rw(ST_Agnode_s n, double v) {
		((ST_Agnodeinfo_t) n.data).rw = v;
	}

	// #define ND_save_in(n) (((Agnodeinfo_t*)AGDATA(n))->save_in)
	public static ST_elist ND_save_in(ST_Agnode_s n) {
		return ((ST_Agnodeinfo_t) n.data).save_in;
	}

	public static void ND_save_in(ST_Agnode_s n, __struct__<ST_elist> v) {
		((ST_Agnodeinfo_t) n.data).save_in.___(v);
	}

	// #define ND_save_out(n) (((Agnodeinfo_t*)AGDATA(n))->save_out)
	public static ST_elist ND_save_out(ST_Agnode_s n) {
		return ((ST_Agnodeinfo_t) n.data).save_out;
	}

	public static void ND_save_out(ST_Agnode_s n, __struct__<ST_elist> v) {
		((ST_Agnodeinfo_t) n.data).save_out.___(v);
	}

	// #define ND_shape(n) (((Agnodeinfo_t*)AGDATA(n))->shape)
	public static ST_shape_desc ND_shape(ST_Agnode_s n) {
		return ((ST_Agnodeinfo_t) n.data).shape;
	}

	public static void ND_shape(ST_Agnode_s n, ST_shape_desc v) {
		((ST_Agnodeinfo_t) n.data).shape = v;
	}

	// #define ND_shape_info(n) (((Agnodeinfo_t*)AGDATA(n))->shape_info)
	public static SHAPE_INFO ND_shape_info(ST_Agnode_s n) {
		return ((ST_Agnodeinfo_t) n.data).shape_info;
	}

	public static void ND_shape_info(ST_Agnode_s n, SHAPE_INFO v) {
		((ST_Agnodeinfo_t) n.data).shape_info = v;
	}

	// #define ND_showboxes(n) (((Agnodeinfo_t*)AGDATA(n))->showboxes)
	public static int ND_showboxes(ST_Agnode_s n) {
		return ((ST_Agnodeinfo_t) n.data).showboxes;
	}

	public static void ND_showboxes(ST_Agnode_s n, int v) {
		((ST_Agnodeinfo_t) n.data).showboxes = v;
	}

	// #define ND_state(n) (((Agnodeinfo_t*)AGDATA(n))->state)
	// #define ND_clustnode(n) (((Agnodeinfo_t*)AGDATA(n))->clustnode)
	// #define ND_tree_in(n) (((Agnodeinfo_t*)AGDATA(n))->tree_in)
	public static ST_elist ND_tree_in(ST_Agnode_s n) {
		return ((ST_Agnodeinfo_t) n.data).tree_in;
	}

	// #define ND_tree_out(n) (((Agnodeinfo_t*)AGDATA(n))->tree_out)
	public static ST_elist ND_tree_out(ST_Agnode_s n) {
		return ((ST_Agnodeinfo_t) n.data).tree_out;
	}

	// #define ND_weight_class(n) (((Agnodeinfo_t*)AGDATA(n))->weight_class)
	public static int ND_weight_class(ST_Agnode_s n) {
		return ((ST_Agnodeinfo_t) n.data).weight_class;
	}

	public static void ND_weight_class(ST_Agnode_s n, int v) {
		((ST_Agnodeinfo_t) n.data).weight_class = v;
	}

	// #define ND_width(n) (((Agnodeinfo_t*)AGDATA(n))->width)
	public static double ND_width(ST_Agnode_s n) {
		return ((ST_Agnodeinfo_t) n.data).width;
	}

	public static void ND_width(ST_Agnode_s n, double v) {
		((ST_Agnodeinfo_t) n.data).width = v;
	}

	// #define ND_xsize(n) (ND_lw(n)+ND_rw(n))
	// #define ND_ysize(n) (ND_ht(n))

	// #define ED_alg(e) (((Agedgeinfo_t*)AGDATA(e))->alg)
	// #define ED_conc_opp_flag(e) (((Agedgeinfo_t*)AGDATA(e))->conc_opp_flag)
	public static boolean ED_conc_opp_flag(ST_Agedge_s e) {
		return ((ST_Agedgeinfo_t) e.data).conc_opp_flag;
	}

	public static void ED_conc_opp_flag(ST_Agedge_s e, boolean v) {
		((ST_Agedgeinfo_t) e.data).conc_opp_flag = v;
	}

	// #define ED_count(e) (((Agedgeinfo_t*)AGDATA(e))->count)
	public static int ED_count(ST_Agedge_s e) {
		return ((ST_Agedgeinfo_t) e.data).count;
	}

	public static void ED_count(ST_Agedge_s e, int v) {
		((ST_Agedgeinfo_t) e.data).count = v;
	}

	// #define ED_cutvalue(e) (((Agedgeinfo_t*)AGDATA(e))->cutvalue)
	public static int ED_cutvalue(ST_Agedge_s e) {
		return ((ST_Agedgeinfo_t) e.data).cutvalue;
	}

	public static void ED_cutvalue(ST_Agedge_s e, int v) {
		((ST_Agedgeinfo_t) e.data).cutvalue = v;
	}

	// #define ED_edge_type(e) (((Agedgeinfo_t*)AGDATA(e))->edge_type)
	// #define ED_adjacent(e) (((Agedgeinfo_t*)AGDATA(e))->adjacent)
	public static int ED_adjacent(ST_Agedge_s e) {
		return ((ST_Agedgeinfo_t) e.data).adjacent;
	}

	public static void ED_adjacent(ST_Agedge_s e, int v) {
		((ST_Agedgeinfo_t) e.data).adjacent = v;
	}

	// #define ED_factor(e) (((Agedgeinfo_t*)AGDATA(e))->factor)
	// #define ED_gui_state(e) (((Agedgeinfo_t*)AGDATA(e))->gui_state)
	// #define ED_head_label(e) (((Agedgeinfo_t*)AGDATA(e))->head_label)
	public static ST_textlabel_t ED_head_label(ST_Agedge_s e) {
		return ((ST_Agedgeinfo_t) e.data).head_label;
	}

	public static void ED_head_label(ST_Agedge_s e, ST_textlabel_t v) {
		((ST_Agedgeinfo_t) e.data).head_label = v;
	}

	// #define ED_head_port(e) (((Agedgeinfo_t*)AGDATA(e))->head_port)
	public static ST_port ED_head_port(ST_Agedge_s e) {
		return ((ST_Agedgeinfo_t) e.data).head_port;
	}

	public static void ED_head_port(ST_Agedge_s e, ST_port v) {
		((ST_Agedgeinfo_t) e.data).head_port.___(v);
	}

	// #define ED_label(e) (((Agedgeinfo_t*)AGDATA(e))->label)
	public static ST_textlabel_t ED_label(ST_Agedge_s e) {
		return (ST_textlabel_t) ((ST_Agedgeinfo_t) e.data).label;
	}

	public static void ED_label(ST_Agedge_s e, ST_textlabel_t v) {
		((ST_Agedgeinfo_t) e.data).label = v;
	}

	// #define ED_xlabel(e) (((Agedgeinfo_t*)AGDATA(e))->xlabel)
	public static ST_textlabel_t ED_xlabel(ST_Agedge_s e) {
		return (ST_textlabel_t) ((ST_Agedgeinfo_t) e.data).xlabel;
	}

	// #define ED_label_ontop(e) (((Agedgeinfo_t*)AGDATA(e))->label_ontop)
	public static boolean ED_label_ontop(ST_Agedge_s e) {
		return ((ST_Agedgeinfo_t) e.data).label_ontop;
	}

	public static void ED_label_ontop(ST_Agedge_s e, boolean v) {
		((ST_Agedgeinfo_t) e.data).label_ontop = v;
	}

	// #define ED_minlen(e) (((Agedgeinfo_t*)AGDATA(e))->minlen)
	public static int ED_minlen(ST_Agedge_s e) {
		return ((ST_Agedgeinfo_t) e.data).minlen;
	}

	public static void ED_minlen(ST_Agedge_s e, int v) {
		((ST_Agedgeinfo_t) e.data).minlen = v;
	}

	// #define ED_path(e) (((Agedgeinfo_t*)AGDATA(e))->path)
	// #define ED_showboxes(e) (((Agedgeinfo_t*)AGDATA(e))->showboxes)
	public static int ED_showboxes(ST_Agedge_s e) {
		return ((ST_Agedgeinfo_t) e.data).showboxes;
	}

	public static void ED_showboxes(ST_Agedge_s e, int v) {
		((ST_Agedgeinfo_t) e.data).showboxes = v;
	}

	// #define ED_spl(e) (((Agedgeinfo_t*)AGDATA(e))->spl)
	public static ST_splines ED_spl(ST_Agedge_s e) {
		return ((ST_Agedgeinfo_t) e.data).spl;
	}

	public static void ED_spl(ST_Agedge_s e, ST_splines v) {
		((ST_Agedgeinfo_t) e.data).spl = v;
	}

	// #define ED_tail_label(e) (((Agedgeinfo_t*)AGDATA(e))->tail_label)
	public static ST_textlabel_t ED_tail_label(ST_Agedge_s e) {
		return ((ST_Agedgeinfo_t) e.data).tail_label;
	}

	public static void ED_tail_label(ST_Agedge_s e, ST_textlabel_t v) {
		((ST_Agedgeinfo_t) e.data).tail_label = v;
	}

	// #define ED_tail_port(e) (((Agedgeinfo_t*)AGDATA(e))->tail_port)
	public static ST_port ED_tail_port(ST_Agedge_s e) {
		return ((ST_Agedgeinfo_t) e.data).tail_port;
	}

	public static void ED_tail_port(ST_Agedge_s e, ST_port v) {
		((ST_Agedgeinfo_t) e.data).tail_port.___(v);
	}

	// #define ED_to_orig(e) (((Agedgeinfo_t*)AGDATA(e))->to_orig)
	public static ST_Agedge_s ED_to_orig(ST_Agedge_s e) {
		return ((ST_Agedgeinfo_t) e.data).to_orig;
	}

	public static void ED_to_orig(ST_Agedge_s e, ST_Agedge_s v) {
		((ST_Agedgeinfo_t) e.data).to_orig = v;
	}

	// #define ED_to_virt(e) (((Agedgeinfo_t*)AGDATA(e))->to_virt)
	public static ST_Agedge_s ED_to_virt(ST_Agedge_s e) {
		return ((ST_Agedgeinfo_t) e.data).to_virt;
	}

	public static void ED_to_virt(ST_Agedge_s e, ST_Agedge_s v) {
		((ST_Agedgeinfo_t) e.data).to_virt = v;
	}

	// #define ED_tree_index(e) (((Agedgeinfo_t*)AGDATA(e))->tree_index)
	public static int ED_tree_index(ST_Agedge_s e) {
		return ((ST_Agedgeinfo_t) e.data).tree_index;
	}

	public static void ED_tree_index(ST_Agedge_s e, int v) {
		((ST_Agedgeinfo_t) e.data).tree_index = v;
	}

	// #define ED_xpenalty(e) (((Agedgeinfo_t*)AGDATA(e))->xpenalty)
	public static int ED_xpenalty(ST_Agedge_s e) {
		return ((ST_Agedgeinfo_t) e.data).xpenalty;
	}

	public static void ED_xpenalty(ST_Agedge_s e, int v) {
		((ST_Agedgeinfo_t) e.data).xpenalty = v;
	}

	// #define ED_dist(e) (((Agedgeinfo_t*)AGDATA(e))->dist)
	public static double ED_dist(ST_Agedge_s e) {
		return ((ST_Agedgeinfo_t) e.data).dist;
	}

	public static void ED_dist(ST_Agedge_s e, double v) {
		((ST_Agedgeinfo_t) e.data).dist = v;
	}

	// #define ED_weight(e) (((Agedgeinfo_t*)AGDATA(e))->weight)
	public static int ED_weight(ST_Agedge_s e) {
		return ((ST_Agedgeinfo_t) e.data).weight;
	}

	public static void ED_weight(ST_Agedge_s e, int v) {
		((ST_Agedgeinfo_t) e.data).weight = v;
	}

	public static int ED_edge_type(ST_Agedge_s e) {
		return ((ST_Agedgeinfo_t) e.data).edge_type;
	}

	public static void ED_edge_type(ST_Agedge_s e, int v) {
		((ST_Agedgeinfo_t) e.data).edge_type = v;
	}

	public static int[] ALLOC_INT(int size, int[] old) {
		if (old == null)
			return new int[size];

		if (old.length > size)
			return old;

		final int result[] = new int[size];

		for (int i = 0; i < old.length; i++)
			result[i] = old[i];
		return result;
//		return (__ptr__) (ptr != null ? JUtils.size_t_array_of_integer(size).realloc(ptr) : JUtils
//				.size_t_array_of_integer(size).malloc());
	}

	// #define RALLOC(size,ptr,type) ((type*)realloc(ptr,(size)*sizeof(type)))
//	public static __ptr__ RALLOC(int nb, __ptr__ ptr, Class type) {
//		throw new UnsupportedOperationException();
//	}

	// #define elist_append(item,L) do {L.list = ALLOC(L.size + 2,L.list,edge_t*);
	// L.list[L.size++] = item;
	// L.list[L.size] = NULL;} while(0)
	public static void elist_append(ST_Agedge_s item, ST_elist L) {
		L.list = CArrayOfStar.<ST_Agedge_s>REALLOC(L.size + 2, L.list, ZType.ST_Agedge_s);
		L.list.set_(L.size++, item);
		L.list.set_(L.size, null);
	}

	// #define alloc_elist(n,L) do {L.size = 0; L.list = N_NEW(n + 1,edge_t*); }
	// while (0)
	public static void alloc_elist(int n, ST_elist L) {
		L.size = 0;
		L.list = CArrayOfStar.<ST_Agedge_s>ALLOC(n + 1, ZType.ST_Agedge_s);
	}

	// #define free_list(L) do {if (L.list) free(L.list);} while (0)
	public static void free_list(ST_elist L) {
		if (L.list != null)
			Memory.free(L.list);
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

	public static void MAKEFWDEDGE(ST_Agedge_s new_, ST_Agedge_s old) {
		SmetanaDebug.LOG("MAKEFWDEDGE");
		ST_Agedge_s newp;
		ST_Agedgeinfo_t info;
		newp = new_;
		info = (ST_Agedgeinfo_t) newp.base.data;
		info.___((ST_Agedgeinfo_t) old.base.data);
		newp.___(old);
		newp.base.data = info;
		M_agtail(newp, AGHEAD(old));
		M_aghead(newp, AGTAIL(old));
		ED_tail_port(newp, ED_head_port(old));
		ED_head_port(newp, ED_tail_port(old));
		ED_edge_type(newp, VIRTUAL);
		ED_to_orig(newp, old);
	}

//	// #define ZALLOC(size,ptr,type,osize) (ptr?
//	// (type*)zrealloc(ptr,size,sizeof(type),osize):(type*)zmalloc((size)*sizeof(type)))
//
//	public static CStar<ST_textspan_t> ZALLOC_ST_textspan_t(ST_textspan_t.Array old, int size) {
//		return old != null ? old.reallocJ(size) : new ST_textspan_t.Array(size);
//	}

//	public static CStarStar<ST_Agraph_s> ZALLOC_ST_Agraph_s(CStarStar<ST_Agraph_s> old, int size) {
//		if (old == null) {
//			return new CStarStar<ST_Agraph_s>(size);
//		}
//		old.realloc(size);
//		return old;
//		// return old != null ? old.reallocJ(size) : new ST_Agraph_s.Array(size);
//	}

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
	public static boolean APPROXEQPT(ST_pointf p, ST_pointf q, double tol) {
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
	public static double DIST2(ST_pointf p, ST_pointf q) {
		return (LEN2(((p).x - (q).x), ((p).y - (q).y)));
	}

	public static double DIST2(ST_point p, ST_point q) {
		return (LEN2(((p).x - (q).x), ((p).y - (q).y)));
	}

	// #define DIST(p,q) (sqrt(DIST2((p),(q))))

	// #define INSIDE(p,b) (BETWEEN((b).LL.x,(p).x,(b).UR.x) &&
	// BETWEEN((b).LL.y,(p).y,(b).UR.y))
	public static boolean INSIDE(ST_pointf p, ST_boxf b) {
		return (BETWEEN(b.LL.x, p.x, b.UR.x) && BETWEEN(b.LL.y, p.y, b.UR.y));
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

	public static double DISTSQ(ST_pointf a, ST_pointf b) {
		return (((a).x - (b).x) * ((a).x - (b).x)) + (((a).y - (b).y) * ((a).y - (b).y));
	}

	public static void hackInitDimensionFromLabel(ST_pointf size, String label) {
		final Pattern p = Pattern.compile("_dim_([.\\d]+)_([\\d.]+)_");
		final Matcher m = p.matcher(label);
		if (m.matches()) {
			final double ww = Double.parseDouble(m.group(1));
			final double hh = Double.parseDouble(m.group(2));
			size.x = ww;
			size.y = hh;
			JUtils.LOG2("Hacking dimension to width=" + ww + " height=" + hh);
		} else {
			JUtils.LOG2("Strange label " + label);
		}
	}

	public static CString createHackInitDimensionFromLabel(int width, int height) {
		return new CString("_dim_" + width + "_" + height + "_");
	}

	// geom.h

	// #define P2PF(p,pf) ((pf).x = (p).x,(pf).y = (p).y)

	// #define PF2P(pf,p) ((p).x = ROUND((pf).x),(p).y = ROUND((pf).y))
	public static void PF2P(ST_pointf pf, ST_pointf p) {
		p.x = ROUND(pf.x);
		p.y = ROUND(pf.y);
	}

	public static void PF2P(ST_pointf pf, ST_point p) {
		p.x = ROUND(pf.x);
		p.y = ROUND(pf.y);
	}

	// #define B2BF(b,bf) (P2PF((b).LL,(bf).LL),P2PF((b).UR,(bf).UR))
	// #define BF2B(bf,b) (PF2P((bf).LL,(b).LL),PF2P((bf).UR,(b).UR))

	// #define APPROXEQ(a,b,tol) (ABS((a) - (b)) < (tol))
	// #define APPROXEQPT(p,q,tol) (DIST2((p),(q)) < SQR(tol))

	/* some common tolerance values */
	// #define MILLIPOINT .001
	// #define MICROPOINT .000001

	// ADDED AFTER PREPROCESSING EXTRACTION
	public static final int LEFT = (1 << 3);
	public static final int RIGHT = (1 << 1);
	public static final int BOTTOM = (1 << 0);
	public static final int TOP = (1 << 2);

	/* label types */
	public static final int LT_NONE = (0 << 1);
	public static final int LT_HTML = (1 << 1);
	public static final int LT_RECD = (2 << 1);

	/* existence of labels */
	public static final int EDGE_LABEL = (1 << 0);
	public static final int HEAD_LABEL = (1 << 1);
	public static final int TAIL_LABEL = (1 << 2);
	public static final int GRAPH_LABEL = (1 << 3);
	public static final int NODE_XLABEL = (1 << 4);
	public static final int EDGE_XLABEL = (1 << 5);
//

	/* edge types */
	public static final int ET_NONE = (0 << 1);
	public static final int ET_LINE = (1 << 1);
	public static final int ET_CURVED = (2 << 1);
	public static final int ET_PLINE = (3 << 1);
	public static final int ET_ORTHO = (4 << 1);
	public static final int ET_SPLINE = (5 << 1);
	public static final int ET_COMPOUND = (6 << 1);

	/* New ranking is used */
	public static final int NEW_RANK = (1 << 4);

	/* node,edge types */
	public static final int NORMAL = 0; /* an original input node */
	public static final int VIRTUAL = 1; /* virtual nodes in long edge chains */
	public static final int SLACKNODE = 2; /* encode edges in node position phase */
	public static final int REVERSED = 3; /* reverse of an original edge */
	public static final int FLATORDER = 4; /* for ordered edges */
	public static final int CLUSTER_EDGE = 5; /* for ranking clusters */
	public static final int IGNORED = 6; /* concentrated multi-edges */

	/* collapsed node classifications */
	public static final int NOCMD = 0; /* default */
	public static final int SAMERANK = 1; /* place on same rank */
	public static final int MINRANK = 2; /* place on "least" rank */
	public static final int SOURCERANK = 3; /* strict version of MINRANK */
	public static final int MAXRANK = 4; /* place on "greatest" rank */
	public static final int SINKRANK = 5; /* strict version of MAXRANK */
	public static final int LEAFSET = 6; /* set of collapsed leaf nodes */
	public static final int CLUSTER = 7; /* set of clustered nodes */

	/* type of graph label: GD_label_pos */
	public static final int LABEL_AT_BOTTOM = 0;
	public static final int LABEL_AT_TOP = 1;
	public static final int LABEL_AT_LEFT = 2;
	public static final int LABEL_AT_RIGHT = 4;

	/* values specifying rankdir */
	public static final int RANKDIR_TB = 0;
	public static final int RANKDIR_LR = 1;
	public static final int RANKDIR_BT = 2;
	public static final int RANKDIR_RL = 3;

	/* edge types */
	public static final int REGULAREDGE = 1;
	public static final int FLATEDGE = 2;
	public static final int SELFWPEDGE = 4;
	public static final int SELFNPEDGE = 8;
	public static final int SELFEDGE = 8;
	public static final int EDGETYPEMASK = 15; /* the OR of the above */

	public static final int LAYOUT_USES_RANKDIR = (1 << 0);

	public static void dtinsert(Globals zz, ST_dt_s d, Object o) {
		d.searchf.exe(zz, d, o, DT_INSERT);
	}

	public static Object dtsearch(Globals zz, ST_dt_s d, Object o) {
		return d.searchf.exe(zz, d, o, DT_SEARCH);
	}

	public static Object dtfirst(Globals zz, ST_dt_s d) {
		return d.searchf.exe(zz, d, null, DT_FIRST);
	}

	public static Object dtnext(Globals zz, ST_dt_s d, Object o) {
		return d.searchf.exe(zz, d, o, DT_NEXT);
	}

	public static Object dtdelete(Globals zz, ST_dt_s d, Object o) {
		return d.searchf.exe(zz, d, o, DT_DELETE);
	}

//	#define dtlink(d,e)	(((Dtlink_t*)(e))->right)
//	#define dtobj(d,e)	_DTOBJ((e), _DT(d)->disc->link)
//	#define dtfinger(d)	(_DT(d)->data->here ? dtobj((d),_DT(d)->data->here):(Void_t*)(0))
//
//	#define dtfirst(d)	(*(_DT(d)->searchf))((d),(Void_t*)(0),DT_FIRST)
//	#define dtnext(d,o)	(*(_DT(d)->searchf))((d),(Void_t*)(o),DT_NEXT)
//	#define dtleast(d,o)	(*(_DT(d)->searchf))((d),(Void_t*)(o),DT_SEARCH|DT_NEXT)
//	#define dtlast(d)	(*(_DT(d)->searchf))((d),(Void_t*)(0),DT_LAST)
//	#define dtprev(d,o)	(*(_DT(d)->searchf))((d),(Void_t*)(o),DT_PREV)
//	#define dtmost(d,o)	(*(_DT(d)->searchf))((d),(Void_t*)(o),DT_SEARCH|DT_PREV)
//	#define dtsearch(d,o)	(*(_DT(d)->searchf))((d),(Void_t*)(o),DT_SEARCH)
//	#define dtmatch(d,o)	(*(_DT(d)->searchf))((d),(Void_t*)(o),DT_MATCH)
//	#define dtinsert(d,o)	(*(_DT(d)->searchf))((d),(Void_t*)(o),DT_INSERT)
//	#define dtappend(d,o)	(*(_DT(d)->searchf))((d),(Void_t*)(o),DT_INSERT|DT_APPEND)
//	#define dtdelete(d,o)	(*(_DT(d)->searchf))((d),(Void_t*)(o),DT_DELETE)
//	#define dtattach(d,o)	(*(_DT(d)->searchf))((d),(Void_t*)(o),DT_ATTACH)
//	#define dtdetach(d,o)	(*(_DT(d)->searchf))((d),(Void_t*)(o),DT_DETACH)
//	#define dtclear(d)	(*(_DT(d)->searchf))((d),(Void_t*)(0),DT_CLEAR)
//	#define dtfound(d)	(_DT(d)->type & DT_FOUND)

	/* flag set if the last search operation actually found the object */
	public static final int DT_FOUND = 0100000;

	/* supported storage methods */
	public static final int DT_SET = 0000001; /* set with unique elements */
	public static final int DT_BAG = 0000002; /* multiset */
	public static final int DT_OSET = 0000004; /* ordered set (self-adjusting tree) */
	public static final int DT_OBAG = 0000010; /* ordered multiset */
	public static final int DT_LIST = 0000020; /* linked list */
	public static final int DT_STACK = 0000040; /* stack: insert/delete at top */
	public static final int DT_QUEUE = 0000100; /* queue: insert at top, delete at tail */
	public static final int DT_DEQUE = 0000200; /* deque: insert at top, append at tail */
	public static final int DT_METHODS = 0000377; /* all currently supported methods */

	/* asserts to dtdisc() */
	public static final int DT_SAMECMP = 0000001; /* compare methods equivalent */
	public static final int DT_SAMEHASH = 0000002; /* hash methods equivalent */

	/* types of search */
	public static final int DT_INSERT = 0000001; /* insert object if not found */
	public static final int DT_DELETE = 0000002; /* delete object if found */
	public static final int DT_SEARCH = 0000004; /* look for an object */
	public static final int DT_NEXT = 0000010; /* look for next element */
	public static final int DT_PREV = 0000020; /* find previous element */
	public static final int DT_RENEW = 0000040; /* renewing an object */
	public static final int DT_CLEAR = 0000100; /* clearing all objects */
	public static final int DT_FIRST = 0000200; /* get first object */
	public static final int DT_LAST = 0000400; /* get last object */
	public static final int DT_MATCH = 0001000; /* find object matching key */
	public static final int DT_VSEARCH = 0002000; /* search using internal representation */
	public static final int DT_ATTACH = 0004000; /* attach an object to the dictionary */
	public static final int DT_DETACH = 0010000; /* detach an object from the dictionary */
	public static final int DT_APPEND = 0020000; /* used on Dtlist to append an object */

	/* events */
	public static final int DT_OPEN = 1; /* a dictionary is being opened */
	public static final int DT_CLOSE = 2; /* a dictionary is being closed */
	public static final int DT_DISC = 3; /* discipline is about to be changed */
	public static final int DT_METH = 4; /* method is about to be changed */
	public static final int DT_ENDOPEN = 5; /* dtopen() is done */
	public static final int DT_ENDCLOSE = 6; /* dtclose() is done */
	public static final int DT_HASHSIZE = 7; /* setting hash table size */

	/* this must be disjoint from DT_METHODS */
	public static final int DT_FLATTEN = 010000; /* dictionary already flattened */
	public static final int DT_WALK = 020000; /* hash table being walked */

	/* how the Dt_t handle was allocated */
	public static final int DT_MALLOC = 0;
	public static final int DT_MEMORYF = 1;

	/* tree rotation/linking functions */
//	#define rrotate(x,y)	((x)->left  = (y)->right, (y)->right = (x))
//	#define lrotate(x,y)	((x)->right = (y)->left,  (y)->left  = (x))
//	#define rlink(r,x)	((r) = (r)->left   = (x) )
//	#define llink(l,x)	((l) = (l)->right  = (x) )
	public static void rrotate(ST_dtlink_s x, ST_dtlink_s y) {
		x._left = y.right;
		y.right = x;
	}

	public static void lrotate(ST_dtlink_s x, ST_dtlink_s y) {
		x.right = y._left;
		y._left = x;
	}

	public static ST_dtlink_s rlink____warning(ST_dtlink_s r, ST_dtlink_s x) {
		r._left = x;
		// r = x; WARNING THIS IS DIFFERENT FROM C: you must use returned value
		return x;
	}

	public static ST_dtlink_s llink____warning(ST_dtlink_s l, ST_dtlink_s x) {
		l.right = x;
		// l = x; WARNING THIS IS DIFFERENT FROM C: you must use returned value
		return x;
	}

	public static final int GVRENDER_PLUGIN = 300; /* a plugin supported language */
	public static final int NO_SUPPORT = 999; /* no support */

	/* type of cluster rank assignment */
	public static final int LOCAL = 100;
	public static final int GLOBAL = 101;
	public static final int NOCLUST = 102;

//	#define agfindedge(g,t,h) (agedge(g,t,h,NULL,0))
//	#define agfindnode(g,n) (agnode(g,n,0))
//	#define agfindgraphattr(g,a) (agattr(g,AGRAPH,a,NULL))
//	#define agfindnodeattr(g,a) (agattr(g,AGNODE,a,NULL))
//	#define agfindedgeattr(g,a) (agattr(g,AGEDGE,a,NULL))

	public static ST_Agsym_s agfindgraphattr(Globals zz, ST_Agraph_s g, final String a) {
		return agattr(zz, g, AGRAPH, new CString(a), null);
	}

	public static ST_Agsym_s agfindnodeattr(Globals zz, ST_Agraph_s g, final String a) {
		return agattr(zz, g, AGNODE, new CString(a), null);
	}

	public static ST_Agsym_s agfindedgeattr(Globals zz, ST_Agraph_s g, final String a) {
		return agattr(zz, g, AGEDGE, new CString(a), null);
	}

	public static final double DEFAULT_NODESEP = 0.25;
	public static final double MIN_NODESEP = 0.02;
	public static final double DEFAULT_RANKSEP = 0.5;
	public static final double MIN_RANKSEP = 0.02;

	public static final int POINTS_PER_INCH = 72;

	public static int POINTS(double a_inches) {
		return ROUND((a_inches) * POINTS_PER_INCH);
	}

	public static double INCH2PS(double a_inches) {
		return ((a_inches) * (double) POINTS_PER_INCH);
	}

	public static double PS2INCH(double a_points) {
		return ((a_points) / (double) POINTS_PER_INCH);
	}

	/* drawing phases */
	public static final int GVBEGIN = 0;
	public static final int GVSPLINES = 1;

	/* for neato */
	public static final double Spring_coeff = 1.0;
	public static final double MYHUGE = (1.0e+37);
	public static final int MAXDIM = 10;

	public static final String NODENAME_ESC = "\\N";
	public static final String DEFAULT_NODESHAPE = "ellipse";

	public static final double DEFAULT_NODEHEIGHT = 0.5;
	public static final double MIN_NODEHEIGHT = 0.02;
	public static final double DEFAULT_NODEWIDTH = 0.75;
	public static final double MIN_NODEWIDTH = 0.01;

	/* sides (e.g. of cluster margins) */
	public static final int BOTTOM_IX = 0;
	public static final int RIGHT_IX = 1;
	public static final int TOP_IX = 2;
	public static final int LEFT_IX = 3;

	public static final int GAP = 4;

	public static void PAD(ST_pointf d) {
		XPAD(d);
		YPAD(d);
	}

	public static void YPAD(ST_pointf d) {
		d.y += 2 * GAP;
	}

	public static void XPAD(ST_pointf d) {
		d.x += 4 * GAP;
	}

	public static void UNFLATTEN(ST_dt_s dt) {
		if ((dt.data.type & DT_FLATTEN) != 0)
			dtrestore(dt, null);
	}

	public static final int CB_INITIALIZE = 100;
	public static final int CB_UPDATE = 101;
	public static final int CB_DELETION = 102;

	public static final int CL_BACK = 10; /* cost of backward pointing edge */
	public static final int CL_OFFSET = 8; /* margin of cluster box in PS points */
	public static final int CL_CROSS = 1000; /* cost of cluster skeleton edge crossing */

	public static __ptr__ AGCLOS_id(ST_Agraph_s g) {
		return g.clos.state.id;
	}

	public static ST_Agiddisc_s AGDISC_id(ST_Agraph_s g) {
		return g.clos.disc.id;
	}

	public static __ptr__ AGCLOS_mem(ST_Agraph_s g) {
		return g.clos.state.mem;
	}

	public static ST_Agmemdisc_s AGDISC_mem(ST_Agraph_s g) {
		return g.clos.disc.mem;
	}

	public static ST_Agiodisc_s AGDISC_io(ST_Agraph_s g) {
		return g.clos.disc.io;
	}

	public static final double DEFAULT_FONTSIZE = 14.00;
	public static final double DEFAULT_LABEL_FONTSIZE = 11.0; /* for head/taillabel */
	public static final double MIN_FONTSIZE = 1.0;

	/* style flags (0-23) */
	public static final int FILLED = (1 << 0);
	public static final int RADIAL = (1 << 1);
	public static final int ROUNDED = (1 << 2);
	public static final int DIAGONALS = (1 << 3);
	public static final int AUXLABELS = (1 << 4);
	public static final int INVISIBLE = (1 << 5);
	public static final int STRIPED = (1 << 6);
	public static final int DOTTED = (1 << 7);
	public static final int DASHED = (1 << 8);
	public static final int WEDGED = (1 << 9);
	public static final int UNDERLINE = (1 << 10);
	public static final int FIXEDSHAPE = (1 << 11);

	public static final int SHAPE_MASK = (127 << 24);

	public static final CString HEAD_ID = new CString("headport");
	public static final CString TAIL_ID = new CString("tailport");

	public static __ptr__ _DTKEY(__ptr__ o, FieldOffset ky, int sz) {
		// return (__ptr__) (sz < 0 ? ((__ptr__)o).addVirtualBytes(ky) :
		// ((__ptr__)o).addVirtualBytes(ky));
		return (__ptr__) o.getTheField(ky);
	}

	public static int _DTCMP(Globals zz, ST_dt_s dt, __ptr__ k1, __ptr__ k2, final ST_dtdisc_s dc, CFunction cmpf,
			int sz) {
		return cmpf != null ? (Integer) ((CFunction) cmpf).exe(zz, dt, k1, k2, dc)
				: (sz <= 0 ? strcmp((CString) k1, (CString) k2) : UNSUPPORTED_INT("memcmp(ok,nk,sz)"));
	}

	public static final int SEARCHSIZE = 30;

	public static ST_Agedge_s agfindedge(Globals zz, ST_Agraph_s g, ST_Agnode_s t, ST_Agnode_s h) {
		return agedge(zz, g, t, h, null, false);
	}

	public static int flatindex(ST_Agnode_s v) {
		return ND_low(v);
	}

	public static void flatindex(ST_Agnode_s v, int data) {
		ND_low(v, data);
	}

	public static final int NODECARD = 64;
	public static final int SMALLBUF = 128;

//	#define ND_subtree(n) (subtree_t*)ND_par(n)
//	#define ND_subtree_set(n,value) (ND_par(n) = (edge_t*)value)

	public static ST_subtree_t ND_subtree(ST_Agnode_s n) {
		// return (ST_subtree_t) ((ST_Agnodeinfo_t)AGDATA(n)).par;
		throw new UnsupportedOperationException();
	}

	public static void ND_subtree_set(ST_Agnode_s n, ST_subtree_t value) {
		// ((ST_Agnodeinfo_t)AGDATA(n)).par = value;
		throw new UnsupportedOperationException();
	}

	// #define streq(a,b) (*(a)==*(b)&&!strcmp(a,b))
	public static boolean streq(CString a, CString b) {
		return a.charAt(0) == b.charAt(0) && strcmp(a, b) == 0;
	}

	public static boolean streq(CString a, String b) {
		return streq(a, new CString(b));
	}

	public static final int ARR_NONE = 0;
	public static final int ARR_TYPE_NONE = ARR_NONE;
	public static final int ARR_TYPE_NORM = 1;
	public static final int ARR_TYPE_CROW = 2;
	public static final int ARR_TYPE_TEE = 3;
	public static final int ARR_TYPE_BOX = 4;
	public static final int ARR_TYPE_DIAMOND = 5;
	public static final int ARR_TYPE_DOT = 6;
	public static final int ARR_TYPE_CURVE = 7;
	public static final int ARR_TYPE_GAP = 8;

	public static final double ARROW_LENGTH = 10.;

	public static final int NUMB_OF_ARROW_HEADS = 4;
	/* each arrow in 8 bits. Room for NUMB_OF_ARROW_HEADS arrows in 32 bit int. */

	public static final int BITS_PER_ARROW = 8;

	public static final int BITS_PER_ARROW_TYPE = 4;

}
