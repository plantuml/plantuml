# Floor modulo and division: the sign follows the divisor.
assert 4 % 3 == 1
assert -4 % 3 == 2
assert 4 % -3 == -2
assert -4 % -3 == -1
assert 4 // 3 == 1
assert -4 // 3 == -2
assert 4 // -3 == -2
assert -4 // -3 == 1
assert 4.5 % 2 == 0.5
assert -4.5 % 2 == 1.5
assert 4.5 // 2 == 2.0
assert -4.5 // 2 == -3.0
assert 7 % 7 == 0
assert 0 % 5 == 0
assert divmod(-7, 2) == (-4, 1)
assert divmod(7, -2) == (-4, -1)
assert 7 / 2 == 3.5
assert 4 / 2 == 2.0
assert type(4 / 2) == type(1.0)
assert type(4 // 2) == type(1)
for a in range(-6, 7):
    for b in (-4, -3, -2, -1, 1, 2, 3, 4):
        assert (a // b) * b + a % b == a
        assert a % b == 0 or (a % b < 0) == (b < 0)
