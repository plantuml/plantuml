t = {}
# The generated chunk is written in the same syntax. Its functions are locals of
# main(), as the originals are locals of the chunk. The `result` cell of each
# function was a block-local (`do ... end`) in the original: here it is a slot of
# one shared table, because there are no block scopes.
template = """
    def f<i>():
        if not cache[<i>]:
            cache[<i>] = f<i-2>() + f<i-1>()
        return cache[<i>]
"""
t[1] = """def main():
    cache = {}
    def f1(): return 1
"""
t[2] = """    def f2(): return 1
"""
for i in range(3, 200):
    def expand(s):
        c = assert(load("return " .. s, "f" .. i, "bt", {"i": i}), "could not compile: " .. s)
        return c()
    t[i] = template:gsub("<([^>]+)>", expand)
t[200] = """    print("5th fibonacci number is", f5())
    print("10th fibonacci number is", f10())
    print("199th fibonacci number is", f199())

main()
"""

s = table.concat(t)
print(s)
f = load(s)
f()
