def label(x):
    if x < 3:
        return 'small'
    else:
        return 'big'

print(label(2))
print(label(5))
if label(2) == 'small':
    print('eq')
