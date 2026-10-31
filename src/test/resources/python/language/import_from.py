# from module import name: the names of a module bound in the current scope.

from math import sqrt
print(sqrt(16), sqrt(2) == 2 ** 0.5)

from math import sqrt as racine
print(racine(9), racine is sqrt)

from math import floor, ceil as plafond, pi
print(floor(2.5), plafond(2.5), round(pi, 5))

from math import (
    fabs,
    gcd as pgcd,
)
print(fabs(-3), pgcd(12, 18))

# the module itself is not bound
try:
    math
except NameError as e:
    print("NameError:", e)

# the same object as through the module
import math
print(math.sqrt is sqrt, math.pi == pi)

# other modules
from json import dumps, loads
print(dumps(loads('{"a": [1, 2]}')))
from datetime import date
print(date(2024, 2, 29).isoformat())

# in a function: the names are local to it
def hypot(x, y):
    from math import sqrt as root
    return root(x * x + y * y)
print(hypot(3, 4))
try:
    root
except NameError as e:
    print("NameError:", e)

# a name can be bound again, as any other
def sqrt(x):
    return "mine"
print(sqrt(4), racine(4))

# global and nonlocal names
def bind_global():
    global gg
    from math import e as gg
bind_global()
print(round(gg, 3))

def outer():
    v = None
    def inner():
        nonlocal v
        from math import tau as v
    inner()
    return round(v, 3)
print(outer())

# errors: a name the module does not have, a module that does not exist
try:
    from math import nothing
except ImportError as e:
    print(type(e).__name__, str(e).startswith("cannot import name 'nothing' from 'math'"))
try:
    from math import sqrt, nothing
except ImportError as e:
    print(type(e).__name__)
try:
    from nomodule import x
except ImportError as e:
    print(type(e).__name__, e)
try:
    from nomodule import x
except ModuleNotFoundError as e:
    print("caught as ModuleNotFoundError")
