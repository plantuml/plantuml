a, b = [1, 2]
print(a)
print(b)

(c, d) = (3, 4)
print(c + d)

[e, f] = 'xy'
print(e)
print(f)

out = []
for x, y in [(1, 2), (3, 4)]:
    out.append(x + y)
print(out)
