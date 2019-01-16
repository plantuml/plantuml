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
import static gen.lib.label.node__c.AddBranch;
import static gen.lib.label.node__c.DisconBranch;
import static gen.lib.label.node__c.NodeCover;
import static gen.lib.label.node__c.PickBranch;
import static gen.lib.label.node__c.RTreeNewNode;
import static gen.lib.label.rectangle__c.CombineRect;
import static gen.lib.label.rectangle__c.Overlap;
import static smetana.core.JUtilsDebug.ENTERING;
import static smetana.core.JUtilsDebug.LEAVING;
import static smetana.core.Macro.N;
import static smetana.core.Macro.UNSUPPORTED;
import h.ST_Branch_t;
import h.ST_LeafList_t;
import h.ST_Node_t___;
import h.ST_Node_t___or_object_t;
import h.ST_RTree;
import h.ST_Rect_t;
import smetana.core.Memory;
import smetana.core.__ptr__;

public class index__c {


//3 1rfaqe5urty5uyp5xb2r0idce
// LeafList_t *RTreeNewLeafList(Leaf_t * lp) 
public static ST_LeafList_t RTreeNewLeafList(ST_Branch_t lp) {
ENTERING("1rfaqe5urty5uyp5xb2r0idce","RTreeNewLeafList");
try {
     ST_LeafList_t llp;
     llp = new ST_LeafList_t();
     llp.leaf = lp;
     llp.next = null;
          return llp;
//UNSUPPORTED("3b215c61vcll0rkorzyelp40j"); //     if ((llp = (LeafList_t*)zmalloc(sizeof(LeafList_t)))) {
//UNSUPPORTED("48u04cv4b40c0avzy99mdycx5"); // 	llp->leaf = lp;
//UNSUPPORTED("bbvk7v1s0z6yw1xdoq99v233w"); // 	llp->next = 0;
//UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
//UNSUPPORTED("5a1d3zolzdjict0gus6vz04a2"); //     return llp;
//UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }
//
//throw new UnsupportedOperationException();
} finally {
LEAVING("1rfaqe5urty5uyp5xb2r0idce","RTreeNewLeafList");
}
}




//3 6pvstz7axi8a7saeqe3nrrmg5
// LeafList_t *RTreeLeafListAdd(LeafList_t * llp, Leaf_t * lp) 
public static ST_LeafList_t RTreeLeafListAdd(ST_LeafList_t llp, ST_Branch_t lp) {
ENTERING("6pvstz7axi8a7saeqe3nrrmg5","RTreeLeafListAdd");
try {
     ST_LeafList_t nlp;
     if (N(lp))
 	return llp;
     nlp = RTreeNewLeafList(lp);
     nlp.next = llp;
     return nlp;
} finally {
LEAVING("6pvstz7axi8a7saeqe3nrrmg5","RTreeLeafListAdd");
}
}




//3 6zraor7x44vrnm19d2igkvow2
// void RTreeLeafListFree(LeafList_t * llp) 
public static void RTreeLeafListFree(ST_LeafList_t llp) {
ENTERING("6zraor7x44vrnm19d2igkvow2","RTreeLeafListFree");
try {
     ST_LeafList_t tlp;
     while (llp.next!=null) {
 	tlp = (ST_LeafList_t) llp.next;
 	Memory.free(llp);
 	llp = tlp;
     }
     Memory.free(llp);
} finally {
LEAVING("6zraor7x44vrnm19d2igkvow2","RTreeLeafListFree");
}
}

///* Allocate space for a node in the list used in DeletRect to
// * store Nodes that are too empty.
// */
//static struct ListNode *RTreeNewListNode(void)
//{
//    return (struct ListNode*)zmalloc(sizeof(struct ListNode));
//}
//
///* Add a node to the reinsertion list.  All its branches will later
// * be reinserted into the index structure.
// */
//3 aa19m7d7qc06m8id896e60lkg
//static int RTreeReInsert(RTree_t * rtp, Node_t * n, struct ListNode **ee)
//{
// WARNING!! STRUCT
//    register struct ListNode *l;
//
//    if (!(l = RTreeNewListNode()))
//	return -1;
//    l->node = n;
//    l->next = *ee;
//    *ee = l;
//    return 0;
//}
//
//3 aa29m7d7qc06m8id896e60lkg
//RTree_t *RTreeOpen()
public static __ptr__ RTreeOpen() {
ENTERING("aa29m7d7qc06m8id896e60lkg","RTreeOpen");
try {
	ST_RTree rtp;
    rtp = new ST_RTree();
    if (rtp!=null)
    	rtp.setPtr("root", RTreeNewIndex(rtp));
    return rtp;
} finally {
LEAVING("aa29m7d7qc06m8id896e60lkg","RTreeOpen");
}
}

/* Make a new index, empty.  Consists of a single node. */
//3 aa39m7d7qc06m8id896e60lkg
//Node_t *RTreeNewIndex(RTree_t * rtp)
public static ST_Node_t___ RTreeNewIndex(ST_RTree rtp) {
ENTERING("aa39m7d7qc06m8id896e60lkg","RTreeNewIndex");
try {
	ST_Node_t___ x;
	x = RTreeNewNode(rtp);
	x.setInt("level", 0); /* leaf */
	rtp.setInt("LeafCount", rtp.LeafCount+1);
	return x;
} finally {
LEAVING("aa39m7d7qc06m8id896e60lkg","RTreeNewIndex");
}
}

//3 aa49m7d7qc06m8id896e60lkg
//static int RTreeClose2(RTree_t * rtp, Node_t * n)
public static int RTreeClose2(ST_RTree rtp, ST_Node_t___ n) {
ENTERING("aa49m7d7qc06m8id896e60lkg","RTreeClose2");
try {
    int i;

    if (n.level > 0) {
	for (i = 0; i < 64; i++) {
	    if (N(n.branch[i].child))
		continue;
	    if (N(RTreeClose2(rtp, (ST_Node_t___) n.branch[i].child))) {
		Memory.free(n.branch[i].child);
		DisconBranch(n, i);
	    rtp.setInt("EntryCount", rtp.EntryCount-1);
		if (rtp.StatFlag!=0)
		    rtp.setInt("ElimCount", rtp.ElimCount+1);
	    }
	}
    } else {
	for (i = 0; i < 64; i++) {
		if (N(n.branch[i].child))
		continue;
	    // free(n->branch[i].child);
	    DisconBranch(n, i);
	    rtp.setInt("EntryCount", rtp.EntryCount-1);
	    if (rtp.StatFlag!=0)
		    rtp.setInt("ElimCount", rtp.ElimCount+1);
	}
	//free(n);
    }
    return 0;
} finally {
LEAVING("aa49m7d7qc06m8id896e60lkg","RTreeClose2");
}
}

//3 aa59m7d7qc06m8id896e60lkg
//int RTreeClose(RTree_t * rtp)
public static int RTreeClose(ST_RTree rtp) {
ENTERING("aa59m7d7qc06m8id896e60lkg","RTreeClose");
try {
    RTreeClose2(rtp, (ST_Node_t___) rtp.root);
    Memory.free(rtp.root);
    Memory.free(rtp);
    return 0;
} finally {
LEAVING("aa59m7d7qc06m8id896e60lkg","RTreeClose");
}
}


	
/* RTreeSearch in an index tree or subtree for all data retangles that
** overlap the argument rectangle.
** Returns the number of qualifying data rects.
*/
//3 aa69m7d7qc06m8id896e60lkg
//LeafList_t *RTreeSearch(RTree_t * rtp, Node_t * n, Rect_t * r)
public static ST_LeafList_t RTreeSearch(ST_RTree rtp, ST_Node_t___ n, ST_Rect_t r) {
ENTERING("aa69m7d7qc06m8id896e60lkg","RTreeSearch");
try {
	int i;
    ST_LeafList_t llp = null;

//    assert(n);
//    assert(n->level >= 0);
//    assert(r);

    rtp.setInt("SeTouchCount", rtp.SeTouchCount+1);

    if (n.level > 0) {		/* this is an internal node in the tree */
	for (i = 0; i < 64; i++)
	    if (n.branch[i].child!=null &&
	    	Overlap(r, n.branch[i].rect)) {
		ST_LeafList_t tlp = RTreeSearch(rtp, (ST_Node_t___) n.branch[i].child, r);
		if (llp!=null) {
		    ST_LeafList_t xlp = llp;
		    while (xlp.next!=null)
			xlp = (ST_LeafList_t) xlp.next;
		    xlp.next = tlp;
		} else
		    llp = tlp;
   }
    } else {			/* this is a leaf node */
	for (i = 0; i < 64; i++) {
	    if (n.branch[i].child!=null && Overlap(r, n.branch[i].rect)) {
		   llp = RTreeLeafListAdd(llp, /*(Leaf_t *)*/ n.branch[i]);
	    }
	}
    }
    return llp;
} finally {
LEAVING("aa69m7d7qc06m8id896e60lkg","RTreeSearch");
}
}

//}
//
///* Insert a data rectangle into an index structure.
//** RTreeInsert provides for splitting the root;
//** returns 1 if root was split, 0 if it was not.
//** The level argument specifies the number of steps up from the leaf
//** level to insert; e.g. a data rectangle goes in at level = 0.
//** RTreeInsert2 does the recursion.
//*/
//static int RTreeInsert2(RTree_t *, Rect_t *, void *, Node_t *, Node_t **,
//			int);
///*static int RTreeInsert2(RTree_t*, Rect_t*, int, Node_t*, Node_t**, int); */



//3 3wss9r0zgt5k06j8ovjv7hq0d
// int RTreeInsert(RTree_t * rtp, Rect_t * r, void *data, Node_t ** n, int level) 
public static int RTreeInsert(ST_RTree rtp, ST_Rect_t r, __ptr__ data, ST_Node_t___ n[], int level) {
ENTERING("3wss9r0zgt5k06j8ovjv7hq0d","RTreeInsert");
try {
     int i;
     ST_Node_t___ newroot;
     ST_Node_t___ newnode[] = new ST_Node_t___[1];
     final ST_Branch_t b = new ST_Branch_t();
     int result = 0;
//     assert(r && n);
//     assert(level >= 0 && level <= (*n)->level);
//     for (i = 0; i < 2; i++)
// 	assert(r->boundary[i] <= r->boundary[2 + i]);
if (rtp.StatFlag!=0) {
UNSUPPORTED("akhni40ndam0u9c6i7raxw4mp"); // 	if (rtp->Deleting)
UNSUPPORTED("bzb1oqc35evr96528iv16glb0"); // 	    rtp->ReInsertCount++;
UNSUPPORTED("9352ql3e58qs4fzapgjfrms2s"); // 	else
UNSUPPORTED("3kxquse3qg2crme5dzybg9jxe"); // 	    rtp->InsertCount++;
}
     if (N(rtp.Deleting))
 	rtp.setInt("RectCount", rtp.RectCount+1);
     if (RTreeInsert2(rtp, r, data, n[0], newnode, level)!=0) {	/* root was split */
 	if (rtp.StatFlag!=0) {
UNSUPPORTED("2y8kv6b3ysrr61q7tqn76rhhc"); // 	    if (rtp->Deleting)
UNSUPPORTED("dn4oynmx9ugizzs5pkxiyptbi"); // 		rtp->DeTouchCount++;
UNSUPPORTED("5c97f6vfxny0zz35l2bu4maox"); // 	    else
UNSUPPORTED("2u8wpa4w1q7rg14t07bny6p8i"); // 		rtp->InTouchCount++;
 	}
 	newroot = RTreeNewNode(rtp);	/* grow a new root, make tree taller */
 	rtp.setInt("NonLeafCount", rtp.NonLeafCount+1);
 	newroot.setInt("level", n[0].level + 1);
 	b.setStruct("rect", NodeCover(n[0]));
 	b.child = n[0];
 	AddBranch(rtp, b, newroot, null);
 	b.setStruct("rect", NodeCover(newnode[0]));
 	b.child = newnode[0];
 	AddBranch(rtp, b, newroot, null);
 	n[0] = newroot;
 	// rtp->root = newroot;
 	rtp.setInt("EntryCount", rtp.EntryCount + 2);
 	result = 1;
     }
     return result;
} finally {
LEAVING("3wss9r0zgt5k06j8ovjv7hq0d","RTreeInsert");
}
}



// public static int RTreeInsert(ST_RTree rtp, ST_Rect_t r, __ptr__ data, ST_Node_t___ n[], int level) {

//3 bsc9m7d7qc06m8id896e60lkg
// static int RTreeInsert2(RTree_t * rtp, Rect_t * r, void *data, 	     Node_t * n, Node_t ** new, int level) 
public static int RTreeInsert2(ST_RTree rtp, ST_Rect_t r, __ptr__ data, ST_Node_t___ n, ST_Node_t___ new_[], int level) {
ENTERING("bsc9m7d7qc06m8id896e60lkg","RTreeInsert2");
try {
     /*static int */
     /* RTreeInsert2(RTree_t*rtp, Rect_t*r,
        int data, Node_t*n, Node_t**new, int level) {
      */
     int i=0;
     final ST_Branch_t b = new ST_Branch_t();
     ST_Node_t___ n2[]=new ST_Node_t___[1];
//     assert(r && n && new);
//     assert(level >= 0 && level <= n->level);
     if (rtp.StatFlag!=0) {
UNSUPPORTED("akhni40ndam0u9c6i7raxw4mp"); // 	if (rtp->Deleting)
UNSUPPORTED("8k1kgaoa4b2mcye1xthc3f1kf"); // 	    rtp->DeTouchCount++;
UNSUPPORTED("9352ql3e58qs4fzapgjfrms2s"); // 	else
UNSUPPORTED("1um729vqiy3529kbsrzyl9u3y"); // 	    rtp->InTouchCount++;
     }
/* Still above level for insertion, go down tree recursively */
     if (n.level > level) {
 	i = PickBranch(r, n);
 	if (N(RTreeInsert2(rtp, r, data, (ST_Node_t___) n.branch[i].child, n2, level))) {
/* recurse: child was not split */
 	    n.branch[i].setStruct("rect",
 	    		CombineRect((ST_Rect_t)r, (ST_Rect_t) n.branch[i].rect));
 	    return 0;
 	} else {		/* child was split */
UNSUPPORTED("7evrfdq7uc1smqislqm9d82l6"); // 	    n->branch[i].rect = NodeCover(n->branch[i].child);
UNSUPPORTED("echuth2qnq0o4n5gkzgtu5bgs"); // 	    b.child = n2;
UNSUPPORTED("50z4r9qcomgi4o7vvwq0v0xs"); // 	    b.rect = NodeCover(n2);
UNSUPPORTED("451qw2ioqybj69k9abzvqw4mk"); // 	    rtp->EntryCount++;
UNSUPPORTED("9uz11nbvh6yp6yq2axvo7e0fb"); // 	    return AddBranch(rtp, &b, n, new);
 	}
     } else if (n.level == level) {	/* at level for insertion. */
 	/*Add rect, split if necessary */
 	b.rect.___(r);
 	b.child = /*(Node_t *)*/(ST_Node_t___or_object_t) data; // THIS CAST IS A BIG ISSUE
// UNSUPPORTED("7w1b5nw2bj3zmo70m9bczwwov"); // 	b.child = (Node_t *) data;
 	rtp.setInt("EntryCount", rtp.EntryCount+1);
 	return AddBranch(rtp, b, n, new_);
     } else {			/* Not supposed to happen */
UNSUPPORTED("22oqraxnqrjall7fj6pooexmi"); // 	assert((0));
UNSUPPORTED("c9ckhc8veujmwcw0ar3u3zld4"); // 	return 0;
     }
throw new UnsupportedOperationException();
} finally {
LEAVING("bsc9m7d7qc06m8id896e60lkg","RTreeInsert2");
}
}




//3 eybi74pqddw71yno71n1dxch1
// static void FreeListNode(register struct ListNode *p) 
public static Object FreeListNode(Object... arg) {
UNSUPPORTED("enkn7pc4meks3igihpafaoxnl"); // static void FreeListNode(register struct ListNode *p)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("bo0y3vz195pcz24vm46pixpb2"); //     free(p);
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 a4tq9skwvzdutka9ei6pbydrk
// int RTreeDelete(RTree_t * rtp, Rect_t * r, void *data, Node_t ** nn) 
public static Object RTreeDelete(Object... arg) {
UNSUPPORTED("dxan13j7zc5tysdskndrhp0jy"); // int RTreeDelete(RTree_t * rtp, Rect_t * r, void *data, Node_t ** nn)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("a12nb5dbhoiu403g443fctkns"); //     /* int */
UNSUPPORTED("db7nkyc7g4zod4pcosw0eosmi"); //     /* RTreeDelete(RTree_t*rtp, Rect_t*r, int data, Node_t**nn) { */
UNSUPPORTED("pp6gyv6pecd6kik4hoguluwp"); //     register int i;
UNSUPPORTED("ben8zsxtpzeiqo8eli2l8uwl0"); //     register Node_t *t;
UNSUPPORTED("5pdj91y3888dsu9aiv163adqi"); //     struct ListNode *reInsertList = (void *)0;
UNSUPPORTED("ej513ottpyeoq8nr5ek3cqnyu"); //     register struct ListNode *e;
UNSUPPORTED("9fgs287rhe40d3q36tlijajk3"); //     assert(r && nn);
UNSUPPORTED("djo1drf8zqgd4dlmtnp8268ma"); //     assert(*nn);
UNSUPPORTED("aickurv6sbkajrl6u32h8s7n0"); //     assert(data >= 0);
UNSUPPORTED("d7p6had3bhs1yux0acgomhxcq"); //     rtp->Deleting = (!(0));
UNSUPPORTED("607lavmustb15rxzq2849utq7"); //     if (!RTreeDelete2(rtp, r, data, *nn, &reInsertList)) {
UNSUPPORTED("78928a78vetdygse25vd6qw33"); // 	/* found and deleted a data item */
UNSUPPORTED("bi6ay2t4s62zfticozlr791yq"); // 	if (rtp->StatFlag)
UNSUPPORTED("aiqjtlu1lbfame9tsdduxksph"); // 	    rtp->DeleteCount++;
UNSUPPORTED("337cl2y5ymgyje4han1uiddia"); // 	rtp->RectCount--;
UNSUPPORTED("dm3fsqsr312twcwr7ejz72sd5"); // 	/* reinsert any branches from eliminated nodes */
UNSUPPORTED("3miqozg3j3sz0440ya0edtkn1"); // 	while (reInsertList) {
UNSUPPORTED("8bi7drdkfhozlzpnr9a8beo9r"); // 	    t = reInsertList->node;
UNSUPPORTED("ehzcswv2o675ah20nlk8ll73b"); // 	    for (i = 0; i < 64; i++) {
UNSUPPORTED("16k4a0oof3m3shq22rp10sufm"); // 		if (t->branch[i].child) {
UNSUPPORTED("4pmqw3ejoeiktbmgw5li4osuk"); // 		    RTreeInsert(rtp, &(t->branch[i].rect),
UNSUPPORTED("jn668g7qkg8vdom23tf6xyua"); // 				/* (int)t->branch[i].child, nn, t->level); */
UNSUPPORTED("cr2tz58k73uenuatj8dllr5e"); // 				t->branch[i].child, nn, t->level);
UNSUPPORTED("3ll931j526x4h3iq2n8lb8npl"); // 		    rtp->EntryCount--;
UNSUPPORTED("6eq5kf0bj692bokt0bixy1ixh"); // 		}
UNSUPPORTED("6t98dcecgbvbvtpycwiq2ynnj"); // 	    }
UNSUPPORTED("a0driook10zxazzgo71kxwf5t"); // 	    e = reInsertList;
UNSUPPORTED("3ycjjmywhr2h58szv9f0c3r67"); // 	    reInsertList = reInsertList->next;
UNSUPPORTED("2l03pg61762f3m81wcifv6o2t"); // 	    RTreeFreeNode(rtp, e->node);
UNSUPPORTED("2nfqlmqqiafmfh2uxj5r72e5z"); // 	    FreeListNode(e);
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("3uomzgqvjm03fwau1petngub8"); // 	/* check for redundant root (not leaf, 1 child) and eliminate */
UNSUPPORTED("crynploao8fgrtf7envjtfdaz"); // 	if ((*nn)->count == 1 && (*nn)->level > 0) {
UNSUPPORTED("a588ej7tdnabye2dhwn96zbay"); // 	    if (rtp->StatFlag)
UNSUPPORTED("4bzchhtuz1r199e9mi0bp1ece"); // 		rtp->ElimCount++;
UNSUPPORTED("6u2n1qlqovrhoecwygc6fuq2c"); // 	    rtp->EntryCount--;
UNSUPPORTED("ehzcswv2o675ah20nlk8ll73b"); // 	    for (i = 0; i < 64; i++) {
UNSUPPORTED("jk2oyzi48hf05v99gu6wc2o2"); // 		if ((t = (*nn)->branch[i].child))
UNSUPPORTED("czyohktf9bkx4udfqhx42f4lu"); // 		    break;
UNSUPPORTED("6t98dcecgbvbvtpycwiq2ynnj"); // 	    }
UNSUPPORTED("5ba1w7saym0g246ykbjnn0qrf"); // 	    RTreeFreeNode(rtp, *nn);
UNSUPPORTED("dnoogjer2v3hmfm7qtw2p4qrv"); // 	    *nn = t;
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("cpwomgmpysmof2uglfr37v494"); // 	rtp->Deleting = (0);
UNSUPPORTED("c9ckhc8veujmwcw0ar3u3zld4"); // 	return 0;
UNSUPPORTED("c07up7zvrnu2vhzy6d7zcu94g"); //     } else {
UNSUPPORTED("cpwomgmpysmof2uglfr37v494"); // 	rtp->Deleting = (0);
UNSUPPORTED("eleqpc2p2r3hvma6tipoy7tr"); // 	return 1;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 bax12o5n6n8s94wnn7cxgn99
// static int RTreeDelete2(RTree_t * rtp, Rect_t * r, void *data, Node_t * n, 	     ListNode_t ** ee) 
public static Object RTreeDelete2(Object... arg) {
UNSUPPORTED("eyp5xkiyummcoc88ul2b6tkeg"); // static int
UNSUPPORTED("dl163ikex89epdeiymlnlhkkt"); // RTreeDelete2(RTree_t * rtp, Rect_t * r, void *data, Node_t * n,
UNSUPPORTED("7cv8lwqbi5i6si9trbnke7zl9"); // 	     ListNode_t ** ee)
UNSUPPORTED("8suowst9wgd6gxhgbdi3h24b7"); // /* static int */
UNSUPPORTED("ap2yhjmvw1l4wh1rujus8t8ef"); // /* RTreeDelete2(RTree_t*rtp, Rect_t*r, int data, Node_t*n, ListNode_t**ee) */
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("pp6gyv6pecd6kik4hoguluwp"); //     register int i;
UNSUPPORTED("9yzorirytkswjf0omka5g4c20"); //     assert(r && n && ee);
UNSUPPORTED("aickurv6sbkajrl6u32h8s7n0"); //     assert(data >= 0);
UNSUPPORTED("btdpzkb56pz41hfftabazxn4s"); //     assert(n->level >= 0);
UNSUPPORTED("c435v08qyezw1xnbv2zrqr3te"); //     if (rtp->StatFlag)
UNSUPPORTED("9qx61yt5qzelxylomit9cn4rv"); // 	rtp->DeTouchCount++;
UNSUPPORTED("5zjfl81difxhf334pznet7o8z"); //     if (n->level > 0) {		/* not a leaf node */
UNSUPPORTED("r6wug9hvutzrx2jp04v9slbe"); // 	for (i = 0; i < 64; i++) {
UNSUPPORTED("9cxgoq0fd8kxz4eymv34vt66k"); // 	    if (n->branch[i].child && Overlap(r, &(n->branch[i].rect))) {
UNSUPPORTED("9c2dxowbn34szo9si1s67zn1d"); // 		if (!RTreeDelete2(rtp, r, data, n->branch[i].child, ee)) {	/*recurse */
UNSUPPORTED("1p8d3xzz5d8g7ach93r5dkfpy"); // 		    if (n->branch[i].child->count >= rtp->MinFill)
UNSUPPORTED("8qc1vxcpq7d0q3d4pt56sagi6"); // 			n->branch[i].rect = NodeCover(n->branch[i].child);
UNSUPPORTED("1pj51sbe7du437gcgun1tdp0c"); // 		    else {	/* not enough entries in child, eliminate child node */
UNSUPPORTED("1mqsg2rc7oaykxyne9jioil3j"); // 			RTreeReInsert(rtp, n->branch[i].child, ee);
UNSUPPORTED("36htmotk6j7fq59vwfp4lanaj"); // 			DisconBranch(n, i);
UNSUPPORTED("614qavwsv6dpxdog4wxkb2op0"); // 			rtp->EntryCount--;
UNSUPPORTED("ad48i843uruoaahfmd2nxb4qv"); // 			if (rtp->StatFlag)
UNSUPPORTED("jk9sg727bbh6mlccfj53sg1m"); // 			    rtp->ElimCount++;
UNSUPPORTED("dkxvw03k2gg9anv4dbze06axd"); // 		    }
UNSUPPORTED("y2l9mpq5754ggnklm39b7wg"); // 		    return 0;
UNSUPPORTED("6eq5kf0bj692bokt0bixy1ixh"); // 		}
UNSUPPORTED("6t98dcecgbvbvtpycwiq2ynnj"); // 	    }
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("eleqpc2p2r3hvma6tipoy7tr"); // 	return 1;
UNSUPPORTED("dppt1v5lkz1rfqbwx4n4xwltd"); //     } else {			/* a leaf node */
UNSUPPORTED("r6wug9hvutzrx2jp04v9slbe"); // 	for (i = 0; i < 64; i++) {
UNSUPPORTED("5g2zznotye25yuclpeyzatgrf"); // 	    if (n->branch[i].child
UNSUPPORTED("dx5d5kpda621ehao92qybpo7n"); // 		&& n->branch[i].child == (Node_t *) data) {
UNSUPPORTED("3nnhyf15i3s8j44ptc0zjxkof"); // 		DisconBranch(n, i);
UNSUPPORTED("cazijll74xx4uxev9qe61vl0h"); // 		rtp->EntryCount--;
UNSUPPORTED("5izxoao5ryte71964f8yjfd5y"); // 		return 0;
UNSUPPORTED("6t98dcecgbvbvtpycwiq2ynnj"); // 	    }
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("eleqpc2p2r3hvma6tipoy7tr"); // 	return 1;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}


}
