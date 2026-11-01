# Comparisons between values of different types and chained comparisons.
assert 1 == 1.0
assert 1 < 1.5 < 2
assert not (1 < 2 < 2)
assert 1 <= 1 <= 1.0
assert "abc" < "abd"
assert "abc" < "abcd"
assert "" < "a"
assert "Z" < "a"
assert (1, 2) < (1, 3)
assert (1, 2) < (1, 2, 0)
assert [1, 2] < [1, 3]
assert [] < [0]
assert [1, [2, 3]] == [1, [2, 3]]
assert {"a": 1, "b": 2} == {"b": 2, "a": 1}
assert 1 != "1"
assert None != 0
assert None == None
assert True == 1
assert False == 0
assert not (1 == "1")

def raises_type_error(f):
    try:
        f()
    except TypeError:
        return True
    return False

assert raises_type_error(lambda: 1 < "2")
assert raises_type_error(lambda: [1] < (1,))
assert raises_type_error(lambda: None < 1)

# the middle operand of a chain is evaluated once
calls = []

def mid():
    calls.append(1)
    return 5

assert 1 < mid() < 10
assert len(calls) == 1
# a chain stops at the first false comparison
assert not (10 < mid() < 1 / 0)
