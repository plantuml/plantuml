package net.sourceforge.plantuml.cucadiagram;

import net.sourceforge.plantuml.Pragma;
import net.sourceforge.plantuml.core.DiagramDescription;
import net.sourceforge.plantuml.graphic.USymbol;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.*;

class CucaDiagramTest {
	@Test
	void getDotStringsPassThroughLayout() {
		CucaDiagram mockedCucaDiagram = mock(CucaDiagram.class);
		when(mockedCucaDiagram.getDotStrings()).thenReturn(Arrays.asList("layout=false"));
		Pragma pragma = mock(Pragma.class);
		when(mockedCucaDiagram.getPragma()).thenReturn(pragma);

		assert Arrays.asList(mockedCucaDiagram.getDotStringSkek()).contains("layout=false");
	}

	@Test
	void getDotStringsDoesNotPassThroughUnsupportedAttribute() {
		CucaDiagram mockedCucaDiagram = mock(CucaDiagram.class);
		when(mockedCucaDiagram.getDotStrings()).thenReturn(Arrays.asList("unsupportedparam=false"));
		Pragma pragma = mock(Pragma.class);
		when(mockedCucaDiagram.getPragma()).thenReturn(pragma);
		assert !Arrays.asList(mockedCucaDiagram.getDotStringSkek()).contains("unsupportedparam=false");
	}


}