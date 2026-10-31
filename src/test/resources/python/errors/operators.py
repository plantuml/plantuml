package.path = "?.py;test/python/errors/?.py"
require("args")

# arg types for language operator
notstringortable = {None, anumber, aboolean, afunction, "n": 4}
# ========= unary operators: - # not

# unary minus -
banner('unary -')
def negative(a):
    return - a
checkallpass('negative',[somenumber])
checkallerrors('negative',[notanumber],'attempt to perform arithmetic')

# length
banner('#')
def lengthop(a):
    return len(a)
checkallpass('lengthop',[sometable])
checkallerrors('lengthop',[notstringortable],'attempt to get length of')

# length
banner('not')
def notop(a):
    return not a
checkallpass('notop',[somenumber])
checkallpass('notop',[notanumber])

# function call
banner('()')
def funcop(a):
    return a()
checkallpass('funcop',[somefunction])
checkallerrors('funcop',[notafunction],'attempt to call')

# ========= binary ops: .. + - * / % ^ == ~= <= >= < > [] . and or
banner('..')
def concatop(a,b):
    return a..b
checkallpass('concatop',[somestring, somestring])
checkallerrors('concatop',[notastring, somestring],'attempt to concatenate')
checkallerrors('concatop',[somestring, notastring],'attempt to concatenate')

banner('+')
def plusop(a,b):
    return a+b
checkallpass('plusop',[somenumber, somenumber])
checkallerrors('plusop',[notanumber, somenumber],'attempt to perform arithmetic')
checkallerrors('plusop',[somenumber, notanumber],'attempt to perform arithmetic')

banner('-')
def minusop(a,b):
    return a-b
checkallpass('minusop',[somenumber, somenumber])
checkallerrors('minusop',[notanumber, somenumber],'attempt to perform arithmetic')
checkallerrors('minusop',[somenumber, notanumber],'attempt to perform arithmetic')

banner('*')
def timesop(a,b):
    return a*b
checkallpass('timesop',[somenumber, somenumber])
checkallerrors('timesop',[notanumber, somenumber],'attempt to perform arithmetic')
checkallerrors('timesop',[somenumber, notanumber],'attempt to perform arithmetic')

banner('/')
def divideop(a,b):
    return a/b
checkallpass('divideop',[somenumber, somenumber])
checkallerrors('divideop',[notanumber, somenumber],'attempt to perform arithmetic')
checkallerrors('divideop',[somenumber, notanumber],'attempt to perform arithmetic')

banner('%')
def modop(a,b):
    return a%b
checkallpass('modop',[somenumber, somenumber])
checkallerrors('modop',[notanumber, somenumber],'attempt to perform arithmetic')
checkallerrors('modop',[somenumber, notanumber],'attempt to perform arithmetic')

banner('^')
def powerop(a,b):
    return a ** b
checkallpass('powerop',[[2, '2.5'], [3, '3.5']])
checkallerrors('powerop',[notanumber, [3, '3.1']],'attempt to perform arithmetic')
checkallerrors('powerop',[[2, '2.1'], notanumber],'attempt to perform arithmetic')

banner('==')
def equalsop(a,b):
    return a==b
checkallpass('equalsop',[anylua, anylua])

banner('~=')
def noteqop(a,b):
    return a != b
checkallpass('noteqop',[anylua, anylua])

banner('<=')
def leop(a,b):
    return a<=b
checkallpass('leop',[[anumber], [anumber]])
checkallpass('leop',[[astring, astrnum], [astring, astrnum]])
checkallerrors('leop',[notanumber, [anumber]],'attempt to compare')
checkallerrors('leop',[[astrnum], [anumber]],'attempt to compare')
checkallerrors('leop',[notastring, [astring, astrnum]],'attempt to compare')
checkallerrors('leop',[[anumber], notanumber],'attempt to compare')
checkallerrors('leop',[[anumber], [astrnum]],'attempt to compare')
checkallerrors('leop',[[astring, astrnum], notastring],'attempt to compare')

banner('>=')
def geop(a,b):
    return a>=b
checkallpass('geop',[[anumber], [anumber]])
checkallpass('geop',[[astring, astrnum], [astring, astrnum]])
checkallerrors('geop',[notanumber, [anumber]],'attempt to compare')
checkallerrors('geop',[[astrnum], [anumber]],'attempt to compare')
checkallerrors('geop',[notastring, [astring, astrnum]],'attempt to compare')
checkallerrors('geop',[[anumber], notanumber],'attempt to compare')
checkallerrors('geop',[[anumber], [astrnum]],'attempt to compare')
checkallerrors('geop',[[astring, astrnum], notastring],'attempt to compare')

banner('<')
def ltop(a,b):
    return a<b
checkallpass('ltop',[[anumber], [anumber]])
checkallpass('ltop',[[astring, astrnum], [astring, astrnum]])
checkallerrors('ltop',[notanumber, [anumber]],'attempt to compare')
checkallerrors('ltop',[[astrnum], [anumber]],'attempt to compare')
checkallerrors('ltop',[notastring, [astring, astrnum]],'attempt to compare')
checkallerrors('ltop',[[anumber], notanumber],'attempt to compare')
checkallerrors('ltop',[[anumber], [astrnum]],'attempt to compare')
checkallerrors('ltop',[[astring, astrnum], notastring],'attempt to compare')

banner('>')
def gtop(a,b):
    return a>b
checkallpass('gtop',[[anumber], [anumber]])
checkallpass('gtop',[[astring, astrnum], [astring, astrnum]])
checkallerrors('gtop',[notanumber, [anumber]],'attempt to compare')
checkallerrors('gtop',[[astrnum], [anumber]],'attempt to compare')
checkallerrors('gtop',[notastring, [astring, astrnum]],'attempt to compare')
checkallerrors('gtop',[[anumber], notanumber],'attempt to compare')
checkallerrors('gtop',[[anumber], [astrnum]],'attempt to compare')
checkallerrors('gtop',[[astring, astrnum], notastring],'attempt to compare')

banner('[]')
def bracketop(a,b):
    return a[b]
checkallpass('bracketop',[sometable, notanil])
checkallerrors('bracketop',[notstringortable, notanil],'attempt to index')
#checkallerrors('bracketop',[sometable],'attempt to index')

banner('.')
def dotop(a,b):
    return a.b
checkallpass('dotop',[sometable, notanil])
checkallerrors('dotop',[notstringortable, notanil],'attempt to index')
#checkallerrors('dotop',[sometable],'attempt to index')

banner('and')
types = {"table": "table", "function": "function", "thread": "thread"}
def clean(x):
    return types[type(x)] or x
def andop(a,b):
    return clean(a and b)
checkallpass('andop',[anylua, anylua])

banner('or')
def orop(a,b):
    return clean(a or b)
checkallpass('orop',[anylua, anylua])

# ========= for x in y
banner('for x=a,b,c')
# A numeric `for` with arbitrary (possibly non-numeric) bounds. The original checks
# the error messages "'for' initial value must be a number" and so on; with
# range()'s exclusive limit the compiler would have to keep those checks.
def forop(a, b, c):
    for x in range(a, b, c):
        pass
checkallpass('forop',[[1, '1.1'], [10, '10.1'], [2, '2.1']])
checkallerrors('forop',[notanumber, [10, '10.1'], [2, '2.1']],"'for' initial value must be a number")
checkallerrors('forop',[[1, '1.1'], notanumber, [2, '2.1']],"'for' limit must be a number")
checkallerrors('forop',[[1, '1.1'], [10, '10.1'], notanumber],"'for' step must be a number")


