# A backward jump or a call is a step: the number of steps of this script is the
# max-steps of step_boundary_ok.err, and one less is not enough (step_boundary_fail).
def add(a, b):
    return a + b

total = 0
for i in range(10):
    total = add(total, i)
i = 0
while i < 5:
    i += 1
print(total, i)
