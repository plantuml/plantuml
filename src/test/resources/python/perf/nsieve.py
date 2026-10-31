# The Computer Language Shootout
# http://shootout.alioth.debian.org/
# contributed by Isaac Gouy; modified by Mike Pall; adapted to the Python subset

def nsieve(m):
    is_prime = [True] * (m + 1)
    count = 0
    for i in range(2, m + 1):
        if is_prime[i]:
            for k in range(i + i, m + 1, i):
                is_prime[k] = False
            count += 1
    return count

def main(n):
    m = (2 ** n) * 10000
    print("Primes up to %8d %8d" % (m, nsieve(m)))
    m = (2 ** (n - 1)) * 10000
    print("Primes up to %8d %8d" % (m, nsieve(m)))
    m = (2 ** (n - 2)) * 10000
    print("Primes up to %8d %8d" % (m, nsieve(m)))

main(2)
