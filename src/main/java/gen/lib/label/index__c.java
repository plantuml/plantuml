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
package gen.lib.label;
import static gen.lib.label.node__c.AddBranch;
import static gen.lib.label.node__c.DisconBranch;
import static gen.lib.label.node__c.NodeCover;
import static gen.lib.label.node__c.PickBranch;
import static gen.lib.label.node__c.RTreeNewNode;
import static gen.lib.label.rectangle__c.CombineRect;
import static gen.lib.label.rectangle__c.Overlap;
import static smetana.core.Macro.UNSUPPORTED;
import static smetana.core.debug.SmetanaDebug.ENTERING;
import static smetana.core.debug.SmetanaDebug.LEAVING;

import gen.annotation.Original;
import gen.annotation.Unused;
import h.ST_Branch_t;
import h.ST_LeafList_t;
import h.ST_Node_t___;
import h.ST_Node_t___or_object_t;
import h.ST_RTree;
import h.ST_Rect_t;
import smetana.core.Memory;
import smetana.core.__ptr__;

public class index__c {
    // ::remove folder when __HAXE__


//3 1rfaqe5urty5uyp5xb2r0idce
// LeafList_t *RTreeNewLeafList(Leaf_t * lp) 
@Unused
@Original(version="2.38.0", path="lib/label/index.c", name="", key="1rfaqe5urty5uyp5xb2r0idce", definition="LeafList_t *RTreeNewLeafList(Leaf_t * lp)")
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
@Unused
@Original(version="2.38.0", path="lib/label/index.c", name="", key="6pvstz7axi8a7saeqe3nrrmg5", definition="LeafList_t *RTreeLeafListAdd(LeafList_t * llp, Leaf_t * lp)")
public static ST_LeafList_t RTreeLeafListAdd(ST_LeafList_t llp, ST_Branch_t lp) {
ENTERING("6pvstz7axi8a7saeqe3nrrmg5","RTreeLeafListAdd");
try {
     ST_LeafList_t nlp;
     if ((lp) == null)
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
@Unused
@Original(version="2.38.0", path="lib/label/index.c", name="RTreeLeafListFree", key="6zraor7x44vrnm19d2igkvow2", definition="void RTreeLeafListFree(LeafList_t * llp)")
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
@Unused
@Original(version="2.38.0", path="lib/label/index.c", name="", key="aa29m7d7qc06m8id896e60lkg", definition="Tree_t *RTreeOpen()")
public static ST_RTree RTreeOpen() {
ENTERING("aa29m7d7qc06m8id896e60lkg","RTreeOpen");
try {
	ST_RTree rtp;
    rtp = new ST_RTree();
    if (rtp!=null)
    	rtp.root = RTreeNewIndex(rtp);
    return rtp;
} finally {
LEAVING("aa29m7d7qc06m8id896e60lkg","RTreeOpen");
}
}

/* Make a new index, empty.  Consists of a single node. */
//3 aa39m7d7qc06m8id896e60lkg
//Node_t *RTreeNewIndex(RTree_t * rtp)
@Unused
@Original(version="2.38.0", path="lib/label/index.c", name="", key="aa39m7d7qc06m8id896e60lkg", definition="ode_t *RTreeNewIndex(RTree_t * rtp)")
public static ST_Node_t___ RTreeNewIndex(ST_RTree rtp) {
ENTERING("aa39m7d7qc06m8id896e60lkg","RTreeNewIndex");
try {
	ST_Node_t___ x;
	x = RTreeNewNode(rtp);
	x.level = 0; /* leaf */
	rtp.LeafCount = rtp.LeafCount+1;
	return x;
} finally {
LEAVING("aa39m7d7qc06m8id896e60lkg","RTreeNewIndex");
}
}

//3 aa49m7d7qc06m8id896e60lkg
//static int RTreeClose2(RTree_t * rtp, Node_t * n)
@Unused
@Original(version="2.38.0", path="lib/label/index.c", name="RTreeClose2", key="aa49m7d7qc06m8id896e60lkg", definition="tatic int RTreeClose2(RTree_t * rtp, Node_t * n)")
public static int RTreeClose2(ST_RTree rtp, ST_Node_t___ n) {
ENTERING("aa49m7d7qc06m8id896e60lkg","RTreeClose2");
try {
    int i;

    if (n.level > 0) {
	for (i = 0; i < 64; i++) {
	    if ((n.branch[i].child) == null)
		continue;
	    if (RTreeClose2(rtp, (ST_Node_t___) n.branch[i].child) == 0) {
		Memory.free(n.branch[i].child);
		DisconBranch(n, i);
	    rtp.EntryCount = rtp.EntryCount-1;
		if (rtp.StatFlag!=0)
		    rtp.ElimCount = rtp.ElimCount+1;
	    }
	}
    } else {
	for (i = 0; i < 64; i++) {
		if ((n.branch[i].child) == null)
		continue;
	    // free(n->branch[i].child);
	    DisconBranch(n, i);
	    rtp.EntryCount = rtp.EntryCount-1;
	    if (rtp.StatFlag!=0)
		    rtp.ElimCount = rtp.ElimCount+1;
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
@Unused
@Original(version="2.38.0", path="lib/label/index.c", name="RTreeClose", key="aa59m7d7qc06m8id896e60lkg", definition="nt RTreeClose(RTree_t * rtp)")
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
@Unused
@Original(version="2.38.0", path="lib/label/index.c", name="", key="aa69m7d7qc06m8id896e60lkg", definition="eafList_t *RTreeSearch(RTree_t * rtp, Node_t * n, Rect_t * r)")
public static ST_LeafList_t RTreeSearch(ST_RTree rtp, ST_Node_t___ n, ST_Rect_t r) {
ENTERING("aa69m7d7qc06m8id896e60lkg","RTreeSearch");
try {
	int i;
    ST_LeafList_t llp = null;

//    assert(n);
//    assert(n->level >= 0);
//    assert(r);

    rtp.SeTouchCount = rtp.SeTouchCount+1;

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
@Unused
@Original(version="2.38.0", path="lib/label/index.c", name="RTreeInsert", key="3wss9r0zgt5k06j8ovjv7hq0d", definition="int RTreeInsert(RTree_t * rtp, Rect_t * r, void *data, Node_t ** n, int level)")
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
     if (rtp.Deleting == 0)
 	rtp.RectCount = rtp.RectCount+1;
     if (RTreeInsert2(rtp, r, data, n[0], newnode, level)!=0) {	/* root was split */
 	if (rtp.StatFlag!=0) {
UNSUPPORTED("2y8kv6b3ysrr61q7tqn76rhhc"); // 	    if (rtp->Deleting)
UNSUPPORTED("dn4oynmx9ugizzs5pkxiyptbi"); // 		rtp->DeTouchCount++;
UNSUPPORTED("5c97f6vfxny0zz35l2bu4maox"); // 	    else
UNSUPPORTED("2u8wpa4w1q7rg14t07bny6p8i"); // 		rtp->InTouchCount++;
 	}
 	newroot = RTreeNewNode(rtp);	/* grow a new root, make tree taller */
 	rtp.NonLeafCount = rtp.NonLeafCount+1;
 	newroot.level = n[0].level + 1;
 	b.rect.___(NodeCover(n[0]));
 	b.child = n[0];
 	AddBranch(rtp, b, newroot, null);
 	b.rect.___(NodeCover(newnode[0]));
 	b.child = newnode[0];
 	AddBranch(rtp, b, newroot, null);
 	n[0] = newroot;
 	// rtp->root = newroot;
 	rtp.EntryCount = rtp.EntryCount + 2;
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
@Unused
@Original(version="2.38.0", path="lib/label/index.c", name="RTreeInsert2", key="bsc9m7d7qc06m8id896e60lkg", definition="static int RTreeInsert2(RTree_t * rtp, Rect_t * r, void *data, 	     Node_t * n, Node_t ** new, int level)")
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
 	if (RTreeInsert2(rtp, r, data, (ST_Node_t___) n.branch[i].child, n2, level) == 0) {
/* recurse: child was not split */
 	    n.branch[i].rect.___(
 	    		CombineRect((ST_Rect_t)r, (ST_Rect_t) n.branch[i].rect));
 	    return 0;
 	} else {		/* child was split */
 	    n.branch[i].rect.___(
 	    		NodeCover((ST_Node_t___)n.branch[i].child));
 	    b.child = n2[0];
 	    b.rect.___(NodeCover(n2[0]));
 		rtp.EntryCount = rtp.EntryCount+1;
 	 	return AddBranch(rtp, b, n, new_);
 	}
     } else if (n.level == level) {	/* at level for insertion. */
 	/*Add rect, split if necessary */
 	b.rect.___(r);
 	b.child = /*(Node_t *)*/(ST_Node_t___or_object_t) data; // THIS CAST IS A BIG ISSUE
// UNSUPPORTED("7w1b5nw2bj3zmo70m9bczwwov"); // 	b.child = (Node_t *) data;
 	rtp.EntryCount = rtp.EntryCount+1;
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




}
