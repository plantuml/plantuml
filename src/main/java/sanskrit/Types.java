package sanskrit;

/** The builtin types and exception classes. */
final class Types {
	private Types() {
	}

	/** The root of every type, so that isinstance(x, object) is true and a class may name it as its base. */
	static final PyType OBJECT = new PyType("object", null, false);
	static final PyType NONE_TYPE = new PyType("NoneType", OBJECT, false);
	static final PyType INT = new PyType("int", OBJECT, false);
	static final PyType BOOL = new PyType("bool", INT, false);
	static final PyType FLOAT = new PyType("float", OBJECT, false);
	static final PyType STR = new PyType("str", OBJECT, false);
	static final PyType LIST = new PyType("list", OBJECT, false);
	static final PyType TUPLE = new PyType("tuple", OBJECT, false);
	static final PyType DICT = new PyType("dict", OBJECT, false);
	static final PyType SET = new PyType("set", OBJECT, false);
	static final PyType RANGE = new PyType("range", OBJECT, false);
	static final PyType SLICE = new PyType("slice", OBJECT, false);
	static final PyType FUNCTION = new PyType("function", OBJECT, false);
	static final PyType BUILTIN = new PyType("builtin_function_or_method", OBJECT, false);
	static final PyType METHOD = new PyType("method", OBJECT, false);
	static final PyType MODULE = new PyType("module", OBJECT, false);
	static final PyType TYPE = new PyType("type", OBJECT, false);
	static final PyType DICT_KEYS = new PyType("dict_keys", OBJECT, false);
	static final PyType DICT_VALUES = new PyType("dict_values", OBJECT, false);
	static final PyType DICT_ITEMS = new PyType("dict_items", OBJECT, false);
	static final PyType ITERATOR = new PyType("iterator", OBJECT, false);
	/** The type of NotImplemented: not in ALL, CPython has no builtin name for it. */
	static final PyType NOT_IMPLEMENTED_TYPE = new PyType("NotImplementedType", OBJECT, false);

	static final PyType BASE_EXCEPTION = new PyType("BaseException", OBJECT, true);
	static final PyType EXCEPTION = new PyType("Exception", BASE_EXCEPTION, true);
	static final PyType ARITHMETIC_ERROR = new PyType("ArithmeticError", EXCEPTION, true);
	static final PyType ZERO_DIVISION = new PyType("ZeroDivisionError", ARITHMETIC_ERROR, true);
	static final PyType OVERFLOW = new PyType("OverflowError", ARITHMETIC_ERROR, true);
	static final PyType LOOKUP_ERROR = new PyType("LookupError", EXCEPTION, true);
	static final PyType KEY_ERROR = new PyType("KeyError", LOOKUP_ERROR, true);
	static final PyType INDEX_ERROR = new PyType("IndexError", LOOKUP_ERROR, true);
	static final PyType VALUE_ERROR = new PyType("ValueError", EXCEPTION, true);
	static final PyType TYPE_ERROR = new PyType("TypeError", EXCEPTION, true);
	static final PyType ASSERTION_ERROR = new PyType("AssertionError", EXCEPTION, true);
	static final PyType ATTRIBUTE_ERROR = new PyType("AttributeError", EXCEPTION, true);
	static final PyType NAME_ERROR = new PyType("NameError", EXCEPTION, true);
	static final PyType UNBOUND_LOCAL = new PyType("UnboundLocalError", NAME_ERROR, true);
	static final PyType RUNTIME_ERROR = new PyType("RuntimeError", EXCEPTION, true);
	static final PyType RECURSION_ERROR = new PyType("RecursionError", RUNTIME_ERROR, true);
	static final PyType NOT_IMPLEMENTED = new PyType("NotImplementedError", RUNTIME_ERROR, true);
	static final PyType MEMORY_ERROR = new PyType("MemoryError", EXCEPTION, true);
	static final PyType IMPORT_ERROR = new PyType("ImportError", EXCEPTION, true);
	static final PyType MODULE_NOT_FOUND = new PyType("ModuleNotFoundError", IMPORT_ERROR, true);

	static final PyType[] ALL = { OBJECT, NONE_TYPE, INT, BOOL, FLOAT, STR, LIST, TUPLE, DICT, SET, RANGE, BASE_EXCEPTION,
			EXCEPTION, ARITHMETIC_ERROR, ZERO_DIVISION, OVERFLOW, LOOKUP_ERROR, KEY_ERROR, INDEX_ERROR, VALUE_ERROR,
			TYPE_ERROR, ASSERTION_ERROR, ATTRIBUTE_ERROR, NAME_ERROR, UNBOUND_LOCAL, RUNTIME_ERROR, RECURSION_ERROR,
			NOT_IMPLEMENTED, MEMORY_ERROR, IMPORT_ERROR, MODULE_NOT_FOUND };
}
