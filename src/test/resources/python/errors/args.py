# Shared harness for the argument-type tests of this directory.
# (Lua's version was a module loaded with require("args"). The subset cannot
# import user modules, so each test file carries its own copy of this harness;
# this file runs a self-check of it.)

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

# self-check of the harness itself
anylua = [None, False, True, 0, 1.5, "abc", [1, 2], {"k": 1}]

banner("harness")
checkallpass("len", len, [["abc", [1, 2], {"k": 1}]])
checkallerrors("len", len, [[None, 1, 1.5]], "TypeError")
checkallpass("type", type, [anylua])
checkallerrors("type", type, [], "TypeError")
print("OK")
