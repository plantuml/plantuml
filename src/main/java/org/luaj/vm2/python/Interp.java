package org.luaj.vm2.python;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * The interpreter: runs compiled code on a stack machine. One instance holds
 * the global variables, the output sink and the limits of one program.
 */
public final class Interp {
	/** Receives what the program prints. */
	public interface Output {
		void write(String text);
	}

	final Map<String, Object> globals = new HashMap<String, Object>();
	final Map<String, Object> builtins = new HashMap<String, Object>();
	final Map<String, PyModule> modules = new HashMap<String, PyModule>();
	private final Output out;
	private long steps;
	private long maxSteps = 200000000L;
	private int depth;
	private int maxDepth = 1000;
	/** pcs[d] is the next instruction of the frame at call depth d, kept for the exception handlers. */
	private int[] pcs = new int[1002];

	public Interp(Output out) {
		this.out = out;
		Builtins.install(this);
		globals.put("__name__", "__main__");
	}

	public void setMaxSteps(long n) {
		maxSteps = n;
	}

	public void setMaxDepth(int n) {
		maxDepth = n;
		pcs = new int[n + 2];
	}

	/** Number of steps (backward jumps and calls) executed so far. */
	public long getSteps() {
		return steps;
	}

	void write(String s) {
		out.write(s);
	}

	void tick() {
		if (++steps > maxSteps)
			throw new ExecutionLimitError("instruction limit exceeded");
	}

	/** Compiles a program. */
	public static Code compile(String source, String filename) {
		return CodeGen.compile(PyParser.parse(source), filename);
	}

	/** Compiles and runs a program in this interpreter's global scope. */
	public void run(String source, String filename) {
		run(compile(source, filename));
	}

	public void run(Code code) {
		Object[] frame = new Object[code.nslots + code.maxStack];
		try {
			execute(code, frame, globals);
		} catch (PyError e) {
			throw e;
		}
	}

	/** The value of a global variable converted for display, or null if it is not defined. */
	public String getGlobalAsString(String name) {
		Object v = globals.get(name);
		return v == null ? null : PyOps.str(v);
	}

	// ------------------------------------------------------------ calls

	Object call(Object f, Object... args) {
		return callGeneric(f, args, 0, args.length, null, null);
	}

	Object callGeneric(Object fn, Object[] src, int off, int n, String[] kwn, Object[] kwv) {
		if (fn instanceof PyFunction)
			return invoke((PyFunction) fn, src, off, n, kwn, kwv);
		Builtin b;
		Object[] args;
		if (fn instanceof Builtin) {
			b = (Builtin) fn;
			args = new Object[n];
			System.arraycopy(src, off, args, 0, n);
		} else if (fn instanceof BoundMethod) {
			BoundMethod m = (BoundMethod) fn;
			b = m.method;
			args = new Object[n + 1];
			args[0] = m.self;
			System.arraycopy(src, off, args, 1, n);
		} else if (fn instanceof PyType) {
			b = ((PyType) fn).ctor;
			if (b == null)
				throw PyOps.err(Types.TYPE_ERROR, "cannot create '" + ((PyType) fn).name + "' instances");
			args = new Object[n];
			System.arraycopy(src, off, args, 0, n);
		} else {
			throw PyOps.err(Types.TYPE_ERROR, "'" + PyOps.typeName(fn) + "' object is not callable");
		}
		tick();
		Object r;
		if (kwn == null || kwn.length == 0) {
			r = b.call(this, args);
		} else {
			Map<String, Object> kw = new LinkedHashMap<String, Object>();
			for (int i = 0; i < kwn.length; i++)
				kw.put(kwn[i], kwv[i]);
			r = b.callKw(this, args, kw);
		}
		return r == null ? PyNone.NONE : r;
	}

	private Object invoke(PyFunction fn, Object[] src, int off, int n, String[] kwn, Object[] kwv) {
		Code c = fn.code;
		Object[] f = new Object[c.nslots + c.maxStack];
		if (kwn == null && n == c.argcount && c.varargsSlot < 0 && c.kwargsSlot < 0 && c.kwonlycount == 0)
			System.arraycopy(src, off, f, 0, n);
		else
			bind(fn, f, src, off, n, kwn, kwv);
		if (c.nfree > 0)
			System.arraycopy(fn.closure, 0, f, c.firstFree, c.nfree);
		tick();
		return execute(c, f, fn.globals);
	}

	private void bind(PyFunction fn, Object[] f, Object[] src, int off, int n, String[] kwn, Object[] kwv) {
		Code c = fn.code;
		int argcount = c.argcount;
		int npos = Math.min(n, argcount);
		System.arraycopy(src, off, f, 0, npos);
		if (n > argcount) {
			if (c.varargsSlot < 0)
				throw PyOps.err(Types.TYPE_ERROR, c.name + "() takes " + argcount + " positional argument"
						+ (argcount == 1 ? "" : "s") + " but " + n + " " + (n == 1 ? "was" : "were") + " given");
			Object[] extra = new Object[n - argcount];
			System.arraycopy(src, off + argcount, extra, 0, extra.length);
			f[c.varargsSlot] = new PyTuple(extra);
		} else if (c.varargsSlot >= 0) {
			f[c.varargsSlot] = PyTuple.EMPTY;
		}
		PyDict extraKw = null;
		if (c.kwargsSlot >= 0) {
			extraKw = new PyDict();
			f[c.kwargsSlot] = extraKw;
		}
		if (kwn != null) {
			int nparams = c.paramNames.length;
			for (int k = 0; k < kwn.length; k++) {
				int idx = -1;
				for (int p = 0; p < nparams; p++)
					if (c.paramNames[p].equals(kwn[k])) {
						idx = p;
						break;
					}
				if (idx >= 0) {
					int slot = idx;
					if (f[slot] != null)
						throw PyOps.err(Types.TYPE_ERROR,
								c.name + "() got multiple values for argument '" + kwn[k] + "'");
					f[slot] = kwv[k];
				} else if (extraKw != null) {
					if (extraKw.containsKey(kwn[k]))
						throw PyOps.err(Types.TYPE_ERROR,
								c.name + "() got multiple values for keyword argument '" + kwn[k] + "'");
					extraKw.put(kwn[k], kwv[k]);
				} else {
					throw PyOps.err(Types.TYPE_ERROR,
							c.name + "() got an unexpected keyword argument '" + kwn[k] + "'");
				}
			}
		}
		int ndef = fn.defaults == null ? 0 : fn.defaults.length;
		ArrayList<String> missing = null;
		for (int i = 0; i < argcount; i++) {
			if (f[i] == null) {
				int di = i - (argcount - ndef);
				if (di >= 0) {
					f[i] = fn.defaults[di];
				} else {
					if (missing == null)
						missing = new ArrayList<String>();
					missing.add(c.paramNames[i]);
				}
			}
		}
		if (missing != null)
			throw PyOps.err(Types.TYPE_ERROR, c.name + "() missing " + missing.size() + " required positional argument"
					+ (missing.size() == 1 ? "" : "s") + ": " + quoteList(missing));
		for (int i = argcount; i < argcount + c.kwonlycount; i++) {
			if (f[i] == null) {
				Object d = fn.kwdefaults == null ? null : fn.kwdefaults.get(c.paramNames[i]);
				if (d == null)
					throw PyOps.err(Types.TYPE_ERROR, c.name + "() missing required keyword-only argument: '"
							+ c.paramNames[i] + "'");
				f[i] = d;
			}
		}
	}

	private static String quoteList(ArrayList<String> names) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < names.size(); i++) {
			if (i > 0)
				sb.append(i == names.size() - 1 ? " and " : ", ");
			sb.append('\'').append(names.get(i)).append('\'');
		}
		return sb.toString();
	}

	// ------------------------------------------------------------ the loop

	/**
	 * Runs a frame. The interpreter loop ({@link #run}) is kept out of any
	 * try block (entering one is slow on TeaVM C): this method has the only
	 * one, once per call, and re-enters the loop at the handler after an
	 * exception.
	 */
	private Object execute(Code code, Object[] f, Map<String, Object> globals) {
		if (depth >= maxDepth)
			throw PyOps.err(Types.RECURSION_ERROR, "maximum recursion depth exceeded");
		final int d = ++depth;
		int pc = 0;
		int sp = code.nslots;
		try {
			for (;;) {
				PyError e;
				try {
					return run(code, f, globals, pc, sp, d);
				} catch (PyError pe) {
					e = pe;
				} catch (StackOverflowError so) {
					e = PyOps.err(Types.RECURSION_ERROR, "maximum recursion depth exceeded");
				}
				int fault = pcs[d] - 1;
				int h = findHandler(code, fault);
				if (h < 0) {
					e.addFrame(code.filename, code.lines[fault], code.name);
					throw e;
				}
				sp = code.nslots + code.excDepth[h];
				f[sp++] = e.exc;
				pc = code.excHandler[h];
			}
		} finally {
			depth = d - 1;
		}
	}

	private static int findHandler(Code code, int pc) {
		for (int i = 0; i < code.excStart.length; i++)
			if (pc >= code.excStart[i] && pc < code.excEnd[i])
				return i;
		return -1;
	}

	private static PyError unbound(Code code, int slot) {
		String name = code.slotNames[slot];
		if (slot >= code.firstFree && slot < code.firstFree + code.nfree)
			return PyOps.err(Types.NAME_ERROR, "cannot access free variable '" + name
					+ "' where it is not associated with a value in enclosing scope");
		return PyOps.err(Types.UNBOUND_LOCAL,
				"cannot access local variable '" + name + "' where it is not associated with a value");
	}

	private static boolean excMatches(PyException e, Object t) {
		if (t instanceof PyType && ((PyType) t).exception)
			return e.type.isSubtypeOf((PyType) t);
		if (t instanceof PyTuple) {
			for (Object o : ((PyTuple) t).items)
				if (excMatches(e, o))
					return true;
			return false;
		}
		throw PyOps.err(Types.TYPE_ERROR, "catching classes that do not inherit from BaseException is not allowed");
	}

	private PyError toRaise(Object v) {
		if (v instanceof PyException)
			return new PyError((PyException) v);
		if (v instanceof PyType && ((PyType) v).exception)
			return new PyError(new PyException((PyType) v, new Object[0]));
		return PyOps.err(Types.TYPE_ERROR, "exceptions must derive from BaseException");
	}

	private Object run(Code code, Object[] f, Map<String, Object> globals, int pc, int sp, int level) {
		final int[] pcs = this.pcs;
		final int[] c = code.code;
		final Object[] consts = code.consts;
		final String[] names = code.names;
		for (;;) {
			{
				for (;;) {
					int ins = c[pc++];
					pcs[level] = pc;
					int arg = ins >>> 8;
					switch (ins & 0xff) {
					case Op.NOP:
						break;
					case Op.POP:
						f[--sp] = null;
						break;
					case Op.DUP:
						f[sp] = f[sp - 1];
						sp++;
						break;
					case Op.DUP2:
						f[sp] = f[sp - 2];
						f[sp + 1] = f[sp - 1];
						sp += 2;
						break;
					case Op.ROT2: {
						Object t = f[sp - 1];
						f[sp - 1] = f[sp - 2];
						f[sp - 2] = t;
						break;
					}
					case Op.ROT3: {
						Object t = f[sp - 1];
						f[sp - 1] = f[sp - 2];
						f[sp - 2] = f[sp - 3];
						f[sp - 3] = t;
						break;
					}
					case Op.LOAD_CONST:
						f[sp++] = consts[arg];
						break;
					case Op.LOAD_FAST: {
						Object v = f[arg];
						if (v == null)
							throw unbound(code, arg);
						f[sp++] = v;
						break;
					}
					case Op.STORE_FAST:
						f[arg] = f[--sp];
						f[sp] = null;
						break;
					case Op.DELETE_FAST:
						if (f[arg] == null)
							throw unbound(code, arg);
						f[arg] = null;
						break;
					case Op.LOAD_CELL: {
						Object v = ((Cell) f[arg]).value;
						if (v == null)
							throw unbound(code, arg);
						f[sp++] = v;
						break;
					}
					case Op.STORE_CELL:
						((Cell) f[arg]).value = f[--sp];
						f[sp] = null;
						break;
					case Op.DELETE_CELL: {
						Cell cell = (Cell) f[arg];
						if (cell.value == null)
							throw unbound(code, arg);
						cell.value = null;
						break;
					}
					case Op.LOAD_CLOSURE:
						f[sp++] = f[arg];
						break;
					case Op.MAKE_CELL: {
						Cell cell = new Cell();
						cell.value = f[arg];
						f[arg] = cell;
						break;
					}
					case Op.LOAD_GLOBAL: {
						String name = names[arg];
						Object v = globals.get(name);
						if (v == null) {
							v = builtins.get(name);
							if (v == null)
								throw PyOps.err(Types.NAME_ERROR, "name '" + name + "' is not defined");
						}
						f[sp++] = v;
						break;
					}
					case Op.STORE_GLOBAL:
						globals.put(names[arg], f[--sp]);
						f[sp] = null;
						break;
					case Op.DELETE_GLOBAL:
						if (globals.remove(names[arg]) == null)
							throw PyOps.err(Types.NAME_ERROR, "name '" + names[arg] + "' is not defined");
						break;
					case Op.LOAD_ATTR:
						f[sp - 1] = PyOps.getattr(f[sp - 1], names[arg]);
						break;
					case Op.BINARY_OP: {
						Object b = f[--sp];
						f[sp] = null;
						if ((arg & Op.INPLACE) != 0)
							f[sp - 1] = PyOps.inplace(arg & 0xff, f[sp - 1], b);
						else
							f[sp - 1] = PyOps.binary(arg, f[sp - 1], b);
						break;
					}
					case Op.UNARY_NEG:
						f[sp - 1] = PyOps.negate(f[sp - 1]);
						break;
					case Op.UNARY_POS:
						f[sp - 1] = PyOps.plus(f[sp - 1]);
						break;
					case Op.UNARY_INVERT:
						f[sp - 1] = PyOps.invert(f[sp - 1]);
						break;
					case Op.UNARY_NOT:
						f[sp - 1] = PyOps.isTrue(f[sp - 1]) ? Boolean.FALSE : Boolean.TRUE;
						break;
					case Op.COMPARE_OP: {
						Object b = f[--sp];
						f[sp] = null;
						f[sp - 1] = PyOps.compare(arg, f[sp - 1], b);
						break;
					}
					case Op.IS_OP: {
						Object b = f[--sp];
						f[sp] = null;
						boolean same = PyOps.sameObject(f[sp - 1], b);
						f[sp - 1] = Boolean.valueOf(arg == 0 ? same : !same);
						break;
					}
					case Op.CONTAINS_OP: {
						Object container = f[--sp];
						f[sp] = null;
						boolean in = PyOps.contains(container, f[sp - 1]);
						f[sp - 1] = Boolean.valueOf(arg == 0 ? in : !in);
						break;
					}
					case Op.GETITEM: {
						Object idx = f[--sp];
						f[sp] = null;
						f[sp - 1] = PyOps.getitem(f[sp - 1], idx);
						break;
					}
					case Op.SETITEM: {
						Object idx = f[--sp];
						Object obj = f[--sp];
						Object v = f[--sp];
						f[sp] = f[sp + 1] = f[sp + 2] = null;
						PyOps.setitem(obj, idx, v);
						break;
					}
					case Op.DELITEM: {
						Object idx = f[--sp];
						Object obj = f[--sp];
						f[sp] = f[sp + 1] = null;
						PyOps.delitem(obj, idx);
						break;
					}
					case Op.BUILD_SLICE: {
						Object step = f[--sp];
						Object upper = f[--sp];
						f[sp - 1] = new PySlice(f[sp - 1], upper, step);
						f[sp] = f[sp + 1] = null;
						break;
					}
					case Op.BUILD_LIST: {
						ArrayList<Object> l = new ArrayList<Object>(arg);
						for (int i = sp - arg; i < sp; i++) {
							l.add(f[i]);
							f[i] = null;
						}
						sp -= arg;
						f[sp++] = new PyList(l);
						break;
					}
					case Op.BUILD_TUPLE: {
						Object[] t = new Object[arg];
						System.arraycopy(f, sp - arg, t, 0, arg);
						for (int i = sp - arg; i < sp; i++)
							f[i] = null;
						sp -= arg;
						f[sp++] = new PyTuple(t);
						break;
					}
					case Op.BUILD_DICT: {
						PyDict d = new PyDict();
						int base = sp - 2 * arg;
						for (int i = base; i < sp; i += 2) {
							d.put(f[i], f[i + 1]);
							f[i] = f[i + 1] = null;
						}
						sp = base;
						f[sp++] = d;
						break;
					}
					case Op.BUILD_STRING: {
						StringBuilder sb = new StringBuilder();
						for (int i = sp - arg; i < sp; i++) {
							sb.append((String) f[i]);
							f[i] = null;
							if (sb.length() > PyOps.MAX_STRING)
								throw PyOps.err(Types.MEMORY_ERROR, "string too large");
						}
						sp -= arg;
						f[sp++] = sb.toString();
						break;
					}
					case Op.LIST_APPEND: {
						Object v = f[--sp];
						f[sp] = null;
						((PyList) f[sp - arg]).items.add(v);
						break;
					}
					case Op.LIST_EXTEND: {
						Object v = f[--sp];
						f[sp] = null;
						try {
							((PyList) f[sp - arg]).items.addAll(PyOps.toArrayList(v));
						} catch (PyError e) {
							if (e.exc.type == Types.TYPE_ERROR && !(v instanceof PyList) && !(v instanceof PyTuple)
									&& !(v instanceof String) && !(v instanceof PyDict) && !(v instanceof PyRange))
								throw PyOps.err(Types.TYPE_ERROR,
										"argument after * must be an iterable, not " + PyOps.typeName(v));
							throw e;
						}
						break;
					}
					case Op.MAP_ADD: {
						Object v = f[--sp];
						Object k = f[--sp];
						f[sp] = f[sp + 1] = null;
						((PyDict) f[sp - arg]).put(k, v);
						break;
					}
					case Op.DICT_ADD_KW: {
						Object v = f[--sp];
						Object k = f[--sp];
						f[sp] = f[sp + 1] = null;
						PyDict d = (PyDict) f[sp - arg];
						if (d.containsKey(k))
							throw PyOps.err(Types.TYPE_ERROR, "got multiple values for keyword argument '" + k + "'");
						d.put(k, v);
						break;
					}
					case Op.DICT_MERGE_KW: {
						Object m = f[--sp];
						f[sp] = null;
						if (!(m instanceof PyDict))
							throw PyOps.err(Types.TYPE_ERROR,
									"argument after ** must be a mapping, not " + PyOps.typeName(m));
						PyDict d = (PyDict) f[sp - arg];
						for (PyDict.Entry e : ((PyDict) m).map.values()) {
							if (!(e.key instanceof String))
								throw PyOps.err(Types.TYPE_ERROR, "keywords must be strings");
							if (d.containsKey(e.key))
								throw PyOps.err(Types.TYPE_ERROR,
										"got multiple values for keyword argument '" + e.key + "'");
							d.put(e.key, e.value);
						}
						break;
					}
					case Op.UNPACK: {
						Object seq = f[--sp];
						Object[] items;
						if (seq instanceof PyTuple)
							items = ((PyTuple) seq).items;
						else if (seq instanceof PyList)
							items = ((PyList) seq).items.toArray();
						else
							items = PyOps.toArrayList(seq).toArray();
						if (items.length < arg)
							throw PyOps.err(Types.VALUE_ERROR, "not enough values to unpack (expected " + arg
									+ ", got " + items.length + ")");
						if (items.length > arg)
							throw PyOps.err(Types.VALUE_ERROR, "too many values to unpack (expected " + arg + ")");
						for (int i = arg - 1; i >= 0; i--)
							f[sp++] = items[i];
						break;
					}
					case Op.JUMP:
						if (arg < pc && ++steps > maxSteps)
							throw new ExecutionLimitError("instruction limit exceeded");
						pc = arg;
						break;
					case Op.JUMP_IF_FALSE: {
						Object v = f[--sp];
						f[sp] = null;
						if (!(v == Boolean.TRUE || (v != Boolean.FALSE && PyOps.isTrue(v)))) {
							if (arg < pc && ++steps > maxSteps)
								throw new ExecutionLimitError("instruction limit exceeded");
							pc = arg;
						}
						break;
					}
					case Op.JUMP_IF_TRUE: {
						Object v = f[--sp];
						f[sp] = null;
						if (v == Boolean.TRUE || (v != Boolean.FALSE && PyOps.isTrue(v))) {
							if (arg < pc && ++steps > maxSteps)
								throw new ExecutionLimitError("instruction limit exceeded");
							pc = arg;
						}
						break;
					}
					case Op.JUMP_IF_FALSE_KEEP:
						if (!PyOps.isTrue(f[sp - 1]))
							pc = arg;
						else
							f[--sp] = null;
						break;
					case Op.JUMP_IF_TRUE_KEEP:
						if (PyOps.isTrue(f[sp - 1]))
							pc = arg;
						else
							f[--sp] = null;
						break;
					case Op.GET_ITER:
						f[sp - 1] = PyOps.getIter(f[sp - 1]);
						break;
					case Op.FOR_ITER: {
						Object v = ((PyIter) f[sp - 1]).next();
						if (v == null) {
							f[--sp] = null;
							pc = arg;
						} else {
							f[sp++] = v;
						}
						break;
					}
					case Op.CALL: {
						int base = sp - arg;
						Object fn = f[base - 1];
						Object r;
						if (fn instanceof PyFunction) {
							r = invoke((PyFunction) fn, f, base, arg, null, null);
						} else {
							r = callGeneric(fn, f, base, arg, null, null);
						}
						for (int i = base - 1; i < sp; i++)
							f[i] = null;
						sp = base;
						f[sp - 1] = r;
						break;
					}
					case Op.CALL_KW: {
						PyTuple names2 = (PyTuple) f[--sp];
						f[sp] = null;
						int k = names2.items.length;
						int npos = arg - k;
						int base = sp - arg;
						String[] kwn = new String[k];
						Object[] kwv = new Object[k];
						for (int i = 0; i < k; i++) {
							kwn[i] = (String) names2.items[i];
							kwv[i] = f[base + npos + i];
						}
						Object r = callGeneric(f[base - 1], f, base, npos, kwn, kwv);
						for (int i = base - 1; i < sp; i++)
							f[i] = null;
						sp = base;
						f[sp - 1] = r;
						break;
					}
					case Op.CALL_EX: {
						PyDict kwd = null;
						if ((arg & 1) != 0) {
							kwd = (PyDict) f[--sp];
							f[sp] = null;
						}
						PyList pos = (PyList) f[--sp];
						f[sp] = null;
						Object fn = f[sp - 1];
						Object[] args = pos.items.toArray();
						String[] kwn = null;
						Object[] kwv = null;
						if (kwd != null && kwd.size() > 0) {
							kwn = new String[kwd.size()];
							kwv = new Object[kwd.size()];
							int i = 0;
							for (PyDict.Entry e : kwd.map.values()) {
								kwn[i] = (String) e.key;
								kwv[i++] = e.value;
							}
						}
						f[sp - 1] = callGeneric(fn, args, 0, args.length, kwn, kwv);
						break;
					}
					case Op.MAKE_FUNCTION: {
						Code fcode = (Code) f[--sp];
						f[sp] = null;
						Cell[] closure = null;
						Map<String, Object> kwdef = null;
						Object[] defaults = null;
						if ((arg & 4) != 0) {
							Object[] items = ((PyTuple) f[--sp]).items;
							f[sp] = null;
							closure = new Cell[items.length];
							for (int i = 0; i < items.length; i++)
								closure[i] = (Cell) items[i];
						}
						if ((arg & 2) != 0) {
							PyDict d = (PyDict) f[--sp];
							f[sp] = null;
							kwdef = new HashMap<String, Object>();
							for (PyDict.Entry e : d.map.values())
								kwdef.put((String) e.key, e.value);
						}
						if ((arg & 1) != 0) {
							defaults = ((PyTuple) f[--sp]).items;
							f[sp] = null;
						}
						f[sp++] = new PyFunction(fcode, globals, defaults, kwdef, closure);
						break;
					}
					case Op.RETURN:
						return f[--sp];
					case Op.RAISE:
						if (arg == 0)
							throw PyOps.err(Types.RUNTIME_ERROR, "No active exception to reraise");
						throw toRaise(f[--sp]);
					case Op.RERAISE:
						throw new PyError((PyException) f[--sp]);
					case Op.EXC_MATCH: {
						Object t = f[--sp];
						f[sp] = null;
						f[sp - 1] = Boolean.valueOf(excMatches((PyException) f[sp - 1], t));
						break;
					}
					case Op.IMPORT: {
						PyModule m = modules.get(names[arg]);
						if (m == null)
							throw PyOps.err(Types.MODULE_NOT_FOUND, "No module named '" + names[arg] + "'");
						f[sp++] = m;
						break;
					}
					case Op.FORMAT_VALUE: {
						String spec = "";
						if ((arg & 4) != 0) {
							spec = (String) f[--sp];
							f[sp] = null;
						}
						Object v = f[sp - 1];
						switch (arg & 3) {
						case 1:
							v = PyOps.str(v);
							break;
						case 2:
							v = PyOps.repr(v);
							break;
						case 3:
							v = Fmt.asciiStr(PyOps.repr(v));
							break;
						default:
							break;
						}
						f[sp - 1] = spec.isEmpty() ? PyOps.str(v) : Fmt.format(v, spec);
						break;
					}
					default:
						throw new IllegalStateException("bad opcode " + (ins & 0xff));
					}
				}
			}
		}
	}
}
