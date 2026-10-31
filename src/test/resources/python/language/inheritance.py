# Classes: inheritance, explicit calls to the base class, private name mangling,
# attribute targets of augmented assignments.

class Account:
    __count = 0

    def __init__(self, owner):
        self.__owner = owner
        Account.__count += 1
        self.__log = []

    def __record(self, what):
        self.__log.append(what)

    def deposit(self, n):
        self.__record("deposit %d" % n)
        return self

    def owner(self):
        return self.__owner

    def history(self):
        return list(self.__log)

    def total(self):
        return Account.__count

class Savings(Account):
    def __init__(self, owner):
        Account.__init__(self, owner)
        self.__owner = "shadow"

    def both(self):
        return self.owner() + "/" + self.__owner

a = Account("ann").deposit(3).deposit(4)
print(a.owner(), a.history(), a.total())
print(a._Account__owner, Account._Account__count)
s = Savings("bob")
print(s.both(), s.total(), s._Savings__owner)
try:
    a.__owner
except AttributeError:
    print("private")

class Shape:
    def area(self):
        raise NotImplementedError("area")
    def describe(self):
        return "%s with area %s" % (type(self).__name__, self.area())

class Rect(Shape):
    def __init__(self, w, h):
        self.w, self.h = w, h
    def area(self):
        return self.w * self.h

class Square(Rect):
    def __init__(self, side):
        Rect.__init__(self, side, side)

shapes = [Rect(2, 3), Square(4)]
for sh in shapes:
    print(sh.describe())
print(sum([sh.area() for sh in shapes]))
try:
    Shape().describe()
except NotImplementedError as e:
    print("abstract", e)
print(isinstance(Square(1), (int, Shape)), isinstance(1, Shape), isinstance(Square(1), Rect))

class Node:
    def __init__(self, value, children=None):
        self.value = value
        self.children = children if children is not None else []
    def add(self, child):
        self.children.append(child)
        return child
    def walk(self, depth=0):
        out = ["  " * depth + str(self.value)]
        for c in self.children:
            out.extend(c.walk(depth + 1))
        return out

root = Node("root")
b = root.add(Node("b"))
b.add(Node("b1"))
root.add(Node("c"))
print("\n".join(root.walk()))
methods = [root.add, b.walk]
print(len(methods), methods[1](1))
print(f"{root.value!r} {len(root.children)}")
counter = {"n": 0}
class Holder:
    pass
h = Holder()
h.items = {}
h.items["k"] = [1]
h.items["k"] += [2]
h.n = 1
h.n *= 5
print(h.items, h.n)
