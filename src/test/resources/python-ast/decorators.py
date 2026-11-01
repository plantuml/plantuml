@macro
def color(n):
    return "#FFCCCC"

@macro("$color")
@puml.macro
def size(n, unit="px"):
    pass

# a comment between the decorator and the def

@register(name="box")

def box():
    pass

@tagged
class Box(Base):
    @upper
    def describe(self):
        pass
