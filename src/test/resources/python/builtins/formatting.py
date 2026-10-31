# String formatting: the % operator, f-strings, str.format, repr / str.

def raises(exc, f, *args):
    try:
        f(*args)
    except exc:
        return True
    return False

# --- % operator ---
assert "%d" % 42 == "42"
assert "%i" % -7 == "-7"
assert "%5d|%-5d|%05d" % (42, 42, 42) == "   42|42   |00042"
assert "%+d % d" % (5, 5) == "+5  5"
assert "%x %X %o %#x %#o" % (255, 255, 8, 255, 8) == "ff FF 10 0xff 0o10"
assert "%08.3f" % 3.14159 == "0003.142"
assert "%.2f" % 2.675 == "2.67" and "%.2f" % 0.125 == "0.12" and "%.1f" % 0.25 == "0.2"
assert "%.0f" % 0.5 == "0" and "%.0f" % 1.5 == "2" and "%.0f" % 2.5 == "2"
assert "%f" % 1.5 == "1.500000"
assert "%10.2f|%-10.2f|" % (3.14159, 3.14159) == "      3.14|3.14      |"
assert "%e" % 12345.678 == "1.234568e+04"
assert "%.2e" % 0.000123 == "1.23e-04"
assert "%E" % 12345.678 == "1.234568E+04"
assert "%g" % 100000.0 == "100000" and "%g" % 1000000.0 == "1e+06"
assert "%g" % 0.0001 == "0.0001" and "%g" % 0.00001 == "1e-05"
assert "%g" % 1.5 == "1.5" and "%.3g" % 3.14159 == "3.14"
assert "%s" % "str" == "str"
assert "%s|%s|%s" % (1, 2.5, None) == "1|2.5|None"
assert "%s" % [1, "a"] == "[1, 'a']"
assert "%s" % (1,) == "1"
assert "%r" % "a" == "'a'" and "%r" % 1 == "1"
assert "%10s|%-10s|" % ("ab", "ab") == "        ab|ab        |"
assert "%.2s" % "abcdef" == "ab"
assert "%c%c" % (65, "b") == "Ab"
assert "100%%" % () == "100%"
assert "%d%%" % 50 == "50%"
assert "%d" % 3.9 == "3"
assert "%d" % True == "1"
assert "%f" % 1 == "1.000000"
assert "%(a)s-%(b)d" % {"a": "x", "b": 2} == "x-2"
assert "%s" % {"a": 1} == "{'a': 1}"
assert "no format" % () == "no format"
assert raises(TypeError, lambda: "%d" % "x")
assert raises(TypeError, lambda: "%d" % None)
assert raises(TypeError, lambda: "%d %d" % (1,))
assert raises(TypeError, lambda: "%d" % (1, 2))
assert raises(TypeError, lambda: "%s" % (1, 2))
assert raises(TypeError, lambda: "no format" % 5)
assert raises(ValueError, lambda: "%y" % 5)
assert raises(KeyError, lambda: "%(zz)s" % {"a": 1})
assert raises(TypeError, lambda: "%c" % 1.5)

# --- f-strings ---
name = "World"
n = 42
pi = 3.14159265
assert f"Hello {name}!" == "Hello World!"
assert f"{n}" == "42" and f"{n + 1}" == "43" and f"{n * 2:5d}|" == "   84|"
assert f"{pi:.2f}" == "3.14" and f"{pi:10.3f}|" == "     3.142|"
assert f"{pi:<10.1f}|" == "3.1       |" and f"{pi:^10.1f}|" == "   3.1    |"
assert f"{n:05d}" == "00042" and f"{n:+d}" == "+42" and f"{n:x}" == "2a" and f"{n:#x}" == "0x2a"
assert f"{n:b}" == "101010" and f"{n:o}" == "52" and f"{n:X}" == "2A"
assert f"{1234567:,}" == "1,234,567" and f"{1234567.891:,.2f}" == "1,234,567.89"
assert f"{1234567:_}" == "1_234_567"
assert f"{name:>8}|{name:<8}|{name:^9}|" == "   World|World   |  World  |"
assert f"{name:*^11}" == "***World***" and f"{name:->8}" == "---World"
assert f"{name!r}" == "'World'" and f"{name!s}" == "World"
assert f"{'quoted'}" == "quoted" and f"{[1, 2][0]}" == "1"
assert f"{{literal}}" == "{literal}" and f"{{{n}}}" == "{42}"
assert f"{n}{n}" == "4242" and f"a{n}b{name}c" == "a42bWorldc"
assert f"{n if n > 10 else 0}" == "42"
assert f"{ n }" == "42"
assert f"{ {'a': 1}['a'] }" == "1"
assert f"{None} {True} {1.5} {[1]} {(1, 2)}" == "None True 1.5 [1] (1, 2)"
assert f"{0.1 + 0.2}" == "0.30000000000000004"
assert f"{1e20}" == "1e+20" and f"{1e-7}" == "1e-07"
assert f"{12.0}" == "12.0" and f"{12.0:g}" == "12" and f"{0.5:%}" == "50.000000%"
assert f"{255:08b}" == "11111111"
assert f"{-5:05d}" == "-0005" and f"{-5.5:08.2f}" == "-0005.50"
assert f"{3:>{n}}" == " " * 41 + "3"
width = 6
prec = 2
assert f"{pi:{width}.{prec}f}|" == "  3.14|"
assert f"""triple {n}""" == "triple 42"
assert rf"\d{n}" == "\\d42"
assert f"{'a' + 'b'}" == "ab"
assert f"{name.upper()}" == "WORLD"
assert f"{len(name)}" == "5"
assert f"{n:d}" == "42" and f"{n:>6}" == "    42" and f"{n:<6}|" == "42    |"
assert f"{'x':3}|" == "x  |" and f"{5:3}|" == "  5|"

# --- str.format ---
assert "{} {}".format("a", "b") == "a b"
assert "{1} {0} {1}".format("a", "b") == "b a b"
assert "{name} is {age}".format(name="Bob", age=30) == "Bob is 30"
assert "{:>5}|{:<5}|{:^5}|".format("a", "b", "c") == "    a|b    |  c  |"
assert "{:5d}|{:05d}|{:+d}".format(42, 42, 42) == "   42|00042|+42"
assert "{:.3f}".format(2.0) == "2.000"
assert "{0:x} {0:b} {0:o}".format(10) == "a 1010 12"
assert "{!r}".format("a") == "'a'"
assert "{{}}".format() == "{}"
assert "{:,}".format(1000000) == "1,000,000"
assert "{:*^7}".format("ab") == "**ab***"
assert raises(IndexError, lambda: "{} {}".format(1))
assert raises(KeyError, lambda: "{a}".format(b=1))
assert raises(ValueError, lambda: "{".format())
assert raises(ValueError, lambda: "{:z}".format(1))

# --- str / repr of the containers ---
assert str([]) == "[]" and str(()) == "()" and str({}) == "{}"
assert str((1,)) == "(1,)" and str((1, 2)) == "(1, 2)"
assert str([1, [2, [3]]]) == "[1, [2, [3]]]"
assert str({"a": [1, {"b": None}]}) == "{'a': [1, {'b': None}]}"
assert str(["a", 'b"', "c'"]) == "['a', 'b\"', \"c'\"]"
assert repr([1.5, True, None]) == "[1.5, True, None]"
assert str({1: "a", 2.5: "b", (1, 2): "c"}) == "{1: 'a', 2.5: 'b', (1, 2): 'c'}"
assert str(range(3)) == "range(0, 3)" and str(range(1, 5, 2)) == "range(1, 5, 2)"
assert str([(1, "a")]) == "[(1, 'a')]"
assert str(print).startswith("<built-in function")
assert str(lambda: 1).startswith("<function")
assert str(True) + str(None) == "TrueNone"
assert str(1_000) == "1000" and str(-0) == "0" and str(+5) == "5"
assert str(2147483647) == "2147483647" and str(-2147483647 - 1) == "-2147483648"

# print
print("%-6s|%6.2f|%03d" % ("ab", 3.14159, 7))
print(f"{'x':>4}{n:>4}{pi:>8.3f}")
print([1, "two", 3.0, None, (4,), {"five": 5}])
print("a", "b", 1, None, sep=", ", end=" END\n")
print("é", "😀", "\t|")
print(1.0, 1e100, 1.5e-10, 123456789012345.0, 1e16, 0.1, 2147483647, -2147483647 - 1)
