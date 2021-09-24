package net.sourceforge.plantuml.ugraphic;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import sun.java2d.pipe.RenderingEngine;

public class RenderingEngineTest {
	
	@Test
	void test_rendering_engine_class() {
		assertThat(RenderingEngine.getInstance().getClass().getName())
				.isEqualTo("sun.java2d.marlin.DMarlinRenderingEngine");
	}
}
