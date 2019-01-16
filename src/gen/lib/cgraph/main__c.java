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
package gen.lib.cgraph;
import static smetana.core.Macro.UNSUPPORTED;

public class main__c {


//3 6odliptr9wa623cqopkvk2jli
// static void my_ins(Agraph_t * g, Agobj_t * obj, void *context) 
public static Object my_ins(Object... arg) {
UNSUPPORTED("c7iv5iry7vbk7hni09hhaowd5"); // static void my_ins(Agraph_t * g, Agobj_t * obj, void *context)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("2jcii9cclu1dijzqekzc175pe"); //     Agnode_t *n;
UNSUPPORTED("a9l6doo688qv3rdvch9hmibgq"); //     if (AGTYPE(obj) == AGNODE) {
UNSUPPORTED("d450vypq8xi24vzde0qbtu2r8"); // 	n = (Agnode_t *) obj;
UNSUPPORTED("2gpi6t9bnp5hdft0cecv3iune"); // 	fprintf(stderr, "%s initialized with label %s\n", agnameof(n),
UNSUPPORTED("1p5eazfn0cyfxuxjea9lnir1w"); // 		agget(n, "label"));
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}


//1 712rrmbvh64z403qurukiza4t
// static Agcbdisc_t mydisc = 




//3 bvktrg27hkm4awzgaxie82v5n
// main(int argc, char **argv) 
public static Object main(Object... arg) {
UNSUPPORTED("2vxu1fvoegqfch6u8yop5e3yd"); // main(int argc, char **argv)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("9rw11eamkbc6vjzz8oon7h5or"); //     Agraph_t *g, *prev;
UNSUPPORTED("ecwk3deuub77ow53wzobyi1um"); //     int dostat;
UNSUPPORTED("ay3nlfl1osekrj2sqshobe5at"); //     if (argc > 1)
UNSUPPORTED("ckyyp5xta8o4677c6fu181fx7"); // 	dostat = atoi(argv[1]);
UNSUPPORTED("div10atae09n36x269sl208r1"); //     else
UNSUPPORTED("cl0t34fw12lib2l4xzfg1a37y"); // 	dostat = 0;
UNSUPPORTED("ct1ab9xijd5ig91peu6a6mt9k"); //     prev = agopen("some_name", Agdirected, NIL(Agdisc_t *));
UNSUPPORTED("e0xgjn1wccsmsi4ek4d7a2qei"); //     agcallbacks(prev, FALSE);
UNSUPPORTED("7c3x2xl11ikgliugx67a51w0d"); //     agpushdisc(prev, &mydisc, NIL(void *));
UNSUPPORTED("bngzo05d6auu02ybcab17exp"); //     while (g = agconcat(prev, stdin, NIL(Agdisc_t *))) {
UNSUPPORTED("69r6skguykum6b262jtd67j6e"); // 	/*do_it(g, dostat); */
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("c49oayps7r0q8498dhp8trru4"); //     /*agwrite(prev,stdout); */
UNSUPPORTED("a9cya29glvbdxdy722npvubi1"); //     fprintf(stderr, "ready to go, computer fans\n");
UNSUPPORTED("c2ap65hg9mbsyttiia9s186mf"); //     agcallbacks(prev, TRUE);
UNSUPPORTED("b0t9qgq0y5pre95c9o03imotq"); //     agclose(prev);
UNSUPPORTED("3tcgz4dupb6kw5tdk7n3pca2l"); //     return 1;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 bhswp7qq6w162hh6ho3csmea3
// static void prstats(Agraph_t * g, int verbose) 
public static Object prstats(Object... arg) {
UNSUPPORTED("5szlwllz47f72n6dum53fsuvt"); // static void prstats(Agraph_t * g, int verbose)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 5aj3p757f0uumfpssnoqimpjp
// static void do_it(Agraph_t * g, int dostat) 
public static Object do_it(Object... arg) {
UNSUPPORTED("cq0p9fgngzp1ynr7fuablhs5i"); // static void do_it(Agraph_t * g, int dostat)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("9w53fmba5tercyxnm4fnbyshd"); //     agwrite(g, stdout);
UNSUPPORTED("25junfxb4dqovzjqy48puogi1"); //     if (dostat)
UNSUPPORTED("542sekyz7qr0nvvoorslw1nkq"); // 	prstats(g, dostat > 1);
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}


}
