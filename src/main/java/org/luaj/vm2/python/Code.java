package org.luaj.vm2.python;

/** A compiled function body, or a whole module. */
public final class Code {
	public final String name;
	public final String filename;
	final int[] code;
	/** The source line of each instruction. */
	final int[] lines;
	final Object[] consts;
	final String[] names;
	/** Number of slots of a frame: variables, then free variables, then temporaries. */
	final int nslots;
	final int maxStack;
	/** Positional parameters (they come first among the slots). */
	final int argcount;
	/** Keyword-only parameters (the slots after the positional ones). */
	final int kwonlycount;
	/** The slot of {@code *args}, or -1. */
	final int varargsSlot;
	/** The slot of {@code **kwargs}, or -1. */
	final int kwargsSlot;
	/** Names of the positional and keyword-only parameters. */
	final String[] paramNames;
	/** First slot that receives a cell of the closure, and their number. */
	final int firstFree;
	final int nfree;
	final boolean isModule;
	/** Exception table: instructions in [start, end) are protected by the handler. */
	final int[] excStart;
	final int[] excEnd;
	final int[] excHandler;
	final int[] excDepth;
	/** Names of the variable slots, for messages and the disassembler. */
	final String[] slotNames;

	Code(String name, String filename, int[] code, int[] lines, Object[] consts, String[] names, int nslots,
			int maxStack, int argcount, int kwonlycount, int varargsSlot, int kwargsSlot, String[] paramNames,
			int firstFree, int nfree, boolean isModule, int[] excStart, int[] excEnd, int[] excHandler,
			int[] excDepth, String[] slotNames) {
		this.name = name;
		this.filename = filename;
		this.code = code;
		this.lines = lines;
		this.consts = consts;
		this.names = names;
		this.nslots = nslots;
		this.maxStack = maxStack;
		this.argcount = argcount;
		this.kwonlycount = kwonlycount;
		this.varargsSlot = varargsSlot;
		this.kwargsSlot = kwargsSlot;
		this.paramNames = paramNames;
		this.firstFree = firstFree;
		this.nfree = nfree;
		this.isModule = isModule;
		this.excStart = excStart;
		this.excEnd = excEnd;
		this.excHandler = excHandler;
		this.excDepth = excDepth;
		this.slotNames = slotNames;
	}

	/** Human readable listing of the code, for debugging and tests. */
	public String disassemble() {
		StringBuilder sb = new StringBuilder();
		disassemble(sb, "");
		return sb.toString();
	}

	private void disassemble(StringBuilder sb, String indent) {
		sb.append(indent).append("code ").append(name).append(" (slots=").append(nslots).append(", stack=")
				.append(maxStack).append(", args=").append(argcount).append(")\n");
		for (int i = 0; i < code.length; i++) {
			int op = code[i] & 0xff;
			int arg = code[i] >>> 8;
			sb.append(indent).append(String.format("%4d  line %-4d %-20s", i, lines[i], Op.NAMES[op]));
			sb.append(arg);
			switch (op) {
			case Op.LOAD_CONST:
				sb.append("  (").append(describe(consts[arg])).append(')');
				break;
			case Op.LOAD_GLOBAL:
			case Op.STORE_GLOBAL:
			case Op.DELETE_GLOBAL:
			case Op.LOAD_ATTR:
			case Op.IMPORT:
				sb.append("  (").append(names[arg]).append(')');
				break;
			case Op.LOAD_FAST:
			case Op.STORE_FAST:
			case Op.DELETE_FAST:
			case Op.LOAD_CELL:
			case Op.STORE_CELL:
			case Op.DELETE_CELL:
			case Op.LOAD_CLOSURE:
			case Op.MAKE_CELL:
				sb.append("  (").append(slotNames[arg] == null ? "?" : slotNames[arg]).append(')');
				break;
			default:
				break;
			}
			sb.append('\n');
		}
		for (int i = 0; i < excStart.length; i++)
			sb.append(indent).append("  except [").append(excStart[i]).append(", ").append(excEnd[i]).append(") -> ")
					.append(excHandler[i]).append(" depth ").append(excDepth[i]).append('\n');
		for (Object c : consts)
			if (c instanceof Code)
				((Code) c).disassemble(sb, indent + "    ");
	}

	private static String describe(Object c) {
		if (c instanceof Code)
			return "<code " + ((Code) c).name + ">";
		if (c instanceof String)
			return "'" + c + "'";
		return String.valueOf(c);
	}
}
