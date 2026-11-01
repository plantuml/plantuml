# Integer keys at the limits of a 32-bit int, in a dict.
a = {}
a[2147483647] = 10
a[1073741824] = 11
a[-2147483647 - 1] = 12
a[0] = 13
a[-1] = 14
assert a[2147483647] == 10
assert a[1073741824] == 11
assert a[-2147483647 - 1] == 12
assert a[0] == 13
assert a[-1] == 14
assert len(a) == 5
assert list(a.keys()) == [2147483647, 1073741824, -2147483648, 0, -1]
