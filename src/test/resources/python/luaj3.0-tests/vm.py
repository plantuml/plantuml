print("-------- basic vm tests --------")

print("-- boolean tests")
def booleantests():
    global t, f, n, s, z, one
    t = True
    f = False
    n = None
    s = "Hello"
    z = 0
    one = 1

    print(t)
    print(f)

    print(not t)
    print(not f)
    print(not n)
    print(not z)
    print(not s)
    print(not (not (t)))
    print(not (not (z)))
    print(not (not (n)))

    print(t and f)
    print(t or f)
    print(f and t)
    print(f or t)

    print(f or one)
    print(f or z)
    print(f or n)

    print(t and one)
    print(t and z)
    print(t and n)

print("booleantests result:", pcall(booleantests))


print("------------- varargs")
def varargstest():
    global arg
    def p(a, *args):
        print("a", a)
        print("...", *args)
        print("...,a", *args, a)
        print("a,...", a, *args)
    def q(a, *args):
        print("a,arg[1],arg[2],arg[3]", a, arg.n, arg[1], arg[2], arg[3])
    def r(a, *args):
        print("a,arg[1],arg[2],arg[3]", a, arg.n, arg[1], arg[2], arg[3])
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
    def u(arg):
        print("arg", arg)
    def v(arg, *args):
        print("arg", arg)
        print("...", *args)
        print("arg,...", arg, *args)
    arg = ["global-1", "global-2", "global-3"]
    def tryall(f, name):
        print("---- function " .. name .. "()")
        print("--" .. name .. "():")
        print(" ->", pcall(f))
        print("--" .. name .. '("q"):')
        print(" ->", pcall(f, "q"))
        print("--" .. name .. '("q","r"):')
        print(" ->", pcall(f, "q", "r"))
        print("--" .. name .. '("q","r","s"):')
        print(" ->", pcall(f, "q", "r", "s"))
    tryall(p, "p")
    tryall(q, "q")
    tryall(r, "r")
    tryall(s, "s")
    tryall(t, "t")
    tryall(u, "u")
    tryall(v, "v")

print("varargstest result:", pcall(varargstest))

# The purpose of this test case is to demonstrate that
# basic metatable operations on non-table types work.
# i.e. that s.sub(s,...) could be used in place of string.sub(s,...)
print("---------- metatable tests")
def metatabletests():
    global tostring

    # tostring replacement that assigns ids
    ts, id, nid, types = tostring, {}, 0, {"table": "tbl", "thread": "thr", "userdata": "uda", "function": "func"}
    def tostring(x):
        if not x or not types[type(x)]:
            return ts(x)
        if not id[x]:
            nid += 1
            id[x] = types[type(x)] .. "." .. nid
        return id[x]
    def results(s, e, *args):
        if s: return e, *args
        return False, type(e)
    # `def pcall` would make the inner call recursive (the name is already local
    # in the whole function), so the original pcall is kept under another name.
    pc = pcall
    def pcall(*args):
        return results(pc(*args))

    s = "hello"
    print(s:sub(2, 4))

    t = {}
    def op(name, *args):
        a, b = pcall(setmetatable, t, *args)
        print(name, t, getmetatable(t), a, b)
    op("set{}  ", {})
    op("set-nil", None)
    op("set{}  ", {})
    op("set")
    op("set{}  ", {})
    op("set{}  ", {})
    op("set{}{}", {}, {})
    op("set-nil", None)
    op("set{__}", {"__metatable": {}})
    op("set{}  ", {})
    op("set-nil", None)
    t = {}
    op("set{}  ", {})
    op("set-nil", None)
    op("set{__}", {"__metatable": "abc"})
    op("set{}  ", {})
    op("set-nil", None)


    # `i` is also the loop variable below: a Lua `for` has its own scope, a
    # function-level name does not, so the counter and the loop index are named apart.
    i = 1234
    def lazy(t, k):
        v = i
        i += 1
        rawset(t, k, v)
        return v
    weak = setmetatable({}, {"__mode": "v", "__index": lazy})

    l = ["a", "b", "a", "b", "c", "a", "b", "c", "d"]
    for pos, key in ipairs(l):
        print("t." .. key, weak[key])

print("metatabletests result:", pcall(metatabletests))

# test tables with more than 50 elements
print("------------ huge tables")
def hugetables():
    t = [
        1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
        1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
        1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
        1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
        1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
        1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
        1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
        1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
        1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
        1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
    ]
    print("#t=", len(t), "t[1,50,51,59]", t[1], t[50], t[51], t[59])
    print(table.concat(t, ","))

    t2 = [
        0, 3, 4, 7, 9, 8, 12, 15, 23, 5,
        10, 13, 14, 17, 19, 18, 112, 115, 123, 15,
        20, 33, 24, 27, 29, 28, 212, 215, 223, 25,
        40, 43, 44, 47, 49, 48, 412, 415, 423, 45,
        50, 53, 54, 57, 59, 58, 512, 515, 523, 55,
        60, 63, 64, 67, 69, 68, 612, 615, 623, 65,
        70, 73, 74, 77, 79, 78, 72, 715, 723, 75
    ]

    print("#t2=", len(t2), "t[1,50,51,59]", t[1], t[50], t[51], t[59])
    print(table.concat(t2, ","))

    t = {
        2000: "a", 2001: "b", 2002: "c", 2003: "d", 2004: "e", 2005: "f", 2006: "g", 2007: "h", 2008: "i", 2009: "j",
        3000: "a", 3001: "b", 3002: "c", 3003: "d", 3004: "e", 3005: "f", 3006: "g", 3007: "h", 3008: "i", 3009: "j",
        4000: "a", 4001: "b", 4002: "c", 4003: "d", 4004: "e", 4005: "f", 4006: "g", 4007: "h", 4008: "i", 4009: "j",
        5000: "a", 5001: "b", 5002: "c", 5003: "d", 5004: "e", 5005: "f", 5006: "g", 5007: "h", 5008: "i", 5009: "j",
        6000: "a", 6001: "b", 6002: "c", 6003: "d", 6004: "e", 6005: "f", 6006: "g", 6007: "h", 6008: "i", 6009: "j",
        7000: "a", 7001: "b", 7002: "c", 7003: "d", 7004: "e", 7005: "f", 7006: "g", 7007: "h", 7008: "i", 7009: "j",
        8000: "a", 8001: "b", 8002: "c", 8003: "d", 8004: "e", 8005: "f", 8006: "g", 8007: "h", 8008: "i", 8009: "j"
    }

    for i in range(2000, 8001, 1000):
        for j in range(0, 10):
            print("t[" .. tostring(i + j) .. "]", t[i + j])

print("hugetables result:", pcall(hugetables))

print("--------- many locals")
def manylocals():
    # test program with more than 50 non-sequential integer elements
    t0000 = "a"; t0001 = "b"; t0002 = "c"; t0003 = "d"; t0004 = "e"; t0005 = "f"; t0006 = "g"; t0007 = "h"; t0008 = "i"; t0009 = "j"
    t1000 = "a"; t1001 = "b"; t1002 = "c"; t1003 = "d"; t1004 = "e"; t1005 = "f"; t1006 = "g"; t1007 = "h"; t1008 = "i"; t1009 = "j"
    t2000 = "a"; t2001 = "b"; t2002 = "c"; t2003 = "d"; t2004 = "e"; t2005 = "f"; t2006 = "g"; t2007 = "h"; t2008 = "i"; t2009 = "j"
    t3000 = "a"; t3001 = "b"; t3002 = "c"; t3003 = "d"; t3004 = "e"; t3005 = "f"; t3006 = "g"; t3007 = "h"; t3008 = "i"; t3009 = "j"
    t4000 = "a"; t4001 = "b"; t4002 = "c"; t4003 = "d"; t4004 = "e"; t4005 = "f"; t4006 = "g"; t4007 = "h"; t4008 = "i"; t4009 = "j"
    t5000 = "a"; t5001 = "b"; t5002 = "c"; t5003 = "d"; t5004 = "e"; t5005 = "f"; t5006 = "g"; t5007 = "h"; t5008 = "i"; t5009 = "j"
    t6000 = "a"; t6001 = "b"; t6002 = "c"; t6003 = "d"; t6004 = "e"; t6005 = "f"; t6006 = "g"; t6007 = "h"; t6008 = "i"; t6009 = "j"
    t7000 = "a"; t7001 = "b"; t7002 = "c"; t7003 = "d"; t7004 = "e"; t7005 = "f"; t7006 = "g"; t7007 = "h"; t7008 = "i"; t7009 = "j"
    t8000 = "a"; t8001 = "b"; t8002 = "c"; t8003 = "d"; t8004 = "e"; t8005 = "f"; t8006 = "g"; t8007 = "h"; t8008 = "i"; t8009 = "j"
    t9000 = "a"; t9001 = "b"; t9002 = "c"; t9003 = "d"; t9004 = "e"; t9005 = "f"; t9006 = "g"; t9007 = "h"; t9008 = "i"; t9009 = "j"
    t10000 = "a"; t10001 = "b"; t10002 = "c"; t10003 = "d"; t10004 = "e"; t10005 = "f"; t10006 = "g"; t10007 = "h"; t10008 = "i"; t10009 = "j"
    t11000 = "a"; t11001 = "b"; t11002 = "c"; t11003 = "d"; t11004 = "e"; t11005 = "f"; t11006 = "g"; t11007 = "h"; t11008 = "i"; t11009 = "j"
    t12000 = "a"; t12001 = "b"; t12002 = "c"; t12003 = "d"; t12004 = "e"; t12005 = "f"; t12006 = "g"; t12007 = "h"; t12008 = "i"; t12009 = "j"
    t13000 = "a"; t13001 = "b"; t13002 = "c"; t13003 = "d"; t13004 = "e"; t13005 = "f"; t13006 = "g"; t13007 = "h"; t13008 = "i"; t13009 = "j"

    # print the variables
    print(t0000, "a"); print(t0001, "b"); print(t0002, "c"); print(t0003, "d"); print(t0004, "e"); print(t0005, "f"); print(t0006, "g"); print(t0007, "h"); print(t0008, "i"); print(t0009, "j")
    print(t1000, "a"); print(t1001, "b"); print(t1002, "c"); print(t1003, "d"); print(t1004, "e"); print(t1005, "f"); print(t1006, "g"); print(t1007, "h"); print(t1008, "i"); print(t1009, "j")
    print(t2000, "a"); print(t2001, "b"); print(t2002, "c"); print(t2003, "d"); print(t2004, "e"); print(t2005, "f"); print(t2006, "g"); print(t2007, "h"); print(t2008, "i"); print(t2009, "j")
    print(t3000, "a"); print(t3001, "b"); print(t3002, "c"); print(t3003, "d"); print(t3004, "e"); print(t3005, "f"); print(t3006, "g"); print(t3007, "h"); print(t3008, "i"); print(t3009, "j")
    print(t4000, "a"); print(t4001, "b"); print(t4002, "c"); print(t4003, "d"); print(t4004, "e"); print(t4005, "f"); print(t4006, "g"); print(t4007, "h"); print(t4008, "i"); print(t4009, "j")
    print(t5000, "a"); print(t5001, "b"); print(t5002, "c"); print(t5003, "d"); print(t5004, "e"); print(t5005, "f"); print(t5006, "g"); print(t5007, "h"); print(t5008, "i"); print(t5009, "j")
    print(t6000, "a"); print(t6001, "b"); print(t6002, "c"); print(t6003, "d"); print(t6004, "e"); print(t6005, "f"); print(t6006, "g"); print(t6007, "h"); print(t6008, "i"); print(t6009, "j")
    print(t7000, "a"); print(t7001, "b"); print(t7002, "c"); print(t7003, "d"); print(t7004, "e"); print(t7005, "f"); print(t7006, "g"); print(t7007, "h"); print(t7008, "i"); print(t7009, "j")
    print(t8000, "a"); print(t8001, "b"); print(t8002, "c"); print(t8003, "d"); print(t8004, "e"); print(t8005, "f"); print(t8006, "g"); print(t8007, "h"); print(t8008, "i"); print(t8009, "j")
    print(t9000, "a"); print(t9001, "b"); print(t9002, "c"); print(t9003, "d"); print(t9004, "e"); print(t9005, "f"); print(t9006, "g"); print(t9007, "h"); print(t9008, "i"); print(t9009, "j")
    print(t10000, "a"); print(t10001, "b"); print(t10002, "c"); print(t10003, "d"); print(t10004, "e"); print(t10005, "f"); print(t10006, "g"); print(t10007, "h"); print(t10008, "i"); print(t10009, "j")
    print(t11000, "a"); print(t11001, "b"); print(t11002, "c"); print(t11003, "d"); print(t11004, "e"); print(t11005, "f"); print(t11006, "g"); print(t11007, "h"); print(t11008, "i"); print(t11009, "j")
    print(t12000, "a"); print(t12001, "b"); print(t12002, "c"); print(t12003, "d"); print(t12004, "e"); print(t12005, "f"); print(t12006, "g"); print(t12007, "h"); print(t12008, "i"); print(t12009, "j")
    print(t13000, "a"); print(t13001, "b"); print(t13002, "c"); print(t13003, "d"); print(t13004, "e"); print(t13005, "f"); print(t13006, "g"); print(t13007, "h"); print(t13008, "i"); print(t13009, "j")

print("manylocals result:", pcall(manylocals))
