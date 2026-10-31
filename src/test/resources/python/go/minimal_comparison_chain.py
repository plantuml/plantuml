x = 5
print(1 < x < 10)
print(1 < x < 3)
print(5 == x == 5)
print(5 == x != 6)
print(0 <= x < 5)
print(0 <= x <= 5)

count = 0
def bump():
    global_count = 1
    return 9

print(1 < 0 < bump())
