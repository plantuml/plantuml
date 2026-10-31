# Iteration over the builtin containers, enumerate / zip / sorted / reversed.

def raises(exc, f, *args):
    try:
        f(*args)
    except exc:
        return True
    return False

# lists
seen = []
for v in [10, 20, 30]:
    seen.append(v)
assert seen == [10, 20, 30]

# tuples and strings
assert [c for c in "aé€"] == ["a", "é", "€"]
assert [v for v in (1, 2)] == [1, 2]

# dict: insertion order
d = {}
for k in ["z", "a", "m", "b"]:
    d[k] = len(d)
assert list(d) == ["z", "a", "m", "b"]
assert list(d.keys()) == ["z", "a", "m", "b"]
assert list(d.values()) == [0, 1, 2, 3]
assert list(d.items()) == [("z", 0), ("a", 1), ("m", 2), ("b", 3)]
del d["a"]
d["a"] = 99
assert list(d) == ["z", "m", "b", "a"]
for k, v in d.items():
    assert d[k] == v

# updating an existing key keeps its position
d = {"x": 1, "y": 2, "z": 3}
d["x"] = 10
assert list(d.items()) == [("x", 10), ("y", 2), ("z", 3)]

# changing a dict while iterating over it is an error
def grow():
    d = {"a": 1}
    for k in d:
        d["b" + k] = 1

assert raises(RuntimeError, grow)

# updating values during iteration is fine
d = {"a": 1, "b": 2}
for k in d:
    d[k] = d[k] * 10
assert d == {"a": 10, "b": 20}

# an empty container
for _ in []:
    assert False
for _ in {}:
    assert False
for _ in "":
    assert False
for _ in ():
    assert False

# not iterable
assert raises(TypeError, lambda: [x for x in 5])
assert raises(TypeError, lambda: [x for x in None])
assert raises(TypeError, lambda: list(5))
assert raises(TypeError, lambda: len(5))

# the sequence can grow during the loop over a list
lst = [1, 2, 3]
count = 0
for v in lst:
    count += 1
    if count < 5:
        lst.append(v)
assert count == 7
assert lst == [1, 2, 3, 1, 2, 3, 1]

# enumerate / zip / reversed / sorted
assert list(enumerate(["a", "b", "c"])) == [(0, "a"), (1, "b"), (2, "c")]
assert list(enumerate(["a", "b"], 1)) == [(1, "a"), (2, "b")]
assert list(enumerate("")) == []
for i, c in enumerate("xyz"):
    assert "xyz"[i] == c
assert list(zip([1, 2, 3], "ab")) == [(1, "a"), (2, "b")]
assert list(zip([1, 2], [3, 4], [5, 6])) == [(1, 3, 5), (2, 4, 6)]
assert list(zip()) == []
assert list(zip([1])) == [(1,)]
assert dict(zip("abc", range(3))) == {"a": 0, "b": 1, "c": 2}
keys, values = zip(*[(1, "a"), (2, "b")])
assert keys == (1, 2) and values == ("a", "b")
assert list(reversed([1, 2, 3])) == [3, 2, 1]
assert list(reversed("abc")) == ["c", "b", "a"]
assert list(reversed(range(3))) == [2, 1, 0]
assert list(reversed(())) == []
assert sorted([3, 1, 2]) == [1, 2, 3]
assert sorted("hello") == ["e", "h", "l", "l", "o"]
assert sorted({"b": 1, "a": 2}) == ["a", "b"]
assert sorted([3, 1, 2], reverse=True) == [3, 2, 1]
assert sorted(["bb", "a", "ccc"], key=len) == ["a", "bb", "ccc"]
assert sorted(["bb", "a", "ccc"], key=len, reverse=True) == ["ccc", "bb", "a"]

# sorted is stable
people = [("bob", 30), ("alice", 25), ("carol", 30), ("dave", 25)]
assert sorted(people, key=lambda p: p[1]) == [("alice", 25), ("dave", 25), ("bob", 30), ("carol", 30)]
assert sorted(people, key=lambda p: p[1], reverse=True) == [("bob", 30), ("carol", 30), ("alice", 25), ("dave", 25)]

# sum / min / max / any / all over iterables
assert sum([1, 2, 3]) == 6
assert sum([]) == 0
assert sum((1.5, 2.5)) == 4.0
assert sum([[1], [2]], []) == [1, 2]
assert sum(range(101)) == 5050
assert sum([x * x for x in range(4)]) == 14
assert min([3, 1, 2]) == 1 and max([3, 1, 2]) == 3
assert min(3, 1, 2) == 1 and max(3, 1, 2) == 3
assert min("b", "a") == "a"
assert max([1, 2.5]) == 2.5
assert max(["aa", "b"], key=len) == "aa"
assert min([(2, "a"), (1, "z")]) == (1, "z")
assert max([], default=7) == 7
assert raises(ValueError, lambda: max([]))
assert raises(ValueError, lambda: min(()))
assert any([0, "", 3]) and not any([0, "", None]) and not any([])
assert all([1, "a", [0]]) and not all([1, 0]) and all([])

# map / filter
assert list(map(lambda x: x * 2, [1, 2, 3])) == [2, 4, 6]
assert list(map(str, [1, 2])) == ["1", "2"]
assert list(map(lambda a, b: a + b, [1, 2], [10, 20])) == [11, 22]
assert list(filter(lambda x: x % 2, range(6))) == [1, 3, 5]
assert list(filter(None, [0, 1, "", "a", None])) == [1, "a"]

# list / tuple / dict constructors from iterables
assert list("abc") == ["a", "b", "c"]
assert list((1, 2)) == [1, 2]
assert list({"a": 1}) == ["a"]
assert list(range(3)) == [0, 1, 2]
assert list() == []
assert tuple([1, 2]) == (1, 2)
assert tuple("ab") == ("a", "b")
assert tuple() == ()
assert dict([("a", 1), ("b", 2)]) == {"a": 1, "b": 2}
assert dict(a=1, b=2) == {"a": 1, "b": 2}
assert dict({"a": 1}) == {"a": 1}
assert dict() == {}

# range as a value
r = range(2, 20, 4)
assert list(r) == [2, 6, 10, 14, 18]
assert len(r) == 5
assert r[0] == 2 and r[-1] == 18
assert 6 in r and 7 not in r
assert list(range(5)[1:3]) == [1, 2]
assert range(3) == range(3)

# iterating twice over a list is fine
lst = [1, 2, 3]
assert [x for x in lst] == [x for x in lst]

# nested iteration builds a product
product = []
for a in "ab":
    for b in (1, 2):
        product.append(a + str(b))
assert product == ["a1", "a2", "b1", "b2"]

# unpacking while iterating
matrix = [[1, 2, 3], [4, 5, 6]]
assert [a + b + c for a, b, c in matrix] == [6, 15]
for i, (a, b) in enumerate([(1, 2), (3, 4)]):
    assert a == 2 * i + 1 and b == 2 * i + 2
# swap values of a dict
d = {"a": 1, "b": 2}
inverse = {}
for k, v in d.items():
    inverse[v] = k
assert inverse == {1: "a", 2: "b"}
print("iteration ok")
