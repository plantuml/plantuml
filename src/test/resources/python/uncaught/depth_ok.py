# max-depth is 50: f(48) is the deepest call that works, with the module that makes 50 frames.
def f(n):
    if n == 0:
        return 0
    return 1 + f(n - 1)

print(f(48))
