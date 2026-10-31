"""Generates src/test/resources/python/language/limits.py.

    python3 tools/gen_limits.py src/test/resources/python/language/limits.py
"""
import sys

out = []
w = out.append
w("# Stress tests for the compiler and the VM: many locals, constants, upvalues,")
w("# parameters, long jumps and deep nesting.  Generated once, then checked in.")
w("")
# 1. many locals
w("def many_locals():")
n = 200
for i in range(n):
    w("    v%d = %d" % (i, i))
w("    total = " + " + ".join("v%d" % i for i in range(n)))
w("    return total")
w("")
w("assert many_locals() == %d" % sum(range(n)))
w("")
# 2. many constants
w("def many_constants():")
w("    values = [")
nc = 600
for i in range(nc):
    w("        %d.5," % i if i % 2 == 0 else "        'k%d'," % i)
w("    ]")
w("    return values")
w("")
w("vals = many_constants()")
w("assert len(vals) == %d" % nc)
w("assert vals[0] == 0.5 and vals[1] == 'k1' and vals[%d] == %d.5 and vals[%d] == 'k%d'" % (nc - 2, nc - 2, nc - 1, nc - 1))
w("assert sum([v for v in vals if not isinstance(v, str)]) == %s" % repr(sum(i + 0.5 for i in range(0, nc, 2))))
w("")
# constants used as operands after > 256 distinct constants
w("def late_constants():")
w("    x = 0")
for i in range(300):
    w("    x = x + %d" % (1000 + i))
w("    y = x * 2 + 123456")
w("    return x, y")
w("")
tot = sum(1000 + i for i in range(300))
w("assert late_constants() == (%d, %d)" % (tot, tot * 2 + 123456))
w("")
# 3. big displays
w("big_list = [")
for i in range(600):
    w("    %d," % i)
w("]")
w("assert len(big_list) == 600 and big_list[599] == 599 and sum(big_list) == %d" % sum(range(600)))
w("")
w("big_dict = {")
for i in range(500):
    w("    'key%d': %d," % (i, i))
w("}")
w("assert len(big_dict) == 500 and big_dict['key499'] == 499 and big_dict['key0'] == 0")
w("")
w("big_tuple = (")
for i in range(300):
    w("    %d," % i)
w(")")
w("assert len(big_tuple) == 300 and big_tuple[299] == 299")
w("")
# 4. long if/elif chain
w("def chain(n):")
w("    if n == 0:")
w("        return 'zero'")
for i in range(1, 300):
    w("    elif n == %d:" % i)
    w("        return 'n%d'" % i)
w("    else:")
w("        return 'other'")
w("")
w("assert chain(0) == 'zero' and chain(1) == 'n1' and chain(299) == 'n299' and chain(300) == 'other'")
w("")
# 5. long jumps
w("def long_body(flag):")
w("    count = 0")
w("    for i in range(3):")
w("        if flag:")
for i in range(600):
    w("            count += 1")
w("        else:")
w("            count += 1000")
w("    return count")
w("")
w("assert long_body(True) == 1800")
w("assert long_body(False) == 3000")
w("")
w("def long_while():")
w("    i = 0")
w("    total = 0")
w("    while i < 10:")
w("        i += 1")
w("        if i % 2 == 0:")
w("            continue")
for k in range(400):
    w("        total += %d" % (k % 7))
w("        if i > 7:")
w("            break")
w("    return total")
w("")
per = sum(k % 7 for k in range(400))
w("assert long_while() == %d" % (per * 5))
w("")
# 6. many parameters / arguments
w("def many_params(" + ", ".join("p%d" % i for i in range(100)) + "):")
w("    return " + " + ".join("p%d" % i for i in range(100)))
w("")
w("assert many_params(" + ", ".join(str(i) for i in range(100)) + ") == %d" % sum(range(100)))
w("assert many_params(*range(100)) == %d" % sum(range(100)))
w("")
w("def count_args(*args):")
w("    return len(args)")
w("")
w("assert count_args(*range(1000)) == 1000")
w("assert count_args(" + ", ".join(str(i) for i in range(200)) + ") == 200")
w("assert sum(range(1000)) == %d" % sum(range(1000)))
w("")
# 7. deep expression nesting
w("deep = " + "(" * 100 + "1" + " + 1)" * 100)
w("assert deep == 101")
w("flat = " + " + ".join(["1"] * 500))
w("assert flat == 500")
w("deep_list = " + "[" * 100 + "]" * 100)
w("depth = 0")
w("node = deep_list")
w("while node:")
w("    node = node[0]")
w("    depth += 1")
w("assert depth == 99")
w("chain_str = " + " + ".join(["'ab'"] * 200))
w("assert len(chain_str) == 400")
w("")
# 8. many upvalues
w("def many_upvalues():")
nu = 200
for i in range(nu):
    w("    u%d = %d" % (i, i))
w("    def inner():")
w("        return " + " + ".join("u%d" % i for i in range(nu)))
w("    def updater():")
w("        nonlocal u0, u%d" % (nu - 1))
w("        u0 += 1000")
w("        u%d += 1000" % (nu - 1))
w("    return inner, updater")
w("")
w("inner, updater = many_upvalues()")
w("assert inner() == %d" % sum(range(nu)))
w("updater()")
w("assert inner() == %d" % (sum(range(nu)) + 2000))
w("")
# 9. nested functions
nd = 40
w("def nest_functions(x):")
for i in range(nd):
    w("    " * (i + 1) + "def f%d(x):" % i)
w("    " * (nd + 1) + "return x")
for i in range(nd - 1, -1, -1):
    w("    " * (i + 1) + "return f%d(x + 1)" % i)
w("")
w("assert nest_functions(0) == 40")
w("")
with open(sys.argv[1], "w") as f:
    f.write("\n".join(out))
