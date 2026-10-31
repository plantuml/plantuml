# The Computer Language Benchmarks Game
# http://shootout.alioth.debian.org/
# contributed by Mike Pall

def BottomUpTree(item, depth):
    if depth > 0:
        i = item + item
        depth -= 1
        left, right = BottomUpTree(i - 1, depth), BottomUpTree(i, depth)
        return [item, left, right]
    else:
        return [item]

def ItemCheck(tree):
    if tree[2]:
        return tree[1] + ItemCheck(tree[2]) - ItemCheck(tree[3])
    else:
        return tree[1]

def main():
    N = tonumber(arg and arg[1]) or 0
    mindepth = 4
    maxdepth = mindepth + 2
    if maxdepth < N: maxdepth = N

    # (a `do ... end` block in the original, which has no equivalent here)
    stretchdepth = maxdepth + 1
    stretchtree = BottomUpTree(0, stretchdepth)
    io.write(string.format("stretch tree of depth %d\t check: %d\n",
        stretchdepth, ItemCheck(stretchtree)))

    longlivedtree = BottomUpTree(0, maxdepth)

    for depth in range(mindepth, maxdepth + 1, 2):
        iterations = 2 ** (maxdepth - depth + mindepth)
        check = 0
        for i in range(1, iterations + 1):
            check = (check + ItemCheck(BottomUpTree(1, depth)) +
                     ItemCheck(BottomUpTree(-1, depth)))
        io.write(string.format("%d\t trees of depth %d\t check: %d\n",
            iterations * 2, depth, check))

    io.write(string.format("long lived tree of depth %d\t check: %d\n",
        maxdepth, ItemCheck(longlivedtree)))

main()
