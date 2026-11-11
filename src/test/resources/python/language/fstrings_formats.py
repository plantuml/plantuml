# f-strings and str.format / % formatting: conversions, specs, nesting, escapes.
x = 42
f = 3.14159
s = "hi"
n = None
l = [1, "a"]
d = {"k": 1}

print(f"{x} {f} {s} {n} {l} {d}")
print(f"{s!r} {s!s} {l!r} {n!r} {x!r} {f!r}")
print(f"{x:5}|{x:<5}|{x:>5}|{x:^5}|{x:05}|{x:+}|{x: }|{-x:05}")
print(f"{x:x} {x:X} {x:o} {x:b} {x:#x} {x:#o} {x:#b} {x:08b} {x:,} {1234567:,} {1234567:_}")
print(f"{f:.2f} {f:8.3f}|{f:<8.1f}|{f:+.0f} {f:e} {f:.2e} {f:g} {1e20:g} {f:%} {0.5:.0%}")
print(f"{s:>6}|{s:<6}|{s:^6}|{s:*^7}|{s:.1}|{s:6.1}|{s:->4}")
print(f"{x:{5}} {f:{8}.{2}f} {s:{'>'}{6}}")
w = 7
p = 1
print(f"{f:{w}.{p}f}|{s:{w}}|{x:<{w}}|")
print(f"{{}} {{x}} {{{x}}} }}{{")
print(f"{x + 1} {x * 2} {x // 5} {x % 5} {x ** 2} {-x} {x > 3} {x if x else 0}")
print(f"{s.upper()} {s * 2} {len(s)} {l[0]} {l[-1]} {d['k']} {', '.join(['a', 'b'])}")
print(f"{'nested ' + s} {f'{x}'} {f'{x:3}'}|")
print(f"{x}" f"{s}" "plain" f"{f:.1f}")
print(f"""multi
{x}
line {s!r}""")
print(f'{"quotes"} {"a" "b"}')
print(f"{True} {False} {1 == 1} {None is None} {1.0} {1e100} {-0.0} {10 ** 9} {2 ** 30}")
print(f"{'':5}|{'':<5}|{'':>5}|{x!s:>5}|{l!r:>12}|")
print(f"{0:5} {0.0:5} {-1:3} {-1.5:6.2f} {1e-7} {123456789.123456789}")

# str.format
print("{} {} {}".format(1, "a", None))
print("{0} {1} {0}".format("x", "y"))
print("{a} {b}".format(a=1, b=2))
print("{0[0]} {0[1]} {1[k]}".format([1, 2], {"k": 3}))
print("{:>5} {:<5}| {:^5} {:.2f} {:5d} {:x}".format("a", "b", "c", 2.5, 3, 255))
print("{0!r} {1!s} {2:>{3}}".format("a", "a", "b", 4))
print("{{literal}} {}".format(1))
print("{:,} {:_} {:+d} {: d}".format(1234567, 1234567, 5, 5))
print("{0:{1}} {0:{1}.{2}}".format(3.14159, 8, 2))

# % formatting
print("%d %s %r %5d|%-5d|%05d|%+d" % (1, "a", "a", 2, 3, 4, 5))
print("%x %X %o %e %f %g %%" % (255, 255, 8, 1.5, 1.5, 1.5))
print("%5.2f|%-8.3f|%08.2f|%.0f|%5s|%-5s|%.2s" % (3.14159, 2.5, -1.5, 2.5, "ab", "cd", "xyz"))
print("%(a)s %(b)d %(a)r" % {"a": "x", "b": 2})
print("%s %s" % ("a", [1, 2]), "%s" % (None,), "%s" % 5, "%s" % "plain", "%r" % [1])
print("%c%c %c" % (72, "i", "!"), "%i" % 3.9, "%d" % -2.5, "%5.1f%%" % 99.5)
print("%*d|%-*d|%.*f" % (4, 1, 4, 2, 2, 3.14159))

# errors in formatting
def err(fn):
    try:
        fn()
    except Exception as e:
        return type(e).__name__
    return "ok"

print(err(lambda: "%d" % "a"), err(lambda: "%d %d" % (1,)), err(lambda: "%s" % (1, 2)), err(lambda: "{}".format()))
print(err(lambda: "{a}".format(b=1)), err(lambda: "{:d}".format("a")), err(lambda: "{:z}".format(1)), err(lambda: "%z" % 1))
