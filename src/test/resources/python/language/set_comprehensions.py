# Set displays and set comprehensions: evaluation, nested loops and
# conditions, the scope of the loop variables, closures over them.
# The order of the items differs from CPython: what is printed is sorted.

def show(s):
    return "{" + ", ".join(repr(x) for x in sorted(s)) + "}"

# displays: every element is evaluated, from left to right
order = []
def item(x):
    order.append(x)
    return x % 3
s = {item(5), item(4), item(3), item(8)}
print(show(s), order)
print(show({1, 2,}), show({(1, 2), (1, 2)}), show({"a" "b", "c"}))
print(show({1 if True else 2, -1, not 0}))
print({1} == {True}, {1, 2} == {2, 1}, show({x for x in []}), set())

# comprehensions
print(show({x * x for x in range(-3, 4)}))
print(show({c for c in "mississippi"}))
print(show({x for x in range(20) if x % 2 if x % 3 == 0}))
print(show({(i, j) for i in range(3) for j in range(i)}))
print(show({i + j for i in range(3) for j in range(3) if i != j}))
print(show({len(w) for w in "a bb cc ddd".split()}))
print(show({x % 3 for x in {5, 6, 7, 8}}))
print(show({frozen for frozen in [(1, 2), (2, 1), (1, 2)]}))
print([show(s) for s in [{j for j in range(i)} for i in range(3)]])
print({k: show({k * x for x in range(3)}) for k in range(2)})

# the loop variable does not leak
x = "outer"
r = {x for x in range(3)}
print(x, show(r))

# the outermost iterable is evaluated once, in the enclosing scope
calls = []
def src():
    calls.append(1)
    return range(3)
print(show({x + y for x in src() for y in src()}), len(calls))

# the variables of the function around, and closures
def outer(a):
    b = 10
    return {a + b + x for x in range(3)}
print(show(outer(1)))
fs = {(lambda: n) for n in range(3)}
print(sorted(f() for f in fs))
gs = {(lambda n=n: n) for n in range(3)}
print(sorted(g() for g in gs))

# items must be hashable, also in a comprehension
try:
    {[x] for x in range(2)}
except TypeError as e:
    print("TypeError:", e)
try:
    {x for x in 5}
except TypeError as e:
    print("TypeError:", e)

# in a class body, the iterable is evaluated in the class
class C:
    base = [1, 2]
    squares = {v * v for v in base}
print(show(C.squares))
