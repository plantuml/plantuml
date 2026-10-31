print("testing (parts of) table library")

print("testing unpack")

unpack = table.unpack

a = {}
lim = 2000
for i in range(1, lim + 1): a[i] = i
assert(select(lim, unpack(a)) == lim and select("#", unpack(a)) == lim)
x = unpack(a)
assert(x == 1)
x = [unpack(a)]
assert(len(x) == lim and x[1] == 1 and x[lim] == lim)
x = [unpack(a, lim - 2)]
assert(len(x) == 3 and x[1] == lim - 2 and x[3] == lim)
x = [unpack(a, 10, 6)]
assert(next(x) == None)   # no elements
x = [unpack(a, 11, 10)]
assert(next(x) == None)   # no elements
x, y = unpack(a, 10, 10)
assert(x == 10 and y == None)
x, y, z = unpack(a, 10, 11)
assert(x == 10 and y == 11 and z == None)
a, x = unpack([1])
assert(a == 1 and x == None)
a, x = unpack([1, 2], 1, 1)
assert(a == 1 and x == None)

if not _no32:
    assert(not pcall(unpack, {}, 0, 2 ** 31 - 1))
    assert(not pcall(unpack, {}, 1, 2 ** 31 - 1))
    assert(not pcall(unpack, {}, -(2 ** 31), 2 ** 31 - 1))
    assert(not pcall(unpack, {}, -(2 ** 31 - 1), 2 ** 31 - 1))
    assert(pcall(unpack, {}, 2 ** 31 - 1, 0))
    assert(pcall(unpack, {}, 2 ** 31 - 1, 1))
    pcall(unpack, {}, 1, 2 ** 31)
    a, b = unpack({2 ** 31 - 1: 20}, 2 ** 31 - 1, 2 ** 31 - 1)
    assert(a == 20 and b == None)
    a, b = unpack({2 ** 31 - 1: 20}, 2 ** 31 - 2, 2 ** 31 - 1)
    assert(a == None and b == 20)

print("testing pack")

a = table.pack()
assert(a[1] == None and a.n == 0)

a = table.pack(table)
assert(a[1] == table and a.n == 1)

a = table.pack(None, None, None, None)
assert(a[1] == None and a.n == 4)


print("testing sort")


# test checks for invalid order functions
def check(t):
    def f(a, b):
        assert(a and b)
        return True
    s, e = pcall(table.sort, t, f)
    assert(not s and e:find("invalid order function"))

check([1, 2, 3, 4])
check([1, 2, 3, 4, 5])
check([1, 2, 3, 4, 5, 6])


def check(a, f):
    f = f or (lambda x, y: x < y)
    for n in range(len(a), 1, -1):
        assert(not f(a[n], a[n - 1]))

a = ["Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep",
     "Oct", "Nov", "Dec"]

table.sort(a)
check(a)

def perm(s, n):
    n = n or len(s)
    if n == 1:
        t = [unpack(s)]
        table.sort(t)
        check(t)
    else:
        for i in range(1, n + 1):
            s[i], s[n] = s[n], s[i]
            perm(s, n - 1)
            s[i], s[n] = s[n], s[i]

perm([])
perm([1])
perm([1, 2])
perm([1, 2, 3])
perm([1, 2, 3, 4])
perm([2, 2, 3, 4])
perm([1, 2, 3, 4, 5])
perm([1, 2, 3, 3, 5])
perm([1, 2, 3, 4, 5, 6])
perm([2, 2, 3, 3, 5, 6])

limit = 30000
if _soft: limit = 5000

a = {}
for i in range(1, limit + 1):
    a[i] = math.random()

x = os.clock()
table.sort(a)
print(string.format("Sorting %d elements in %.2f sec.", limit, os.clock() - x))
check(a)

x = os.clock()
table.sort(a)
print(string.format("Re-sorting %d elements in %.2f sec.", limit, os.clock() - x))
check(a)

a = {}
for i in range(1, limit + 1):
    a[i] = math.random()

x = os.clock()
i = 0
def invert(x, y):
    global i
    i += 1
    return y < x
table.sort(a, invert)
print(string.format("Invert-sorting other %d elements in %.2f sec., with %i comparisons",
      limit, os.clock() - x, i))
check(a, lambda x, y: y < x)


table.sort([])  # empty array

for i in range(1, limit + 1): a[i] = False
x = os.clock()
table.sort(a, lambda x, y: None)
print(string.format("Sorting %d equal elements in %.2f sec.", limit, os.clock() - x))
check(a, lambda x, y: None)
for i, v in pairs(a): assert(not v or i == "n" and v == limit)

A = ["\xe1lo", "\x00first :-)", "alo", "then this one", "45", "and a new"]
table.sort(A)
check(A)

def sort_with_load(x, y):
    load(string.format("A[%q] = ''", x))()
    collectgarbage()
    return x < y
table.sort(A, sort_with_load)


tt = {"__lt": lambda a, b: a.val < b.val}
a = {}
for i in range(1, 11):
    a[i] = {"val": math.random(100)}
    setmetatable(a[i], tt)
table.sort(a)
check(a, tt.__lt)
check(a)

print("OK")
