/*
ASCIIMathTeXImg.js
Based on ASCIIMathML, Version 1.4.7 Aug 30, 2005, (c) Peter Jipsen http://www.chapman.edu/~jipsen
Modified with TeX conversion for IMG rendering Sept 6, 2006 (c) David Lippman http://www.pierce.ctc.edu/dlippman
  Updated to match ver 2.2 Mar 3, 2014
  Latest at https://github.com/mathjax/asciimathml

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.
*/

//var AMTcgiloc = '';			//set to the URL of your LaTex renderer
var noMathRender = false;


var config = {
  translateOnLoad: true,		  //true to autotranslate
  mathcolor: "",       	      // defaults to back, or specify any other color
  displaystyle: true,         // puts limits above and below large operators
  showasciiformulaonhover: true, // helps students learn ASCIIMath
  decimalsign: ".",           // change to "," if you like, beware of `(1,2)`!
  AMdelimiter1: "`", AMescape1: "\\\\`", // can use other characters
  AMusedelimiter2: false, 	  //whether to use second delimiter below
  AMdelimiter2: "$", AMescape2: "\\\\\\$", AMdelimiter2regexp: "\\$",
  AMdocumentId: "wikitext",   // PmWiki element containing math (default=body)
  doubleblankmathdelimiter: false // if true,  x+1  is equal to `x+1`
};                                // for IE this works only in <!--   -->

var CONST = 0, UNARY = 1, BINARY = 2, INFIX = 3, LEFTBRACKET = 4, 
    RIGHTBRACKET = 5, SPACE = 6, UNDEROVER = 7, DEFINITION = 8,
    LEFTRIGHT = 9, TEXT = 10; // token types

var AMsqrt = {input:"sqrt", tag:"msqrt", output:"sqrt", tex:null, ttype:UNARY},
  AMroot  = {input:"root", tag:"mroot", output:"root", tex:null, ttype:BINARY},
  AMfrac  = {input:"frac", tag:"mfrac", output:"/",    tex:null, ttype:BINARY},
  AMdiv   = {input:"/",    tag:"mfrac", output:"/",    tex:null, ttype:INFIX},
  AMover  = {input:"stackrel", tag:"mover", output:"stackrel", tex:null, ttype:BINARY},
  AMsub   = {input:"_",    tag:"msub",  output:"_",    tex:null, ttype:INFIX},
  AMsup   = {input:"^",    tag:"msup",  output:"^",    tex:null, ttype:INFIX},
  AMtext  = {input:"text", tag:"mtext", output:"text", tex:null, ttype:TEXT},
  AMmbox  = {input:"mbox", tag:"mtext", output:"mbox", tex:null, ttype:TEXT},
  AMquote = {input:"\"",   tag:"mtext", output:"mbox", tex:null, ttype:TEXT};

var AMsymbols = [
//some greek symbols
{input:"alpha",  tag:"mi", output:"\u03B1", tex:null, ttype:CONST},
{input:"beta",   tag:"mi", output:"\u03B2", tex:null, ttype:CONST},
{input:"chi",    tag:"mi", output:"\u03C7", tex:null, ttype:CONST},
{input:"delta",  tag:"mi", output:"\u03B4", tex:null, ttype:CONST},
{input:"Delta",  tag:"mo", output:"\u0394", tex:null, ttype:CONST},
{input:"epsi",   tag:"mi", output:"\u03B5", tex:"epsilon", ttype:CONST},
{input:"varepsilon", tag:"mi", output:"\u025B", tex:null, ttype:CONST},
{input:"eta",    tag:"mi", output:"\u03B7", tex:null, ttype:CONST},
{input:"gamma",  tag:"mi", output:"\u03B3", tex:null, ttype:CONST},
{input:"Gamma",  tag:"mo", output:"\u0393", tex:null, ttype:CONST},
{input:"iota",   tag:"mi", output:"\u03B9", tex:null, ttype:CONST},
{input:"kappa",  tag:"mi", output:"\u03BA", tex:null, ttype:CONST},
{input:"lambda", tag:"mi", output:"\u03BB", tex:null, ttype:CONST},
{input:"Lambda", tag:"mo", output:"\u039B", tex:null, ttype:CONST},
{input:"lamda",   tag:"mi", output:"lambda", tex:null, ttype:DEFINITION},
{input:"Lamda",   tag:"mi", output:"Lambda", tex:null, ttype:DEFINITION},
{input:"mu",     tag:"mi", output:"\u03BC", tex:null, ttype:CONST},
{input:"nu",     tag:"mi", output:"\u03BD", tex:null, ttype:CONST},
{input:"omega",  tag:"mi", output:"\u03C9", tex:null, ttype:CONST},
{input:"Omega",  tag:"mo", output:"\u03A9", tex:null, ttype:CONST},
{input:"phi",    tag:"mi", output:"\u03C6", tex:null, ttype:CONST},
{input:"varphi", tag:"mi", output:"\u03D5", tex:null, ttype:CONST},
{input:"Phi",    tag:"mo", output:"\u03A6", tex:null, ttype:CONST},
{input:"pi",     tag:"mi", output:"\u03C0", tex:null, ttype:CONST},
{input:"Pi",     tag:"mo", output:"\u03A0", tex:null, ttype:CONST},
{input:"psi",    tag:"mi", output:"\u03C8", tex:null, ttype:CONST},
{input:"Psi",    tag:"mi", output:"\u03A8", tex:null, ttype:CONST},
{input:"rho",    tag:"mi", output:"\u03C1", tex:null, ttype:CONST},
{input:"sigma",  tag:"mi", output:"\u03C3", tex:null, ttype:CONST},
{input:"Sigma",  tag:"mo", output:"\u03A3", tex:null, ttype:CONST},
{input:"tau",    tag:"mi", output:"\u03C4", tex:null, ttype:CONST},
{input:"theta",  tag:"mi", output:"\u03B8", tex:null, ttype:CONST},
{input:"vartheta", tag:"mi", output:"\u03D1", tex:null, ttype:CONST},
{input:"Theta",  tag:"mo", output:"\u0398", tex:null, ttype:CONST},
{input:"upsilon", tag:"mi", output:"\u03C5", tex:null, ttype:CONST},
{input:"xi",     tag:"mi", output:"\u03BE", tex:null, ttype:CONST},
{input:"Xi",     tag:"mo", output:"\u039E", tex:null, ttype:CONST},
{input:"zeta",   tag:"mi", output:"\u03B6", tex:null, ttype:CONST},

//binary operation symbols
{input:"*",  tag:"mo", output:"\u22C5", tex:"cdot", ttype:CONST},
{input:"**", tag:"mo", output:"\u2217", tex:"ast", ttype:CONST},
{input:"***", tag:"mo", output:"\u22C6", tex:"star", ttype:CONST},
{input:"//", tag:"mo", output:"/",      tex:"/", ttype:CONST, val:true, notexcopy:true},
{input:"\\\\", tag:"mo", output:"\\",   tex:"backslash", ttype:CONST},
{input:"setminus", tag:"mo", output:"\\", tex:null, ttype:CONST},
{input:"xx", tag:"mo", output:"\u00D7", tex:"times", ttype:CONST},
{input:"|><", tag:"mo", output:"\u22C9", tex:"ltimes", ttype:CONST},
{input:"><|", tag:"mo", output:"\u22CA", tex:"rtimes", ttype:CONST},
{input:"|><|", tag:"mo", output:"\u22C8", tex:"bowtie", ttype:CONST},
{input:"-:", tag:"mo", output:"\u00F7", tex:"div", ttype:CONST},
{input:"divide",   tag:"mo", output:"-:", tex:null, ttype:DEFINITION},
{input:"@",  tag:"mo", output:"\u2218", tex:"circ", ttype:CONST},
{input:"o+", tag:"mo", output:"\u2295", tex:"oplus", ttype:CONST},
{input:"ox", tag:"mo", output:"\u2297", tex:"otimes", ttype:CONST},
{input:"o.", tag:"mo", output:"\u2299", tex:"odot", ttype:CONST},
{input:"sum", tag:"mo", output:"\u2211", tex:null, ttype:UNDEROVER},
{input:"prod", tag:"mo", output:"\u220F", tex:null, ttype:UNDEROVER},
{input:"^^",  tag:"mo", output:"\u2227", tex:"wedge", ttype:CONST},
{input:"^^^", tag:"mo", output:"\u22C0", tex:"bigwedge", ttype:UNDEROVER},
{input:"vv",  tag:"mo", output:"\u2228", tex:"vee", ttype:CONST},
{input:"vvv", tag:"mo", output:"\u22C1", tex:"bigvee", ttype:UNDEROVER},
{input:"nn",  tag:"mo", output:"\u2229", tex:"cap", ttype:CONST},
{input:"nnn", tag:"mo", output:"\u22C2", tex:"bigcap", ttype:UNDEROVER},
{input:"uu",  tag:"mo", output:"\u222A", tex:"cup", ttype:CONST},
{input:"uuu", tag:"mo", output:"\u22C3", tex:"bigcup", ttype:UNDEROVER},
{input:"overset", tag:"mover", output:"stackrel", tex:null, ttype:BINARY},
{input:"underset", tag:"munder", output:"stackrel", tex:null, ttype:BINARY},

//binary relation symbols
{input:"!=",  tag:"mo", output:"\u2260", tex:"ne", ttype:CONST},
{input:":=",  tag:"mo", output:":=",     tex:null, ttype:CONST},
{input:"lt",  tag:"mo", output:"<",      tex:null, ttype:CONST},
{input:"gt",  tag:"mo", output:">",      tex:null, ttype:CONST},
{input:"<=",  tag:"mo", output:"\u2264", tex:"le", ttype:CONST},
{input:"lt=", tag:"mo", output:"\u2264", tex:"leq", ttype:CONST},
{input:"gt=",  tag:"mo", output:"\u2265", tex:"geq", ttype:CONST},
{input:">=",  tag:"mo", output:"\u2265", tex:"ge", ttype:CONST},
{input:"-<",  tag:"mo", output:"\u227A", tex:"prec", ttype:CONST},
{input:"-lt", tag:"mo", output:"\u227A", tex:null, ttype:CONST},
{input:">-",  tag:"mo", output:"\u227B", tex:"succ", ttype:CONST},
{input:"-<=", tag:"mo", output:"\u2AAF", tex:"preceq", ttype:CONST},
{input:">-=", tag:"mo", output:"\u2AB0", tex:"succeq", ttype:CONST},
{input:"in",  tag:"mo", output:"\u2208", tex:null, ttype:CONST},
{input:"!in", tag:"mo", output:"\u2209", tex:"notin", ttype:CONST},
{input:"sub", tag:"mo", output:"\u2282", tex:"subset", ttype:CONST},
{input:"sup", tag:"mo", output:"\u2283", tex:"supset", ttype:CONST},
{input:"sube", tag:"mo", output:"\u2286", tex:"subseteq", ttype:CONST},
{input:"supe", tag:"mo", output:"\u2287", tex:"supseteq", ttype:CONST},
{input:"-=",  tag:"mo", output:"\u2261", tex:"equiv", ttype:CONST},
{input:"~=",  tag:"mo", output:"\u2245", tex:"stackrel{\\sim}{=}", ttype:CONST}, //back hack b/c mimetex doesn't support /cong
{input:"cong",   tag:"mo", output:"~=", tex:null, ttype:DEFINITION},
{input:"~~",  tag:"mo", output:"\u2248", tex:"approx", ttype:CONST},
{input:"prop", tag:"mo", output:"\u221D", tex:"propto", ttype:CONST},

//logical symbols
{input:"and", tag:"mtext", output:"and", tex:null, ttype:SPACE},
{input:"or",  tag:"mtext", output:"or",  tex:null, ttype:SPACE},
{input:"not", tag:"mo", output:"\u00AC", tex:"neg", ttype:CONST},
{input:"=>",  tag:"mo", output:"\u21D2", tex:"Rightarrow", ttype:CONST},
{input:"implies",   tag:"mo", output:"=>", tex:null, ttype:DEFINITION},
{input:"if",  tag:"mo", output:"if",     tex:null, ttype:SPACE},
{input:"<=>", tag:"mo", output:"\u21D4", tex:"Leftrightarrow", ttype:CONST},
{input:"iff",   tag:"mo", output:"<=>", tex:null, ttype:DEFINITION},
{input:"AA",  tag:"mo", output:"\u2200", tex:"forall", ttype:CONST},
{input:"EE",  tag:"mo", output:"\u2203", tex:"exists", ttype:CONST},
{input:"_|_", tag:"mo", output:"\u22A5", tex:"bot", ttype:CONST},
{input:"TT",  tag:"mo", output:"\u22A4", tex:"top", ttype:CONST},
{input:"|--",  tag:"mo", output:"\u22A2", tex:"vdash", ttype:CONST},
{input:"|==",  tag:"mo", output:"\u22A8", tex:"models", ttype:CONST}, //mimetex doesn't support

//grouping brackets
{input:"(", tag:"mo", output:"(", tex:null, ttype:LEFTBRACKET, val:true},
{input:")", tag:"mo", output:")", tex:null, ttype:RIGHTBRACKET, val:true},
{input:"[", tag:"mo", output:"[", tex:null, ttype:LEFTBRACKET, val:true},
{input:"]", tag:"mo", output:"]", tex:null, ttype:RIGHTBRACKET, val:true},
{input:"{", tag:"mo", output:"{", tex:"lbrace", ttype:LEFTBRACKET},
{input:"}", tag:"mo", output:"}", tex:"rbrace", ttype:RIGHTBRACKET},
{input:"|", tag:"mo", output:"|", tex:null, ttype:LEFTRIGHT, val:true},
//{input:"||", tag:"mo", output:"||", tex:null, ttype:LEFTRIGHT},
{input:"(:", tag:"mo", output:"\u2329", tex:"langle", ttype:LEFTBRACKET},
{input:":)", tag:"mo", output:"\u232A", tex:"rangle", ttype:RIGHTBRACKET},
{input:"<<", tag:"mo", output:"\u2329", tex:"langle", ttype:LEFTBRACKET},
{input:">>", tag:"mo", output:"\u232A", tex:"rangle", ttype:RIGHTBRACKET},
{input:"{:", tag:"mo", output:"{:", tex:null, ttype:LEFTBRACKET, invisible:true},
{input:":}", tag:"mo", output:":}", tex:null, ttype:RIGHTBRACKET, invisible:true},

//miscellaneous symbols
{input:"int",  tag:"mo", output:"\u222B", tex:null, ttype:CONST},
{input:"dx",   tag:"mi", output:"{:d x:}", tex:null, ttype:DEFINITION},
{input:"dy",   tag:"mi", output:"{:d y:}", tex:null, ttype:DEFINITION},
{input:"dz",   tag:"mi", output:"{:d z:}", tex:null, ttype:DEFINITION},
{input:"dt",   tag:"mi", output:"{:d t:}", tex:null, ttype:DEFINITION},
{input:"oint", tag:"mo", output:"\u222E", tex:null, ttype:CONST},
{input:"del",  tag:"mo", output:"\u2202", tex:"partial", ttype:CONST},
{input:"grad", tag:"mo", output:"\u2207", tex:"nabla", ttype:CONST},
{input:"+-",   tag:"mo", output:"\u00B1", tex:"pm", ttype:CONST},
{input:"O/",   tag:"mo", output:"\u2205", tex:"emptyset", ttype:CONST},
{input:"oo",   tag:"mo", output:"\u221E", tex:"infty", ttype:CONST},
{input:"aleph", tag:"mo", output:"\u2135", tex:null, ttype:CONST},
{input:"...",  tag:"mo", output:"...",    tex:"ldots", ttype:CONST},
{input:":.",  tag:"mo", output:"\u2234",  tex:"therefore", ttype:CONST},
{input:":'",  tag:"mo", output:"\u2235",  tex:"because", ttype:CONST},
{input:"/_",  tag:"mo", output:"\u2220",  tex:"angle", ttype:CONST},
{input:"/_\\",  tag:"mo", output:"\u25B3",  tex:"triangle", ttype:CONST},
{input:"\\ ",  tag:"mo", output:"\u00A0", tex:null, ttype:CONST, val:true},
{input:"frown",  tag:"mo", output:"\u2322", tex:null, ttype:CONST},
{input:"%",  tag:"mo", output:"%", tex:"%", ttype:CONST, notexcopy:true},
{input:"quad", tag:"mo", output:"\u00A0\u00A0", tex:null, ttype:CONST},
{input:"qquad", tag:"mo", output:"\u00A0\u00A0\u00A0\u00A0", tex:null, ttype:CONST},
{input:"cdots", tag:"mo", output:"\u22EF", tex:null, ttype:CONST},
{input:"vdots", tag:"mo", output:"\u22EE", tex:null, ttype:CONST},
{input:"ddots", tag:"mo", output:"\u22F1", tex:null, ttype:CONST},
{input:"diamond", tag:"mo", output:"\u22C4", tex:null, ttype:CONST},
{input:"square", tag:"mo", output:"\u25A1", tex:"boxempty", ttype:CONST},
{input:"|__", tag:"mo", output:"\u230A",  tex:"lfloor", ttype:CONST},
{input:"__|", tag:"mo", output:"\u230B",  tex:"rfloor", ttype:CONST},
{input:"|~", tag:"mo", output:"\u2308",  tex:"lceil", ttype:CONST},
{input:"lceiling",   tag:"mo", output:"|~", tex:null, ttype:DEFINITION},
{input:"~|", tag:"mo", output:"\u2309",  tex:"rceil", ttype:CONST},
{input:"rceiling",   tag:"mo", output:"~|", tex:null, ttype:DEFINITION},
{input:"CC",  tag:"mo", output:"\u2102", tex:"mathbb{C}", ttype:CONST, notexcopy:true},
{input:"NN",  tag:"mo", output:"\u2115", tex:"mathbb{N}", ttype:CONST, notexcopy:true},
{input:"QQ",  tag:"mo", output:"\u211A", tex:"mathbb{Q}", ttype:CONST, notexcopy:true},
{input:"RR",  tag:"mo", output:"\u211D", tex:"mathbb{R}", ttype:CONST, notexcopy:true},
{input:"ZZ",  tag:"mo", output:"\u2124", tex:"mathbb{Z}", ttype:CONST, notexcopy:true},
{input:"f",   tag:"mi", output:"f",      tex:null, ttype:UNARY, func:true, val:true},
{input:"g",   tag:"mi", output:"g",      tex:null, ttype:UNARY, func:true, val:true},
{input:"''", tag:"mo", output:"''", tex:null, val:true},
{input:"'''", tag:"mo", output:"'''", tex:null, val:true},
{input:"''''", tag:"mo", output:"''''", tex:null, val:true},


//standard functions
{input:"lim",  tag:"mo", output:"lim", tex:null, ttype:UNDEROVER},
{input:"Lim",  tag:"mo", output:"Lim", tex:null, ttype:UNDEROVER},
{input:"sin",  tag:"mo", output:"sin", tex:null, ttype:UNARY, func:true},
{input:"cos",  tag:"mo", output:"cos", tex:null, ttype:UNARY, func:true},
{input:"tan",  tag:"mo", output:"tan", tex:null, ttype:UNARY, func:true},
{input:"arcsin",  tag:"mo", output:"arcsin", tex:null, ttype:UNARY, func:true},
{input:"arccos",  tag:"mo", output:"arccos", tex:null, ttype:UNARY, func:true},
{input:"arctan",  tag:"mo", output:"arctan", tex:null, ttype:UNARY, func:true},
{input:"sinh", tag:"mo", output:"sinh", tex:null, ttype:UNARY, func:true},
{input:"cosh", tag:"mo", output:"cosh", tex:null, ttype:UNARY, func:true},
{input:"tanh", tag:"mo", output:"tanh", tex:null, ttype:UNARY, func:true},
{input:"cot",  tag:"mo", output:"cot", tex:null, ttype:UNARY, func:true},
{input:"coth",  tag:"mo", output:"coth", tex:null, ttype:UNARY, func:true},
{input:"sech",  tag:"mo", output:"sech", tex:null, ttype:UNARY, func:true},
{input:"csch",  tag:"mo", output:"csch", tex:null, ttype:UNARY, func:true},
{input:"sec",  tag:"mo", output:"sec", tex:null, ttype:UNARY, func:true},
{input:"csc",  tag:"mo", output:"csc", tex:null, ttype:UNARY, func:true},
{input:"log",  tag:"mo", output:"log", tex:null, ttype:UNARY, func:true},
{input:"ln",   tag:"mo", output:"ln",  tex:null, ttype:UNARY, func:true},
{input:"abs",   tag:"mo", output:"abs",  tex:null, ttype:UNARY, notexcopy:true, rewriteleftright:["|","|"]},
{input:"norm",   tag:"mo", output:"norm",  tex:null, ttype:UNARY, notexcopy:true, rewriteleftright:["\\|","\\|"]},
{input:"floor",   tag:"mo", output:"floor",  tex:null, ttype:UNARY, notexcopy:true, rewriteleftright:["\\lfloor","\\rfloor"]}, 
{input:"ceil",   tag:"mo", output:"ceil",  tex:null, ttype:UNARY, notexcopy:true, rewriteleftright:["\\lceil","\\rceil"]}, 
{input:"Sin",  tag:"mo", output:"sin", tex:null, ttype:UNARY, func:true},
{input:"Cos",  tag:"mo", output:"cos", tex:null, ttype:UNARY, func:true},
{input:"Tan",  tag:"mo", output:"tan", tex:null, ttype:UNARY, func:true},
{input:"Arcsin",  tag:"mo", output:"arcsin", tex:null, ttype:UNARY, func:true},
{input:"Arccos",  tag:"mo", output:"arccos", tex:null, ttype:UNARY, func:true},
{input:"Arctan",  tag:"mo", output:"arctan", tex:null, ttype:UNARY, func:true},
{input:"Sinh", tag:"mo", output:"sinh", tex:null, ttype:UNARY, func:true},
{input:"Sosh", tag:"mo", output:"cosh", tex:null, ttype:UNARY, func:true},
{input:"Tanh", tag:"mo", output:"tanh", tex:null, ttype:UNARY, func:true},
{input:"Cot",  tag:"mo", output:"cot", tex:null, ttype:UNARY, func:true},
{input:"Sec",  tag:"mo", output:"sec", tex:null, ttype:UNARY, func:true},
{input:"Csc",  tag:"mo", output:"csc", tex:null, ttype:UNARY, func:true},
{input:"Log",  tag:"mo", output:"log", tex:null, ttype:UNARY, func:true},
{input:"Ln",   tag:"mo", output:"ln",  tex:null, ttype:UNARY, func:true},
{input:"Abs",   tag:"mo", output:"abs",  tex:null, ttype:UNARY, notexcopy:true, rewriteleftright:["|","|"]},

{input:"det",  tag:"mo", output:"det", tex:null, ttype:UNARY, func:true},
{input:"exp",  tag:"mo", output:"exp", tex:null, ttype:UNARY, func:true},
{input:"dim",  tag:"mo", output:"dim", tex:null, ttype:CONST},
{input:"mod",  tag:"mo", output:"mod", tex:"text{mod}", ttype:CONST, notexcopy:true},
{input:"gcd",  tag:"mo", output:"gcd", tex:null, ttype:UNARY, func:true},
{input:"lcm",  tag:"mo", output:"lcm", tex:"text{lcm}", ttype:UNARY, func:true, notexcopy:true},
{input:"lub",  tag:"mo", output:"lub", tex:null, ttype:CONST},
{input:"glb",  tag:"mo", output:"glb", tex:null, ttype:CONST},
{input:"min",  tag:"mo", output:"min", tex:null, ttype:UNDEROVER},
{input:"max",  tag:"mo", output:"max", tex:null, ttype:UNDEROVER},

//arrows
{input:"uarr", tag:"mo", output:"\u2191", tex:"uparrow", ttype:CONST},
{input:"darr", tag:"mo", output:"\u2193", tex:"downarrow", ttype:CONST},
{input:"rarr", tag:"mo", output:"\u2192", tex:"rightarrow", ttype:CONST},
{input:"->",   tag:"mo", output:"\u2192", tex:"to", ttype:CONST},
{input:">->",   tag:"mo", output:"\u21A3", tex:"rightarrowtail", ttype:CONST},
{input:"->>",   tag:"mo", output:"\u21A0", tex:"twoheadrightarrow", ttype:CONST},
{input:">->>",   tag:"mo", output:"\u2916", tex:"twoheadrightarrowtail", ttype:CONST},
{input:"|->",  tag:"mo", output:"\u21A6", tex:"mapsto", ttype:CONST},
{input:"larr", tag:"mo", output:"\u2190", tex:"leftarrow", ttype:CONST},
{input:"harr", tag:"mo", output:"\u2194", tex:"leftrightarrow", ttype:CONST},
{input:"rArr", tag:"mo", output:"\u21D2", tex:"Rightarrow", ttype:CONST},
{input:"lArr", tag:"mo", output:"\u21D0", tex:"Leftarrow", ttype:CONST},
{input:"hArr", tag:"mo", output:"\u21D4", tex:"Leftrightarrow", ttype:CONST},

//commands with argument
AMsqrt, AMroot, AMfrac, AMdiv, AMover, AMsub, AMsup,
{input:"cancel", tag:"menclose", output:"cancel", tex:null, ttype:UNARY},
{input:"Sqrt", tag:"msqrt", output:"sqrt", tex:null, ttype:UNARY},
{input:"hat", tag:"mover", output:"\u005E", tex:null, ttype:UNARY, acc:true},
{input:"bar", tag:"mover", output:"\u00AF", tex:"overline", ttype:UNARY, acc:true},
{input:"vec", tag:"mover", output:"\u2192", tex:null, ttype:UNARY, acc:true},
{input:"tilde", tag:"mover", output:"~", tex:null, ttype:UNARY, acc:true}, 
{input:"dot", tag:"mover", output:".",      tex:null, ttype:UNARY, acc:true},
{input:"ddot", tag:"mover", output:"..",    tex:null, ttype:UNARY, acc:true},
{input:"ul", tag:"munder", output:"\u0332", tex:"underline", ttype:UNARY, acc:true},
{input:"ubrace", tag:"munder", output:"\u23DF", tex:"underbrace", ttype:UNARY, acc:true},
{input:"obrace", tag:"mover", output:"\u23DE", tex:"overbrace", ttype:UNARY, acc:true},
AMtext, AMmbox, AMquote,
//{input:"var", tag:"mstyle", atname:"fontstyle", atval:"italic", output:"var", tex:null, ttype:UNARY},
{input:"color", tag:"mstyle", ttype:BINARY},
{input:"bb", tag:"mstyle", atname:"mathvariant", atval:"bold", output:"bb", tex:"mathbf", ttype:UNARY, notexcopy:true},
{input:"mathbf", tag:"mstyle", atname:"mathvariant", atval:"bold", output:"mathbf", tex:null, ttype:UNARY},
{input:"sf", tag:"mstyle", atname:"mathvariant", atval:"sans-serif", output:"sf", tex:"mathsf", ttype:UNARY, notexcopy:true},
{input:"mathsf", tag:"mstyle", atname:"mathvariant", atval:"sans-serif", output:"mathsf", tex:null, ttype:UNARY},
{input:"bbb", tag:"mstyle", atname:"mathvariant", atval:"double-struck", output:"bbb", tex:"mathbb", ttype:UNARY, notexcopy:true},
{input:"mathbb", tag:"mstyle", atname:"mathvariant", atval:"double-struck", output:"mathbb", tex:null, ttype:UNARY},
{input:"cc",  tag:"mstyle", atname:"mathvariant", atval:"script", output:"cc", tex:"mathcal", ttype:UNARY, notexcopy:true},
{input:"mathcal", tag:"mstyle", atname:"mathvariant", atval:"script", output:"mathcal", tex:null, ttype:UNARY},
{input:"tt",  tag:"mstyle", atname:"mathvariant", atval:"monospace", output:"tt", tex:"mathtt", ttype:UNARY, notexcopy:true},
{input:"mathtt", tag:"mstyle", atname:"mathvariant", atval:"monospace", output:"mathtt", tex:null, ttype:UNARY},
{input:"fr",  tag:"mstyle", atname:"mathvariant", atval:"fraktur", output:"fr", tex:"mathfrak", ttype:UNARY, notexcopy:true},
{input:"mathfrak",  tag:"mstyle", atname:"mathvariant", atval:"fraktur", output:"mathfrak", tex:null, ttype:UNARY}
];

function compareNames(s1,s2) {
  if (s1.input > s2.input) return 1
  else return -1;
}

var AMnames = []; //list of input symbols

function AMinitSymbols() {
  var i;
  var symlen = AMsymbols.length;
  for (i=0; i<symlen; i++) {
    if (AMsymbols[i].tex && !(typeof AMsymbols[i].notexcopy == "boolean" && AMsymbols[i].notexcopy)) {
       AMsymbols.push({input:AMsymbols[i].tex, 
        tag:AMsymbols[i].tag, output:AMsymbols[i].output, ttype:AMsymbols[i].ttype,
        acc:(AMsymbols[i].acc||false)});
    }
  }
  refreshSymbols();
}

function refreshSymbols(){
  var i;
  AMsymbols.sort(compareNames);
  for (i=0; i<AMsymbols.length; i++) AMnames[i] = AMsymbols[i].input;
}

function newcommand(oldstr,newstr) {
  AMsymbols.push({input:oldstr, tag:"mo", output:newstr, tex:null, ttype:DEFINITION});
  refreshSymbols();
}

function newsymbol(symbolobj) {
  AMsymbols.push(symbolobj);
  refreshSymbols();
}

function AMremoveCharsAndBlanks(str,n) {
//remove n characters and any following blanks
  var st;
  if (str.charAt(n)=="\\" && str.charAt(n+1)!="\\" && str.charAt(n+1)!=" ") 
    st = str.slice(n+1);
  else st = str.slice(n);
  for (var i=0; i<st.length && st.charCodeAt(i)<=32; i=i+1);
  return st.slice(i);
}

function AMposition(arr, str, n) { 
// return position >=n where str appears or would be inserted
// assumes arr is sorted
  if (n==0) {
    var h,m;
    n = -1;
    h = arr.length;
    while (n+1<h) {
      m = (n+h) >> 1;
      if (arr[m]<str) n = m; else h = m;
    }
    return h;
  } else
    for (var i=n; i<arr.length && arr[i]<str; i++);
  return i; // i=arr.length || arr[i]>=str
}

function AMgetSymbol(str) {
//return maximal initial substring of str that appears in names
//return null if there is none
  var k = 0; //new pos
  var j = 0; //old pos
  var mk; //match pos
  var st;
  var tagst;
  var match = "";
  var more = true;
  for (var i=1; i<=str.length && more; i++) {
    st = str.slice(0,i); //initial substring of length i
    j = k;
    k = AMposition(AMnames, st, j);
    if (k<AMnames.length && str.slice(0,AMnames[k].length)==AMnames[k]){
      match = AMnames[k];
      mk = k;
      i = match.length;
    }
    more = k<AMnames.length && str.slice(0,AMnames[k].length)>=AMnames[k];
  }
  AMpreviousSymbol=AMcurrentSymbol;
  if (match!=""){
    AMcurrentSymbol=AMsymbols[mk].ttype;
    return AMsymbols[mk]; 
  }
// if str[0] is a digit or - return maxsubstring of digits.digits
  AMcurrentSymbol=CONST;
  k = 1;
  st = str.slice(0,1);
  var integ = true;
 	  
  while ("0"<=st && st<="9" && k<=str.length) {
    st = str.slice(k,k+1);
    k++;
  }
  if (st == config.decimalsign) {
    st = str.slice(k,k+1);
    if ("0"<=st && st<="9") {
      integ = false;
      k++;
      while ("0"<=st && st<="9" && k<=str.length) {
        st = str.slice(k,k+1);
        k++;
      }
    }
  }
  if ((integ && k>1) || k>2) {
    st = str.slice(0,k-1);
    tagst = "mn";
  } else {
    k = 2;
    st = str.slice(0,1); //take 1 character
    tagst = (("A">st || st>"Z") && ("a">st || st>"z")?"mo":"mi");
  }
  if (st=="-" && AMpreviousSymbol==INFIX) {
    AMcurrentSymbol = INFIX;
    return {input:st, tag:tagst, output:st, ttype:UNARY, func:true, val:true};
  }
  return {input:st, tag:tagst, output:st, ttype:CONST, val:true}; //added val bit
}

function AMTremoveBrackets(node) {
	
  var st;
  if (node.charAt(0)=='{' && node.charAt(node.length-1)=='}') {
    var leftchop = 0;
    
    st = node.substr(1,5);
    if (st=='\\left') {
    	    st = node.charAt(6);
    	    if (st=="(" || st=="[" || st=="{") {
    	    	    leftchop = 7;
    	    } else {
    	    	    st = node.substr(6,7);
    	    	    if (st=='\\lbrace') {
    	    	    	    leftchop = 13;
    	    	    }
    	    }
    } else {
    	    st = node.charAt(1);
    	    if (st=="(" || st=="[") {
    	    	    leftchop = 2;
    	    }
    } 
    if (leftchop>0) {
    	    //st = node.charAt(node.length-7);
    	    st = node.substr(node.length-8);
    	    if (st=="\\right)}" || st=="\\right]}" || st=='\\right.}') {
    	    	    node = '{'+node.substr(leftchop);
    	    	    node = node.substr(0,node.length-8)+'}';
    	    } else if (st=='\\rbrace}') {
    	    	    node = '{'+node.substr(leftchop);
    	    	    node = node.substr(0,node.length-14)+'}';
    	    } 
    }
  }
  return node;
}

/*Parsing ASCII math expressions with the following grammar
v ::= [A-Za-z] | greek letters | numbers | other constant symbols
u ::= sqrt | text | bb | other unary symbols for font commands
b ::= frac | root | stackrel         binary symbols
l ::= ( | [ | { | (: | {:            left brackets
r ::= ) | ] | } | :) | :}            right brackets
S ::= v | lEr | uS | bSS             Simple expression
I ::= S_S | S^S | S_S^S | S          Intermediate expression
E ::= IE | I/I                       Expression
Each terminal symbol is translated into a corresponding mathml node.*/

var AMnestingDepth,AMpreviousSymbol,AMcurrentSymbol;

function AMTgetTeXsymbol(symb) {
	if (typeof symb.val == "boolean" && symb.val) {
		pre = '';
	} else {
		pre = '\\';
	}
	if (symb.tex==null) {
		//can't remember why this was here.  Breaks /delta /Delta to removed
		//return (pre+(pre==''?symb.input:symb.input.toLowerCase()));
		return (pre+symb.input);
	} else {
		return (pre+symb.tex);
	}
}

function AMTparseSexpr(str) { //parses str and returns [node,tailstr]
  var symbol, node, result, i, st,// rightvert = false,
    newFrag = '';
  str = AMremoveCharsAndBlanks(str,0);
  symbol = AMgetSymbol(str);             //either a token or a bracket or empty
  if (symbol == null || symbol.ttype == RIGHTBRACKET && AMnestingDepth > 0) {
    return [null,str];
  }
  if (symbol.ttype == DEFINITION) {
    str = symbol.output+AMremoveCharsAndBlanks(str,symbol.input.length); 
    symbol = AMgetSymbol(str);
  }
  switch (symbol.ttype) {
  case UNDEROVER:
  case CONST:
    str = AMremoveCharsAndBlanks(str,symbol.input.length); 
     var texsymbol = AMTgetTeXsymbol(symbol);
     if (texsymbol.charAt(0)=="\\" || symbol.tag=="mo") return [texsymbol,str];
     else return ['{'+texsymbol+'}',str];
    
  case LEFTBRACKET:   //read (expr+)
    AMnestingDepth++;
    str = AMremoveCharsAndBlanks(str,symbol.input.length); 
   
    result = AMTparseExpr(str,true);
    AMnestingDepth--;
    var leftchop = 0;
    if (result[0].substr(0,6)=="\\right") {
    	    st = result[0].charAt(6);
    	    if (st==")" || st=="]" || st=="}") {
    	    	    leftchop = 6;
    	    } else if (st==".") {
    	    	    leftchop = 7;
    	    } else {
    	    	    st = result[0].substr(6,7);
    	    	    if (st=='\\rbrace') {
    	    	    	    leftchop = 13;
    	    	    }
    	    }
    }
    if (leftchop>0) {
	    result[0] = result[0].substr(leftchop);
	    if (typeof symbol.invisible == "boolean" && symbol.invisible) 
		    node = '{'+result[0]+'}';
	    else {
		    node = '{'+AMTgetTeXsymbol(symbol) + result[0]+'}';
	    }    
    } else {
	    if (typeof symbol.invisible == "boolean" && symbol.invisible) 
		    node = '{\\left.'+result[0]+'}';
	    else {
		    node = '{\\left'+AMTgetTeXsymbol(symbol) + result[0]+'}';
	    }
    }
    return [node,result[1]];
  case TEXT:
      if (symbol!=AMquote) str = AMremoveCharsAndBlanks(str,symbol.input.length);
      if (str.charAt(0)=="{") i=str.indexOf("}");
      else if (str.charAt(0)=="(") i=str.indexOf(")");
      else if (str.charAt(0)=="[") i=str.indexOf("]");
      else if (symbol==AMquote) i=str.slice(1).indexOf("\"")+1;
      else i = 0;
      if (i==-1) i = str.length;
      st = str.slice(1,i);
      if (st.charAt(0) == " ") {
	      newFrag = '\\ ';
      }
     newFrag += '\\text{'+st+'}';
      if (st.charAt(st.length-1) == " ") {
	      newFrag += '\\ ';
      }
      str = AMremoveCharsAndBlanks(str,i+1);
      return [newFrag,str];
  case UNARY:
      str = AMremoveCharsAndBlanks(str,symbol.input.length); 
      result = AMTparseSexpr(str);
      if (result[0]==null) return ['{'+AMTgetTeXsymbol(symbol)+'}',str];
      if (typeof symbol.func == "boolean" && symbol.func) { // functions hack
        st = str.charAt(0);
        if (st=="^" || st=="_" || st=="/" || st=="|" || st=="," || (symbol.input.length==1 && symbol.input.match(/\w/) && st!="(")) {
          return ['{'+AMTgetTeXsymbol(symbol)+'}',str];
        } else {
		node = '{'+AMTgetTeXsymbol(symbol)+'{'+result[0]+'}}';
		return [node,result[1]];
        }
      }
      result[0] = AMTremoveBrackets(result[0]);
      if (symbol.input == "sqrt") {           // sqrt
	      return ['\\sqrt{'+result[0]+'}',result[1]];
      } else if (symbol.input == "cancel") {           // cancel
	      return ['\\cancel{'+result[0]+'}',result[1]];
      } else if (typeof symbol.rewriteleftright != "undefined") {  // abs, floor, ceil
	      return ['{\\left'+symbol.rewriteleftright[0]+result[0]+'\\right'+symbol.rewriteleftright[1]+'}',result[1]];
      } else if (typeof symbol.acc == "boolean" && symbol.acc) {   // accent
	      //return ['{'+AMTgetTeXsymbol(symbol)+'{'+result[0]+'}}',result[1]];
	      return [AMTgetTeXsymbol(symbol)+'{'+result[0]+'}',result[1]];
      } else {                        // font change command  
	    return ['{'+AMTgetTeXsymbol(symbol)+'{'+result[0]+'}}',result[1]];
      }
  case BINARY:
    str = AMremoveCharsAndBlanks(str,symbol.input.length); 
    result = AMTparseSexpr(str);
    if (result[0]==null) return ['{'+AMTgetTeXsymbol(symbol)+'}',str];
    result[0] = AMTremoveBrackets(result[0]);
    var result2 = AMTparseSexpr(result[1]);
    if (result2[0]==null) return ['{'+AMTgetTeXsymbol(symbol)+'}',str];
    result2[0] = AMTremoveBrackets(result2[0]);
    if (symbol.input=="color") {
    	newFrag = '{\\color{'+result[0].replace(/[\{\}]/g,'')+'}'+result2[0]+'}';    
    } else  if (symbol.input=="root") {
	    newFrag = '{\\sqrt['+result[0]+']{'+result2[0]+'}}';
    } else {
	    newFrag = '{'+AMTgetTeXsymbol(symbol)+'{'+result[0]+'}{'+result2[0]+'}}';
    }
    return [newFrag,result2[1]];
  case INFIX:
    str = AMremoveCharsAndBlanks(str,symbol.input.length); 
    return [symbol.output,str];
  case SPACE:
    str = AMremoveCharsAndBlanks(str,symbol.input.length);
    return ['{\\quad\\text{'+symbol.input+'}\\quad}',str];
  case LEFTRIGHT:
//    if (rightvert) return [null,str]; else rightvert = true;
    AMnestingDepth++;
    str = AMremoveCharsAndBlanks(str,symbol.input.length); 
    result = AMTparseExpr(str,false);
    AMnestingDepth--;
    var st = "";
    st = result[0].charAt(result[0].length -1);
//alert(result[0].lastChild+"***"+st);
    if (st == "|") { // its an absolute value subterm
	    node = '{\\left|'+result[0]+'}';
      return [node,result[1]];
    } else { // the "|" is a \mid
      node = '{\\mid}';
      return [node,str];
    }
   
  default:
//alert("default");
    str = AMremoveCharsAndBlanks(str,symbol.input.length); 
    return ['{'+AMTgetTeXsymbol(symbol)+'}',str];
  }
}

function AMTparseIexpr(str) {
  var symbol, sym1, sym2, node, result;
  str = AMremoveCharsAndBlanks(str,0);
  sym1 = AMgetSymbol(str);
  result = AMTparseSexpr(str);
  node = result[0];
  str = result[1];
  symbol = AMgetSymbol(str);
  if (symbol.ttype == INFIX && symbol.input != "/") {
    str = AMremoveCharsAndBlanks(str,symbol.input.length);
   // if (symbol.input == "/") result = AMTparseIexpr(str); else 
    result = AMTparseSexpr(str);
    if (result[0] == null) // show box in place of missing argument
	    result[0] = '{}';
    else result[0] = AMTremoveBrackets(result[0]);
    str = result[1];
//    if (symbol.input == "/") AMTremoveBrackets(node);
    if (symbol.input == "_") {
      sym2 = AMgetSymbol(str);
      if (sym2.input == "^") {
        str = AMremoveCharsAndBlanks(str,sym2.input.length);
        var res2 = AMTparseSexpr(str);
        res2[0] = AMTremoveBrackets(res2[0]);
        str = res2[1];
        node = '{' + node;
       	node += '_{'+result[0]+'}';
	node += '^{'+res2[0]+'}';
        node += '}';
      } else {
        node += '_{'+result[0]+'}';
      }
    } else { //must be ^
      //node = '{'+node+'}^{'+result[0]+'}';
      node = node+'^{'+result[0]+'}';
    }
    if (typeof sym1.func != 'undefined' && sym1.func) {
    	sym2 = AMgetSymbol(str);
    	if (sym2.ttype != INFIX && sym2.ttype != RIGHTBRACKET) {
    		result = AMTparseIexpr(str);
    		node = '{'+node+result[0]+'}';
    		str = result[1];
    	}
    }
  } 
  
  return [node,str];
}

function AMTparseExpr(str,rightbracket) {
  var symbol, node, result, i, nodeList = [],
  newFrag = '';
  var addedright = false;
  do {
    str = AMremoveCharsAndBlanks(str,0);
    result = AMTparseIexpr(str);
    node = result[0];
    str = result[1];
    symbol = AMgetSymbol(str);
    if (symbol.ttype == INFIX && symbol.input == "/") {
      str = AMremoveCharsAndBlanks(str,symbol.input.length);
      result = AMTparseIexpr(str);
      
      if (result[0] == null) // show box in place of missing argument
	      result[0] = '{}';
      else result[0] = AMTremoveBrackets(result[0]);
      str = result[1];
      node = AMTremoveBrackets(node);
      node = '\\frac' + '{'+ node + '}';
      node += '{'+result[0]+'}';
      newFrag += node;
      symbol = AMgetSymbol(str);
    }  else if (node!=undefined) newFrag += node;
  } while ((symbol.ttype != RIGHTBRACKET && 
           (symbol.ttype != LEFTRIGHT || rightbracket)
           || AMnestingDepth == 0) && symbol!=null && symbol.output!="");
  if (symbol.ttype == RIGHTBRACKET || symbol.ttype == LEFTRIGHT) {
//    if (AMnestingDepth > 0) AMnestingDepth--;
	var len = newFrag.length;
	if (len>2 && newFrag.charAt(0)=='{' && newFrag.indexOf(',')>0) { //could be matrix (total rewrite from .js)
		var right = newFrag.charAt(len - 2);
		if (right==')' || right==']') {
			var left = newFrag.charAt(6);
			if ((left=='(' && right==')' && symbol.output != '}') || (left=='[' && right==']')) {
				// Modified by Arnaud Roques
				//var mxout = '\\matrix{';
				var mxout = '\\begin{matrix}';
				var pos = new Array(); //position of commas
				pos.push(0);
				var matrix = true;
				var mxnestingd = 0;
				var subpos = [];
				subpos[0] = [0];
				var lastsubposstart = 0;
				var mxanynestingd = 0;
				for (i=1; i<len-1; i++) {
					if (newFrag.charAt(i)==left) mxnestingd++;
					if (newFrag.charAt(i)==right) {
						mxnestingd--;
						if (mxnestingd==0 && newFrag.charAt(i+2)==',' && newFrag.charAt(i+3)=='{') {
							pos.push(i+2);
							lastsubposstart = i+2;
							subpos[lastsubposstart] = [i+2];
						}
					}
					if (newFrag.charAt(i)=='[' || newFrag.charAt(i)=='(' || newFrag.charAt(i)=='{') { mxanynestingd++;}
					if (newFrag.charAt(i)==']' || newFrag.charAt(i)==')' || newFrag.charAt(i)=='}') { mxanynestingd--;}
					if (newFrag.charAt(i)==',' && mxanynestingd==1) {
						subpos[lastsubposstart].push(i);
					}
					if (mxanynestingd<0) {  //happens at the end of the row
						if (lastsubposstart == i+1) { //if at end of row, skip to next row
							i++;
						} else { //misformed something - abandon treating as a matrix
							matrix = false;
						}
					}
				}

				pos.push(len);
				var lastmxsubcnt = -1;
				if (mxnestingd==0 && pos.length>0 && matrix) {
					for (i=0;i<pos.length-1;i++) {
						if (i>0) mxout += '\\\\';
						if (i==0) {
							//var subarr = newFrag.substr(pos[i]+7,pos[i+1]-pos[i]-15).split(',');
							if (subpos[pos[i]].length==1) {
								var subarr = [newFrag.substr(pos[i]+7,pos[i+1]-pos[i]-15)];
							} else {
								var subarr = [newFrag.substring(pos[i]+7,subpos[pos[i]][1])];
								for (var j=2;j<subpos[pos[i]].length;j++) {
									subarr.push(newFrag.substring(subpos[pos[i]][j-1]+1,subpos[pos[i]][j]));
								}
								subarr.push(newFrag.substring(subpos[pos[i]][subpos[pos[i]].length-1]+1,pos[i+1]-8));
							}
						} else {
							//var subarr = newFrag.substr(pos[i]+8,pos[i+1]-pos[i]-16).split(',');
							if (subpos[pos[i]].length==1) {
								var subarr = [newFrag.substr(pos[i]+8,pos[i+1]-pos[i]-16)];
							} else {
								var subarr = [newFrag.substring(pos[i]+8,subpos[pos[i]][1])];
								for (var j=2;j<subpos[pos[i]].length;j++) {
									subarr.push(newFrag.substring(subpos[pos[i]][j-1]+1,subpos[pos[i]][j]));
								}
								subarr.push(newFrag.substring(subpos[pos[i]][subpos[pos[i]].length-1]+1,pos[i+1]-8));
							}
						}
						if (lastmxsubcnt>0 && subarr.length!=lastmxsubcnt) {
							matrix = false;
						} else if (lastmxsubcnt==-1) {
							lastmxsubcnt=subarr.length;
						}
						mxout += subarr.join('&');
					}
				}
				// Modified by Arnaud Roques
				// mxout += '}';
				mxout += '\\end{matrix}';

				if (matrix) { newFrag = mxout;}
			}
		}
	}
    
    str = AMremoveCharsAndBlanks(str,symbol.input.length);
    if (typeof symbol.invisible != "boolean" || !symbol.invisible) {
      node = '\\right'+AMTgetTeXsymbol(symbol); //AMcreateMmlNode("mo",document.createTextNode(symbol.output));
      newFrag += node;
      addedright = true;
    } else {
	    newFrag += '\\right.';
	    addedright = true;
    }
   
  }
  if(AMnestingDepth>0 && !addedright) {
	  newFrag += '\\right.'; //adjust for non-matching left brackets
	  //todo: adjust for non-matching right brackets
  }
  
  return [newFrag,str];
}

function AMTparseAMtoTeX(str) {
 AMnestingDepth = 0;
  str = str.replace(/(&nbsp;|\u00a0|&#160;)/g,"");
  str = str.replace(/&gt;/g,">");
  str = str.replace(/&lt;/g,"<");
  if (str.match(/\S/)==null) {
	  return "";
  }
  return AMTparseExpr(str.replace(/^\s+/g,""),false)[0];
}

AMinitSymbols();

function plantuml(asciiMathInput) {
	return AMTparseAMtoTeX(asciiMathInput);
}
