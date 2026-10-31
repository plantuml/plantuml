# Definitions, calls, arity, defaults, keyword arguments, returns.

def f0(): return "f0"
def f1(a): return ("f1", a)
def f2(a, b): return ("f2", a, b)
def f3(a, b, c): return ("f3", a, b, c)
def f4(a, b, c, d): return ("f4", a, b, c, d)

assert f0() == "f0"
assert f1("a") == ("f1", "a")
assert f2("a", "b") == ("f2", "a", "b")
assert f3(1, 2, 3) == ("f3", 1, 2, 3)
assert f4(1, 2, 3, 4) == ("f4", 1, 2, 3, 4)

def raises(exc, f, *args, **kwargs):
    try:
        f(*args, **kwargs)
    except exc:
        return True
    return False

# arity is checked
assert raises(TypeError, f0, 1)
assert raises(TypeError, f1)
assert raises(TypeError, f1, 1, 2)
assert raises(TypeError, f2, 1)
assert raises(TypeError, f4, 1, 2, 3)
assert raises(TypeError, f4, 1, 2, 3, 4, 5)

# returns
def g0(a, b, c, d): return
def g1(a, b, c, d): return d
def g2(a, b, c, d): return c, d
def g4(a, b, c, d): return a, b, c, d

assert g0(1, 2, 3, 4) is None
assert g1(1, 2, 3, 4) == 4
assert g2(1, 2, 3, 4) == (3, 4)
assert g4(1, 2, 3, 4) == (1, 2, 3, 4)
a, b = g2(1, 2, 3, 4)
assert (a, b) == (3, 4)
a, b, c, d = g4("p", "q", "r", "s")
assert (a, b, c, d) == ("p", "q", "r", "s")

def unpack3():
    x, y, z = g2(1, 2, 3, 4)

assert raises(ValueError, unpack3)

def falls_off():
    x = 1

assert falls_off() is None

def early(n):
    if n < 0:
        return "negative"
    for i in range(10):
        if i == n:
            return "found"
    return "missing"

assert early(-1) == "negative"
assert early(3) == "found"
assert early(30) == "missing"

# nested calls
def func(a, b, c):
    return a, b, c

assert func(11, 12, 13) == (11, 12, 13)
assert func(func(32, 33, 34), func(45, 46, 47), func(58, 59, 50)) == ((32, 33, 34), (45, 46, 47), (58, 59, 50))

# default values
def d1(a, b=10, c=20):
    return a, b, c

assert d1(1) == (1, 10, 20)
assert d1(1, 2) == (1, 2, 20)
assert d1(1, 2, 3) == (1, 2, 3)
assert d1(1, c=30) == (1, 10, 30)
assert d1(c=3, a=1, b=2) == (1, 2, 3)
assert raises(TypeError, d1)
assert raises(TypeError, d1, 1, 2, 3, 4)
assert raises(TypeError, d1, 1, a=2)
assert raises(TypeError, d1, 1, z=2)

# defaults are evaluated once, when the function is defined
counter = [0]

def next_default():
    counter[0] += 1
    return counter[0]

def d2(x=next_default()):
    return x

assert d2() == 1
assert d2() == 1
assert d2(5) == 5
assert counter[0] == 1

# a mutable default is shared between calls
def d3(item, acc=[]):
    acc.append(item)
    return acc

assert d3(1) == [1]
assert d3(2) == [1, 2]
assert d3(3, []) == [3]

# *args and **kwargs
def v1(*args):
    return args

assert v1() == ()
assert v1(1) == (1,)
assert v1(1, 2, 3) == (1, 2, 3)

def v2(a, *args):
    return a, args

assert v2(1) == (1, ())
assert v2(1, 2, 3) == (1, (2, 3))
assert raises(TypeError, v2)

def v3(**kwargs):
    return kwargs

assert v3() == {}
assert v3(a=1, b=2) == {"a": 1, "b": 2}

def v4(a, b=2, *args, **kwargs):
    return a, b, args, kwargs

assert v4(1) == (1, 2, (), {})
assert v4(1, 3, 4, 5, x=6) == (1, 3, (4, 5), {"x": 6})
assert v4(a=1, y=2) == (1, 2, (), {"y": 2})

# unpacking in calls
args = [1, 2, 3]
assert f3(*args) == ("f3", 1, 2, 3)
assert f4(0, *args) == ("f4", 0, 1, 2, 3)
assert f4(*args, 4) == ("f4", 1, 2, 3, 4)
assert f3(*(7, 8), 9) == ("f3", 7, 8, 9)
assert d1(**{"a": 1, "c": 3}) == (1, 10, 3)
assert d1(*[1, 2], **{"c": 3}) == (1, 2, 3)
assert v1(*[], *()) == ()
assert v1(*"ab", *[1]) == ("a", "b", 1)

# keyword-only parameters
def k1(a, *, b, c=3):
    return a, b, c

assert k1(1, b=2) == (1, 2, 3)
assert k1(1, c=5, b=2) == (1, 2, 5)
assert raises(TypeError, k1, 1)
assert raises(TypeError, k1, 1, 2)

# lambda
sq = lambda x: x * x
assert sq(7) == 49
assert (lambda: 42)() == 42
assert (lambda a, b=2, *c, **d: (a, b, c, d))(1) == (1, 2, (), {})
assert (lambda x: lambda y: x + y)(1)(2) == 3

# functions are values
def apply(f, *args):
    return f(*args)

assert apply(f2, 1, 2) == ("f2", 1, 2)
assert apply(len, [1, 2, 3]) == 3
table = {"f1": f1, "f2": f2}
assert table["f2"](1, 2) == ("f2", 1, 2)
handlers = [f0, lambda: "other"]
assert [h() for h in handlers] == ["f0", "other"]

# printing
def p(a, *args):
    print("a", a)
    print("...", *args)
    print("...,a", *args, a)
    print("a,...", a, *args)

p("q")
p("q", "r")
p("q", "r", "s")
print("end", sep="")
print("a", "b", sep="-")
print("a", "b", end="!\n")
print()
print(None, True, False, 1, 2.5, "s", [1, "a"], (1,), {"k": None})
