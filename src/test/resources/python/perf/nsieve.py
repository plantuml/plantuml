# The Computer Language Shootout
# http://shootout.alioth.debian.org/
# contributed by Isaac Gouy
# modified by Mike Pall


def nsieve(m, isPrime):
    for i in range(2, m + 1):
        isPrime[i] = True
    count = 0

    for i in range(2, m + 1):
        if isPrime[i]:
            for k in range(i + i, m + 1, i):
                if isPrime[k]: isPrime[k] = False
            count += 1
    return count

def main():
    n = tonumber(arg and arg[1]) or 1
    flags = {}

    m = (2 ** n) * 10000
    print(string.format("Primes up to %8d %8d", m, nsieve(m, flags)))

    m = (2 ** (n - 1)) * 10000
    print(string.format("Primes up to %8d %8d", m, nsieve(m, flags)))

    m = (2 ** (n - 2)) * 10000
    print(string.format("Primes up to %8d %8d", m, nsieve(m, flags)))

main()
