def p(a, *args):
    print("a", a)
    print("...", *args)
    print("...,a", *args, a)
    print("a,...", a, *args)

def q(a, *args):
    print("a,args[0],args[1],args[2]", a, args[0], args[1], args[2])

def r(a, *args):
    print("a,args[0],args[1],args[2]", a, args[0], args[1], args[2])
    print("a", a)
    print("...", *args)
    print("...,a", *args, a)
    print("a,...", a, *args)

def s(a):
    args = ["1", "2", "3"]
    print("a,args[0],args[1],args[2]", a, args[0], args[1], args[2])
    print("a", a)

def t(a, *args):
    args = ["1", "2", "3"]
    print("a,args[0],args[1],args[2]", a, args[0], args[1], args[2])
    print("a", a)

p("a")
p("a", "b", "c")
q("a", 1, 2, 3)
r("a", 1, 2, 3)
s("a")
t("a", "x", "y")
t("a")
assert len((lambda *a: a)()) == 0
assert (lambda *a: a)(1, 2) == (1, 2)
