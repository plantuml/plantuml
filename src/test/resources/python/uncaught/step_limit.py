# NOT run under CPython: the VM stops a script that runs too long.  The error
# raised when the instruction counter reaches its limit cannot be caught by
# `except`, so the code after the loop is never reached and the run ends with
# an execution-limit error (max-steps is in step_limit.err).

try:
    while True:
        pass
except Exception:
    print("must not be reached")
except:
    print("must not be reached either")
print("must not be reached")
