import * as core from '@actions/core';
import * as github from '@actions/github';
import {PullRequestEvent, PushEvent, ReleaseEvent, WorkflowDispatchEvent} from '@octokit/webhooks-types';

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

function onPullRequestEvent(payload: PullRequestEvent) {
	switch(payload.action){
		case "labeled":
			console.log("pull request label "+payload.label)
			break;
		default:
			console.log("pull request action "+payload.action)
	}
	console.info('pull request event:',payload)
}

function onWorkflowDispatchEvent(payload: WorkflowDispatchEvent): void {
	console.info('workflow dispatch event:',payload)
}

function onPushEvent(payload: PushEvent): void {
	console.info('push event:',payload)
}

function onReleaseEvent(payload: ReleaseEvent) {
	console.info('onReleaseEvent:',payload)

}

function handleEvent(): void {
	console.info(`eventName: ${github.context.eventName}`);
	switch (github.context.eventName) {
		case "workflow_dispatch":
			onWorkflowDispatchEvent(github.context.payload as WorkflowDispatchEvent);
			break;
		case "push":
			onPushEvent(github.context.payload as PushEvent);
			break;
		case "pull_request":
			onPullRequestEvent(github.context.payload as PullRequestEvent)
			break;
			// case "pull_request_target":
			// onPullRequestEvent(github.context.payload as PullRequestEvent)
			// break;
		case "release":
			onReleaseEvent(github.context.payload as ReleaseEvent)
		default:
			break;
	}
}

handleEvent();
run();