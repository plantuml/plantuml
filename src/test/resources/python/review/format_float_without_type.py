# A float formatted with a precision and no type uses the general format with
# at least one digit after the point; the alternate form (#) of g keeps the point.
# VM at 148134d: '-532.0', '2.0', '159583' and '769'.
print('{:.3}'.format(-531.825), '{:.5}'.format(-33834.5), '{:.3}'.format(1234.5678))
print('{:.0}'.format(1.5), '{:.1}'.format(1.5), '{:.1}'.format(15.0), '{:.2}'.format(150.0))
print('{:.3}'.format(1.23456), '{:.3}'.format(123456.0), '{:.3}'.format(0.00001234), '{:.3}'.format(1e20), '{:.3}'.format(100.0))
print('{:#g}'.format(159583.0), '{:#.3g}'.format(769.0), '%#g' % 504309.0, '{:#.3g}'.format(-512.0))
print('{:#g}'.format(1.0), '{:#.3g}'.format(1.0), '%#.0e' % 12345, '{:#.0f}'.format(1.0), '%#.0f' % 2.5)
print(f'{-531.825:.3}', format(-531.825, '.3'))
