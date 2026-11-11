'use strict';

const fs = require('fs');
const path = require('path');
const { spawnSync } = require('child_process');
const { parseTargetArg } = require('../lib/browser-cli');

const scriptName = path.basename(__filename, '.js');
const { dir, file } = parseTargetArg(process.argv, `node ${scriptName}.js target=<dir-or-js>`);
const targetArg = `target=${path.join(dir, file)}`;

const scripts = fs.readdirSync(__dirname)
	.filter(name => /^check-.*\.js$/.test(name))
	.sort();

if (scripts.length === 0) {
	console.error('No check-*.js scripts found.');
	process.exit(1);
}

const failures = [];
for (const script of scripts) {
	console.log(`::group::Run ${script}`);
	const result = spawnSync(process.execPath, [path.join(__dirname, script), targetArg], {
		cwd: __dirname,
		stdio: 'inherit',
	});

	if (result.error) {
		console.error(`${script} could not be started: ${result.error.message}`);
		failures.push(script);
	} else if (result.status !== 0) {
		console.error(`${script} failed${result.signal ? ` (signal ${result.signal})` : ` (exit ${result.status})`}.`);
		failures.push(script);
	}
	console.log(`::endgroup::`);
}

if (failures.length > 0) {
	console.error(`\n❌ ${failures.length} check(s) failed: ${failures.join(', ')}`);
	process.exitCode = 1;
} else {
	console.log(`\n✅ All ${scripts.length} checks passed.`);
}