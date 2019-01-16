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
package gen.lib.pathplan;
import static smetana.core.Macro.UNSUPPORTED;
import smetana.core.jmp_buf;

public class triang__c {
//1 baedz5i9est5csw3epz3cv7z
// typedef Ppoly_t Ppolyline_t


//1 7pb9zum2n4wlgil34lvh8i0ts
// typedef double COORD


//1 540u5gu9i0x1wzoxqqx5n2vwp
// static jmp_buf jbuf
private static jmp_buf jbuf = new jmp_buf();



//3 9l5xg5aowmh2yvhbzseo8ws0i
// static int dpd_ccw(Ppoint_t * p1, Ppoint_t * p2, Ppoint_t * p3) 
public static Object dpd_ccw(Object... arg) {
UNSUPPORTED("66u8hjpmzz1u1podxvft7tqbr"); // static int dpd_ccw(Ppoint_t * p1, Ppoint_t * p2, Ppoint_t * p3)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("8l4kygp6iwsssqf4pip9kt8ih"); //     double d =
UNSUPPORTED("a5i2lyqa3iblrijf5i8d2gc2l"); // 	((p1->y - p2->y) * (p3->x - p2->x)) -
UNSUPPORTED("8vxxbchvixjdl9bnn73jj0ken"); // 	((p3->y - p2->y) * (p1->x - p2->x));
UNSUPPORTED("2duek0l6qi5enj1p52uzx935s"); //     return (d > 0) ? 2 : ((d < 0) ? 1 : 3);
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 3cpm045bql7do4skuvm0gysbz
// int Ptriangulate(Ppoly_t * polygon, void (*fn) (void *, Ppoint_t *), 		  void *vc) 
public static Object Ptriangulate(Object... arg) {
UNSUPPORTED("9wq0zzi4wsqf1qmh5lisb5tur"); // int Ptriangulate(Ppoly_t * polygon, void (*fn) (void *, Ppoint_t *),
UNSUPPORTED("aiup6hqt7vuonmdnhtadcpyyg"); // 		  void *vc)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("b17di9c7wgtqm51bvsyxz6e2f"); //     int i;
UNSUPPORTED("79ig2xj5nogd41esx7798m82t"); //     int pointn;
UNSUPPORTED("vbp57y34yrr1stscac6ij6wl"); //     Ppoint_t **pointp;
UNSUPPORTED("8i4e5opqy9uomak4ow0vdfkyu"); //     pointn = polygon->pn;
UNSUPPORTED("c7ts682vzzct8ooh1x4u7zy09"); //     pointp = (Ppoint_t **) malloc(pointn * sizeof(Ppoint_t *));
UNSUPPORTED("69msjv91b63ali4jnpa1iae96"); //     for (i = 0; i < pointn; i++)
UNSUPPORTED("66ykefpx20ohahuts3stdhpv4"); // 	pointp[i] = &(polygon->ps[i]);
UNSUPPORTED("ci9r8sj8tbc6yer5c8cebb0cm"); //     if (setjmp(jbuf)) {
UNSUPPORTED("1hfjyk5uqecl2hrvu9vdplqm9"); // 	free(pointp);
UNSUPPORTED("eleqpc2p2r3hvma6tipoy7tr"); // 	return 1;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("3epdv4rxh1dblgm1uu3cirnns"); //     triangulate(pointp, pointn, fn, vc);
UNSUPPORTED("d7eui8n8t6ty33reomekpb4uy"); //     free(pointp);
UNSUPPORTED("5oxhd3fvp0gfmrmz12vndnjt"); //     return 0;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 db1gjkgbhdyik8njcwxidnm06
// static void triangulate(Ppoint_t ** pointp, int pointn, 	    void (*fn) (void *, Ppoint_t *), void *vc) 
public static Object triangulate(Object... arg) {
UNSUPPORTED("e2z2o5ybnr5tgpkt8ty7hwan1"); // static void
UNSUPPORTED("cycr5htq0awpt4lysrda94z75"); // triangulate(Ppoint_t ** pointp, int pointn,
UNSUPPORTED("b80kmvbhu4qon4ithxw7iqghl"); // 	    void (*fn) (void *, Ppoint_t *), void *vc)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("47im1y8cb3jov8e7vfoq6976"); //     int i, ip1, ip2, j;
UNSUPPORTED("9vrtuc7i6xtgewghaf5mhcvw7"); //     Ppoint_t A[3];
UNSUPPORTED("exebh5u9jcrlenwyhvh1q7ydp"); //     if (pointn > 3) {
UNSUPPORTED("17s2by4i08aiirk34qoufejm1"); // 	for (i = 0; i < pointn; i++) {
UNSUPPORTED("4s8abivdx5zru8lby110pnbr0"); // 	    ip1 = (i + 1) % pointn;
UNSUPPORTED("el7eqg9364rd6m2jmmgd1x0w6"); // 	    ip2 = (i + 2) % pointn;
UNSUPPORTED("eugvvlona15w69kyxc7uao5rn"); // 	    if (dpd_isdiagonal(i, ip2, pointp, pointn)) {
UNSUPPORTED("7jt096yrjcjaka77bxj8ow4ur"); // 		A[0] = *pointp[i];
UNSUPPORTED("798zfru1x3zjftqir7h3184cy"); // 		A[1] = *pointp[ip1];
UNSUPPORTED("5sabp0m6pi8misjllge9rhs6i"); // 		A[2] = *pointp[ip2];
UNSUPPORTED("3tx9m6zu4gpdon7itam4m4zj6"); // 		fn(vc, A);
UNSUPPORTED("cwdl8048erup925vkw9wm50tm"); // 		j = 0;
UNSUPPORTED("b406sjxectwut71daq5renp3v"); // 		for (i = 0; i < pointn; i++)
UNSUPPORTED("17mmkxxi6uyt2rtn26wqpnjq0"); // 		    if (i != ip1)
UNSUPPORTED("20nocric7p5rwm1keba2y9u9s"); // 			pointp[j++] = pointp[i];
UNSUPPORTED("f1yvgmqdgpzz2ld9mihtb1ny6"); // 		triangulate(pointp, pointn - 1, fn, vc);
UNSUPPORTED("6bj8inpmr5ulm16jmfxsstjtn"); // 		return;
UNSUPPORTED("6t98dcecgbvbvtpycwiq2ynnj"); // 	    }
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("efu63j23oynz0w42y5x2ccgl"); // 	longjmp(jbuf,1);
UNSUPPORTED("c07up7zvrnu2vhzy6d7zcu94g"); //     } else {
UNSUPPORTED("du12jp7y7eb3k2lzktkq97ocv"); // 	A[0] = *pointp[0];
UNSUPPORTED("3zxe87agbrkgcdv3ezmeb4e0r"); // 	A[1] = *pointp[1];
UNSUPPORTED("7kqa9p0bujx0a7qu7g0l8u5gf"); // 	A[2] = *pointp[2];
UNSUPPORTED("4a2pon3qy0ncl805797s5a1cg"); // 	fn(vc, A);
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 6g3z4d4wjf8de2l784sgpmmol
// static int dpd_isdiagonal(int i, int ip2, Ppoint_t ** pointp, int pointn) 
public static Object dpd_isdiagonal(Object... arg) {
UNSUPPORTED("6igaattr8mose3ux86cjxqy8f"); // static int dpd_isdiagonal(int i, int ip2, Ppoint_t ** pointp, int pointn)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("i2fy680j9sex9dnpbrntxl4b"); //     int ip1, im1, j, jp1, res;
UNSUPPORTED("5lvzmrs5o783t9b2alzlkj6uq"); //     /* neighborhood test */
UNSUPPORTED("9eqc4ewd8x2tb2o7676bqvzbk"); //     ip1 = (i + 1) % pointn;
UNSUPPORTED("7uaqndtt8y6wd5jenwxduolym"); //     im1 = (i + pointn - 1) % pointn;
UNSUPPORTED("e4zga3b8zozs44qyy6bccq35q"); //     /* If P[i] is a convex vertex [ i+1 left of (i-1,i) ]. */
UNSUPPORTED("6rxb1urly3d45lz9xrdat9rjc"); //     if (dpd_ccw(pointp[im1], pointp[i], pointp[ip1]) == 1)
UNSUPPORTED("1h8m3wqaqis9j4awgexj22ljg"); // 	res = (dpd_ccw(pointp[i], pointp[ip2], pointp[im1]) == 1) &&
UNSUPPORTED("54snmiylw2hrwv7yf250ut7kk"); // 	    (dpd_ccw(pointp[ip2], pointp[i], pointp[ip1]) == 1);
UNSUPPORTED("f2vywip5w6wo2ku9n219qyc0t"); //     /* Assume (i - 1, i, i + 1) not collinear. */
UNSUPPORTED("div10atae09n36x269sl208r1"); //     else
UNSUPPORTED("8s457vlt2s8q2dv1carkz8jw1"); // 	res = ((dpd_ccw(pointp[i], pointp[ip2], pointp[ip1]) == 2)
UNSUPPORTED("5lgnvc71rkx0ldj6euv5vtcsr"); // 	    );
UNSUPPORTED("4v614d3uabme2jyn0anuritbb"); // /*
UNSUPPORTED("71hkfc5n8im1y698xlsrhbyp5"); // 		&&
UNSUPPORTED("axkcv4kehdkqwxnlh4284iio4"); //                 (dpd_ccw (pointp[ip2], pointp[i], pointp[im1]) != ISCW));
UNSUPPORTED("bnetqzovnscxile7ao44kc0qd"); // */
UNSUPPORTED("6o97tfdzw11zrs51pped6bix"); //     if (!res) {
UNSUPPORTED("c9ckhc8veujmwcw0ar3u3zld4"); // 	return 0;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("bsnnstj5ry3f53yyx6h80if6r"); //     /* check against all other edges */
UNSUPPORTED("2eknpu2grk40zpq5rvl1rsf0e"); //     for (j = 0; j < pointn; j++) {
UNSUPPORTED("7j38zwxm4hyg9g5fi62sr5dz6"); // 	jp1 = (j + 1) % pointn;
UNSUPPORTED("7cqino9kvp1bszwsv2z4zllv1"); // 	if (!((j == i) || (jp1 == i) || (j == ip2) || (jp1 == ip2)))
UNSUPPORTED("attqyhhvmboobjbl2qf7l6hk1"); // 	    if (dpd_intersects
UNSUPPORTED("43hv90vngw3gvon8vmtn7o3in"); // 		(pointp[i], pointp[ip2], pointp[j], pointp[jp1])) {
UNSUPPORTED("5izxoao5ryte71964f8yjfd5y"); // 		return 0;
UNSUPPORTED("6t98dcecgbvbvtpycwiq2ynnj"); // 	    }
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("2mmsh4mer8e3bkt2jk4gf4cyq"); //     return ((!(0)));
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 8pjte4rru806nqx2myxn2h8tn
// static int dpd_intersects(Ppoint_t * pa, Ppoint_t * pb, Ppoint_t * pc, 			  Ppoint_t * pd) 
public static Object dpd_intersects(Object... arg) {
UNSUPPORTED("b7u1jnesjd68hfofduzpr4qf0"); // static int dpd_intersects(Ppoint_t * pa, Ppoint_t * pb, Ppoint_t * pc,
UNSUPPORTED("def51j09bielz8iq0blza86vg"); // 			  Ppoint_t * pd)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("9nscw3h7ol3rzfhy7cmityo7h"); //     int ccw1, ccw2, ccw3, ccw4;
UNSUPPORTED("98ug1zrydddu1pwy2oplua9u6"); //     if (dpd_ccw(pa, pb, pc) == 3 || dpd_ccw(pa, pb, pd) == 3 ||
UNSUPPORTED("4cfcl9frsb20o33zdgi2pm67u"); // 	dpd_ccw(pc, pd, pa) == 3 || dpd_ccw(pc, pd, pb) == 3) {
UNSUPPORTED("48qgrwlmwsf461u8tcizu96zg"); // 	if (dpd_between(pa, pb, pc) || dpd_between(pa, pb, pd) ||
UNSUPPORTED("64gna5xeh3ex4eoernwe8f797"); // 	    dpd_between(pc, pd, pa) || dpd_between(pc, pd, pb))
UNSUPPORTED("a56gc2zenjhptik6h3r86au9x"); // 	    return ((!(0)));
UNSUPPORTED("c07up7zvrnu2vhzy6d7zcu94g"); //     } else {
UNSUPPORTED("345wiq942wt8egchavxisiate"); // 	ccw1 = (dpd_ccw(pa, pb, pc) == 1) ? 1 : 0;
UNSUPPORTED("8ss56vnafb15h2m5amgdjkdeo"); // 	ccw2 = (dpd_ccw(pa, pb, pd) == 1) ? 1 : 0;
UNSUPPORTED("281uxj8f020cd62tq8wnntaf6"); // 	ccw3 = (dpd_ccw(pc, pd, pa) == 1) ? 1 : 0;
UNSUPPORTED("9u7kvtaa6q6nms4h93wl3llei"); // 	ccw4 = (dpd_ccw(pc, pd, pb) == 1) ? 1 : 0;
UNSUPPORTED("e8shba3pxorddpi51sfeac4ju"); // 	return (ccw1 ^ ccw2) && (ccw3 ^ ccw4);
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("5oxhd3fvp0gfmrmz12vndnjt"); //     return 0;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 coo3dmcddl2hcgc5gprzj3xtf
// static int dpd_between(Ppoint_t * pa, Ppoint_t * pb, Ppoint_t * pc) 
public static Object dpd_between(Object... arg) {
UNSUPPORTED("8i6slq3k2lvso1osulneg0qfd"); // static int dpd_between(Ppoint_t * pa, Ppoint_t * pb, Ppoint_t * pc)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("42b52ogcufv0s2qul8bwyz1mc"); //     Ppoint_t pba, pca;
UNSUPPORTED("71f5gd0n8v9s3kgph6iiqu4xl"); //     pba.x = pb->x - pa->x, pba.y = pb->y - pa->y;
UNSUPPORTED("8grh7ovfy0h3pcie2b7bxoxf9"); //     pca.x = pc->x - pa->x, pca.y = pc->y - pa->y;
UNSUPPORTED("ajkxh6482351n4hst9pf3sajt"); //     if (dpd_ccw(pa, pb, pc) != 3)
UNSUPPORTED("c9ckhc8veujmwcw0ar3u3zld4"); // 	return 0;
UNSUPPORTED("e461rpw0ig3rd1xwxn9quym9h"); //     return (pca.x * pba.x + pca.y * pba.y >= 0) &&
UNSUPPORTED("2nprox8kgwp76fqf186ca4l78"); // 	(pca.x * pca.x + pca.y * pca.y <= pba.x * pba.x + pba.y * pba.y);
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}


}
