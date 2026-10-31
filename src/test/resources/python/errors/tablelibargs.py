package.path = "?.py;test/python/errors/?.py"
require("args")

# arg type tests for table library functions

# table.concat
somestringstable = [[8, 7, 6, 5, 4, 3, 2, 1]]
somenonstringtable = [[True, True, True, True, True, True, True, True]]
somesep = [',', 1.23]
notasep = [aboolean, atable, afunction]
somei = [2, '2', '2.2']
somej = [4, '4', '4.4']
notij = [astring, aboolean, atable, afunction]
banner('table.concat')
checkallpass('table.concat',[somestringstable])
checkallpass('table.concat',[somestringstable, somesep])
checkallpass('table.concat',[somestringstable, ['-'], somei])
checkallpass('table.concat',[somestringstable, ['-'], [2], somej])
checkallerrors('table.concat',[notatable],'bad argument')
checkallerrors('table.concat',[somenonstringtable],'boolean')
checkallerrors('table.concat',[somestringstable, notasep],'bad argument')
checkallerrors('table.concat',[somestringstable, ['-'], notij],'bad argument')
checkallerrors('table.concat',[somestringstable, ['-'], [2], notij],'bad argument')

# table.insert
banner('table.insert')
checkallpass('table.insert',[sometable, notanil])
checkallpass('table.insert',[sometable, somei, notanil])
checkallerrors('table.insert',[notatable, somestring],'bad argument')
checkallerrors('table.insert',[sometable, notij, notanil],'bad argument')

# table.remove
banner('table.remove')
checkallpass('table.remove',[sometable])
checkallpass('table.remove',[sometable, somei])
checkallerrors('table.remove',[notatable],'bad argument')
checkallerrors('table.remove',[notatable, somei],'bad argument')
checkallerrors('table.remove',[sometable, notij],'bad argument')

# table.sort
somecomp = {None, afunction, "n": 2}
notacomp = [astring, anumber, aboolean, atable]
banner('table.sort')
checkallpass('table.sort',[somestringstable, somecomp])
checkallerrors('table.sort',[sometable],'attempt to')
checkallerrors('table.sort',[notatable, somecomp],'bad argument')
checkallerrors('table.sort',[sometable, notacomp],'bad argument')

# table get
banner('table_get - tbl[key]')
def table_get(tbl, key): return tbl[key]
checkallpass('table_get',[sometable, anylua])

# table set
banner('table_set - tbl[key]=val')
def table_set(tbl, key, val): tbl[key] = val
def table_set_nil_key(tbl, val): tbl[None] = val
checkallpass('table_set',[sometable, notanil, anylua])
checkallerrors('table_set_nil_key',[sometable, anylua],'table index')

# table.unpack
banner('table.unpack')
checkallpass('table.unpack',[sometable])
checkallpass('table.unpack',[sometable, [3, '5']])
checkallpass('table.unpack',[sometable, [3, '5'], [1.25, '7']])
checkallerrors('table.unpack',[notatable, somenumber, somenumber],'bad argument')
checkallerrors('table.unpack',[sometable, nonnumber, somenumber],'bad argument')
checkallerrors('table.unpack',[sometable, somenumber, nonnumber],'bad argument')


