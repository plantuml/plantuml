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
package gen.lib.xdot;
import static smetana.core.Macro.UNSUPPORTED;

public class xdot__c {


//3 18i9xd96uncylv9umnz90gzrh
// static void agxbinit(agxbuf * xb, unsigned int hint, unsigned char *init) 
public static Object agxbinit(Object... arg) {
UNSUPPORTED("84pe35dre7aor7etu052tqf8t"); // static void agxbinit(agxbuf * xb, unsigned int hint, unsigned char *init)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("3ctz7esjgupftv01eshc2dhx2"); //     if (init) {
UNSUPPORTED("7ccqm4ipez0mmdk6bv50shi8z"); // 	xb->buf = init;
UNSUPPORTED("bgb2e1tveztx6w0nuo6t6kxbd"); // 	xb->dyna = 0;
UNSUPPORTED("c07up7zvrnu2vhzy6d7zcu94g"); //     } else {
UNSUPPORTED("49jhfm9yw3megswomc4gzzgd7"); // 	if (hint == 0)
UNSUPPORTED("5h1ggxg5ypn75rue18vgizp12"); // 	    hint = BUFSIZ;
UNSUPPORTED("bwuph2kus95n04yy45ff0ygwg"); // 	xb->dyna = 1;
UNSUPPORTED("b3ogvpic8o2143ihrt95fsmt5"); // 	xb->buf = (unsigned char*)calloc((hint), sizeof(unsigned char));
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("1vqjbr0qaxnp1tks2ilwqgn3g"); //     xb->eptr = xb->buf + hint;
UNSUPPORTED("dtbxbzhkw05lut0ozk9a49lw6"); //     xb->ptr = xb->buf;
UNSUPPORTED("5ymin98xo0ermvpyhsuo8xwim"); //     *xb->ptr = '\0';
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 deo0nqa6fmyacu293m9q0f3ad
// static int agxbmore(agxbuf * xb, unsigned int ssz) 
public static Object agxbmore(Object... arg) {
UNSUPPORTED("dr7xxpbudfd4q496z1hjh0uam"); // static int agxbmore(agxbuf * xb, unsigned int ssz)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("ayzlme0ebe3j87lpz076cg2vq"); //     int cnt;			/* current no. of characters in buffer */
UNSUPPORTED("71r9w1f25l9h79jodjer7my8a"); //     int size;			/* current buffer size */
UNSUPPORTED("2bckq0rejaf94iovglem6hqko"); //     int nsize;			/* new buffer size */
UNSUPPORTED("dmokvf86b5kj3srnwwbr7ebmc"); //     unsigned char *nbuf;	/* new buffer */
UNSUPPORTED("347vqdgen21gpinnet4lkk7gk"); //     size = xb->eptr - xb->buf;
UNSUPPORTED("49rolm7orqt4aw3uud529g4qc"); //     nsize = 2 * size;
UNSUPPORTED("6t3aq9fvpvhbjssfqnjmktfh0"); //     if (size + ssz > nsize)
UNSUPPORTED("7ykdy1xuvrmibubukfxhfmz91"); // 	nsize = size + ssz;
UNSUPPORTED("bfinudkdiev8mwo6udbayoaex"); //     cnt = xb->ptr - xb->buf;
UNSUPPORTED("4cmq49lu7qhmui59rl36qd6fr"); //     if (xb->dyna) {
UNSUPPORTED("8vf8arbygb0610hkc403uiror"); // 	nbuf = realloc(xb->buf, nsize);
UNSUPPORTED("c07up7zvrnu2vhzy6d7zcu94g"); //     } else {
UNSUPPORTED("3k38m0q2g7fwya1dna0s39dqc"); // 	nbuf = (unsigned char*)calloc((nsize), sizeof(unsigned char));
UNSUPPORTED("do1ts7n0vopj00hs85yw95qjx"); // 	memcpy(nbuf, xb->buf, cnt);
UNSUPPORTED("bwuph2kus95n04yy45ff0ygwg"); // 	xb->dyna = 1;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("5a5gutjqda6fhdylt7vi64g71"); //     xb->buf = nbuf;
UNSUPPORTED("dodqzfdb41641741187lm9aci"); //     xb->ptr = xb->buf + cnt;
UNSUPPORTED("e45cf41wsuzfjfi2my7jn7odp"); //     xb->eptr = xb->buf + nsize;
UNSUPPORTED("5oxhd3fvp0gfmrmz12vndnjt"); //     return 0;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 8zf71o02uu26r06lmjzr6c8df
// static int agxbput(char *s, agxbuf * xb) 
public static Object agxbput(Object... arg) {
UNSUPPORTED("dpuodut5jl740qijx0viiicj3"); // static int agxbput(char *s, agxbuf * xb)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("dmstj6pqesin7a4ufrtut65t5"); //     unsigned int ssz = strlen(s);
UNSUPPORTED("6q0wy2t9qi11079j0wyuhsmiw"); //     if (xb->ptr + ssz > xb->eptr)
UNSUPPORTED("82p9dlopf5tuzmyy5454sv4mm"); // 	agxbmore(xb, ssz);
UNSUPPORTED("9tvk5ztcu9lg2u40sfmevitl2"); //     memcpy(xb->ptr, s, ssz);
UNSUPPORTED("2wy6om6o92zevnnevl5v0pr0c"); //     xb->ptr += ssz;
UNSUPPORTED("dzondrrxi2pe7xy72r0jstd20"); //     return ssz;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 6pn991iar889bzaq1znh7dj3b
// static void agxbfree(agxbuf * xb) 
public static Object agxbfree(Object... arg) {
UNSUPPORTED("3dnzpw59o9bwwyrb7eat7i3u4"); // static void agxbfree(agxbuf * xb)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("da8gv3dgf6bnks54n9mnlgzxk"); //     if (xb->dyna)
UNSUPPORTED("62irfu7vrq2ewolixn0ksp5f6"); // 	free(xb->buf);
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 4l360un6jpvisf8n73t7pqy9
// static char *parseReal(char *s, double *fp) 
public static Object parseReal(Object... arg) {
UNSUPPORTED("27cxezovcf9115tk1jj1gyw8j"); // static char *parseReal(char *s, double *fp)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("aexhdud6z2wbwwi73yppp0ynl"); //     char *p;
UNSUPPORTED("8ne6p4d5pykwl1d3xk0yg0ipb"); //     double d;
UNSUPPORTED("1xd0ne3bq4byy8xcbce2jvh7s"); //     d = strtod(s, &p);
UNSUPPORTED("66hd5x2o87fqb7ug58kbyi4e5"); //     if (p == s) return 0;
UNSUPPORTED("f3dzvb82hjtktpqsfn7uyp86z"); //     *fp = d;
UNSUPPORTED("1fnaj5qlhzixwhovwrjmw4vgb"); //     return (p);
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 bsxeh14qqmz6v3qdi9irrllr
// static char *parseInt(char *s, int *ip) 
public static Object parseInt(Object... arg) {
UNSUPPORTED("73v5uzvbtjguvilqpgltjnq7t"); // static char *parseInt(char *s, int *ip)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("bkz9mqmemp1ljxdwdbu8xv3e9"); //     char* endp;
UNSUPPORTED("c5qif45ng54sfzc28pvot6iwq"); //     *ip = (int)strtol (s, &endp, 10);
UNSUPPORTED("ct4gh2jrkm41477ikplh0rn57"); //     if (s == endp)
UNSUPPORTED("c9ckhc8veujmwcw0ar3u3zld4"); // 	return 0;
UNSUPPORTED("div10atae09n36x269sl208r1"); //     else
UNSUPPORTED("2ulm7bppf40z5aob8ngdrce73"); // 	return endp;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 3mwttoeqe8u9fia2d46vwzsve
// static char *parseUInt(char *s, unsigned int *ip) 
public static Object parseUInt(Object... arg) {
UNSUPPORTED("enlzxotrmwou3zcdcg63q0u1i"); // static char *parseUInt(char *s, unsigned int *ip)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("bkz9mqmemp1ljxdwdbu8xv3e9"); //     char* endp;
UNSUPPORTED("2osjo1qcy5wbt8hb0i484t7gl"); //     *ip = (unsigned int)strtoul (s, &endp, 10);
UNSUPPORTED("ct4gh2jrkm41477ikplh0rn57"); //     if (s == endp)
UNSUPPORTED("c9ckhc8veujmwcw0ar3u3zld4"); // 	return 0;
UNSUPPORTED("div10atae09n36x269sl208r1"); //     else
UNSUPPORTED("2ulm7bppf40z5aob8ngdrce73"); // 	return endp;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 8jcdtdxdrx01gn9f1nebzs68r
// static char *parseRect(char *s, xdot_rect * rp) 
public static Object parseRect(Object... arg) {
UNSUPPORTED("8cfahk8txxaintbez7726qocf"); // static char *parseRect(char *s, xdot_rect * rp)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("bkz9mqmemp1ljxdwdbu8xv3e9"); //     char* endp;
UNSUPPORTED("ayw7luucspha90kbooccenfvz"); //     rp->x = strtod (s, &endp);
UNSUPPORTED("ct4gh2jrkm41477ikplh0rn57"); //     if (s == endp)
UNSUPPORTED("c9ckhc8veujmwcw0ar3u3zld4"); // 	return 0;
UNSUPPORTED("div10atae09n36x269sl208r1"); //     else
UNSUPPORTED("6ojhqyohyu9t2drew53jrtxrw"); // 	s = endp;
UNSUPPORTED("3bz6uhgg11k6vjacbsjnx1wi3"); //     rp->y = strtod (s, &endp);
UNSUPPORTED("ct4gh2jrkm41477ikplh0rn57"); //     if (s == endp)
UNSUPPORTED("c9ckhc8veujmwcw0ar3u3zld4"); // 	return 0;
UNSUPPORTED("div10atae09n36x269sl208r1"); //     else
UNSUPPORTED("6ojhqyohyu9t2drew53jrtxrw"); // 	s = endp;
UNSUPPORTED("7o4n2gvc98j75dt3j9dxbjo78"); //     rp->w = strtod (s, &endp);
UNSUPPORTED("ct4gh2jrkm41477ikplh0rn57"); //     if (s == endp)
UNSUPPORTED("c9ckhc8veujmwcw0ar3u3zld4"); // 	return 0;
UNSUPPORTED("div10atae09n36x269sl208r1"); //     else
UNSUPPORTED("6ojhqyohyu9t2drew53jrtxrw"); // 	s = endp;
UNSUPPORTED("7yrlj0k618ctn709clsxdxl1d"); //     rp->h = strtod (s, &endp);
UNSUPPORTED("ct4gh2jrkm41477ikplh0rn57"); //     if (s == endp)
UNSUPPORTED("c9ckhc8veujmwcw0ar3u3zld4"); // 	return 0;
UNSUPPORTED("div10atae09n36x269sl208r1"); //     else
UNSUPPORTED("6ojhqyohyu9t2drew53jrtxrw"); // 	s = endp;
UNSUPPORTED("3y6wj3ntgmr1qkdpm7wp1dsch"); //     return s;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 9zgiw1nnozptos14kotjlqzes
// static char *parsePolyline(char *s, xdot_polyline * pp) 
public static Object parsePolyline(Object... arg) {
UNSUPPORTED("5vennd9p6hb7tz8vier5n3awr"); // static char *parsePolyline(char *s, xdot_polyline * pp)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("b17di9c7wgtqm51bvsyxz6e2f"); //     int i;
UNSUPPORTED("aqektudazrrmp3o28qu9lc8lr"); //     xdot_point *pts;
UNSUPPORTED("224clts6kmlangjk0mxje18pw"); //     xdot_point *ps;
UNSUPPORTED("bkz9mqmemp1ljxdwdbu8xv3e9"); //     char* endp;
UNSUPPORTED("9k4v6oml42z495w37d15bdhgq"); //     s = parseInt(s, &i);
UNSUPPORTED("4tppo7loevc5w3rzs7kcd6q3s"); //     if (!s) return s;
UNSUPPORTED("2jikm4wubyo74pnrnkqlp64n3"); //     pts = ps = (xdot_point*)calloc((i), sizeof(xdot_point));
UNSUPPORTED("4eh3im98g60ywzmhhcdwd6fvl"); //     pp->cnt = i;
UNSUPPORTED("cphbu2o9nlpu87ymbrrqqzxyd"); //     for (i = 0; i < pp->cnt; i++) {
UNSUPPORTED("31n0sbteq5vit0duqm4fw62us"); // 	ps->x = strtod (s, &endp);
UNSUPPORTED("1jb4h180dxiqktyhn2gltjn0v"); // 	if (s == endp) {
UNSUPPORTED("7bqn4djl4wq567zf4ce3ocb57"); // 	    free (pts);
UNSUPPORTED("6f1138i13x0xz1bf1thxgjgka"); // 	    return 0;
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("9352ql3e58qs4fzapgjfrms2s"); // 	else
UNSUPPORTED("91nzbbhlz222quh6avosg1gs3"); // 	    s = endp;
UNSUPPORTED("e65aaxn30tf5qsj1a1qq7w231"); // 	ps->y = strtod (s, &endp);
UNSUPPORTED("1jb4h180dxiqktyhn2gltjn0v"); // 	if (s == endp) {
UNSUPPORTED("7bqn4djl4wq567zf4ce3ocb57"); // 	    free (pts);
UNSUPPORTED("6f1138i13x0xz1bf1thxgjgka"); // 	    return 0;
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("9352ql3e58qs4fzapgjfrms2s"); // 	else
UNSUPPORTED("91nzbbhlz222quh6avosg1gs3"); // 	    s = endp;
UNSUPPORTED("cc5r26z00xi25odsicfy1n1mg"); // 	ps->z = 0;
UNSUPPORTED("87sp63nt8twfjdetw9u20nv3l"); // 	ps++;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("6rgl3frmyuop1sfw5ytbajpe"); //     pp->pts = pts;
UNSUPPORTED("3y6wj3ntgmr1qkdpm7wp1dsch"); //     return s;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 9ndna3jb07n84p1schywyuztb
// static char *parseString(char *s, char **sp) 
public static Object parseString(Object... arg) {
UNSUPPORTED("6ho8lvhy0v0vmiwxtchvowdag"); // static char *parseString(char *s, char **sp)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("b17di9c7wgtqm51bvsyxz6e2f"); //     int i;
UNSUPPORTED("a1dehvgts7vkximbs2yo0q6pa"); //     char *c;
UNSUPPORTED("aexhdud6z2wbwwi73yppp0ynl"); //     char *p;
UNSUPPORTED("9k4v6oml42z495w37d15bdhgq"); //     s = parseInt(s, &i);
UNSUPPORTED("8t62hsoemjy6c138b6890zgr6"); //     if (!s || (i <= 0)) return 0;
UNSUPPORTED("3ugsfktr0fa4ozg4wrl9ol7dc"); //     while (*s && (*s != '-')) s++;
UNSUPPORTED("98g0ujdodpjy3g7nibv2z5ait"); //     if (*s) s++;
UNSUPPORTED("1nyzbeonram6636b1w955bypn"); //     else {
UNSUPPORTED("c9ckhc8veujmwcw0ar3u3zld4"); // 	return 0;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("50parur6008yns69htbrun3l2"); //     c = (char*)calloc((i + 1), sizeof(char));
UNSUPPORTED("c920n72fhgint9bu1xhd382e5"); //     p = c;
UNSUPPORTED("7ph7rzi6mme47sk2y140dwlbm"); //     while ((i > 0) && *s) {
UNSUPPORTED("8e6st6hhl4yfs7qub4b87bkm0"); // 	*p++ = *s++;
UNSUPPORTED("2fvaz29y6ivihvxotxz84fsl9"); // 	i--;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("2gn9hkx01ingqp86itwc4b5dm"); //     if (i > 0) {
UNSUPPORTED("1jlx2cuwttezm3zsuuehbh1l8"); // 	free (c);
UNSUPPORTED("c9ckhc8veujmwcw0ar3u3zld4"); // 	return 0;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("2t1d66kwn86kqh0i665hqw6cl"); //     *p = '\0';
UNSUPPORTED("a0gcud6ayeka85e6o5adbzjd2"); //     *sp = c;
UNSUPPORTED("3y6wj3ntgmr1qkdpm7wp1dsch"); //     return s;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 7reshvtfhworq8s9q6lowo6nl
// static char *parseAlign(char *s, xdot_align * ap) 
public static Object parseAlign(Object... arg) {
UNSUPPORTED("8si3sn14sdmyqz32r6ja6zl9h"); // static char *parseAlign(char *s, xdot_align * ap)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("b17di9c7wgtqm51bvsyxz6e2f"); //     int i;
UNSUPPORTED("9k4v6oml42z495w37d15bdhgq"); //     s = parseInt(s, &i);
UNSUPPORTED("7jqpiltkvdvvkjldnxqmz4nfw"); //     if (i < 0)
UNSUPPORTED("bfu0mpq4mmjh0pu6dyx8xlrna"); // 	*ap = xd_left;
UNSUPPORTED("8544wlrukmsh3w27eef43oivi"); //     else if (i > 0)
UNSUPPORTED("1bi0jgsifwqkvdvmho2gvvfad"); // 	*ap = xd_right;
UNSUPPORTED("div10atae09n36x269sl208r1"); //     else
UNSUPPORTED("6pvvrqivfj7ir7h3u5x5yfrrc"); // 	*ap = xd_center;
UNSUPPORTED("3y6wj3ntgmr1qkdpm7wp1dsch"); //     return s;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 ehnxo19guzhgzj6t8jgklcy9t
// static char *parseOp(xdot_op * op, char *s, drawfunc_t ops[], int* error) 
public static Object parseOp(Object... arg) {
UNSUPPORTED("6n4wgd5e0tmwigktwbqr7b2mh"); // static char *parseOp(xdot_op * op, char *s, drawfunc_t ops[], int* error)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("1al7rtqrsk1oru479p6d19nt"); //     char* cs;
UNSUPPORTED("dcftaleqvuytghzlz8x1uo05u"); //     xdot_color clr;
UNSUPPORTED("6fdmf6t8ozu6ifv41ah2khjh5"); //     *error = 0;
UNSUPPORTED("5gi4ktom734bivy5bia2jyoyg"); //     while (isspace(*s))
UNSUPPORTED("1fe0kohehgdxhenrepo7ymdcw"); // 	s++;
UNSUPPORTED("avvtfrt0s9pcst93venr1pjk3"); //     switch (*s++) {
UNSUPPORTED("4qkgjhvxkpafhor6qtoe37y14"); //     case 'E':
UNSUPPORTED("9dnkzv2t54la5ntx5vagvydnh"); // 	op->kind = xd_filled_ellipse;
UNSUPPORTED("atahrjx7tu9jw9spyi7jwfptn"); // 	s = parseRect(s, &op->u.ellipse);
UNSUPPORTED("dgnldyd2pb5vmg1g32106eh4l"); // 	if(!s){*error=1;return 0;};
UNSUPPORTED("68fwibhr1nqhg298v23jvoosh"); // 	if (ops)
UNSUPPORTED("f3txy198klrpx73ctc6lg125r"); // 	    op->drawfunc = ops[xop_ellipse];
UNSUPPORTED("6aw91xzjmqvmtdvt1di23af8y"); // 	break;
UNSUPPORTED("yic0mjrlpepyvrda3gv6t25f"); //     case 'e':
UNSUPPORTED("e9q42iiycw42fzv9sxtvjkwh1"); // 	op->kind = xd_unfilled_ellipse;
UNSUPPORTED("atahrjx7tu9jw9spyi7jwfptn"); // 	s = parseRect(s, &op->u.ellipse);
UNSUPPORTED("dgnldyd2pb5vmg1g32106eh4l"); // 	if(!s){*error=1;return 0;};
UNSUPPORTED("68fwibhr1nqhg298v23jvoosh"); // 	if (ops)
UNSUPPORTED("f3txy198klrpx73ctc6lg125r"); // 	    op->drawfunc = ops[xop_ellipse];
UNSUPPORTED("6aw91xzjmqvmtdvt1di23af8y"); // 	break;
UNSUPPORTED("61e1o543la5tkcimttlw8ywf2"); //     case 'P':
UNSUPPORTED("altg4bjpb2pgvh11pwr1uwmr"); // 	op->kind = xd_filled_polygon;
UNSUPPORTED("7f8c0fkrsn9rb7rkurug6gf2z"); // 	s = parsePolyline(s, &op->u.polygon);
UNSUPPORTED("dgnldyd2pb5vmg1g32106eh4l"); // 	if(!s){*error=1;return 0;};
UNSUPPORTED("68fwibhr1nqhg298v23jvoosh"); // 	if (ops)
UNSUPPORTED("3jz0du43oha0dv721jmcm9iaq"); // 	    op->drawfunc = ops[xop_polygon];
UNSUPPORTED("6aw91xzjmqvmtdvt1di23af8y"); // 	break;
UNSUPPORTED("8nhycr2g1b6hvihfh3uztphax"); //     case 'p':
UNSUPPORTED("22idwk0wq7hih6wgv79c42gxj"); // 	op->kind = xd_unfilled_polygon;
UNSUPPORTED("7f8c0fkrsn9rb7rkurug6gf2z"); // 	s = parsePolyline(s, &op->u.polygon);
UNSUPPORTED("dgnldyd2pb5vmg1g32106eh4l"); // 	if(!s){*error=1;return 0;};
UNSUPPORTED("68fwibhr1nqhg298v23jvoosh"); // 	if (ops)
UNSUPPORTED("3jz0du43oha0dv721jmcm9iaq"); // 	    op->drawfunc = ops[xop_polygon];
UNSUPPORTED("6aw91xzjmqvmtdvt1di23af8y"); // 	break;
UNSUPPORTED("volih9dzznkmqxv0f6d40beq"); //     case 'b':
UNSUPPORTED("dghgxw5iaz1lqcpx6khg9fwt5"); // 	op->kind = xd_filled_bezier;
UNSUPPORTED("de36xrnd6y03zp55847a4giju"); // 	s = parsePolyline(s, &op->u.bezier);
UNSUPPORTED("dgnldyd2pb5vmg1g32106eh4l"); // 	if(!s){*error=1;return 0;};
UNSUPPORTED("68fwibhr1nqhg298v23jvoosh"); // 	if (ops)
UNSUPPORTED("1pmk6j85vg5odzetxiw461gw"); // 	    op->drawfunc = ops[xop_bezier];
UNSUPPORTED("6aw91xzjmqvmtdvt1di23af8y"); // 	break;
UNSUPPORTED("9nbe45sgjnq9pebqfddm57j34"); //     case 'B':
UNSUPPORTED("7yupkrdo7hn0u4tlxtznjbh03"); // 	op->kind = xd_unfilled_bezier;
UNSUPPORTED("de36xrnd6y03zp55847a4giju"); // 	s = parsePolyline(s, &op->u.bezier);
UNSUPPORTED("dgnldyd2pb5vmg1g32106eh4l"); // 	if(!s){*error=1;return 0;};
UNSUPPORTED("68fwibhr1nqhg298v23jvoosh"); // 	if (ops)
UNSUPPORTED("1pmk6j85vg5odzetxiw461gw"); // 	    op->drawfunc = ops[xop_bezier];
UNSUPPORTED("6aw91xzjmqvmtdvt1di23af8y"); // 	break;
UNSUPPORTED("rzit3m9f6iuku0clu8nqw5ih"); //     case 'c':
UNSUPPORTED("5mc0sy0u0wyb9hblr1cbvyp1h"); // 	s = parseString(s, &cs);
UNSUPPORTED("dgnldyd2pb5vmg1g32106eh4l"); // 	if(!s){*error=1;return 0;};
UNSUPPORTED("ddh0k5n3l9rto4gcuggkkja61"); // 	cs = parseXDotColor (cs, &clr);
UNSUPPORTED("ix8ie1z4452fb8rtdsib6ufu"); // 	if(!cs){*error=1;return 0;};
UNSUPPORTED("dqfrfyicjfcf6t6vr1g1atdpm"); // 	if (clr.type == xd_none) {
UNSUPPORTED("4jv4w1znjmyk4pv8e2882cz0"); // 	    op->kind = xd_pen_color;
UNSUPPORTED("9ohuadhd8hihpbkr4vazi9dho"); // 	    op->u.color = clr.u.clr;
UNSUPPORTED("8npw75ua3iw8jlh0dm8nidoy9"); // 	    if (ops)
UNSUPPORTED("1r3cxmw9aafv54g3bc6bxkrey"); // 		op->drawfunc = ops[xop_pen_color];
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("8k75h069sv2k9b6tgz77dscwd"); // 	else {
UNSUPPORTED("eamachthb8mef0nhonusnktu9"); // 	    op->kind = xd_grad_pen_color;
UNSUPPORTED("1t72m9kz99a1xacrrrcfo1472"); // 	    op->u.grad_color = clr;
UNSUPPORTED("8npw75ua3iw8jlh0dm8nidoy9"); // 	    if (ops)
UNSUPPORTED("2uoht3mjgjvn6q5p8grt2eqt"); // 		op->drawfunc = ops[xop_grad_color];
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("6aw91xzjmqvmtdvt1di23af8y"); // 	break;
UNSUPPORTED("1b9yj8jr8j9uh2dubizj3pb1w"); //     case 'C':
UNSUPPORTED("5mc0sy0u0wyb9hblr1cbvyp1h"); // 	s = parseString(s, &cs);
UNSUPPORTED("dgnldyd2pb5vmg1g32106eh4l"); // 	if(!s){*error=1;return 0;};
UNSUPPORTED("ddh0k5n3l9rto4gcuggkkja61"); // 	cs = parseXDotColor (cs, &clr);
UNSUPPORTED("ix8ie1z4452fb8rtdsib6ufu"); // 	if(!cs){*error=1;return 0;};
UNSUPPORTED("dqfrfyicjfcf6t6vr1g1atdpm"); // 	if (clr.type == xd_none) {
UNSUPPORTED("50qk3ylyw205nzn8fqok09cs5"); // 	    op->kind = xd_fill_color;
UNSUPPORTED("9ohuadhd8hihpbkr4vazi9dho"); // 	    op->u.color = clr.u.clr;
UNSUPPORTED("8npw75ua3iw8jlh0dm8nidoy9"); // 	    if (ops)
UNSUPPORTED("4zwt9xuucvf9zu22n83cq16mv"); // 		op->drawfunc = ops[xop_fill_color];
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("8k75h069sv2k9b6tgz77dscwd"); // 	else {
UNSUPPORTED("crmgghygtre74dw1tfur2pnt4"); // 	    op->kind = xd_grad_fill_color;
UNSUPPORTED("1t72m9kz99a1xacrrrcfo1472"); // 	    op->u.grad_color = clr;
UNSUPPORTED("8npw75ua3iw8jlh0dm8nidoy9"); // 	    if (ops)
UNSUPPORTED("2uoht3mjgjvn6q5p8grt2eqt"); // 		op->drawfunc = ops[xop_grad_color];
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("6aw91xzjmqvmtdvt1di23af8y"); // 	break;
UNSUPPORTED("92wd4259ggzetbnn56kh75cap"); //     case 'L':
UNSUPPORTED("adyh3da2x6ec56lvn4k7ke2if"); // 	op->kind = xd_polyline;
UNSUPPORTED("cxbj7pu045lx6cudbid7zp9qe"); // 	s = parsePolyline(s, &op->u.polyline);
UNSUPPORTED("dgnldyd2pb5vmg1g32106eh4l"); // 	if(!s){*error=1;return 0;};
UNSUPPORTED("68fwibhr1nqhg298v23jvoosh"); // 	if (ops)
UNSUPPORTED("9vy8b22mc6n5byr6erdtsw40t"); // 	    op->drawfunc = ops[xop_polyline];
UNSUPPORTED("6aw91xzjmqvmtdvt1di23af8y"); // 	break;
UNSUPPORTED("2s6hwvc7utwke7l45suhmumhk"); //     case 'T':
UNSUPPORTED("7jtgg1g4yy9elu7uj6ort6gx5"); // 	op->kind = xd_text;
UNSUPPORTED("ddz6ejg5d3ser8syqgqgor5or"); // 	s = parseReal(s, &op->u.text.x);
UNSUPPORTED("dgnldyd2pb5vmg1g32106eh4l"); // 	if(!s){*error=1;return 0;};
UNSUPPORTED("dtm3mydushiqsv1486knmsmk4"); // 	s = parseReal(s, &op->u.text.y);
UNSUPPORTED("dgnldyd2pb5vmg1g32106eh4l"); // 	if(!s){*error=1;return 0;};
UNSUPPORTED("bprdmfsh0vryv3p7ij0qf40ew"); // 	s = parseAlign(s, &op->u.text.align);
UNSUPPORTED("dgnldyd2pb5vmg1g32106eh4l"); // 	if(!s){*error=1;return 0;};
UNSUPPORTED("ah35sfvnqnxyb683wo4p2rk35"); // 	s = parseReal(s, &op->u.text.width);
UNSUPPORTED("dgnldyd2pb5vmg1g32106eh4l"); // 	if(!s){*error=1;return 0;};
UNSUPPORTED("246imx3icdy7iqe7js9nneni"); // 	s = parseString(s, &op->u.text.text);
UNSUPPORTED("dgnldyd2pb5vmg1g32106eh4l"); // 	if(!s){*error=1;return 0;};
UNSUPPORTED("68fwibhr1nqhg298v23jvoosh"); // 	if (ops)
UNSUPPORTED("5knq711m08yljnaejvjqfcg3j"); // 	    op->drawfunc = ops[xop_text];
UNSUPPORTED("6aw91xzjmqvmtdvt1di23af8y"); // 	break;
UNSUPPORTED("6q33jx6joepwjvm90xt4bf0vh"); //     case 'F':
UNSUPPORTED("9kl1bheiih6co789yxhdhiujb"); // 	op->kind = xd_font;
UNSUPPORTED("t4du5s5scamal3d24vj1iqbh"); // 	s = parseReal(s, &op->u.font.size);
UNSUPPORTED("dgnldyd2pb5vmg1g32106eh4l"); // 	if(!s){*error=1;return 0;};
UNSUPPORTED("2fc9sdef2b51gow7bu6sjkkg4"); // 	s = parseString(s, &op->u.font.name);
UNSUPPORTED("dgnldyd2pb5vmg1g32106eh4l"); // 	if(!s){*error=1;return 0;};
UNSUPPORTED("68fwibhr1nqhg298v23jvoosh"); // 	if (ops)
UNSUPPORTED("35fyib2apc4v7sbldsq0go062"); // 	    op->drawfunc = ops[xop_font];
UNSUPPORTED("6aw91xzjmqvmtdvt1di23af8y"); // 	break;
UNSUPPORTED("a64n1v2knehfy9fh7m8eu7hsw"); //     case 'S':
UNSUPPORTED("8zk5sf79y3v9t2m0y1ymj3iey"); // 	op->kind = xd_style;
UNSUPPORTED("7cq7xz66wdm8q5mb092031z4u"); // 	s = parseString(s, &op->u.style);
UNSUPPORTED("dgnldyd2pb5vmg1g32106eh4l"); // 	if(!s){*error=1;return 0;};
UNSUPPORTED("68fwibhr1nqhg298v23jvoosh"); // 	if (ops)
UNSUPPORTED("7squrebrjdtz8u4fte81buvyz"); // 	    op->drawfunc = ops[xop_style];
UNSUPPORTED("6aw91xzjmqvmtdvt1di23af8y"); // 	break;
UNSUPPORTED("29wq7b3fga4nijwl3yta17mv7"); //     case 'I':
UNSUPPORTED("eg0zxy0wpuyiu2oougybrgiy7"); // 	op->kind = xd_image;
UNSUPPORTED("8z1u31p0vzmblgt8ohzyr1ndx"); // 	s = parseRect(s, &op->u.image.pos);
UNSUPPORTED("dgnldyd2pb5vmg1g32106eh4l"); // 	if(!s){*error=1;return 0;};
UNSUPPORTED("bumle8azn9e1h3v7bopmk5n54"); // 	s = parseString(s, &op->u.image.name);
UNSUPPORTED("dgnldyd2pb5vmg1g32106eh4l"); // 	if(!s){*error=1;return 0;};
UNSUPPORTED("68fwibhr1nqhg298v23jvoosh"); // 	if (ops)
UNSUPPORTED("6m0v33rzuqjeq92tc2rckvele"); // 	    op->drawfunc = ops[xop_image];
UNSUPPORTED("6aw91xzjmqvmtdvt1di23af8y"); // 	break;
UNSUPPORTED("eat2i5ddqniirlvawyhxdst46"); //     case 't':
UNSUPPORTED("e92hvbw42702796zjdpygw9hm"); // 	op->kind = xd_fontchar;
UNSUPPORTED("1ccegleo0pp43r2ln4pqcss9g"); // 	s = parseUInt(s, &op->u.fontchar);
UNSUPPORTED("dgnldyd2pb5vmg1g32106eh4l"); // 	if(!s){*error=1;return 0;};
UNSUPPORTED("68fwibhr1nqhg298v23jvoosh"); // 	if (ops)
UNSUPPORTED("9d0mk8yn2iofol0nxgvjskxm2"); // 	    op->drawfunc = ops[xop_fontchar];
UNSUPPORTED("6aw91xzjmqvmtdvt1di23af8y"); // 	break;
UNSUPPORTED("1luutzji7osg8esr8b5j2cmsr"); //     case '\0':
UNSUPPORTED("cx0xi1vqbff1wttv329u71fsf"); // 	s = 0;
UNSUPPORTED("6aw91xzjmqvmtdvt1di23af8y"); // 	break;
UNSUPPORTED("8l3rwj6ctswoa4gvh2j4poq70"); //     default:
UNSUPPORTED("12iorka61r0svbo67ymiyk2xm"); // 	*error = 1;
UNSUPPORTED("cx0xi1vqbff1wttv329u71fsf"); // 	s = 0;
UNSUPPORTED("6aw91xzjmqvmtdvt1di23af8y"); // 	break;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("3y6wj3ntgmr1qkdpm7wp1dsch"); //     return s;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 5wc1slq9x7lxo1mohysgr0u9v
// xdot *parseXDotFOn (char *s, drawfunc_t fns[], int sz, xdot* x) 
public static Object parseXDotFOn(Object... arg) {
UNSUPPORTED("del1weqvytt23ivpfz8ioo7lq"); // xdot *parseXDotFOn (char *s, drawfunc_t fns[], int sz, xdot* x)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("6qc99eq4i3e3ogqtvwjenpax6"); //     xdot_op op;
UNSUPPORTED("de9l6pn4qzf2wjdqbawozkf7e"); //     char *ops;
UNSUPPORTED("1fre7x15xdukb9hyi6d6711i5"); //     int oldsz, bufsz;
UNSUPPORTED("cknrkc54njhr0aclf9l9gw0bw"); //     int error;
UNSUPPORTED("ecv0zeja1tcgfn8qgbqizf04m"); //     int initcnt;
UNSUPPORTED("5io7qudgtw3wrjkc5ndpni08d"); //     if (!s)
UNSUPPORTED("7ngxpkghjermbwbho2j4f5qvi"); // 	return x;
UNSUPPORTED("ke1x8t1fmvv316rneoda0ss2"); //     if (!x) {
UNSUPPORTED("1bt9ibfd7selwlyjzzj8zugyw"); // 	x = (xdot*)calloc(1, sizeof(xdot));
UNSUPPORTED("2e1w10luru5il8ocut51grwgv"); // 	if (sz <= sizeof(xdot_op))
UNSUPPORTED("7apwr620hx5egvyfz58suchvi"); // 	    sz = sizeof(xdot_op);
UNSUPPORTED("ek6enscr1j99c8kzgpj6u2bxa"); // 	/* cnt, freefunc, ops, flags zeroed by NEW */
UNSUPPORTED("cajvvx0dkfkoti3nbxwiluats"); // 	x->sz = sz;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("e077xzd8dapealp6nhtlqdpl4"); //     initcnt = x->cnt;
UNSUPPORTED("7mal8ay7kpitrb9g5mthqfbjb"); //     sz = x->sz;
UNSUPPORTED("8i2hi7mj1cgsl90sxhbpqranx"); //     if (initcnt == 0) {
UNSUPPORTED("bkcefor4uv5jyrv0x0yfbxyna"); // 	bufsz = 100;
UNSUPPORTED("201agqdoyv7sx9c17j6fuw2sg"); // 	ops = (char *) calloc(100, sz);
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("1nyzbeonram6636b1w955bypn"); //     else {
UNSUPPORTED("cwo1tpf247100oddqjti32zmh"); // 	ops = (char*)(x->ops);
UNSUPPORTED("7nja6mm5tt34cve70y0571hub"); // 	bufsz = initcnt + 100;
UNSUPPORTED("cnb950g9dc109a90346xph8iy"); // 	ops = (char *) realloc(ops, bufsz * sz);
UNSUPPORTED("b43qdbc13emvwq99ngcburls5"); // 	memset(ops + (initcnt*sz), '\0', (bufsz - initcnt)*sz);
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("73x1ejpfjxn2mhkzjznytsxvn"); //     while ((s = parseOp(&op, s, fns, &error))) {
UNSUPPORTED("5ol0b0rd00pjdvj32o6c1il1x"); // 	if (x->cnt == bufsz) {
UNSUPPORTED("1w2w52ovyn8un9gr3j9lxj202"); // 	    oldsz = bufsz;
UNSUPPORTED("3psgdtqxnrljp3xnkep0f4qm4"); // 	    bufsz *= 2;
UNSUPPORTED("2ykj4yj4vfhwxjomqhhrg8uzr"); // 	    ops = (char *) realloc(ops, bufsz * sz);
UNSUPPORTED("6hvg5i6znthuu7cdcsjq5mnz0"); // 	    memset(ops + (oldsz*sz), '\0', (bufsz - oldsz)*sz);
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("du21yaxgccjzlo45llct4tqak"); // 	*(xdot_op *) (ops + (x->cnt * sz)) = op;
UNSUPPORTED("ai5pt7k72sx5jp61rk4fvdhxp"); // 	x->cnt++;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("9ob7nuswfwxxvd5brie5gso4d"); //     if (error)
UNSUPPORTED("dyyumzikvlv9fg24z6x09p5lu"); // 	x->flags |= 1;
UNSUPPORTED("1n5koxwuji1d5l2zzs1e4h1qi"); //     if (x->cnt) {
UNSUPPORTED("4ssdlr3gorr9y6t08ll8z4znz"); // 	x->ops = (xdot_op *) realloc(ops, x->cnt * sz);
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("1nyzbeonram6636b1w955bypn"); //     else {
UNSUPPORTED("cycxnymrxk0vovp1xbx5ma9md"); // 	free (ops);
UNSUPPORTED("c0mmv67btk6s9utzudcjef1b"); // 	free (x);
UNSUPPORTED("ajmuq0bs0l1j2jqys1lqyatkp"); // 	x = NULL;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("4jnfz0yeu1qihsx3sbr7l2fsh"); //     return x;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 4460p54l4myil3tss1cr4fryd
// xdot *parseXDotF(char *s, drawfunc_t fns[], int sz) 
public static Object parseXDotF(Object... arg) {
UNSUPPORTED("969km4cd3fzkxaw7yidsr8p7p"); // xdot *parseXDotF(char *s, drawfunc_t fns[], int sz)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("dlkx6l0c5f3r3r98huhdg2ooa"); //     return parseXDotFOn (s, fns, sz, NULL);
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 1l1kp80xwz00t0yjxs3q5rmk3
// xdot *parseXDot(char *s) 
public static Object parseXDot(Object... arg) {
UNSUPPORTED("8q2xd8yodlhxzirc9af1nt5x5"); // xdot *parseXDot(char *s)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("arcemicyd1w4yf877xwdo6rx7"); //     return parseXDotF(s, 0, 0);
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 2f1smkubo9aphaw4mu9x6fqep
// static void trim (char* buf) 
public static Object trim(Object... arg) {
UNSUPPORTED("16qqcors2mtgshjsd8bo4p83w"); // static void trim (char* buf)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("7puz5on3kut1d51f3su87yzvy"); //     char* dotp;
UNSUPPORTED("a4px33i4moqe8ybwatz0g8k6"); //     char* p;
UNSUPPORTED("25j4nlrn8fb330rtpgs3znj2e"); //     if ((dotp = strchr (buf,'.'))) {
UNSUPPORTED("4ahv95yhrsexjeiyqeqh89ui5"); //         p = dotp+1;
UNSUPPORTED("8owim6df9ae01xere8qw3zijk"); //         while (*p) p++;  // find end of string
UNSUPPORTED("6y68c149kyxt4ja9q3jh7bc19"); //         p--;
UNSUPPORTED("e2htnsr0x54tbh21x4z55a8fg"); //         while (*p == '0') *p-- = '\0';
UNSUPPORTED("azdcgp3weiwu7xxfo6mjw11v3"); //         if (*p == '.')        // If all decimals were zeros, remove ".".
UNSUPPORTED("6568pmkatbjs0ljsylsyvwepv"); //             *p = '\0';
UNSUPPORTED("35nw1pbiz2p3s6qwlam5eoo3m"); //         else
UNSUPPORTED("a0t85lvr3qdf70ajg30c8eadd"); //             p++;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 dkdckkwtr1hxr1itqzjgv1uwc
// static void printRect(xdot_rect * r, pf print, void *info) 
public static Object printRect(Object... arg) {
UNSUPPORTED("1j4yxl2tdu1mgcs8f18wo7y1p"); // static void printRect(xdot_rect * r, pf print, void *info)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("ao5jsn9dl1jrlcy9pc1frc1o0"); //     char buf[128];
UNSUPPORTED("6ded6umxsn4kpbdobx5psese6"); //     sprintf(buf, " %.02f", r->x);
UNSUPPORTED("c6edvxq3lg824jb9zcjytatwi"); //     trim(buf);
UNSUPPORTED("1pvfmyr62uut2ima4wfa83yq7"); //     print(buf, info);
UNSUPPORTED("1dq8e468ztu7szq5wr6jwqoyk"); //     sprintf(buf, " %.02f", r->y);
UNSUPPORTED("c6edvxq3lg824jb9zcjytatwi"); //     trim(buf);
UNSUPPORTED("1pvfmyr62uut2ima4wfa83yq7"); //     print(buf, info);
UNSUPPORTED("4hbjzmluo9cbzej5u95q8a7z4"); //     sprintf(buf, " %.02f", r->w);
UNSUPPORTED("c6edvxq3lg824jb9zcjytatwi"); //     trim(buf);
UNSUPPORTED("1pvfmyr62uut2ima4wfa83yq7"); //     print(buf, info);
UNSUPPORTED("aneklasez7u2mjv2budi0ucgq"); //     sprintf(buf, " %.02f", r->h);
UNSUPPORTED("c6edvxq3lg824jb9zcjytatwi"); //     trim(buf);
UNSUPPORTED("1pvfmyr62uut2ima4wfa83yq7"); //     print(buf, info);
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 42jio1rnt74r1eotoz52e2103
// static void printPolyline(xdot_polyline * p, pf print, void *info) 
public static Object printPolyline(Object... arg) {
UNSUPPORTED("5duf17af1csfzpnc3dyas5huc"); // static void printPolyline(xdot_polyline * p, pf print, void *info)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("b17di9c7wgtqm51bvsyxz6e2f"); //     int i;
UNSUPPORTED("bx1dr4weajm160sejmy560gvb"); //     char buf[512];
UNSUPPORTED("2wggiazumqcdxtujbvty42yuv"); //     sprintf(buf, " %d", p->cnt);
UNSUPPORTED("1pvfmyr62uut2ima4wfa83yq7"); //     print(buf, info);
UNSUPPORTED("1zh4rtgdnj1q51o48247n8113"); //     for (i = 0; i < p->cnt; i++) {
UNSUPPORTED("6npaqu2tg8ldsxi0i5k0n0z3j"); // 	sprintf(buf, " %.02f", p->pts[i].x);
UNSUPPORTED("3b5kljt1py58tdgjxk9e2dsww"); // 	trim(buf);
UNSUPPORTED("7oss8g82at7padefgh6z1oko6"); // 	print(buf, info);
UNSUPPORTED("3ci7wlatgm8j39td4y1jtvcyq"); // 	sprintf(buf, " %.02f", p->pts[i].y);
UNSUPPORTED("3b5kljt1py58tdgjxk9e2dsww"); // 	trim(buf);
UNSUPPORTED("7oss8g82at7padefgh6z1oko6"); // 	print(buf, info);
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 4gdy7r7tpkmrd0prm55gzhki5
// static void printString(char *p, pf print, void *info) 
public static Object printString(Object... arg) {
UNSUPPORTED("9f4f8ckw2jx1xeiecbc5j14jx"); // static void printString(char *p, pf print, void *info)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("4f58isghm038gd5wa6xw6tk4k"); //     char buf[30];
UNSUPPORTED("axfy0wcb6535wfyimof2qaa44"); //     sprintf(buf, " %d -", (int) strlen(p));
UNSUPPORTED("1pvfmyr62uut2ima4wfa83yq7"); //     print(buf, info);
UNSUPPORTED("bp53hk5r9lhkewy59gijr0m4j"); //     print(p, info);
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 8sfpijalp2tilivgvo7ipgyo2
// static void printInt(int i, pf print, void *info) 
public static Object printInt(Object... arg) {
UNSUPPORTED("9vsvsk7dh3k27thd38londqpp"); // static void printInt(int i, pf print, void *info)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("4f58isghm038gd5wa6xw6tk4k"); //     char buf[30];
UNSUPPORTED("1ikq43mlxdu4l67uy39bejql4"); //     sprintf(buf, " %d", i);
UNSUPPORTED("1pvfmyr62uut2ima4wfa83yq7"); //     print(buf, info);
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 bb85wa9ydpjx8azkt6j8fkk9z
// static void printFloat(float f, pf print, void *info, int space) 
public static Object printFloat(Object... arg) {
UNSUPPORTED("d94ew8o9g8u5ic657j9wqhrwz"); // static void printFloat(float f, pf print, void *info, int space)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("ao5jsn9dl1jrlcy9pc1frc1o0"); //     char buf[128];
UNSUPPORTED("4syg3ja72oets2mdjgyjmzh5w"); //     if (space)
UNSUPPORTED("8dxllh2z42b75e6r0pxkdg2j1"); // 	sprintf(buf, " %.02f", f);
UNSUPPORTED("div10atae09n36x269sl208r1"); //     else
UNSUPPORTED("97zm9gyi28c0wrfbvlls52v1h"); // 	sprintf(buf, "%.02f", f);
UNSUPPORTED("481rvn40hp1stspd04wqvyej6"); //     trim (buf);
UNSUPPORTED("1pvfmyr62uut2ima4wfa83yq7"); //     print(buf, info);
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 cewa9h3fzdsvn1pbwq6owgc41
// static void printAlign(xdot_align a, pf print, void *info) 
public static Object printAlign(Object... arg) {
UNSUPPORTED("8miq75j7m3nbilqa8rw1slpm9"); // static void printAlign(xdot_align a, pf print, void *info)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("b5n0x9uualh7k0dnk80sdcgqs"); //     switch (a) {
UNSUPPORTED("css95m4wfpdkor1v8y1jvb0yf"); //     case xd_left:
UNSUPPORTED("5j6u0d7ydfnkl37wkp62dee5z"); // 	print(" -1", info);
UNSUPPORTED("6aw91xzjmqvmtdvt1di23af8y"); // 	break;
UNSUPPORTED("3qghu38yz817sx6eaztlm3p6l"); //     case xd_right:
UNSUPPORTED("bm2tfxfdkjx58e5wkpecpko4u"); // 	print(" 1", info);
UNSUPPORTED("6aw91xzjmqvmtdvt1di23af8y"); // 	break;
UNSUPPORTED("1hh2q36cwlihkbrqqwveukrto"); //     case xd_center:
UNSUPPORTED("8qpabgae6mxxb1ly8yuw0h9pa"); // 	print(" 0", info);
UNSUPPORTED("6aw91xzjmqvmtdvt1di23af8y"); // 	break;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 8sdpv3zwgeio07qexxyifrki9
// static void gradprint (char* s, void* v) 
public static Object gradprint(Object... arg) {
UNSUPPORTED("e2z2o5ybnr5tgpkt8ty7hwan1"); // static void
UNSUPPORTED("e9q23vdw8dsq7sa76x8tatioi"); // gradprint (char* s, void* v)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("byn5a1ck7ae0nmlhvjrcigb3i"); //     agxbput(s, (agxbuf*)v);
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 d8h0f1ny0lfqgdlltgznth70f
// static void toGradString (agxbuf* xb, xdot_color* cp) 
public static Object toGradString(Object... arg) {
UNSUPPORTED("e2z2o5ybnr5tgpkt8ty7hwan1"); // static void
UNSUPPORTED("e098a5mtgj1bjdwal9mpcyohu"); // toGradString (agxbuf* xb, xdot_color* cp)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("bg1wmmucmjnf92wix4t7nfnaa"); //     int i, n_stops;
UNSUPPORTED("5c0mu092bwx9r1ah38xzg3b4c"); //     xdot_color_stop* stops;
UNSUPPORTED("19ar0bs2h13txvy4uwtw868m"); //     if (cp->type == xd_linear) {
UNSUPPORTED("a5e0eku2d7lu5e1urereer9qd"); // 	((((xb)->ptr >= (xb)->eptr) ? agxbmore(xb,1) : 0), (int)(*(xb)->ptr++ = ((unsigned char)'[')));
UNSUPPORTED("26is4zw7egae66druypu7x87g"); // 	printFloat (cp->u.ling.x0, gradprint, xb, 0);
UNSUPPORTED("7enppj732f9vvrl746z88q49u"); // 	printFloat (cp->u.ling.y0, gradprint, xb, 1);
UNSUPPORTED("3gu8dszmue79k293so5y2wfvm"); // 	printFloat (cp->u.ling.x1, gradprint, xb, 1);
UNSUPPORTED("cal670m45zbhvvhzumai92o9y"); // 	printFloat (cp->u.ling.y1, gradprint, xb, 1);
UNSUPPORTED("es7ru0t95ecdn3lk7jb64x0h4"); // 	n_stops = cp->u.ling.n_stops;
UNSUPPORTED("24qiepo9vtk8pnivng82po3mt"); // 	stops = cp->u.ling.stops;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("1nyzbeonram6636b1w955bypn"); //     else {
UNSUPPORTED("4onc3nmgcaapl00s6m043sg26"); // 	((((xb)->ptr >= (xb)->eptr) ? agxbmore(xb,1) : 0), (int)(*(xb)->ptr++ = ((unsigned char)'(')));
UNSUPPORTED("aagp8bu8z9h8c1osmyvdw10fi"); // 	printFloat (cp->u.ring.x0, gradprint, xb, 0);
UNSUPPORTED("5aksyngm1c2y5a0lcsafnlkx3"); // 	printFloat (cp->u.ring.y0, gradprint, xb, 1);
UNSUPPORTED("pf3sl7ifvvw4cxut4qrg5pyy"); // 	printFloat (cp->u.ring.r0, gradprint, xb, 1);
UNSUPPORTED("4h1gr2eixkyvubhblkd58jcbi"); // 	printFloat (cp->u.ring.x1, gradprint, xb, 1);
UNSUPPORTED("6e9hjapf13xkygv9wqzgrd6b3"); // 	printFloat (cp->u.ring.y1, gradprint, xb, 1);
UNSUPPORTED("a5durn757p58xt7ajmie3ne0r"); // 	printFloat (cp->u.ring.r1, gradprint, xb, 1);
UNSUPPORTED("9vzzyqc3eqpw3j9gk4twef732"); // 	n_stops = cp->u.ring.n_stops;
UNSUPPORTED("1ifkpfecqerrx44isr3gttz6y"); // 	stops = cp->u.ring.stops;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("9bbs1gkxn1py6cenyvu86g6io"); //     printInt (n_stops, gradprint, xb);
UNSUPPORTED("4c8mz82f1vdtbgs20z3sj5piu"); //     for (i = 0; i < n_stops; i++) {
UNSUPPORTED("4hufkcyrhdph16ckosmd5cghd"); // 	printFloat (stops[i].frac, gradprint, xb, 1);
UNSUPPORTED("es15carbpwf4hdyr222w7gct0"); // 	printString (stops[i].color, gradprint, xb);
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("a0vxpwbxeeom01urwsawtv3c4"); //     if (cp->type == xd_linear)
UNSUPPORTED("9pej3878vsbmnvzgtbqgdb9a5"); // 	((((xb)->ptr >= (xb)->eptr) ? agxbmore(xb,1) : 0), (int)(*(xb)->ptr++ = ((unsigned char)']')));
UNSUPPORTED("div10atae09n36x269sl208r1"); //     else
UNSUPPORTED("yifq4dmob9tld1rc5va5zha8"); // 	((((xb)->ptr >= (xb)->eptr) ? agxbmore(xb,1) : 0), (int)(*(xb)->ptr++ = ((unsigned char)')')));
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 2ifo26ctb4oo4z48u3q84ejrn
// static void printXDot_Op(xdot_op * op, pf print, void *info, int more) 
public static Object printXDot_Op(Object... arg) {
UNSUPPORTED("1793b7u18bt999rej4hfqd18z"); // static void printXDot_Op(xdot_op * op, pf print, void *info, int more)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("9gou5otj6s39l2cbyc8i5i5lq"); //     agxbuf xb;
UNSUPPORTED("esg3s800dx899v69pkng2kavv"); //     unsigned char buf[BUFSIZ];
UNSUPPORTED("cx1hx0xl1yzchdfsninsbfsek"); //     agxbinit (&xb, BUFSIZ, buf);
UNSUPPORTED("egsu3y4d03wndsn6zaevl28hr"); //     switch (op->kind) {
UNSUPPORTED("92w4nbepujb6qq0yhaml31tcv"); //     case xd_filled_ellipse:
UNSUPPORTED("avfl9oam76dloz4i7gb63380f"); // 	print("E", info);
UNSUPPORTED("18zcxe29q21cnf6ocj8rtkhsv"); // 	printRect(&op->u.ellipse, print, info);
UNSUPPORTED("6aw91xzjmqvmtdvt1di23af8y"); // 	break;
UNSUPPORTED("7zgc3n99mi30jda6c8ii6z9ij"); //     case xd_unfilled_ellipse:
UNSUPPORTED("83413w1sz69a4c9t1pbqq1y"); // 	print("e", info);
UNSUPPORTED("18zcxe29q21cnf6ocj8rtkhsv"); // 	printRect(&op->u.ellipse, print, info);
UNSUPPORTED("6aw91xzjmqvmtdvt1di23af8y"); // 	break;
UNSUPPORTED("a02o53vx69zifve1qybxt6bwg"); //     case xd_filled_polygon:
UNSUPPORTED("7u9vnqf5qa98cod2pe107pm8c"); // 	print("P", info);
UNSUPPORTED("awy43l3osrby3v6u35pv3pgcn"); // 	printPolyline(&op->u.polygon, print, info);
UNSUPPORTED("6aw91xzjmqvmtdvt1di23af8y"); // 	break;
UNSUPPORTED("1amgc8nhs2zl8rzvqurr02syl"); //     case xd_unfilled_polygon:
UNSUPPORTED("eqr0mrqf32c8edkz2vxxufss3"); // 	print("p", info);
UNSUPPORTED("awy43l3osrby3v6u35pv3pgcn"); // 	printPolyline(&op->u.polygon, print, info);
UNSUPPORTED("6aw91xzjmqvmtdvt1di23af8y"); // 	break;
UNSUPPORTED("i75482wp0xfww8yo6wanbdpe"); //     case xd_filled_bezier:
UNSUPPORTED("dps7uwiaic5ssgx5ak76dknwc"); // 	print("b", info);
UNSUPPORTED("4btk2wauocq7ho3pcmjhpaep3"); // 	printPolyline(&op->u.bezier, print, info);
UNSUPPORTED("6aw91xzjmqvmtdvt1di23af8y"); // 	break;
UNSUPPORTED("cljys033beawi3mtklyme87lc"); //     case xd_unfilled_bezier:
UNSUPPORTED("bx3k28i94anx726x3fsw3hcbn"); // 	print("B", info);
UNSUPPORTED("4btk2wauocq7ho3pcmjhpaep3"); // 	printPolyline(&op->u.bezier, print, info);
UNSUPPORTED("6aw91xzjmqvmtdvt1di23af8y"); // 	break;
UNSUPPORTED("3hbkjfy0n8isa112ucvobo206"); //     case xd_pen_color:
UNSUPPORTED("etajunwxx38izkyu1vgomrd18"); // 	print("c", info);
UNSUPPORTED("2acpcykvd9wut6d3psoj4licj"); // 	printString(op->u.color, print, info);
UNSUPPORTED("6aw91xzjmqvmtdvt1di23af8y"); // 	break;
UNSUPPORTED("7xkva0g2qpvpgvf24yglj76mo"); //     case xd_grad_pen_color:
UNSUPPORTED("etajunwxx38izkyu1vgomrd18"); // 	print("c", info);
UNSUPPORTED("dn1ii4k6v4t36te9fit9p5ow"); // 	toGradString (&xb, &op->u.grad_color);
UNSUPPORTED("50kwynjmi5e57cvsa7vzmi43r"); // 	printString((((((&xb)->ptr >= (&xb)->eptr) ? agxbmore(&xb,1) : 0), (int)(*(&xb)->ptr++ = ((unsigned char)'\0'))),(char*)((&xb)->ptr = (&xb)->buf)), print, info);
UNSUPPORTED("6aw91xzjmqvmtdvt1di23af8y"); // 	break;
UNSUPPORTED("p898bkyv60y69qbm68pjwrc"); //     case xd_fill_color:
UNSUPPORTED("42hiueari9313xfmjkmlsh4nl"); // 	print("C", info);
UNSUPPORTED("2acpcykvd9wut6d3psoj4licj"); // 	printString(op->u.color, print, info);
UNSUPPORTED("6aw91xzjmqvmtdvt1di23af8y"); // 	break;
UNSUPPORTED("iqk6nvbp31j9twtqmoldcx45"); //     case xd_grad_fill_color:
UNSUPPORTED("42hiueari9313xfmjkmlsh4nl"); // 	print("C", info);
UNSUPPORTED("dn1ii4k6v4t36te9fit9p5ow"); // 	toGradString (&xb, &op->u.grad_color);
UNSUPPORTED("50kwynjmi5e57cvsa7vzmi43r"); // 	printString((((((&xb)->ptr >= (&xb)->eptr) ? agxbmore(&xb,1) : 0), (int)(*(&xb)->ptr++ = ((unsigned char)'\0'))),(char*)((&xb)->ptr = (&xb)->buf)), print, info);
UNSUPPORTED("6aw91xzjmqvmtdvt1di23af8y"); // 	break;
UNSUPPORTED("a19xwyhpt7uym00dh6pmlvhhk"); //     case xd_polyline:
UNSUPPORTED("bnzcmoivqx29pzecf56klb5rt"); // 	print("L", info);
UNSUPPORTED("6jpvv2wygo44odwyu9lynwlla"); // 	printPolyline(&op->u.polyline, print, info);
UNSUPPORTED("6aw91xzjmqvmtdvt1di23af8y"); // 	break;
UNSUPPORTED("8iuq6qr8jjaqun0ulqu2u1x77"); //     case xd_text:
UNSUPPORTED("1covx2pxt7w158hh2znfdjibj"); // 	print("T", info);
UNSUPPORTED("du2g2u2x2kjtop4xp0foszscr"); // 	printInt(op->u.text.x, print, info);
UNSUPPORTED("e5fzs5nglnsh9m8cvrm6mhh9j"); // 	printInt(op->u.text.y, print, info);
UNSUPPORTED("12ip8eyyw9723ifasdged1v2g"); // 	printAlign(op->u.text.align, print, info);
UNSUPPORTED("dff5na2fdib9h0c3ltg1q62m1"); // 	printInt(op->u.text.width, print, info);
UNSUPPORTED("4j0eb8wt0yftyu0xsxwmht5y0"); // 	printString(op->u.text.text, print, info);
UNSUPPORTED("6aw91xzjmqvmtdvt1di23af8y"); // 	break;
UNSUPPORTED("7mkkywc80hjajd0vttqrsfcsz"); //     case xd_font:
UNSUPPORTED("5cr592wslkjhte61diwmbbiao"); // 	print("F", info);
UNSUPPORTED("2v4nq9epbpb850zg0p0ne7i9z"); // 	printFloat(op->u.font.size, print, info, 1);
UNSUPPORTED("m0mhdn5rhrw213vfetr1qvrr"); // 	printString(op->u.font.name, print, info);
UNSUPPORTED("6aw91xzjmqvmtdvt1di23af8y"); // 	break;
UNSUPPORTED("4bvixlppj4a6yebt6egxnzu9e"); //     case xd_fontchar:
UNSUPPORTED("4paiwfmol6vza6gj5t58bj1tu"); // 	print("t", info);
UNSUPPORTED("7f3e0jgah853oke7gxa99azrt"); // 	printInt(op->u.fontchar, print, info);
UNSUPPORTED("6aw91xzjmqvmtdvt1di23af8y"); // 	break;
UNSUPPORTED("9c1yqt1pf6mjkrdhf7pxoea82"); //     case xd_style:
UNSUPPORTED("5s484jqdx1mqox7ld6hgx74h3"); // 	print("S", info);
UNSUPPORTED("31kaly9swqallffqmpz4d8g8q"); // 	printString(op->u.style, print, info);
UNSUPPORTED("6aw91xzjmqvmtdvt1di23af8y"); // 	break;
UNSUPPORTED("3tev1dlfh70jm5km963v1i7rr"); //     case xd_image:
UNSUPPORTED("8u2x1dwtxbkqfhrhav6mps4zz"); // 	print("I", info);
UNSUPPORTED("cmjkhpcu8kludztp79rewpw3r"); // 	printRect(&op->u.image.pos, print, info);
UNSUPPORTED("7huh682je4jhfwyzr6jis9v3e"); // 	printString(op->u.image.name, print, info);
UNSUPPORTED("6aw91xzjmqvmtdvt1di23af8y"); // 	break;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("21eozdlhui5hv29fj41fdny45"); //     if (more)
UNSUPPORTED("3fhfv2tii5y8pg1pxuqf61414"); // 	print(" ", info);
UNSUPPORTED("9ocnzhe59r19odwgtedwnydm"); //     agxbfree (&xb);
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 22qizeibm0jpts105p4r4ssu7
// static void jsonRect(xdot_rect * r, pf print, void *info) 
public static Object jsonRect(Object... arg) {
UNSUPPORTED("atngzawms4mstydgpenmodeae"); // static void jsonRect(xdot_rect * r, pf print, void *info)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("ao5jsn9dl1jrlcy9pc1frc1o0"); //     char buf[128];
UNSUPPORTED("899gofiybbl25xf9pqimd5aww"); //     sprintf(buf, "[%.06f,%.06f,%.06f,%.06f]", r->x, r->y, r->w, r->h);
UNSUPPORTED("1pvfmyr62uut2ima4wfa83yq7"); //     print(buf, info);
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 cfkyn9xd2i5z05dsbh0ne2rjg
// static void jsonPolyline(xdot_polyline * p, pf print, void *info) 
public static Object jsonPolyline(Object... arg) {
UNSUPPORTED("egjrc5eqaywh9r95et92i5j12"); // static void jsonPolyline(xdot_polyline * p, pf print, void *info)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("b17di9c7wgtqm51bvsyxz6e2f"); //     int i;
UNSUPPORTED("ao5jsn9dl1jrlcy9pc1frc1o0"); //     char buf[128];
UNSUPPORTED("dx17wwbu2afo621ke8m0p66bx"); //     print("[", info);
UNSUPPORTED("1zh4rtgdnj1q51o48247n8113"); //     for (i = 0; i < p->cnt; i++) {
UNSUPPORTED("9kduk4sv1f8wp0siawqgz9c4o"); // 	sprintf(buf, "%.06f,%.06f", p->pts[i].x, p->pts[i].y);
UNSUPPORTED("7oss8g82at7padefgh6z1oko6"); // 	print(buf, info);
UNSUPPORTED("dmxnc6gb50dg4v9n0xtjz51hf"); // 	if (i < p->cnt-1) print(",", info);
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("4oeezkifb3drxphpg1cjeg70d"); //     print("]", info);
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 aje680ivpjh0mfzcsw9x1ht0r
// static void jsonString(char *p, pf print, void *info) 
public static Object jsonString(Object... arg) {
UNSUPPORTED("xd8vixlem3ryrydnitavheuh"); // static void jsonString(char *p, pf print, void *info)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("ccqtabt7b8gb82xm56jlgsmct"); //     unsigned char c, buf[BUFSIZ];
UNSUPPORTED("9gou5otj6s39l2cbyc8i5i5lq"); //     agxbuf xb;
UNSUPPORTED("3jwm77zyv02ukrvjv9jppejf7"); //     agxbinit(&xb, BUFSIZ, buf);
UNSUPPORTED("b3e82u1ow4q1lk03crhf13ayh"); //     ((((&xb)->ptr >= (&xb)->eptr) ? agxbmore(&xb,1) : 0), (int)(*(&xb)->ptr++ = ((unsigned char)'"')));
UNSUPPORTED("5idssb31ihz3min8hoscutczg"); //     while ((c = *p++)) {
UNSUPPORTED("cm03my2812x36d3fncswnob84"); // 	if (c == '"') agxbput("\\\"", &xb);
UNSUPPORTED("94otk4xgi87nfbyoszjr7j55w"); // 	else if (c == '\\') agxbput("\\\\", &xb);
UNSUPPORTED("2jd1vf95nxgl1jccn2hj57ltn"); // 	/* else if (c > 127) handle UTF-8 */
UNSUPPORTED("2cra9r66covnc0bjlevlzi5jj"); // 	else ((((&xb)->ptr >= (&xb)->eptr) ? agxbmore(&xb,1) : 0), (int)(*(&xb)->ptr++ = ((unsigned char)c)));
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("b3e82u1ow4q1lk03crhf13ayh"); //     ((((&xb)->ptr >= (&xb)->eptr) ? agxbmore(&xb,1) : 0), (int)(*(&xb)->ptr++ = ((unsigned char)'"')));
UNSUPPORTED("cc3sjgjwfnwoa96mfkgf04x8y"); //     print((((((&xb)->ptr >= (&xb)->eptr) ? agxbmore(&xb,1) : 0), (int)(*(&xb)->ptr++ = ((unsigned char)'\0'))),(char*)((&xb)->ptr = (&xb)->buf)), info);
UNSUPPORTED("1at5m9ctjn3ukv5gqtfswik02"); //     agxbfree(&xb);
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 en6kdtyyuve3yy1mk979yf17k
// static void jsonXDot_Op(xdot_op * op, pf print, void *info, int more) 
public static Object jsonXDot_Op(Object... arg) {
UNSUPPORTED("9emtpnf2em1erh1ibaoqmcvew"); // static void jsonXDot_Op(xdot_op * op, pf print, void *info, int more)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("9gou5otj6s39l2cbyc8i5i5lq"); //     agxbuf xb;
UNSUPPORTED("esg3s800dx899v69pkng2kavv"); //     unsigned char buf[BUFSIZ];
UNSUPPORTED("cx1hx0xl1yzchdfsninsbfsek"); //     agxbinit (&xb, BUFSIZ, buf);
UNSUPPORTED("egsu3y4d03wndsn6zaevl28hr"); //     switch (op->kind) {
UNSUPPORTED("92w4nbepujb6qq0yhaml31tcv"); //     case xd_filled_ellipse:
UNSUPPORTED("7shzdk63y5m3x09b9ff524dm6"); // 	print("{E : ", info);
UNSUPPORTED("cfit896buh0gkw7q1d7stizok"); // 	jsonRect(&op->u.ellipse, print, info);
UNSUPPORTED("6aw91xzjmqvmtdvt1di23af8y"); // 	break;
UNSUPPORTED("7zgc3n99mi30jda6c8ii6z9ij"); //     case xd_unfilled_ellipse:
UNSUPPORTED("101qypctltvfnagwnn6e8ofl"); // 	print("{e : ", info);
UNSUPPORTED("cfit896buh0gkw7q1d7stizok"); // 	jsonRect(&op->u.ellipse, print, info);
UNSUPPORTED("6aw91xzjmqvmtdvt1di23af8y"); // 	break;
UNSUPPORTED("a02o53vx69zifve1qybxt6bwg"); //     case xd_filled_polygon:
UNSUPPORTED("6rcmfpfjotckasov8w4bzpb7r"); // 	print("{P : ", info);
UNSUPPORTED("cp182xgfbyzs7pjw98qp82hzb"); // 	jsonPolyline(&op->u.polygon, print, info);
UNSUPPORTED("6aw91xzjmqvmtdvt1di23af8y"); // 	break;
UNSUPPORTED("1amgc8nhs2zl8rzvqurr02syl"); //     case xd_unfilled_polygon:
UNSUPPORTED("dzu0713owvp18q74j7ppfmhw6"); // 	print("{p : ", info);
UNSUPPORTED("cp182xgfbyzs7pjw98qp82hzb"); // 	jsonPolyline(&op->u.polygon, print, info);
UNSUPPORTED("6aw91xzjmqvmtdvt1di23af8y"); // 	break;
UNSUPPORTED("i75482wp0xfww8yo6wanbdpe"); //     case xd_filled_bezier:
UNSUPPORTED("dqqwbsqud2t7i7xz1gohsqcq5"); // 	print("{b : ", info);
UNSUPPORTED("4xnyfd4ws1ocexeydxb9xpvth"); // 	jsonPolyline(&op->u.bezier, print, info);
UNSUPPORTED("6aw91xzjmqvmtdvt1di23af8y"); // 	break;
UNSUPPORTED("cljys033beawi3mtklyme87lc"); //     case xd_unfilled_bezier:
UNSUPPORTED("e7cley1ku4kmdkxq0aawfeb2t"); // 	print("{B : ", info);
UNSUPPORTED("4xnyfd4ws1ocexeydxb9xpvth"); // 	jsonPolyline(&op->u.bezier, print, info);
UNSUPPORTED("6aw91xzjmqvmtdvt1di23af8y"); // 	break;
UNSUPPORTED("3hbkjfy0n8isa112ucvobo206"); //     case xd_pen_color:
UNSUPPORTED("c4qk7i453wj5fobvuvr3ei45w"); // 	print("{c : ", info);
UNSUPPORTED("7zcrklnkletwkzkl84au4weoa"); // 	jsonString(op->u.color, print, info);
UNSUPPORTED("6aw91xzjmqvmtdvt1di23af8y"); // 	break;
UNSUPPORTED("7xkva0g2qpvpgvf24yglj76mo"); //     case xd_grad_pen_color:
UNSUPPORTED("c4qk7i453wj5fobvuvr3ei45w"); // 	print("{c : ", info);
UNSUPPORTED("dn1ii4k6v4t36te9fit9p5ow"); // 	toGradString (&xb, &op->u.grad_color);
UNSUPPORTED("efndbch1vq95g86ddil3h32hq"); // 	jsonString((((((&xb)->ptr >= (&xb)->eptr) ? agxbmore(&xb,1) : 0), (int)(*(&xb)->ptr++ = ((unsigned char)'\0'))),(char*)((&xb)->ptr = (&xb)->buf)), print, info);
UNSUPPORTED("6aw91xzjmqvmtdvt1di23af8y"); // 	break;
UNSUPPORTED("p898bkyv60y69qbm68pjwrc"); //     case xd_fill_color:
UNSUPPORTED("92we9quymvrj11tqzxd0xxwun"); // 	print("{C : ", info);
UNSUPPORTED("7zcrklnkletwkzkl84au4weoa"); // 	jsonString(op->u.color, print, info);
UNSUPPORTED("6aw91xzjmqvmtdvt1di23af8y"); // 	break;
UNSUPPORTED("iqk6nvbp31j9twtqmoldcx45"); //     case xd_grad_fill_color:
UNSUPPORTED("92we9quymvrj11tqzxd0xxwun"); // 	print("{C : ", info);
UNSUPPORTED("dn1ii4k6v4t36te9fit9p5ow"); // 	toGradString (&xb, &op->u.grad_color);
UNSUPPORTED("efndbch1vq95g86ddil3h32hq"); // 	jsonString((((((&xb)->ptr >= (&xb)->eptr) ? agxbmore(&xb,1) : 0), (int)(*(&xb)->ptr++ = ((unsigned char)'\0'))),(char*)((&xb)->ptr = (&xb)->buf)), print, info);
UNSUPPORTED("6aw91xzjmqvmtdvt1di23af8y"); // 	break;
UNSUPPORTED("a19xwyhpt7uym00dh6pmlvhhk"); //     case xd_polyline:
UNSUPPORTED("7a0hwcdrq1t22zkthfk8rjvny"); // 	print("{L :", info);
UNSUPPORTED("agckpp5qx857omx0z2p1omp9w"); // 	jsonPolyline(&op->u.polyline, print, info);
UNSUPPORTED("6aw91xzjmqvmtdvt1di23af8y"); // 	break;
UNSUPPORTED("8iuq6qr8jjaqun0ulqu2u1x77"); //     case xd_text:
UNSUPPORTED("6su4jcg42a5hqw1iewbdbqss8"); // 	print("{T : [", info);
UNSUPPORTED("du2g2u2x2kjtop4xp0foszscr"); // 	printInt(op->u.text.x, print, info);
UNSUPPORTED("9vp943ns0hhsqhqgt5bko6l5e"); // 	print(",", info);
UNSUPPORTED("e5fzs5nglnsh9m8cvrm6mhh9j"); // 	printInt(op->u.text.y, print, info);
UNSUPPORTED("9vp943ns0hhsqhqgt5bko6l5e"); // 	print(",", info);
UNSUPPORTED("12ip8eyyw9723ifasdged1v2g"); // 	printAlign(op->u.text.align, print, info);
UNSUPPORTED("9vp943ns0hhsqhqgt5bko6l5e"); // 	print(",", info);
UNSUPPORTED("dff5na2fdib9h0c3ltg1q62m1"); // 	printInt(op->u.text.width, print, info);
UNSUPPORTED("9vp943ns0hhsqhqgt5bko6l5e"); // 	print(",", info);
UNSUPPORTED("exr3mgpya5ekmani7zwyecckp"); // 	jsonString(op->u.text.text, print, info);
UNSUPPORTED("1xljw3cra5mz10d8m2ofd93g1"); // 	print("]", info);
UNSUPPORTED("6aw91xzjmqvmtdvt1di23af8y"); // 	break;
UNSUPPORTED("7mkkywc80hjajd0vttqrsfcsz"); //     case xd_font:
UNSUPPORTED("chm0rcdooqkwdilqs4ob4x7er"); // 	print("{F : [", info);
UNSUPPORTED("9kl1bheiih6co789yxhdhiujb"); // 	op->kind = xd_font;
UNSUPPORTED("2v4nq9epbpb850zg0p0ne7i9z"); // 	printFloat(op->u.font.size, print, info, 1);
UNSUPPORTED("9vp943ns0hhsqhqgt5bko6l5e"); // 	print(",", info);
UNSUPPORTED("4gjyxukif0iv2opnta7dxjuud"); // 	jsonString(op->u.font.name, print, info);
UNSUPPORTED("1xljw3cra5mz10d8m2ofd93g1"); // 	print("]", info);
UNSUPPORTED("6aw91xzjmqvmtdvt1di23af8y"); // 	break;
UNSUPPORTED("4bvixlppj4a6yebt6egxnzu9e"); //     case xd_fontchar:
UNSUPPORTED("7kn2tqcgq6mh4f6a2wjucq8ut"); // 	print("{t : ", info);
UNSUPPORTED("7f3e0jgah853oke7gxa99azrt"); // 	printInt(op->u.fontchar, print, info);
UNSUPPORTED("6aw91xzjmqvmtdvt1di23af8y"); // 	break;
UNSUPPORTED("9c1yqt1pf6mjkrdhf7pxoea82"); //     case xd_style:
UNSUPPORTED("c96ck14wi4rohmbat8gi5dld6"); // 	print("{S : ", info);
UNSUPPORTED("aee1scodw2sd4ao9kc1vbmqhk"); // 	jsonString(op->u.style, print, info);
UNSUPPORTED("6aw91xzjmqvmtdvt1di23af8y"); // 	break;
UNSUPPORTED("3tev1dlfh70jm5km963v1i7rr"); //     case xd_image:
UNSUPPORTED("arfqyu09gop5ue9k4msfg8etr"); // 	print("{I : [", info);
UNSUPPORTED("71vovzeon9tb2c11s1nknqmff"); // 	jsonRect(&op->u.image.pos, print, info);
UNSUPPORTED("9vp943ns0hhsqhqgt5bko6l5e"); // 	print(",", info);
UNSUPPORTED("cr1qzeh98mlvqnlw58iuual50"); // 	jsonString(op->u.image.name, print, info);
UNSUPPORTED("1xljw3cra5mz10d8m2ofd93g1"); // 	print("]", info);
UNSUPPORTED("6aw91xzjmqvmtdvt1di23af8y"); // 	break;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("21eozdlhui5hv29fj41fdny45"); //     if (more)
UNSUPPORTED("5b86atqkvtk6yh0jjr574te6i"); // 	print("},\n", info);
UNSUPPORTED("div10atae09n36x269sl208r1"); //     else
UNSUPPORTED("697b92co0pv9qoy9ikskp8tt2"); // 	print("}\n", info);
UNSUPPORTED("9ocnzhe59r19odwgtedwnydm"); //     agxbfree (&xb);
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 2zdgagi4yv5fkpnso5ildjfj5
// static void _printXDot(xdot * x, pf print, void *info, print_op ofn) 
public static Object _printXDot(Object... arg) {
UNSUPPORTED("beqsyisd37qh2rmab3wmlihg9"); // static void _printXDot(xdot * x, pf print, void *info, print_op ofn)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("b17di9c7wgtqm51bvsyxz6e2f"); //     int i;
UNSUPPORTED("7iglpnbs5h4cbinybqfog9ddx"); //     xdot_op *op;
UNSUPPORTED("7hh6k0a6zqwhdnmtvr8eessta"); //     char *base = (char *) (x->ops);
UNSUPPORTED("budcfgz6nozjl4wd3lhsp20s7"); //     for (i = 0; i < x->cnt; i++) {
UNSUPPORTED("coll83cylkgt1g7npgfdqqgx3"); // 	op = (xdot_op *) (base + i * x->sz);
UNSUPPORTED("dh2m2dnrjv9clw1u2sgwmpw6t"); // 	ofn(op, print, info, (i < x->cnt - 1));
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 1c9ib0ywnb7bedzxnss4cmagy
// char *sprintXDot(xdot * x) 
public static Object sprintXDot(Object... arg) {
UNSUPPORTED("2e52l2dihzy7a2f1vl7qlv1xl"); // char *sprintXDot(xdot * x)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("8yytudftst76763qgnjebkzhm"); //     char *s;
UNSUPPORTED("esg3s800dx899v69pkng2kavv"); //     unsigned char buf[BUFSIZ];
UNSUPPORTED("9gou5otj6s39l2cbyc8i5i5lq"); //     agxbuf xb;
UNSUPPORTED("3jwm77zyv02ukrvjv9jppejf7"); //     agxbinit(&xb, BUFSIZ, buf);
UNSUPPORTED("6sioi1y1n9tqffo04oybnku8f"); //     _printXDot(x, (pf) agxbput, &xb, printXDot_Op);
UNSUPPORTED("ezli236dfm9v74jhi9tffjqlg"); //     s = strdup((((((&xb)->ptr >= (&xb)->eptr) ? agxbmore(&xb,1) : 0), (int)(*(&xb)->ptr++ = ((unsigned char)'\0'))),(char*)((&xb)->ptr = (&xb)->buf)));
UNSUPPORTED("1at5m9ctjn3ukv5gqtfswik02"); //     agxbfree(&xb);
UNSUPPORTED("3y6wj3ntgmr1qkdpm7wp1dsch"); //     return s;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 eyjcl48szw6vtlxtutpssg7q5
// void fprintXDot(FILE * fp, xdot * x) 
public static Object fprintXDot(Object... arg) {
UNSUPPORTED("85u4x93wot46fvnzf3cz0cfk0"); // void fprintXDot(FILE * fp, xdot * x)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("1qdeobw8612jy9o2hfcrb6kq2"); //     _printXDot(x, (pf) fputs, fp, printXDot_Op);
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 701gu0x25va3ckongikjzclu2
// void jsonXDot(FILE * fp, xdot * x) 
public static Object jsonXDot(Object... arg) {
UNSUPPORTED("8gckq5ubk6r40s6smk07wzmip"); // void jsonXDot(FILE * fp, xdot * x)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("6izaqjssjpc0w431vv0fizft2"); //     fputs ("[\n", fp);
UNSUPPORTED("4ui1pnf80zlet8vuuq94tv91m"); //     _printXDot(x, (pf) fputs, fp, jsonXDot_Op);
UNSUPPORTED("d0endh1to4wsuqqpyuqcwflcz"); //     fputs ("]\n", fp);
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 eu3dz6gm2u949z4rva28vws5z
// static void freeXOpData(xdot_op * x) 
public static Object freeXOpData(Object... arg) {
UNSUPPORTED("d67njmp17xncuo32921x74jb7"); // static void freeXOpData(xdot_op * x)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("ebv1xjp5fu1yrwjo0u1nkgbku"); //     switch (x->kind) {
UNSUPPORTED("a02o53vx69zifve1qybxt6bwg"); //     case xd_filled_polygon:
UNSUPPORTED("1amgc8nhs2zl8rzvqurr02syl"); //     case xd_unfilled_polygon:
UNSUPPORTED("3bulq0614xay7aipkjqh6zpyj"); // 	free(x->u.polyline.pts);
UNSUPPORTED("6aw91xzjmqvmtdvt1di23af8y"); // 	break;
UNSUPPORTED("i75482wp0xfww8yo6wanbdpe"); //     case xd_filled_bezier:
UNSUPPORTED("cljys033beawi3mtklyme87lc"); //     case xd_unfilled_bezier:
UNSUPPORTED("3bulq0614xay7aipkjqh6zpyj"); // 	free(x->u.polyline.pts);
UNSUPPORTED("6aw91xzjmqvmtdvt1di23af8y"); // 	break;
UNSUPPORTED("a19xwyhpt7uym00dh6pmlvhhk"); //     case xd_polyline:
UNSUPPORTED("3bulq0614xay7aipkjqh6zpyj"); // 	free(x->u.polyline.pts);
UNSUPPORTED("6aw91xzjmqvmtdvt1di23af8y"); // 	break;
UNSUPPORTED("8iuq6qr8jjaqun0ulqu2u1x77"); //     case xd_text:
UNSUPPORTED("bsij5o7b7yq4jagg1x1rw9m7b"); // 	free(x->u.text.text);
UNSUPPORTED("6aw91xzjmqvmtdvt1di23af8y"); // 	break;
UNSUPPORTED("p898bkyv60y69qbm68pjwrc"); //     case xd_fill_color:
UNSUPPORTED("3hbkjfy0n8isa112ucvobo206"); //     case xd_pen_color:
UNSUPPORTED("4fm7etfn9hr7qh74k70k45uc5"); // 	free(x->u.color);
UNSUPPORTED("6aw91xzjmqvmtdvt1di23af8y"); // 	break;
UNSUPPORTED("iqk6nvbp31j9twtqmoldcx45"); //     case xd_grad_fill_color:
UNSUPPORTED("7xkva0g2qpvpgvf24yglj76mo"); //     case xd_grad_pen_color:
UNSUPPORTED("eeydb7vp5du56qijz8fccw2r3"); // 	freeXDotColor (&x->u.grad_color);
UNSUPPORTED("6aw91xzjmqvmtdvt1di23af8y"); // 	break;
UNSUPPORTED("7mkkywc80hjajd0vttqrsfcsz"); //     case xd_font:
UNSUPPORTED("5tw7fqrjrai5235dxqqspkn4e"); // 	free(x->u.font.name);
UNSUPPORTED("6aw91xzjmqvmtdvt1di23af8y"); // 	break;
UNSUPPORTED("9c1yqt1pf6mjkrdhf7pxoea82"); //     case xd_style:
UNSUPPORTED("7ubzpnx9p19ff69lqpgd94zvj"); // 	free(x->u.style);
UNSUPPORTED("6aw91xzjmqvmtdvt1di23af8y"); // 	break;
UNSUPPORTED("3tev1dlfh70jm5km963v1i7rr"); //     case xd_image:
UNSUPPORTED("1hkdbbhj8w4le1mure7j1iycv"); // 	free(x->u.image.name);
UNSUPPORTED("6aw91xzjmqvmtdvt1di23af8y"); // 	break;
UNSUPPORTED("8l3rwj6ctswoa4gvh2j4poq70"); //     default:
UNSUPPORTED("6aw91xzjmqvmtdvt1di23af8y"); // 	break;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 6w9r3p709c95b1186uuktgd6w
// void freeXDot (xdot * x) 
public static Object freeXDot(Object... arg) {
UNSUPPORTED("bkwtbjppiirw4uu074jdrqmwx"); // void freeXDot (xdot * x)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("b17di9c7wgtqm51bvsyxz6e2f"); //     int i;
UNSUPPORTED("7iglpnbs5h4cbinybqfog9ddx"); //     xdot_op *op;
UNSUPPORTED("ejkl9z7itgy7zzscudx1yzdk7"); //     char *base;
UNSUPPORTED("3wjppwgev4ff3xk0a3t00ad33"); //     freefunc_t ff = x->freefunc;
UNSUPPORTED("122mngve03ds89670xc36bby4"); //     if (!x) return;
UNSUPPORTED("bfek0k78f6s41yzixruv7bn0n"); //     base = (char *) (x->ops);
UNSUPPORTED("budcfgz6nozjl4wd3lhsp20s7"); //     for (i = 0; i < x->cnt; i++) {
UNSUPPORTED("coll83cylkgt1g7npgfdqqgx3"); // 	op = (xdot_op *) (base + i * x->sz);
UNSUPPORTED("btmjd8b69pae4tiax28j2a9yb"); // 	if (ff) ff (op);
UNSUPPORTED("7guxp0atlez0kkyrftijyd56n"); // 	freeXOpData(op);
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("bh93g0pb87hg3tj0uqdyob6ch"); //     free(base);
UNSUPPORTED("8uuggodur7e2m5hhf7yudpatc"); //     free(x);
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 5ppkb51932f7y8920b91b8yz0
// int statXDot (xdot* x, xdot_stats* sp) 
public static Object statXDot(Object... arg) {
UNSUPPORTED("b17qzxcu2x9pfzv1bgj20qzsu"); // int statXDot (xdot* x, xdot_stats* sp)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("b17di9c7wgtqm51bvsyxz6e2f"); //     int i;
UNSUPPORTED("7iglpnbs5h4cbinybqfog9ddx"); //     xdot_op *op;
UNSUPPORTED("ejkl9z7itgy7zzscudx1yzdk7"); //     char *base;
UNSUPPORTED("dvr4f7ur0dz4axrewk8pjbmr6"); //     if (!x || !sp) return 1;
UNSUPPORTED("dceumk5yz37ggmopa5ozrd0kd"); //     memset(sp, 0, sizeof(xdot_stats));
UNSUPPORTED("9qgptx96avnkvh76z1iqxnluz"); //     sp->cnt = x->cnt;
UNSUPPORTED("bfek0k78f6s41yzixruv7bn0n"); //     base = (char *) (x->ops);
UNSUPPORTED("budcfgz6nozjl4wd3lhsp20s7"); //     for (i = 0; i < x->cnt; i++) {
UNSUPPORTED("coll83cylkgt1g7npgfdqqgx3"); // 	op = (xdot_op *) (base + i * x->sz);
UNSUPPORTED("a11dc1tsoh8jlk9a6zq7xidtf"); //  	switch (op->kind) {
UNSUPPORTED("gyhetlwsld2yu7e13jh9wl0p"); // 	case xd_filled_ellipse:
UNSUPPORTED("7o72maa292xeug6fqyefi5yeh"); // 	case xd_unfilled_ellipse:
UNSUPPORTED("byo1z59r61djcdrqo2sz0s6d8"); // 	    sp->n_ellipse++;
UNSUPPORTED("ai3czg6gaaxspsmndknpyvuiu"); // 	    break;
UNSUPPORTED("c4cta0iol0ehph9h9jbj3hq6b"); // 	case xd_filled_polygon:
UNSUPPORTED("bouwa31z2x0t0i08rrhgcc80s"); // 	case xd_unfilled_polygon:
UNSUPPORTED("n653bv9uo9zdwuay4ttk727m"); // 	    sp->n_polygon++;
UNSUPPORTED("8pkl31upi6bo7e5456dqgg9cf"); // 	    sp->n_polygon_pts += op->u.polygon.cnt;
UNSUPPORTED("ai3czg6gaaxspsmndknpyvuiu"); // 	    break;
UNSUPPORTED("c5d0rnemshsbpz91xa6p8zlgm"); // 	case xd_filled_bezier:
UNSUPPORTED("cfxvwl549fde103677s4mrkan"); // 	case xd_unfilled_bezier:
UNSUPPORTED("bc9n0o6ayfum7bnpr3uy803re"); // 	    sp->n_bezier++;
UNSUPPORTED("eong22lj6t9v9twe68w6225vm"); // 	    sp->n_bezier_pts += op->u.bezier.cnt;
UNSUPPORTED("ai3czg6gaaxspsmndknpyvuiu"); // 	    break;
UNSUPPORTED("2o8knajzbqdw17pjofl3lyba5"); // 	case xd_polyline:
UNSUPPORTED("5yiqntymnbiahfga4vc7fodvi"); // 	    sp->n_polyline++;
UNSUPPORTED("wqyxazka9baia8rl594mcn8v"); // 	    sp->n_polyline_pts += op->u.polyline.cnt;
UNSUPPORTED("ai3czg6gaaxspsmndknpyvuiu"); // 	    break;
UNSUPPORTED("3j1afgdctetzfhmpq1in869a8"); // 	case xd_text:
UNSUPPORTED("cmtkneyksr4n2nqm6zix8obpb"); // 	    sp->n_text++;
UNSUPPORTED("ai3czg6gaaxspsmndknpyvuiu"); // 	    break;
UNSUPPORTED("a7hdcxzowxlm4uwwzc9birrhz"); // 	case xd_image:
UNSUPPORTED("cyrdu153ri3djcqvdysa3e9ma"); // 	    sp->n_image++;
UNSUPPORTED("ai3czg6gaaxspsmndknpyvuiu"); // 	    break;
UNSUPPORTED("5iiy6stlclt7q11quzekzoahe"); // 	case xd_fill_color:
UNSUPPORTED("dasyqbofzz98834s6dlt2j59r"); // 	case xd_pen_color:
UNSUPPORTED("drrnos6mkmowo515fz3aho484"); // 	    sp->n_color++;
UNSUPPORTED("ai3czg6gaaxspsmndknpyvuiu"); // 	    break;
UNSUPPORTED("awaj31d0herhghbn9hyh0h1ud"); // 	case xd_grad_fill_color:
UNSUPPORTED("4jem039cg71t5w6niim5cmtja"); // 	case xd_grad_pen_color:
UNSUPPORTED("a0wcwsdvkhuvy96sq00kf9bdv"); // 	    sp->n_gradcolor++;
UNSUPPORTED("ai3czg6gaaxspsmndknpyvuiu"); // 	    break;
UNSUPPORTED("5buaw3jwt248z13cbi4dw72rv"); //         case xd_font:
UNSUPPORTED("9vmuklbiba31872a14t1ah25g"); // 	    sp->n_font++;
UNSUPPORTED("ai3czg6gaaxspsmndknpyvuiu"); // 	    break;
UNSUPPORTED("3zomvq2vb2nxfyjrz92qbvebb"); //         case xd_fontchar:
UNSUPPORTED("ab2c6l3irdvzagnh22t7cm7u7"); // 	    sp->n_fontchar++;
UNSUPPORTED("ai3czg6gaaxspsmndknpyvuiu"); // 	    break;
UNSUPPORTED("8y8p7g08bet801pbrz3v2318k"); // 	case xd_style:
UNSUPPORTED("64gbg1bn71xd32z3wwcxw7nvs"); // 	    sp->n_style++;
UNSUPPORTED("ai3czg6gaaxspsmndknpyvuiu"); // 	    break;
UNSUPPORTED("5ujjs4gho9mjjupbibyqyplxp"); // 	default :
UNSUPPORTED("ai3czg6gaaxspsmndknpyvuiu"); // 	    break;
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("5oxhd3fvp0gfmrmz12vndnjt"); //     return 0;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 99e5rt9esmjy9qkrpjj7ynswj
// xdot_grad_type  colorType (char* cp) 
public static Object colorType(Object... arg) {
UNSUPPORTED("cwdrzw5wilfdxd4c2q0iq3jk7"); // xdot_grad_type 
UNSUPPORTED("4mpmfofo3mrmcqks805lq8fcz"); // colorType (char* cp)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("cr1ew9y7c1x3j9hax94mlznse"); //     xdot_grad_type rv;
UNSUPPORTED("eck0zpg9yxtq1rk3duhbe3bra"); //     switch (*cp) {
UNSUPPORTED("5hf3lvn9xppbffbljy7ctxuws"); //     case '[' :
UNSUPPORTED("cfshpws9bhzz0wwfffkxsx9zn"); // 	rv = xd_linear;
UNSUPPORTED("6aw91xzjmqvmtdvt1di23af8y"); // 	break;
UNSUPPORTED("7fug2421p5vf8mm40vy1p2m9v"); //     case '(' :
UNSUPPORTED("4q5p6a8iwssmvo5k6r8rh4w7p"); // 	rv = xd_radial;
UNSUPPORTED("6aw91xzjmqvmtdvt1di23af8y"); // 	break;
UNSUPPORTED("29hi90x8rlo9f7q3mhj5dg71d"); //     default :
UNSUPPORTED("ev130oonej2ev8dyzjye3mh13"); // 	rv = xd_none;
UNSUPPORTED("6aw91xzjmqvmtdvt1di23af8y"); // 	break;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("v7vqc9l7ge2bfdwnw11z7rzi"); //     return rv;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 cr8h18xais4lf1qgyf0g4asw8
// static char* radGradient (char* cp, xdot_color* clr) 
public static Object radGradient(Object... arg) {
UNSUPPORTED("1yranxmu2maol02ulzd1ka1re"); // static char*
UNSUPPORTED("c06wjt41hfdz5kqs2bgvfzceu"); // radGradient (char* cp, xdot_color* clr)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("al4i3evop0chjzmtu1hand9z7"); //     char* s = cp;
UNSUPPORTED("b17di9c7wgtqm51bvsyxz6e2f"); //     int i;
UNSUPPORTED("8ne6p4d5pykwl1d3xk0yg0ipb"); //     double d;
UNSUPPORTED("3cwcf8fhklyr0kmoplrimnxif"); //     xdot_color_stop* stops = NULL;
UNSUPPORTED("9no119gmgymj8afua0bzjg6s"); //     clr->type = xd_radial;
UNSUPPORTED("7tr6l5gdo66lrh52cf1q5euke"); //     s = parseReal(s, &clr->u.ring.x0);
UNSUPPORTED("d12z8bcj00q4anqzoqw7ooslh"); //     if(!s){free(stops);return NULL;};
UNSUPPORTED("aj0fpvr0kifndf28kqgvnvcxj"); //     s = parseReal(s, &clr->u.ring.y0);
UNSUPPORTED("d12z8bcj00q4anqzoqw7ooslh"); //     if(!s){free(stops);return NULL;};
UNSUPPORTED("8z7hm5iter228czykl2d7o2u"); //     s = parseReal(s, &clr->u.ring.r0);
UNSUPPORTED("d12z8bcj00q4anqzoqw7ooslh"); //     if(!s){free(stops);return NULL;};
UNSUPPORTED("5szk7acro300ld5mynca1crd7"); //     s = parseReal(s, &clr->u.ring.x1);
UNSUPPORTED("d12z8bcj00q4anqzoqw7ooslh"); //     if(!s){free(stops);return NULL;};
UNSUPPORTED("6mszl2wb5yjgawvt86wt4xnfk"); //     s = parseReal(s, &clr->u.ring.y1);
UNSUPPORTED("d12z8bcj00q4anqzoqw7ooslh"); //     if(!s){free(stops);return NULL;};
UNSUPPORTED("eonwrf0vyl6643nxoe4243ufp"); //     s = parseReal(s, &clr->u.ring.r1);
UNSUPPORTED("d12z8bcj00q4anqzoqw7ooslh"); //     if(!s){free(stops);return NULL;};
UNSUPPORTED("21uyg70wnmu3pyvfnpsru6z84"); //     s = parseInt(s, &clr->u.ring.n_stops);
UNSUPPORTED("d12z8bcj00q4anqzoqw7ooslh"); //     if(!s){free(stops);return NULL;};
UNSUPPORTED("463gu8qdr6ujd5ewcr0qb7iwt"); //     stops = (xdot_color_stop*)calloc((clr->u.ring.n_stops), sizeof(xdot_color_stop));
UNSUPPORTED("8ivfd2dyo5rbybzlrwxp6wyn3"); //     for (i = 0; i < clr->u.ring.n_stops; i++) {
UNSUPPORTED("3u2xlz22rewp744asdc0bytvg"); // 	s = parseReal(s, &d);
UNSUPPORTED("a0v4lnk3pysd85f3bisny1r3d"); // 	if(!s){free(stops);return NULL;};
UNSUPPORTED("jlx4hmg3e9km3tzyrivx28ia"); // 	stops[i].frac = d;
UNSUPPORTED("2epsrdxaugx40ma9hbigwwgwn"); // 	s = parseString(s, &stops[i].color);
UNSUPPORTED("a0v4lnk3pysd85f3bisny1r3d"); // 	if(!s){free(stops);return NULL;};
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("6m8rt8apu9sd63f5z0wr4k8b6"); //     clr->u.ring.stops = stops;
UNSUPPORTED("az4lr3wfzqu3df9wce5kcyady"); //     return cp;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 etmxebtapyaikihfpbsuehvzw
// static char* linGradient (char* cp, xdot_color* clr) 
public static Object linGradient(Object... arg) {
UNSUPPORTED("1yranxmu2maol02ulzd1ka1re"); // static char*
UNSUPPORTED("b55lo3bj9pnkmwxlyocdqefjc"); // linGradient (char* cp, xdot_color* clr)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("al4i3evop0chjzmtu1hand9z7"); //     char* s = cp;
UNSUPPORTED("b17di9c7wgtqm51bvsyxz6e2f"); //     int i;
UNSUPPORTED("8ne6p4d5pykwl1d3xk0yg0ipb"); //     double d;
UNSUPPORTED("3cwcf8fhklyr0kmoplrimnxif"); //     xdot_color_stop* stops = NULL;
UNSUPPORTED("57x32822mgbsnq8whh355dqre"); //     clr->type = xd_linear;
UNSUPPORTED("ark8bh8iswvlirv4bjk1bqhrw"); //     s = parseReal(s, &clr->u.ling.x0);
UNSUPPORTED("d12z8bcj00q4anqzoqw7ooslh"); //     if(!s){free(stops);return NULL;};
UNSUPPORTED("5td0oqu8abk0thbuh313mzd6o"); //     s = parseReal(s, &clr->u.ling.y0);
UNSUPPORTED("d12z8bcj00q4anqzoqw7ooslh"); //     if(!s){free(stops);return NULL;};
UNSUPPORTED("b8cvrv8opa85wy8igqk79g7d7"); //     s = parseReal(s, &clr->u.ling.x1);
UNSUPPORTED("d12z8bcj00q4anqzoqw7ooslh"); //     if(!s){free(stops);return NULL;};
UNSUPPORTED("dknvmejt8kipg3y8sbtkoep7z"); //     s = parseReal(s, &clr->u.ling.y1);
UNSUPPORTED("d12z8bcj00q4anqzoqw7ooslh"); //     if(!s){free(stops);return NULL;};
UNSUPPORTED("25k8bj46m6jyyal6cq6jekyvv"); //     s = parseInt(s, &clr->u.ling.n_stops);
UNSUPPORTED("d12z8bcj00q4anqzoqw7ooslh"); //     if(!s){free(stops);return NULL;};
UNSUPPORTED("5rf9mvmwp48cnm63n826n47hs"); //     stops = (xdot_color_stop*)calloc((clr->u.ling.n_stops), sizeof(xdot_color_stop));
UNSUPPORTED("33udax4ghl8rclztmn80tqcms"); //     for (i = 0; i < clr->u.ling.n_stops; i++) {
UNSUPPORTED("3u2xlz22rewp744asdc0bytvg"); // 	s = parseReal(s, &d);
UNSUPPORTED("a0v4lnk3pysd85f3bisny1r3d"); // 	if(!s){free(stops);return NULL;};
UNSUPPORTED("jlx4hmg3e9km3tzyrivx28ia"); // 	stops[i].frac = d;
UNSUPPORTED("2epsrdxaugx40ma9hbigwwgwn"); // 	s = parseString(s, &stops[i].color);
UNSUPPORTED("a0v4lnk3pysd85f3bisny1r3d"); // 	if(!s){free(stops);return NULL;};
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("ptvrq9unacmo19dbcuqsw5hs"); //     clr->u.ling.stops = stops;
UNSUPPORTED("az4lr3wfzqu3df9wce5kcyady"); //     return cp;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 2xdgjw8k00kahh9gsbqku1umv
// char* parseXDotColor (char* cp, xdot_color* clr) 
public static Object parseXDotColor(Object... arg) {
UNSUPPORTED("cqm25rponse4rsi686sbn1lo0"); // char*
UNSUPPORTED("b1mfye1zmcj65mtkq9c97iewc"); // parseXDotColor (char* cp, xdot_color* clr)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("4jzs7e30qz04dbigz4312soqm"); //     char c = *cp;
UNSUPPORTED("8amt8fmqdipygnxirowfbawox"); //     switch (c) {
UNSUPPORTED("5hf3lvn9xppbffbljy7ctxuws"); //     case '[' :
UNSUPPORTED("6mfazl1u2o7yhg2w5wjkacuam"); // 	return linGradient (cp+1, clr);
UNSUPPORTED("6aw91xzjmqvmtdvt1di23af8y"); // 	break;
UNSUPPORTED("7fug2421p5vf8mm40vy1p2m9v"); //     case '(' :
UNSUPPORTED("4q00cne45f9wmtg4ib0a706ke"); // 	return radGradient (cp+1, clr);
UNSUPPORTED("6aw91xzjmqvmtdvt1di23af8y"); // 	break;
UNSUPPORTED("4cj632tvft1wcpezg8rs6k4ux"); //     case '#' :
UNSUPPORTED("akaqca9sqliw6p0fmic1xj32y"); //     case '/' :
UNSUPPORTED("9lk7f6fpju45nq995i59ec3uq"); // 	clr->type = xd_none; 
UNSUPPORTED("b1eztpupu7ok5m0w7u2ji432j"); // 	clr->u.clr = cp;
UNSUPPORTED("e6bh2um7pqrlkuxueips4t7pi"); // 	return cp;
UNSUPPORTED("6aw91xzjmqvmtdvt1di23af8y"); // 	break;
UNSUPPORTED("29hi90x8rlo9f7q3mhj5dg71d"); //     default :
UNSUPPORTED("8ukynmh00mj2vf4p5rh4mn72h"); // 	if (isalnum(c)) {
UNSUPPORTED("3osvgszhwg2l36m35rz7hjl8d"); // 	    clr->type = xd_none; 
UNSUPPORTED("af2fdffzkgez7alg79ev8oafq"); // 	    clr->u.clr = cp;
UNSUPPORTED("hlsdiu6i05x2q5cxsa9myd04"); // 	    return cp;
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("9352ql3e58qs4fzapgjfrms2s"); // 	else
UNSUPPORTED("7t3fvwp9cv90qu5bdjdglcgtk"); // 	    return NULL;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 am6ngtbh04wxuyw4ohmmjtiy2
// void freeXDotColor (xdot_color* cp) 
public static Object freeXDotColor(Object... arg) {
UNSUPPORTED("dz8v7jtcg5p1c58myh27zc9pt"); // void freeXDotColor (xdot_color* cp)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("b17di9c7wgtqm51bvsyxz6e2f"); //     int i;
UNSUPPORTED("19ar0bs2h13txvy4uwtw868m"); //     if (cp->type == xd_linear) {
UNSUPPORTED("81392dv0g58pud5wxe52pgf6e"); // 	for (i = 0; i < cp->u.ling.n_stops; i++) {
UNSUPPORTED("6wzfng0iio7thawoxyaiwc5xt"); // 	    free (cp->u.ling.stops[i].color);
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("3317yv7m2rcitjg00m6d02wnt"); // 	free (cp->u.ling.stops);
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("etp4zs2ofz2x0b9yrx7z9xa08"); //     else if (cp->type == xd_radial) {
UNSUPPORTED("8plb1dios9foj9qo7n5jc5n7m"); // 	for (i = 0; i < cp->u.ring.n_stops; i++) {
UNSUPPORTED("kp618tg06ig032jupq8pyp1k"); // 	    free (cp->u.ring.stops[i].color);
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("eo4csunnsophbm4ec0ilzoal8"); // 	free (cp->u.ring.stops);
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}


}
