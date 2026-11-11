# Strings are sequences of code points: indices, lengths, slices, iteration and
# methods must count a character outside the BMP (an emoji) as one, whatever the
# way the interpreter stores its strings.  Written with escapes only, so that
# the file is plain ASCII.

e = "\U0001F600"          # one code point, two UTF-16 units
a = "a" + e + "b"          # 3 code points
m = "x\u00e9" + e + "\u4e2d" + e + "z"   # x, e-acute, emoji, CJK, emoji, z: 6 code points

# length
print(len(""), len("abc"), len(e), len(a), len(m))
print(len(e * 40), len("ab" * 20 + e))   # long strings (cache of the scan)

# indexing
print(a[0] == "a", a[1] == e, a[2] == "b", a[-1] == "b", a[-2] == e, a[-3] == "a")
print([c == e for c in m])
print(ord(a[1]), ord(m[1]), ord(m[3]), hex(ord(e)))
print(chr(0x1F600) == e, chr(0xe9) == m[1], len(chr(0x10FFFF)), ord(chr(0x10FFFF)))
for s, i in ((a, 3), (a, -4), (m, 6), (m, -7)):
    try:
        s[i]
    except IndexError as err:
        print("IndexError", err)

# slices
print(a[1:] == e + "b", a[:2] == "a" + e, a[1:2] == e, a[2:] == "b", a[:-1] == "a" + e)
print(m[2:5] == e + "\u4e2d" + e, m[1:3] == "\u00e9" + e, m[-4:-1] == e + "\u4e2d" + e)
print(m[::2] == "x" + e + e, m[1::2] == "\u00e9\u4e2d" + "z", m[::-1] == "z" + e + "\u4e2d" + e + "\u00e9x")
print(m[::-2] == "z\u4e2d\u00e9", m[5:1:-2] == "z\u4e2d", m[2:2] == "", m[9:] == "", m[-99:2] == "x\u00e9")
print(len(m[1:5]), len(m[::3]), len(m[::-1]), len((e * 5)[1:4]))
print([len(a[i:j]) for i in range(-4, 5) for j in range(-4, 5)])
print([len(m[i:j:3]) for i in range(-7, 8) for j in range(-7, 8)])

# iteration, membership, reversed, enumerate
print(len(list(m)), [c == e for c in m], [i for i, c in enumerate(m) if c == e])
print("".join(reversed(m)) == m[::-1], e in a, e + "b" in a, "b" in a)
print(len({c: 1 for c in m}), len({c: 1 for c in list(m) + list(a)}))

# searching
print(a.find("b"), a.find(e), a.find(e, 2), a.rfind(e), a.index("b"), a.count(e), m.count(e))
print(m.find("z"), m.rfind(e), m.find(e, 3), m.find(e, 2, 3), m.find(e, 3, 5), m.find("\u4e2d"))
print(m.startswith("x\u00e9"), m.startswith(e, 2), m.endswith(e + "z"), m.endswith("z", 0, 5), m.endswith(e, 0, 5))
try:
    a.index("zz")
except ValueError as err:
    print("ValueError", err)

# splitting, joining, stripping, replacing
print([len(p) for p in m.split(e)], [p == "x\u00e9" for p in m.split(e)], len(m.split(e, 1)))
print(len(m.rsplit(e, 1)), [len(p) for p in (e + " " + e + "  " + e).split()])
print(len((" " + e + " ").strip()), len(("\u3000" + e + "\u3000").strip()), (e + "a" + e).strip(e) == "a")
print(len(m.partition(e)), [len(p) for p in m.partition(e)], [len(p) for p in m.rpartition(e)])
print("-".join([e, a]) == e + "-" + a, len(e.join("abc")), e.join("ab") == "a" + e + "b")
print(m.replace(e, "-") == "x\u00e9-\u4e2d-z", a.replace("a", e) == e + e + "b", a.replace("", "-") == "-a-" + e + "-b-")
print(len(a.replace(e, "")), a.replace(e, "", 1) == "ab", len(m.replace("\u4e2d", e + e)))

# padding and formatting count code points
print(len(e.center(5)), len(e.ljust(4)), len(e.rjust(4)), len(e.zfill(3)), e.ljust(3, e) == e * 3)
print(len("%5s" % e), len("%-5s|" % e), len("%.1s" % (e + "b")), "%c" % 0x1F600 == e)
print(len("{:5}".format(e)), len("{:>4}".format(e)), len("{:^5}".format(e)), "{:.1}".format(e + "b") == e)
print(len(f"{e:5}"), len(f"{a!r:10}"), len(f"{e:{3}}"), len(f"{e}{e}"))
print(len(repr(e)), repr("\u00e9") == "'\u00e9'")

# case and character classes
print(e.upper() == e, ("\u00e9" + e).upper() == "\u00c9" + e, ("\u00c9" + e).lower() == "\u00e9" + e)
print(e.isalpha(), "\u00e9".isalpha(), "\u4e2d".isalpha(), e.isdigit(), "\u0663".isdigit(), e.isspace(), "\u3000".isspace())
print(e.isupper(), e.islower(), e.isalnum(), (e + "A").isupper(), "\u00e9a".isalnum())
print(a.title() == "A" + e + "B", "\u00e9a b".capitalize() == "\u00c9a b", "ab".swapcase() == "AB")

# comparison is by code point: a character above the BMP is greater than U+FFFF
print(e > "\uffff", "\uffff" < e, e > "\ue000", "\ud7ff" < e, e < "\uffff")
print(max(e, "\uffff") == e, min(e, "\uffff") == "\uffff", sorted(["\uffff", e, "\ue000", "a"]) == ["a", "\ue000", "\uffff", e])
print((e,) > ("\uffff",), [e] > ["\uffff"], "a" + e < "a\uffff", "a" + e > "a\uffff")
print(e == "\U0001F600", e + "" == e)
d = {e: 1, "\uffff": 2, "\u00e9": 3}
print(d[e], d["\uffff"], d["\u00e9"], e in d, sorted(d) == ["\u00e9", "\uffff", e])

# concatenation, repetition, multiplication of strings of different kinds
s = ""
for i in range(5):
    s += e if i % 2 else "a"
print(len(s), s == "a" + e + "a" + e + "a", s[1] == e, s[2] == "a", s[::-1] == s)
print(len(e * 0), len(e * -1), len(2 * a), (2 * a)[4] == e, (2 * a)[3] == "a")
print(len(a + m), (a + m)[3:5] == m[0:2], (a + m)[4] == "\u00e9")

# str() of non-strings, round trips through ord/chr
print(len(str(e)), str(e) == e, "".join([chr(ord(c)) for c in m]) == m, [ord(c) for c in e + "\u00e9"])
print(len("".join([chr(c) for c in range(0x1F600, 0x1F610)])), "".join(map(chr, [0x1F600, 0x61])) == e + "a")
