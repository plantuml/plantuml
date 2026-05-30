package net.sourceforge.plantuml.gantt.ngm;

import java.time.Duration;
import java.time.LocalDateTime;

import net.sourceforge.plantuml.gantt.ngm.math.LoadIntegrator;

public class NGMTaskFixedTotalEffort extends NGMTask {

	private Load totalEffort;
	private LocalDateTime start;
	private LocalDateTime end;

	public NGMTaskFixedTotalEffort(NGMAllocation allocation, LocalDateTime start, Load totalEffort) {
		super(allocation);
		this.totalEffort = totalEffort;
		this.start = start;
		this.end = new LoadIntegrator(allocation.getLoadFunction(), totalEffort).computeEnd(start);
	}

	@Override
	public LocalDateTime getStart() {
		return start;
	}

	@Override
	public void setStart(LocalDateTime start) {
		this.start = start;
		this.end = new LoadIntegrator(allocation.getLoadFunction(), totalEffort).computeEnd(start);
	}

	@Override
	public LocalDateTime getEnd() {
		return end;
	}

	@Override
	public void setEnd(LocalDateTime end) {
		this.end = end;
		this.start = new LoadIntegrator(allocation.getLoadFunction(), totalEffort).computeStart(end);
	}

	@Override
	public void setEffort(Load effort) {
		this.totalEffort = effort;
		this.end = new LoadIntegrator(allocation.getLoadFunction(), totalEffort).computeEnd(start);
	}

	@Override
	public Duration getDuration() {
		return Duration.between(start, end);
	}

	@Override
	public Load getTotalEffort() {
		return totalEffort;
	}

}
