# Module tests. Lua's require() and package.seeall have no equivalent in this
# subset (user modules cannot be imported), so what is tested is the builtin
# math module: it can be imported, and reading a missing attribute fails.
import math

print("-------- import math")
assert math.pi > 3

print("-------- missing attribute")
try:
    x = math.nosuchfunction
    print("unexpected: " + str(x))
except AttributeError:
    print("AttributeError")

print("OK")
