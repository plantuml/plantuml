print("testing (parts of) table library")

# Slices (Lua: table.unpack), packing, sorting. The Lua original relied on
# unpack ranges beyond 2^31, on invalid order functions, on math.random, os.clock
# and metatables: those are not ported (Python's sort never raises for an
# inconsistent comparator). Random data is replaced by a deterministic sequence.

print("testing unpack")

lim = 2000
a = [i for i in range(1, lim + 1)]
assert a[-1] == lim and len(a) == lim
x = a[0]
assert x == 1
x = list(a)
assert len(x) == lim and x[0] == 1 and x[lim - 1] == lim
x = a[lim - 3:]
assert len(x) == 3 and x[0] == lim - 2 and x[2] == lim
x = a[5:0]
assert len(x) == 0   # no elements
x = a[10:10]
assert len(x) == 0   # no elements
x = a[9]
y = None
assert x == 10 and y is None
x = [1][0]
y = [1][1:2]
assert x == 1 and len(y) == 0

print("testing pack")

a = ()
assert len(a) == 0

a = (print,)
assert a[0] == print and len(a) == 1

a = (None, None, None, None)
assert a[0] is None and len(a) == 4


print("testing sort")


def check(a, f=None):
    if f is None: f = (lambda x, y: x < y)
    for n in range(len(a) - 1, 0, -1):
        assert not f(a[n], a[n - 1])

a = ["Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep",
     "Oct", "Nov", "Dec"]

a.sort()
check(a)

def perm(s, n=None):
    if n is None: n = len(s)
    if n == 1:
        t = list(s)
        t.sort()
        check(t)
    else:
        for i in range(n):
            s[i], s[n - 1] = s[n - 1], s[i]
            perm(s, n - 1)
            s[i], s[n - 1] = s[n - 1], s[i]

perm([])
perm([1])
perm([1, 2])
perm([1, 2, 3])
perm([1, 2, 3, 4])
perm([2, 2, 3, 4])
perm([1, 2, 3, 4, 5])
perm([1, 2, 3, 3, 5])
perm([1, 2, 3, 4, 5, 6])
perm([2, 2, 3, 3, 5, 6])

limit = 30000

a = [(i * 7919) % 30011 for i in range(limit)]
a.sort()
print("Sorting", limit, "elements")
check(a)

a.sort()
print("Re-sorting", limit, "elements")
check(a)

a = [(i * 7919) % 30011 for i in range(limit)]
a.sort(reverse=True)
print("Inverted sort of", limit, "elements")
check(a, lambda x, y: y < x)


[].sort()  # empty array

a = [False] * limit
print("Sorting", limit, "equal elements")
check(a, lambda x, y: None)
for v in a: assert not v

A = ["\xe1lo", "\x00first :-)", "alo", "then this one", "45", "and a new"]
A.sort()
check(A)

# records sorted by one of their fields (Lua: a metatable __lt)
items = [(i * 37) % 101 for i in range(1, 11)]
items.sort()
check(items)

print("OK")
