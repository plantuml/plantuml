platform = select(1, *argv)
print("platform", platform)

aliases = {
    "0": "<zero>",
    "-0": "<zero>",
    "nan": "<nan>",
    "inf": "<pos-inf>",
    "-inf": "<neg-inf>",
    "1.#INF": "<pos-inf>",
    "-1.#INF": "<neg-inf>",
    "1.#IND": "<nan>",
    "-1.#IND": "<nan>",
}

UNOPVALUES = [-2.5, -2, 0, 2, 2.5, "'-2.5'", "'-2'", "'0'", "'2'", "'2.5'"]

NUMBERPAIRS = [
    [2, 0], [-2.5, 0], [2, 1],
    [5, 2], [-5, 2], [16, 2], [-16, -2],
    [.5, 0], [.5, 1], [.5, 2], [.5, -1], [.5, 2],
    [2.25, 0], [2.25, 2], [-2, 0],
    [3, 3],
]

STRINGPAIRS = [
    ["'2'", "'0'"], ["'2.5'", "'3'"], ["'-2'", "'1.5'"], ["'-2.5'", "'-1.5'"],
    ["'3.0'", "'3.0'"], [2.75, 2.75], ["'2.75'", "'2.75'"],
]

MIXEDPAIRS = [
    [3, "'3'"], ["'3'", 3], [2.75, "'2.75'"], ["'2.75'", 2.75],
    [-3, "'-4'"], ["'-3'", 4], [-3, "'4'"], ["'-3'", -4],
    [-4.75, "'2.75'"], ["'-2.75'", 1.75], [4.75, "'-2.75'"], ["'2.75'", -1.75],
]

BINOPVALUES = []

RELATIONALOPVALUES = []

def addall(t, s):
    for i, v in ipairs(s):
        t[len(t) + 1] = v

addall(BINOPVALUES, NUMBERPAIRS)
addall(BINOPVALUES, STRINGPAIRS)
addall(BINOPVALUES, MIXEDPAIRS)
addall(RELATIONALOPVALUES, NUMBERPAIRS)
addall(RELATIONALOPVALUES, STRINGPAIRS)

VARARGSVALUES = [
    [4], [-4.5], ["'5.5'"], ["'-5'"],
    [4, "'8'"], [-4.5, "'-8'"], ["'5.5'", 2.2], ["'-5'", -2.2],
    [111, 222, 333], [-222, -333, -111], [444, -111, -222],
]

CONSTANTS = [
    "huge", "pi",
]
UNARYOPS = [
    "-", "not ",
]
BINARYOPS = [
    "+", "-", "*", "**", "/", "%",
]
RELATIONALS = [
    "==", "!=", ">", "<", ">=", "<=",
]
ONEARG_JME = [
    "abs", "ceil", "cos", "deg",
    "exp", "floor", "frexp", "modf",
    "rad", "sin", "sqrt", "tan",
]
ONEARG_JSE = [
    "acos", "asin", "atan", "cosh",
    "log", "sinh", "tanh",
]
TWOARGS_JME = [
    "fmod", "ldexp", "pow",
]
TWOARGS_JSE = [
    "atan2",
]
VARARGSFUNCS = [
    "max", "min",
]

ts = tostring
def tostring(x):
    s = ts(x)
    if type(x) != "number": return s
    if aliases[s]: return aliases[s]
    if len(s) < 7: return s
    a, b = string.match(s, "([%-0-9%.]*)([eE]?[%-0-9]*)")
    return a and (string.sub(a, 1, 6) .. (b or "")) or s

def eval(expr, script):
    script = script or ("return " .. expr)
    s, a, b = load(script, "expr")
    if s: print(expr, pcall(s))
    else: print(expr, "load:", a)

# misc tests
print("---------- miscellaneous tests ----------")
eval("math.sin( 0.0 )")
eval("math.cos( math.pi )")
eval("math.sqrt( 9.0 )")
eval("math.modf( 5.25 )")
eval("math.frexp(0.00625)")
eval("-5 ** 2")
eval("-5 / 2")
eval("-5 % 2")

# constants
print("---------- constants ----------")
for i, v in ipairs(CONSTANTS):
    eval("math." .. v)

# unary operators
for i, v in ipairs(UNARYOPS):
    print("---------- unary operator " .. v .. " ----------")
    for j, a in ipairs(UNOPVALUES):
        eval(v .. a, "return " .. v .. a)

# binary operators
for i, v in ipairs(BINARYOPS):
    print("---------- binary operator " .. v .. " ----------")
    for j, xy in ipairs(BINOPVALUES):
        eval(xy[1] .. v .. xy[2],
            "x, y = " .. xy[1] .. ", " .. xy[2] .. "; return x" .. v .. "y")

# relational operators
for i, v in ipairs(RELATIONALS):
    print("---------- binary operator " .. v .. " ----------")
    for j, xy in ipairs(RELATIONALOPVALUES):
        eval(xy[1] .. v .. xy[2],
            "x, y = " .. xy[1] .. ", " .. xy[2] .. "; return x" .. v .. "y")

# one-argument math functions
for i, v in ipairs(ONEARG_JME):
    print("---------- math." .. v .. " ----------")
    for j, x in ipairs(UNOPVALUES):
        eval("math." .. v .. "(" .. x .. ")")
if platform != "JME":
    for i, v in ipairs(ONEARG_JSE):
        print("---------- math." .. v .. " (jse only) ----------")
        for j, x in ipairs(UNOPVALUES):
            eval("math." .. v .. "(" .. x .. ")")

# two-argument math functions
for i, v in ipairs(TWOARGS_JME):
    print("---------- math." .. v .. " ----------")
    for j, x in ipairs(BINOPVALUES):
        eval("math." .. v .. "(" .. x[1] .. "," .. x[2] .. ")")
if platform != "JME":
    for i, v in ipairs(TWOARGS_JSE):
        print("---------- math." .. v .. " (jse only) ----------")
        for j, x in ipairs(BINOPVALUES):
            eval("math." .. v .. "(" .. x[1] .. "," .. x[2] .. ")")

# var-arg math functions
for i, v in ipairs(VARARGSFUNCS):
    print("---------- math." .. v .. " ----------")
    for j, x in ipairs(VARARGSVALUES):
        eval("math." .. v .. "(" .. table.concat(x, ",") .. ")")

# random tests
print("----------- Random number tests")
def testrandom(string, lo, hi):
    c, e = load("return " .. string)
    for i in range(1, 6):
        s, e = pcall(c)
        if s:
            print(string, s and type(e) or e, (e >= lo) and (e <= hi))
        else:
            print(string, "error", e)

testrandom("math.random()", 0, 1)
testrandom("math.random(5,10)", 5, 10)
testrandom("math.random(30)", 0, 30)
testrandom("math.random(-4,-2)", -4, -2)
t = {}
print(math.randomseed(20))
for i in range(1, 21):
    t[i] = math.random()
print("-- comparing new numbers")
for i in range(1, 21):
    print(t[i] == math.random(), t[i] == t[0])
print("-- resetting seed")

print(math.randomseed(20))
for i in range(1, 21):
    print(t[i] == math.random())

# tests involving -0, which is folded into 0 for luaj, but not for plain lua
print("----------- Tests involving -0 and NaN")
print("0 == -0", 0 == -0)
t = {0: 10, 20, 30, 40, 50}
print("t[-0] == t[0]", t[-0] == t[0])
x = -1
mz, z = 0 / x, 0  # minus zero, zero
print("mz, z", mz, z)
print("mz == z", mz == z)
a = {mz: 1}
print("a[z] == 1 and a[mz] == 1", a[z] == 1 and a[mz] == 1)
# string with same binary representation as 0.0 (may create problems
# for constant manipulation in the pre-compiler)
a1, a2, a3, a4, a5 = 0, 0, "\x00\x00\x00\x00\x00\x00\x00\x00", 0, "\x00\x00\x00\x00\x00\x00\x00\x00"
assert(a1 == a2 and a2 == a4 and a1 != a3)
assert(a3 == a5)
