"""Dump the CPython syntax tree of a script in the text format of AstDump.

Usage, from the project root:

    python3 -I tools/dump_ast.py <script.py>

The output is compared with the tree built by org.luaj.vm2.python.PyParser
(PyParserTest), and is used to (re)generate the reference dumps of
src/test/resources/python-ast/*.ast. Format (see AstDump.java):

- a node on one line when all its fields are scalars or empty, otherwise one
  field per line, children indented by two spaces;
- lists in brackets, one element per line;
- operators and contexts as bare names (Add, Load, ...);
- strings in double quotes, with a backslash before '"' and '\\', and
  \\u{hex} for anything outside printable ASCII;
- floats as float:<the 16 hex digits of the IEEE 754 bits>;
- LF line endings, whatever the platform.

The fields that PyParser does not build (type_comment, type_params,
type_ignores, kind) are left out.
"""

import ast
import struct
import sys

SKIPPED_FIELDS = {"type_comment", "type_params", "type_ignores", "kind"}


def fields(node):
    return [f for f in node._fields if f not in SKIPPED_FIELDS]


def is_flat(node):
    for f in fields(node):
        v = getattr(node, f)
        if isinstance(v, ast.AST):
            if fields(v):
                return False
        elif isinstance(v, list):
            if v:
                return False
    return True


def scalar(v):
    if v is None:
        return "None"
    if v is True:
        return "True"
    if v is False:
        return "False"
    if isinstance(v, int):
        return str(v)
    if isinstance(v, float):
        return "float:%016x" % struct.unpack(">Q", struct.pack(">d", v))[0]
    if isinstance(v, str):
        out = []
        for c in v:
            code = ord(c)
            if c == '"':
                out.append('\\"')
            elif c == "\\":
                out.append("\\\\")
            elif 0x20 <= code < 0x7F:
                out.append(c)
            else:
                out.append("\\u{%x}" % code)
        return '"' + "".join(out) + '"'
    raise TypeError("cannot dump %r" % (v,))


def value(v, level):
    if isinstance(v, ast.AST):
        name = type(v).__name__
        fs = fields(v)
        if not fs:
            return name
        flat = is_flat(v)
        parts = []
        for i, f in enumerate(fs):
            s = ""
            if i > 0:
                s += ", " if flat else ","
            if not flat:
                s += "\n" + "  " * (level + 1)
            s += f + "=" + value(getattr(v, f), level + 1)
            parts.append(s)
        tail = "" if flat else "\n" + "  " * level
        return name + "(" + "".join(parts) + tail + ")"
    if isinstance(v, list):
        if not v:
            return "[]"
        items = ",".join("\n" + "  " * (level + 1) + value(x, level + 1) for x in v)
        return "[" + items + "\n" + "  " * level + "]"
    return scalar(v)


def main(argv):
    if len(argv) != 2:
        sys.stderr.write("usage: python3 -I tools/dump_ast.py <script.py>\n")
        return 2
    with open(argv[1], encoding="utf-8") as f:
        source = f.read()
    text = value(ast.parse(source, filename=argv[1]), 0) + "\n"
    # bytes, so that the line endings stay LF on Windows too
    sys.stdout.buffer.write(text.encode("utf-8"))
    return 0


if __name__ == "__main__":
    sys.exit(main(sys.argv))
