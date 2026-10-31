# set: items, operators, methods, errors.
# The order of the items is not tested: this VM keeps the order of insertion,
# CPython the order of the hashes. What is printed is sorted, or has one item.

def raises(exc, f, *args):
    try:
        f(*args)
    except exc:
        return True
    return False

def error(f):
    try:
        f()
    except Exception as e:
        return type(e).__name__ + ": " + str(e)
    return "no error"

# displays and set()
s = {3, 1, 2, 1}
assert len(s) == 3 and s
assert sorted(s) == [1, 2, 3]
assert set() == set() and not set() and len(set()) == 0
assert set([1, 2, 2]) == {1, 2}
assert set("abca") == {"a", "b", "c"}
assert set(range(3)) == {0, 1, 2}
assert set((1, 2)) == {2, 1}
assert set({"k": 1, "j": 2}) == {"k", "j"}
assert set({1, 2}) == {1, 2}
t = {1, 2}
assert set(t) is not t
assert type(s) is set and isinstance(s, set)
assert type({}) is dict
print(repr(set()), repr({1}), str({"a"}), set(), {(1, 2)})
print(f"{set()} {({None})}")

# items: equal values are one item, the first one given is kept
n = {1, 1.0, True}
assert len(n) == 1
print(n)
assert {0, False, 0.0} == {0}
assert {(1, 2), (1.0, 2)} == {(1, 2)}
assert {None, "a", 2.5} == {2.5, "a", None}

# items must be hashable
assert raises(TypeError, lambda: {[1]})
assert raises(TypeError, lambda: {{}})
assert raises(TypeError, lambda: {set()})
assert raises(TypeError, lambda: set([[1]]))
assert raises(TypeError, lambda: {1}.add([]))
assert raises(TypeError, lambda: [] in {1})
assert raises(TypeError, lambda: {1: 0}[{1}])
assert raises(TypeError, lambda: {(1, [2])})
print(error(lambda: {[1]}))
print(error(lambda: {{1}: 0}))

# membership
assert 1 in s and 4 not in s and 1.0 in s and True in s
assert "1" not in s

# equality and inclusion
assert {1, 2} == {2, 1} and {1, 2} != {1, 3} and {1} != {1, 2}
assert {1, 2} != [1, 2] and not ({1} == (1,)) and {1} != {1: None}
assert {1} < {1, 2} and not {1, 2} < {1, 2} and {1, 2} <= {1, 2}
assert {1, 2} > {2} and {1, 2} >= {1, 2} and not {3} >= {1, 2}
assert not {1, 3} < {1, 2} and not {1, 3} > {1, 2} and not {1, 3} == {1, 2}
assert set() < {0} and set() <= set()
assert raises(TypeError, lambda: {1} < [1, 2])
assert raises(TypeError, lambda: {1} <= 3)
d = {"a": 1, "b": 2}
assert d.keys() == {"a", "b"} and {"b", "a"} == d.keys()
assert d.keys() != {"a"} and d.items() != {"a", "b"} and {"a", "b"} != d.values()
print(error(lambda: {1} < [1]))

# operators
a = {1, 2, 3}
b = {3, 4}
assert a | b == {1, 2, 3, 4}
assert a & b == {3}
assert a - b == {1, 2} and b - a == {4}
assert a ^ b == {1, 2, 4}
assert a == {1, 2, 3} and b == {3, 4}
assert (a | b) is not a
assert a | set() == a and (a | set()) is not a and a & set() == set()
assert a - a == set() and a ^ a == set()
assert raises(TypeError, lambda: a | [4])
assert raises(TypeError, lambda: a + b)
assert raises(TypeError, lambda: a * 2)
assert raises(TypeError, lambda: [4] - a)
print(error(lambda: a + b))
print(error(lambda: a | [4]))

# operators in place change the set
c = {1, 2}
alias = c
c |= {3}
c -= {1}
c &= {2, 3, 4}
c ^= {3, 5}
assert alias is c and c == {2, 5}
c |= c
assert c == {2, 5}
c &= c
assert c == {2, 5}
c ^= c
assert c == set() and alias is c
c = {1, 2}
c -= c
assert c == set()
c = {1}
def grow():
    global c
    c |= [2]
assert raises(TypeError, grow)

# add, remove, discard, pop, clear, copy
s = set()
assert s.add(1) is None
s.add(2)
s.add(1)
assert s == {1, 2}
assert s.remove(1) is None and s == {2}
assert raises(KeyError, lambda: s.remove(9))
print(error(lambda: s.remove(9)))
print(error(lambda: s.remove((1, 2))))
assert s.discard(9) is None and s.discard(2) is None and s == set()
s = {7}
assert s.pop() == 7 and s == set()
assert raises(KeyError, s.pop)
print(error(s.pop))
s = {1, 2, 3}
popped = {s.pop(), s.pop(), s.pop()}
assert popped == {1, 2, 3} and s == set()
s = {1, 2}
s.clear()
assert s == set() and len(s) == 0
s = {1, 2}
k = s.copy()
assert k == s and k is not s
k.add(3)
assert s == {1, 2}

# union, intersection, difference, symmetric_difference: any iterable
a = {1, 2, 3}
assert a.union() == a and a.union() is not a
assert a.union([4], (5,), "") == {1, 2, 3, 4, 5}
assert a.union({1: 0}) == a
assert a.intersection([2, 3, 4]) == {2, 3}
assert a.intersection([2, 3], range(3)) == {2}
assert a.intersection() == a and a.intersection() is not a
assert a.difference([1], {2}) == {3}
assert a.difference() == a
assert a.symmetric_difference([3, 4, 4]) == {1, 2, 4}
assert a == {1, 2, 3}
assert raises(TypeError, lambda: a.union(1))
assert raises(TypeError, lambda: a.symmetric_difference())
assert raises(TypeError, lambda: a.intersection([[1]]))
print(error(lambda: a.union(1)))

# the _update methods change the set and return None
s = {1, 2}
assert s.update([3], {4}) is None and s == {1, 2, 3, 4}
s.update()
s.update(s)
assert s == {1, 2, 3, 4}
assert s.intersection_update(range(1, 4), [2, 3, 9]) is None and s == {2, 3}
assert s.difference_update([2]) is None and s == {3}
s.difference_update(s)
assert s == set()
s = {1, 2}
assert s.symmetric_difference_update([2, 3, 3]) is None and s == {1, 3}
s.symmetric_difference_update(s)
assert s == set()

# issubset, issuperset, isdisjoint
assert {1}.issubset([1, 2]) and not {1, 3}.issubset({1, 2})
assert set().issubset([]) and {1, 2}.issubset({1, 2})
assert {1, 2}.issuperset([2]) and not {1}.issuperset((1, 2))
assert {1}.isdisjoint([2, 3]) and not {1, 2}.isdisjoint(range(2))
assert set().isdisjoint(set())
assert raises(TypeError, lambda: {1}.issubset(3))

# iteration
total = 0
for x in {1, 2, 3}:
    total += x
assert total == 6
assert sum({1, 2, 3}) == 6 and max({3, 1, 2}) == 3 and min({3, 1, 2}) == 1
assert sorted({"b", "c", "a"}) == ["a", "b", "c"]
assert "-".join(sorted({"x", "y"})) == "x-y"
assert any({0, 1}) and not any({0}) and all({1, 2}) and not all({0, 1})
assert sorted(list({1, 2})) == [1, 2] and len(tuple({1, 2})) == 2
x, = {5}
assert x == 5
assert sorted(x * 2 for x in {1, 2}) == [2, 4]
assert {k: 0 for k in {1}} == {1: 0}

# a set may not change size while it is iterated
s = {1, 2, 3}
def grow_while_iterating():
    for x in s:
        s.add(x + 10)
def shrink_while_iterating():
    for x in s:
        s.discard(x)
assert raises(RuntimeError, grow_while_iterating)
assert raises(RuntimeError, shrink_while_iterating)
print(error(grow_while_iterating))

# a set is not subscriptable, has no item assignment and is not JSON
one = {1}
assert raises(TypeError, lambda: one[0])
print(error(lambda: one[0]))
def assign():
    s = {1}
    s[0] = 1
assert raises(TypeError, assign)
import json
print(error(lambda: json.dumps({1})))

# methods are attributes
assert hasattr(set(), "add") and not hasattr(set(), "append")
assert raises(AttributeError, lambda: set().append)
m = {1}.issubset
assert m([1, 2])
print("sets ok")
