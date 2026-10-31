# Arithmetic, bitwise, comparison and sequence operators, with their errors.

def raises(exc, f, *args):
    try:
        f(*args)
    except exc:
        return True
    return False

# int arithmetic
assert 1 + 2 == 3
assert 1 - 2 == -1
assert 3 * 4 == 12
assert 7 // 2 == 3
assert -7 // 2 == -4
assert 7 % 3 == 1
assert -7 % 3 == 2
assert 2 ** 10 == 1024
assert 2 ** 0 == 1
assert 0 ** 0 == 1
assert (-3) ** 3 == -27
assert 2 ** -1 == 0.5
assert 7 / 2 == 3.5
assert 8 / 2 == 4.0
assert type(8 / 2) is type(4.0)
assert type(2 ** 2) is type(4)
assert type(2 ** -2) is type(0.5)
assert -(-5) == 5
assert abs(-5) == 5

# float arithmetic
assert 0.5 + 0.25 == 0.75
assert 1.5 * 2 == 3.0
assert 5.0 // 2 == 2.0
assert -5.0 // 2 == -3.0
assert 5.5 % 2 == 1.5
assert 2.0 ** 0.5 == 1.4142135623730951
assert 1e3 == 1000.0
assert 1.5e-3 == 0.0015
assert .5 == 0.5
assert 5. == 5.0
assert 0.1 + 0.2 != 0.3
assert abs(0.1 + 0.2 - 0.3) < 1e-9
assert 1 == 1.0 and 1.0 == 1
assert 3 + 0.5 == 3.5
assert type(3 + 0.5) is type(1.0)
assert type(True + 1) is type(1)
assert True + True == 2

# int literals
assert 0x10 == 16
assert 0xff == 255
assert 0o17 == 15
assert 0b101 == 5
assert 1_000 == 1000

# bitwise
assert 6 & 3 == 2
assert 6 | 3 == 7
assert 6 ^ 3 == 5
assert ~0 == -1
assert ~-1 == 0
assert 1 << 4 == 16
assert 256 >> 4 == 16
assert -16 >> 2 == -4
assert 5 & -2 == 4
assert True & False == 0
assert raises(TypeError, lambda: 1.5 & 1)
assert raises(TypeError, lambda: 1 << 1.0)
assert raises(ValueError, lambda: 1 << -1)

# division by zero
assert raises(ZeroDivisionError, lambda: 1 / 0)
assert raises(ZeroDivisionError, lambda: 1 // 0)
assert raises(ZeroDivisionError, lambda: 1 % 0)
assert raises(ZeroDivisionError, lambda: 1.5 / 0)
assert raises(ZeroDivisionError, lambda: 1.5 // 0.0)
assert raises(ZeroDivisionError, lambda: 1.5 % 0)
assert raises(ZeroDivisionError, lambda: 0 ** -1)

# strings
assert "ab" + "cd" == "abcd"
assert "ab" * 3 == "ababab"
assert 3 * "ab" == "ababab"
assert "ab" * 0 == ""
assert "ab" * -1 == ""
assert "a" < "b" and "a" <= "a" and "b" > "a" and "b" >= "b"
assert "abc" == "abc" and "abc" != "abd"
assert "b" in "abc" and "bc" in "abc" and "" in "abc" and "x" not in "abc"
assert raises(TypeError, lambda: "a" + 1)
assert raises(TypeError, lambda: 1 + "a")
assert raises(TypeError, lambda: "a" - "b")
assert raises(TypeError, lambda: "a" * "b")
assert raises(TypeError, lambda: "a" * 1.5)
assert raises(TypeError, lambda: 1 in "abc")
assert raises(TypeError, lambda: -"a")
assert raises(TypeError, lambda: "10" + 1)
assert raises(TypeError, lambda: "10" * "2")

# lists and tuples
assert [1, 2] + [3] == [1, 2, 3]
assert [1] * 3 == [1, 1, 1]
assert 2 * [1, 2] == [1, 2, 1, 2]
assert (1, 2) + (3,) == (1, 2, 3)
assert (1,) * 3 == (1, 1, 1)
assert [] + [] == []
assert 2 in [1, 2, 3] and 5 not in [1, 2, 3]
assert 2 in (1, 2, 3)
assert "a" in {"a": 1} and "b" not in {"a": 1}
assert 1 in range(3) and 3 not in range(3)
assert raises(TypeError, lambda: [1] + (2,))
assert raises(TypeError, lambda: (1,) + [2])
assert raises(TypeError, lambda: [1] + 1)
assert raises(TypeError, lambda: [1] * "a")
assert raises(TypeError, lambda: 1 in 1)
assert raises(TypeError, lambda: {} + {})

# the repeated list holds the same inner objects
inner = [0]
outer = [inner] * 3
inner[0] = 5
assert outer == [[5], [5], [5]]
outer[0] = [9]
assert outer == [[9], [5], [5]]

# operand errors
assert raises(TypeError, lambda: None + 1)
assert raises(TypeError, lambda: 1 + None)
assert raises(TypeError, lambda: {} - 1)
assert raises(TypeError, lambda: [] - [])
assert raises(TypeError, lambda: -None)
assert raises(TypeError, lambda: -[])
assert raises(TypeError, lambda: ~1.5)
assert raises(TypeError, lambda: ~"a")
assert raises(TypeError, lambda: (lambda: 1) + 1)

# comparison between incompatible types
assert raises(TypeError, lambda: 1 < "a")
assert raises(TypeError, lambda: "a" < 1)
assert raises(TypeError, lambda: [] < 1)
assert raises(TypeError, lambda: None < None)
assert raises(TypeError, lambda: {} < {})
assert raises(TypeError, lambda: (1,) < [1])
assert not (1 == "1") and (1 != "1")
assert not ([] == ()) and ([] != ())
assert not (None == False)

# identity
assert None is None
assert (None is not None) is False
x = [1]
y = x
assert x is y
assert x is not [1]
assert x == [1]
assert True is True
assert x is not None

# not / bool
assert (not True) is False
assert bool(0) is False and bool(1) is True and bool("") is False and bool([]) is False
assert bool(None) is False and bool("a") is True

# calls and subscripts on wrong types
one = 1
nothing = None
text = "a"
lst0 = []
assert raises(TypeError, lambda: nothing())
assert raises(TypeError, lambda: one())
assert raises(TypeError, lambda: text())
assert raises(TypeError, lambda: one[0])
assert raises(TypeError, lambda: nothing[0])
assert raises(TypeError, lambda: len(1))
assert raises(TypeError, lambda: len())
assert raises(TypeError, lambda: lst0[nothing])
assert raises(TypeError, lambda: lst0[1.0])
assert raises(TypeError, lambda: "abc"[text])
assert raises(TypeError, lambda: (1, 2)[text])
assert raises(TypeError, lambda: {}[[]])
assert raises(TypeError, lambda: {[1]: 2})
assert raises(TypeError, lambda: {(1, [2]): 3})
assert raises(AttributeError, lambda: (1).foo)
assert raises(AttributeError, lambda: "a".foo)
assert raises(AttributeError, lambda: [].foo)
assert raises(AttributeError, lambda: None.foo)

# int and float to string, as printed
assert str(1) == "1" and str(-1) == "-1" and str(1.0) == "1.0" and str(0.5) == "0.5"
assert str(1e20) == "1e+20" and str(1e16) == "1e+16" and str(1e15) == "1000000000000000.0"
assert str(1e-5) == "1e-05" and str(0.0001) == "0.0001"
assert str(123456789.125) == "123456789.125"
assert str(1 / 3) == "0.3333333333333333"
assert str(2 / 3) == "0.6666666666666666"
assert str(100.0) == "100.0"
assert str(-0.0) == "-0.0"
assert str(1e300 * 1e10) == "inf"
assert str(-1e300 * 1e10) == "-inf"
inf = 1e300 * 1e300
nan = inf - inf
assert str(nan) == "nan"
assert nan != nan
assert not (nan < 1) and not (nan > 1) and not (nan == nan)
assert inf > 10 ** 9 and -inf < -10 ** 9
assert str(True) == "True" and str(False) == "False" and str(None) == "None"
assert str(3) + str(4) == "34"
print(1 / 3, 2 / 3, 1e20, 1e-5, 0.1 + 0.2, 100.0, -0.0, 3.0e10, 12345.6789)
