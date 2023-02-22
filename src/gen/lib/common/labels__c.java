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
package gen.lib.common;
import static gen.lib.cgraph.edge__c.aghead;
import static gen.lib.cgraph.graph__c.agisdirected;
import static gen.lib.cgraph.id__c.agnameof;
import static gen.lib.cgraph.obj__c.agobjkind;
import static gen.lib.cgraph.obj__c.agraphof;
import static gen.lib.cgraph.obj__c.agroot;
import static gen.lib.common.htmltable__c.make_html_label;
import static gen.lib.common.utils__c.htmlEntityUTF8;
import static smetana.core.JUtils.EQ_CSTRING;
import static smetana.core.Macro.AGEDGE;
import static smetana.core.Macro.AGNODE;
import static smetana.core.Macro.AGRAPH;
import static smetana.core.Macro.ED_head_port;
import static smetana.core.Macro.ED_label;
import static smetana.core.Macro.ED_tail_port;
import static smetana.core.Macro.GD_charset;
import static smetana.core.Macro.GD_gvc;
import static smetana.core.Macro.GD_label;
import static smetana.core.Macro.LT_HTML;
import static smetana.core.Macro.LT_RECD;
import static smetana.core.Macro.M_agtail;
import static smetana.core.Macro.ND_label;
import static smetana.core.Macro.UNSUPPORTED;
import static smetana.core.Macro.hackInitDimensionFromLabel;
import static smetana.core.debug.SmetanaDebug.ENTERING;
import static smetana.core.debug.SmetanaDebug.LEAVING;

import gen.annotation.Difficult;
import gen.annotation.Doc;
import gen.annotation.Original;
import gen.annotation.Reviewed;
import gen.annotation.Unused;
import h.ST_Agedge_s;
import h.ST_Agnode_s;
import h.ST_Agobj_s;
import h.ST_Agraph_s;
import h.ST_GVC_s;
import h.ST_pointf;
import h.ST_port;
import h.ST_textlabel_t;
import h.ST_textspan_t;
import smetana.core.CArray;
import smetana.core.CString;
import smetana.core.Globals;
import smetana.core.Memory;
import smetana.core.ZType;
import smetana.core.__ptr__;
import smetana.core.debug.SmetanaDebug;

public class labels__c {




@Reviewed(when = "13/11/2020")
@Difficult
@Doc("Compute size of a string and store size")
@Original(version="2.38.0", path="lib/common/labels.c", name="storeline", key="4wkeqik2dt7ecr64ej6ltbnvb", definition="static void storeline(GVC_t *gvc, textlabel_t *lp, char *line, char terminator)")
public static void storeline(Globals zz, ST_GVC_s gvc, ST_textlabel_t lp, CString line, char terminator) {
ENTERING("4wkeqik2dt7ecr64ej6ltbnvb","storeline");
try {
    final ST_pointf size = new ST_pointf();
    ST_textspan_t span = null;
    int oldsz = lp.nspans + 1;
    
    lp.span = CArray.<ST_textspan_t>REALLOC__(oldsz + 1, lp.span, ZType.ST_textspan_t);
    span = lp.span.get__(lp.nspans);
    span.str = line;
    span.just = terminator;

	// WE CHEAT
	zz.tf.name = lp.fontname;
	zz.tf.size = lp.fontsize;
	size.x = 0.0;
	size.y = (int)(lp.fontsize * 1.20);
	hackInitDimensionFromLabel(size, line.getContent());
    SmetanaDebug.LOG("storeline line.getContent()="+line.getContent()+" size="+size);
	span.size.y = (int)size.y;

    lp.nspans++;
    /* width = max line width */
    lp.dimen.x = Math.max(lp.dimen.x, size.x);
    /* accumulate height */
    lp.dimen.y = lp.dimen.y + size.y;
    SmetanaDebug.LOG("storeline "+lp);
} finally {
LEAVING("4wkeqik2dt7ecr64ej6ltbnvb","storeline");
}
}




@Reviewed(when = "12/11/2020")
@Doc("Parse simple label")
@Original(version="2.38.0", path="lib/common/labels.c", name="make_simple_label", key="22ar72ye93a8ntj8pagnt5b5k", definition="void make_simple_label(GVC_t * gvc, textlabel_t * lp)")
public static void make_simple_label(Globals zz, ST_GVC_s gvc, ST_textlabel_t lp) {
ENTERING("22ar72ye93a8ntj8pagnt5b5k","make_simple_label");
try {
    char c;
    CString p, line, lineptr, str = lp.text;
    char bytee = 0x00;
    
    lp.dimen.x = lp.dimen.y = 0.0;
    if (str.charAt(0) == '\0')
	return;
    
    line = lineptr = null;
    p = str;
    line = lineptr = CString.gmalloc((p.length() + 1));
    line.setCharAt(0, '\0');
    while ((c = p.charAt(0))!='\0') {
    p = p.plus_(1);
	bytee = c;
	/* wingraphviz allows a combination of ascii and big-5. The latter
         * is a two-byte encoding, with the first byte in 0xA1-0xFE, and
         * the second in 0x40-0x7e or 0xa1-0xfe. We assume that the input
         * is well-formed, but check that we don't go past the ending '\0'.
         */
	if ((lp.charset == 2) && 0xA1 <= bytee && bytee <= 0xFE) {
UNSUPPORTED("6la63t1mnqv30shyyp3yfroxb"); // 	    *lineptr++ = c;
UNSUPPORTED("ebmmarxykvf76hmfmjuk0ssjz"); // 	    c = *p++;
UNSUPPORTED("6la63t1mnqv30shyyp3yfroxb"); // 	    *lineptr++ = c;
UNSUPPORTED("1kri3b36twfj4t7bvjbrt6dhs"); // 	    if (!c) /* NB. Protect against unexpected string end here */
UNSUPPORTED("9ekmvj13iaml5ndszqyxa8eq"); // 		break;
	} else {
	    if (c == '\\') {
		switch (p.charAt(0)) {
		case 'n':
		case 'l':
		case 'r':
		    lineptr.setCharAt(0, '\0');
		    lineptr = lineptr.plus_(1);
		    storeline(zz, gvc, lp, line, p.charAt(0));
		    line = lineptr;
		    break;
		default:
		    lineptr.setCharAt(0, p.charAt(0));
		    lineptr = lineptr.plus_(1);
		}
		if (p.charAt(0)!='\0')
		    p = p.plus_(1);
		/* tcldot can enter real linend characters */
	    } else if (c == '\n') {
		    lineptr.setCharAt(0, '\0');
		    lineptr = lineptr.plus_(1);
		storeline(zz, gvc, lp, line, 'n');
		line = lineptr;
	    } else {
	    lineptr.setCharAt(0, c);
	    lineptr = lineptr.plus_(1);
	    }
	}
    }
    
    
    if (!EQ_CSTRING(line, lineptr)) {
	lineptr.setCharAt(0, '\0');
	lineptr = lineptr.plus_(1);
	storeline(zz, gvc, lp, line, 'n');
    }
    
    lp.space.___(lp.dimen);
} finally {
LEAVING("22ar72ye93a8ntj8pagnt5b5k","make_simple_label");
}
}




@Reviewed(when = "12/11/2020")
@Original(version="2.38.0", path="lib/common/labels.c", name="", key="ecq5lydlrjrlaz8o6vm6svc8i", definition="textlabel_t *make_label(void *obj, char *str, int kind, double fontsize, char *fontname, char *fontcolor)")
public static ST_textlabel_t make_label(Globals zz, ST_Agobj_s obj, CString str, int kind, double fontsize, CString fontname, CString fontcolor) {
ENTERING("ecq5lydlrjrlaz8o6vm6svc8i","make_label");
try {
	ST_textlabel_t rv = new ST_textlabel_t();
    ST_Agraph_s g = null, sg = null;
    ST_Agnode_s n = null;
    ST_Agedge_s e = null;
        CString s = null;
        
        
    switch (agobjkind(obj)) {
    case AGRAPH:
        sg = (ST_Agraph_s)obj;
	g = (ST_Agraph_s) sg.root;
	break;
    case AGNODE:
        n = (ST_Agnode_s)obj;
	g = agroot(agraphof(n));
	break;
    case AGEDGE:
        e = (ST_Agedge_s)obj;
	g = agroot(agraphof(aghead(e)));
	break;
    }
    rv.fontname =  fontname;
    rv.fontcolor = fontcolor;
    rv.fontsize = fontsize;
    rv.charset = GD_charset(g);
    if ((kind & LT_RECD)!=0) {
	rv.text = str.strdup();
        if ((kind & LT_HTML)!=0) {
	    rv.html = (true);
	}
    }
    else if (kind == LT_HTML) {
	rv.text = str.strdup();
	rv.html = true;
	if (make_html_label(obj, rv)!=0) {
	    switch (agobjkind(obj)) {
    case AGRAPH:
	        UNSUPPORTED("agerr(AGPREV, in label of graph %s\n,agnameof(sg));");
		break;
    case AGNODE:
	        UNSUPPORTED("agerr(AGPREV, in label of node %s\n, agnameof(n));");
		break;
    case AGEDGE:
		UNSUPPORTED("agerr(AGPREV, in label of edge %s %s %s\n,");
//		        agnameof(((((((Agobj_t*)(e))->tag).objtype) == 3? (e): ((e)+1))->node)), agisdirected(g)?"->":"--", agnameof(((((((Agobj_t*)(e))->tag).objtype) == 2? (e): ((e)-1))->node)));
		break;
	    }
	}
    }
    else {
        //assert(kind == (0 << 1));
	/* This call just processes the graph object based escape sequences. The formatting escape
         * sequences (\n, \l, \r) are processed in make_simple_label. That call also replaces \\ with \.
         */
	rv.text = strdup_and_subst_obj0(zz, str, obj, 0);
        switch (rv.charset) {
    case 1:
	    UNSUPPORTED("s = latin1ToUTF8(rv->text);");
	    break;
	default: /* UTF8 */
	    s = htmlEntityUTF8(rv.text, g);
	    break;
	}
        Memory.free(rv.text);
        rv.text = s;
	make_simple_label(zz, GD_gvc(g), rv);
    }
    return rv;
} finally {
LEAVING("ecq5lydlrjrlaz8o6vm6svc8i","make_label");
}
}




/* strdup_and_subst_obj0:
 * Replace various escape sequences with the name of the associated
 * graph object. A double backslash \\ can be used to avoid a replacement.
 * If escBackslash is true, convert \\ to \; else leave alone. All other dyads 
 * of the form \. are passed through unchanged.
 */
@Difficult
@Reviewed(when = "12/11/2020")
@Original(version="2.38.0", path="lib/common/labels.c", name="", key="ajohywvjbrvkc7zca2uew6ghm", definition="static char *strdup_and_subst_obj0 (char *str, void *obj, int escBackslash)")
public static CString strdup_and_subst_obj0(Globals zz, CString str, ST_Agobj_s obj, int escBackslash) {
ENTERING("ajohywvjbrvkc7zca2uew6ghm","strdup_and_subst_obj0");
try {
    char c; CString s, p, t, newstr;
    CString tp_str = new CString(""), hp_str = new CString("");
    CString g_str = new CString("\\G"), n_str = new CString("\\N"), e_str = new CString("\\E"),
	h_str = new CString("\\H"), t_str = new CString("\\T"), l_str = new CString("\\L");
    int g_len = 2, n_len = 2, e_len = 2,
	h_len = 2, t_len = 2, l_len = 2,
	tp_len = 0, hp_len = 0;
    int newlen = 0;
    int isEdge = 0;
    ST_textlabel_t tl;
    final ST_port pt = new ST_port();
    
    
    /* prepare substitution strings */
    switch (agobjkind(obj)) {
	case AGRAPH:
	    g_str = agnameof(zz, (ST_Agraph_s)obj);
	    g_len = g_str.length();
	    tl = GD_label((ST_Agraph_s)obj);
	    if (tl!=null) {
		l_str = tl.text;
	    	if (str!=null) l_len = l_str.length();
	    }
	    break;
	case AGNODE:
	    g_str = agnameof(zz, agraphof(obj));
	    g_len = g_str.length();
	    n_str = agnameof(zz, obj);
	    n_len = n_str.length();
	    tl = ND_label((ST_Agnode_s)obj);
	    if (tl!=null) {
		l_str = tl.text;
	    	if (str!=null) l_len = l_str.length();
	    }
	    break;
	case AGEDGE:
	    isEdge = 1;
	    g_str = agnameof(zz, agroot(agraphof((M_agtail((ST_Agedge_s)obj)))));
	    g_len = g_str.length();
	    t_str = agnameof(zz, M_agtail((ST_Agedge_s)obj));
	    t_len = t_str.length();
	    pt.___(ED_tail_port((ST_Agedge_s)obj));
	    if ((tp_str = pt.name)!=null)
	        tp_len = tp_str.length();
	    h_str = agnameof(zz, aghead((obj)));
	    h_len = h_str.length();
	    pt.___(ED_head_port((ST_Agedge_s)obj));
	    if ((hp_str = pt.name)!=null)
		hp_len = hp_str.length();
	    h_len = h_str.length();
	    tl = ED_label((ST_Agedge_s)obj);
	    if (tl!=null) {
	    	l_str = tl.text;
	    	if (str!=null) l_len = l_str.length();
	    }
	    if (agisdirected(agroot(agraphof(M_agtail((ST_Agedge_s)obj)))))
		e_str = new CString("->");
	    else
		e_str = new CString("--");
	    e_len = t_len + (tp_len!=0?tp_len+1:0) + 2 + h_len + (hp_len!=0?hp_len+1:0);
	    break;
    }
    /* two passes over str.
     *
     * first pass prepares substitution strings and computes 
     * total length for newstring required from malloc.
     */
    for (s = str; ;) {
    c = s.charAt(0);
    s = s.plus_(1);
    if (c=='\0') break;
	if (c == '\\') {
	  c = s.charAt(0);
	  s = s.plus_(1);
	    switch (c) {
	    case 'G':
		newlen += g_len;
		break;
	    case 'N':
		newlen += n_len;
		break;
	    case 'E':
		newlen += e_len;
		break;
	    case 'H':
		newlen += h_len;
		break;
	    case 'T':
		newlen += t_len;
		break; 
	    case 'L':
		newlen += l_len;
		break; 
	    case '\\':
		if (escBackslash!=0) {
		    newlen += 1;
		    break; 
		}
		/* Fall through */
	    default:  /* leave other escape sequences unmodified, e.g. \n \l \r */
		newlen += 2;
	    }
	} else {
	    newlen++;
	}
    }
    /* allocate new string */
    newstr = CString.gmalloc(newlen + 1);
    
    
    /* second pass over str assembles new string */
    p = newstr;
    for (s = str; ;) {
    c = s.charAt(0);
    s = s.plus_(1);
    if (c=='\0') break;
	if (c == '\\') {
	  c = s.charAt(0);
	  s = s.plus_(1);
	    switch (c) {
	    case 'G':
		UNSUPPORTED("for (t = g_str; (*p = *t++); p++);");
		break;
	    case 'N':
		for (t = n_str; ; ) {
		p.setCharAt(0, t.charAt(0));
		t = t.plus_(1);
		if (p.charAt(0)=='\0') break;
		p = p.plus_(1);
		}
		break;
	    case 'E':
		UNSUPPORTED("if (isEdge) {");
/*		    for (t = t_str; (*p = *t++); p++);
		    if (tp_len) {
			*p++ = ':';
			for (t = tp_str; (*p = *t++); p++);
		    }
		    for (t = e_str; (*p = *t++); p++);
		    for (t = h_str; (*p = *t++); p++);
		    if (hp_len) {
			*p++ = ':';
			for (t = hp_str; (*p = *t++); p++);
		    }
		}*/
		break;
	    case 'T':
		UNSUPPORTED("for (t = t_str; (*p = *t++); p++);");
		break;
	    case 'H':
		UNSUPPORTED("for (t = h_str; (*p = *t++); p++);");
		break;
	    case 'L':
		UNSUPPORTED("for (t = l_str; (*p = *t++); p++);");
		break;
	    case '\\':
		UNSUPPORTED("if (escBackslash) {");
/*		    *p++ = '\\';
		    break; 
		}*/
		/* Fall through */
	    default:  /* leave other escape sequences unmodified, e.g. \n \l \r */
	    p.setCharAt(0, '\\');
	    p = p.plus_(1);
	    p.setCharAt(0, c);
	    p = p.plus_(1);
		break;
	    }
	} else {
	    p.setCharAt(0, c);
	    p = p.plus_(1);
	}
    }
    p.setCharAt(0, '\0');
    p = p.plus_(1);
    return newstr;
} finally {
LEAVING("ajohywvjbrvkc7zca2uew6ghm","strdup_and_subst_obj0");
}
}




//3 af2a0cdl8ld7bbq0qu0rt1d8z
// char *strdup_and_subst_obj(char *str, void *obj) 
@Unused
@Original(version="2.38.0", path="lib/common/labels.c", name="", key="af2a0cdl8ld7bbq0qu0rt1d8z", definition="char *strdup_and_subst_obj(char *str, void *obj)")
public static CString strdup_and_subst_obj(CString str, __ptr__ obj) {
ENTERING("af2a0cdl8ld7bbq0qu0rt1d8z","strdup_and_subst_obj");
try {
 UNSUPPORTED("7eeocxzl6qhtvcv7gnh73o7d1"); // char *strdup_and_subst_obj(char *str, void *obj)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("67419rdrhawe7vudn882sohkd"); //     return strdup_and_subst_obj0 (str, obj, 1);
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
} finally {
LEAVING("af2a0cdl8ld7bbq0qu0rt1d8z","strdup_and_subst_obj");
}
}



}
