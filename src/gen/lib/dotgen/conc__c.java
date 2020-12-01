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
package gen.lib.dotgen;
import static smetana.core.Macro.UNSUPPORTED;

import gen.annotation.Original;
import gen.annotation.Unused;
import smetana.core.jmp_buf;

public class conc__c {


//1 540u5gu9i0x1wzoxqqx5n2vwp
// static jmp_buf jbuf
private static jmp_buf jbuf = new jmp_buf();




//3 3mzzkxpsezmtvlbzshvr46b8m
// void dot_concentrate(graph_t * g) 
@Unused
@Original(version="2.38.0", path="lib/dotgen/conc.c", name="dot_concentrate", key="3mzzkxpsezmtvlbzshvr46b8m", definition="void dot_concentrate(graph_t * g)")
public static Object dot_concentrate(Object... arg_) {
UNSUPPORTED("4h7xkpw4zpae6ztth270w22gd"); // void dot_concentrate(graph_t * g)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("62hcnpegd6vvkgtfo3a833aq9"); //     int c, r, leftpos, rightpos;
UNSUPPORTED("amyi14irz6dbz0367id35hvp"); //     node_t *left, *right;
UNSUPPORTED("1exryvusfp7fkh0rn8bxcz96g"); //     if (GD_maxrank(g) - GD_minrank(g) <= 1)
UNSUPPORTED("a7fgam0j0jm7bar0mblsv3no4"); // 	return;
UNSUPPORTED("1o57fdj7j63samf9jysgvpqnq"); //     /* this is the downward looking pass. r is a candidate rank. */
UNSUPPORTED("ca08a4ijjaxoo89carapxu5ur"); //     for (r = 1; GD_rank(g)[r + 1].n; r++) {
UNSUPPORTED("8elk0ekrvmk0yt6bmbwub0epc"); // 	for (leftpos = 0; leftpos < GD_rank(g)[r].n; leftpos++) {
UNSUPPORTED("ela65u1ff5sc7nw7eq6hioji0"); // 	    left = GD_rank(g)[r].v[leftpos];
UNSUPPORTED("7yq5j8b980yc5rz1e4lkwgmth"); // 	    if (downcandidate(left) == 0)
UNSUPPORTED("6hyelvzskqfqa07xtgjtvg2is"); // 		continue;
UNSUPPORTED("exizgsc0bwm37dxc6j6op74lz"); // 	    for (rightpos = leftpos + 1; rightpos < GD_rank(g)[r].n;
UNSUPPORTED("5a4wx0786piefhj7q3m241wof"); // 		 rightpos++) {
UNSUPPORTED("3jh1iupmf6freiri4v7zbt87k"); // 		right = GD_rank(g)[r].v[rightpos];
UNSUPPORTED("7ap3iy5ysmyd0x869id0jyedv"); // 		if (bothdowncandidates(left, right) == 0)
UNSUPPORTED("czyohktf9bkx4udfqhx42f4lu"); // 		    break;
UNSUPPORTED("6t98dcecgbvbvtpycwiq2ynnj"); // 	    }
UNSUPPORTED("3q4uw7garqdhjalzpk1bivu8s"); // 	    if (rightpos - leftpos > 1)
UNSUPPORTED("4o5cptipygbr3qu2m6rkk4qwg"); // 		mergevirtual(g, r, leftpos, rightpos - 1, 1);
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("cakeuerd3mfjfwy396sci21tn"); //     /* this is the corresponding upward pass */
UNSUPPORTED("9cqq87ffcq47f55xlupeeuo6u"); //     while (r > 0) {
UNSUPPORTED("8elk0ekrvmk0yt6bmbwub0epc"); // 	for (leftpos = 0; leftpos < GD_rank(g)[r].n; leftpos++) {
UNSUPPORTED("ela65u1ff5sc7nw7eq6hioji0"); // 	    left = GD_rank(g)[r].v[leftpos];
UNSUPPORTED("dgbio6b5e3ja3yul0q4rl5qqn"); // 	    if (upcandidate(left) == 0)
UNSUPPORTED("6hyelvzskqfqa07xtgjtvg2is"); // 		continue;
UNSUPPORTED("exizgsc0bwm37dxc6j6op74lz"); // 	    for (rightpos = leftpos + 1; rightpos < GD_rank(g)[r].n;
UNSUPPORTED("5a4wx0786piefhj7q3m241wof"); // 		 rightpos++) {
UNSUPPORTED("3jh1iupmf6freiri4v7zbt87k"); // 		right = GD_rank(g)[r].v[rightpos];
UNSUPPORTED("6j41xobwrcrep12b6hhs39r8z"); // 		if (bothupcandidates(left, right) == 0)
UNSUPPORTED("czyohktf9bkx4udfqhx42f4lu"); // 		    break;
UNSUPPORTED("6t98dcecgbvbvtpycwiq2ynnj"); // 	    }
UNSUPPORTED("3q4uw7garqdhjalzpk1bivu8s"); // 	    if (rightpos - leftpos > 1)
UNSUPPORTED("c3v2wops656i36vmsec5ezxk0"); // 		mergevirtual(g, r, leftpos, rightpos - 1, 0);
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("8a1wsnjguqfvz8vfx6erejakp"); // 	r--;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("ci9r8sj8tbc6yer5c8cebb0cm"); //     if (setjmp(jbuf)) {
UNSUPPORTED("ej9htgkxfq6bintcw04c6svwu"); // 	agerr(AGPREV, "concentrate=true may not work correctly.\n");
UNSUPPORTED("a7fgam0j0jm7bar0mblsv3no4"); // 	return;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("7z5fb6iyowsosn1hiz7opeoc6"); //     for (c = 1; c <= GD_n_cluster(g); c++)
UNSUPPORTED("1d7lqtmagp5mwcbbkf4xawdog"); // 	rebuild_vlists(GD_clust(g)[c]);
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}


}
