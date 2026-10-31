# Indentation, continuation lines, comments, blank lines, semicolons.

# comment at the start
x = 1  # trailing comment

   # indented comment

y = 2


def f(a,
      b):
    # comment inside
    return (a +
            b)


def g():

    x = 1

    if x:

        y = 2
    # a comment at a lower level
    return x


s = 1 + \
    2
t = [1,
     2,
     3]
d = {
    "a": 1,
    "b": 2,
}
call = f(1,
         2)
a = 1; b = 2
a = 1; b = 2;
if a: b = 1
if a: b = 1; c = 2
if a: pass
else: pass
for i in x: pass
while a: break
def h(): return 1
def k(): pass; pass
if a:
    if b:
        if c:
            d
        else:
            e
    else:
        g
x = [
    1, 2,
    3,
]
nested = f(g(h(
    1
)))
text = ("abc"
        "def"
        'ghi')
if (a and
        b):
    pass
long = (1 +
        2 +
        3)
try:
    pass
except E:
    pass
