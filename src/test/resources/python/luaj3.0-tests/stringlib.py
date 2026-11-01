# String methods: searching, slicing, case, splitting, joining, formatting.
# (The Lua original used Lua patterns (string.find, string.match, string.gsub with
# %-classes), string.byte, string.dump and string.rep: they have no equivalent in
# the subset. The same operations are done with the Python str methods.)

print("1234567890".find(".", 0))
print("alo alx 123 b\x00o b\x00o".find("alx"))
print("aloALO".lower(), "aloALO".upper())
print(" \n isto \xef\xbf\xbd assim".split())

print("".find(""))
print("ababaabbaba".find("abb"))
print("ababaabbaba".find("abb", 7))
print("ababaabbaba".rfind("abb"))
print("ababaabbaba".index("aba"))

print("aabaa".strip("a"), "aabaa".lstrip("a"), "aabaa".rstrip("a"))
print("abbaaababaabaaabaa".split("b"))
print("abbaaababaabaaabaa".count("aa"))

print("hi"[-3:], "hi"[-1:])

print("ABC@123@qrs@def@tuv".replace("@", "|abcd"))
print("ABC@123@qrs@def@tuv".replace("@", "@ab", 2))

print(str(1234567890123))
print(str(1234567890124))
print(str(1234567890125))

print("w=200&h=150".split("&"))
for pair in "w=200&h=150".split("&"):
    k, v = pair.split("=")
    print(k, v)

# slicing (Lua: string.sub with its clamping rules)
def t(s):
    i = [0, 1, 2, 8, -1]
    for vi in i:
        j = [0, 1, 2, 4, 8, -1]
        for vj in j:
            print('slice("' + s + '",' + str(vi) + "," + str(vj) + ")=" + repr(s[vi:vj]))
        print('slice("' + s + '",' + str(vi) + ")=" + repr(s[vi:]))

t("abcdefghijklmn")
t("abcdefg")
t("abcd")
t("abc")
t("ab")
t("a")
t("")

print(len("Hello, world"))
print(len("\x00\x00\x00"))
print(len("\x00\x01\x02\x03"))
s = "My JaCk-O-lAnTeRn CaSe TeXt"
print(s, len(s))

# formatting of integers: precision, sign, width, alignment, bases
print("(%.0d) (%.0d) (%.0d)" % (0, -5, 9))
print("(%.1d) (%.1d) (%.1d)" % (0, -5, 9))
print("(%.2d) (%.2d) (%.2d)" % (0, -5, 9))

print("(%+.0d) (%+.0d) (%+.0d)" % (0, -5, 9))
print("(%+.1d) (%+.1d) (%+.1d)" % (0, -5, 9))
print("(%+.2d) (%+.2d) (%+.2d)" % (0, -5, 9))

print("(%+3d) (% 3d) (%+ 3d)" % (55, 55, 55))

print("(%-1d) (%-1d) (%-1d)" % (1, 12, -12))
print("(%-2d) (%-2d) (%-2d)" % (1, 12, -12))
print("(%-3d) (%-3d) (%-3d)" % (1, 12, -12))

print("(%8x) (%8d) (%8o)" % (255, 255, 255))
print("(%08x) (%08d) (%08o)" % (255, 255, 255))

print("simple%ssimple" % " simple ")

def testformat(message, fmt, *args):
    print(message, repr(fmt % args))

testformat("plain %", "%%")
testformat("specials (%s)", "---%s---", " %% \x00 \r \n ")
testformat("specials (%r)", "---%r---", " %% \x00 \r \n ")
testformat("specials (%r)", "---%r---", "0%%0\x000\r0\n0")
testformat("controls (%r)", "---%r---", " \a \b \f \t \v \\ ")
testformat("controls (%r)", "---%r---", "0\a0\b0\f0\t0\v0\\0")
testformat("extended (%r)", "---%r---", " \xde \xdf \xe0 ")
testformat("extended (%r)", "---%r---", "0\xde0\xdf0\xe00")
testformat("embedded newlines", "%s\r%s\n%s", "===", "===", "===")

print("this is a %s long string" % ("really, " * 30))

print("lower", "My JaCk-O-lAnTeRn".lower())
print("upper", "My JaCk-O-lAnTeRn".upper())
print("reverse", "My JaCk-O-lAnTeRn"[::-1])
print("chars", chr(92) + chr(60) + chr(61) + chr(93))

# errors while formatting
def fmterr(fmt, *args):
    try:
        return fmt % args
    except Exception as e:
        return type(e).__name__

print(fmterr("%d", "x"))
print(fmterr("%(a)s", 1))
