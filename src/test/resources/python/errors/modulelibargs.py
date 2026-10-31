package.path = "?.py;test/python/errors/?.py"
require("args")

# arg type tests for module library functions

# require
banner('require')
checkallpass('require',[['math', 'coroutine', 'package', 'string', 'table']],True)
checkallerrors('require',[[anumber]],'not found')
checkallerrors('require',[[anil, aboolean, afunction, atable]],'bad argument')

# package.seeall
banner('package.seeall')
checkallpass('package.seeall',[sometable])
checkallerrors('package.seeall',[notatable],'bad argument')

