# The name of "except ... as name" is unbound at the end of the handler; it is
# not an error if the handler has deleted it already, or if an inner handler
# with the same name has.
# VM at 148134d: UnboundLocalError (in a function) or NameError (at the top
# level) at the end of the handler.
def f():
    try:
        raise ValueError('a')
    except ValueError as e:
        del e
    return 1
print(f())

def g():
    try:
        raise ValueError('a')
    except ValueError as x:
        try:
            raise KeyError('k')
        except KeyError as x:
            pass
    return 2
print(g())

try:
    raise ValueError('a')
except ValueError as e:
    del e
print(3)

x = 'x'
try:
    raise ValueError
except ValueError as x:
    try:
        raise KeyError
    except KeyError as x:
        pass
    try:
        print(x)
    except NameError:
        print('inner unbound')
print(4)
