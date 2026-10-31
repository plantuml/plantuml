def join(*items, sep='-'):
    print(sep)
    print(items)

join('a', 'b')
join('a', 'b', sep=':')
join(*['x'], **{'sep': '/'})

def need(*items, marker):
    print(items)
    print(marker)

need(1, 2, marker=3)

fmt = lambda *items, sep='|': sep
print(fmt(1, 2))
print(fmt(1, 2, sep=':'))

count = lambda *items, required: len(items) + required
print(count(1, 2, required=5))
