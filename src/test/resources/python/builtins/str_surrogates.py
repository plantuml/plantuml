# Lone surrogates are characters: len, ord, subscripts, order, searches, json,
# formatting and the methods of str work on code points.
import json
hi = chr(0xD83D)
lo = chr(0xDE00)
face = chr(0x1F600)
both = hi + lo
print(len(hi), len(lo), len(face), len(both), both == face, ord(both[0]), ord(both[1]))
print(repr(hi), repr(lo), repr(both), repr(both) == repr(face))
print(len('\ud83d'), len('😀'), '😀' == face, '\ud83d' == hi, len('\U0001f600'), ord('\udfff'), ord('\ud800'))
print([ord(c) for c in both + face + 'a' + lo + hi])
print([ord(c) for c in (face + hi + 'é' + lo)[::-1]], [ord(c) for c in (face + hi + lo)[1:]], ord((lo + face)[-1]))
print(sorted([lo, hi, face, 'a', chr(0xFFFF), chr(0xE000), chr(0xD7FF), both]) == [ 'a', chr(0xD7FF), hi, both, lo, chr(0xE000), chr(0xFFFF), face])
print(hi < both, both < lo, both < face, face > lo, hi + 'a' < hi + lo, max(hi, face) == face, min(lo, chr(0xE000)) == lo)
print(both.find(lo), both.index(hi), both.rfind(lo), (face + both).find(lo), (face + both).find(hi), both.count(hi), (both * 3).count(both))
print(both.replace(hi, 'x') == 'x' + lo, both.split(lo) == [hi, ''], both.partition(lo) == (hi, lo, ''), (hi + 'a' + hi).strip(hi), both.strip(lo) == hi, both.lstrip(hi) == lo)
print(json.dumps(hi), json.dumps(both), json.dumps(face), json.dumps([lo, 'a' + hi]))
print(len(json.loads('"\\ud83d"')), len(json.loads('"\\ud83d\\ude00"')), json.loads('"\\ud83d\\ude00"') == face, len(json.loads('"\\ude00\\ud83d"')), json.loads(json.dumps(both)) == face, len(json.loads('"\\ud83dx"')))
print(len(json.loads('"' + hi + '\\ude00"')), len(json.loads('"\\ud83d' + lo + '"')), json.loads('"' + both + '"') == both)
print('%c' % 0xD83D == hi, '{:c}'.format(0xDE00) == lo, len('%c%c' % (0xD83D, 0xDE00)), '%s' % hi == hi, '%5s|' % hi == '    ' + hi + '|', len('{:>3}'.format(both)))
print(len('{:\ud83d^5}'.format(lo)), '{:.1}'.format(both) == hi, '%.1s' % both == hi, len(both.center(5, lo)), len(both.zfill(4)), both.rjust(3, 'a') == 'a' + both)
print(both.upper() == both, both.lower() == both, both.title() == both, both.capitalize() == both, both.swapcase() == both, hi.isalpha(), hi.isdigit(), both.isspace(), hi.isupper(), hi.islower())
print({hi: 1, lo: 2, both: 3, face: 4}[both], hi in {hi: 1}, face in {both: 1}, len({both: 1, face: 2, hi + lo: 3}))
print(list(both) == [hi, lo], tuple(face + both) == (face, hi, lo), [len(c) for c in both], ''.join(reversed(both)) == lo + hi)
print(both.startswith(hi), both.endswith(lo), face.startswith(hi), both.startswith(lo), both.removeprefix(hi) == lo, both.removesuffix(lo) == hi)
print(both.replace('', '-') == '-' + hi + '-' + lo + '-', len(both.replace('', '-')), both.count(''), both.splitlines() == [both], both.split() == [both], (hi + ' ' + lo).split() == [hi, lo], (hi + ' ' + lo).rsplit(None, 1) == [hi, lo])
print(both * 2 == hi + lo + hi + lo, len(both * 2), (lo + hi) == chr(0xDE00) + chr(0xD83D), len(lo + hi), both[0] == hi, both[-1] == lo, both[0:1] == hi, both[1:] == lo, both[::2] == hi)
print(str(both) == both, len(repr(both)), repr([hi]), repr({lo: face}) == "{'\\ude00': '" + face + "'}", repr(hi + "'"), repr(chr(0xDBFF) + chr(0xDC00)))
for x in [0xD800, 0xD801, 0xD83F, 0xD840, 0xD900, 0xD93F, 0xDBFF, 0xDC00, 0xDFFF]:
    c = chr(x)
    assert len(c) == 1 and ord(c) == x and len(c + c) == 2 and (c + c)[1] == c and repr(c) == "'\\u%04x'" % x, x
    for y in [0xD800, 0xD81F, 0xD900, 0xD93F, 0xDC00, 0xDFFF, 0x41, 0x1F600]:
        d = chr(y)
        s = c + d + c
        assert len(s) == 3 and [ord(k) for k in s] == [x, y, x] and s[1] == d and s.find(d) == (0 if x == y else 1) and s[::-1] == s, (x, y)
        assert (c < d) == (x < y) and (c + 'a' < d + 'a') == (x < y) and (d + c > d + d) == (x > y), (x, y)
print('ok')
