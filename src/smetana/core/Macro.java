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

import h.ST_Agedge_s;
import h.ST_Agedgeinfo_t;
import h.ST_Agnode_s;
import h.ST_Agnodeinfo_t;
import h.ST_Agobj_s;
import h.ST_Agraph_s;
import h.ST_Agraphinfo_t;
import h.ST_Agrec_s;
import h.ST_Agtag_s;
import h.ST_GVC_s;
import h.ST_Pedge_t;
import h.ST_bezier;
import h.ST_boxf;
import h.ST_elist;
import h.ST_layout_t;
import h.ST_nlist_t;
import h.ST_pointf;
import h.ST_port;
import h.ST_rank_t;
import h.ST_shape_desc;
import h.ST_splines;
import h.ST_textlabel_t;
import h.ST_textspan_t;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

	public static int NOTI(boolean v) {
		return v ? 0 : 1;
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
	public static ST_Agtag_s AGTAG(__ptr__ obj) {
		return (ST_Agtag_s) ((ST_Agobj_s) obj.castTo(ST_Agobj_s.class)).tag;
	}

	// #define AGTYPE(obj) (AGTAG(obj).objtype)
	public static int AGTYPE(__ptr__ obj) {
		return AGTAG(obj).objtype;
	}

	public static void AGTYPE(__ptr__ obj, int v) {
		AGTAG(obj).objtype = v;
	}

	// #define AGID(obj) (AGTAG(obj).id)
	public static int AGID(__ptr__ obj) {
		return AGTAG(obj).id;
	}

	public static void AGID(__ptr__ obj, int v) {
		AGTAG(obj).id = v;
	}

	// #define AGSEQ(obj) (AGTAG(obj).seq)
	public static int AGSEQ(__ptr__ obj) {
		return AGTAG(obj).seq;
	}

	public static void AGSEQ(__ptr__ obj, int v) {
		AGTAG(obj).seq = v;
	}

	// #define AGATTRWF(obj) (AGTAG(obj).attrwf)
	// #define AGDATA(obj) (((Agobj_t*)(obj))->data)
	public static ST_Agrec_s AGDATA(__ptr__ obj) {
		return ((ST_Agobj_s) obj.castTo(ST_Agobj_s.class)).data;
	}

	public static void AGDATA(__ptr__ obj, __ptr__ v) {
		obj.castTo(ST_Agobj_s.class).setPtr("data", v);
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
	public static ST_Agedge_s AGOPP(ST_Agedge_s e) {
		return (ST_Agedge_s) (AGTYPE(e) == AGINEDGE ? AGIN2OUT(e) : AGOUT2IN(e));
	}

	// #define AGMKOUT(e) (AGTYPE(e) == AGOUTEDGE? (e): AGIN2OUT(e))
	public static ST_Agedge_s AGMKOUT(__ptr__ e) {
		return (ST_Agedge_s) (AGTYPE(e) == AGOUTEDGE ? (e) : AGIN2OUT(e));
	}

	// #define AGMKIN(e) (AGTYPE(e) == AGINEDGE? (e): AGOUT2IN(e))
	public static ST_Agedge_s AGMKIN(__ptr__ e) {
		return (ST_Agedge_s) (AGTYPE(e) == AGINEDGE ? (e) : AGOUT2IN(e));
	}

	// #define AGTAIL(e) (AGMKIN(e)->node)
	public static ST_Agnode_s AGTAIL(__ptr__ e) {
		return (ST_Agnode_s) AGMKIN(e).node;
	}

	public static ST_Agnode_s agtail(__ptr__ e) {
		return (ST_Agnode_s) AGMKIN(e).node;
	}

	public static void agtail(ST_Agedge_s e, __ptr__ v) {
		AGMKIN(e).setPtr("node", v);
	}

	// #define AGHEAD(e) (AGMKOUT(e)->node)
	public static ST_Agnode_s AGHEAD(__ptr__ e) {
		return (ST_Agnode_s) AGMKOUT(e).node;
	}

	private static ST_Agnode_s aghead(ST_Agedge_s e) {
		return (ST_Agnode_s) AGMKOUT(e).node;
	}

	public static void aghead(ST_Agedge_s e, __ptr__ v) {
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
	public static int LENGTH(ST_Agedge_s e) {
		return ND_rank(aghead(e)) - ND_rank(agtail(e));
	}

	// #define SLACK(e) (LENGTH(e) - ED_minlen(e))
	public static int SLACK(ST_Agedge_s e) {
		return LENGTH(e) - ED_minlen(e);
	}

	// #define SEQ(a,b,c) (((a) <= (b)) && ((b) <= (c)))
	public static boolean SEQ(int a, int b, int c) {
		return (((a) <= (b)) && ((b) <= (c)));
	}

	// #define TREE_EDGE(e) (ED_tree_index(e) >= 0)
	public static boolean TREE_EDGE(ST_Agedge_s e) {
		return ED_tree_index(e) >= 0;
	}

	// #define GD_parent(g) (((Agraphinfo_t*)AGDATA(g))->parent)
	public static __ptr__ GD_parent(ST_Agraph_s g) {
		return ((ST_Agraphinfo_t)AGDATA(g).castTo(ST_Agraphinfo_t.class)).parent;
	}

	public static void GD_parent(ST_Agraph_s g, __ptr__ v) {
		((ST_Agraphinfo_t)AGDATA(g).castTo(ST_Agraphinfo_t.class)).setPtr("parent", v);
	}

	// #define GD_level(g) (((Agraphinfo_t*)AGDATA(g))->level)
	// #define GD_drawing(g) (((Agraphinfo_t*)AGDATA(g))->drawing)
	public static ST_layout_t GD_drawing(ST_Agraph_s g) {
		return ((ST_Agraphinfo_t)AGDATA(g).castTo(ST_Agraphinfo_t.class)).drawing;
	}

	public static void GD_drawing(ST_Agraph_s g, __ptr__ v) {
		((ST_Agraphinfo_t)AGDATA(g).castTo(ST_Agraphinfo_t.class)).setPtr("drawing", v);
	}

	// #define GD_bb(g) (((Agraphinfo_t*)AGDATA(g))->bb)
	public static ST_boxf GD_bb(__ptr__ g) {
		return (ST_boxf) ((ST_Agraphinfo_t)AGDATA(g).castTo(ST_Agraphinfo_t.class)).bb;
	}

	// #define GD_gvc(g) (((Agraphinfo_t*)AGDATA(g))->gvc)
	public static ST_GVC_s GD_gvc(ST_Agraph_s g) {
		return (ST_GVC_s) ((ST_Agraphinfo_t)AGDATA(g).castTo(ST_Agraphinfo_t.class)).gvc;
	}

	public static void GD_gvc(ST_Agraph_s g, ST_GVC_s v) {
		((ST_Agraphinfo_t)AGDATA(g).castTo(ST_Agraphinfo_t.class)).setPtr("gvc", v);
	}

	// #define GD_cleanup(g) (((Agraphinfo_t*)AGDATA(g))->cleanup)
	public static __ptr__ GD_cleanup(ST_Agraph_s g) {
		return ((ST_Agraphinfo_t)AGDATA(g).castTo(ST_Agraphinfo_t.class)).cleanup;
	}

	// #define GD_dist(g) (((Agraphinfo_t*)AGDATA(g))->dist)
	// #define GD_alg(g) (((Agraphinfo_t*)AGDATA(g))->alg)
	// #define GD_border(g) (((Agraphinfo_t*)AGDATA(g))->border)
	public static ST_pointf[] GD_border(ST_Agraph_s g) {
		return ((ST_Agraphinfo_t) AGDATA(g).castTo(ST_Agraphinfo_t.class)).border;
	}

	// #define GD_cl_cnt(g) (((Agraphinfo_t*)AGDATA(g))->cl_nt)
	// #define GD_clust(g) (((Agraphinfo_t*)AGDATA(g))->clust)
	public static ST_Agraph_s.Array GD_clust(ST_Agraph_s g) {
		return ((ST_Agraphinfo_t)AGDATA(g).castTo(ST_Agraphinfo_t.class)).clust;
	}

	public static void GD_clust(ST_Agraph_s g, __ptr__ v) {
		((ST_Agraphinfo_t)AGDATA(g).castTo(ST_Agraphinfo_t.class)).setPtr("clust", v);
	}

	// #define GD_dotroot(g) (((Agraphinfo_t*)AGDATA(g))->dotroot)
	public static ST_Agraph_s GD_dotroot(ST_Agraph_s g) {
		return (ST_Agraph_s) ((ST_Agraphinfo_t)AGDATA(g).castTo(ST_Agraphinfo_t.class)).dotroot;
	}

	public static void GD_dotroot(ST_Agraph_s g, __ptr__ v) {
		((ST_Agraphinfo_t)AGDATA(g).castTo(ST_Agraphinfo_t.class)).setPtr("dotroot", v);
	}

	// #define GD_comp(g) (((Agraphinfo_t*)AGDATA(g))->comp)
	public static ST_nlist_t GD_comp(ST_Agraph_s g) {
		return (ST_nlist_t) ((ST_Agraphinfo_t)AGDATA(g).castTo(ST_Agraphinfo_t.class)).comp;
	}

	// #define GD_exact_ranksep(g) (((Agraphinfo_t*)AGDATA(g))->exact_ranksep)
	public static int GD_exact_ranksep(ST_Agraph_s g) {
		return ((ST_Agraphinfo_t)AGDATA(g).castTo(ST_Agraphinfo_t.class)).exact_ranksep;
	}

	public static void GD_exact_ranksep(ST_Agraph_s g, int v) {
		((ST_Agraphinfo_t)AGDATA(g).castTo(ST_Agraphinfo_t.class)).setInt("exact_ranksep", v);
	}

	// #define GD_expanded(g) (((Agraphinfo_t*)AGDATA(g))->expanded)
	public static boolean GD_expanded(ST_Agraph_s g) {
		return ((ST_Agraphinfo_t)AGDATA(g).castTo(ST_Agraphinfo_t.class)).expanded;
	}

	public static void GD_expanded(ST_Agraph_s g, boolean v) {
		((ST_Agraphinfo_t)AGDATA(g).castTo(ST_Agraphinfo_t.class)).expanded = v;
	}

	// #define GD_flags(g) (((Agraphinfo_t*)AGDATA(g))->flags)
	public static int GD_flags(ST_Agraph_s g) {
		return ((ST_Agraphinfo_t)AGDATA(g).castTo(ST_Agraphinfo_t.class)).flags;
	}

	public static void GD_flags(ST_Agraph_s g, int v) {
		((ST_Agraphinfo_t)AGDATA(g).castTo(ST_Agraphinfo_t.class)).setInt("flags", v);
	}

	// #define GD_gui_state(g) (((Agraphinfo_t*)AGDATA(g))->gui_state)
	// #define GD_charset(g) (((Agraphinfo_t*)AGDATA(g))->charset)
	public static int GD_charset(ST_Agraph_s g) {
		return ((ST_Agraphinfo_t)AGDATA(g).castTo(ST_Agraphinfo_t.class)).charset;
	}

	public static void GD_charset(ST_Agraph_s g, int v) {
		((ST_Agraphinfo_t)AGDATA(g).castTo(ST_Agraphinfo_t.class)).setInt("charset", v);
	}

	// #define GD_has_labels(g) (((Agraphinfo_t*)AGDATA(g))->has_labels)
	public static int GD_has_labels(__ptr__ g) {
		return ((ST_Agraphinfo_t)AGDATA(g).castTo(ST_Agraphinfo_t.class)).has_labels;
	}

	public static void GD_has_labels(__ptr__ g, int v) {
		((ST_Agraphinfo_t)AGDATA(g).castTo(ST_Agraphinfo_t.class)).setInt("has_labels", v);
	}

	// #define GD_has_images(g) (((Agraphinfo_t*)AGDATA(g))->has_images)
	// #define GD_has_flat_edges(g) (((Agraphinfo_t*)AGDATA(g))->has_flat_edges)
	public static int GD_has_flat_edges(ST_Agraph_s g) {
		return ((ST_Agraphinfo_t)AGDATA(g).castTo(ST_Agraphinfo_t.class)).has_flat_edges;
	}

	public static void GD_has_flat_edges(ST_Agraph_s g, boolean v) {
		((ST_Agraphinfo_t)AGDATA(g).castTo(ST_Agraphinfo_t.class)).has_flat_edges = v?1:0;
	}

	// #define GD_has_sourcerank(g) (((Agraphinfo_t*)AGDATA(g))->has_sourcerank)
	// #define GD_has_sinkrank(g) (((Agraphinfo_t*)AGDATA(g))->has_sinkrank)
	// #define GD_ht1(g) (((Agraphinfo_t*)AGDATA(g))->ht1)
	public static double GD_ht1(ST_Agraph_s g) {
		return ((ST_Agraphinfo_t)AGDATA(g).castTo(ST_Agraphinfo_t.class)).ht1;
	}

	public static void GD_ht1(ST_Agraph_s g, double v) {
		((ST_Agraphinfo_t)AGDATA(g).castTo(ST_Agraphinfo_t.class)).setDouble("ht1", v);
	}

	// #define GD_ht2(g) (((Agraphinfo_t*)AGDATA(g))->ht2)
	public static double GD_ht2(ST_Agraph_s g) {
		return ((ST_Agraphinfo_t)AGDATA(g).castTo(ST_Agraphinfo_t.class)).ht2;
	}

	public static void GD_ht2(ST_Agraph_s g, double v) {
		((ST_Agraphinfo_t)AGDATA(g).castTo(ST_Agraphinfo_t.class)).setDouble("ht2", v);
	}

	// #define GD_inleaf(g) (((Agraphinfo_t*)AGDATA(g))->inleaf)
	// #define GD_installed(g) (((Agraphinfo_t*)AGDATA(g))->installed)
	public static int GD_installed(ST_Agraph_s g) {
		return ((ST_Agraphinfo_t)AGDATA(g).castTo(ST_Agraphinfo_t.class)).installed;
	}

	public static void GD_installed(ST_Agraph_s g, int v) {
		((ST_Agraphinfo_t)AGDATA(g).castTo(ST_Agraphinfo_t.class)).setInt("installed", v);
	}

	// #define GD_label(g) (((Agraphinfo_t*)AGDATA(g))->label)
	public static ST_textlabel_t GD_label(__ptr__ g) {
		return (ST_textlabel_t) ((ST_Agraphinfo_t)AGDATA(g).castTo(ST_Agraphinfo_t.class)).label;
	}

	public static void GD_label(__ptr__ g, __ptr__ v) {
		((ST_Agraphinfo_t)AGDATA(g).castTo(ST_Agraphinfo_t.class)).setPtr("label", v);
	}

	// #define GD_leader(g) (((Agraphinfo_t*)AGDATA(g))->leader)
	public static ST_Agnode_s GD_leader(ST_Agraph_s g) {
		return (ST_Agnode_s) ((ST_Agraphinfo_t)AGDATA(g).castTo(ST_Agraphinfo_t.class)).leader;
	}

	public static void GD_leader(ST_Agraph_s g, __ptr__ v) {
		((ST_Agraphinfo_t)AGDATA(g).castTo(ST_Agraphinfo_t.class)).setPtr("leader", v);
	}

	// #define GD_rankdir2(g) (((Agraphinfo_t*)AGDATA(g))->rankdir)
	public static int GD_rankdir2(ST_Agraph_s g) {
		return ((ST_Agraphinfo_t)AGDATA(g).castTo(ST_Agraphinfo_t.class)).rankdir;
	}

	public static void GD_rankdir2(ST_Agraph_s g, int v) {
		((ST_Agraphinfo_t)AGDATA(g).castTo(ST_Agraphinfo_t.class)).setInt("rankdir", v);
	}

	// #define GD_rankdir(g) (((Agraphinfo_t*)AGDATA(g))->rankdir & 0x3)
	public static int GD_rankdir(ST_Agraph_s g) {
		return ((ST_Agraphinfo_t)AGDATA(g).castTo(ST_Agraphinfo_t.class)).rankdir & 0x3;
	}

	// #define GD_flip(g) (GD_rankdir(g) & 1)
	public static int GD_flip(ST_Agraph_s g) {
		return GD_rankdir(g) & 1;
	}

	// #define GD_realrankdir(g) ((((Agraphinfo_t*)AGDATA(g))->rankdir) >> 2)
	// #define GD_realflip(g) (GD_realrankdir(g) & 1)
	// #define GD_ln(g) (((Agraphinfo_t*)AGDATA(g))->ln)
	public static ST_Agnode_s GD_ln(ST_Agraph_s g) {
		return (ST_Agnode_s) ((ST_Agraphinfo_t)AGDATA(g).castTo(ST_Agraphinfo_t.class)).ln;
	}

	public static void GD_ln(ST_Agraph_s g, __ptr__ v) {
		((ST_Agraphinfo_t)AGDATA(g).castTo(ST_Agraphinfo_t.class)).setPtr("ln", v);
	}

	// #define GD_maxrank(g) (((Agraphinfo_t*)AGDATA(g))->maxrank)
	public static int GD_maxrank(ST_Agraph_s g) {
		return ((ST_Agraphinfo_t)AGDATA(g).castTo(ST_Agraphinfo_t.class)).maxrank;
	}

	public static void GD_maxrank(ST_Agraph_s g, int v) {
		((ST_Agraphinfo_t)AGDATA(g).castTo(ST_Agraphinfo_t.class)).setInt("maxrank", v);
	}

	// #define GD_maxset(g) (((Agraphinfo_t*)AGDATA(g))->maxset)
	public static __ptr__ GD_maxset(ST_Agraph_s g) {
		return ((ST_Agraphinfo_t)AGDATA(g).castTo(ST_Agraphinfo_t.class)).maxset;
	}

	// #define GD_minrank(g) (((Agraphinfo_t*)AGDATA(g))->minrank)
	public static int GD_minrank(ST_Agraph_s g) {
		return ((ST_Agraphinfo_t)AGDATA(g).castTo(ST_Agraphinfo_t.class)).minrank;
	}

	public static void GD_minrank(ST_Agraph_s g, int v) {
		((ST_Agraphinfo_t)AGDATA(g).castTo(ST_Agraphinfo_t.class)).setInt("minrank", v);
	}

	// #define GD_minset(g) (((Agraphinfo_t*)AGDATA(g))->minset)
	public static __ptr__ GD_minset(ST_Agraph_s g) {
		return ((ST_Agraphinfo_t)AGDATA(g).castTo(ST_Agraphinfo_t.class)).minset;
	}

	// #define GD_minrep(g) (((Agraphinfo_t*)AGDATA(g))->minrep)
	// #define GD_maxrep(g) (((Agraphinfo_t*)AGDATA(g))->maxrep)
	// #define GD_move(g) (((Agraphinfo_t*)AGDATA(g))->move)
	// #define GD_n_cluster(g) (((Agraphinfo_t*)AGDATA(g))->n_cluster)
	public static int GD_n_cluster(ST_Agraph_s g) {
		return ((ST_Agraphinfo_t)AGDATA(g).castTo(ST_Agraphinfo_t.class)).n_cluster;
	}

	public static void GD_n_cluster(ST_Agraph_s g, int v) {
		((ST_Agraphinfo_t)AGDATA(g).castTo(ST_Agraphinfo_t.class)).setInt("n_cluster", v);
	}

	// #define GD_n_nodes(g) (((Agraphinfo_t*)AGDATA(g))->n_nodes)
	public static int GD_n_nodes(ST_Agraph_s g) {
		return ((ST_Agraphinfo_t)AGDATA(g).castTo(ST_Agraphinfo_t.class)).n_nodes;
	}

	public static void GD_n_nodes(ST_Agraph_s g, int v) {
		((ST_Agraphinfo_t)AGDATA(g).castTo(ST_Agraphinfo_t.class)).setInt("n_nodes", v);
	}

	// #define GD_ndim(g) (((Agraphinfo_t*)AGDATA(g))->ndim)
	// #define GD_odim(g) (((Agraphinfo_t*)AGDATA(g))->odim)
	// #define GD_neato_nlist(g) (((Agraphinfo_t*)AGDATA(g))->neato_nlist)
	// #define GD_nlist(g) (((Agraphinfo_t*)AGDATA(g))->nlist)
	public static ST_Agnode_s GD_nlist(ST_Agraph_s g) {
		return (ST_Agnode_s) ((ST_Agraphinfo_t)AGDATA(g).castTo(ST_Agraphinfo_t.class)).nlist;
	}

	public static void GD_nlist(ST_Agraph_s g, __ptr__ v) {
		((ST_Agraphinfo_t)AGDATA(g).castTo(ST_Agraphinfo_t.class)).setPtr("nlist", v);
	}

	// #define GD_nodesep(g) (((Agraphinfo_t*)AGDATA(g))->nodesep)
	public static int GD_nodesep(ST_Agraph_s g) {
		return ((ST_Agraphinfo_t)AGDATA(g).castTo(ST_Agraphinfo_t.class)).nodesep;
	}

	public static void GD_nodesep(ST_Agraph_s g, int v) {
		((ST_Agraphinfo_t)AGDATA(g).castTo(ST_Agraphinfo_t.class)).setInt("nodesep", v);
	}

	// #define GD_outleaf(g) (((Agraphinfo_t*)AGDATA(g))->outleaf)
	// #define GD_rank(g) (((Agraphinfo_t*)AGDATA(g))->rank)
	public static ST_rank_t.Array2 GD_rank(ST_Agraph_s g) {
		return (ST_rank_t.Array2) ((ST_Agraphinfo_t)AGDATA(g).castTo(ST_Agraphinfo_t.class)).rank;
	}

	public static void GD_rank(ST_Agraph_s g, ST_rank_t.Array2 v) {
		((ST_Agraphinfo_t)AGDATA(g).castTo(ST_Agraphinfo_t.class)).setPtr("rank", v);
	}

	// #define GD_rankleader(g) (((Agraphinfo_t*)AGDATA(g))->rankleader)
	public static ST_Agnode_s.Array GD_rankleader(ST_Agraph_s g) {
		return ((ST_Agraphinfo_t)AGDATA(g).castTo(ST_Agraphinfo_t.class)).rankleader;
	}

	public static void GD_rankleader(ST_Agraph_s g, __ptr__ v) {
		((ST_Agraphinfo_t)AGDATA(g).castTo(ST_Agraphinfo_t.class)).setPtr("rankleader", v);
	}

	// #define GD_ranksep(g) (((Agraphinfo_t*)AGDATA(g))->ranksep)
	public static int GD_ranksep(ST_Agraph_s g) {
		return ((ST_Agraphinfo_t)AGDATA(g).castTo(ST_Agraphinfo_t.class)).ranksep;
	}

	public static void GD_ranksep(ST_Agraph_s g, int v) {
		((ST_Agraphinfo_t)AGDATA(g).castTo(ST_Agraphinfo_t.class)).setInt("ranksep", v);
	}

	// #define GD_rn(g) (((Agraphinfo_t*)AGDATA(g))->rn)
	public static ST_Agnode_s GD_rn(ST_Agraph_s g) {
		return (ST_Agnode_s) ((ST_Agraphinfo_t)AGDATA(g).castTo(ST_Agraphinfo_t.class)).rn;
	}

	public static void GD_rn(ST_Agraph_s g, __ptr__ v) {
		((ST_Agraphinfo_t)AGDATA(g).castTo(ST_Agraphinfo_t.class)).setPtr("rn", v);
	}

	// #define GD_set_type(g) (((Agraphinfo_t*)AGDATA(g))->set_type)
	public static int GD_set_type(ST_Agraph_s g) {
		return ((ST_Agraphinfo_t)AGDATA(g).castTo(ST_Agraphinfo_t.class)).set_type;
	}

	public static void GD_set_type(ST_Agraph_s g, int v) {
		((ST_Agraphinfo_t)AGDATA(g).castTo(ST_Agraphinfo_t.class)).setInt("set_type", v);
	}

	// #define GD_label_pos(g) (((Agraphinfo_t*)AGDATA(g))->label_pos)
	public static int GD_label_pos(ST_Agraph_s g) {
		return ((ST_Agraphinfo_t)AGDATA(g).castTo(ST_Agraphinfo_t.class)).label_pos;
	}

	public static void GD_label_pos(ST_Agraph_s g, int v) {
		((ST_Agraphinfo_t)AGDATA(g).castTo(ST_Agraphinfo_t.class)).setInt("label_pos", v);
	}

	// #define GD_showboxes(g) (((Agraphinfo_t*)AGDATA(g))->showboxes)
	public static int GD_showboxes(ST_Agraph_s g) {
		return ((ST_Agraphinfo_t)AGDATA(g).castTo(ST_Agraphinfo_t.class)).showboxes;
	}

	public static void GD_showboxes(ST_Agraph_s g, int v) {
		((ST_Agraphinfo_t)AGDATA(g).castTo(ST_Agraphinfo_t.class)).setInt("showboxes", v);
	}

	// #define GD_fontnames(g) (((Agraphinfo_t*)AGDATA(g))->fontnames)
	public static int GD_fontnames(ST_Agraph_s g) {
		return ((ST_Agraphinfo_t)AGDATA(g).castTo(ST_Agraphinfo_t.class)).fontnames;
	}

	public static void GD_fontnames(ST_Agraph_s g, int v) {
		((ST_Agraphinfo_t)AGDATA(g).castTo(ST_Agraphinfo_t.class)).setInt("fontnames", v);
	}

	// #define GD_spring(g) (((Agraphinfo_t*)AGDATA(g))->spring)
	// #define GD_sum_t(g) (((Agraphinfo_t*)AGDATA(g))->sum_t)
	// #define GD_t(g) (((Agraphinfo_t*)AGDATA(g))->t)

	// #define ND_id(n) (((Agnodeinfo_t*)AGDATA(n))->id)
	public static int ND_id(ST_Agnode_s n) {
		return ((ST_Agnodeinfo_t)AGDATA(n).castTo(ST_Agnodeinfo_t.class)).id;
	}

	// #define ND_alg(n) (((Agnodeinfo_t*)AGDATA(n))->alg)
	public static __ptr__ ND_alg(ST_Agnode_s n) {
		return ((ST_Agnodeinfo_t)AGDATA(n).castTo(ST_Agnodeinfo_t.class)).alg;
	}

	public static void ND_alg(ST_Agnode_s n, __ptr__ value) {
		((ST_Agnodeinfo_t)AGDATA(n).castTo(ST_Agnodeinfo_t.class)).setPtr("alg", value);
	}

	// #define ND_UF_parent(n) (((Agnodeinfo_t*)AGDATA(n))->UF_parent)
	public static ST_Agnode_s ND_UF_parent(__ptr__ n) {
		return (ST_Agnode_s) ((ST_Agnodeinfo_t)AGDATA(n).castTo(ST_Agnodeinfo_t.class)).UF_parent;
	}

	public static void ND_UF_parent(__ptr__ n, __ptr__ v) {
		((ST_Agnodeinfo_t)AGDATA(n).castTo(ST_Agnodeinfo_t.class)).setPtr("UF_parent", v);
	}

	// #define ND_set(n) (((Agnodeinfo_t*)AGDATA(n))->set)
	// #define ND_UF_size(n) (((Agnodeinfo_t*)AGDATA(n))->UF_size)
	public static int ND_UF_size(ST_Agnode_s n) {
		return ((ST_Agnodeinfo_t)AGDATA(n).castTo(ST_Agnodeinfo_t.class)).UF_size;
	}

	public static void ND_UF_size(ST_Agnode_s n, int v) {
		((ST_Agnodeinfo_t)AGDATA(n).castTo(ST_Agnodeinfo_t.class)).setInt("UF_size", v);
	}

	// #define ND_bb(n) (((Agnodeinfo_t*)AGDATA(n))->bb)
	// #define ND_clust(n) (((Agnodeinfo_t*)AGDATA(n))->clust)
	public static ST_Agraph_s ND_clust(__ptr__ n) {
		return (ST_Agraph_s) ((ST_Agnodeinfo_t)AGDATA(n).castTo(ST_Agnodeinfo_t.class)).clust;
	}

	public static void ND_clust(ST_Agnode_s n, __ptr__ v) {
		((ST_Agnodeinfo_t)AGDATA(n).castTo(ST_Agnodeinfo_t.class)).setPtr("clust", v);
	}

	// #define ND_coord(n) (((Agnodeinfo_t*)AGDATA(n))->coord)
	public static ST_pointf ND_coord(__ptr__ n) {
		return (ST_pointf) ((ST_Agnodeinfo_t)AGDATA(n).castTo(ST_Agnodeinfo_t.class)).coord;
	}

	// #define ND_dist(n) (((Agnodeinfo_t*)AGDATA(n))->dist)

	// #define ND_flat_in(n) (((Agnodeinfo_t*)AGDATA(n))->flat_in)
	public static ST_elist ND_flat_in(ST_Agnode_s n) {
		return (ST_elist) ((ST_Agnodeinfo_t)AGDATA(n).castTo(ST_Agnodeinfo_t.class)).flat_in;
	}

	// #define ND_flat_out(n) (((Agnodeinfo_t*)AGDATA(n))->flat_out)
	public static ST_elist ND_flat_out(ST_Agnode_s n) {
		return (ST_elist) ((ST_Agnodeinfo_t)AGDATA(n).castTo(ST_Agnodeinfo_t.class)).flat_out;
	}

	// #define ND_gui_state(n) (((Agnodeinfo_t*)AGDATA(n))->gui_state)
	// #define ND_has_port(n) (((Agnodeinfo_t*)AGDATA(n))->has_port)
	public static boolean ND_has_port(ST_Agnode_s n) {
		return ((ST_Agnodeinfo_t)AGDATA(n).castTo(ST_Agnodeinfo_t.class)).has_port;
	}

	// #define ND_rep(n) (((Agnodeinfo_t*)AGDATA(n))->rep)
	// #define ND_heapindex(n) (((Agnodeinfo_t*)AGDATA(n))->heapindex)
	// #define ND_height(n) (((Agnodeinfo_t*)AGDATA(n))->height)
	public static double ND_height(__ptr__ n) {
		return ((ST_Agnodeinfo_t)AGDATA(n).castTo(ST_Agnodeinfo_t.class)).height;
	}

	public static void ND_height(ST_Agnode_s n, double v) {
		((ST_Agnodeinfo_t)AGDATA(n).castTo(ST_Agnodeinfo_t.class)).setDouble("height", v);
	}

	// #define ND_hops(n) (((Agnodeinfo_t*)AGDATA(n))->hops)
	// #define ND_ht(n) (((Agnodeinfo_t*)AGDATA(n))->ht)
	public static double ND_ht(__ptr__ n) {
		return ((ST_Agnodeinfo_t)AGDATA(n).castTo(ST_Agnodeinfo_t.class)).ht;
	}

	public static void ND_ht(ST_Agnode_s n, double v) {
		((ST_Agnodeinfo_t)AGDATA(n).castTo(ST_Agnodeinfo_t.class)).setDouble("ht", v);
	}

	// #define ND_in(n) (((Agnodeinfo_t*)AGDATA(n))->in)
	public static ST_elist ND_in(ST_Agnode_s n) {
		return ((ST_Agnodeinfo_t) AGDATA(n).castTo(ST_Agnodeinfo_t.class)).in;
	}

	public static void ND_in(__ptr__ n, __struct__<ST_elist> v) {
		((ST_Agnodeinfo_t)AGDATA(n).castTo(ST_Agnodeinfo_t.class)).setStruct("in", v);
	}

	// #define ND_inleaf(n) (((Agnodeinfo_t*)AGDATA(n))->inleaf)
	public static __ptr__ ND_inleaf(ST_Agnode_s n) {
		return ((ST_Agnodeinfo_t)AGDATA(n).castTo(ST_Agnodeinfo_t.class)).inleaf;
	}

	// #define ND_label(n) (((Agnodeinfo_t*)AGDATA(n))->label)
	public static ST_textlabel_t ND_label(ST_Agnode_s n) {
		return (ST_textlabel_t) ((ST_Agnodeinfo_t)AGDATA(n).castTo(ST_Agnodeinfo_t.class)).label;
	}

	public static void ND_label(ST_Agnode_s n, __ptr__ v) {
		((ST_Agnodeinfo_t)AGDATA(n).castTo(ST_Agnodeinfo_t.class)).setPtr("label", v);
	}

	// #define ND_xlabel(n) (((Agnodeinfo_t*)AGDATA(n))->xlabel)
	public static ST_textlabel_t ND_xlabel(ST_Agnode_s n) {
		return (ST_textlabel_t) ((ST_Agnodeinfo_t)AGDATA(n).castTo(ST_Agnodeinfo_t.class)).xlabel;
	}

	// #define ND_lim(n) (((Agnodeinfo_t*)AGDATA(n))->lim)
	public static int ND_lim(ST_Agnode_s n) {
		return ((ST_Agnodeinfo_t)AGDATA(n).castTo(ST_Agnodeinfo_t.class)).lim;
	}

	public static void ND_lim(ST_Agnode_s n, int v) {
		((ST_Agnodeinfo_t)AGDATA(n).castTo(ST_Agnodeinfo_t.class)).setInt("lim", v);
	}

	// #define ND_low(n) (((Agnodeinfo_t*)AGDATA(n))->low)
	public static int ND_low(ST_Agnode_s n) {
		return ((ST_Agnodeinfo_t)AGDATA(n).castTo(ST_Agnodeinfo_t.class)).low;
	}

	public static void ND_low(ST_Agnode_s n, int v) {
		((ST_Agnodeinfo_t)AGDATA(n).castTo(ST_Agnodeinfo_t.class)).setInt("low", v);
	}

	// #define ND_lw(n) (((Agnodeinfo_t*)AGDATA(n))->lw)
	public static double ND_lw(__ptr__ n) {
		return ((ST_Agnodeinfo_t)AGDATA(n).castTo(ST_Agnodeinfo_t.class)).lw;
	}

	public static void ND_lw(ST_Agnode_s n, double v) {
		((ST_Agnodeinfo_t)AGDATA(n).castTo(ST_Agnodeinfo_t.class)).setDouble("lw", v);
	}

	// #define ND_mark(n) (((Agnodeinfo_t*)AGDATA(n))->mark)
	public static int ND_mark(__ptr__ n) {
		return ((ST_Agnodeinfo_t)AGDATA(n).castTo(ST_Agnodeinfo_t.class)).mark;
	}

	public static void ND_mark(__ptr__ n, int v) {
		((ST_Agnodeinfo_t)AGDATA(n).castTo(ST_Agnodeinfo_t.class)).setInt("mark", v);
	}

	public static void ND_mark(__ptr__ n, boolean v) {
		((ST_Agnodeinfo_t)AGDATA(n).castTo(ST_Agnodeinfo_t.class)).mark = v?1:0;
	}

	// #define ND_mval(n) (((Agnodeinfo_t*)AGDATA(n))->mval)
	public static double ND_mval(__ptr__ n) {
		return ((ST_Agnodeinfo_t)AGDATA(n).castTo(ST_Agnodeinfo_t.class)).mval;
	}

	public static void ND_mval(ST_Agnode_s n, double v) {
		((ST_Agnodeinfo_t)AGDATA(n).castTo(ST_Agnodeinfo_t.class)).setDouble("mval", v);
	}

	// #define ND_n_cluster(n) (((Agnodeinfo_t*)AGDATA(n))->n_cluster)
	// #define ND_next(n) (((Agnodeinfo_t*)AGDATA(n))->next)
	public static ST_Agnode_s ND_next(ST_Agnode_s n) {
		return (ST_Agnode_s) ((ST_Agnodeinfo_t) AGDATA(n).castTo(ST_Agnodeinfo_t.class)).next;
	}

	public static void ND_next(__ptr__ n, __ptr__ v) {
		((ST_Agnodeinfo_t)AGDATA(n).castTo(ST_Agnodeinfo_t.class)).setPtr("next", v);
	}

	// #define ND_node_type(n) (((Agnodeinfo_t*)AGDATA(n))->node_type)
	public static int ND_node_type(ST_Agnode_s n) {
		return ((ST_Agnodeinfo_t)AGDATA(n).castTo(ST_Agnodeinfo_t.class)).node_type;
	}

	public static void ND_node_type(ST_Agnode_s n, int v) {
		((ST_Agnodeinfo_t)AGDATA(n).castTo(ST_Agnodeinfo_t.class)).setInt("node_type", v);
	}

	// #define ND_onstack(n) (((Agnodeinfo_t*)AGDATA(n))->onstack)
	public static boolean ND_onstack(ST_Agnode_s n) {
		return ((ST_Agnodeinfo_t)AGDATA(n).castTo(ST_Agnodeinfo_t.class)).onstack!=0;
	}

	public static void ND_onstack(ST_Agnode_s n, int v) {
		((ST_Agnodeinfo_t)AGDATA(n).castTo(ST_Agnodeinfo_t.class)).setInt("onstack", v);
	}

	public static void ND_onstack(ST_Agnode_s n, boolean v) {
		((ST_Agnodeinfo_t)AGDATA(n).castTo(ST_Agnodeinfo_t.class)).onstack = v?1:0;
	}

	// #define ND_order(n) (((Agnodeinfo_t*)AGDATA(n))->order)
	public static int ND_order(__ptr__ n) {
		return ((ST_Agnodeinfo_t)AGDATA(n).castTo(ST_Agnodeinfo_t.class)).order;
	}

	public static void ND_order(__ptr__ n, int v) {
		((ST_Agnodeinfo_t)AGDATA(n).castTo(ST_Agnodeinfo_t.class)).setInt("order", v);
	}

	// #define ND_other(n) (((Agnodeinfo_t*)AGDATA(n))->other)
	public static ST_elist ND_other(ST_Agnode_s n) {
		return (ST_elist) ((ST_Agnodeinfo_t)AGDATA(n).castTo(ST_Agnodeinfo_t.class)).other;
	}

	// #define ND_out(n) (((Agnodeinfo_t*)AGDATA(n))->out)
	public static ST_elist ND_out(__ptr__ n) {
		return ((ST_Agnodeinfo_t) AGDATA(n).castTo(ST_Agnodeinfo_t.class)).out;
	}

	public static void ND_out(__ptr__ n, __struct__<ST_elist> v) {
		((ST_Agnodeinfo_t)AGDATA(n).castTo(ST_Agnodeinfo_t.class)).setStruct("out", v);
	}

	// #define ND_outleaf(n) (((Agnodeinfo_t*)AGDATA(n))->outleaf)
	public static __ptr__ ND_outleaf(ST_Agnode_s n) {
		return ((ST_Agnodeinfo_t)AGDATA(n).castTo(ST_Agnodeinfo_t.class)).outleaf;
	}

	// #define ND_par(n) (((Agnodeinfo_t*)AGDATA(n))->par)
	public static ST_Agedge_s ND_par(ST_Agnode_s n) {
		return (ST_Agedge_s) ((ST_Agnodeinfo_t)AGDATA(n).castTo(ST_Agnodeinfo_t.class)).par;
	}

	public static void ND_par(ST_Agnode_s n, __ptr__ v) {
		((ST_Agnodeinfo_t)AGDATA(n).castTo(ST_Agnodeinfo_t.class)).setPtr("par", v);
	}

	// #define ND_pinned(n) (((Agnodeinfo_t*)AGDATA(n))->pinned)
	// #define ND_pos(n) (((Agnodeinfo_t*)AGDATA(n))->pos)
	// #define ND_prev(n) (((Agnodeinfo_t*)AGDATA(n))->prev)
	public static __ptr__ ND_prev(ST_Agnode_s n) {
		return ((ST_Agnodeinfo_t)AGDATA(n).castTo(ST_Agnodeinfo_t.class)).prev;
	}

	public static void ND_prev(ST_Agnode_s n, __ptr__ v) {
		((ST_Agnodeinfo_t)AGDATA(n).castTo(ST_Agnodeinfo_t.class)).setPtr("prev", v);
	}

	// #define ND_priority(n) (((Agnodeinfo_t*)AGDATA(n))->priority)
	public static int ND_priority(ST_Agnode_s n) {
		return ((ST_Agnodeinfo_t)AGDATA(n).castTo(ST_Agnodeinfo_t.class)).priority;
	}

	public static void ND_priority(ST_Agnode_s n, int v) {
		((ST_Agnodeinfo_t)AGDATA(n).castTo(ST_Agnodeinfo_t.class)).setInt("priority", v);
	}

	// #define ND_rank(n) (((Agnodeinfo_t*)AGDATA(n))->rank)
	public static int ND_rank(__ptr__ n) {
		return ((ST_Agnodeinfo_t)AGDATA(n).castTo(ST_Agnodeinfo_t.class)).rank;
	}

	public static void ND_rank(__ptr__ n, int v) {
		((ST_Agnodeinfo_t)AGDATA(n).castTo(ST_Agnodeinfo_t.class)).setInt("rank", v);
	}

	// #define ND_ranktype(n) (((Agnodeinfo_t*)AGDATA(n))->ranktype)
	public static int ND_ranktype(ST_Agnode_s n) {
		return ((ST_Agnodeinfo_t)AGDATA(n).castTo(ST_Agnodeinfo_t.class)).ranktype;
	}

	public static void ND_ranktype(ST_Agnode_s n, int v) {
		((ST_Agnodeinfo_t)AGDATA(n).castTo(ST_Agnodeinfo_t.class)).setInt("ranktype", v);
	}

	// #define ND_rw(n) (((Agnodeinfo_t*)AGDATA(n))->rw)
	public static double ND_rw(__ptr__ n) {
		return ((ST_Agnodeinfo_t)AGDATA(n).castTo(ST_Agnodeinfo_t.class)).rw;
	}

	public static void ND_rw(ST_Agnode_s n, double v) {
		((ST_Agnodeinfo_t)AGDATA(n).castTo(ST_Agnodeinfo_t.class)).setDouble("rw", v);
	}

	// #define ND_save_in(n) (((Agnodeinfo_t*)AGDATA(n))->save_in)
	public static ST_elist ND_save_in(ST_Agnode_s n) {
		return (ST_elist) ((ST_Agnodeinfo_t)AGDATA(n).castTo(ST_Agnodeinfo_t.class)).save_in;
	}

	public static void ND_save_in(ST_Agnode_s n, __struct__<ST_elist> v) {
		((ST_Agnodeinfo_t)AGDATA(n).castTo(ST_Agnodeinfo_t.class)).setStruct("save_in", v);
	}

	// #define ND_save_out(n) (((Agnodeinfo_t*)AGDATA(n))->save_out)
	public static ST_elist ND_save_out(ST_Agnode_s n) {
		return (ST_elist) ((ST_Agnodeinfo_t)AGDATA(n).castTo(ST_Agnodeinfo_t.class)).save_out;
	}

	public static void ND_save_out(ST_Agnode_s n, __struct__<ST_elist> v) {
		((ST_Agnodeinfo_t)AGDATA(n).castTo(ST_Agnodeinfo_t.class)).setStruct("save_out", v);
	}

	// #define ND_shape(n) (((Agnodeinfo_t*)AGDATA(n))->shape)
	public static ST_shape_desc ND_shape(ST_Agnode_s n) {
		return ((ST_Agnodeinfo_t) AGDATA(n).castTo(ST_Agnodeinfo_t.class)).shape;
	}

	public static void ND_shape(ST_Agnode_s n, __ptr__ v) {
		((ST_Agnodeinfo_t)AGDATA(n).castTo(ST_Agnodeinfo_t.class)).setPtr("shape", v);
	}

	// #define ND_shape_info(n) (((Agnodeinfo_t*)AGDATA(n))->shape_info)
	public static __ptr__ ND_shape_info(__ptr__ n) {
		return ((ST_Agnodeinfo_t)AGDATA(n).castTo(ST_Agnodeinfo_t.class)).shape_info;
	}

	// #define ND_showboxes(n) (((Agnodeinfo_t*)AGDATA(n))->showboxes)
	public static int ND_showboxes(ST_Agnode_s n) {
		return ((ST_Agnodeinfo_t)AGDATA(n).castTo(ST_Agnodeinfo_t.class)).showboxes;
	}

	public static void ND_showboxes(ST_Agnode_s n, int v) {
		((ST_Agnodeinfo_t)AGDATA(n).castTo(ST_Agnodeinfo_t.class)).setInt("showboxes", v);
	}

	// #define ND_state(n) (((Agnodeinfo_t*)AGDATA(n))->state)
	// #define ND_clustnode(n) (((Agnodeinfo_t*)AGDATA(n))->clustnode)
	// #define ND_tree_in(n) (((Agnodeinfo_t*)AGDATA(n))->tree_in)
	public static ST_elist ND_tree_in(ST_Agnode_s n) {
		return (ST_elist) ((ST_Agnodeinfo_t)AGDATA(n).castTo(ST_Agnodeinfo_t.class)).tree_in;
	}

	// #define ND_tree_out(n) (((Agnodeinfo_t*)AGDATA(n))->tree_out)
	public static ST_elist ND_tree_out(ST_Agnode_s n) {
		return (ST_elist) ((ST_Agnodeinfo_t)AGDATA(n).castTo(ST_Agnodeinfo_t.class)).tree_out;
	}

	// #define ND_weight_class(n) (((Agnodeinfo_t*)AGDATA(n))->weight_class)
	public static int ND_weight_class(ST_Agnode_s n) {
		return ((ST_Agnodeinfo_t)AGDATA(n).castTo(ST_Agnodeinfo_t.class)).weight_class;
	}

	public static void ND_weight_class(ST_Agnode_s n, int v) {
		((ST_Agnodeinfo_t)AGDATA(n).castTo(ST_Agnodeinfo_t.class)).setInt("weight_class", v);
	}

	// #define ND_width(n) (((Agnodeinfo_t*)AGDATA(n))->width)
	public static double ND_width(__ptr__ n) {
		return ((ST_Agnodeinfo_t)AGDATA(n).castTo(ST_Agnodeinfo_t.class)).width;
	}

	public static void ND_width(ST_Agnode_s n, double v) {
		((ST_Agnodeinfo_t)AGDATA(n).castTo(ST_Agnodeinfo_t.class)).setDouble("width", v);
	}

	// #define ND_xsize(n) (ND_lw(n)+ND_rw(n))
	// #define ND_ysize(n) (ND_ht(n))

	// #define ED_alg(e) (((Agedgeinfo_t*)AGDATA(e))->alg)
	// #define ED_conc_opp_flag(e) (((Agedgeinfo_t*)AGDATA(e))->conc_opp_flag)
	public static boolean ED_conc_opp_flag(ST_Agedge_s e) {
		return ((ST_Agedgeinfo_t)AGDATA(e).castTo(ST_Agedgeinfo_t.class)).conc_opp_flag;
	}

	public static void ED_conc_opp_flag(ST_Agedge_s e, boolean v) {
		((ST_Agedgeinfo_t)AGDATA(e).castTo(ST_Agedgeinfo_t.class)).setInt("conc_opp_flag", v ? 1 : 0);
	}

	// #define ED_count(e) (((Agedgeinfo_t*)AGDATA(e))->count)
	public static int ED_count(__ptr__ e) {
		return ((ST_Agedgeinfo_t) AGDATA(e).castTo(ST_Agedgeinfo_t.class)).count;
	}

	public static void ED_count(__ptr__ e, int v) {
		((ST_Agedgeinfo_t)AGDATA(e).castTo(ST_Agedgeinfo_t.class)).setInt("count", v);
	}

	// #define ED_cutvalue(e) (((Agedgeinfo_t*)AGDATA(e))->cutvalue)
	public static int ED_cutvalue(ST_Agedge_s e) {
		return ((ST_Agedgeinfo_t)AGDATA(e).castTo(ST_Agedgeinfo_t.class)).cutvalue;
	}

	public static void ED_cutvalue(ST_Agedge_s e, int v) {
		((ST_Agedgeinfo_t)AGDATA(e).castTo(ST_Agedgeinfo_t.class)).setInt("cutvalue", v);
	}

	// #define ED_edge_type(e) (((Agedgeinfo_t*)AGDATA(e))->edge_type)
	// #define ED_adjacent(e) (((Agedgeinfo_t*)AGDATA(e))->adjacent)
	public static int ED_adjacent(__ptr__ e) {
		return ((ST_Agedgeinfo_t)AGDATA(e).castTo(ST_Agedgeinfo_t.class)).adjacent;
	}

	public static void ED_adjacent(ST_Agedge_s e, int v) {
		((ST_Agedgeinfo_t)AGDATA(e).castTo(ST_Agedgeinfo_t.class)).setInt("adjacent", v);
	}

	// #define ED_factor(e) (((Agedgeinfo_t*)AGDATA(e))->factor)
	// #define ED_gui_state(e) (((Agedgeinfo_t*)AGDATA(e))->gui_state)
	// #define ED_head_label(e) (((Agedgeinfo_t*)AGDATA(e))->head_label)
	public static ST_textlabel_t ED_head_label(ST_Agedge_s e) {
		return (ST_textlabel_t) ((ST_Agedgeinfo_t)AGDATA(e).castTo(ST_Agedgeinfo_t.class)).head_label;
	}

	public static void ED_head_label(ST_Agedge_s e, __ptr__ v) {
		((ST_Agedgeinfo_t)AGDATA(e).castTo(ST_Agedgeinfo_t.class)).setPtr("head_label", v);
	}

	// #define ED_head_port(e) (((Agedgeinfo_t*)AGDATA(e))->head_port)
	public static ST_port ED_head_port(__ptr__ e) {
		return (ST_port) ((ST_Agedgeinfo_t)AGDATA(e).castTo(ST_Agedgeinfo_t.class)).head_port;
	}

	public static void ED_head_port(ST_Agedge_s e, ST_port v) {
		((ST_Agedgeinfo_t)AGDATA(e).castTo(ST_Agedgeinfo_t.class)).setStruct("head_port", v);
	}

	// #define ED_label(e) (((Agedgeinfo_t*)AGDATA(e))->label)
	public static ST_textlabel_t ED_label(__ptr__ e) {
		return (ST_textlabel_t) ((ST_Agedgeinfo_t)AGDATA(e).castTo(ST_Agedgeinfo_t.class)).label;
	}

	public static void ED_label(ST_Agedge_s e, __ptr__ v) {
		((ST_Agedgeinfo_t)AGDATA(e).castTo(ST_Agedgeinfo_t.class)).setPtr("label", v);
	}

	// #define ED_xlabel(e) (((Agedgeinfo_t*)AGDATA(e))->xlabel)
	public static ST_textlabel_t ED_xlabel(ST_Agedge_s e) {
		return (ST_textlabel_t) ((ST_Agedgeinfo_t)AGDATA(e).castTo(ST_Agedgeinfo_t.class)).xlabel;
	}

	// #define ED_label_ontop(e) (((Agedgeinfo_t*)AGDATA(e))->label_ontop)
	public static boolean ED_label_ontop(ST_Agedge_s e) {
		return ((ST_Agedgeinfo_t)AGDATA(e).castTo(ST_Agedgeinfo_t.class)).label_ontop;
	}

	public static void ED_label_ontop(ST_Agedge_s e, boolean v) {
		((ST_Agedgeinfo_t)AGDATA(e).castTo(ST_Agedgeinfo_t.class)).label_ontop = v;
	}

	// #define ED_minlen(e) (((Agedgeinfo_t*)AGDATA(e))->minlen)
	public static int ED_minlen(ST_Agedge_s e) {
		return ((ST_Agedgeinfo_t)AGDATA(e).castTo(ST_Agedgeinfo_t.class)).minlen;
	}

	public static void ED_minlen(ST_Agedge_s e, int v) {
		((ST_Agedgeinfo_t)AGDATA(e).castTo(ST_Agedgeinfo_t.class)).setInt("minlen", v);
	}

	// #define ED_path(e) (((Agedgeinfo_t*)AGDATA(e))->path)
	// #define ED_showboxes(e) (((Agedgeinfo_t*)AGDATA(e))->showboxes)
	public static int ED_showboxes(ST_Agedge_s e) {
		return ((ST_Agedgeinfo_t)AGDATA(e).castTo(ST_Agedgeinfo_t.class)).showboxes;
	}

	public static void ED_showboxes(ST_Agedge_s e, int v) {
		((ST_Agedgeinfo_t)AGDATA(e).castTo(ST_Agedgeinfo_t.class)).setInt("showboxes", v);
	}

	// #define ED_spl(e) (((Agedgeinfo_t*)AGDATA(e))->spl)
	public static ST_splines ED_spl(ST_Agedge_s e) {
		return ((ST_Agedgeinfo_t) AGDATA(e).castTo(ST_Agedgeinfo_t.class)).spl;
	}

	public static void ED_spl(ST_Agedge_s e, ST_splines v) {
		((ST_Agedgeinfo_t) AGDATA(e).castTo(ST_Agedgeinfo_t.class)).spl = v;
	}

	// #define ED_tail_label(e) (((Agedgeinfo_t*)AGDATA(e))->tail_label)
	public static ST_textlabel_t ED_tail_label(ST_Agedge_s e) {
		return (ST_textlabel_t) ((ST_Agedgeinfo_t)AGDATA(e).castTo(ST_Agedgeinfo_t.class)).tail_label;
	}

	public static void ED_tail_label(ST_Agedge_s e, __ptr__ v) {
		((ST_Agedgeinfo_t)AGDATA(e).castTo(ST_Agedgeinfo_t.class)).setPtr("tail_label", v);
	}

	// #define ED_tail_port(e) (((Agedgeinfo_t*)AGDATA(e))->tail_port)
	public static ST_port ED_tail_port(__ptr__ e) {
		return (ST_port) ((ST_Agedgeinfo_t)AGDATA(e).castTo(ST_Agedgeinfo_t.class)).tail_port;
	}

	public static void ED_tail_port(ST_Agedge_s e, ST_port v) {
		((ST_Agedgeinfo_t)AGDATA(e).castTo(ST_Agedgeinfo_t.class)).setStruct("tail_port", v);
	}

	// #define ED_to_orig(e) (((Agedgeinfo_t*)AGDATA(e))->to_orig)
	public static ST_Agedge_s ED_to_orig(__ptr__ e) {
		return (ST_Agedge_s) ((ST_Agedgeinfo_t)AGDATA(e).castTo(ST_Agedgeinfo_t.class)).to_orig;
	}

	public static void ED_to_orig(ST_Agedge_s e, __ptr__ v) {
		((ST_Agedgeinfo_t)AGDATA(e).castTo(ST_Agedgeinfo_t.class)).setPtr("to_orig", v);
	}

	// #define ED_to_virt(e) (((Agedgeinfo_t*)AGDATA(e))->to_virt)
	public static ST_Agedge_s ED_to_virt(ST_Agedge_s e) {
		return (ST_Agedge_s) ((ST_Agedgeinfo_t)AGDATA(e).castTo(ST_Agedgeinfo_t.class)).to_virt;
	}

	public static void ED_to_virt(ST_Agedge_s e, __ptr__ v) {
		((ST_Agedgeinfo_t)AGDATA(e).castTo(ST_Agedgeinfo_t.class)).setPtr("to_virt", v);
	}

	// #define ED_tree_index(e) (((Agedgeinfo_t*)AGDATA(e))->tree_index)
	public static int ED_tree_index(__ptr__ e) {
		return ((ST_Agedgeinfo_t)AGDATA(e).castTo(ST_Agedgeinfo_t.class)).tree_index;
	}

	public static void ED_tree_index(__ptr__ e, int v) {
		((ST_Agedgeinfo_t)AGDATA(e).castTo(ST_Agedgeinfo_t.class)).setInt("tree_index", v);
	}

	// #define ED_xpenalty(e) (((Agedgeinfo_t*)AGDATA(e))->xpenalty)
	public static int ED_xpenalty(__ptr__ e) {
		return ((ST_Agedgeinfo_t)AGDATA(e).castTo(ST_Agedgeinfo_t.class)).xpenalty;
	}

	public static void ED_xpenalty(ST_Agedge_s e, int v) {
		((ST_Agedgeinfo_t)AGDATA(e).castTo(ST_Agedgeinfo_t.class)).setInt("xpenalty", v);
	}

	// #define ED_dist(e) (((Agedgeinfo_t*)AGDATA(e))->dist)
	public static double ED_dist(ST_Agedge_s e) {
		return ((ST_Agedgeinfo_t)AGDATA(e).castTo(ST_Agedgeinfo_t.class)).dist;
	}

	public static void ED_dist(ST_Agedge_s e, double v) {
		((ST_Agedgeinfo_t)AGDATA(e).castTo(ST_Agedgeinfo_t.class)).setDouble("dist", v);
	}

	// #define ED_weight(e) (((Agedgeinfo_t*)AGDATA(e))->weight)
	public static int ED_weight(ST_Agedge_s e) {
		return ((ST_Agedgeinfo_t)AGDATA(e).castTo(ST_Agedgeinfo_t.class)).weight;
	}

	public static void ED_weight(ST_Agedge_s e, int v) {
		((ST_Agedgeinfo_t)AGDATA(e).castTo(ST_Agedgeinfo_t.class)).setInt("weight", v);
	}

	//
	// #define ED_alg(e) (((Agedgeinfo_t*)AGDATA(e))->alg)
	// #define ED_conc_opp_flag(e) (((Agedgeinfo_t*)AGDATA(e))->conc_opp_flag)
	// #define ED_count(e) (((Agedgeinfo_t*)AGDATA(e))->count)
	// #define ED_cutvalue(e) (((Agedgeinfo_t*)AGDATA(e))->cutvalue)
	// #define ED_edge_type(e) (((Agedgeinfo_t*)AGDATA(e))->edge_type)
	public static int ED_edge_type(ST_Agedge_s e) {
		return ((ST_Agedgeinfo_t)AGDATA(e).castTo(ST_Agedgeinfo_t.class)).edge_type;
	}

	public static void ED_edge_type(ST_Agedge_s e, int v) {
		((ST_Agedgeinfo_t)AGDATA(e).castTo(ST_Agedgeinfo_t.class)).setInt("edge_type", v);
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

	public static ST_Agedge_s.ArrayOfStar ALLOC_allocated_ST_Agedge_s(ST_Agedge_s.ArrayOfStar old, int size) {
		return old != null ? old.reallocJ(size) : new ST_Agedge_s.ArrayOfStar(size);
	}

	public static ST_Agnode_s.ArrayOfStar ALLOC_allocated_ST_Agnode_s(ST_Agnode_s.ArrayOfStar old, int size) {
		return old != null ? old.reallocJ(size) : new ST_Agnode_s.ArrayOfStar(size);
	}

	public static ST_pointf.Array ALLOC_allocated_ST_pointf(ST_pointf.Array old, int size) {
		return old != null ? old.reallocJ(size) : new ST_pointf.Array(size);
	}

	public static ST_Pedge_t.Array ALLOC_allocated_ST_Pedge_t(ST_Pedge_t.Array old, int size) {
		return old != null ? old.reallocJ(size) : new ST_Pedge_t.Array(size);
	}

	public static __ptr__ ALLOC_INT(int size, __ptr__ ptr) {
		return (__ptr__) (ptr != null ? JUtils.size_t_array_of_integer(size).realloc(ptr) : JUtils
				.size_t_array_of_integer(size).malloc());
	}

	// #define RALLOC(size,ptr,type) ((type*)realloc(ptr,(size)*sizeof(type)))
	public static __ptr__ RALLOC(int nb, __ptr__ ptr, Class type) {
		throw new UnsupportedOperationException();
	}

	public static ST_Agnode_s.ArrayOfStar ALLOC_Agnode_s(int nb, ST_Agnode_s.ArrayOfStar old) {
		if (old == null) {
			return new ST_Agnode_s.ArrayOfStar(nb);
		}
		return old.reallocJ(nb);
	}

	public static ST_bezier.Array2 ALLOC_ST_bezier(int nb, ST_bezier.Array2 old) {
		if (old == null) {
			return new ST_bezier.Array2(nb);
		}
		return old.reallocJ(nb);
	}

	public static ST_rank_t.Array2 ALLOC_ST_rank_t(int nb, ST_rank_t.Array2 old) {
		if (old == null) {
			return new ST_rank_t.Array2(nb);
		}
		return old.reallocJ(nb);
	}

	// #define elist_append(item,L) do {L.list = ALLOC(L.size + 2,L.list,edge_t*); L.list[L.size++] = item;
	// L.list[L.size] = NULL;} while(0)
	public static void elist_append(__ptr__ item, ST_elist L) {
		// L.setPtr("list", ALLOC_empty(L.size + 2, L.getPtr("list"), Agedge_s.class));
		L.realloc(L.size + 2);
		L.setInList(L.size, item);
		L.size = 1 + L.size;
		L.setInList(L.size, null);
	}

	// #define alloc_elist(n,L) do {L.size = 0; L.list = N_NEW(n + 1,edge_t*); } while (0)
	public static void alloc_elist(int n, ST_elist L) {
		L.size = 0;
		L.mallocEmpty(n + 1);
		// L.setPtr("list", (__ptr__) JUtils.sizeof_starstar_empty(cl, n + 1).malloc());
	}

	// #define free_list(L) do {if (L.list) free(L.list);} while (0)
	public static void free_list(ST_elist L) {
		if (L.listNotNull())
			L.free();
		// Memory.free(L.getPtr("list"));
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

	public static void MAKEFWDEDGE(ST_Agedge_s new_, ST_Agedge_s old) {
		ST_Agedge_s newp;
		ST_Agedgeinfo_t info;
		newp = (ST_Agedge_s) new_.getPtr();
		info = (ST_Agedgeinfo_t) newp.base.data.castTo(ST_Agedgeinfo_t.class);
		info.copyDataFrom(old.base.data.castTo(ST_Agedgeinfo_t.class).getStruct());
		newp.copyDataFrom((__ptr__)old);
		newp.base.setPtr("data", info);
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

	public static ST_textspan_t.Array ZALLOC_ST_textspan_t(ST_textspan_t.Array old, int size) {
		return old != null ? old.reallocJ(size) : new ST_textspan_t.Array(size);
	}

	public static ST_Agraph_s.Array ZALLOC_ST_Agraph_s(ST_Agraph_s.Array old, int size) {
		return old != null ? old.reallocJ(size) : new ST_Agraph_s.Array(size);
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

	// #define DIST(p,q) (sqrt(DIST2((p),(q))))

	// #define INSIDE(p,b) (BETWEEN((b).LL.x,(p).x,(b).UR.x) && BETWEEN((b).LL.y,(p).y,(b).UR.y))
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
		return (((a).x - (b).x) * ((a).x - (b).x))
				+ (((a).y - (b).y) * ((a).y - (b).y));
	}

	public static void hackInitDimensionFromLabel(ST_pointf size, String label) {
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
			JUtils.LOG2("Hacking dimension to width=" + ww + " height=" + hh);
		}
	}

	public static CString createHackInitDimensionFromLabel(int width, int height) {
		return new CString("_dim_" + width + "_" + height + "_");
	}
}
