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
import static smetana.core.Macro.UNSUPPORTED;

public class split_q__c {


//3 6vl3snxd6k95gamfkwfsfdguc
// void SplitNode(RTree_t * rtp, Node_t * n, Branch_t * b, Node_t ** nn) 
public static Object SplitNode(Object... arg) {
UNSUPPORTED("78ce6yisssjvu8419g3hps65l"); // void SplitNode(RTree_t * rtp, Node_t * n, Branch_t * b, Node_t ** nn)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("ajyfxc076ll1tzroielljxa4s"); //     register struct PartitionVars *p;
UNSUPPORTED("8jufm5xh68khkqy7z1kcdmesw"); //     register int level;
UNSUPPORTED("7bskhxl3c4vhiobuvlm02c79c"); //     int area;
UNSUPPORTED("25fhmphgra40j368i6b09n5xz"); //     assert(n);
UNSUPPORTED("5xtwc50ov4mxby7k6gahwv12t"); //     assert(b);
UNSUPPORTED("21ol6vbghnu1ewovfs645sts7"); //     if (rtp->StatFlag) {
UNSUPPORTED("akhni40ndam0u9c6i7raxw4mp"); // 	if (rtp->Deleting)
UNSUPPORTED("4g80zdlbvunm838x8g3ic9tex"); // 	    rtp->DeSplitCount++;
UNSUPPORTED("9352ql3e58qs4fzapgjfrms2s"); // 	else
UNSUPPORTED("2cjo6wz1rmxfm5k7u7rw5dqpj"); // 	    rtp->InSplitCount++;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("8396srmzb1jm8lij0ngslpcrf"); //     /* load all the branches into a buffer, initialize old node */
UNSUPPORTED("21fuexra37tdi6sx7rtjdg1nj"); //     level = n->level;
UNSUPPORTED("bqz4krdoll1zhnuqj6zgotbcu"); //     GetBranches(rtp, n, b);
UNSUPPORTED("hdjjn3pouo1k1eq8y4x0nmxs"); //     /* find partition */
UNSUPPORTED("prf503ssj57ewtg2w9ypvt6"); //     p = &rtp->split.Partitions[0];
UNSUPPORTED("e5s838qmpxfzesnb90fmga2th"); //     MethodZero(rtp);
UNSUPPORTED("e2qxl0u7yon3spy2kr56ah1lx"); //     area = RectArea(&p->cover[0]) + RectArea(&p->cover[1]);
UNSUPPORTED("bqvd9kyv5qlcddypjudg6lwpl"); //     /* record how good the split was for statistics */
UNSUPPORTED("9isnpzrwseyh76rz7fawgkdyw"); //     if (rtp->StatFlag && !rtp->Deleting && area)
UNSUPPORTED("z7xk6s3hzi3qcoiq2exj9hpv"); // 	rtp->SplitMeritSum += (float) rtp->split.CoverSplitArea / area;
UNSUPPORTED("dytz8fm640zs5ercfjtork8u2"); //     /* put branches from buffer into 2 nodes according to chosen partition */
UNSUPPORTED("3vka4yfkd2kilsogvopxmh6ry"); //     *nn = RTreeNewNode(rtp);
UNSUPPORTED("3wybhajryp9yjj3lxvzuiv7p9"); //     (*nn)->level = n->level = level;
UNSUPPORTED("izrbnvm6p45dy2xfr3265jdf"); //     LoadNodes(rtp, n, *nn, p);
UNSUPPORTED("e0lxlkm80cy0577wy8jdxbhq9"); //     assert(n->count + (*nn)->count == 64 + 1);
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 al7lyin008m7kvrvuxhcuvn61
// static void GetBranches(RTree_t * rtp, Node_t * n, Branch_t * b) 
public static Object GetBranches(Object... arg) {
UNSUPPORTED("378qaodt9k6ytly4svwl41z9w"); // static void GetBranches(RTree_t * rtp, Node_t * n, Branch_t * b)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("pp6gyv6pecd6kik4hoguluwp"); //     register int i;
UNSUPPORTED("25fhmphgra40j368i6b09n5xz"); //     assert(n);
UNSUPPORTED("5xtwc50ov4mxby7k6gahwv12t"); //     assert(b);
UNSUPPORTED("4oznkldmd6dyb0i9hwec78vcs"); //     /* load the branch buffer */
UNSUPPORTED("82kuymnzv3rubrwutx9bcz4xs"); //     for (i = 0; i < 64; i++) {
UNSUPPORTED("8q6h71l7igid38ccu2eiu2u59"); // 	assert(n->branch[i].child);	/* node should have every entry full */
UNSUPPORTED("gimqiz18hbjhs9folzw2kmv9"); // 	rtp->split.BranchBuf[i] = n->branch[i];
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("e53m9mpp58nv3640vo2p82rdv"); //     rtp->split.BranchBuf[64] = *b;
UNSUPPORTED("277onaphbigykl17b8k9ucsw3"); //     /* calculate rect containing all in the set */
UNSUPPORTED("ea4y9kg2yx11whdarwr9up60y"); //     rtp->split.CoverSplit = rtp->split.BranchBuf[0].rect;
UNSUPPORTED("27yzy7ezx5w65cas3232s6j4m"); //     for (i = 1; i < 64 + 1; i++) {
UNSUPPORTED("4qq8r3ecwl603g3xs97658ebu"); // 	rtp->split.CoverSplit = CombineRect(&rtp->split.CoverSplit,
UNSUPPORTED("8fgchf6ahna7tcxprht01sodq"); // 					    &rtp->split.BranchBuf[i].rect);
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("8kobdqnp7t9xhtshqkemby6us"); //     rtp->split.CoverSplitArea = RectArea(&rtp->split.CoverSplit);
UNSUPPORTED("a4ey5uckjqallol1ktyqe35bv"); //     InitNode(n);
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 4woz5xy4gjlahoj7no3ljxmex
// static void MethodZero(RTree_t * rtp) 
public static Object MethodZero(Object... arg) {
UNSUPPORTED("4skckzef98x4fz6mws7s1ya15"); // static void MethodZero(RTree_t * rtp)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("cdtuwprcw1d3bn91v9sde844l"); //     register Rect_t *r;
UNSUPPORTED("ackphjde797dk6rshtiyv1ydx"); //     register int i, growth0, growth1, diff, biggestDiff;
UNSUPPORTED("5lh1z9jfdefgm50i9fgjjzf8q"); //     register int group, chosen, betterGroup;
UNSUPPORTED("6y4x18eeg7eay10tbawh6myhr"); //     InitPVars(rtp);
UNSUPPORTED("5tz02egfuze5n9nquye65i9lr"); //     PickSeeds(rtp);
UNSUPPORTED("59gdg3tgr5tyx6gkffgutcpn7"); //     while (rtp->split.Partitions[0].count[0] +
UNSUPPORTED("7e0o94342ckw0syu4j0m8vyml"); // 	   rtp->split.Partitions[0].count[1] < 64 + 1 &&
UNSUPPORTED("3kmgnkrx7ku1nnb7vprgwnfvt"); // 	   rtp->split.Partitions[0].count[0] < 64 + 1 - rtp->MinFill
UNSUPPORTED("9efzei446ga5t6kd5zstv3mat"); // 	   && rtp->split.Partitions[0].count[1] <
UNSUPPORTED("cxlhyya4bw33nn0kxhwtgj7ni"); // 	   64 + 1 - rtp->MinFill) {
UNSUPPORTED("1e60en5izveud5k83xw9vkxwy"); // 	biggestDiff = -1;
UNSUPPORTED("2x1nx9nsne4x3ygmcywl1m3og"); // 	for (i = 0; i < 64 + 1; i++) {
UNSUPPORTED("4c65v1kbz8jvl3tmy8gyj66qi"); // 	    if (!rtp->split.Partitions[0].taken[i]) {
UNSUPPORTED("6qli7mcgtgb1ne6pl33r3okkj"); // 		Rect_t rect;
UNSUPPORTED("9tqgad4ybio0vj8j5qgol8sqe"); // 		r = &rtp->split.BranchBuf[i].rect;
UNSUPPORTED("9xitbkq1bfy3lqwrboyzd3wk0"); // 		/* growth0 = RectArea(&CombineRect(r,
UNSUPPORTED("bqznmsnmx0j2z1sfi1s0rtx0t"); // 		   &rtp->split.Partitions[0].cover[0])) -
UNSUPPORTED("6tdjjnpjjde476wvn4ez9gy0h"); // 		   rtp->split.Partitions[0].area[0];
UNSUPPORTED("edw1ss2r31mqgsvca2gowi52v"); // 		 */
UNSUPPORTED("b2awl1bpq2krcq7yjahwzc77s"); // 		/* growth1 = RectArea(&CombineRect(r,
UNSUPPORTED("bkjabgaskndtlequuc8ahat39"); // 		   &rtp->split.Partitions[0].cover[1])) -
UNSUPPORTED("567lkxsqa5bu7d4nibvr4tpe1"); // 		   rtp->split.Partitions[0].area[1];
UNSUPPORTED("edw1ss2r31mqgsvca2gowi52v"); // 		 */
UNSUPPORTED("5046nw6shbhk2m5dtt3itg1qf"); // 		rect = CombineRect(r, &rtp->split.Partitions[0].cover[0]);
UNSUPPORTED("nc6om9tl3e7nixzkvrf0etup"); // 		growth0 =
UNSUPPORTED("ed7mreakvazgougj1ls6ioj33"); // 		    RectArea(&rect) - rtp->split.Partitions[0].area[0];
UNSUPPORTED("eh4bst7jlzxrquw8wrweiosfn"); // 		rect = CombineRect(r, &rtp->split.Partitions[0].cover[1]);
UNSUPPORTED("9jwgrynpgu0d39tmz3uvb0f5o"); // 		growth1 =
UNSUPPORTED("i0duy3nph3k4msny0ibs81ph"); // 		    RectArea(&rect) - rtp->split.Partitions[0].area[1];
UNSUPPORTED("b9ktilnf5m1wc70yqwf4mdq9e"); // 		diff = growth1 - growth0;
UNSUPPORTED("81ij2fgplw64rbpouql9na7a1"); // 		if (diff >= 0)
UNSUPPORTED("b49dtkykbufvv3lk5trobmbw5"); // 		    group = 0;
UNSUPPORTED("d28blrbmwwqp80cyksuz7dwx9"); // 		else {
UNSUPPORTED("7sge254zb30sgd45k1os7fw3y"); // 		    group = 1;
UNSUPPORTED("adlw9es1w2znqr4h71hd9k1j8"); // 		    diff = -diff;
UNSUPPORTED("6eq5kf0bj692bokt0bixy1ixh"); // 		}
UNSUPPORTED("7qd8i0mpxxu4wueggpmmx9704"); // 		if (diff > biggestDiff) {
UNSUPPORTED("cc2jxkxp6wf0y4a6er7iildb4"); // 		    biggestDiff = diff;
UNSUPPORTED("4gq0kbfwv4ls5gbqaxqusd6k"); // 		    chosen = i;
UNSUPPORTED("dwalhyg0t90zo8gdq7pstef8a"); // 		    betterGroup = group;
UNSUPPORTED("71bce3js2b0lpi50misxff7uy"); // 		} else if (diff == biggestDiff &&
UNSUPPORTED("fjvt87on1mhn9dyt0b9vp3jg"); // 			   rtp->split.Partitions[0].count[group] <
UNSUPPORTED("1v2g0u9t6ou9xeajwcl3u2nr4"); // 			   rtp->split.Partitions[0].count[betterGroup]) {
UNSUPPORTED("4gq0kbfwv4ls5gbqaxqusd6k"); // 		    chosen = i;
UNSUPPORTED("dwalhyg0t90zo8gdq7pstef8a"); // 		    betterGroup = group;
UNSUPPORTED("6eq5kf0bj692bokt0bixy1ixh"); // 		}
UNSUPPORTED("6t98dcecgbvbvtpycwiq2ynnj"); // 	    }
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("967zxvkt7liyzfgjy03xrpwf2"); // 	Classify(rtp, chosen, betterGroup);
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("15gmufvsx34eo7l2gxmf9sjph"); //     /* if one group too full, put remaining rects in the other */
UNSUPPORTED("bz0f1s1b90c4mqpxr24oamywd"); //     if (rtp->split.Partitions[0].count[0] +
UNSUPPORTED("d6a1dolgcf9n4go7fsk616dlz"); // 	rtp->split.Partitions[0].count[1] < 64 + 1) {
UNSUPPORTED("9xvua0lzynx3u05pjeoibo9tt"); // 	group = 0;
UNSUPPORTED("4edp65b21liyii0fj1ikco7o0"); // 	if (rtp->split.Partitions[0].count[0] >=
UNSUPPORTED("20lpsuiyepr2ujozaf6gp4cc"); // 	    64 + 1 - rtp->MinFill)
UNSUPPORTED("9qtt6i40h8vtjp2cvqyb8ycaz"); // 	    group = 1;
UNSUPPORTED("2x1nx9nsne4x3ygmcywl1m3og"); // 	for (i = 0; i < 64 + 1; i++) {
UNSUPPORTED("jdepsnmrs3ghh78ql301sfvu"); // 	    if (!rtp->split.Partitions[0].taken[i])
UNSUPPORTED("gqfhorilvtlogp6f2ozx5akf"); // 		Classify(rtp, i, group);
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("5r5xug64f1z60bggncz1pne3f"); //     assert(rtp->split.Partitions[0].count[0] +
UNSUPPORTED("1whhfnenwvqqzfd4kkosc09z"); // 	   rtp->split.Partitions[0].count[1] == 64 + 1);
UNSUPPORTED("c4k8arobh90w9e9sk4s8zx6a5"); //     assert(rtp->split.Partitions[0].count[0] >= rtp->MinFill
UNSUPPORTED("2lio9rd6ztigudntp9gohkge9"); // 	   && rtp->split.Partitions[0].count[1] >= rtp->MinFill);
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 8rui4cun4tvq5xy6ke6r3p55e
// static void PickSeeds(RTree_t * rtp) 
public static Object PickSeeds(Object... arg) {
UNSUPPORTED("4lrul9ern8gc76sno14wqgtne"); // static void PickSeeds(RTree_t * rtp)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("vshrs98l6la9fwui89y20ji3"); //   register int i, j;
UNSUPPORTED("cz82o6423cha33x8v31g7lhso"); //   unsigned int waste, worst;
UNSUPPORTED("8glrb479bftuwm5m4w4eqy3id"); //   int seed0, seed1;
UNSUPPORTED("em2tlievesov4lr9nkas30k2p"); //   unsigned int area[64 + 1];
UNSUPPORTED("1rer9hfsfz4to936cdqosy26h"); //     for (i = 0; i < 64 + 1; i++)
UNSUPPORTED("2f701q18yaasbx3zewp4pc3i3"); // 	area[i] = RectArea(&rtp->split.BranchBuf[i].rect);
UNSUPPORTED("5aj4ab555713s4o7hsg305y2c"); //     //worst = -rtp->split.CoverSplitArea - 1;
UNSUPPORTED("b2ayju4kcpmgel4or2zbz0dyr"); //     worst=0;
UNSUPPORTED("82kuymnzv3rubrwutx9bcz4xs"); //     for (i = 0; i < 64; i++) {
UNSUPPORTED("dw3tar5d5xv4kxcnkcevzm9iw"); // 	for (j = i + 1; j < 64 + 1; j++) {
UNSUPPORTED("4wt347mvef1i2kr5h5z9l0g8i"); // 	    Rect_t rect;
UNSUPPORTED("d5jmdkycgoaq3d846xryh0wtz"); // 	    /* waste = RectArea(&CombineRect(&rtp->split.BranchBuf[i].rect,
UNSUPPORTED("1d3xkv54rko81ltklwjdtrmj1"); // 	       //                  &rtp->split.BranchBuf[j].rect)) - area[i] - area[j];
UNSUPPORTED("20m1lc1moer8x00tx9ceto0iw"); // 	     */
UNSUPPORTED("8vf1f6q13lthe8sasqcvpatr3"); // 	    rect = CombineRect(&rtp->split.BranchBuf[i].rect,
UNSUPPORTED("5wjck74u7gv0xbv7a01rp3eia"); // 			       &rtp->split.BranchBuf[j].rect);
UNSUPPORTED("bzjsjoq354eduv4f511rmjmtk"); // 	    waste = RectArea(&rect) - area[i] - area[j];
UNSUPPORTED("7g3m0cplrcsffy7f3iqsrwztc"); // 	    if (waste > worst) {
UNSUPPORTED("1irem1gz77fx5eym990j8l9v5"); // 		worst = waste;
UNSUPPORTED("29q6pzycm1gb2gukvicejmmks"); // 		seed0 = i;
UNSUPPORTED("11wyv1lzvxs0lyd59xqrp77lb"); // 		seed1 = j;
UNSUPPORTED("6t98dcecgbvbvtpycwiq2ynnj"); // 	    }
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("c1o6jr885s70ej9txo4zjtk3k"); //     Classify(rtp, seed0, 0);
UNSUPPORTED("bf0ov5eigcyw1mhs6rlnwt7g"); //     Classify(rtp, seed1, 1);
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 4qyy2dpbkziuubssvfwb8u1sh
// static void Classify(RTree_t * rtp, int i, int group) 
public static Object Classify(Object... arg) {
UNSUPPORTED("8ml9djski25i25i59wyn3dld9"); // static void Classify(RTree_t * rtp, int i, int group)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("2ulo1dpl3kexdah5mcmnqpzdl"); //     assert(!rtp->split.Partitions[0].taken[i]);
UNSUPPORTED("i4edl48bp8pljcmtxd1rep7a"); //     rtp->split.Partitions[0].partition[i] = group;
UNSUPPORTED("ackdd63x1sev2rjvlsh0xgqbo"); //     rtp->split.Partitions[0].taken[i] = (!(0));
UNSUPPORTED("7bethw7ebtjrhmrrq3004xvhh"); //     if (rtp->split.Partitions[0].count[group] == 0)
UNSUPPORTED("cyhkvve52l3mzao5x0u5df13f"); // 	rtp->split.Partitions[0].cover[group] =
UNSUPPORTED("9mprl7dpvobrbs232soqoyv62"); // 	    rtp->split.BranchBuf[i].rect;
UNSUPPORTED("div10atae09n36x269sl208r1"); //     else
UNSUPPORTED("cyhkvve52l3mzao5x0u5df13f"); // 	rtp->split.Partitions[0].cover[group] =
UNSUPPORTED("7ez406d998w0vpfm3e5tbc0qq"); // 	    CombineRect(&rtp->split.BranchBuf[i].rect,
UNSUPPORTED("e0r4nqeeu1jmir6akus7bjggm"); // 			&rtp->split.Partitions[0].cover[group]);
UNSUPPORTED("3n9txb7nb3kh2v0bsy0fufx1e"); //     rtp->split.Partitions[0].area[group] =
UNSUPPORTED("anowu5wmi1sw0e8v669a1obek"); // 	RectArea(&rtp->split.Partitions[0].cover[group]);
UNSUPPORTED("d8ahf9tcusfmm5zeecg8pmdxb"); //     rtp->split.Partitions[0].count[group]++;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 ay7l4setwyl3hbx4o2jpa7vyz
// static void LoadNodes(RTree_t * rtp, Node_t * n, Node_t * q, 		      struct PartitionVars *p) 
public static Object LoadNodes(Object... arg) {
UNSUPPORTED("d0dvfornj5165j53js6eyidii"); // static void LoadNodes(RTree_t * rtp, Node_t * n, Node_t * q,
UNSUPPORTED("an9dlrefy1hbusq999x8s9qgf"); // 		      struct PartitionVars *p)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("pp6gyv6pecd6kik4hoguluwp"); //     register int i;
UNSUPPORTED("25fhmphgra40j368i6b09n5xz"); //     assert(n);
UNSUPPORTED("30uxqb850wr1s0f0jg8uipd4m"); //     assert(q);
UNSUPPORTED("eh686hxootmu11yk2foc2tmr2"); //     assert(p);
UNSUPPORTED("83olszqrugs1dppzl6flgshr5"); //     for (i = 0; i < 64 + 1; i++) {
UNSUPPORTED("46lk8hxsutpzodwl5udrq4tik"); // 	assert(rtp->split.Partitions[0].partition[i] == 0 ||
UNSUPPORTED("8jebrifw0oq30vs4i2wf1cbse"); // 	       rtp->split.Partitions[0].partition[i] == 1);
UNSUPPORTED("56sxhs1suv0k6iod6sl1w72dw"); // 	if (rtp->split.Partitions[0].partition[i] == 0)
UNSUPPORTED("cwda9vt3f43bi23igyzgossds"); // 	    AddBranch(rtp, &rtp->split.BranchBuf[i], n, (void *)0);
UNSUPPORTED("9m8w97hthfczabpgdcm7xah33"); // 	else if (rtp->split.Partitions[0].partition[i] == 1)
UNSUPPORTED("2f02d8m6p439gevwt7mxtyo4p"); // 	    AddBranch(rtp, &rtp->split.BranchBuf[i], q, (void *)0);
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 dvgjc83sogjhzf5kxpir405rh
// static void InitPVars(RTree_t * rtp) 
public static Object InitPVars(Object... arg) {
UNSUPPORTED("18v3kgq3ud3q6c0nu07xyv8wa"); // static void InitPVars(RTree_t * rtp)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("pp6gyv6pecd6kik4hoguluwp"); //     register int i;
UNSUPPORTED("2yvv0x8uir74h718l313i7it6"); //     rtp->split.Partitions[0].count[0] = rtp->split.Partitions[0].count[1] =
UNSUPPORTED("556sry61ttzbp13quuzhes74v"); // 	0;
UNSUPPORTED("bvhvtlpo10lxy8rquegyfg5jb"); //     rtp->split.Partitions[0].cover[0] = rtp->split.Partitions[0].cover[1] =
UNSUPPORTED("5bf9thav82syw0gzxp8b9p5ou"); // 	NullRect();
UNSUPPORTED("8avyuhj51jxnpeujd7fmgcgtq"); //     rtp->split.Partitions[0].area[0] = rtp->split.Partitions[0].area[1] =
UNSUPPORTED("556sry61ttzbp13quuzhes74v"); // 	0;
UNSUPPORTED("83olszqrugs1dppzl6flgshr5"); //     for (i = 0; i < 64 + 1; i++) {
UNSUPPORTED("5dttrbxrjqa1omiz2oovpe18k"); // 	rtp->split.Partitions[0].taken[i] = (0);
UNSUPPORTED("324vwwpg2hf07ejcj9w02evkm"); // 	rtp->split.Partitions[0].partition[i] = -1;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}


}
