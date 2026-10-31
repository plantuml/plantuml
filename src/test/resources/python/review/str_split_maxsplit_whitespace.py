# split / rsplit without a separator and with a maxsplit: what is left after
# the last split keeps its inner and outer blanks on the far side.
# VM at 148134d: the remainder is stripped on both sides, ['a', 'b'] everywhere.
print('  a  b  '.split(None, 1))
print('  a  b  '.rsplit(None, 1))
print('  a  b  '.split(None, 0))
print('  a  b  '.rsplit(None, 0))
print('  a  b  c  '.split(None, 1))
print('  a  b  c  '.split(None, 2))
print('  a  b  c  '.rsplit(None, 1))
print('  a  b  c  '.rsplit(None, 2))
print('a b c'.split(None, 1), 'a b c'.rsplit(None, 1))
print(' a\tb \n c '.split(maxsplit=1), ' a\tb \n c '.rsplit(maxsplit=1))
print('  a  b  '.split(None, 5), '  a  b  '.rsplit(None, 5), '  a  b  '.split(None, -1))
print('   '.split(None, 1), ''.split(None, 1), 'a'.split(None, 0), ' a '.split(None, 0), ' a '.rsplit(None, 0))
