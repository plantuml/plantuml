def p(a, *args):
    print("a", a)
    print("...", *args)
    print("...,a", *args, a)
    print("a,...", a, *args)

def q(a, *args):
    print("a,arg[1],arg[2],arg[3]", a, arg[1], arg[2], arg[3])

def r(a, *args):
    print("a,arg[1],arg[2],arg[3]", a, arg[1], arg[2], arg[3])
    print("a", a)
    print("...", *args)
    print("...,a", *args, a)
    print("a,...", a, *args)

def s(a):
    arg = ["1", "2", "3"]
    print("a,arg[1],arg[2],arg[3]", a, arg[1], arg[2], arg[3])
    print("a", a)

def t(a, *args):
    arg = ["1", "2", "3"]
    print("a,arg[1],arg[2],arg[3]", a, arg[1], arg[2], arg[3])
    print("a", a)
    print("...", *args)
    print("...,a", *args, a)
    print("a,...", a, *args)
