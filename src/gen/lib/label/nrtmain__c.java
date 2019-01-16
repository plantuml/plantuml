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
import static smetana.core.Macro.UNSUPPORTED;
import h.ST_pointf;

public class nrtmain__c {
//1 2digov3edok6d5srhgtlmrycs
// extern lt_symlist_t lt_preloaded_symbols[]


//1 baedz5i9est5csw3epz3cv7z
// typedef Ppoly_t Ppolyline_t


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


//1 ez7q2nzpr17flkmb0odk67ggx
// static char *progname


//1 4k7cnypsvtgywml89uxukug5r
// static int Verbose




//3 c1s4k85p1cdfn176o3uryeros
// static inline pointf pointfof(double x, double y) 
public static ST_pointf pointfof(double x, double y) {
// WARNING!! STRUCT
return pointfof_w_(x, y).copy();
}
private static ST_pointf pointfof_w_(double x, double y) {
ENTERING("c1s4k85p1cdfn176o3uryeros","pointfof");
try {
    final ST_pointf r = new ST_pointf();
    r.setDouble("x", x);
    r.setDouble("y", y);
    return r;
} finally {
LEAVING("c1s4k85p1cdfn176o3uryeros","pointfof");
}
}




//3 2i713kmewjct2igf3lwm80462
// static pointf centerPt(xlabel_t * xlp) 
public static Object centerPt(Object... arg) {
UNSUPPORTED("2ehkj7fn7wtb77mg9wdqpdig1"); // static pointf centerPt(xlabel_t * xlp)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("2bghyit203pd6xw2ihhenzyn8"); //     pointf p;
UNSUPPORTED("1zfb5l8l1ajr1qwg3mcy71mrm"); //     p = xlp->pos;
UNSUPPORTED("8hxf8i35knrbncbh2vfaidbi7"); //     p.x += (xlp->sz.x) / 2.0;
UNSUPPORTED("4pemnun9akazartmat9b8062r"); //     p.y += (xlp->sz.y) / 2.0;
UNSUPPORTED("91xduilalb406jjyw2g1i07th"); //     return p;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 axo7jzudoju3r02pbuzu5u5cg
// static int printData(object_t * objs, int n_objs, xlabel_t * lbls, int n_lbls, 	  label_params_t * params) 
public static Object printData(Object... arg) {
UNSUPPORTED("eyp5xkiyummcoc88ul2b6tkeg"); // static int
UNSUPPORTED("1ts7sby2d9t84a9e3n3h9z7i4"); // printData(object_t * objs, int n_objs, xlabel_t * lbls, int n_lbls,
UNSUPPORTED("b2402200eyc80fno3tkigspyc"); // 	  label_params_t * params)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("b17di9c7wgtqm51bvsyxz6e2f"); //     int i;
UNSUPPORTED("2a3rymfsfswadknge9x92kumi"); //     fprintf(stderr,
UNSUPPORTED("9ffvrcx1kz69kf4ohq458tntr"); // 	    "%d objs %d xlabels force=%d bb=(%.02f,%.02f) (%.02f,%.02f)\n",
UNSUPPORTED("8lkiqntjtlrl6e91jrj1crmy7"); // 	    n_objs, n_lbls, params->force, params->bb.LL.x,
UNSUPPORTED("atpr27z058nxgtq1injs7j3xx"); // 	    params->bb.LL.y, params->bb.UR.x, params->bb.UR.y);
UNSUPPORTED("8rqgsy04sx7rq9dr227gsfdny"); //     if (Verbose < 2)
UNSUPPORTED("c9ckhc8veujmwcw0ar3u3zld4"); // 	return 0;
UNSUPPORTED("2646yqgzt1fr9gq4thnuvqw62"); //     fprintf(stderr, "objects\n");
UNSUPPORTED("bznihqrwh4167vo2mn4du53da"); //     for (i = 0; i < n_objs; i++) {
UNSUPPORTED("3h9on00kwjf4xo4j60tbhbd0d"); //       if(objs[i].lbl && objs[i].lbl->lbl)
UNSUPPORTED("2p5wg8zjn154ih218upte0m8u"); // 	fprintf (stderr, " [%d] %p %p (%.02f, %.02f) (%.02f, %.02f) %s\n",
UNSUPPORTED("3nrlvh61ecaah41rdce7opuz6"); // 		 i, &objs[i], objs[i].lbl, objs[i].pos.x,objs[i].pos.y,
UNSUPPORTED("38c5nmfxdrj8anxj2tvsuzwyd"); // 		 objs[i].sz.x,objs[i].sz.y,
UNSUPPORTED("5010od1avt4gufwitovfspv53"); // 		 ((textlabel_t*)objs[i].lbl->lbl)->text );  
UNSUPPORTED("e2koj2xocq76awegpydpyu62m"); //       else
UNSUPPORTED("aoaa9ojdu28j43q5olg7ipirz"); // 	fprintf (stderr, " [%d] %p %p (%.02f, %.02f) (%.02f, %.02f)\n",
UNSUPPORTED("3nrlvh61ecaah41rdce7opuz6"); // 		 i, &objs[i], objs[i].lbl, objs[i].pos.x,objs[i].pos.y,
UNSUPPORTED("enm5yxjvcqabshcso24gtrc2e"); // 		 objs[i].sz.x,objs[i].sz.y);  
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("5qiw0ng2ayzadjgb1k8dym0y7"); //     fprintf(stderr, "xlabels\n");
UNSUPPORTED("30yvif5t111f94y1fs2gd8crq"); //     for (i = 0; i < n_lbls; i++) {
UNSUPPORTED("5cpl2wq6ypkvhfql4dd7c82s3"); // 	fprintf(stderr, " [%d] %p (%.02f, %.02f) (%.02f, %.02f) %s\n",
UNSUPPORTED("8qph05eaq6mqxunl6b71h8x2g"); // 		i, &lbls[i], lbls[i].pos.x, lbls[i].pos.y,
UNSUPPORTED("4cl6jk5xeitdm8kz9zfyea3zm"); // 		lbls[i].sz.x, lbls[i].sz.y,
UNSUPPORTED("3vd0l036v68y2ae4kl150n8yp"); // 		((textlabel_t *)lbls[i].lbl)->text);
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("5oxhd3fvp0gfmrmz12vndnjt"); //     return 0;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 c4cg32mzgkpbcst0jy2upq5w3
// int doxlabel(opts_t * opts) 
public static Object doxlabel(Object... arg) {
UNSUPPORTED("8wurvokwz90fe88uipw31lwep"); // int doxlabel(opts_t * opts)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("3l5bdi04ekui6vvrgr3s666fx"); //     Agraph_t *gp;
UNSUPPORTED("1q5pw5siho05tilkhpvceuax5"); //     object_t *objs;
UNSUPPORTED("88sdp733wy18rctixyqpiarvo"); //     xlabel_t *lbls;
UNSUPPORTED("aq6jdvqcw04pxk55fs8ppod2c"); //     int i, n_objs, n_lbls;
UNSUPPORTED("asd06xewa83y0cc930zzf5d25"); //     label_params_t params;
UNSUPPORTED("4dnqwjhhs92m1h2q7ld3cpbxw"); //     Agnode_t *np;
UNSUPPORTED("1bl759aws53yjk3xg9shiogx5"); //     Agedge_t *ep;
UNSUPPORTED("2wem1nsm7z6vxqjo9edb38fpu"); //     int n_nlbls = 0, n_elbls = 0;
UNSUPPORTED("2lzsl1e035wt5epd1h8f4bn8m"); //     boxf bb;
UNSUPPORTED("cqwl7s9yvzr8n5v8svuuv1a1q"); //     textlabel_t *lp;
UNSUPPORTED("1exmnagt3khrhapqoj43kchc9"); //     object_t *objp;
UNSUPPORTED("42yb201vo7e3ryt0pg3pupi84"); //     xlabel_t *xlp;
UNSUPPORTED("1ilrhzyqh05f2u3j3vzg0ys8u"); //     pointf ur;
UNSUPPORTED("2ntp2ibjekwh9j8nygd0ti1xm"); //     fprintf(stderr, "reading %s\n", opts->infname);
UNSUPPORTED("b7tc1qd0ygab8ky9f8mq6yr1r"); //     if (!(gp = agread(opts->inf))) {
UNSUPPORTED("2z171ve8godqlic0qmbasth1e"); // 	fprintf(stderr, "%s: %s not a dot file\n", progname,
UNSUPPORTED("6oq4a7osqwufq0eszpp57ktu6"); // 		opts->infname);
UNSUPPORTED("dtw3cma0ziyha0w534bszl0tx"); // 	exit(1);
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("beo8iaf6acg59u8is886fmt2x"); //     fclose(opts->inf);
UNSUPPORTED("9s7txbykwaiwj5rv1xjdpwh4y"); //     fprintf(stderr, "laying out %s\n", opts->lay);
UNSUPPORTED("aojth6x8xid0d88cuvl2wn8nf"); //     if (gvLayout(opts->gvc, gp, opts->lay)) {
UNSUPPORTED("e1ce3rd38aij0uzxsx09fu5kp"); // 	fprintf(stderr, "%s: layout %s failed\n", progname, opts->lay);
UNSUPPORTED("dtw3cma0ziyha0w534bszl0tx"); // 	exit(1);
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("8s81ox08m1abqhigygxh81299"); //     fprintf(stderr, "attach labels\n");
UNSUPPORTED("6cchlbw0dymzb3cw1hfo94n6a"); //     /* In the real code, this should be optimized using GD_has_labels() */
UNSUPPORTED("bfdixk0pjg05sljut4h57xbbh"); //     /* We could probably provide the number of node and edge xlabels */
UNSUPPORTED("27ppdplfezcqw6rdrkzyrr8yg"); //     for (np = agfstnode(gp); np; np = agnxtnode(gp, np)) {
UNSUPPORTED("cnz7ckum0lz95m8c8e6i1vok7"); // 	if (ND_xlabel(np))
UNSUPPORTED("3uhi0myy92p6xl4a5oxde7v9z"); // 	    n_nlbls++;
UNSUPPORTED("41dpbku41lh3gpb30ds9ex6aa"); // 	for (ep = agfstout(gp, np); ep; ep = agnxtout(gp, ep)) {
UNSUPPORTED("biq91l2ud7l5kr26ls9zbzzke"); // 	    if (ED_xlabel(ep))
UNSUPPORTED("bffseaexubrngpi9o059qepow"); // 		n_elbls++;
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("5ft4bvs2l0ce26wb6gnuzjao7"); //     n_objs = agnnodes(gp) + n_elbls;
UNSUPPORTED("5kla6rwar4q1ig2olguocajn6"); //     n_lbls = n_nlbls + n_elbls;
UNSUPPORTED("3e10kvi719e134x675xgwi1tj"); //     objp = objs = (object_t*)zmalloc((n_objs)*sizeof(object_t));
UNSUPPORTED("6z86cmeenod2nx8ej72n0qotk"); //     xlp = lbls = (xlabel_t*)zmalloc((n_lbls)*sizeof(xlabel_t));
UNSUPPORTED("f2nzvg1xnr11v28w2feg923cs"); //     bb.LL = pointfof(INT_MAX, INT_MAX);
UNSUPPORTED("7tttoj8cnxfqgnq2aagnnav48"); //     bb.UR = pointfof(-INT_MAX, -INT_MAX);
UNSUPPORTED("27ppdplfezcqw6rdrkzyrr8yg"); //     for (np = agfstnode(gp); np; np = agnxtnode(gp, np)) {
UNSUPPORTED("4siyxwjbs44obgm7h82zjml69"); // 	/* Add an obstacle per node */
UNSUPPORTED("6cuxjl9g4nxwyz58c201qdb94"); // 	objp->sz.x = ((ND_width(np))*(double)72);
UNSUPPORTED("e3zk2j9kbexxv2xbsgu3pser6"); // 	objp->sz.y = ((ND_height(np))*(double)72);
UNSUPPORTED("cbc8atcbxsu44juz82lkfcgwq"); // 	objp->pos = ND_coord(np);
UNSUPPORTED("3zy29s8ijfmwn910twoywpps1"); // 	objp->pos.x -= (objp->sz.x) / 2.0;
UNSUPPORTED("7jkvg5hvdxus7k3bv57jyjh88"); // 	objp->pos.y -= (objp->sz.y) / 2.0;
UNSUPPORTED("7jndn79f10gyxumgbo6czxe5m"); // 	/* Adjust bounding box */
UNSUPPORTED("bsgbwiykz32eqdyc49dobwpnr"); // 	bb.LL.x = MIN(bb.LL.x, objp->pos.x);
UNSUPPORTED("2u94r8rlczbki3ge3ihvmo9mi"); // 	bb.LL.y = MIN(bb.LL.y, objp->pos.y);
UNSUPPORTED("4lusjec057jj66bscit5s7lft"); // 	ur.x = objp->pos.x + objp->sz.x;
UNSUPPORTED("9zdffh9aglmrm8cdsmyxb0wrw"); // 	ur.y = objp->pos.y + objp->sz.y;
UNSUPPORTED("crkzyggmy41k3nrp8csmiakes"); // 	bb.UR.x = MAX(bb.UR.x, ur.x);
UNSUPPORTED("5ejpea5qlp1pbghy6n2lsuwfs"); // 	bb.UR.y = MAX(bb.UR.y, ur.y);
UNSUPPORTED("eezvruvdh9ueqsgad8k5xzbqi"); // 	if (ND_xlabel(np)) {
UNSUPPORTED("dq9r3oew49g4p62l8mj95y1dh"); // 	    xlp->sz = ND_xlabel(np)->dimen;
UNSUPPORTED("7xq75ru9hsux949swpm3cp4ue"); // 	    xlp->lbl = ND_xlabel(np);
UNSUPPORTED("lch95gg7hal8p4g2coq8qya9"); // 	    xlp->set = 0;
UNSUPPORTED("9ozlxoe5hy2ssm40umphbfgxe"); // 	    objp->lbl = xlp;
UNSUPPORTED("ddik10sdd1c59te8kbgpqsncp"); // 	    xlp++;
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("bhbvoj12subdn6905juhiubo2"); // 	objp++;
UNSUPPORTED("41dpbku41lh3gpb30ds9ex6aa"); // 	for (ep = agfstout(gp, np); ep; ep = agnxtout(gp, ep)) {
UNSUPPORTED("92f340ohb0u21xl6jgpc2hieo"); // 	    if (ED_label(ep)) {
UNSUPPORTED("bbidnvg3rfydq4lbw358qvlik"); // 		textlabel_t *lp = ED_label(ep);
UNSUPPORTED("dow8hf9884ywwrt8hgipox54g"); // 		lp->pos.x = lp->pos.y = 0.0;
UNSUPPORTED("6t98dcecgbvbvtpycwiq2ynnj"); // 	    }
UNSUPPORTED("58ej5pyvcqel713ci83qh7j2n"); // 	    if (!ED_xlabel(ep))
UNSUPPORTED("6hyelvzskqfqa07xtgjtvg2is"); // 		continue;
UNSUPPORTED("d6wg3egk1x5aesy2ng1qyjh0z"); // 	    objp->sz.x = 0;
UNSUPPORTED("dy9evuaclpueiidwo0pp40904"); // 	    objp->sz.y = 0;
UNSUPPORTED("bpwvk9oag9fx8aiebw7xa9rw8"); // 	    objp->pos = edgeMidpoint(gp, ep);
UNSUPPORTED("64lz6mea65zfl2kbogusvglpn"); // 	    xlp->sz = ED_xlabel(ep)->dimen;
UNSUPPORTED("5rvgp0x8n9fqvbyp32952ouk2"); // 	    xlp->lbl = ED_xlabel(ep);
UNSUPPORTED("lch95gg7hal8p4g2coq8qya9"); // 	    xlp->set = 0;
UNSUPPORTED("9ozlxoe5hy2ssm40umphbfgxe"); // 	    objp->lbl = xlp;
UNSUPPORTED("ddik10sdd1c59te8kbgpqsncp"); // 	    xlp++;
UNSUPPORTED("9061jd99guumlgucvlj3a8vub"); // 	    objp++;
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("d447casut2vhvblc969saqfs4"); //     params.force = opts->force;
UNSUPPORTED("2ol916ffwy0e2vxinxn4v4sgt"); //     params.bb = bb;
UNSUPPORTED("2di5wqm6caczzl6bvqe35ry8y"); //     if (Verbose)
UNSUPPORTED("4iypau1fdov37qnq2ub6iq5ra"); // 	printData(objs, n_objs, lbls, n_lbls, &params);
UNSUPPORTED("25rb35acbkepp55u3bkjxb1gc"); //     placeLabels(objs, n_objs, lbls, n_lbls, &params);
UNSUPPORTED("tpyqsliwrqzwhuw8vzkm8loc"); //     fprintf(stderr, "read label positions\n");
UNSUPPORTED("52mefujap7scy273ud7nyj9hn"); //     xlp = lbls;
UNSUPPORTED("30yvif5t111f94y1fs2gd8crq"); //     for (i = 0; i < n_lbls; i++) {
UNSUPPORTED("w0kpfap6pb5scjkqkgsfira0"); // 	if (xlp->set) {
UNSUPPORTED("8xqwhcveb6ivragr1ebkp4pfh"); // 	    lp = (textlabel_t *) (xlp->lbl);
UNSUPPORTED("a5h8ktnl3raui7zo5kcjzd2e0"); // 	    lp->set = 1;
UNSUPPORTED("4lub8ddx8vt0gove63lajjr4s"); // 	    lp->pos = centerPt(xlp);
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("1966qdxqc520zc0itk8al0xus"); // 	xlp++;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("baez6nmarx9nht65vulvjojic"); //     free(objs);
UNSUPPORTED("ayak2o9js1lmsa5vuzul2sdxs"); //     free(lbls);
UNSUPPORTED("bftxx8axittt2mwc80wvu1f00"); //     fprintf(stderr, "writing %s\n", opts->outfname);
UNSUPPORTED("323sem4b7jzyg7l5kylxe0gnx"); //     gvRender(opts->gvc, gp, opts->fmt, opts->outf);
UNSUPPORTED("bp54wy9c70v90k5bb11lppo5l"); //     /* clean up */
UNSUPPORTED("67guepsgjnhtoqor7wh4gstti"); //     fprintf(stderr, "cleaning up\n");
UNSUPPORTED("dve51clmbbuj7sc653o65y416"); //     gvFreeLayout(opts->gvc, gp);
UNSUPPORTED("9d0rk5v0wtzmux625ddclpdn1"); //     agclose(gp);
UNSUPPORTED("5oxhd3fvp0gfmrmz12vndnjt"); //     return 0;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 9wfwgoq79qhinosrkyial6epj
// int checkOpt(char *l, char *legal[], int n) 
public static Object checkOpt(Object... arg) {
UNSUPPORTED("16prj059n95sysjma58fiqgzu"); // int checkOpt(char *l, char *legal[], int n)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("b17di9c7wgtqm51bvsyxz6e2f"); //     int i;
UNSUPPORTED("1vi49g48u2rc9v88yhabta0yw"); //     for (i = 0; i < n; i++) {
UNSUPPORTED("nnm3nrg0fnq3so1cn59gl1ty"); // 	if (strcmp(l, legal[i]) == 0)
UNSUPPORTED("btmwubugs9vkexo4yb7a5nqel"); // 	    return 1;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("5oxhd3fvp0gfmrmz12vndnjt"); //     return 0;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 8q6likcxw6ut5zeraaze8shtb
// void usage(char *pp) 
public static Object usage(Object... arg) {
UNSUPPORTED("7m2ltqr2kx3w6q4t3g8dzfqpp"); // void usage(char *pp)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("2a3rymfsfswadknge9x92kumi"); //     fprintf(stderr,
UNSUPPORTED("4wrs4bfjfwklmwoco1koczjdv"); // 	    "Usage: %s [-fv] [-V$level] [-T$fmt] [-l$layout] [-o$outfile] dotfile\n",
UNSUPPORTED("9ollqr0ef4x8spk0rt3sr8vqp"); // 	    pp);
UNSUPPORTED("b9185t6i77ez1ac587ul8ndnc"); //     return;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 3u8wp1vjie9akflq8id8h4jml
// static FILE *openFile(char *name, char *mode) 
public static Object openFile(Object... arg) {
UNSUPPORTED("d20hfpuvv6zzaoadalivef5gx"); // static FILE *openFile(char *name, char *mode)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("dn2hi0km4q5ldibguffbger1c"); //     FILE *fp;
UNSUPPORTED("44fhep40ukv2a3ja1nq34hrm8"); //     char *modestr;
UNSUPPORTED("a5fahi1wh7pd0cs3oalvsoaiv"); //     fp = fopen(name, mode);
UNSUPPORTED("304p80cpezoo5tih4fcsob9cq"); //     if (!fp) {
UNSUPPORTED("9ympiq6pzqmd8nshnoq3vpwfi"); // 	if (*mode == 'r')
UNSUPPORTED("14y4vxn380hggwx7a5m3zwyd0"); // 	    modestr = "reading";
UNSUPPORTED("9352ql3e58qs4fzapgjfrms2s"); // 	else
UNSUPPORTED("55vho08zxn3hxhpr4tx4use3p"); // 	    modestr = "writing";
UNSUPPORTED("c7s8at3cqmzgv80hgd8tm7wfb"); // 	fprintf(stderr, "%s: could not open file %s for %s -- %s\n",
UNSUPPORTED("56vywxbowmjyd2ssgb5jaawi7"); // 		progname, name, modestr, strerror(errno));
UNSUPPORTED("dtw3cma0ziyha0w534bszl0tx"); // 	exit(1);
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("6x54oh4xeozqrj2isrpofhher"); //     return (fp);
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 egkkwztfbkxu9vvlc0sm05nxv
// static void init(int argc, char *argv[], opts_t * opts) 
public static Object init(Object... arg) {
UNSUPPORTED("elmto38lhyd8ckgkm1qswhosv"); // static void init(int argc, char *argv[], opts_t * opts)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("emc3vdvz7smauda3se70e5ogd"); //     int c, cnt;
UNSUPPORTED("1pbwdmcrlnqb7teoelyxecqu9"); //     char **optList;
UNSUPPORTED("87dxsbpdsz45s6w205t6ds10b"); //     opts->outf = stdout;
UNSUPPORTED("7mpmbqdt8ajbx6itgsay4kr6i"); //     opts->outfname = "stdout";
UNSUPPORTED("2ah6k3x8emq2o4qkba0zu0zuv"); //     progname = argv[0];
UNSUPPORTED("63lodtevhv92534zqnwg4wf1t"); //     opts->gvc = gvContext();
UNSUPPORTED("81tz51f3gan5smiwrvn3h1lt2"); //     opts->lay = "dot";
UNSUPPORTED("1yiembfofkehuhrorie2b9gsi"); //     opts->fmt = "ps";
UNSUPPORTED("7oi2c7s5gl8voe3x4yum7jszu"); //     opts->force = 0;
UNSUPPORTED("60bek3ektewmma3p6pak5x2am"); //     while ((c = getopt(argc, argv, "o:vFl:T:V:")) != EOF) {
UNSUPPORTED("7rk995hpmaqbbasmi40mqg0yw"); // 	switch (c) {
UNSUPPORTED("c7a78qt1dx3jxjw23fwcmwr3p"); // 	case 'F':
UNSUPPORTED("biextvcnz6zkqlqxgssaqrstl"); // 	    opts->force = 1;
UNSUPPORTED("ai3czg6gaaxspsmndknpyvuiu"); // 	    break;
UNSUPPORTED("bwy7mh2nb7lz950r20rfilwa4"); // 	case 'l':
UNSUPPORTED("6pele3rfkd0r1k5r8qzc5vbha"); // 	    optList = gvPluginList(opts->gvc, "layout", &cnt, NULL);
UNSUPPORTED("7kfiavaqy7qmfg2hg1e8z80e1"); // 	    if (checkOpt(optarg, optList, cnt))
UNSUPPORTED("anmrnwabtq1ijqqr2uop2n1bh"); // 		opts->lay = optarg;
UNSUPPORTED("6q044im7742qhglc4553noina"); // 	    else {
UNSUPPORTED("4rymfp2u3aj6lx2s6plx3hojv"); // 		fprintf(stderr, "%s: unknown layout %s\n", progname,
UNSUPPORTED("4xhetnmuyh8emewbykj08vroq"); // 			optarg);
UNSUPPORTED("7ujm7da8xuut83e2rygja0n9d"); // 		exit(1);
UNSUPPORTED("6t98dcecgbvbvtpycwiq2ynnj"); // 	    }
UNSUPPORTED("ai3czg6gaaxspsmndknpyvuiu"); // 	    break;
UNSUPPORTED("2f0a79iza84u7nhurd3n3jjjf"); // 	case 'T':
UNSUPPORTED("9cus1va3ujh87mdys6c9v2z1t"); // 	    optList = gvPluginList(opts->gvc, "device", &cnt, NULL);
UNSUPPORTED("7kfiavaqy7qmfg2hg1e8z80e1"); // 	    if (checkOpt(optarg, optList, cnt))
UNSUPPORTED("5altfebkbv6f35oj03qk2492n"); // 		opts->fmt = optarg;
UNSUPPORTED("6q044im7742qhglc4553noina"); // 	    else {
UNSUPPORTED("bzcwn6blrae9inr3p5m09e9d"); // 		fprintf(stderr, "%s: unknown format %s\n", progname,
UNSUPPORTED("4xhetnmuyh8emewbykj08vroq"); // 			optarg);
UNSUPPORTED("7ujm7da8xuut83e2rygja0n9d"); // 		exit(1);
UNSUPPORTED("6t98dcecgbvbvtpycwiq2ynnj"); // 	    }
UNSUPPORTED("ai3czg6gaaxspsmndknpyvuiu"); // 	    break;
UNSUPPORTED("318vlfmcdz9no6i45k86aygon"); // 	case 'v':
UNSUPPORTED("z08wni1aiuv5b8ommxtq10kj"); // 	    Verbose = 1;
UNSUPPORTED("ai3czg6gaaxspsmndknpyvuiu"); // 	    break;
UNSUPPORTED("6k6q2a5qrb52v1ue87bbj6dtv"); // 	case 'V':
UNSUPPORTED("3r3fz5bgdw11zenv47obhu5vt"); // 	    Verbose = atoi(optarg);
UNSUPPORTED("ai3czg6gaaxspsmndknpyvuiu"); // 	    break;
UNSUPPORTED("2tj3jzqar12mlqrsqiqtnaml4"); // 	case 'o':
UNSUPPORTED("1wx5260eycnikbxaanmopl7b3"); // 	    opts->outf = openFile(optarg, "w");
UNSUPPORTED("e167mkpssrx4z8qunrmw8kh1n"); // 	    opts->outfname = optarg;
UNSUPPORTED("ai3czg6gaaxspsmndknpyvuiu"); // 	    break;
UNSUPPORTED("1drv0xz8hp34qnf72b4jpprg2"); // 	default:
UNSUPPORTED("22xk7nfw0r35o66cqu5qjubqb"); // 	    usage(progname);
UNSUPPORTED("6f1y0d5qfp1r9zpw0r7m6xfb4"); // 	    exit(1);
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("3phw6ctc4ona74z9ww17xjaw3"); //     if (optind < argc) {
UNSUPPORTED("7hpkub9g5s8almwdoymfuenxh"); // 	opts->inf = openFile(argv[optind], "r");
UNSUPPORTED("3zkn0yg2bqlmxotocssnzhxws"); // 	opts->infname = argv[optind];
UNSUPPORTED("c07up7zvrnu2vhzy6d7zcu94g"); //     } else {
UNSUPPORTED("30pkf9oeg7gpxsywbm5shgg64"); // 	opts->inf = stdin;
UNSUPPORTED("e61j0rmaw1t3upfgqro72rx91"); // 	opts->infname = "<stdin>";
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("ct8h35c783zokfb7wxjbkavha"); //     if (!opts->outf) {
UNSUPPORTED("2s6g8jva40gbl5p87hy413rg4"); // 	opts->outf = stdout;
UNSUPPORTED("e9nmk5kmlczlng9wz7f4q8gtv"); // 	opts->outfname = "<stdout>";
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 7i7prctjnltgmsf7dgzmddjg9
// int main(int argc, char *argv[]) 
public static Object main(Object... arg) {
UNSUPPORTED("8fr3wh1xwllvel0m0nkh09bjl"); // int main(int argc, char *argv[])
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("kbo4pgmde2ojznpsie5m4t8h"); //     opts_t opts;
UNSUPPORTED("db6kkkb1xqkrydzxgyiyxl8ds"); //     init(argc, argv, &opts);
UNSUPPORTED("dap4chqfn6jjoa6j31f8lngs4"); //     doxlabel(&opts);
UNSUPPORTED("5oxhd3fvp0gfmrmz12vndnjt"); //     return 0;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}


}
