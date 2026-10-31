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

# The original script keeps `a` and `t` as chunk-level locals (upvalues in the
# closures below, which shows in the error messages): the body lives in main().
def main():
    global s, sm

    # test of common types of errors
    def a(*args):
        s, e = pcall(*args)
        if s:
            return s, e
        else:
            return False, type(e)

    s = "some string"
    t = {}
    # error message tests
    print("a(error)", a(error))
    print('a(error,"msg")', a(error, "msg"))
    print('a(error,"msg",0)', a(error, "msg", 0))
    print('a(error,"msg",1)', a(error, "msg", 1))
    print('a(error,"msg",2)', a(error, "msg", 2))
    print('a(error,"msg",3)', a(error, "msg", 3))
    print('a(error,"msg",4)', a(error, "msg", 4))
    print('a(error,"msg",5)', a(error, "msg", 5))
    print('a(error,"msg",6)', a(error, "msg", 6))

    # call errors
    def call_local_true():
        b = True
        return b()
    print("a(nil())", a(lambda: n()))
    print("a(t())  ", a(lambda: t()))
    print("a(s())  ", a(lambda: s()))
    print("a(true())", a(call_local_true))

    # arithmetic errors
    def add_local_true():
        b = True
        return b + 1
    print("a(nil+1)", a(lambda: None + 1))
    print("a(a+1)  ", a(lambda: a + 1))
    print("a(s+1)  ", a(lambda: s + 1))
    print("a(true+1)", a(add_local_true))

    # table errors
    def index_local_true():
        b = True
        return b.x
    def set_nil_x():
        n.x = 5
    def set_a_x():
        a.x = 5
    def set_s_x():
        s.x = 5
    def set_local_true_x():
        b = True
        b.x = 5
    print("a(nil.x)", a(lambda: n.x))
    print("a(a.x)  ", a(lambda: a.x))
    print("a(s.x)  ", a(lambda: s.x))
    print("a(true.x)", a(index_local_true))
    print("a(nil.x=5)", a(set_nil_x))
    print("a(a.x=5)  ", a(set_a_x))
    print("a(s.x=5)  ", a(set_s_x))
    print("a(true.x=5)", a(set_local_true_x))

    # len operator
    def len_local_true():
        b = True
        return len(b)
    print("a(#nil) ", a(lambda: len(n)))
    print("a(#t)   ", a(lambda: len(t)))
    print("a(#s)   ", a(lambda: len(s)))
    print("a(#a)   ", a(lambda: len(a)))
    print("a(#true)", a(len_local_true))

    # comparison errors
    def gt_local_true():
        b = True
        return b > 1
    print("a(nil>1)", a(lambda: None > 1))
    print("a(a>1)  ", a(lambda: a > 1))
    print("a(s>1)  ", a(lambda: s > 1))
    print("a(true>1)", a(gt_local_true))

    # unary minus errors
    def neg_local_true():
        b = True
        return -b
    print("a(-nil)", a(lambda: -n))
    print("a(-a)  ", a(lambda: -a))
    print("a(-s)  ", a(lambda: -s))
    print("a(-true)", a(neg_local_true))

    # string concatenation
    def concatsuite(comparefunc):
        print('"a".."b"', comparefunc("a", "b"))
        print('"a"..nil', comparefunc("a", None))
        print('nil.."b"', comparefunc(None, "b"))
        print('"a"..{}', comparefunc("a", {}))
        print('{}.."b"', comparefunc({}, "b"))
        print('"a"..2', comparefunc("a", 2))
        print('2.."b"', comparefunc(2, "b"))
        print('"a"..print', comparefunc("a", print))
        print('print.."b"', comparefunc(print, "b"))
        print('"a"..true', comparefunc("a", True))
        print('true.."b"', comparefunc(True, "b"))
        print("nil..true", comparefunc(None, True))
        print('"a"..3.5', comparefunc("a", 3.5))
        print('3.5.."b"', comparefunc(3.5, "b"))

    def strconcat(a, b):
        return (pcall(lambda: a .. b))

    def tblconcat(a, b):
        t = [a, b]
        return (pcall(lambda: table.concat(t, "-", 1, 2)))

    print("-------- string concatenation")
    concatsuite(strconcat)
    print("-------- table concatenation")
    concatsuite(tblconcat)

    # pairs
    def pairs_local_true():
        b = True
        return pairs(b, {})
    print("-------- pairs tests")
    print("a(pairs(nil))", a(lambda: pairs(None, {})))
    print("a(pairs(a))  ", a(lambda: pairs(a, {})))
    print("a(pairs(s))  ", a(lambda: pairs(s, {})))
    print("a(pairs(t))  ", a(lambda: pairs(t, {})))
    print("a(pairs(true))", a(pairs_local_true))

    # setmetatable
    def sm(*args):
        return tostring(setmetatable(*args))
    def meta():
        return getmetatable(t), type(getmetatable(t))
    def sm_local_true():
        b = True
        return sm(b, {})
    print("-------- setmetatable tests")
    print("a(setmetatable(nil))", a(lambda: sm(None, {})))
    print("a(setmetatable(a))  ", a(lambda: sm(a, {})))
    print("a(setmetatable(s))  ", a(lambda: sm(s, {})))
    print("a(setmetatable(true))", a(sm_local_true))
    print("a(setmetatable(t))  ", a(lambda: sm(t, {})))
    print("a(getmetatable(t))  ", a(meta))
    print("a(setmetatable(t*))  ", a(lambda: sm(t, {"__metatable": {}})))
    print("a(getmetatable(t))  ", a(meta))
    print("a(setmetatable(t))  ", a(lambda: sm(t, {})))
    print("a(getmetatable(t))  ", a(meta))
    t = {}
    print("a(setmetatable(t))  ", a(lambda: sm(t, {})))
    print("a(getmetatable(t))  ", a(meta))
    print("a(setmetatable(t*))  ", a(lambda: sm(t, {"__metatable": "some string"})))
    print("a(getmetatable(t))  ", a(meta))
    print("a(setmetatable(t))  ", a(lambda: sm(t, {})))
    print("a(getmetatable(t))  ", a(meta))
    print("a(setmetatable(t,nil))  ", a(lambda: sm(t, None)))
    print("a(setmetatable(t))  ", a(lambda: sm(t)))
    print('a(setmetatable({},"abc"))  ', a(lambda: sm({}, "abc")))

    # bad args to error!
    def bad_error_arg():
        error("some message", "some bad arg")
    print('error("msg","arg")', a(bad_error_arg))

    # loadfile, dofile on missing files
    print('loadfile("bogus.txt")', a(lambda: loadfile("bogus.txt")))
    print('dofile("bogus.txt")', a(lambda: dofile("bogus.txt")))

main()
