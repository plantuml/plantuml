# getattr, setattr, delattr, hasattr, issubclass

class Base:
    kind = 'base'

    def __init__(self):
        self.x = 1

    def hello(self):
        return 'hello ' + str(self.x)


class Child(Base):
    pass


def attempt(f):
    try:
        return f()
    except Exception as e:
        return type(e).__name__


c = Child()
print(getattr(c, 'x'), getattr(c, 'kind'), getattr(c, 'hello')())
print(getattr(c, 'missing', 'default'), getattr(c, 'x', 'default'), getattr(c, 'missing', None))
print(attempt(lambda: getattr(c, 'missing')))
print(attempt(lambda: getattr(c, 1)), attempt(lambda: getattr(c, 1, 2)))
print(attempt(lambda: getattr(c)), attempt(lambda: getattr(c, 'a', 1, 2)))
print(getattr('abc', 'upper')(), getattr([3, 1], 'missing', 0))
print(getattr(Child, 'kind'), getattr(Child, '__name__'))

setattr(c, 'y', 2)
setattr(c, 'x', 10)
print(c.x, c.y, c.hello())
setattr(Child, 'kind', 'child')
print(Child.kind, Base.kind, c.kind)
print(attempt(lambda: setattr(1, 'a', 2)), attempt(lambda: setattr(c, 2, 2)), attempt(lambda: setattr(c, 'a')))

print(hasattr(c, 'y'), hasattr(c, 'z'), hasattr(Child, 'hello'), hasattr(1, 'x'), hasattr('', 'join'))
print(attempt(lambda: hasattr(c, 1)), attempt(lambda: hasattr(c)))
delattr(c, 'y')
print(hasattr(c, 'y'))
print(attempt(lambda: delattr(c, 'y')), attempt(lambda: delattr(c, 'kind')), attempt(lambda: delattr(c, 3)))
delattr(Child, 'kind')
print(Child.kind)

e = ValueError('a', 'b')
print(getattr(e, 'args'), hasattr(e, 'args'), hasattr(e, 'other'))
setattr(e, 'other', 5)
print(e.other, getattr(e, 'other'))

print(issubclass(Child, Base), issubclass(Base, Child), issubclass(Child, Child), issubclass(Child, object))
print(issubclass(bool, int), issubclass(int, bool), issubclass(KeyError, LookupError), issubclass(KeyError, Exception))
print(issubclass(Child, (int, Base)), issubclass(Child, (int, str)), issubclass(Child, ()), issubclass(Child, (int, (str, Base))))
print(attempt(lambda: issubclass(c, Base)), attempt(lambda: issubclass(Child, c)), attempt(lambda: issubclass(Child, (Base, 1))))
print(isinstance(c, Base), isinstance(c, (int, Child)), isinstance(True, int), attempt(lambda: isinstance(c, 1)))
