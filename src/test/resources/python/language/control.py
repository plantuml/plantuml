# if / while / for, break, continue, else on loops, boolean operators,
# conditional expressions.

# if / elif / else
def classify(n):
    if n < 0:
        return "neg"
    elif n == 0:
        return "zero"
    elif n < 10:
        return "small"
    else:
        return "big"

assert [classify(n) for n in (-5, 0, 3, 10, 99)] == ["neg", "zero", "small", "big", "big"]

# truth values
def truth(x):
    if x:
        return True
    return False

assert truth(1) and truth(-1) and truth(0.5) and truth("a") and truth([0]) and truth((0,)) and truth({"a": 1})
assert not truth(0) and not truth(0.0) and not truth("") and not truth([]) and not truth(()) and not truth({}) and not truth(None) and not truth(False)

# and / or return an operand
assert (1 and 2) == 2
assert (0 and 2) == 0
assert (None and 2) is None
assert (1 or 2) == 1
assert (0 or 2) == 2
assert (0 or "") == ""
assert ("" or None) is None
assert ([] or {} or 7) == 7
assert (not None) is True
assert (not 0) is True
assert (not "x") is False

# short circuit
log = []

def t(name):
    log.append(name)
    return True

def f(name):
    log.append(name)
    return False

assert (f("a") and t("b")) is False
assert (t("c") or f("d")) is True
assert log == ["a", "c"]
log.clear()
assert (f("a") or t("b") or t("c")) is True
assert (t("a") and t("b") and f("c") and t("d")) is False
assert log == ["a", "b", "a", "b", "c"]

# precedence
assert 2 + 3 * 4 == 14
assert (2 + 3) * 4 == 20
assert 2 ** 3 ** 2 == 512
assert -2 ** 2 == -4
assert (-2) ** 2 == 4
assert not 1 == 2
assert (1 or 0 and 0) == 1
assert 10 - 4 - 3 == 3
assert 100 // 10 // 5 == 2
assert 2 * 3 % 4 == 2
assert 1 + 2 < 4 == 4
assert not (1 < 2) == False
assert 1 << 2 + 1 == 8
assert 6 & 3 | 8 == 10
assert 6 ^ 3 & 1 == 7
assert ~5 == -6
assert - -3 == 3
assert +3 == 3

# conditional expression
def sign(n):
    return "neg" if n < 0 else "zero" if n == 0 else "pos"

assert [sign(n) for n in (-1, 0, 1)] == ["neg", "zero", "pos"]
x = 5
assert (x if x > 3 else 0) == 5
assert (1 if False else 2 if False else 3) == 3
log.clear()
assert (t("yes") if True else f("no")) is True
assert log == ["yes"]

# while
i = 0
total = 0
while i < 10:
    total += i
    i += 1
assert total == 45

n = 27
steps = 0
while n != 1:
    n = n // 2 if n % 2 == 0 else 3 * n + 1
    steps += 1
assert steps == 111

# while with break / continue / else
i = 0
found = None
while i < 100:
    i += 1
    if i % 2 == 0:
        continue
    if i * i > 50:
        found = i
        break
assert found == 9

i = 0
while i < 3:
    i += 1
else:
    i += 100
assert i == 103

i = 0
while i < 3:
    i += 1
    if i == 2:
        break
else:
    i += 100
assert i == 2

# for
total = 0
for i in range(5):
    total += i
assert total == 10
assert i == 4

for i in range(0):
    assert False
for i in range(3, 3):
    assert False
for i in range(5, 0):
    assert False

assert [i for i in range(1, 10, 3)] == [1, 4, 7]
assert [i for i in range(10, 0, -3)] == [10, 7, 4, 1]
assert [i for i in range(-3)] == []
assert list(range(5)) == [0, 1, 2, 3, 4]
assert len(range(0, 10, 3)) == 4

try:
    for i in range(0, 10, 0):
        pass
    assert False
except ValueError:
    pass

# the loop variable survives the loop and loops can be modified safely
for i in range(3):
    pass
assert i == 2

# nested loops with break / continue
pairs = []
for a in range(4):
    if a == 2:
        continue
    for b in range(4):
        if b > a:
            break
        pairs.append((a, b))
assert pairs == [(0, 0), (1, 0), (1, 1), (3, 0), (3, 1), (3, 2), (3, 3)]

# for ... else
def find(items, wanted):
    for i in range(len(items)):
        if items[i] == wanted:
            break
    else:
        return -1
    return i

assert find([5, 6, 7], 7) == 2
assert find([5, 6, 7], 8) == -1

# primes (loop else)
primes = []
for n in range(2, 30):
    for d in range(2, n):
        if n % d == 0:
            break
    else:
        primes.append(n)
assert primes == [2, 3, 5, 7, 11, 13, 17, 19, 23, 29]

# pass and empty bodies
if True:
    pass
for _ in range(2):
    pass
while False:
    pass

# tuple targets
total = 0
for a, b in [(1, 2), (3, 4)]:
    total += a * b
assert total == 14
for (a, b), c in [((1, 2), 3)]:
    assert (a, b, c) == (1, 2, 3)

# assignment forms
a = b = c = 7
assert (a, b, c) == (7, 7, 7)
a, b = 1, 2
a, b = b, a
assert (a, b) == (2, 1)
a, (b, c), d = 1, (2, 3), 4
assert (a, b, c, d) == (1, 2, 3, 4)
a, b = "xy"
assert (a, b) == ("x", "y")
a, b = [10, 20]
assert (a, b) == (10, 20)
lst = [0, 0, 0]
i = 0
i, lst[i] = 1, 9
assert i == 1 and lst == [0, 9, 0]
x = 10
x += 5
x -= 3
x *= 2
x //= 5
x **= 3
x %= 50
assert x == 14
s = "a"
s += "b"
s *= 2
assert s == "abab"
x = 7
x /= 2
assert x == 3.5
x = 6
x &= 3
x |= 8
x ^= 1
x <<= 2
x >>= 1
assert x == 22
lst = [1]
lst += [2, 3]
assert lst == [1, 2, 3]
lst *= 2
assert len(lst) == 6

# assert with a message
try:
    assert 1 == 2, "message"
    assert False
except AssertionError:
    pass
print("control ok")
