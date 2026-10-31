print("testing vararg")

_G.arg = None

def f(a, *args):
    arg = {"n": select("#", *args), *args}
    for i in range(1, arg.n + 1): assert(a[i] == arg[i])
    return arg.n

def c12(*args):
    assert(arg == None)
    x = [*args]
    x.n = len(x)
    res = (x.n == 2 and x[1] == 1 and x[2] == 2)
    if res: res = 55
    return res, 2

def vararg(*args): return {"n": select("#", *args), *args}

def call(f, args): return f(table.unpack(args, 1, args.n))

assert(f() == 0)
assert(f([1, 2, 3], 1, 2, 3) == 3)
assert(f(["alo", None, 45, f, None], "alo", None, 45, f, None) == 5)

assert(c12(1, 2) == 55)
a, b = assert(call(c12, [1, 2]))
assert(a == 55 and b == 2)
a = call(c12, {1, 2, "n": 2})
assert(a == 55 and b == 2)
a = call(c12, {1, 2, "n": 1})
assert(not a)
assert(c12(1, 2, 3) == False)
a = vararg(call(next, {_G, None, "n": 2}))
b, c = next(_G)
assert(a[1] == b and a[2] == c and a.n == 2)
a = vararg(call(call, {c12, [1, 2]}))
assert(a.n == 2 and a[1] == 55 and a[2] == 2)
a = call(print, ["+"])
assert(a == None)

t = [1, 10]
def t:f(*args):
    arg = [*args]
    return self[(*args)] + len(arg)
assert(t:f(1, 4) == 3 and t:f(2) == 11)
print("+")

lim = 20
i, a = 1, {}
while i <= lim:
    a[i] = i + 0.3
    i += 1

def f(a, b, c, d, *args):
    more = [*args]
    assert(a == 1.3 and more[1] == 5.3 and
           more[lim - 4] == lim + 0.3 and not more[lim - 3])

def g(a, b, c):
    assert(a == 1.3 and b == 2.3 and c == 3.3)

call(f, a)
call(g, a)

a = {}
i = 1
while i <= lim:
    a[i] = i
    i += 1
assert(call(math.max, a) == lim)

print("+")


# new-style varargs

def oneless(a, *args): return *args

def f(n, a, *args):
    b = None
    assert(arg == None)
    if n == 0:
        b, c, d = *args
        return a, b, c, d, oneless(oneless(oneless(*args)))
    else:
        n, b, a = n - 1, *args, a
        assert(b == (*args))
        return f(n, a, *args)

a, b, c, d, e = assert(f(10, 5, 4, 3, 2, 1))
assert(a == 5 and b == 4 and c == 3 and d == 2 and e == 1)

a, b, c, d, e = f(4)
assert(a == None and b == None and c == None and d == None and e == None)


# varargs for main chunks (the chunk's `...` is `*argv`)
f = load("return [*argv]")
x = f(2, 3)
assert(x[1] == 2 and x[2] == 3 and x[3] == None)


f = load("""
x = [*argv]
for i in range(1, select("#", *argv) + 1): assert(x[i] == select(i, *argv))
assert(x[select("#", *argv) + 1] == None)
return True
""")

assert(f("a", "b", None, {}, assert))
assert(f())

a = [select(3, table.unpack([10, 20, 30, 40]))]
assert(len(a) == 2 and a[1] == 30 and a[2] == 40)
a = [select(1)]
assert(next(a) == None)
a = [select(-1, 3, 5, 7)]
assert(a[1] == 7 and a[2] == None)
a = [select(-2, 3, 5, 7)]
assert(a[1] == 5 and a[2] == 7 and a[3] == None)
pcall(select, 10000)
pcall(select, -10000)

print("OK")
