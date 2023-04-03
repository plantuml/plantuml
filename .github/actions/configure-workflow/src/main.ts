import * as core from '@actions/core';
import * as github from '@actions/github';
import { PushEvent, WorkflowDispatchEvent } from '@octokit/webhooks-types';

import { wait } from "./wait";

async function run(): Promise<void> {
	try {
		const ms: string = core.getInput("milliseconds");
		core.debug(`Waiting ${ms} milliseconds ...`); // debug is only output if you set the secret `ACTIONS_STEP_DEBUG` to true

		core.debug(new Date().toTimeString());
		await wait(parseInt(ms, 10));
		core.debug(new Date().toTimeString());

		core.setOutput("time", new Date().toTimeString());
	} catch (error) {
		if (error instanceof Error) core.setFailed(error.message);
	}
}

function hadnleEvent(): void {
	console.info(`eventName: ${github.context.eventName}`);
	switch (github.context.eventName) {
		case "workflow_dispatch":
			onWorkflowDispatchEvent(github.context.payload as WorkflowDispatchEvent);
			break;
		case "push":
			onPush(github.context.payload as PushEvent);
			break;
	}
}

function onWorkflowDispatchEvent(payload: WorkflowDispatchEvent): void {
	core.info(`The workflow is: ${payload.workflow}`);
}

function onPush(payload: PushEvent): void {
	core.info(`The head commit is: ${payload.head_commit}`);
}

hadnleEvent();
run();