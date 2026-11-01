# NOT run under CPython: `int` is a 32-bit integer here and an overflow raises
# OverflowError (CPython has unbounded integers).

def overflows(f):
    try:
        f()
    except OverflowError:
        return True
    return False

top = 2147483647
bottom = -top - 1
assert top + 0 == 2147483647 and bottom == -2147483648
assert overflows(lambda: top + 1)
assert overflows(lambda: bottom - 1)
assert overflows(lambda: top * 2)
assert overflows(lambda: -bottom)
assert overflows(lambda: abs(bottom))
assert overflows(lambda: 2 ** 31)
assert overflows(lambda: 2 ** 100)
assert overflows(lambda: 1 << 31)
assert overflows(lambda: int("2147483648"))
assert overflows(lambda: int(3e9))
assert overflows(lambda: sum([top, 1]))
assert overflows(lambda: bottom // -1)
assert top - 1 + 1 == top
assert float(top) + 1 == 2147483648.0
