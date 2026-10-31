# The views of a dict: keys() and items() compare by content, values() by
# identity; reversed() goes through a view backwards; popitem.
# VM at 148134d: d.keys() == d.keys() is False, reversed(d.keys()) is a
# TypeError and popitem is missing.
d = {'a': 1, 'b': 2}
print(d.keys() == d.keys(), d.items() == d.items(), d.values() == d.values())
print(d.keys() == {'b': 0, 'a': 0}.keys(), d.keys() != {'a': 0}.keys(), d.items() == {'b': 2, 'a': 1}.items(), d.items() != {'a': 1, 'b': 3}.items())
print(d.keys() == ['a', 'b'], d.items() == [('a', 1), ('b', 2)])
print(list(reversed(d)), list(reversed(d.keys())), list(reversed(d.values())), list(reversed(d.items())))
print(d.popitem(), d.popitem(), d)
try:
    d.popitem()
except KeyError:
    print('KeyError')
