# The math module: functions on regular, negative, zero and string-free inputs.
# (The Lua original evaluated expressions through load and used math.random with
# a seed; the subset has no random module, so the values are computed directly.
# Special values are printed as Python prints them, e.g. inf and nan.)

import math

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
print(math.sin(0.0))
print(math.cos(math.pi))
print(math.sqrt(9.0))
print(math.floor(5.25), math.ceil(5.25))
print(math.trunc(-5.25))
print(-5 ** 2, -5 / 2, -5 % 2)

print("---------- constants ----------")
print(math.pi, math.e, math.tau)
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
    print("cos", math.cos(x), "exp", math.exp(x), "sin", math.sin(x))
    print("degrees", math.degrees(x), "radians", math.radians(x))
    print("fabs", math.fabs(x), "sqrt", math.sqrt(abs(x)))
for x in [0.5, 1, 2]:
    print("acos", math.acos(x / 4), "asin", math.asin(x / 4), "atan", math.atan(x))
    print("cosh", math.cosh(x), "sinh", math.sinh(x), "tanh", math.tanh(x))
    print("log", math.log(x), "log10", math.log10(x), "log2", math.log2(x))

print("---------- two-argument math functions ----------")
for x, y in NUMBERPAIRS:
    print("fmod", math.fmod(x, y) if y != 0 else "zero", "pow", math.pow(abs(x), y) if x != 0 else 0)
    print("atan2", math.atan2(x, y), "hypot", math.hypot(x, y))
    print("copysign", math.copysign(x, y))

print("---------- variadic math functions ----------")
print(max(4, -4.5, 5.5), min(4, -4.5, 5.5))
print(max(111, 222, 333), min(-222, -333, -111))
print(max(444, -111, -222), min(444, -111, -222))

print("---------- integer helpers ----------")
print(math.factorial(5), math.gcd(12, 18))
print(math.fsum([0.1, 0.2, 0.3]))

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
