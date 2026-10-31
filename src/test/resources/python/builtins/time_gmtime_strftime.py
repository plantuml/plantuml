import time
stamps = [0, 1, 59, 86399, 86400, 951782400, 951868800, 1000000000, 1234567890, 1700000000, 1767225599, 1767225600, 2147483647, 4102444799.0, -1, -86400, -2208988800.0, 253402300799.0, -62135596800.0, 946684799, 1704067200]
for s in stamps:
    t = time.gmtime(s)
    print(s, tuple(t))
fmts = ["%a %A %b %B %c", "%C %d %D %e %F", "%g %G %h %H %I", "%j %m %M %p %r %R %S", "%T %u %U %V %w %W", "%x %X %y %Y %%", "[%z]", "plain text", "%d/%m/%Y %H:%M:%S"]
# %C of the year 1 is "0" with glibc and "00" in C99: not compared
for s in [x for x in stamps if x > -60000000000.0]:
    t = time.gmtime(s)
    for f in fmts:
        print(s, f, repr(time.strftime(f, t)))
for s in [0, 1234567890, 1700000000, -1]:
    print(time.asctime(time.gmtime(s)))
print(time.gmtime(1.9)[5], time.gmtime(-0.5)[:6], tuple(time.gmtime(1234567890.75)))
for bad in [(2026, 13, 1, 0, 0, 0, 0, 1, 0), (2026, 1, 32, 0, 0, 0, 0, 1, 0), (2026, 1, 1, 24, 0, 0, 0, 1, 0), (2026, 1, 1, 0, 60, 0, 0, 1, 0), (2026, 1, 1, 0, 0, 62, 0, 1, 0)]:
    try:
        time.strftime("%Y", bad)
        print("no error", bad)
    except ValueError as e:
        print("ValueError")
for bad in [(1, 2, 3), "x", 5]:
    try:
        time.strftime("%Y", bad)
    except TypeError:
        print("TypeError")
print(type(time.time()).__name__, time.time() > 1700000000)
