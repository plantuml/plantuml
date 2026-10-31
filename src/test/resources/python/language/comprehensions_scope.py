# Comprehensions: dict comprehensions, nested loops and conditions, the scope of the
# loop variables, closures over them, and evaluation order.

print({k: v for k, v in [(1, "a"), (2, "b")]})
print({i: i * i for i in range(5) if i % 2 == 0})
print({c: ord(c) for c in "abc"})
print({(i, j): i + j for i in range(2) for j in range(2)})
print({k: [x for x in range(k)] for k in range(4)})
print({k: v for k, v in {"x": 1, "y": 2}.items() if v > 1})
dup = {i % 3: i for i in range(10)}
print(dup, len(dup))
print({}, {i: i for i in []})

print([i * j for i in range(1, 4) for j in range(1, 4) if i != j])
print([x for x in range(20) if x % 2 if x % 3 == 0])
print([(x, y) for x in "ab" for y in range(2)])
print([[j for j in range(i)] for i in range(4)])
print([x if x > 2 else -x for x in range(5)])
print([y for x in [[1, 2], [3], []] for y in x])
print([c.upper() for c in "abc" if c != "b"], [len(w) for w in "a bb ccc".split()])

# the loop variable does not leak
i = "outer"
r = [i for i in range(3)]
print(i, r)
k = "outer"
d = {k: 1 for k in range(2)}
print(k, d)

# the outermost iterable is evaluated once, in the enclosing scope
calls = []
def src():
    calls.append(1)
    return range(3)

print([x for x in src()], len(calls))
print([x + y for x in src() for y in src()], len(calls))

# the closure sees the variable of the comprehension when called later
fs = [lambda: n for n in range(3)]
print([f() for f in fs])
gs = [(lambda n=n: n) for n in range(3)]
print([g() for g in gs])

# access to the variables of the function around, and assignment to a name
def outer(a):
    b = 10
    return [a + b + x for x in range(3)], {x: a for x in range(2)}

print(outer(1))

def accum():
    total = 0
    for row in [[1, 2], [3, 4]]:
        total += sum([x * 2 for x in row])
    return total

print(accum())

# order of evaluation
order = []
def tag(v):
    order.append(v)
    return v

print([tag(x) + tag(x * 10) for x in range(3)], order)
order = []
print({tag(k): tag(k * 2) for k in range(2)}, order)

# comprehension over dict, str, tuple, range, enumerate, zip
print([k for k in {"a": 1, "b": 2}], [v for v in {"a": 1, "b": 2}.values()])
print([c for c in "hello"], [x for x in (1, 2, 3)], [x for x in range(2, 10, 3)])
print([(i, x) for i, x in enumerate("ab")], [(a, b) for a, b in zip([1, 2], "xy")])
print(sum([x * x for x in range(10)]), max([len(s) for s in ["a", "bcd", "ef"]]), sorted([-x for x in range(4)]))

def err(fn):
    try:
        fn()
    except Exception as e:
        return type(e).__name__
    return "ok"

print(err(lambda: [1 / x for x in [1, 0]]), err(lambda: {x: 1 for x in 5}), err(lambda: [x for x in None]))
print(err(lambda: {[1]: 1 for _ in range(1)}), err(lambda: [y for x in [1] for y in x]))
