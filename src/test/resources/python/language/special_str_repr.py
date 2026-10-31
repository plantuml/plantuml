class Point:
    def __init__(self, x, y):
        self.x = x
        self.y = y

    def __repr__(self):
        return "Point(%d, %d)" % (self.x, self.y)


class Named:
    def __init__(self, name):
        self.name = name

    def __str__(self):
        return "<" + self.name + ">"

    def __repr__(self):
        return "Named(" + repr(self.name) + ")"


class OnlyStr:
    def __str__(self):
        return "only str"


class Child(Named):
    pass


class Loud(Named):
    def __str__(self):
        return Named.__str__(self).upper()


p = Point(1, 2)
n = Named("bob")
print(repr(p), str(p), p)
print(repr(n), str(n), n)
print(str(OnlyStr()))
print("%s | %r | %s | %r" % (p, p, n, n))
print("{} | {!r} | {!s} | {}".format(n, n, n, p))
print(f"{n} {n!r} {n!s} {p} {p!r}")
print(format(n), format(p), format(n, ""))
print([p, n], (p, n), {"k": p, "j": n})
print(str([p, n]), repr((p, n)))
print(", ".join([str(a) for a in [p, n, OnlyStr()]]))
print(str(Child("kid")), repr(Child("kid")), Loud("shout"), repr(Loud("shout")))
print(n.__str__(), p.__repr__(), Named.__str__(n))
print([[p], [n, [p]]])
print(len(str(p)), str(n) + "!", str(n) == "<bob>", repr(p) == "Point(1, 2)")


class Bad:
    def __str__(self):
        return 42

    def __repr__(self):
        return None


for fn in [str, repr]:
    try:
        fn(Bad())
    except TypeError:
        print("TypeError")
try:
    print("%s" % Bad())
except TypeError:
    print("TypeError")


class Wrap:
    def __init__(self, inner):
        self.inner = inner

    def __repr__(self):
        return "Wrap(" + repr(self.inner) + ")"


print(Wrap(Wrap(p)), [Wrap(n)])
