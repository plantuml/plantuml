print("testing functions and calls")

# Types, functions as values, recursion, closures, multiple returns.
# Not ported (no Python equivalent in this subset): the Lua method syntax
# (def a:x), load, _G, _ENV, string.dump, rawget/rawset, table.sort with a
# comparator, and the Lua-only varargs forms. Recursion depths that Python cannot
# reach without tail call elimination are reduced (30000 becomes 300).

def pcall(fn, *args):
    try:
        return True, fn(*args)
    except Exception:
        return False, None

assert type(1 < 2).__name__ == "bool"
assert type(True).__name__ == "bool" and type(False).__name__ == "bool"
assert type(None).__name__ == "NoneType" and type(-3).__name__ == "int" and type("x").__name__ == "str" and \
    type({}).__name__ == "dict" and type(type).__name__ == "type"

assert type(len) == type(print)
f = None
def f(x): return x
assert type(f).__name__ == "function"


# testing local-function recursion
fact = False
def block1():
    res = 1
    def fact(n):
        if n == 0: return res
        else: return n * fact(n - 1)
    assert fact(5) == 120
block1()
assert fact == False

# testing declarations (methods become functions taking the table as first argument)
a = {"i": 10}
self_value = 20
def ax(self, x): return x + self["i"]
def ay(x): return x + self_value

assert ax(a, 1) + 10 == ay(1)

a["t"] = {"i": -100}
def tx(self, a, b): return self["i"] + a + b
a["t"]["x"] = tx

assert a["t"]["x"](a["t"], 2, 3) == -95

def block2():
    a = {"x": 0}
    def add(self, x):
        self["x"], self["y"] = self["x"] + x, 20
        return self
    assert add(add(add(a, 10), 20), 30)["x"] == 60 and a["y"] == 20
block2()

a = {"b": {"c": {}}}
def f1(x): return x + 1
def f2(self, x, y): self[x] = y
a["b"]["c"]["f1"] = f1
a["b"]["c"]["f2"] = f2
assert a["b"]["c"]["f1"](4) == 5
a["b"]["c"]["f2"](a["b"]["c"], "k", 12)
assert a["b"]["c"]["k"] == 12

print("+")

t = None  # 'declare' t
def f(a=None, b=None, c=None, *_):
    global t
    d = "a"
    t = [a, b, c, d]

f(1, 2)
assert t[0] == 1 and t[1] == 2 and t[2] is None and t[3] == "a"
f(1, 2, 3, 4)
assert t[0] == 1 and t[1] == 2 and t[2] == 3 and t[3] == "a"

def fat(x):
    if x <= 1: return 1
    else: return x * fat(x - 1)

assert fat(6) == 720
a, b = fat(5), 3
assert a == 120 and b == 3
print("+")

def err_on_n(n):
    if n == 0:
        raise Exception("error")
    else:
        err_on_n(n - 1)

def dummy(n):
    if n > 0:
        assert not pcall(err_on_n, n)[0]
        dummy(n - 1)

dummy(10)

def deep(n):
    if n > 0: deep(n - 1)
deep(10)
deep(200)

# testing (what Python can do of) the tail call
def deep(n):
    if n > 0: return deep(n - 1)
    else: return 101
assert deep(300) == 101
a = {}
def deep_method(self, n):
    if n > 0: return deep_method(self, n - 1)
    else: return 101
assert deep_method(a, 300) == 101

print("+")


a = None
def setter(x):
    global a
    a = x
setter(23)
assert a == 23 and (lambda x: x * 2)(20) == 40


# testing closures

# fixed-point operator
def Z(le):
    def a(f):
        return le(lambda x: f(f)(x))
    return a(a)


# non-recursive factorial

def F(f):
    def step(n):
        if n == 0: return 1
        else: return n * f(n - 1)
    return step

fat = Z(F)

assert fat(0) == 1 and fat(4) == 24 and Z(F)(5) == 5 * Z(F)(4)

def g(z):
    def f(a, b, c, d):
        return lambda x, y: a + b + c + d + a + x + y + z
    return f(z, z + 1, z + 2, z + 3)

f = g(10)
assert f(9, 16) == 10 + 11 + 12 + 13 + 10 + 9 + 16 + 10

Z, F, f = None, None, None
print("+")

# testing multiple returns

def unlpack(t, i=0):
    # all the values of t from index i on, as a tuple (Lua: multiple results)
    return tuple(t[i:])

def equaltab(t1, t2):
    assert len(t1) == len(t2)
    for i in range(len(t1)):
        assert t1[i] == t2[i]

def pack(*args): return list(args)

def f(): return 1, 2, 30, 4
def ret2(a=None, b=None, *_): return a, b

a, b, c, d = (unlpack([1, 2, 3]) + (None, None))[:4]
assert a == 1 and b == 2 and c == 3 and d is None
a = [1, 2, 3, 4, False, 10, "alo", False, print]
equaltab(pack(*unlpack(a)), a)
equaltab(pack(unlpack(a)[0], -1), [1, -1])
# only the first result of ret2(f()) in the middle of a list, all results at the end
a, b, c, d = ((ret2(*f())[0],) + ret2(*f()) + (None, None))[:4]
assert a == 1 and b == 1 and c == 2 and d is None
a, b, c, d = ((ret2(*f())[0], ret2(*f())[0]) + (None, None))[:4]
assert a == 1 and b == 1 and c is None and d is None

a = [unlpack([1, 2, 3])[0], unlpack([3, 2, 1])[0], "a", "b"]
assert a[0] == 1 and a[1] == 3 and a[2] == "a" and a[3] == "b"


# calls with 'extra' arguments
def ignores_extra(a=None, b=None, *_): return a
assert ignores_extra(1, 2, 3) == 1


# test for bug in parameter adjustment
def returns_local(*_):
    a = None
    return a
assert (lambda *_: None)(4) is None
assert returns_local(4) is None
assert (lambda a=None: a)() is None

print("OK")
