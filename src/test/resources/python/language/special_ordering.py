class Ver:
    """Only __lt__: enough for sorted, min, max, < and, by reflection, >."""

    def __init__(self, n):
        self.n = n

    def __lt__(self, other):
        if isinstance(other, Ver) == False:
            return NotImplemented
        return self.n < other.n

    def __repr__(self):
        return "Ver(%d)" % self.n


class Full:
    def __init__(self, n):
        self.n = n

    def __lt__(self, other):
        return self.n < other.n

    def __le__(self, other):
        return self.n <= other.n

    def __gt__(self, other):
        return self.n > other.n

    def __ge__(self, other):
        return self.n >= other.n

    def __repr__(self):
        return "Full(%d)" % self.n


class Num:
    """Compares with ints too: the other side of the comparison is an int."""

    def __init__(self, n):
        self.n = n

    def __lt__(self, other):
        return self.n < (other.n if isinstance(other, Num) else other)

    def __gt__(self, other):
        return self.n > (other.n if isinstance(other, Num) else other)

    def __repr__(self):
        return "Num(%d)" % self.n


a, b, c = Ver(1), Ver(2), Ver(3)
print(a < b, b < a, a > b, b > a, c > a, a < a)
print(sorted([c, a, b]), sorted([c, a, b], reverse=True))
print(min([c, a, b]), max([c, a, b]), min(c, a), max(b, c))
items = [Ver(3), Ver(1), Ver(2)]
items.sort()
print(items)
print((a, 1) < (b, 0), (a, 2) < (a, 3), [a, b] < [a, c], [c] > [a, b])


def kind(f):
    try:
        return f()
    except TypeError:
        return "TypeError"


print(kind(lambda: a <= b), kind(lambda: a >= b))
print(kind(lambda: a < 5), kind(lambda: 5 < a), kind(lambda: a > "x"))
print(kind(lambda: sorted([a, 3])), kind(lambda: max(a, None)))

x, y = Full(1), Full(2)
print(x < y, x <= y, x > y, x >= y, y >= y, y <= y, x <= x)
print(sorted([y, x, Full(0)]), max(x, y), min(x, y))

n = Num(5)
print(n > 3, n < 3, 3 < n, 3 > n, n < Num(9), Num(9) > n)
print(kind(lambda: n <= 3), kind(lambda: 3 >= n))

# the order only, no equality: two objects that are neither smaller are not ==
p, q = Ver(7), Ver(7)
print(p < q, q < p, p == q, p == p, p != q)
print(sorted([Ver(2), Ver(1), Ver(2)]))


class Boom:
    def __lt__(self, other):
        raise ValueError("no order")


for fn in [lambda: Boom() < Boom(), lambda: sorted([Boom(), Boom()])]:
    try:
        fn()
    except ValueError:
        print("ValueError")

print(NotImplemented, type(NotImplemented).__name__)
