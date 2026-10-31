def risky():
    raise KeyError("k")

def only_values():
    try:
        risky()
    except ValueError:
        print("not reached")

def twice():
    try:
        only_values()
    except IndexError:
        print("not reached")

twice()
