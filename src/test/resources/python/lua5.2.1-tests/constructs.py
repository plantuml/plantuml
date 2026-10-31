print("testing syntax")

# testing semicolons
# (empty statements `;;;`, `do ... end` blocks and `repeat until` have no equivalent)
a = 3
assert(a == 3)


# testing priorities

assert(2 ** 3 ** 2 == 2 ** (3 ** 2))
assert(2 ** 3 * 4 == (2 ** 3) * 4)
assert(2 ** -2 == 1 / 4 and -2 ** - -2 == - - -4)
assert(not None and 2 and not (2 > 3 or 3 < 2))
assert(-3 - 1 - 5 == 0 + 0 - 9)
assert(-2 ** 2 == -4 and (-2) ** 2 == 4 and 2 * 2 - 3 - 1 == 0)
assert(2 * 1 + 3 / 3 == 3 and 1 + 2 .. 3 * 1 == "33")
assert(not (2 + 1 > 3 * 1) and "a" .. "b" > "a")

assert(not ((True or False) and None))
assert(True or False and None)

# old bug
assert((((1 or False) and True) or False) == True)
assert((((None and True) or False) and True) == False)

a, b = 1, None
assert(-(1 or 2) == -1 and (1 and 2) + (-1.25 or -4) == 0.75)
x = ((b or a) + 1 == 2 and (10 or a) + 1 == 11)
assert(x)
x = (((2 < 3) or 1) == True and (2 < 3 and 4) == 4)
assert(x)

x, y = 1, 2
assert((x > y) and x or y == 2)
x, y = 2, 1
assert((x > y) and x or y == 2)

assert(1234567890 == tonumber("1234567890") and 1234567890 + 1 == 1234567891)


# silly loops
while False: pass
while None: pass

# test old bug (first name could not be an `upvalue')
def silly():
    a = None
    def f(x):
        x = {"a": 1}
        x = {"x": 1}
        x = {"G": 1}
silly()

def f(i):
    if type(i) != "number": return i, "jojo"
    if i > 0: return i, f(i - 1)

x = [f(3), f(5), f(10)]
assert(x[1] == 3 and x[2] == 5 and x[3] == 10 and x[4] == 9 and x[12] == 1)
assert(x[None] == None)
x = [f("alo"), f("xixi"), None]
assert(x[1] == "alo" and x[2] == "xixi" and x[3] == None)
x = [f("alo") .. "xixi"]
assert(x[1] == "aloxixi")
x = [f({})]
assert(x[2] == "jojo" and type(x[1]) == "table")


def f(i):
    if i < 10: return "a"
    elif i < 20: return "b"
    elif i < 30: return "c"

assert(f(3) == "a" and f(12) == "b" and f(26) == "c" and f(100) == None)

# The original nests two loops that both use `i` and checks that the outer `i`
# is untouched afterwards. A loop variable is scoped to its loop in Lua; here it
# would be a plain function-level name, so the loops use their own names.
for k in range(1, 1001): break
n = 100
i = 3
t = {}
a = None
while not a:
    a = 0
    for j in range(1, n + 1):
        for k in range(j, 0, -1):
            a += 1
            t[k] = 1
assert(a == n * (n + 1) / 2 and i == 3)
assert(t[1] and t[n] and not t[0] and not t[n + 1])

# `local b` / `local a,b,c,d` inside the blocks of the original shadow the
# parameter; here they get other names.
def f(b):
    x = 1
    while True:
        a = None
        if b == 1:
            b1 = 1
            x = 10
            break
        elif b == 2:
            x = 20
            break
        elif b == 3:
            x = 30
        else:
            a, b4, c, d = math.sin(1)
            x += 1
        if x >= 12: break
    return x

assert(f(1) == 10 and f(2) == 20 and f(3) == 30 and f(4) == 12)


def f(i):
    if i < 10: return "a"
    elif i < 20: return "b"
    elif i < 30: return "c"
    else: return 8

assert(f(3) == "a" and f(12) == "b" and f(26) == "c" and f(100) == 8)

a, b = None, 23
x = [f(100) * 2 + 3 or a, a or b + 2]
assert(x[1] == 19 and x[2] == 25)
x = {"f": 2 + 3 or a, "a": b + 2}
assert(x.f == 5 and x.a == 25)

a = {"y": 1}
x = [a.y]
assert(x[1] == 1)

def f(i):
    while True:
        if i > 0: i -= 1
        else: return

def g(i):
    while True:
        if i > 0: i -= 1
        else: return

f(10)
g(10)

def f(): return 1, 2, 3
a, b, c = f()
assert(a == 1 and b == 2 and c == 3)
a, b, c = (f())
assert(a == 1 and b == None and c == None)

a, b = 3 and f()
assert(a == 1 and b == None)

def g():
    f()
    return
assert(g() == None)
def g(): return None or f()
a, b = g()
assert(a == 1 and b == None)

print("+")

# (the tests that generate operator combinations as source text are not rewritten)

# Comparisons are not chained here: `a < b == c` is `(a < b) == c`, as in Lua.
assert(1 and (2 < 3) == True and 2 < 3 and ("a" < "b") == True)
x = 2 < 3 and not 3
assert(x == False)
x = 2 < 1 or (2 > 1 and "a")
assert(x == "a")

# ----------------------------------------------------------------
print("testing short-circuit optimizations")

_G.GLOB1 = 1
_G.GLOB2 = 2

# the first element of each pair is source text, in the Python-like syntax
basiccases = [
    ["None", None],
    ["False", False],
    ["True", True],
    ["10", 10],
    ["(_G.GLOB1 < _G.GLOB2)", True],
    ["(_G.GLOB2 < _G.GLOB1)", False],
]

def and_(a, b):
    if not a: return a
    else: return b

def or_(a, b):
    if a: return a
    else: return b

binops = [
    [" and ", and_],
    [" or ", or_],
]

mem = [basiccases]    # for memoization

def allcases(n):
    if mem[n]: return mem[n]
    res = []
    # include all smaller cases
    for _, v in ipairs(allcases(n - 1)):
        res[len(res) + 1] = v
    for i in range(1, n):
        for _, v1 in ipairs(allcases(i)):
            for _, v2 in ipairs(allcases(n - i)):
                for _, op in ipairs(binops):
                    res[len(res) + 1] = [
                        "(" .. v1[1] .. op[1] .. v2[1] .. ")",
                        op[2](v1[2], v2[2])
                    ]
        print("+")
    mem[n] = res   # memoize
    return res

# do not do too many combinations for soft tests
level = _soft and 3 or 4

for _, v in pairs(allcases(level)):
    res = load("return " .. v[1])()
    assert(res == v[2])

print("OK")
