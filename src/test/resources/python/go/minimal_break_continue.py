i = 0
out = []
while i < 6:
    i = i + 1
    if i == 2:
        continue
    if i == 5:
        break
    out.append(i)
print(out)

out = []
for x in [1, 2, 3, 4, 5]:
    if x == 2:
        continue
    if x == 4:
        break
    out.append(x)
print(out)

out = []
for x in [1, 2, 3]:
    for y in [1, 2, 3]:
        if y == 2:
            break
        out.append(x * 10 + y)
print(out)
