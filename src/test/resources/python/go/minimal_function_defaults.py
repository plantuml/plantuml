def add(a, b=10):
    return a + b

print(add(2))
print(add(2, 3))
print(add(a=4))
print(add(4, b=5))

base = 7
def plus(x=base + 1):
    return x
base = 100
print(plus())
print(plus(9))

def pair(a, b=2, c=3):
    return a + b + c
print(pair(1))
print(pair(1, c=10))
