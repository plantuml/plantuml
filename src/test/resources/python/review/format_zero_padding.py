# Zero padding with a thousands separator puts the separators in the padding,
# and the 0 flag fills a str on the right (the default alignment of a str).
# VM at 148134d: '000001,234' and '0000a'.
print('{:010,}'.format(1234))
print('{:011,}'.format(1234567), '{:06,}'.format(1000), '{:07,}'.format(1000), '{:08,.1f}'.format(1234.5))
print('{:010_}'.format(1234), '{:08,.2f}'.format(-1000), '{:09,}'.format(-1234), '{:+010,}'.format(1234))
print('{:05}'.format('a'), '{:05}'.format('ab'), '{:<05}'.format('a'), '{:>05}'.format('a'), '{:^05}'.format('a'))
print(f'{1234:010,}', format(1234, '010,'), f'{"a":05}')
