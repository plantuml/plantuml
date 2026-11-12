# Powers with an infinity or a NaN follow C99: (-1) ** inf and 1 ** nan are 1.0.
# VM at 148134d: nan (the result of java.lang.Math.pow), and math.pow(-1, nan)
# raises ValueError.
import math
inf = float('inf')
nan = float('nan')
print((-1) ** inf, (-1) ** -inf, (-1.0) ** inf, 1 ** nan, 1.0 ** nan, 1 ** inf, 1.0 ** -inf)
print(math.pow(1, nan), math.pow(-1, inf), math.pow(-1, -inf), math.pow(1, inf), math.pow(True, nan))
print(math.pow(-1, nan), math.pow(nan, 0), nan ** 0, nan ** 0.0, math.pow(nan, 1))
print(0.5 ** inf, 2 ** inf, 2 ** -inf, 0.5 ** -inf, inf ** 0, (-inf) ** 3, (-inf) ** 2, (-inf) ** -3, 0.0 ** inf)
print(pow(-1, inf), pow(1, nan))
