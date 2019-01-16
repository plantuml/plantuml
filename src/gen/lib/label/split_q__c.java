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
import static gen.lib.label.node__c.InitNode;
import static gen.lib.label.node__c.RTreeNewNode;
import static gen.lib.label.rectangle__c.CombineRect;
import static gen.lib.label.rectangle__c.NullRect;
import static gen.lib.label.rectangle__c.RectArea;
import static smetana.core.JUtilsDebug.ENTERING;
import static smetana.core.JUtilsDebug.LEAVING;
import static smetana.core.Macro.N;
import static smetana.core.Macro.UNSUPPORTED;
import h.Branch_t;
import h.ST_Branch_t;
import h.ST_Node_t___;
import h.ST_PartitionVars;
import h.ST_RTree;
import h.ST_Rect_t;

public class split_q__c {


//3 6vl3snxd6k95gamfkwfsfdguc
// void SplitNode(RTree_t * rtp, Node_t * n, Branch_t * b, Node_t ** nn) 
public static void SplitNode(ST_RTree rtp, ST_Node_t___ n, ST_Branch_t b, ST_Node_t___ nn[]) {
ENTERING("6vl3snxd6k95gamfkwfsfdguc","SplitNode");
try {
     ST_PartitionVars p;
     int level;
     int area;
//     assert(n);
//     assert(b);
     if (rtp.StatFlag!=0) {
UNSUPPORTED("akhni40ndam0u9c6i7raxw4mp"); // 	if (rtp->Deleting)
UNSUPPORTED("4g80zdlbvunm838x8g3ic9tex"); // 	    rtp->DeSplitCount++;
UNSUPPORTED("9352ql3e58qs4fzapgjfrms2s"); // 	else
UNSUPPORTED("2cjo6wz1rmxfm5k7u7rw5dqpj"); // 	    rtp->InSplitCount++;
     }
     /* load all the branches into a buffer, initialize old node */
     level = n.level;
     GetBranches(rtp, n, b);
     /* find partition */
     p = rtp.split.Partitions[0];
     MethodZero(rtp);
     area = RectArea((ST_Rect_t)p.cover[0]) + RectArea((ST_Rect_t)p.cover[1]);
     /* record how good the split was for statistics */
     if (rtp.StatFlag!=0 && N(rtp.Deleting) && area!=0)
UNSUPPORTED("z7xk6s3hzi3qcoiq2exj9hpv"); // 	rtp->SplitMeritSum += (float) rtp->split.CoverSplitArea / area;
     /* put branches from buffer into 2 nodes according to chosen partition */
     nn[0] = RTreeNewNode(rtp);
     n.setInt("level", level);
     nn[0].setInt("level", level);
     LoadNodes(rtp, n, nn[0], p);
//     assert(n->count + (*nn)->count == 64 + 1);
} finally {
LEAVING("6vl3snxd6k95gamfkwfsfdguc","SplitNode");
}
}




//3 al7lyin008m7kvrvuxhcuvn61
// static void GetBranches(RTree_t * rtp, Node_t * n, Branch_t * b) 
public static void GetBranches(ST_RTree rtp, ST_Node_t___ n, ST_Branch_t b) {
ENTERING("al7lyin008m7kvrvuxhcuvn61","GetBranches");
try {
     int i;
//     assert(n);
//     assert(b);
     /* load the branch buffer */
     for (i = 0; i < 64; i++) {
       // 	assert(n->branch[i].child);	/* node should have every entry full */
    	 rtp.split.BranchBuf[i].___(n.branch[i]);
     }
     rtp.split.BranchBuf[64].___(b);
     /* calculate rect containing all in the set */
     rtp.split.CoverSplit.___(rtp.split.BranchBuf[0].rect);
     for (i = 1; i < 64 + 1; i++) {
    	 rtp.split.setStruct("CoverSplit",
    	 CombineRect((ST_Rect_t)rtp.split.CoverSplit,
    			 (ST_Rect_t)rtp.split.BranchBuf[i].castTo(ST_Rect_t.class)));
     }
     rtp.split.setInt("CoverSplitArea", RectArea((ST_Rect_t)rtp.split.CoverSplit));
     InitNode(n);
} finally {
LEAVING("al7lyin008m7kvrvuxhcuvn61","GetBranches");
}
}




//3 4woz5xy4gjlahoj7no3ljxmex
// static void MethodZero(RTree_t * rtp) 
public static void MethodZero(ST_RTree rtp) {
ENTERING("4woz5xy4gjlahoj7no3ljxmex","MethodZero");
try {
     ST_Rect_t r;
     int i, growth0, growth1, diff, biggestDiff;
     int group, chosen=0, betterGroup=0;
     InitPVars(rtp);
     PickSeeds(rtp);
     while (rtp.split.Partitions[0].count[0] +
    		 rtp.split.Partitions[0].count[1] < 64 + 1 &&
 	  rtp.split.Partitions[0].count[0] < 64 + 1 - rtp.MinFill
 	   && rtp.split.Partitions[0].count[1] <
 	   64 + 1 - rtp.MinFill) {
 	biggestDiff = -1;
 	for (i = 0; i < 64 + 1; i++) {
 	    if (N(rtp.split.Partitions[0].taken[i])) {
 		final ST_Rect_t rect = new ST_Rect_t();
 		r = (ST_Rect_t) rtp.split.BranchBuf[i].castTo(ST_Rect_t.class);
 		/* growth0 = RectArea(&CombineRect(r,
 		   &rtp->split.Partitions[0].cover[0])) -
 		   rtp->split.Partitions[0].area[0];
 		 */
 		/* growth1 = RectArea(&CombineRect(r,
 		   &rtp->split.Partitions[0].cover[1])) -
 		   rtp->split.Partitions[0].area[1];
 		 */
 		rect.___(CombineRect(r, (ST_Rect_t) rtp.split.Partitions[0].cover[0]));
 		growth0 =
 		    RectArea((ST_Rect_t)rect) - rtp.split.Partitions[0].area[0];
 		rect.___(CombineRect(r, (ST_Rect_t) rtp.split.Partitions[0].cover[1]));
 		growth1 =
 		    RectArea((ST_Rect_t)rect) - rtp.split.Partitions[0].area[1];
 		diff = growth1 - growth0;
 		if (diff >= 0)
 		    group = 0;
 		else {
 		    group = 1;
 		    diff = -diff;
 		}
 		if (diff > biggestDiff) {
 		    biggestDiff = diff;
 		    chosen = i;
 		    betterGroup = group;
 		} else if (diff == biggestDiff &&
 				rtp.split.Partitions[0].count[group] <
 				rtp.split.Partitions[0].count[betterGroup]) {
 		    chosen = i;
 		    betterGroup = group;
 		}
 	    }
 	}
 	Classify(rtp, chosen, betterGroup);
     }
     /* if one group too full, put remaining rects in the other */
     if (rtp.split.Partitions[0].count[0] +
    		 rtp.split.Partitions[0].count[1] < 64 + 1) {
 	group = 0;
UNSUPPORTED("4edp65b21liyii0fj1ikco7o0"); // 	if (rtp->split.Partitions[0].count[0] >=
UNSUPPORTED("20lpsuiyepr2ujozaf6gp4cc"); // 	    64 + 1 - rtp->MinFill)
UNSUPPORTED("9qtt6i40h8vtjp2cvqyb8ycaz"); // 	    group = 1;
UNSUPPORTED("2x1nx9nsne4x3ygmcywl1m3og"); // 	for (i = 0; i < 64 + 1; i++) {
UNSUPPORTED("jdepsnmrs3ghh78ql301sfvu"); // 	    if (!rtp->split.Partitions[0].taken[i])
UNSUPPORTED("gqfhorilvtlogp6f2ozx5akf"); // 		Classify(rtp, i, group);
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
     }
 //     assert(rtp->split.Partitions[0].count[0] +
 // 	   rtp->split.Partitions[0].count[1] == 64 + 1);
 //     assert(rtp->split.Partitions[0].count[0] >= rtp->MinFill
 // 	   && rtp->split.Partitions[0].count[1] >= rtp->MinFill);
} finally {
LEAVING("4woz5xy4gjlahoj7no3ljxmex","MethodZero");
}
}




//3 8rui4cun4tvq5xy6ke6r3p55e
// static void PickSeeds(RTree_t * rtp) 
public static void PickSeeds(ST_RTree rtp) {
ENTERING("8rui4cun4tvq5xy6ke6r3p55e","PickSeeds");
try {
   int i, j;
   int waste, worst;
   int seed0=0, seed1=0;
   int area[] = new int[64 + 1];
     for (i = 0; i < 64 + 1; i++)
 	area[i] = RectArea((ST_Rect_t) rtp.split.BranchBuf[i].rect);
     //worst = -rtp->split.CoverSplitArea - 1;
     worst=0;
     for (i = 0; i < 64; i++) {
 	for (j = i + 1; j < 64 + 1; j++) {
 	    final ST_Rect_t rect = new ST_Rect_t();
 	    /* waste = RectArea(&CombineRect(&rtp->split.BranchBuf[i].rect,
 	                         &rtp->split.BranchBuf[j].rect)) - area[i] - area[j];
 	     */
 	    rect.___(
   	 CombineRect((ST_Rect_t)rtp.split.BranchBuf[i].rect,
			 (ST_Rect_t)rtp.split.BranchBuf[j].rect));
 	    waste = RectArea((ST_Rect_t)rect) - area[i] - area[j];
 	    if (waste > worst) {
 		worst = waste;
 		seed0 = i;
 		seed1 = j;
 	    }
 	}
     }
     Classify(rtp, seed0, 0);
     Classify(rtp, seed1, 1);
} finally {
LEAVING("8rui4cun4tvq5xy6ke6r3p55e","PickSeeds");
}
}




//3 4qyy2dpbkziuubssvfwb8u1sh
// static void Classify(RTree_t * rtp, int i, int group) 
public static void Classify(ST_RTree rtp, int i, int group) {
ENTERING("4qyy2dpbkziuubssvfwb8u1sh","Classify");
try {
//     assert(!rtp->split.Partitions[0].taken[i]);
	rtp.split.Partitions[0].partition[i]=group;
	rtp.split.Partitions[0].taken[i]=1;
	if (rtp.split.Partitions[0].count[group] == 0)
		rtp.split.Partitions[0].cover[group].___(
				rtp.split.BranchBuf[i].rect);
     else
    	 rtp.split.Partitions[0].cover[group].___(
 	    CombineRect((ST_Rect_t)rtp.split.BranchBuf[i].rect,
 	    		(ST_Rect_t)rtp.split.Partitions[0].cover[group]));
	rtp.split.Partitions[0].area[group]=
 	RectArea((ST_Rect_t)rtp.split.Partitions[0].cover[group]);

	rtp.split.Partitions[0].count[group]=
	rtp.split.Partitions[0].count[group]+1;
} finally {
LEAVING("4qyy2dpbkziuubssvfwb8u1sh","Classify");
}
}




//3 ay7l4setwyl3hbx4o2jpa7vyz
// static void LoadNodes(RTree_t * rtp, Node_t * n, Node_t * q, 		      struct PartitionVars *p) 
public static void LoadNodes(ST_RTree rtp, ST_Node_t___ n, ST_Node_t___ q, ST_PartitionVars p) {
ENTERING("ay7l4setwyl3hbx4o2jpa7vyz","LoadNodes");
try {
     int i;
//     assert(n);
//     assert(q);
//     assert(p);
     for (i = 0; i < 64 + 1; i++) {
// 	assert(rtp->split.Partitions[0].partition[i] == 0 ||
// 	       rtp->split.Partitions[0].partition[i] == 1);
 	if (rtp.split.Partitions[0].partition[i] == 0)
 	    AddBranch(rtp, (ST_Branch_t)rtp.split.BranchBuf[i].castTo(Branch_t.class), n, null);
 	else if (rtp.split.Partitions[0].partition[i] == 1)
 	    AddBranch(rtp, (ST_Branch_t)rtp.split.BranchBuf[i].castTo(Branch_t.class), q, null);
     }
} finally {
LEAVING("ay7l4setwyl3hbx4o2jpa7vyz","LoadNodes");
}
}




//3 dvgjc83sogjhzf5kxpir405rh
// static void InitPVars(RTree_t * rtp) 
public static void InitPVars(ST_RTree rtp) {
ENTERING("dvgjc83sogjhzf5kxpir405rh","InitPVars");
     int i;
     rtp.split.Partitions[0].count[0]=0;
     rtp.split.Partitions[0].count[1]=0;
     rtp.split.Partitions[0].cover[0].___(NullRect());
     rtp.split.Partitions[0].cover[1].___(NullRect());
     rtp.split.Partitions[0].area[0]=0;
     rtp.split.Partitions[0].area[1]=0;
     for (i = 0; i < 64 + 1; i++) {
         rtp.split.Partitions[0].taken[i]=0;
         rtp.split.Partitions[0].partition[i]=-1;
     }
try {
} finally {
LEAVING("dvgjc83sogjhzf5kxpir405rh","InitPVars");
}
}


}
