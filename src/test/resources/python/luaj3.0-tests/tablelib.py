def func(t, *args):
    return (*args)
tbl = setmetatable({}, {"__index": func})
print(tbl[2])


# tostring replacement that assigns ids
ts, id, nid, types = tostring, {}, 0, {"table": "tbl", "thread": "thr", "userdata": "uda", "function": "func"}

def tostring(x):
    global nid
    if not x or not types[type(x)]:
        return ts(x)
    if not id[x]:
        nid += 1
        id[x] = types[type(x)] .. "." .. nid
    return id[x]

t = {"one", "two", "three", "a": "aaa", "b": "bbb", "c": "ccc"}

table.insert(t, "six")
table.insert(t, 1, "seven")
table.insert(t, 4, "eight")
table.insert(t, 7, "nine")
table.insert(t, 10, "ten")
print(len(t))

# concat
print("-- concat tests")
def tryconcat(t):
    print(table.concat(t))
    print(table.concat(t, "--"))
    print(table.concat(t, ",", 2))
    print(table.concat(t, ",", 2, 2))
    print(table.concat(t, ",", 5, 2))
tryconcat({"one", "two", "three", "a": "aaa", "b": "bbb", "c": "ccc"})
tryconcat(["one", "two", "three", "four", "five"])
def tryconcat(t):
    print(table.concat(t))
    print(table.concat(t, "--"))
    print(table.concat(t, ",", 2))
tryconcat({"a": "aaa", "b": "bbb", "c": "ccc", "d": "ddd", "e": "eee"})
tryconcat({501: "one", 502: "two", 503: "three", 504: "four", 505: "five"})
tryconcat([])

# print the elements of a table in a platform-independent way
def eles(t, f):
    f = f or pairs
    all = []
    for k, v in f(t):
        table.insert(all, "[" .. tostring(k) .. "]=" .. tostring(v))
    table.sort(all)
    return "{" .. table.concat(all, ",") .. "}"

# insert, len
print("-- insert, len tests")
t = {"one", "two", "three", "a": "aaa", "b": "bbb", "c": "ccc"}

print(eles(t), len(t))
table.insert(t, "six")
print(eles(t), len(t))
table.insert(t, 1, "seven")
print(eles(t), len(t))
table.insert(t, 4, "eight")
print(eles(t), len(t))
table.insert(t, 7, "nine")
print(eles(t), len(t))
table.insert(t, 10, "ten")
print(eles(t), len(t))
print("#{}", len([]))
print('#{"a"}', len(["a"]))
print('#{"a","b"}', len(["a", "b"]))
print('#{"a",nil}', len(["a", None]))
print("#{nil,nil}", len([None, None]))
print('#{nil,"b"}', len([None, "b"]) == 0 or len([None, "b"]) == 2)
print('#{"a","b","c"}', len(["a", "b", "c"]))
print('#{"a","b",nil}', len(["a", "b", None]))
print('#{"a",nil,nil}', len(["a", None, None]))
print("#{nil,nil,nil}", len([None, None, None]))
print('#{nil,nil,"c"}', len([None, None, "c"]) == 0 or len([None, None, "c"]) == 3)
print('#{nil,"b","c"}', len([None, "b", "c"]) == 0 or len([None, "b", "c"]) == 3)
print('#{nil,"b",nil}', len([None, "b", None]) == 0 or len([None, "b", None]) == 2)
print('#{"a",nil,"c"}', len(["a", None, "c"]) == 1 or len(["a", None, "c"]) == 3)

# remove
print("-- remove tests")
t = {"one", "two", "three", "four", "five", "six", "seven", 10: "ten", "a": "aaa", "b": "bbb", "c": "ccc"}
print(eles(t), len(t))
print("table.remove(t)", table.remove(t))
print(eles(t), len(t))
print("table.remove(t,1)", table.remove(t, 1))
print(eles(t), len(t))
print("table.remove(t,3)", table.remove(t, 3))
print(eles(t), len(t))
print("table.remove(t,5)", table.remove(t, 5))
print(eles(t), len(t))
print("table.remove(t,10)", table.remove(t, 10))
print(eles(t), len(t))
print("table.remove(t,-1)", table.remove(t, -1))
print(eles(t), len(t))
print("table.remove(t,-1)", table.remove(t, -1))
print(eles(t), len(t))

# sort
print("-- sort tests")
def sorttest(t, f):
    t = (t)
    print(table.concat(t, "-"))
    if f:
        table.sort(t, f)
    else:
        table.sort(t)
    print(table.concat(t, "-"))

sorttest(["one", "two", "three"])
sorttest(["www", "vvv", "uuu", "ttt", "sss", "zzz", "yyy", "xxx"])
sorttest(["www", "vvv", "uuu", "ttt", "sss", "zzz", "yyy", "xxx"], lambda a, b: b < a)

# pairs, ipairs
#def testpairs(f, t, name):
#    print(name)
#    for a, b in f(t):
#        print(" ", a, b)
#def testbothpairs(t):
#    testpairs(pairs, t, "pairs( " .. eles(t) .. " )")
#    testpairs(ipairs, t, "ipairs( " .. eles(t) .. " )")
#for i, t in ipairs([t0, t1, t2, t3]):
#    testbothpairs(t)
#
#t = ["one", "two", "three", "four", "five"]
#testbothpairs(t)
#t[6] = "six"
#testbothpairs(t)
#t[4] = None
#testbothpairs(t)

# tests of setlist table constructors
# length is tested elsewhere
print("----- unpack tests -------")
unpack = table.unpack
print("pcall(unpack)", (pcall(unpack)))
print("pcall(unpack,nil)", (pcall(unpack, None)))
print('pcall(unpack,"abc")', (pcall(unpack, "abc")))
print("pcall(unpack,1)", (pcall(unpack, 1)))
print('unpack({"aa"})', unpack(["aa"]))
print('unpack({"aa","bb"})', unpack(["aa", "bb"]))
print('unpack({"aa","bb","cc"})', unpack(["aa", "bb", "cc"]))
def a(*args): return *args
print("unpack -", unpack([]))
print("unpack a", unpack(["a"]))
print("unpack .", unpack([None], 1, 1))
print("unpack ab", unpack(["a", "b"]))
print("unpack .b", unpack([None, "b"], 1, 2))
print("unpack a.", unpack(["a", None], 1, 2))
print("unpack abc", unpack(["a", "b", "c"]))
print("unpack .ab", unpack([None, "a", "b"], 1, 3))
print("unpack a.b", unpack(["a", None, "b"], 1, 3))
print("unpack ab.", unpack(["a", "b", None], 1, 3))
print("unpack ..b", unpack([None, None, "b"], 1, 3))
print("unpack a..", unpack(["a", None, None], 1, 3))
print("unpack .b.", unpack([None, "b", None], 1, 3))
print("unpack ...", unpack([None, None, None], 1, 3))
print("unpack (-)", unpack([a()]))
print("unpack (a)", unpack([a("a")]))
print("unpack (.)", unpack([a(None)], 1, 1))
print("unpack (ab)", unpack([a("a", "b")]))
print("unpack (.b)", unpack([a(None, "b")], 1, 2))
print("unpack (a.)", unpack([a("a", None)], 1, 2))
print("unpack (abc)", unpack([a("a", "b", "c")]))
print("unpack (.ab)", unpack([a(None, "a", "b")], 1, 3))
print("unpack (a.b)", unpack([a("a", None, "b")], 1, 3))
print("unpack (ab.)", unpack([a("a", "b", None)], 1, 3))
print("unpack (..b)", unpack([a(None, None, "b")], 1, 3))
print("unpack (a..)", unpack([a("a", None, None)], 1, 3))
print("unpack (.b.)", unpack([a(None, "b", None)], 1, 3))
print("unpack (...)", unpack([a(None, None, None)], 1, 3))
t = ["aa", "bb", "cc", "dd", "ee", "ff"]
print("pcall(unpack,t)", pcall(unpack, t))
print("pcall(unpack,t,2)", pcall(unpack, t, 2))
print("pcall(unpack,t,2,5)", pcall(unpack, t, 2, 5))
print("pcall(unpack,t,2,6)", pcall(unpack, t, 2, 6))
print("pcall(unpack,t,2,7)", pcall(unpack, t, 2, 7))
print("pcall(unpack,t,1)", pcall(unpack, t, 1))
print("pcall(unpack,t,1,5)", pcall(unpack, t, 1, 5))
print("pcall(unpack,t,1,6)", pcall(unpack, t, 1, 6))
print("pcall(unpack,t,1,7)", pcall(unpack, t, 1, 7))
print("pcall(unpack,t,0)", pcall(unpack, t, 0))
print("pcall(unpack,t,0,5)", pcall(unpack, t, 0, 5))
print("pcall(unpack,t,0,6)", pcall(unpack, t, 0, 6))
print("pcall(unpack,t,0,7)", pcall(unpack, t, 0, 7))
print("pcall(unpack,t,-1)", pcall(unpack, t, -1))
print("pcall(unpack,t,-1,5)", pcall(unpack, t, -1, 5))
print("pcall(unpack,t,-1,6)", pcall(unpack, t, -1, 6))
print("pcall(unpack,t,-1,7)", pcall(unpack, t, -1, 7))
print("pcall(unpack,t,2,4)", pcall(unpack, t, 2, 4))
print("pcall(unpack,t,2,5)", pcall(unpack, t, 2, 5))
print("pcall(unpack,t,2,6)", pcall(unpack, t, 2, 6))
print("pcall(unpack,t,2,7)", pcall(unpack, t, 2, 7))
print("pcall(unpack,t,2,8)", pcall(unpack, t, 2, 8))
print("pcall(unpack,t,2,2)", pcall(unpack, t, 2, 0))
print("pcall(unpack,t,2,1)", pcall(unpack, t, 2, 0))
print("pcall(unpack,t,2,0)", pcall(unpack, t, 2, 0))
print("pcall(unpack,t,2,-1)", pcall(unpack, t, 2, -1))
t[0] = "zz"
t[-1] = "yy"
t[-2] = "xx"
print("pcall(unpack,t,0)", pcall(unpack, t, 0))
print("pcall(unpack,t,2,0)", pcall(unpack, t, 2, 0))
print("pcall(unpack,t,2,-1)", pcall(unpack, t, 2, -1))
print('pcall(unpack,t,"3")', pcall(unpack, t, "3"))
print('pcall(unpack,t,"a")', (pcall(unpack, t, "a")))
print("pcall(unpack,t,function() end)", (pcall(unpack, t, lambda: None)))

# misc tests
print("----- misc table initializer tests -------")
print(len(["abc", "def", "ghi", None]))  # should be 3 !
print(len(["abc", "def", "ghi", False]))  # should be 4 !
print(len(["abc", "def", "ghi", 0]))  # should be 4 !

# basic table operation tests
print("----- basic table operations -------")

def dummyfunc(t, *args):
    print("metatable call args", type(t), *args)
    return "dummy"
def makeloud(t):
    def index(t, k):
        print("__index", type(t), k)
        return rawset(t, k)
    def newindex(t, k, v):
        print("__newindex", type(t), k, v)
        rawset(t, k, v)
    return setmetatable(t, {"__index": index, "__newindex": newindex})
tests = [
    ["basic table", {}],
    ["function metatable on __index", setmetatable({}, {"__index": dummyfunc})],
    ["function metatable on __newindex", setmetatable({}, {"__newindex": dummyfunc})],
    ["plain metatable on __index", setmetatable({}, makeloud({}))],
    ["plain metatable on __newindex", setmetatable({}, makeloud({}))],
]
def shoulderr(s, e):
    return s, type(e)
for i, test in ipairs(tests):
    testname = test[1]
    testtable = test[2]
    def set_1(): testtable[1] = 2
    def get_1(): return testtable[1]
    def del_1(): testtable[1] = None
    def set_a(): testtable["a"] = "b"
    def get_a(): return testtable["a"], testtable.a
    def set_a_dot(): testtable.a = "c"
    def del_a_dot(): testtable.a = None
    def set_nil(): testtable[None] = "d"
    def get_nil(): return testtable[None]
    def del_nil(): testtable[None] = None
    print("------ basic table tests on " .. testname .. " " .. type(testtable))
    print("t[1]=2", pcall(set_1))
    print("t[1]", pcall(get_1))
    print("t[1]=nil", pcall(del_1))
    print("t[1]", pcall(get_1))
    print('t["a"]="b"', pcall(set_a))
    print('t["a"],t.a', pcall(get_a))
    print('t.a="c"', pcall(set_a_dot))
    print('t["a"],t.a', pcall(get_a))
    print("t.a=nil", pcall(del_a_dot))
    print('t["a"],t.a', pcall(get_a))
    print('t[nil]="d"', shoulderr(pcall(set_nil)))
    print("t[nil]", pcall(get_nil))
    print("t[nil]=nil", shoulderr(pcall(del_nil)))
    print("t[nil]", pcall(get_nil))

print("-- sort tests")
def tryall(cmp):
    def attempt(t):
        print(table.concat(t, "-"))
        if pcall(table.sort, t, cmp):
            print(table.concat(t, "-"))
        else:
            print("sort failed")
    attempt([2, 4, 6, 8, 1, 3, 5, 7])
    attempt([333, 222, 111])
    attempt(["www", "xxx", "yyy", "aaa", "bbb", "ccc"])
    attempt([21, 23, "25", 27, 22, "24", 26, 28])
def comparator(a, b):
    return tonumber(a) < tonumber(b)
print("default (lexical) comparator")
tryall()
print("custom (numerical) comparator")
tryall(comparator)
