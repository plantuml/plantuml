# Generator expressions. They are NOT lazy in this subset: the elements are all
# computed when the expression is evaluated (like a list comprehension) and the
# value is an iterator over them. These tests only use what is the same as in
# CPython: the values, and the single pass.

assert all(x > 0 for x in [1, 2, 3])
assert not all(x > 1 for x in [1, 2, 3])
assert any(x > 2 for x in [1, 2, 3])
assert not any(x > 3 for x in [1, 2, 3])
assert all(x for x in [])
assert not any(x for x in [])

assert sum(x * x for x in range(5)) == 30
assert min(x - 3 for x in range(5)) == -3
assert max(len(w) for w in ["a", "abc", "ab"]) == 3
assert sorted(x % 3 for x in range(6)) == [0, 0, 1, 1, 2, 2]
assert list(x + 1 for x in range(3)) == [1, 2, 3]
assert ",".join(str(x) for x in range(4)) == "0,1,2,3"
assert tuple(x for x in "ab") == ("a", "b")

# a parenthesized generator expression, and with other arguments
g = (x * 2 for x in range(3))
assert list(g) == [0, 2, 4]
assert list(g) == []  # one pass only
assert sum((x for x in range(4)), 10) == 16

# in a for loop
seen = []
for v in (c.upper() for c in "abc" if c != "b"):
    seen.append(v)
assert seen == ["A", "C"]

# conditions, several for clauses, destructuring
assert list(x for x in range(10) if x % 2 if x % 3) == [1, 5, 7]
assert list((x, y) for x in range(3) for y in range(x)) == [(1, 0), (2, 0), (2, 1)]
assert list(a * b for a, b in [(1, 2), (3, 4)]) == [2, 12]

# nested generator expressions
assert sum(sum(j for j in range(i)) for i in range(4)) == 4
assert list(list(j for j in range(i)) for i in range(3)) == [[], [0], [0, 1]]

# the loop variable does not leak, and the first iterable is evaluated outside
x = "outer"
assert list(x for x in range(3)) == [0, 1, 2]
assert x == "outer"
n = 3
assert list(n for n in range(n)) == [0, 1, 2]

# closures
def scaled(factor, items):
    return sum(factor * v for v in items)

assert scaled(10, [1, 2]) == 30

def counter():
    total = 0
    def add(values):
        return sum(total + v for v in values)
    total = 5
    return add

assert counter()([1, 2]) == 13

# a generator expression sees the variables of the enclosing function
def has_prime(limit):
    return any(all(n % d != 0 for d in range(2, n)) for n in range(2, limit))

assert has_prime(10)
assert not has_prime(2)

# primes, as the classic example
primes = [n for n in range(2, 30) if all(n % d != 0 for d in range(2, n))]
assert primes == [2, 3, 5, 7, 11, 13, 17, 19, 23, 29]
