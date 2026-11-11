# Control flow of try/except/else: matching, else, bare raise, nesting, return/break/
# continue in handlers, the exception variable, custom classes (CPython is the oracle).

log = []

def t(label, fn):
    try:
        r = fn()
    except Exception as e:
        log.append((label, "raised", type(e).__name__, str(e)))
        return
    log.append((label, "returned", r))

# which handler runs
def pick(exc):
    try:
        raise exc
    except KeyError:
        return "key"
    except (IndexError, ValueError):
        return "index/value"
    except ArithmeticError as e:
        return "arith " + type(e).__name__
    except Exception:
        return "exception"

for e in (KeyError("k"), IndexError(), ValueError("v"), ZeroDivisionError("z"), OverflowError(), TypeError("t"), RuntimeError()):
    print(pick(e))

# else runs only without exception
def with_else(fail):
    out = []
    try:
        out.append("body")
        if fail:
            raise ValueError("x")
    except ValueError:
        out.append("handler")
    else:
        out.append("else")
    out.append("after")
    return out

print(with_else(False), with_else(True))

# an exception in else is not handled by the handlers of the same try
def else_raises():
    try:
        pass
    except ValueError:
        return "wrong"
    else:
        raise KeyError("from else")

t("else raises", else_raises)

# bare raise re-raises the active exception
def reraise():
    try:
        try:
            raise ValueError("inner")
        except ValueError:
            log.append("cleanup")
            raise
    except ValueError as e:
        return "outer got " + str(e)

t("reraise", reraise)

def raise_other():
    try:
        raise ValueError("first")
    except ValueError:
        raise KeyError("second")

t("raise other", raise_other)

# the handler can see and change names, the variable is deleted after the handler
def names():
    msg = "none"
    try:
        raise ValueError("boom")
    except ValueError as e:
        msg = str(e)
        args = e.args
    try:
        e
    except NameError:
        return msg, args, "e is gone"

print(names())

# return, break and continue in handlers and loops
def ret():
    for i in range(5):
        try:
            if i == 2:
                raise ValueError(i)
        except ValueError:
            return i * 10
    return -1

def loop():
    done = []
    for i in range(6):
        try:
            if i % 2:
                raise KeyError(i)
            done.append(i)
        except KeyError:
            if i == 5:
                break
            continue
        else:
            done.append("e%d" % i)
    return done

print(ret(), loop())

# nested functions and propagation through frames
def a():
    return b()

def b():
    return c()

def c():
    raise IndexError("deep")

def catcher():
    try:
        a()
    except IndexError as e:
        return "caught " + str(e)

print(catcher())

# exception classes
class Base(Exception):
    pass

class Child(Base):
    def __init__(self, code):
        Base.__init__(self, "code %d" % code)
        self.code = code

class Plain(Exception):
    pass

def raises(x):
    raise x

for exc in (Child(3), Base("b"), Plain(), Plain("p", 2), Child):
    try:
        raises(exc if not isinstance(exc, type) else exc(9))
    except Child as e:
        print("Child", e.code, e)
    except Base as e:
        print("Base", e)
    except Exception as e:
        print("Other", type(e).__name__, e.args, str(e), repr(e))

print(isinstance(Child(1), Base), isinstance(Child(1), Exception), isinstance(Plain(), Base), isinstance(KeyError(), LookupError))
print(isinstance(ZeroDivisionError(), ArithmeticError), isinstance(IndexError(), LookupError), isinstance(RecursionError(), RuntimeError))

# exception objects
try:
    raise ValueError("a", 1, [2])
except ValueError as e:
    print(e.args, str(e), repr(e), type(e).__name__, isinstance(e, Exception))
try:
    raise KeyError("k")
except KeyError as e:
    print(e.args, str(e), repr(e))
try:
    {}["x"]
except KeyError as e:
    print(e.args, str(e))
try:
    raise Plain
except Plain as e:
    print(e.args, repr(str(e)), repr(e))

# errors raised by the interpreter itself: only the class, the messages depend on the version
def t(label, fn):
    try:
        r = fn()
    except Exception as e:
        log.append((label, "raised", type(e).__name__))
        return
    log.append((label, "returned", r))

t("zero", lambda: 1 / 0)
t("zero floor", lambda: 1 // 0)
t("zero mod", lambda: 1 % 0)
t("float zero", lambda: 1.0 / 0)
t("index", lambda: [1][5])
t("key", lambda: {}["k"])
t("attr", lambda: (1).foo)
t("type", lambda: 1 + "a")
t("value", lambda: int("z"))
t("name", lambda: undefined)
five = 5
t("call", lambda: five())
t("neg shift", lambda: 1 << -1)
t("range", lambda: range(0, 5, 0))
t("unpack", lambda: [a for a, b in [(1, 2, 3)]])
t("unpack2", lambda: [a for a, b in [(1,)]])
t("str index", lambda: "abc"[3])
t("pop empty", lambda: [].pop())
t("max empty", lambda: max([]))
t("list remove", lambda: [1].remove(2))
t("index method", lambda: [1].index(2))
t("str to int", lambda: "a" * "b")
t("compare", lambda: [1] < 1)
t("hash", lambda: {[1]: 2})
for entry in log:
    print(entry)
