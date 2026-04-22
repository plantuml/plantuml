package test.test;

import static test.utils.PlantUmlTestUtils.exportDiagram;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class SvekEdgeOrthoLabelTest {

	@Test
	void test_ortho_labels_render_without_error() throws Exception {

		final String output = exportDiagram(
						"@startuml",
						"skinparam linetype ortho",
						"",
						"rectangle A",
						"rectangle B",
						"rectangle C",
						"",
						"A --> B : Very very long label",
						"B --> C : Another very very long label",
						"A --> C : Crossing label",
						"",
						"@enduml"
		).asString();

		assertThat(output).isNotEmpty();

		assertThat(output)
						.contains("A")
						.contains("B")
						.contains("C");
	}

	@Test
	void test_dense_ortho_labels_render_without_error() throws Exception {

		final String output = exportDiagram(
						"@startuml",
						"skinparam linetype ortho",
						"",
						"rectangle A",
						"rectangle B",
						"rectangle C",
						"rectangle D",
						"",
						"A --> B : Long label",
						"B --> C : Long label",
						"C --> D : Long label",
						"A --> C : Crossing label",
						"B --> D : Crossing label",
						"",
						"@enduml"
		).asString();

		assertThat(output).isNotEmpty();

		assertThat(output)
						.contains("A")
						.contains("B")
						.contains("C")
						.contains("D");
	}
}