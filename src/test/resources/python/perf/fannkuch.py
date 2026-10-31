# The Computer Language Benchmarks Game
# http://shootout.alioth.debian.org/
# adapted to the Python subset

def fannkuch(n):
    perm1 = list(range(n))
    count = [0] * n
    max_flips = 0
    checksum = 0
    nperm = 0
    r = n
    while True:
        while r != 1:
            count[r - 1] = r
            r -= 1
        perm = perm1[:]
        flips = 0
        k = perm[0]
        while k != 0:
            i, j = 0, k
            while i < j:
                perm[i], perm[j] = perm[j], perm[i]
                i += 1
                j -= 1
            flips += 1
            k = perm[0]
        if flips > max_flips:
            max_flips = flips
        if nperm % 2 == 0:
            checksum += flips
        else:
            checksum -= flips
        while True:
            if r == n:
                return checksum, max_flips
            first = perm1[0]
            for i in range(r):
                perm1[i] = perm1[i + 1]
            perm1[r] = first
            count[r] -= 1
            if count[r] > 0:
                break
            r += 1
        nperm += 1

checksum, maxflips = fannkuch(7)
print("%d\nPfannkuchen(%d) = %d" % (checksum, 7, maxflips))
assert (checksum, maxflips) == (228, 16)
