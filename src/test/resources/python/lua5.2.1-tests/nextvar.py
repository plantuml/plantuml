print("testing tables, next, and for")

a = {}

# make sure table has lots of space in hash part
for i in range(1, 101): a[i .. "+"] = True
for i in range(1, 101): a[i .. "+"] = None
# fill hash part with numeric indices testing size operator
for i in range(1, 101):
    a[i] = True
    assert(len(a) == i)

# testing ipairs
x = 0
for k, v in ipairs({10, 20, 30, "x": 12}):
    x += 1
    assert(k == x and v == x * 10)

for _ in ipairs({"x": 12, "y": 24}): assert(None)

# test for 'false' x ipair
x = False
i = 0
for k, v in ipairs([True, False, True, False]):
    i += 1
    x = not x
    assert(x == v)
assert(i == 4)

# iterator function is always the same
assert(type(ipairs({})) == "function" and ipairs({}) == ipairs({}))

# (the table-size tests, which need the C test library `T`, are not rewritten)


# test size operation on empty tables
assert(len({}) == 0)
assert(len([None]) == 0)
assert(len([None, None]) == 0)
assert(len([None, None, None]) == 0)
assert(len([None, None, None, None]) == 0)
print("+")


nofind = {}

a, b, c = 1, 2, 3
a, b, c = None


# next uses always the same iteraction function
assert(next({}) == next({}))

def find(name):
    n = None
    v = None
    while 1:
        n, v = next(_G, n)
        if not n: return nofind
        assert(v != None)
        if n == name: return v

def find1(name):
    for n, v in pairs(_G):
        if n == name: return v
    return None  # not found

assert(print == find("print") and print == find1("print"))
assert(_G["print"] == find("print"))
assert(assert == find1("assert"))
assert(nofind == find("return"))
assert(not find1("return"))
_G["ret" .. "urn"] = None
assert(nofind == find("return"))
_G["xxx"] = 1
assert(xxx == find("xxx"))

print("+")

a = {}
for i in range(0, 10001):
    if math.fmod(i, 10) != 0:
        a["x" .. i] = i

n = {"n": 0}
for i, v in pairs(a):
    n.n += 1
    assert(i and v and a[i] == v)
assert(n.n == 9000)
a = None

# clear global table
def clearglobals():
    a = {}
    for n, v in pairs(_G): a[n] = v
    for n, v in pairs(a):
        if (not package.loaded[n] and type(v) != "function" and
                not string.find(n, "^[%u_]")):
            _G[n] = None
        collectgarbage()
clearglobals()


#

def checknext(a):
    b = {}
    k, v = next(a)
    while k:
        b[k] = v
        k, v = next(a, k)
    for k, v in pairs(b): assert(a[k] == v)
    for k, v in pairs(a): assert(b[k] == v)

checknext({1, "x": 1, "y": 2, "z": 3})
checknext({1, 2, "x": 1, "y": 2, "z": 3})
checknext({1, 2, 3, "x": 1, "y": 2, "z": 3})
checknext({1, 2, 3, 4, "x": 1, "y": 2, "z": 3})
checknext({1, 2, 3, 4, 5, "x": 1, "y": 2, "z": 3})

assert(len({}) == 0)
assert(len({-1: 2}) == 0)
assert(len([1, 2, 3, None, None]) == 3)
for i in range(0, 41):
    a = {}
    for j in range(1, i + 1): a[j] = j
    assert(len(a) == i)

# 'maxn' is now deprecated, but it is easily defined in Lua
def table.maxn(t):
    max = 0
    for k in pairs(t):
        max = (type(k) == "number") and math.max(max, k) or max
    return max

assert(table.maxn({}) == 0)
assert(table.maxn({"1000": True}) == 0)
assert(table.maxn({"1000": True, 24.5: 3}) == 24.5)
assert(table.maxn({1000: True}) == 1000)
assert(table.maxn({10: True, 100 * math.pi: print}) == 100 * math.pi)

table.maxn = None

# int overflow
a = {}
for i in range(0, 51): a[math.pow(2, i)] = True
assert(a[len(a)])

print("+")


# erasing values
t = {[1]: 1, [2]: 2, string.rep("x ", 4): 3,
     100.3: 4, 4: 5}

n = 0
for k, v in pairs(t):
    n += 1
    assert(t[k] == v)
    t[k] = None
    collectgarbage()
    assert(t[k] == None)
assert(n == 5)


def test(a):
    table.insert(a, 10)
    table.insert(a, 2, 20)
    table.insert(a, 1, -1)
    table.insert(a, 40)
    table.insert(a, len(a) + 1, 50)
    table.insert(a, 2, -2)
    assert(table.remove(a, 1) == -1)
    assert(table.remove(a, 1) == -2)
    assert(table.remove(a, 1) == 10)
    assert(table.remove(a, 1) == 20)
    assert(table.remove(a, 1) == 40)
    assert(table.remove(a, 1) == 50)
    assert(table.remove(a, 1) == None)

a = {"n": 0, -7: "ban"}
test(a)
assert(a.n == 0 and a[-7] == "ban")

a = {-7: "ban"}
test(a)
assert(a.n == None and len(a) == 0 and a[-7] == "ban")


table.insert(a, 1, 10)
table.insert(a, 1, 20)
table.insert(a, 1, -1)
assert(table.remove(a) == 10)
assert(table.remove(a) == 20)
assert(table.remove(a) == -1)

a = ["c", "d"]
table.insert(a, 3, "a")
table.insert(a, "b")
assert(table.remove(a, 1) == "c")
assert(table.remove(a, 1) == "d")
assert(table.remove(a, 1) == "a")
assert(table.remove(a, 1) == "b")
assert(len(a) == 0 and a.n == None)

a = [10, 20, 30, 40]
assert(table.remove(a, len(a) + 1) == None and table.remove(a, 0) == None)
assert(a[len(a)] == 40)
assert(table.remove(a, len(a)) == 40)
assert(a[len(a)] == 30)
assert(table.remove(a, 2) == 20)
assert(a[len(a)] == 30 and len(a) == 2)
print("+")

a = {}
for i in range(1, 1001):
    a[i] = i
    a[i - 1] = None
assert(next(a, None) == 1000 and next(a, 1000) == None)

assert(next({}) == None)
assert(next({}, None) == None)

# (Lua `for i=1,0` is range(1, 1): the limit of range() is excluded)
for a, b in pairs({}): error("not here")
for i in range(1, 1): error("not here")
for i in range(0, 1, -1): error("not here")
a = None
for i in range(1, 2):
    assert(not a)
    a = 1
assert(a)
a = None
for i in range(1, 0, -1):
    assert(not a)
    a = 1
assert(a)

# The original also tests numeric `for` loops with float steps and inclusive
# float limits (`for i=0, 1, 0.1`), and with string bounds (`for i="10","1","-2"`).
# They are not rewritten: `range()` has an exclusive limit, so the number of
# iterations of those loops cannot be written the same way.


collectgarbage()


# testing generic 'for'

def f(n, p):
    t = {}
    for i in range(1, p + 1): t[i] = i * 10
    def step(_, n):
        if n > 0:
            n -= 1
            return n, table.unpack(t)
    return step, None, n

x = 0
for n, a, b, c, d in f(5, 3):
    x += 1
    assert(a == 10 and b == 20 and c == 30 and d == None)
assert(x == 5)



# testing __pairs and __ipairs metamethod
a = {}
def check_pairs():
    x, y, z = pairs(a)
    assert(type(x) == "function" and y == a and z == None)
check_pairs()

def foo(e, i):
    assert(e == a)
    if i <= 10: return i + 1, i + 2

def foo1(e, i):
    i += 1
    assert(e == a)
    if i <= e.n: return i, a[i]

def mypairs(x): return foo, x, 0
setmetatable(a, {"__pairs": mypairs})

i = 0
for k, v in pairs(a):
    i += 1
    assert(k == i and v == k + 1)

a.n = 5
a[3] = 30

a = {"n": 10}
def mylen(x): return x.n
def myiter(e, i):
    if i < len(e): return i + 1
def myipairs(x): return myiter, x, 0
setmetatable(a, {"__len": mylen, "__ipairs": myipairs})
i = 0
for k, v in ipairs(a):
    i += 1
    assert(k == i and v == None)
assert(i == a.n)

print("OK")
