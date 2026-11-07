# Classes: attributes, methods, bound methods, class attributes, nested and local classes,
# user-defined exceptions.

class Point:
    """A point."""
    dims = 2

    def __init__(self, x, y=0):
        self.x = x
        self.y = y

    def norm2(self):
        return self.x * self.x + self.y * self.y

    def move(self, dx, dy=0):
        self.x += dx
        self.y += dy
        return self

p = Point(3, 4)
print(p.x, p.y, p.norm2(), Point.dims, p.dims)
p.move(1).move(0, dy=2)
print(p.x, p.y)
m = p.norm2
print(m())
print(Point.norm2(p))
print(type(p) is Point, isinstance(p, Point), isinstance(p, object), type(p).__name__)
print(Point, type(Point.norm2).__name__)

class Point3(Point):
    dims = 3
    def __init__(self, x, y, z):
        Point.__init__(self, x, y)
        self.z = z
    def norm2(self):
        return Point.norm2(self) + self.z ** 2

q = Point3(1, 2, 3)
print(q.norm2(), q.dims, isinstance(q, Point))
print([c.__name__ for c in [Point, Point3]])

class Empty:
    pass
e = Empty()
e.a = 1
print(e.a, callable(Empty), callable(e))
try:
    Empty(1)
except TypeError:
    print("TypeError")
try:
    e.zz
except AttributeError:
    print("AttributeError")
del e.a
try:
    print(e.a)
except AttributeError:
    print("deleted")

class MyError(ValueError):
    pass

class CodeError(Exception):
    def __init__(self, code, msg):
        Exception.__init__(self, msg)
        self.code = code
    def describe(self):
        return "E%d: %s" % (self.code, self.args[0])

try:
    raise MyError("bad", 2)
except ValueError as ex:
    print(type(ex).__name__, ex.args, str(ex), repr(ex))
try:
    raise CodeError(42, "oops")
except CodeError as ex:
    print(ex.code, ex.args, ex, ex.describe())
try:
    raise MyError
except MyError as ex:
    print("class raised", ex.args)

def make(n):
    class Counter:
        start = n
        def __init__(self):
            self.v = n
        def inc(self):
            self.v += n
            return self.v
    return Counter

C = make(5)
c = C()
print(c.inc(), c.inc(), C.start, C.__qualname__, C)

class Stack(object):
    def __init__(self):
        self.items = []
    def push(self, *xs):
        for x in xs:
            self.items.append(x)
    def __size(self):
        return len(self.items)
    def size(self):
        return self.__size()
s = Stack()
s.push(1, 2, 3)
print(s.size(), s.items, sorted([Stack(), s], key=lambda st: st.size())[1] is s)
Stack.kind = "lifo"
print(s.kind, Stack.kind)
k = {s: 1}
print(k[s], s == s, s != Stack(), s is not None)

class Outer:
    class Inner:
        def hello(self):
            return "hi"
    tag = "o"
print(Outer.Inner().hello(), Outer.Inner)
x = 10
class UsesGlobal:
    x = 1
    y = x + 1
    def get(self):
        return x
print(UsesGlobal.y, UsesGlobal().get())
class Sq:
    vals = [i * i for i in range(4)]
    f = lambda self: 7
print(Sq.vals, Sq().f())
