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
import static gen.lib.label.rectangle__c.CombineRect;
import static gen.lib.label.rectangle__c.InitRect;
import static gen.lib.label.rectangle__c.RectArea;
import static gen.lib.label.split_q__c.SplitNode;
import static smetana.core.JUtilsDebug.ENTERING;
import static smetana.core.JUtilsDebug.LEAVING;
import static smetana.core.Macro.UNSUPPORTED;
import h.ST_Branch_t;
import h.ST_Node_t___;
import h.ST_RTree;
import h.ST_Rect_t;

public class node__c {


//3 9uj7ni1m6q6drtoh56w82d6m4
// Node_t *RTreeNewNode(RTree_t * rtp) 
public static ST_Node_t___ RTreeNewNode(ST_RTree rtp) {
ENTERING("9uj7ni1m6q6drtoh56w82d6m4","RTreeNewNode");
try {
	ST_Node_t___ n;
	rtp.setInt("NodeCount", rtp.NodeCount + 1);
	n = new ST_Node_t___();
     InitNode(n);
     return n;
} finally {
LEAVING("9uj7ni1m6q6drtoh56w82d6m4","RTreeNewNode");
}
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
public static void InitNode(ST_Node_t___ n) {
ENTERING("4qk9wkm05q2pwf20ud6g2tufg","InitNode");
try {
     int i;
     n.setInt("count", 0);
     n.setInt("level", -1);
     for (i = 0; i < 64; i++)
    	 InitBranch(n.branch[i]);
     // InitBranch(&(n->branch[i]));
} finally {
LEAVING("4qk9wkm05q2pwf20ud6g2tufg","InitNode");
}
}




//3 ruhxixxei7au9z1iaj0zggwo
// void InitBranch(Branch_t * b) 
public static void InitBranch(ST_Branch_t b) {
ENTERING("ruhxixxei7au9z1iaj0zggwo","InitBranch");
try {
	InitRect(b.rect);
	b.child = null;
} finally {
LEAVING("ruhxixxei7au9z1iaj0zggwo","InitBranch");
}
}




//3 42vjqe8n5yeq2jjby00xzrotk
// Rect_t NodeCover(Node_t * n) 
public static ST_Rect_t NodeCover(ST_Node_t___ n) {
ENTERING("42vjqe8n5yeq2jjby00xzrotk","NodeCover");
try {
     int i, flag;
     final ST_Rect_t r = new ST_Rect_t();
     //     assert(n);
     InitRect(r);
     flag = 1;
     for (i = 0; i < 64; i++)
 	if (n.branch[i].child!=null) {
 	    if (flag!=0) {
 		r.___(n.branch[i].rect);
 		flag = 0;
 	    } else
 		r.___(CombineRect(r, (ST_Rect_t) n.branch[i].rect));
 	}
     return r;
} finally {
LEAVING("42vjqe8n5yeq2jjby00xzrotk","NodeCover");
}
}




//3 bek56v2skz6jfvw4uggy2h5w3
// int PickBranch(Rect_t * r, Node_t * n) 
public static int PickBranch(ST_Rect_t r, ST_Node_t___ n) {
	ENTERING("bek56v2skz6jfvw4uggy2h5w3","PickBranch");
	try {
     ST_Rect_t rr=null;
     int i=0, flag=1, increase=0, bestIncr=0, area=0, bestArea=0;
     int best=0;
//     assert(r && n);
     for (i = 0; i < 64; i++) {
 	if (n.branch[i].child!=null) {
 	    final ST_Rect_t rect = new ST_Rect_t();
 	    rr = (ST_Rect_t) n.branch[i].rect;
 	    area = RectArea((ST_Rect_t) rr);
 	    /* increase = RectArea(&CombineRect(r, rr)) - area; */
 	    rect.___(CombineRect((ST_Rect_t) r, rr));
 	    increase = RectArea((ST_Rect_t) rect) - area;
 	    if (increase < bestIncr || flag!=0) {
 		best = i;
 		bestArea = area;
 		bestIncr = increase;
 		flag = 0;
 	    } else if (increase == bestIncr && area < bestArea) {
 		best = i;
 		bestArea = area;
 		bestIncr = increase;
 	    }
 	}
     }
     return best;
} finally {
LEAVING("bek56v2skz6jfvw4uggy2h5w3","PickBranch");
}
}




//3 2njctcrpeff95ysmv9ji34x4s
// int AddBranch(RTree_t * rtp, Branch_t * b, Node_t * n, Node_t ** new) 
public static int AddBranch(ST_RTree rtp, ST_Branch_t b, ST_Node_t___ n, ST_Node_t___ new_[]) {
ENTERING("2njctcrpeff95ysmv9ji34x4s","AddBranch");
try {
     int i;
//     assert(b);
//     assert(n);
     if (n.count < 64) {	/* split won't be necessary */
 	for (i = 0; i < 64; i++) {	/* find empty branch */
 	    if (n.branch[i].child == null) {
 		n.branch[i].___(b.getStruct());
 		n.setInt("count", n.count+1);
 		break;
 	    }
}
// 	assert(i < 64);
 	return 0;
     } else {
 	if (rtp.StatFlag!=0) {
UNSUPPORTED("2y8kv6b3ysrr61q7tqn76rhhc"); // 	    if (rtp->Deleting)
UNSUPPORTED("dn4oynmx9ugizzs5pkxiyptbi"); // 		rtp->DeTouchCount++;
UNSUPPORTED("5c97f6vfxny0zz35l2bu4maox"); // 	    else
UNSUPPORTED("2u8wpa4w1q7rg14t07bny6p8i"); // 		rtp->InTouchCount++;
 	}
// 	assert(new);
 	SplitNode(rtp, n, b, new_);
 	if (n.level == 0)
 		rtp.setInt("LeafCount", rtp.LeafCount+1);
 	else
UNSUPPORTED("6tkfiebspy7ecivrzb3l5y7jd"); // 	    rtp->NonLeafCount++;
 	return 1;
     }
} finally {
LEAVING("2njctcrpeff95ysmv9ji34x4s","AddBranch");
}
}




//3 eqzamflj58f43cflwns9cemnk
// void DisconBranch(Node_t * n, int i) 
public static void DisconBranch(ST_Node_t___ n, int i) {
ENTERING("eqzamflj58f43cflwns9cemnk","DisconBranch");
try {
//     assert(n && i >= 0 && i < 64);
//     assert(n->branch[i].child);
     InitBranch(n.branch[i]);
     n.setInt("count", n.count-1);
} finally {
LEAVING("eqzamflj58f43cflwns9cemnk","DisconBranch");
}
}


}
