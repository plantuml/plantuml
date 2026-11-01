import math

print("testing tables, iteration, and for")

# (The Lua original tested the border of tables with holes, next() on _G, the
# __pairs/__ipairs/__len metamethods and generic for with custom iterators. Python
# dicts and lists have no holes and no metamethods: the same questions are asked
# of dict and list operations, with the results Python defines.)

a = {}

# make sure the dict has many keys, then remove them all
for i in range(1, 101): a[str(i) + "+"] = True
for i in range(1, 101): del a[str(i) + "+"]
assert len(a) == 0
# fill with integer keys and check the size after each insertion
for i in range(1, 101):
    a[i] = True
    assert len(a) == i

# testing enumerate (Lua: ipairs)
x = 0
for k, v in enumerate([10, 20, 30], 1):
    x += 1
    assert k == x and v == x * 10

for _ in enumerate([]): assert False

# test for 'false' with enumerate
x = False
i = 0
for k, v in enumerate([True, False, True, False], 1):
    i += 1
    x = not x
    assert x == v
assert i == 4

# the iterator of a dict is always the same kind of object
assert len({}) == 0
assert len([None]) == 1
assert len([None, None]) == 2
assert len([None, None, None]) == 3
print("+")

# next-like iteration over a dict in insertion order
d = {"a": 1, "b": 2, "c": 3}
keys = []
for k in d:
    keys.append(k)
assert keys == ["a", "b", "c"]
assert list(d.keys()) == keys

# iteration of a dict that is not modified: each key is seen once
d = {}
for i in range(0, 10001):
    if i % 10 != 0:
        d["x" + str(i)] = i

n = 0
for k, v in d.items():
    n += 1
    assert k and v and d[k] == v
assert n == 9000
d = None

# check that the keys and values of a dict are consistent with items()
def checkitems(a):
    b = {}
    for k, v in a.items():
        b[k] = v
    for k, v in b.items(): assert a[k] == v
    for k, v in a.items(): assert b[k] == v

checkitems({1: 0, "x": 1, "y": 2, "z": 3})
checkitems({1: 0, 2: 0, "x": 1, "y": 2, "z": 3})
checkitems({1: 0, 2: 0, 3: 0, "x": 1, "y": 2, "z": 3})
checkitems({1: 0, 2: 0, 3: 0, 4: 0, "x": 1, "y": 2, "z": 3})
checkitems({1: 0, 2: 0, 3: 0, 4: 0, 5: 0, "x": 1, "y": 2, "z": 3})

assert len({}) == 0
assert len({-1: 2}) == 1
assert len([1, 2, 3]) == 3
for i in range(0, 41):
    a = {}
    for j in range(1, i + 1): a[j] = j
    assert len(a) == i

# the largest numeric key of a dict (Lua: the deprecated table.maxn)
def maxn(t):
    m = 0
    for k in t:
        if isinstance(k, (int, float)):
            m = max(m, k)
    return m

assert maxn({}) == 0
assert maxn({"1000": True}) == 0
assert maxn({"1000": True, 24.5: 3}) == 24.5
assert maxn({1000: True}) == 1000
assert maxn({10: True, 100 * math.pi: print}) == 100 * math.pi

# keys that are floats as well as ints
a = {}
for i in range(0, 31): a[2.0 ** i] = True
assert a[2 ** 30]
print("+")

# removing values while iterating over a copy of the keys
t = {1: 1, 2: 2, "x x x x ": 3, 100.3: 4, 4: 5}
n = 0
for k, v in list(t.items()):
    n += 1
    assert t[k] == v
    del t[k]
    assert k not in t
assert n == 5


# list operations: insert and pop at both ends and in the middle
def test(a):
    a.append(10)
    a.insert(1, 20)
    a.insert(0, -1)
    a.append(40)
    a.append(50)
    a.insert(1, -2)
    assert a.pop(0) == -1
    assert a.pop(0) == -2
    assert a.pop(0) == 10
    assert a.pop(0) == 20
    assert a.pop(0) == 40
    assert a.pop(0) == 50
    assert len(a) == 0

a = []
test(a)
assert len(a) == 0

a = ["c", "d"]
a.insert(2, "a")
a.append("b")
assert a.pop(0) == "c"
assert a.pop(0) == "d"
assert a.pop(0) == "a"
assert a.pop(0) == "b"
assert len(a) == 0

a = [10, 20, 30, 40]
assert a[len(a) - 1] == 40
assert a.pop() == 40
assert a[len(a) - 1] == 30
assert a.pop(1) == 20
assert a[len(a) - 1] == 30 and len(a) == 2
print("+")

# next-like iteration with an explicit iterator
a = {}
for i in range(1, 1001):
    a[i] = i
    if i - 1 in a:
        del a[i - 1]
assert list(a.keys()) == [1000]

assert len({}) == 0

# empty loops: range() excludes its stop value
for a1, b1 in {}.items(): assert False
for i in range(1, 1): assert False
for i in range(0, 1, -1): assert False
a = None
for i in range(1, 2):
    assert not a
    a = 1
assert a
a = None
for i in range(1, 0, -1):
    assert not a
    a = 1
assert a

# generic for with a custom sequence of tuples
def f(n, p):
    t = {}
    for i in range(1, p + 1): t[i] = i * 10
    result = []
    for m in range(n):
        result.append((m, t[1], t[2], t[3] if p >= 3 else None, None))
    return result

x = 0
for n, a1, b1, c1, d1 in f(5, 3):
    x += 1
    assert a1 == 10 and b1 == 20 and c1 == 30 and d1 is None
assert x == 5

# a dict and a list that are iterated in insertion order
a = {}
i = 0
for k, v in a.items():
    i += 1
assert i == 0

a = {}
for k in range(1, 11):
    a[k] = k + 1
i = 0
for k, v in a.items():
    i += 1
    assert k == i and v == k + 1

print("OK")
