inc = lambda x: x + 1
print(inc(2))

add = lambda a, b=10: a + b
print(add(5))
print(add(5, 6))
print(add(a=1, b=2))

collect = lambda *items, **kw: len(items) + len(kw)
print(collect())
print(collect(1, 2, a=3))

choose = lambda x: 'big' if x > 3 else 'small'
print(choose(2))
print(choose(5))
