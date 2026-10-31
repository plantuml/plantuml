class AppError(Exception):
    pass

class ConfigError(AppError):
    pass

def load(name):
    raise ConfigError("cannot load " + name)

try:
    load("a")
except ConfigError as e:
    print("caught", e)
load("b")
