# Errors raised by the operations of the language: calls, arithmetic, indexing,
# len, comparisons, concatenation, unary minus, and bad arguments to builtins.
# (The Lua original printed the messages with their positions; here each case
# reports the type of the exception raised, which is the part Python defines.)

def a(fn, *args):
    try:
        return True, fn(*args)
    except Exception as e:
        return False, type(e).__name__

class_like = {}
s = "some string"
n = None
t = {}
A = {"x": 1}

print("a(div)", a(lambda: 1 / 0))

def raiser(msg):
    raise ValueError(msg)
print('a(raise,"msg")', a(raiser, "msg"))

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
print("a(a+1)  ", a(lambda: A + 1))
print("a(s+1)  ", a(lambda: s + 1))
print("a(true+1)", a(add_local_true))

# index errors
def index_local_true():
    b = True
    return b["x"]
def set_nil_x():
    n["x"] = 5
def set_a_x():
    arr = [1]
    arr["x"] = 5
def set_s_x():
    s["x"] = 5
def set_local_true_x():
    b = True
    b["x"] = 5
print("a(nil.x)", a(lambda: n["x"]))
print("a(a.x)  ", a(lambda: A["zz"]))
print("a(s.x)  ", a(lambda: s["x"]))
print("a(true.x)", a(index_local_true))
print("a(nil.x=5)", a(set_nil_x))
print("a(a.x=5)  ", a(set_a_x))
print("a(s.x=5)  ", a(set_s_x))
print("a(true.x=5)", a(set_local_true_x))

# len
def len_local_true():
    b = True
    return len(b)
print("a(#nil) ", a(lambda: len(n)))
print("a(#t)   ", a(lambda: len(t)))
print("a(#s)   ", a(lambda: len(s)))
print("a(#A)   ", a(lambda: len(A)))
print("a(#true)", a(len_local_true))

# comparison errors
def gt_local_true():
    b = True
    return b > 1
print("a(nil>1)", a(lambda: None > 1))
print("a(A>1)  ", a(lambda: A > 1))
print("a(s>1)  ", a(lambda: s > 1))
print("a(true>1)", a(gt_local_true))

# unary minus errors
def neg_local_true():
    b = "x"
    return -b
print("a(-nil)", a(lambda: -n))
print("a(-A)  ", a(lambda: -A))
print("a(-s)  ", a(lambda: -s))
print("a(-str)", a(neg_local_true))

# concatenation of mixed types
def concatsuite(comparefunc):
    print('"a"+"b"', comparefunc("a", "b"))
    print('"a"+None', comparefunc("a", None))
    print('None+"b"', comparefunc(None, "b"))
    print('"a"+{}', comparefunc("a", {}))
    print('{}+"b"', comparefunc({}, "b"))
    print('"a"+2', comparefunc("a", 2))
    print('2+"b"', comparefunc(2, "b"))
    print('"a"+print', comparefunc("a", print))
    print('print+"b"', comparefunc(print, "b"))
    print('"a"+True', comparefunc("a", True))
    print('True+"b"', comparefunc(True, "b"))
    print("None+True", comparefunc(None, True))
    print('"a"+3.5', comparefunc("a", 3.5))
    print('3.5+"b"', comparefunc(3.5, "b"))

def strconcat(x, y):
    return a(lambda: x + y)

def lstconcat(x, y):
    t = [x, y]
    return a(lambda: "-".join(t))

print("-------- string concatenation")
concatsuite(strconcat)
print("-------- list join")
concatsuite(lstconcat)

# iteration over values that are not iterable
def iter_local_true():
    b = True
    return [k for k in b]
print("-------- iteration tests")
print("a(iter(None))", a(lambda: [k for k in None]))
print("a(iter(A))  ", a(lambda: [k for k in A]))
print("a(iter(s))  ", a(lambda: [k for k in s]))
print("a(iter(true))", a(iter_local_true))

# bad arguments to builtins
def sm(x):
    return int(x)
print("-------- builtin argument tests")
print("a(int(nil))", a(lambda: sm(None)))
print("a(int(t))  ", a(lambda: sm(t)))
print("a(int(s))  ", a(lambda: sm(s)))
print("a(int('1'))", a(lambda: sm("1")))

def bad_raise_arg():
    raise KeyError("some message")
print('raise KeyError("msg")', a(bad_raise_arg))
