# The Computer Language Benchmarks Game
# http://shootout.alioth.debian.org/
# contributed by Mike Pall; adapted to the Python subset

def bottom_up_tree(item, depth):
    if depth > 0:
        i = item + item
        depth -= 1
        left, right = bottom_up_tree(i - 1, depth), bottom_up_tree(i, depth)
        return (item, left, right)
    return (item, None, None)

def item_check(tree):
    item, left, right = tree
    if left is not None:
        return item + item_check(left) - item_check(right)
    return item

def main(n):
    mindepth = 4
    maxdepth = mindepth + 2
    if maxdepth < n:
        maxdepth = n

    stretchdepth = maxdepth + 1
    stretchtree = bottom_up_tree(0, stretchdepth)
    print("stretch tree of depth %d\t check: %d" % (stretchdepth, item_check(stretchtree)))

    longlivedtree = bottom_up_tree(0, maxdepth)

    for depth in range(mindepth, maxdepth + 1, 2):
        iterations = 2 ** (maxdepth - depth + mindepth)
        check = 0
        for i in range(iterations):
            check += item_check(bottom_up_tree(1, depth)) + item_check(bottom_up_tree(-1, depth))
        print("%d\t trees of depth %d\t check: %d" % (iterations * 2, depth, check))

    print("long lived tree of depth %d\t check: %d" % (maxdepth, item_check(longlivedtree)))

main(6)
