def add(a, b=0, c=0):
    return a + b + c

kw = {'b': 2, 'c': 3}
print(add(1, **kw))
print(add(**{'a': 4, 'c': 5}))
print(add(1, b=2, **{'c': 7}))

def show(a, b):
    print(a)
    print(b)
show(**{'a': 'x', 'b': 'y'})

def mix(a, b, c=0, *rest):
    print(a + b + c)
    print(rest)
mix(*[1], **{'b': 2, 'c': 3})
mix(1, *[2, 3], **{})
