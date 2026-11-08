import datetime

dates = [(1, 1, 1), (1970, 1, 1), (1999, 12, 31), (2000, 2, 29), (2000, 3, 1), (2024, 2, 29), (2026, 11, 8),
         (2026, 1, 1), (2025, 12, 29), (2021, 1, 3), (2020, 12, 31), (2018, 12, 31), (9999, 12, 31)]
for y, m, d in dates:
    x = datetime.date(y, m, d)
    print(x, repr(x), x.year, x.month, x.day, x.weekday(), x.isoweekday(), tuple(x.isocalendar()), x.toordinal())
    print(tuple(x.timetuple()), x.isoformat(), x.ctime())
    print(datetime.date.fromordinal(x.toordinal()) == x, x.replace(day=1), x.replace(month=1, day=1))
    if y > 1000:
        print(x.strftime("%Y-%m-%d|%a %A %b %B|%j %U %W %V %G %g|%u %w %y %C|%e %d %m|%x"))

stamps = [(2026, 11, 8, 14, 31, 10, 0), (2026, 11, 8, 0, 0, 0, 0), (2000, 2, 29, 23, 59, 59, 999999),
          (1970, 1, 1, 0, 0, 0, 1), (1999, 12, 31, 12, 30, 0, 500000), (2026, 11, 8, 0, 0, 5, 0)]
for t in stamps:
    x = datetime.datetime(*t)
    print(x, repr(x), x.year, x.hour, x.minute, x.second, x.microsecond, x.tzinfo)
    print(x.isoformat(), x.isoformat(" "), x.isoformat("T", "milliseconds"), x.isoformat("_", "minutes"),
          x.isoformat("T", "hours"), x.isoformat("T", "seconds"), x.isoformat("T", "microseconds"))
    print(tuple(x.timetuple()), x.date(), x.ctime(), x.weekday(), x.toordinal(), tuple(x.isocalendar()))
    print(x.strftime("%Y-%m-%d %H:%M:%S.%f|%I %p|%Z|%z|%c|%x %X|%T|%D|%F|%R|%r"))
    print(x.replace(hour=1), x.replace(year=2001, month=2, day=3, hour=4, minute=5, second=6, microsecond=7))
    print(datetime.datetime.fromordinal(x.toordinal()))

# the order and the equality are on the values
a = datetime.date(2026, 11, 8)
b = datetime.date(2026, 11, 8)
c = datetime.date(2026, 11, 9)
print(a == b, a != b, a == c, a != c, a < c, a <= b, a > c, a >= b, a is b, a in [b], [c, b].index(a))
print(a < c, c < a, c > a, c >= a, a <= c, c <= a)
print([c, a, b].count(a), sorted([c, a, datetime.date(2020, 1, 1)]), min(c, a), max(c, a))
d = {a: "a"}
print(d[b], b in d, c in d, len({a: 1, b: 2}), {a: 1, c: 2}[datetime.date(2026, 11, 9)])
x1 = datetime.datetime(2026, 11, 8, 10)
x2 = datetime.datetime(2026, 11, 8, 10)
print(x1 == x2, x1 != x2, x1 < datetime.datetime(2026, 11, 8, 10, 0, 1), x1 > x2, x1 >= x2, {x1: 1}[x2])
print(x1 == a, a == x1, x1 != a, x1 == "2026-11-08", a == 5, a == None)
print(sorted([datetime.datetime(2026, 1, 2), datetime.datetime(2025, 5, 1), datetime.datetime(2026, 1, 1, 5)]))
print(isinstance(x1, datetime.date), isinstance(a, datetime.datetime), isinstance(x1, datetime.datetime),
      type(x1).__name__, type(a).__name__, datetime.datetime.__name__)
print(datetime.MINYEAR, datetime.MAXYEAR, datetime.date.min, datetime.date.max)
print(datetime.datetime.min, datetime.datetime.max, repr(datetime.datetime.max))


def kind(f):
    try:
        f()
        return "no error"
    except TypeError:
        return "TypeError"
    except ValueError:
        return "ValueError"
    except AttributeError:
        return "AttributeError"
    except OverflowError:
        return "OverflowError"


def setyear():
    a.year = 3


def newattr():
    a.foo = 1


def delyear():
    del a.year


def setclass():
    datetime.date.x = 1


print(kind(setyear), kind(newattr), kind(delyear), kind(setclass))
print(kind(lambda: a < x1), kind(lambda: x1 < a), kind(lambda: a < 5), kind(lambda: 5 > a), kind(lambda: a <= "x"))
print(kind(lambda: datetime.date(0, 1, 1)), kind(lambda: datetime.date(10000, 1, 1)),
      kind(lambda: datetime.date(2026, 13, 1)), kind(lambda: datetime.date(2026, 0, 1)),
      kind(lambda: datetime.date(2026, 2, 30)), kind(lambda: datetime.date(2026, 4, 31)),
      kind(lambda: datetime.date(2023, 2, 29)), kind(lambda: datetime.date(2024, 2, 29)))
print(kind(lambda: datetime.datetime(2026, 1, 1, 24)), kind(lambda: datetime.datetime(2026, 1, 1, 0, 60)),
      kind(lambda: datetime.datetime(2026, 1, 1, 0, 0, 60)), kind(lambda: datetime.datetime(2026, 1, 1, 0, 0, 0, 1000000)),
      kind(lambda: datetime.datetime(2026, 1, 1, -1)))
print(kind(lambda: datetime.date("a", 1, 1)), kind(lambda: datetime.date(1, 1)), kind(lambda: datetime.date(1, 1, 1, 1)),
      kind(lambda: datetime.date(1, 1, 1.5)), kind(lambda: datetime.date(1, 1, day=1, year=2)),
      kind(lambda: datetime.date(1, 1, foo=1)), kind(lambda: datetime.datetime(1, 1)))
print(datetime.date(year=2026, month=1, day=2), datetime.date(2026, 1, day=2), datetime.datetime(2026, 1, 2, minute=5))
print(kind(lambda: a.replace(day=31)), kind(lambda: a.replace(foo=1)), kind(lambda: a.replace(2027, 1, 1, 1)))
print(kind(lambda: datetime.date.fromordinal(0)), kind(lambda: datetime.date.fromordinal(3652060)),
      datetime.date.fromordinal(1), datetime.date.fromordinal(3652059), datetime.date.fromordinal(737000))
print(datetime.datetime.fromordinal(1), datetime.datetime.fromordinal(737000))
print(a.strftime(""), kind(lambda: a.strftime(5)), kind(lambda: a.strftime()), kind(lambda: a.isoformat(1)))

for text in ["2026-11-08", "0001-01-01", "9999-12-31", "2024-02-29"]:
    print(datetime.date.fromisoformat(text), datetime.datetime.fromisoformat(text))
for text in ["2026-11-08T14:31:10", "2026-11-08 14:31", "2026-11-08T14", "2026-11-08T14:31:10.5",
             "2026-11-08T14:31:10.123456", "2026-11-08T14:31:10.123456789", "2026-11-08T00:00:00", "2026-11-08T23:59:59"]:
    print(repr(datetime.datetime.fromisoformat(text)))
for text in ["2026-13-01", "2026/11/08", "2026-11-8", "hello", "", "2026-11-08T25:00", "2026-11-08T", "2026-11-08T14:3",
             "2026-02-30", "26-11-08", "2026-11-08T14:31:10.", "2026-11-08T14:31:10.x"]:
    print(kind(lambda: datetime.datetime.fromisoformat(text)), kind(lambda: datetime.date.fromisoformat(text)))
print(kind(lambda: datetime.date.fromisoformat("2026-11-08T14:31")), kind(lambda: datetime.date.fromisoformat(5)))

# the arithmetic of CPython with the methods that exist
d0 = datetime.date(2026, 11, 8)
print(datetime.date.fromordinal(d0.toordinal() + 7), datetime.date.fromordinal(d0.toordinal() - 8),
      datetime.date(2026, 12, 25).toordinal() - d0.toordinal())
print(datetime.date.fromordinal(datetime.date(2024, 2, 28).toordinal() + 2),
      datetime.date.fromordinal(datetime.date(2026, 12, 31).toordinal() + 1))
