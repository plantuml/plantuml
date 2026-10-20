# Brotli decoder

Decoder of https://github.com/google/brotli (MIT, see LICENSE), copied as is
from the `c/` directory at commit 7b93874f (only `dec/`, the files of `common/`
it needs, and the headers of `include/brotli/` the decoder uses; no encoder).

It decompresses the `.spm` files of the standard library embedded in the
executable, see `plantuml_resources.c`.
