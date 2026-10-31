i = 0
while i < 3:
    i += 1
else:
    print('while normal')

while True:
    break
else:
    print('while broken')

for x in [1, 2]:
    pass
else:
    print('for normal')

for x in [1, 2]:
    if x == 1:
        break
else:
    print('for broken')

for x in []:
    print('never')
else:
    print('for empty')
