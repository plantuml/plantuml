# Every statement of the subset.
pass
x = 1
x = y = z = 2
a, b = b, a
(a, b), c = (1, 2), 3
[a, b] = 1, 2
a[0], b.c = 1, 2
x += 1
x -= 1
x *= 2
x /= 2
x //= 2
x %= 2
x **= 2
x <<= 1
x >>= 1
x &= 1
x |= 1
x ^= 1
a[i] += 1
a.b += 1
t = 1, 2, 3
u = 1,
v = ()
del x
del a[0], b
del (c, d)
del a[1:2]
assert x
assert x, "message"
global g1, g2
import math
import os.path as p, sys
f(1)
f(1)(2)(3)
if a:
    pass
if a:
    b
else:
    c
if a:
    b
elif c:
    d
elif e:
    f
else:
    g
while a:
    break
else:
    continue
for i in range(3):
    pass
for a, b in x:
    pass
for (a, b), c in x:
    pass
for i in 1, 2, 3:
    pass
else:
    pass
try:
    a
except:
    b
try:
    a
except E:
    b
except (E1, E2) as e:
    c
except E3 as e3:
    d
else:
    e
def f():
    return
def f(a, b):
    return a, b
def f(a, b=1, *args, c, d=2, **kwargs):
    return
def f(*, a, b=1):
    pass
def f(*args):
    pass
def f(**kwargs):
    pass
def f(a=1, b=2):
    global q
    q = 1
    def inner():
        nonlocal a
        a = 2
raise
raise E
raise E("x")
raise E from None
return_value = 3
