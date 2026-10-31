# Calls in return position, deep recursion through helpers, calls to builtins.
# (Python has no tail call elimination: the Lua original's 25000-level recursion
# is replaced by 300 levels, which fits both CPython's and the interpreter's depth.)

def pcall(fn, *args):
    try:
        return True, fn(*args)
    except Exception as e:
        return False, type(e).__name__


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

def total(*args):
    s = 0
    for v in args:
        s += v
    return s

def f1(n, a=0, b=0, c=0):
    a = a or 0
    b = b or 0
    c = c or 0
    if n <= 0:
        return a, a + b, a + b + c
    return f1(n - 1, a, a + b, a + b + c)

def f2(n, *args):
    if n <= 0:
        print(" --f2, n<=0, returning sum(...)", *args)
        return total(*args)
    print(" --f2, n>0, returning f2(n-1,n,...)", n - 1, n, *args)
    return f2(n - 1, n, *args)

def f3(n, *args):
    if n <= 0:
        return total(*args)
    print("    f3,n-1,n,...", n - 1, n, *args)
    return pcall(f3, n - 1, n, *args)

def all_cases(f):
    for n in range(0, 4):
        t = []
        for m in range(1, 6):
            print("--f, n, unpacked", f.__name__, n, *t)
            print(pcall(f, n, *t))
            t.append(m)

all_cases(f1)
all_cases(f2)
all_cases(f3)


def f(x):
    # call to a builtin in return position
    return abs(x)

def factorial(i):
    def helper(product, n):
        if n <= 0:
            return product
        else:
            return helper(n * product, n - 1)
    return helper(1, i)

result1 = factorial(5)
print(result1)
print(factorial(5))

result2 = f(-1234)
print(result2)

def fib_good(n):
    def helper(i, a, b):
        if i >= n:
            return a
        else:
            return helper(i + 1, b, a + b)
    return helper(1, 1, 1)

aliases = {
    "inf": "inf",
    "-inf": "-inf",
    "nan": "nan",
}

def p(label, e):
    print(label, e)

print(pcall(fib_good, 30))
print(pcall(fib_good, 300))

def fib_all(n, i=None, a=None, b=None):
    i = i or 1
    a = a or 1
    b = b or 1
    if i >= n:
        return
    else:
        return a, fib_all(n, i + 1, b, a + b)

print(fib_all(10))
