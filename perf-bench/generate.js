'use strict';
// Deterministic corpus generator for the perf benchmark. Running it must reproduce the checked-in
// corpus/ files byte for byte; if you change a generator, regenerate and commit the fixtures and
// update expected-bands.json in the same commit (band history is only meaningful for a fixed corpus).
const fs = require('fs'), path = require('path');
const C = path.join(__dirname, 'corpus');
fs.mkdirSync(path.join(C, 'small'), { recursive: true });

function w(rel, lines) {
  fs.writeFileSync(path.join(C, rel), lines.join('\n') + '\n');
  console.log(rel, lines.length, 'lines');
}

// Message and note heavy sequence diagrams, n arrows (the shape a test-reporting tool emits:
// autonumbered calls with JSON/SQL note payloads).
function gen(n) {
  const L = ['@startuml', 'skinparam responseMessageBelowArrow true', 'participant "Test" as T', 'participant "Example.Api" as A', 'database "OrdersDb" as D', 'participant "External" as X'];
  for (let i = 0; i < n; i++) {
    L.push('T -> A: GET /orders/' + i + '?include=lines');
    L.push('A -> D: SELECT o.id, o.total FROM orders o WHERE o.id = ' + i);
    L.push('D --> A: 1 row (id=' + i + ')');
    if (i % 3 === 0) { L.push('note right of A'); L.push('{ "id": ' + i + ', "customer": "cust-' + i + '", "total": ' + (i * 3.5).toFixed(2) + ' }'); L.push('end note'); }
    if (i % 5 === 0) { L.push('A -> X: POST /audit/' + i); L.push('X --> A: 202 Accepted'); }
    L.push('A --> T: 200 OK (' + (i * 7 % 40) + ' ms)');
  }
  L.push('@enduml');
  return L;
}
w('gen-50.puml', gen(50));
w('gen-200.puml', gen(200));
w('gen-500.puml', gen(500));

// Nested groups: alt/opt/loop five deep, repeated blocks.
{
  const L = ['@startuml', 'participant "Test" as T', 'participant "Api" as A', 'database "Db" as D'];
  for (let b = 0; b < 12; b++) {
    L.push('alt case ' + b, 'T -> A: outer ' + b, 'opt config ' + b, 'A -> D: q1-' + b, 'loop retry', 'D --> A: rows ' + b, 'alt found', 'A -> D: q2-' + b, 'opt cached', 'D --> A: hit ' + b, 'A --> T: 200 (' + b + ')', 'end', 'else missing', 'A --> T: 404 (' + b + ')', 'end', 'end', 'end', 'else skip ' + b, 'T -> A: fallback ' + b, 'A --> T: 204', 'end');
  }
  L.push('@enduml');
  w('shape-groups.puml', L);
}

// Parallel (&) arrows.
{
  const L = ['@startuml', 'participant "A" as A', 'participant "B" as B', 'participant "C" as C', 'participant "D" as D'];
  for (let i = 0; i < 80; i++) {
    L.push('A -> B: left ' + i, '& C -> D: right ' + i, 'B --> A: ack ' + i, '& D --> C: ack ' + i);
  }
  L.push('@enduml');
  w('shape-parallel.puml', L);
}

// Wide: 24 participants, messages hop across the full width.
{
  const N = 24;
  const L = ['@startuml'];
  for (let p = 0; p < N; p++) L.push('participant "Svc' + String(p).padStart(2, '0') + '" as P' + p);
  for (let i = 0; i < 150; i++) {
    const a = i % N, b = (i * 7 + 3) % N;
    if (a === b) continue;
    L.push('P' + a + ' -> P' + b + ': call ' + i);
    if (i % 4 === 0) L.push('P' + b + ' --> P' + a + ': reply ' + i);
  }
  L.push('@enduml');
  w('shape-wide.puml', L);
}

// Activation heavy: activate/deactivate around every call, nested.
{
  const L = ['@startuml', 'participant "T" as T', 'participant "A" as A', 'participant "B" as B'];
  for (let i = 0; i < 90; i++) {
    L.push('T -> A: req ' + i, 'activate A', 'A -> B: inner ' + i, 'activate B', 'B --> A: done ' + i, 'deactivate B', 'A --> T: resp ' + i, 'deactivate A');
    if (i % 6 === 0) {
      L.push('T -> A: nested ' + i, 'activate A', 'A -> A: recurse ' + i, 'activate A', 'A --> A: pop ' + i, 'deactivate A', 'A --> T: out ' + i, 'deactivate A');
    }
  }
  L.push('@enduml');
  w('shape-activation.puml', L);
}

// Self messages.
{
  const L = ['@startuml', 'participant "A" as A', 'participant "B" as B'];
  for (let i = 0; i < 120; i++) {
    L.push('A -> A: local step ' + i);
    if (i % 3 === 0) L.push('A -> B: sync ' + i);
    if (i % 3 === 1) L.push('B -> B: check ' + i);
  }
  L.push('@enduml');
  w('shape-self.puml', L);
}

// Create/destroy participants.
{
  const L = ['@startuml', 'participant "Main" as M'];
  for (let i = 0; i < 40; i++) {
    L.push('create participant "Worker' + i + '" as W' + i, 'M -> W' + i + ': spawn ' + i, 'W' + i + ' --> M: ready ' + i, 'M -> W' + i + ': task ' + i, 'W' + i + ' --> M: result ' + i, 'destroy W' + i);
  }
  L.push('@enduml');
  w('shape-createdestroy.puml', L);
}

// Many small: 30 diagrams x 10 arrows. Reports with one diagram per test are dominated by
// per diagram fixed cost, so this set is reported as a single aggregated row.
for (let d = 0; d < 30; d++) {
  const L = ['@startuml', 'participant "Test" as T', 'participant "Api" as A', 'database "Db" as D'];
  for (let i = 0; i < 5; i++) {
    L.push('T -> A: op-' + d + '-' + i, 'A -> D: query ' + d + '.' + i, 'D --> A: rows');
    if (i === 2) L.push('note right of A', '{ "d": ' + d + ', "i": ' + i + ' }', 'end note');
    L.push('A --> T: ok');
  }
  L.push('@enduml');
  w('small/small-' + String(d).padStart(2, '0') + '.puml', L);
}
console.log('done');
