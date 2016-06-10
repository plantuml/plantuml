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
package h;
import java.util.Arrays;
import java.util.List;

import smetana.core.__ptr__;

//2 e3hfh80mtu75t9spasjqrsdrh

public interface re_opcode_t extends __ptr__ {
	public static List<String> DEFINITION = Arrays.asList(
"typedef enum",
"{",
"no_op = 0,",
"succeed,",
"exactn,",
"anychar,",
"charset,",
"charset_not,",
"start_memory,",
"stop_memory,",
"duplicate,",
"begline,",
"endline,",
"begbuf,",
"endbuf,",
"jump,",
"jump_past_alt,",
"on_failure_jump,",
"on_failure_keep_string_jump,",
"pop_failure_jump,",
"maybe_pop_jump,",
"dummy_failure_jump,",
"push_dummy_failure,",
"succeed_n,",
"jump_n,",
"set_number_at,      wordchar,",
"notwordchar,",
"wordbeg,",
"wordend,",
"wordbound,",
"notwordbound",
"}",
"re_opcode_t");
}

// typedef enum
// {
//     no_op = 0,
// 
//     /* Succeed right away--no more backtracking.  */
//     succeed,
// 
//     /* Followed by one byte giving n, then by n literal bytes.  */
//     exactn,
// 
//     /* Matches any (more or less) character.  */
//     anychar,
// 
//     /* Matches any one char belonging to specified set.  First
//        following byte is number of bitmap bytes.  Then come bytes
//        for a bitmap saying which chars are in.  Bits in each byte
//        are ordered low-bit-first.  A character is in the set if its
//        bit is 1.  A character too large to have a bit in the map is
//        automatically not in the set.  */
//     charset,
// 
//     /* Same parameters as charset, but match any character that is
//        not one of those specified.  */
//     charset_not,
// 
//     /* Start remembering the text that is matched, for storing in a
//        register.  Followed by one byte with the register number, in
//        the range 0 to one less than the pattern buffer's re_nsub
//        field.  Then followed by one byte with the number of groups
//        inner to this one.  (This last has to be part of the
//        start_memory only because we need it in the on_failure_jump
//        of re_match_2.)  */
//     start_memory,
// 
//     /* Stop remembering the text that is matched and store it in a
//        memory register.  Followed by one byte with the register
//        number, in the range 0 to one less than `re_nsub' in the
//        pattern buffer, and one byte with the number of inner groups,
//        just like `start_memory'.  (We need the number of inner
//        groups here because we don't have any easy way of finding the
//        corresponding start_memory when we're at a stop_memory.)  */
//     stop_memory,
// 
//     /* Match a duplicate of something remembered. Followed by one
//        byte containing the register number.  */
//     duplicate,
// 
//     /* Fail unless at beginning of line.  */
//     begline,
// 
//     /* Fail unless at end of line.  */
//     endline,
// 
//     /* Succeeds if at beginning of buffer (if emacs) or at beginning
//        of string to be matched (if not).  */
//     begbuf,
// 
//     /* Analogously, for end of buffer/string.  */
//     endbuf,
// 
//     /* Followed by two byte relative address to which to jump.  */
//     jump,
// 
//     /* Same as jump, but marks the end of an alternative.  */
//     jump_past_alt,
// 
//     /* Followed by two-byte relative address of place to resume at
//        in case of failure.  */
//     on_failure_jump,
// 
//     /* Like on_failure_jump, but pushes a placeholder instead of the
//        current string position when executed.  */
//     on_failure_keep_string_jump,
// 
//     /* Throw away latest failure point and then jump to following
//        two-byte relative address.  */
//     pop_failure_jump,
// 
//     /* Change to pop_failure_jump if know won't have to backtrack to
//        match; otherwise change to jump.  This is used to jump
//        back to the beginning of a repeat.  If what follows this jump
//        clearly won't match what the repeat does, such that we can be
//        sure that there is no use backtracking out of repetitions
//        already matched, then we change it to a pop_failure_jump.
//        Followed by two-byte address.  */
//     maybe_pop_jump,
// 
//     /* Jump to following two-byte address, and push a dummy failure
//        point. This failure point will be thrown away if an attempt
//        is made to use it for a failure.  A `+' construct makes this
//        before the first repeat.  Also used as an intermediary kind
//        of jump when compiling an alternative.  */
//     dummy_failure_jump,
// 
//     /* Push a dummy failure point and continue.  Used at the end of
//        alternatives.  */
//     push_dummy_failure,
// 
//     /* Followed by two-byte relative address and two-byte number n.
//        After matching N times, jump to the address upon failure.  */
//     succeed_n,
// 
//     /* Followed by two-byte relative address, and two-byte number n.
//        Jump to the address N times, then fail.  */
//     jump_n,
// 
//     /* Set the following two-byte relative address to the
//        subsequent two-byte number.  The address *includes* the two
//        bytes of number.  */
//     set_number_at,
// 
//     wordchar,   /* Matches any word-constituent character.  */
//     notwordchar,        /* Matches any char that is not a word-constituent.  */
// 
//     wordbeg,    /* Succeeds if at word beginning.  */
//     wordend,    /* Succeeds if at word end.  */
// 
//     wordbound,  /* Succeeds if at a word boundary.  */
//     notwordbound        /* Succeeds if not at a word boundary.  */
// 
// 
//     
//     
//     
// 
//     
// 
//     
// 
//     
//     
// 
// } re_opcode_t;