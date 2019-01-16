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
package gen.lib.cdt;
import static gen.lib.cdt.dtrestore__c.dtrestore;
import static smetana.core.JUtils.NEQ;
import static smetana.core.JUtils.sizeof;
import static smetana.core.JUtils.strcmp;
import static smetana.core.JUtilsDebug.ENTERING;
import static smetana.core.JUtilsDebug.LEAVING;
import static smetana.core.Macro.N;
import static smetana.core.Macro.UNSUPPORTED;
import static smetana.core.Macro.UNSUPPORTED_INT;
import h.Dtcompar_f;
import h.ST_dt_s;
import h.ST_dtdisc_s;
import h.ST_dthold_s;
import h.ST_dtlink_s;
import h.ST_dthold_s;
import smetana.core.CFunction;
import smetana.core.CString;
import smetana.core.__ptr__;

public class dttree__c {
//1 9k44uhd5foylaeoekf3llonjq
// extern Dtmethod_t* 	Dtset


//1 1ahfywsmzcpcig2oxm7pt9ihj
// extern Dtmethod_t* 	Dtbag


//1 anhghfj3k7dmkudy2n7rvt31v
// extern Dtmethod_t* 	Dtoset


//1 5l6oj1ux946zjwvir94ykejbc
// extern Dtmethod_t* 	Dtobag


//1 2wtf222ak6cui8cfjnw6w377z
// extern Dtmethod_t*	Dtlist


//1 d1s1s6ibtcsmst88e3057u9r7
// extern Dtmethod_t*	Dtstack


//1 axa7mflo824p6fspjn1rdk0mt
// extern Dtmethod_t*	Dtqueue


//1 ega812utobm4xx9oa9w9ayij6
// extern Dtmethod_t*	Dtdeque


//1 cyfr996ur43045jv1tjbelzmj
// extern Dtmethod_t*	Dtorder


//1 wlofoiftbjgrrabzb2brkycg
// extern Dtmethod_t*	Dttree


//1 12bds94t7voj7ulwpcvgf6agr
// extern Dtmethod_t*	Dthash


//1 9lqknzty480cy7zsubmabkk8h
// extern Dtmethod_t	_Dttree


//1 bvn6zkbcp8vjdhkccqo1xrkrb
// extern Dtmethod_t	_Dthash


//1 9lidhtd6nsmmv3e7vjv9e10gw
// extern Dtmethod_t	_Dtlist


//1 34ujfamjxo7xn89u90oh2k6f8
// extern Dtmethod_t	_Dtqueue


//1 3jy4aceckzkdv950h89p4wjc8
// extern Dtmethod_t	_Dtstack




static class no_root extends RuntimeException {}
static class has_root extends RuntimeException {}
static class do_search extends RuntimeException {}
static class dt_delete extends RuntimeException {}
static class dt_insert extends RuntimeException {}
static class dt_next extends RuntimeException {}
//3 abqfzg1d1vkzk51225tcdlik5
//static void* dttree(Dt_t* dt, void* obj, int type)       
public static Object dttree(ST_dt_s dt, __ptr__ obj, int type) {
ENTERING("abqfzg1d1vkzk51225tcdlik5","dttree");
try {
	ST_dtlink_s	root, t;
	int		cmp, lk, sz, ky;
	ST_dtlink_s	l, r, me=null;
	final ST_dtlink_s link = new ST_dtlink_s();
	Object		o, k, key = null;
	int		n, minp; //, turn[(sizeof(size_t)*8 - 2)];
	Dtcompar_f	cmpf;
	ST_dtdisc_s	disc;
	if (((dt.data.type) & 010000) !=0) { dtrestore(dt,null); }
	disc = (ST_dtdisc_s) dt.disc; ky = disc.key;
	sz = disc.size;
	lk = disc.link;
	cmpf = (Dtcompar_f) disc.comparf;
	dt.setInt("type", dt.type&~0100000);
	root = (ST_dtlink_s) dt.data.here;
	if(N(obj))
	{	if(N(root) || N(type&(0000100|0000200|0000400)) )
			return null;
		if((type&0000100)!=0) /* delete all objects */
		{
			if(disc.freef!=null || disc.link < 0) {
				do {
					while((t = (ST_dtlink_s) root._left)!=null ) {
						root._left = t.right;
						t.right = root;
						root = t;
					}
					t = (ST_dtlink_s) root.right;
					if(disc.freef!=null)
						throw new UnsupportedOperationException();
//						(*disc->freef)(dt,(lk < 0 ? ((Dthold_t*)(root))->obj : (void*)((char*)(root) - lk) ),disc);
					if(disc.link < 0)
						dt.memoryf.exe(dt, root, null, disc);
				} while((root = t)!=null );
			}
			dt.data.setInt("size", 0);
			dt.data.here = null;
			return null;
		}
		else /* computing largest/smallest element */
		{	if((type&0000400)!=0)
			{	while((t = (ST_dtlink_s) root.right)!=null ) {
				root.right = t._left;
				t._left = root;
				root = t;
				}
			}
			else /* type&DT_FIRST */
			{	while((t = (ST_dtlink_s) root._left)!=null ) {
					root._left = t.right;
					t.right = root;
					root = t;
				}
			}
			dt.data.here = root;
			return (lk < 0 ? root.castTo_ST_dthold_s().obj : root.addVirtualBytes(-lk) );
		}
	}
	/* note that link.right is LEFT tree and link.left is RIGHT tree */
	l = r = link;
	/* allow apps to delete an object "actually" in the dictionary */
	try {
	if(dt.meth.type == 0000010 && ((type&(0000002|0010000))!=0) ) {
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
	if(((type&(0001000|0000004|0000001|0004000))!=0))
		{	key = ((type&0001000)!=0) ? obj : (sz < 0 ? ((__ptr__)obj).addVirtualBytes(ky) : ((__ptr__)obj).addVirtualBytes(ky));
		if(root!=null)
			throw new do_search();
	}
	else if((type&0000040)!=0) {
		throw new UnsupportedOperationException();
//	{	me = (Dtlink_t*)obj;
//		obj = (lk < 0 ? ((Dthold_t*)(me))->obj : (void*)((char*)(me) - lk) );
//		key = (void*)(sz < 0 ? *((char**)((char*)(obj)+ky)) : ((char*)(obj)+ky));
//		if(root)
//			goto do_search;
	}
	else if(root!=null && (lk < 0 ? (root.castTo_ST_dthold_s().obj!=null): NEQ(root.addVirtualBytes(-lk), obj)))
	{	key = (sz < 0 ? ((__ptr__)obj).addVirtualBytes(ky) : ((__ptr__)obj).addVirtualBytes(ky));
		throw new do_search();
	}
	} catch (do_search do_search) {
//		do_search:
		if(dt.meth.type == 0000004 &&
		   (minp = dt.data.minp) != 0 && (type&(0001000|0000004))!=0)
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
			k = lk < 0 ? root.castTo_ST_dthold_s().obj : root.addVirtualBytes(-lk); 
			k = sz < 0 ? ((__ptr__)k).addVirtualBytes(ky) : ((__ptr__)k).addVirtualBytes(ky);
			if((cmp = (cmpf!=null ? (Integer)((CFunction)cmpf).exe(dt,key,k,disc) : 
				(sz <= 0 ? strcmp((CString)key,(CString)k) : UNSUPPORTED_INT("memcmp(key,k,sz))") ))) == 0)
				break;
			else if(cmp < 0)
			{	if((t = (ST_dtlink_s) root._left)!=null )
				{
				k = lk < 0 ? t.castTo_ST_dthold_s().obj : t.addVirtualBytes(-lk);
				k = sz < 0 ? ((__ptr__)k).addVirtualBytes(ky) : ((__ptr__)k).addVirtualBytes(ky);
				if((cmp = (cmpf!=null ? (Integer)((CFunction)cmpf).exe(dt,key,k,disc)
	 					 : (sz <= 0 ? strcmp((CString)key,(CString)k) : UNSUPPORTED_INT("memcmp(key,k,sz))") ))) < 0)
					{	root._left = t.right;
						t.right = root;
						r._left = t;
						r = t;
						if(N(root = (ST_dtlink_s) t._left) )
							break;
					}
					else if(cmp == 0)
					{	r._left = root;
						r = root;
						root = t;
						break;
					}
					else /* if(cmp > 0) */
					{	l.right = t;
						l = t;
						r._left = root;
						r  = root;
						if(N(root = (ST_dtlink_s) t.right) )
							break;
					}
				}
				else
				{
					r._left = root;
					r = root;
					root = null;
					break;
				}
			}
			else /* if(cmp > 0) */
			{	if ((t = (ST_dtlink_s) root.right)!=null )
				{
					k = (lk < 0 ? t.castTo_ST_dthold_s().obj : t.addVirtualBytes(-lk) ); 
 					k = sz < 0 ? ((__ptr__)k).addVirtualBytes(ky) : ((__ptr__)k).addVirtualBytes(ky);
					if((cmp = (cmpf!=null ? (Integer)((CFunction)cmpf).exe(dt,key,k,disc) 
 					 : (sz <= 0 ? strcmp((CString)key,(CString)k) : UNSUPPORTED_INT("memcmp(key,k,sz))") ))) > 0)
					{
						root.right = t._left;
						t._left = root;
						l.right = t;
						l = t;
						if(N(root = (ST_dtlink_s) t.right ))
							break;
					}
					else if(cmp == 0)
					{	l.right = root;
						l = root;
						root = t;
						break;
					}
					else /* if(cmp < 0) */
					{	r._left = t;
						r = t;
						l.right = root;
						l = root;
						if(N(root = (ST_dtlink_s) t._left ))
							break;
					}
				}
				else
				{	l.right = root;
					l = root;
					root = null;
					break;
				}
			}
		}
	}
	if(root!=null)
	{	/* found it, now isolate it */
		dt.setInt("type", dt.type | 0100000);
		l.right = root._left;
		r._left = root.right;
		if((type&(0000004|0001000))!=0)
		{ /*has_root:*/
		throw new has_root();
		}
		else if((type&0000010)!=0)
		{	root._left = link.right;
			root.right = null;
			link.right = root;
		//dt_next:
			if((root = (ST_dtlink_s) link._left)!=null )	
			{	while((t = (ST_dtlink_s) root._left)!=null ) {
					root._left = t.right;
					t.right = root;
					root = t;
				}
				link._left = root.right;
				throw new has_root();
			}
			else	throw new no_root();
		}
		else if((type&0000020)!=0) {
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
		else if((type&(0000002|0010000))!=0) {
		throw new UnsupportedOperationException();
//		{	/* taking an object out of the dictionary */
//		dt_delete:
//			obj = (lk < 0 ? ((Dthold_t*)(root))->obj : (void*)((char*)(root) - lk) );
//			if(disc->freef && (type&0000002))
//				(*disc->freef)(dt,obj,disc);
//			if(disc->link < 0)
//				(*dt->memoryf)(dt,(void*)root,0,disc);
//			if((dt->data->size -= 1) < 0)
//				dt->data->size = -1;
//			goto no_root;
		}
		else if((type&(0000001|0004000))!=0)
		{	if((dt.meth.type&0000004)!=0)
				throw new has_root();
			else
		{
				root._left = null;
				root.right = link._left;
				link._left = root;
				 /*dt_insert: DUPLICATION*/
				if(disc.makef!=null && (type&0000001)!=0)
					obj = (__ptr__) disc.makef.exe(dt,obj,disc);
				if(obj!=null)
				{
					if(lk >= 0)
						root = (ST_dtlink_s) ((__ptr__)obj.addVirtualBytes(lk)).castTo(ST_dtlink_s.class);
					else
					{
						root = (ST_dtlink_s)(((ST_dthold_s)dt.memoryf.exe(
								dt,null,sizeof(ST_dthold_s.class),disc)).castTo(ST_dtlink_s.class));
						if(root!=null)
							root.castTo(ST_dthold_s.class).setPtr("obj", obj);
						else if(disc.makef!=null && disc.freef!=null &&
							((type&0000001))!=0)
							UNSUPPORTED("(*disc->freef)(dt,obj,disc);");
					}
				}
				if(root!=null)
				{	if(dt.data.size >= 0)
						dt.data.setInt("size", dt.data.size+1 );
				throw new has_root();
				}
				else	throw new UnsupportedOperationException("goto no_root");
			}
		}
		else if((type&0000040)!=0) /* a duplicate */ {
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
		if((type&0000010)!=0)
		{
		    //goto dt_next:
			if((root = (ST_dtlink_s) link._left)!=null )	
			{	while((t = (ST_dtlink_s) root._left)!=null ) {
					root._left = t.right;
					t.right = root;
					root = t;
				}
				link._left = root.right;
				throw new has_root();
			}
			else	throw new no_root();
		
		}
		else if((type&0000020)!=0)
			throw new UnsupportedOperationException("goto dt_prev");
		else if((type&(0000004|0001000))!=0)
		{
			throw new no_root();
		}
		else if((type&(0000001|0004000))!=0)
		{ /*dt_insert: DUPLICATION*/
			if(disc.makef!=null && (type&0000001)!=0)
				obj = (__ptr__) disc.makef.exe(dt,obj,disc);
			if(obj!=null)
			{
				if(lk >= 0)
					root = (ST_dtlink_s) ((__ptr__)obj.addVirtualBytes(lk)).castTo(ST_dtlink_s.class);
				else
				{
					root = (ST_dtlink_s)(((ST_dthold_s)dt.memoryf.exe(
						dt,null,sizeof(ST_dthold_s.class),disc)).castTo(ST_dtlink_s.class));
					if(root!=null)
						root.castTo(ST_dthold_s.class).setPtr("obj", obj);
					else if(disc.makef!=null && disc.freef!=null &&
						((type&0000001))!=0)
						UNSUPPORTED("(*disc->freef)(dt,obj,disc);");
				}
			}
			if(root!=null)
			{	if(dt.data.size >= 0)
					dt.data.setInt("size", dt.data.size+1 );
			throw new has_root();
			}
			else	throw new UnsupportedOperationException("goto no_root");
		}
		else if((type&0000040)!=0)
		{	root = me;
			dt.data.setInt("size", dt.data.size+1 );
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
	    root = (ST_dtlink_s) root.castTo(ST_dtlink_s.class);
		root._left = link.right;
		root.right = link._left;
		if((dt.meth.type&0000010)!=0 && (type&(0000004|0001000))!=0 )
		{	//key = (lk < 0 ? ((Dthold_t*)(root))->obj : (void*)((char*)(root) - lk) ); key = (void*)(sz < 0 ? *((char**)((char*)(key)+ky)) : ((char*)(key)+ky));
			throw new UnsupportedOperationException();
//			while((t = root->hl._left) )
//			{	/* find max of left subtree */
//				while((r = t->right) )
//					(((t)->right = (r)->hl._left, (r)->hl._left = (t)), (t) = (r));
//				root->hl._left = t;
//				/* now see if it's in the same group */
//				k = (lk < 0 ? ((Dthold_t*)(t))->obj : (void*)((char*)(t) - lk) ); k = (void*)(sz < 0 ? *((char**)((char*)(k)+ky)) : ((char*)(k)+ky));
//				if((cmpf ? (*cmpf)(dt,key,k,disc) : (sz <= 0 ? strcmp(key,k) : memcmp(key,k,sz)) ) != 0)
//					break;
//				(((root)->hl._left = (t)->right, (t)->right = (root)), (root) = (t));
//			}
		}
		dt.data.here = root;
		return (lk < 0 ? root.castTo_ST_dthold_s().obj : root.addVirtualBytes(-lk));
	} catch (no_root no_root) {
			while((t = (ST_dtlink_s) r._left)!=null)
				r = t;
			r._left = link.right;
			dt.data.here = link._left;
			return (type&0000002)!=0 ? obj : null;
	}
throw new UnsupportedOperationException();
} finally {
LEAVING("abqfzg1d1vkzk51225tcdlik5","dttree");
}
}


//1 9g22bw0wprm2n836sva9a545j
// static Dtmethod_t	_Dtoset =  


//1 6lntjtw57dbb52lukeu9qbjm0
// static Dtmethod_t	_Dtobag =  


//1 23jnfpje5fw0aejl101d0mofi
// Dtmethod_t* Dtoset = &_Dtoset


//1 b8q5rjkvf93u0f27esfu3h9ay
// Dtmethod_t* Dtobag = &_Dtobag


//1 5kf1n1yhdy9ojgc0mc3pyowaz
// Dtmethod_t		_Dttree = 
/*public static final __struct__<_dtmethod_s> _Dttree = JUtils.from(_dtmethod_s.class);
static {
	_Dttree.setPtr("searchf", function(dttree__c.class, "dttree"));
	_Dttree.setInt("type", 0000004);
}*/

//1 bvfgwxg0ik8j0au3xhv5ear7h
// Dtmethod_t* Dtorder = &_Dttree


//1 avyrqfvu00yyj95dihtoiwmao
// Dtmethod_t* Dttree = &_Dttree
//public static _dtmethod_s Dttree = _Dttree.amp();
}
