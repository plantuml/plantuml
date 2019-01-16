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
package gen.lib.cgraph;
import static smetana.core.Macro.UNSUPPORTED;

public class agxbuf__c {


//3 688cb39nv214oqldmhd8roirz
// void agxbinit(agxbuf * xb, unsigned int hint, unsigned char *init) 
public static Object agxbinit(Object... arg) {
UNSUPPORTED("4l5fskgdbcbqssdclb6lwckwe"); // void agxbinit(agxbuf * xb, unsigned int hint, unsigned char *init)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("3ctz7esjgupftv01eshc2dhx2"); //     if (init) {
UNSUPPORTED("7ccqm4ipez0mmdk6bv50shi8z"); // 	xb->buf = init;
UNSUPPORTED("bgb2e1tveztx6w0nuo6t6kxbd"); // 	xb->dyna = 0;
UNSUPPORTED("c07up7zvrnu2vhzy6d7zcu94g"); //     } else {
UNSUPPORTED("49jhfm9yw3megswomc4gzzgd7"); // 	if (hint == 0)
UNSUPPORTED("5h1ggxg5ypn75rue18vgizp12"); // 	    hint = BUFSIZ;
UNSUPPORTED("bwuph2kus95n04yy45ff0ygwg"); // 	xb->dyna = 1;
UNSUPPORTED("rowpmdm8bmtos5w0uxjinsts"); // 	xb->buf = (unsigned char*)malloc((hint)*sizeof(unsigned char));
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("1vqjbr0qaxnp1tks2ilwqgn3g"); //     xb->eptr = xb->buf + hint;
UNSUPPORTED("dtbxbzhkw05lut0ozk9a49lw6"); //     xb->ptr = xb->buf;
UNSUPPORTED("5ymin98xo0ermvpyhsuo8xwim"); //     *xb->ptr = '\0';
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 lu77u8ojbx0os3eapudi7k0e
// int agxbmore(agxbuf * xb, unsigned int ssz) 
public static Object agxbmore(Object... arg) {
UNSUPPORTED("1zbuuoy42nrbwgmz0kzwxatml"); // int agxbmore(agxbuf * xb, unsigned int ssz)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("ayzlme0ebe3j87lpz076cg2vq"); //     int cnt;			/* current no. of characters in buffer */
UNSUPPORTED("71r9w1f25l9h79jodjer7my8a"); //     int size;			/* current buffer size */
UNSUPPORTED("2bckq0rejaf94iovglem6hqko"); //     int nsize;			/* new buffer size */
UNSUPPORTED("dmokvf86b5kj3srnwwbr7ebmc"); //     unsigned char *nbuf;	/* new buffer */
UNSUPPORTED("347vqdgen21gpinnet4lkk7gk"); //     size = xb->eptr - xb->buf;
UNSUPPORTED("49rolm7orqt4aw3uud529g4qc"); //     nsize = 2 * size;
UNSUPPORTED("svb97awsbpziy0qhoaq68u7b"); //     if (size + (int)ssz > nsize)
UNSUPPORTED("7ykdy1xuvrmibubukfxhfmz91"); // 	nsize = size + ssz;
UNSUPPORTED("bfinudkdiev8mwo6udbayoaex"); //     cnt = xb->ptr - xb->buf;
UNSUPPORTED("4cmq49lu7qhmui59rl36qd6fr"); //     if (xb->dyna) {
UNSUPPORTED("8vf8arbygb0610hkc403uiror"); // 	nbuf = realloc(xb->buf, nsize);
UNSUPPORTED("c07up7zvrnu2vhzy6d7zcu94g"); //     } else {
UNSUPPORTED("3sajbfvh0ecfy6mdfwrwrojl6"); // 	nbuf = (unsigned char*)malloc((nsize)*sizeof(unsigned char));
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




//3 7q04bdt3el30gta4bdtl0cr8s
// int agxbput_n(agxbuf * xb, const char *s, unsigned int ssz) 
public static Object agxbput_n(Object... arg) {
UNSUPPORTED("xj5zv8tlevqufi7kllkj6uu1"); // int agxbput_n(agxbuf * xb, const char *s, unsigned int ssz)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("6q0wy2t9qi11079j0wyuhsmiw"); //     if (xb->ptr + ssz > xb->eptr)
UNSUPPORTED("82p9dlopf5tuzmyy5454sv4mm"); // 	agxbmore(xb, ssz);
UNSUPPORTED("9tvk5ztcu9lg2u40sfmevitl2"); //     memcpy(xb->ptr, s, ssz);
UNSUPPORTED("2wy6om6o92zevnnevl5v0pr0c"); //     xb->ptr += ssz;
UNSUPPORTED("dzondrrxi2pe7xy72r0jstd20"); //     return ssz;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 12y895mwxr60srt40guuma6j5
// int agxbput(agxbuf * xb, const char *s) 
public static Object agxbput(Object... arg) {
UNSUPPORTED("3oqw629wj8yulgtvwwlxn8iyi"); // int agxbput(agxbuf * xb, const char *s)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("dmstj6pqesin7a4ufrtut65t5"); //     unsigned int ssz = strlen(s);
UNSUPPORTED("7i8gvckml5cbfmuhw2pjs8czl"); //     return agxbput_n(xb, s, ssz);
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 2zlar00xk0a2nnqcmjb587kvc
// void agxbfree(agxbuf * xb) 
public static Object agxbfree(Object... arg) {
UNSUPPORTED("3zwjoplc2wp6x7lb2b5g59pl8"); // void agxbfree(agxbuf * xb)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("da8gv3dgf6bnks54n9mnlgzxk"); //     if (xb->dyna)
UNSUPPORTED("62irfu7vrq2ewolixn0ksp5f6"); // 	free(xb->buf);
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 cn9iiltxtts5ijjpixwt2uziu
// int agxbpop(agxbuf * xb) 
public static Object agxbpop(Object... arg) {
UNSUPPORTED("1hje4ns6ul7cj52y9n9wlgkkc"); // int agxbpop(agxbuf * xb)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("53xzwretgdbd0atozc0w6hagb"); //     int c;
UNSUPPORTED("a6gx1f2k9r1jreuxvg1n7jpiw"); //     if (xb->ptr > xb->buf) {
UNSUPPORTED("asuii09e2jl1px5timbisq3sx"); // 	c = *xb->ptr--;
UNSUPPORTED("7q6l09k0u87z31e0nmv2lwr94"); // 	return c;
UNSUPPORTED("2lkbqgh2h6urnppaik3zo7ywi"); //     } else
UNSUPPORTED("8d9xfgejx5vgd6shva5wk5k06"); // 	return -1;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}


}
