# Argument-type tests for the operators: unary minus, len, not, call, binary
# arithmetic, comparisons, indexing, attribute access, and/or, for-range, and
# concatenation. Lua's "a..b" is Python's + on strings. Python never raises on
# == and !=, so those are only checked for passing. Unlike Lua, Python raises
# on "abc" + 1 and on 1 + None, and repeats "abc" * 2 without error.

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

anylua = [None, False, True, 0, 1.5, "abc", [1, 2], {"k": 1}]
somenumber = [1, 0.75, -1, -0.25]
notanumber = [None, "abc", [1, 2], {"k": 1}]
notmulnumber = [None, {"k": 1}]
somestring = ["abc", "def"]
nonstring = [None, 1, [1]]

def neg(a):
    return -a

def lnot(a):
    return not a

def lcall(f, *args):
    return f(*args)

def add(a, b):
    return a + b

def sub(a, b):
    return a - b

def mul(a, b):
    return a * b

def div(a, b):
    return a / b

def mod(a, b):
    return a % b

def power(a, b):
    return a ** b

def eq(a, b):
    return a == b

def neq(a, b):
    return a != b

def le(a, b):
    return a <= b

def ge(a, b):
    return a >= b

def lt(a, b):
    return a < b

def gt(a, b):
    return a > b

def idx(t, k):
    return t[k]

def fattr(o):
    return o.upper

def land(a, b):
    return a and b

def lor(a, b):
    return a or b

def forop(a, b, c):
    for i in range(a, b, c):
        pass

def concat(a, b):
    return a + b

banner("unary minus")
checkallpass("neg", neg, [somenumber])
checkallerrors("neg", neg, [notanumber], "TypeError")

banner("len")
checkallpass("len", len, [["abc", [1, 2], {"k": 1}]])
checkallerrors("len", len, [[None, 1, 1.5]], "TypeError")

banner("not")
checkallpass("not", lnot, [anylua])

banner("call")
checkallpass("call", lcall, [[repr, str], [1, None]])
checkallerrors("call-notfunction", lcall, [[None, 1, "abc", [1]]], "TypeError")

banner("+")
checkallpass("add", add, [somenumber, somenumber])
checkallerrors("add-left", add, [notanumber, somenumber], "TypeError")
checkallerrors("add-right", add, [somenumber, notanumber], "TypeError")

banner("-")
checkallpass("sub", sub, [somenumber, somenumber])
checkallerrors("sub-left", sub, [notanumber, somenumber], "TypeError")
checkallerrors("sub-right", sub, [somenumber, notanumber], "TypeError")

banner("*")
checkallpass("mul", mul, [somenumber, somenumber])
checkallerrors("mul-left", mul, [notmulnumber, somenumber], "TypeError")
checkallerrors("mul-right", mul, [somenumber, notmulnumber], "TypeError")

banner("/")
checkallpass("div", div, [somenumber, somenumber])
checkallerrors("div-left", div, [notanumber, somenumber], "TypeError")
checkallerrors("div-right", div, [somenumber, notanumber], "TypeError")

banner("%")
checkallpass("mod", mod, [somenumber, somenumber])
checkallerrors("mod-left", mod, [[None, [1, 2]], somenumber], "TypeError")
checkallerrors("mod-right", mod, [somenumber, [None, [1, 2]]], "TypeError")

banner("**")
# A negative base with a fractional exponent gives a complex number in CPython
# (and a ValueError here: there is no complex type), so those pairs are left out.
checkallpass("power", power, [[1, 0.75], somenumber])
checkallpass("power-negative", power, [[-1, -0.25], [1, -1]])
checkallerrors("power-left", power, [notanumber, somenumber], "TypeError")
checkallerrors("power-right", power, [somenumber, notanumber], "TypeError")

banner("==, !=")
checkallpass("eq", eq, [anylua, anylua])
checkallpass("neq", neq, [anylua, anylua])

banner("<=, >=, <, >")
checkallpass("le", le, [somenumber, somenumber])
checkallpass("ge", ge, [somenumber, somenumber])
checkallpass("lt", lt, [somenumber, somenumber])
checkallpass("gt", gt, [somenumber, somenumber])
checkallerrors("le-left", le, [notanumber, somenumber], "TypeError")
checkallerrors("ge-right", ge, [somenumber, notanumber], "TypeError")
checkallerrors("lt-left", lt, [notanumber, somenumber], "TypeError")
checkallerrors("gt-right", gt, [somenumber, notanumber], "TypeError")

banner("[]")
checkallpass("index-dict", idx, [[{"k": 1}], ["k"]])
checkallpass("index-list", idx, [[[1, 2]], [0, 1]])
checkallerrors("index-none", idx, [[None, 1], [0]], "TypeError")
checkallerrors("index-list-key", idx, [[[1, 2]], ["k"]], "TypeError")
checkallerrors("index-missing", idx, [[{"k": 1}], ["zz"]], "KeyError")

banner(".")
checkallpass("attr", fattr, [["abc"]])
checkallerrors("attr", fattr, [[None]], "AttributeError")

banner("and, or")
checkallpass("and", land, [anylua, anylua])
checkallpass("or", lor, [anylua, anylua])

banner("for x = a, b, c (range)")
checkallpass("for", forop, [[1, 3, -2], [4, 0, 2], [1, -2, -1]])
checkallerrors("for-type", forop, [[1.5, None, "abc"], [3], [1]], "TypeError")
checkallerrors("for-zero-step", forop, [[1], [3], [0]], "ValueError")

banner("..")
checkallpass("concat", concat, [somestring, somestring])
checkallerrors("concat-right", concat, [somestring, nonstring], "TypeError")
checkallerrors("concat-left", concat, [nonstring, somestring], "TypeError")

print("OK")
