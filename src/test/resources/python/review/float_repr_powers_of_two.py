# repr(float) must be the shortest text that reads back as the same value.
# Around a power of two the interval of the texts that read back is not
# symmetric, so the nearest 16-digit rounding can fail where the next one
# works: repr(2.0 ** -24) is '5.960464477539063e-08'.
# VM at 148134d: '5.9604644775390625e-08' (17 digits), for 46 of these values.
for k in range(-1022, 1024):
    print(k, repr(2.0 ** k))
print(repr(64 / 1073741824), repr(1 / 16777216), str(2.0 ** -24), '%r' % 2.0 ** -24, f'{2.0 ** -24}')
