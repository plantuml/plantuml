# __str__ defined by an exception class of the program is used by str(), print,
# the formatting and the text of the exception.
# VM at 148134d: the method is ignored, str(E('a')) is 'a'.
class E(Exception):
    def __str__(self):
        return 'custom'

class Coded(ValueError):
    def __init__(self, code):
        ValueError.__init__(self, code)
        self.code = code
    def __str__(self):
        return 'code %d' % self.code

class OnlyRepr(Exception):
    def __repr__(self):
        return 'OnlyRepr!'

print(str(E('a')), E('a'), '%s' % E('a'), f'{E("a")}', '{}'.format(E('a')), [str(E())])
print(repr(E('a')), E('a').args)
try:
    raise E('a')
except Exception as e:
    print(e, str(e), repr(e), e.args)
try:
    raise Coded(7)
except ValueError as e:
    print(e, str(e), e.args, e.code)
print(repr(OnlyRepr('x')), str(OnlyRepr('x')), [OnlyRepr('x')])
