# Decorators on functions, methods and classes: order of evaluation and of
# application, decorators with arguments, decorators that replace or register.

trace = []

def log(name):
    trace.append("evaluate " + name)
    def decorator(f):
        trace.append("apply " + name + " to " + f.__name__)
        return f
    return decorator

@log("outer")
@log("inner")
def target():
    return "target"

print(target())
print(trace)

# a decorator that replaces the function
def twice(f):
    def wrapper(*args, **kwargs):
        return [f(*args, **kwargs), f(*args, **kwargs)]
    return wrapper

@twice
def greet(name, punct="!"):
    return "hello " + name + punct

print(greet("ann"), greet("bob", punct="?"))
print(greet.__name__)

# a registry, the shape of the future @macro
registry = {}

def register(name=None):
    def decorator(f):
        registry[name if name is not None else f.__name__] = f
        return f
    return decorator

@register()
def color(n):
    return "#FFCCCC"

@register("$size")
def size(n):
    return len(n)

print(sorted(registry.keys()), registry["color"]("P"), registry["$size"]("abc"), color("Q"))

# a decorator reached through an attribute
class Tools:
    def __init__(self):
        self.seen = []
    def mark(self, f):
        self.seen.append(f.__name__)
        return f

tools = Tools()

@tools.mark
def first():
    return 1

@tools.mark
def second():
    return 2

print(tools.seen, first() + second())

# decorators of methods, and of a class
def upper(f):
    def wrapper(self, *args):
        return f(self, *args).upper()
    return wrapper

def tagged(cls):
    cls.tag = "tagged " + cls.__name__
    return cls

@tagged
class Box:
    def __init__(self, label):
        self.label = label
    @upper
    def describe(self, prefix=""):
        return prefix + "box " + self.label

b = Box("a")
print(b.describe(), b.describe("big "), Box.tag, b.tag)

# a decorator may return something that is not a function
def constant(f):
    return 42

@constant
def answer():
    return 0

print(answer)

# decorators evaluated in the enclosing scope, with closures
def make(prefix):
    def deco(f):
        def wrapper():
            return prefix + f()
        return wrapper
    @deco
    def inner():
        return "x"
    return inner()

print(make("<"), make(">"))
