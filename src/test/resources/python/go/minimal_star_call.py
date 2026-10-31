def show(a, b=0, *rest):
    print(a)
    print(b)
    print(rest)

items = [1, 2, 3]
show(*items)
show(4, *[5, 6])
show(*[7])

pairs = [('x', 1), ('y', 2)]
def combine(a, b):
    print(a + b)
combine(*[10, 20])
