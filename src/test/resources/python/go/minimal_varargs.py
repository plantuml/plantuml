def collect(*items):
    return items
print(collect())
print(collect(1, 2, 3))

def head_tail(head, *tail):
    print(head)
    print(tail)
head_tail('a', 'b', 'c')
head_tail('z')

def with_default(a=1, *rest):
    print(a)
    print(rest)
with_default()
with_default(5, 6, 7)
