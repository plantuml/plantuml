# Argument-type tests for the math module. Lua's math.random, math.randomseed,
# math.frexp and math.ldexp have no counterpart in this subset (no random module,
# and frexp/ldexp are not in the math module offered here) and are not tested.
# Lua's math.abs/deg/rad are abs (builtin), math.degrees and math.radians.
import math

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

# Python does not coerce numeric strings, so only real numbers are "somenumber".
somenumber = [1, 0.75, -1, -0.25]
somepositive = [1, 0.75, 2, 2.5]
notanumber = [None, "abc", [1, 2], {"k": 1}]
nonnumber = ["abc", [1, 2], {"k": 1}]

singleargfunctions = [
    ("abs", abs), ("math.acos", math.acos), ("math.asin", math.asin),
    ("math.atan", math.atan), ("math.ceil", math.ceil), ("math.cos", math.cos),
    ("math.cosh", math.cosh), ("math.degrees", math.degrees),
    ("math.exp", math.exp), ("math.fabs", math.fabs), ("math.floor", math.floor),
    ("math.radians", math.radians), ("math.sin", math.sin),
    ("math.sinh", math.sinh), ("math.tan", math.tan), ("math.tanh", math.tanh),
]

singleargposdomain = [
    ("math.log", math.log), ("math.sqrt", math.sqrt),
]

twoargfunctions = [
    ("math.atan2", math.atan2),
]

twoargsposdomain = [
    ("math.pow", math.pow), ("math.fmod", math.fmod),
]

# single argument tests
for name, f in singleargfunctions:
    banner(name)
    checkallpass(name, f, [somenumber])
    checkallerrors(name, f, [notanumber], "TypeError")

# single argument, positive domain tests
for name, f in singleargposdomain:
    banner(name)
    checkallpass(name, f, [somepositive])
    checkallerrors(name, f, [notanumber], "TypeError")

# two-argument tests
for name, f in twoargfunctions:
    banner(name)
    checkallpass(name, f, [somenumber, somenumber])
    checkallerrors(name, f, [], "TypeError")
    checkallerrors(name, f, [notanumber], "TypeError")
    checkallerrors(name, f, [notanumber, somenumber], "TypeError")
    checkallerrors(name, f, [somenumber], "TypeError")
    checkallerrors(name, f, [somenumber, notanumber], "TypeError")

# two-argument, positive domain tests
for name, f in twoargsposdomain:
    banner(name)
    checkallpass(name, f, [somepositive, somenumber])
    checkallerrors(name, f, [], "TypeError")
    checkallerrors(name, f, [notanumber], "TypeError")
    checkallerrors(name, f, [notanumber, somenumber], "TypeError")
    checkallerrors(name, f, [somenumber], "TypeError")
    checkallerrors(name, f, [somenumber, notanumber], "TypeError")

# max and min (Python's builtins take any number of arguments)
banner("max")
checkallpass("max", max, [somenumber, somenumber])
checkallerrors("max", max, [], "TypeError")
checkallerrors("max", max, [somenumber, nonnumber], "TypeError")

banner("min")
checkallpass("min", min, [somenumber, somenumber])
checkallerrors("min", min, [], "TypeError")
checkallerrors("min", min, [somenumber, nonnumber], "TypeError")

print("OK")
