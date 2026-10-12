# teavm-coroutines

`check_no_coroutines.py` fails when the code TeaVM generated for PlantUML still
contains coroutines. It runs in CI on the browser engine, the headless engine
(MCP server) and the C sources (`browser-test.yml`, `teavm-c.yml`).

```
python3 tools/teavm-coroutines/check_no_coroutines.py build/generated/teavm/js/plantuml.js
python3 tools/teavm-coroutines/check_no_coroutines.py build/generated/teavm/c
```

A file argument is a JavaScript output (obfuscated or not), a directory is a C
output. Python 3, standard library only.

## Why

TeaVM emulates Java's blocking calls in single-threaded targets with
coroutines: a method that can suspend (`Thread.sleep`, `Object.wait`,
`Thread.join`, a contended `synchronized` once a thread has been started, an
`@Async` method, `java.net.URL` in JavaScript, which waits for an XHR...) is
compiled to a state machine that can save its locals and resume later, and so
is **every method that can reach it**. One suspension point deep in the call
graph is enough to turn most of PlantUML into such code: about 1,940 methods
in the browser engine before this was removed. The author of TeaVM asked us not
to rely on it, and the C backend fails on some of these methods
(`ArrayIndexOutOfBoundsException` in its transformation).

PlantUML therefore uses none of it. What a rendering needs from JavaScript and
can only get asynchronously is handled by rendering again
(`teavm.js.browser.PendingResources` and `RenderQueue`): the attempt that missed
a resource is discarded, the resource is loaded with JavaScript callbacks, and
the diagram is rendered again from its source. Do not add a thread, a
`wait()`, a `sleep()` or an `@Async` method on a path the TeaVM entry points
(`PlantUMLBrowser`, `PlantUMLHeadless`, `MainC`) can reach; guard such code with
`TeaVM.isTeaVM()` (or `isJavaScript()` / `isC()`) so that TeaVM drops it.

## What is counted

- JavaScript: the state machines of coroutines (`switch ($ptr)`, `switch($p)`
  when obfuscated) and the points where a thread suspends
  (`thread.suspend(`). TeaVM always emits its own `Object.monitorEnter()` as a
  coroutine, with the `monitorEnterWait()` it suspends in, even when nothing
  calls it: one of each is allowed, nothing more. When the output is not
  obfuscated, the names of the coroutines are listed.
- C: the functions that check whether their fiber is resuming
  (`meth_otr_Fiber_isResuming`), outside TeaVM's own runtime and except
  `Object.monitorEnter()`, for the same reason. They are listed with their
  file.

Before the coroutines were removed, the script reported 1,939 coroutines and
4 suspension points for the browser engine (`Object.wait`,
`Object.monitorEnterWait`, the XHR of `java.net.URL`, and the Viz.js bridge),
and 1,945 for the headless engine.
