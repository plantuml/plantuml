# Closures, free variables, global and nonlocal.

# a counter shared by two closures
def make_pair():
    x = 5
    def inc():
        nonlocal x
        x += 1
        return x
    def dec():
        nonlocal x
        x -= 1
        return x
    return inc, dec

f1, g1 = make_pair()
assert f1() == 6
assert g1() == 5
f2, g2 = make_pair()
assert f2() == 6
assert f2() == 7
assert g2() == 6
# the two pairs are independent
assert g1() == 4
assert f1() == 5

# variables captured in the middle of the frame
def middle():
    x = 3
    y = 5
    z = 7
    def f(): return y
    def g(): return z
    def h(): return x
    def setter(x1, y1, z1):
        nonlocal x, y, z
        x = x1
        y = y1
        z = z1
    return f, g, h, setter

f, g, h, setter = middle()
assert (h(), f(), g()) == (3, 5, 7)
setter("x", "y", "z")
assert (h(), f(), g()) == ("x", "y", "z")

# a closure refers to a function defined after it
def nested():
    def g():
        return (x, f())
    x = 10
    def f():
        return 20
    return g()

assert nested() == (10, 20)

# mutual recursion between nested functions
def parity(n):
    def is_even(k):
        return True if k == 0 else is_odd(k - 1)
    def is_odd(k):
        return False if k == 0 else is_even(k - 1)
    return is_even(n), is_odd(n)

assert parity(10) == (True, False)
assert parity(7) == (False, True)

# closures created in a loop share the variable (late binding)
def late():
    fs = []
    for i in range(3):
        fs.append(lambda: i)
    return [f() for f in fs]

assert late() == [2, 2, 2]

# the usual fix: bind the value through a default or a factory
def bound():
    fs = []
    for i in range(3):
        fs.append(lambda i=i: i)
    return [f() for f in fs]

assert bound() == [0, 1, 2]

def factory(i):
    return lambda: i

assert [factory(i)() for i in range(3)] == [0, 1, 2]

# nonlocal across several levels
def outer():
    n = 0
    def middle():
        def inner():
            nonlocal n
            n += 10
            return n
        return inner
    return middle()

inner = outer()
assert inner() == 10
assert inner() == 20

# global
counter = 0

def bump():
    global counter
    counter += 1
    return counter

assert bump() == 1
assert bump() == 2
assert counter == 2

def shadow():
    counter = 100
    return counter

assert shadow() == 100
assert counter == 2

def create_global():
    global created_later
    created_later = "yes"

create_global()
assert created_later == "yes"

# reading a global that is assigned later at module level
def read_late():
    return late_global

late_global = "late"
assert read_late() == "late"

# a local assigned after a read is an error
def unbound():
    y = x_unbound
    x_unbound = 1
    return y

x_unbound = 5
try:
    unbound()
    assert False
except NameError:
    pass

# undefined names
def undefined():
    return no_such_name

try:
    undefined()
    assert False
except NameError:
    pass

# accumulator
def make_acc(total):
    def acc(n):
        nonlocal total
        total += n
        return total
    return acc

acc = make_acc(100)
assert acc(10) == 110
assert acc(10) == 120

# closures keep their variables alive after the function returned
def fib_memo():
    cache = {}
    def fib(n):
        if n < 2:
            return n
        if n not in cache:
            cache[n] = fib(n - 1) + fib(n - 2)
        return cache[n]
    return fib

assert fib_memo()(30) == 832040

# a lambda in a default argument sees the global
base = 10
def with_default(f=lambda x: x + base):
    return f(1)

assert with_default() == 11
base = 20
assert with_default() == 21
