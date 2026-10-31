# The conversions %a and !a give the repr with the non-ASCII characters escaped.
# VM at 148134d: the repr is quoted a second time, "'\xe9'" becomes "\"'\\xe9'\"".
print('%a' % 'é')
print('{!a}'.format('é'))
print(f'{"é"!a}')
print('%a' % 'abc', '{!a}'.format('abc'), f'{"abc"!a}')
print('%a' % 1, '{!a}'.format(1.5), f'{[1, "é"]!a}', '%a' % None)
print('%a' % '\U0001F600', '{!a}'.format('\u20ac'), '%a|%5a|%-6a|' % ('a', 'b', 'c'))
print('{!a:>8}'.format('é'), f'{"a"!a:*^7}')
