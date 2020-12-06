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
package gen.lib.cdt;
import static gen.lib.cdt.dtrestore__c.dtrestore;
import static smetana.core.JUtils.EQ;
import static smetana.core.JUtils.sizeof;
import static smetana.core.JUtils.strcmp;
import static smetana.core.Macro.DT_ATTACH;
import static smetana.core.Macro.DT_CLEAR;
import static smetana.core.Macro.DT_DELETE;
import static smetana.core.Macro.DT_DETACH;
import static smetana.core.Macro.DT_FIRST;
import static smetana.core.Macro.DT_FLATTEN;
import static smetana.core.Macro.DT_FOUND;
import static smetana.core.Macro.DT_INSERT;
import static smetana.core.Macro.DT_LAST;
import static smetana.core.Macro.DT_MATCH;
import static smetana.core.Macro.DT_NEXT;
import static smetana.core.Macro.DT_OBAG;
import static smetana.core.Macro.DT_OSET;
import static smetana.core.Macro.DT_PREV;
import static smetana.core.Macro.DT_RENEW;
import static smetana.core.Macro.DT_SEARCH;
import static smetana.core.Macro.N;
import static smetana.core.Macro.UNSUPPORTED;
import static smetana.core.Macro.llink____warning;
import static smetana.core.Macro.lrotate;
import static smetana.core.Macro.rlink____warning;
import static smetana.core.Macro.rrotate;
import static smetana.core.debug.SmetanaDebug.ENTERING;
import static smetana.core.debug.SmetanaDebug.LEAVING;

import gen.annotation.Original;
import gen.annotation.Reviewed;
import h.ST_dt_s;
import h.ST_dtdisc_s;
import h.ST_dthold_s;
import h.ST_dtlink_s;
import smetana.core.CFunction;
import smetana.core.CFunctionAbstract;
import smetana.core.CString;
import smetana.core.OFFSET;
import smetana.core.__ptr__;

public class dttree__c {




static class no_root extends RuntimeException {}
static class has_root extends RuntimeException {}
static class do_search extends RuntimeException {}
static class dt_delete extends RuntimeException {}
static class dt_insert extends RuntimeException {}
static class dt_next extends RuntimeException {}



public static CFunction dttree = new CFunctionAbstract("dttree") {
	
	public Object exe(Object... args) {
		return dttree((ST_dt_s)args[0], (__ptr__)args[1], (Integer)args[2]);
	}};

@Reviewed(when = "11/11/2020")
@Original(version="2.38.0", path="lib/cdt/dttree.c", name="dttree", key="abqfzg1d1vkzk51225tcdlik5", definition="static void* dttree(Dt_t* dt, void* obj, int type)")
public static Object dttree(ST_dt_s dt, __ptr__ obj, int type) {
ENTERING("abqfzg1d1vkzk51225tcdlik5","dttree");
try {
	ST_dtlink_s	root, t;
	int		cmp, sz; OFFSET lk, ky;
	ST_dtlink_s	l, r, me=null;
	final ST_dtlink_s link = new ST_dtlink_s();
	Object		o, k, key = null;
	int		n, minp; //, turn[(sizeof(size_t)*8 - 2)];
	CFunction	cmpf;
	ST_dtdisc_s	disc;
	
	UNFLATTEN(dt);
	disc = dt.disc; ky = disc.key;
	sz = disc.size;
	lk = disc.link;
	cmpf = disc.comparf;
	dt.type &= ~DT_FOUND;
	
	root = dt.data.here;
	if(N(obj))
	{	if(N(root) || N(type&(DT_CLEAR|DT_FIRST|DT_LAST)) )
			return null;
	
		if((type&DT_CLEAR)!=0) /* delete all objects */
		{
			if(disc.freef!=null || disc.link.getSign() < 0) {
				do {
					while((t = root._left)!=null )
						{ rrotate(root, t); root = t; }
					t = root.right;
					if(disc.freef!=null)
						throw new UnsupportedOperationException();
//						(*disc->freef)(dt,(lk < 0 ? ((Dthold_t*)(root))->obj : (void*)((char*)(root) - lk) ),disc);
					if(disc.link.getSign() < 0)
						dt.memoryf.exe(dt, root, null, disc);
				} while((root = t)!=null );
			}
			dt.data.size = 0;
			dt.data.here = null;
			return null;
		}
		else /* computing largest/smallest element */
		{	if((type&DT_LAST)!=0)
			{	while((t = root.right)!=null ) {
				lrotate(root, t);
				root = t;
				}
			}
			else /* type&DT_FIRST */
			{	while((t = root._left)!=null ) {
					rrotate(root, t);
					root = t;
				}
			}
			dt.data.here = root;
			return _DTOBJ(root, lk);
		}
	}
	/* note that link.right is LEFT tree and link.left is RIGHT tree */
	l = r = link;
	/* allow apps to delete an object "actually" in the dictionary */
	try {
	if(dt.meth.type == DT_OBAG && ((type&(DT_DELETE|DT_DETACH))!=0) ) {
		throw new UnsupportedOperationException();
//	{	key = (void*)(sz < 0 ? *((char**)((char*)(obj)+ky)) : ((char*)(obj)+ky));
//		for(o = (*(((Dt_t*)(dt))->searchf))((dt),(void*)(obj),0000004); o; o = (*(((Dt_t*)(dt))->searchf))((dt),(void*)(o),0000010) )
//		{	k = (void*)(sz < 0 ? *((char**)((char*)(o)+ky)) : ((char*)(o)+ky));
//			if((cmpf ? (*cmpf)(dt,key,k,disc) : (sz <= 0 ? strcmp(key,k) : memcmp(key,k,sz)) ) != 0)
//				break;
//			if(o == obj)
//			{	root = dt->data->here;
//				l->right = root->hl._left;
//				r->hl._left  = root->right;
//				goto dt_delete;
//			}
//		}
	}
	try {
	if(((type&(DT_MATCH|DT_SEARCH|DT_INSERT|DT_ATTACH))!=0))
		{	key = ((type&DT_MATCH)!=0) ? obj : _DTKEY(obj, ky);
		if(root!=null)
			throw new do_search();
	}
	else if((type&DT_RENEW)!=0) {
		throw new UnsupportedOperationException();
//	{	me = (Dtlink_t*)obj;
//		obj = (lk < 0 ? ((Dthold_t*)(me))->obj : (void*)((char*)(me) - lk) );
//		key = (void*)(sz < 0 ? *((char**)((char*)(obj)+ky)) : ((char*)(obj)+ky));
//		if(root)
//			goto do_search;
	}
	else if(root!=null && EQ(_DTOBJ(root, lk), obj) == false)
	{	key = _DTKEY(obj, ky);
		throw new do_search();
	}
	} catch (do_search do_search) {
//		do_search:
		if(dt.meth.type == DT_OSET &&
		   (minp = dt.data.minp) != 0 && (type&(DT_MATCH|DT_SEARCH))!=0)
		{	/* simple search, note that minp should be even */
//			for(t = root, n = 0; n < minp; ++n)
//			{	k = (lk < 0 ? ((Dthold_t*)(t))->obj : (void*)((char*)(t) - lk) ); k = (void*)(sz < 0 ? *((char**)((char*)(k)+ky)) : ((char*)(k)+ky));
//				if((cmp = (cmpf ? (*cmpf)(dt,key,k,disc) : (sz <= 0 ? strcmp(key,k) : memcmp(key,k,sz)) )) == 0)
//					return (lk < 0 ? ((Dthold_t*)(t))->obj : (void*)((char*)(t) - lk) );
//				else
//				{	turn[n] = cmp;	
//					if(!(t = cmp < 0 ? t->hl._left : t->right) )
//						return ((void*)0);
//				}
//			}
//			/* exceed search length, top-down splay now */
//			for(n = 0; n < minp; n += 2)
//			{	if(turn[n] < 0)
//				{	t = root->hl._left;
//					if(turn[n+1] < 0)
//					{	((root)->hl._left = (t)->right, (t)->right = (root));
//						((r) = (r)->hl._left = (t) );
//						root = t->hl._left;
//					}
//					else
//					{	((l) = (l)->right = (t) );
//						((r) = (r)->hl._left = (root) );
//						root = t->right;
//					}
//				}
//				else
//				{	t = root->right;
//					if(turn[n+1] > 0)
//					{	((root)->right = (t)->hl._left, (t)->hl._left = (root));
//						((l) = (l)->right = (t) );
//						root = t->right;
//					}
//					else
//					{	((r) = (r)->hl._left = (t) );
//						((l) = (l)->right = (root) );
//						root = t->hl._left;
//					}
//				}
//			}
			throw new UnsupportedOperationException("do_search1");
		}
		
		
		
		while(true) {
			k = _DTOBJ(root, lk); k = _DTKEY((__ptr__) k,ky);
			if((cmp = _DTCMP(dt, key, k, disc, cmpf, sz)) == 0)
				break;
			else if(cmp < 0)
			{	if((t = root._left)!=null )
				{ k = _DTOBJ(t, lk); k = _DTKEY((__ptr__) k,ky);
				if((cmp = _DTCMP(dt, key, k, disc, cmpf, sz)) < 0)
					{	rrotate(root, t);
						r = rlink____warning(r, t);
						if(N(root = t._left) )
							break;
					}
					else if(cmp == 0)
					{	r = rlink____warning(r, root);
						root = t;
						break;
					}
					else /* if(cmp > 0) */
					{	l = llink____warning(l, t);
						r = rlink____warning(r, root);
						if(N(root = t.right) )
							break;
					}
				}
				else
				{   r = rlink____warning(r, root);
					root = null;
					break;
				}
			}
			else /* if(cmp > 0) */
			{	if ((t = root.right)!=null )
				{   k = _DTOBJ(t, lk); k = _DTKEY((__ptr__) k, ky);
					if((cmp = _DTCMP(dt, key, k, disc, cmpf, sz)) > 0)
					{   lrotate(root, t);
						l = llink____warning(l, t);
						if(N(root = t.right ))
							break;
					}
					else if(cmp == 0)
					{	l = llink____warning(l, root);
						root = t;
						break;
					}
					else /* if(cmp < 0) */
					{	r = rlink____warning(r, t);
						l = llink____warning(l, root);
						if(N(root = t._left ))
							break;
					}
				}
				else
				{	l = llink____warning(l, root);
					root = null;
					break;
				}
			}
		}
	}
	if(root!=null)
	{	/* found it, now isolate it */
		dt.type |= DT_FOUND;
		l.right = root._left;
		r._left = root.right;
		
		if((type&(DT_SEARCH|DT_MATCH))!=0)
		{ /*has_root:*/
		throw new has_root();
		}
		else if((type&DT_NEXT)!=0)
		{	root._left = link.right;
			root.right = null;
			link.right = root;
		//dt_next:
			if((root = link._left)!=null )	
			{	while((t = root._left)!=null ) {
					rrotate(root, t);
					root = t;
				}
				link._left = root.right;
				throw new has_root();
			}
			else	throw new no_root();
		}
		else if((type&DT_PREV)!=0) {
		throw new UnsupportedOperationException();
//		{	root->right = link.hl._left;
//			root->hl._left = ((Dtlink_t*)0);
//			link.hl._left = root;
//		dt_prev:
//			if((root = link.right) )
//			{	while((t = root->right) )
//					(((root)->right = (t)->hl._left, (t)->hl._left = (root)), (root) = (t));
//				link.right = root->hl._left;
//				goto has_root;
//			}
//			else	goto no_root;
		}
		else if((type&(DT_DELETE|DT_DETACH))!=0) {
//		{	/* taking an object out of the dictionary */
//		dt_delete:
			obj = (__ptr__) _DTOBJ(root,lk);
			//if(disc.freef!=null && (type&DT_DELETE)!=0)
			//UNSUPPORTED("(*disc->freef)(dt,obj,disc);");
			//if(disc.link.getSign() < 0);
			//dt.memoryf.exe(dt, root, null, disc);
			if((dt.data.size -= 1) < 0)
			UNSUPPORTED("//				dt->data->size = -1;");
			throw new no_root();
		}
		else if((type&(DT_INSERT|DT_ATTACH))!=0)
		{	if((dt.meth.type&DT_OSET)!=0)
				throw new has_root();
			else
			{   root._left = null;
				root.right = link._left;
				link._left = root;
				 /*dt_insert: DUPLICATION*/
				if(disc.makef!=null && (type&DT_INSERT)!=0)
					obj = (__ptr__) disc.makef.exe(dt,obj,disc);
				if(obj!=null)
				{
					if(lk.getSign() >= 0)
						root = _DTLNK(obj, lk);
					else
					{
						root = (ST_dtlink_s)(((ST_dthold_s)dt.memoryf.exe(
								dt,null,sizeof(ST_dthold_s.class),disc)).castTo(ST_dtlink_s.class));
						if(root!=null)
							((ST_dthold_s)root).obj = obj;
						else if(disc.makef!=null && disc.freef!=null &&
							((type&DT_INSERT))!=0)
							UNSUPPORTED("(*disc->freef)(dt,obj,disc);");
					}
				}
				if(root!=null)
				{	if(dt.data.size >= 0)
						dt.data.size += 1;
				throw new has_root();
				}
				else	throw new UnsupportedOperationException("goto no_root");
			}
		}
		else if((type&DT_RENEW)!=0) /* a duplicate */
		{
		throw new UnsupportedOperationException();
//		{	if(dt->meth->type&0000004)
//			{	if(disc->freef)
//					(*disc->freef)(dt,obj,disc);
//				if(disc->link < 0)
//					(*dt->memoryf)(dt,(void*)me,0,disc);
//			}
//			else
//			{	me->hl._left = ((Dtlink_t*)0);
//				me->right = link.hl._left;
//				link.hl._left = me;
//				dt->data->size += 1;
//			}
//			goto has_root;
		}
	}
	else
	{	/* not found, finish up LEFT and RIGHT trees */
		r._left = null;
		l.right = null;
		
		if((type&DT_NEXT)!=0)
		{
		    //goto dt_next:
			if((root = link._left)!=null )	
			{	while((t = root._left)!=null ) {
					rrotate(root, t);
					root = t;
				}
				link._left = root.right;
				throw new has_root();
			}
			else	throw new no_root();
		
		}
		else if((type&DT_PREV)!=0)
			throw new UnsupportedOperationException("goto dt_prev");
		else if((type&(DT_SEARCH|DT_MATCH))!=0)
		{
			throw new no_root();
		}
		else if((type&(DT_INSERT|DT_ATTACH))!=0)
		{ /*dt_insert: DUPLICATION*/
			if(disc.makef!=null && (type&DT_INSERT)!=0)
				obj = (__ptr__) disc.makef.exe(dt,obj,disc);
			if(obj!=null)
			{
				if(lk.getSign() >= 0)
					root = _DTLNK(obj, lk);
				else
				{
					root = (ST_dtlink_s)(((ST_dthold_s)dt.memoryf.exe(
						dt, null, sizeof(ST_dthold_s.class),disc)).castTo(ST_dtlink_s.class));
					if(root!=null)
						((ST_dthold_s)root).obj = obj;
					else if(disc.makef!=null && disc.freef!=null &&
						((type&DT_INSERT))!=0)
						UNSUPPORTED("(*disc->freef)(dt,obj,disc);");
				}
			}
			if(root!=null)
			{	if(dt.data.size >= 0)
					dt.data.size += 1;
			throw new has_root();
			}
			else	throw new UnsupportedOperationException("goto no_root");
		}
		else if((type&DT_RENEW)!=0)
		{	root = me;
			dt.data.size += 1;
			throw new UnsupportedOperationException("goto has_root");
		}
		else /*if(type&DT_DELETE)*/
		{	obj = null;
		throw new UnsupportedOperationException("goto no_root");
		}
		// throw new UnsupportedOperationException();
	}
//	return ((void*)0);
	} catch (has_root has_root) {
		root._left = link.right;
		root.right = link._left;
		if((dt.meth.type&DT_OBAG)!=0 && (type&(DT_SEARCH|DT_MATCH))!=0 )
		{	//key = (lk < 0 ? ((Dthold_t*)(root))->obj : (void*)((char*)(root) - lk) ); key = (void*)(sz < 0 ? *((char**)((char*)(key)+ky)) : ((char*)(key)+ky));
			throw new UnsupportedOperationException();
//			while((t = root->hl._left) )
//			{	/* find max of left subtree */
//				while((r = t->right) )
//					(((t)->right = (r)->hl._left, (r)->hl._left = (t)), (t) = (r));
//				root->hl._left = t;
//				/*  */
//				k = (lk < 0 ? ((Dthold_t*)(t))->obj : (void*)((char*)(t) - lk) ); k = (void*)(sz < 0 ? *((char**)((char*)(k)+ky)) : ((char*)(k)+ky));
//				if((cmpf ? (*cmpf)(dt,key,k,disc) : (sz <= 0 ? strcmp(key,k) : memcmp(key,k,sz)) ) != 0)
//					break;
//				(((root)->hl._left = (t)->right, (t)->right = (root)), (root) = (t));
//			}
		}
		dt.data.here = root;
		return _DTOBJ(root, lk);
	} catch (no_root no_root) {
			while((t = r._left)!=null)
				r = t;
			r._left = link.right;
			dt.data.here = link._left;
			return (type&DT_DELETE)!=0 ? obj : null;
	}
throw new UnsupportedOperationException();
} finally {
LEAVING("abqfzg1d1vkzk51225tcdlik5","dttree");
}
}


///* internal functions for translating among holder, object and key */
//#define _DT(dt)		((Dt_t*)(dt))
//#define _DTDSC(dc,ky,sz,lk,cmpf) \
//			(ky = dc->key, sz = dc->size, lk = dc->link, cmpf = dc->comparf)
//#define _DTKEY(o,ky,sz)	(Void_t*)(sz < 0 ? *((char**)((char*)(o)+ky)) : ((char*)(o)+ky))


//#define _DTLNK(o,lk)	((Dtlink_t*)((char*)(o) + lk) )
private static ST_dtlink_s _DTLNK(__ptr__ o, OFFSET lk) {
		final __ptr__ tmp1 = (__ptr__) o.getTheField(lk);
		final __ptr__ tmp2 = tmp1.castTo(ST_dtlink_s.class);
		return (ST_dtlink_s) tmp2;
	}

//#define _DTCMP(dt,k1,k2,dc,cmpf,sz) \
//(cmpf ? (*cmpf)(dt,k1,k2,dc) : \
// (sz <= 0 ? strcmp(k1,k2) : memcmp(k1,k2,sz)) )
private static int _DTCMP(ST_dt_s dt, Object k1, Object k2, ST_dtdisc_s dc, CFunction cmpf, int sz) {
		if (cmpf == null) {
			if (sz <= 0) {
				return strcmp((CString) k1, (CString) k2);
			}
			throw new UnsupportedOperationException("memcmp(key,k,sz))");
		}
		return (Integer) cmpf.exe(dt, k1, k2, dc);
	}


//#define _DTOBJ(e,lk)	(lk < 0 ? ((Dthold_t*)(e))->obj : (Void_t*)((char*)(e) - lk) )
private static Object _DTOBJ(ST_dtlink_s root, OFFSET lk) {
	if (lk.getSign() < 0) {
		return ((ST_dthold_s)root).obj;
	}
	return root.getTheField(lk.negative());
}


private static Object _DTKEY(__ptr__ obj, OFFSET ky) {
	return obj.getTheField(ky);
}


private static void UNFLATTEN(ST_dt_s dt) {
	if ((dt.data.type & DT_FLATTEN) !=0) { dtrestore(dt, null); }
}



}
