# The math module: functions on regular, negative, zero and string-free inputs.
# (The Lua original evaluated expressions through load and used math.random with
# a seed; the subset has no random module, so the values are computed directly.
# Special values are printed as Python prints them, e.g. inf and nan.)

import math


def r(x):
    # The last digit of sin, acos, cosh, atan2... can differ by 1 ulp between the
    # C libm used by CPython and java.lang.Math, so floats are rounded to 12 digits.
    return round(x, 12) if isinstance(x, float) else x


print("platform", "python")

UNOPVALUES = [-2.5, -2, 0, 2, 2.5]

NUMBERPAIRS = [
    [2, 0], [-2.5, 0], [2, 1],
    [5, 2], [-5, 2], [16, 2], [-16, -2],
    [.5, 0], [.5, 1], [.5, 2], [.5, -1],
    [2.25, 0], [2.25, 2], [-2, 0],
    [3, 3],
]

print("---------- miscellaneous tests ----------")
print(r(math.sin(0.0)))
print(r(math.cos(math.pi)))
print(r(math.sqrt(9.0)))
print(math.floor(5.25), math.ceil(5.25))
print(math.trunc(-5.25))
print(-5 ** 2, -5 / 2, -5 % 2)

print("---------- constants ----------")
print(r(math.pi), r(math.e), r(math.tau))
print(math.inf, -math.inf)
print(math.isinf(math.inf), math.isnan(math.nan))

print("---------- unary operators ----------")
for v in UNOPVALUES:
    print(-v, not v)

print("---------- binary operators ----------")
for xy in NUMBERPAIRS:
    x, y = xy
    print(x + y, x - y, x * y, x / y if y != 0 else "zero division", x % y if y != 0 else "zero modulo")

print("---------- relational operators ----------")
for xy in NUMBERPAIRS:
    x, y = xy
    print(x == y, x != y, x > y, x < y, x >= y, x <= y)

print("---------- one-argument math functions ----------")
for x in UNOPVALUES:
    print("abs", abs(x), "floor", math.floor(x), "ceil", math.ceil(x))
    print("cos", r(math.cos(x)), "exp", r(math.exp(x)), "sin", r(math.sin(x)))
    print("degrees", r(math.degrees(x)), "radians", r(math.radians(x)))
    print("fabs", r(math.fabs(x)), "sqrt", r(math.sqrt(abs(x))))
for x in [0.5, 1, 2]:
    print("acos", r(math.acos(x / 4)), "asin", r(math.asin(x / 4)), "atan", r(math.atan(x)))
    print("cosh", r(math.cosh(x)), "sinh", r(math.sinh(x)), "tanh", r(math.tanh(x)))
    print("log", r(math.log(x)), "log10", r(math.log10(x)), "log2", r(math.log2(x)))

print("---------- two-argument math functions ----------")
for x, y in NUMBERPAIRS:
    print("fmod", r(math.fmod(x, y)) if y != 0 else "zero", "pow", r(math.pow(abs(x), y)) if x != 0 else 0)
    print("atan2", r(math.atan2(x, y)), "hypot", r(math.hypot(x, y)))
    print("copysign", r(math.copysign(x, y)))

print("---------- variadic math functions ----------")
print(max(4, -4.5, 5.5), min(4, -4.5, 5.5))
print(max(111, 222, 333), min(-222, -333, -111))
print(max(444, -111, -222), min(444, -111, -222))

print("---------- integer helpers ----------")
print(math.factorial(5), math.gcd(12, 18))
print(r(math.fsum([0.1, 0.2, 0.3])))

print("---------- Tests involving -0 and NaN ----------")
print("0 == -0", 0 == -0)
t = {0: 10, 20: 30}
print("t[-0] == t[0]", t[-0] == t[0])
x = -1
mz, z = 0.0 / x, 0.0
print("mz == z", mz == z)
a = {mz: 1}
print("a[z] == 1 and a[mz] == 1", a[z] == 1 and a[mz] == 1)
nan = math.nan
print("nan == nan", nan == nan, math.isnan(nan))
