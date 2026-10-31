# Chained comparisons: each operand is evaluated once, the chain stops at the first
# false link, and every operator of the family can be mixed.

seen = []
def v(x):
    seen.append(x)
    return x

print(1 < 2 < 3, 1 < 3 < 2, 3 > 2 > 1, 1 <= 1 <= 1, 1 == 1 == 1, 1 != 2 != 1, 1 < 2 > 1, 2 > 1 < 2)
print(v(1) < v(2) < v(3), seen)
seen = []
print(v(3) < v(2) < v(1), seen)
seen = []
print(v(1) < v(5) < v(3) < v(9), seen)
seen = []
print(v(1) == v(1) != v(2) <= v(2), seen)

x = 5
print(0 < x < 10, 0 < x < 5, 5 <= x <= 5, 0 < x <= 5 < 6, not 0 < x < 3)
print(1 < 2 == 2, 1 < 2 == 3, "a" < "b" < "c", "a" < "c" < "b", (1, 2) < (1, 3) < (2, 0))
print(1 < 2.5 < 3, 1.0 == 1 == True, 0 < True < 2)
y = None
one = 1
two = 2
print(y is None is not one, y is not one is not two, 1 in [1] in [[1]], 2 not in [1] not in [[2]])
print("a" in "abc" in ["abc"], 1 in [1, 2] == True, [1] == [1] == [1], [1] < [2] < [3])

# the result of a chain is a bool of the last evaluated comparison
r = 1 < 2 < 3
print(r, type(r).__name__, (1 < 2 > 5), (3 > 2 > 1 > 0))
a = b = 3
print(a is b, a == b == 3, a < b, not a < b, a <= b <= 4)

# chains in conditions, comprehensions and with and/or
print([n for n in range(10) if 2 < n < 6], [n for n in range(10) if 2 < n < 6 or n == 9])
print(1 < 2 < 3 and 4 > 3 > 2, 1 < 2 < 3 and 4 > 3 > 5, 1 > 2 > 3 or 5 > 4 > 3)
if 1 < x < 10:
    print("in range")
while 0 < x < 8:
    x += 1
print(x)

def err(fn):
    try:
        fn()
    except Exception as e:
        return type(e).__name__
    return "ok"

print(err(lambda: 1 < "a" < 3), err(lambda: 2 < 1 < "a"), err(lambda: 1 < 2 < "a"), err(lambda: [1] < 1 < 2))
