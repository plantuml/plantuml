# A bare raise re-raises the exception being handled, also when it is written in
# a function called from the handler.
# VM at 148134d: RuntimeError "No active exception to reraise".
def again():
    raise

def k():
    try:
        raise ValueError('a')
    except ValueError:
        again()

try:
    k()
except Exception as x:
    print(type(x).__name__, x)

def nested():
    try:
        raise KeyError('k')
    except KeyError:
        def inner():
            raise
        inner()

try:
    nested()
except Exception as x:
    print(type(x).__name__, x)

try:
    again()
except RuntimeError as x:
    print('RuntimeError outside a handler')
