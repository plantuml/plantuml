def f0(): print("f0:")
def f1(a): print("f1:", a)
def f2(a, b): print("f2:", a, b)
def f3(a, b, c): print("f3:", a, b, c)
def f4(a, b, c, d): print("f4:", a, b, c, d)

f0()
f0("a1/1")
f0("a1/2", "a2/2")
f0("a1/3", "a2/3", "a3/3")
f0("a1/4", "a2/4", "a3/4", "a4/4")
f1()
f1("a1/1")
f1("a1/2", "a2/2")
f1("a1/3", "a2/3", "a3/3")
f1("a1/4", "a2/4", "a3/4", "a4/4")
f2()
f2("a1/1")
f2("a1/2", "a2/2")
f2("a1/3", "a2/3", "a3/3")
f2("a1/4", "a2/4", "a3/4", "a4/4")
f3()
f3("a1/1")
f3("a1/2", "a2/2")
f3("a1/3", "a2/3", "a3/3")
f3("a1/4", "a2/4", "a3/4", "a4/4")
f4()
f4("a1/1")
f4("a1/2", "a2/2")
f4("a1/3", "a2/3", "a3/3")
f4("a1/4", "a2/4", "a3/4", "a4/4")

def g0(a, b, c, d): return
def g1(a, b, c, d): return d
def g2(a, b, c, d): return c, d
def g3(a, b, c, d): return b, c, d
def g4(a, b, c, d): return a, b, c, d

z = g0("c0.1/4", "c0.2/4", "c0.3/4", "c0.4/4")
print("z0:", z)
z = g2("c2.1/4", "c2.2/4", "c2.3/4", "c2.4/4")
print("z2:", z)
z = g4("c4.1/4", "c4.2/4", "c4.3/4", "c4.4/4")
print("z4:", z)

a, b, c, d = g0("c0.1/4", "c0.2/4", "c0.3/4", "c0.4/4")
print("g0:", a, b, c, d, "(eol)")
a, b, c, d = g2("b2.1/4", "b2.2/4", "b2.3/4", "b2.4/4")
print("g2:", a, b, c, d, "(eol)")
a, b, c, d = g4("b4.1/4", "b4.2/4", "b4.3/4", "b4.4/4")
print("g4:", a, b, c, d, "(eol)")

def func(a, b, c):
    return a, b, c

print(func(11, 12, 13))
print(func(23, 22, 21))
print(func(func(32, 33, 34), func(45, 46, 47), func(58, 59, 50)))

def p(a, *args):
    print("a", a)
    print("...", *args)
    print("...,a", *args, a)
    print("a,...", a, *args)

p()
p("q")
p("q", "r")
p("q", "r", "s")

# tail call tests
def first(*args):
    return "abc", *args, "|", *args

def second(a, *args):
    return "def", *args, "|", a, *args

def third(a, b, c):
    print("third", first(a, b, c))
    print("third", second(a, b, c))
    return second(a, b, c)

print("third", third())
print("third", third("p"))
print("third", third("p", "q"))
print("third", third("p", "q", "r"))
print("third", third("p", "q", "r", "s"))
print("third", third())
