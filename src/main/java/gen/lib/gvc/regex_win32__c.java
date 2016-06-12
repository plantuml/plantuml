/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * Project Info:  http://plantuml.com
 * 
 * This file is part of Smetana.
 * Smetana is a partial translation of Graphviz/Dot sources from C to Java.
 *
 * (C) Copyright 2009-2017, Arnaud Roques
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

public class regex_win32__c {
//1 11gt8s6kgcau21nr404v703de
// static char re_syntax_table[256]




//3 70j25rmarcwvzsh361zahv8zo
// static void init_syntax_once () 
public static Object init_syntax_once(Object... arg) {
UNSUPPORTED("e2z2o5ybnr5tgpkt8ty7hwan1"); // static void
UNSUPPORTED("efoxnnabz2ejqjq7yhilggwkl"); // init_syntax_once ()
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("ch8iyg809c0lyd0bc1ytqve9u"); //     register int c;
UNSUPPORTED("e5h3t56qntljcivxieh5a2mkl"); //     static int done = 0;
UNSUPPORTED("97t7ymgg630qac6285iprjw9v"); //     if (done)
UNSUPPORTED("z22f11bf96kkfe4raxc9v85x"); //         return;
UNSUPPORTED("9t38w19yl5jyjw7oodb9irryl"); //     bzero (re_syntax_table, sizeof re_syntax_table);
UNSUPPORTED("2rmhh76mmq03za3zgh7h64oxo"); //     for (c = 'a'; c <= 'z'; c++)
UNSUPPORTED("c9iqf6ajwc71dgjf9wrkrmbc4"); //         re_syntax_table[c] = 1;
UNSUPPORTED("7su4oyfxw838uiaf1heiygrem"); //     for (c = 'A'; c <= 'Z'; c++)
UNSUPPORTED("c9iqf6ajwc71dgjf9wrkrmbc4"); //         re_syntax_table[c] = 1;
UNSUPPORTED("3204uyvac8xoymt69hx2jb6xy"); //     for (c = '0'; c <= '9'; c++)
UNSUPPORTED("c9iqf6ajwc71dgjf9wrkrmbc4"); //         re_syntax_table[c] = 1;
UNSUPPORTED("bh45yjprswi5um0lyt9x4oony"); //     re_syntax_table['_'] = 1;
UNSUPPORTED("2py1y9xsuhv2b6cmhyzmcm7c5"); //     done = 1;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}


//1 cnkdb29e6yd10874mh5xc3onb
// typedef long int s_reg_t


//1 112kj8zza71a63rts3ariqovo
// typedef unsigned long int active_reg_t


//1 5mb2m65nd4m3w2de4v3md7o7m
// typedef unsigned long int reg_syntax_t


//1 289hb08yhe1j84wgbo0riknbd
// extern reg_syntax_t re_syntax_options


//1 3tctp51mbz3u1lyv5hope3jn4
// typedef int regoff_t


//1 5bxw12pxo297kjn20mqjfmb86
// reg_syntax_t re_syntax_options




//3 cxgvt7ypgdfqf2pew3m9adw9i
// reg_syntax_t re_set_syntax(reg_syntax_t syntax) 
public static Object re_set_syntax(Object... arg) {
UNSUPPORTED("1jmrr8fkrd2tsxzkjzybt7zfh"); // reg_syntax_t
UNSUPPORTED("54rdixdzkwy1r5i8yfr9pfenx"); // re_set_syntax(reg_syntax_t syntax)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("8mpejiyi92887r7wji6mys7nq"); //     reg_syntax_t ret = re_syntax_options;
UNSUPPORTED("4kgq73hxpobga7t16qgh5znpe"); //     re_syntax_options = syntax;
UNSUPPORTED("f3b7mj138albdr4lodyomke0z"); //     return ret;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}


//1 4vl073wk5uvf75of82q0v5yyo
// static const char *re_error_msgid[] = 


//1 1nsczrbp8olzcxwsty8y22p1n
// static int re_max_failures = 20000


//1 bktlq1f2qk5dg76humjskteaa
// static char reg_unset_dummy


//1 1lj2324om1rijb97q9h9lxrhv
// typedef unsigned regnum_t


//1 e3zkpgg5cfqoq1zcfbcl2p5au
// typedef long pattern_offset_t




//3 b8oct2odjhnzacejd6swmozqo
// static reg_errcode_t regex_compile (const char *pattern,                size_t size,                reg_syntax_t syntax,                struct re_pattern_buffer *bufp) 
public static Object regex_compile(Object... arg) {
UNSUPPORTED("9aerjmw5n52ektwk47t1r9bh0"); // static reg_errcode_t
UNSUPPORTED("d6mg3ls5pg233p4i2fdv7ex1n"); // regex_compile (const char *pattern,
UNSUPPORTED("6t71eiupt12wdbfduq8n5i2sz"); //                size_t size,
UNSUPPORTED("rg73gczoqylzcinp4bt2p1bw"); //                reg_syntax_t syntax,
UNSUPPORTED("86tpwausdjloqigvxe0g3anqv"); //                struct re_pattern_buffer *bufp)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("7mnubzvdt62l8vdiefqprs10u"); //     /* We fetch characters from PATTERN here.  Even though PATTERN is
UNSUPPORTED("a1g4ovz742smqh486dhwytetq"); //        `char *' (i.e., signed), we declare these variables as unsigned, so
UNSUPPORTED("6yz29a8qermrzbhvqskuno14k"); //        they can be reliably used as array indices.  */
UNSUPPORTED("9zgwtw1nsbdz6hcmyfrgkt63h"); //     register unsigned char c, c1;
UNSUPPORTED("4z9j463uvcrdj4sj8xj9ye9py"); //     /* A random temporary spot in PATTERN.  */
UNSUPPORTED("7ccacvizh7wnu163gfkc81hu9"); //     const char *p1;
UNSUPPORTED("a293r2jyasseioor4io0pp504"); //     /* Points to the end of the buffer, where we should append.  */
UNSUPPORTED("5o1tyy96cneoiw799jnb2n7ke"); //     register unsigned char *b;
UNSUPPORTED("cb629v9wydhjaoevjdzc89oy7"); //     /* Keeps track of unclosed groups.  */
UNSUPPORTED("dgr9dbgbyc1ook38b1euoeouv"); //     compile_stack_type compile_stack;
UNSUPPORTED("39kpylizmi208i8v5yv2jhjp1"); //     /* Points to the current (ending) position in the pattern.  */
UNSUPPORTED("9mu7yzuommm5kljxb28ceesow"); //     const char *p = pattern;
UNSUPPORTED("9ok1q4b2nv6m90q14x6i0k0vm"); //     const char *pend = pattern + size;
UNSUPPORTED("3oga1grlge5vw6xitb7bzcjw9"); //   /* How to translate the characters in the pattern.  */
UNSUPPORTED("b6qhkmvrybk31a74eyxl9sf73"); //     char * translate = bufp->translate;
UNSUPPORTED("34h9x8riezst40pxo4ww0500r"); //     /* Address of the count-byte of the most recently inserted `exactn'
UNSUPPORTED("a8lirzoby6qul9ds74xai8gbs"); //      command.  This makes it possible to tell if a new exact-match
UNSUPPORTED("6w2dibeuk4g54m7pwlej7e8g8"); //      character can be added to that command or if the character requires
UNSUPPORTED("atv0mvbd2maopv2ing97xuaph"); //      a new `exactn' command.  */
UNSUPPORTED("s22nw5te3tpx7wkcpookrrve"); //     unsigned char *pending_exact = 0;
UNSUPPORTED("er4ntnybigvcvnva5ls2gkx9f"); //     /* Address of start of the most recently finished expression.
UNSUPPORTED("d9z7cp97wfgynilwmgxdn4w5p"); //      This tells, e.g., postfix * where to find the start of its
UNSUPPORTED("cwisfez4xccc9t277piikcz7u"); //      operand.  Reset at the beginning of groups and alternatives.  */
UNSUPPORTED("5319mhfghffljm52aipkegz7i"); //     unsigned char *laststart = 0;
UNSUPPORTED("asyqn8tnmhbn0u8yih4eowlsv"); //     /* Address of beginning of regexp, or inside of last group.  */
UNSUPPORTED("2prv77rl1a0ic6cloa505n74"); //     unsigned char *begalt;
UNSUPPORTED("1mpmicgb0aqvs4efkuf1423bb"); //     /* Place in the uncompiled pattern (i.e., the {) to
UNSUPPORTED("6vlkvivbip6585ngqifn0yut3"); //      which to go back if the interval is invalid.  */
UNSUPPORTED("dhc5wj4xmde53jd8j5mmpspo2"); //     const char *beg_interval;
UNSUPPORTED("3gbwrgdnq67nw2ais7b1oaood"); //     /* Address of the place where a forward jump should go to the end of
UNSUPPORTED("39t3zafmxkdw0y4k5jpg705xl"); //      the containing expression.  Each alternative of an `or' -- except the
UNSUPPORTED("7ax9rzx6regnwitqwud6fas5h"); //      last -- ends with a forward jump of this sort.  */
UNSUPPORTED("c8bejewn623onq21mfqd22e2w"); //     unsigned char *fixup_alt_jump = 0;
UNSUPPORTED("a0p6z20zonf6yhyd6u7i62l1f"); //     /* Counts open-groups as they are encountered.  Remembered for the
UNSUPPORTED("f3mw4fx5ayqshia0u0wfwq612"); //      matching close-group on the compile stack, so the same register
UNSUPPORTED("albygnk4x6lv0oonnbwcx9zse"); //      number is put in the stop_memory as the start_memory.  */
UNSUPPORTED("1jexufhhoiubt850z3ndj8ej5"); //     regnum_t regnum = 0;
UNSUPPORTED("7481cz4i3vyo797cewvok0k4c"); //     /* Initialize the compile stack.  */
UNSUPPORTED("ao1ytyamhvicfeyzqsidalxsk"); //     compile_stack.stack = ((compile_stack_elt_t *) malloc ((32) * sizeof (compile_stack_elt_t)));
UNSUPPORTED("3pmfpvmkfjp9kx78cvcxd6cpy"); //     if (compile_stack.stack == (void *)0)
UNSUPPORTED("ex5zj292r0bm8ons2oenc7d53"); //         return REG_ESPACE;
UNSUPPORTED("6rth766rpftr2lf9tsj3ipcy3"); //     compile_stack.size = 32;
UNSUPPORTED("7l76n0edqpze4bfgwt0jnlr23"); //     compile_stack.avail = 0;
UNSUPPORTED("4d5adh0083zve5yjpmlxli8h1"); //   /* Initialize the pattern buffer.  */
UNSUPPORTED("6qgussdbr9ruavpzcr0y58ts9"); //     bufp->syntax = syntax;
UNSUPPORTED("c9tm2xzkehz9kbyrn2bkv3dwg"); //     bufp->fastmap_accurate = 0;
UNSUPPORTED("91jbv9it8qe6sah0sxn6nd4w2"); //     bufp->not_bol = bufp->not_eol = 0;
UNSUPPORTED("3rafz8q4yl9qbchg80xvodlsy"); //     /* Set `used' to zero, so that if we return an error, the pattern
UNSUPPORTED("b6wnwmphpmnza6rdw0x8yy5kj"); //      printer (for debugging) will think there's no pattern.  We reset it
UNSUPPORTED("8xe4i97w2l1mmth54di1u7rrd"); //      at the end.  */
UNSUPPORTED("b65vrlaj7a5mdhjx1m4vm2kzu"); //     bufp->used = 0;
UNSUPPORTED("5v5ffm48jpbjz5t7q75z95oqu"); //     /* Always count groups, whether or not bufp->no_sub is set.  */
UNSUPPORTED("fcycoe4nhtup93j74aqh2jlu"); //     bufp->re_nsub = 0;
UNSUPPORTED("57cfyb08w4wnczh1o759ayl4l"); //     /* Initialize the syntax table.  */
UNSUPPORTED("eoiuv77ltbli38ssdpi2gptkk"); //     init_syntax_once ();
UNSUPPORTED("f0l0ry0lbt2gj40a13217dx11"); //     if (bufp->allocated == 0)
UNSUPPORTED("6pjalxixg8dnhbhc46pm6e6ay"); //         {
UNSUPPORTED("eblz85b36y9pboxm6zt66xr2h"); //             if (bufp->buffer)
UNSUPPORTED("53s3p7apfgsn3pkcwkqznue34"); //                 { /* If zero allocated, but buffer is non-null, try to realloc
UNSUPPORTED("3mwc7vljmgej2pox1thi77h1t"); //                      enough space.  This loses if buffer's address is bogus, but
UNSUPPORTED("2hni8mwtvtzlrz7iztjmo1gkm"); //                      that is the user's responsibility.  */
UNSUPPORTED("4soy7dr6bhuvmx7k9nk1d0ql8"); //                     ((bufp->buffer) = (unsigned char *) realloc (bufp->buffer, (32) * sizeof (unsigned char)));
UNSUPPORTED("7nxu74undh30brb8laojud3f9"); //                 }
UNSUPPORTED("1knjyao8ci3w18zqqcnmnitir"); //             else
UNSUPPORTED("17v9yg0xtevf2uu5v3tmolocu"); //                 { /* Caller did not allocate a buffer.  Do it for them.  */
UNSUPPORTED("6fravsvvcv922fixqgd9sfp3z"); //                     bufp->buffer = ((unsigned char *) malloc ((32) * sizeof (unsigned char)));
UNSUPPORTED("7nxu74undh30brb8laojud3f9"); //                 }
UNSUPPORTED("4as6xhocbf55x8cvv0z11to2x"); //             if (!bufp->buffer) return (free (compile_stack.stack), REG_ESPACE);
UNSUPPORTED("af932qywpz9q6n0xek5lztd84"); //             bufp->allocated = 32;
UNSUPPORTED("4mhlpjofolwivhm0tl8cxznly"); //         }
UNSUPPORTED("5mg602ef37gzyyfswwutbcqs8"); //     begalt = b = bufp->buffer;
UNSUPPORTED("89ht4djrhvh6xphi9py7k3pam"); //   /* Loop through the uncompiled pattern until we're at the end.  */
UNSUPPORTED("5r7butg5genmj9dzegcwao4et"); //     while (p != pend)
UNSUPPORTED("6pjalxixg8dnhbhc46pm6e6ay"); //         {
UNSUPPORTED("42vbjcv24l0oaozyfwaw4j5nc"); //             do {if (p == pend) return REG_EEND; c = (unsigned char) *p++; if (translate) c = (unsigned char) translate[c]; } while (0);
UNSUPPORTED("719kmsx403baae0d0ox2gyi3a"); //             switch (c)
UNSUPPORTED("9ua540u2gx5jpu302s81qfxhi"); //                 {
UNSUPPORTED("32y7yv66mqjhrdqaf9ck9oi4y"); //                 case '^':
UNSUPPORTED("4vdjxw5o61xlsk38ouw1wsypc"); //                     {
UNSUPPORTED("3j5jurrtqcxdphc3jlcg8lu39"); //                         if (   /* If at start of pattern, it's an operator.  */
UNSUPPORTED("acrtuah53gidq3uywocguoav"); //                             p == pattern + 1
UNSUPPORTED("bs7cbfp1wd9jknqlj749u3bj3"); //                             /* If context independent, it's an operator.  */
UNSUPPORTED("9sznx5vvd3orgqcisdh571wv9"); //                             || syntax & (((((unsigned long int) 1) << 1) << 1) << 1)
UNSUPPORTED("7gzhx2l4j1gaba7epz2kdwhlj"); //                             /* Otherwise, depends on what's come before.  */
UNSUPPORTED("10earmgj12izv0tbj9aimxmji"); //                             || at_begline_loc_p (pattern, p, syntax))
UNSUPPORTED("clssp7ivct1ku99qbhyb7dtzy"); //                             do { while ((unsigned long) (b - bufp->buffer + (1)) > bufp->allocated) do { unsigned char *old_buffer = bufp->buffer; if (bufp->allocated == (1L << 16)) return REG_ESIZE; bufp->allocated <<= 1; if (bufp->allocated > (1L << 16)) bufp->allocated = (1L << 16); bufp->buffer = (unsigned char *) realloc ((bufp->buffer), (bufp->allocated)); if (bufp->buffer == (void *)0) return REG_ESPACE; if (old_buffer != bufp->buffer) { b = (b - old_buffer) + bufp->buffer; begalt = (begalt - old_buffer) + bufp->buffer; if (fixup_alt_jump) fixup_alt_jump = (fixup_alt_jump - old_buffer) + bufp->buffer; if (laststart) laststart = (laststart - old_buffer) + bufp->buffer; if (pending_exact) pending_exact = (pending_exact - old_buffer) + bufp->buffer; } } while (0); *b++ = (unsigned char) (begline); } while (0);
UNSUPPORTED("euvqyl9ihnvv92dr2v2zshlo"); //                         else
UNSUPPORTED("dxpbgmfj2w220xy1q99fxje94"); //                             goto normal_char;
UNSUPPORTED("3e08x1y395304nd0y3uwffvim"); //                     }
UNSUPPORTED("ctqmerohp1f69mb1v1t20jx33"); //                     break;
UNSUPPORTED("9ogqrievi7oirs7cd4cs33egg"); //                 case '$':
UNSUPPORTED("4vdjxw5o61xlsk38ouw1wsypc"); //                     {
UNSUPPORTED("hai791a97htfukh6iawb46oz"); //                         if (   /* If at end of pattern, it's an operator.  */
UNSUPPORTED("a0h309uru3crtdmsxpji7m2j3"); //                             p == pend
UNSUPPORTED("bs7cbfp1wd9jknqlj749u3bj3"); //                             /* If context independent, it's an operator.  */
UNSUPPORTED("9sznx5vvd3orgqcisdh571wv9"); //                             || syntax & (((((unsigned long int) 1) << 1) << 1) << 1)
UNSUPPORTED("2iqqtdvj90ata5pvx2o27gw4s"); //                             /* Otherwise, depends on what's next.  */
UNSUPPORTED("67wgd1do640yncemi3ce0qj90"); //                             || at_endline_loc_p (p, pend, syntax))
UNSUPPORTED("35lv6vfpgmavt421g5ug4tdbc"); //                             do { while ((unsigned long) (b - bufp->buffer + (1)) > bufp->allocated) do { unsigned char *old_buffer = bufp->buffer; if (bufp->allocated == (1L << 16)) return REG_ESIZE; bufp->allocated <<= 1; if (bufp->allocated > (1L << 16)) bufp->allocated = (1L << 16); bufp->buffer = (unsigned char *) realloc ((bufp->buffer), (bufp->allocated)); if (bufp->buffer == (void *)0) return REG_ESPACE; if (old_buffer != bufp->buffer) { b = (b - old_buffer) + bufp->buffer; begalt = (begalt - old_buffer) + bufp->buffer; if (fixup_alt_jump) fixup_alt_jump = (fixup_alt_jump - old_buffer) + bufp->buffer; if (laststart) laststart = (laststart - old_buffer) + bufp->buffer; if (pending_exact) pending_exact = (pending_exact - old_buffer) + bufp->buffer; } } while (0); *b++ = (unsigned char) (endline); } while (0);
UNSUPPORTED("euvqyl9ihnvv92dr2v2zshlo"); //                         else
UNSUPPORTED("dxpbgmfj2w220xy1q99fxje94"); //                             goto normal_char;
UNSUPPORTED("3e08x1y395304nd0y3uwffvim"); //                     }
UNSUPPORTED("ctqmerohp1f69mb1v1t20jx33"); //                     break;
UNSUPPORTED("4yw8evd0fubkfck5nezds5dhn"); //                 case '+':
UNSUPPORTED("f0rooqc2e80a2plohrq4c3c91"); //                 case '?':
UNSUPPORTED("1ku9g8vhwccbudpz9e300f03z"); //                     if ((syntax & (((unsigned long int) 1) << 1))
UNSUPPORTED("7sxu3x1nkq0rp14piw14s336e"); //                         || (syntax & ((((((((((((unsigned long int) 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1)))
UNSUPPORTED("uivpjrr5me8j92jky5okan1c"); //                         goto normal_char;
UNSUPPORTED("ak9il2l94xwmflwso0qxuq07i"); //                 handle_plus:
UNSUPPORTED("77sjlj9q2m2icdnuxqgd896ux"); //                 case '*':
UNSUPPORTED("bog9pqkld0m0t6ypotbesbb2s"); //                     /* If there is no previous pattern... */
UNSUPPORTED("6horrl50ttv3j0vlv0d550qe5"); //                     if (!laststart)
UNSUPPORTED("5k2digv672hnrndhc9ktw0oii"); //                         {
UNSUPPORTED("8qgst65bgbkg69wykq7paitn0"); //                             if (syntax & (((((((unsigned long int) 1) << 1) << 1) << 1) << 1) << 1))
UNSUPPORTED("4zp3pjt3o3sewewdrckac5e7t"); //                                 return (free (compile_stack.stack), REG_BADRPT);
UNSUPPORTED("2ji76vo0qsyt5ldqyext7tdsg"); //                             else if (!(syntax & ((((((unsigned long int) 1) << 1) << 1) << 1) << 1)))
UNSUPPORTED("ck4wdxm4o1ub3048vodmtvy6s"); //                                 goto normal_char;
UNSUPPORTED("b86ovw6olwwo6gnqlt1wqqzb4"); //                         }
UNSUPPORTED("4vdjxw5o61xlsk38ouw1wsypc"); //                     {
UNSUPPORTED("2vnh9foicn3ppcbpvb6hdon87"); //                         /* Are we optimizing this jump?  */
UNSUPPORTED("60ry57pj80rgxp87816o6vpy2"); //                         boolean keep_string_p = 0;
UNSUPPORTED("6qwgte4fwcf1xqfiuge2ssau3"); //                         /* 1 means zero (many) matches is allowed.  */
UNSUPPORTED("7z75b80h28y72skvw3v96x2y5"); //                         char zero_times_ok = 0, many_times_ok = 0;
UNSUPPORTED("a3pm3eb949njdi5ypawoqc46v"); //                         /* If there is a sequence of repetition chars, collapse it
UNSUPPORTED("dovcg85ak5c45w4j2nz1vphpt"); //                            down to just one (the right one).  We can't combine
UNSUPPORTED("enmqxu33e2oz4v7ad1h3regi1"); //                            interval operators with these because of, e.g., `a{2}*',
UNSUPPORTED("2ic453t1t5s0w63b328mvtw1h"); //                            which should only match an even number of `a's.  */
UNSUPPORTED("1nx31c51hb376ceydwsae35jq"); //                         for (;;)
UNSUPPORTED("9aq2ft2quyeattze0cwemwweo"); //                             {
UNSUPPORTED("1998skt4l5yp1v1sml3xtum0t"); //                                 zero_times_ok |= c != '+';
UNSUPPORTED("d4p0t1ujgqb32xcytuivs2m0m"); //                                 many_times_ok |= c != '?';
UNSUPPORTED("891uwttbfnv5bc58wpbg1fhgl"); //                                 if (p == pend)
UNSUPPORTED("1fg4xctmq8acy7m0oob45guaz"); //                                     break;
UNSUPPORTED("1ph6d2iv2trnwdx326iuetr4g"); //                                 do {if (p == pend) return REG_EEND; c = (unsigned char) *p++; if (translate) c = (unsigned char) translate[c]; } while (0);
UNSUPPORTED("do4ccpdwh7h97tg1nxsm8tt4d"); //                                 if (c == '*'
UNSUPPORTED("b9cokjueamyv69axw1ekph5qa"); //                                     || (!(syntax & (((unsigned long int) 1) << 1)) && (c == '+' || c == '?')))
UNSUPPORTED("c6tldeu5ffnykyfto7hi9ixau"); //                                     ;
UNSUPPORTED("4zyx7dzchkorh28bz90x76p2g"); //                                 else if (syntax & (((unsigned long int) 1) << 1)  &&  c == '\\')
UNSUPPORTED("3trws9zymzc11hz9w8m2b4fl9"); //                                     {
UNSUPPORTED("e9xhg2qnbm79uk0vrkf792u29"); //                                         if (p == pend) return (free (compile_stack.stack), REG_EESCAPE);
UNSUPPORTED("518hxi8xgn35zetedpma2oo2k"); //                                         do {if (p == pend) return REG_EEND; c1 = (unsigned char) *p++; if (translate) c1 = (unsigned char) translate[c1]; } while (0);
UNSUPPORTED("31364dtwzgpxty30b38zc4zkj"); //                                         if (!(c1 == '+' || c1 == '?'))
UNSUPPORTED("el767oi7fhlc4apdbx8dsvnk6"); //                                             {
UNSUPPORTED("2t22wkw5f3ffgm4ga51u5nrej"); //                                                 p--;
UNSUPPORTED("2t22wkw5f3ffgm4ga51u5nrej"); //                                                 p--;
UNSUPPORTED("brutz3uly6r5jyimug3eb5y9n"); //                                                 break;
UNSUPPORTED("b5m0ttlk23avgs4i47o8lbzg1"); //                                             }
UNSUPPORTED("e31jqqw74li41ohhn86f6dom7"); //                                         c = c1;
UNSUPPORTED("2v7ai112w6nt2xgs1gbzo22oe"); //                                     }
UNSUPPORTED("a4hbs6luulhltmygux2zimbcc"); //                                 else
UNSUPPORTED("3trws9zymzc11hz9w8m2b4fl9"); //                                     {
UNSUPPORTED("5cyajgw0lqe5reqenlh6er6vk"); //                                         p--;
UNSUPPORTED("7vckzntfuo1ibi8r53gsfg8al"); //                                         break;
UNSUPPORTED("2v7ai112w6nt2xgs1gbzo22oe"); //                                     }
UNSUPPORTED("5usbancvr06c7aua10b6rnz2v"); //                                 /* If we get here, we found another repeat character.  */
UNSUPPORTED("13jcwbk3vyfh9xrmwi5hbe7so"); //                             }
UNSUPPORTED("4z09n9ulcj74vli0m0kclo5i"); //                         /* Star, etc. applied to an empty pattern is equivalent
UNSUPPORTED("1ixkehs9qjp0ehix96jklbcfm"); //                            to an empty pattern.  */
UNSUPPORTED("60xmx571g6e7xq9uvs63uu1eq"); //                         if (!laststart)
UNSUPPORTED("7mosouhqcis2k8sbg88g9wol8"); //                             break;
UNSUPPORTED("hi54423z7c97klm7jyc2i54y"); //                         /* Now we know whether or not zero matches is allowed
UNSUPPORTED("cdhyalprzmam31a4ky61gdsu5"); //                            and also whether or not two or more matches is allowed.  */
UNSUPPORTED("82umz1mp8urx9qjjtqh65sk5"); //                         if (many_times_ok)
UNSUPPORTED("8omf20fupbos0ngb4nxaz3sj"); //                             { /* More than one repetition is allowed, so put in at the
UNSUPPORTED("6vwybrs5isd11ynby19imvgnc"); //                                  end a backward relative jump from `b' to before the next
UNSUPPORTED("7k3po89z1y12gwpxracwd1zwr"); //                                  jump we're going to put in below (which jumps from
UNSUPPORTED("497rlrbp2r6kokgq8fic5z8li"); //                                  laststart to after this jump).
UNSUPPORTED("elt3pujxvh1j9wbns2s7urafl"); //                                  But if we are at the `*' in the exact sequence `.*\n',
UNSUPPORTED("aq9r7pdk4mwsuo46v85xd7nle"); //                                  insert an unconditional jump backwards to the .,
UNSUPPORTED("afees40xo77whl3qvfrsjg0p2"); //                                  instead of the beginning of the loop.  This way we only
UNSUPPORTED("2f6wchn2e63ok4vso04jpeozp"); //                                  push a failure point once, instead of every time
UNSUPPORTED("2mne6btvrwz6ly84kpb5k8fz2"); //                                  through the loop.  */
UNSUPPORTED("9fy78dg2q1uu4c2t1zsyaestf"); //                                 ;
UNSUPPORTED("7osfsgzoickbv6hh4uet35ib8"); //                                 /* Allocate the space for the jump.  */
UNSUPPORTED("38dw1m55ji4uptkiga2iwgab7"); //                                 while ((unsigned long) (b - bufp->buffer + (3)) > bufp->allocated) do { unsigned char *old_buffer = bufp->buffer; if (bufp->allocated == (1L << 16)) return REG_ESIZE; bufp->allocated <<= 1; if (bufp->allocated > (1L << 16)) bufp->allocated = (1L << 16); bufp->buffer = (unsigned char *) realloc ((bufp->buffer), (bufp->allocated)); if (bufp->buffer == (void *)0) return REG_ESPACE; if (old_buffer != bufp->buffer) { b = (b - old_buffer) + bufp->buffer; begalt = (begalt - old_buffer) + bufp->buffer; if (fixup_alt_jump) fixup_alt_jump = (fixup_alt_jump - old_buffer) + bufp->buffer; if (laststart) laststart = (laststart - old_buffer) + bufp->buffer; if (pending_exact) pending_exact = (pending_exact - old_buffer) + bufp->buffer; } } while (0);
UNSUPPORTED("2v0jxylpfaawr8vdl2dov0us5"); //                                 /* We know we are not at the first character of the pattern,
UNSUPPORTED("djrrtgjb76xh1l5pw5u4kxcnv"); //                                    because laststart was nonzero.  And we've already
UNSUPPORTED("5f4ly8ng2ytcbc7ypdbpa8e11"); //                                    incremented `p', by the way, to be the character after
UNSUPPORTED("4as3ltw89z2kch0mrty7q58ey"); //                                    the `*'.  Do we have to do something analogous here
UNSUPPORTED("d3grtqmpebse1zxznnwl3jm7c"); //                                    for null bytes, because of RE_DOT_NOT_NULL?  */
UNSUPPORTED("vbl73uentmynsl9r6evk8ulc"); //                                 if ((translate ? (char) translate[(unsigned char) (*(p - 2))] : (*(p - 2))) == (translate ? (char) translate[(unsigned char) ('.')] : ('.'))
UNSUPPORTED("3lbv5kv4tdnxzqobvqv6gz12l"); //                                     && zero_times_ok
UNSUPPORTED("1ykhfelz1q80iguwwtohwqvm2"); //                                     && p < pend && (translate ? (char) translate[(unsigned char) (*p)] : (*p)) == (translate ? (char) translate[(unsigned char) ('\n')] : ('\n'))
UNSUPPORTED("bxrp4x7jxoo38mvd9ft3k5db"); //                                     && !(syntax & ((((((((unsigned long int) 1) << 1) << 1) << 1) << 1) << 1) << 1)))
UNSUPPORTED("741cloh2pdrplimiiruq6t4ct"); //                                     { /* We have .*\n.  */
UNSUPPORTED("317e9s7jnbmgql8x4fdezgmv0"); //                                         store_op1 (jump, b, (int) ((laststart) - (b) - 3));
UNSUPPORTED("8ryrgqjl7v6ohb01o5em2psns"); //                                         keep_string_p = 1;
UNSUPPORTED("2v7ai112w6nt2xgs1gbzo22oe"); //                                     }
UNSUPPORTED("a4hbs6luulhltmygux2zimbcc"); //                                 else
UNSUPPORTED("3ksp6xs2pd8hkq033e06g4a00"); //                                     /* Anything else.  */
UNSUPPORTED("4ab5zrwtdkltgbt8onvdet2r1"); //                                     store_op1 (maybe_pop_jump, b, (int) ((laststart - 3) - (b) - 3));
UNSUPPORTED("f4gsd1otghvhc8czjmw8qq776"); //                                 /* We've added more stuff to the buffer.  */
UNSUPPORTED("7hdmsj9mi4jwnv83ar1ez4kwn"); //                                 b += 3;
UNSUPPORTED("13jcwbk3vyfh9xrmwi5hbe7so"); //                             }
UNSUPPORTED("7htghz4zhsf1uqpwp6ytvbhih"); //                         /* On failure, jump from laststart to b + 3, which will be the
UNSUPPORTED("c630s54imd7p1b0raogcspf6p"); //                            end of the buffer after this jump is inserted.  */
UNSUPPORTED("5i16nim58sq8mvfckzqrj7q7e"); //                         while ((unsigned long) (b - bufp->buffer + (3)) > bufp->allocated) do { unsigned char *old_buffer = bufp->buffer; if (bufp->allocated == (1L << 16)) return REG_ESIZE; bufp->allocated <<= 1; if (bufp->allocated > (1L << 16)) bufp->allocated = (1L << 16); bufp->buffer = (unsigned char *) realloc ((bufp->buffer), (bufp->allocated)); if (bufp->buffer == (void *)0) return REG_ESPACE; if (old_buffer != bufp->buffer) { b = (b - old_buffer) + bufp->buffer; begalt = (begalt - old_buffer) + bufp->buffer; if (fixup_alt_jump) fixup_alt_jump = (fixup_alt_jump - old_buffer) + bufp->buffer; if (laststart) laststart = (laststart - old_buffer) + bufp->buffer; if (pending_exact) pending_exact = (pending_exact - old_buffer) + bufp->buffer; } } while (0);
UNSUPPORTED("e1gqwwg06ek9zwsxdqko22t8"); //                         insert_op1 (keep_string_p ? on_failure_keep_string_jump
UNSUPPORTED("od51xm1io9lvsdmwr60fm9nx"); //  : on_failure_jump, 
UNSUPPORTED("demgpwduvdu6c55lzwftle76m"); //  laststart, (int) ((b + 3) - (
UNSUPPORTED("a887hw7h3lz3jofwz10uxjcdp"); //  laststart) - 3), b);
UNSUPPORTED("1t3kkww9qyeskw70p028ll4r1"); //                         pending_exact = 0;
UNSUPPORTED("1jpgahganw1gip7gnwn5n2vx6"); //                         b += 3;
UNSUPPORTED("38n9qcy0vfr2upedrm1d6by5o"); //                         if (!zero_times_ok)
UNSUPPORTED("9aq2ft2quyeattze0cwemwweo"); //                             {
UNSUPPORTED("15rx6swjp6qikvp0jmiy5rm4m"); //                                 /* At least one repetition is required, so insert a
UNSUPPORTED("5xq4uy1msul1zhp9ptz49bbvn"); //                                    `dummy_failure_jump' before the initial
UNSUPPORTED("3q62p85szhotzr22iijxb1lzh"); //                                    `on_failure_jump' instruction of the loop. This
UNSUPPORTED("5ukburrf5iegq4x208czcfxp7"); //                                    effects a skip over that instruction the first time
UNSUPPORTED("9zr6wiepyct1g2798ohlu821l"); //                                    we hit that loop.  */
UNSUPPORTED("38dw1m55ji4uptkiga2iwgab7"); //                                 while ((unsigned long) (b - bufp->buffer + (3)) > bufp->allocated) do { unsigned char *old_buffer = bufp->buffer; if (bufp->allocated == (1L << 16)) return REG_ESIZE; bufp->allocated <<= 1; if (bufp->allocated > (1L << 16)) bufp->allocated = (1L << 16); bufp->buffer = (unsigned char *) realloc ((bufp->buffer), (bufp->allocated)); if (bufp->buffer == (void *)0) return REG_ESPACE; if (old_buffer != bufp->buffer) { b = (b - old_buffer) + bufp->buffer; begalt = (begalt - old_buffer) + bufp->buffer; if (fixup_alt_jump) fixup_alt_jump = (fixup_alt_jump - old_buffer) + bufp->buffer; if (laststart) laststart = (laststart - old_buffer) + bufp->buffer; if (pending_exact) pending_exact = (pending_exact - old_buffer) + bufp->buffer; } } while (0);
UNSUPPORTED("caftp211etknfq81tggr97kq3"); //                                 insert_op1 (dummy_failure_jump, laststart, (int) ((laststart + 6) - (laststart) - 3), b);
UNSUPPORTED("7hdmsj9mi4jwnv83ar1ez4kwn"); //                                 b += 3;
UNSUPPORTED("13jcwbk3vyfh9xrmwi5hbe7so"); //                             }
UNSUPPORTED("3e08x1y395304nd0y3uwffvim"); //                     }
UNSUPPORTED("ctqmerohp1f69mb1v1t20jx33"); //                     break;
UNSUPPORTED("a2evu0k4b91gqn1jmn3a0jwzw"); //                 case '.':
UNSUPPORTED("5nlf7z9fx92ejvc0jjc27l3ks"); //                     laststart = b;
UNSUPPORTED("14r433r50a5mz9kdbq4siwjqy"); //                     do { while ((unsigned long) (b - bufp->buffer + (1)) > bufp->allocated) do { unsigned char *old_buffer = bufp->buffer; if (bufp->allocated == (1L << 16)) return REG_ESIZE; bufp->allocated <<= 1; if (bufp->allocated > (1L << 16)) bufp->allocated = (1L << 16); bufp->buffer = (unsigned char *) realloc ((bufp->buffer), (bufp->allocated)); if (bufp->buffer == (void *)0) return REG_ESPACE; if (old_buffer != bufp->buffer) { b = (b - old_buffer) + bufp->buffer; begalt = (begalt - old_buffer) + bufp->buffer; if (fixup_alt_jump) fixup_alt_jump = (fixup_alt_jump - old_buffer) + bufp->buffer; if (laststart) laststart = (laststart - old_buffer) + bufp->buffer; if (pending_exact) pending_exact = (pending_exact - old_buffer) + bufp->buffer; } } while (0); *b++ = (unsigned char) (anychar); } while (0);
UNSUPPORTED("ctqmerohp1f69mb1v1t20jx33"); //                     break;
UNSUPPORTED("7r93xgyv26x925aap3ia8ymon"); //                 case '[':
UNSUPPORTED("4vdjxw5o61xlsk38ouw1wsypc"); //                     {
UNSUPPORTED("2dyj1s6d9ag4nvt1f1crrdjfn"); //                         boolean had_char_class = 0;
UNSUPPORTED("4a3jhscbb3cd0w52yk8g7bkhm"); //                         if (p == pend) return (free (compile_stack.stack), REG_EBRACK);
UNSUPPORTED("18lthbap52pmrc5qc0gdkqazi"); //                         /* Ensure that we have enough space to push a charset: the
UNSUPPORTED("64im3as1hhwe4tfm27p855icz"); //                            opcode, the length count, and the bitset; 34 bytes in all.  */
UNSUPPORTED("dtfdonlqdk0z7gsn1s36st33w"); //                         while ((unsigned long) (b - bufp->buffer + (34)) > bufp->allocated) do { unsigned char *old_buffer = bufp->buffer; if (bufp->allocated == (1L << 16)) return REG_ESIZE; bufp->allocated <<= 1; if (bufp->allocated > (1L << 16)) bufp->allocated = (1L << 16); bufp->buffer = (unsigned char *) realloc ((bufp->buffer), (bufp->allocated)); if (bufp->buffer == (void *)0) return REG_ESPACE; if (old_buffer != bufp->buffer) { b = (b - old_buffer) + bufp->buffer; begalt = (begalt - old_buffer) + bufp->buffer; if (fixup_alt_jump) fixup_alt_jump = (fixup_alt_jump - old_buffer) + bufp->buffer; if (laststart) laststart = (laststart - old_buffer) + bufp->buffer; if (pending_exact) pending_exact = (pending_exact - old_buffer) + bufp->buffer; } } while (0);
UNSUPPORTED("8dx5294ey8snj5idlltwvrhb1"); //                         laststart = b;
UNSUPPORTED("610aa188uk96qcj24orwjx8fs"); //                         /* We test `*p == '^' twice, instead of using an if
UNSUPPORTED("5bc7brntk24ooxfmk1srxj8cb"); //                            statement, so we only need one BUF_PUSH.  */
UNSUPPORTED("f50kr5oskb25svx2g868njm8n"); //                         do { while ((unsigned long) (b - bufp->buffer + (1)) > bufp->allocated) do { unsigned char *old_buffer = bufp->buffer; if (bufp->allocated == (1L << 16)) return REG_ESIZE; bufp->allocated <<= 1; if (bufp->allocated > (1L << 16)) bufp->allocated = (1L << 16); bufp->buffer = (unsigned char *) realloc ((bufp->buffer), (bufp->allocated)); if (bufp->buffer == (void *)0) return REG_ESPACE; if (old_buffer != bufp->buffer) { b = (b - old_buffer) + bufp->buffer; begalt = (begalt - old_buffer) + bufp->buffer; if (fixup_alt_jump) fixup_alt_jump = (fixup_alt_jump - old_buffer) + bufp->buffer; if (laststart) laststart = (laststart - old_buffer) + bufp->buffer; if (pending_exact) pending_exact = (pending_exact - old_buffer) + bufp->buffer; } } while (0); *b++ = (unsigned char) (*p == '^' ? charset_not : charset); } while (0);
UNSUPPORTED("8evba7dkivr9l77jkvcmqcwz8"); //                         if (*p == '^')
UNSUPPORTED("6qmpdja8bt371mk2seyaa2gbr"); //                             p++;
UNSUPPORTED("7cikubv9dt5tpzkohonns5grc"); //                         /* Remember the first position in the bracket expression.  */
UNSUPPORTED("ams11hw6png742la6sel3atlf"); //                         p1 = p;
UNSUPPORTED("7xg1okqa5l3piclyijoek8ed8"); //                         /* Push the number of bytes in the bitmap.  */
UNSUPPORTED("attsgdtz0wo8uzzx0jzzakd9s"); //                         do { while ((unsigned long) (b - bufp->buffer + (1)) > bufp->allocated) do { unsigned char *old_buffer = bufp->buffer; if (bufp->allocated == (1L << 16)) return REG_ESIZE; bufp->allocated <<= 1; if (bufp->allocated > (1L << 16)) bufp->allocated = (1L << 16); bufp->buffer = (unsigned char *) realloc ((bufp->buffer), (bufp->allocated)); if (bufp->buffer == (void *)0) return REG_ESPACE; if (old_buffer != bufp->buffer) { b = (b - old_buffer) + bufp->buffer; begalt = (begalt - old_buffer) + bufp->buffer; if (fixup_alt_jump) fixup_alt_jump = (fixup_alt_jump - old_buffer) + bufp->buffer; if (laststart) laststart = (laststart - old_buffer) + bufp->buffer; if (pending_exact) pending_exact = (pending_exact - old_buffer) + bufp->buffer; } } while (0); *b++ = (unsigned char) ((1 << 8) / 8); } while (0);
UNSUPPORTED("9i1ubvjzgw51kylobwxvkoguf"); //                         /* Clear the whole map.  */
UNSUPPORTED("3yt3lfhbs0hx9w9ci87vt4p51"); //                         bzero (b, (1 << 8) / 8);
UNSUPPORTED("2m8299zx4gtxg4ncikd476gkx"); //                         /* charset_not matches newline according to a syntax bit.  */
UNSUPPORTED("16flegxkvnil6me4t0kb73hcj"); //                         if ((re_opcode_t) b[-2] == charset_not
UNSUPPORTED("44gymzta58w37g7hbxz29z8ht"); //                             && (syntax & ((((((((((unsigned long int) 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1)))
UNSUPPORTED("bm4wsfgshzu6e47pvkdqzgoz6"); //                             (b[((unsigned char) ('\n')) / 8] |= 1 << (((unsigned char) '\n') % 8));
UNSUPPORTED("2dle8bszohkrok9ivamd6wqu8"); //                         /* Read in characters and ranges, setting map bits.  */
UNSUPPORTED("1nx31c51hb376ceydwsae35jq"); //                         for (;;)
UNSUPPORTED("9aq2ft2quyeattze0cwemwweo"); //                             {
UNSUPPORTED("6jfb28a8r43ndwdevd7rrjlb2"); //                                 if (p == pend) return (free (compile_stack.stack), REG_EBRACK);
UNSUPPORTED("1ph6d2iv2trnwdx326iuetr4g"); //                                 do {if (p == pend) return REG_EEND; c = (unsigned char) *p++; if (translate) c = (unsigned char) translate[c]; } while (0);
UNSUPPORTED("djf8gjrarnydrvlu8tzndsd4t"); //                                 /* \ might escape characters inside [...] and [^...].  */
UNSUPPORTED("89w50n0xz0xx1wgf8ls0sce25"); //                                 if ((syntax & ((unsigned long int) 1)) && c == '\\')
UNSUPPORTED("3trws9zymzc11hz9w8m2b4fl9"); //                                     {
UNSUPPORTED("e9xhg2qnbm79uk0vrkf792u29"); //                                         if (p == pend) return (free (compile_stack.stack), REG_EESCAPE);
UNSUPPORTED("518hxi8xgn35zetedpma2oo2k"); //                                         do {if (p == pend) return REG_EEND; c1 = (unsigned char) *p++; if (translate) c1 = (unsigned char) translate[c1]; } while (0);
UNSUPPORTED("d1ssg2hoazmd76m91z40sp5sn"); //                                         (b[((unsigned char) (c1)) / 8] |= 1 << (((unsigned char) c1) % 8));
UNSUPPORTED("cke4ze78n6mw03utm8hpk0ozq"); //                                         continue;
UNSUPPORTED("2v7ai112w6nt2xgs1gbzo22oe"); //                                     }
UNSUPPORTED("bvll4h37ech1nvv3p44tc97ia"); //                                 /* Could be the end of the bracket expression.  If it's
UNSUPPORTED("464yveauwiujz5tkl76ikld18"); //                                    not (i.e., when the bracket expression is `[]' so
UNSUPPORTED("ekv9yerdywogeillz767k3yq9"); //                                    far), the ']' character bit gets set way below.  */
UNSUPPORTED("39qeut34k7f0lyjj4z495n6am"); //                                 if (c == ']' && p != p1 + 1)
UNSUPPORTED("1fg4xctmq8acy7m0oob45guaz"); //                                     break;
UNSUPPORTED("d3v48zs419s4mxk9yyv5xgccj"); //                                 /* Look ahead to see if it's a range when the last thing
UNSUPPORTED("65vqnx3pinl0sxqecc7937imc"); //                                    was a character class.  */
UNSUPPORTED("8dtiybchqrzjmzzltkvknsq96"); //                                 if (had_char_class && c == '-' && *p != ']')
UNSUPPORTED("8zurthmteo7b71btgvw1ke4rg"); //                                     return (free (compile_stack.stack), REG_ERANGE);
UNSUPPORTED("d3v48zs419s4mxk9yyv5xgccj"); //                                 /* Look ahead to see if it's a range when the last thing
UNSUPPORTED("5wt9bnagn7y3isqyb5kxh04s7"); //                                    was a character: if this is a hyphen not at the
UNSUPPORTED("342gxn589rm7wkuq6jbilt9fr"); //                                    beginning or the end of a list, then it's the range
UNSUPPORTED("8a0p71nhvq7jckckna5u4qr7t"); //                                    operator.  */
UNSUPPORTED("bfxjivoy25rc3bw7qrspkzv1k"); //                                 if (c == '-'
UNSUPPORTED("93mph3xo98uvv6qn8e4tbid15"); //                                     && !(p - 2 >= pattern && p[-2] == '[')
UNSUPPORTED("1ce2lptewofyiq5ojhwc5va79"); //                                     && !(p - 3 >= pattern && p[-3] == '[' && p[-2] == '^')
UNSUPPORTED("5e843qj23bor49sf7nbcpwezc"); //                                     && *p != ']')
UNSUPPORTED("3trws9zymzc11hz9w8m2b4fl9"); //                                     {
UNSUPPORTED("f3s9czjbpp43p2benwj88e4vz"); //                                         reg_errcode_t ret
UNSUPPORTED("bgzy0fkxhr0j88bt28d2oeo7m"); //                                             = compile_range (&p, pend, translate, syntax, b);
UNSUPPORTED("6to7cm3b6s0m5pujlkqqm3m2x"); //                                         if (ret != REG_NOERROR) return (free (compile_stack.stack), ret);
UNSUPPORTED("2v7ai112w6nt2xgs1gbzo22oe"); //                                     }
UNSUPPORTED("65gxrs7y9lu8v93z6nz75m4ck"); //                                 else if (p[0] == '-' && p[1] != ']')
UNSUPPORTED("5av67x62ybjftr7suvc3tfqay"); //                                     { /* This handles ranges made up of characters only.  */
UNSUPPORTED("cmimxguouw0qbcmhrejq3dnrm"); //                                         reg_errcode_t ret;
UNSUPPORTED("6th47hqx3651aqvqnkzfm9yhl"); //                                         /* Move past the `-'.  */
UNSUPPORTED("518hxi8xgn35zetedpma2oo2k"); //                                         do {if (p == pend) return REG_EEND; c1 = (unsigned char) *p++; if (translate) c1 = (unsigned char) translate[c1]; } while (0);
UNSUPPORTED("8tyv3mj3db8ezfohi5j249qg9"); //                                         ret = compile_range (&p, pend, translate, syntax, b);
UNSUPPORTED("6to7cm3b6s0m5pujlkqqm3m2x"); //                                         if (ret != REG_NOERROR) return (free (compile_stack.stack), ret);
UNSUPPORTED("2v7ai112w6nt2xgs1gbzo22oe"); //                                     }
UNSUPPORTED("60ee1e25m2s16fec1vpvlz4n7"); //                                 /* See if we're at the beginning of a possible character
UNSUPPORTED("5paafwhmjbew6wm1r82cmglhi"); //                                    class.  */
UNSUPPORTED("1fxr5b7xujuw4t8tdfwstlhb5"); //                                 else if (syntax & ((((unsigned long int) 1) << 1) << 1) && c == '[' && *p == ':')
UNSUPPORTED("c6rvznitpculxv2jbhy4ncxyq"); //                                     { /* Leave room for the null.  */
UNSUPPORTED("35kxjv2sfddffc4bqeyh4imoc"); //                                         char str[6 + 1];
UNSUPPORTED("eqri2rfe7jnz1m2rij615jaj1"); //                                         do {if (p == pend) return REG_EEND; c = (unsigned char) *p++; if (translate) c = (unsigned char) translate[c]; } while (0);
UNSUPPORTED("d4co4ddeumifnjey3eestnfzq"); //                                         c1 = 0;
UNSUPPORTED("4w2zfigedythaavjy46j6zdev"); //                                         /* If pattern is `[[:'.  */
UNSUPPORTED("7eef052f6ws02kwwd07t2gogt"); //                                         if (p == pend) return (free (compile_stack.stack), REG_EBRACK);
UNSUPPORTED("6rl79w31o26w7y9v243bzntd1"); //                                         for (;;)
UNSUPPORTED("el767oi7fhlc4apdbx8dsvnk6"); //                                             {
UNSUPPORTED("5rs5zp1k5smyekvir9pcnto9c"); //                                                 do {if (p == pend) return REG_EEND; c = (unsigned char) *p++; if (translate) c = (unsigned char) translate[c]; } while (0);
UNSUPPORTED("5epbd4u9xrfpj6miw9wkaw3as"); //                                                 if (c == ':' || c == ']' || p == pend
UNSUPPORTED("1xzx4vfazvv793wm3hh5e4ixz"); //                                                     || c1 == 6)
UNSUPPORTED("1lzsscwayr2ygeagix6ouorzi"); //                                                     break;
UNSUPPORTED("a6wcfy57u3wkrc8jiwpoxhutg"); //                                                 str[c1++] = c;
UNSUPPORTED("b5m0ttlk23avgs4i47o8lbzg1"); //                                             }
UNSUPPORTED("6hm7an9ruv18077h9go7vsgf4"); //                                         str[c1] = '\0';
UNSUPPORTED("bi9fgjbp9y3y2pyuu9tszhyox"); //                                         /* If isn't a word bracketed by `[:' and:`]':
UNSUPPORTED("7nvg1ljm6ad0h7g7s60ej9uca"); //                                            undo the ending character, the letters, and leave
UNSUPPORTED("2272ea1yqp1bpznxgbrxbp00w"); //                                            the leading `:' and `[' (but set bits for them).  */
UNSUPPORTED("5tqbx68w26ea5218a43fyowz3"); //                                         if (c == ':' && *p == ']')
UNSUPPORTED("el767oi7fhlc4apdbx8dsvnk6"); //                                             {
UNSUPPORTED("dicsp7memxlin76m5jtuhk6dv"); //                                                 int ch;
UNSUPPORTED("74i3rk9pefjb3y4b7o4aa2iq9"); //                                                 boolean is_alnum = ((strcmp (str, "alnum") == 0));
UNSUPPORTED("89wnjt7xu39ig5ibxfz800la7"); //                                                 boolean is_alpha = ((strcmp (str, "alpha") == 0));
UNSUPPORTED("70u6gm5ko26rbqgsh5w8iswjq"); //                                                 boolean is_blank = ((strcmp (str, "blank") == 0));
UNSUPPORTED("1aaohxjchuctfrlc9v29p77u8"); //                                                 boolean is_cntrl = ((strcmp (str, "cntrl") == 0));
UNSUPPORTED("74upuhtioo8extzco95rwsrj8"); //                                                 boolean is_digit = ((strcmp (str, "digit") == 0));
UNSUPPORTED("44vvyq4v0mo3kf1e60wcz5pq1"); //                                                 boolean is_graph = ((strcmp (str, "graph") == 0));
UNSUPPORTED("ampk1k6gw3p7tnpd7rlk9zpbh"); //                                                 boolean is_lower = ((strcmp (str, "lower") == 0));
UNSUPPORTED("dfcf36k8u7asts9g1790th87m"); //                                                 boolean is_print = ((strcmp (str, "print") == 0));
UNSUPPORTED("94toygdeo23h0nswi5ypjemuj"); //                                                 boolean is_punct = ((strcmp (str, "punct") == 0));
UNSUPPORTED("5gzado7yzz8iw3b7wmsnnv1c"); //                                                 boolean is_space = ((strcmp (str, "space") == 0));
UNSUPPORTED("brptc46jbde0ytz2cf57z0qj9"); //                                                 boolean is_upper = ((strcmp (str, "upper") == 0));
UNSUPPORTED("6y30d6k6j6v6mo0u34x84x7pi"); //                                                 boolean is_xdigit = ((strcmp (str, "xdigit") == 0));
UNSUPPORTED("5xbajy2nf78y44rt7wqi0ipvw"); //                                                 if (!(((strcmp (str, "alpha") == 0)) || ((strcmp (str, "upper") == 0)) || ((strcmp (str, "lower") == 0)) || ((strcmp (str, "digit") == 0)) || ((strcmp (str, "alnum") == 0)) || ((strcmp (str, "xdigit") == 0)) || ((strcmp (str, "space") == 0)) || ((strcmp (str, "print") == 0)) || ((strcmp (str, "punct") == 0)) || ((strcmp (str, "graph") == 0)) || ((strcmp (str, "cntrl") == 0)) || ((strcmp (str, "blank") == 0))))
UNSUPPORTED("9roq2o5jp44jioxvumhpzhasp"); //                                                     return (free (compile_stack.stack), REG_ECTYPE);
UNSUPPORTED("b01volng00k0jszlwguip1za2"); //                                                 /* Throw away the ] at the end of the character
UNSUPPORTED("6v4ad7tk28ej4z6x4p9zfn9ip"); //                                                    class.  */
UNSUPPORTED("5rs5zp1k5smyekvir9pcnto9c"); //                                                 do {if (p == pend) return REG_EEND; c = (unsigned char) *p++; if (translate) c = (unsigned char) translate[c]; } while (0);
UNSUPPORTED("ahygyv10li3nm82zryhpg9mbm"); //                                                 if (p == pend) return (free (compile_stack.stack), REG_EBRACK);
UNSUPPORTED("e0htyvo32yvtsowvtr07q7jcw"); //                                                 for (ch = 0; ch < 1 << 8; ch++)
UNSUPPORTED("ednrjrwumcyqj77ytrr2eszv"); //                                                     {
UNSUPPORTED("3k2ocusl5iq9ds3ppdqj68572"); //                                                         /* This was split into 3 if's to
UNSUPPORTED("9gbw8s1phg1eyew1sfpr5kp3f"); //                                                            avoid an arbitrary limit in some compiler.  */
UNSUPPORTED("2cntwsg5hxbdi0ckf9y8w0mnq"); //                                                         if (   (is_alnum  && (1 && isalnum (ch)))
UNSUPPORTED("1zgfij4s84eufjba2ozx3lkrk"); //                                                                || (is_alpha  && (1 && isalpha (ch)))
UNSUPPORTED("bqa0xuagje3ikuybu56a5lsfa"); //                                                                || (is_blank  && ((ch) == ' ' || (ch) == '\t'))
UNSUPPORTED("dnw7neodbiod4p16f06qsugzl"); //                                                                || (is_cntrl  && (1 && iscntrl (ch))))
UNSUPPORTED("6qfn7xakyg09tnfa2ie1sh8p6"); //                                                             (b[((unsigned char) (ch)) / 8] |= 1 << (((unsigned char) ch) % 8));
UNSUPPORTED("6a1x5lr2tbh4dux8kqoyovq6z"); //                                                         if (   (is_digit  && (1 && isdigit (ch)))
UNSUPPORTED("6uh9v7mcu8jqpc8ab1xqdpupe"); //                                                                || (is_graph  && (1 && isprint (ch) && !isspace (ch)))
UNSUPPORTED("2kza3pwfg7co598ybwubyyjtb"); //                                                                || (is_lower  && (1 && islower (ch)))
UNSUPPORTED("2ve5o48kbom0zknpyyo4nrw8b"); //                                                                || (is_print  && (1 && isprint (ch))))
UNSUPPORTED("6qfn7xakyg09tnfa2ie1sh8p6"); //                                                             (b[((unsigned char) (ch)) / 8] |= 1 << (((unsigned char) ch) % 8));
UNSUPPORTED("eu7igk6hg1s58sklwzk61h4b7"); //                                                         if (   (is_punct  && (1 && ispunct (ch)))
UNSUPPORTED("eu3t6ngjxiw3bd3n428dpndsz"); //                                                                || (is_space  && (1 && isspace (ch)))
UNSUPPORTED("cyrylgd8tsf5lm7q40ozat1u6"); //                                                                || (is_upper  && (1 && isupper (ch)))
UNSUPPORTED("6uhsgqbeke0ipkactcwfponfm"); //                                                                || (is_xdigit && (1 && isxdigit (ch))))
UNSUPPORTED("6qfn7xakyg09tnfa2ie1sh8p6"); //                                                             (b[((unsigned char) (ch)) / 8] |= 1 << (((unsigned char) ch) % 8));
UNSUPPORTED("d10pfpemohjrojtw32b9u288j"); //                                                         if (   translate && (is_upper || is_lower)
UNSUPPORTED("dn91c7ek6aq14v8orwpui41nf"); //                                                                && ((1 && isupper (ch)) || (1 && islower (ch))))
UNSUPPORTED("6qfn7xakyg09tnfa2ie1sh8p6"); //                                                             (b[((unsigned char) (ch)) / 8] |= 1 << (((unsigned char) ch) % 8));
UNSUPPORTED("4mlmk54q1pd097btjgvhds4or"); //                                                     }
UNSUPPORTED("377au4sk1ya757gr7n0hqcmrn"); //                                                 had_char_class = 1;
UNSUPPORTED("b5m0ttlk23avgs4i47o8lbzg1"); //                                             }
UNSUPPORTED("61qf7b2jxc7j3knm5ts69f0b6"); //                                         else
UNSUPPORTED("el767oi7fhlc4apdbx8dsvnk6"); //                                             {
UNSUPPORTED("7m4co85wi9arl9dcdfqpb4za8"); //                                                 c1++;
UNSUPPORTED("20tgbjihyvtowact7dzcr1ecf"); //                                                 while (c1--)
UNSUPPORTED("8je1mldqzk080hkvqxenba16t"); //                                                     p--;
UNSUPPORTED("48kiij4wie0njh1vtga7it2dv"); //                                                 (b[((unsigned char) ('[')) / 8] |= 1 << (((unsigned char) '[') % 8));
UNSUPPORTED("dv206lne3atc9xm9yvrhv1ybg"); //                                                 (b[((unsigned char) (':')) / 8] |= 1 << (((unsigned char) ':') % 8));
UNSUPPORTED("41vc3rnsrwhzg40t2l6ddpjfb"); //                                                 had_char_class = 0;
UNSUPPORTED("b5m0ttlk23avgs4i47o8lbzg1"); //                                             }
UNSUPPORTED("2v7ai112w6nt2xgs1gbzo22oe"); //                                     }
UNSUPPORTED("a4hbs6luulhltmygux2zimbcc"); //                                 else
UNSUPPORTED("3trws9zymzc11hz9w8m2b4fl9"); //                                     {
UNSUPPORTED("nugbt1in6a1t1qaxqdyyi3ep"); //                                         had_char_class = 0;
UNSUPPORTED("17maz3hujafwurweozb151wdn"); //                                         (b[((unsigned char) (c)) / 8] |= 1 << (((unsigned char) c) % 8));
UNSUPPORTED("2v7ai112w6nt2xgs1gbzo22oe"); //                                     }
UNSUPPORTED("13jcwbk3vyfh9xrmwi5hbe7so"); //                             }
UNSUPPORTED("ehzhyhula77mysp4xro2dci8t"); //                         /* Discard any (non)matching list bytes that are all 0 at the
UNSUPPORTED("el3n9zylp6tlrb6d73napyvmy"); //                            end of the map.  Decrease the map-length byte too.  */
UNSUPPORTED("5aojv97eoyl5m04xfurgaavwm"); //                         while ((int) b[-1] > 0 && b[b[-1] - 1] == 0)
UNSUPPORTED("ajlocpcq3igxmb5gmsoqvv2es"); //                             b[-1]--;
UNSUPPORTED("bhvngkxhzl8kmmso1yya535k3"); //                         b += b[-1];
UNSUPPORTED("3e08x1y395304nd0y3uwffvim"); //                     }
UNSUPPORTED("ctqmerohp1f69mb1v1t20jx33"); //                     break;
UNSUPPORTED("6bh4kues0yl62d5yxc9atvjif"); //                 case '(':
UNSUPPORTED("10vjrts0i5hyw8tlbrt0z7rxn"); //                     if (syntax & (((((((((((((((unsigned long int) 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1))
UNSUPPORTED("bmyrcl5ximm9vy0tneijgrytk"); //                         goto handle_open;
UNSUPPORTED("cunk7vpvzj28y1x4gn62gxpce"); //                     else
UNSUPPORTED("uivpjrr5me8j92jky5okan1c"); //                         goto normal_char;
UNSUPPORTED("1e0dw1ejy35o3woadzbcsm7fr"); //                 case ')':
UNSUPPORTED("10vjrts0i5hyw8tlbrt0z7rxn"); //                     if (syntax & (((((((((((((((unsigned long int) 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1))
UNSUPPORTED("3s6h8cwqnf4sxe6ctgnhnea58"); //                         goto handle_close;
UNSUPPORTED("cunk7vpvzj28y1x4gn62gxpce"); //                     else
UNSUPPORTED("uivpjrr5me8j92jky5okan1c"); //                         goto normal_char;
UNSUPPORTED("e3rwmj3a70hs7pld4kwdx60mn"); //                 case '\n':
UNSUPPORTED("70gx6eoougt8rlinl56yw83xr"); //                     if (syntax & (((((((((((((unsigned long int) 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1))
UNSUPPORTED("5jivfhwm58xf3nhm2fe6mhe63"); //                         goto handle_alt;
UNSUPPORTED("cunk7vpvzj28y1x4gn62gxpce"); //                     else
UNSUPPORTED("uivpjrr5me8j92jky5okan1c"); //                         goto normal_char;
UNSUPPORTED("4alxh05h1fjl2sladyp35v6wj"); //                 case '|':
UNSUPPORTED("3torkuptfbkp3zjwrckz7pqdn"); //                     if (syntax & (((((((((((((((((unsigned long int) 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1))
UNSUPPORTED("5jivfhwm58xf3nhm2fe6mhe63"); //                         goto handle_alt;
UNSUPPORTED("cunk7vpvzj28y1x4gn62gxpce"); //                     else
UNSUPPORTED("uivpjrr5me8j92jky5okan1c"); //                         goto normal_char;
UNSUPPORTED("4pyfskl7wp2jd81x0kmp62qoi"); //                 case '{':
UNSUPPORTED("5b1d1n2on553ca86p9l993tzn"); //                     if (syntax & (((((((((((unsigned long int) 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) && syntax & ((((((((((((((unsigned long int) 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1))
UNSUPPORTED("48zoic9u273bq7o6wfz7y7fsv"); //                         goto handle_interval;
UNSUPPORTED("cunk7vpvzj28y1x4gn62gxpce"); //                     else
UNSUPPORTED("uivpjrr5me8j92jky5okan1c"); //                         goto normal_char;
UNSUPPORTED("5350k8qj9jz4pvuqs0weppxzm"); //                 case '\\':
UNSUPPORTED("drfl8x3xhp9nddd3popxg4kkf"); //                     if (p == pend) return (free (compile_stack.stack), REG_EESCAPE);
UNSUPPORTED("27v1be7swlc5n86wdyk7yqicm"); //                     /* Do not translate the character after the \, so that we can
UNSUPPORTED("aimxtldmdqlup4n4gcasbmjpm"); //                        distinguish, e.g., \B from \b, even if we normally would
UNSUPPORTED("8y750xv4fwoof20pgyb54iljp"); //                        translate, e.g., B to b.  */
UNSUPPORTED("emr80p3t30z3qcchdf6qsg7op"); //                     do {if (p == pend) return REG_EEND; c = (unsigned char) *p++; } while (0);
UNSUPPORTED("1wwqm41fujo96fngaibe11cs"); //                     switch (c)
UNSUPPORTED("5k2digv672hnrndhc9ktw0oii"); //                         {
UNSUPPORTED("8jxolljx883gvj5ry1lre79rm"); //                         case '(':
UNSUPPORTED("59nlrexduxa9d2pv4p8p4wfyn"); //                             if (syntax & (((((((((((((((unsigned long int) 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1))
UNSUPPORTED("duzsz5s9lw56m1on7ajxgk9o2"); //                                 goto normal_backslash;
UNSUPPORTED("1mo91efxfidrlkj7o2hpimm5g"); //                         handle_open:
UNSUPPORTED("cc2g9mgbjo7991epy9celv5dq"); //                             bufp->re_nsub++;
UNSUPPORTED("e816ua3kiygoarxwt4u3kjw2e"); //                             regnum++;
UNSUPPORTED("abfkotx8f2o71l3ungy2dc61b"); //                             if ((compile_stack.avail == compile_stack.size))
UNSUPPORTED("4hzaau620c4rh7xorkrmxfut7"); //                                 {
UNSUPPORTED("5r9ltvya89zzc88t9p8g86dxp"); //                                     ((compile_stack.stack) = (
UNSUPPORTED("5q6n9lfuwcrd4xf1g8vam0mqg"); //  compile_stack_elt_t *) realloc (compile_stack.stack, (compile_stack.size << 1) * sizeof (
UNSUPPORTED("2a7bsn9w2iinvqmd970g996fq"); //  compile_stack_elt_t)));
UNSUPPORTED("dsrcc4quas84hhfjjm5yh2be8"); //                                     if (compile_stack.stack == (void *)0) return REG_ESPACE;
UNSUPPORTED("8kq2kc3jebolynu1cy6403pl0"); //                                     compile_stack.size <<= 1;
UNSUPPORTED("2tfish0jog6m8uhlhaokmzvm3"); //                                 }
UNSUPPORTED("e0bq2fat5uym1sf4darkcwl7v"); //                             /* These are the values to restore when we hit end of this
UNSUPPORTED("3vpixvd7xctqmc3ngtezp4xh4"); //                                group.  They are all relative offsets, so that if the
UNSUPPORTED("2v6zztbhem2o09k7pqvggwe8h"); //                                whole pattern moves because of realloc, they will still
UNSUPPORTED("57di400em2riuffwpvny0f24u"); //                                be valid.  */
UNSUPPORTED("8rxnof37j5xup2c6z4diq79ou"); //                             (compile_stack.stack[compile_stack.avail]).begalt_offset = begalt - bufp->buffer;
UNSUPPORTED("9nay0qlhefwna3c6dilyk0ljp"); //                             (compile_stack.stack[compile_stack.avail]).fixup_alt_jump
UNSUPPORTED("aq269vu0tq0y8wvt7vg6y41u8"); //                                 = fixup_alt_jump ? fixup_alt_jump - bufp->buffer + 1 : 0;
UNSUPPORTED("f2a1p7nj50nhzemnvvv85l9wp"); //                             (compile_stack.stack[compile_stack.avail]).laststart_offset = b - bufp->buffer;
UNSUPPORTED("3ikd9wh2ibpe6fnfbwac6mal"); //                             (compile_stack.stack[compile_stack.avail]).regnum = regnum;
UNSUPPORTED("1222t3yuwlm1hv92xmzgstlpj"); //                             /* We will eventually replace the 0 with the number of
UNSUPPORTED("b5a63c42wobd53sp3c64zj34u"); //                                groups inner to this one.  But do not push a
UNSUPPORTED("1indupc2ufbt22ci1btaoi3w1"); //                                start_memory for groups beyond the last one we can
UNSUPPORTED("clbbjbwg9b4yr40r3qt26gayn"); //                                represent in the compiled pattern.  */
UNSUPPORTED("dn6qx81h224i04gen8lz36bur"); //                             if (regnum <= 255)
UNSUPPORTED("4hzaau620c4rh7xorkrmxfut7"); //                                 {
UNSUPPORTED("b55i0sl0dvhuj7r94cz8uascg"); //                                     (compile_stack.stack[compile_stack.avail]).inner_group_offset = b - bufp->buffer + 2;
UNSUPPORTED("crcx4g4ali1dijvt94wdz6nsm"); //                                     do { while ((unsigned long) (b - bufp->buffer + (3)) > bufp->allocated) do { unsigned char *old_buffer = bufp->buffer; if (bufp->allocated == (1L << 16)) return REG_ESIZE; bufp->allocated <<= 1; if (bufp->allocated > (1L << 16)) bufp->allocated = (1L << 16); bufp->buffer = (unsigned char *) realloc ((bufp->buffer), (bufp->allocated)); if (bufp->buffer == (void *)0) return REG_ESPACE; if (old_buffer != bufp->buffer) { b = (b - old_buffer) + bufp->buffer; begalt = (begalt - old_buffer) + bufp->buffer; if (fixup_alt_jump) fixup_alt_jump = (fixup_alt_jump - old_buffer) + bufp->buffer; if (laststart) laststart = (laststart - old_buffer) + bufp->buffer; if (pending_exact) pending_exact = (pending_exact - old_buffer) + bufp->buffer; } } while (0); *b++ = (unsigned char) (start_memory); *b++ = (unsigned char) (regnum); *b++ = (unsigned char) (0); } while (0);
UNSUPPORTED("2tfish0jog6m8uhlhaokmzvm3"); //                                 }
UNSUPPORTED("94rzmxep7enhqkfnp2762ytyy"); //                             compile_stack.avail++;
UNSUPPORTED("dphv9qka2txhy68rnsif6jynt"); //                             fixup_alt_jump = 0;
UNSUPPORTED("dvt6va09huijx10zufnnjqgl3"); //                             laststart = 0;
UNSUPPORTED("90zo4it529eaunfkfdxoxrdgj"); //                             begalt = b;
UNSUPPORTED("dvj2nuag66y7el3zrpla0153y"); //                             /* If we've reached MAX_REGNUM groups, then this open
UNSUPPORTED("2u4e034ze821746znru3h0b45"); //                                won't actually generate any code, so we'll have to
UNSUPPORTED("dk5vb9d5gkrw8sibofm4qt4ue"); //                                clear pending_exact explicitly.  */
UNSUPPORTED("7s8z4qnugzgnbuvds8u8a5jep"); //                             pending_exact = 0;
UNSUPPORTED("7mosouhqcis2k8sbg88g9wol8"); //                             break;
UNSUPPORTED("f58m8dl8wsiu09tv3k6hddsnz"); //                         case ')':
UNSUPPORTED("8rxf228e9g6ihw68iqgw5ye4v"); //                             if (syntax & (((((((((((((((unsigned long int) 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1)) goto normal_backslash;
UNSUPPORTED("7yogipyu9e9xkgapyvsso5js4"); //                             if ((compile_stack.avail == 0)) {
UNSUPPORTED("8jaoyupj1e28u8vqk14htwrtv"); //                                 if (syntax & (((((((((((((((((((unsigned long int) 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1))
UNSUPPORTED("7twsisd1xmsx52xuobbqhozvn"); //                                     goto normal_backslash;
UNSUPPORTED("a4hbs6luulhltmygux2zimbcc"); //                                 else
UNSUPPORTED("dn8ttvbrvj1svr73kt4zmcugv"); //                                     return (free (compile_stack.stack), REG_ERPAREN);
UNSUPPORTED("13jcwbk3vyfh9xrmwi5hbe7so"); //                             }
UNSUPPORTED("3bc4i0fs7quuiznm2wabuv57g"); //                         handle_close:
UNSUPPORTED("cba08qms5w8uqvkd4p9iqz909"); //                             if (fixup_alt_jump)
UNSUPPORTED("1lzprxcdmzqjb9whh02ptkgin"); //                                 { /* Push a dummy failure point at the end of the
UNSUPPORTED("cc7pak11xcdny8f782y93y6d4"); //                                      alternative for a possible future
UNSUPPORTED("4iprb2tgw3ra22d8s7u4ny04z"); //                                      `pop_failure_jump' to pop.  See comments at
UNSUPPORTED("il2pao7ylwdupdmwkc63wt74"); //                                      `push_dummy_failure' in `re_match_2'.  */
UNSUPPORTED("bsoqdb55d2fj7nrshha8baj79"); //                                     do { while ((unsigned long) (b - bufp->buffer + (1)) > bufp->allocated) do { unsigned char *old_buffer = bufp->buffer; if (bufp->allocated == (1L << 16)) return REG_ESIZE; bufp->allocated <<= 1; if (bufp->allocated > (1L << 16)) bufp->allocated = (1L << 16); bufp->buffer = (unsigned char *) realloc ((bufp->buffer), (bufp->allocated)); if (bufp->buffer == (void *)0) return REG_ESPACE; if (old_buffer != bufp->buffer) { b = (b - old_buffer) + bufp->buffer; begalt = (begalt - old_buffer) + bufp->buffer; if (fixup_alt_jump) fixup_alt_jump = (fixup_alt_jump - old_buffer) + bufp->buffer; if (laststart) laststart = (laststart - old_buffer) + bufp->buffer; if (pending_exact) pending_exact = (pending_exact - old_buffer) + bufp->buffer; } } while (0); *b++ = (unsigned char) (push_dummy_failure); } while (0);
UNSUPPORTED("bsg3cvulx3vuba3c4g7h8wi9e"); //                                     /* We allocated space for this jump when we assigned
UNSUPPORTED("81pcbxblcl9t5t7fqo3daz26"); //                                        to `fixup_alt_jump', in the `handle_alt' case below.  */
UNSUPPORTED("42dp50c2b5kexwd7wxmwewwe1"); //                                     store_op1 (jump_past_alt, fixup_alt_jump, (int) ((b - 1) - (fixup_alt_jump) - 3));
UNSUPPORTED("2tfish0jog6m8uhlhaokmzvm3"); //                                 }
UNSUPPORTED("172uzd85uuznm53m6t1nv9ke5"); //                             /* See similar code for backslashed left paren above.  */
UNSUPPORTED("7yogipyu9e9xkgapyvsso5js4"); //                             if ((compile_stack.avail == 0)) {
UNSUPPORTED("8jaoyupj1e28u8vqk14htwrtv"); //                                 if (syntax & (((((((((((((((((((unsigned long int) 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1))
UNSUPPORTED("6gqrsbcf35slq98fdu38ysotz"); //                                     goto normal_char;
UNSUPPORTED("a4hbs6luulhltmygux2zimbcc"); //                                 else
UNSUPPORTED("dn8ttvbrvj1svr73kt4zmcugv"); //                                     return (free (compile_stack.stack), REG_ERPAREN);
UNSUPPORTED("13jcwbk3vyfh9xrmwi5hbe7so"); //                             }
UNSUPPORTED("eniovbcxrkcn8texhpvd1c3j"); //                             /* Since we just checked for an empty stack above, this
UNSUPPORTED("oah0j3x85xy0y5snf99b5uto"); //                                ``can't happen''.  */
UNSUPPORTED("e4kt1tmmevqvy3cbyk6xnznck"); //                             ;
UNSUPPORTED("9aq2ft2quyeattze0cwemwweo"); //                             {
UNSUPPORTED("zxem7lefmt7jmq1sphx8jq5d"); //                                 /* We don't just want to restore into `regnum', because
UNSUPPORTED("eshff1bkln1euqcvo55z6arar"); //                                    later groups should continue to be numbered higher,
UNSUPPORTED("hyn2g413dbg1jtec9jsz3ujb"); //                                    as in `(ab)c(de)' -- the second group is #2.  */
UNSUPPORTED("e26xy4hqgvbad7pusqf04i868"); //                                 regnum_t this_group_regnum;
UNSUPPORTED("6n0r61042a9cp17p8uaa2pvkk"); //                                 compile_stack.avail--;
UNSUPPORTED("b8x4vi5eywvcxrfzoej5bwryf"); //                                 begalt = bufp->buffer + (compile_stack.stack[compile_stack.avail]).begalt_offset;
UNSUPPORTED("1xvm5ivpx5tz8e199a7p2rmqz"); //                                 fixup_alt_jump
UNSUPPORTED("4umag2hy9kw4kqozrwydhawfq"); //                                     = (compile_stack.stack[compile_stack.avail]).fixup_alt_jump
UNSUPPORTED("5gcmnf7zgwup5dbu5kddwwfq5"); //                                     ? bufp->buffer + (compile_stack.stack[compile_stack.avail]).fixup_alt_jump - 1
UNSUPPORTED("1gewh4lotwkkm4iqn6ggzwkoq"); //                                     : 0;
UNSUPPORTED("85k6r2lf00egr6wmh6ynrl2p5"); //                                 laststart = bufp->buffer + (compile_stack.stack[compile_stack.avail]).laststart_offset;
UNSUPPORTED("8hnnnyzrlgnojw4fgthz9m20m"); //                                 this_group_regnum = (compile_stack.stack[compile_stack.avail]).regnum;
UNSUPPORTED("dlko1f9w54peneqoca91cgrci"); //                                 /* If we've reached MAX_REGNUM groups, then this open
UNSUPPORTED("74txdb0o7dhsdiultkxutc0e7"); //                                    won't actually generate any code, so we'll have to
UNSUPPORTED("185wciv8bzl59e6kldksp04w4"); //                                    clear pending_exact explicitly.  */
UNSUPPORTED("47qf9noup192j3ixvbaqfafxw"); //                                 pending_exact = 0;
UNSUPPORTED("askuaehvoj7enatduzagqwmbk"); //                                 /* We're at the end of the group, so now we know how many
UNSUPPORTED("autf5q3thbf8mmptmgx0msnh4"); //                                    groups were inside this one.  */
UNSUPPORTED("a8riafizqdv5zjapabhopp72g"); //                                 if (this_group_regnum <= 255)
UNSUPPORTED("3trws9zymzc11hz9w8m2b4fl9"); //                                     {
UNSUPPORTED("2wmjb2xbz36ytg6g1bgfd4hb1"); //                                         unsigned char *inner_group_loc
UNSUPPORTED("bzfdj6g2ck20pq1mb50l27a9v"); //                                             = bufp->buffer + (compile_stack.stack[compile_stack.avail]).inner_group_offset;
UNSUPPORTED("el3pwrfnbqiexy2sk0rffy0cf"); //                                         *inner_group_loc = regnum - this_group_regnum;
UNSUPPORTED("cguv9rjc1iive3bv1yez75f03"); //                                         do { while ((unsigned long) (b - bufp->buffer + (3)) > bufp->allocated) do { unsigned char *old_buffer = bufp->buffer; if (bufp->allocated == (1L << 16)) return REG_ESIZE; bufp->allocated <<= 1; if (bufp->allocated > (1L << 16)) bufp->allocated = (1L << 16); bufp->buffer = (unsigned char *) realloc ((bufp->buffer), (bufp->allocated)); if (bufp->buffer == (void *)0) return REG_ESPACE; if (old_buffer != bufp->buffer) { b = (b - old_buffer) + bufp->buffer; begalt = (begalt - old_buffer) + bufp->buffer; if (fixup_alt_jump) fixup_alt_jump = (fixup_alt_jump - old_buffer) + bufp->buffer; if (laststart) laststart = (laststart - old_buffer) + bufp->buffer; if (pending_exact) pending_exact = (pending_exact - old_buffer) + bufp->buffer; } } while (0); *b++ = (unsigned char) (stop_memory); *b++ = (unsigned char) (this_group_regnum); *b++ = (unsigned char) (
UNSUPPORTED("7sev1sdu00ye0rtlo3hndpvge"); //  regnum - this_group_regnum); } while (0);
UNSUPPORTED("2v7ai112w6nt2xgs1gbzo22oe"); //                                     }
UNSUPPORTED("13jcwbk3vyfh9xrmwi5hbe7so"); //                             }
UNSUPPORTED("7mosouhqcis2k8sbg88g9wol8"); //                             break;
UNSUPPORTED("1r6y7bujaca34ru0thv040wer"); //                         case '|':                                       /* `\|'.  */
UNSUPPORTED("46dgptds7v1z98qko5xqgje7h"); //                             if (syntax & ((((((((((((unsigned long int) 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) || syntax & (((((((((((((((((unsigned long int) 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1))
UNSUPPORTED("duzsz5s9lw56m1on7ajxgk9o2"); //                                 goto normal_backslash;
UNSUPPORTED("4zdduwo0bies2nyvyh3rkqec5"); //                         handle_alt:
UNSUPPORTED("eh6qytrwlnojjauu38ckl7ieo"); //                             if (syntax & ((((((((((((unsigned long int) 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1))
UNSUPPORTED("ck4wdxm4o1ub3048vodmtvy6s"); //                                 goto normal_char;
UNSUPPORTED("71lbycb6i00gg9x1a2qrb3eip"); //                             /* Insert before the previous alternative a jump which
UNSUPPORTED("7e4xxb42diyggtjzhyj0r4iht"); //                                jumps to this alternative if the former fails.  */
UNSUPPORTED("12a3mmfyhz3lv9g634sbhwpnl"); //                             while ((unsigned long) (b - bufp->buffer + (3)) > bufp->allocated) do { unsigned char *old_buffer = bufp->buffer; if (bufp->allocated == (1L << 16)) return REG_ESIZE; bufp->allocated <<= 1; if (bufp->allocated > (1L << 16)) bufp->allocated = (1L << 16); bufp->buffer = (unsigned char *) realloc ((bufp->buffer), (bufp->allocated)); if (bufp->buffer == (void *)0) return REG_ESPACE; if (old_buffer != bufp->buffer) { b = (b - old_buffer) + bufp->buffer; begalt = (begalt - old_buffer) + bufp->buffer; if (fixup_alt_jump) fixup_alt_jump = (fixup_alt_jump - old_buffer) + bufp->buffer; if (laststart) laststart = (laststart - old_buffer) + bufp->buffer; if (pending_exact) pending_exact = (pending_exact - old_buffer) + bufp->buffer; } } while (0);
UNSUPPORTED("e3r36lsf9nfoaplwmusccx2ko"); //                             insert_op1 (on_failure_jump, begalt, (int) ((b + 6) - (begalt) - 3), b);
UNSUPPORTED("7s8z4qnugzgnbuvds8u8a5jep"); //                             pending_exact = 0;
UNSUPPORTED("ebm7g0g1nuacoouvopi1thit2"); //                             b += 3;
UNSUPPORTED("2oav1ek69wp16iydew6ok9ox8"); //                             /* The alternative before this one has a jump after it
UNSUPPORTED("cmcbmjy59tx8k81e3ek6xwlvq"); //                                which gets executed if it gets matched.  Adjust that
UNSUPPORTED("7jb4wxi0em6v0jucfjtw3a4up"); //                                jump so it will jump to this alternative's analogous
UNSUPPORTED("5zlorzwitjp83i56wns9w8u0r"); //                                jump (put in below, which in turn will jump to the next
UNSUPPORTED("3y12zjyrd8x7fd4xc7uz7wk9b"); //                                (if any) alternative's such jump, etc.).  The last such
UNSUPPORTED("7oj6n4fel5k5oe1hc1w024dj5"); //                                jump jumps to the correct final destination.  A picture:
UNSUPPORTED("ey74hniulk9kz47tqr8by3ul4"); //                                _____ _____
UNSUPPORTED("ded5vl0okjroo4lbzal107hid"); //                                |   | |   |
UNSUPPORTED("8dlq9vw86vwqva8uk09fpjhnz"); //                                |   v |   v
UNSUPPORTED("6xr500bohtokobmkx1m276vk2"); //                                a | b   | c
UNSUPPORTED("k27r9mjl8i2jpghyf3yd76ss"); //                                If we are at `b', then fixup_alt_jump right now points to a
UNSUPPORTED("9jlwvx46zdiyc0cgpc9aichx2"); //                                three-byte space after `a'.  We'll put in the jump, set
UNSUPPORTED("7gw6bsjyzgswrermi9mrvnnd9"); //                                fixup_alt_jump to right after `b', and leave behind three
UNSUPPORTED("996t1kfvkq1wsgyy9twl2hmf5"); //                                bytes which we'll fill in when we get to after `c'.  */
UNSUPPORTED("cba08qms5w8uqvkd4p9iqz909"); //                             if (fixup_alt_jump)
UNSUPPORTED("1na9w390cmfhlvzr6zo51tmtb"); //                                 store_op1 (jump_past_alt, fixup_alt_jump, (int) ((b) - (fixup_alt_jump) - 3));
UNSUPPORTED("8ed964065ejrs0q6lzv94yez1"); //                             /* Mark and leave space for a jump after this alternative,
UNSUPPORTED("25baag8i46m3zhww0ac4k5o0x"); //                                to be filled in later either by next alternative or
UNSUPPORTED("87vumt536jh7giifqd1s7e35m"); //                                when know we're at the end of a series of alternatives.  */
UNSUPPORTED("ev3p48k8fo943q12gph5h8h5i"); //                             fixup_alt_jump = b;
UNSUPPORTED("12a3mmfyhz3lv9g634sbhwpnl"); //                             while ((unsigned long) (b - bufp->buffer + (3)) > bufp->allocated) do { unsigned char *old_buffer = bufp->buffer; if (bufp->allocated == (1L << 16)) return REG_ESIZE; bufp->allocated <<= 1; if (bufp->allocated > (1L << 16)) bufp->allocated = (1L << 16); bufp->buffer = (unsigned char *) realloc ((bufp->buffer), (bufp->allocated)); if (bufp->buffer == (void *)0) return REG_ESPACE; if (old_buffer != bufp->buffer) { b = (b - old_buffer) + bufp->buffer; begalt = (begalt - old_buffer) + bufp->buffer; if (fixup_alt_jump) fixup_alt_jump = (fixup_alt_jump - old_buffer) + bufp->buffer; if (laststart) laststart = (laststart - old_buffer) + bufp->buffer; if (pending_exact) pending_exact = (pending_exact - old_buffer) + bufp->buffer; } } while (0);
UNSUPPORTED("ebm7g0g1nuacoouvopi1thit2"); //                             b += 3;
UNSUPPORTED("dvt6va09huijx10zufnnjqgl3"); //                             laststart = 0;
UNSUPPORTED("90zo4it529eaunfkfdxoxrdgj"); //                             begalt = b;
UNSUPPORTED("7mosouhqcis2k8sbg88g9wol8"); //                             break;
UNSUPPORTED("99gf69q37flmk5nul07d806ch"); //                         case '{':
UNSUPPORTED("eh7o49r6vtoc1lilgh11tvn4b"); //                             /* If \{ is a literal.  */
UNSUPPORTED("bpfokxd4igwyfns09495mnyj8"); //                             if (!(syntax & (((((((((((unsigned long int) 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1))
UNSUPPORTED("5qn7772e7nzkqvtlkiys53olq"); //                                 /* If we're at `\{' and it's not the open-interval
UNSUPPORTED("8a0p71nhvq7jckckna5u4qr7t"); //                                    operator.  */
UNSUPPORTED("cswuz8yxul8hsi2ysw71xb9rz"); //                                 || ((syntax & (((((((((((unsigned long int) 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1)) && (syntax & ((((((((((((((unsigned long int) 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1)))
UNSUPPORTED("d0s744fq2fs9l9aaekpxlqj4e"); //                                 || (p - 2 == pattern  &&  p == pend))
UNSUPPORTED("duzsz5s9lw56m1on7ajxgk9o2"); //                                 goto normal_backslash;
UNSUPPORTED("bxsvs3tj432mvaj55kae5o9ue"); //                         handle_interval:
UNSUPPORTED("9aq2ft2quyeattze0cwemwweo"); //                             {
UNSUPPORTED("ey78j6wwy5vn22b5iutepsjvj"); //                                 /* If got here, then the syntax allows intervals.  */
UNSUPPORTED("22u5dv9jzoa3qwk32b1729l4r"); //                                 /* At least (most) this many matches must be made.  */
UNSUPPORTED("8hhxunayr90degkndrot61r2v"); //                                 int lower_bound = -1, upper_bound = -1;
UNSUPPORTED("b3m9z0v48oef8fma37xkjtew6"); //                                 beg_interval = p - 1;
UNSUPPORTED("891uwttbfnv5bc58wpbg1fhgl"); //                                 if (p == pend)
UNSUPPORTED("3trws9zymzc11hz9w8m2b4fl9"); //                                     {
UNSUPPORTED("cbdohhh0bm6q3vbmnwn4e1vyq"); //                                         if (syntax & ((((((((((((((unsigned long int) 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1))
UNSUPPORTED("ckqpiwbyjnqn5cpwu2kw8rzbb"); //                                             goto unfetch_interval;
UNSUPPORTED("61qf7b2jxc7j3knm5ts69f0b6"); //                                         else
UNSUPPORTED("5ln10oel04ejmsrfm7uzyxmm3"); //                                             return (free (compile_stack.stack), REG_EBRACE);
UNSUPPORTED("2v7ai112w6nt2xgs1gbzo22oe"); //                                     }
UNSUPPORTED("3imw63qg7eb3mef09yezz6uj7"); //                                 { if (p != pend) { do {if (p == pend) return REG_EEND; c = (unsigned char) *p++; if (translate) c = (unsigned char) translate[c]; } while (0); while ((1 && isdigit (c))) { if (lower_bound < 0) lower_bound = 0; lower_bound = lower_bound * 10 + c - '0'; if (p == pend) break; do {if (p == pend) return REG_EEND; c = (unsigned char) *p++; if (translate) c = (unsigned char) translate[c]; } while (0); } } };
UNSUPPORTED("1uyn0tiw7bqjiss70oyjn9l7f"); //                                 if (c == ',')
UNSUPPORTED("3trws9zymzc11hz9w8m2b4fl9"); //                                     {
UNSUPPORTED("5xu6gzc4kvvybje2lnr7qdjka"); //                                         { if (p != pend) { do {if (p == pend) return REG_EEND; c = (unsigned char) *p++; if (translate) c = (unsigned char) translate[c]; } while (0); while ((1 && isdigit (c))) { if (upper_bound < 0) upper_bound = 0; upper_bound = upper_bound * 10 + c - '0'; if (p == pend) break; do {if (p == pend) return REG_EEND; c = (unsigned char) *p++; if (translate) c = (unsigned char) translate[c]; } while (0); } } };
UNSUPPORTED("36f9x0yetwobrju6guskv8zsf"); //                                         if (upper_bound < 0) upper_bound = (0x7fff);
UNSUPPORTED("2v7ai112w6nt2xgs1gbzo22oe"); //                                     }
UNSUPPORTED("a4hbs6luulhltmygux2zimbcc"); //                                 else
UNSUPPORTED("76by7r7x6guk4yvf4na11j7fa"); //                                     /* Interval such as `{1}' => match exactly once. */
UNSUPPORTED("7oi51mz0kxn0ljkpu564jd1zz"); //                                     upper_bound = lower_bound;
UNSUPPORTED("7ec3ilddc87ddo0ohyphhvkag"); //                                 if (lower_bound < 0 || upper_bound > (0x7fff)
UNSUPPORTED("dg03lkmdnpyic7onasg3rhqv3"); //                                     || lower_bound > upper_bound)
UNSUPPORTED("3trws9zymzc11hz9w8m2b4fl9"); //                                     {
UNSUPPORTED("cbdohhh0bm6q3vbmnwn4e1vyq"); //                                         if (syntax & ((((((((((((((unsigned long int) 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1))
UNSUPPORTED("ckqpiwbyjnqn5cpwu2kw8rzbb"); //                                             goto unfetch_interval;
UNSUPPORTED("61qf7b2jxc7j3knm5ts69f0b6"); //                                         else
UNSUPPORTED("dncd6ntkh4fds08gm3c4f7y7z"); //                                             return (free (compile_stack.stack), REG_BADBR);
UNSUPPORTED("2v7ai112w6nt2xgs1gbzo22oe"); //                                     }
UNSUPPORTED("8zi6rfr6252dxyzqv0myhm4xm"); //                                 if (!(syntax & ((((((((((((((unsigned long int) 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1)))
UNSUPPORTED("3trws9zymzc11hz9w8m2b4fl9"); //                                     {
UNSUPPORTED("3nwh4wgghtnfkscuiwfo0cu3y"); //                                         if (c != '\\') return (free (compile_stack.stack), REG_EBRACE);
UNSUPPORTED("eqri2rfe7jnz1m2rij615jaj1"); //                                         do {if (p == pend) return REG_EEND; c = (unsigned char) *p++; if (translate) c = (unsigned char) translate[c]; } while (0);
UNSUPPORTED("2v7ai112w6nt2xgs1gbzo22oe"); //                                     }
UNSUPPORTED("7yy5bnz36zs5pyi05n1qbbyfx"); //                                 if (c != '}')
UNSUPPORTED("3trws9zymzc11hz9w8m2b4fl9"); //                                     {
UNSUPPORTED("cbdohhh0bm6q3vbmnwn4e1vyq"); //                                         if (syntax & ((((((((((((((unsigned long int) 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1))
UNSUPPORTED("ckqpiwbyjnqn5cpwu2kw8rzbb"); //                                             goto unfetch_interval;
UNSUPPORTED("61qf7b2jxc7j3knm5ts69f0b6"); //                                         else
UNSUPPORTED("dncd6ntkh4fds08gm3c4f7y7z"); //                                             return (free (compile_stack.stack), REG_BADBR);
UNSUPPORTED("2v7ai112w6nt2xgs1gbzo22oe"); //                                     }
UNSUPPORTED("5wvp3nau0n8tyg9q5a45z6lyw"); //                                 /* We just parsed a valid interval.  */
UNSUPPORTED("8abj2dxygzm4q2t6cik367htu"); //                                 /* If it's invalid to have no preceding re.  */
UNSUPPORTED("7jmcihqsic23w36x8uyw6rgq0"); //                                 if (!laststart)
UNSUPPORTED("3trws9zymzc11hz9w8m2b4fl9"); //                                     {
UNSUPPORTED("dhcq2d5ztdbu4o4zvyapxw6nh"); //                                         if (syntax & (((((((unsigned long int) 1) << 1) << 1) << 1) << 1) << 1))
UNSUPPORTED("1m9o7j67m70ez7l1kcm89n5hu"); //                                             return (free (compile_stack.stack), REG_BADRPT);
UNSUPPORTED("cd9am2nn7ebpy7n4jg9h1ufr4"); //                                         else if (syntax & ((((((unsigned long int) 1) << 1) << 1) << 1) << 1))
UNSUPPORTED("9bhpbnocq89hcx0yaaav9o55q"); //                                             laststart = b;
UNSUPPORTED("61qf7b2jxc7j3knm5ts69f0b6"); //                                         else
UNSUPPORTED("ckqpiwbyjnqn5cpwu2kw8rzbb"); //                                             goto unfetch_interval;
UNSUPPORTED("2v7ai112w6nt2xgs1gbzo22oe"); //                                     }
UNSUPPORTED("4gb3yehghg1r4z81pfpjdfvgt"); //                                 /* If the upper bound is zero, don't want to succeed at
UNSUPPORTED("aatlwtk51r4vhf2pwijmg9mp2"); //                                    all; jump from `laststart' to `b + 3', which will be
UNSUPPORTED("5qe2o1vhmd60525scpziso3qd"); //                                    the end of the buffer after we insert the jump.  */
UNSUPPORTED("7mu4hmjs37m7lx095ek5vdxxv"); //                                 if (upper_bound == 0)
UNSUPPORTED("3trws9zymzc11hz9w8m2b4fl9"); //                                     {
UNSUPPORTED("a881bhs9uyxvv8hkudjh48ded"); //                                         while ((unsigned long) (b - bufp->buffer + (3)) > bufp->allocated) do { unsigned char *old_buffer = bufp->buffer; if (bufp->allocated == (1L << 16)) return REG_ESIZE; bufp->allocated <<= 1; if (bufp->allocated > (1L << 16)) bufp->allocated = (1L << 16); bufp->buffer = (unsigned char *) realloc ((bufp->buffer), (bufp->allocated)); if (bufp->buffer == (void *)0) return REG_ESPACE; if (old_buffer != bufp->buffer) { b = (b - old_buffer) + bufp->buffer; begalt = (begalt - old_buffer) + bufp->buffer; if (fixup_alt_jump) fixup_alt_jump = (fixup_alt_jump - old_buffer) + bufp->buffer; if (laststart) laststart = (laststart - old_buffer) + bufp->buffer; if (pending_exact) pending_exact = (pending_exact - old_buffer) + bufp->buffer; } } while (0);
UNSUPPORTED("1iszfmgzsydg6wplo0b3rdscs"); //                                         insert_op1 (jump, laststart, (int) ((b + 3) - (laststart) - 3), b);
UNSUPPORTED("ef7wtmbarkb4uc7wd67hvt8vg"); //                                         b += 3;
UNSUPPORTED("2v7ai112w6nt2xgs1gbzo22oe"); //                                     }
UNSUPPORTED("er56h1ofqtaixpmzkkaco2esj"); //                                 /* Otherwise, we have a nontrivial interval.  When
UNSUPPORTED("as06jnzy16oy1d2k8yrsmnzce"); //                                    we're all done, the pattern will look like:
UNSUPPORTED("2sy9ixs1qds1ov6cizjv2ekst"); //                                    set_number_at <jump count> <upper bound>
UNSUPPORTED("57q8h3cnrxlq45gvr3k5coz5l"); //                                    set_number_at <succeed_n count> <lower bound>
UNSUPPORTED("9nr6blw8qubgah89xl4e7nkc9"); //                                    succeed_n <after jump addr> <succeed_n count>
UNSUPPORTED("3jejf324x7ol07x2qsz7dal9u"); //                                    <body of loop>
UNSUPPORTED("91gylqgyabjgru87d2uf6v0sb"); //                                    jump_n <succeed_n addr> <jump count>
UNSUPPORTED("3ef68fn6o1w25y5pabsc3ly4g"); //                                    (The upper bound and `jump_n' are omitted if
UNSUPPORTED("bk78ox2ujcbxp08sj3dypdu4d"); //                                    `upper_bound' is 1, though.)  */
UNSUPPORTED("a4hbs6luulhltmygux2zimbcc"); //                                 else
UNSUPPORTED("efpf0x8clufa77cutrnkx4lcj"); //                                     { /* If the upper bound is > 1, we need to insert
UNSUPPORTED("dzt6navygpg7km0wu0um9ly47"); //                                          more at the end of the loop.  */
UNSUPPORTED("bj2zq9uzbacy1f5scg45mc8o7"); //                                         unsigned nbytes = 10 + (upper_bound > 1) * 10;
UNSUPPORTED("a3dj4ob1sccvwmhsasm69xe72"); //                                         while ((unsigned long) (b - bufp->buffer + (nbytes)) > bufp->allocated) do { unsigned char *old_buffer = bufp->buffer; if (bufp->allocated == (1L << 16)) return REG_ESIZE; bufp->allocated <<= 1; if (bufp->allocated > (1L << 16)) bufp->allocated = (1L << 16); bufp->buffer = (unsigned char *) realloc ((bufp->buffer), (bufp->allocated)); if (bufp->buffer == (void *)0) return REG_ESPACE; if (old_buffer != bufp->buffer) { b = (b - old_buffer) + bufp->buffer; begalt = (begalt - old_buffer) + bufp->buffer; if (fixup_alt_jump) fixup_alt_jump = (fixup_alt_jump - old_buffer) + bufp->buffer; if (laststart) laststart = (laststart - old_buffer) + bufp->buffer; if (pending_exact) pending_exact = (pending_exact - old_buffer) + bufp->buffer; } } while (0);
UNSUPPORTED("1su4wz7ud8htx57ifpl7dvcjb"); //                                         /* Initialize lower bound of the `succeed_n', even
UNSUPPORTED("9j79ov5k583ecforn4p7uyj07"); //                                            though it will be set during matching by its
UNSUPPORTED("8e98bgll3zoiy6g3o125b3q2f"); //                                            attendant `set_number_at' (inserted next),
UNSUPPORTED("53ep471y7ys6dzp9yufebyily"); //                                            because `re_compile_fastmap' needs to know.
UNSUPPORTED("14vynt5c8wnonl64olb8ttvtd"); //                                            Jump to the `jump_n' we might insert below.  */
UNSUPPORTED("70iuvqfyzlre4zcn83hksrxn8"); //                                         insert_op2 (succeed_n, laststart, (int) ((
UNSUPPORTED("5pma6876qpamutcafga5yib97"); //  b + 5 +(upper_bound > 1) * 5) - (laststart) - 3), 
UNSUPPORTED("6vch8eo9jutmcjre2de6d9gbd"); //  lower_bound, b);
UNSUPPORTED("43kr4g9sdi8c4jh0s5i56vuom"); //                                         b += 5;
UNSUPPORTED("9wq1ll164vusxqbxnhcmfglr2"); //                                         /* Code to initialize the lower bound.  Insert
UNSUPPORTED("8pvo9br1w68oucw65nlm51oyr"); //                                            before the `succeed_n'.  The `5' is the last two
UNSUPPORTED("a0wkdu3a33nzfqwoi97ucrydd"); //                                            bytes of this `set_number_at', plus 3 bytes of
UNSUPPORTED("3f5d3pqpnky1yym79bby1sz14"); //                                            the following `succeed_n'.  */
UNSUPPORTED("6tnprwkxn86sjk70t31xuoner"); //                                         insert_op2 (set_number_at, laststart, 5, lower_bound, b);
UNSUPPORTED("43kr4g9sdi8c4jh0s5i56vuom"); //                                         b += 5;
UNSUPPORTED("oi58bgxgyehr7fep6gg7b30i"); //                                         if (upper_bound > 1)
UNSUPPORTED("8k5l99ygyd8csogbu2fv5zo"); //                                             { /* More than one repetition is allowed, so
UNSUPPORTED("dfqu908jf8ce7624rr03va1d4"); //                                                  append a backward jump to the `succeed_n'
UNSUPPORTED("3bwv4hhwkxq1dxz91v6urcgft"); //                                                  that starts this interval.
UNSUPPORTED("9d1i5cmg65d2yqlnnwhhgb10d"); //                                                  When we've reached this during matching,
UNSUPPORTED("a4w172v3cp6ffpdtc6uecy35z"); //                                                  we'll have matched the interval once, so
UNSUPPORTED("ek50qyp6pxt5f0d13fhqk7gb3"); //                                                  jump back only `upper_bound - 1' times.  */
UNSUPPORTED("cw1t4ts9rzslpfolcfywhn3qn"); //                                                 store_op2 (jump_n, b, (int) ((laststart + 5) - (b) - 3), 
UNSUPPORTED("3bkogjbp2zrhl1e57w52wgtur"); //  upper_bound - 1);
UNSUPPORTED("e4xjnlgh2lwxl5rgrsy1t0lzp"); //                                                 b += 5;
UNSUPPORTED("9eb4sjjix0zbs0m5165ieamk0"); //                                                 /* The location we want to set is the second
UNSUPPORTED("5y1422w9b3ttxiq7uxejd8dl7"); //                                                    parameter of the `jump_n'; that is `b-2' as
UNSUPPORTED("7pbl00qw1iwl2shqi1boj4s0o"); //                                                    an absolute address.  `laststart' will be
UNSUPPORTED("8nzimsq57reptkqovcwmi5is2"); //                                                    the `set_number_at' we're about to insert;
UNSUPPORTED("8zphslv1mlv2s77w43gkoi1ul"); //                                                    `laststart+3' the number to set, the source
UNSUPPORTED("d2f41o7p6x4ikqydu4firrznx"); //                                                    for the relative address.  But we are
UNSUPPORTED("ykrtets348ms6o03g441goo7"); //                                                    inserting into the middle of the pattern --
UNSUPPORTED("8wbqzkcrlw3e1ea51h8blhurv"); //                                                    so everything is getting moved up by 5.
UNSUPPORTED("a3f2u6qy5p003dtis6m0fpvl1"); //                                                    Conclusion: (b - 2) - (laststart + 3) + 5,
UNSUPPORTED("5emb5r1kvteh6n0qces39m5f2"); //                                                    i.e., b - laststart.
UNSUPPORTED("3zbu3199lym6vc2u1vakclmn6"); //                                                    We insert this at the beginning of the loop
UNSUPPORTED("csmu8b2er6ytie5e9nt8t4ii4"); //                                                    so that if we fail during matching, we'll
UNSUPPORTED("6i50gsk8v7o3h2xgwwmiiftk6"); //                                                    reinitialize the bounds.  */
UNSUPPORTED("87vlmr3dcbomq11myh4sw1fl6"); //                                                 insert_op2 (set_number_at, laststart, b - laststart,
UNSUPPORTED("9qs8k9xnugrbv80hqanlzsj0k"); //                                                             upper_bound - 1, b);
UNSUPPORTED("e4xjnlgh2lwxl5rgrsy1t0lzp"); //                                                 b += 5;
UNSUPPORTED("b5m0ttlk23avgs4i47o8lbzg1"); //                                             }
UNSUPPORTED("2v7ai112w6nt2xgs1gbzo22oe"); //                                     }
UNSUPPORTED("47qf9noup192j3ixvbaqfafxw"); //                                 pending_exact = 0;
UNSUPPORTED("5bsb6g154mdvmrq2r8pg7c57z"); //                                 beg_interval = (void *)0;
UNSUPPORTED("13jcwbk3vyfh9xrmwi5hbe7so"); //                             }
UNSUPPORTED("7mosouhqcis2k8sbg88g9wol8"); //                             break;
UNSUPPORTED("4g8id4otqpbtngfcwz2lvm1dj"); //                         unfetch_interval:
UNSUPPORTED("2uaon9lzacwcq3uk6um6vhs5b"); //                             /* If an invalid interval, match the characters as literals.  */
UNSUPPORTED("e4kt1tmmevqvy3cbyk6xnznck"); //                             ;
UNSUPPORTED("del20sog75q2qfuqvkporgtm0"); //                             p = beg_interval;
UNSUPPORTED("34xptp017sapmiseov6178cf4"); //                             beg_interval = (void *)0;
UNSUPPORTED("biznyjdfohe9yytw4pz3d8dox"); //                             /* normal_char and normal_backslash need `c'.  */
UNSUPPORTED("criokqqji74yuk47n1jhnpq75"); //                             do {if (p == pend) return REG_EEND; c = (unsigned char) *p++; if (translate) c = (unsigned char) translate[c]; } while (0);
UNSUPPORTED("axdhegkjispkpmlj1rwmr7893"); //                             if (!(syntax & ((((((((((((((unsigned long int) 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1)))
UNSUPPORTED("4hzaau620c4rh7xorkrmxfut7"); //                                 {
UNSUPPORTED("szfu82l2yyl4abpqquds47zb"); //                                     if (p > pattern  &&  p[-1] == '\\')
UNSUPPORTED("4pgdoo1pi0fajdrnqd2gl86zc"); //                                         goto normal_backslash;
UNSUPPORTED("2tfish0jog6m8uhlhaokmzvm3"); //                                 }
UNSUPPORTED("dxpbgmfj2w220xy1q99fxje94"); //                             goto normal_char;
UNSUPPORTED("28ro2br1t858h9e64b2eclvsh"); //                         case 'w':
UNSUPPORTED("f3s47ztvpkb3579t2leyzyw26"); //                             if (re_syntax_options & (((((((((((((((((((((unsigned long int) 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1))
UNSUPPORTED("ck4wdxm4o1ub3048vodmtvy6s"); //                                 goto normal_char;
UNSUPPORTED("bzlbs3vmfi1kf0101kec4qyco"); //                             laststart = b;
UNSUPPORTED("br0jt3hvd6j06mrnfv1j1du2c"); //                             do { while ((unsigned long) (b - bufp->buffer + (1)) > bufp->allocated) do { unsigned char *old_buffer = bufp->buffer; if (bufp->allocated == (1L << 16)) return REG_ESIZE; bufp->allocated <<= 1; if (bufp->allocated > (1L << 16)) bufp->allocated = (1L << 16); bufp->buffer = (unsigned char *) realloc ((bufp->buffer), (bufp->allocated)); if (bufp->buffer == (void *)0) return REG_ESPACE; if (old_buffer != bufp->buffer) { b = (b - old_buffer) + bufp->buffer; begalt = (begalt - old_buffer) + bufp->buffer; if (fixup_alt_jump) fixup_alt_jump = (fixup_alt_jump - old_buffer) + bufp->buffer; if (laststart) laststart = (laststart - old_buffer) + bufp->buffer; if (pending_exact) pending_exact = (pending_exact - old_buffer) + bufp->buffer; } } while (0); *b++ = (unsigned char) (wordchar); } while (0);
UNSUPPORTED("7mosouhqcis2k8sbg88g9wol8"); //                             break;
UNSUPPORTED("cgixiaqx7fff7w14lfzv9un9b"); //                         case 'W':
UNSUPPORTED("f3s47ztvpkb3579t2leyzyw26"); //                             if (re_syntax_options & (((((((((((((((((((((unsigned long int) 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1))
UNSUPPORTED("ck4wdxm4o1ub3048vodmtvy6s"); //                                 goto normal_char;
UNSUPPORTED("bzlbs3vmfi1kf0101kec4qyco"); //                             laststart = b;
UNSUPPORTED("csd38b3d31jeqlokfvq1k1w8f"); //                             do { while ((unsigned long) (b - bufp->buffer + (1)) > bufp->allocated) do { unsigned char *old_buffer = bufp->buffer; if (bufp->allocated == (1L << 16)) return REG_ESIZE; bufp->allocated <<= 1; if (bufp->allocated > (1L << 16)) bufp->allocated = (1L << 16); bufp->buffer = (unsigned char *) realloc ((bufp->buffer), (bufp->allocated)); if (bufp->buffer == (void *)0) return REG_ESPACE; if (old_buffer != bufp->buffer) { b = (b - old_buffer) + bufp->buffer; begalt = (begalt - old_buffer) + bufp->buffer; if (fixup_alt_jump) fixup_alt_jump = (fixup_alt_jump - old_buffer) + bufp->buffer; if (laststart) laststart = (laststart - old_buffer) + bufp->buffer; if (pending_exact) pending_exact = (pending_exact - old_buffer) + bufp->buffer; } } while (0); *b++ = (unsigned char) (notwordchar); } while (0);
UNSUPPORTED("7mosouhqcis2k8sbg88g9wol8"); //                             break;
UNSUPPORTED("56yutn3bw63gli62ao6byqpz6"); //                         case '<':
UNSUPPORTED("f3s47ztvpkb3579t2leyzyw26"); //                             if (re_syntax_options & (((((((((((((((((((((unsigned long int) 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1))
UNSUPPORTED("ck4wdxm4o1ub3048vodmtvy6s"); //                                 goto normal_char;
UNSUPPORTED("bvnndrb6xvkus381wvq683jko"); //                             do { while ((unsigned long) (b - bufp->buffer + (1)) > bufp->allocated) do { unsigned char *old_buffer = bufp->buffer; if (bufp->allocated == (1L << 16)) return REG_ESIZE; bufp->allocated <<= 1; if (bufp->allocated > (1L << 16)) bufp->allocated = (1L << 16); bufp->buffer = (unsigned char *) realloc ((bufp->buffer), (bufp->allocated)); if (bufp->buffer == (void *)0) return REG_ESPACE; if (old_buffer != bufp->buffer) { b = (b - old_buffer) + bufp->buffer; begalt = (begalt - old_buffer) + bufp->buffer; if (fixup_alt_jump) fixup_alt_jump = (fixup_alt_jump - old_buffer) + bufp->buffer; if (laststart) laststart = (laststart - old_buffer) + bufp->buffer; if (pending_exact) pending_exact = (pending_exact - old_buffer) + bufp->buffer; } } while (0); *b++ = (unsigned char) (wordbeg); } while (0);
UNSUPPORTED("7mosouhqcis2k8sbg88g9wol8"); //                             break;
UNSUPPORTED("b27z9bdaut5nhut2j9qtvrc6x"); //                         case '>':
UNSUPPORTED("f3s47ztvpkb3579t2leyzyw26"); //                             if (re_syntax_options & (((((((((((((((((((((unsigned long int) 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1))
UNSUPPORTED("ck4wdxm4o1ub3048vodmtvy6s"); //                                 goto normal_char;
UNSUPPORTED("9x7h5ccf7d8cb7d008zc3fvnn"); //                             do { while ((unsigned long) (b - bufp->buffer + (1)) > bufp->allocated) do { unsigned char *old_buffer = bufp->buffer; if (bufp->allocated == (1L << 16)) return REG_ESIZE; bufp->allocated <<= 1; if (bufp->allocated > (1L << 16)) bufp->allocated = (1L << 16); bufp->buffer = (unsigned char *) realloc ((bufp->buffer), (bufp->allocated)); if (bufp->buffer == (void *)0) return REG_ESPACE; if (old_buffer != bufp->buffer) { b = (b - old_buffer) + bufp->buffer; begalt = (begalt - old_buffer) + bufp->buffer; if (fixup_alt_jump) fixup_alt_jump = (fixup_alt_jump - old_buffer) + bufp->buffer; if (laststart) laststart = (laststart - old_buffer) + bufp->buffer; if (pending_exact) pending_exact = (pending_exact - old_buffer) + bufp->buffer; } } while (0); *b++ = (unsigned char) (wordend); } while (0);
UNSUPPORTED("7mosouhqcis2k8sbg88g9wol8"); //                             break;
UNSUPPORTED("1lgl6j1beved5wkn8lbc9pj9v"); //                         case 'b':
UNSUPPORTED("f3s47ztvpkb3579t2leyzyw26"); //                             if (re_syntax_options & (((((((((((((((((((((unsigned long int) 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1))
UNSUPPORTED("ck4wdxm4o1ub3048vodmtvy6s"); //                                 goto normal_char;
UNSUPPORTED("ee4emtgp2qmp2if03ztroprzx"); //                             do { while ((unsigned long) (b - bufp->buffer + (1)) > bufp->allocated) do { unsigned char *old_buffer = bufp->buffer; if (bufp->allocated == (1L << 16)) return REG_ESIZE; bufp->allocated <<= 1; if (bufp->allocated > (1L << 16)) bufp->allocated = (1L << 16); bufp->buffer = (unsigned char *) realloc ((bufp->buffer), (bufp->allocated)); if (bufp->buffer == (void *)0) return REG_ESPACE; if (old_buffer != bufp->buffer) { b = (b - old_buffer) + bufp->buffer; begalt = (begalt - old_buffer) + bufp->buffer; if (fixup_alt_jump) fixup_alt_jump = (fixup_alt_jump - old_buffer) + bufp->buffer; if (laststart) laststart = (laststart - old_buffer) + bufp->buffer; if (pending_exact) pending_exact = (pending_exact - old_buffer) + bufp->buffer; } } while (0); *b++ = (unsigned char) (wordbound); } while (0);
UNSUPPORTED("7mosouhqcis2k8sbg88g9wol8"); //                             break;
UNSUPPORTED("1rh1jh861iaq6ptkgabcumr89"); //                         case 'B':
UNSUPPORTED("f3s47ztvpkb3579t2leyzyw26"); //                             if (re_syntax_options & (((((((((((((((((((((unsigned long int) 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1))
UNSUPPORTED("ck4wdxm4o1ub3048vodmtvy6s"); //                                 goto normal_char;
UNSUPPORTED("f1o6i9gjd99gwklmvn8viskl6"); //                             do { while ((unsigned long) (b - bufp->buffer + (1)) > bufp->allocated) do { unsigned char *old_buffer = bufp->buffer; if (bufp->allocated == (1L << 16)) return REG_ESIZE; bufp->allocated <<= 1; if (bufp->allocated > (1L << 16)) bufp->allocated = (1L << 16); bufp->buffer = (unsigned char *) realloc ((bufp->buffer), (bufp->allocated)); if (bufp->buffer == (void *)0) return REG_ESPACE; if (old_buffer != bufp->buffer) { b = (b - old_buffer) + bufp->buffer; begalt = (begalt - old_buffer) + bufp->buffer; if (fixup_alt_jump) fixup_alt_jump = (fixup_alt_jump - old_buffer) + bufp->buffer; if (laststart) laststart = (laststart - old_buffer) + bufp->buffer; if (pending_exact) pending_exact = (pending_exact - old_buffer) + bufp->buffer; } } while (0); *b++ = (unsigned char) (notwordbound); } while (0);
UNSUPPORTED("7mosouhqcis2k8sbg88g9wol8"); //                             break;
UNSUPPORTED("59swrjh32j9w6w9fnlmqjwqhm"); //                         case '`':
UNSUPPORTED("f3s47ztvpkb3579t2leyzyw26"); //                             if (re_syntax_options & (((((((((((((((((((((unsigned long int) 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1))
UNSUPPORTED("ck4wdxm4o1ub3048vodmtvy6s"); //                                 goto normal_char;
UNSUPPORTED("21zy7115epyqb00qfyrak2wlh"); //                             do { while ((unsigned long) (b - bufp->buffer + (1)) > bufp->allocated) do { unsigned char *old_buffer = bufp->buffer; if (bufp->allocated == (1L << 16)) return REG_ESIZE; bufp->allocated <<= 1; if (bufp->allocated > (1L << 16)) bufp->allocated = (1L << 16); bufp->buffer = (unsigned char *) realloc ((bufp->buffer), (bufp->allocated)); if (bufp->buffer == (void *)0) return REG_ESPACE; if (old_buffer != bufp->buffer) { b = (b - old_buffer) + bufp->buffer; begalt = (begalt - old_buffer) + bufp->buffer; if (fixup_alt_jump) fixup_alt_jump = (fixup_alt_jump - old_buffer) + bufp->buffer; if (laststart) laststart = (laststart - old_buffer) + bufp->buffer; if (pending_exact) pending_exact = (pending_exact - old_buffer) + bufp->buffer; } } while (0); *b++ = (unsigned char) (begbuf); } while (0);
UNSUPPORTED("7mosouhqcis2k8sbg88g9wol8"); //                             break;
UNSUPPORTED("enp735bu0pvi1g2slk3f3xjlt"); //                         case '\'':
UNSUPPORTED("f3s47ztvpkb3579t2leyzyw26"); //                             if (re_syntax_options & (((((((((((((((((((((unsigned long int) 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1))
UNSUPPORTED("ck4wdxm4o1ub3048vodmtvy6s"); //                                 goto normal_char;
UNSUPPORTED("efz3fdd9mbv7nl371w3u6ezhv"); //                             do { while ((unsigned long) (b - bufp->buffer + (1)) > bufp->allocated) do { unsigned char *old_buffer = bufp->buffer; if (bufp->allocated == (1L << 16)) return REG_ESIZE; bufp->allocated <<= 1; if (bufp->allocated > (1L << 16)) bufp->allocated = (1L << 16); bufp->buffer = (unsigned char *) realloc ((bufp->buffer), (bufp->allocated)); if (bufp->buffer == (void *)0) return REG_ESPACE; if (old_buffer != bufp->buffer) { b = (b - old_buffer) + bufp->buffer; begalt = (begalt - old_buffer) + bufp->buffer; if (fixup_alt_jump) fixup_alt_jump = (fixup_alt_jump - old_buffer) + bufp->buffer; if (laststart) laststart = (laststart - old_buffer) + bufp->buffer; if (pending_exact) pending_exact = (pending_exact - old_buffer) + bufp->buffer; } } while (0); *b++ = (unsigned char) (endbuf); } while (0);
UNSUPPORTED("7mosouhqcis2k8sbg88g9wol8"); //                             break;
UNSUPPORTED("5p5kyaam049yzq965bmdz6nnh"); //                         case '1': case '2': case '3': case '4': case '5':
UNSUPPORTED("9hv7da8hbwl3dpaoiieu3rdte"); //                         case '6': case '7': case '8': case '9':
UNSUPPORTED("8x9ia20e11qngbvg3030t63ib"); //                             if (syntax & ((((((((((((((((unsigned long int) 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1))
UNSUPPORTED("ck4wdxm4o1ub3048vodmtvy6s"); //                                 goto normal_char;
UNSUPPORTED("984azd04wqga97vlg77nqw3hl"); //                             c1 = c - '0';
UNSUPPORTED("2j8gb2lyb8b45sk2yf69wj6rw"); //                             if (c1 > regnum)
UNSUPPORTED("3t049zz47oarl9dkg8euljouk"); //                                 return (free (compile_stack.stack), REG_ESUBREG);
UNSUPPORTED("b52s0xefwdzq23ebdsm2l635g"); //                             /* Can't back reference to a subexpression if inside of it.  */
UNSUPPORTED("289fpytgoqtnz6sbd26j596ma"); //                             if (group_in_compile_stack (compile_stack, (regnum_t) c1))
UNSUPPORTED("ck4wdxm4o1ub3048vodmtvy6s"); //                                 goto normal_char;
UNSUPPORTED("bzlbs3vmfi1kf0101kec4qyco"); //                             laststart = b;
UNSUPPORTED("ayh7963cwi35nzazymns2xr4j"); //                             do { while ((unsigned long) (b - bufp->buffer + (2)) > bufp->allocated) do { unsigned char *old_buffer = bufp->buffer; if (bufp->allocated == (1L << 16)) return REG_ESIZE; bufp->allocated <<= 1; if (bufp->allocated > (1L << 16)) bufp->allocated = (1L << 16); bufp->buffer = (unsigned char *) realloc ((bufp->buffer), (bufp->allocated)); if (bufp->buffer == (void *)0) return REG_ESPACE; if (old_buffer != bufp->buffer) { b = (b - old_buffer) + bufp->buffer; begalt = (begalt - old_buffer) + bufp->buffer; if (fixup_alt_jump) fixup_alt_jump = (fixup_alt_jump - old_buffer) + bufp->buffer; if (laststart) laststart = (laststart - old_buffer) + bufp->buffer; if (pending_exact) pending_exact = (pending_exact - old_buffer) + bufp->buffer; } } while (0); *b++ = (unsigned char) (duplicate); *b++ = (unsigned char) (c1); } while (0);
UNSUPPORTED("7mosouhqcis2k8sbg88g9wol8"); //                             break;
UNSUPPORTED("10rhhnvrykevpmqtifs3vfsbn"); //                         case '+':
UNSUPPORTED("5d03wz66bdkv7mubtsqfqptwh"); //                         case '?':
UNSUPPORTED("cmtz225wrrfrtyrfnva1w0xyx"); //                             if (syntax & (((unsigned long int) 1) << 1))
UNSUPPORTED("1e36xo9vcfmetpl665flebmxr"); //                                 goto handle_plus;
UNSUPPORTED("ex6jbb36b02x7vzl22fq0yh2l"); //                             else
UNSUPPORTED("duzsz5s9lw56m1on7ajxgk9o2"); //                                 goto normal_backslash;
UNSUPPORTED("623zujkdt9es8v0w13jz9jw8x"); //                         default:
UNSUPPORTED("beo8prktwmjyif35z778zfw1g"); //                         normal_backslash:
UNSUPPORTED("d6o76edxq0225kqke2hja81jl"); //                             /* You might think it would be useful for \ to mean
UNSUPPORTED("3ifiyvf5lf26yw26enbob7qmu"); //                                not to translate; but if we don't translate it
UNSUPPORTED("ecuhc5c6wpoj7vpzvjyv5x62r"); //                                it will never match anything.  */
UNSUPPORTED("1nghju7haq80s0kj7x01pojis"); //                             c = (translate ? (char) translate[(unsigned char) (c)] : (c));
UNSUPPORTED("dxpbgmfj2w220xy1q99fxje94"); //                             goto normal_char;
UNSUPPORTED("b86ovw6olwwo6gnqlt1wqqzb4"); //                         }
UNSUPPORTED("ctqmerohp1f69mb1v1t20jx33"); //                     break;
UNSUPPORTED("comxch6w0lbi64ejq9m4p0u7o"); //                 default:
UNSUPPORTED("egnm0qedqx5klxttjvckp1nh0"); //                     /* Expects the character in `c'.  */
UNSUPPORTED("4qeb36tm7edhyh2kuighgnt3l"); //                 normal_char:
UNSUPPORTED("1fk2r6llu2tyc89u61aqedfcu"); //                     /* If no exactn currently being built.  */
UNSUPPORTED("dvgbrbk801umnqbk21dcl1jqt"); //                     if (!pending_exact
UNSUPPORTED("4sk1dp6gncxrc6q5vqax6q1ff"); //                         /* If last exactn not at current position.  */
UNSUPPORTED("8ta654rp3f80109ds7wainnbo"); //                         || pending_exact + *pending_exact + 1 != b
UNSUPPORTED("77sazmv1hkm382fcbfgk2amhz"); //                         /* We have only one byte following the exactn for the count.  */
UNSUPPORTED("195b1d4l8t8zfx7373dninfqs"); //                         || *pending_exact == (1 << 8) - 1
UNSUPPORTED("dfhnk487nvojvngpft8d1m5m6"); //                         /* If followed by a repetition operator.  */
UNSUPPORTED("3ub8lclci64fyijvq4kh6b7wv"); //                         || *p == '*' || *p == '^'
UNSUPPORTED("eus82vo98skwtodf1ct43m3j2"); //                         || ((syntax & (((unsigned long int) 1) << 1))
UNSUPPORTED("c35rzowssstcdv8ruusljr55b"); //                             ? *p == '\\' && (p[1] == '+' || p[1] == '?')
UNSUPPORTED("an9wblbb8w5ryyrzneu7n5ko8"); //                             : (*p == '+' || *p == '?'))
UNSUPPORTED("9fpdj9n2jjcr421s2g9z3r4i5"); //                         || ((syntax & (((((((((((unsigned long int) 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1))
UNSUPPORTED("doucnd8aksv0c0n2qke841f3d"); //                             && ((syntax & ((((((((((((((unsigned long int) 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1))
UNSUPPORTED("3d5qlx6ctgmlvlyvps6mliwz5"); //                                 ? *p == '{'
UNSUPPORTED("5g4kw7ayxlcp3eufwirvf96oj"); //                                 : (p[0] == '\\' && p[1] == '{'))))
UNSUPPORTED("5k2digv672hnrndhc9ktw0oii"); //                         {
UNSUPPORTED("nbxskwykyxkuuturxjw7zviw"); //                             /* Start building a new exactn.  */
UNSUPPORTED("bzlbs3vmfi1kf0101kec4qyco"); //                             laststart = b;
UNSUPPORTED("ejmhwadfnfrzhgtdtjhon9thk"); //                             do { while ((unsigned long) (b - bufp->buffer + (2)) > bufp->allocated) do { unsigned char *old_buffer = bufp->buffer; if (bufp->allocated == (1L << 16)) return REG_ESIZE; bufp->allocated <<= 1; if (bufp->allocated > (1L << 16)) bufp->allocated = (1L << 16); bufp->buffer = (unsigned char *) realloc ((bufp->buffer), (bufp->allocated)); if (bufp->buffer == (void *)0) return REG_ESPACE; if (old_buffer != bufp->buffer) { b = (b - old_buffer) + bufp->buffer; begalt = (begalt - old_buffer) + bufp->buffer; if (fixup_alt_jump) fixup_alt_jump = (fixup_alt_jump - old_buffer) + bufp->buffer; if (laststart) laststart = (laststart - old_buffer) + bufp->buffer; if (pending_exact) pending_exact = (pending_exact - old_buffer) + bufp->buffer; } } while (0); *b++ = (unsigned char) (exactn); *b++ = (unsigned char) (0); } while (0);
UNSUPPORTED("3d84yn9kh97rpe3bpdhnhbnpc"); //                             pending_exact = b - 1;
UNSUPPORTED("b86ovw6olwwo6gnqlt1wqqzb4"); //                         }
UNSUPPORTED("2k7zblp37vmofrhji8gga03t0"); //                     do { while ((unsigned long) (b - bufp->buffer + (1)) > bufp->allocated) do { unsigned char *old_buffer = bufp->buffer; if (bufp->allocated == (1L << 16)) return REG_ESIZE; bufp->allocated <<= 1; if (bufp->allocated > (1L << 16)) bufp->allocated = (1L << 16); bufp->buffer = (unsigned char *) realloc ((bufp->buffer), (bufp->allocated)); if (bufp->buffer == (void *)0) return REG_ESPACE; if (old_buffer != bufp->buffer) { b = (b - old_buffer) + bufp->buffer; begalt = (begalt - old_buffer) + bufp->buffer; if (fixup_alt_jump) fixup_alt_jump = (fixup_alt_jump - old_buffer) + bufp->buffer; if (laststart) laststart = (laststart - old_buffer) + bufp->buffer; if (pending_exact) pending_exact = (pending_exact - old_buffer) + bufp->buffer; } } while (0); *b++ = (unsigned char) (c); } while (0);
UNSUPPORTED("cib3f9ag6ywox7sedgbxkkmzk"); //                     (*pending_exact)++;
UNSUPPORTED("ctqmerohp1f69mb1v1t20jx33"); //                     break;
UNSUPPORTED("61b5pg30vl032ddas1lgz7fom"); //                 } /* switch (c) */
UNSUPPORTED("9758gpbrep7p4gr36dhunt05n"); //         } /* while p != pend */
UNSUPPORTED("93p2q85dy4ac5kim9nivyn79p"); //     /* Through the pattern now.  */
UNSUPPORTED("1owvoxb3fhlen2k6t0r5ai42z"); //     if (fixup_alt_jump)
UNSUPPORTED("13s484p9xd2bw2xbz1wlvdo2i"); //         store_op1 (jump_past_alt, fixup_alt_jump, (int) ((b) - (fixup_alt_jump) - 3));
UNSUPPORTED("aprj6xezpwmu3a179m5g8vbya"); //     if (!(compile_stack.avail == 0))
UNSUPPORTED("3qkjmx7dwj6otc7vge7e62fak"); //         return (free (compile_stack.stack), REG_EPAREN);
UNSUPPORTED("bif9tomfcf0bfh8yujmms4e00"); //   /* If we don't want backtracking, force success
UNSUPPORTED("6in6drehljv4k3imbk7ay9paw"); //      the first time we reach the end of the compiled pattern.  */
UNSUPPORTED("65u4ochrazrfxoz1im3b6cxt5"); //     if (syntax & ((((((((((((((((((((unsigned long int) 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1))
UNSUPPORTED("2gp9v2s1qkxulysls2k1go519"); //         do { while ((unsigned long) (b - bufp->buffer + (1)) > bufp->allocated) do { unsigned char *old_buffer = bufp->buffer; if (bufp->allocated == (1L << 16)) return REG_ESIZE; bufp->allocated <<= 1; if (bufp->allocated > (1L << 16)) bufp->allocated = (1L << 16); bufp->buffer = (unsigned char *) realloc ((bufp->buffer), (bufp->allocated)); if (bufp->buffer == (void *)0) return REG_ESPACE; if (old_buffer != bufp->buffer) { b = (b - old_buffer) + bufp->buffer; begalt = (begalt - old_buffer) + bufp->buffer; if (fixup_alt_jump) fixup_alt_jump = (fixup_alt_jump - old_buffer) + bufp->buffer; if (laststart) laststart = (laststart - old_buffer) + bufp->buffer; if (pending_exact) pending_exact = (pending_exact - old_buffer) + bufp->buffer; } } while (0); *b++ = (unsigned char) (succeed); } while (0);
UNSUPPORTED("cvaeac7ysy55222mdduh1aku9"); //     free (compile_stack.stack);
UNSUPPORTED("588061fcmtocd9luhfsdk74at"); //     /* We have succeeded; set the length of the buffer.  */
UNSUPPORTED("7at4a9mth8j1h0n9mhg91s816"); //     bufp->used = b - bufp->buffer;
UNSUPPORTED("cwwrkrko9fc4redgkt1b13h6d"); //     return REG_NOERROR;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 1a5b1tkqolinqh3eydzvc550l
// static void store_op1 (re_opcode_t op,            unsigned char *loc,            int arg) 
public static Object store_op1(Object... arg) {
UNSUPPORTED("e2z2o5ybnr5tgpkt8ty7hwan1"); // static void
UNSUPPORTED("eili3inuq1bw9eshdmskywxtg"); // store_op1 (re_opcode_t op,
UNSUPPORTED("eqn95cpnpj01ulxvedlp62t24"); //            unsigned char *loc,
UNSUPPORTED("4bho2a0suqymuq2e9rqlexiq3"); //            int arg)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("1hzyviac306vd3ph2a79xocx7"); //     *loc = (unsigned char) op;
UNSUPPORTED("bmuizeo6n9t1qzeegovoyri2g"); //     do { (loc + 1)[0] = (arg) & 0377; (loc + 1)[1] = (arg) >> 8; } while (0);
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 1dmpl1eqm04oq44hm3nqojfqi
// static void store_op2(re_opcode_t op,           unsigned char *loc,           int arg1,           int arg2) 
public static Object store_op2(Object... arg) {
UNSUPPORTED("e2z2o5ybnr5tgpkt8ty7hwan1"); // static void
UNSUPPORTED("3uuxvmysdrpmrqq4esv6u5jvy"); // store_op2(re_opcode_t op,
UNSUPPORTED("a8vol3cdyfu7j1q063xc83pt0"); //           unsigned char *loc,
UNSUPPORTED("dk2zk2gteycj6jwc1rcuw4jot"); //           int arg1,
UNSUPPORTED("afi4spwfrmjjgwzrd48g2eu1g"); //           int arg2)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("1hzyviac306vd3ph2a79xocx7"); //     *loc = (unsigned char) op;
UNSUPPORTED("6ula40dcnni5p95iedvl6iz0p"); //     do { (loc + 1)[0] = (arg1) & 0377; (loc + 1)[1] = (arg1) >> 8; } while (0);
UNSUPPORTED("692n1w538qervykzhu59yxbnn"); //     do { (loc + 3)[0] = (arg2) & 0377; (loc + 3)[1] = (arg2) >> 8; } while (0);
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 4oawjz5vt9t86ibo8qr5a5ot7
// static void insert_op1(re_opcode_t op,            unsigned char *loc,            int arg,            unsigned char *end) 
public static Object insert_op1(Object... arg) {
UNSUPPORTED("e2z2o5ybnr5tgpkt8ty7hwan1"); // static void
UNSUPPORTED("9kqafhtvgqir3019lsqbgnaik"); // insert_op1(re_opcode_t op,
UNSUPPORTED("eqn95cpnpj01ulxvedlp62t24"); //            unsigned char *loc,
UNSUPPORTED("epz3wdlgykwaxdrfpx2zq4v1l"); //            int arg,
UNSUPPORTED("ebratu77atbhul7tuupzmelvf"); //            unsigned char *end)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("169hmon34dm6xedl4afpa0i1j"); //     register unsigned char *pfrom = end;
UNSUPPORTED("45zeyp0r8qs22j0ji3pvdf6z6"); //     register unsigned char *pto = end + 3;
UNSUPPORTED("3022v7vf3t5s8s2dsxql29xo4"); //     while (pfrom != loc)
UNSUPPORTED("648ldcfu6yydgsxc2w4dn9lep"); //         *--pto = *--pfrom;
UNSUPPORTED("a9874mjpfiitct058pp2hkjid"); //     store_op1 (op, loc, arg);
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 v4p3krro14wk7jqs0uz06n05
// static void insert_op2(re_opcode_t op,            unsigned char *loc,            int arg1,            int arg2,            unsigned char *end) 
public static Object insert_op2(Object... arg) {
UNSUPPORTED("e2z2o5ybnr5tgpkt8ty7hwan1"); // static void
UNSUPPORTED("2udn0ng3gbc8cd8v2x946r2ss"); // insert_op2(re_opcode_t op,
UNSUPPORTED("eqn95cpnpj01ulxvedlp62t24"); //            unsigned char *loc,
UNSUPPORTED("9nlhn9pgj5kcxamj9gw78b0qw"); //            int arg1,
UNSUPPORTED("e6ty8yiwetqhvokp26upcjwjt"); //            int arg2,
UNSUPPORTED("ebratu77atbhul7tuupzmelvf"); //            unsigned char *end)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("169hmon34dm6xedl4afpa0i1j"); //     register unsigned char *pfrom = end;
UNSUPPORTED("b1q68dlnzwnkjo4hd7zcri3hx"); //     register unsigned char *pto = end + 5;
UNSUPPORTED("3022v7vf3t5s8s2dsxql29xo4"); //     while (pfrom != loc)
UNSUPPORTED("648ldcfu6yydgsxc2w4dn9lep"); //         *--pto = *--pfrom;
UNSUPPORTED("cd1n16p68qnglebcy01f0qn6l"); //     store_op2 (op, loc, arg1, arg2);
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 e4latxw6wysh73m40r7m5x3jb
// static boolean at_begline_loc_p(const char *pattern,                  const char *p,                  reg_syntax_t syntax) 
public static Object at_begline_loc_p(Object... arg) {
UNSUPPORTED("etarlz3ybg3wdofiesiw8dwbq"); // static boolean
UNSUPPORTED("dvoumivore2yi3vu64r9o17rz"); // at_begline_loc_p(const char *pattern,
UNSUPPORTED("1ysotgxi8uwyxakudbb9firnu"); //                  const char *p,
UNSUPPORTED("3yknuo3jrjif745axkby3960n"); //                  reg_syntax_t syntax)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("8jnr30ihhlcqx0p7qembtdxv2"); //     const char *prev = p - 2;
UNSUPPORTED("7v8vzv9bs9dhey9spvhvxxgyp"); //     boolean prev_prev_backslash = prev > pattern && prev[-1] == '\\';
UNSUPPORTED("ef16816hd7el6q6461fnw6p1l"); //     return
UNSUPPORTED("dumav0wvej216wv8szi4x00s7"); //         /* After a subexpression?  */
UNSUPPORTED("3ki59spy9w7z11gtdahfx1ew3"); //         (*prev == '(' && (syntax & (((((((((((((((unsigned long int) 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) || prev_prev_backslash))
UNSUPPORTED("ck5g71shir4kf9n8pbhvzz9ti"); //         /* After an alternative?  */
UNSUPPORTED("6zvsscasxk7x2dzo9s2jr9okc"); //         || (*prev == '|' && (syntax & (((((((((((((((((unsigned long int) 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) || prev_prev_backslash));
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 1c0buhtq2gms7qzzw7ql4drl1
// static boolean at_endline_loc_p(const char *p,                  const char *pend,                  reg_syntax_t syntax) 
public static Object at_endline_loc_p(Object... arg) {
UNSUPPORTED("etarlz3ybg3wdofiesiw8dwbq"); // static boolean
UNSUPPORTED("8w70icoej05avtfcbstpra0d"); // at_endline_loc_p(const char *p,
UNSUPPORTED("eououd67tewr9lfimix16skdn"); //                  const char *pend,
UNSUPPORTED("3yknuo3jrjif745axkby3960n"); //                  reg_syntax_t syntax)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("eh8pwbqqa4049a7x1lhsxtsc1"); //     const char *next = p;
UNSUPPORTED("4wxnm7brzem3ywa9ptyl6m3he"); //     boolean next_backslash = *next == '\\';
UNSUPPORTED("6yvr1bnbn14453b6eonom0ubu"); //     const char *next_next = p + 1 < pend ? p + 1 : 0;
UNSUPPORTED("ef16816hd7el6q6461fnw6p1l"); //     return
UNSUPPORTED("5zilqqenoq1qg4oaqhst16iny"); //         /* Before a subexpression?  */
UNSUPPORTED("bt1ckpmadq6m2rwsptz7rdax4"); //         (syntax & (((((((((((((((unsigned long int) 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) ? *next == ')'
UNSUPPORTED("ch55eypsczed6pggvuky7qg54"); //          : next_backslash && next_next && *next_next == ')')
UNSUPPORTED("cguaa6op9dr0opeabtp5b3ucn"); //         /* Before an alternative?  */
UNSUPPORTED("8fq9j881kguwbavolyyz6299l"); //         || (syntax & (((((((((((((((((unsigned long int) 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) ? *next == '|'
UNSUPPORTED("84irf5dgp7xptskletavd7lcl"); //             : next_backslash && next_next && *next_next == '|');
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 35rs0qiyjwppzg2ck5atwxnul
// static boolean group_in_compile_stack(compile_stack_type compile_stack,                        regnum_t regnum) 
public static Object group_in_compile_stack(Object... arg) {
UNSUPPORTED("etarlz3ybg3wdofiesiw8dwbq"); // static boolean
UNSUPPORTED("aja1fo6z3x54rgftvbds69e"); // group_in_compile_stack(compile_stack_type compile_stack,
UNSUPPORTED("ddun09npytwyxleyy5nr14lky"); //                        regnum_t regnum)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("dfroqi0gdjuzge2bbdy5jzym0"); //     int this_element;
UNSUPPORTED("cf73uwdus915lin6frmrap08w"); //     for (this_element = compile_stack.avail - 1;
UNSUPPORTED("d8j8jtpe5iq1esx7uzvbvm14l"); //          this_element >= 0;
UNSUPPORTED("1xpv8yktiv0jzqarajl3v79zc"); //          this_element--)
UNSUPPORTED("bq2imq1log4s4jzgs1z1rv2lj"); //         if (compile_stack.stack[this_element].regnum == regnum)
UNSUPPORTED("8qe9dt2l8vdfqmmg989im37zb"); //             return 1;
UNSUPPORTED("5oxhd3fvp0gfmrmz12vndnjt"); //     return 0;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 b8zwgtmxpoyrzfdfecydtrxda
// static reg_errcode_t compile_range(const char **p_ptr,               const char *pend,               char * translate,               reg_syntax_t syntax,               unsigned char *b) 
public static Object compile_range(Object... arg) {
UNSUPPORTED("9aerjmw5n52ektwk47t1r9bh0"); // static reg_errcode_t
UNSUPPORTED("banxmfbwacgp17kgyqg9m8x68"); // compile_range(const char **p_ptr,
UNSUPPORTED("ejc17tabtd2x12qzehf5o0sdm"); //               const char *pend,
UNSUPPORTED("cgokv0yhej0pe8aib1t4cxlz2"); //               char * translate,
UNSUPPORTED("1em1aqal71wcsn34n49xy07nb"); //               reg_syntax_t syntax,
UNSUPPORTED("3n1ptb0viq6trnbcy4c86udvu"); //               unsigned char *b)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("1m07wmw0rmjh2kja0xep3mall"); //     unsigned this_char;
UNSUPPORTED("yjjwp976mzzqbaq4la5vjvo"); //     const char *p = *p_ptr;
UNSUPPORTED("77gsk1nkdba3ni503f93jkxhi"); //     unsigned int range_start, range_end;
UNSUPPORTED("d8twi4xwvpjc0n8mumv8b1ht6"); //     if (p == pend)
UNSUPPORTED("94ycrvh63vkb1re4zpw8hjmuq"); //         return REG_ERANGE;
UNSUPPORTED("9fsyho4e2hiebyqb8qkinfir7"); //     /* Even though the pattern is a signed `char *', we need to fetch
UNSUPPORTED("3pyxw5ydi3c4tot9rd8kfdojm"); //        with unsigned char *'s; if the high bit of the pattern character
UNSUPPORTED("4cd58zz07lkzfj8p5blv29zy6"); //        is set, the range endpoints will be negative if we fetch using a
UNSUPPORTED("5loab6k7g2in3add2emwojqiz"); //        signed char *.
UNSUPPORTED("ktiibovpw3rzck0paixyi46"); //        We also want to fetch the endpoints without translating them; the
UNSUPPORTED("44ikyyh3eyhoi57l6n4hpq24u"); //        appropriate translation is done in the bit-setting loop below.  */
UNSUPPORTED("234jza0nhkmiwh04ymakixz0n"); //     /* The SVR4 compiler on the 3B2 had trouble with unsigned const char *.  */
UNSUPPORTED("bakbcowbgzdyup9hjat04heth"); //     range_start = ((const unsigned char *) p)[-2];
UNSUPPORTED("6thno4r8eqqdp5ffdk4l4xs3o"); //     range_end   = ((const unsigned char *) p)[0];
UNSUPPORTED("eyt8946jvpa0gtrnsnyalwh7j"); //     /* Have to increment the pointer into the pattern string, so the
UNSUPPORTED("3xddmz8ld8kco5vdujzs1mda5"); //        caller isn't still at the ending character.  */
UNSUPPORTED("3fa4923s6t6yycjm4mwp7vra6"); //     (*p_ptr)++;
UNSUPPORTED("deim4fwtozk7jqq58t4co6tpz"); //     /* If the start is after the end, the range is empty.  */
UNSUPPORTED("a8txh7awa0h63gqw46mc6cesc"); //     if (range_start > range_end)
UNSUPPORTED("4heoi1bxvv6q4nbsuw4o3n2up"); //         return syntax & ((((((((((((((((((unsigned long int) 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) ? REG_ERANGE : REG_NOERROR;
UNSUPPORTED("c04gtbipb7crea92dxobvfr2o"); //     /* Here we see why `this_char' has to be larger than an `unsigned
UNSUPPORTED("1ylkxbvyuwl7mimub9q6r3fad"); //        char' -- the range is inclusive, so if `range_end' == 0xff
UNSUPPORTED("1sabxy9r3dw93krw4s4a7jvs5"); //        (assuming 8-bit characters), we would otherwise go into an infinite
UNSUPPORTED("akcgy517agwm89djtay996rlu"); //        loop, since all characters <= 0xff.  */
UNSUPPORTED("8ascd4lg2uczakrabmoc2tbxc"); //     for (this_char = range_start; this_char <= range_end; this_char++)
UNSUPPORTED("6pjalxixg8dnhbhc46pm6e6ay"); //         {
UNSUPPORTED("96hzw7fz10m1eg84p3h8xw6md"); //             (b[((unsigned char) ((translate ? (char) translate[(unsigned char) (this_char)] : (this_char)))) / 8] |= 1 << (((unsigned char) (translate ? (char) translate[(unsigned char) (this_char)] : (this_char))) % 8));
UNSUPPORTED("4mhlpjofolwivhm0tl8cxznly"); //         }
UNSUPPORTED("cwwrkrko9fc4redgkt1b13h6d"); //     return REG_NOERROR;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 2w5dx8i20qj29gqbasz6ieo34
// int re_compile_fastmap(struct re_pattern_buffer *bufp) 
public static Object re_compile_fastmap(Object... arg) {
UNSUPPORTED("etrjsq5w49uo9jq5pzifohkqw"); // int
UNSUPPORTED("bq6qjjl5z4ymj67dzg1sxznk"); // re_compile_fastmap(struct re_pattern_buffer *bufp)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("9e6bnowy6jfhnib5uev3scpsu"); //     int j, k;
UNSUPPORTED("5dndxq85ri0pdc1zsswjws66p"); //     fail_stack_type fail_stack;
UNSUPPORTED("5vwwchpo0x2nq1kweizvzpx7r"); //     char *destination;
UNSUPPORTED("4hjgjnhp72gq4oozv5vrn8v10"); //     register char *fastmap = bufp->fastmap;
UNSUPPORTED("1j52put1dns981rqf534qs3le"); //     unsigned char *pattern = bufp->buffer;
UNSUPPORTED("1jmcmh4kbvfcpijmdwn6jc3nj"); //     unsigned char *p = pattern;
UNSUPPORTED("b53ls4akw5mhgh7jwfepoi7vo"); //     register unsigned char *pend = pattern + bufp->used;
UNSUPPORTED("cs2p9nwy2lugscf16o02dzs8e"); //     /* Assume that each path through the pattern can be null until
UNSUPPORTED("9dmmpbml2ftmau9q3col8nnct"); //        proven otherwise.  We set this false at the bottom of switch
UNSUPPORTED("b68s3vc4td13a333tqb4omf2a"); //        statement, to which we get only if a particular path doesn't
UNSUPPORTED("et7h2ks857zgdavpz3yc5qe44"); //        match the empty string.  */
UNSUPPORTED("51ynrqjxzi49qavh1cav2tkft"); //     boolean path_can_be_null = 1;
UNSUPPORTED("1gbvpj2yj85p505am5rilkj6r"); //     /* We aren't doing a `succeed_n' to begin with.  */
UNSUPPORTED("5xxc75r9op15sgdu02o4lgxav"); //     boolean succeed_n_p = 0;
UNSUPPORTED("5i0sddp616zsw63jk38od62l4"); //     ;
UNSUPPORTED("bbul77ox50tuvngn3t4agr5uo"); //     do { fail_stack.stack = (fail_stack_elt_t *) alloca (5 * sizeof (fail_stack_elt_t)); if (fail_stack.stack == (void *)0) return -2; fail_stack.size = 5; fail_stack.avail = 0; } while (0);
UNSUPPORTED("bzen69xwi5iake0fs9avj7zyk"); //     bzero (fastmap, 1 << 8);  /* Assume nothing's valid.  */
UNSUPPORTED("5t1sb7m6db0rmhvndw9dbiev3"); //     bufp->fastmap_accurate = 1;     /* It will be when we're done.  */
UNSUPPORTED("6jtlz1qyfqnlyp421hyq8of6x"); //     bufp->can_be_null = 0;
UNSUPPORTED("epinmskuv2cgr5ahv1yieh4wx"); //     while (1)
UNSUPPORTED("6pjalxixg8dnhbhc46pm6e6ay"); //         {
UNSUPPORTED("d9novosz5eiozbi3d0qtrp39a"); //             if (p == pend || *p == succeed)
UNSUPPORTED("9ua540u2gx5jpu302s81qfxhi"); //                 {
UNSUPPORTED("6kmea0xiqrfcm0nj8n7td2oex"); //                     /* We have reached the (effective) end of pattern.  */
UNSUPPORTED("m3xwmrnky21zpg13i5rj47jr"); //                     if (!(fail_stack.avail == 0))
UNSUPPORTED("5k2digv672hnrndhc9ktw0oii"); //                         {
UNSUPPORTED("44fedf1x4d76z1qso6huhgzfg"); //                             bufp->can_be_null |= path_can_be_null;
UNSUPPORTED("dr8ubpktswy9oks8vqmi0x94x"); //                             /* Reset for next path.  */
UNSUPPORTED("cqqk40u5ykr4up1khyq0hdreu"); //                             path_can_be_null = 1;
UNSUPPORTED("701062lrplwck0vemijk6tqcn"); //                             p = fail_stack.stack[--fail_stack.avail].pointer;
UNSUPPORTED("9rd1yahrea8hsebjyzzz61sl3"); //                             continue;
UNSUPPORTED("b86ovw6olwwo6gnqlt1wqqzb4"); //                         }
UNSUPPORTED("cunk7vpvzj28y1x4gn62gxpce"); //                     else
UNSUPPORTED("605r8o1isen77125aqrohs6ac"); //                         break;
UNSUPPORTED("7nxu74undh30brb8laojud3f9"); //                 }
UNSUPPORTED("a9id4zq8rzlovc4blpl12mdrm"); //             /* We should never be about to go beyond the end of the pattern.  */
UNSUPPORTED("cf8srqrmhz47tb7zdgoe9ufhv"); //             ;
UNSUPPORTED("3d9jhchhw3lu8yajca4mi058b"); //             switch (((re_opcode_t) *p++))
UNSUPPORTED("9ua540u2gx5jpu302s81qfxhi"); //                 {
UNSUPPORTED("6y2d5u8u0sy9seg0d2wbqhh5e"); //                     /* I guess the idea here is to simply not bother with a fastmap
UNSUPPORTED("mj34lm9e61u2qvfpqzkhd5io"); //                        if a backreference is used, since it's too hard to figure out
UNSUPPORTED("9citude1apedkbhbg4qz50zt8"); //                        the fastmap for the corresponding group.  Setting
UNSUPPORTED("a0ktcb92qa12q41u9ncckmzua"); //                        `can_be_null' stops `re_search_2' from using the fastmap, so
UNSUPPORTED("4rjoi86n2xfyhdheaakalexzv"); //                        that is all we do.  */
UNSUPPORTED("9zkejga7r0tbainhrb6zuw9os"); //                 case duplicate:
UNSUPPORTED("crvpgpa1og37lio7o6tvycmsg"); //                     bufp->can_be_null = 1;
UNSUPPORTED("7s6msojra17ajwq7l7wrmhhbx"); //                     goto done;
UNSUPPORTED("cr9angfhvv1ykkzaoo7x91pyz"); //                     /* Following are the cases which match a character.  These end
UNSUPPORTED("5u9igxod775u9rmzhdtoj0uux"); //                        with `break'.  */
UNSUPPORTED("cw984demptqbnja38a25p2xi1"); //                 case exactn:
UNSUPPORTED("99pcdhcif67xm8lp80laisjza"); //                     fastmap[p[1]] = 1;
UNSUPPORTED("ctqmerohp1f69mb1v1t20jx33"); //                     break;
UNSUPPORTED("2ro4nzmlhhrmitxu9446arhlo"); //                 case charset:
UNSUPPORTED("d5oxjemm2desl63l2fbifejay"); //                     for (j = *p++ * 8 - 1; j >= 0; j--)
UNSUPPORTED("34lg6n1mkddxpy07uf61o2pq1"); //                         if (p[j / 8] & (1 << (j % 8)))
UNSUPPORTED("ac5wgoqadttw9m0eqkmq93vpu"); //                             fastmap[j] = 1;
UNSUPPORTED("ctqmerohp1f69mb1v1t20jx33"); //                     break;
UNSUPPORTED("2zj0t91dpl66cu75cb0h2j1hu"); //                 case charset_not:
UNSUPPORTED("cagpd8yl8h3c4v8e4gh3f0z2w"); //                     /* Chars beyond end of map must be allowed.  */
UNSUPPORTED("7zzkrrvc3sqjxvhrr4s309imh"); //                     for (j = *p * 8; j < (1 << 8); j++)
UNSUPPORTED("b1s3rgvbln9pwub90llybu41x"); //                         fastmap[j] = 1;
UNSUPPORTED("d5oxjemm2desl63l2fbifejay"); //                     for (j = *p++ * 8 - 1; j >= 0; j--)
UNSUPPORTED("a9l035zjjirwoyk53mqv16l5n"); //                         if (!(p[j / 8] & (1 << (j % 8))))
UNSUPPORTED("ac5wgoqadttw9m0eqkmq93vpu"); //                             fastmap[j] = 1;
UNSUPPORTED("ctqmerohp1f69mb1v1t20jx33"); //                     break;
UNSUPPORTED("buxp1030z7swkjl42wovj9hxd"); //                 case wordchar:
UNSUPPORTED("erc7fe209pzd8kqxh3h4huo03"); //                     for (j = 0; j < (1 << 8); j++)
UNSUPPORTED("e6f90hjwdfaen2gl195u9sabh"); //                         if (re_syntax_table[j] == 1)
UNSUPPORTED("ac5wgoqadttw9m0eqkmq93vpu"); //                             fastmap[j] = 1;
UNSUPPORTED("ctqmerohp1f69mb1v1t20jx33"); //                     break;
UNSUPPORTED("7frlpnfuvuphd3bzoulwg4vsr"); //                 case notwordchar:
UNSUPPORTED("erc7fe209pzd8kqxh3h4huo03"); //                     for (j = 0; j < (1 << 8); j++)
UNSUPPORTED("d6si8hs1n9s2q5b16vyt9ka5x"); //                         if (re_syntax_table[j] != 1)
UNSUPPORTED("ac5wgoqadttw9m0eqkmq93vpu"); //                             fastmap[j] = 1;
UNSUPPORTED("ctqmerohp1f69mb1v1t20jx33"); //                     break;
UNSUPPORTED("1bv8e740f9rna5i036bo292wc"); //                 case anychar:
UNSUPPORTED("4vdjxw5o61xlsk38ouw1wsypc"); //                     {
UNSUPPORTED("czlnu0hbyndj2sl7bud8algx4"); //                         int fastmap_newline = fastmap['\n'];
UNSUPPORTED("2q6r1vdt5x04pt3p41rzto4yl"); //                         /* `.' matches anything ...  */
UNSUPPORTED("5ftk445qjdfu62ddixb4p6q0i"); //                         for (j = 0; j < (1 << 8); j++)
UNSUPPORTED("ac5wgoqadttw9m0eqkmq93vpu"); //                             fastmap[j] = 1;
UNSUPPORTED("93vg14cvrax6ffdlubqre52zy"); //                         /* ... except perhaps newline.  */
UNSUPPORTED("53qidfpnvywpfz8jbsitg0hk2"); //                         if (!(bufp->syntax & ((((((((unsigned long int) 1) << 1) << 1) << 1) << 1) << 1) << 1)))
UNSUPPORTED("9wuk4mwn83ls1g7lick6497yp"); //                             fastmap['\n'] = fastmap_newline;
UNSUPPORTED("e47uuia66ybg7wem8m4h8zf1m"); //                         /* Return if we have already set `can_be_null'; if we have,
UNSUPPORTED("5o7fl6yjw81x2g5sww79aih3s"); //                            then the fastmap is irrelevant.  Something's wrong here.  */
UNSUPPORTED("3750ces0c2deqeyn6w33ndhis"); //                         else if (bufp->can_be_null)
UNSUPPORTED("380tmjmsc0f2y0m6vckz8g0ox"); //                             goto done;
UNSUPPORTED("4nfkkkamxp2xfq2r2eg5c5880"); //                         /* Otherwise, have to check alternative paths.  */
UNSUPPORTED("605r8o1isen77125aqrohs6ac"); //                         break;
UNSUPPORTED("3e08x1y395304nd0y3uwffvim"); //                     }
UNSUPPORTED("anhhi3sf756j0lcm11sy9nyfc"); //                 case no_op:
UNSUPPORTED("7zj5ggx1l6hln4ude6h5p4hgw"); //                 case begline:
UNSUPPORTED("8pqai98vstdak51ot078dn1ut"); //                 case endline:
UNSUPPORTED("889sq08u5lsi3c6y2z7adu8ex"); //                 case begbuf:
UNSUPPORTED("cx4ezn3s02koh0pq7idm2xvxc"); //                 case endbuf:
UNSUPPORTED("a8jtmsnmpa4937exo9nhopyyb"); //                 case wordbound:
UNSUPPORTED("2jqqzr491mztwr9h8hvs55ja1"); //                 case notwordbound:
UNSUPPORTED("991vlwoc47i8p67jyv2qapdhy"); //                 case wordbeg:
UNSUPPORTED("65os7te2jc1i6uacyszvy5spd"); //                 case wordend:
UNSUPPORTED("9446ac5zvkn7r9yqkh2brh10l"); //                 case push_dummy_failure:
UNSUPPORTED("ci4p4wle87mwq773w72esmnae"); //                     continue;
UNSUPPORTED("f41u8fntqiy246yy3btlg6w1v"); //                 case jump_n:
UNSUPPORTED("6coluo8cd7scxlqwnbvt3jqs5"); //                 case pop_failure_jump:
UNSUPPORTED("d7ziafib9p2sa0rqh34hv3hwj"); //                 case maybe_pop_jump:
UNSUPPORTED("9a1qj2mhg3oh4ehsjpp4d81on"); //                 case jump:
UNSUPPORTED("7ifq76or69643hqib3roiq9f4"); //                 case jump_past_alt:
UNSUPPORTED("ejqzfph0mmtiqin1hmi981bzm"); //                 case dummy_failure_jump:
UNSUPPORTED("ddwq2jxcrhkxhe1dqkaf3heul"); //                     do { do { (j) = *(p) & 0377; (j) += ((((unsigned char) (*((p) + 1))) ^ 128) - 128) << 8; } while (0); (p) += 2; } while (0);
UNSUPPORTED("3s98ctay3wrlwmgbs6jycommh"); //                     p += j;
UNSUPPORTED("4aknhsfjvky2yxmkba43w40on"); //                     if (j > 0)
UNSUPPORTED("6ux57x1tcbm5aung2xa8i26f1"); //                         continue;
UNSUPPORTED("1gcc9mlki3vcqxqcamgw15psl"); //                     /* Jump backward implies we just went through the body of a
UNSUPPORTED("dhbf21he6r14900wxjfhl23n8"); //                        loop and matched nothing.  Opcode jumped to should be
UNSUPPORTED("b1z48ihpqy8w68mtnyaogzkre"); //                        `on_failure_jump' or `succeed_n'.  Just treat it like an
UNSUPPORTED("8sudhkuaiqtvp0rzeztrzphwa"); //                        ordinary jump.  For a * loop, it has pushed its failure
UNSUPPORTED("14e2cgrthjy5xos3c84nxu3b8"); //                        point already; if so, discard that as redundant.  */
UNSUPPORTED("eq8dnm5llhgszut0h62teamny"); //                     if ((re_opcode_t) *p != on_failure_jump
UNSUPPORTED("8sbpd0r7b4u37kj9inzim8nty"); //                         && (re_opcode_t) *p != succeed_n)
UNSUPPORTED("6ux57x1tcbm5aung2xa8i26f1"); //                         continue;
UNSUPPORTED("4s6jr3tssbvzb5ee96md1ncoi"); //                     p++;
UNSUPPORTED("ddwq2jxcrhkxhe1dqkaf3heul"); //                     do { do { (j) = *(p) & 0377; (j) += ((((unsigned char) (*((p) + 1))) ^ 128) - 128) << 8; } while (0); (p) += 2; } while (0);
UNSUPPORTED("3s98ctay3wrlwmgbs6jycommh"); //                     p += j;
UNSUPPORTED("7s373wkpewfatxsumkd67pyu7"); //                     /* If what's on the stack is where we are now, pop it.  */
UNSUPPORTED("80wyvh0vjsiujqym2stjdj4wh"); //                     if (!(fail_stack.avail == 0)
UNSUPPORTED("a3ueg01xdmhuq0grmerpgbvbb"); //                         && fail_stack.stack[fail_stack.avail - 1].pointer == p)
UNSUPPORTED("3s1nxe7g3rfik4tqx98en3cd8"); //                         fail_stack.avail--;
UNSUPPORTED("ci4p4wle87mwq773w72esmnae"); //                     continue;
UNSUPPORTED("992rmruvdtrxnk46307iiqyjo"); //                 case on_failure_jump:
UNSUPPORTED("753el6ua6knou432p32d1kbcc"); //                 case on_failure_keep_string_jump:
UNSUPPORTED("ceceofttw3ld7litzqabu648u"); //                 handle_on_failure_jump:
UNSUPPORTED("665sufie9pj8i06aycoqewann"); //                 do { do { (j) = *(p) & 0377; (j) += ((((unsigned char) (*((p) + 1))) ^ 128) - 128) << 8; } while (0); (p) += 2; } while (0);
UNSUPPORTED("dc5yaeh41cb0brsq1pv7799eq"); //                 /* For some patterns, e.g., `(a?)?', `p+j' here points to the
UNSUPPORTED("2t3hud6hrminx80odtyp1iwa7"); //                    end of the pattern.  We don't want to push such a point,
UNSUPPORTED("547mpa99j3jp91cc97q8bgppy"); //                    since when we restore it above, entering the switch will
UNSUPPORTED("d41nubecflssie5s3le3qh9ij"); //                    increment `p' past the end of the pattern.  We don't need
UNSUPPORTED("2a6fei9v5xjokdzwz2ib426kh"); //                    to push such a point since we obviously won't find any more
UNSUPPORTED("9kvyfd4vo7d8i4eapffyigb8w"); //                    fastmap entries beyond `pend'.  Such a pattern can match
UNSUPPORTED("921h84ug24r379ndj6u4ti857"); //                    the null string, though.  */
UNSUPPORTED("a3exq030sp29tpsjuxvbncjka"); //                 if (p + j < pend)
UNSUPPORTED("4vdjxw5o61xlsk38ouw1wsypc"); //                     {
UNSUPPORTED("6actr5ig2vfyitfzjmpn5gcvs"); //                         if (!(((fail_stack.avail == fail_stack.size) && !((fail_stack).size > (unsigned) (re_max_failures * (5 * 3 + 4)) ? 0 : ((fail_stack).stack = (fail_stack_elt_t *) (destination = (char *) alloca (((fail_stack).size << 1) * sizeof(fail_stack_elt_t)), bcopy ((fail_stack).stack, destination, (fail_stack).size * sizeof(fail_stack_elt_t)), destination), (fail_stack).stack == (void *)0 ? 0 : ((fail_stack).size <<= 1, 1)))) ? 0 : ((fail_stack).stack[(fail_stack).avail++].pointer = p + j, 1)))
UNSUPPORTED("9aq2ft2quyeattze0cwemwweo"); //                             {
UNSUPPORTED("9fy78dg2q1uu4c2t1zsyaestf"); //                                 ;
UNSUPPORTED("cyqv0odsj3l93ykpf16zgxlh5"); //                                 return -2;
UNSUPPORTED("13jcwbk3vyfh9xrmwi5hbe7so"); //                             }
UNSUPPORTED("3e08x1y395304nd0y3uwffvim"); //                     }
UNSUPPORTED("c0op0grmjt3kp22s10twqy66r"); //                 else
UNSUPPORTED("crvpgpa1og37lio7o6tvycmsg"); //                     bufp->can_be_null = 1;
UNSUPPORTED("3rb9irs4dl9mnesxjmb9f42vt"); //                 if (succeed_n_p)
UNSUPPORTED("4vdjxw5o61xlsk38ouw1wsypc"); //                     {
UNSUPPORTED("vdcz3hf29s758qnvg2tdn3vc"); //                         do { do { (k) = *(p) & 0377; (k) += ((((unsigned char) (*((p) + 1))) ^ 128) - 128) << 8; } while (0); (p) += 2; } while (0); /* Skip the n.  */
UNSUPPORTED("501xoe7qse6xea1u7i29ck0mi"); //                         succeed_n_p = 0;
UNSUPPORTED("3e08x1y395304nd0y3uwffvim"); //                     }
UNSUPPORTED("bam1am28aekae7y6j816hx07n"); //                 continue;
UNSUPPORTED("bdrmz6krrbsihqbsklz1j7d7a"); //                 case succeed_n:
UNSUPPORTED("3gsxz3wc6v485diavavhdc980"); //                     /* Get to the number of times to succeed.  */
UNSUPPORTED("8mazj3o2k4ts6oo83xpc0clf6"); //                     p += 2;
UNSUPPORTED("8o2myd39fg1ragrtinpt906a8"); //                     /* Increment p past the n for when k != 0.  */
UNSUPPORTED("4chyl5sa2aguucbi2603y1r7c"); //                     do { do { (k) = *(p) & 0377; (k) += ((((unsigned char) (*((p) + 1))) ^ 128) - 128) << 8; } while (0); (p) += 2; } while (0);
UNSUPPORTED("6zxjfjfmip0xaqdemcjjztblg"); //                     if (k == 0)
UNSUPPORTED("5k2digv672hnrndhc9ktw0oii"); //                         {
UNSUPPORTED("b4wfmlrv6zbxvwrfouk2eo7kw"); //                             p -= 4;
UNSUPPORTED("909biyv92rbqo3ayb2nd8yidv"); //                             succeed_n_p = 1;  /* Spaghetti code alert.  */
UNSUPPORTED("1ubpxbj2kbf7554qirpm1k0mw"); //                             goto handle_on_failure_jump;
UNSUPPORTED("b86ovw6olwwo6gnqlt1wqqzb4"); //                         }
UNSUPPORTED("ci4p4wle87mwq773w72esmnae"); //                     continue;
UNSUPPORTED("dmqxgjrlj5ddxj88dw919ujoj"); //                 case set_number_at:
UNSUPPORTED("9stirsnvehglwumsosptm8ngk"); //                     p += 4;
UNSUPPORTED("ci4p4wle87mwq773w72esmnae"); //                     continue;
UNSUPPORTED("6c0ei34e676sv2kl1dxfzm5lm"); //                 case start_memory:
UNSUPPORTED("3q89uw4g68y7kyiitvliyykpb"); //                 case stop_memory:
UNSUPPORTED("8mazj3o2k4ts6oo83xpc0clf6"); //                     p += 2;
UNSUPPORTED("ci4p4wle87mwq773w72esmnae"); //                     continue;
UNSUPPORTED("comxch6w0lbi64ejq9m4p0u7o"); //                 default:
UNSUPPORTED("55kbrxcqu6yyh19ldzw88t99r"); //                     abort (); /* We have listed all the cases.  */
UNSUPPORTED("88ufjis6u0ijo8dyph0trsd9f"); //                 } /* switch *p++ */
UNSUPPORTED("cwq7qsz40n4bhj5dg5eym4pub"); //             /* Getting here means we have found the possible starting
UNSUPPORTED("9cuw6gsavq5jezett5z0kqhtr"); //                characters for one path of the pattern -- and that the empty
UNSUPPORTED("1nuw25jxiky0vsokzstwqjshg"); //                string does not match.  We need not follow this path further.
UNSUPPORTED("ir1qnihuj9d2pchsx533wqak"); //                Instead, look at the next alternative (remembered on the
UNSUPPORTED("2yh8to75n640o6rt44ql2gpr1"); //                stack), or quit if no more.  The test at the top of the loop
UNSUPPORTED("48eepx7kyfmje1rnq1bcr79sq"); //                does these things.  */
UNSUPPORTED("7b7dtwbvg82guicn1qbjlrm0i"); //             path_can_be_null = 0;
UNSUPPORTED("9iqvxxuyvmn0bkipb04jv1fp2"); //             p = pend;
UNSUPPORTED("8qk14yz36d7vk72ifwa7qiujq"); //         } /* while p */
UNSUPPORTED("fe2n2uj632yqtenyvepbd4on"); //     /* Set `can_be_null' for the last path (also the first path, if the
UNSUPPORTED("eoc9k5utu0yxlarln062f7ebo"); //        pattern is empty).  */
UNSUPPORTED("3uys3oae24dxx6zj9f9r0b80z"); //     bufp->can_be_null |= path_can_be_null;
UNSUPPORTED("c1mu8v1bx1gc9xwu0pmd4wrmg"); //  done:
UNSUPPORTED("5i0sddp616zsw63jk38od62l4"); //     ;
UNSUPPORTED("5oxhd3fvp0gfmrmz12vndnjt"); //     return 0;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 aql5ifimwt0liaidklmsf048e
// void re_set_registers(struct re_pattern_buffer *bufp,                  struct re_registers *regs,                  unsigned num_regs,                  regoff_t *starts,                  regoff_t *ends) 
public static Object re_set_registers(Object... arg) {
UNSUPPORTED("c01vxogao855zs8fe94tpim9g"); // void
UNSUPPORTED("4aeowny9csghzgfhys3rxd4bb"); // re_set_registers(struct re_pattern_buffer *bufp,
UNSUPPORTED("v08al94r6tf851td77qv1lrw"); //                  struct re_registers *regs,
UNSUPPORTED("cws3mkdihiuxfnp7ty29mle8p"); //                  unsigned num_regs,
UNSUPPORTED("3bn37bfn6icqtacq0lj8zj9cg"); //                  regoff_t *starts,
UNSUPPORTED("aqc83ygcz30vhx0fxtyw3a3gc"); //                  regoff_t *ends)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("d3ztzcne2cr4fi3m42q2opwfy"); //     if (num_regs)
UNSUPPORTED("6pjalxixg8dnhbhc46pm6e6ay"); //         {
UNSUPPORTED("9z36hkw7bkc2nch28htx6k0sp"); //             bufp->regs_allocated = 1;
UNSUPPORTED("atb5wuee0ohtv7w4cswzrtdfh"); //             regs->num_regs = num_regs;
UNSUPPORTED("6xqxd9992s4jjhafwdu58y4w3"); //             regs->start = starts;
UNSUPPORTED("1dplqbibwb9py7ly7iqtemaka"); //             regs->end = ends;
UNSUPPORTED("4mhlpjofolwivhm0tl8cxznly"); //         }
UNSUPPORTED("div10atae09n36x269sl208r1"); //     else
UNSUPPORTED("6pjalxixg8dnhbhc46pm6e6ay"); //         {
UNSUPPORTED("5err36958fxwo1a5uvi85wj9r"); //             bufp->regs_allocated = 0;
UNSUPPORTED("3jgm69kgp5moxxlzgyht3brgv"); //             regs->num_regs = 0;
UNSUPPORTED("1b975m061jnwz38mhlafj6a72"); //             regs->start = regs->end = (regoff_t *) 0;
UNSUPPORTED("4mhlpjofolwivhm0tl8cxznly"); //         }
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 8wxth458oyuuwe1d7zeibvlq3
// int re_search(struct re_pattern_buffer *bufp,           const char *string,           int size,           int startpos,           int range,           struct re_registers *regs) 
public static Object re_search(Object... arg) {
UNSUPPORTED("etrjsq5w49uo9jq5pzifohkqw"); // int
UNSUPPORTED("5punk3p7482f96e5oexdvqbus"); // re_search(struct re_pattern_buffer *bufp,
UNSUPPORTED("bagvrj9iq4kkqcdgysmduv9t1"); //           const char *string,
UNSUPPORTED("1xy13yd3ws9kwbjft9qc97kcx"); //           int size,
UNSUPPORTED("9ohmanngcm89n339btr8asvfv"); //           int startpos,
UNSUPPORTED("23b7j6n4wp3b1b28bigxtgmnl"); //           int range,
UNSUPPORTED("erz9qq7ug4oj13nqxxy6jbbsy"); //           struct re_registers *regs)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("5eefi20lbvtfppz7qla7ovjwv"); //     return re_search_2 (bufp, (void *)0, 0, string, size, startpos, range,
UNSUPPORTED("et83rvt66i87q8ub41vrx6w5v"); //                         regs, size);
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 6aljedusbc3m5z49o0khre2m2
// int re_search_2(struct re_pattern_buffer *bufp,             const char *string1,             int size1,             const char *string2,             int size2,             int startpos,             int range,             struct re_registers *regs,             int stop) 
public static Object re_search_2(Object... arg) {
UNSUPPORTED("etrjsq5w49uo9jq5pzifohkqw"); // int
UNSUPPORTED("5usfyqpnmi7csbuxwbxofoyw7"); // re_search_2(struct re_pattern_buffer *bufp,
UNSUPPORTED("74m54b0hvpnb34rks2y7z9363"); //             const char *string1,
UNSUPPORTED("8nls60axvgy7utfkk3mgzdwb2"); //             int size1,
UNSUPPORTED("1iaduxc33uthqae9ewpgmxil1"); //             const char *string2,
UNSUPPORTED("dqei6asi26pdc31aztct1a8je"); //             int size2,
UNSUPPORTED("3w9t84zdf75a4tulh53r5von1"); //             int startpos,
UNSUPPORTED("5u62quyw1qekgwt8o4pnjcc5l"); //             int range,
UNSUPPORTED("5f39e12vpxsielezt5ujbqvho"); //             struct re_registers *regs,
UNSUPPORTED("7bd44vcdrb5ytkbgi2jb4dl2s"); //             int stop)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("3z4qqbm5080y3sn2lfr6rs0nk"); //     int val;
UNSUPPORTED("4hjgjnhp72gq4oozv5vrn8v10"); //     register char *fastmap = bufp->fastmap;
UNSUPPORTED("e2n10ohqvssxiromz1ghi94lp"); //     register char * translate = bufp->translate;
UNSUPPORTED("iaij8d98sbsjgborym38smlw"); //     int total_size = size1 + size2;
UNSUPPORTED("cjv924qhstzo4k8fa23nn3y5t"); //     int endpos = startpos + range;
UNSUPPORTED("7etis36i9hk8smvitf2zjrk21"); //     /* Check for out-of-range STARTPOS.  */
UNSUPPORTED("dtj4vp5xpp6ux07kkppwzg5ao"); //     if (startpos < 0 || startpos > total_size)
UNSUPPORTED("f3a98gxettwtewduvje9y3524"); //         return -1;
UNSUPPORTED("abu2iwp6rjr8ppylnna5u9yqs"); //     /* Fix up RANGE if it might eventually take us outside
UNSUPPORTED("9hmt2lbj7vhejhpf2d95tq14f"); //        the virtual concatenation of STRING1 and STRING2.
UNSUPPORTED("d3ccdgft2wzndli7yy75yesc6"); //        Make sure we won't move STARTPOS below 0 or above TOTAL_SIZE.  */
UNSUPPORTED("7ynt8jnrj8clotz4nfyn2ysy9"); //     if (endpos < 0)
UNSUPPORTED("5ra97p6qdpo5phvpk9fwd2h3j"); //         range = 0 - startpos;
UNSUPPORTED("9w57i1mbsaq0ywxj3y6q7577v"); //     else if (endpos > total_size)
UNSUPPORTED("6boq6ulbdze7abapaemgg7svh"); //         range = total_size - startpos;
UNSUPPORTED("ds4p5oizcxuw4xycv8l0vcqdv"); //     /* If the search isn't to be a backwards one, don't waste time in a
UNSUPPORTED("djej1eoe004hvk7gffedre39u"); //        search for a pattern that must be anchored.  */
UNSUPPORTED("cvy0dazfbe38ki1z0hsl3dn1l"); //     if (bufp->used > 0 && (re_opcode_t) bufp->buffer[0] == begbuf && range > 0)
UNSUPPORTED("6pjalxixg8dnhbhc46pm6e6ay"); //         {
UNSUPPORTED("bpeukjf6wgkfxtlskf398co2v"); //             if (startpos > 0)
UNSUPPORTED("1bjpmpr3p20x2b029ko5zgklx"); //                 return -1;
UNSUPPORTED("1knjyao8ci3w18zqqcnmnitir"); //             else
UNSUPPORTED("ad8lfo0w7g7423hbhmk3ljtia"); //                 range = 1;
UNSUPPORTED("4mhlpjofolwivhm0tl8cxznly"); //         }
UNSUPPORTED("86solugecvztzl7zf0cnd6cvd"); //     /* Update the fastmap now if not correct already.  */
UNSUPPORTED("1d8z3kbyyw3rdl2mysjgwzxjl"); //     if (fastmap && !bufp->fastmap_accurate)
UNSUPPORTED("ckj45nonmakbapzpe7kn1d15x"); //         if (re_compile_fastmap (bufp) == -2)
UNSUPPORTED("kh5ykxeb4qomvs3j2wfbfj0v"); //             return -2;
UNSUPPORTED("78edqutsfw99a52tozy5vek1b"); //     /* Loop through the string, looking for a place to start matching.  */
UNSUPPORTED("3s761dh42eu37yg4q6j6rw0kx"); //     for (;;)
UNSUPPORTED("6pjalxixg8dnhbhc46pm6e6ay"); //         {
UNSUPPORTED("4sxzib864bpin10halp8eff8a"); //             /* If a fastmap is supplied, skip quickly over characters that
UNSUPPORTED("d5j6rsa3cqak3dmcpdugforyy"); //                cannot be the start of a match.  If the pattern can match the
UNSUPPORTED("bwce0xep6mggs5v11uftrozcs"); //                null string, however, we don't need to skip characters; we want
UNSUPPORTED("12gdm8hdukh6bx7z396xe3zzf"); //                the first null string.  */
UNSUPPORTED("cj0pagb4mphhjuy0gimrygb50"); //             if (fastmap && startpos < total_size && !bufp->can_be_null)
UNSUPPORTED("9ua540u2gx5jpu302s81qfxhi"); //                 {
UNSUPPORTED("6ofes2gdk56aoc5qpr3f4zr4a"); //                     if (range > 0)      /* Searching forwards.  */
UNSUPPORTED("5k2digv672hnrndhc9ktw0oii"); //                         {
UNSUPPORTED("67v7cteuwrvbevvug4l85mak"); //                             register const char *d;
UNSUPPORTED("25rc040ywtpg6wfpwl19bnpyo"); //                             register int lim = 0;
UNSUPPORTED("9qdwkju7e5sftfnsb8qapaizw"); //                             int irange = range;
UNSUPPORTED("1wkzshwijxvvgs068kb4ducku"); //                             if (startpos < size1 && startpos + range >= size1)
UNSUPPORTED("eiy4fhjr94xbrb2h7bvte577b"); //                                 lim = range - (size1 - startpos);
UNSUPPORTED("eu28a1ugba5e46l0ni4uhl4a"); //                             d = (startpos >= size1 ? string2 - size1 : string1) + startpos;
UNSUPPORTED("cvmf6f97q523j1gxuc06p4zaw"); //                             /* Written out as an if-else to avoid testing `translate'
UNSUPPORTED("42arvib5v1138qds1m4gq8ifq"); //                                inside the loop.  */
UNSUPPORTED("c1esipwvnh5vdk2nuoqprrzy9"); //                             if (translate)
UNSUPPORTED("99bsp1v6500gcigrncj819bmz"); //                                 while (range > lim
UNSUPPORTED("7dwf7n5ce54ua4203lrcvsgyi"); //                                        && !fastmap[(unsigned char)
UNSUPPORTED("7fd297npapod0yzvmotp0ad6j"); //                                                   translate[(unsigned char) *d++]])
UNSUPPORTED("ede1n2qkk5dq4dchoitn0ij36"); //                                     range--;
UNSUPPORTED("ex6jbb36b02x7vzl22fq0yh2l"); //                             else
UNSUPPORTED("d6y3y331fli9a7o5dfc8fwn41"); //                                 while (range > lim && !fastmap[(unsigned char) *d++])
UNSUPPORTED("ede1n2qkk5dq4dchoitn0ij36"); //                                     range--;
UNSUPPORTED("7ov5wkldv7ip6gr3qqe7r9fwz"); //                             startpos += irange - range;
UNSUPPORTED("b86ovw6olwwo6gnqlt1wqqzb4"); //                         }
UNSUPPORTED("bvrbbagbquje7me47zpel7bq4"); //                     else                                /* Searching backwards.  */
UNSUPPORTED("5k2digv672hnrndhc9ktw0oii"); //                         {
UNSUPPORTED("84cdrx9bovy6vauwg0lqjqewy"); //                             register char c = (size1 == 0 || startpos >= size1
UNSUPPORTED("4e056gfwhytvjg4a7elynhkew"); //                                                ? string2[startpos - size1]
UNSUPPORTED("8xrw0w684a0sbx04150wtf94o"); //                                                : string1[startpos]);
UNSUPPORTED("37wt6pwingk4yq2cmuffily2s"); //                             if (!fastmap[(unsigned char) (translate ? (char) translate[(unsigned char) (c)] : (c))])
UNSUPPORTED("8vj5bzh7lvr4w2s7svhv0kgxc"); //                                 goto advance;
UNSUPPORTED("b86ovw6olwwo6gnqlt1wqqzb4"); //                         }
UNSUPPORTED("7nxu74undh30brb8laojud3f9"); //                 }
UNSUPPORTED("cgimbkjo920x6t1512vxlx8ko"); //             /* If can't match the null string, and that's all we have left, fail.  */
UNSUPPORTED("445mlipz9ii8giv4yb08sv90e"); //             if (range >= 0 && startpos == total_size && fastmap
UNSUPPORTED("10kowgvu386ep80mmzt22sc9v"); //                 && !bufp->can_be_null)
UNSUPPORTED("1bjpmpr3p20x2b029ko5zgklx"); //                 return -1;
UNSUPPORTED("5ry8yqv40slwqwx0mofc5x95i"); //             val = re_match_2_internal (bufp, string1, size1, string2, size2,
UNSUPPORTED("3cfs2qe5fkkt6t09tbm9q025r"); //                                        startpos, regs, stop);
UNSUPPORTED("2djilouwt96a4942a9xk0bq2j"); //             if (val >= 0)
UNSUPPORTED("4bosd0hhkhi65o9tz6w39sd4r"); //                 return startpos;
UNSUPPORTED("7mnsrrb2kz0jaysx2jmdwxxhs"); //             if (val == -2)
UNSUPPORTED("9hr24f7mnlogxg6ehbtrhe9ys"); //                 return -2;
UNSUPPORTED("5ztyn3js8nuvtozugmyim7k2"); //         advance:
UNSUPPORTED("48h8uf83j8j6i9dl6uwpnxoai"); //             if (!range)
UNSUPPORTED("d1pumbibe8xz2i7gr1wj6zdak"); //                 break;
UNSUPPORTED("9qvrpsi6e30j6d028sbxm75m8"); //             else if (range > 0)
UNSUPPORTED("9ua540u2gx5jpu302s81qfxhi"); //                 {
UNSUPPORTED("9tfu9zglyv6ato7noi8by9cx7"); //                     range--;
UNSUPPORTED("bpejet1xdfpyxq7gb5gygl6c6"); //                     startpos++;
UNSUPPORTED("7nxu74undh30brb8laojud3f9"); //                 }
UNSUPPORTED("1knjyao8ci3w18zqqcnmnitir"); //             else
UNSUPPORTED("9ua540u2gx5jpu302s81qfxhi"); //                 {
UNSUPPORTED("c1l5bq4p029ozyiuq76a11c6c"); //                     range++;
UNSUPPORTED("1fnqbyp4904zoq2u6a0h2xo1u"); //                     startpos--;
UNSUPPORTED("7nxu74undh30brb8laojud3f9"); //                 }
UNSUPPORTED("4mhlpjofolwivhm0tl8cxznly"); //         }
UNSUPPORTED("8azkpi8o0wzdufa90lw8hpt6q"); //     return -1;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 cese97kukrkyoho0nl5aeefuf
// int re_match(struct re_pattern_buffer *bufp,          const char *string,          int size,          int pos,          struct re_registers *regs) 
public static Object re_match(Object... arg) {
UNSUPPORTED("etrjsq5w49uo9jq5pzifohkqw"); // int
UNSUPPORTED("dv1reldaciy8oglpt3lvrj467"); // re_match(struct re_pattern_buffer *bufp,
UNSUPPORTED("c9d38ul1z2b760kkkwbmup6aq"); //          const char *string,
UNSUPPORTED("8ax0ba4mtdwcwwg0ygxfl2hyx"); //          int size,
UNSUPPORTED("5wdd1vovm572huu99r72nr8kp"); //          int pos,
UNSUPPORTED("ly5zx6ikzskn7s3qibi5tipo"); //          struct re_registers *regs)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("f3va2gee7dhwlha1g39azqlfa"); //     int result = re_match_2_internal (bufp, (void *)0, 0, string, size,
UNSUPPORTED("ea7wwobss2bdmnj8fhllcq46k"); //                                       pos, regs, size);
UNSUPPORTED("e73y2609z2557xahrcvzmcb8e"); //     return result;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 4bis6vnixbumphbwvjkcy4c2n
// int re_match_2(struct re_pattern_buffer *bufp,            const char *string1,            int size1,            const char *string2,            int size2,            int pos,            struct re_registers *regs,            int stop) 
public static Object re_match_2(Object... arg) {
UNSUPPORTED("etrjsq5w49uo9jq5pzifohkqw"); // int
UNSUPPORTED("bo3qvsxgxy71pt7n5tjlfpl7q"); // re_match_2(struct re_pattern_buffer *bufp,
UNSUPPORTED("8xwssdsckjeb1q0lmxlu07pk5"); //            const char *string1,
UNSUPPORTED("chena2gcw8p49mjvwjotj1ue5"); //            int size1,
UNSUPPORTED("7kll64nft07fqgencnx9f2u3h"); //            const char *string2,
UNSUPPORTED("12hpd55p7b671svle7njxpr6c"); //            int size2,
UNSUPPORTED("eyuz74k3yno3lawo6tmqeahqh"); //            int pos,
UNSUPPORTED("5du4j7mghegymbd75ui1ml4fw"); //            struct re_registers *regs,
UNSUPPORTED("2hcygji7llu5b02n114djuqj0"); //            int stop)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("a2u8v8zl4azq1kujf4s4cuii1"); //     int result = re_match_2_internal (bufp, string1, size1, string2, size2,
UNSUPPORTED("7l7rv6dwb5eaxilwku2m9z8we"); //                                       pos, regs, stop);
UNSUPPORTED("e73y2609z2557xahrcvzmcb8e"); //     return result;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 2xc3iz0d568vx4ken91tl6gyt
// static int re_match_2_internal(struct re_pattern_buffer *bufp,                     const char *string1,                     int size1,                     const char *string2,                     int size2,                     int pos,                     struct re_registers *regs,                     int stop) 
public static Object re_match_2_internal(Object... arg) {
UNSUPPORTED("eyp5xkiyummcoc88ul2b6tkeg"); // static int
UNSUPPORTED("6pa1vmha3pyewzpq2e2wsz00n"); // re_match_2_internal(struct re_pattern_buffer *bufp,
UNSUPPORTED("4itq0umd8n4zaefp01c54wxac"); //                     const char *string1,
UNSUPPORTED("2da6uko1m9uyu226zvu3kgswo"); //                     int size1,
UNSUPPORTED("62ljq7ee0r8hkx89qdr6zrcj1"); //                     const char *string2,
UNSUPPORTED("eu49m9ze4ikwzobpj8jmx8zjf"); //                     int size2,
UNSUPPORTED("fsoirv97r4lbqxpez1e1kh6l"); //                     int pos,
UNSUPPORTED("5das77r3z9spuajdb6a5zoqcg"); //                     struct re_registers *regs,
UNSUPPORTED("9byy70s4d1i719ix6yy7okfe0"); //                     int stop)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("n8hlkyr29gxgnkj4x75w0pbu"); //     /* General temporaries.  */
UNSUPPORTED("5rwd4tuvikkbfw1s56awbwtbe"); //     int mcnt;
UNSUPPORTED("7ijkchsbw8xrlcbwdzkzy7sg5"); //     unsigned char *p1;
UNSUPPORTED("6wjea7744xso80td4x9ur7whp"); //     /* Just past the end of the corresponding string.  */
UNSUPPORTED("1fqaa6ix61rs3yld0danksdzt"); //     const char *end1, *end2;
UNSUPPORTED("4qjvhq2dmvddcpe2sq02ul57e"); //     /* Pointers into string1 and string2, just past the last characters in
UNSUPPORTED("e2o1kagsjdr4x6alq0db0ex6"); //      each to consider matching.  */
UNSUPPORTED("3lcnzq2ymn1yaonefakdi2gf9"); //     const char *end_match_1, *end_match_2;
UNSUPPORTED("2x1qwx7dh9vq9oyh9l2ffuhki"); //     /* Where we are in the data, and the end of the current string.  */
UNSUPPORTED("44aamau4r9tm9tp5eks2hjeeg"); //     const char *d, *dend;
UNSUPPORTED("1v5unbhjqcfvkvfh3bwyzig4m"); //     /* Where we are in the pattern, and the end of the pattern.  */
UNSUPPORTED("7juswz68hsy9xygg4vwwp1dk4"); //     unsigned char *p = bufp->buffer;
UNSUPPORTED("egc1gcrry7bqm2rmed1tbm4p7"); //     register unsigned char *pend = p + bufp->used;
UNSUPPORTED("e8rkyc240i479ffdjn5bis1o6"); //   /* Mark the opcode just after a start_memory, so we can test for an
UNSUPPORTED("asrb0rht6g8nqkxfrpbqqwhji"); //      empty subpattern when we get to the stop_memory.  */
UNSUPPORTED("1xod1essveohmbzqsm3chxtaa"); //     unsigned char *just_past_start_mem = 0;
UNSUPPORTED("5cqk44skhero82pev2rwsxqod"); //     /* We use this to map every character in the string.  */
UNSUPPORTED("b6qhkmvrybk31a74eyxl9sf73"); //     char * translate = bufp->translate;
UNSUPPORTED("clib1bwajaiu950opbz65380n"); //     /* Failure point stack.  Each place that can handle a failure further
UNSUPPORTED("3lyfugbo3isunzbvdm5i0o4cj"); //      down the line pushes a failure point on this stack.  It consists of
UNSUPPORTED("1w6peyhoigpqgnv3qujk00vnp"); //      restart, regend, and reg_info for all registers corresponding to
UNSUPPORTED("8qdk2kexk0h3w8v82dc1gi8fc"); //      the subexpressions we're currently inside, plus the number of such
UNSUPPORTED("c464idhhkc8aqmfjb0s7zn2jq"); //      registers, and, finally, two char *'s.  The first char * is where
UNSUPPORTED("d39bcwihp0jxqnlaq775160n0"); //      to resume scanning the pattern; the second one is where to resume
UNSUPPORTED("bt7bl5ufgmqudglf331090q99"); //      scanning the strings.  If the latter is zero, the failure point is
UNSUPPORTED("dxhf42bpkithht6k5midiilvm"); //      a ``dummy''; if a failure happens and the failure point is a dummy,
UNSUPPORTED("7k01qsxjsxh1mzfj3zkxymge4"); //      it gets discarded and the next next one is tried.  */
UNSUPPORTED("5dndxq85ri0pdc1zsswjws66p"); //     fail_stack_type fail_stack;
UNSUPPORTED("3ussoc6j234lhv666usd1ujlb"); //     /* We fill all the registers internally, independent of what we
UNSUPPORTED("7hukvye8ngqw8xf4c86mq7ksb"); //      return, for use in backreferences.  The number here includes
UNSUPPORTED("800kciibxx8zzy7gqlip12chx"); //      an element for register zero.  */
UNSUPPORTED("9te8xao6v2wvrtemmbarxo8r7"); //     size_t num_regs = bufp->re_nsub + 1;
UNSUPPORTED("69fqftzbp36nb88gj0hyzt7n5"); //     /* The currently active registers.  */
UNSUPPORTED("210a4lsg6g1jyyakcj1y0z9co"); //     active_reg_t lowest_active_reg = ((1 << 8) + 1);
UNSUPPORTED("cav7rdafkpj7lnazs9bzo4f01"); //     active_reg_t highest_active_reg = (1 << 8);
UNSUPPORTED("9lffam35cw7ao19j9sq1na2kz"); //   /* Information on the contents of registers. These are pointers into
UNSUPPORTED("ezs9s06ouogq0bo54g0m18dwg"); //      the input strings; they record just what was matched (on this
UNSUPPORTED("cnjuuaptdt6ott2ct87p0obeb"); //      attempt) by a subexpression part of the pattern, that is, the
UNSUPPORTED("74e8guuvf2ep3sm5wuwk3pvak"); //      regnum-th regstart pointer points to where in the pattern we began
UNSUPPORTED("3bvo1nxt2ti5kli33880w0y4f"); //      matching and the regnum-th regend points to right after where we
UNSUPPORTED("btmswfvhn3ofbmow8ypu5e13p"); //      stopped matching the regnum-th subexpression.  (The zeroth register
UNSUPPORTED("1kngd0q79bcbooydkquebsoaf"); //      keeps track of what the whole pattern matches.)  */
UNSUPPORTED("3bagrvj67tgg2hcld2oq7uq12"); //     const char **regstart, **regend;
UNSUPPORTED("cj6z5o1u5gz9fj1ldc50421nu"); //     /* If a group that's operated upon by a repetition operator fails to
UNSUPPORTED("9h8hefhboxbyq3lbhmtfl4dbe"); //      match anything, then the register for its start will need to be
UNSUPPORTED("269993sksx5a7dxx2skmjmuv0"); //      restored because it will have been set to wherever in the string we
UNSUPPORTED("8sl9yewrsty5lu2b0b5210k4l"); //      are when we last see its open-group operator.  Similarly for a
UNSUPPORTED("64mro6c1shytou2fsehv14t6e"); //      register's end.  */
UNSUPPORTED("dzseh4slrayug3iyhvd08tt7k"); //     const char **old_regstart, **old_regend;
UNSUPPORTED("60rk8kklgq8pft794dlh37glr"); //     /* The is_active field of reg_info helps us keep track of which (possibly
UNSUPPORTED("bavzn3th9ptlj1baje3pfbu2r"); //      nested) subexpressions we are currently in. The matched_something
UNSUPPORTED("3wklpcvgm1mxc3ncudzv3u7er"); //      field of reg_info[reg_num] helps us tell whether or not we have
UNSUPPORTED("9owj5d6jjiwrr65xe8b66ynmg"); //      matched any of the pattern so far this time through the reg_num-th
UNSUPPORTED("5o4rfc4vl6f2f4i6gl8zb2c12"); //      subexpression.  These two fields get reset each time through any
UNSUPPORTED("jainf18l355wvb9svfolzfyu"); //      loop their register is in.  */
UNSUPPORTED("cldhi92ebvv3qkzk7hcp1acpx"); //     register_info_type *reg_info;
UNSUPPORTED("3tm9df4zwjcbf5wzasl5vbo41"); //     /* The following record the register info as found in the above
UNSUPPORTED("62fw4aryj1l9mpyzofxartoaf"); //      variables when we find a match better than any we've seen before.
UNSUPPORTED("3yswh3sslq5jxfw5qiqyposlu"); //      This happens as we backtrack through the failure points, which in
UNSUPPORTED("7fwl7a78gtt9lpholdubg8h20"); //      turn happens only if we have not yet matched the entire string. */
UNSUPPORTED("bv3kpg4ykugxqsd55m9yhp5af"); //     unsigned best_regs_set = 0;
UNSUPPORTED("boct5aaty3lmh8421fbwe811s"); //     const char **best_regstart, **best_regend;
UNSUPPORTED("4rxhejgld2fxwm1gablnrag3j"); //     /* Logically, this is `best_regend[0]'.  But we don't want to have to
UNSUPPORTED("7qi1q2tujh3qj95sucmglupqv"); //      allocate space for that if we're not allocating space for anything
UNSUPPORTED("awo7q2rkwc6jlfdsam4my1k84"); //      else (see below).  Also, we never need info about register 0 for
UNSUPPORTED("2f9d4yn8chlunlz3lckbg2mw0"); //      any of the other register vectors, and it seems rather a kludge to
UNSUPPORTED("dixl88kjwfhphwh4xmk51px61"); //      treat `best_regend' differently than the rest.  So we keep track of
UNSUPPORTED("lthy8n6i428ea9wqx66x5syo"); //      the end of the best match so far in a separate variable.  We
UNSUPPORTED("1g3sl9fbv6pwfi9bysp5k62i"); //      initialize this to NULL so that when we backtrack the first time
UNSUPPORTED("ek5k7ui0tsvl05jargwtlh04g"); //      and need to test it, it's not garbage.  */
UNSUPPORTED("274v72hhp6qzltud0qx3natuw"); //     const char *match_end = (void *)0;
UNSUPPORTED("ce5dox5nl3mnb89gapdwnz2co"); //     /* This helps SET_REGS_MATCHED avoid doing redundant work.  */
UNSUPPORTED("7rtu9eogaexvklmhbd0nvi8rq"); //     int set_regs_matched_done = 0;
UNSUPPORTED("6f52yhx59atis4v6w50r78tpr"); //     /* Used when we pop values we don't care about.  */
UNSUPPORTED("cuer8lfpvninqja7rr37wv7vr"); //     const char **reg_dummy;
UNSUPPORTED("1tfv2eq96tkv3dnxi9htt3ogy"); //     register_info_type *reg_info_dummy;
UNSUPPORTED("5i0sddp616zsw63jk38od62l4"); //     ;
UNSUPPORTED("bbul77ox50tuvngn3t4agr5uo"); //     do { fail_stack.stack = (fail_stack_elt_t *) alloca (5 * sizeof (fail_stack_elt_t)); if (fail_stack.stack == (void *)0) return -2; fail_stack.size = 5; fail_stack.avail = 0; } while (0);
UNSUPPORTED("f2odxckhb2j0jb0skzptkj1li"); //     /* Do not bother to initialize all the register variables if there are
UNSUPPORTED("7rdujqxkgmrsifkkjc8uc4uf8"); //      no groups in the pattern, as it takes a fair amount of time.  If
UNSUPPORTED("a8btab3qm6qk1ruzu54espdbw"); //      there are groups, we include space for register 0 (the whole
UNSUPPORTED("2fkpn3ylgs86igj0rin9q175c"); //      pattern), even though we never use it, since it simplifies the
UNSUPPORTED("a82pq8o4xe5vl6izykmrmv80p"); //      array indexing.  We should fix this.  */
UNSUPPORTED("3b6nyivy298vtrccdf2yz5zfy"); //     if (bufp->re_nsub)
UNSUPPORTED("6pjalxixg8dnhbhc46pm6e6ay"); //         {
UNSUPPORTED("3s4x1dwphpxvwftzx6myntwof"); //             regstart = ((const char * *) alloca ((num_regs) * sizeof (const char *)));
UNSUPPORTED("2q4zpv4cg2m1hlu19z7n20v0i"); //             regend = ((const char * *) alloca ((num_regs) * sizeof (const char *)));
UNSUPPORTED("dncg8rhgtq841coamkzozn70f"); //             old_regstart = ((const char * *) alloca ((num_regs) * sizeof (const char *)));
UNSUPPORTED("dyvpshht388f50j2qsprkqq31"); //             old_regend = ((const char * *) alloca ((num_regs) * sizeof (const char *)));
UNSUPPORTED("dprck6oszi2zdnvn8ranbm5ax"); //             best_regstart = ((const char * *) alloca ((num_regs) * sizeof (const char *)));
UNSUPPORTED("3dm3mock6ighuyjpbnckimggr"); //             best_regend = ((const char * *) alloca ((num_regs) * sizeof (const char *)));
UNSUPPORTED("5732hglv8omqh0obbstzw9ovr"); //             reg_info = ((register_info_type *) alloca ((num_regs) * sizeof (register_info_type)));
UNSUPPORTED("4itgi9p0hm10q9gc8ligcmjw9"); //             reg_dummy = ((const char * *) alloca ((num_regs) * sizeof (const char *)));
UNSUPPORTED("2w784gfhj47yvqr5bkl3rnvb1"); //             reg_info_dummy = ((register_info_type *) alloca ((num_regs) * sizeof (register_info_type)));
UNSUPPORTED("bknt92c4iiz2fki0ya4b4zu9l"); //             if (!(regstart && regend && old_regstart && old_regend && reg_info
UNSUPPORTED("bvpk762fja9xad61rr2ecsbun"); //                   && best_regstart && best_regend && reg_dummy && reg_info_dummy))
UNSUPPORTED("9ua540u2gx5jpu302s81qfxhi"); //                 {
UNSUPPORTED("8f1nfq1jn2ejt7ub9midgshrh"); //                     do { ; if ((void*) regstart) ((void)0); (void*) regstart = (void *)0; if ((void*) regend) ((void)0); (void*) regend = (void *)0; if ((void*) old_regstart) ((void)0); (void*) old_regstart = (void *)0; if ((void*) old_regend) ((void)0); (void*) old_regend = (void *)0; if ((void*) best_regstart) ((void)0); (void*) best_regstart = (void *)0; if ((void*) best_regend) ((void)0); (void*) best_regend = (void *)0; if ((void*) reg_info) ((void)0); (void*) reg_info = (void *)0; if ((void*) reg_dummy) ((void)0); (void*) reg_dummy = (void *)0; if ((void*) reg_info_dummy) ((void)0); (void*) reg_info_dummy = (void *)0; } while (0);
UNSUPPORTED("7ivse5do9752etnc6lpwep4id"); //                     return -2;
UNSUPPORTED("7nxu74undh30brb8laojud3f9"); //                 }
UNSUPPORTED("4mhlpjofolwivhm0tl8cxznly"); //         }
UNSUPPORTED("div10atae09n36x269sl208r1"); //     else
UNSUPPORTED("6pjalxixg8dnhbhc46pm6e6ay"); //         {
UNSUPPORTED("9hkjvu3lvnwrjs0wre7judo3c"); //             /* We must initialize all our variables to NULL, so that
UNSUPPORTED("45j98q6y8yuy75ls6dsgx1k64"); //                `FREE_VARIABLES' doesn't try to free them.  */
UNSUPPORTED("2lkorxvitynsptmav1g59ixmb"); //             regstart = regend = old_regstart = old_regend = best_regstart
UNSUPPORTED("bzn1un5rdvzm7al9b58svbna4"); //                 = best_regend = reg_dummy = (void *)0;
UNSUPPORTED("d9o9396ai0y6hfmg0mj1ebkq3"); //             reg_info = reg_info_dummy = (register_info_type *) (void *)0;
UNSUPPORTED("4mhlpjofolwivhm0tl8cxznly"); //         }
UNSUPPORTED("8w78v1mfbuuwhx2hypx8jcldw"); //     /* The starting position is bogus.  */
UNSUPPORTED("a4wmr8zj9xfpfbqugg1rwnak2"); //     if (pos < 0 || pos > size1 + size2)
UNSUPPORTED("6pjalxixg8dnhbhc46pm6e6ay"); //         {
UNSUPPORTED("7sq147wjkuyi93ra9jbzwkvbf"); //             do { ; if ((void*) regstart) ((void)0); (void*) regstart = (void *)0; if ((void*) regend) ((void)0); (void*) regend = (void *)0; if ((void*) old_regstart) ((void)0); (void*) old_regstart = (void *)0; if ((void*) old_regend) ((void)0); (void*) old_regend = (void *)0; if ((void*) best_regstart) ((void)0); (void*) best_regstart = (void *)0; if ((void*) best_regend) ((void)0); (void*) best_regend = (void *)0; if ((void*) reg_info) ((void)0); (void*) reg_info = (void *)0; if ((void*) reg_dummy) ((void)0); (void*) reg_dummy = (void *)0; if ((void*) reg_info_dummy) ((void)0); (void*) reg_info_dummy = (void *)0; } while (0);
UNSUPPORTED("62ko03w39aomt1h9y758mag0k"); //             return -1;
UNSUPPORTED("4mhlpjofolwivhm0tl8cxznly"); //         }
UNSUPPORTED("cqf83jwvi6ehnlngujy4qchdu"); //     /* Initialize subexpression text positions to -1 to mark ones that no
UNSUPPORTED("57srgq1s42r2qebxntv9z4v5k"); //      start_memory/stop_memory has been seen for. Also initialize the
UNSUPPORTED("1ow37crla9qcqw19bbyb805t9"); //      register information struct.  */
UNSUPPORTED("9ydw7k6i7j7x2k6zngd59rojt"); //     for (mcnt = 1; (unsigned) mcnt < num_regs; mcnt++)
UNSUPPORTED("6pjalxixg8dnhbhc46pm6e6ay"); //         {
UNSUPPORTED("2kyhfd6kl7ms8bwg1wlvfqwap"); //             regstart[mcnt] = regend[mcnt]
UNSUPPORTED("2kl8ke0fnk9nauyk9zokcxkoj"); //                 = old_regstart[mcnt] = old_regend[mcnt] = (&reg_unset_dummy);
UNSUPPORTED("91q00i6munm3i7qie7rj8jfei"); //             ((reg_info[mcnt]).bits.match_null_string_p) = 3;
UNSUPPORTED("afk8kaqsmcxqe0q6zubefocrn"); //             ((reg_info[mcnt]).bits.is_active) = 0;
UNSUPPORTED("df366kaag3ynqzmm60vrmh0vx"); //             ((reg_info[mcnt]).bits.matched_something) = 0;
UNSUPPORTED("5mtz90agac60yikyx4k2p0tcy"); //             ((reg_info[mcnt]).bits.ever_matched_something) = 0;
UNSUPPORTED("4mhlpjofolwivhm0tl8cxznly"); //         }
UNSUPPORTED("j5sjexwvtormmkwlvyyw0j3m"); //     /* We move `string1' into `string2' if the latter's empty -- but not if
UNSUPPORTED("22snitog4zamx1t4ccq83m95r"); //      `string1' is null.  */
UNSUPPORTED("e5i3kugt3ww6rnm9voflfgawq"); //     if (size2 == 0 && string1 != (void *)0)
UNSUPPORTED("6pjalxixg8dnhbhc46pm6e6ay"); //         {
UNSUPPORTED("e42qfjjyl84hogtxnpbthj5rz"); //             string2 = string1;
UNSUPPORTED("crben3jjw8pci91x5lvrk4w8q"); //             size2 = size1;
UNSUPPORTED("f01lcid5mzfewcivqnd332urs"); //             string1 = 0;
UNSUPPORTED("5c8judheb0ofzkgdnwqnml8z0"); //             size1 = 0;
UNSUPPORTED("4mhlpjofolwivhm0tl8cxznly"); //         }
UNSUPPORTED("e172015pwzssq7c0vo2tq5aq1"); //     end1 = string1 + size1;
UNSUPPORTED("4m5wyc9k9qkl4sfiwwyvvla1h"); //     end2 = string2 + size2;
UNSUPPORTED("92k1xad7xyoitcixjggddz2xx"); //     /* Compute where to stop matching, within the two strings.  */
UNSUPPORTED("6gdkxgosf1sannyt40j9kmngq"); //     if (stop <= size1)
UNSUPPORTED("6pjalxixg8dnhbhc46pm6e6ay"); //         {
UNSUPPORTED("6cypxpgmpeoi6emdah5s9k61u"); //             end_match_1 = string1 + stop;
UNSUPPORTED("14iyugw7zeo04abwg6k9mz77r"); //             end_match_2 = string2;
UNSUPPORTED("4mhlpjofolwivhm0tl8cxznly"); //         }
UNSUPPORTED("div10atae09n36x269sl208r1"); //     else
UNSUPPORTED("6pjalxixg8dnhbhc46pm6e6ay"); //         {
UNSUPPORTED("ubox1xzc7tf43rh7nzqo0y2w"); //             end_match_1 = end1;
UNSUPPORTED("g15ad7lvemt1j3tfd3b2wy5n"); //             end_match_2 = string2 + stop - size1;
UNSUPPORTED("4mhlpjofolwivhm0tl8cxznly"); //         }
UNSUPPORTED("b72xjrsh15ceq6n0mp9o3dr3b"); //     /* `p' scans through the pattern as `d' scans through the data.
UNSUPPORTED("ee8kj3n0j7z3zgw3x29pr4iwg"); //      `dend' is the end of the input string that `d' points within.  `d'
UNSUPPORTED("9ht9fo2bvlr07alij7ev6sc64"); //      is advanced into the following input string whenever necessary, but
UNSUPPORTED("delvy2rsoiwhe79dztx86nhlo"); //      this happens before fetching; therefore, at the beginning of the
UNSUPPORTED("4fil0e4w6u5hnt1i6cgmhslcj"); //      loop, `d' can be pointing at the end of a string, but it cannot
UNSUPPORTED("3m773nmhi0vb3kvwhk5zj1fvx"); //      equal `string2'.  */
UNSUPPORTED("1sztmr22bzx9qg638iuxf0qcf"); //     if (size1 > 0 && pos <= size1)
UNSUPPORTED("6pjalxixg8dnhbhc46pm6e6ay"); //         {
UNSUPPORTED("effnp1jm0qoqpcub7rcliiklk"); //             d = string1 + pos;
UNSUPPORTED("a3z31vmcwv4izh4gaf0zzu63f"); //             dend = end_match_1;
UNSUPPORTED("4mhlpjofolwivhm0tl8cxznly"); //         }
UNSUPPORTED("div10atae09n36x269sl208r1"); //     else
UNSUPPORTED("6pjalxixg8dnhbhc46pm6e6ay"); //         {
UNSUPPORTED("a6h3karx5gb77i5icjwtr70s"); //             d = string2 + pos - size1;
UNSUPPORTED("2mh3yoyin5cy0p7onls0cxs88"); //             dend = end_match_2;
UNSUPPORTED("4mhlpjofolwivhm0tl8cxznly"); //         }
UNSUPPORTED("5i0sddp616zsw63jk38od62l4"); //     ;
UNSUPPORTED("5i0sddp616zsw63jk38od62l4"); //     ;
UNSUPPORTED("5i0sddp616zsw63jk38od62l4"); //     ;
UNSUPPORTED("5i0sddp616zsw63jk38od62l4"); //     ;
UNSUPPORTED("5i0sddp616zsw63jk38od62l4"); //     ;
UNSUPPORTED("99zhv2pwx2q8a48j197pw6xt4"); //   /* This loops over pattern commands.  It exits by returning from the
UNSUPPORTED("7e60h1pt63bigon94i6v0grl5"); //      function if the match is complete, or it drops through if the match
UNSUPPORTED("b3n3kmzfara10ikrekjiv0woz"); //      fails at this starting point in the input data.  */
UNSUPPORTED("3s761dh42eu37yg4q6j6rw0kx"); //     for (;;)
UNSUPPORTED("6pjalxixg8dnhbhc46pm6e6ay"); //         {
UNSUPPORTED("cf8srqrmhz47tb7zdgoe9ufhv"); //             ;
UNSUPPORTED("coqe9flw9y84q7eb1xt2wxm8v"); //             if (p == pend)
UNSUPPORTED("a41002aq4p23pyz97pvq70zys"); //                 { /* End of pattern means we might have succeeded.  */
UNSUPPORTED("1qe3sa29ntp5400stzdrrbgdg"); //                     ;
UNSUPPORTED("c3on5mrar07ygte8kqbl2hntc"); //                     /* If we haven't matched the entire string, and we want the
UNSUPPORTED("apgoqswuwqwo0jdc88f1g6zjk"); //                        longest match, try backtracking.  */
UNSUPPORTED("6quev9lqtkd3rm2zf8o08q0d4"); //                     if (d != end_match_2)
UNSUPPORTED("5k2digv672hnrndhc9ktw0oii"); //                         {
UNSUPPORTED("84ttk9zo6qwdugyag6bsfijsa"); //                             /* 1 if this match ends in the same string (string1 or string2)
UNSUPPORTED("3d7aa3i6sz2b5v1rdku8mfkve"); //                                as the best previous match.  */
UNSUPPORTED("99rxk6hqfc6820y8brqyzr1ix"); //                             boolean same_str_p = ((size1 && string1 <= (match_end) && (match_end) <= string1 + size1)
UNSUPPORTED("5h23tai9w26g9uu3555t1s2ko"); //                                                   == (dend == end_match_1));
UNSUPPORTED("a9j7f142ppbm4bfda6vllbzkt"); //                             /* 1 if this match is the best seen so far.  */
UNSUPPORTED("7ssv33yt0mo7z7naegb53acst"); //                             boolean best_match_p;
UNSUPPORTED("406ocol9hh6qa1tg01aqw4fm4"); //                             /* AIX compiler got confused when this was combined
UNSUPPORTED("doec2mp5fgho754yj67fiufk6"); //                                with the previous declaration.  */
UNSUPPORTED("3olytwmx4gzy8yajx9lv0w470"); //                             if (same_str_p)
UNSUPPORTED("b8puesm16uljvnmkyguxlpjz2"); //                                 best_match_p = d > match_end;
UNSUPPORTED("ex6jbb36b02x7vzl22fq0yh2l"); //                             else
UNSUPPORTED("8g5axzy7uh94mclmd8107jt4r"); //                                 best_match_p = !(dend == end_match_1);
UNSUPPORTED("e4kt1tmmevqvy3cbyk6xnznck"); //                             ;
UNSUPPORTED("61g695v6madw50govi0fpaymy"); //                             if (!(fail_stack.avail == 0))
UNSUPPORTED("c5mtgdtcl0w9vn5m7roo5mclf"); //                                 { /* More failure points to try.  */
UNSUPPORTED("6k0fj673itx4y90om89cfw4au"); //                                     /* If exceeds best match so far, save it.  */
UNSUPPORTED("8lvy2hc2kk7wk0osmu4kt3b46"); //                                     if (!best_regs_set || best_match_p)
UNSUPPORTED("bho20rvti5y7pi0voz7il3e40"); //                                         {
UNSUPPORTED("c1z8tnsgrw2hio3750gukv093"); //                                             best_regs_set = 1;
UNSUPPORTED("6cvml1s44f1rz7ut1p9732o24"); //                                             match_end = d;
UNSUPPORTED("a6r7lto035pp3gvk1bdixang2"); //                                             ;
UNSUPPORTED("5lgbvvnv5vugg8j3y9jm7tmto"); //                                             for (mcnt = 1; (unsigned) mcnt < num_regs; mcnt++)
UNSUPPORTED("1fi5w21ki1vo1jwyr4xwgm27n"); //                                                 {
UNSUPPORTED("36h25f46gwl1bjkvlv2hi12al"); //                                                     best_regstart[mcnt] = regstart[mcnt];
UNSUPPORTED("38u63aln2507yexeleac5i5f6"); //                                                     best_regend[mcnt] = regend[mcnt];
UNSUPPORTED("bf03xtomlwkahoqno8ua4cbpc"); //                                                 }
UNSUPPORTED("ad3bk4xnx8bgy7plhun3c0mvv"); //                                         }
UNSUPPORTED("3cl1in74qmmbb26vs4qw3mcuv"); //                                     goto fail;
UNSUPPORTED("2tfish0jog6m8uhlhaokmzvm3"); //                                 }
UNSUPPORTED("9608e8vynn7blihqg1sa5x6hm"); //                             /* If no failure points, don't restore garbage.  And if
UNSUPPORTED("cvm5ogwjizby9vpz4bh4t4kmf"); //                                last match is real best match, don't restore second
UNSUPPORTED("5019dps6elcptg07efvg8ytlp"); //                                best one. */
UNSUPPORTED("31h7b1f1py5xliuo37yr49j0e"); //                             else if (best_regs_set && !best_match_p)
UNSUPPORTED("4hzaau620c4rh7xorkrmxfut7"); //                                 {
UNSUPPORTED("51sj2rq6npgedmtb80qyu24n8"); //                                 restore_best_regs:
UNSUPPORTED("eassx3c83qc4vxc85c75b9qjl"); //                                     /* Restore best match.  It may happen that `dend ==
UNSUPPORTED("5pm2irkm5dt0s4qwuwz8u3q1h"); //                                        end_match_1' while the restored d is in string2.
UNSUPPORTED("bc82mepahwmg9x3r4zc1uhtn2"); //                                        For example, the pattern `x.*y.*z' against the
UNSUPPORTED("dux3vuywslng2dhkfnnw5vg60"); //                                        strings `x-' and `y-z-', if the two strings are
UNSUPPORTED("8xrw6nr4f038b5khvfblfbvnn"); //                                        not consecutive in memory.  */
UNSUPPORTED("c6tldeu5ffnykyfto7hi9ixau"); //                                     ;
UNSUPPORTED("20z1s97l71qromoe39rg38hm2"); //                                     d = match_end;
UNSUPPORTED("3ue7lk6sdffqfud3cceri8m8b"); //                                     dend = ((d >= string1 && d <= end1)
UNSUPPORTED("67n43dsreslafvliflcrjntcd"); //                                             ? end_match_1 : end_match_2);
UNSUPPORTED("7f2q0145iwgp8w2wsim63zj7m"); //                                     for (mcnt = 1; (unsigned) mcnt < num_regs; mcnt++)
UNSUPPORTED("bho20rvti5y7pi0voz7il3e40"); //                                         {
UNSUPPORTED("f2o08vllkyap6mdqirtbkpmpk"); //                                             regstart[mcnt] = best_regstart[mcnt];
UNSUPPORTED("3pqb99yerpeb7fyy71o36q7nc"); //                                             regend[mcnt] = best_regend[mcnt];
UNSUPPORTED("ad3bk4xnx8bgy7plhun3c0mvv"); //                                         }
UNSUPPORTED("2tfish0jog6m8uhlhaokmzvm3"); //                                 }
UNSUPPORTED("7qpxyrpi9p8bv5o56myo9c2dh"); //                         } /* d != end_match_2 */
UNSUPPORTED("eo36u7f29du5zdvcxohew6ccd"); //                 succeed_label:
UNSUPPORTED("1qe3sa29ntp5400stzdrrbgdg"); //                     ;
UNSUPPORTED("ch5rhljwem9e6yq0bvid9b9dj"); //                     /* If caller wants register contents data back, do it.  */
UNSUPPORTED("1yp4u3stmh22ubjzoa5psbbuy"); //                     if (regs && !bufp->no_sub)
UNSUPPORTED("5k2digv672hnrndhc9ktw0oii"); //                         {
UNSUPPORTED("3kol441pj0oa248vzhp3vv7ls"); //                             /* Have the register data arrays been allocated?  */
UNSUPPORTED("1gzi6m8z4rjss7i9b9wcz0nhw"); //                             if (bufp->regs_allocated == 0)
UNSUPPORTED("bfr9pjr2qri3sxl2k6brn5xer"); //                                 { /* No.  So allocate them with malloc.  We need one
UNSUPPORTED("eo5lmm6txjqzwpnc51q2rr86p"); //                                      extra element beyond `num_regs' for the `-1' marker
UNSUPPORTED("1qr602sxafqljujgz9tt6qcf9"); //                                      GNU code uses.  */
UNSUPPORTED("w1tdx38yyrw1sowe6ghaj2nk"); //                                     regs->num_regs = ((30) > (num_regs + 1) ? (30) : (num_regs + 1));
UNSUPPORTED("be8i44ggcohl9z9hz4rhqk56n"); //                                     regs->start = ((regoff_t *) malloc ((regs->num_regs) * sizeof (regoff_t)));
UNSUPPORTED("cyfi6zngbv1dslzcl7x1sr152"); //                                     regs->end = ((regoff_t *) malloc ((regs->num_regs) * sizeof (regoff_t)));
UNSUPPORTED("cxow73omypjzg053jt8epepo8"); //                                     if (regs->start == (void *)0 || regs->end == (void *)0)
UNSUPPORTED("bho20rvti5y7pi0voz7il3e40"); //                                         {
UNSUPPORTED("eb9qndp4gf1s242vxc2lh5maj"); //                                             do { ; if ((void*) regstart) ((void)0); (void*) regstart = (void *)0; if ((void*) regend) ((void)0); (void*) regend = (void *)0; if ((void*) old_regstart) ((void)0); (void*) old_regstart = (void *)0; if ((void*) old_regend) ((void)0); (void*) old_regend = (void *)0; if ((void*) best_regstart) ((void)0); (void*) best_regstart = (void *)0; if ((void*) best_regend) ((void)0); (void*) best_regend = (void *)0; if ((void*) reg_info) ((void)0); (void*) reg_info = (void *)0; if ((void*) reg_dummy) ((void)0); (void*) reg_dummy = (void *)0; if ((void*) reg_info_dummy) ((void)0); (void*) reg_info_dummy = (void *)0; } while (0);
UNSUPPORTED("dqq88wul2lqxsx8tregfubikv"); //                                             return -2;
UNSUPPORTED("ad3bk4xnx8bgy7plhun3c0mvv"); //                                         }
UNSUPPORTED("i0ztbbhawtrpj2duc3ya6kdq"); //                                     bufp->regs_allocated = 1;
UNSUPPORTED("2tfish0jog6m8uhlhaokmzvm3"); //                                 }
UNSUPPORTED("6c4alibipwc6an6jcy479a3m"); //                             else if (bufp->regs_allocated == 1)
UNSUPPORTED("73h0ws2srjiuc9g7nvfsil3p7"); //                                 { /* Yes.  If we need more elements than were already
UNSUPPORTED("3k4931vs4niesagz1oxg57nbg"); //                                      allocated, reallocate them.  If we need fewer, just
UNSUPPORTED("e6dbjm0iyzngr68xmg81z2z0m"); //                                      leave it alone.  */
UNSUPPORTED("djowzzvskdbwjag9m9fnha4jh"); //                                     if (regs->num_regs < num_regs + 1)
UNSUPPORTED("bho20rvti5y7pi0voz7il3e40"); //                                         {
UNSUPPORTED("9xesw41tb2u8ao5widm29kiyp"); //                                             regs->num_regs = num_regs + 1;
UNSUPPORTED("87dmzxu9te8vokzvs3irtuxov"); //                                             ((regs->start) = (regoff_t *) realloc (regs->start, (regs->num_regs) * sizeof (regoff_t)));
UNSUPPORTED("6e6qfl2iibo9mkatufr410gra"); //                                             ((regs->end) = (regoff_t *) realloc (regs->end, (regs->num_regs) * sizeof (regoff_t)));
UNSUPPORTED("1vrn0xsco7imnquwgs11osuf6"); //                                             if (regs->start == (void *)0 || regs->end == (void *)0)
UNSUPPORTED("1fi5w21ki1vo1jwyr4xwgm27n"); //                                                 {
UNSUPPORTED("93c8iop6pb7zwgnohfyvit0s"); //                                                     do { ; if ((void*) regstart) ((void)0); (void*) regstart = (void *)0; if ((void*) regend) ((void)0); (void*) regend = (void *)0; if ((void*) old_regstart) ((void)0); (void*) old_regstart = (void *)0; if ((void*) old_regend) ((void)0); (void*) old_regend = (void *)0; if ((void*) best_regstart) ((void)0); (void*) best_regstart = (void *)0; if ((void*) best_regend) ((void)0); (void*) best_regend = (void *)0; if ((void*) reg_info) ((void)0); (void*) reg_info = (void *)0; if ((void*) reg_dummy) ((void)0); (void*) reg_dummy = (void *)0; if ((void*) reg_info_dummy) ((void)0); (void*) reg_info_dummy = (void *)0; } while (0);
UNSUPPORTED("7l01rs860ssm9kixuhgx9hdbq"); //                                                     return -2;
UNSUPPORTED("bf03xtomlwkahoqno8ua4cbpc"); //                                                 }
UNSUPPORTED("ad3bk4xnx8bgy7plhun3c0mvv"); //                                         }
UNSUPPORTED("2tfish0jog6m8uhlhaokmzvm3"); //                                 }
UNSUPPORTED("ex6jbb36b02x7vzl22fq0yh2l"); //                             else
UNSUPPORTED("4hzaau620c4rh7xorkrmxfut7"); //                                 {
UNSUPPORTED("3fvt2dndjzd64gdhzxvql5d8b"); //                                     /* These braces fend off a "empty body in an else-statement"
UNSUPPORTED("2luomnvf5pqa41oaxy8f8r9or"); //                                        warning under GCC when assert expands to nothing.  */
UNSUPPORTED("c6tldeu5ffnykyfto7hi9ixau"); //                                     ;
UNSUPPORTED("2tfish0jog6m8uhlhaokmzvm3"); //                                 }
UNSUPPORTED("87z47ijxyqp3enbpawsz2ln6c"); //                             /* Convert the pointer data in `regstart' and `regend' to
UNSUPPORTED("2au9uvntej9ho5mvvkamw1j5w"); //                                indices.  Register zero has to be set differently,
UNSUPPORTED("7hnlk3p3u0dhyz803a3lfbxnj"); //                                since we haven't kept track of any info for it.  */
UNSUPPORTED("35mb19qsz9130m513vyoka7y0"); //                             if (regs->num_regs > 0)
UNSUPPORTED("4hzaau620c4rh7xorkrmxfut7"); //                                 {
UNSUPPORTED("9009sy5tyjmheeqydo5wo4kwq"); //                                     regs->start[0] = pos;
UNSUPPORTED("edxcovxefspqos0b0zuq6z7ha"); //                                     regs->end[0] = ((dend == end_match_1)
UNSUPPORTED("99qit228aypztxnllw89nwokv"); //                                                     ? ((regoff_t) (d - string1))
UNSUPPORTED("4aa9l4flk3x9zqpcce1dwt1ta"); //                                                     : ((regoff_t) (d - string2 + size1)));
UNSUPPORTED("2tfish0jog6m8uhlhaokmzvm3"); //                                 }
UNSUPPORTED("egg8arm7ozeve9mx29jgkz7bs"); //                             /* Go through the first `min (num_regs, regs->num_regs)'
UNSUPPORTED("9c1fouoack9c0oqkqs0d1tbyn"); //                                registers, since that is all we initialized.  */
UNSUPPORTED("76y0zjrbtva0kjq7q5c1wths1"); //                             for (mcnt = 1; (unsigned) mcnt < ((num_regs) < (regs->num_regs) ? (num_regs) : (regs->num_regs));
UNSUPPORTED("1eh3h1x9wgjp35deszyvz3o6e"); //                                  mcnt++)
UNSUPPORTED("4hzaau620c4rh7xorkrmxfut7"); //                                 {
UNSUPPORTED("b0l8ghi3p5kmv8ns6tw59wvuu"); //                                     if (((regstart[mcnt]) == (&reg_unset_dummy)) || ((regend[mcnt]) == (&reg_unset_dummy)))
UNSUPPORTED("nmubntmt76n8354k37ekf09c"); //                                         regs->start[mcnt] = regs->end[mcnt] = -1;
UNSUPPORTED("ecas48ylhxx9t2hquqr1nnwzj"); //                                     else
UNSUPPORTED("bho20rvti5y7pi0voz7il3e40"); //                                         {
UNSUPPORTED("elvx8lxakvbaqd1oax9ecwh6w"); //                                             regs->start[mcnt]
UNSUPPORTED("2ftoc2pu10jb5p4scmxq2z1j2"); //                                                 = (regoff_t) ((size1 && string1 <= (regstart[mcnt]) && (regstart[mcnt]) <= string1 + size1) ? ((regoff_t) ((regstart[mcnt]) - string1)) : ((regoff_t) ((regstart[mcnt]) - string2 + size1)));
UNSUPPORTED("eblc8kac69cvh0xmx81t3z7qw"); //                                             regs->end[mcnt]
UNSUPPORTED("7lahjjs9zz1e6q38t501ceam8"); //                                                 = (regoff_t) ((size1 && string1 <= (regend[mcnt]) && (regend[mcnt]) <= string1 + size1) ? ((regoff_t) ((regend[mcnt]) - string1)) : ((regoff_t) ((regend[mcnt]) - string2 + size1)));
UNSUPPORTED("ad3bk4xnx8bgy7plhun3c0mvv"); //                                         }
UNSUPPORTED("2tfish0jog6m8uhlhaokmzvm3"); //                                 }
UNSUPPORTED("dud4yeckicq4pijwz9m705cmx"); //                             /* If the regs structure we return has more elements than
UNSUPPORTED("dg2liz6izarfhgllr7c0lvfzx"); //                                were in the pattern, set the extra elements to -1.  If
UNSUPPORTED("bgul1odz2k47e5yo08ifpsxux"); //                                we (re)allocated the registers, this is the case,
UNSUPPORTED("8pq9l6jwi4wy8aly679isfqnx"); //                                because we always allocate enough to have at least one
UNSUPPORTED("ccbog2f5misacunpi5lovnpm0"); //                                -1 at the end.  */
UNSUPPORTED("3rm0qbtrqgfau6g7gecmhdspm"); //                             for (mcnt = num_regs; (unsigned) mcnt < regs->num_regs; mcnt++)
UNSUPPORTED("129q7ylakhk2gu6j7p4jpz372"); //                                 regs->start[mcnt] = regs->end[mcnt] = -1;
UNSUPPORTED("93qdoi4cltvsoa26wtattpcrj"); //                         } /* regs && !bufp->no_sub */
UNSUPPORTED("1qe3sa29ntp5400stzdrrbgdg"); //                     ;
UNSUPPORTED("1qe3sa29ntp5400stzdrrbgdg"); //                     ;
UNSUPPORTED("4wc9t8vouh3w18pod77gkuoaz"); //                     mcnt = d - pos - ((dend == end_match_1)
UNSUPPORTED("48tr2aou1bn895ggwv1qgo056"); //                                       ? string1
UNSUPPORTED("e9wr9ptogadx1o3labo85tlgf"); //                                       : string2 - size1);
UNSUPPORTED("1qe3sa29ntp5400stzdrrbgdg"); //                     ;
UNSUPPORTED("8f1nfq1jn2ejt7ub9midgshrh"); //                     do { ; if ((void*) regstart) ((void)0); (void*) regstart = (void *)0; if ((void*) regend) ((void)0); (void*) regend = (void *)0; if ((void*) old_regstart) ((void)0); (void*) old_regstart = (void *)0; if ((void*) old_regend) ((void)0); (void*) old_regend = (void *)0; if ((void*) best_regstart) ((void)0); (void*) best_regstart = (void *)0; if ((void*) best_regend) ((void)0); (void*) best_regend = (void *)0; if ((void*) reg_info) ((void)0); (void*) reg_info = (void *)0; if ((void*) reg_dummy) ((void)0); (void*) reg_dummy = (void *)0; if ((void*) reg_info_dummy) ((void)0); (void*) reg_info_dummy = (void *)0; } while (0);
UNSUPPORTED("c0a9sr78bshk0e2ijyxlzxg7h"); //                     return mcnt;
UNSUPPORTED("7nxu74undh30brb8laojud3f9"); //                 }
UNSUPPORTED("b0kkxy3zkf3f430688gewaj4w"); //             /* Otherwise match next pattern command.  */
UNSUPPORTED("3d9jhchhw3lu8yajca4mi058b"); //             switch (((re_opcode_t) *p++))
UNSUPPORTED("9ua540u2gx5jpu302s81qfxhi"); //                 {
UNSUPPORTED("1cr7m9kscv8emiq2bhdx0l21"); //                     /* Ignore these.  Used to ignore the n of succeed_n's which
UNSUPPORTED("4p7fiu2s102akmzhjtzxqkrdi"); //                        currently have n == 0.  */
UNSUPPORTED("anhhi3sf756j0lcm11sy9nyfc"); //                 case no_op:
UNSUPPORTED("1qe3sa29ntp5400stzdrrbgdg"); //                     ;
UNSUPPORTED("ctqmerohp1f69mb1v1t20jx33"); //                     break;
UNSUPPORTED("8xm4dpngfjaadz6e2suqjuja7"); //                 case succeed:
UNSUPPORTED("1qe3sa29ntp5400stzdrrbgdg"); //                     ;
UNSUPPORTED("97sc3afg030x5rva8s655k37e"); //                     goto succeed_label;
UNSUPPORTED("bsqnanqt8sp33wb2pm6xt6ulq"); //                     /* Match the next n pattern characters exactly.  The following
UNSUPPORTED("4hpiyumzwmrkzpbtwhyro0nin"); //                        byte in the pattern defines n, and the n bytes after that
UNSUPPORTED("126jkfju6a6kncm4twhsy8b50"); //                        are the characters to match.  */
UNSUPPORTED("cw984demptqbnja38a25p2xi1"); //                 case exactn:
UNSUPPORTED("e299xchgd28m8kehs3yk4j0m0"); //                     mcnt = *p++;
UNSUPPORTED("1qe3sa29ntp5400stzdrrbgdg"); //                     ;
UNSUPPORTED("bh28o7qdz7h0u89in1fxom93a"); //                     /* This is written out as an if-else so we don't waste time
UNSUPPORTED("87zph5xuncm4m58n4ggl0buj8"); //                        testing `translate' inside the loop.  */
UNSUPPORTED("7jj6ay2afdj7lpspqsgwusj6m"); //                     if (translate)
UNSUPPORTED("5k2digv672hnrndhc9ktw0oii"); //                         {
UNSUPPORTED("af97jdpx3vxfjkevna4yd8iu3"); //                             do
UNSUPPORTED("4hzaau620c4rh7xorkrmxfut7"); //                                 {
UNSUPPORTED("664ue5cxfdx7fkvvgla8dw3ko"); //                                     while (d == dend) { if (dend == end_match_2) goto fail; d = string2; dend = end_match_2; };
UNSUPPORTED("4nbfd88egqevnftfub4el1t0p"); //                                     if ((unsigned char) translate[(unsigned char) *d++]
UNSUPPORTED("c44weohee2ybnj7w12bc184r0"); //                                         != (unsigned char) *p++)
UNSUPPORTED("bkcykqhkoe9lpop9i2qgzu9yp"); //                                         goto fail;
UNSUPPORTED("2tfish0jog6m8uhlhaokmzvm3"); //                                 }
UNSUPPORTED("74t2bggypeoowelqm6xtisx30"); //                             while (--mcnt);
UNSUPPORTED("b86ovw6olwwo6gnqlt1wqqzb4"); //                         }
UNSUPPORTED("cunk7vpvzj28y1x4gn62gxpce"); //                     else
UNSUPPORTED("5k2digv672hnrndhc9ktw0oii"); //                         {
UNSUPPORTED("af97jdpx3vxfjkevna4yd8iu3"); //                             do
UNSUPPORTED("4hzaau620c4rh7xorkrmxfut7"); //                                 {
UNSUPPORTED("664ue5cxfdx7fkvvgla8dw3ko"); //                                     while (d == dend) { if (dend == end_match_2) goto fail; d = string2; dend = end_match_2; };
UNSUPPORTED("aozx58qal2txvazcjwrwyio1g"); //                                     if (*d++ != (char) *p++) goto fail;
UNSUPPORTED("2tfish0jog6m8uhlhaokmzvm3"); //                                 }
UNSUPPORTED("74t2bggypeoowelqm6xtisx30"); //                             while (--mcnt);
UNSUPPORTED("b86ovw6olwwo6gnqlt1wqqzb4"); //                         }
UNSUPPORTED("2o2dzkzmcvnlj1d5ychre7cqo"); //                     do { if (!set_regs_matched_done) { active_reg_t r; set_regs_matched_done = 1; for (r = lowest_active_reg; r <= highest_active_reg; r++) { ((reg_info[r]).bits.matched_something) = ((reg_info[r]).bits.ever_matched_something) = 1; } } } while (0);
UNSUPPORTED("ctqmerohp1f69mb1v1t20jx33"); //                     break;
UNSUPPORTED("7tks8jmj7cqrcj4nyqm3djfno"); //                     /* Match any character except possibly a newline or a null.  */
UNSUPPORTED("1bv8e740f9rna5i036bo292wc"); //                 case anychar:
UNSUPPORTED("1qe3sa29ntp5400stzdrrbgdg"); //                     ;
UNSUPPORTED("6l2kf5tzbvscsr8vy2bos6ng7"); //                     while (d == dend) { if (dend == end_match_2) goto fail; d = string2; dend = end_match_2; };
UNSUPPORTED("avzqzqv7s0tr2npgctz1b2ana"); //                     if ((!(bufp->syntax & ((((((((unsigned long int) 1) << 1) << 1) << 1) << 1) << 1) << 1)) && (translate ? (char) translate[(unsigned char) (*d)] : (*d)) == '\n')
UNSUPPORTED("83t3ma7nbx7fiz0zawdo05ob2"); //                         || (bufp->syntax & (((((((((unsigned long int) 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) && (translate ? (char) translate[(unsigned char) (*d)] : (*d)) == '\000'))
UNSUPPORTED("etjodgz127fwk1r4166wqxg7n"); //                         goto fail;
UNSUPPORTED("2o2dzkzmcvnlj1d5ychre7cqo"); //                     do { if (!set_regs_matched_done) { active_reg_t r; set_regs_matched_done = 1; for (r = lowest_active_reg; r <= highest_active_reg; r++) { ((reg_info[r]).bits.matched_something) = ((reg_info[r]).bits.ever_matched_something) = 1; } } } while (0);
UNSUPPORTED("1qe3sa29ntp5400stzdrrbgdg"); //                     ;
UNSUPPORTED("p26x5fh4zcf5ddyi146lh558"); //                     d++;
UNSUPPORTED("ctqmerohp1f69mb1v1t20jx33"); //                     break;
UNSUPPORTED("2ro4nzmlhhrmitxu9446arhlo"); //                 case charset:
UNSUPPORTED("2zj0t91dpl66cu75cb0h2j1hu"); //                 case charset_not:
UNSUPPORTED("4vdjxw5o61xlsk38ouw1wsypc"); //                     {
UNSUPPORTED("8n4s0kz2yj50t5rvnitti5hyl"); //                         register unsigned char c;
UNSUPPORTED("deppcas7zridwg11cevwayois"); //                         boolean not = (re_opcode_t) *(p - 1) == charset_not;
UNSUPPORTED("6mnx06fa3p4n9bhd9htqjy0il"); //                         ;
UNSUPPORTED("6mf4c0e1oxwfped5hh8u7fz2n"); //                         while (d == dend) { if (dend == end_match_2) goto fail; d = string2; dend = end_match_2; };
UNSUPPORTED("9aaydk7f3la99u6jfo9kyxp5i"); //                         c = (translate ? (char) translate[(unsigned char) (*d)] : (*d)); /* The character to match.  */
UNSUPPORTED("cc3v2ndt3kmkt0r26mvu8ftfi"); //                         /* Cast to `unsigned' instead of `unsigned char' in case the
UNSUPPORTED("2szsk231obr0m7gvx69h0jje"); //                            bit list is a full 32 bytes long.  */
UNSUPPORTED("2bmmfw3dqxl0rnfrjgzxlk2gw"); //                         if (c < (unsigned) (*p * 8)
UNSUPPORTED("721an1atvtz1g2jzxegur68o9"); //                             && p[1 + c / 8] & (1 << (c % 8)))
UNSUPPORTED("16fenxsijr0vmtxzyb8psvofo"); //                             not = !not;
UNSUPPORTED("9pg86i2witmji2iwfc6jhm9j8"); //                         p += 1 + *p;
UNSUPPORTED("6rkrjn87jai9lq2d129w2h7g8"); //                         if (!not) goto fail;
UNSUPPORTED("632iqprva0k4cq9az0mwgy0ua"); //                         do { if (!set_regs_matched_done) { active_reg_t r; set_regs_matched_done = 1; for (r = lowest_active_reg; r <= highest_active_reg; r++) { ((reg_info[r]).bits.matched_something) = ((reg_info[r]).bits.ever_matched_something) = 1; } } } while (0);
UNSUPPORTED("2ddfcenepkfjkw5qk64bs5pjs"); //                         d++;
UNSUPPORTED("605r8o1isen77125aqrohs6ac"); //                         break;
UNSUPPORTED("3e08x1y395304nd0y3uwffvim"); //                     }
UNSUPPORTED("ax93bd9x56qib5q0g9wvm2qcy"); //                     /* The beginning of a group is represented by start_memory.
UNSUPPORTED("e38f63tkhbflv1p396hknxdwy"); //                        The arguments are the register number in the next byte, and the
UNSUPPORTED("c64u5xem18b4mqy79877eqfcs"); //                        number of groups inner to this one in the next.  The text
UNSUPPORTED("dbdk515uk79zg5hip4kru378u"); //                        matched within the group is recorded (in the internal
UNSUPPORTED("8j4ek9641uur5fxjgkizkb06i"); //                        registers data structure) under the register number.  */
UNSUPPORTED("6c0ei34e676sv2kl1dxfzm5lm"); //                 case start_memory:
UNSUPPORTED("1qe3sa29ntp5400stzdrrbgdg"); //                     ;
UNSUPPORTED("dus38gizylomi1jon9xa9a9gi"); //                     /* Find out if this group can match the empty string.  */
UNSUPPORTED("1e0kjdjtknuzgjuge4nu2rvvv"); //                     p1 = p;             /* To send to group_match_null_string_p.  */
UNSUPPORTED("1lvbl8fbh2kfnyp76ial3wyge"); //                     if (((reg_info[*p]).bits.match_null_string_p) == 3)
UNSUPPORTED("1yvuh3ye9g4wks9vy2jwlla11"); //                         ((reg_info[*p]).bits.match_null_string_p)
UNSUPPORTED("crq9r37sdaimjd51v7rr5rifz"); //                             = group_match_null_string_p (&p1, pend, reg_info);
UNSUPPORTED("ea7tbs2n5vp5hwe31r9ym79d1"); //                     /* Save the position in the string where we were the last time
UNSUPPORTED("b64kvidnxk3y1txigfo1pl99t"); //                        we were at this open-group operator in case the group is
UNSUPPORTED("6w4zb5o8illk23kubyigl5wig"); //                        operated upon by a repetition operator, e.g., with `(a*)*b'
UNSUPPORTED("9d7meouvzhwtqek5lj1q19d3n"); //                        against `ab'; then we want to ignore where we are now in
UNSUPPORTED("elzx65ahdc47yfgprdxj1sjl1"); //                        the string in case this attempt to match fails.  */
UNSUPPORTED("1qsu4c7bkuq3vofwjjijw1e3k"); //                     old_regstart[*p] = ((reg_info[*p]).bits.match_null_string_p)
UNSUPPORTED("6fd5qfd27ra7djj22yvjcis5j"); //                         ? ((regstart[*p]) == (&reg_unset_dummy)) ? d : regstart[*p]
UNSUPPORTED("eg76mx8hl74su6aktlwej5s9l"); //                         : regstart[*p];
UNSUPPORTED("1qe3sa29ntp5400stzdrrbgdg"); //                     ;
UNSUPPORTED("837i1cwpv2l0eyw1m5ly72e5q"); //                     regstart[*p] = d;
UNSUPPORTED("1qe3sa29ntp5400stzdrrbgdg"); //                     ;
UNSUPPORTED("1pjvhqen01q14gt1u2ipmfn78"); //                     ((reg_info[*p]).bits.is_active) = 1;
UNSUPPORTED("7yf870aytlplqgsdoktbv23o8"); //                     ((reg_info[*p]).bits.matched_something) = 0;
UNSUPPORTED("5qe9dgngpf1z0x2gaouwzuh33"); //                     /* Clear this whenever we change the register activity status.  */
UNSUPPORTED("dckkxudermmywleu21wcsh6wk"); //                     set_regs_matched_done = 0;
UNSUPPORTED("4v5t4lf3m98srbm2dhq6otf2z"); //                     /* This is the new highest active register.  */
UNSUPPORTED("3z1903inbnuh0u7jl2xrmgjwy"); //                     highest_active_reg = *p;
UNSUPPORTED("c5um95y8m6g52hnhowkayvp2f"); //                     /* If nothing was active before, this is the new lowest active
UNSUPPORTED("33rqqd71rbnr2br416p5pko3h"); //                        register.  */
UNSUPPORTED("bbhu9dtas1n5xtephxw36fi5o"); //                     if (lowest_active_reg == ((1 << 8) + 1))
UNSUPPORTED("2zewvmpnwbg7dwzrf032pngut"); //                         lowest_active_reg = *p;
UNSUPPORTED("247xejwk6ewf4ot2xglm0foep"); //                     /* Move past the register number and inner group count.  */
UNSUPPORTED("8mazj3o2k4ts6oo83xpc0clf6"); //                     p += 2;
UNSUPPORTED("9h673b93wmz9abfvqdehryv9v"); //                     just_past_start_mem = p;
UNSUPPORTED("ctqmerohp1f69mb1v1t20jx33"); //                     break;
UNSUPPORTED("958t8ufjo5g39llb7x2vk8gey"); //                     /* The stop_memory opcode represents the end of a group.  Its
UNSUPPORTED("3diz7dyrj8z41hkn0gv0u90nj"); //                        arguments are the same as start_memory's: the register
UNSUPPORTED("ancmrtd12048bir4um1a42lk2"); //                        number, and the number of inner groups.  */
UNSUPPORTED("3q89uw4g68y7kyiitvliyykpb"); //                 case stop_memory:
UNSUPPORTED("1qe3sa29ntp5400stzdrrbgdg"); //                     ;
UNSUPPORTED("6myxkco5k76etfo4hu8yr5wbc"); //                     /* We need to save the string position the last time we were at
UNSUPPORTED("79ss3fgfp7q2srolhcfv565wc"); //                        this close-group operator in case the group is operated
UNSUPPORTED("4g7o1h1hqyb3ed7o7xoukat2e"); //                        upon by a repetition operator, e.g., with `((a*)*(b*)*)*'
UNSUPPORTED("7ya5jxfxzzwl6gbtsm5gp06kq"); //                        against `aba'; then we want to ignore where we are now in
UNSUPPORTED("elzx65ahdc47yfgprdxj1sjl1"); //                        the string in case this attempt to match fails.  */
UNSUPPORTED("6tbn6td0l64kefdcsz4l5v4aq"); //                     old_regend[*p] = ((reg_info[*p]).bits.match_null_string_p)
UNSUPPORTED("6ei747c3h9zuuwuuoeuutcbvc"); //                         ? ((regend[*p]) == (&reg_unset_dummy)) ? d : regend[*p]
UNSUPPORTED("8mmdpk59qqy95iwnf47y5p6dl"); //                         : regend[*p];
UNSUPPORTED("1qe3sa29ntp5400stzdrrbgdg"); //                     ;
UNSUPPORTED("1g18yrcifqxtxpfho7si6eeta"); //                     regend[*p] = d;
UNSUPPORTED("1qe3sa29ntp5400stzdrrbgdg"); //                     ;
UNSUPPORTED("7vycocnhdpq8h26dxroqf6qv3"); //                     /* This register isn't active anymore.  */
UNSUPPORTED("7ntazk8yalx2z9fbvgnqmgjyj"); //                     ((reg_info[*p]).bits.is_active) = 0;
UNSUPPORTED("5qe9dgngpf1z0x2gaouwzuh33"); //                     /* Clear this whenever we change the register activity status.  */
UNSUPPORTED("dckkxudermmywleu21wcsh6wk"); //                     set_regs_matched_done = 0;
UNSUPPORTED("42clmiwrmzlsyiozfpc5z4nc9"); //                     /* If this was the only register active, nothing is active
UNSUPPORTED("1ujxcs2n921afe94v57lwlhbh"); //                        anymore.  */
UNSUPPORTED("63r84tgp0e98zrp5vtjft0j7v"); //                     if (lowest_active_reg == highest_active_reg)
UNSUPPORTED("5k2digv672hnrndhc9ktw0oii"); //                         {
UNSUPPORTED("8kmp96mmwzp28qyx61vh0flaz"); //                             lowest_active_reg = ((1 << 8) + 1);
UNSUPPORTED("7x2sqxkjont50piw66yo0dtpp"); //                             highest_active_reg = (1 << 8);
UNSUPPORTED("b86ovw6olwwo6gnqlt1wqqzb4"); //                         }
UNSUPPORTED("cunk7vpvzj28y1x4gn62gxpce"); //                     else
UNSUPPORTED("c1jewz2a8ue76qm3v9zzr62p2"); //                         { /* We must scan for the new highest active register, since
UNSUPPORTED("decsayupwmti5a04jbhouqvoo"); //                              it isn't necessarily one less than now: consider
UNSUPPORTED("2xmzbvq25xm2umlpe67f0xxcl"); //                              (a(b)c(d(e)f)g).  When group 3 ends, after the f), the
UNSUPPORTED("epks85v9quym7a8m6zf2fqi3"); //                              new highest active register is 1.  */
UNSUPPORTED("2txbhcyz9wjvsf90upkqlpnc2"); //                             unsigned char r = *p - 1;
UNSUPPORTED("5734gbg5fpueutgkx1nymooqp"); //                             while (r > 0 && !((reg_info[r]).bits.is_active))
UNSUPPORTED("bdia7d21cgyoa54eec4z33p89"); //                                 r--;
UNSUPPORTED("8n4iy0kw6ecl8rffe6zhoim7f"); //                             /* If we end up at register zero, that means that we saved
UNSUPPORTED("euyoh0d7li8ww59zhb7bcmwss"); //                                the registers as the result of an `on_failure_jump', not
UNSUPPORTED("9b4zvqn8u6k8s7opuitogjuop"); //                                a `start_memory', and we jumped to past the innermost
UNSUPPORTED("2qdslp2g91aru02gnj2jzfngo"); //                                `stop_memory'.  For example, in ((.)*) we save
UNSUPPORTED("c49yz0o5hen5lvybntd5j2fns"); //                                registers 1 and 2 as a result of the *, but when we pop
UNSUPPORTED("eqfql6meyzhinquampkweqzy6"); //                                back to the second ), we are at the stop_memory 1.
UNSUPPORTED("3y5zlgk5fpwzvouq2l6ssxmd1"); //                                Thus, nothing is active.  */
UNSUPPORTED("34eseuqzde8opwrs60ycvdlou"); //                             if (r == 0)
UNSUPPORTED("4hzaau620c4rh7xorkrmxfut7"); //                                 {
UNSUPPORTED("6sxj2komvl2u668mxzv7uw1bk"); //                                     lowest_active_reg = ((1 << 8) + 1);
UNSUPPORTED("4wvntivmvrdiicobd01fuopc1"); //                                     highest_active_reg = (1 << 8);
UNSUPPORTED("2tfish0jog6m8uhlhaokmzvm3"); //                                 }
UNSUPPORTED("ex6jbb36b02x7vzl22fq0yh2l"); //                             else
UNSUPPORTED("2gpimcvwuqcsn5m8kxgtrur9s"); //                                 highest_active_reg = r;
UNSUPPORTED("b86ovw6olwwo6gnqlt1wqqzb4"); //                         }
UNSUPPORTED("6tnujp0ji1y0x3qf7nnoewlwi"); //                     /* If just failed to match something this time around with a
UNSUPPORTED("c2a0wegxd9akp346q7kd756xk"); //                        group that's operated on by a repetition operator, try to
UNSUPPORTED("5sojth3d9jro95jciohpo41gv"); //                        force exit from the ``loop'', and restore the register
UNSUPPORTED("6y1gydjqgpmctcds7f5sg7r7a"); //                        information for this group that we had before trying this
UNSUPPORTED("7k609wk6ordqikf4gj9wim2mb"); //                        last match.  */
UNSUPPORTED("drsdlw7kcvkqlu9vl2ekb5pva"); //                     if ((!((reg_info[*p]).bits.matched_something)
UNSUPPORTED("djhystklzfwsz99ch5tg2mdul"); //                          || just_past_start_mem == p - 1)
UNSUPPORTED("73puidl4bjed2y05glwuha2tr"); //                         && (p + 2) < pend)
UNSUPPORTED("5k2digv672hnrndhc9ktw0oii"); //                         {
UNSUPPORTED("5bmgzdh5rp0tqqvuep6upitw5"); //                             boolean is_a_jump_n = 0;
UNSUPPORTED("6i6e11jwv13din36enbo5swaa"); //                             p1 = p + 2;
UNSUPPORTED("wr9bhelppzmwslf6sw3zxs9m"); //                             mcnt = 0;
UNSUPPORTED("8y47wte4d9uar38b22v7dqq3g"); //                             switch ((re_opcode_t) *p1++)
UNSUPPORTED("4hzaau620c4rh7xorkrmxfut7"); //                                 {
UNSUPPORTED("bok1esp1zekmbuklmyk3rk5rx"); //                                 case jump_n:
UNSUPPORTED("38gwuf75sc2x4fup5mqsis4x6"); //                                     is_a_jump_n = 1;
UNSUPPORTED("a8ugqe6z5croeioatypl4otaw"); //                                 case pop_failure_jump:
UNSUPPORTED("75n3w229uzeshyir5i61r1043"); //                                 case maybe_pop_jump:
UNSUPPORTED("6l3m2qx4riktyve4e2l0wh9fe"); //                                 case jump:
UNSUPPORTED("dnliw06x9ef4nef4vszyc6y2o"); //                                 case dummy_failure_jump:
UNSUPPORTED("4sq4b65a0bh8lf919g64pqica"); //                                     do { do { (mcnt) = *(p1) & 0377; (mcnt) += ((((unsigned char) (*((p1) + 1))) ^ 128) - 128) << 8; } while (0); (p1) += 2; } while (0);
UNSUPPORTED("c286v7bq7t229rlti3rgsn6i4"); //                                     if (is_a_jump_n)
UNSUPPORTED("70d9iq4l9znncby73z47xsgqd"); //                                         p1 += 2;
UNSUPPORTED("1fg4xctmq8acy7m0oob45guaz"); //                                     break;
UNSUPPORTED("cyzkq50b2b248lxpi6lj66jqs"); //                                 default:
UNSUPPORTED("2oxjzlotqn0ba3akkxgs9h02q"); //                                     /* do nothing */ ;
UNSUPPORTED("2tfish0jog6m8uhlhaokmzvm3"); //                                 }
UNSUPPORTED("8yasklhrgy6q7ca1rahwnp80l"); //                             p1 += mcnt;
UNSUPPORTED("efql51mwt6tl2tkzt0wc69svs"); //                             /* If the next operation is a jump backwards in the pattern
UNSUPPORTED("b1jwyhb9r9kdtn75700ivpm08"); //                                to an on_failure_jump right before the start_memory
UNSUPPORTED("32ioduqtcldpe0p955ukfx3zq"); //                                corresponding to this stop_memory, exit from the loop
UNSUPPORTED("7u1pbravuknawsh6n44eiljij"); //                                by forcing a failure after pushing on the stack the
UNSUPPORTED("1rtxd5d0unekapkzuv0h8bfs9"); //                                on_failure_jump's jump in the pattern, and d.  */
UNSUPPORTED("49ilcm43sqxuhnrflxjzpkyrz"); //                             if (mcnt < 0 && (re_opcode_t) *p1 == on_failure_jump
UNSUPPORTED("bbntf9gewbymuo5dr9q61l4ad"); //                                 && (re_opcode_t) p1[3] == start_memory && p1[4] == *p)
UNSUPPORTED("4hzaau620c4rh7xorkrmxfut7"); //                                 {
UNSUPPORTED("82lu6y87gmwdhb71nwh4nyhlo"); //                                     /* If this group ever matched anything, then restore
UNSUPPORTED("2hafp02wsznqsqyi5m15pval5"); //                                        what its registers were before trying this last
UNSUPPORTED("d7lxjfsfhmoewr3xbgu83ccq6"); //                                        failed match, e.g., with `(a*)*b' against `ab' for
UNSUPPORTED("2a1hjbfps60tel0gx3rexgyr7"); //                                        regstart[1], and, e.g., with `((a*)*(b*)*)*'
UNSUPPORTED("c9ye99yclkdsse5qxjva6xyqv"); //                                        against `aba' for regend[3].
UNSUPPORTED("dwcg6me58csfjm6h83aoldo1o"); //                                        Also restore the registers for inner groups for,
UNSUPPORTED("ainjqejclhkthdqm7hwkfddil"); //                                        e.g., `((a*)(b*))*' against `aba' (register 3 would
UNSUPPORTED("8je91go3933r1lpui4r55qp19"); //                                        otherwise get trashed).  */
UNSUPPORTED("1eohn7q2w1elvkijli82m25w4"); //                                     if (((reg_info[*p]).bits.ever_matched_something))
UNSUPPORTED("bho20rvti5y7pi0voz7il3e40"); //                                         {
UNSUPPORTED("bah7hcsvx8y54udzheryy1hzy"); //                                             unsigned r;
UNSUPPORTED("8akg97mqbnr924ugno750b17t"); //                                             ((reg_info[*p]).bits.ever_matched_something) = 0;
UNSUPPORTED("7m9djhctqynaualhx1nqdnjc0"); //                                             /* Restore this and inner groups' (if any) registers.  */
UNSUPPORTED("7js4crblmtp6bs951tlz5pibu"); //                                             for (r = *p; r < (unsigned) *p + (unsigned) *(p + 1);
UNSUPPORTED("41t6k9ezn45tjk4rj4o95hvw7"); //                                                  r++)
UNSUPPORTED("1fi5w21ki1vo1jwyr4xwgm27n"); //                                                 {
UNSUPPORTED("89r32r5193oexr1uoui24rp55"); //                                                     regstart[r] = old_regstart[r];
UNSUPPORTED("ie5lviszvuyotv6i5dfa0pe5"); //                                                     /* xx why this test?  */
UNSUPPORTED("caayksps71ouk9rtbgpk336bb"); //                                                     if (old_regend[r] >= regstart[r])
UNSUPPORTED("13lqnupq8b5xodzv3xff3oze1"); //                                                         regend[r] = old_regend[r];
UNSUPPORTED("bf03xtomlwkahoqno8ua4cbpc"); //                                                 }
UNSUPPORTED("ad3bk4xnx8bgy7plhun3c0mvv"); //                                         }
UNSUPPORTED("3aju47l7l855mt7l00l7xg11q"); //                                     p1++;
UNSUPPORTED("4sq4b65a0bh8lf919g64pqica"); //                                     do { do { (mcnt) = *(p1) & 0377; (mcnt) += ((((unsigned char) (*((p1) + 1))) ^ 128) - 128) << 8; } while (0); (p1) += 2; } while (0);
UNSUPPORTED("a9p5b9oxka3gxpe3wgzvhfj4b"); //                                     do { char *destination; s_reg_t this_reg; ; ; ; ; ; ; ; while (((fail_stack).size - (fail_stack).avail) < (((0 ? 0 : highest_active_reg - lowest_active_reg + 1) * 3) + 4)) { if (!((fail_stack).size > (unsigned) (re_max_failures * (5 * 3 + 4)) ? 0 : ((fail_stack).stack = (fail_stack_elt_t *) (destination = (char *) alloca (((fail_stack).size << 1) * sizeof(fail_stack_elt_t)), bcopy ((fail_stack).stack, destination, (fail_stack).size * sizeof(fail_stack_elt_t)), destination), (fail_stack).stack == (void *)0 ? 0 : ((fail_stack).size <<= 1, 1)))) return -2; ; ; } ; if (1) for (this_reg = lowest_active_reg; this_reg <= highest_active_reg; this_reg++) { ; ; ; fail_stack.stack[fail_stack.avail++].pointer = (unsigned char *) (regstart[this_reg]); ; fail_stack.stack[fail_stack.avail++].pointer = (unsigned char *) (regend[this_reg]); ; ; ; ; ; ; fail_stack.stack[fail_stack.avail++] = (reg_info[this_reg].word); } ; fail_stack.stack[fail_stack.avail++].integer = (lowest_active_reg); ; fail_stack.stack[fail_stack.avail++].integer = (highest_active_reg); ; ; fail_stack.stack[fail_stack.avail++].pointer = (unsigned char *) (p1 + mcnt); ; ; ; fail_stack.stack[fail_stack.avail++].pointer = (unsigned char *) (d); ; ; } while (0);
UNSUPPORTED("3cl1in74qmmbb26vs4qw3mcuv"); //                                     goto fail;
UNSUPPORTED("2tfish0jog6m8uhlhaokmzvm3"); //                                 }
UNSUPPORTED("b86ovw6olwwo6gnqlt1wqqzb4"); //                         }
UNSUPPORTED("2wofa1c2dglau2ufwmcabib0n"); //                     /* Move past the register number and the inner group count.  */
UNSUPPORTED("8mazj3o2k4ts6oo83xpc0clf6"); //                     p += 2;
UNSUPPORTED("ctqmerohp1f69mb1v1t20jx33"); //                     break;
UNSUPPORTED("7ckwgnb7aasatvtyteljdqc2k"); //                     /* \<digit> has been turned into a `duplicate' command which is
UNSUPPORTED("dmy8z2x1ttnsvz6kx6l9j4rtv"); //                        followed by the numeric value of <digit> as the register number.  */
UNSUPPORTED("9zkejga7r0tbainhrb6zuw9os"); //                 case duplicate:
UNSUPPORTED("4vdjxw5o61xlsk38ouw1wsypc"); //                     {
UNSUPPORTED("27u228yvq9k655bp1tq40v02i"); //                         register const char *d2, *dend2;
UNSUPPORTED("bq38n54ifn0fjxun9mtdpyxkj"); //                         int regno = *p++;   /* Get which register to match against.  */
UNSUPPORTED("6mnx06fa3p4n9bhd9htqjy0il"); //                         ;
UNSUPPORTED("91m4oa28hi3m5f5icw5pumfab"); //                         /* Can't back reference a group which we've never matched.  */
UNSUPPORTED("9r6debcfgkrh9r4029g0p2tsz"); //                         if (((regstart[regno]) == (&reg_unset_dummy)) || ((regend[regno]) == (&reg_unset_dummy)))
UNSUPPORTED("1k6xdu198jqq8v2oj3209y50s"); //                             goto fail;
UNSUPPORTED("dng36k157gcr0rr6l2hysesr7"); //                         /* Where in input to try to start matching.  */
UNSUPPORTED("209civ9l0k19m94b62pp4080r"); //                         d2 = regstart[regno];
UNSUPPORTED("eexsdzuwnhjynwkc8g0u6hnwm"); //                         /* Where to stop matching; if both the place to start and
UNSUPPORTED("bup7nhzrolgesclm7ac7j3pke"); //                            the place to stop matching are in the same string, then
UNSUPPORTED("8kg60w180bvkgrv4dgbrd491u"); //                            set to the place to stop, otherwise, for now have to use
UNSUPPORTED("9tumayloc0nncf60m1sfzkjhl"); //                            the end of the first string.  */
UNSUPPORTED("9dfy4gfwxkdnc83mqwy6mg4hw"); //                         dend2 = (((size1 && string1 <= (regstart[regno]) && (regstart[regno]) <= string1 + size1)
UNSUPPORTED("c0t5y6icuv24moevxaqu2h4zx"); //                                   == (size1 && string1 <= (regend[regno]) && (regend[regno]) <= string1 + size1))
UNSUPPORTED("ebo6m6ze117x4ioed7wvc6omz"); //                                  ? regend[regno] : end_match_1);
UNSUPPORTED("1nx31c51hb376ceydwsae35jq"); //                         for (;;)
UNSUPPORTED("9aq2ft2quyeattze0cwemwweo"); //                             {
UNSUPPORTED("6bj5pjn8kja9qic9u7at0g4va"); //                                 /* If necessary, advance to next segment in register
UNSUPPORTED("8qcz04tc3rbrg7kjuf9xezhks"); //                                    contents.  */
UNSUPPORTED("braae8dlluapap8zd5o6ihc1u"); //                                 while (d2 == dend2)
UNSUPPORTED("3trws9zymzc11hz9w8m2b4fl9"); //                                     {
UNSUPPORTED("47ijn5xqcu6vix8sa88nyj6g3"); //                                         if (dend2 == end_match_2) break;
UNSUPPORTED("5wcf12qydzz346az1zp6sbphr"); //                                         if (dend2 == regend[regno]) break;
UNSUPPORTED("36wliwq9pfherfng9wg1z2930"); //                                         /* End of string1 => advance to string2. */
UNSUPPORTED("97sxan60njmrt6lx6dhy0t35a"); //                                         d2 = string2;
UNSUPPORTED("yedssw060ohvbc7klzkqa7ky"); //                                         dend2 = regend[regno];
UNSUPPORTED("2v7ai112w6nt2xgs1gbzo22oe"); //                                     }
UNSUPPORTED("a3i8zhtb0tt9m1ilvwxr0yry6"); //                                 /* At end of register contents => success */
UNSUPPORTED("9zvbjhvrz5uc7k4zmhbwdfvr6"); //                                 if (d2 == dend2) break;
UNSUPPORTED("afb4xzyk6gdokmo985hyfw8c9"); //                                 /* If necessary, advance to next segment in data.  */
UNSUPPORTED("ey839udf98rupfliazw91iwf4"); //                                 while (d == dend) { if (dend == end_match_2) goto fail; d = string2; dend = end_match_2; };
UNSUPPORTED("ecd7rg6163cvfm8v1rtt0jtpb"); //                                 /* How many characters left in this segment to match.  */
UNSUPPORTED("az6v71e6bfrxdlm5zelm70aji"); //                                 mcnt = dend - d;
UNSUPPORTED("96v7a28oxrisen4iasx18f02h"); //                                 /* Want how many consecutive characters we can match in
UNSUPPORTED("1iug3sdumn7i4romdq11ea8gc"); //                                    one shot, so, if necessary, adjust the count.  */
UNSUPPORTED("k5cjd1jaihwrz80htbwhcz1k"); //                                 if (mcnt > dend2 - d2)
UNSUPPORTED("8sygn6fz8wegxhzaxpof9nfdy"); //                                     mcnt = dend2 - d2;
UNSUPPORTED("cq9zkdhm0s7gm3of2ojllwxev"); //                                 /* Compare that many; failure if mismatch, else move
UNSUPPORTED("cwb3st7nyauiej3bt6si7nk"); //                                    past them.  */
UNSUPPORTED("8yrz36iknz8u14syi843ya22w"); //                                 if (translate
UNSUPPORTED("1csum7liyqa4kjt5vzjgwl055"); //                                     ? bcmp_translate (d, d2, mcnt, translate)
UNSUPPORTED("2mfklscfw9ja01nrbw8irsd34"); //                                     : bcmp (d, d2, mcnt))
UNSUPPORTED("3cl1in74qmmbb26vs4qw3mcuv"); //                                     goto fail;
UNSUPPORTED("ejde9u9b01jxuvulwaibu252x"); //                                 d += mcnt, d2 += mcnt;
UNSUPPORTED("egy6z5e2fbinnxr0gts3znpv4"); //                                 /* Do this because we've match some characters.  */
UNSUPPORTED("b1re2muw6n42ivurg658dizzz"); //                                 do { if (!set_regs_matched_done) { active_reg_t r; set_regs_matched_done = 1; for (r = lowest_active_reg; r <= highest_active_reg; r++) { ((reg_info[r]).bits.matched_something) = ((reg_info[r]).bits.ever_matched_something) = 1; } } } while (0);
UNSUPPORTED("13jcwbk3vyfh9xrmwi5hbe7so"); //                             }
UNSUPPORTED("3e08x1y395304nd0y3uwffvim"); //                     }
UNSUPPORTED("ctqmerohp1f69mb1v1t20jx33"); //                     break;
UNSUPPORTED("if344u3tlfkgjfkh9bwgpq8a"); //                     /* begline matches the empty string at the beginning of the string
UNSUPPORTED("ezwcv5j2ponvqm9xcbipsht8u"); //                        (unless `not_bol' is set in `bufp'), and, if
UNSUPPORTED("5elzkvlxjqh3r6z34t1j5y21o"); //                        `newline_anchor' is set, after newlines.  */
UNSUPPORTED("7zj5ggx1l6hln4ude6h5p4hgw"); //                 case begline:
UNSUPPORTED("1qe3sa29ntp5400stzdrrbgdg"); //                     ;
UNSUPPORTED("9vfz6wya8x71wz4tl74mnon03"); //                     if (((d) == (size1 ? string1 : string2) || !size2))
UNSUPPORTED("5k2digv672hnrndhc9ktw0oii"); //                         {
UNSUPPORTED("5xiy4c6bz8y6xryzgmegy61lb"); //                             if (!bufp->not_bol) break;
UNSUPPORTED("b86ovw6olwwo6gnqlt1wqqzb4"); //                         }
UNSUPPORTED("5vfekfkt5mncyuqxnse5u327k"); //                     else if (d[-1] == '\n' && bufp->newline_anchor)
UNSUPPORTED("5k2digv672hnrndhc9ktw0oii"); //                         {
UNSUPPORTED("7mosouhqcis2k8sbg88g9wol8"); //                             break;
UNSUPPORTED("b86ovw6olwwo6gnqlt1wqqzb4"); //                         }
UNSUPPORTED("cmda966opn67h0f3cl7lk5oaa"); //                     /* In all other cases, we fail.  */
UNSUPPORTED("9s7xl8jyc2iazjidoga852mei"); //                     goto fail;
UNSUPPORTED("6ab5d4s6v9zc6g10652lkpfxb"); //                     /* endline is the dual of begline.  */
UNSUPPORTED("8pqai98vstdak51ot078dn1ut"); //                 case endline:
UNSUPPORTED("1qe3sa29ntp5400stzdrrbgdg"); //                     ;
UNSUPPORTED("ruaqj050rr07ll8b70yjadcf"); //                     if (((d) == end2))
UNSUPPORTED("5k2digv672hnrndhc9ktw0oii"); //                         {
UNSUPPORTED("3zg6eo8m095cishibd0umbzff"); //                             if (!bufp->not_eol) break;
UNSUPPORTED("b86ovw6olwwo6gnqlt1wqqzb4"); //                         }
UNSUPPORTED("1n7gdt1mnk1dwyec8i0pk3vkx"); //                     /* We have to ``prefetch'' the next character.  */
UNSUPPORTED("67ozwfkze7vtbzog58zzkcvh7"); //                     else if ((d == end1 ? *string2 : *d) == '\n'
UNSUPPORTED("8whjciz1pz1blross2jpqly5f"); //                              && bufp->newline_anchor)
UNSUPPORTED("5k2digv672hnrndhc9ktw0oii"); //                         {
UNSUPPORTED("7mosouhqcis2k8sbg88g9wol8"); //                             break;
UNSUPPORTED("b86ovw6olwwo6gnqlt1wqqzb4"); //                         }
UNSUPPORTED("9s7xl8jyc2iazjidoga852mei"); //                     goto fail;
UNSUPPORTED("1aa2xpr66utsdr273h0vay7rp"); //                     /* Match at the very beginning of the data.  */
UNSUPPORTED("889sq08u5lsi3c6y2z7adu8ex"); //                 case begbuf:
UNSUPPORTED("1qe3sa29ntp5400stzdrrbgdg"); //                     ;
UNSUPPORTED("9vfz6wya8x71wz4tl74mnon03"); //                     if (((d) == (size1 ? string1 : string2) || !size2))
UNSUPPORTED("605r8o1isen77125aqrohs6ac"); //                         break;
UNSUPPORTED("9s7xl8jyc2iazjidoga852mei"); //                     goto fail;
UNSUPPORTED("4z8rppnva31fi9mwshpp8kxvw"); //                     /* Match at the very end of the data.  */
UNSUPPORTED("cx4ezn3s02koh0pq7idm2xvxc"); //                 case endbuf:
UNSUPPORTED("1qe3sa29ntp5400stzdrrbgdg"); //                     ;
UNSUPPORTED("ruaqj050rr07ll8b70yjadcf"); //                     if (((d) == end2))
UNSUPPORTED("605r8o1isen77125aqrohs6ac"); //                         break;
UNSUPPORTED("9s7xl8jyc2iazjidoga852mei"); //                     goto fail;
UNSUPPORTED("3wki3bf5yoz1vc1fprg2lncya"); //                     /* on_failure_keep_string_jump is used to optimize `.*\n'.  It
UNSUPPORTED("pql30urhclx8h7lod7pnj4ip"); //                        pushes NULL as the value for the string on the stack.  Then
UNSUPPORTED("dzwsld9trwycslc5k99jr9muh"); //                        `pop_failure_point' will keep the current value for the
UNSUPPORTED("3xl98pouep4b4iwcgj8vvabvc"); //                        string, instead of restoring it.  To see why, consider
UNSUPPORTED("8wxp3mbfp1jdn6hlq6djbv5gj"); //                        matching `foo\nbar' against `.*\n'.  The .* matches the foo;
UNSUPPORTED("djf7x62cv5c4kahfoqaehjrk9"); //                        then the . fails against the \n.  But the next thing we want
UNSUPPORTED("4n6sotfrko2ssux5cwdwroytq"); //                        to do is match the \n against the \n; if we restored the
UNSUPPORTED("9gm1fhuma2g5732wasioh293h"); //                        string value, we would be back at the foo.
UNSUPPORTED("9pocj5oo194wevmdb2pun171k"); //                        Because this is used only in specific cases, we don't need to
UNSUPPORTED("22xw129jbwcohdom5on8mruf1"); //                        check all the things that `on_failure_jump' does, to make
UNSUPPORTED("ov1lrnfw4f23mi84a3odmle4"); //                        sure the right things get saved on the stack.  Hence we don't
UNSUPPORTED("8xgl4y5ygq098dtwrbuwey6gq"); //                        share its code.  The only reason to push anything on the
UNSUPPORTED("by06u3v9h2tr9d1hfpggutrts"); //                        stack at all is that otherwise we would have to change
UNSUPPORTED("cd2otw100j4q2pgbijt7jkkzt"); //                        `anychar's code to do something besides goto fail in this
UNSUPPORTED("32dqefvhlxi1u35k3ewta7mrl"); //                        case; that seems worse than this.  */
UNSUPPORTED("753el6ua6knou432p32d1kbcc"); //                 case on_failure_keep_string_jump:
UNSUPPORTED("1qe3sa29ntp5400stzdrrbgdg"); //                     ;
UNSUPPORTED("cxpumlm6doboe0ubkx51bqcx1"); //                     do { do { (mcnt) = *(p) & 0377; (mcnt) += ((((unsigned char) (*((p) + 1))) ^ 128) - 128) << 8; } while (0); (p) += 2; } while (0);
UNSUPPORTED("1qe3sa29ntp5400stzdrrbgdg"); //                     ;
UNSUPPORTED("4l5nbs7foacbvjtuv07q72uuo"); //                     do { char *destination; s_reg_t this_reg; ; ; ; ; ; ; ; while (((fail_stack).size - (fail_stack).avail) < (((0 ? 0 : highest_active_reg - lowest_active_reg + 1) * 3) + 4)) { if (!((fail_stack).size > (unsigned) (re_max_failures * (5 * 3 + 4)) ? 0 : ((fail_stack).stack = (fail_stack_elt_t *) (destination = (char *) alloca (((fail_stack).size << 1) * sizeof(fail_stack_elt_t)), bcopy ((fail_stack).stack, destination, (fail_stack).size * sizeof(fail_stack_elt_t)), destination), (fail_stack).stack == (void *)0 ? 0 : ((fail_stack).size <<= 1, 1)))) return -2; ; ; } ; if (1) for (this_reg = lowest_active_reg; this_reg <= highest_active_reg; this_reg++) { ; ; ; fail_stack.stack[fail_stack.avail++].pointer = (unsigned char *) (regstart[this_reg]); ; fail_stack.stack[fail_stack.avail++].pointer = (unsigned char *) (regend[this_reg]); ; ; ; ; ; ; fail_stack.stack[fail_stack.avail++] = (reg_info[this_reg].word); } ; fail_stack.stack[fail_stack.avail++].integer = (lowest_active_reg); ; fail_stack.stack[fail_stack.avail++].integer = (highest_active_reg); ; ; fail_stack.stack[fail_stack.avail++].pointer = (unsigned char *) (p + mcnt); ; ; ; fail_stack.stack[fail_stack.avail++].pointer = (unsigned char *) ((void *)0); ; ; } while (0);
UNSUPPORTED("ctqmerohp1f69mb1v1t20jx33"); //                     break;
UNSUPPORTED("18mowhc7dokjwwbpjm3c0jjz3"); //                     /* Uses of on_failure_jump:
UNSUPPORTED("6hohd1mhntprbr29uarcgbh63"); //                        Each alternative starts with an on_failure_jump that points
UNSUPPORTED("9ltnucad533b0qouy73o7txqh"); //                        to the beginning of the next alternative.  Each alternative
UNSUPPORTED("56exr10nxswdfl6el06yzn1kl"); //                        except the last ends with a jump that in effect jumps past
UNSUPPORTED("7mx68jjud7wzo6avh55bb818j"); //                        the rest of the alternatives.  (They really jump to the
UNSUPPORTED("9nytxeg7tfysw0cho7phep44z"); //                        ending jump of the following alternative, because tensioning
UNSUPPORTED("ax1r80aorhdnf7k83i580xkrd"); //                        these jumps is a hassle.)
UNSUPPORTED("b3w92k7mmdeshyrsyx5j12h3g"); //                        Repeats start with an on_failure_jump that points past both
UNSUPPORTED("aef21o4yg56obwkotdy9xg0ho"); //                        the repetition text and either the following jump or
UNSUPPORTED("3wjc6x34irsiejqg1kbgn3iyt"); //                        pop_failure_jump back to this on_failure_jump.  */
UNSUPPORTED("992rmruvdtrxnk46307iiqyjo"); //                 case on_failure_jump:
UNSUPPORTED("c776gsd8agjizu0u6zgrf49u3"); //                 on_failure:
UNSUPPORTED("7xijz1d7skix4nbsjyzbkawyz"); //                 ;
UNSUPPORTED("6rhr1dl5qu710655mp1bqo0i6"); //                 do { do { (mcnt) = *(p) & 0377; (mcnt) += ((((unsigned char) (*((p) + 1))) ^ 128) - 128) << 8; } while (0); (p) += 2; } while (0);
UNSUPPORTED("7xijz1d7skix4nbsjyzbkawyz"); //                 ;
UNSUPPORTED("f46w21fc8pt03jhpnaepq03um"); //                 /* If this on_failure_jump comes right before a group (i.e.,
UNSUPPORTED("4osoy23qvwvclpht018wy5fe"); //                    the original * applied to a group), save the information
UNSUPPORTED("bfbi4c30km7bjt8vdpfguqx3b"); //                    for that group and all inner ones, so that if we fail back
UNSUPPORTED("1ra232qtft0mi461dabhedxp4"); //                    to this point, the group's information will be correct.
UNSUPPORTED("b2mqhhpsbx6j245sm4leiu3dn"); //                    For example, in \(a*\)*\1, we need the preceding group,
UNSUPPORTED("6do6go4hsy6lmqykgor4s9ygv"); //                    and in \(zz\(a*\)b*\)\2, we need the inner group.  */
UNSUPPORTED("enwxe23t405zzw5yadk743a5r"); //                 /* We can't use `p' to check ahead because we push
UNSUPPORTED("6yxbydq88le4or0xwwq5siqmw"); //                    a failure point to `p + mcnt' after we do this.  */
UNSUPPORTED("di4spe1yrvg37sczy634usb7k"); //                 p1 = p;
UNSUPPORTED("816d2v18z6b4clqeuef1am657"); //                 /* We need to skip no_op's before we look for the
UNSUPPORTED("ch11lyve93ighowv1prcy2j8k"); //                    start_memory in case this on_failure_jump is happening as
UNSUPPORTED("6qg7rlv2r0m6k4j5x75pimlaa"); //                    the result of a completed succeed_n, as in \(a\)\{1,3\}b\1
UNSUPPORTED("978ibng6ccfuidf65t6s6mgq0"); //                    against aba.  */
UNSUPPORTED("ajcmgk4q2zko19taooby9ydhf"); //                 while (p1 < pend && (re_opcode_t) *p1 == no_op)
UNSUPPORTED("4hemvstzejy8d3hmd82kukj7v"); //                     p1++;
UNSUPPORTED("6srqj3lww8szsp64bd64yus7k"); //                 if (p1 < pend && (re_opcode_t) *p1 == start_memory)
UNSUPPORTED("4vdjxw5o61xlsk38ouw1wsypc"); //                     {
UNSUPPORTED("54s4csu4aujalr4c0swqowr5k"); //                         /* We have a new highest active register now.  This will
UNSUPPORTED("4ig8j5bvaq59fsf9hfdn1q5ys"); //                            get reset at the start_memory we are about to get to,
UNSUPPORTED("6aatr095mst5lfh4soia9qpg2"); //                            but we will have saved all the registers relevant to
UNSUPPORTED("2a6e61mk8qy9pwnls9zifuok6"); //                            this repetition op, as described above.  */
UNSUPPORTED("50t6ewzx8ghn8hjs3qg8z17sc"); //                         highest_active_reg = *(p1 + 1) + *(p1 + 2);
UNSUPPORTED("bxlrtrilu12d2jxucjuex0vfp"); //                         if (lowest_active_reg == ((1 << 8) + 1))
UNSUPPORTED("6li0h0b9p3vauo9vgyypu8fux"); //                             lowest_active_reg = *(p1 + 1);
UNSUPPORTED("3e08x1y395304nd0y3uwffvim"); //                     }
UNSUPPORTED("7xijz1d7skix4nbsjyzbkawyz"); //                 ;
UNSUPPORTED("4637n0f945d4uznxowpb1ha4i"); //                 do { char *destination; s_reg_t this_reg; ; ; ; ; ; ; ; while (((fail_stack).size - (fail_stack).avail) < (((0 ? 0 : highest_active_reg - lowest_active_reg + 1) * 3) + 4)) { if (!((fail_stack).size > (unsigned) (re_max_failures * (5 * 3 + 4)) ? 0 : ((fail_stack).stack = (fail_stack_elt_t *) (destination = (char *) alloca (((fail_stack).size << 1) * sizeof(fail_stack_elt_t)), bcopy ((fail_stack).stack, destination, (fail_stack).size * sizeof(fail_stack_elt_t)), destination), (fail_stack).stack == (void *)0 ? 0 : ((fail_stack).size <<= 1, 1)))) return -2; ; ; } ; if (1) for (this_reg = lowest_active_reg; this_reg <= highest_active_reg; this_reg++) { ; ; ; fail_stack.stack[fail_stack.avail++].pointer = (unsigned char *) (regstart[this_reg]); ; fail_stack.stack[fail_stack.avail++].pointer = (unsigned char *) (regend[this_reg]); ; ; ; ; ; ; fail_stack.stack[fail_stack.avail++] = (reg_info[this_reg].word); } ; fail_stack.stack[fail_stack.avail++].integer = (lowest_active_reg); ; fail_stack.stack[fail_stack.avail++].integer = (highest_active_reg); ; ; fail_stack.stack[fail_stack.avail++].pointer = (unsigned char *) (p + mcnt); ; ; ; fail_stack.stack[fail_stack.avail++].pointer = (unsigned char *) (d); ; ; } while (0);
UNSUPPORTED("d1pumbibe8xz2i7gr1wj6zdak"); //                 break;
UNSUPPORTED("ezli68qam5jhg7sd2n0z30pa1"); //                 /* A smart repeat ends with `maybe_pop_jump'.
UNSUPPORTED("5d7n4fkgg5a38sjzmkhaoa8hl"); //                    We change it to either `pop_failure_jump' or `jump'.  */
UNSUPPORTED("d7ziafib9p2sa0rqh34hv3hwj"); //                 case maybe_pop_jump:
UNSUPPORTED("cxpumlm6doboe0ubkx51bqcx1"); //                     do { do { (mcnt) = *(p) & 0377; (mcnt) += ((((unsigned char) (*((p) + 1))) ^ 128) - 128) << 8; } while (0); (p) += 2; } while (0);
UNSUPPORTED("1qe3sa29ntp5400stzdrrbgdg"); //                     ;
UNSUPPORTED("4vdjxw5o61xlsk38ouw1wsypc"); //                     {
UNSUPPORTED("4i49ynnzyaszs2krb0aw4uqa0"); //                         register unsigned char *p2 = p;
UNSUPPORTED("8b4raalmwo7274mwsk0kcdlbp"); //                         /* Compare the beginning of the repeat with what in the
UNSUPPORTED("6ox8v5j7jznz10vsvok39lx6h"); //                            pattern follows its end. If we can establish that there
UNSUPPORTED("1qm38p3f1vojknhw7v973ela0"); //                            is nothing that they would both match, i.e., that we
UNSUPPORTED("2wnj0iemov40iryz5lsfp3zi5"); //                            would have to backtrack because of (as in, e.g., `a*a')
UNSUPPORTED("9zp4vdc61w9qqpto61p74x8x0"); //                            then we can change to pop_failure_jump, because we'll
UNSUPPORTED("cdtoaohseagznp9y8u81kbpru"); //                            never have to backtrack.
UNSUPPORTED("63w8z2r32mq0x6xowpwds9vjb"); //                            This is not true in the case of alternatives: in
UNSUPPORTED("a98qcnnc9pzff1gk8kfj32f1i"); //                            `(a|ab)*' we do need to backtrack to the `ab' alternative
UNSUPPORTED("errex35giie09vkvn4e9fe4zk"); //                            (e.g., if the string was `ab').  But instead of trying to
UNSUPPORTED("1147ja0a6sr58jg8z4hjs19ji"); //                            detect that here, the alternative has put on a dummy
UNSUPPORTED("64mr7nyur7w7m85zrdx15fqli"); //                            failure point which is what we will end up popping.  */
UNSUPPORTED("amvyb0hgs5zfdebpexntlvwfd"); //                         /* Skip over open/close-group commands.
UNSUPPORTED("2lx4i6yu241papyqk442d7gf1"); //                            If what follows this loop is a ...+ construct,
UNSUPPORTED("1pvxwmxmctfoyisu33wn2tgym"); //                            look at what begins its body, since we will have to
UNSUPPORTED("8tbga6r8dqnryesfg5bmccsge"); //                            match at least one of that.  */
UNSUPPORTED("9nuuiq5ff5vu3ehueiarndup8"); //                         while (1)
UNSUPPORTED("9aq2ft2quyeattze0cwemwweo"); //                             {
UNSUPPORTED("2dak6ikzsj12jm54tispfishb"); //                                 if (p2 + 2 < pend
UNSUPPORTED("7yw7v0svrd5pmn1jcqlc4e3u1"); //                                     && ((re_opcode_t) *p2 == stop_memory
UNSUPPORTED("2yx468hmjjmhvuzxlu2jyltzc"); //                                         || (re_opcode_t) *p2 == start_memory))
UNSUPPORTED("4hgr2e6ls0cizy5kicpo9i13a"); //                                     p2 += 3;
UNSUPPORTED("1yuf55oz0qpsj5aqp5h5fpr42"); //                                 else if (p2 + 6 < pend
UNSUPPORTED("bw2og6j9w6s47jv3rzusqgra5"); //                                          && (re_opcode_t) *p2 == dummy_failure_jump)
UNSUPPORTED("6ennx95kxfn7n27tkdpz2xiun"); //                                     p2 += 6;
UNSUPPORTED("a4hbs6luulhltmygux2zimbcc"); //                                 else
UNSUPPORTED("1fg4xctmq8acy7m0oob45guaz"); //                                     break;
UNSUPPORTED("13jcwbk3vyfh9xrmwi5hbe7so"); //                             }
UNSUPPORTED("d2fh23u27kss8fzwuwtp0p6sf"); //                         p1 = p + mcnt;
UNSUPPORTED("90n7ynf24mp5fmsi2nznee8aj"); //                         /* p1[0] ... p1[2] are the `on_failure_jump' corresponding
UNSUPPORTED("4vso45anytez4cjlpkfnzk1ul"); //                            to the `maybe_finalize_jump' of this case.  Examine what
UNSUPPORTED("b0juiqz00zb3viboquom9nj05"); //                            follows.  */
UNSUPPORTED("66jpg0e4bmb6ltcah5ku9m5wf"); //                         /* If we're at the end of the pattern, we can change.  */
UNSUPPORTED("5xkeog52k82g0w44z3mfurqp7"); //                         if (p2 == pend)
UNSUPPORTED("9aq2ft2quyeattze0cwemwweo"); //                             {
UNSUPPORTED("e25zbxyj1st10oslj3hfvjw9a"); //                                 /* Consider what happens when matching ":\(.*\)"
UNSUPPORTED("1qa8sj8fsagixe1jkmm9j7s4u"); //                                    against ":/".  I don't really understand this code
UNSUPPORTED("bawpwuc0i5d4c4v2365l038h3"); //                                    yet.  */
UNSUPPORTED("5zj568kdmyc87wog3sgbfzsd2"); //                                 p[-3] = (unsigned char) pop_failure_jump;
UNSUPPORTED("9fy78dg2q1uu4c2t1zsyaestf"); //                                 ;
UNSUPPORTED("13jcwbk3vyfh9xrmwi5hbe7so"); //                             }
UNSUPPORTED("2wredhzqyuxjyd1qy0vxr02p6"); //                         else if ((re_opcode_t) *p2 == exactn
UNSUPPORTED("5439ysolyacwtm0iroxxof9ja"); //                                  || (bufp->newline_anchor && (re_opcode_t) *p2 == endline))
UNSUPPORTED("9aq2ft2quyeattze0cwemwweo"); //                             {
UNSUPPORTED("6ailij025yibbye551dmh9is0"); //                                 register unsigned char c
UNSUPPORTED("68vch1mgcskmseij0qhms2rl9"); //                                     = *p2 == (unsigned char) endline ? '\n' : p2[2];
UNSUPPORTED("1yo80wyjqsiw1t17we6c8a6gr"); //                                 if ((re_opcode_t) p1[3] == exactn && p1[5] != c)
UNSUPPORTED("3trws9zymzc11hz9w8m2b4fl9"); //                                     {
UNSUPPORTED("agl6h59gsalgrbxzq7fk4n0vn"); //                                         p[-3] = (unsigned char) pop_failure_jump;
UNSUPPORTED("czvlqjtrpajrtul735yf6cmfz"); //                                         ;
UNSUPPORTED("2v7ai112w6nt2xgs1gbzo22oe"); //                                     }
UNSUPPORTED("exal5xqo67c7zaxk3gaoj3n0i"); //                                 else if ((re_opcode_t) p1[3] == charset
UNSUPPORTED("e0kae24mabhgjjynwkmainteq"); //                                          || (re_opcode_t) p1[3] == charset_not)
UNSUPPORTED("3trws9zymzc11hz9w8m2b4fl9"); //                                     {
UNSUPPORTED("do2ngnlega9hi7rlbi6pwnera"); //                                         int not = (re_opcode_t) p1[3] == charset_not;
UNSUPPORTED("5f33xcwhul4d1gb3t7lj27aam"); //                                         if (c < (unsigned char) (p1[4] * 8)
UNSUPPORTED("78cradlgzgss8hx6xr31uun4d"); //                                             && p1[5 + c / 8] & (1 << (c % 8)))
UNSUPPORTED("7n22hwonng6xigiqxvbruxroo"); //                                             not = !not;
UNSUPPORTED("dpe5oc4fbt7r8mffvrkacad7a"); //                                         /* `not' is equal to 1 if c would match, which means
UNSUPPORTED("bqyj06a9kascwc6t354y5gh61"); //                                            that we can't change to pop_failure_jump.  */
UNSUPPORTED("z5vz9r2m8zndo9aeyvz3w07z"); //                                         if (!not)
UNSUPPORTED("el767oi7fhlc4apdbx8dsvnk6"); //                                             {
UNSUPPORTED("6aq9gjy2dwkpua80yonp1njtn"); //                                                 p[-3] = (unsigned char) pop_failure_jump;
UNSUPPORTED("4eruune9bfskm05zy5ipimp2s"); //                                                 ;
UNSUPPORTED("b5m0ttlk23avgs4i47o8lbzg1"); //                                             }
UNSUPPORTED("2v7ai112w6nt2xgs1gbzo22oe"); //                                     }
UNSUPPORTED("13jcwbk3vyfh9xrmwi5hbe7so"); //                             }
UNSUPPORTED("3ra1229golg94oou1y8zbvkkp"); //                         else if ((re_opcode_t) *p2 == charset)
UNSUPPORTED("9aq2ft2quyeattze0cwemwweo"); //                             {
UNSUPPORTED("auubej4lkmydm7wamwki5zgyp"); //                                     if ((re_opcode_t) p1[3] == exactn
UNSUPPORTED("6zebpplfmgrbk07lmll1yrrsi"); //                                         && ! ((int) p2[1] * 8 > (int) p1[4]
UNSUPPORTED("9wp8kzivj0sustlx4ftn0l36h"); //                                               && (p2[2 + p1[4] / 8]
UNSUPPORTED("eyu63dq7uwiiwzvudc2gopty4"); //                                                   & (1 << (p1[4] % 8)))))
UNSUPPORTED("bho20rvti5y7pi0voz7il3e40"); //                                         {
UNSUPPORTED("8azfcbpc7h84lhyicbrd2yjqp"); //                                             p[-3] = (unsigned char) pop_failure_jump;
UNSUPPORTED("a6r7lto035pp3gvk1bdixang2"); //                                             ;
UNSUPPORTED("ad3bk4xnx8bgy7plhun3c0mvv"); //                                         }
UNSUPPORTED("dcwjh55x5f2wjp4ezmipdxn2b"); //                                     else if ((re_opcode_t) p1[3] == charset_not)
UNSUPPORTED("bho20rvti5y7pi0voz7il3e40"); //                                         {
UNSUPPORTED("7gajjg3j6bbfnu7tfpv8vd1he"); //                                             int idx;
UNSUPPORTED("dxd6z46rfort3j8amp5v1n0wb"); //                                             /* We win if the charset_not inside the loop
UNSUPPORTED("5r3347hzknz05fu925j2m38f9"); //                                                lists every character listed in the charset after.  */
UNSUPPORTED("drl6mtwfmrng8pxbu6xzqfb18"); //                                             for (idx = 0; idx < (int) p2[1]; idx++)
UNSUPPORTED("9gmmtilb47lw5pq4j4sejrbj"); //                                                 if (! (p2[2 + idx] == 0
UNSUPPORTED("74zwa83c3pcunsiarfams873p"); //                                                        || (idx < (int) p1[4]
UNSUPPORTED("3ytcwj384elha7evsxusun8r2"); //                                                            && ((p2[2 + idx] & ~ p1[5 + idx]) == 0))))
UNSUPPORTED("1lzsscwayr2ygeagix6ouorzi"); //                                                     break;
UNSUPPORTED("etst8xjhq076idz2ki2lkc3kl"); //                                             if (idx == p2[1])
UNSUPPORTED("1fi5w21ki1vo1jwyr4xwgm27n"); //                                                 {
UNSUPPORTED("gi02bd0hnoko53b18tp6muxp"); //                                                     p[-3] = (unsigned char) pop_failure_jump;
UNSUPPORTED("7ia8gpv947o65avjiwl96gln5"); //                                                     ;
UNSUPPORTED("bf03xtomlwkahoqno8ua4cbpc"); //                                                 }
UNSUPPORTED("ad3bk4xnx8bgy7plhun3c0mvv"); //                                         }
UNSUPPORTED("bfp6jy3jhgcx1qlqs2ugtk84o"); //                                     else if ((re_opcode_t) p1[3] == charset)
UNSUPPORTED("bho20rvti5y7pi0voz7il3e40"); //                                         {
UNSUPPORTED("7gajjg3j6bbfnu7tfpv8vd1he"); //                                             int idx;
UNSUPPORTED("3gf62oi7ics60q83j50ht4j54"); //                                             /* We win if the charset inside the loop
UNSUPPORTED("axtgnnoh8n7p39m9fbbxipnxm"); //                                                has no overlap with the one after the loop.  */
UNSUPPORTED("6fsyvlzx4skvg5xqoif9n2bli"); //                                             for (idx = 0;
UNSUPPORTED("6d7whyux0667vbtt3aoh8j8mc"); //                                                  idx < (int) p2[1] && idx < (int) p1[4];
UNSUPPORTED("d4o8f5f71ai8ygtqvrs77062h"); //                                                  idx++)
UNSUPPORTED("b029onao79pzr7fwgf9oboalu"); //                                                 if ((p2[2 + idx] & p1[5 + idx]) != 0)
UNSUPPORTED("1lzsscwayr2ygeagix6ouorzi"); //                                                     break;
UNSUPPORTED("6whqfrc9hpkwoqoqk2cpgstzz"); //                                             if (idx == p2[1] || idx == p1[4])
UNSUPPORTED("1fi5w21ki1vo1jwyr4xwgm27n"); //                                                 {
UNSUPPORTED("gi02bd0hnoko53b18tp6muxp"); //                                                     p[-3] = (unsigned char) pop_failure_jump;
UNSUPPORTED("7ia8gpv947o65avjiwl96gln5"); //                                                     ;
UNSUPPORTED("bf03xtomlwkahoqno8ua4cbpc"); //                                                 }
UNSUPPORTED("ad3bk4xnx8bgy7plhun3c0mvv"); //                                         }
UNSUPPORTED("13jcwbk3vyfh9xrmwi5hbe7so"); //                             }
UNSUPPORTED("3e08x1y395304nd0y3uwffvim"); //                     }
UNSUPPORTED("dtbtyud0b7t5ily0nm0qkjrov"); //                     p -= 2;             /* Point at relative address again.  */
UNSUPPORTED("9hexwrt0gnjztdabmcy13w96a"); //                     if ((re_opcode_t) p[-1] != pop_failure_jump)
UNSUPPORTED("5k2digv672hnrndhc9ktw0oii"); //                         {
UNSUPPORTED("ca1ptlw6k6dixmzbwm3a4iusp"); //                             p[-1] = (unsigned char) jump;
UNSUPPORTED("e4kt1tmmevqvy3cbyk6xnznck"); //                             ;
UNSUPPORTED("578iyred8hniiy6kj99i6zcx7"); //                             goto unconditional_jump;
UNSUPPORTED("b86ovw6olwwo6gnqlt1wqqzb4"); //                         }
UNSUPPORTED("et4hfzxq1ccol7oenwuqop34c"); //                     /* Note fall through.  */
UNSUPPORTED("evkl6mpyrcnjwer6edp6j9emc"); //                     /* The end of a simple repeat has a pop_failure_jump back to
UNSUPPORTED("2jcckuegby5tkaf50t6wemnmz"); //                        its matching on_failure_jump, where the latter will push a
UNSUPPORTED("5971g2j40ktdkl0khlo1gpoy6"); //                        failure point.  The pop_failure_jump takes off failure
UNSUPPORTED("byi3j1ugndbk3u7yo028ikqz3"); //                        points put on by this pop_failure_jump's matching
UNSUPPORTED("48etkzu6pax8j2rcmbcbd2s1k"); //                        on_failure_jump; we got through the pattern to here from the
UNSUPPORTED("8f9as95a103nww8jbbn2vx61n"); //                        matching on_failure_jump, so didn't fail.  */
UNSUPPORTED("6coluo8cd7scxlqwnbvt3jqs5"); //                 case pop_failure_jump:
UNSUPPORTED("4vdjxw5o61xlsk38ouw1wsypc"); //                     {
UNSUPPORTED("aaykmyayp7k026o2rgh0xnxtt"); //                         /* We need to pass separate storage for the lowest and
UNSUPPORTED("3w39tfh3tm57hhf0ob12sj5yk"); //                            highest registers, even though we don't care about the
UNSUPPORTED("angsr7u35nqld9m876d6dx7xq"); //                            actual values.  Otherwise, we will restore only one
UNSUPPORTED("4ixesek08pb5l67pi00fqsriy"); //                            register from the stack, since lowest will == highest in
UNSUPPORTED("1rzihep5lcgvrl52235a9a2uy"); //                            `pop_failure_point'.  */
UNSUPPORTED("9m9v5kmsap0mtdznppskik3k"); //                         active_reg_t dummy_low_reg, dummy_high_reg;
UNSUPPORTED("5z662sre8crmvzwkqdm96nybf"); //                         unsigned char *pdummy;
UNSUPPORTED("35v2cyfyscndlz7rd41acza4q"); //                         const char *sdummy;
UNSUPPORTED("6mnx06fa3p4n9bhd9htqjy0il"); //                         ;
UNSUPPORTED("3g97rrgu1b7a7ep0xjxi3013b"); //                         {  s_reg_t this_reg; const unsigned char *string_temp; ; ; ; ; ; ; ; string_temp = fail_stack.stack[--fail_stack.avail].pointer; if (string_temp != (void *)0) sdummy = (const char *) string_temp; ; ; ; pdummy = (unsigned char *) fail_stack.stack[--fail_stack.avail].pointer; ; ; dummy_high_reg = (active_reg_t) fail_stack.stack[--fail_stack.avail].integer; ; 
UNSUPPORTED("1zjxjg81gnrzmbjfvorezv23b"); //  dummy_low_reg = (active_reg_t) fail_stack.stack[--fail_stack.avail].integer; ; if (1) for (this_reg = dummy_high_reg; this_reg >= 
UNSUPPORTED("5y05eeb8zcu1hnp1r7u72wea0"); //  dummy_low_reg; this_reg--) { ; reg_info_dummy[this_reg].word = fail_stack.stack[--fail_stack.avail]; ; reg_dummy[this_reg] = (const char *) fail_stack.stack[--fail_stack.avail].pointer; ; 
UNSUPPORTED("170qj0cyzc65li5h2h8nibj1a"); //  reg_dummy[this_reg] = (const char *) fail_stack.stack[--fail_stack.avail].pointer; ; } else { for (this_reg = highest_active_reg; this_reg > dummy_high_reg; this_reg--) { reg_info_dummy[this_reg].word.integer = 0; reg_dummy[this_reg] = 0; 
UNSUPPORTED("o5cb1sy4lw7ahxhb9ir34ywe"); //  reg_dummy[this_reg] = 0; } highest_active_reg = dummy_high_reg; } set_regs_matched_done = 0; ; };
UNSUPPORTED("3e08x1y395304nd0y3uwffvim"); //                     }
UNSUPPORTED("et4hfzxq1ccol7oenwuqop34c"); //                     /* Note fall through.  */
UNSUPPORTED("7jzfafc0e4ysay5587d498v0g"); //                 unconditional_jump:
UNSUPPORTED("7xijz1d7skix4nbsjyzbkawyz"); //                 ;
UNSUPPORTED("lbfv20cb4iwb8ilhovngktwp"); //                 /* Note fall through.  */
UNSUPPORTED("6d0r5m74qtbvaned81fau8l5f"); //                 /* Unconditionally jump (without popping any failure points).  */
UNSUPPORTED("9a1qj2mhg3oh4ehsjpp4d81on"); //                 case jump:
UNSUPPORTED("39nxr5mia7bkm0tyl5uoxm1i0"); //                     do { do { (mcnt) = *(p) & 0377; (mcnt) += ((((unsigned char) (*((p) + 1))) ^ 128) - 128) << 8; } while (0); (p) += 2; } while (0);  /* Get the amount to jump.  */
UNSUPPORTED("1qe3sa29ntp5400stzdrrbgdg"); //                     ;
UNSUPPORTED("ard612jdkjyh07vvh6p7xec5q"); //                     p += mcnt;                          /* Do the jump.  */
UNSUPPORTED("1qe3sa29ntp5400stzdrrbgdg"); //                     ;
UNSUPPORTED("ctqmerohp1f69mb1v1t20jx33"); //                     break;
UNSUPPORTED("6eoc6h9g64ctzuyv0zhwzsp88"); //                     /* We need this opcode so we can detect where alternatives end
UNSUPPORTED("6m0nust2xxmwf4b1plaq3zle2"); //                        in `group_match_null_string_p' et al.  */
UNSUPPORTED("7ifq76or69643hqib3roiq9f4"); //                 case jump_past_alt:
UNSUPPORTED("1qe3sa29ntp5400stzdrrbgdg"); //                     ;
UNSUPPORTED("8efvwfkzonjvc0xgk7ss31fh2"); //                     goto unconditional_jump;
UNSUPPORTED("eiucbu410s7gabqq4tu08bg8c"); //                     /* Normally, the on_failure_jump pushes a failure point, which
UNSUPPORTED("ctsxylfj3puoijkz434hjg4jg"); //                        then gets popped at pop_failure_jump.  We will end up at
UNSUPPORTED("7ociskstxycbbkh41vyojlnby"); //                        pop_failure_jump, also, and with a pattern of, say, `a+', we
UNSUPPORTED("7n2w2rey1jvho9kvlfgz2mero"); //                        are skipping over the on_failure_jump, so we have to push
UNSUPPORTED("ciwekfad3iz5mqjje8hiorvda"); //                        something meaningless for pop_failure_jump to pop.  */
UNSUPPORTED("ejqzfph0mmtiqin1hmi981bzm"); //                 case dummy_failure_jump:
UNSUPPORTED("1qe3sa29ntp5400stzdrrbgdg"); //                     ;
UNSUPPORTED("csbowj5rzwk0s0pqd3urbka91"); //                     /* It doesn't matter what we push for the string here.  What
UNSUPPORTED("4qwf1zf8lyqii172mcx2gdsms"); //                        the code at `fail' tests is the value for the pattern.  */
UNSUPPORTED("359ip48et2ofltl9170r6hayz"); //                     do { char *destination; s_reg_t this_reg; ; ; ; ; ; ; ; while (((fail_stack).size - (fail_stack).avail) < (((0 ? 0 : highest_active_reg - lowest_active_reg + 1) * 3) + 4)) { if (!((fail_stack).size > (unsigned) (re_max_failures * (5 * 3 + 4)) ? 0 : ((fail_stack).stack = (fail_stack_elt_t *) (destination = (char *) alloca (((fail_stack).size << 1) * sizeof(fail_stack_elt_t)), bcopy ((fail_stack).stack, destination, (fail_stack).size * sizeof(fail_stack_elt_t)), destination), (fail_stack).stack == (void *)0 ? 0 : ((fail_stack).size <<= 1, 1)))) return -2; ; ; } ; if (1) for (this_reg = lowest_active_reg; this_reg <= highest_active_reg; this_reg++) { ; ; ; fail_stack.stack[fail_stack.avail++].pointer = (unsigned char *) (regstart[this_reg]); ; fail_stack.stack[fail_stack.avail++].pointer = (unsigned char *) (regend[this_reg]); ; ; ; ; ; ; fail_stack.stack[fail_stack.avail++] = (reg_info[this_reg].word); } ; fail_stack.stack[fail_stack.avail++].integer = (lowest_active_reg); ; fail_stack.stack[fail_stack.avail++].integer = (highest_active_reg); ; ; fail_stack.stack[fail_stack.avail++].pointer = (unsigned char *) (0); ; ; ; fail_stack.stack[fail_stack.avail++].pointer = (unsigned char *) (0); ; ; } while (0);
UNSUPPORTED("8efvwfkzonjvc0xgk7ss31fh2"); //                     goto unconditional_jump;
UNSUPPORTED("9mevu5auyy39j4yiw0b2j9h2o"); //                     /* At the end of an alternative, we need to push a dummy failure
UNSUPPORTED("d2uf5y4x1b2hmxla6j6tlr1rb"); //                        point in case we are followed by a `pop_failure_jump', because
UNSUPPORTED("rnnvhhghy5jgw4jg1fhty1td"); //                        we don't want the failure point for the alternative to be
UNSUPPORTED("dkubbc7oql3ppqcdkgq9qelbn"); //                        popped.  For example, matching `(a|ab)*' against `aab'
UNSUPPORTED("8eoqcy9m0jjec5bxgkzbvg1c7"); //                        requires that we match the `ab' alternative.  */
UNSUPPORTED("9446ac5zvkn7r9yqkh2brh10l"); //                 case push_dummy_failure:
UNSUPPORTED("1qe3sa29ntp5400stzdrrbgdg"); //                     ;
UNSUPPORTED("c992qnyh0yle1kop18y31trwb"); //                     /* See comments just above at `dummy_failure_jump' about the
UNSUPPORTED("5synfaxsw4ed7tb33m3un4m4s"); //                        two zeroes.  */
UNSUPPORTED("359ip48et2ofltl9170r6hayz"); //                     do { char *destination; s_reg_t this_reg; ; ; ; ; ; ; ; while (((fail_stack).size - (fail_stack).avail) < (((0 ? 0 : highest_active_reg - lowest_active_reg + 1) * 3) + 4)) { if (!((fail_stack).size > (unsigned) (re_max_failures * (5 * 3 + 4)) ? 0 : ((fail_stack).stack = (fail_stack_elt_t *) (destination = (char *) alloca (((fail_stack).size << 1) * sizeof(fail_stack_elt_t)), bcopy ((fail_stack).stack, destination, (fail_stack).size * sizeof(fail_stack_elt_t)), destination), (fail_stack).stack == (void *)0 ? 0 : ((fail_stack).size <<= 1, 1)))) return -2; ; ; } ; if (1) for (this_reg = lowest_active_reg; this_reg <= highest_active_reg; this_reg++) { ; ; ; fail_stack.stack[fail_stack.avail++].pointer = (unsigned char *) (regstart[this_reg]); ; fail_stack.stack[fail_stack.avail++].pointer = (unsigned char *) (regend[this_reg]); ; ; ; ; ; ; fail_stack.stack[fail_stack.avail++] = (reg_info[this_reg].word); } ; fail_stack.stack[fail_stack.avail++].integer = (lowest_active_reg); ; fail_stack.stack[fail_stack.avail++].integer = (highest_active_reg); ; ; fail_stack.stack[fail_stack.avail++].pointer = (unsigned char *) (0); ; ; ; fail_stack.stack[fail_stack.avail++].pointer = (unsigned char *) (0); ; ; } while (0);
UNSUPPORTED("ctqmerohp1f69mb1v1t20jx33"); //                     break;
UNSUPPORTED("cch41i8kiaadul49ef19omdn3"); //                     /* Have to succeed matching what follows at least n times.
UNSUPPORTED("8nb8fa32tplp2rqeqbr6hbn7m"); //                        After that, handle like `on_failure_jump'.  */
UNSUPPORTED("bdrmz6krrbsihqbsklz1j7d7a"); //                 case succeed_n:
UNSUPPORTED("84ijqow5jif2ajlhwgtdm91f7"); //                     do { (mcnt) = *(p + 2) & 0377; (mcnt) += ((((unsigned char) (*((p + 2) + 1))) ^ 128) - 128) << 8; } while (0);
UNSUPPORTED("1qe3sa29ntp5400stzdrrbgdg"); //                     ;
UNSUPPORTED("1qe3sa29ntp5400stzdrrbgdg"); //                     ;
UNSUPPORTED("bgjxyv37kr5myctgyhh94vsi6"); //                     /* Originally, this is how many times we HAVE to succeed.  */
UNSUPPORTED("4hf4aztjmayqr0tjd70d8mo5l"); //                     if (mcnt > 0)
UNSUPPORTED("5k2digv672hnrndhc9ktw0oii"); //                         {
UNSUPPORTED("e8xmciie2t26hwi49v7e64msh"); //                             mcnt--;
UNSUPPORTED("f0uzigopn4c90eyozalab8dsx"); //                             p += 2;
UNSUPPORTED("3trn8e2hbd5q9a392rlo55y4x"); //                             do { do { (p)[0] = (mcnt) & 0377; (p)[1] = (mcnt) >> 8; } while (0); (p) += 2; } while (0);
UNSUPPORTED("e4kt1tmmevqvy3cbyk6xnznck"); //                             ;
UNSUPPORTED("b86ovw6olwwo6gnqlt1wqqzb4"); //                         }
UNSUPPORTED("ach1i5r2ivzzx0xwwovmzz94b"); //                     else if (mcnt == 0)
UNSUPPORTED("5k2digv672hnrndhc9ktw0oii"); //                         {
UNSUPPORTED("e4kt1tmmevqvy3cbyk6xnznck"); //                             ;
UNSUPPORTED("8kebz7kyb83b0vya08j7g1h0s"); //                             p[2] = (unsigned char) no_op;
UNSUPPORTED("7naalvvwte5koasfwykgk8jy7"); //                             p[3] = (unsigned char) no_op;
UNSUPPORTED("evigqo62udorsulmaru504u73"); //                             goto on_failure;
UNSUPPORTED("b86ovw6olwwo6gnqlt1wqqzb4"); //                         }
UNSUPPORTED("ctqmerohp1f69mb1v1t20jx33"); //                     break;
UNSUPPORTED("f41u8fntqiy246yy3btlg6w1v"); //                 case jump_n:
UNSUPPORTED("84ijqow5jif2ajlhwgtdm91f7"); //                     do { (mcnt) = *(p + 2) & 0377; (mcnt) += ((((unsigned char) (*((p + 2) + 1))) ^ 128) - 128) << 8; } while (0);
UNSUPPORTED("1qe3sa29ntp5400stzdrrbgdg"); //                     ;
UNSUPPORTED("45q1dm5e8czy7dfyydg2p0qre"); //                     /* Originally, this is how many times we CAN jump.  */
UNSUPPORTED("c925pgmxvmas9boqu5ovt7bwp"); //                     if (mcnt)
UNSUPPORTED("5k2digv672hnrndhc9ktw0oii"); //                         {
UNSUPPORTED("e8xmciie2t26hwi49v7e64msh"); //                             mcnt--;
UNSUPPORTED("bj0025yphmeva1dnjc83w2d82"); //                             do { (p + 2)[0] = (mcnt) & 0377; (p + 2)[1] = (mcnt) >> 8; } while (0);
UNSUPPORTED("e4kt1tmmevqvy3cbyk6xnznck"); //                             ;
UNSUPPORTED("578iyred8hniiy6kj99i6zcx7"); //                             goto unconditional_jump;
UNSUPPORTED("b86ovw6olwwo6gnqlt1wqqzb4"); //                         }
UNSUPPORTED("6xb6uhe949e197w3856eykp9b"); //                     /* If don't have to jump any more, skip over the rest of command.  */
UNSUPPORTED("cunk7vpvzj28y1x4gn62gxpce"); //                     else
UNSUPPORTED("4g9g5t2dqwtbpe7orh2oew0y9"); //                         p += 4;
UNSUPPORTED("ctqmerohp1f69mb1v1t20jx33"); //                     break;
UNSUPPORTED("dmqxgjrlj5ddxj88dw919ujoj"); //                 case set_number_at:
UNSUPPORTED("4vdjxw5o61xlsk38ouw1wsypc"); //                     {
UNSUPPORTED("6mnx06fa3p4n9bhd9htqjy0il"); //                         ;
UNSUPPORTED("5pj3hnv50h51ji8ogyd48xedj"); //                         do { do { (mcnt) = *(p) & 0377; (mcnt) += ((((unsigned char) (*((p) + 1))) ^ 128) - 128) << 8; } while (0); (p) += 2; } while (0);
UNSUPPORTED("d2fh23u27kss8fzwuwtp0p6sf"); //                         p1 = p + mcnt;
UNSUPPORTED("5pj3hnv50h51ji8ogyd48xedj"); //                         do { do { (mcnt) = *(p) & 0377; (mcnt) += ((((unsigned char) (*((p) + 1))) ^ 128) - 128) << 8; } while (0); (p) += 2; } while (0);
UNSUPPORTED("6mnx06fa3p4n9bhd9htqjy0il"); //                         ;
UNSUPPORTED("7iin6qwm1f2j9sjclecva957u"); //                         do { (p1)[0] = (mcnt) & 0377; (p1)[1] = (mcnt) >> 8; } while (0);
UNSUPPORTED("605r8o1isen77125aqrohs6ac"); //                         break;
UNSUPPORTED("3e08x1y395304nd0y3uwffvim"); //                     }
UNSUPPORTED("a8jtmsnmpa4937exo9nhopyyb"); //                 case wordbound:
UNSUPPORTED("4vdjxw5o61xlsk38ouw1wsypc"); //                     {
UNSUPPORTED("1q5kvi9ggao878ze57ikl6vuk"); //                         boolean prevchar, thischar;
UNSUPPORTED("6mnx06fa3p4n9bhd9htqjy0il"); //                         ;
UNSUPPORTED("dugr1bul8t32joy65do0a7h6d"); //                         if (((d) == (size1 ? string1 : string2) || !size2) || ((d) == end2))
UNSUPPORTED("7mosouhqcis2k8sbg88g9wol8"); //                             break;
UNSUPPORTED("8ijy2sdya91ygss8ru67qxx66"); //                         prevchar = (re_syntax_table[(d - 1) == end1 ? *string2 :(d - 1) == string2 - 1 ? *(end1 - 1) : *(d - 1)] == 1);
UNSUPPORTED("33zxkfaori9emq7m5ssmpr1rd"); //                         thischar = (re_syntax_table[(d) == end1 ? *string2 :(d) == string2 - 1 ? *(end1 - 1) : *(d)] == 1);
UNSUPPORTED("79ooz7jacd1z2sfddoakjbcze"); //                         if (prevchar != thischar)
UNSUPPORTED("7mosouhqcis2k8sbg88g9wol8"); //                             break;
UNSUPPORTED("etjodgz127fwk1r4166wqxg7n"); //                         goto fail;
UNSUPPORTED("3e08x1y395304nd0y3uwffvim"); //                     }
UNSUPPORTED("2jqqzr491mztwr9h8hvs55ja1"); //                 case notwordbound:
UNSUPPORTED("4vdjxw5o61xlsk38ouw1wsypc"); //                     {
UNSUPPORTED("1q5kvi9ggao878ze57ikl6vuk"); //                         boolean prevchar, thischar;
UNSUPPORTED("6mnx06fa3p4n9bhd9htqjy0il"); //                         ;
UNSUPPORTED("dugr1bul8t32joy65do0a7h6d"); //                         if (((d) == (size1 ? string1 : string2) || !size2) || ((d) == end2))
UNSUPPORTED("1k6xdu198jqq8v2oj3209y50s"); //                             goto fail;
UNSUPPORTED("8ijy2sdya91ygss8ru67qxx66"); //                         prevchar = (re_syntax_table[(d - 1) == end1 ? *string2 :(d - 1) == string2 - 1 ? *(end1 - 1) : *(d - 1)] == 1);
UNSUPPORTED("33zxkfaori9emq7m5ssmpr1rd"); //                         thischar = (re_syntax_table[(d) == end1 ? *string2 :(d) == string2 - 1 ? *(end1 - 1) : *(d)] == 1);
UNSUPPORTED("79ooz7jacd1z2sfddoakjbcze"); //                         if (prevchar != thischar)
UNSUPPORTED("1k6xdu198jqq8v2oj3209y50s"); //                             goto fail;
UNSUPPORTED("605r8o1isen77125aqrohs6ac"); //                         break;
UNSUPPORTED("3e08x1y395304nd0y3uwffvim"); //                     }
UNSUPPORTED("991vlwoc47i8p67jyv2qapdhy"); //                 case wordbeg:
UNSUPPORTED("1qe3sa29ntp5400stzdrrbgdg"); //                     ;
UNSUPPORTED("721tzo9yoqf86tjgrubhs6lcg"); //                     if ((re_syntax_table[(d) == end1 ? *string2 :(d) == string2 - 1 ? *(end1 - 1) : *(d)] == 1) && (((d) == (size1 ? string1 : string2) || !size2) || !(re_syntax_table[(d - 1) == end1 ? *string2 :(d - 1) == string2 - 1 ? *(end1 - 1) : *(d - 1)] == 1)))
UNSUPPORTED("605r8o1isen77125aqrohs6ac"); //                         break;
UNSUPPORTED("9s7xl8jyc2iazjidoga852mei"); //                     goto fail;
UNSUPPORTED("65os7te2jc1i6uacyszvy5spd"); //                 case wordend:
UNSUPPORTED("1qe3sa29ntp5400stzdrrbgdg"); //                     ;
UNSUPPORTED("88nu0py5i2vups7wk3tj8wcf4"); //                     if (!((d) == (size1 ? string1 : string2) || !size2) && (re_syntax_table[(d - 1) == end1 ? *string2 :(d - 1) == string2 - 1 ? *(end1 - 1) : *(d - 1)] == 1)
UNSUPPORTED("1a0r7hi8r1po69cu5fxqrw4u8"); //                         && (!(re_syntax_table[(d) == end1 ? *string2 :(d) == string2 - 1 ? *(end1 - 1) : *(d)] == 1) || ((d) == end2)))
UNSUPPORTED("605r8o1isen77125aqrohs6ac"); //                         break;
UNSUPPORTED("9s7xl8jyc2iazjidoga852mei"); //                     goto fail;
UNSUPPORTED("buxp1030z7swkjl42wovj9hxd"); //                 case wordchar:
UNSUPPORTED("1qe3sa29ntp5400stzdrrbgdg"); //                     ;
UNSUPPORTED("6l2kf5tzbvscsr8vy2bos6ng7"); //                     while (d == dend) { if (dend == end_match_2) goto fail; d = string2; dend = end_match_2; };
UNSUPPORTED("7qor4v3lsqk2266u914165c3w"); //                     if (!(re_syntax_table[(d) == end1 ? *string2 :(d) == string2 - 1 ? *(end1 - 1) : *(d)] == 1))
UNSUPPORTED("etjodgz127fwk1r4166wqxg7n"); //                         goto fail;
UNSUPPORTED("2o2dzkzmcvnlj1d5ychre7cqo"); //                     do { if (!set_regs_matched_done) { active_reg_t r; set_regs_matched_done = 1; for (r = lowest_active_reg; r <= highest_active_reg; r++) { ((reg_info[r]).bits.matched_something) = ((reg_info[r]).bits.ever_matched_something) = 1; } } } while (0);
UNSUPPORTED("p26x5fh4zcf5ddyi146lh558"); //                     d++;
UNSUPPORTED("ctqmerohp1f69mb1v1t20jx33"); //                     break;
UNSUPPORTED("7frlpnfuvuphd3bzoulwg4vsr"); //                 case notwordchar:
UNSUPPORTED("1qe3sa29ntp5400stzdrrbgdg"); //                     ;
UNSUPPORTED("6l2kf5tzbvscsr8vy2bos6ng7"); //                     while (d == dend) { if (dend == end_match_2) goto fail; d = string2; dend = end_match_2; };
UNSUPPORTED("7efqv32ta2jk6dolelm1jju58"); //                     if ((re_syntax_table[(d) == end1 ? *string2 :(d) == string2 - 1 ? *(end1 - 1) : *(d)] == 1))
UNSUPPORTED("etjodgz127fwk1r4166wqxg7n"); //                         goto fail;
UNSUPPORTED("2o2dzkzmcvnlj1d5ychre7cqo"); //                     do { if (!set_regs_matched_done) { active_reg_t r; set_regs_matched_done = 1; for (r = lowest_active_reg; r <= highest_active_reg; r++) { ((reg_info[r]).bits.matched_something) = ((reg_info[r]).bits.ever_matched_something) = 1; } } } while (0);
UNSUPPORTED("p26x5fh4zcf5ddyi146lh558"); //                     d++;
UNSUPPORTED("ctqmerohp1f69mb1v1t20jx33"); //                     break;
UNSUPPORTED("comxch6w0lbi64ejq9m4p0u7o"); //                 default:
UNSUPPORTED("4ga2fk86no0o6rms0bh0zuh4t"); //                     abort ();
UNSUPPORTED("7nxu74undh30brb8laojud3f9"); //                 }
UNSUPPORTED("9vmmpq61luc1cbbupgqomzlw5"); //             continue;  /* Successfully executed one pattern command; keep going.  */
UNSUPPORTED("9ndw6vvwoffc31wvgwvgra578"); //             /* We goto here if a matching operation fails. */
UNSUPPORTED("1x3juxkh7bw5pjqp84ugtlmce"); //         fail:
UNSUPPORTED("6j68ym8upocfb9ipivxd80ark"); //             if (!(fail_stack.avail == 0))
UNSUPPORTED("92q1l5zz5c3sr57dyzmui3e99"); //                 { /* A restart point is known.  Restore to that state.  */
UNSUPPORTED("1qe3sa29ntp5400stzdrrbgdg"); //                     ;
UNSUPPORTED("ayzf7vhhjnbynnstdi6n454f4"); //                     {  s_reg_t this_reg; const unsigned char *string_temp; ; ; ; ; ; ; ; string_temp = fail_stack.stack[--fail_stack.avail].pointer; if (string_temp != (void *)0) d = (const char *) string_temp; ; ; ; p = (unsigned char *) fail_stack.stack[--fail_stack.avail].pointer; ; ; highest_active_reg = (active_reg_t) fail_stack.stack[--fail_stack.avail].integer; ; 
UNSUPPORTED("cb1z2qn566ao3l0dqcxiow6dp"); //  lowest_active_reg = (active_reg_t) fail_stack.stack[--fail_stack.avail].integer; ; if (1) for (this_reg = highest_active_reg; this_reg >= 
UNSUPPORTED("2g0zjfujimxzkr97d2g902f35"); //  lowest_active_reg; this_reg--) { ; reg_info[this_reg].word = fail_stack.stack[--fail_stack.avail]; ; regend[this_reg] = (const char *) fail_stack.stack[--fail_stack.avail].pointer; ; 
UNSUPPORTED("2qadsytv032wa95wodwip6p4p"); //  regstart[this_reg] = (const char *) fail_stack.stack[--fail_stack.avail].pointer; ; } else { for (this_reg = highest_active_reg; this_reg > highest_active_reg; this_reg--) { reg_info[this_reg].word.integer = 0; regend[this_reg] = 0; 
UNSUPPORTED("1sgt5yrkh4wqtk60f39qhmqyh"); //  regstart[this_reg] = 0; } highest_active_reg = highest_active_reg; } set_regs_matched_done = 0; ; };
UNSUPPORTED("a59azprq7sley0l2fkpr8iplf"); //                     /* If this failure point is a dummy, try the next one.  */
UNSUPPORTED("27dzlpjb6otkjse11xs60aoen"); //                     if (!p)
UNSUPPORTED("etjodgz127fwk1r4166wqxg7n"); //                         goto fail;
UNSUPPORTED("5ezd84f3c0gvxbdr48go3nu9d"); //                     /* If we failed to the end of the pattern, don't examine *p.  */
UNSUPPORTED("1qe3sa29ntp5400stzdrrbgdg"); //                     ;
UNSUPPORTED("enro4gwm81mrbyp937vtdmluy"); //                     if (p < pend)
UNSUPPORTED("5k2digv672hnrndhc9ktw0oii"); //                         {
UNSUPPORTED("5bmgzdh5rp0tqqvuep6upitw5"); //                             boolean is_a_jump_n = 0;
UNSUPPORTED("6rryel9f7nixy5bn6j0bu5pim"); //                             /* If failed to a backwards jump that's part of a repetition
UNSUPPORTED("6lkiil4jkazqh0sr2kuzl2a6n"); //                                loop, need to pop this failure point and use the next one.  */
UNSUPPORTED("3ov8y2d12vzh7q5i3tuyoelxe"); //                             switch ((re_opcode_t) *p)
UNSUPPORTED("4hzaau620c4rh7xorkrmxfut7"); //                                 {
UNSUPPORTED("bok1esp1zekmbuklmyk3rk5rx"); //                                 case jump_n:
UNSUPPORTED("38gwuf75sc2x4fup5mqsis4x6"); //                                     is_a_jump_n = 1;
UNSUPPORTED("75n3w229uzeshyir5i61r1043"); //                                 case maybe_pop_jump:
UNSUPPORTED("a8ugqe6z5croeioatypl4otaw"); //                                 case pop_failure_jump:
UNSUPPORTED("6l3m2qx4riktyve4e2l0wh9fe"); //                                 case jump:
UNSUPPORTED("84b1pj26tamclxb0mw5nrxg5g"); //                                     p1 = p + 1;
UNSUPPORTED("4sq4b65a0bh8lf919g64pqica"); //                                     do { do { (mcnt) = *(p1) & 0377; (mcnt) += ((((unsigned char) (*((p1) + 1))) ^ 128) - 128) << 8; } while (0); (p1) += 2; } while (0);
UNSUPPORTED("8ocr60qo89yr0vtq5dj8v1owo"); //                                     p1 += mcnt;
UNSUPPORTED("6cxswairiyne2bjb1dmz6d26z"); //                                     if ((is_a_jump_n && (re_opcode_t) *p1 == succeed_n)
UNSUPPORTED("ekvd2x03u6gf8ylhhxyq7h336"); //                                         || (!is_a_jump_n
UNSUPPORTED("cw8zksvsbjm3lb2eh5chm46x2"); //                                             && (re_opcode_t) *p1 == on_failure_jump))
UNSUPPORTED("bkcykqhkoe9lpop9i2qgzu9yp"); //                                         goto fail;
UNSUPPORTED("1fg4xctmq8acy7m0oob45guaz"); //                                     break;
UNSUPPORTED("cyzkq50b2b248lxpi6lj66jqs"); //                                 default:
UNSUPPORTED("2oxjzlotqn0ba3akkxgs9h02q"); //                                     /* do nothing */ ;
UNSUPPORTED("2tfish0jog6m8uhlhaokmzvm3"); //                                 }
UNSUPPORTED("b86ovw6olwwo6gnqlt1wqqzb4"); //                         }
UNSUPPORTED("9g1qr2dsx4ng98gqf2c94flil"); //                     if (d >= string1 && d <= end1)
UNSUPPORTED("eb6uc0689wgv8mqsvkm38mu5z"); //                         dend = end_match_1;
UNSUPPORTED("7nxu74undh30brb8laojud3f9"); //                 }
UNSUPPORTED("1knjyao8ci3w18zqqcnmnitir"); //             else
UNSUPPORTED("8dc5stfweb0p9s0z2aqvucjp7"); //                 break;   /* Matching at this starting point really fails.  */
UNSUPPORTED("7mfls4p0ojvjkaotqvjflu5ht"); //         } /* for (;;) */
UNSUPPORTED("78m9mze1ip3qflmasmayufoml"); //     if (best_regs_set)
UNSUPPORTED("2mqxvuzj0ksk0m6oea72s11bm"); //         goto restore_best_regs;
UNSUPPORTED("d7ubx9odh5b3nkaac1zt1epbl"); //     do { ; if ((void*) regstart) ((void)0); (void*) regstart = (void *)0; if ((void*) regend) ((void)0); (void*) regend = (void *)0; if ((void*) old_regstart) ((void)0); (void*) old_regstart = (void *)0; if ((void*) old_regend) ((void)0); (void*) old_regend = (void *)0; if ((void*) best_regstart) ((void)0); (void*) best_regstart = (void *)0; if ((void*) best_regend) ((void)0); (void*) best_regend = (void *)0; if ((void*) reg_info) ((void)0); (void*) reg_info = (void *)0; if ((void*) reg_dummy) ((void)0); (void*) reg_dummy = (void *)0; if ((void*) reg_info_dummy) ((void)0); (void*) reg_info_dummy = (void *)0; } while (0);
UNSUPPORTED("2k1latznr3ux22xinn931zwue"); //     return -1;                          /* Failure to match.  */
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 cq5u8rqlecamd29odxfaybq58
// static boolean group_match_null_string_p(unsigned char **p,                           unsigned char *end,                           register_info_type *reg_info) 
public static Object group_match_null_string_p(Object... arg) {
UNSUPPORTED("etarlz3ybg3wdofiesiw8dwbq"); // static boolean
UNSUPPORTED("7fmzghcqs81ua3tkzybmg2t5u"); // group_match_null_string_p(unsigned char **p,
UNSUPPORTED("2800cclhjee5reqodgs0uz6jp"); //                           unsigned char *end,
UNSUPPORTED("1zudh11exsdumrcui0azslboo"); //                           register_info_type *reg_info)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("5rwd4tuvikkbfw1s56awbwtbe"); //     int mcnt;
UNSUPPORTED("dbm120px96ap42fmbuxy5yrj5"); //     /* Point to after the args to the start_memory.  */
UNSUPPORTED("bjzqlm993x9boruj0kyeol53l"); //     unsigned char *p1 = *p + 2;
UNSUPPORTED("azfsm7rfz2u17du52a0mrfyoq"); //     while (p1 < end)
UNSUPPORTED("6pjalxixg8dnhbhc46pm6e6ay"); //         {
UNSUPPORTED("23a56718i3eh15dzfmbft7w8p"); //             /* Skip over opcodes that can match nothing, and return true or
UNSUPPORTED("bbrge68fsb7cmb7ocyo176sox"); //                false, as appropriate, when we get to one that can't, or to the
UNSUPPORTED("9nn48i9apg1r7w3mm0yftr0gb"); //                matching stop_memory.  */
UNSUPPORTED("3kakimebymsdj9dnon7o0iz9v"); //             switch ((re_opcode_t) *p1)
UNSUPPORTED("9ua540u2gx5jpu302s81qfxhi"); //                 {
UNSUPPORTED("dpdkpfbea8z9xx4m3qriv0ks"); //                     /* Could be either a loop or a series of alternatives.  */
UNSUPPORTED("992rmruvdtrxnk46307iiqyjo"); //                 case on_failure_jump:
UNSUPPORTED("4hemvstzejy8d3hmd82kukj7v"); //                     p1++;
UNSUPPORTED("18pz18zbmozo48uq20alb8ok5"); //                     do { do { (mcnt) = *(p1) & 0377; (mcnt) += ((((unsigned char) (*((p1) + 1))) ^ 128) - 128) << 8; } while (0); (p1) += 2; } while (0);
UNSUPPORTED("7ysd5uwx66rnhuasuke0gw17h"); //                     /* If the next operation is not a jump backwards in the
UNSUPPORTED("chqkvvtqui6nji6p4nqjnxnc0"); //                        pattern.  */
UNSUPPORTED("clp8uz1ldh06ksh93asr2xtu6"); //                     if (mcnt >= 0)
UNSUPPORTED("5k2digv672hnrndhc9ktw0oii"); //                         {
UNSUPPORTED("9vuq76qui8wedzsb144fnx8dd"); //                             /* Go through the on_failure_jumps of the alternatives,
UNSUPPORTED("4k14n5k0mqv0fovwitf4alxzx"); //                                seeing if any of the alternatives cannot match nothing.
UNSUPPORTED("clkpepohs7tguzyn8ncluhakf"); //                                The last alternative starts with only a jump,
UNSUPPORTED("49dbeehco6191jqwdivzoevhh"); //                                whereas the rest start with on_failure_jump and end
UNSUPPORTED("6h3f5m3yjc34la8qgzsbzbcsv"); //                                with a jump, e.g., here is the pattern for `a|b|c':
UNSUPPORTED("9m5jtr9332etcn1h5ctckidp4"); //                                /on_failure_jump/0/6/exactn/1/a/jump_past_alt/0/6
UNSUPPORTED("ck48ad3kd04evjrca6aqzw2g"); //                                /on_failure_jump/0/6/exactn/1/b/jump_past_alt/0/3
UNSUPPORTED("6vxw9gryj1dxtxoycbews4j87"); //                                /exactn/1/c
UNSUPPORTED("3p6xfpp9zbgix9sb10dz2cfhd"); //                                So, we have to first go through the first (n-1)
UNSUPPORTED("cp25hojwgralgldniw273kdj3"); //                                alternatives and then deal with the last one separately.  */
UNSUPPORTED("9a0det71z82exleln86167ddq"); //                             /* Deal with the first (n-1) alternatives, which start
UNSUPPORTED("az6wcsp2m1rn4g8e9nkvxomfi"); //                                with an on_failure_jump (see above) that jumps to right
UNSUPPORTED("cip7j7cjuis96j2g1tyo2fogf"); //                                past a jump_past_alt.  */
UNSUPPORTED("am40qiyqgt5i95zqw8gmfb43f"); //                             while ((re_opcode_t) p1[mcnt-3] == jump_past_alt)
UNSUPPORTED("4hzaau620c4rh7xorkrmxfut7"); //                                 {
UNSUPPORTED("2lhz8ag4lccpzb4fzqq8rviyo"); //                                     /* `mcnt' holds how many bytes long the alternative
UNSUPPORTED("bu59ahle478dwgxifr5a81zrn"); //                                        is, including the ending `jump_past_alt' and
UNSUPPORTED("4xvr333ml76572j0cidzpewwy"); //                                        its number.  */
UNSUPPORTED("6tqlvyqvppynpnuuf3j8ebtlu"); //                                     if (!alt_match_null_string_p (p1, p1 + mcnt - 3,
UNSUPPORTED("3cqjfljoigoe1s3yctsmb49io"); //                                                                   reg_info))
UNSUPPORTED("5eqwqroxusaik54ze9ykumu4w"); //                                         return 0;
UNSUPPORTED("1nweigx3aj1nu16la9727cgek"); //                                     /* Move to right after this alternative, including the
UNSUPPORTED("9wjlfe40zk6mcnijph9zw3skr"); //                                        jump_past_alt.  */
UNSUPPORTED("8ocr60qo89yr0vtq5dj8v1owo"); //                                     p1 += mcnt;
UNSUPPORTED("7me1al3m500othjustdgp25s8"); //                                     /* Break if it's the beginning of an n-th alternative
UNSUPPORTED("bsa0fgfj9n2wksuh9f164m03i"); //                                        that doesn't begin with an on_failure_jump.  */
UNSUPPORTED("dxkrs3xxv5uhishofpjt2j6mb"); //                                     if ((re_opcode_t) *p1 != on_failure_jump)
UNSUPPORTED("7vckzntfuo1ibi8r53gsfg8al"); //                                         break;
UNSUPPORTED("dcrlv6lsj26x00kq7h5mb1tft"); //                                     /* Still have to check that it's not an n-th
UNSUPPORTED("ehy9rg3hrcurcpp2ofykuu16t"); //                                        alternative that starts with an on_failure_jump.  */
UNSUPPORTED("3aju47l7l855mt7l00l7xg11q"); //                                     p1++;
UNSUPPORTED("4sq4b65a0bh8lf919g64pqica"); //                                     do { do { (mcnt) = *(p1) & 0377; (mcnt) += ((((unsigned char) (*((p1) + 1))) ^ 128) - 128) << 8; } while (0); (p1) += 2; } while (0);
UNSUPPORTED("7i4x97abeu0pj34gwgukjoe00"); //                                     if ((re_opcode_t) p1[mcnt-3] != jump_past_alt)
UNSUPPORTED("bho20rvti5y7pi0voz7il3e40"); //                                         {
UNSUPPORTED("7v0khgzw96aytjvy24azi9yi3"); //                                             /* Get to the beginning of the n-th alternative.  */
UNSUPPORTED("d58xf2hqair572yx3r8pofa0e"); //                                             p1 -= 3;
UNSUPPORTED("b4o9itamiw6ccgnlojl73v2fx"); //                                             break;
UNSUPPORTED("ad3bk4xnx8bgy7plhun3c0mvv"); //                                         }
UNSUPPORTED("2tfish0jog6m8uhlhaokmzvm3"); //                                 }
UNSUPPORTED("j8cvgeroqesrtqchx5ejvs06"); //                             /* Deal with the last alternative: go back and get number
UNSUPPORTED("b74m2a2e9cgu2v8i8t1u5b2x1"); //                                of the `jump_past_alt' just before it.  `mcnt' contains
UNSUPPORTED("cu400hxya6t1qs6iquuqabq0s"); //                                the length of the alternative.  */
UNSUPPORTED("26bdoghsw938ft3sgehp275kx"); //                             do { (mcnt) = *(p1 - 2) & 0377; (mcnt) += ((((unsigned char) (*((p1 - 2) + 1))) ^ 128) - 128) << 8; } while (0);
UNSUPPORTED("3xtanhdz88xahyfe15omt5obd"); //                             if (!alt_match_null_string_p (p1, p1 + mcnt, reg_info))
UNSUPPORTED("4h8qa3kxqmv2m8g445wmasnl"); //                                 return 0;
UNSUPPORTED("f53vent58m68zyw39fxmgqbr0"); //                             p1 += mcnt; /* Get past the n-th alternative.  */
UNSUPPORTED("9euvgwd19qm3deiwc663wdzb0"); //                         } /* if mcnt > 0 */
UNSUPPORTED("ctqmerohp1f69mb1v1t20jx33"); //                     break;
UNSUPPORTED("3q89uw4g68y7kyiitvliyykpb"); //                 case stop_memory:
UNSUPPORTED("1qe3sa29ntp5400stzdrrbgdg"); //                     ;
UNSUPPORTED("8jfkppp2w8v4t5gyqcelvinb4"); //                     *p = p1 + 2;
UNSUPPORTED("15lnhn427yvuetx9312e81jrg"); //                     return 1;
UNSUPPORTED("comxch6w0lbi64ejq9m4p0u7o"); //                 default:
UNSUPPORTED("ir4atstqu8v5sqigzqbwdop4"); //                     if (!common_op_match_null_string_p (&p1, end, reg_info))
UNSUPPORTED("bwb5ofkxp13uiyvgbvx70039w"); //                         return 0;
UNSUPPORTED("7nxu74undh30brb8laojud3f9"); //                 }
UNSUPPORTED("55ht7a89z8ua4oqsrrwpapvpg"); //         } /* while p1 < end */
UNSUPPORTED("5oxhd3fvp0gfmrmz12vndnjt"); //     return 0;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 azrkydvahepcjvkkru2gdwg0m
// static boolean alt_match_null_string_p(unsigned char *p,                         unsigned char *end,                         register_info_type *reg_info) 
public static Object alt_match_null_string_p(Object... arg) {
UNSUPPORTED("etarlz3ybg3wdofiesiw8dwbq"); // static boolean
UNSUPPORTED("7z4yu5m5t5mwjca6t6wpfajrj"); // alt_match_null_string_p(unsigned char *p,
UNSUPPORTED("91jh331pn45y9rnhwhm4fiavm"); //                         unsigned char *end,
UNSUPPORTED("ereoaeyy0phqbbu5cnhfb78ew"); //                         register_info_type *reg_info)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("5rwd4tuvikkbfw1s56awbwtbe"); //     int mcnt;
UNSUPPORTED("15ytldlmpb2i6cvtxquvhjs2m"); //     unsigned char *p1 = p;
UNSUPPORTED("azfsm7rfz2u17du52a0mrfyoq"); //     while (p1 < end)
UNSUPPORTED("6pjalxixg8dnhbhc46pm6e6ay"); //         {
UNSUPPORTED("3e0wppcum116c0gyks4koglo6"); //             /* Skip over opcodes that can match nothing, and break when we get
UNSUPPORTED("ei8lrs2smmn3h63q7rpj9sd0o"); //                to one that can't.  */
UNSUPPORTED("3kakimebymsdj9dnon7o0iz9v"); //             switch ((re_opcode_t) *p1)
UNSUPPORTED("9ua540u2gx5jpu302s81qfxhi"); //                 {
UNSUPPORTED("5n9m839emxb8of9xbq805onsd"); //                     /* It's a loop.  */
UNSUPPORTED("992rmruvdtrxnk46307iiqyjo"); //                 case on_failure_jump:
UNSUPPORTED("4hemvstzejy8d3hmd82kukj7v"); //                     p1++;
UNSUPPORTED("18pz18zbmozo48uq20alb8ok5"); //                     do { do { (mcnt) = *(p1) & 0377; (mcnt) += ((((unsigned char) (*((p1) + 1))) ^ 128) - 128) << 8; } while (0); (p1) += 2; } while (0);
UNSUPPORTED("28bkn6qebb96emlyztm3kg6wt"); //                     p1 += mcnt;
UNSUPPORTED("ctqmerohp1f69mb1v1t20jx33"); //                     break;
UNSUPPORTED("comxch6w0lbi64ejq9m4p0u7o"); //                 default:
UNSUPPORTED("ir4atstqu8v5sqigzqbwdop4"); //                     if (!common_op_match_null_string_p (&p1, end, reg_info))
UNSUPPORTED("bwb5ofkxp13uiyvgbvx70039w"); //                         return 0;
UNSUPPORTED("7nxu74undh30brb8laojud3f9"); //                 }
UNSUPPORTED("eixc7qyme2j14k5zjjghyiexn"); //         }  /* while p1 < end */
UNSUPPORTED("3tcgz4dupb6kw5tdk7n3pca2l"); //     return 1;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 7hv0wkt4lrkp5whfzuke2qytb
// static boolean common_op_match_null_string_p(unsigned char **p,                               unsigned char *end,                               register_info_type *reg_info) 
public static Object common_op_match_null_string_p(Object... arg) {
UNSUPPORTED("etarlz3ybg3wdofiesiw8dwbq"); // static boolean
UNSUPPORTED("8k0shz76dhg3w8h6nz5pjbbu2"); // common_op_match_null_string_p(unsigned char **p,
UNSUPPORTED("28yc2ksklc2vvzt1s5ynbtv0f"); //                               unsigned char *end,
UNSUPPORTED("6qhd2068gir26rtqc477pm6m1"); //                               register_info_type *reg_info)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("5rwd4tuvikkbfw1s56awbwtbe"); //     int mcnt;
UNSUPPORTED("7n9r83dkya7i0zuq00imgrukd"); //     boolean ret;
UNSUPPORTED("7ju59c3dbukl0mz5vgjdoozn5"); //     int reg_no;
UNSUPPORTED("87zyg7fzbf21hmhh461pn9zf9"); //     unsigned char *p1 = *p;
UNSUPPORTED("qr7u5j2ref5m7rf6d3wqgmps"); //     switch ((re_opcode_t) *p1++)
UNSUPPORTED("6pjalxixg8dnhbhc46pm6e6ay"); //         {
UNSUPPORTED("cf4augtxav7j1ap91bbu3vwy1"); //         case no_op:
UNSUPPORTED("30ts9llmyexduvfuq7fczrer8"); //         case begline:
UNSUPPORTED("db3lgui4636z7pglgbzpqe54h"); //         case endline:
UNSUPPORTED("8fekxuxe035cl7l6pccwsy7fh"); //         case begbuf:
UNSUPPORTED("16msnj7hxrfentkv031ct5vc3"); //         case endbuf:
UNSUPPORTED("bcaajfoyi3jhpmdxhguo7iofc"); //         case wordbeg:
UNSUPPORTED("bkapw3yogqx0sw1ngisv3mb5d"); //         case wordend:
UNSUPPORTED("9pqp8vzoeyor0e1x4eqhthk8m"); //         case wordbound:
UNSUPPORTED("7hyd3tgheqr4untlslak7uheb"); //         case notwordbound:
UNSUPPORTED("dtx9szdvwh3uhziubh9zvgbk5"); //             break;
UNSUPPORTED("1olrmh5xaxc4lppwfnwemvaej"); //         case start_memory:
UNSUPPORTED("4rjnecf4q32xuk4ie6vozpkfh"); //             reg_no = *p1;
UNSUPPORTED("cf8srqrmhz47tb7zdgoe9ufhv"); //             ;
UNSUPPORTED("eluoqmhl3j8dyk8hyv680sy72"); //             ret = group_match_null_string_p (&p1, end, reg_info);
UNSUPPORTED("8hm5umlf6osua8zn0w4y735nh"); //             /* Have to set this here in case we're checking a group which
UNSUPPORTED("8xeab9le3ct3b2q7y6ly9yrhh"); //                contains a group and a back reference to it.  */
UNSUPPORTED("5kozscfkat4mdav2anbiib5hw"); //             if (((reg_info[reg_no]).bits.match_null_string_p) == 3)
UNSUPPORTED("7qym75wzdha4x8u9v6fik2mwy"); //                 ((reg_info[reg_no]).bits.match_null_string_p) = ret;
UNSUPPORTED("4cj58wdjt8jr4gsngmucb56o0"); //             if (!ret)
UNSUPPORTED("9co1titw8ibive55ugr6yfqa4"); //                 return 0;
UNSUPPORTED("dtx9szdvwh3uhziubh9zvgbk5"); //             break;
UNSUPPORTED("608rfpgf7ytuxs7hkgnig1by5"); //             /* If this is an optimized succeed_n for zero times, make the jump.  */
UNSUPPORTED("9agy5bj4opmh2vksf6lpzm8cd"); //         case jump:
UNSUPPORTED("9ngggpjqcpnq88nnkxr6y7vpq"); //             do { do { (mcnt) = *(p1) & 0377; (mcnt) += ((((unsigned char) (*((p1) + 1))) ^ 128) - 128) << 8; } while (0); (p1) += 2; } while (0);
UNSUPPORTED("1fxhzdbiknha03fye9yen0bje"); //             if (mcnt >= 0)
UNSUPPORTED("blqj2uomfewkrosqhn5f7bu73"); //                 p1 += mcnt;
UNSUPPORTED("1knjyao8ci3w18zqqcnmnitir"); //             else
UNSUPPORTED("9co1titw8ibive55ugr6yfqa4"); //                 return 0;
UNSUPPORTED("dtx9szdvwh3uhziubh9zvgbk5"); //             break;
UNSUPPORTED("e7ahhsjjo9f2hw3bbtucupqlw"); //         case succeed_n:
UNSUPPORTED("9gimh1u1p2gmf0q5weiqpbbop"); //             /* Get to the number of times to succeed.  */
UNSUPPORTED("1xi9qxsoaehueyjhabvspqapk"); //             p1 += 2;
UNSUPPORTED("9ngggpjqcpnq88nnkxr6y7vpq"); //             do { do { (mcnt) = *(p1) & 0377; (mcnt) += ((((unsigned char) (*((p1) + 1))) ^ 128) - 128) << 8; } while (0); (p1) += 2; } while (0);
UNSUPPORTED("1ju62vxdlb0sw55v80s2h9p8i"); //             if (mcnt == 0)
UNSUPPORTED("9ua540u2gx5jpu302s81qfxhi"); //                 {
UNSUPPORTED("9o2tf6im70km8pb1v0mz4tbhc"); //                     p1 -= 4;
UNSUPPORTED("18pz18zbmozo48uq20alb8ok5"); //                     do { do { (mcnt) = *(p1) & 0377; (mcnt) += ((((unsigned char) (*((p1) + 1))) ^ 128) - 128) << 8; } while (0); (p1) += 2; } while (0);
UNSUPPORTED("28bkn6qebb96emlyztm3kg6wt"); //                     p1 += mcnt;
UNSUPPORTED("7nxu74undh30brb8laojud3f9"); //                 }
UNSUPPORTED("1knjyao8ci3w18zqqcnmnitir"); //             else
UNSUPPORTED("9co1titw8ibive55ugr6yfqa4"); //                 return 0;
UNSUPPORTED("dtx9szdvwh3uhziubh9zvgbk5"); //             break;
UNSUPPORTED("8qjtjs80wrz4f6y4td1b7mvma"); //         case duplicate:
UNSUPPORTED("asaad7roragv1dquu2i7eek6v"); //             if (!((reg_info[*p1]).bits.match_null_string_p))
UNSUPPORTED("9co1titw8ibive55ugr6yfqa4"); //                 return 0;
UNSUPPORTED("dtx9szdvwh3uhziubh9zvgbk5"); //             break;
UNSUPPORTED("75h4k3rjfi85b2x98vw0ydipc"); //         case set_number_at:
UNSUPPORTED("4n2daij5pdw7lao7n4sjeg0jt"); //             p1 += 4;
UNSUPPORTED("p0mt8wznalavjdm44ot4ykl7"); //         default:
UNSUPPORTED("7clvngmkw0ra8af8a3iq45vx"); //             /* All other opcodes mean we cannot match the empty string.  */
UNSUPPORTED("7opo20y2y6rg5i89ocvk6qi3c"); //             return 0;
UNSUPPORTED("4mhlpjofolwivhm0tl8cxznly"); //         }
UNSUPPORTED("7aoui9hygj5j6kdd0rxq44xxu"); //     *p = p1;
UNSUPPORTED("3tcgz4dupb6kw5tdk7n3pca2l"); //     return 1;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 bmn9h3qza6i9e7dqjjfrybinz
// static int bcmp_translate(const char *s1,                const char *s2,                register int len,                char * translate) 
public static Object bcmp_translate(Object... arg) {
UNSUPPORTED("eyp5xkiyummcoc88ul2b6tkeg"); // static int
UNSUPPORTED("49tjvn2d5kzof9i3764qd8gth"); // bcmp_translate(const char *s1,
UNSUPPORTED("5uinwhadus9kwopd7lyrqnv1a"); //                const char *s2,
UNSUPPORTED("cmbl8wi47y4b1fybqcgqp3cad"); //                register int len,
UNSUPPORTED("buglwifdxboeujcjzksjh6swn"); //                char * translate)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("1ugbvzngjn5o1kzerkwa4adyp"); //     register const unsigned char *p1 = (const unsigned char *) s1;
UNSUPPORTED("180lhbq291ugus1sv6zq5b3l5"); //     register const unsigned char *p2 = (const unsigned char *) s2;
UNSUPPORTED("6wkqjlkf1iustuo36c77rrlrr"); //     while (len)
UNSUPPORTED("6pjalxixg8dnhbhc46pm6e6ay"); //         {
UNSUPPORTED("ezhykxoqbj0p9ighugfp2pi29"); //             if (translate[*p1++] != translate[*p2++]) return 1;
UNSUPPORTED("bzd5vvu7ht1mg1wbv21csikm0"); //             len--;
UNSUPPORTED("4mhlpjofolwivhm0tl8cxznly"); //         }
UNSUPPORTED("5oxhd3fvp0gfmrmz12vndnjt"); //     return 0;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 34ah0o8fvtlfnsffbxiyqyidp
// const char * re_compile_pattern(const char *pattern,                    size_t length,                    struct re_pattern_buffer *bufp) 
public static Object re_compile_pattern(Object... arg) {
UNSUPPORTED("6j2ty6zxx5788lqe504lttmv9"); // const char *
UNSUPPORTED("840rb5ffowq19aqwebzlrs0ix"); // re_compile_pattern(const char *pattern,
UNSUPPORTED("dl8tq4y4w9kpglxq4nad9nbg9"); //                    size_t length,
UNSUPPORTED("aoahpx5nic1p28er7zbeilibx"); //                    struct re_pattern_buffer *bufp)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("3e5irvh3tz7ehf2px38xbca06"); //     reg_errcode_t ret;
UNSUPPORTED("5e5qu4o19kt10axby5ksjp1wz"); //     /* GNU code is written to assume at least RE_NREGS registers will be set
UNSUPPORTED("aj16ee11rx90k6v260pcrptdx"); //        (and at least one extra will be -1).  */
UNSUPPORTED("45osi16xhss2jxm7u3qpsz7c8"); //     bufp->regs_allocated = 0;
UNSUPPORTED("1s5igzhlf8kjprjkw59u0qgie"); //     /* And GNU code determines whether or not to get register information
UNSUPPORTED("4guz1aey6odyu4c6y9tmn7zks"); //        by passing null for the REGS argument to re_match, etc., not by
UNSUPPORTED("52atj9kiaa2ty4xp5v9tsxd36"); //        setting no_sub.  */
UNSUPPORTED("bbp8v34kk2eq5jyypmrxegzfj"); //     bufp->no_sub = 0;
UNSUPPORTED("9zh94yub2h9x6jb5ygl7zj9j3"); //     /* Match anchors at newline.  */
UNSUPPORTED("e5hvlkcul9024me3za5w0z09"); //     bufp->newline_anchor = 1;
UNSUPPORTED("f3zeg0ozvbzdxvagvz40uveju"); //     ret = regex_compile (pattern, length, re_syntax_options, bufp);
UNSUPPORTED("2ymc69pz5pg0nhr5joxv3v743"); //     if (!ret)
UNSUPPORTED("4mszslwsjcaxojerluqjpsu2v"); //         return (void *)0;
UNSUPPORTED("d6waz7r04hy6vrqylyux3566y"); //     return (re_error_msgid[(int) ret]);
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 ark6qnryn24fu1bsbt5806lp
// int regcomp(regex_t *preg,         const char *pattern,         int cflags) 
public static Object regcomp(Object... arg) {
UNSUPPORTED("etrjsq5w49uo9jq5pzifohkqw"); // int
UNSUPPORTED("blyavrrspx8u7mmx6skbsrpuy"); // regcomp(regex_t *preg,
UNSUPPORTED("ez93viyirx32wiwx9ilmwnk2u"); //         const char *pattern,
UNSUPPORTED("s47pwvnf26eqviz0566rtafr"); //         int cflags)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("3e5irvh3tz7ehf2px38xbca06"); //     reg_errcode_t ret;
UNSUPPORTED("arhekyz17wtkt6jcym8vpwl49"); //     reg_syntax_t syntax
UNSUPPORTED("cbdjhw8vkpou2zuaqsu38y9nq"); //         = (cflags & 1) ?
UNSUPPORTED("79wzspap1pncga08fi5fl45p3"); //         ((((((unsigned long int) 1) << 1) << 1) | ((((((((unsigned long int) 1) << 1) << 1) << 1) << 1) << 1) << 1) | (((((((((unsigned long int) 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) | (((((((((((unsigned long int) 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) | ((((((((((((((((((unsigned long int) 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1)) | (((((unsigned long int) 1) << 1) << 1) << 1) | ((((((unsigned long int) 1) << 1) << 1) << 1) << 1) | ((((((((((((((unsigned long int) 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) | (((((((((((((((unsigned long int) 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) | (((((((((((((((((unsigned long int) 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) | (((((((((((((((((((unsigned long int) 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1)) : ((((((unsigned long int) 1) << 1) << 1) | ((((((((unsigned long int) 1) << 1) << 1) << 1) << 1) << 1) << 1) | (((((((((unsigned long int) 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) | (((((((((((unsigned long int) 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) | ((((((((((((((((((unsigned long int) 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1)) | (((unsigned long int) 1) << 1));
UNSUPPORTED("2c2dfk6a0ejxysnnaajue8m3p"); //     /* regex_compile will allocate the space for the compiled pattern.  */
UNSUPPORTED("ciyvseirt32x5s8n7w8wi9s8b"); //     preg->buffer = 0;
UNSUPPORTED("b766kips3z6tm9tifbomnvtmy"); //     preg->allocated = 0;
UNSUPPORTED("2ne52scg87du6fvmgx4iz1ysx"); //     preg->used = 0;
UNSUPPORTED("dxwa3e8lqasfevebnvit8vz60"); //     /* Don't bother to use a fastmap when searching.  This simplifies the
UNSUPPORTED("d5umtgupwg9d4iorhfyjyv8h5"); //        REG_NEWLINE case: if we used a fastmap, we'd have to put all the
UNSUPPORTED("ci8pjdo3b21gzu9zuu8df58su"); //        characters after newlines into the fastmap.  This way, we just try
UNSUPPORTED("b5ux13ie7uoiwgtxbhqxccoi6"); //        every character.  */
UNSUPPORTED("57t8as5xu3zastwhuvl9x5qvx"); //     preg->fastmap = 0;
UNSUPPORTED("enpzhkdq6ocdhen3xvl8ye5na"); //     if (cflags & (1 << 1))
UNSUPPORTED("6pjalxixg8dnhbhc46pm6e6ay"); //         {
UNSUPPORTED("nujvj3hueebzfkhe2zarvfil"); //             unsigned i;
UNSUPPORTED("1pg69zbhom0cik7h8kn6t7y3g"); //             preg->translate
UNSUPPORTED("4nsrzyk2btcgmsdqjm2ar3vro"); //                 = (char *) malloc (256
UNSUPPORTED("121bcje0610pzlltu9346d7c4"); //                                               * sizeof (*(char *)0));
UNSUPPORTED("bg80cnygjx78pa8v3kgmb63z4"); //             if (preg->translate == (void *)0)
UNSUPPORTED("ez6fz5kzz38v9ggr3wcumi254"); //                 return (int) REG_ESPACE;
UNSUPPORTED("1vzdm7mgve5oi32ohhuqg8i00"); //             /* Map uppercase characters to corresponding lowercase ones.  */
UNSUPPORTED("d532lt7aisdb9o1mkdskq134t"); //             for (i = 0; i < 256; i++)
UNSUPPORTED("aty2mttvyracw7p9x8sls4efr"); //                 preg->translate[i] = (1 && isupper (i)) ? tolower (i) : i;
UNSUPPORTED("4mhlpjofolwivhm0tl8cxznly"); //         }
UNSUPPORTED("div10atae09n36x269sl208r1"); //     else
UNSUPPORTED("c74ru7c24sev3knmidtcsy39e"); //         preg->translate = (void *)0;
UNSUPPORTED("a5kyad26nzmqkixgei1o847n"); //     /* If REG_NEWLINE is set, newlines are treated differently.  */
UNSUPPORTED("db72pzje2ov1p3o5omyykyi7e"); //     if (cflags & ((1 << 1) << 1))
UNSUPPORTED("bpj2wmqxauupj14jn3tkadfjh"); //         { /* REG_NEWLINE implies neither . nor [^...] match newline.  */
UNSUPPORTED("359fgbemkmx8zzoobn2anh8hi"); //             syntax &= ~((((((((unsigned long int) 1) << 1) << 1) << 1) << 1) << 1) << 1);
UNSUPPORTED("6g64qw2xiqubh3y7z9htmuzh5"); //             syntax |= ((((((((((unsigned long int) 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1) << 1);
UNSUPPORTED("ciyi7qvu3im41264ljmdkdzcl"); //             /* It also changes the matching behavior.  */
UNSUPPORTED("8qz1elzfwgfxjvri21nioniys"); //             preg->newline_anchor = 1;
UNSUPPORTED("4mhlpjofolwivhm0tl8cxznly"); //         }
UNSUPPORTED("div10atae09n36x269sl208r1"); //     else
UNSUPPORTED("2vftf31ujo45k9qv67n4ycbkv"); //         preg->newline_anchor = 0;
UNSUPPORTED("cx4zfp27tpwfh8xisrvqqbgks"); //     preg->no_sub = !!(cflags & (((1 << 1) << 1) << 1));
UNSUPPORTED("2x159h0yz24rnjci2cq7y1ntz"); //     /* POSIX says a null character in the pattern terminates it, so we
UNSUPPORTED("d66drlhc8quicmkt8miicu0ea"); //        can use strlen here in compiling the pattern.  */
UNSUPPORTED("d0cg1xygicxugayg5yya59guo"); //     ret = regex_compile (pattern, strlen (pattern), syntax, preg);
UNSUPPORTED("erkyjok9d0k2psk0sb56f6vvz"); //     /* POSIX doesn't distinguish between an unmatched open-group and an
UNSUPPORTED("1u6sohla9epfpyet1i7nzcgad"); //        unmatched close-group: both are REG_EPAREN.  */
UNSUPPORTED("3e5v05pv5yk1o9jdj6bh0zg5w"); //     if (ret == REG_ERPAREN) ret = REG_EPAREN;
UNSUPPORTED("7ancqvxv19g9moyznhw9my8xj"); // //	printf("done with regcomp\n");
UNSUPPORTED("f3hb5xwlfzeslhmq4ytwr8pgd"); //     return (int) ret;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 9wdgvv1gwc1o7f0w05768p2i3
// int regexec(const regex_t *preg,         const char *string,         size_t nmatch,         regmatch_t pmatch[],         int eflags) 
public static Object regexec(Object... arg) {
UNSUPPORTED("etrjsq5w49uo9jq5pzifohkqw"); // int
UNSUPPORTED("2v3o1uaqb2zp3puxfw779kms2"); // regexec(const regex_t *preg,
UNSUPPORTED("ekpgp0iac0e6o9udtd55uiuly"); //         const char *string,
UNSUPPORTED("dvlw3353jood9rlb0w1geec5z"); //         size_t nmatch,
UNSUPPORTED("2864eyed7whs62o97tq2npado"); //         regmatch_t pmatch[],
UNSUPPORTED("3i4rg5r8fnniwlx6ybr4n906a"); //         int eflags)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("7p7i4cv4qt10ow22vl9znw72j"); //     int ret;
UNSUPPORTED("82u0gsptykltdsomlyhj9rk19"); //     struct re_registers regs;
UNSUPPORTED("9z47qt0zii8jvt2jglo0lgszf"); //     regex_t private_preg;
UNSUPPORTED("6tsm0ylmo6ex2b24vcrr2cim7"); //     int len = strlen (string);
UNSUPPORTED("17kwuhblkqgc3mdapwyc395sz"); //     boolean want_reg_info = !preg->no_sub && nmatch > 0;
UNSUPPORTED("agwedmhzs0prdnnlk9jnsfg9c"); //     private_preg = *preg;
UNSUPPORTED("13e7wtxa8hlobsw8k2nqqg3r3"); //     private_preg.not_bol = !!(eflags & 1);
UNSUPPORTED("57scmexrxxrwf0l9nzr1szrpt"); //     private_preg.not_eol = !!(eflags & (1 << 1));
UNSUPPORTED("mql47hhdnpgkjou517m3726f"); //     /* The user has told us exactly how many registers to return
UNSUPPORTED("cqy8u5p4xwvs8y4q6w7fce3xv"); //        information about, via `nmatch'.  We have to pass that on to the
UNSUPPORTED("3rosx96qxeg0kqk0uxh7l1cl8"); //        matching routines.  */
UNSUPPORTED("c013ob9fi1fg0vxd1oam5865g"); //     private_preg.regs_allocated = 2;
UNSUPPORTED("9rkeiv1fdp19eb173sjfltu2t"); //     if (want_reg_info)
UNSUPPORTED("6pjalxixg8dnhbhc46pm6e6ay"); //         {
UNSUPPORTED("8a02jnbpnzttrw1ayq7w5mj62"); //             regs.num_regs = nmatch;
UNSUPPORTED("aoe6tbrgjwbzymn65261rgcum"); //             regs.start = ((regoff_t *) malloc ((nmatch) * sizeof (regoff_t)));
UNSUPPORTED("3hfltfy7wmk9a24vq9wzlmq9v"); //             regs.end = ((regoff_t *) malloc ((nmatch) * sizeof (regoff_t)));
UNSUPPORTED("7njv4u89envwukxkawqtq0oto"); //             if (regs.start == (void *)0 || regs.end == (void *)0)
UNSUPPORTED("873f3rccmawjgt3m3uqo8p3ci"); //                 return (int) REG_NOMATCH;
UNSUPPORTED("4mhlpjofolwivhm0tl8cxznly"); //         }
UNSUPPORTED("a8yuopeuk07ow83nm5njscoxn"); //     /* Perform the searching operation.  */
UNSUPPORTED("cwamuwhai30lyfbzzvibn401x"); //     ret = re_search (&private_preg, string, len,
UNSUPPORTED("6jnlnbkqqmvrtbxyne0meo7jt"); //                      /* start: */ 0, /* range: */ len,
UNSUPPORTED("1xnfnhd8hk3tln4va3uu7f2wm"); //                      want_reg_info ? &regs : (struct re_registers *) 0);
UNSUPPORTED("86oesm7mqi9hv2n08zwleh73m"); //     /* Copy the register information to the POSIX structure.  */
UNSUPPORTED("9rkeiv1fdp19eb173sjfltu2t"); //     if (want_reg_info)
UNSUPPORTED("6pjalxixg8dnhbhc46pm6e6ay"); //         {
UNSUPPORTED("ce73wb0bbrngqy808tp6ppsrq"); //             if (ret >= 0)
UNSUPPORTED("9ua540u2gx5jpu302s81qfxhi"); //                 {
UNSUPPORTED("bxs4m6oognafzhor2ww93b692"); //                     unsigned r;
UNSUPPORTED("8soywsau32kjtl93ct4sxxyma"); //                     for (r = 0; r < nmatch; r++)
UNSUPPORTED("5k2digv672hnrndhc9ktw0oii"); //                         {
UNSUPPORTED("cfq91dne0qag57cuzkcw5mxcg"); //                             pmatch[r].rm_so = regs.start[r];
UNSUPPORTED("gibau4k00r5dzlvk4kcbwb56"); //                             pmatch[r].rm_eo = regs.end[r];
UNSUPPORTED("b86ovw6olwwo6gnqlt1wqqzb4"); //                         }
UNSUPPORTED("7nxu74undh30brb8laojud3f9"); //                 }
UNSUPPORTED("c0xnifbhwrr011t5ogyy7sxk4"); //             /* If we needed the temporary register info, free the space now.  */
UNSUPPORTED("ec72z8dmt4gcwsz42a7ubbvgx"); //             free (regs.start);
UNSUPPORTED("8gandm25f5zvbvckszq77jha"); //             free (regs.end);
UNSUPPORTED("4mhlpjofolwivhm0tl8cxznly"); //         }
UNSUPPORTED("7dob0tgisyvw4rk6jg4viupyw"); //     /* We want zero return to mean success, unlike `re_search'.  */
UNSUPPORTED("246hp3joht768z00rf97n4yvo"); //     return ret >= 0 ? (int) REG_NOERROR : (int) REG_NOMATCH;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 6u3voe6b7jao3ubd49rpvpmmx
// size_t regerror(int errcode,          const regex_t *preg,          char *errbuf,          size_t errbuf_size) 
public static Object regerror(Object... arg) {
UNSUPPORTED("4hv4edbckfpx53agqilues063"); // size_t
UNSUPPORTED("72xsdxwxd93qf4oxx4jyhdo8z"); // regerror(int errcode,
UNSUPPORTED("asif8gxw337t4n3r3vlc5nfry"); //          const regex_t *preg,
UNSUPPORTED("7k1ct0qwx3daob1tp19njd6q0"); //          char *errbuf,
UNSUPPORTED("dx6x6qmmym6uzf7roqbw9kvt"); //          size_t errbuf_size)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("91x25pzkmtzqo9hslez13lges"); //     const char *msg;
UNSUPPORTED("6m38jprvakqd3ls3jeotcz1xg"); //     size_t msg_size;
UNSUPPORTED("adpjyd1odey6jlw7vkjk429xt"); //     if (errcode < 0
UNSUPPORTED("8ybd3hnsngs38rh8ddk8q0w26"); //         || errcode >= (int) (sizeof (re_error_msgid)
UNSUPPORTED("c9vfuy341wx3rri8wvq9sua5s"); //                              / sizeof (re_error_msgid[0])))
UNSUPPORTED("7u1uewk6wx5joexwehp38vlng"); //         /* Only error codes returned by the rest of the code should be passed
UNSUPPORTED("6hlmqhjzj6vqjlpwwklal6m89"); //            to this routine.  If we are given anything else, or if other regex
UNSUPPORTED("cbs38fwyo0gkko91u7cenjci"); //            code generates an invalid error code, then the program has a bug.
UNSUPPORTED("8or7291qatc4r8j81dj07fgb4"); //            Dump core so we can fix it.  */
UNSUPPORTED("68wdgoila4oldpk4gdc6i6uc3"); //         abort ();
UNSUPPORTED("4yci96v8pzj722abptwqxdkso"); //     msg = (re_error_msgid[errcode]);
UNSUPPORTED("dujj3eb9b3zvr0euc5s4itcj"); //     msg_size = strlen (msg) + 1; /* Includes the null.  */
UNSUPPORTED("6oho364pztumj8yc7uc6bsusm"); //     if (errbuf_size != 0)
UNSUPPORTED("6pjalxixg8dnhbhc46pm6e6ay"); //         {
UNSUPPORTED("4oxtwxt5zv8auje423kp2f05r"); //             if (msg_size > errbuf_size)
UNSUPPORTED("9ua540u2gx5jpu302s81qfxhi"); //                 {
UNSUPPORTED("3cof3rq3oujwsp8nrbb9snh96"); //                     strncpy (errbuf, msg, errbuf_size - 1);
UNSUPPORTED("17sh1j757ozjg8a39nvcnmean"); //                     errbuf[errbuf_size - 1] = 0;
UNSUPPORTED("7nxu74undh30brb8laojud3f9"); //                 }
UNSUPPORTED("1knjyao8ci3w18zqqcnmnitir"); //             else
UNSUPPORTED("6ru8c1linplme96gzmn6v9ezn"); //                 strcpy (errbuf, msg);
UNSUPPORTED("4mhlpjofolwivhm0tl8cxznly"); //         }
UNSUPPORTED("ban7kvxs6qguwfg0cz79w2fqe"); //     return msg_size;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 1ydg62ufhooqvgftuuovy004y
// void regfree(regex_t *preg) 
public static Object regfree(Object... arg) {
UNSUPPORTED("c01vxogao855zs8fe94tpim9g"); // void
UNSUPPORTED("yxtwt5xf8mgfwucdzaq8xg0"); // regfree(regex_t *preg)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("axv8f27k41wevzbd8jorfk8em"); //     if (preg->buffer != (void *)0)
UNSUPPORTED("4krgfpqw59bwla8p73a5potcs"); //         free (preg->buffer);
UNSUPPORTED("duswsbmztnkjw8nmmpoanyq0l"); //     preg->buffer = (void *)0;
UNSUPPORTED("b766kips3z6tm9tifbomnvtmy"); //     preg->allocated = 0;
UNSUPPORTED("2ne52scg87du6fvmgx4iz1ysx"); //     preg->used = 0;
UNSUPPORTED("ac48j1opcvu8p6bmkvhdmubhe"); //     if (preg->fastmap != (void *)0)
UNSUPPORTED("43aaw7li7lbfcd1lewzzyp7to"); //         free (preg->fastmap);
UNSUPPORTED("4lv04h2kcmrw99yqy6i26w8g4"); //     preg->fastmap = (void *)0;
UNSUPPORTED("5e92vh1r7ol7emrm0ijcg45a9"); //     preg->fastmap_accurate = 0;
UNSUPPORTED("e4iva4m5k6n7oj8enuvyimttj"); //     if (preg->translate != (void *)0)
UNSUPPORTED("4mliivt4jjgt9njb860f17323"); //         free (preg->translate);
UNSUPPORTED("7c5ulxpurhe3moa5q2eu01tom"); //     preg->translate = (void *)0;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}


}
