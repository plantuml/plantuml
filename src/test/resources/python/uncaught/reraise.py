def risky():
    raise KeyError("k")

try:
    risky()
except KeyError:
    print("cleanup")
    raise
