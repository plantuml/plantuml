# Regression: the math module must be importable and usable at top level.
# (The Lua original called math.randomseed(0), which has no Python equivalent in
# the subset: the random module is not supported, so the test checks math itself.)
import math

assert math.pi > 3
assert math.floor(2.5) == 2
