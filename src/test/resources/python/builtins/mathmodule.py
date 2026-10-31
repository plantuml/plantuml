# The math module and the numeric builtins.
import math

def raises(exc, f, *args):
    try:
        f(*args)
    except exc:
        return True
    return False

def close(a, b):
    return abs(a - b) < 1e-9

# constants
assert close(math.pi, 3.141592653589793) and math.pi == 3.141592653589793
assert math.e == 2.718281828459045
assert math.inf > 1e308 and -math.inf < -1e308 and math.inf == math.inf
assert math.isnan(math.nan) and not math.isnan(1.0)
assert math.isinf(math.inf) and math.isinf(-math.inf) and not math.isinf(1e308)

# rounding
assert math.floor(2.7) == 2 and math.floor(-2.7) == -3 and math.floor(5) == 5
assert math.ceil(2.1) == 3 and math.ceil(-2.1) == -2 and math.ceil(5) == 5
assert math.trunc(2.7) == 2 and math.trunc(-2.7) == -2
assert type(math.floor(2.5)) is type(1) and type(math.ceil(2.5)) is type(1)
assert type(math.trunc(2.5)) is type(1)
assert math.floor(-0.5) == -1 and math.ceil(-0.5) == 0
assert round(2.5) == 2 and round(3.5) == 4 and round(-2.5) == -2 and round(2.6) == 3
assert round(2.675, 2) == 2.67 and round(1.234, 1) == 1.2 and round(1234, -2) == 1200
assert round(5) == 5 and type(round(2.5)) is type(1) and type(round(2.5, 1)) is type(1.0)
assert int(2.9) == 2 and int(-2.9) == -2

# abs / fabs / fmod
assert abs(-3) == 3 and abs(3) == 3 and abs(-3.5) == 3.5 and abs(0) == 0
assert math.fabs(-3) == 3.0 and type(math.fabs(-3)) is type(1.0)
assert math.fmod(7, 3) == 1.0 and math.fmod(-7, 3) == -1.0 and math.fmod(7, -3) == 1.0
assert math.fmod(5.5, 2) == 1.5
assert raises(ValueError, lambda: math.fmod(1, 0))

# powers and roots
assert math.sqrt(16) == 4.0 and math.sqrt(2) == 1.4142135623730951 and math.sqrt(0) == 0.0
assert raises(ValueError, lambda: math.sqrt(-1))
assert math.pow(2, 10) == 1024.0 and math.pow(2, 0.5) == 1.4142135623730951
assert math.pow(2, -2) == 0.25 and type(math.pow(2, 2)) is type(1.0)
assert pow(2, 10) == 1024 and pow(2, -1) == 0.5 and pow(2.0, 3) == 8.0
assert pow(3, 4, 5) == 1 and pow(2, 10, 1000) == 24
assert math.exp(0) == 1.0 and close(math.exp(1), math.e)
assert math.log(1) == 0.0 and close(math.log(math.e), 1.0)
assert close(math.log(8, 2), 3.0) and close(math.log(100, 10), 2.0)
assert math.log10(1000) == 3.0 and math.log10(1) == 0.0
assert raises(ValueError, lambda: math.log(0))
assert raises(ValueError, lambda: math.log(-1))
assert raises(ValueError, lambda: math.log10(0))

# trigonometry
assert math.sin(0) == 0.0 and math.cos(0) == 1.0 and math.tan(0) == 0.0
assert close(math.sin(math.pi / 2), 1.0) and close(math.cos(math.pi), -1.0)
assert close(math.sin(math.pi / 6), 0.5) and close(math.cos(math.pi / 3), 0.5)
assert close(math.tan(math.pi / 4), 1.0)
assert close(math.asin(1), math.pi / 2) and close(math.acos(1), 0.0) and close(math.acos(-1), math.pi)
assert close(math.atan(1), math.pi / 4)
assert close(math.atan2(1, 1), math.pi / 4) and close(math.atan2(-1, -1), -3 * math.pi / 4)
assert math.atan2(0, -1) == math.pi
assert raises(ValueError, lambda: math.asin(2))
assert raises(ValueError, lambda: math.acos(-2))
assert close(math.sin(1) ** 2 + math.cos(1) ** 2, 1.0)

# min / max / sum / divmod
assert min(1, 2.5) == 1 and max(1, 2.5) == 2.5 and min(-1, -1.0) == -1
assert max(1, 1.0) == 1 and type(max(1, 1.0)) is type(1)
assert min([4, 2, 8]) == 2 and max([4, 2, 8]) == 8
assert sum([0.5, 0.25, 0.25]) == 1.0
assert divmod(17, 5) == (3, 2) and divmod(-17, 5) == (-4, 3) and divmod(17.5, 5) == (3.0, 2.5)
assert raises(ZeroDivisionError, lambda: divmod(1, 0))

# conversions
assert int("42") == 42 and int("-42") == -42 and int("  7  ") == 7 and int("+5") == 5
assert int("ff", 16) == 255 and int("0xff", 16) == 255 and int("101", 2) == 5 and int("z", 36) == 35
assert int("0b11", 0) == 3 and int("0o17", 0) == 15 and int("0x1f", 0) == 31
assert int("1_000") == 1000
assert int(3.99) == 3 and int(-3.99) == -3 and int(True) == 1 and int() == 0
assert raises(ValueError, lambda: int("4.5"))
assert raises(ValueError, lambda: int(""))
assert raises(ValueError, lambda: int("abc"))
assert raises(ValueError, lambda: int("12", 2))
assert raises(TypeError, lambda: int(None))
assert raises(TypeError, lambda: int([1]))
assert raises(OverflowError, lambda: int(math.inf))
assert raises(ValueError, lambda: int(math.nan))
assert float("1.5") == 1.5 and float("  -2  ") == -2.0 and float("1e3") == 1000.0
assert float("inf") == math.inf and float("-inf") == -math.inf and math.isnan(float("nan"))
assert float("INF") == math.inf and float("Infinity") == math.inf
assert float(3) == 3.0 and type(float(3)) is type(1.0) and float() == 0.0 and float(True) == 1.0
assert float(".5") == 0.5 and float("5.") == 5.0 and float("1_0.5") == 10.5
assert raises(ValueError, lambda: float("abc"))
assert raises(ValueError, lambda: float(""))
assert raises(TypeError, lambda: float(None))
assert raises(TypeError, lambda: float([]))
assert bool(1) is True and bool(0.0) is False and bool(-1) is True and bool() is False
assert bool([]) is False and bool([0]) is True and bool("False") is True and bool({}) is False

# type / isinstance
assert type(1) is int and type(1.5) is float and type("a") is str and type(True) is bool
assert type([]) is list and type(()) is tuple and type({}) is dict and type(None) is not int
assert type(1) == int and type(1) != float and type(range(1)) is range
assert isinstance(1, int) and isinstance(True, int) and not isinstance(1.5, int)
assert isinstance(1.5, float) and not isinstance(1, float)
assert isinstance("a", str) and isinstance([], list) and isinstance((), tuple) and isinstance({}, dict)
assert isinstance(True, bool) and not isinstance(1, bool)
assert isinstance(1, (str, int)) and not isinstance(1, (str, list))
assert not isinstance(None, int) and not isinstance("a", list)
assert isinstance(ValueError("x"), ValueError) and isinstance(ValueError("x"), Exception)
assert isinstance(range(3), range)
assert raises(TypeError, lambda: isinstance(1, 5))

# bools are ints
assert True + True == 2 and True * 5 == 5 and sum([True, True, False]) == 2
assert [10, 20][True] == 20
assert str(True) == "True" and int(True) == 1 and True == 1 and False == 0
assert (1 < 2) + (2 < 3) == 2
assert math.floor(True) == 1

# integers at the limits of a 32-bit int
big = 2147483647
assert big == 0x7fffffff and big + 0 == 2147483647
assert -big - 1 == -2147483648
assert big // 2 == 1073741823 and big % 10 == 7
assert str(big) == "2147483647" and int("2147483647") == big and int("-2147483648") == -big - 1
assert float(big) == 2147483647.0
assert big * 1.0 + 1 == 2147483648.0
assert big & 0xff == 255 and big >> 30 == 1 and 1 << 30 == 1073741824
assert hex(255) == "0xff" and hex(-255) == "-0xff" and oct(8) == "0o10" and bin(5) == "0b101" and bin(-5) == "-0b101"
print(math.pi, math.sqrt(2), math.floor(-0.5), round(2.5), round(2.675, 2), 7 / 3)
