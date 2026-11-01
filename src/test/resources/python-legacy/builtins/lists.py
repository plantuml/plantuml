# list and tuple: indexing, slicing, methods, sorting.

def raises(exc, f, *args):
    try:
        f(*args)
    except exc:
        return True
    return False

# indexing
a = [10, 20, 30, 40]
assert a[0] == 10 and a[3] == 40 and a[-1] == 40 and a[-4] == 10
assert raises(IndexError, lambda: a[4])
assert raises(IndexError, lambda: a[-5])
assert raises(IndexError, lambda: [][0])
a[0] = 11
a[-1] = 41
assert a == [11, 20, 30, 41]
def set_out_of_range():
    a[4] = 0
assert raises(IndexError, set_out_of_range)
assert len(a) == 4

# slicing
s = [0, 1, 2, 3, 4, 5, 6, 7, 8, 9]
assert s[2:5] == [2, 3, 4]
assert s[:3] == [0, 1, 2]
assert s[7:] == [7, 8, 9]
assert s[:] == s and s[:] is not s
assert s[-3:] == [7, 8, 9]
assert s[::3] == [0, 3, 6, 9]
assert s[::-1] == [9, 8, 7, 6, 5, 4, 3, 2, 1, 0]
assert s[8:2:-2] == [8, 6, 4]
assert s[5:2] == [] and s[20:] == [] and s[:20] == s
assert (1, 2, 3, 4)[1:3] == (2, 3)
assert (1, 2, 3)[::-1] == (3, 2, 1)

# slice assignment and deletion
b = [0, 1, 2, 3, 4, 5]
b[1:3] = ["a", "b", "c", "d"]
assert b == [0, "a", "b", "c", "d", 3, 4, 5]
b[1:5] = []
assert b == [0, 3, 4, 5]
b[1:1] = [9, 9]
assert b == [0, 9, 9, 3, 4, 5]
b[:2] = [7]
assert b == [7, 9, 3, 4, 5]
b[len(b):] = [6]
assert b == [7, 9, 3, 4, 5, 6]
b[::2] = [0, 0, 0]
assert b == [0, 9, 0, 4, 0, 6]
def bad_extended():
    b[::2] = [1]
assert raises(ValueError, bad_extended)
del b[0]
assert b == [9, 0, 4, 0, 6]
del b[-1]
assert b == [9, 0, 4, 0]
del b[1:3]
assert b == [9, 0]
del b[:]
assert b == []
c = [1, 2, 3, 4, 5, 6]
del c[::2]
assert c == [2, 4, 6]

# append / extend / insert
l = []
l.append(1)
l.append([2])
assert l == [1, [2]]
assert l.append(3) is None
l.extend([4, 5])
l.extend((6,))
l.extend("ab")
l.extend(range(2))
assert l == [1, [2], 3, 4, 5, 6, "a", "b", 0, 1]
l = [1, 2, 3]
l.insert(0, "first")
l.insert(2, "mid")
l.insert(100, "last")
l.insert(-1, "before last")
assert l == ["first", 1, "mid", 2, 3, "before last", "last"]
l = [1]
l.extend(l)
assert l == [1, 1]

# pop / remove / clear
l = [1, 2, 3, 2, 1]
assert l.pop() == 1
assert l.pop(0) == 1
assert l.pop(-2) == 3
assert l == [2, 2]
assert raises(IndexError, lambda: l.pop(5))
assert raises(IndexError, lambda: [].pop())
l = [1, 2, 3, 2, 1]
l.remove(2)
assert l == [1, 3, 2, 1]
l.remove(1)
assert l == [3, 2, 1]
assert raises(ValueError, lambda: l.remove(99))
l.clear()
assert l == [] and len(l) == 0

# index / count / in
l = [5, 6, 7, 6]
assert l.index(6) == 1
assert l.index(6, 2) == 3
assert raises(ValueError, lambda: l.index(9))
assert l.count(6) == 2 and l.count(9) == 0
assert (1, 2, 1).index(2) == 1 and (1, 2, 1).count(1) == 2
assert [1, "a"].count("a") == 1
assert 7 in l and 8 not in l
assert [1, 2] in [[1, 2], [3]]

# reverse / copy / sort
l = [1, 2, 3]
assert l.reverse() is None
assert l == [3, 2, 1]
m = l.copy()
m.append(0)
assert l == [3, 2, 1] and m == [3, 2, 1, 0]
nested = [[1], [2]]
shallow = nested.copy()
shallow[0].append(9)
assert nested == [[1, 9], [2]]
l = [3, 1, 2]
assert l.sort() is None
assert l == [1, 2, 3]
l.sort(reverse=True)
assert l == [3, 2, 1]
w = ["banana", "kiwi", "apple", "fig"]
w.sort()
assert w == ["apple", "banana", "fig", "kiwi"]
w.sort(key=len)
assert w == ["fig", "kiwi", "apple", "banana"]
w.sort(key=len, reverse=True)
assert w == ["banana", "apple", "kiwi", "fig"]
w.sort(key=lambda s: s[-1])
assert w == ["banana", "apple", "fig", "kiwi"]
mixed = [3, 1.5, 2, -1, 2.5]
mixed.sort()
assert mixed == [-1, 1.5, 2, 2.5, 3]
assert raises(TypeError, lambda: [1, "a"].sort())
assert raises(TypeError, lambda: [[1], 2].sort())
t = [(2, "b"), (1, "z"), (2, "a"), (1, "a")]
t.sort()
assert t == [(1, "a"), (1, "z"), (2, "a"), (2, "b")]
lists = [[2, 1], [1, 5], [1], []]
lists.sort()
assert lists == [[], [1], [1, 5], [2, 1]]
strs = ["b", "B", "a", "A", "é", "z"]
strs.sort()
assert strs == ["A", "B", "a", "b", "z", "é"]

# sort is stable and calls key once per element
calls = []
def key(v):
    calls.append(v)
    return v % 3
l = [5, 3, 9, 1, 4, 8, 2]
l.sort(key=key)
assert l == [3, 9, 1, 4, 5, 8, 2]
assert len(calls) == 7

# sort of larger data, and permutations
def check_sorted(l):
    for i in range(len(l) - 1):
        assert l[i] <= l[i + 1]

seed = 12345
data = []
for i in range(500):
    seed = (seed * 1103 + 12345) % 65536
    data.append(seed)
copy = data[:]
data.sort()
check_sorted(data)
assert len(data) == 500 and sorted(copy) == data
data.sort(reverse=True)
assert data == sorted(copy, reverse=True)
assert sorted(copy, key=lambda v: -v) == data

def perm(s, n, out):
    if n == 1:
        t = s[:]
        t.sort()
        check_sorted(t)
        out.append(1)
    else:
        for i in range(n):
            s[i], s[n - 1] = s[n - 1], s[i]
            perm(s, n - 1, out)
            s[i], s[n - 1] = s[n - 1], s[i]

out = []
perm([3, 1, 4, 1, 5], 5, out)
assert len(out) == 120

# list as stack and queue
stack = []
for i in range(5):
    stack.append(i)
assert [stack.pop() for _ in range(5)] == [4, 3, 2, 1, 0]
queue = [1, 2, 3]
queue.append(4)
assert queue.pop(0) == 1 and queue == [2, 3, 4]

# aliasing and copies
x = [1, 2]
y = x
y.append(3)
assert x == [1, 2, 3] and x is y
z = x[:]
z.append(4)
assert x == [1, 2, 3]
grid = [[0] * 3 for _ in range(2)]
grid[0][0] = 1
assert grid == [[1, 0, 0], [0, 0, 0]]
bad = [[0] * 3] * 2
bad[0][0] = 1
assert bad == [[1, 0, 0], [1, 0, 0]]

# tuples
t = (1, "a", [2])
assert t[0] == 1 and t[-1] == [2] and len(t) == 3
assert raises(AttributeError, lambda: t.append(1))
def assign_tuple():
    t[0] = 5
assert raises(TypeError, assign_tuple)
t[2].append(3)
assert t == (1, "a", [2, 3])
assert (1, 2) == (1, 2) and (1, 2) != (2, 1) and () == ()
assert (1,) + (2,) == (1, 2)
assert tuple([1, [2]]) == (1, [2])
assert min((3, 1, 2)) == 1 and sorted((3, 1, 2)) == [1, 2, 3]
assert {(1, 2): "a"}[(1, 2)] == "a"
assert raises(TypeError, lambda: {(1, [2]): 1})
# length and truthiness
assert len([]) == 0 and not [] and [0]
assert len([[], []]) == 2
print("lists ok")
