# Expressions and precedence.
a or b
a or b or c
a and b and c
a or b and c
a and b or c
not a
not not a
not a and b
a < b
a < b < c
a == b != c
a is b
a is not b
a in b
a not in b
a < b is c not in d
x if a else y
x if a else y if b else z
(x if a else y) if b else z
a or b if c else d
lambda: 1
lambda x: x
lambda x, y=2, *a, k=3, **kw: x
lambda: lambda: 1
f(lambda x: x, key=lambda y: y)
a | b ^ c & d
a << b >> c
a + b - c
a * b / c // d % e
-a
+a
~a
- -a
-a ** b
a ** -b
a ** b ** c
(a ** b) ** c
a + b * c ** d
-a * b
not a == b
a if not b else c
f()
f(a)
f(a, b)
f(a, b=1)
f(*a)
f(*a, *b)
f(**k)
f(a, *b, c=1, **k)
f(a)(b)(c)
f(a,)
a.b
a.b.c
a.b()
a.b(c).d
a[0]
a[0][1]
a[b, c]
a[b, c,]
a[1:2]
a[:2]
a[1:]
a[:]
a[::2]
a[1:2:3]
a[::-1]
a[1:2, 3]
a[-1]
a[b:c][d]
f(x)[0].y(z)
()
(1,)
(1, 2)
(1, 2,)
((1, 2), (3,))
(a)
((a))
[]
[1]
[1, 2,]
[[1], [2, 3]]
{}
{1: 2}
{1: 2, 3: 4,}
{a: b, c: d}
{"k": [1, {"n": None}]}
[x for x in y]
[x for x in y if z]
[x for x in y if z if w]
[x for x in y for z in w]
[x for x, y in z]
[x for (x, y), z in w]
[x for x in y if a or b]
[x if a else b for x in y]
[(x, y) for x in a for y in b if x != y]
[[y for y in x] for x in z]
{k: v for k, v in x}
{k: v for k in x if k}
{k: [v for v in w] for k in x for w in y}
[lambda: x for x in y]
None
True
False
a == None
x = y if z else w
