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

import static smetana.core.Macro.ARR_TYPE_BOX;
import static smetana.core.Macro.ARR_TYPE_CROW;
import static smetana.core.Macro.ARR_TYPE_CURVE;
import static smetana.core.Macro.ARR_TYPE_DIAMOND;
import static smetana.core.Macro.ARR_TYPE_DOT;
import static smetana.core.Macro.ARR_TYPE_GAP;
import static smetana.core.Macro.ARR_TYPE_NORM;
import static smetana.core.Macro.ARR_TYPE_TEE;

import java.util.HashMap;
import java.util.Map;

import com.plantuml.api.cheerpj.WasmLog;

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
import h.ST_shape_desc;
import h.ST_shape_functions;
import h.ST_splineInfo;
import h.ST_textfont_t;
import h.ST_tna_t;
import h.ST_triangle_t;

final public class Globals {

	public static Globals open() {
		WasmLog.log("Starting smetana instance");
		return new Globals();
	}

	public static void close() {
		WasmLog.log("Ending smetana instance");
	}

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

	public final ST_arrowname_t Arrowsynonyms[] = new ST_arrowname_t[] { create_arrowname_t(null, 0) };

	public final ST_arrowname_t Arrownames[] = new ST_arrowname_t[] { create_arrowname_t("normal", ARR_TYPE_NORM),
			create_arrowname_t("none", ARR_TYPE_GAP), create_arrowname_t(null, 0) };

	public final ST_arrowname_t Arrowmods[] = new ST_arrowname_t[] { create_arrowname_t(null, 0) };

	public final ST_arrowtype_t Arrowtypes[] = new ST_arrowtype_t[] {
			createArrowtypes(ARR_TYPE_NORM, 1.0, arrows__c.arrow_type_normal),
			createArrowtypes(ARR_TYPE_CROW, 1.0, arrows__c.arrow_type_crow),
			createArrowtypes(ARR_TYPE_TEE, 0.5, arrows__c.arrow_type_tee),
			createArrowtypes(ARR_TYPE_BOX, 1.0, arrows__c.arrow_type_box),
			createArrowtypes(ARR_TYPE_DIAMOND, 1.2, arrows__c.arrow_type_diamond),
			createArrowtypes(ARR_TYPE_DOT, 0.8, arrows__c.arrow_type_dot),
			createArrowtypes(ARR_TYPE_CURVE, 1.0, arrows__c.arrow_type_curve),
			createArrowtypes(ARR_TYPE_GAP, 0.5, arrows__c.arrow_type_gap), createArrowtypes(0, 0.0, null) };

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
			N_showboxes, N_sides, N_peripheries, N_ordering, N_orientation, N_skew, N_distortion, N_fixed, N_imagescale,
			N_layer, N_group, N_comment, N_vertices, N_z, N_penwidth, N_gradientangle;

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

	public CArray<ST_pointf> ps;

	public int maxpn;

	public CArray<ST_pointf> polypoints;

	public int polypointn;

	public CArray<ST_Pedge_t> edges;

	public int edgen;

	public final ST_boxf[] boxes = ST_boxf.malloc(1000);

	public int MinQuit;

	public double Convergence;

	public ST_Agraph_s Root;

	public int GlobalMinRank, GlobalMaxRank;

	public boolean ReMincross;

	public CArrayOfStar<ST_Agedge_s> TE_list;

	public int TI_list[];

	public ST_Agnode_s Last_node_decomp;
	public ST_Agnode_s Last_node_rank;

	public char Cmark;

	public int trin, tril;

	public CArray<ST_triangle_t> tris;

	public int pnln, pnll;

	public ST_pointnlink_t pnls[];
	public ST_pointnlink_t pnlps[];

	public final ST_port Center = new ST_port();

	public final ST_polygon_t p_ellipse = new ST_polygon_t();

	public final ST_polygon_t p_box = new ST_polygon_t();

	public final ST_shape_functions poly_fns = new ST_shape_functions();

	public final ST_shape_functions record_fns = new ST_shape_functions();

	public CArray<ST_tna_t> tnas;
	public int tnan;

	public final ST_shape_desc Shapes[] = { __Shapes__("box", poly_fns, p_box),
			__Shapes__("ellipse", poly_fns, p_ellipse), __Shapes__("record", record_fns, null),
			__Shapes__(null, null, null) };

	public final ST_dtdisc_s Ag_mainedge_id_disc = new ST_dtdisc_s();

	public final ST_deque_t dq = new ST_deque_t();

	public final ST_Agdesc_s Agdirected = new ST_Agdesc_s();

	public final ST_splineInfo sinfo = new ST_splineInfo();

	public ST_Agnode_s lastn; /* last node argument */
	public ST_polygon_t poly;
	public int last, outp, sides;
	public final ST_pointf O = new ST_pointf(); /* point (0,0) */
	public CArray<ST_pointf> vertex;
	public double xsize, ysize, scalex, scaley, box_URx, box_URy;

	public final ST_textfont_t tf = new ST_textfont_t();

	public CArray<ST_pointf> pointfs;
	public CArray<ST_pointf> pointfs2;
	public int numpts;
	public int numpts2;

	public int[] Count;
	public int C;

	public int ctr = 1;

	public final ST_Agsubnode_s template = new ST_Agsubnode_s();
	public final ST_Agnode_s dummy = new ST_Agnode_s();

	public ST_Agraph_s G_ns;
	public ST_Agraph_s G_decomp;

	public int opl;

	public int opn_route;
	public int opn_shortest;

	public CArray<ST_pointf> ops_route;
	public CArray<ST_pointf> ops_shortest;

	public CString reclblp;

	public int isz;
	public CArray<ST_pointf> ispline;

	private ST_shape_desc __Shapes__(String s, ST_shape_functions shape_functions, ST_polygon_t polygon) {
		ST_shape_desc result = new ST_shape_desc();
		result.name = s == null ? null : new CString(s);
		result.fns = shape_functions;
		result.polygon = polygon;
		return result;
	}

	private final static ST_arrowtype_t createArrowtypes(int type, double lenfact, CFunction function) {
		final ST_arrowtype_t result = new ST_arrowtype_t();
		result.type = type;
		result.lenfact = lenfact;
		result.gen = function;
		return result;
	}

	private final static ST_arrowname_t create_arrowname_t(String name, int type) {
		final ST_arrowname_t result = new ST_arrowname_t();
		result.name = name == null ? null : new CString(name);
		result.type = type;
		return result;
	}

	private Globals() {

		_Dttree.searchf = dttree__c.dttree;
		_Dttree.type = Macro.DT_OSET;

		_Dtobag.searchf = dttree__c.dttree;
		_Dtobag.type = Macro.DT_OBAG;

		AgDataDictDisc.key = FieldOffset.name;
		AgDataDictDisc.size = -1;
		AgDataDictDisc.link = FieldOffset.link;
		AgDataDictDisc.makef = null;
		AgDataDictDisc.freef = attr__c.freesym;
		AgDataDictDisc.comparf = null;
		AgDataDictDisc.hashf = null;

		ProtoDesc.directed = 1;
		ProtoDesc.strict = 0;
		ProtoDesc.no_loop = 1;
		ProtoDesc.maingraph = 0;
		ProtoDesc.flatlock = 1;
		ProtoDesc.no_write = 1;

		Ag_mainedge_seq_disc.key = FieldOffset.zero;
		Ag_mainedge_seq_disc.size = 0;
		Ag_mainedge_seq_disc.link = FieldOffset.seq_link; // seq_link is the third
															// field in Agedge_t
		Ag_mainedge_seq_disc.makef = null;
		Ag_mainedge_seq_disc.freef = null;
		Ag_mainedge_seq_disc.comparf = edge__c.agedgeseqcmpf;
		Ag_mainedge_seq_disc.hashf = null;
		Ag_mainedge_seq_disc.memoryf = utils__c.agdictobjmem;
		Ag_mainedge_seq_disc.eventf = null;

		Ag_subedge_seq_disc.key = FieldOffset.zero;
		Ag_subedge_seq_disc.size = 0;
		Ag_subedge_seq_disc.link = FieldOffset.externalHolder;
		Ag_subedge_seq_disc.makef = null;
		Ag_subedge_seq_disc.freef = null;
		Ag_subedge_seq_disc.comparf = edge__c.agedgeseqcmpf;
		Ag_subedge_seq_disc.hashf = null;
		Ag_subedge_seq_disc.memoryf = utils__c.agdictobjmem;
		Ag_subedge_seq_disc.eventf = null;

		Ag_subedge_id_disc.key = FieldOffset.zero;
		Ag_subedge_id_disc.size = 0;
		Ag_subedge_id_disc.link = FieldOffset.externalHolder;
		Ag_subedge_id_disc.makef = null;
		Ag_subedge_id_disc.freef = null;
		Ag_subedge_id_disc.comparf = edge__c.agedgeidcmpf;
		Ag_subedge_id_disc.hashf = null;
		Ag_subedge_id_disc.memoryf = utils__c.agdictobjmem;
		Ag_subedge_id_disc.eventf = null;

		Ag_subgraph_id_disc.key = FieldOffset.zero;
		Ag_subgraph_id_disc.size = 0;
		Ag_subgraph_id_disc.link = FieldOffset.link; // link is the third field in
														// Agraph_t
		Ag_subgraph_id_disc.makef = null;
		Ag_subgraph_id_disc.freef = null;
		Ag_subgraph_id_disc.comparf = graph__c.agraphidcmpf;
		Ag_subgraph_id_disc.hashf = null;
		Ag_subgraph_id_disc.memoryf = utils__c.agdictobjmem;
		Ag_subgraph_id_disc.eventf = null;

		AgIdDisc.open = id__c.idopen;
		AgIdDisc.map = id__c.idmap;
		AgIdDisc.alloc = id__c.idalloc;
		AgIdDisc.free = id__c.idfree;
		AgIdDisc.print = id__c.idprint;
		AgIdDisc.close = id__c.idclose;
		AgIdDisc.idregister = id__c.idregister;

		AgMemDisc.open = mem__c.memopen;
		AgMemDisc.alloc = mem__c.memalloc;
		AgMemDisc.resize = mem__c.memresize;
		AgMemDisc.free = mem__c.memfree;
		AgMemDisc.close = null;

		Ag_subnode_id_disc.key = FieldOffset.zero;
		Ag_subnode_id_disc.size = 0;
		Ag_subnode_id_disc.link = FieldOffset.id_link; // id_link is the second
															// field in Agsubnode_t
		Ag_subnode_id_disc.makef = null;
		Ag_subnode_id_disc.freef = null;
		Ag_subnode_id_disc.comparf = node__c.agsubnodeidcmpf;
		Ag_subnode_id_disc.hashf = null;
		Ag_subnode_id_disc.memoryf = utils__c.agdictobjmem;
		Ag_subnode_id_disc.eventf = null;

		Ag_subnode_seq_disc.key = FieldOffset.zero;
		Ag_subnode_seq_disc.size = 0;
		Ag_subnode_seq_disc.link = FieldOffset.seq_link; // link is the first
															// field in
															// Agsubnode_t
		Ag_subnode_seq_disc.makef = null;
		Ag_subnode_seq_disc.freef = node__c.free_subnode;
		Ag_subnode_seq_disc.comparf = node__c.agsubnodeseqcmpf;
		Ag_subnode_seq_disc.hashf = null;
		Ag_subnode_seq_disc.memoryf = utils__c.agdictobjmem;
		Ag_subnode_seq_disc.eventf = null;

		Refstrdisc.key = FieldOffset.s; // *s is the third field in refstr_t
		Refstrdisc.size = -1;
		Refstrdisc.link = FieldOffset.zero;
		Refstrdisc.makef = null;
		Refstrdisc.freef = utils__c.agdictobjfree;
		Refstrdisc.comparf = null;
		Refstrdisc.hashf = null;
		Refstrdisc.memoryf = utils__c.agdictobjmem;
		Refstrdisc.eventf = null;

		Hdisc.key = FieldOffset.key;
		Hdisc.size = 4;
		Hdisc.link = FieldOffset.externalHolder;
		Hdisc.makef = null;
		Hdisc.freef = null;
		Hdisc.comparf = xlabels__c.icompare;
		Hdisc.hashf = null;
		Hdisc.memoryf = null;
		Hdisc.eventf = null;

		Center.p.x = 0;
		Center.p.y = 0;
		Center.theta = -1;
		Center.bp = null;
		Center.defined = false;
		Center.constrained = false;
		Center.clip = true;
		Center.dyna = false;
		Center.order = 0;
		Center.side = 0;

		p_ellipse.regular = false;
		p_ellipse.peripheries = 1;
		p_ellipse.sides = 1;
		p_ellipse.orientation = 0.;
		p_ellipse.distortion = 0.;
		p_ellipse.skew = 0.;

		p_box.regular = false;
		p_box.peripheries = 1;
		p_box.sides = 4;
		p_box.orientation = 0.;
		p_box.distortion = 0.;
		p_box.skew = 0.;

		poly_fns.initfn = shapes__c.poly_init;
		poly_fns.freefn = shapes__c.poly_free;
		poly_fns.portfn = shapes__c.poly_port;
		poly_fns.insidefn = shapes__c.poly_inside;
		poly_fns.pboxfn = shapes__c.poly_path;
		poly_fns.codefn = shapes__c.poly_gencode;

		record_fns.initfn = shapes__c.record_init;
		record_fns.freefn = shapes__c.record_free;
		record_fns.portfn = shapes__c.record_port;
		record_fns.insidefn = shapes__c.record_inside;
		record_fns.pboxfn = shapes__c.record_path;
		record_fns.codefn = shapes__c.record_gencode;

		Ag_mainedge_id_disc.key = FieldOffset.zero;
		Ag_mainedge_id_disc.size = 0;
		Ag_mainedge_id_disc.link = FieldOffset.id_link; // id_link is the second
															// field in Agedge_t
		Ag_mainedge_id_disc.makef = null;
		Ag_mainedge_id_disc.freef = null;
		Ag_mainedge_id_disc.comparf = edge__c.agedgeidcmpf;
		Ag_mainedge_id_disc.hashf = null;
		Ag_mainedge_id_disc.memoryf = utils__c.agdictobjmem;
		Ag_mainedge_id_disc.eventf = null;

		Agdirected.directed = 1;
		Agdirected.strict = 0;
		Agdirected.no_loop = 0;
		Agdirected.maingraph = 1;

		sinfo.swapEnds = dotsplines__c.swap_ends_p;
		sinfo.splineMerge = dotsplines__c.spline_merge;

		ispline = null;
		isz = 0;

	}

}
