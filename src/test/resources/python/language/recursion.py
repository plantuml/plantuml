# Recursion, mutual recursion, accumulators, deep calls.
# There is no tail call elimination: depth stays under a few hundred.

def factorial(n):
    def helper(product, k):
        if k <= 0:
            return product
        return helper(k * product, k - 1)
    return helper(1, n)

assert factorial(0) == 1
assert factorial(5) == 120
assert factorial(10) == 3628800

def fib_slow(n):
    if n < 2:
        return n
    return fib_slow(n - 1) + fib_slow(n - 2)

assert fib_slow(15) == 610

def fib_iter(n):
    def helper(i, a, b):
        if i >= n:
            return a
        return helper(i + 1, b, a + b)
    return helper(1, 1, 1)

assert fib_iter(30) == 832040
assert fib_iter(40) == 102334155

def fib_all(n, i=1, a=1, b=1):
    if i >= n:
        return ()
    return (a,) + fib_all(n, i + 1, b, a + b)

assert fib_all(10) == (1, 1, 2, 3, 5, 8, 13, 21, 34)

def total(*args):
    s = 0
    for v in args:
        s += v
    return s

def f1(n, a=0, b=0, c=0):
    if n <= 0:
        return (a, a + b, a + b + c)
    return f1(n - 1, a, a + b, a + b + c)

def f2(n, *args):
    if n <= 0:
        return total(*args)
    return f2(n - 1, n, *args)

assert f1(0) == (0, 0, 0)
assert f1(3, 1, 2, 3) == (1, 6, 21)
assert f2(0, 1, 2, 3) == 6
assert f2(4, 1) == 1 + 4 + 3 + 2 + 1
assert f2(100) == 5050

# the call goes through a builtin
def absolute(x):
    return abs(x)

assert absolute(-1234) == 1234

# depth
def depth(n):
    if n == 0:
        return 0
    return 1 + depth(n - 1)

assert depth(0) == 0
assert depth(400) == 400

def ackermann(m, n):
    if m == 0:
        return n + 1
    if n == 0:
        return ackermann(m - 1, 1)
    return ackermann(m - 1, ackermann(m, n - 1))

assert ackermann(2, 3) == 9

# infinite recursion is an error that can be caught
def forever(n):
    return forever(n + 1) + 1

try:
    forever(0)
    assert False
except RecursionError:
    pass

# and the interpreter is still usable afterwards
assert depth(100) == 100

# mutual recursion at module level
def is_even(n):
    return True if n == 0 else is_odd(n - 1)

def is_odd(n):
    return False if n == 0 else is_even(n - 1)

assert is_even(100)
assert is_odd(99)
assert not is_even(7)

# hanoi
moves = []

def hanoi(n, src, dst, via):
    if n == 0:
        return
    hanoi(n - 1, src, via, dst)
    moves.append((src, dst))
    hanoi(n - 1, via, dst, src)

hanoi(4, "a", "c", "b")
assert len(moves) == 15
assert moves[0] == ("a", "b")
assert moves[-1] == ("b", "c")

# permutations and flatten
def permutations(items):
    if len(items) <= 1:
        return [list(items)]
    result = []
    for i in range(len(items)):
        rest = items[:i] + items[i + 1:]
        for p in permutations(rest):
            result.append([items[i]] + p)
    return result

assert len(permutations([1, 2, 3, 4])) == 24
assert permutations([1, 2, 3])[0] == [1, 2, 3]
assert permutations([1, 2, 3])[-1] == [3, 2, 1]

def flatten(x):
    if isinstance(x, list):
        out = []
        for item in x:
            out.extend(flatten(item))
        return out
    return [x]

assert flatten([1, [2, [3, [4, []]], 5], [[6]]]) == [1, 2, 3, 4, 5, 6]
