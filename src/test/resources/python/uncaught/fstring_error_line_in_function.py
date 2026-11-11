def show(items, i):
    return "item: " + f"{items[i]!r:>8}"

def main():
    print(show([1, 2, 3], 1))
    print(show([1, 2, 3], 7))

main()
