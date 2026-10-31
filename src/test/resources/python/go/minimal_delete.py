items = [0, 1, 2, 3, 4]
del items[1:4:2]
print(items)
d = {'a': 1, 'b': 2}
del d['a']
print(len(d))
class Box:
    value = 7
del Box.value
print(hasattr(Box, 'value'))
name = 5
del name
