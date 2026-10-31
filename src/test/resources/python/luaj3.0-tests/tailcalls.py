
# tostring replacement that assigns ids
ts, id, nid, types = tostring, {}, 0, {"table": "tbl", "thread": "thr", "userdata": "uda", "function": "func"}

def tostring(x):
    global nid
    if not x or not types[type(x)]:
        return ts(x)
    if not id[x]:
        nid += 1
        id[x] = types[type(x)] .. "." .. nid
    return id[x]


def nothing():
    pass

def prints_b():
    print("b")

def a():
    return pcall(nothing)

def b():
    return pcall(prints_b)

def c():
    return pcall(lambda: "c")

print(pcall(a))
print(pcall(b))
print(pcall(c))

def sum(*args):
    s = 0
    for i, v in ipairs([*args]):
        s += v
    return s

def f1(n, a, b, c):
    a = a or 0
    b = b or 0
    c = c or 0
    if n <= 0:
        return a, a + b, a + b + c
    return f1(n - 1, a, a + b, a + b + c)

def f2(n, *args):
    if n <= 0:
        print(" --f2, n<=0, returning sum(...)", *args)
        return sum(*args)
    print(" --f2, n>0, returning f2(n-1,n,...)", n - 1, n, *args)
    return f2(n - 1, n, *args)

def f3(n, *args):
    if n <= 0:
        return sum(*args)
    print("    f3,n-1,n,...", f3, n - 1, n, *args)
    return pcall(f3, n - 1, n, *args)

def all(f):
    for n in range(0, 4):
        t = []
        for m in range(1, 6):
            print("--f, n, table.unpack(t)", f, n, table.unpack(t))
            print(pcall(f, n, table.unpack(t)))
            t[m] = m

all(f1)
all(f2)
all(f3)


def f(x):
    # tailcall to a builtin
    return math.abs(x)

def factorial(i):
    def helper(product, n):
        if n <= 0:
            return product
        else:
            # tail call to a nested Lua function
            return helper(n * product, n - 1)
    return helper(1, i)

result1 = factorial(5)
print(result1)
print(factorial(5))

result2 = f(-1234)
print(result2)

def fib_bad(n):
    def helper(i, a, b):
        if i >= n:
            return a
        else:
            # not recognized by luac as a tailcall!
            result = helper(i + 1, b, a + b)
            return result
    return helper(1, 1, 1)

def fib_good(n):
    def helper(i, a, b):
        if i >= n:
            return a
        else:
            # must be a tail call!
            return helper(i + 1, b, a + b)
    return helper(1, 1, 1)

aliases = {
    "1.#INF": "inf",
    "-1.#INF": "-inf",
    "1.#IND": "nan",
    "-1.#IND": "nan",
}

def p(s, e):
    print(s, e and aliases[tostring(e)] or e)

p(pcall(fib_bad, 30))
#p((pcall(fib_bad, 25000)))
p(pcall(fib_good, 30))
p(pcall(fib_good, 25000))

def fib_all(n, i, a, b):
    i = i or 1
    a = a or 1
    b = b or 1
    if i >= n:
        return
    else:
        return a, fib_all(n, i + 1, b, a + b)

print(fib_all(10))
