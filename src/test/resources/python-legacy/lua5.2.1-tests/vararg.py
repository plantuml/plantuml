print("testing vararg")

# Variable number of arguments: *args, starred calls and returns, select-like slices.
# (The Lua original used tables with an "n" field for the varargs: here the
# varargs are tuples, and len() gives their count.)

def f(a=None, *args):
    n = len(args)
    for i in range(n):
        assert a[i] == args[i]
    return n

assert f() == 0
assert f([1, 2, 3], 1, 2, 3) == 3
assert f(["alo", None, 45, f, None], "alo", None, 45, f, None) == 5

def c12(*args):
    res = (len(args) == 2 and args[0] == 1 and args[1] == 2)
    if res:
        res = 55
    return res, 2

def vararg(*args):
    return args

def call(fn, args):
    return fn(*args)

assert c12(1, 2)[0] == 55
a, b = call(c12, [1, 2])
assert a == 55 and b == 2
a, b = call(c12, [1])
assert not a
assert c12(1, 2, 3)[0] == False
a = vararg(*divmod(7, 2))
assert len(a) == 2 and a[0] == 3 and a[1] == 1
a = vararg(*call(call, (c12, [1, 2])))
assert len(a) == 2 and a[0] == 55 and a[1] == 2
a = call(print, ["+"])
assert a is None

t = {1: 1, 2: 10}
def tf(self, *args):
    return self[args[0]] + len(args)
assert tf(t, 1, 4) == 3 and tf(t, 2) == 11
print("+")

lim = 20
a = {}
for i in range(1, lim + 1):
    a[i] = i + 0.3

def f(a, b, c, d, *args):
    more = list(args)
    assert a == 1.3 and more[0] == 5.3 and more[lim - 5] == lim + 0.3 and len(more) == lim - 4

def g(a, b, c, *_):
    assert a == 1.3 and b == 2.3 and c == 3.3

values = [a[i] for i in range(1, lim + 1)]
call(f, values)
call(g, values)

ints = [i for i in range(1, lim + 1)]
assert call(max, ints) == lim

print("+")


# new-style varargs
def oneless(a=None, *args): return args

def f(n, a=None, *args):
    if n == 0:
        b, c, d = (list(args) + [None, None, None])[:3]
        return (a, b, c, d) + oneless(*oneless(*oneless(*args)))
    else:
        n, b, a = n - 1, args, a
        assert b == args
        return f(n, a, *args)

result = f(10, 5, 4, 3, 2, 1)
a, b, c, d, e = result
assert a == 5 and b == 4 and c == 3 and d == 2 and e == 1

result = (f(4) + (None, None, None, None, None))[:5]
a, b, c, d, e = result
assert a is None and b is None and c is None and d is None and e is None


# varargs of the main chunk: the first function receives its arguments as a tuple
def chunk(*argv):
    return list(argv)

x = chunk(2, 3)
assert x[0] == 2 and x[1] == 3 and len(x) == 2

def chunk2(*argv):
    x = list(argv)
    for i in range(len(argv)):
        assert x[i] == argv[i]
    return True

assert chunk2("a", "b", None, {}, print)
assert chunk2()

a = (10, 20, 30, 40)[2:]
assert len(a) == 2 and a[0] == 30 and a[1] == 40
a = ()
assert len(a) == 0
a = (3, 5, 7)[-1:]
assert a[0] == 7 and len(a) == 1
a = (3, 5, 7)[-2:]
assert a[0] == 5 and a[1] == 7 and len(a) == 2

print("OK")
