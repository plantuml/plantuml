def risky(n):
    if n > 1:
        return risky(n - 1)
    raise KeyError("deep")

def guard():
    try:
        risky(3)
    except KeyError:
        print("guard cleanup")
        raise

def caller():
    guard()

try:
    caller()
except KeyError:
    print("outer cleanup")
    raise
