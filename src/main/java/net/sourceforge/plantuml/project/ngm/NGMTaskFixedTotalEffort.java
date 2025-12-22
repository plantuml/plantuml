package net.sourceforge.plantuml.project.ngm;

import java.time.Duration;
import java.time.LocalDateTime;

import net.sourceforge.plantuml.project.ngm.math.Fraction;
import net.sourceforge.plantuml.project.ngm.math.LoadIntegrator;

public class NGMTaskFixedTotalEffort extends NGMTask {

	private NGMTotalEffort totalEffort;
	private LocalDateTime start;
	private LocalDateTime end;

	public NGMTaskFixedTotalEffort(NGMAllocation allocation, NGMTotalEffort totalEffort) {
		super(allocation);
		this.totalEffort = totalEffort;
	}

	@Override
	public LocalDateTime getStart() {
		return start;
	}

	@Override
	public void setStart(LocalDateTime start) {
		this.start = start;
		this.end = new LoadIntegrator(allocation.getLoadFunction(), start, new Fraction(totalEffort.toSeconds(), 86_400)).computeEnd();
	}

	@Override
	public LocalDateTime getEnd() {
		return end;		
	}

	@Override
	public void setEnd(LocalDateTime end) {
		this.end = end;
		this.start = new LoadIntegrator(allocation.getLoadFunction(), end, new Fraction(totalEffort.toSeconds(), 86_400)).computeStart();
		// TODO implement reverse calculation to set start based on end and totalEffort
	}

	@Override
	public Duration getDuration() {
		return Duration.between(start, end);
	}

	@Override
	public NGMTotalEffort getTotalEffort() {
		return totalEffort;
	}

}
