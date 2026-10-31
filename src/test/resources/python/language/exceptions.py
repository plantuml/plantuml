# try / except / else, raise, the builtin hierarchy and the errors raised by
# the builtin operations.  There is no finally and no user-defined exception.

# basic
def safe_div(a, b):
    try:
        return a / b
    except ZeroDivisionError:
        return "div0"

assert safe_div(1, 2) == 0.5
assert safe_div(1, 0) == "div0"

# the exception object
try:
    raise ValueError("bad value", 42)
except ValueError as e:
    assert e.args == ("bad value", 42)
    assert str(e) == "('bad value', 42)"

try:
    raise ValueError("only one")
except ValueError as e:
    assert e.args == ("only one",)
    assert str(e) == "only one"

try:
    raise ValueError
except ValueError as e:
    assert e.args == ()
    assert str(e) == ""

# else runs when nothing was raised
def with_else(f):
    log = []
    try:
        f()
    except ValueError:
        log.append("except")
    else:
        log.append("else")
    log.append("after")
    return log

def ok(): pass
def bad(): raise ValueError("x")

assert with_else(ok) == ["else", "after"]
assert with_else(bad) == ["except", "after"]

# an exception in the else block is not caught by the same try
def else_raises():
    try:
        pass
    except ValueError:
        return "caught"
    else:
        raise ValueError("from else")

try:
    else_raises()
    assert False
except ValueError as e:
    assert e.args == ("from else",)

# hierarchy
for cls in (ValueError, TypeError, KeyError, IndexError, ZeroDivisionError,
            OverflowError, AssertionError, RuntimeError, NameError,
            AttributeError, RecursionError):
    try:
        raise cls("m")
    except Exception as e:
        assert isinstance(e, Exception)
        assert isinstance(e, BaseException)

try:
    raise KeyError("k")
except LookupError:
    pass
try:
    raise IndexError("i")
except LookupError:
    pass
try:
    raise ZeroDivisionError("z")
except ArithmeticError:
    pass
try:
    raise OverflowError("o")
except ArithmeticError:
    pass
try:
    raise RecursionError("r")
except RuntimeError:
    pass

# a handler that does not match lets the exception through
def pass_through():
    try:
        try:
            raise KeyError("k")
        except ValueError:
            return "wrong handler"
    except KeyError:
        return "outer handler"

assert pass_through() == "outer handler"

# tuples of classes, bare except, the first matching handler wins
def which(f):
    try:
        f()
    except (KeyError, IndexError):
        return "lookup"
    except ZeroDivisionError:
        return "zero"
    except Exception:
        return "other"
    except:
        return "bare"
    return "none"

def r_key(): raise KeyError("k")
def r_index(): raise IndexError("i")
def r_zero(): raise ZeroDivisionError("z")
def r_value(): raise ValueError("v")

assert which(r_key) == "lookup"
assert which(r_index) == "lookup"
assert which(r_zero) == "zero"
assert which(r_value) == "other"
assert which(ok) == "none"

def bare():
    try:
        raise TypeError("t")
    except:
        return "caught"

assert bare() == "caught"

# re-raise
def reraise():
    try:
        raise ValueError("inner")
    except ValueError:
        raise

try:
    reraise()
    assert False
except ValueError as e:
    assert e.args == ("inner",)

# raise another exception from a handler
def convert():
    try:
        {}["missing"]
    except KeyError as e:
        raise ValueError("converted")

try:
    convert()
    assert False
except ValueError as e:
    assert e.args == ("converted",)

# nested functions: the exception crosses the frames
def level3(): raise ValueError("deep")
def level2(): return level3()
def level1(): return level2()

try:
    level1()
    assert False
except ValueError as e:
    assert e.args == ("deep",)

# return / break / continue inside try
def ret():
    try:
        return "from try"
    except ValueError:
        return "from except"

assert ret() == "from try"

def loop():
    out = []
    for i in range(6):
        try:
            if i % 2:
                raise ValueError(i)
            if i == 4:
                break
            out.append(i)
        except ValueError:
            continue
    return out

assert loop() == [0, 2]

# an exception in a loop does not stop the next iterations
results = []
for d in (1, 0, 2):
    try:
        results.append(10 // d)
    except ZeroDivisionError:
        results.append(None)
assert results == [10, None, 5]

# nested handlers
def nested():
    log = []
    try:
        try:
            raise ValueError("a")
        except ValueError:
            log.append("inner")
            raise TypeError("b")
    except TypeError:
        log.append("outer")
    return log

assert nested() == ["inner", "outer"]

# exceptions raised by the language
def exc_class(f):
    try:
        f()
    except ZeroDivisionError:
        return "ZeroDivisionError"
    except IndexError:
        return "IndexError"
    except KeyError:
        return "KeyError"
    except ValueError:
        return "ValueError"
    except TypeError:
        return "TypeError"
    except AttributeError:
        return "AttributeError"
    except NameError:
        return "NameError"
    except AssertionError:
        return "AssertionError"
    except RuntimeError:
        return "RuntimeError"
    return "none"

lst = [1, 2, 3]
d = {"a": 1}
assert exc_class(lambda: 1 / 0) == "ZeroDivisionError"
assert exc_class(lambda: lst[3]) == "IndexError"
assert exc_class(lambda: lst[-4]) == "IndexError"
assert exc_class(lambda: d["b"]) == "KeyError"
assert exc_class(lambda: lst.index(9)) == "ValueError"
assert exc_class(lambda: lst.remove(9)) == "ValueError"
assert exc_class(lambda: [].pop()) == "IndexError"
assert exc_class(lambda: {}.pop("k")) == "KeyError"
assert exc_class(lambda: int("x")) == "ValueError"
assert exc_class(lambda: float("x")) == "ValueError"
assert exc_class(lambda: chr(-1)) == "ValueError"
assert exc_class(lambda: "a" + 1) == "TypeError"
assert exc_class(lambda: len(5)) == "TypeError"
assert exc_class(lambda: lst.foo) == "AttributeError"
assert exc_class(lambda: undefined_name) == "NameError"
assert exc_class(lambda: (lambda: 1)(2)) == "TypeError"
assert exc_class(lambda: "abc".index("z")) == "ValueError"
assert exc_class(lambda: "a".join([1])) == "TypeError"
assert exc_class(lambda: int(None)) == "TypeError"
assert exc_class(lambda: ok()) == "none"

def failing_assert():
    assert 1 == 2, "nope"

assert exc_class(failing_assert) == "AssertionError"
try:
    failing_assert()
except AssertionError as e:
    assert e.args == ("nope",)

def raise_str():
    raise "not an exception"

assert exc_class(raise_str) == "TypeError"

# KeyError carries the key
try:
    {"a": 1}["zz"]
except KeyError as e:
    assert e.args == ("zz",)
try:
    {}[(1, 2)]
except KeyError as e:
    assert e.args == ((1, 2),)

# the handler variable can be reused
for i in range(3):
    try:
        raise ValueError(i)
    except ValueError as e:
        assert e.args == (i,)
print("exceptions ok")
