# Base functions: print, assert, type, len, int, sorted, enumerate, zip, repr.
# (The Lua original tested pcall, xpcall, load, dofile, collectgarbage, _G,
# setmetatable, rawget/rawset/rawlen and select with negative indices. Python has
# no equivalents for those in this subset: the same ground is covered with the
# Python builtins, and errors are caught with try/except.)

def pcall(fn, *args):
    try:
        return True, fn(*args)
    except Exception as e:
        return False, type(e).__name__

# print
print()
print(11)
print("abc", 123, None, "pqr")
print(None and "T" or "F")
print(False and "T" or "F")
print(0 and "T" or "F")

# assert
def assert_value(v, msg="assertion failed"):
    assert v, msg
    return v

print("pcall(assert,true)", pcall(assert_value, True))
print("pcall(assert,false)", pcall(assert_value, False))
print("pcall(assert,None)", pcall(assert_value, None))
print('pcall(assert,true,"msg")', pcall(assert_value, True, "msg"))
print('pcall(assert,false,"msg")', pcall(assert_value, False, "msg"))
print('pcall(assert,None,"msg")', pcall(assert_value, None, "msg"))

# type
print("type(None)", type(None).__name__)
print('type("a")', type("a").__name__)
print("type(1)", type(1).__name__)
print("type(1.5)", type(1.5).__name__)
print("type(lambda)", type(lambda: None).__name__)
print("type({})", type({}).__name__)
print("type(True)", type(True).__name__)
print("type(False)", type(False).__name__)
print("type([])", type([]).__name__)
print("type(())", type(()).__name__)

# int, float, str, repr
print("int('1234567890')", int("1234567890"))
print("int(' 123 ')", int(" 123 "))
print("int(3.9)", int(3.9), "int(-3.9)", int(-3.9))
print("pcall(int,'abc')", pcall(int, "abc"))
print("pcall(int,None)", pcall(int, None))
print("float('1.5')", float("1.5"), "float(2)", float(2))
print("str(123)", str(123), "str(True)", str(True), "str(None)", str(None))
print("repr('abc')", repr("abc"), "repr(1.0)", repr(1.0))
print("repr(None)", repr(None), "repr([1,'a'])", repr([1, "a"]))

# len
print("len('Hello, world')", len("Hello, world"))
print("len('\\x00\\x00\\x00')", len("\x00\x00\x00"))
print("len({})", len({}), "len(())", len(()))

# enumerate, zip, sorted, reversed
for k, v in enumerate([]): print("enumerate1", k, v)
for k, v in enumerate(["one", "two"]): print("enumerate2", k, v)
for k, v in enumerate({"aa": "aaa", "bb": "bbb"}): print("enumerate3", k, v)
for k, v in zip([30, 20], ["x", "y"]): print("zip", k, v)
print("sorted", sorted([30, 20, 10]), sorted(["b", "a"]), sorted([]))
print("reversed", list(reversed([1, 2, 3])))

# min, max, sum, abs, round, divmod, pow
print("min/max", min(3, 1, 2), max(3, 1, 2), min([5]), max("ab"))
print("sum", sum([1, 2, 3]), sum([]), sum(range(4)))
print("abs", abs(-3), abs(2.5))
print("round", round(2.5), round(3.5), round(2.675, 2))
print("divmod", divmod(17, 5), divmod(-17, 5))
print("pow", pow(2, 10), pow(2, -1))

# isinstance, callable
print("isinstance", isinstance(1, int), isinstance("a", int), isinstance([], list))
print("callable", callable(print), callable(1))

# chr, ord, hex, bin, oct
print("chr/ord", chr(65), ord("A"), chr(ord("z")))
print("hex/bin/oct", hex(255), bin(5), oct(8))

# any, all, filter, map
print("any/all", any([0, 1]), all([1, 2]), any([]), all([]))
print("filter", list(filter(lambda x: x > 1, [1, 2, 3])))
print("map", list(map(lambda x: x * 2, [1, 2, 3])))

# functions as values, closures, default and keyword arguments
def make_adder(n):
    def add(x):
        return x + n
    return add
print("closure", make_adder(3)(4))
def kw(a, b=2, c=5):
    return (a, b, c)
print("defaults", kw(1), kw(1, 3), kw(1, c=6))

# exceptions raised by builtins
print("pcall(len,1)", pcall(len, 1))
print("pcall(lambda: 1/0)", pcall(lambda: 1 / 0))
print("pcall(list.index)", pcall(lambda: [1, 2].index(5)))

print("OK")
