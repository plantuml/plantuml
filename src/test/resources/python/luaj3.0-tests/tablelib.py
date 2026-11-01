# Lists and dicts: insertion, removal, joining, sorting, slicing, unpacking.
# (The Lua original used table.insert/remove/concat/sort/unpack, the "#" border
# of tables with holes and metatables. Python lists have no holes, so the same
# operations are written with list methods and the results are checked as Python
# defines them.)

def show(t):
    if isinstance(t, dict):
        items = sorted([repr(k) + ": " + repr(v) for k, v in t.items()])
        return "{" + ", ".join(items) + "}"
    return "[" + ", ".join([repr(v) for v in t]) + "]"

print("-- insert, len tests")
t = ["one", "two", "three"]
print(show(t), len(t))
t.append("six")
print(show(t), len(t))
t.insert(0, "seven")
print(show(t), len(t))
t.insert(3, "eight")
print(show(t), len(t))
t.insert(6, "nine")
print(show(t), len(t))
t.insert(9, "ten")
print(show(t), len(t))
print("#[]", len([]))
print('#["a"]', len(["a"]))
print('#["a","b"]', len(["a", "b"]))
print('#["a",None]', len(["a", None]))
print("#[None,None]", len([None, None]))
print('#[None,"b"]', len([None, "b"]))
print('#["a","b","c"]', len(["a", "b", "c"]))

print("-- remove tests")
t = ["one", "two", "three", "four", "five", "six", "seven", "ten"]
print(show(t), len(t))
print("pop()", t.pop(), show(t))
print("pop(0)", t.pop(0), show(t))
print("pop(2)", t.pop(2), show(t))
print("pop(4)", t.pop(4), show(t))
print("pop(-1)", t.pop(-1), show(t))
print("remove('three')", t.remove("three"), show(t))
print("pop(-1)", t.pop(-1), show(t), len(t))

print("-- concat tests")
def tryconcat(t):
    print(repr("".join(t)))
    print(repr("--".join(t)))
    print(repr(",".join(t[1:])))
    print(repr(",".join(t[1:2])))
    print(repr(",".join(t[4:2])))
tryconcat(["one", "two", "three", "four", "five"])
tryconcat(["a", "b", "c", "d", "e"])
tryconcat([])

print("-- sort tests")
def sorttest(t, key=None):
    print("-".join(t))
    if key is None:
        t.sort()
    else:
        t.sort(key=key)
    print("-".join(t))

sorttest(["one", "two", "three"])
sorttest(["www", "vvv", "uuu", "ttt", "sss", "zzz", "yyy", "xxx"])
sorttest(["www", "vvv", "uuu", "ttt", "sss", "zzz", "yyy", "xxx"], lambda s: s[::-1])
sorttest(["bb", "a", "ccc"], len)

print("----- unpack tests -------")
def a(*args): return args
print("unpack -", *[])
print("unpack a", *["a"])
print("unpack ab", *["a", "b"])
print("unpack .b", *[None, "b"])
print("unpack a.", *["a", None])
print("unpack abc", *["a", "b", "c"])
print("unpack (-)", *a())
print("unpack (a)", *a("a"))
print("unpack (ab)", *a("a", "b"))
print("unpack (.b)", *a(None, "b"))
t = ["aa", "bb", "cc", "dd", "ee", "ff"]
print("slices", t[1:], t[1:3], t[2:5], t[-2:], t[::2])
print("empty slices", t[10:], t[3:1], t[-1:-2])
print("index errors:")
for i in [0, 5, 6, -1, -7]:
    try:
        print(i, t[i])
    except IndexError:
        print(i, "IndexError")

print("----- misc list initializer tests -------")
print(len(["abc", "def", "ghi", None]))
print(len(["abc", "def", "ghi", False]))
print(len(["abc", "def", "ghi", 0]))

print("----- dict operations -------")
d = {}
print("empty", show(d), len(d))
d["a"] = "b"
d[1] = 2
d[None] = "d"
print(show(d), len(d))
print("get", d.get("a"), d.get("zz"), d.get("zz", "default"))
print("in", "a" in d, "zz" in d, None in d)
del d[None]
print(show(d), len(d))
print("pop", d.pop(1), show(d))
print("setdefault", d.setdefault("c", "ccc"), show(d))
print("keys", sorted(d.keys()), "values", sorted(d.values()))

print("OK")
