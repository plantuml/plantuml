# A bool is formatted as the int it is when the specification is not empty.
# VM at 148134d: TypeError.
print('{:5}|'.format(True))
print(f'{True:10}|')
print('{:<5}|'.format(True), '{:+}'.format(True), '{:,}'.format(True), '{:05}'.format(True), '{:_}'.format(False))
print('{:d}'.format(True), '{}'.format(True), '{!s:>6}'.format(True), '{:x}'.format(True), '{:.1f}'.format(True))
print(format(True, '3'), format(False, '<3') + '|', format(True), format(True, ''))
