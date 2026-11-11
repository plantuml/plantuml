def risky():
    raise ValueError("v")

def wrapper():
    try:
        risky()
    except ValueError as e:
        print("wrapper", e)
        raise e

wrapper()
