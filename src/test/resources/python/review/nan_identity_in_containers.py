# The containers compare their items with "is" before "==": a NaN is found in
# the list that holds it, and that list is equal to itself.
# VM at 148134d: False, then ValueError from index.
n = float('nan')
l = [n]
print(n == n, n in l, n in (n,), n in {n: 1}, n in {1: n}.values())
print(l.index(n), l.count(n), (n,).index(n), (n,).count(n))
print(l == l, [n] == [n], (n,) == (n,), {1: n} == {1: n}, [n] == [float('nan')])
l.remove(n)
print(l)
print(max([n, n]) is n, sorted([n]) == [n])
