print(not None)
print(not 0)
print(not 'x')
print(1 and 2)
print(0 and 2)
print(None and 2)
print(1 or 2)
print(0 or 2)
print(0 or '')
print('' or None)
log = []

def t(name):
    log.append(name)
    return True

def f(name):
    log.append(name)
    return False

print(f('a') and t('b'))
print(t('c') or f('d'))
print(log)
