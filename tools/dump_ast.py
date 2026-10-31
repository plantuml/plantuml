#!/usr/bin/env python3
"""Dump the CPython AST of a script in the canonical text format that the Java
parser (org.luaj.vm2.python.AstDump) also produces, so the two can be compared.

    python3 tools/dump_ast.py script.py > script.ast

Format: one node per line when all its fields are scalars, otherwise one field
per line; lists in brackets; operators and contexts as bare names; strings
escaped as \\u{hex} outside printable ASCII; floats as float:<IEEE bits>.
Position attributes and the fields type_ignores, type_comment, kind and
type_params are left out.
"""
import ast
import struct
import sys

sys.setrecursionlimit(20000)
SKIP = {"type_ignores", "type_comment", "kind", "type_params"}


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
        out = ['"']
        for ch in v:
            c = ord(ch)
            if ch == '"':
                out.append('\\"')
            elif ch == "\\":
                out.append("\\\\")
            elif 0x20 <= c < 0x7F:
                out.append(ch)
            else:
                out.append("\\u{%x}" % c)
        out.append('"')
        return "".join(out)
    raise TypeError(repr(v))


def fields(node):
    return [(n, v) for n, v in ast.iter_fields(node) if n not in SKIP]


def is_flat(v):
    if isinstance(v, ast.AST):
        return all(not isinstance(x, (ast.AST, list)) or
                   (isinstance(x, ast.AST) and not fields(x)) or
                   (isinstance(x, list) and not x)
                   for _, x in fields(v))
    return not isinstance(v, list) or not v


def dump(v, ind):
    pad = "  " * ind
    if isinstance(v, ast.AST):
        fs = fields(v)
        name = type(v).__name__
        if not fs:
            return name
        if is_flat(v):
            return "%s(%s)" % (name, ", ".join("%s=%s" % (n, dump(x, 0)) for n, x in fs))
        body = ",\n".join("%s  %s=%s" % (pad, n, dump(x, ind + 1)) for n, x in fs)
        return "%s(\n%s\n%s)" % (name, body, pad)
    if isinstance(v, list):
        if not v:
            return "[]"
        body = ",\n".join("%s  %s" % (pad, dump(x, ind + 1)) for x in v)
        return "[\n%s\n%s]" % (body, pad)
    return scalar(v)


def main(path):
    with open(path, encoding="utf-8") as f:
        tree = ast.parse(f.read(), path)
    sys.stdout.write(dump(tree, 0) + "\n")


if __name__ == "__main__":
    main(sys.argv[1])
