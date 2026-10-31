class Point:
    kind = 'pt'
    def __init__(self, x):
        self.x = x
    def add(self, y):
        return self.x + y

p = Point(6)
print(p.add(4))
print(p.kind)
