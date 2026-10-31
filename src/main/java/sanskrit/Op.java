package sanskrit;

/**
 * Instruction set of the stack machine. An instruction is one {@code int}:
 * the opcode in the low 8 bits, an unsigned 24-bit argument above it.
 */
final class Op {
	private Op() {
	}

	static final int NOP = 0;
	static final int POP = 1;
	static final int DUP = 2;
	static final int DUP2 = 3;
	static final int ROT2 = 4;
	static final int ROT3 = 5;

	static final int LOAD_CONST = 6;
	static final int LOAD_FAST = 7;
	static final int STORE_FAST = 8;
	static final int DELETE_FAST = 9;
	static final int LOAD_CELL = 10;
	static final int STORE_CELL = 11;
	static final int DELETE_CELL = 12;
	static final int LOAD_CLOSURE = 13;
	static final int MAKE_CELL = 14;
	static final int LOAD_GLOBAL = 15;
	static final int STORE_GLOBAL = 16;
	static final int DELETE_GLOBAL = 17;
	static final int LOAD_ATTR = 18;

	/** Argument: the operator (BIN_*), plus {@link #INPLACE} for augmented assignment. */
	static final int BINARY_OP = 19;
	static final int UNARY_NEG = 20;
	static final int UNARY_POS = 21;
	static final int UNARY_INVERT = 22;
	static final int UNARY_NOT = 23;
	/** Argument: CMP_*. */
	static final int COMPARE_OP = 24;
	/** Argument: 1 for "is not". */
	static final int IS_OP = 25;
	/** Argument: 1 for "not in". */
	static final int CONTAINS_OP = 26;

	static final int GETITEM = 27;
	static final int SETITEM = 28;
	static final int DELITEM = 29;
	static final int BUILD_SLICE = 30;
	static final int BUILD_LIST = 31;
	static final int BUILD_TUPLE = 32;
	static final int BUILD_DICT = 33;
	static final int BUILD_STRING = 34;
	static final int LIST_APPEND = 35;
	static final int LIST_EXTEND = 36;
	static final int MAP_ADD = 37;
	static final int BUILD_SET = 38;
	static final int SET_ADD = 39;
	static final int DICT_ADD_KW = 40;
	static final int DICT_MERGE_KW = 41;
	static final int UNPACK = 42;

	static final int JUMP = 43;
	static final int JUMP_IF_FALSE = 44;
	static final int JUMP_IF_TRUE = 45;
	static final int JUMP_IF_FALSE_KEEP = 46;
	static final int JUMP_IF_TRUE_KEEP = 47;
	static final int GET_ITER = 48;
	static final int FOR_ITER = 49;

	static final int CALL = 50;
	static final int CALL_KW = 51;
	/** Argument: bit 0 set if a dict of keyword arguments is on the stack. */
	static final int CALL_EX = 52;
	/** Argument: bit 0 defaults tuple, bit 1 keyword-only defaults dict, bit 2 closure tuple. */
	static final int MAKE_FUNCTION = 53;
	static final int RETURN = 54;
	/** Argument 0: re-raise the active exception (an error here); 1: raise TOS. */
	static final int RAISE = 55;
	static final int RERAISE = 56;
	static final int EXC_MATCH = 57;
	static final int IMPORT = 58;
	/** Pushes a member of the module on top, which stays there. */
	static final int IMPORT_FROM = 59;
	/** Argument: bits 0-1 conversion (0 none, 1 str, 2 repr, 3 ascii), bit 2 a format spec is on the stack. */
	static final int FORMAT_VALUE = 60;

	/** obj.name = value: obj on top, the value under it. */
	static final int STORE_ATTR = 61;
	static final int DELETE_ATTR = 62;
	/** Pops the exception that a handler starts to handle; argument: the nesting of the handler in its frame. */
	static final int HANDLER_ENTER = 63;
	/** The handler of the nesting given as argument is over. */
	static final int HANDLER_EXIT = 64;
	/** Like DELETE_FAST, DELETE_CELL and DELETE_GLOBAL, but the name may be unbound already. */
	static final int DELETE_FAST_Q = 65;
	static final int DELETE_CELL_Q = 66;
	static final int DELETE_GLOBAL_Q = 67;
	static final int COUNT = 68;

	// BINARY_OP operators
	static final int BIN_ADD = 0;
	static final int BIN_SUB = 1;
	static final int BIN_MUL = 2;
	static final int BIN_DIV = 3;
	static final int BIN_FLOORDIV = 4;
	static final int BIN_MOD = 5;
	static final int BIN_POW = 6;
	static final int BIN_LSHIFT = 7;
	static final int BIN_RSHIFT = 8;
	static final int BIN_AND = 9;
	static final int BIN_OR = 10;
	static final int BIN_XOR = 11;
	static final int INPLACE = 0x100;

	// COMPARE_OP operators
	static final int CMP_EQ = 0;
	static final int CMP_NE = 1;
	static final int CMP_LT = 2;
	static final int CMP_LE = 3;
	static final int CMP_GT = 4;
	static final int CMP_GE = 5;

	static final String[] NAMES = { "NOP", "POP", "DUP", "DUP2", "ROT2", "ROT3", "LOAD_CONST", "LOAD_FAST",
			"STORE_FAST", "DELETE_FAST", "LOAD_CELL", "STORE_CELL", "DELETE_CELL", "LOAD_CLOSURE", "MAKE_CELL",
			"LOAD_GLOBAL", "STORE_GLOBAL", "DELETE_GLOBAL", "LOAD_ATTR", "BINARY_OP", "UNARY_NEG", "UNARY_POS",
			"UNARY_INVERT", "UNARY_NOT", "COMPARE_OP", "IS_OP", "CONTAINS_OP", "GETITEM", "SETITEM", "DELITEM",
			"BUILD_SLICE", "BUILD_LIST", "BUILD_TUPLE", "BUILD_DICT", "BUILD_STRING", "LIST_APPEND", "LIST_EXTEND",
			"MAP_ADD", "BUILD_SET", "SET_ADD", "DICT_ADD_KW", "DICT_MERGE_KW", "UNPACK", "JUMP", "JUMP_IF_FALSE", "JUMP_IF_TRUE",
			"JUMP_IF_FALSE_KEEP", "JUMP_IF_TRUE_KEEP", "GET_ITER", "FOR_ITER", "CALL", "CALL_KW", "CALL_EX",
			"MAKE_FUNCTION", "RETURN", "RAISE", "RERAISE", "EXC_MATCH", "IMPORT", "IMPORT_FROM", "FORMAT_VALUE",
			"STORE_ATTR", "DELETE_ATTR", "HANDLER_ENTER", "HANDLER_EXIT", "DELETE_FAST_Q", "DELETE_CELL_Q",
			"DELETE_GLOBAL_Q" };

	static boolean isJump(int op) {
		return op >= JUMP && op <= JUMP_IF_TRUE_KEEP || op == FOR_ITER;
	}

	/** Change of the stack depth when the instruction falls through. */
	static int effect(int op, int arg) {
		switch (op) {
		case NOP:
		case ROT2:
		case ROT3:
		case DELETE_FAST:
		case DELETE_CELL:
		case MAKE_CELL:
		case DELETE_GLOBAL:
		case HANDLER_EXIT:
		case DELETE_FAST_Q:
		case DELETE_CELL_Q:
		case DELETE_GLOBAL_Q:
		case LOAD_ATTR:
		case UNARY_NEG:
		case UNARY_POS:
		case UNARY_INVERT:
		case UNARY_NOT:
		case JUMP:
		case GET_ITER:
			return 0;
		case POP:
		case STORE_FAST:
		case STORE_CELL:
		case STORE_GLOBAL:
		case BINARY_OP:
		case COMPARE_OP:
		case IS_OP:
		case CONTAINS_OP:
		case GETITEM:
		case LIST_APPEND:
		case SET_ADD:
		case LIST_EXTEND:
		case DICT_MERGE_KW:
		case JUMP_IF_FALSE:
		case JUMP_IF_TRUE:
		case JUMP_IF_FALSE_KEEP:
		case JUMP_IF_TRUE_KEEP:
		case RETURN:
		case RAISE:
		case RERAISE:
		case EXC_MATCH:
		case DELETE_ATTR:
		case HANDLER_ENTER:
			return -1;
		case DUP:
		case LOAD_CONST:
		case LOAD_FAST:
		case LOAD_CELL:
		case LOAD_CLOSURE:
		case LOAD_GLOBAL:
		case FOR_ITER:
		case IMPORT:
		case IMPORT_FROM:
			return 1;
		case DUP2:
			return 2;
		case SETITEM:
			return -3;
		case DELITEM:
		case STORE_ATTR:
		case BUILD_SLICE:
		case MAP_ADD:
		case DICT_ADD_KW:
			return -2;
		case BUILD_LIST:
		case BUILD_TUPLE:
		case BUILD_SET:
		case BUILD_STRING:
			return 1 - arg;
		case BUILD_DICT:
			return 1 - 2 * arg;
		case UNPACK:
			return arg - 1;
		case CALL:
			return -arg;
		case CALL_KW:
			return -(arg + 1);
		case CALL_EX:
			return -(1 + (arg & 1));
		case MAKE_FUNCTION:
			return -(Integer.bitCount(arg & 7));
		case FORMAT_VALUE:
			return -((arg >> 2) & 1);
		default:
			throw new IllegalArgumentException("opcode " + op);
		}
	}
}
