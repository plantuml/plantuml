# max-depth is 50: one level more than depth_ok.py.
def f(n):
    if n == 0:
        return 0
    return 1 + f(n - 1)

print(f(49))
