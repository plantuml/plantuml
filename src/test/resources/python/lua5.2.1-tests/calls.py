print("testing functions and calls")

# get the opportunity to test 'type' too ;)

assert(type(1 < 2) == "boolean")
assert(type(True) == "boolean" and type(False) == "boolean")
assert(type(None) == "nil" and type(-3) == "number" and type("x") == "string" and
       type({}) == "table" and type(type) == "function")

assert(type(assert) == type(print))
f = None
def f(x): return a:x(x)
assert(type(f) == "function")


# testing local-function recursion
# (the `do ... end` block of the original becomes a function: there are no block scopes)
fact = False
def block1():
    res = 1
    def fact(n):
        if n == 0: return res
        else: return n * fact(n - 1)
    assert(fact(5) == 120)
block1()
assert(fact == False)

# testing declarations
a = {"i": 10}
self = 20
def a:x(x): return x + self.i
def a.y(x): return x + self

assert(a:x(1) + 10 == a.y(1))

a.t = {"i": -100}
def tx(self, a, b): return self.i + a + b
a["t"].x = tx

assert(a.t:x(2, 3) == -95)

def block2():
    a = {"x": 0}
    def a:add(x):
        self.x, a.y = self.x + x, 20
        return self
    assert(a:add(10):add(20):add(30).x == 60 and a.y == 20)
block2()

a = {"b": {"c": {}}}

def a.b.c.f1(x): return x + 1
def a.b.c:f2(x, y): self[x] = y
assert(a.b.c.f1(4) == 5)
a.b.c:f2("k", 12)
assert(a.b.c.k == 12)

print("+")

t = None  # 'declare' t
def f(a, b, c):
    global t
    d = "a"
    t = [a, b, c, d]

f(      # this line change must be valid
  1, 2)
assert(t[1] == 1 and t[2] == 2 and t[3] == None and t[4] == "a")
f(1, 2,   # this one too
      3, 4)
assert(t[1] == 1 and t[2] == 2 and t[3] == 3 and t[4] == "a")

def fat(x):
    if x <= 1: return 1
    else: return x * load("return fat(" .. x - 1 .. ")")()

assert(load("load('assert(fat(6)==720)')()"))()
a = load("return fat(5), 3")
a, b = a()
assert(a == 120 and b == 3)
print("+")

def err_on_n(n):
    if n == 0:
        error()
        exit(1)
    else:
        err_on_n(n - 1)
        exit(1)

def dummy(n):
    if n > 0:
        assert(not pcall(err_on_n, n))
        dummy(n - 1)

dummy(10)

def deep(n):
    if n > 0: deep(n - 1)
deep(10)
deep(200)

# testing tail call
def deep(n):
    if n > 0: return deep(n - 1)
    else: return 101
assert(deep(30000) == 101)
a = {}
def a:deep(n):
    if n > 0: return self:deep(n - 1)
    else: return 101
assert(a:deep(30000) == 101)

print("+")


a = None
def setter(x):
    global a
    a = x
setter(23)
assert(a == 23 and (lambda x: x * 2)(20) == 40)


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

assert(fat(0) == 1 and fat(4) == 24 and Z(F)(5) == 5 * Z(F)(4))

def g(z):
    def f(a, b, c, d):
        return lambda x, y: a + b + c + d + a + x + y + z
    return f(z, z + 1, z + 2, z + 3)

f = g(10)
assert(f(9, 16) == 10 + 11 + 12 + 13 + 10 + 9 + 16 + 10)

Z, F, f = None, None, None
print("+")

# testing multiple returns

def unlpack(t, i):
    i = i or 1
    if i <= len(t):
        return t[i], unlpack(t, i + 1)

def equaltab(t1, t2):
    assert(len(t1) == len(t2))
    for i in range(1, len(t1) + 1):
        assert(t1[i] == t2[i])

def pack(*args): return (table.pack(*args))

def f(): return 1, 2, 30, 4
def ret2(a, b): return a, b

a, b, c, d = unlpack([1, 2, 3])
assert(a == 1 and b == 2 and c == 3 and d == None)
a = [1, 2, 3, 4, False, 10, "alo", False, assert]
equaltab(pack(unlpack(a)), a)
equaltab(pack(unlpack(a), -1), [1, -1])
a, b, c, d = ret2(f()), ret2(f())
assert(a == 1 and b == 1 and c == 2 and d == None)
a, b, c, d = unlpack(pack(ret2(f()), ret2(f())))
assert(a == 1 and b == 1 and c == 2 and d == None)
a, b, c, d = unlpack(pack(ret2(f()), (ret2(f()))))
assert(a == 1 and b == 1 and c == None and d == None)

a = ret2([unlpack([1, 2, 3]), unlpack([3, 2, 1]), unlpack(["a", "b"])])
assert(a[1] == 1 and a[2] == 3 and a[3] == "a" and a[4] == "b")


# testing calls with 'incorrect' arguments
rawget({}, "x", 1)
rawset({}, "x", 1, 2)
assert(math.sin(1, 2) == math.sin(1))
table.sort([10, 9, 8, 4, 19, 23, 0, 0], lambda a, b: a < b, "extra arg")


# (the generic `load` / `string.dump` / `debug.getupvalue` / `_ENV` tests of the
# original are not rewritten: those features are not part of this fork)


# test for bug in parameter adjustment
def returns_local():
    a = None
    return a
assert((lambda: None)(4) == None)
assert(returns_local(4) == None)
assert((lambda a: a)() == None)

print("OK")
return deep
