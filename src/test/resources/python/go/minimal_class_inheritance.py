class Base:
    kind = 'base'
    def value(self):
        return 3

class Child(Base):
    extra = 'child'

c = Child()
print(c.kind)
print(c.extra)
print(c.value())
print(isinstance(c, Base), issubclass(Child, Base))
