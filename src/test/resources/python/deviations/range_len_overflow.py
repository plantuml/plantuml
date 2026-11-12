# NOT run under CPython: `int` is a 32-bit integer here, so the length of a
# range that does not fit raises OverflowError (CPython gives 4294967295).
# VM at 148134d: the length wraps around, len(range(-2147483648, 2147483647))
# is -1 and len(range(-2147483648, 0)) is -2147483648.

def overflows(f):
    try:
        f()
    except OverflowError:
        return True
    return False

top = 2147483647
bottom = -top - 1
assert len(range(top)) == top
assert len(range(bottom, -1)) == top
assert len(range(bottom, top, 2)) == top
assert len(range(top, bottom, -2)) == top
assert len(range(bottom, top, 3)) == 1431655765
assert overflows(lambda: len(range(bottom, top)))
assert overflows(lambda: len(range(bottom, 0)))
assert overflows(lambda: len(range(top, bottom, -1)))
assert overflows(lambda: len(range(-1, top)))
assert overflows(lambda: len(range(0, bottom, -1)))
# what does not need the length still works
r = range(bottom, top)
assert r[0] == bottom and r[1] == bottom + 1 and r[-1] == top - 1
assert 0 in r and top not in r
assert bool(r)
n = 0
for i in r:
    n += 1
    if n == 3:
        break
assert i == bottom + 2
