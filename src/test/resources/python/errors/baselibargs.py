# Argument-type tests for the builtins that have a counterpart in Lua's base
# library. Lua-only functions (collectgarbage, error levels, getmetatable, load,
# next, select, setmetatable) have no equivalent in this subset and are not
# tested. Python does not coerce strings to numbers, so the number sets hold
# numbers only, and bool (an int in Python) is kept out of the "not a number" sets.

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
notanil = [False, True, 0, 1.5, "abc", [1, 2], {"k": 1}]
sometable = [[1, 2], {"k": 1}]
notiterable = [None, 1, 1.5, True]
notanumber = [None, "abc", [1, 2], {"k": 1}]

def pcall(fn, *args):
    try:
        return True, fn(*args)
    except Exception as e:
        return False, type(e).__name__

def pyassert(v, msg="assertion failed"):
    assert v, msg

def pyraise(msg):
    raise ValueError(msg)

def rawequal(a, b):
    return a is b

def rawget(t, k):
    return t[k]

def rawset(t, k, v):
    t[k] = v

def fenum(t):
    return list(enumerate(t))

def fitems(t):
    return list(t.items())

def fint(x):
    return int(x)

banner("assert")
checkallpass("assert", pyassert, [[True, 1, "abc", [1]], anylua])
checkallerrors("assert", pyassert, [[None, False, 0, ""], ["message"]], "AssertionError")

banner("error")
checkallerrors("error", pyraise, [anylua], "ValueError")

banner("enumerate (Lua ipairs)")
checkallpass("enumerate", fenum, [sometable])
checkallerrors("enumerate", fenum, [notiterable], "TypeError")
checkallerrors("enumerate", fenum, [], "TypeError")

banner("dict.items (Lua pairs)")
checkallpass("dict.items", fitems, [[{"k": 1}, {}]])
checkallerrors("dict.items", fitems, [[None, 1, "abc", [1, 2]]], "AttributeError")

banner("pcall")
checkallpass("pcall", pcall, [notanil, anylua])
checkallerrors("pcall", pcall, [], "TypeError")

banner("print")
checkallpass("print", print, [[None, True, 1, 1.5, "abc"]])

banner("rawequal")
checkallpass("rawequal", rawequal, [anylua, anylua])
checkallerrors("rawequal", rawequal, [], "TypeError")
checkallerrors("rawequal", rawequal, [anylua], "TypeError")

banner("rawget")
checkallpass("rawget", rawget, [[{"k": 1}], ["k"]])
checkallerrors("rawget", rawget, [[{"k": 1}], ["zz"]], "KeyError")
checkallerrors("rawget", rawget, [[None, 1, 1.5, True], ["k"]], "TypeError")
checkallerrors("rawget", rawget, [[{"k": 1}]], "TypeError")

banner("rawset")
checkallpass("rawset", rawset, [[{"k": 1}], ["k", "x"], anylua])
checkallerrors("rawset", rawset, [[None, 1], ["k"], anylua], "TypeError")
checkallerrors("rawset", rawset, [[{"k": 1}], [[1, 2]], anylua], "TypeError")
checkallerrors("rawset", rawset, [], "TypeError")

banner("int (Lua tonumber)")
checkallpass("int", fint, [["1", "-2", 3.5, 7]])
checkallerrors("int-value", fint, [["abc"]], "ValueError")
checkallerrors("int-type", fint, [[None, [1, 2], {"k": 1}]], "TypeError")
checkallerrors("int-arity", fint, [], "TypeError")

banner("str (Lua tostring)")
checkallpass("str", str, [anylua])

banner("type")
checkallpass("type", type, [anylua])
checkallerrors("type", type, [], "TypeError")

print("OK")
