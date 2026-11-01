print("testing syntax")

# Operator priorities, short-circuit evaluation, conditionals, multiple assignment.
# (Lua's `..` is `+` on strings here; the Lua `do ... end` and `repeat until`
# blocks and the source-generated operator combinations are not ported.)

a = 3
assert a == 3


# testing priorities

assert 2 ** 3 ** 2 == 2 ** (3 ** 2)
assert 2 ** 3 * 4 == (2 ** 3) * 4
assert 2 ** -2 == 1 / 4 and -2 ** - -2 == - - -4
assert not None and 2 and not (2 > 3 or 3 < 2)
assert -3 - 1 - 5 == 0 + 0 - 9
assert -2 ** 2 == -4 and (-2) ** 2 == 4 and 2 * 2 - 3 - 1 == 0
assert 2 * 1 + 3 / 3 == 3 and str(1 + 2) + str(3 * 1) == "33"
assert not (2 + 1 > 3 * 1) and "a" + "b" > "a"

assert not ((True or False) and None)
assert True or False and None

# old bug
assert (((1 or False) and True) or False) == True
assert (((None and True) or False) and True) == False

a, b = 1, None
assert -(1 or 2) == -1 and (1 and 2) + (-1.25 or -4) == 0.75
x = ((b or a) + 1 == 2 and (10 or a) + 1 == 11)
assert x
x = (((2 < 3) or 1) == True and (2 < 3 and 4) == 4)
assert x

x, y = 1, 2
assert ((x > y) and x or y == 2)
x, y = 2, 1
assert ((x > y) and x or y == 2)

assert 1234567890 == int("1234567890") and 1234567890 + 1 == 1234567891


# silly loops
while False: pass
while None: pass

# a name first assigned in an inner function is local to it
def silly():
    a = None
    def f(x):
        x = {"a": 1}
        x = {"x": 1}
        x = {"G": 1}
silly()

def f(i):
    if not isinstance(i, int): return i, "jojo"
    if i > 0: return i, f(i - 1)
    return None

assert f(3)[0] == 3 and f(3)[1][0] == 2
x = [f("alo"), f("xixi"), None]
assert x[0] == ("alo", "jojo") and x[1] == ("xixi", "jojo") and x[2] is None
x = [f("alo")[0] + "xixi"]
assert x[0] == "aloxixi"
x = [f({})]
assert x[0][1] == "jojo" and type(x[0][0]) == dict


def f(i):
    if i < 10: return "a"
    elif i < 20: return "b"
    elif i < 30: return "c"

assert f(3) == "a" and f(12) == "b" and f(26) == "c" and f(100) is None

# Nested loops, the inner variable does not disturb the outer one.
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
assert a == n * (n + 1) / 2 and i == 3
assert t[1] and t[n] and not (0 in t) and not ((n + 1) in t)

def f(b):
    x = 1
    while True:
        a = None
        if b == 1:
            x = 10
            break
        elif b == 2:
            x = 20
            break
        elif b == 3:
            x = 30
        else:
            x += 1
        if x >= 12: break
    return x

assert f(1) == 10 and f(2) == 20 and f(3) == 30 and f(4) == 12


def f(i):
    if i < 10: return "a"
    elif i < 20: return "b"
    elif i < 30: return "c"
    else: return 8

assert f(3) == "a" and f(12) == "b" and f(26) == "c" and f(100) == 8

a, b = None, 23
x = [f(100) * 2 + 3 or a, a or b + 2]
assert x[0] == 19 and x[1] == 25
x = {"f": 2 + 3 or a, "a": b + 2}
assert x["f"] == 5 and x["a"] == 25

a = {"y": 1}
x = [a["y"]]
assert x[0] == 1

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
assert a == 1 and b == 2 and c == 3
a, b, c = (f()[0], None, None)
assert a == 1 and b is None and c is None

a, b = (3 and f())[0], None
assert a == 1 and b is None

def g():
    f()
    return
assert g() is None
def g(): return None or f()
a, b, c = g()
assert a == 1 and b == 2 and c == 3

print("+")

assert 1 and (2 < 3) == True and 2 < 3 and ("a" < "b") == True
x = 2 < 3 and not 3
assert x == False
x = 2 < 1 or (2 > 1 and "a")
assert x == "a"

# ----------------------------------------------------------------
print("testing short-circuit optimizations")

GLOB1 = 1
GLOB2 = 2

# the values of the basic cases (Lua: source texts evaluated by load)
basiccases = [None, False, True, 10, (GLOB1 < GLOB2), (GLOB2 < GLOB1)]

def and_(a, b):
    if not a: return a
    else: return b

def or_(a, b):
    if a: return a
    else: return b

# every combination of two cases with `and` / `or`, compared with the reference
# functions: the operators must short-circuit exactly like them.
count = 0
for v1 in basiccases:
    for v2 in basiccases:
        assert (v1 and v2) == and_(v1, v2)
        assert (v1 or v2) == or_(v1, v2)
        count += 2
        for v3 in basiccases:
            assert ((v1 and v2) or v3) == or_(and_(v1, v2), v3)
            assert ((v1 or v2) and v3) == and_(or_(v1, v2), v3)
            count += 2
print("+")
assert count == 2 * 6 * 6 + 2 * 6 * 6 * 6

print("OK")
