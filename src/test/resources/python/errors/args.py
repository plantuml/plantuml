# utilities to check that args of various types pass or fail
# argument type checking
#
# The original keeps its helpers as chunk-level locals and publishes a few
# globals. Module-level names are globals here, so the body lives in setup():
# only what is declared `global` below is visible to the scripts that require it.
# (Without that, `types` below would be clobbered by the `types` table that
# operators.py assigns at its own module level.)
def setup():
    global akey, astring, astrnum, anumber, ainteger, adouble, aboolean, atable, afunction, anil
    global anylua, somestring, somenumber, someboolean, sometable, somefunction, somethread, somenil, somekey, notakey
    global notastring, notanumber, notaboolean, notatable, notafunction, notathread, notanil
    global nonstring, nonnumber, nonboolean, nontable, nonfunction, nonthread, nonkey
    global lookup, invoke, banner, checkallpass, checkallerrors

    ok = "-\t"
    fail = "fail "
    needcheck = "needcheck "
    badmsg = "badmsg "

    akey = "aa"
    astring = "abc"
    astrnum = "789"
    anumber = 1.25
    ainteger = 345
    adouble = 12.75
    aboolean = True
    atable = {akey: 456}
    def afunction(): pass
    anil = None

    anylua = [None, astring, anumber, aboolean, atable, afunction]

    somestring = [astring, anumber]
    somenumber = [anumber, astrnum]
    someboolean = [aboolean]
    sometable = [atable]
    somefunction = [afunction]
    somethread = []
    somenil = [anil]
    somekey = [akey]
    notakey = [astring, anumber, aboolean, atable, afunction]

    notastring = [None, aboolean, atable, afunction]
    notanumber = [None, astring, aboolean, atable, afunction]
    notaboolean = [None, astring, anumber, atable, afunction]
    notatable = [None, astring, anumber, aboolean, afunction]
    notafunction = [None, astring, anumber, aboolean, atable]
    notathread = [None, astring, anumber, aboolean, atable, afunction]
    notanil = [astring, anumber, aboolean, atable, afunction]

    nonstring = [aboolean, atable, afunction]
    nonnumber = [astring, aboolean, atable, afunction]
    nonboolean = [astring, anumber, atable, afunction]
    nontable = [astring, anumber, aboolean, afunction]
    nonfunction = [astring, anumber, aboolean, atable]
    nonthread = [astring, anumber, aboolean, atable, afunction]
    nonkey = [astring, anumber, aboolean, atable, afunction]

    structtypes = {
        "table": "<table>",
        "function": "<function>",
        "thread": "<thread>",
        "userdata": "<userdata>",
    }

    def bracket(v):
        return "<" .. type(v) .. ">"

    def quote(v):
        return "'" .. v .. "'"

    def ellipses(v):
        s = tostring(v)
        return len(s) <= 8 and s or (string.sub(s, 1, 8) .. "...")

    pretty = {
        "table": bracket,
        "function": bracket,
        "thread": bracket,
        "userdata": bracket,
        "string": quote,
        "number": ellipses,
    }

    def values(list):
        t = {}
        for i in range(1, (list.n or len(list)) + 1):
            ai = list[i]
            fi = pretty[type(ai)]
            t[i] = fi and fi(ai) or tostring(ai)
        return table.concat(t, ",")

    def types(list):
        t = {}
        for i in range(1, (list.n or len(list)) + 1):
            ai = list[i]
            t[i] = type(ai)
        return table.concat(t, ",")

    def signature(name, arglist):
        return name .. "(" .. values(arglist) .. ")"

    def dup(t):
        s = {}
        for i in range(1, (t.n or len(t)) + 1):
            s[i] = t[i]
        return s

    def split(t):
        s = {}
        n = (t.n or len(t))
        for i in range(1, n):
            s[i] = t[i]
        return s, t[n]

    def expand(argsets, typesets, *args):
        arg = table.pack(*args)
        n = typesets and len(typesets) or 0
        if n <= 0:
            table.insert(argsets, arg)
            return argsets

        s, v = split(typesets)
        for i in range(1, (v.n or len(v)) + 1):
            expand(argsets, s, v[i], table.unpack(arg, 1, arg.n))
        return argsets

    def arglists(typesets):
        argsets = expand({}, typesets)
        return ipairs(argsets)

    def lookup(name):
        return load("return " .. name)()

    def invoke(name, arglist):
        s, c = pcall(lookup, name)
        if not s: return s, c
        return pcall(c, table.unpack(arglist, 1, arglist.n or len(arglist)))

    # messages, banners
    _print = print
    _tostring = tostring
    _find = string.find
    def banner(name):
        _print("====== " .. _tostring(name) .. " ======")

    def subbanner(name):
        _print("--- " .. _tostring(name))

    def pack(s, *args):
        return s, [*args]

    # check that all combinations of arguments pass
    def checkallpass(name, typesets, typesonly):
        subbanner("checkallpass")
        for i, v in arglists(typesets):
            sig = signature(name, v)
            s, r = pack(invoke(name, v))
            if s:
                if typesonly:
                    _print(ok, sig, types(r))
                else:
                    _print(ok, sig, values(r))
            elif type(r[1]) == "string" and _find(r[1], "no integer representation", 1, True):
                # integer arguments do not accept floats that have no integer value (like 1.25)
                _print(ok, sig, "...no integer representation...")
            else:
                _print(fail, sig, values(r))

    # check that all combinations of arguments fail in some way,
    # ignore error messages
    def checkallerrors(name, typesets, template):
        subbanner("checkallerrors")
        template = _tostring(template)
        for i, v in arglists(typesets):
            sig = signature(name, v)
            s, e = invoke(name, v)
            if not s:
                if _find(e, template, 1, True):
                    _print(ok, sig, "..." .. template .. "...")
                elif _find(e, "no integer representation", 1, True):
                    _print(ok, sig, "...no integer representation...")
                else:
                    _print(badmsg, sig, "template='" .. template .. "' actual='" .. e .. "'")
            else:
                _print(needcheck, sig, e)

setup()
