def f():
    try:
        try:
            raise KeyError("inner")
        except KeyError:
            print("first handler")
            raise
    except KeyError:
        print("second handler")
        raise

f()
