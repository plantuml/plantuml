# NOT run under CPython: the depth of the calls is limited by max-depth (recursion_limit.err).
def f(n):
    return f(n + 1)

print("start")
f(0)
