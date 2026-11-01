print("a b".title(), "abc".capitalize(), "AbC".swapcase(), "abc".rindex("c"), "a-b".partition("-"), "a-b-c".rpartition("-"))
print("%5s|%-5s|%05d|%+d|%x|%o|%c|%r" % ("a", "b", 42, 5, 255, 8, 65, "q"))
print("{:>8}|{:<8}|{:^8}|{:08.3f}|{:+}|{:#x}|{:,}".format("a","b","c",3.14159,5,255,1234567))
print(list(map(str, [1,2])), list(zip([1,2],[3,4])), list(enumerate("ab", 1)))
print(sorted({"b":1,"a":2}.items(), key=lambda kv: kv[1]))
a = [1,2,3,4,5]
print(a[::2], a[::-1], a[1:-1], a[-2:], a[10:], a[:0])
a[1:3] = [9]; print(a); del a[0]; print(a)
print(isinstance(1, (int, str)), isinstance(True, int), type(1.0).__name__, type([]) is list)
print(abs(-3), abs(-3.5), pow(2, 10), pow(2, 0.5), round(1.2345, 2), divmod(7, 2), 7 // 2.0, -7 % 3.0)
print(min([3,1,2], key=lambda x: -x), max("abc"), sum([0.1]*3), any([]), all([]))
x = 5
print(x if x > 3 else 0, not x, x and 0, x or 0, 1 < x < 10, x is None, x is not None)
print(int("  12 "), float("1e3"), float("inf"), str(1.0), str(True), bool([]), int(True), int(-2.7))
print(chr(955), ord("é"), len("λ😀"), "λ😀"[1], "ab" * 3, [0] * 3, (1,) * 2)
print({1: 'a', 2.0: 'b'}, {True: 1}, (1, 2) < (1, 3), [1, 2] == [1, 2], "a" < "b")
print(repr("it's"), repr('say "hi"'), repr("\n\t\\"), repr(1e22), repr(1e-7), 1e16, 123456789.0)
d = {"a": 1}; d.update(b=2); d.setdefault("c", 3); print(d, d.pop("a"), d.get("z", 0), "b" in d, len(d))
for i, (k, v) in enumerate(d.items()): print(i, k, v)
def f(a, b=2, *args, c=3, **kw): return (a, b, args, c, kw)
print(f(1), f(1, 2, 3, 4, c=5, z=6), f(*[1, 2], **{"c": 9}))
g = lambda *a, **k: (a, k); print(g(1, x=2))
while True:
    break
else:
    print("no")
for i in range(3):
    pass
else:
    print("done", i)
print(list(range(10, 0, -3)), range(5)[2], len(range(0, 10, 3)), 3 in range(5), list(reversed(range(3))))
