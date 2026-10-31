# Argument-type tests for the list and dict operations. Lua's table library maps
# onto Python's list and dict methods: concat -> str.join, insert -> list.insert,
# remove -> list.pop, sort -> list.sort, unpack -> slicing, and t[k] / t[k]=v.
# Lua's table.maxn has no counterpart and is not tested.

def product(lists):
    result = [[]]
    for choices in lists:
        new = []
        for prefix in result:
            for v in choices:
                new.append(prefix + [v])
        result = new
    return result

def outcome(f, args):
    try:
        f(*args)
        return "ok"
    except Exception as e:
        return type(e).__name__

def banner(name):
    print("-------- " + name)

def checkallpass(name, f, lists):
    combos = product(lists)
    assert len(combos) > 0, name
    for args in combos:
        r = outcome(f, args)
        assert r == "ok", name + " raised " + r
    print(name + ": " + str(len(combos)) + " calls ok")

def checkallerrors(name, f, lists, expected):
    combos = product(lists)
    assert len(combos) > 0, name
    for args in combos:
        r = outcome(f, args)
        assert r == expected, name + " raised " + r + ", expected " + expected
    print(name + ": " + str(len(combos)) + " calls raised " + expected)

anylua = [None, False, True, 0, 1.5, "abc", [1, 2], {"k": 1}]
somestringstable = [["a", "b", "c"]]
somenonstringtable = [[True, False]]
notatable = [None, 1, 1.5]

def tconcat(t, sep):
    return sep.join(t)

def lappend(t, v):
    t.append(v)

def linsert(t, i, v):
    t.insert(i, v)

def lpop_at(i):
    t = [1, 2, 3]
    return t.pop(i)

def lpop_t(t, i):
    return t.pop(i)

def lsort(t):
    t.sort()

def tget(t, k):
    return t[k]

def tset(t, k, v):
    t[k] = v

def tunpack(t, i, j):
    return t[i:j]

banner("table.concat (join)")
checkallpass("join", tconcat, [somestringstable, [",", "-", ""]])
checkallerrors("join-notsep", tconcat, [somestringstable, [1.23, None, True]], "AttributeError")
checkallerrors("join-notitems", tconcat, [notatable, [","]], "TypeError")
checkallerrors("join-nonstring-items", tconcat, [somenonstringtable, [","]], "TypeError")

banner("table.insert (append)")
checkallpass("append", lappend, [[[1, 2]], anylua])
checkallerrors("append-notlist", lappend, [[None, "abc", {"k": 1}], anylua], "AttributeError")

banner("table.insert (insert)")
checkallpass("insert", linsert, [[[1, 2]], [0, 1, -1, 10], anylua])
checkallerrors("insert-notindex", linsert, [[[1, 2]], ["x", None, 1.5], anylua], "TypeError")

banner("table.remove (pop)")
checkallpass("pop-at", lpop_at, [[0, 1, -1]])
checkallerrors("pop-notindex", lpop_at, [[None, "x"]], "TypeError")
checkallerrors("pop-out-of-range", lpop_at, [[5]], "IndexError")
checkallerrors("pop-notlist", lpop_t, [[None, "abc", 1], [0]], "AttributeError")

banner("table.sort")
checkallpass("sort", lsort, [[[3, 1, 2], ["b", "a"]]])
checkallerrors("sort-mixed", lsort, [[[1, "a"]]], "TypeError")
checkallerrors("sort-notlist", lsort, [[None, "abc", 1]], "AttributeError")

banner("table get - t[k]")
checkallpass("table get", tget, [[{"k": 1}], ["k"]])
checkallerrors("table get-missing", tget, [[{"k": 1}], ["zz"]], "KeyError")
checkallerrors("table get-unhashable", tget, [[{"k": 1}], [[1, 2]]], "TypeError")

banner("table set - t[k]=v")
checkallpass("table set", tset, [[{}], ["k", 2], anylua])
checkallerrors("table set-unhashable", tset, [[{}], [[1]], anylua], "TypeError")

banner("table.unpack (slice)")
checkallpass("unpack", tunpack, [[[1, 2, 3]], [0, 1], [1, None]])
checkallerrors("unpack-notsequence", tunpack, [[None, 1], [0], [1]], "TypeError")
checkallerrors("unpack-notindex", tunpack, [[[1, 2, 3]], ["x", 1.5], [1]], "TypeError")

print("OK")
