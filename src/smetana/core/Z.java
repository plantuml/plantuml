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

import static smetana.core.JUtils.function;
import gen.lib.cdt.dttree__c;
import gen.lib.cgraph.attr__c;
import gen.lib.cgraph.edge__c;
import gen.lib.cgraph.graph__c;
import gen.lib.cgraph.id__c;
import gen.lib.cgraph.mem__c;
import gen.lib.cgraph.node__c;
import gen.lib.cgraph.utils__c;
import gen.lib.common.arrows__c;
import gen.lib.common.shapes__c;
import gen.lib.dotgen.dotsplines__c;
import gen.lib.label.xlabels__c;
import h.ST_Agdesc_s;
import h.ST_Agedge_s;
import h.ST_Agiddisc_s;
import h.ST_Agmemdisc_s;
import h.ST_Agnode_s;
import h.ST_Agraph_s;
import h.ST_Agsubnode_s;
import h.ST_Agsym_s;
import h.ST_Agtag_s;
import h.ST_HDict_t;
import h.ST_Pedge_t;
import h.ST_arrowname_t;
import h.ST_arrowtype_t;
import h.ST_boxf;
import h.ST_deque_t;
import h.ST_dt_s;
import h.ST_dtdisc_s;
import h.ST_dtmethod_s;
import h.ST_elist;
import h.ST_nlist_t;
import h.ST_pointf;
import h.ST_pointnlink_t;
import h.ST_polygon_t;
import h.ST_port;
import h.ST_refstr_t;
import h.ST_shape_desc;
import h.ST_shape_functions;
import h.ST_splineInfo;
import h.ST_textfont_t;
import h.ST_tna_t;
import h.ST_triangle_t;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class Z {

	private static final ThreadLocal<LinkedList<Z>> instances2 = new ThreadLocal<LinkedList<Z>>();

	public final Map<Integer, CString> all = new HashMap<Integer, CString>();

	public final ST_dtmethod_s _Dttree = new ST_dtmethod_s();
	public final ST_dtmethod_s Dttree = _Dttree;

	public final ST_dtmethod_s _Dtobag = new ST_dtmethod_s();
	public final ST_dtmethod_s Dtobag = _Dtobag;

	public final ST_dtdisc_s AgDataDictDisc = new ST_dtdisc_s();

	public final ST_Agdesc_s ProtoDesc = new ST_Agdesc_s();

	public ST_Agraph_s ProtoGraph;

	public final ST_Agtag_s Tag = new ST_Agtag_s();

	public final ST_dtdisc_s Ag_mainedge_seq_disc = new ST_dtdisc_s();

	public final ST_dtdisc_s Ag_subedge_seq_disc = new ST_dtdisc_s();

	public final ST_dtdisc_s Ag_subedge_id_disc = new ST_dtdisc_s();

	public final ST_dtdisc_s Ag_subgraph_id_disc = new ST_dtdisc_s();

	public final ST_Agiddisc_s AgIdDisc = new ST_Agiddisc_s();

	public final ST_Agmemdisc_s AgMemDisc = new ST_Agmemdisc_s();

	public final ST_dtdisc_s Ag_subnode_id_disc = new ST_dtdisc_s();

	public final ST_dtdisc_s Ag_subnode_seq_disc = new ST_dtdisc_s();

	public int HTML_BIT;

	public int CNT_BITS;

	public final ST_dtdisc_s Refstrdisc = new ST_dtdisc_s();
	
	public final ST_dtdisc_s Hdisc = new ST_dtdisc_s();

	public ST_dt_s Refdict_default;

	public ST_Agraph_s Ag_dictop_G;

	public final ST_arrowname_t Arrowsynonyms[] = new ST_arrowname_t[] { new ST_arrowname_t() };

	public final ST_arrowname_t Arrownames[] = new ST_arrowname_t[] { new ST_arrowname_t(), new ST_arrowname_t(), new ST_arrowname_t() };

	public final ST_arrowname_t Arrowmods[] = new ST_arrowname_t[] { new ST_arrowname_t() };

	public final ST_arrowtype_t Arrowtypes[] = new ST_arrowtype_t[] { new ST_arrowtype_t(), new ST_arrowtype_t(), new ST_arrowtype_t(), new ST_arrowtype_t(), new ST_arrowtype_t(), new ST_arrowtype_t(), new ST_arrowtype_t(), new ST_arrowtype_t(), new ST_arrowtype_t() };

	public __ptr__ Show_boxes;

	public int CL_type;

	public boolean Concentrate;

	public int MaxIter;

	public int State;

	public int EdgeLabelsDone;

	public double Initial_dist;

	public ST_Agsym_s G_activepencolor, G_activefillcolor, G_selectedpencolor, G_selectedfillcolor, G_visitedpencolor,
			G_visitedfillcolor, G_deletedpencolor, G_deletedfillcolor, G_ordering, G_peripheries, G_penwidth,
			G_gradientangle, G_margin;

	public ST_Agsym_s N_height, N_width, N_shape, N_color, N_fillcolor, N_activepencolor, N_activefillcolor,
			N_selectedpencolor, N_selectedfillcolor, N_visitedpencolor, N_visitedfillcolor, N_deletedpencolor,
			N_deletedfillcolor, N_fontsize, N_fontname, N_fontcolor, N_margin, N_label, N_xlabel, N_nojustify, N_style,
			N_showboxes, N_sides, N_peripheries, N_ordering, N_orientation, N_skew, N_distortion, N_fixed,
			N_imagescale, N_layer, N_group, N_comment, N_vertices, N_z, N_penwidth, N_gradientangle;

	public ST_Agsym_s E_weight, E_minlen, E_color, E_fillcolor, E_activepencolor, E_activefillcolor, E_selectedpencolor,
			E_selectedfillcolor, E_visitedpencolor, E_visitedfillcolor, E_deletedpencolor, E_deletedfillcolor,
			E_fontsize, E_fontname, E_fontcolor, E_label, E_xlabel, E_dir, E_style, E_decorate, E_showboxes, E_arrowsz,
			E_constr, E_layer, E_comment, E_label_float, E_samehead, E_sametail, E_arrowhead, E_arrowtail, E_headlabel,
			E_taillabel, E_labelfontsize, E_labelfontname, E_labelfontcolor, E_labeldistance, E_labelangle, E_tailclip,
			E_headclip, E_penwidth;

	public int N_nodes, N_edges;

	public int Minrank, Maxrank;

	public int S_i;

	public int Search_size;

	public final ST_nlist_t Tree_node = new ST_nlist_t();

	public final ST_elist Tree_edge = new ST_elist();

	public ST_Agedge_s Enter;

	public int Low, Lim, Slack;

	public int Rankdir;

	public boolean Flip;

	public final ST_pointf Offset = new ST_pointf();

	public int nedges, nboxes;

	public int routeinit;

	public ST_pointf.Array ps;

	public int maxpn;

	public ST_pointf.Array polypoints;

	public int polypointn;

	public ST_Pedge_t.Array edges;

	public int edgen;

	public final ST_boxf[] boxes = ST_boxf.malloc(1000);

	public int MinQuit;

	public double Convergence;

	public ST_Agraph_s Root;

	public int GlobalMinRank, GlobalMaxRank;

	public boolean ReMincross;

	public ST_Agedge_s.ArrayOfStar TE_list;

	public __ptr__ TI_list;

	public ST_Agnode_s Last_node_decomp;
	public ST_Agnode_s Last_node_rank;

	public char Cmark;

	public int trin, tril;

	public ST_triangle_t.Array tris;

	public int pnln, pnll;

	public ST_pointnlink_t pnls[];
	public ST_pointnlink_t pnlps[];

	public final ST_port Center = new ST_port();

	public final ST_polygon_t p_ellipse = new ST_polygon_t();

	public final ST_polygon_t p_box = new ST_polygon_t();

	public final ST_shape_functions poly_fns = new ST_shape_functions();

	public ST_tna_t.Array tnas;
	public int tnan;

	public final ST_shape_desc Shapes[] = { __Shapes__("box", poly_fns, p_box),
			__Shapes__("ellipse", poly_fns, p_ellipse), __Shapes__(null, null, null) };

	public final ST_dtdisc_s Ag_mainedge_id_disc = new ST_dtdisc_s();

	public final ST_deque_t dq = new ST_deque_t();

	public final ST_Agdesc_s Agdirected = new ST_Agdesc_s();

	public final ST_splineInfo sinfo = new ST_splineInfo();

	public ST_Agnode_s lastn; /* last node argument */
	public ST_polygon_t poly;
	public int last, outp, sides;
	public final ST_pointf O = new ST_pointf(); /* point (0,0) */
	public ST_pointf.Array vertex;
	public double xsize, ysize, scalex, scaley, box_URx, box_URy;

	public final ST_textfont_t tf = new ST_textfont_t();

	public ST_pointf.Array pointfs;
	public ST_pointf.Array pointfs2;
	public int numpts;
	public int numpts2;

	public __ptr__ Count;
	public int C;

	public int ctr = 1;

	public final ST_Agsubnode_s template = new ST_Agsubnode_s();
	public final ST_Agnode_s dummy = new ST_Agnode_s();

	public ST_Agraph_s G_ns;
	public ST_Agraph_s G_decomp;

	public int opl;

	public int opn_route;
	public int opn_shortest;

	public ST_pointf.Array ops_route;
	public ST_pointf.Array ops_shortest;

	public static Z z() {
		return instances2.get().peekFirst();
	}

	public static void open() {
		LinkedList<Z> list = instances2.get();
		if (list == null) {
			list = new LinkedList<Z>();
			instances2.set(list);
		}
		list.addFirst(new Z());
	}

	public static void close() {
		instances2.get().removeFirst();
	}

	private ST_shape_desc __Shapes__(String s, ST_shape_functions shape_functions, ST_polygon_t polygon) {
		ST_shape_desc result = new ST_shape_desc();
		result.setPtr("name", s == null ? null : new CString(s));
		result.setPtr("fns", shape_functions);
		result.setPtr("polygon", polygon);
		return result;
	}

	private final static __struct__ createArrowtypes(int type, double lenfact, CFunction function) {
		final ST_arrowtype_t result = new ST_arrowtype_t();
		result.type = type;
		result.lenfact = lenfact;
		result.gen = function;
		return result;
	}

	private final static __struct__ create_arrowname_t(String name, int type) {
		final ST_arrowname_t result = new ST_arrowname_t();
		result.name = name == null ? null : new CString(name);
		result.type = type;
		return result;
	}

	private Z() {

		_Dttree.searchf = function(dttree__c.class, "dttree");
		_Dttree.type = 0000004;

		_Dtobag.searchf = function(dttree__c.class, "dttree");
		_Dtobag.type = 0000010;

		AgDataDictDisc.key = OFFSET.create(ST_Agsym_s.class, "name").toInt();
		AgDataDictDisc.size = -1;
		AgDataDictDisc.link = OFFSET.create(ST_Agsym_s.class, "link").toInt();
		AgDataDictDisc.makef = null;
		AgDataDictDisc.freef = function(attr__c.class, "freesym");
		AgDataDictDisc.comparf = null;
		AgDataDictDisc.hashf = null;

		ProtoDesc.directed = 1;
		ProtoDesc.strict = 0;
		ProtoDesc.no_loop = 1;
		ProtoDesc.maingraph = 0;
		ProtoDesc.flatlock = 1;
		ProtoDesc.no_write = 1;

		Ag_mainedge_seq_disc.key = 0;
		Ag_mainedge_seq_disc.size = 0;
		Ag_mainedge_seq_disc.link = OFFSET.create(ST_Agedge_s.class, "seq_link").toInt(); // seq_link is the third
																						// field in Agedge_t
		Ag_mainedge_seq_disc.makef = null;
		Ag_mainedge_seq_disc.freef = null;
		Ag_mainedge_seq_disc.comparf = function(edge__c.class, "agedgeseqcmpf");
		Ag_mainedge_seq_disc.hashf = null;
		Ag_mainedge_seq_disc.memoryf = function(utils__c.class, "agdictobjmem");
		Ag_mainedge_seq_disc.eventf = null;

		Ag_subedge_seq_disc.key = 0;
		Ag_subedge_seq_disc.size = 0;
		Ag_subedge_seq_disc.link = -1;
		Ag_subedge_seq_disc.makef = null;
		Ag_subedge_seq_disc.freef = null;
		Ag_subedge_seq_disc.comparf = function(edge__c.class, "agedgeseqcmpf");
		Ag_subedge_seq_disc.hashf = null;
		Ag_subedge_seq_disc.memoryf = function(utils__c.class, "agdictobjmem");
		Ag_subedge_seq_disc.eventf = null;

		Ag_subedge_id_disc.key = 0;
		Ag_subedge_id_disc.size = 0;
		Ag_subedge_id_disc.link = -1;
		Ag_subedge_id_disc.makef = null;
		Ag_subedge_id_disc.freef = null;
		Ag_subedge_id_disc.comparf = function(edge__c.class, "agedgeidcmpf");
		Ag_subedge_id_disc.hashf = null;
		Ag_subedge_id_disc.memoryf = function(utils__c.class, "agdictobjmem");
		Ag_subedge_id_disc.eventf = null;

		Ag_subgraph_id_disc.key = 0;
		Ag_subgraph_id_disc.size = 0;
		Ag_subgraph_id_disc.link = OFFSET.create(ST_Agraph_s.class, "link").toInt(); // link is the third field in
																					// Agraph_t
		Ag_subgraph_id_disc.makef = null;
		Ag_subgraph_id_disc.freef = null;
		Ag_subgraph_id_disc.comparf = function(graph__c.class, "agraphidcmpf");
		Ag_subgraph_id_disc.hashf = null;
		Ag_subgraph_id_disc.memoryf = function(utils__c.class, "agdictobjmem");
		Ag_subgraph_id_disc.eventf = null;

		AgIdDisc.open = function(id__c.class, "idopen");
		AgIdDisc.map = function(id__c.class, "idmap");
		AgIdDisc.alloc = function(id__c.class, "idalloc");
		AgIdDisc.free = function(id__c.class, "idfree");
		AgIdDisc.print = function(id__c.class, "idprint");
		AgIdDisc.close = function(id__c.class, "idclose");
		AgIdDisc.idregister = function(id__c.class, "idregister");

		AgMemDisc.open = function(mem__c.class, "memopen");
		AgMemDisc.alloc = function(mem__c.class, "memalloc");
		AgMemDisc.resize = function(mem__c.class, "memresize");
		AgMemDisc.free = function(mem__c.class, "memfree");
		AgMemDisc.close = null;

		Ag_subnode_id_disc.key = 0;
		Ag_subnode_id_disc.size = 0;
		Ag_subnode_id_disc.link = OFFSET.create(ST_Agsubnode_s.class, "id_link").toInt(); // id_link is the second
																						// field in Agsubnode_t
		Ag_subnode_id_disc.makef = null;
		Ag_subnode_id_disc.freef = null;
		Ag_subnode_id_disc.comparf = function(node__c.class, "agsubnodeidcmpf");
		Ag_subnode_id_disc.hashf = null;
		Ag_subnode_id_disc.memoryf = function(utils__c.class, "agdictobjmem");
		Ag_subnode_id_disc.eventf = null;

		Ag_subnode_seq_disc.key = 0;
		Ag_subnode_seq_disc.size = 0;
		Ag_subnode_seq_disc.link = OFFSET.create(ST_Agsubnode_s.class, "seq_link").toInt(); // link is the first
																							// field in
																							// Agsubnode_t
		Ag_subnode_seq_disc.makef = null;
		Ag_subnode_seq_disc.freef = function(node__c.class, "free_subnode");
		Ag_subnode_seq_disc.comparf = function(node__c.class, "agsubnodeseqcmpf");
		Ag_subnode_seq_disc.hashf = null;
		Ag_subnode_seq_disc.memoryf = function(utils__c.class, "agdictobjmem");
		Ag_subnode_seq_disc.eventf = null;

		Refstrdisc.key = OFFSET.create(ST_refstr_t.class, "s").toInt(); // *s is the third field in refstr_t
		Refstrdisc.size = -1;
		Refstrdisc.link = 0;
		Refstrdisc.makef = null;
		Refstrdisc.freef = function(utils__c.class, "agdictobjfree");
		Refstrdisc.comparf = null;
		Refstrdisc.hashf = null;
		Refstrdisc.memoryf = function(utils__c.class, "agdictobjmem");
		Refstrdisc.eventf = null;

		Hdisc.key = OFFSET.create(ST_HDict_t.class, "key").toInt();
		Hdisc.size = 4;
		Hdisc.link = -1;
		Hdisc.makef = null;
		Hdisc.freef = null;
		Hdisc.comparf = function(xlabels__c.class, "icompare");
		Hdisc.hashf = null;
		Hdisc.memoryf = null;
		Hdisc.eventf = null;

		Arrowsynonyms[0].___(create_arrowname_t(null, 0));

		Arrownames[0].___(create_arrowname_t("normal", 1));
		Arrownames[1].___(create_arrowname_t("none", 8));
		Arrownames[2].___(create_arrowname_t(null, 0));

		Arrowmods[0].___(create_arrowname_t(null, 0));

		Arrowtypes[0].___(createArrowtypes(1, 1.0, function(arrows__c.class, "arrow_type_normal")));
		Arrowtypes[1].___(createArrowtypes(2, 1.0, function(arrows__c.class, "arrow_type_crow")));
		Arrowtypes[2].___(createArrowtypes(3, 0.5, function(arrows__c.class, "arrow_type_tee")));
		Arrowtypes[3].___(createArrowtypes(4, 1.0, function(arrows__c.class, "arrow_type_box")));
		Arrowtypes[4].___(createArrowtypes(5, 1.2, function(arrows__c.class, "arrow_type_diamond")));
		Arrowtypes[5].___(createArrowtypes(6, 0.8, function(arrows__c.class, "arrow_type_dot")));
		Arrowtypes[6].___(createArrowtypes(7, 1.0, function(arrows__c.class, "arrow_type_curve")));
		Arrowtypes[7].___(createArrowtypes(8, 0.5, function(arrows__c.class, "arrow_type_gap")));
		Arrowtypes[8].___(createArrowtypes(0, 0.0, null));

		Center.p.x = 0;
		Center.p.y = 0;
		Center.theta = -1;
		Center.bp = null;
		Center.defined = 0;
		Center.constrained = 0;
		Center.clip = 1;
		Center.dyna = 0;
		Center.order = 0;
		Center.side = 0;

		p_ellipse.setInt("regular", 0);
		p_ellipse.setInt("peripheries", 1);
		p_ellipse.setInt("sides", 1);
		p_ellipse.setDouble("orientation", 0.);
		p_ellipse.setDouble("distortion", 0.);
		p_ellipse.setDouble("skew", 0.);

		p_box.setInt("regular", 0);
		p_box.setInt("peripheries", 1);
		p_box.setInt("sides", 4);
		p_box.setDouble("orientation", 0.);
		p_box.setDouble("distortion", 0.);
		p_box.setDouble("skew", 0.);

		poly_fns.setPtr("initfn", function(shapes__c.class, "poly_init"));
		poly_fns.setPtr("freefn", function(shapes__c.class, "poly_free"));
		poly_fns.setPtr("portfn", function(shapes__c.class, "poly_port"));
		poly_fns.setPtr("insidefn", function(shapes__c.class, "poly_inside"));
		poly_fns.setPtr("pboxfn", function(shapes__c.class, "poly_path"));
		poly_fns.setPtr("codefn", function(shapes__c.class, "poly_gencode"));

		Ag_mainedge_id_disc.key = 0;
		Ag_mainedge_id_disc.size = 0;
		Ag_mainedge_id_disc.link = OFFSET.create(ST_Agedge_s.class, "id_link").toInt(); // id_link is the second
																						// field in Agedge_t
		Ag_mainedge_id_disc.makef = null;
		Ag_mainedge_id_disc.freef = null;
		Ag_mainedge_id_disc.comparf = function(edge__c.class, "agedgeidcmpf");
		Ag_mainedge_id_disc.hashf = null;
		Ag_mainedge_id_disc.memoryf = function(utils__c.class, "agdictobjmem");
		Ag_mainedge_id_disc.eventf = null;

		Agdirected.directed = 1;
		Agdirected.strict = 0;
		Agdirected.no_loop = 0;
		Agdirected.maingraph = 1;

		sinfo.setPtr("swapEnds", function(dotsplines__c.class, "swap_ends_p"));
		sinfo.setPtr("splineMerge", function(dotsplines__c.class, "spline_merge"));

	}

}
