# Writes a random program of operator expressions on stdout (seed in argv[1]).
# Run it under CPython and on the interpreter and compare the outputs: they must
# match except where a 32-bit overflow or a complex result happens (see PYTHON.md).
import random, sys
random.seed(int(sys.argv[1]))
vals = ["0","1","-1","2","3","-7","10","255","65536","-2147483648","2147483647","0.0","-0.0","0.5","-2.5","1e10","3.0","1e-5","7","True","False"]
ops = ["+","-","*","/","//","%","**","<<",">>","&","|","^","==","<","<=",">","!=",">="]
header = "def t(f):\n    try:\n        r = f()\n    except Exception as e:\n        r = type(e).__name__\n    if isinstance(r, int) and not isinstance(r, bool) and (r > 2147483647 or r < -2147483648):\n        r = 'BIG'\n    print(repr(r))"
lines = [header]
for _ in range(1500):
    a, b = random.choice(vals), random.choice(vals)
    op = random.choice(ops)
    if op in ("**","<<") and b in ("2147483647","65536","255","1e10","-2147483648"): b = "3"
    if op in ("**",) and a in ("2147483647","-2147483648","65536"): a = "2"
    if random.random() < 0.2:
        c = random.choice(vals); op2 = random.choice(ops)
        e = f"({a}) {op} ({b}) {op2} ({c})"
    elif random.random() < 0.15:
        e = f"{random.choice(['-','~','not '])}({a})"
    elif random.random() < 0.15:
        e = f"{random.choice(['int','float','abs','round','str','repr','bool'])}({a})"
    else:
        e = f"({a}) {op} ({b})"
    lines.append(f"t(lambda: {e})")
print("\n".join(lines))
