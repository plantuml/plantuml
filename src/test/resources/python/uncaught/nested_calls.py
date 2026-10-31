def inner(d, k):
    return d[k]

def middle(d):
    total = 0
    for k in ("a", "b", "c"):
        total += inner(d, k)
    return total

def outer():
    return middle({"a": 1, "b": 2})

print("start")
print(outer())
