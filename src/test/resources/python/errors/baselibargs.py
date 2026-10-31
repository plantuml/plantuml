package.path = "?.py;test/python/errors/?.py"
require("args")

# arg types for basic library functions

# assert
banner('assert')
checkallpass('assert',[[True, 123], anylua])
checkallerrors('assert',[{None, False, "n": 2}, {None, "n": 1}],'assertion failed')
checkallerrors('assert',[{None, False, "n": 2}, ['message']],'message')

# collectgarbage
banner('collectgarbage')
# does nothing and always returns 0, whatever the arguments
checkallpass('collectgarbage',[['collect', 'count', 'step', 'abc', 1.25, True]])

# error
banner('error')
#checkallerrors('error', [['message'], {None, 0, 1, 2, "n": 4}], 'message')
#checkallerrors('error', [[123], {None, 1, 2, "n": 3}], 123)

# getmetatable
banner('getmetatable')
checkallpass('getmetatable', [notanil])
checkallerrors('getmetatable',[],'bad argument')

# ipairs
banner('ipairs')
checkallpass('ipairs', [sometable])
checkallerrors('ipairs', [], 'bad argument')

# load
banner('load')
checkallpass('load', [somefunction, {None, astring, "n": 2}])
checkallpass('load', [['return']])
checkallpass('load', [['return'], ['mychunk']])
# FIXME why is the result not printed?
#checkallpass('load', [['return a ... b'], ['mychunk']],True)
checkallerrors('load', [somefunction, nonstring], 'bad argument')
checkallerrors('load', [[None, aboolean, atable], notastring], 'bad argument')
checkallerrors('load', [['return'], nonstring], 'bad argument')

# next
banner('next')
checkallpass('next', [sometable, somekey])
checkallerrors('next', [notatable, {None, 1, "n": 2}], 'bad argument')
checkallerrors('next', [sometable, nonkey], 'invalid key')

# pairs
banner('pairs')
checkallpass('pairs', [sometable])
checkallerrors('pairs', [notatable], 'bad argument')

# pcall
banner('pcall')
checkallpass('pcall', [notanil, anylua], True)
checkallerrors('pcall',[],'bad argument')

# print
banner('print')
checkallpass('print', [])
checkallpass('print', [{None, astring, anumber, aboolean, "n": 4}])

# rawequal
banner('rawequal')
checkallpass('rawequal', [notanil, notanil])
checkallerrors('rawequal', [], 'bad argument')
checkallerrors('rawequal', [notanil], 'bad argument')

# rawget
banner('rawget')
checkallpass('rawget', [sometable, somekey])
checkallpass('rawget', [sometable, nonkey])
checkallerrors('rawget', [sometable, somenil],'bad argument')
checkallerrors('rawget', [notatable, notakey], 'bad argument')
checkallerrors('rawget', [], 'bad argument')

# rawset
banner('rawset')
checkallpass('rawset', [sometable, somekey, notanil])
checkallpass('rawset', [sometable, nonkey, notanil])
checkallerrors('rawset', [sometable, somenil],'table index is nil')
checkallerrors('rawset', [], 'bad argument')
checkallerrors('rawset', [notatable, somestring, somestring], 'bad argument')
checkallerrors('rawset', [sometable, somekey], 'bad argument')

# select
banner('select')
checkallpass('select', [[anumber, '#'], anylua])
checkallerrors('select', [notanumber], 'bad argument')

# setmetatable
banner('setmetatable')
checkallpass('setmetatable', [sometable, {None, atable, "n": 2}])
checkallerrors('setmetatable',[notatable, sometable],'bad argument')
checkallerrors('setmetatable',[sometable, nontable],'bad argument')

# tonumber
banner('tonumber')
checkallpass('tonumber',[somenumber, {None, 2, 10, 36, "n": 4}])
checkallpass('tonumber',[notanil, {None, "n": 1}])
checkallpass('tonumber',[somestring, [10]])
checkallerrors('tonumber',[notastring, [10]],'bad argument')
checkallerrors('tonumber',[{None, afunction, atable, "n": 3}, [2, 9, 11, 36]],'bad argument')
checkallerrors('tonumber',[somenumber, [1, 37, atable, afunction, aboolean]],'bad argument')

# tostring
banner('tostring')
checkallpass('tostring',[[astring, anumber, aboolean]])
checkallpass('tostring',[[atable, afunction]],True)
checkallpass('tostring',[[astring, anumber, aboolean], ['anchor']])
checkallpass('tostring',[[atable, afunction], ['anchor']],True)
checkallerrors('tostring',[],'bad argument')

# type
banner('type')
checkallpass('type',[notanil])
checkallpass('type',[anylua, ['anchor']])
checkallerrors('type',[],'bad argument')

# xpcall
banner('xpcall')
checkallpass('xpcall', [notanil, nonfunction],True)
def threevalues(*args):
    return 'aaa', 'bbb', len([*args])
checkallpass('xpcall', [notanil, [threevalues]],True)
checkallerrors('xpcall',[anylua],'bad argument')

