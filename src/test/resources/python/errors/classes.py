# Wrong uses of classes and instances. Only the type of the exception is compared.

class A:
    def __init__(self, x):
        self.x = x
    def get(self):
        return self.x

def check(f):
    try:
        f()
        print("no error")
    except Exception as e:
        print(type(e).__name__)

check(lambda: A())
check(lambda: A(1, 2))
check(lambda: A(1).get(2))
check(lambda: A.get())
check(lambda: A(1).missing)
check(lambda: A.missing)
check(lambda: A(x=3).get())
check(lambda: A(y=3))

class Bad:
    def __init__(self):
        return 1
check(lambda: Bad())

def set_on_int():
    n = 1
    n.attr = 2
check(set_on_int)

def set_on_object():
    o = object()
    o.attr = 2
check(set_on_object)
check(lambda: object(1))
print(type(object()).__name__, isinstance(A(1), object), isinstance(None, object))

def raise_instance():
    raise A(1)
check(raise_instance)

def del_missing():
    a = A(1)
    del a.y
check(del_missing)

def base_not_class():
    class C(3):
        pass
check(base_not_class)

class E(Exception):
    pass
check(lambda: E(code=1))

class WithInit(Exception):
    def __init__(self, a, b):
        self.a = a
        self.b = b
try:
    raise WithInit(1, 2)
except WithInit as w:
    print(w.args, w.a + w.b, str(w))
try:
    raise WithInit(1, 2)
except Exception as w:
    print(isinstance(w, WithInit), isinstance(w, BaseException))
try:
    try:
        raise E("inner")
    except KeyError:
        print("wrong")
except (ValueError, E) as e:
    print("caught", e)
