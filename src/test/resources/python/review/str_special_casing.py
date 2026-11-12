# Case mappings that change the length, title case, and the final sigma.
# VM at 148134d: 'SS' for 'ß'.capitalize(), 'ß' unchanged by title and
# swapcase, a title-cased 'ǆ' in upper case, and no final sigma outside lower().
print('ß'.upper(), 'ß'.capitalize(), 'ß'.title(), 'ß'.swapcase(), 'ß'.lower())
print('ﬁ'.upper(), 'ﬁ'.capitalize(), 'ﬁ'.title(), 'ﬁ'.swapcase())
print('ǆ'.upper(), 'ǆ'.title(), 'ǆa'.capitalize(), 'ǅ'.swapcase(), 'ǅ'.lower(), 'ǅ'.upper())
print('ΑΣ'.lower(), 'ΑΣ'.title(), 'ΑΣ'.capitalize(), 'ΑΣ'.swapcase(), 'aΣ'.title(), 'ΑΣ Β'.lower(), 'Σ'.lower())
print('ŉ'.upper(), 'ŉ'.capitalize(), 'ŉ'.title(), 'ǰ'.upper(), 'ΐ'.upper(), len('İ'.lower()))
