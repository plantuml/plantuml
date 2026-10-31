def collect(**kw):
    print(kw)
collect()
collect(a=1, b=2)
collect(**{'x': 3})

def mix(a, **kw):
    print(a)
    print(kw)
mix(1)
mix(2, b=3, c=4)

def all_args(a, *rest, **kw):
    print(a)
    print(rest)
    print(kw)
all_args(1, 2, 3, x=4)
all_args(*[5, 6], **{'z': 7})
