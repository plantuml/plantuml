package org.luaj.vm2.python;

import java.util.List;

/**
 * Text form of a syntax tree. The format is the one written by
 * {@code tools/dump_ast.py} for CPython's tree, which lets the two be compared.
 * One node per line when all its fields are scalars, otherwise one field per
 * line; lists in brackets; operators and contexts as bare names; strings
 * escaped as backslash, u and the code point in braces outside printable ASCII; floats as
 * {@code float:<IEEE bits>}.
 */
public final class AstDump {
	private AstDump() {
	}

	public static String dump(Node node) {
		StringBuilder sb = new StringBuilder();
		value(sb, node, 0);
		sb.append('\n');
		return sb.toString();
	}

	private static void indent(StringBuilder sb, int level) {
		for (int i = 0; i < level; i++)
			sb.append("  ");
	}

	private static boolean flat(Node n) {
		for (int i = 0; i < n.fieldCount(); i++) {
			Object v = n.fieldValue(i);
			if (v instanceof Node) {
				if (((Node) v).fieldCount() != 0)
					return false;
			} else if (v instanceof List) {
				if (!((List<?>) v).isEmpty())
					return false;
			}
		}
		return true;
	}

	private static void value(StringBuilder sb, Object v, int level) {
		if (v instanceof Node) {
			Node n = (Node) v;
			sb.append(n.type);
			if (n.fieldCount() == 0)
				return;
			sb.append('(');
			boolean flat = flat(n);
			for (int i = 0; i < n.fieldCount(); i++) {
				if (i > 0)
					sb.append(flat ? ", " : ",");
				if (!flat) {
					sb.append('\n');
					indent(sb, level + 1);
				}
				sb.append(n.fieldName(i)).append('=');
				value(sb, n.fieldValue(i), level + 1);
			}
			if (!flat) {
				sb.append('\n');
				indent(sb, level);
			}
			sb.append(')');
		} else if (v instanceof List) {
			List<?> list = (List<?>) v;
			if (list.isEmpty()) {
				sb.append("[]");
				return;
			}
			sb.append('[');
			for (int i = 0; i < list.size(); i++) {
				if (i > 0)
					sb.append(',');
				sb.append('\n');
				indent(sb, level + 1);
				value(sb, list.get(i), level + 1);
			}
			sb.append('\n');
			indent(sb, level);
			sb.append(']');
		} else {
			scalar(sb, v);
		}
	}

	private static void scalar(StringBuilder sb, Object v) {
		if (v == null) {
			sb.append("None");
		} else if (v instanceof Boolean) {
			sb.append(((Boolean) v).booleanValue() ? "True" : "False");
		} else if (v instanceof Long || v instanceof Integer) {
			sb.append(v.toString());
		} else if (v instanceof Double) {
			String hex = Long.toHexString(Double.doubleToLongBits((Double) v));
			sb.append("float:");
			for (int i = hex.length(); i < 16; i++)
				sb.append('0');
			sb.append(hex);
		} else if (v instanceof String) {
			String s = (String) v;
			sb.append('"');
			for (int i = 0; i < s.length();) {
				int c = s.codePointAt(i);
				i += Character.charCount(c);
				if (c == '"')
					sb.append("\\\"");
				else if (c == '\\')
					sb.append("\\\\");
				else if (c >= 0x20 && c < 0x7f)
					sb.append((char) c);
				else
					sb.append("\\u{").append(Integer.toHexString(c)).append('}');
			}
			sb.append('"');
		} else {
			throw new IllegalArgumentException("cannot dump " + v.getClass());
		}
	}
}
