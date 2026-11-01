# str: literals, indexing, slicing, methods, Unicode.

def raises(exc, f, *args):
    try:
        f(*args)
    except exc:
        return True
    return False

# literals and escapes
assert 'single' == "single"
assert "a\tb" == "a" + chr(9) + "b"
assert "a\nb" == "a" + chr(10) + "b"
assert "\\" == chr(92) and len("\\") == 1
assert "\"" == '"' and '\'' == "'"
assert "\x41\x42" == "AB"
assert "é" == "é" and "€" == "€"
assert "\U0001F600" == "😀"
assert "\101" == "A"
assert "\0" == chr(0)
assert r"\n" == "\\n" and len(r"\n") == 2
assert r"a\tb" == "a\\tb"
assert "a" "b" 'c' == "abc"
assert """triple
quoted""" == "triple\nquoted"
assert '''it's "ok"''' == "it's \"ok\""
assert """a\
b""" == "ab"
assert "line1\
line2" == "line1line2"
text = """
  indented
"""
assert text == "\n  indented\n"

# length and indexing count characters, not bytes
assert len("") == 0 and len("abc") == 3
assert len("é") == 1 and len("€") == 1 and len("😀") == 1
assert len("aé€😀") == 4
assert "aé€😀"[3] == "😀"
assert "aé€😀"[-1] == "😀"
assert "abc"[0] == "a" and "abc"[-1] == "c" and "abc"[1] == "b"
assert raises(IndexError, lambda: "abc"[3])
assert raises(IndexError, lambda: "abc"[-4])
assert raises(IndexError, lambda: ""[0])
assert raises(AttributeError, lambda: "abc"[0].foo)

# slicing
s = "0123456789"
assert s[2:5] == "234"
assert s[:3] == "012"
assert s[7:] == "789"
assert s[:] == s
assert s[-3:] == "789"
assert s[:-3] == "0123456"
assert s[-5:-2] == "567"
assert s[::2] == "02468"
assert s[1::2] == "13579"
assert s[::-1] == "9876543210"
assert s[8:2:-2] == "864"
assert s[5:2] == ""
assert s[100:] == "" and s[:100] == s and s[-100:3] == "012"
assert s[2:5:1] == "234"
assert raises(ValueError, lambda: s[::0])
assert "aé€😀"[1:3] == "é€"
assert "aé€😀"[::-1] == "😀€éa"

# comparison and membership
assert "abc" < "abd" < "abe"
assert "é" > "z"
assert "😀" > "€"
assert "a" in "banana" and "nan" in "banana" and "x" not in "banana"
assert "" in "" and "" in "a"

# upper / lower
assert "Hello World".upper() == "HELLO WORLD"
assert "Hello World".lower() == "hello world"
assert "éàü".upper() == "ÉÀÜ"
assert "ÉÀÜ".lower() == "éàü"
assert "".upper() == ""
assert "abc123".upper() == "ABC123"

# strip
assert "  a b  ".strip() == "a b"
assert "  a b  ".lstrip() == "a b  "
assert "  a b  ".rstrip() == "  a b"
assert "\t\n a \r\n".strip() == "a"
assert "xxhixx".strip("x") == "hi"
assert "xyhiyx".strip("xy") == "hi"
assert "xxhixx".lstrip("x") == "hixx"
assert "xxhixx".rstrip("x") == "xxhi"
assert "".strip() == "" and "   ".strip() == ""

# split / join
assert "a b  c".split() == ["a", "b", "c"]
assert "  a b  ".split() == ["a", "b"]
assert "a,b,,c".split(",") == ["a", "b", "", "c"]
assert "a,b,c".split(",", 1) == ["a", "b,c"]
assert "a--b--c".split("--") == ["a", "b", "c"]
assert "".split() == []
assert "".split(",") == [""]
assert "abc".split("x") == ["abc"]
assert ",a,".split(",") == ["", "a", ""]
assert " a  b ".split(" ") == ["", "a", "", "b", ""]
assert "a b c d".split(None, 2) == ["a", "b", "c d"]
assert raises(ValueError, lambda: "abc".split(""))
assert ",".join(["a", "b", "c"]) == "a,b,c"
assert "".join(["a", "b"]) == "ab"
assert ", ".join(()) == ""
assert "-".join(["x"]) == "x"
assert "".join("abc") == "abc"
assert "+".join(["1", "2"]) == "1+2"
assert raises(TypeError, lambda: ",".join([1, 2]))
assert raises(TypeError, lambda: ",".join(5))
assert "a\nb\r\nc\rd".splitlines() == ["a", "b", "c", "d"]
assert "a\nb\n".splitlines() == ["a", "b"]
assert "a\nb\n".splitlines(True) == ["a\n", "b\n"]
assert "".splitlines() == []
assert "\n".splitlines() == [""]

# replace
assert "banana".replace("a", "o") == "bonono"
assert "banana".replace("an", "AN", 1) == "bANana"
assert "banana".replace("x", "y") == "banana"
assert "banana".replace("a", "") == "bnn"
assert "abc".replace("", "-") == "-a-b-c-"
assert "aaa".replace("aa", "b") == "ba"
assert "a.b.c".replace(".", "") == "abc"
assert "tab\tsep".replace("\t", "    ") == "tab    sep"

# find / index / count
assert "banana".find("an") == 1
assert "banana".find("an", 2) == 3
assert "banana".find("an", 2, 4) == -1
assert "banana".find("x") == -1
assert "banana".find("") == 0
assert "banana".rfind("an") == 3
assert "banana".rfind("a") == 5
assert "banana".rfind("x") == -1
assert "aé€😀é".find("é") == 1 and "aé€😀é".rfind("é") == 4
assert "banana".index("nan") == 2
assert raises(ValueError, lambda: "banana".index("x"))
assert "banana".count("a") == 3
assert "banana".count("an") == 2
assert "aaaa".count("aa") == 2
assert "abc".count("") == 4
assert "banana".count("x") == 0

# startswith / endswith
assert "hello".startswith("he") and not "hello".startswith("lo")
assert "hello".endswith("lo") and not "hello".endswith("he")
assert "hello".startswith("") and "hello".endswith("")
assert "hello".startswith(("x", "he")) and "hello".endswith(("x", "lo"))
assert not "hello".startswith(("x", "y"))
assert "hello".startswith("ll", 2) and "hello".endswith("ll", 0, 4)

# classification
assert "123".isdigit() and not "12a".isdigit() and not "".isdigit()
assert "abc".isalpha() and not "ab1".isalpha() and not "".isalpha()
assert "ab1".isalnum() and not "a b".isalnum() and not "".isalnum()
assert " \t\n".isspace() and not " a ".isspace() and not "".isspace()
assert "é".isalpha() and "€".isalpha() is False
assert "ABC".isupper() and not "AbC".isupper()
assert "abc".islower() and not "aBc".islower()

# padding
assert "ab".ljust(5) == "ab   " and "ab".ljust(5, "*") == "ab***"
assert "ab".rjust(5) == "   ab" and "ab".rjust(5, "*") == "***ab"
assert "ab".center(6) == "  ab  " and "ab".center(5, "*") == "**ab*" and "abc".center(6, "*") == "*abc**"
assert "ab".ljust(1) == "ab" and "ab".rjust(0) == "ab"
assert "42".zfill(5) == "00042" and "-42".zfill(5) == "-0042" and "+4".zfill(3) == "+04"
assert "abcdef".zfill(3) == "abcdef"

# case helpers used with indexing
assert "hello"[0].upper() + "hello"[1:] == "Hello"
assert "".join([c.upper() if i % 2 == 0 else c for i, c in enumerate("abcdef")]) == "AbCdEf"

# immutability
s = "abc"
try:
    s[0] = "x"
    assert False
except TypeError:
    pass

# conversion
assert str(12) == "12" and str(-3.5) == "-3.5" and str([1, "a"]) == "[1, 'a']"
assert str("a") == "a" and repr("a") == "'a'" and repr("it's") == '"it\'s"'
assert repr('say "hi"') == "'say \"hi\"'"
assert repr("a\nb\t\\") == "'a\\nb\\t\\\\'"
assert repr("\x00\x1f\x7f") == "'\\x00\\x1f\\x7f'"
assert repr("é€") == "'é€'"
assert repr("") == "''"
assert ord("a") == 97 and ord("é") == 233 and ord("😀") == 128512
assert chr(97) == "a" and chr(233) == "é" and chr(128512) == "😀"
assert chr(ord("z")) == "z"
assert raises(TypeError, lambda: ord("ab"))
assert raises(TypeError, lambda: ord(""))
assert raises(ValueError, lambda: chr(1114112))
assert raises(TypeError, lambda: chr("a"))
assert [ord(c) for c in "aé"] == [97, 233]
assert "".join([chr(c) for c in range(65, 70)]) == "ABCDE"

# a few usual idioms
assert "a-b_c d".replace("-", " ").replace("_", " ").split() == ["a", "b", "c", "d"]
assert " ".join("the quick brown fox".split()[::-1]) == "fox brown quick the"
assert "racecar" == "racecar"[::-1]
assert sorted("hello world".replace(" ", "")) == ["d", "e", "h", "l", "l", "l", "o", "o", "r", "w"]
assert "x=%s" % 5 == "x=5"
assert "".join(reversed("abc")) == "cba"
counts = {}
for c in "mississippi":
    counts[c] = counts.get(c, 0) + 1
assert counts == {"m": 1, "i": 4, "s": 4, "p": 2}
print("héllo".upper(), len("héllo"), "😀"[0], "€" * 3)
