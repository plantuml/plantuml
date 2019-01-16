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

public class gvdevice__c {
//1 2digov3edok6d5srhgtlmrycs
// extern lt_symlist_t lt_preloaded_symbols[]


//1 baedz5i9est5csw3epz3cv7z
// typedef Ppoly_t Ppolyline_t


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


//1 8dfqgf3u1v830qzcjqh9o8ha7
// extern Agmemdisc_t AgMemDisc


//1 18k2oh2t6llfsdc5x0wlcnby8
// extern Agiddisc_t AgIdDisc


//1 a4r7hi80gdxtsv4hdoqpyiivn
// extern Agiodisc_t AgIoDisc


//1 bnzt5syjb7mgeru19114vd6xx
// extern Agdisc_t AgDefaultDisc


//1 35y2gbegsdjilegaribes00mg
// extern Agdesc_t Agdirected, Agstrictdirected, Agundirected,     Agstrictundirected


//1 c2rygslq6bcuka3awmvy2b3ow
// typedef Agsubnode_t	Agnoderef_t


//1 xam6yv0dcsx57dtg44igpbzn
// typedef Dtlink_t	Agedgeref_t


//1 eru7ktmd59gl3fcbs2oilhvbl
// static const int PAGE_ALIGN = 4095




//3 2bgvvqltcp240iwsswb2msc4b
// static size_t gvwrite_no_z (GVJ_t * job, const char *s, size_t len) 
public static Object gvwrite_no_z(Object... arg) {
UNSUPPORTED("avituu19zqjw7jh9tfrcm2vaa"); // static size_t gvwrite_no_z (GVJ_t * job, const char *s, size_t len)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("6thhrxbooxwjzs6lvh7tmii55"); //     if (job->gvc->write_fn)   /* externally provided write dicipline */
UNSUPPORTED("ajwhqhqyqpwentvna296wopdr"); // 	return (job->gvc->write_fn)(job, (char*)s, len);
UNSUPPORTED("lih0ttredmrxsoz6mrlvzubo"); //     if (job->output_data) {
UNSUPPORTED("bnah2gb2ufomx7jjnzd97mbzm"); // 	if (len > job->output_data_allocated - (job->output_data_position + 1)) {
UNSUPPORTED("3hrbwji40pio192kzij9cn7v1"); // 	    /* ensure enough allocation for string = null terminator */
UNSUPPORTED("1ezdnr79jqff920zcjfu0ppqk"); // 	    job->output_data_allocated = (job->output_data_position + len + 1 + PAGE_ALIGN) & ~PAGE_ALIGN;
UNSUPPORTED("b67xdfs465mphgzhwe78lbfbu"); // 	    job->output_data = realloc(job->output_data, job->output_data_allocated);
UNSUPPORTED("4xh16lf1s9kh9dcfg8f9fkvfb"); // 	    if (!job->output_data) {
UNSUPPORTED("ec5pxlqld1kcgdncsj3us6t3s"); //                 (job->common->errorfn) ("memory allocation failure\n");
UNSUPPORTED("7ujm7da8xuut83e2rygja0n9d"); // 		exit(1);
UNSUPPORTED("6t98dcecgbvbvtpycwiq2ynnj"); // 	    }
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("c02x4fbrjzoq9hakupjq645pz"); // 	memcpy(job->output_data + job->output_data_position, s, len);
UNSUPPORTED("bmmkwdo5fdxd026lky22ivss6"); //         job->output_data_position += len;
UNSUPPORTED("9akolspkl84209r7uz2tjd6dd"); // 	job->output_data[job->output_data_position] = '\0'; /* keep null termnated */
UNSUPPORTED("38a44j47si8lvew9i7jutukn9"); // 	return len;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("div10atae09n36x269sl208r1"); //     else
UNSUPPORTED("8n04i1xqzj3xqxz7bhsek9vn7"); // 	return fwrite(s, sizeof(char), len, job->output_file);
UNSUPPORTED("5oxhd3fvp0gfmrmz12vndnjt"); //     return 0;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 cc376t4uiy6qsmjtdc2stei85
// static void auto_output_filename(GVJ_t *job) 
public static Object auto_output_filename(Object... arg) {
UNSUPPORTED("7bmm5yx4a4rufskcsnxv6wl0z"); // static void auto_output_filename(GVJ_t *job)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("8kywmm7f51dmrjngxwvnpsd4o"); //     static char *buf;
UNSUPPORTED("ej9a738oun75yfz0fwi5yrr5"); //     static size_t bufsz;
UNSUPPORTED("92ye5npa3uq0ky5glgy35w5sk"); //     char gidx[100];  /* large enough for '.' plus any integer */
UNSUPPORTED("4qa1e25as4n8qmsddpuz660zr"); //     char *fn, *p, *q;
UNSUPPORTED("do481ti1ubw0ltflx52iweigu"); //     size_t len;
UNSUPPORTED("8siu0oojewru3x36pkpq3rtcm"); //     if (job->graph_index)
UNSUPPORTED("36vwk7ry5ee8fz8vvvxhsa53y"); //         sprintf(gidx, ".%d", job->graph_index + 1);
UNSUPPORTED("div10atae09n36x269sl208r1"); //     else
UNSUPPORTED("11rmpw063nwapibztle1uicb4"); //         gidx[0] = '\0';
UNSUPPORTED("bdnmi5g7zugx6oqb8oim4qkk6"); //     if (!(fn = job->input_filename))
UNSUPPORTED("1461mo4l6w379jk7almrmjggq"); //         fn = "noname.gv";
UNSUPPORTED("eo5ffy7jun0ei6jtmk8u4kwo5"); //     len = strlen(fn)                    /* typically "something.gv" */
UNSUPPORTED("1ujivh2ss3ln7xyoufm1icu1s"); //         + strlen(gidx)                  /* "", ".2", ".3", ".4", ... */
UNSUPPORTED("kjjd1xhi7gp11bp4k1e57wxd"); //         + 1                             /* "." */
UNSUPPORTED("eygwbha2x1l4n5rzyomr2dbqz"); //         + strlen(job->output_langname)  /* e.g. "png" */
UNSUPPORTED("6smfmxycsc2mrvcsl6mrgi727"); //         + 1;                            /* null terminaor */
UNSUPPORTED("55qowwh2rcv2v2uu70hde9ct4"); //     if (bufsz < len) {
UNSUPPORTED("12hyi8sdel7zfnt9c20av1tp8"); //             bufsz = len + 10;
UNSUPPORTED("7ms5jdr5m50uzo3lxyt1gq0cj"); //             buf = realloc(buf, bufsz * sizeof(char));
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("3czlpsj98ep2bccpqdudm2bej"); //     strcpy(buf, fn);
UNSUPPORTED("5iz9z9zc85yv4b16sbx2lywny"); //     strcat(buf, gidx);
UNSUPPORTED("5uqzbiefai050ewcqnxnhzyd2"); //     strcat(buf, ".");
UNSUPPORTED("erqd7hdzmwkcvzz2s1bjfhgwr"); //     p = strdup(job->output_langname);
UNSUPPORTED("4n8ktnhzjg21hh167bu9kbvuh"); //     while ((q = strrchr(p, ':'))) {
UNSUPPORTED("18g9wxzxu3k31c0stdr62a2r6"); //         strcat(buf, q+1);
UNSUPPORTED("3d0me2fe34rtpbmdfbbp9zyvk"); //         strcat(buf, ".");
UNSUPPORTED("cxlp7boqg1gqp4ii26w29519a"); // 	*q = '\0';
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("94ysqimvfahimlahyh4amvdd8"); //     strcat(buf, p);
UNSUPPORTED("bo0y3vz195pcz24vm46pixpb2"); //     free(p);
UNSUPPORTED("eif3727zpcgrby865izuy2bam"); //     job->output_filename = buf;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 9zdt3xvyjnxhf42eizyfy29ly
// int gvdevice_initialize(GVJ_t * job) 
public static Object gvdevice_initialize(Object... arg) {
UNSUPPORTED("2same1m1pu1ldefe93fodijcl"); // int gvdevice_initialize(GVJ_t * job)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("44r8xaau6kv13q6liujnhwe0i"); //     gvdevice_engine_t *gvde = job->device.engine;
UNSUPPORTED("eyew5ay5wwktz4fysz0b78ugv"); //     GVC_t *gvc = job->gvc;
UNSUPPORTED("2p848auysctbpp02yxskwgoef"); //     if (gvde && gvde->initialize) {
UNSUPPORTED("3vrhe9vyw7wj2er55do0u371j"); // 	gvde->initialize(job);
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("8gs9pxngjqg7knaxiacx0x8tq"); //     else if (job->output_data) {
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("359rwgnb349m83fzxkva3v46w"); //     /* if the device has no initialization then it uses file output */
UNSUPPORTED("33oebivtpnjqlipt0pt25chff"); //     else if (!job->output_file) {        /* if not yet opened */
UNSUPPORTED("dp33ygrqgmzkpm08snkc1ervb"); //         if (gvc->common.auto_outfile_names)
UNSUPPORTED("e114zl89x5x9bx0p6tnctzh9z"); //             auto_output_filename(job);
UNSUPPORTED("a7zftzoyvxtzvya1m1b2ttg72"); //         if (job->output_filename) {
UNSUPPORTED("v1eei32rad2oq6s98de4ew05"); //             job->output_file = fopen(job->output_filename, "w");
UNSUPPORTED("afw1ljocg5i7vzl6ghbj5joaq"); //             if (job->output_file == (void *)0) {
UNSUPPORTED("3kbjb2hiznrrr74oirsmjdsjy"); // 		(job->common->errorfn) ("Could not open \"%s\" for writing : %s\n", 
UNSUPPORTED("2lce20ttxmbpd5czpm6pd2mv6"); // 		    job->output_filename, strerror(errno));
UNSUPPORTED("ef12rdptgkbtbbvkzn1djihki"); //                 /* perror(job->output_filename); */
UNSUPPORTED("6jf2l4v31jakvckptxdonjqpl"); //                 return(1);
UNSUPPORTED("7g94ubxa48a1yi3mf9v521b7c"); //             }
UNSUPPORTED("4mhlpjofolwivhm0tl8cxznly"); //         }
UNSUPPORTED("35nw1pbiz2p3s6qwlam5eoo3m"); //         else
UNSUPPORTED("9c37ox95uc5viasrpewfi74ew"); //             job->output_file = stdout;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("146f5u2dl3dt3devy0qnn6ylz"); //     if (job->flags & (1<<10)) {
UNSUPPORTED("98ow53mx94n2jxerk4g3xuyju"); // 	(job->common->errorfn) ("No libz support.\n");
UNSUPPORTED("atcj5la5r4ghvn5xk7kx25y7f"); // 	return(1);
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("5oxhd3fvp0gfmrmz12vndnjt"); //     return 0;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 ajhncfer509k2pk55o2k8w9aw
// size_t gvwrite (GVJ_t * job, const char *s, size_t len) 
public static Object gvwrite(Object... arg) {
UNSUPPORTED("bwx5n843dguc728qqcypic3er"); // size_t gvwrite (GVJ_t * job, const char *s, size_t len)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("cmn7vwsif1z0j2gjyq9v1u4pc"); //     size_t ret, olen;
UNSUPPORTED("bhgtpxgp12b1cvyt6ozbjx6yy"); //     if (!len || !s)
UNSUPPORTED("c9ckhc8veujmwcw0ar3u3zld4"); // 	return 0;
UNSUPPORTED("146f5u2dl3dt3devy0qnn6ylz"); //     if (job->flags & (1<<10)) {
UNSUPPORTED("98ow53mx94n2jxerk4g3xuyju"); // 	(job->common->errorfn) ("No libz support.\n");
UNSUPPORTED("dtw3cma0ziyha0w534bszl0tx"); // 	exit(1);
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("8k95edr042llogz6nqedj70ic"); //     else { /* uncompressed write */
UNSUPPORTED("3dfkzetcm152gosaspkplit3e"); // 	ret = gvwrite_no_z (job, s, len);
UNSUPPORTED("couxfse3y39odtdvb0dsftko7"); // 	if (ret != len) {
UNSUPPORTED("d5gw78oi8bl5tvl05pegfx7do"); // 	    (job->common->errorfn) ("gvwrite_no_z problem %d\n", len);
UNSUPPORTED("6f1y0d5qfp1r9zpw0r7m6xfb4"); // 	    exit(1);
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("ji4xmwppaalf0z5xzgfw9h36"); //     return len;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 38q4ri7dsm0ur36bqkxqeeu6y
// int gvferror (FILE* stream) 
public static Object gvferror(Object... arg) {
UNSUPPORTED("bw6pcxpys8yj8g1611fjiq85f"); // int gvferror (FILE* stream)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("3g3bthpqcv2dm7v2g4agrgksv"); //     GVJ_t *job = (GVJ_t*)stream;
UNSUPPORTED("e7b7ktlmv7bf41ah2uirp0wr4"); //     if (!job->gvc->write_fn && !job->output_data)
UNSUPPORTED("k1l61wmfzzg66hb7gu3ouzpe"); // 	return ferror(job->output_file);
UNSUPPORTED("5oxhd3fvp0gfmrmz12vndnjt"); //     return 0;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 ejkm659i2t9ni9abwvls81srg
// size_t gvfwrite (const void *ptr, size_t size, size_t nmemb, FILE *stream) 
public static Object gvfwrite(Object... arg) {
UNSUPPORTED("9jmr6dp0tzh6pynfebkrgg0qu"); // size_t gvfwrite (const void *ptr, size_t size, size_t nmemb, FILE *stream)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("2fw95dei1ujeec0ctcdy57c1x"); //     assert(size = sizeof(char));
UNSUPPORTED("bnwpx03tbl086znf32xqomw8m"); //     return gvwrite((GVJ_t*)stream, ptr, nmemb);
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 baq4lnmy9b8h5r38t5kac1wfp
// int gvputs(GVJ_t * job, const char *s) 
public static Object gvputs(Object... arg) {
UNSUPPORTED("1p5e428gxaoorzh5qz00rfy0k"); // int gvputs(GVJ_t * job, const char *s)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("6sarrx22njzxoct1tqx2jnqwz"); //     size_t len = strlen(s);
UNSUPPORTED("ebjgxf9iw1qng39s9u5strvc4"); //     if (gvwrite (job, s, len) != len) {
UNSUPPORTED("5c6r4edgvaov4ot4iejbv50ew"); // 	return EOF;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("68vhwhq54lavtuaojheojvecm"); //     return +1;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 dmzoqt3ukt72kvftmdjotritt
// int gvputc(GVJ_t * job, int c) 
public static Object gvputc(Object... arg) {
UNSUPPORTED("9mag55jpuhl60bjf9unj7xp92"); // int gvputc(GVJ_t * job, int c)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("79xkgu0m0ig0jh2o4v92li22p"); //     const char cc = c;
UNSUPPORTED("fkmtzb4a1u4ktyy2bnjez0jd"); //     if (gvwrite (job, &cc, 1) != 1) {
UNSUPPORTED("5c6r4edgvaov4ot4iejbv50ew"); // 	return EOF;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("bskm24m9z4b23box60oxnymv"); //     return c;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 cv9w48stixnm7kbet4eftjadx
// int gvflush (GVJ_t * job) 
public static Object gvflush(Object... arg) {
UNSUPPORTED("5ckii4epkwj83v2nnt54ofh8d"); // int gvflush (GVJ_t * job)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("f23odcxaxyzdd27mwuhiizdj4"); //     if (job->output_file
UNSUPPORTED("3bjid3e6jo6zuivokehya58qk"); //       && ! job->external_context
UNSUPPORTED("8z4jf2igc0co6dh6zx1110nbs"); //       && ! job->gvc->write_fn) {
UNSUPPORTED("5x7mdmw0x73vwxz61fk2yrvdm"); // 	return fflush(job->output_file);
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("div10atae09n36x269sl208r1"); //     else
UNSUPPORTED("c9ckhc8veujmwcw0ar3u3zld4"); // 	return 0;
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 330e9khckra3n5ssrwak5tfea
// static void gvdevice_close(GVJ_t * job) 
public static Object gvdevice_close(Object... arg) {
UNSUPPORTED("1kd1owjv512h3o8vja7myre50"); // static void gvdevice_close(GVJ_t * job)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("1id7d9wccsfisc4cede1yqiu2"); //     if (job->output_filename
UNSUPPORTED("3d4zs0j1uyhbel5t3tudtwj7i"); //       && job->output_file != stdout 
UNSUPPORTED("9q0a541k3fxrd3zi25ga414vc"); //       && ! job->external_context) {
UNSUPPORTED("cn3fn60ccz3zrmkt743v9paw1"); //         if (job->output_file) {
UNSUPPORTED("3hu3ukt7mqj5b5sj0atl2z0f3"); //             fclose(job->output_file);
UNSUPPORTED("80vq4s94ms6swfrhmfvi3b7w9"); //             job->output_file = (void *)0;
UNSUPPORTED("4mhlpjofolwivhm0tl8cxznly"); //         }
UNSUPPORTED("54nje7askf5ueua9t49ftz331"); // 	job->output_filename = (void *)0;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 b8iwpcj6eij03r64m0360e7qs
// void gvdevice_format(GVJ_t * job) 
public static Object gvdevice_format(Object... arg) {
UNSUPPORTED("6z5f8fkhpqc1bo4eeaxujv2lr"); // void gvdevice_format(GVJ_t * job)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("44r8xaau6kv13q6liujnhwe0i"); //     gvdevice_engine_t *gvde = job->device.engine;
UNSUPPORTED("9ohvvtakp5ajym21rtdihk6v3"); //     if (gvde && gvde->format)
UNSUPPORTED("39cv29fv2w96rfomdzkfzjvyq"); // 	gvde->format(job);
UNSUPPORTED("4jpzlipnntejeqtwox13wbkj6"); //     gvflush (job);
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 46sqk4d6tbspekxqa4h32d301
// void gvdevice_finalize(GVJ_t * job) 
public static Object gvdevice_finalize(Object... arg) {
UNSUPPORTED("e4zp9r9c2a5l12d2tadisfxsi"); // void gvdevice_finalize(GVJ_t * job)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("44r8xaau6kv13q6liujnhwe0i"); //     gvdevice_engine_t *gvde = job->device.engine;
UNSUPPORTED("2524ggmnv8w9jo35baux304v2"); //     boolean finalized_p = 0;
UNSUPPORTED("146f5u2dl3dt3devy0qnn6ylz"); //     if (job->flags & (1<<10)) {
UNSUPPORTED("crmqlvwk4eg6ed4abp1762kia"); // 	(job->common->errorfn) ("No libz support\n");
UNSUPPORTED("dtw3cma0ziyha0w534bszl0tx"); // 	exit(1);
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("79baddj51af049jts2lck9hor"); //     if (gvde) {
UNSUPPORTED("aw6mxvr3p8qyvr6ia2k3985n3"); // 	if (gvde->finalize) {
UNSUPPORTED("3ey2j155dc6rd51zymyvmijuc"); // 	    gvde->finalize(job);
UNSUPPORTED("351335dca88m4t8cwizgrx480"); // 	    finalized_p = (!(0));
UNSUPPORTED("flupwh3kosf3fkhkxllllt1"); // 	}
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("7rzmcwo03kb19vozyklhbkc51"); //     if (! finalized_p) {
UNSUPPORTED("1gp40nfjzceqr97tmcdvl9pmp"); //         /* if the device has no finalization then it uses file output */
UNSUPPORTED("f0b7hjoakoas2g2gn6tq5mmj"); // 	gvflush (job);
UNSUPPORTED("18z7v2vnzihvtt5nnbj4q3vpo"); // 	gvdevice_close(job);
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 1egirdomc1btc6kjc3dgard3o
// void gvprintf(GVJ_t * job, const char *format, ...) 
public static Object gvprintf(Object... arg) {
UNSUPPORTED("6fhkk7sp6y4sbd7qnk0vy9c"); // void gvprintf(GVJ_t * job, const char *format, ...)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("cf8w5z35zxww7q6yjin9lc9op"); //     char buf[BUFSIZ];
UNSUPPORTED("do481ti1ubw0ltflx52iweigu"); //     size_t len;
UNSUPPORTED("6kugk1wk3bh66t9k1cisj3wws"); //     va_list argp;
UNSUPPORTED("dylk0k9c053zb6dp6be1l1vlz"); //     char* bp = buf;
UNSUPPORTED("6kwm594sixkxroywvqwfb048w"); //     va_start(argp, format);
UNSUPPORTED("35tuv43una070zfa0wiq1t98n"); //     len = vsprintf((char *)buf, format, argp);
UNSUPPORTED("9ndja25a5zuub02u8harxnr00"); //     va_end(argp);
UNSUPPORTED("2h330v6zn1h7y9gty8zh7alu6"); //     gvwrite(job, bp, len);
UNSUPPORTED("d89vwxhq1gwui8as32g4iyoqu"); //     if (bp != buf)
UNSUPPORTED("e2oio1w9ngi670q15wq9lrmhg"); // 	free (bp);
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}


//1 1x6fpuyldpyrhmc2wjnt9r3t5
// static double maxnegnum = -999999999999999.99


//1 8lsntfgdc1tpzcdl8okf4a3xl
// static char maxnegnumstr[] = 




//3 alkcimcmn7kdav5dab3x68rin
// static char * gvprintnum (size_t *len, double number) 
public static Object gvprintnum(Object... arg) {
UNSUPPORTED("3sz3rah6s15fileyrygtyvsy4"); // static char * gvprintnum (size_t *len, double number)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("4mh7wqzk1u55blr6bw1de1tu9"); //     static char tmpbuf[sizeof(maxnegnumstr)];   /* buffer big enough for worst case */
UNSUPPORTED("6kv4u88wo0shex0a589kl7n3l"); //     char *result = tmpbuf+sizeof(maxnegnumstr); /* init result to end of tmpbuf */
UNSUPPORTED("bryisyhf57o6i69bxu4ctbqn5"); //     long int N;
UNSUPPORTED("do2ytzai3ubppaq9z7gm00438"); //     boolean showzeros, negative;
UNSUPPORTED("9qzqrvquucu9whxj9cncldfww"); //     int digit, i;
UNSUPPORTED("9gsgfs2guis9c3c3oi57mxpq2"); //     /*
UNSUPPORTED("2xdl5tkxrstua20058b47xnir"); //         number limited to a working range: maxnegnum >= n >= -maxnegnum
UNSUPPORTED("c4rjwybrx6rdpn7mwcxhhdt39"); // 	N = number * DECPLACES_SCALE rounded towards zero,
UNSUPPORTED("2p9kws9yqndurf6n81x7xull6"); // 	printing to buffer in reverse direction,
UNSUPPORTED("7ewnrrb059kj4ko3zan8jdop3"); // 	printing "." after DECPLACES
UNSUPPORTED("d35t12puy1knexzxrcyxbfpzj"); // 	suppressing trailing "0" and "."
UNSUPPORTED("795vpnc8yojryr8b46aidsu69"); //      */
UNSUPPORTED("3ivxbhh2hb2hdvn4pqede8m41"); //     if (number < maxnegnum) {		/* -ve limit */
UNSUPPORTED("ddh4m6vpzx2sr0pr1nvcd9c1t"); // 	*len = sizeof(maxnegnumstr)-1;  /* len doesn't include terminator */
UNSUPPORTED("103x8qz3872e9w56yknxqpcn6"); // 	return maxnegnumstr;;
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("9txbbc6xpk58kk4py9kg3n19n"); //     if (number > -maxnegnum) {		/* +ve limit */
UNSUPPORTED("epuuqnbsnn45v3ldhyk2vcdr1"); // 	*len = sizeof(maxnegnumstr)-2;  /* len doesn't include terminator or sign */
UNSUPPORTED("bsifggy4hjlok1u06o96n8wbn"); // 	return maxnegnumstr+1;		/* +1 to skip the '-' sign */
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("dvimjkoyeydgr0k6m42r5slva"); //     number *= 100;		/* scale by DECPLACES_SCALE */
UNSUPPORTED("2d934k0h3vqdzcfu8c1eroycs"); //     if (number < 0.0)			/* round towards zero */
UNSUPPORTED("c5rejlqioi39lspbd8otnd6yi"); //         N = number - 0.5;
UNSUPPORTED("div10atae09n36x269sl208r1"); //     else
UNSUPPORTED("8j9oa3w53bozqg7s4ajlxsnts"); //         N = number + 0.5;
UNSUPPORTED("8qqpzh7dl5q2wsuc67kne6ply"); //     if (N == 0) {			/* special case for exactly 0 */
UNSUPPORTED("e6psxu2ksijx06nl6oyb12m0f"); // 	*len = 1;
UNSUPPORTED("5l47ei1ksik36ffjsvqzl5yt6"); // 	return "0";
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("disxlfqxvj0f745ce20slp755"); //     if ((negative = (N < 0)))		/* avoid "-0" by testing rounded int */
UNSUPPORTED("nr08nn1obuklfiybvxp8gdik"); //         N = -N;				/* make number +ve */
UNSUPPORTED("wlrwbcx0owaybg2agqhp29n"); //     showzeros = 0;			/* don't print trailing zeros */
UNSUPPORTED("cikkkc06rloyghq0l2srb8vix"); //     for (i = 2; N || i > 0; i--) {  /* non zero remainder,
UNSUPPORTED("23hzgib77z0odvb23kw58z50s"); // 						or still in fractional part */
UNSUPPORTED("mbh9mebm1pcpg3owc2q4v5xe"); //         digit = N % 10;			/* next least-significant digit */
UNSUPPORTED("dzqyilj008a4yb3o3xb49ycd2"); //         N /= 10;
UNSUPPORTED("dcdutieoro6hdlcma5oe5l944"); //         if (digit || showzeros) {	/* if digit is non-zero,
UNSUPPORTED("2bm81grz43tgyhv4jyvdtmsxu"); // 						or if we are printing zeros */
UNSUPPORTED("dycrtr22pmd5lb4j8fj4yi49o"); //             *--result = digit | '0';	/* convert digit to ascii */
UNSUPPORTED("4pqd87ba27akvu14kome4tjze"); //             showzeros = (!(0));		/* from now on we must print zeros */
UNSUPPORTED("4mhlpjofolwivhm0tl8cxznly"); //         }
UNSUPPORTED("akn9rdzphyrql4nu286ezauux"); //         if (i == 1) {			/* if completed fractional part */
UNSUPPORTED("c98gv4lvkslivtou8sp5gjgv5"); //             if (showzeros)		/* if there was a non-zero fraction */
UNSUPPORTED("4e396qblfhvqsvgrwhjm4ucsw"); //                 *--result = '.';	/* print decimal point */
UNSUPPORTED("byd8n8dae1hujyrmzmt6sl0a6"); //             showzeros = (!(0));		/* print all digits in int part */
UNSUPPORTED("4mhlpjofolwivhm0tl8cxznly"); //         }
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("c1bnjfpib859s8q27bm1enmy3"); //     if (negative)			/* print "-" if needed */
UNSUPPORTED("cttlmjpav2o8r87erigjvs5ss"); //         *--result = '-';
UNSUPPORTED("do3t8frc2ui33ozznu6u8jfyd"); //     *len = tmpbuf+sizeof(maxnegnumstr) - result;
UNSUPPORTED("coko8du03f3lw8bdkapbks750"); //     return result;				
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 bmeyiwkpu2qs7dejvs5sh6o8v
// void gvprintdouble(GVJ_t * job, double num) 
public static Object gvprintdouble(Object... arg) {
UNSUPPORTED("cn6ds26lgsd3ujjqzrqcox1x5"); // void gvprintdouble(GVJ_t * job, double num)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("6ik3p99apk5rihypipg0uxu8i"); //     char *buf;
UNSUPPORTED("do481ti1ubw0ltflx52iweigu"); //     size_t len;
UNSUPPORTED("8yb00gp3xzh6a9lvd5mwcrg08"); //     buf = gvprintnum(&len, num);
UNSUPPORTED("56nuv66m5jb3tmogpfyb3w2e9"); //     gvwrite(job, buf, len);
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 5duaexp4igxjoostpe3eattxa
// void gvprintpointf(GVJ_t * job, pointf p) 
public static Object gvprintpointf(Object... arg) {
UNSUPPORTED("ctyuokdenka2j9jhskd3ql9px"); // void gvprintpointf(GVJ_t * job, pointf p)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("6ik3p99apk5rihypipg0uxu8i"); //     char *buf;
UNSUPPORTED("do481ti1ubw0ltflx52iweigu"); //     size_t len;
UNSUPPORTED("1gpnnoam3bzp9eo5m7opks45t"); //     buf = gvprintnum(&len, p.x);
UNSUPPORTED("56nuv66m5jb3tmogpfyb3w2e9"); //     gvwrite(job, buf, len);
UNSUPPORTED("4t98op6wrpqegev92jt8qeuma"); //     gvwrite(job, " ", 1);
UNSUPPORTED("2lggxf5etbmynuok5wquoc4h1"); //     buf = gvprintnum(&len, p.y);
UNSUPPORTED("56nuv66m5jb3tmogpfyb3w2e9"); //     gvwrite(job, buf, len);
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}




//3 9mtarrxfoqaaz8anygvcs0eni
// void gvprintpointflist(GVJ_t * job, pointf *p, int n) 
public static Object gvprintpointflist(Object... arg) {
UNSUPPORTED("bbdhtxxtsiyz2al9t7saa9yqh"); // void gvprintpointflist(GVJ_t * job, pointf *p, int n)
UNSUPPORTED("erg9i1970wdri39osu8hx2a6e"); // {
UNSUPPORTED("7s165b75za7edl3pgyc01kxis"); //     int i = 0;
UNSUPPORTED("5owjshc6gmd9o9lmekzcl6rvg"); //     while ((!(0))) {
UNSUPPORTED("5xxbbxkf0cife9hhrjjzgkvjt"); // 	gvprintpointf(job, p[i]);
UNSUPPORTED("e8aux6mfyxrmvk370dq1a2tt9"); //         if (++i >= n) break;
UNSUPPORTED("1rgsln2lih3ohh497shfjlgye"); //         gvwrite(job, " ", 1);
UNSUPPORTED("dvgyxsnyeqqnyzq696k3vskib"); //     }
UNSUPPORTED("c24nfmv9i7o5eoqaymbibp7m7"); // }

throw new UnsupportedOperationException();
}


}
