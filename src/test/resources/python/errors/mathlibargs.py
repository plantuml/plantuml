package.path = "?.py;test/python/errors/?.py"
require("args")

ts = tostring
def shorttostring(x):
    s = ts(x)
    return type(x) == "number" and len(s) > 4 and (s:sub(1, 5) .. "...") or s
_G.tostring = shorttostring

# arg type tests for math library functions
somenumber = [1, 0.75, '-1', '-0.25']
somepositive = [1, 0.75, '2', '2.5']
notanumber = [None, astring, aboolean, afunction, atable]
nonnumber = [astring, aboolean, afunction, atable]

singleargfunctions = [
    "abs", "acos", "asin", "atan", "cos", "cosh", "deg", "exp", "floor",
    "rad", "randomseed", "sin", "sinh", "tan", "tanh", "frexp",
]

singleargposdomain = [
    "log", "sqrt", "ceil",
]

twoargfunctions = [
    "atan2",
]

twoargsposdomain = [
    "pow", "fmod",
]

# single argument tests
for i, v in ipairs(singleargfunctions):
    funcname = "math." .. v
    banner(funcname)
    checkallpass(funcname,[somenumber])
    checkallerrors(funcname,[notanumber],'bad argument')

# single argument, positive domain tests
for i, v in ipairs(singleargposdomain):
    funcname = "math." .. v
    banner(funcname)
    checkallpass(funcname,[somepositive])
    checkallerrors(funcname,[notanumber],'bad argument')

# two-argument tests
for i, v in ipairs(twoargfunctions):
    funcname = "math." .. v
    banner(funcname)
    checkallpass(funcname,[somenumber, somenumber])
    checkallerrors(funcname,[],'bad argument')
    checkallerrors(funcname,[notanumber],'bad argument')
    checkallerrors(funcname,[notanumber, somenumber],'bad argument')
    checkallerrors(funcname,[somenumber],'bad argument')
    checkallerrors(funcname,[somenumber, notanumber],'bad argument')

# two-argument, positive domain tests
for i, v in ipairs(twoargsposdomain):
    funcname = "math." .. v
    banner(funcname)
    checkallpass(funcname,[somepositive, somenumber])
    checkallerrors(funcname,[],'bad argument')
    checkallerrors(funcname,[notanumber],'bad argument')
    checkallerrors(funcname,[notanumber, somenumber],'bad argument')
    checkallerrors(funcname,[somenumber],'bad argument')
    checkallerrors(funcname,[somenumber, notanumber],'bad argument')

# math.max
banner('math.max')
checkallpass('math.max',[somenumber])
checkallpass('math.max',[somenumber, somenumber])
checkallerrors('math.max',[],'bad argument')
checkallerrors('math.max',[nonnumber],'bad argument')
checkallerrors('math.max',[somenumber, nonnumber],'bad argument')

# math.min
banner('math.min')
checkallpass('math.min',[somenumber])
checkallpass('math.min',[somenumber, somenumber])
checkallerrors('math.min',[],'bad argument')
checkallerrors('math.min',[nonnumber],'bad argument')
checkallerrors('math.min',[somenumber, nonnumber],'bad argument')

# math.random
somem = [3, 4.5, '6.7']
somen = [8, 9.10, '12.34']
notamn = [astring, aboolean, atable, afunction]
banner('math.random')
checkallpass('math.random',[],True)
checkallpass('math.random',[somem],True)
checkallpass('math.random',[somem, somen],True)
checkallpass('math.random',[[-4, -5.6, '-7', '-8.9'], [-1, 100, 23.45, '-1.23']],True)
checkallerrors('math.random',[[-4, -5.6, '-7', '-8.9']],'interval is empty')
checkallerrors('math.random',[somen, somem],'interval is empty')
checkallerrors('math.random',[notamn, somen],'bad argument')
checkallerrors('math.random',[somem, notamn],'bad argument')

# math.ldexp
somee = [-3, 0, 3, 9.10, '12.34']
notae = [None, astring, aboolean, atable, afunction]
banner('math.ldexp')
checkallpass('math.ldexp',[somenumber, somee])
checkallerrors('math.ldexp',[],'bad argument')
checkallerrors('math.ldexp',[notanumber],'bad argument')
checkallerrors('math.ldexp',[notanumber, somee],'bad argument')
checkallerrors('math.ldexp',[somenumber],'bad argument')
checkallerrors('math.ldexp',[somenumber, notae],'bad argument')
