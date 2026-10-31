# str.replace and str.count, with the empty string and a maximum
for s in ['', 'a', 'abc', 'aaa', 'é\U0001f600z']:
    for old, rep in [('', '-'), ('a', 'xy'), ('aa', ''), ('', ''), ('z', 'z'), ('b', '')]:
        print([s.replace(old, rep, n) for n in (-1, 0, 1, 2, 3, 4, 5, 100)], s.replace(old, rep), s.count(old), s.count(old, 1), s.count(old, 1, 2))
