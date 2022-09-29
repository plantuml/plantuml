package net.sourceforge.plantuml.test;

import net.sourceforge.plantuml.project.Failable;
import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.AbstractStringAssert;
import org.assertj.core.api.Assertions;

public class FailableAssert<T> extends AbstractAssert<FailableAssert<T>, Failable<T>> {
	public FailableAssert(Failable<T> actual) {
		super(actual, FailableAssert.class);
	}

	public static <T> FailableAssert<T> assertThat(Failable<T> actual) {
		return new FailableAssert<>(actual);
	}

	public FailableAssert<T> isOk() {
		isNotNull();
		if (actual.isFail()) {
			failWithMessage("Expected Failable to be ok but it was error");
		}
		return this;
	}

	public FailableAssert<T> isError() {
		isNotNull();
		if (!actual.isFail()) {
			failWithMessage("Expected Failable to be error but it was ok");
		}
		return this;
	}

	public AbstractAssert<?, T> data() {
		isNotNull();
		isOk();

		return Assertions.assertThat(actual.get());
	}

	public AbstractStringAssert<?> errorMessage() {
		isNotNull();
		isError();

		return Assertions.assertThat(actual.getError());
	}
}
