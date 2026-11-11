# Binding of the arguments: defaults, keyword-only parameters, *args, **kwargs,
# and the calls with * and ** (CPython is the oracle for the order and the values).

def show(*args, **kwargs):
    print(args, sorted(kwargs.items()))

# defaults are evaluated once, when the function is defined
calls = []
def default():
    calls.append(1)
    return len(calls)

def f(a, b=default(), c=[]):
    c.append(a)
    return a, b, c

print(f(1), f(2), f(3, 9), len(calls))

# keyword-only parameters
def kw(a, *, b, c=3):
    return a, b, c

print(kw(1, b=2), kw(1, c=4, b=5), kw(b=1, a=0))

def kw2(*args, key=None, reverse=False):
    return args, key, reverse

print(kw2(), kw2(1, 2), kw2(1, key="k"), kw2(reverse=True, key=1))

def errors(f, *a, **k):
    try:
        f(*a, **k)
    except TypeError:
        return "TypeError"
    return "ok"

print(errors(kw, 1), errors(kw, 1, 2), errors(kw, 1, b=2, d=4), errors(kw, 1, b=2), errors(kw, a=1, b=2))
print(errors(kw2, 1, 2, 3, key=1), errors(kw2, 1, reverse=1, z=2))

# *args and **kwargs in the definition
def g(a, b=2, *rest, **opts):
    return a, b, rest, sorted(opts.items())

print(g(1), g(1, 2, 3, 4), g(1, x=5), g(1, 2, 3, y=6, x=5), g(b=7, a=8))
print(errors(g), errors(g, 1, a=2), errors(g, 1, 2, b=3))

# calls with * and **
show(*[1, 2], *(3,), *"ab", *range(2))
show(**{"a": 1}, **{"b": 2})
show(1, *[2], x=3, **{"y": 4})
show(*[], **{})
args = [1, 2]
kwargs = {"z": 26}
show(*args, 3, *args, w=0, **kwargs)
print(errors(show, **{"a": 1}), errors(g, *[1], **{"a": 2}))

# the same keyword given twice by ** is an error, a * or ** argument is copied
d = {"k": 1}
def mutate(**kw):
    kw["new"] = 1
    return kw

print(mutate(**d), d)
lst = [1, 2]
def take(*a):
    return a

t = take(*lst)
lst.append(3)
print(t, lst)

# positional arguments by name, in any order
def h(a, b, c):
    return a * 100 + b * 10 + c

print(h(c=3, a=1, b=2), h(1, c=3, b=2), h(*[1, 2], c=3), h(**{"c": 3, "b": 2, "a": 1}))

# nested functions and closures with defaults
def make(n, step=1, *, scale=10):
    def inner(x=n, *more, y=step):
        return (x + sum(more)) * scale + y
    return inner

inner = make(5, 2, scale=3)
print(inner(), inner(1), inner(1, 2, 3), inner(y=0), inner(1, y=9))

# lambdas
lam = lambda a, b=2, *c, d=4, **e: (a, b, c, d, sorted(e.items()))
print(lam(1), lam(1, 2, 3, d=5, z=6))

# methods
class K:
    def m(self, a, b=1, *, c=2):
        return (a, b, c)

k = K()
print(k.m(0), k.m(0, 5, c=6), K.m(k, 1), errors(k.m), errors(k.m, 1, 2, 3))
