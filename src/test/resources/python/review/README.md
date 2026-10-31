# Findings of the review of commit 148134d

Scripts that print what CPython prints (the `.out` files were written by
CPython 3.13) and that the interpreter gets wrong at commit 148134d. They are
run by `PyScriptsTest` (Java) and `TestScripts` (Go) like the other scripts of
`python`. They all pass now, in both implementations. The comment at the top of
each script gives the result of the interpreter at 148134d.

| Script | Defect |
|---|---|
| `float_repr_powers_of_two.py` | `repr(float)` has 17 digits instead of 16 for 46 powers of two |
| `str_split_maxsplit_whitespace.py` | `split(None, n)` / `rsplit(None, n)` strip the remainder |
| `exception_class_str_method.py` | `__str__` of an exception class of the program is ignored |
| `except_name_deleted_in_handler.py` | `del e` in `except ... as e` raises at the end of the handler |
| `bare_raise_in_called_function.py` | a bare `raise` in a function called from a handler |
| `format_ascii_conversion.py` | `%a` and `!a` quote twice |
| `format_bool_as_int.py` | `'{:5}'.format(True)` raises `TypeError` |
| `format_zero_padding.py` | `0` padding with `,`, and `0` padding of a str |
| `format_float_without_type.py` | `'{:.3}'.format(float)`, and the `#` form of `g` |
| `format_type_checks.py` | `format(1, 2)` lets a Java exception out; specifications accepted by mistake |
| `nan_identity_in_containers.py` | `nan in [nan]`: identity is not tried before equality |
| `pow_special_values.py` | `(-1) ** inf`, `math.pow(1, nan)` |
| `str_lone_surrogates.py` | two lone surrogates merge into one character |
| `str_special_casing.py` | `'ß'.capitalize()`, title case, final sigma |
| `dict_views_comparison.py` | `d.keys() == d.keys()`, `reversed` of a view, `popitem` |

Not compared with CPython, because of the 32-bit integers:
`deviations/range_len_overflow.py` (the length of a range wraps around), run by
`PyReviewLimitsTest` (Java) and `review_limits_test.go` (Go), which also hold
the programs that get past the limits of the interpreter (memory, stack,
instruction counter).
