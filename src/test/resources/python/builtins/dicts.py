# dict: keys, methods, ordering, equality.

def raises(exc, f, *args):
    try:
        f(*args)
    except exc:
        return True
    return False

d = {}
assert len(d) == 0 and not d
d["a"] = 1
d["b"] = 2
d[3] = "three"
d[(1, 2)] = "tuple"
d[2.5] = "float"
d[None] = "none"
d[True] = "true"
assert len(d) == 7
assert d["a"] == 1 and d[3] == "three" and d[(1, 2)] == "tuple" and d[2.5] == "float"
assert d[None] == "none" and d[True] == "true"
assert d[1] == "true"        # True == 1
assert 3 in d and "z" not in d and (1, 2) in d and None in d

# int and float keys that are equal are the same key
n = {}
n[1] = "int"
n[1.0] = "float"
assert len(n) == 1 and n[1] == "float"
assert list(n.keys()) == [1]
n[0.5] = "half"
assert n[0.5] == "half"

# keys must be hashable
assert raises(TypeError, lambda: {}[[1]])
assert raises(TypeError, lambda: {}.get([1]))
assert raises(TypeError, lambda: {[1]: 0})
assert raises(TypeError, lambda: {{}: 0})
assert raises(TypeError, lambda: [1] in {})

# lookup
assert d.get("a") == 1
assert d.get("zz") is None
assert d.get("zz", 0) == 0
assert d.get("a", 0) == 1
assert raises(KeyError, lambda: d["zz"])

# delete
del d["a"]
assert "a" not in d
assert raises(KeyError, lambda: d["a"])
def del_missing():
    del d["a"]
assert raises(KeyError, del_missing)
assert d.pop("b") == 2
assert d.pop("b", "dflt") == "dflt"
assert raises(KeyError, lambda: d.pop("b"))
assert d.pop(3) == "three"
assert len(d) == 4

# setdefault / update
s = {}
assert s.setdefault("k", []) == []
s["k"].append(1)
assert s.setdefault("k", [9]) == [1]
assert s == {"k": [1]}
assert s.setdefault("n") is None and s["n"] is None
u = {"a": 1, "b": 2}
assert u.update({"b": 3, "c": 4}) is None
assert u == {"a": 1, "b": 3, "c": 4}
u.update([("d", 5)])
u.update(e=6)
assert u == {"a": 1, "b": 3, "c": 4, "d": 5, "e": 6}
assert list(u) == ["a", "b", "c", "d", "e"]

# keys / values / items are live views
v = {"x": 1}
ks = v.keys()
vs = v.values()
its = v.items()
v["y"] = 2
assert list(ks) == ["x", "y"] and list(vs) == [1, 2] and list(its) == [("x", 1), ("y", 2)]
assert len(ks) == 2 and len(vs) == 2 and len(its) == 2
assert "x" in ks and ("x", 1) in its and 2 in vs
assert sorted(v.keys()) == ["x", "y"]
assert sum(v.values()) == 3

# copy / clear / equality
c = u.copy()
c["a"] = 100
assert u["a"] == 1
shared = {"l": [1]}
cp = shared.copy()
cp["l"].append(2)
assert shared["l"] == [1, 2]
u.clear()
assert u == {} and len(u) == 0
assert {"a": 1, "b": 2} == {"b": 2, "a": 1}
assert {"a": 1} != {"a": 2} and {"a": 1} != {"b": 1} and {} != {"a": 1}
assert {"a": [1, {"b": 2}]} == {"a": [1, {"b": 2}]}
assert {1: "a"} == {1.0: "a"}

# order is the insertion order
o = {}
for k in ["delta", "alpha", "charlie", "bravo"]:
    o[k] = len(o)
assert list(o.keys()) == ["delta", "alpha", "charlie", "bravo"]
del o["alpha"]
o["echo"] = 4
o["alpha"] = 5
assert list(o.keys()) == ["delta", "charlie", "bravo", "echo", "alpha"]
assert list(o.values()) == [0, 2, 3, 4, 5]
assert list(sorted(o.items()))[0] == ("alpha", 5)
assert [k for k in o] == list(o.keys())

# many entries, growth and removal
big = {}
for i in range(1000):
    big[i] = i * i
assert len(big) == 1000 and big[999] == 998001 and big[0] == 0
for i in range(0, 1000, 2):
    del big[i]
assert len(big) == 500
assert list(big.keys())[:3] == [1, 3, 5]
assert sum(big.values()) == sum([i * i for i in range(1, 1000, 2)])
for i in range(1000):
    big.pop(i, None)
assert big == {}
strs = {}
for i in range(300):
    strs["key" + str(i)] = i
assert strs["key299"] == 299 and len(strs) == 300
assert list(strs)[:2] == ["key0", "key1"]

# nested structures
tree = {"name": "root", "children": [{"name": "a", "children": []},
                                      {"name": "b", "children": [{"name": "c", "children": []}]}]}
def names(node):
    out = [node["name"]]
    for ch in node["children"]:
        out.extend(names(ch))
    return out
assert names(tree) == ["root", "a", "b", "c"]

# counting and grouping
text = "one two three two three three"
freq = {}
for w in text.split():
    freq[w] = freq.get(w, 0) + 1
assert freq == {"one": 1, "two": 2, "three": 3}
by_len = {}
for w in text.split():
    by_len.setdefault(len(w), []).append(w)
assert by_len == {3: ["one", "two", "two"], 5: ["three", "three", "three"]}
assert sorted(freq.items(), key=lambda kv: -kv[1])[0] == ("three", 3)
assert max(freq, key=lambda k: freq[k]) == "three"

# dict()
assert dict(a=1) == {"a": 1}
assert dict([("a", 1)], b=2) == {"a": 1, "b": 2}
assert dict(zip(["x", "y"], [1, 2])) == {"x": 1, "y": 2}
assert dict({"a": 1}) is not None
assert raises(TypeError, lambda: dict([1, 2, 3]))
assert raises(ValueError, lambda: dict([(1, 2, 3)]))
assert raises(TypeError, lambda: dict(5))

# unpacking
def kw(**kwargs):
    return kwargs
assert kw(**{"a": 1, "b": 2}) == {"a": 1, "b": 2}
assert raises(TypeError, lambda: kw(**[1]))
print("dicts ok")
