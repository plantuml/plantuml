# The Computer Language Benchmarks Game
# http://shootout.alioth.debian.org/
# contributed by Mike Pall

def fannkuch(n):
    p, q, s, odd, check, maxflips = [], [], [], True, 0, 0
    for i in range(1, n + 1):
        p[i] = i
        q[i] = i
        s[i] = i
    while True:
        # Print max. 30 permutations.
        if check < 30:
            if not p[n]: return maxflips  # Catch n = 0, 1, 2.
            io.write(table.unpack(p))
            io.write("\n")
            check += 1
        # Copy and flip.
        q1 = p[1]  # Cache 1st element.
        if p[n] != n and q1 != 1:  # Avoid useless work.
            for i in range(2, n + 1): q[i] = p[i]  # Work on a copy.
            for flips in range(1, 1000001):  # Flip ...
                qq = q[q1]
                if qq == 1:  # ... until 1st element is 1.
                    if flips > maxflips: maxflips = flips  # New maximum?
                    break
                q[q1] = q1
                if q1 >= 4:
                    i, j = 2, q1 - 1
                    while True:
                        q[i], q[j] = q[j], q[i]
                        i += 1
                        j -= 1
                        if i >= j: break
                q1 = qq
        # Permute.
        if odd:
            p[2], p[1] = p[1], p[2]  # Rotate 1<-2.
            odd = False
        else:
            p[2], p[3] = p[3], p[2]  # Rotate 1<-2 and 1<-2<-3.
            odd = True
            for i in range(3, n + 1):
                sx = s[i]
                if sx != 1:
                    s[i] = sx - 1
                    break
                if i == n: return maxflips  # Out of permutations.
                s[i] = i
                # Rotate 1<-...<-i+1.
                t = p[1]
                for j in range(1, i + 1): p[j] = p[j + 1]
                p[i + 1] = t

def main():
    n = tonumber(arg and arg[1]) or 1
    io.write("Pfannkuchen(", n, ") = ", fannkuch(n), "\n")

main()
