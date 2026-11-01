# Many closures sharing the same upvalues (a memo table and the list of functions).
# The Lua original generated 200 functions as source text and loaded it. Here the
# same closures are created in a loop by a factory: each function captures its own
# index i, the shared memo table and the shared table of functions.

def main():
    cache = {}
    fs = {}

    def make(i):
        def f():
            if i not in cache:
                cache[i] = fs[i - 2]() + fs[i - 1]()
            return cache[i]
        return f

    def f1():
        return 1

    def f2():
        return 1

    fs[1] = f1
    fs[2] = f2
    for i in range(3, 47):
        fs[i] = make(i)

    print("5th fibonacci number is", fs[5]())
    print("10th fibonacci number is", fs[10]())
    print("46th fibonacci number is", fs[46]())

main()
