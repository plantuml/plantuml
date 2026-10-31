# A str is a sequence of code points: two lone surrogates side by side stay two
# characters, and a lone surrogate is not found inside an astral character.
# VM at 148134d: the two halves merge into one character (len 1), and the
# searches match half a character. (To be decided with the Go port, whose
# strings cannot hold a lone surrogate: refusing chr(0xD800) is the alternative.)
hi = chr(0xD83D)
lo = chr(0xDE00)
face = chr(0x1F600)
both = hi + lo
print(len(hi), len(lo), len(face), len(both), both == face, ord(both[0]), ord(both[1]))
print(len(hi.join(['', lo])), len('%s%s' % (hi, lo)), len('{}{}'.format(hi, lo)), len(f'{hi}{lo}'), len(''.join([hi, lo])))
print(len(list(both)), len(tuple(both)), len(sorted(both)), len(both[::-1]), len(both.upper()), len(both * 2))
print(hi in face, lo in face, face.find(lo), face.count(hi), face.startswith(hi), face.endswith(lo))
print(face.replace(hi, 'x') == face, face.split(lo) == [face], face.partition(lo)[1] == '', face.strip(hi) == face)
print({face: 1}.get(both), hi < lo, lo < face, chr(0xFFFF) < face, chr(0xE000) > hi)
print(sorted([hi, face, chr(0xFFFF)]) == [hi, chr(0xFFFF), face], len(hi.ljust(2, lo)), len(lo.center(3, hi)))
