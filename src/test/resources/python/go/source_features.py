def add(a, b):
    c = a + b
    return c

print(add(4, 5))

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

total = 0
for x in range(5):
    total = total + x
print(total)

items = [0, 1, 2, 3, 4]
print(items[1:4])
items[1:4] = [8, 9]
print(items)

d = {'a': 1, 'b': 2}
print('a' in d)
del d['a']
print('a' not in d)
