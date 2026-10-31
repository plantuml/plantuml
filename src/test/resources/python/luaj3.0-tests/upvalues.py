print("-------- simple upvalues tests --------")
def simpleupvalues():
    def test():
        x = 5
        def f():
            x += 1
            return x
        def g():
            x -= 1
            return x
        print(f())
        print(g())
        return f, g

    f1, g1 = test()
    print("f1()=", f1())
    print("g1()=", g1())

    f2, g2 = test()
    print("f2()=", f2())
    print("g2()=", g2())

    print("g1()=", g1())
    print("f1()=", f1())

print("simplevalues result:", pcall(simpleupvalues))


# The point of this test is that when an upvalue is created, it may
# need to be inserted in the middle of the list, rather than always
# appended at the end. Otherwise, it may not be found when it is
# needed by another closure.
print("----------- upvalued in middle ------------")
def middleupvaluestest():
    def test():
        x = 3
        y = 5
        z = 7

        def f():
            print("y=", y)

        def g():
            print("z=", z)

        def h():
            print("x=", x)

        def setter(x1, y1, z1):
            x = x1
            y = y1
            z = z1

        return f, g, h, setter

    f, g, h, setter = test()

    h()
    f()
    g()

    setter("x", "y", "z")

    h()
    f()
    g()

print(pcall(middleupvaluestest))


print("--------- nested upvalues ----------")
def nestedupvaluestest():
    f = None
    if True:
        x = 10
        def g():
            print(x, f())
    def f():
        return 20
    g()

print("nestedupvaluestest result:", pcall(nestedupvaluestest))
