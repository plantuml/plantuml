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
package gen.lib.common;
import static smetana.core.Macro.UNSUPPORTED;

public class intset__c {
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




//3 8qgr88nich230f7xchwdzz29a
// static void* mkIntItem(Dt_t* d,intitem* obj,Dtdisc_t* disc) 
public static Object mkIntItem(Object... arg) {
UNSUPPORTED("8fbx43f8g4rod2yqfdymsxdnx"); // static void*
UNSUPPORTED("a10lzkc4r102m2qlk5imsvovv"); // mkIntItem(Dt_t* d,intitem* obj,Dtdisc_t* disc)
UNSUPPORTED("yo7buicdiu29rv5vxhas0v3s"); // { 
UNSUPPORTED("ekjuvztgs19rbqj0v3lmfo01q"); //     intitem* np = (intitem*)zmalloc(sizeof(intitem));
UNSUPPORTED("506xq20ierdh2vdh0oxwa7m8v"); //     np->id = obj->id;
UNSUPPORTED("184diuw6jvuoeak7fkbqm6fr6"); //     return (void*)np;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 bh6e5ln10dj2wbs1o7bpt00i8
// static void freeIntItem(Dt_t* d,intitem* obj,Dtdisc_t* disc) 
public static Object freeIntItem(Object... arg) {
UNSUPPORTED("e2z2o5ybnr5tgpkt8ty7hwan1"); // static void
UNSUPPORTED("criv4pqv4650sbunujgt3rbya"); // freeIntItem(Dt_t* d,intitem* obj,Dtdisc_t* disc)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("4neq75mnpa0cym29pxiizrkz3"); //     free (obj);
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 47m5dtm8t3vpv956ejy5w6th9
// static int cmpid(Dt_t* d, int* key1, int* key2, Dtdisc_t* disc) 
public static Object cmpid(Object... arg) {
UNSUPPORTED("eyp5xkiyummcoc88ul2b6tkeg"); // static int
UNSUPPORTED("1si01iycmvt6w5p4npvowvekx"); // cmpid(Dt_t* d, int* key1, int* key2, Dtdisc_t* disc)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("1cu94gjng90rrt7xtp42ifr1"); //   if (*key1 > *key2) return 1;
UNSUPPORTED("3h1tdtwyfqaqhne2o7pz8yq9h"); //   else if (*key1 < *key2) return -1;
UNSUPPORTED("7lrkjjj5lce2uf86c1y9o9yoa"); //   else return 0;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}


//1 8lrju563ta1xqsy4xixotjo3l
// static Dtdisc_t intSetDisc = 




//3 2xsz5eza9h7l039872rv37hsv
// Dt_t*  openIntSet (void) 
public static Object openIntSet(Object... arg) {
UNSUPPORTED("96gezykql110n8xkno0gtdrmq"); // Dt_t* 
UNSUPPORTED("68yvszwdppo58pmasvi3gqm0a"); // openIntSet (void)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("bjd7jaxyhis3c4zl58948rs5w"); //     return dtopen(&intSetDisc,Dtoset);
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 djjwoeky7tlyxdg2zx3x4pdib
// void  addIntSet (Dt_t* is, int v) 
public static Object addIntSet(Object... arg) {
UNSUPPORTED("347dderd02mvlozoheqo4ejwo"); // void 
UNSUPPORTED("uq3ewaypmpqc0nyc6mp0osz6"); // addIntSet (Dt_t* is, int v)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("2rjvy9cij6zo2nowb4w37hnjt"); //     intitem obj;
UNSUPPORTED("5ne3fj3dr6iouu1y3bnx3lcd7"); //     obj.id = v;
UNSUPPORTED("2ugf3ujkfn9ofv5ykvcsvr6m5"); //     (*(((Dt_t*)(is))->searchf))((is),(void*)(&obj),0000001);
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 7w5km9pct0qd0skzhgxezhr2x
// int  inIntSet (Dt_t* is, int v) 
public static Object inIntSet(Object... arg) {
UNSUPPORTED("7zkpme13g8rxxwloxvpvvnbcw"); // int 
UNSUPPORTED("cg6pm0yggmopx83usksgtt395"); // inIntSet (Dt_t* is, int v)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("eckrym421t3ug9lj77hzalv1a"); //     return ((*(((Dt_t*)(is))->searchf))((is),(void*)(&v),0001000) != 0);
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}


}
