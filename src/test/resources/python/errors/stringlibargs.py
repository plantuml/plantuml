# Argument-type tests for the string operations. Lua's string library functions
# map onto Python's str methods and builtins: byte -> ord, char -> chr,
# find -> str.find, format -> %, gsub -> str.replace, len, lower, upper, rep -> *,
# reverse -> [::-1], sub -> slicing. Lua's gmatch and match (patterns) have no
# equivalent here and are not tested. Python raises AttributeError for a missing
# method and TypeError for a wrong argument type.

def product(lists):
    result = [[]]
    for choices in lists:
        new = []
        for prefix in result:
            for v in choices:
                new.append(prefix + [v])
        result = new
    return result

def outcome(f, args):
    try:
        f(*args)
        return "ok"
    except Exception as e:
        return type(e).__name__

def banner(name):
    print("-------- " + name)

def checkallpass(name, f, lists):
    combos = product(lists)
    assert len(combos) > 0, name
    for args in combos:
        r = outcome(f, args)
        assert r == "ok", name + " raised " + r
    print(name + ": " + str(len(combos)) + " calls ok")

def checkallerrors(name, f, lists, expected):
    combos = product(lists)
    assert len(combos) > 0, name
    for args in combos:
        r = outcome(f, args)
        assert r == expected, name + " raised " + r + ", expected " + expected
    print(name + ": " + str(len(combos)) + " calls raised " + expected)

somestring = ["abc", "hello"]
notastring = [None, 1, [1, 2]]
somenumber = [0, 1, 2, -1, 3]
notanumber = [None, "abc", {"k": 1}]

def sord(s):
    return ord(s)

def schr(n):
    return chr(n)

def sfind(s, sub, start=0):
    return s.find(sub, start)

def sformat(fmt, v):
    return fmt % v

def sreplace(s, old, new):
    return s.replace(old, new)

def slen(s):
    return len(s)

def slower(s):
    return s.lower()

def supper(s):
    return s.upper()

def srep(s, n):
    return s * n

def sreverse(s):
    return s[::-1]

def ssub(s, i, j):
    return s[i:j]

banner("string.byte (ord)")
checkallpass("ord", sord, [["a", "Z"]])
checkallerrors("ord-length", sord, [["ab", ""]], "TypeError")
checkallerrors("ord-type", sord, [[None, 1, [1, 2]]], "TypeError")

banner("string.char (chr)")
checkallpass("chr", schr, [[0, 9, 40, 127, 255]])
checkallerrors("chr-range", schr, [[-1]], "ValueError")
checkallerrors("chr-type", schr, [["45", None, 6.7]], "TypeError")

banner("string.find")
checkallpass("find", sfind, [somestring, somestring, [0, 1, -3, 3]])
checkallerrors("find-notstring", sfind, [notastring, somestring], "AttributeError")
checkallerrors("find-notsub", sfind, [somestring, [None, 1, [1, 2]]], "TypeError")
checkallerrors("find-notstart", sfind, [somestring, somestring, ["x", 1.5]], "TypeError")

banner("string.format (%)")
checkallpass("format %s", sformat, [["%s", "%r"], anylua_placeholder if False else [None, 1, "abc", [1, 2]]])
checkallpass("format %d", sformat, [["%d", "%i", "%x"], [1, -3, 255]])
checkallerrors("format %d-notnumber", sformat, [["%d", "%x"], ["abc", None, [1, 2]]], "TypeError")
checkallerrors("format bad option", sformat, [["%w"], ["x"]], "ValueError")

banner("string.gsub (replace)")
checkallpass("replace", sreplace, [somestring, ["a", "l"], ["x", "yy"]])
checkallerrors("replace-notstring", sreplace, [notastring, ["a"], ["x"]], "AttributeError")
checkallerrors("replace-notold", sreplace, [somestring, [None, 1], ["x"]], "TypeError")
checkallerrors("replace-notnew", sreplace, [somestring, ["a"], [None, 1]], "TypeError")

banner("string.len")
checkallpass("len", slen, [somestring])
checkallerrors("len", slen, [[None, 1, 1.5]], "TypeError")

banner("string.lower")
checkallpass("lower", slower, [somestring])
checkallerrors("lower", slower, [notastring], "AttributeError")

banner("string.upper")
checkallpass("upper", supper, [somestring])
checkallerrors("upper", supper, [notastring], "AttributeError")

banner("string.rep")
checkallpass("rep", srep, [somestring, [0, 2, 3]])
checkallerrors("rep-notstring", srep, [[None, {"k": 1}], [2]], "TypeError")
checkallerrors("rep-notnumber", srep, [somestring, [None, 1.5, "x"]], "TypeError")

banner("string.reverse")
checkallpass("reverse", sreverse, [somestring])
checkallerrors("reverse", sreverse, [[None, 1]], "TypeError")

banner("string.sub (slice)")
checkallpass("sub", ssub, [somestring, [0, 1, -2], [1, 3, 5]])
checkallerrors("sub-notstring", ssub, [[None, 1], [0], [1]], "TypeError")
checkallerrors("sub-notindex", ssub, [somestring, ["x", 1.5], [1]], "TypeError")
checkallerrors("sub-arity", ssub, [somestring], "TypeError")

print("OK")
