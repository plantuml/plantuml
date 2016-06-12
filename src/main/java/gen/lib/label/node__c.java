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

public class node__c {


//3 9uj7ni1m6q6drtoh56w82d6m4
// Node_t *RTreeNewNode(RTree_t * rtp) 
public static Object RTreeNewNode(Object... arg) {
UNSUPPORTED("6r87cuk0qgu4wqagr86fr5tts"); // Node_t *RTreeNewNode(RTree_t * rtp)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("7wl3hkaktfhqdqsm6ubxboo1q"); //     register Node_t *n;
UNSUPPORTED("4bwg40kiiowl2obspxcr4saa2"); //     rtp->NodeCount++;
UNSUPPORTED("azoy4bfuupxwp4mi8hfbfb78g"); //     n = (Node_t *) malloc(sizeof(Node_t));
UNSUPPORTED("a4ey5uckjqallol1ktyqe35bv"); //     InitNode(n);
UNSUPPORTED("69hc24ic55i66g8tf2ne42327"); //     return n;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 65wa5vy8i5k40218lbhdibrjx
// void RTreeFreeNode(RTree_t * rtp, Node_t * p) 
public static Object RTreeFreeNode(Object... arg) {
UNSUPPORTED("e9yu9bfc7a1ihpoc5axpyg4eg"); // void RTreeFreeNode(RTree_t * rtp, Node_t * p)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("2jtyh6hx1w2fcx2gjs8ksbkuh"); //     rtp->NodeCount--;
UNSUPPORTED("ed5n0l2c9xevohsgtsmv822gw"); //     if (p->level == 0)
UNSUPPORTED("8pbkap1jra9u3gvgio7ou7y1n"); // 	rtp->LeafCount--;
UNSUPPORTED("div10atae09n36x269sl208r1"); //     else
UNSUPPORTED("357domcrb707wvdox73ayvdj1"); // 	rtp->NonLeafCount--;
UNSUPPORTED("bo0y3vz195pcz24vm46pixpb2"); //     free(p);
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 4qk9wkm05q2pwf20ud6g2tufg
// void InitNode(Node_t * n) 
public static Object InitNode(Object... arg) {
UNSUPPORTED("dlpky13v9fa11z9uukagldaj1"); // void InitNode(Node_t * n)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("pp6gyv6pecd6kik4hoguluwp"); //     register int i;
UNSUPPORTED("7rbjc9psqd4hoib2lezl7pnh"); //     n->count = 0;
UNSUPPORTED("55ukro5lb2mre4owzaww8q2hc"); //     n->level = -1;
UNSUPPORTED("8v5nz5apd36odx9nwrk8p8jow"); //     for (i = 0; i < 64; i++)
UNSUPPORTED("80i9o3c1pvhadp8xg5nka4k0z"); // 	InitBranch(&(n->branch[i]));
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 ruhxixxei7au9z1iaj0zggwo
// void InitBranch(Branch_t * b) 
public static Object InitBranch(Object... arg) {
UNSUPPORTED("2ds4g44o2u9jlcjegxiplx78k"); // void InitBranch(Branch_t * b)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("7tixqnx33892tw39ys6j048c9"); //     InitRect(&(b->rect));
UNSUPPORTED("6rffpszutr9tr32hwasnosx1l"); //     b->child = NULL;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 42vjqe8n5yeq2jjby00xzrotk
// Rect_t NodeCover(Node_t * n) 
public static Object NodeCover(Object... arg) {
UNSUPPORTED("2khwamjri7uz3vu8fhl4bz8yr"); // Rect_t NodeCover(Node_t * n)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("e4wfnyowhq6r7cll9ebbr5pcr"); //     register int i, flag;
UNSUPPORTED("9jotn4njsd13qx406m9otorg4"); //     Rect_t r;
UNSUPPORTED("25fhmphgra40j368i6b09n5xz"); //     assert(n);
UNSUPPORTED("7t0m2a3824uy7f5r4wu3p2no9"); //     InitRect(&r);
UNSUPPORTED("e0yg2emzy8u4jgn9z7jdfpziq"); //     flag = 1;
UNSUPPORTED("8v5nz5apd36odx9nwrk8p8jow"); //     for (i = 0; i < 64; i++)
UNSUPPORTED("1ro7ykl3rxfkkcyghzozikkfc"); // 	if (n->branch[i].child) {
UNSUPPORTED("ycid67impnxkwa9mvvkvdu0q"); // 	    if (flag) {
UNSUPPORTED("ektombc80083wiu0lsj6kto83"); // 		r = n->branch[i].rect;
UNSUPPORTED("6bejndin7u1ns7xicz114gqtn"); // 		flag = 0;
UNSUPPORTED("afk9bpom7x393euamnvwwkx6b"); // 	    } else
UNSUPPORTED("31n3s0xk5l7s3rhi9xeraiklo"); // 		r = CombineRect(&r, &(n->branch[i].rect));
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("a2hk6w52njqjx48nq3nnn2e5i"); //     return r;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 bek56v2skz6jfvw4uggy2h5w3
// int PickBranch(Rect_t * r, Node_t * n) 
public static Object PickBranch(Object... arg) {
UNSUPPORTED("efxc8619milx7pkmu113b8cl0"); // int PickBranch(Rect_t * r, Node_t * n)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("4rx3my7ninlw252mkhq0t72v7"); //     register Rect_t *rr=0;
UNSUPPORTED("cu5ueogqdqm7ym79nlop077f9"); //     register int i=0, flag=1, increase=0, bestIncr=0, area=0, bestArea=0;
UNSUPPORTED("b5pkdhy36omd5ubfol2jsyr2z"); //     int best=0;
UNSUPPORTED("3lp95fcfxfbsb08w5umj6mjxb"); //     assert(r && n);
UNSUPPORTED("82kuymnzv3rubrwutx9bcz4xs"); //     for (i = 0; i < 64; i++) {
UNSUPPORTED("1ro7ykl3rxfkkcyghzozikkfc"); // 	if (n->branch[i].child) {
UNSUPPORTED("4wt347mvef1i2kr5h5z9l0g8i"); // 	    Rect_t rect;
UNSUPPORTED("ak8qz4z2mipw1i6img1tvc3hk"); // 	    rr = &n->branch[i].rect;
UNSUPPORTED("d4pnse7tksvywt9f6rcaluv8e"); // 	    area = RectArea(rr);
UNSUPPORTED("4wsrjtivlov1p70rt8tn4dt9v"); // 	    /* increase = RectArea(&CombineRect(r, rr)) - area; */
UNSUPPORTED("8b9or9d0f959zrx6zrd0o9noj"); // 	    rect = CombineRect(r, rr);
UNSUPPORTED("a1p1sz58nzxnj53fxzzkut6cm"); // 	    increase = RectArea(&rect) - area;
UNSUPPORTED("6k6tiifs973t6iordeclfqygk"); // 	    if (increase < bestIncr || flag) {
UNSUPPORTED("93dpfupbht4p3y9bsui3n8km4"); // 		best = i;
UNSUPPORTED("apb6b50as71d87ge91yfjwr33"); // 		bestArea = area;
UNSUPPORTED("45tyhbogdetuq767acgw0cvyg"); // 		bestIncr = increase;
UNSUPPORTED("6bejndin7u1ns7xicz114gqtn"); // 		flag = 0;
UNSUPPORTED("1wqm4msrcepk5ixg5ab8polj7"); // 	    } else if (increase == bestIncr && area < bestArea) {
UNSUPPORTED("93dpfupbht4p3y9bsui3n8km4"); // 		best = i;
UNSUPPORTED("apb6b50as71d87ge91yfjwr33"); // 		bestArea = area;
UNSUPPORTED("45tyhbogdetuq767acgw0cvyg"); // 		bestIncr = increase;
UNSUPPORTED("6t98dcecgbvbvtpycwiq2ynnj"); // 	    }
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("3kkxvbiqcsdmtjhhw3b2jcqcy"); //     return best;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 2njctcrpeff95ysmv9ji34x4s
// int AddBranch(RTree_t * rtp, Branch_t * b, Node_t * n, Node_t ** new) 
public static Object AddBranch(Object... arg) {
UNSUPPORTED("abswgz0jexhhyl0we1x68i5gl"); // int AddBranch(RTree_t * rtp, Branch_t * b, Node_t * n, Node_t ** new)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("pp6gyv6pecd6kik4hoguluwp"); //     register int i;
UNSUPPORTED("5xtwc50ov4mxby7k6gahwv12t"); //     assert(b);
UNSUPPORTED("25fhmphgra40j368i6b09n5xz"); //     assert(n);
UNSUPPORTED("8irlja6ar7hjh8mel91hltz17"); //     if (n->count < 64) {	/* split won't be necessary */
UNSUPPORTED("8vlryher8oaw4h715yfzs4h0"); // 	for (i = 0; i < 64; i++) {	/* find empty branch */
UNSUPPORTED("3eaeba8b8yt8gq9074wtep0e0"); // 	    if (n->branch[i].child == NULL) {
UNSUPPORTED("9newc1m7r062crcakg2dn00kp"); // 		n->branch[i] = *b;
UNSUPPORTED("b4orzj362grzvud5mj73w1cb1"); // 		n->count++;
UNSUPPORTED("9ekmvj13iaml5ndszqyxa8eq"); // 		break;
UNSUPPORTED("6t98dcecgbvbvtpycwiq2ynnj"); // 	    }
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("2gjzrafpfll8afj90mhedcnnq"); // 	assert(i < 64);
UNSUPPORTED("c9ckhc8veujmwcw0ar3u3zld4"); // 	return 0;
UNSUPPORTED("c07up7zvrnu2vhzy6d7zcu94g"); //     } else {
UNSUPPORTED("dhd7sfbis7klukcyf8rg2skvp"); // 	if (rtp->StatFlag) {
UNSUPPORTED("2y8kv6b3ysrr61q7tqn76rhhc"); // 	    if (rtp->Deleting)
UNSUPPORTED("dn4oynmx9ugizzs5pkxiyptbi"); // 		rtp->DeTouchCount++;
UNSUPPORTED("5c97f6vfxny0zz35l2bu4maox"); // 	    else
UNSUPPORTED("2u8wpa4w1q7rg14t07bny6p8i"); // 		rtp->InTouchCount++;
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("chipmsvc1hum7sbmvzrfmewz6"); // 	assert(new);
UNSUPPORTED("91mdffv90jr6ypnu3u1bmc972"); // 	SplitNode(rtp, n, b, new);
UNSUPPORTED("1t8cl7q3utcr23gvhtgc1cp0u"); // 	if (n->level == 0)
UNSUPPORTED("b2cc70cq7gpras4l3rbz241at"); // 	    rtp->LeafCount++;
UNSUPPORTED("9352ql3e58qs4fzapgjfrms2s"); // 	else
UNSUPPORTED("6tkfiebspy7ecivrzb3l5y7jd"); // 	    rtp->NonLeafCount++;
UNSUPPORTED("eleqpc2p2r3hvma6tipoy7tr"); // 	return 1;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 eqzamflj58f43cflwns9cemnk
// void DisconBranch(Node_t * n, int i) 
public static Object DisconBranch(Object... arg) {
UNSUPPORTED("1gbs5xw4y9htfbc5suxq1rdm4"); // void DisconBranch(Node_t * n, int i)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("ays33wicr9a5qwu2dr9g8t1h2"); //     assert(n && i >= 0 && i < 64);
UNSUPPORTED("29zl8z52z6bh8yxdkmezbs82l"); //     assert(n->branch[i].child);
UNSUPPORTED("3rvakcl9f71ez4zya83f1vvgx"); //     InitBranch(&(n->branch[i]));
UNSUPPORTED("4a3m9b5jmmhd18oa8nzovalys"); //     n->count--;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}


}
