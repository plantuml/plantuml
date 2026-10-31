# What the formatting refuses.
# VM at 148134d: format(1, 2) lets a Java ClassCastException out, the other
# lines print a value instead of raising.
def t(f):
    try:
        return repr(f())
    except Exception as e:
        return type(e).__name__

print(t(lambda: format(1, 2)), t(lambda: format(None, None)), t(lambda: format('a', 1.5)), t(lambda: format(1, [])))
print(t(lambda: '%x' % 1.5), t(lambda: '%o' % 1.5), t(lambda: '%#x' % 1.5), t(lambda: '%X' % 2.0))
print(t(lambda: '{:,x}'.format(1)), t(lambda: '{:,b}'.format(255)), t(lambda: '{:.2d}'.format(1)), t(lambda: '{:10.2}'.format(5)))
print(t(lambda: '{:+}'.format('a')), t(lambda: '{:=5}'.format('a')), t(lambda: '{:,}'.format('a')), t(lambda: '{:#}'.format('a')))
print(t(lambda: '{:#c}'.format(65)), t(lambda: '{:+c}'.format(65)), t(lambda: '{:.1c}'.format(65)), t(lambda: '{:,c}'.format(65)))
print(t(lambda: '{:3c}|'.format(65)), t(lambda: '{:<3c}|'.format(65)), t(lambda: '{:_n}'.format(1)))
print(t(lambda: '%5%|' % ()), t(lambda: 'abc' % {}), t(lambda: 'abc' % []), t(lambda: 'abc' % {'a': 1}))
print(t(lambda: int('010', 0)), t(lambda: int('0', 0)), t(lambda: int('0_0', 0)), t(lambda: int('0b_1', 0)), t(lambda: int('12', base=8)))
