# CPython reads a naive datetime as local time: the reference output is made with TZ=UTC
# (TZ=UTC python3 -I datetime_timestamp.py), the time zone of this implementation.
# CPython refuses the first day of the year 1 (year 0 is out of range), so it is not used here.
import datetime

for t in [0, 1, 86399, 951782400, 1234567890, 1700000000, 2147483647, -1, -86400, 4102444799.0, -62135510400.0,
          253402300799.0, 1234567890.5, 1234567890.123456, 1234567890.9999995, -1.5, -0.000001, 0.0000005, 1e9 + 0.25]:
    x = datetime.datetime.fromtimestamp(t)
    print(t, repr(x), x, x.timestamp(), datetime.date.fromtimestamp(t))

for t in [(2026, 11, 8, 14, 31, 10, 0), (1970, 1, 1, 0, 0, 0, 0), (2000, 2, 29, 23, 59, 59, 999999), (1, 1, 2, 0, 0, 0, 0),
          (9999, 12, 31, 23, 59, 58, 0), (1969, 12, 31, 23, 59, 59, 0), (2026, 11, 8, 0, 0, 0, 1)]:
    x = datetime.datetime(*t)
    print(x.timestamp(), datetime.datetime.fromtimestamp(x.timestamp()) == x)


def kind(f):
    try:
        f()
        return "no error"
    except TypeError:
        return "TypeError"
    except ValueError:
        return "ValueError"
    except OverflowError:
        return "OverflowError"


print(kind(lambda: datetime.datetime.fromtimestamp("x")), kind(lambda: datetime.datetime.fromtimestamp()),
      kind(lambda: datetime.date.fromtimestamp(None)))
print(type(datetime.datetime.now()).__name__, type(datetime.datetime.today()).__name__,
      type(datetime.date.today()).__name__, datetime.datetime.now().year > 2000, datetime.date.today().year > 2000,
      datetime.datetime.today() >= datetime.datetime(2020, 1, 1), datetime.datetime.now().tzinfo)
