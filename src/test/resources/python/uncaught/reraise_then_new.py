def load():
    raise ValueError("disk")

def run():
    try:
        load()
    except ValueError:
        try:
            raise
        except ValueError:
            print("inner")
        raise RuntimeError("replaced")

run()
