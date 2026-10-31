class A: pass

class B(A):
    """Doc."""
    size = 1

    def __init__(self, x, y=2):
        self.x = x
        self.y = y

    def area(self):
        return self.x * self.y

class C():
    pass

class D(mod.Base,):
    class Inner:
        pass

obj.a.b = 1
obj.n += 2
del obj.a, obj.b
x, obj.y = 1, 2
