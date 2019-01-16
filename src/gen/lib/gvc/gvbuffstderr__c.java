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
package gen.lib.gvc;
import static smetana.core.Macro.UNSUPPORTED;

public class gvbuffstderr__c {


//3 8e1sa6f5ejc0r49gfiuv32aul
// void *buffstderr(void) 
public static Object buffstderr(Object... arg) {
UNSUPPORTED("bel1uzbdqhzbiuqj5bt2lxzqq"); // void *buffstderr(void)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("aexhdud6z2wbwwi73yppp0ynl"); //     char *p;
UNSUPPORTED("38cr9skhy4jrzhg3wj3bc20yu"); //     struct buff_s *b;
UNSUPPORTED("28m0s1mo38itho1au29oy5rv9"); //     assert ((b = malloc(sizeof(struct buff_s))));
UNSUPPORTED("6rlbco8yzervnvmq2m0r611c1"); //     assert ((b->template = strdup("/tmp/stderr_buffer_XXXXXX")));
UNSUPPORTED("bcqwp3my2rhtv5cf0oviy8odz"); //     assert ((p = mktemp(b->template)));
UNSUPPORTED("aw4nnvbir9iy1nuq4aukql0de"); //     fflush(stderr);
UNSUPPORTED("62lg711j2rddmpadkk7qhn7o7"); //     fgetpos(stderr, &(b->pos));
UNSUPPORTED("1eysolcc2x8f6m0otum5r2501"); //     b->fd = dup(fileno(stderr));
UNSUPPORTED("6hw12z5dqv9hheh1gxxpmwey0"); //     freopen(b->template, "w+", stderr);
UNSUPPORTED("2d461iz08h1aqinzmgs197s94"); //     return (void *)b;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 c9716hxgrfzvnokv7hw8islwa
// char *unbuffstderr(struct buff_s *b) 
public static Object unbuffstderr(Object... arg) {
UNSUPPORTED("evux4jdjx4b2zi15ma1m76h5d"); // char *unbuffstderr(struct buff_s *b)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("eywfpf1qo3rooyuerxrc41h5p"); //     long sz;
UNSUPPORTED("71fkgwtjx5c4orpsdy7jkyr86"); //     char *t = NULL;
UNSUPPORTED("3al75fenupx704zal37z61c8b"); //     FILE *f;
UNSUPPORTED("aw4nnvbir9iy1nuq4aukql0de"); //     fflush(stderr);
UNSUPPORTED("2s4h3spdm3ti16pnbbiw5wl7h"); //     sz = ftell(stderr);
UNSUPPORTED("sy0yol0bacbdtlzdr1qewlj9"); //     dup2(b->fd, fileno(stderr));
UNSUPPORTED("15iuj8qt4zyy6idhhvkhw7kyy"); //     close(b->fd);
UNSUPPORTED("6vd38rt8w7l0uxuayeny4pb4"); //     clearerr(stderr);
UNSUPPORTED("elgraii0bqskacqp0kwicgtfj"); //     fsetpos(stderr, &(b->pos));
UNSUPPORTED("9zb4lfjglf6mq4qwh8w8tysh9"); //     if (sz) {
UNSUPPORTED("61z9k4t8590rqr4gbjt7udecr"); //         /* stderr has been restored; these asserts use normal stderr output */
UNSUPPORTED("2mznswy9uou17ncks1p4of2f7"); //         assert((t = malloc(sz+1)));
UNSUPPORTED("4vbq5cvor2on52t1v9dbok91h"); //         assert((f = fopen(b->template, "r")));
UNSUPPORTED("8s2fk8360i1yh41rn73tpnhj"); //         assert( fread(t, 1, sz, f) == sz);
UNSUPPORTED("dk3j3n9fv8k10vlyl1mk71gbf"); //         fclose(f);
UNSUPPORTED("7w549vhurh491724lsxzvdx0f"); //         t[sz]='\0';
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("1iabjly523zdwdwqm9s4pz5vc"); //     unlink(b->template);
UNSUPPORTED("1xawfl1gbhijvti6c9mg6k3bi"); //     free(b->template);
UNSUPPORTED("am24176ien70yg0oq7iticngs"); //     free(b);
UNSUPPORTED("6lv6zglsw62ek6eoecn583mc9"); //     return t;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}


}
