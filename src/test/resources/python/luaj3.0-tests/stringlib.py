print(string.find("1234567890", ".", 0, True))
print(string.find("alo alx 123 b\x00o b\x00o", "(..*) %1"))
print(string.find("aloALO", "%l*"))
print(string.find(" \n isto \xef\xbf\xbd assim", "%S%S*"))

print(string.find("", ""))
print(string.find("ababaabbaba", "abb"))
print(string.find("ababaabbaba", "abb", 7))

print(string.match("aabaa", "a*"))
print(string.match("aabaa", "a*", 3))
print(string.match("aabaa", "a*b"))
print(string.match("aabaa", "a*b", 3))

print(string.match("abbaaababaabaaabaa", "b(a*)b"))

print(string.match("abbaaababaabaaabaa", "b(a*)()b"))
print(string.match("abbaaababaabaaabaa", "b(a*)()b", 3))
print(string.match("abbaaababaabaaabaa", "b(a*)()b", 8))
print(string.match("abbaaababaabaaabaa", "b(a*)()b", 12))

print(string.byte("hi", -3))

print(string.gsub("ABC", "@(%x+)", lambda s: "|abcd"))
print(string.gsub("@123", "@(%x+)", lambda s: "|abcd"))
print(string.gsub("ABC@123", "@(%x+)", lambda s: "|abcd"))
print(string.gsub("ABC@123@def", "@(%x+)", lambda s: "|abcd"))
print(string.gsub("ABC@123@qrs@def@tuv", "@(%x+)", lambda s: "|abcd"))
print(string.gsub("ABC@123@qrs@def@tuv", "@(%x+)", lambda s: "@ab"))

print(tostring(1234567890123))
print(tostring(1234567890124))
print(tostring(1234567890125))

def f1(s, p):
    print(p)
    p = string.gsub(p, "%%([0-9])", lambda s: "%" .. (s + 1))
    print(p)
    p = string.gsub(p, "^(^?)", "%1()", 1)
    print(p)
    p = string.gsub(p, "($?)$", "()%1", 1)
    print(p)
    t = [string.match(s, p)]
    return string.sub(s, t[1], t[len(t)] - 1)

print(pcall(f1, "alo alx 123 b\x00o b\x00o", "(..*) %1"))

def badpat():
    print(string.gsub("alo", "(.)", "%2"))

print((pcall(badpat)))

for k, v in string.gmatch("w=200&h=150", "(%w+)=(%w+)"):
    print(k, v)

# string.sub
def t(str):
    i = [0, 1, 2, 8, -1]
    for ki, vi in ipairs(i):
        s, v = pcall(string.sub, str, vi)
        print('string.sub("' .. str .. '",' .. tostring(vi) .. ')=' .. tostring(s) .. ',"' .. tostring(v) .. '"')
        j = [0, 1, 2, 4, 8, -1]
        for kj, vj in ipairs(j):
            s, v = pcall(string.sub, str, vi, vj)
            print('string.sub("' .. str .. '",' .. tostring(vi) .. ',' .. tostring(vj) .. ')=' .. tostring(s) .. ',"' .. tostring(v) .. '"')

t("abcdefghijklmn")
t("abcdefg")
t("abcd")
t("abc")
t("ab")
t("a")
t("")

print(string.len("Hello, world"))
print(len("Hello, world"))
print(string.len("\x00\x00\x00"))
print(len("\x00\x00\x00"))
print(string.len("\x00\x01\x02\x03"))
print(len("\x00\x01\x02\x03"))
s = "My JaCk-O-lAnTeRn CaSe TeXt"
print(s, string.len(s), len(s))


# string.format
print(string.format("(%.0d) (%.0d) (%.0d)", 0, -5, 9))
print(string.format("(%.1d) (%.1d) (%.1d)", 0, -5, 9))
print(string.format("(%.2d) (%.2d) (%.2d)", 0, -5, 9))

print(string.format("(%+.0d) (%+.0d) (%+.0d)", 0, -5, 9))
print(string.format("(%+.1d) (%+.1d) (%+.1d)", 0, -5, 9))
print(string.format("(%+.2d) (%+.2d) (%+.2d)", 0, -5, 9))

print(string.format("(%+3d) (% 3d) (%+ 3d)", 55, 55, 55))

print(string.format("(%-1d) (%-1d) (%-1d)", 1, 12, -12))
print(string.format("(%-2d) (%-2d) (%-2d)", 1, 12, -12))
print(string.format("(%-3d) (%-3d) (%-3d)", 1, 12, -12))


print(string.format("(%8x) (%8d) (%8o)", 255, 255, 255))
print(string.format("(%08x) (%08d) (%08o)", 255, 255, 255))

print(string.format("simple%ssimple", " simple "))

def testformat(message, fmt, *args):
    s, e = pcall(string.format, fmt, *args)
    if s:
        if string.find(fmt, "q"):
            print(message, e)
        print(message, string.byte(e, 1, len(e)))
    else:
        print(message, "error", e)

testformat("plain %", "%%")
testformat("specials (%s)", "---%s---", " %% \x00 \r \n ")
testformat("specials (%q)", "---%q---", " %% \x00 \r \n ")
testformat("specials (%q)", "---%q---", "0%%0\x000\r0\n0")
testformat("controls (%q)", "---%q---", " \a \b \f \t \v \\ ")
testformat("controls (%q)", "---%q---", "0\a0\b0\f0\t0\v0\\0")
testformat("extended (%q)", "---%q---", " \xde \xdf \xe0 ")
testformat("extended (%q)", "---%q---", "0\xde0\xdf0\xe00")
testformat("embedded newlines", "%s\r%s\n%s", "===", "===", "===")

# format long string
print("this is a %s long string", string.rep("really, ", 30))

def pc(*args):
    s, e = pcall(*args)
    return s and e or "false-" .. type(e)

def strtests(name, func, *args):
    print(name, "good", pc(func, *args))
    print(name, "empty", pc(func))
    print(name, "table", pc(func, {}))
    print(name, "nil", pc(func, None))

strtests("lower", string.lower, s)
strtests("upper", string.upper, s)
strtests("reverse", string.reverse, s)
strtests("char", string.char, 92, 60, 61, 93)
# string.dump and binary chunks no longer exist in this fork; the original test was:
#   print('string.dump test:', load(string.dump(function(x) return 'foo->'..x end),'bar')('bat'))


# floating point formats (not supported yet)
#prefixes = ["", "+", "-"]
#lengths = ["7", "2", "0", "1", ""]
#letters = ["f", "e", "g"]
#for i, letter in ipairs(letters):
#    for k, before in ipairs(lengths):
#        for j, prefix in ipairs(prefixes):
#            spec = "(%" .. prefix .. before .. letter .. ")"
#            fmt = spec .. "\t" .. spec .. "\t" .. spec .. "\t" .. spec .. "\t" .. spec .. "\t" .. spec
#            print(spec, string.format(fmt, 12.34, -12.34, 1 / 11, -1 / 11, 300 / 11, -300 / 11))
#            for l, after in ipairs(lengths):
#                spec = "(%" .. prefix .. before .. "." .. after .. letter .. ")"
#                fmt = spec .. " " .. spec .. " " .. spec .. " " .. spec .. " " .. spec .. " " .. spec
#                print(spec, string.format(fmt, 12.34, -12.34, 1 / 11, -1 / 11, 300 / 11, -300 / 11))

def fmterr(*args):
    r, s = pcall(*args)
    if r:
        return s
    else:
        s = string.gsub(s, "stdin:%d+:%s*", "")
        return s

print(fmterr(string.find, "ab%c)0(", "%"))
print(fmterr(string.find, "ab%c)0(", "("))
print(pcall(string.find, "ab%c)0(", ")"))
