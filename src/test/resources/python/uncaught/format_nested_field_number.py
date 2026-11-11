# DEVIATION: in str.format a {} inside a format spec is numbered before the field that
# holds it: here the spec is 'b' (argument 0) and the value is 4. CPython numbers the
# outer field first and prints '   b'.
print("{:>{}}".format("b", 4))
