# List and dict comprehensions, their scope and evaluation order.

assert [x * x for x in range(5)] == [0, 1, 4, 9, 16]
assert [x for x in range(10) if x % 3 == 0] == [0, 3, 6, 9]
assert [x for x in range(10) if x % 2 if x % 3] == [1, 5, 7]
assert [(x, y) for x in range(3) for y in range(x)] == [(1, 0), (2, 0), (2, 1)]
assert [x + y for x in "ab" for y in "12"] == ["a1", "a2", "b1", "b2"]
assert [[j for j in range(i)] for i in range(4)] == [[], [0], [0, 1], [0, 1, 2]]
assert [x for x in []] == []
assert [x if x > 0 else -x for x in [-1, 2, -3]] == [1, 2, 3]
assert [a * b for a, b in [(1, 2), (3, 4)]] == [2, 12]
assert [c.upper() for c in "abc" if c != "b"] == ["A", "C"]
assert [len(w) for w in "the quick brown fox".split()] == [3, 5, 5, 3]
assert [(i, c) for i, c in enumerate("ab")] == [(0, "a"), (1, "b")]
assert [x for x in range(3) for _ in range(2)] == [0, 0, 1, 1, 2, 2]

# dict comprehensions
assert {x: x * x for x in range(4)} == {0: 0, 1: 1, 2: 4, 3: 9}
assert {k: v for k, v in [("a", 1), ("b", 2)] if v > 1} == {"b": 2}
assert {c: i for i, c in enumerate("abc")} == {"a": 0, "b": 1, "c": 2}
assert {v: k for k, v in {"a": 1, "b": 2}.items()} == {1: "a", 2: "b"}
assert list({x % 3: x for x in range(6)}.items()) == [(0, 3), (1, 4), (2, 5)]
assert {} == {x: x for x in []}

# the loop variable does not leak
x = "outer"
assert [x for x in range(3)] == [0, 1, 2]
assert x == "outer"

def no_leak():
    y = "kept"
    z = [y for y in range(2)]
    return y, z

assert no_leak() == ("kept", [0, 1])

# the first iterable is evaluated in the enclosing scope
n = 3
assert [n for n in range(n)] == [0, 1, 2]

# comprehensions see enclosing locals
def scaled(factor, items):
    return [factor * v for v in items]

assert scaled(10, [1, 2]) == [10, 20]

def table(rows, cols):
    return [[r * c for c in range(1, cols + 1)] for r in range(1, rows + 1)]

assert table(3, 3) == [[1, 2, 3], [2, 4, 6], [3, 6, 9]]

# a comprehension has one scope, so all its lambdas see the last value
fs = [lambda: i for i in range(3)]
assert [f() for f in fs] == [2, 2, 2]
fs = [(lambda i: lambda: i)(i) for i in range(3)]
assert [f() for f in fs] == [0, 1, 2]

# order of evaluation
log = []

def tag(v):
    log.append(v)
    return v

assert [tag(a) + tag(b) for a in (1, 2) for b in (10, 20)] == [11, 21, 12, 22]
assert log == [1, 10, 1, 20, 2, 10, 2, 20]

# classic uses
words = "the cat and the dog and the bird".split()
counts = {}
for w in words:
    counts[w] = counts.get(w, 0) + 1
assert counts == {"the": 3, "cat": 1, "and": 2, "dog": 1, "bird": 1}
assert sorted([w for w in counts if counts[w] > 1]) == ["and", "the"]
assert sum([c for c in counts.values()]) == len(words)

flat = [v for row in [[1, 2], [3], [], [4, 5]] for v in row]
assert flat == [1, 2, 3, 4, 5]
transposed = [[row[i] for row in [[1, 2, 3], [4, 5, 6]]] for i in range(3)]
assert transposed == [[1, 4], [2, 5], [3, 6]]
primes = [n for n in range(2, 50) if all([n % d != 0 for d in range(2, n)])]
assert primes == [2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37, 41, 43, 47]
