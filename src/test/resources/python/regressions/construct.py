# Displays containing calls, nested displays and trailing commas.
def f(s):
    return s + "!"

x = [f("alo")]
assert x == ["alo!"]
y = [f("a"), [f("b"), (f("c"),)], {"k": [f("d")]},]
assert y == ["a!", ["b!", ("c!",)], {"k": ["d!"]}]
z = {f("a"): f("b"), 1: [2, 3], (4, 5): 6,}
assert z["a!"] == "b!"
assert z[1] == [2, 3]
assert z[(4, 5)] == 6
assert [] == []
assert () == ()
assert {} == {}
assert (1) == 1
assert (1,) != 1
assert len((1,)) == 1
t = 1, 2, 3
assert t == (1, 2, 3)
a, b = 1, 2
a, b = b, a
assert (a, b) == (2, 1)
