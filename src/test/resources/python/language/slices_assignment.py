# Slice assignment and deletion, item assignment and deletion, with steps and
# clamped bounds (the lists are mutated in place; CPython is the oracle).

def check(label, value):
    print(label, value)

l = list(range(10))
l[2:5] = ["a", "b"]
check("shrink", l)
l[1:1] = [7, 7, 7]
check("insert", l)
l[:2] = []
check("delete by assign", l)
l[-2:] = [0, 1, 2, 3]
check("tail", l)
l[len(l):] = [99]
check("append", l)
l[100:200] = ["end"]
check("past the end", l)
l[:] = [1, 2, 3]
check("whole", l)

l = list(range(10))
l[::2] = ["x"] * 5
check("step 2", l)
l[1::3] = [0, 0, 0]
check("step 3", l)
l[::-1] = list(range(10))
check("step -1", l)
l[8:2:-2] = ["p", "q", "r"]
check("step -2", l)

l = list(range(10))
del l[2:4]
check("del", l)
del l[::2]
check("del step", l)
del l[-1]
check("del last", l)
del l[:]
check("del all", l)
l = list(range(10))
del l[8:2:-3]
check("del neg step", l)
del l[100:]
check("del past end", l)
del l[-100:2]
check("del clamped", l)

l = list(range(5))
l[0], l[-1] = l[-1], l[0]
check("swap", l)
l[1:3] = l[3:5]
check("from itself", l)
l[1:1] = l
check("insert itself", l)
l[2:2] = "ab"
check("insert str", l)
l[0:0] = (1, 2)
check("insert tuple", l)

def errs(fn):
    try:
        fn()
    except Exception as e:
        return type(e).__name__
    return "ok"

l = list(range(6))
def ext():
    l[::2] = [1, 2]
def zero():
    l[::0] = []
def idx():
    l[10] = 1
def idx2():
    del l[10]
def nonit():
    l[1:2] = 5
def tup():
    t = (1, 2)
    t[0] = 5
def sstr():
    s = "ab"
    s[0] = "c"
def dstr():
    s = "ab"
    del s[0:1]
print(errs(ext), errs(zero), errs(idx), errs(idx2), errs(nonit), errs(tup), errs(sstr), errs(dstr))
check("unchanged", l)

# dict items
d = {"a": 1}
d["b"] = 2
d["a"] += 10
del d["b"]
check("dict", d)
def dk():
    del d["zz"]
print(errs(dk))

# augmented assignment on subscripts evaluates the container once
calls = []
def get():
    calls.append(1)
    return l
l = [1, 2, 3]
get()[0] += 5
get()[1:2] = [8, 9]
check("aug", l)
print(len(calls))

# negative and out of range indices in reads
s = [1, 2, 3, 4, 5]
print(s[-1], s[-5], s[1:-1], s[-3:], s[:-3], s[3:1], s[::-1][:2], s[10:], s[-10:2], s[::5], s[4::-4], s[:0])
st = "abcdef"
print(st[1:4], st[-3:], st[::2], st[::-2], st[10:], st[-10:2], st[3:1], st[::-1], st[2:-2])
t = (1, 2, 3, 4)
print(t[1:], t[::-1], t[-2:], t[5:], t[:-5])
