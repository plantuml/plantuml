package dev.newline;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import net.sourceforge.plantuml.FileFormat;
import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.GeneratedImage;
import net.sourceforge.plantuml.SourceFileReader;
import net.sourceforge.plantuml.preproc.Defines;

/*
 * 

https://forum.plantuml.net/19243/unicode-u-005c-backslash-in-tooltip-text-causes-error
@startwbs
+ raw string literals \
 [["Example:\n\nconst char* msg1 = <U+0022><U+005C>nHello,world!<U+0022>;" TP]]
@endwbs

https://forum.plantuml.net/19243/unicode-u-005c-backslash-in-tooltip-text-causes-error
@startwbs
+ raw string literals \
 [["Example:\n\nconst char* msg1 = <U+0022>\<U+200B>nHello,world!<U+0022>;" TP]]
@endwbs

https://forum.plantuml.net/19243/unicode-u-005c-backslash-in-tooltip-text-causes-error
@startwbs
+ raw string literals \
 [[{Example:\n\nconst char* msg1 = <U+0022>\<U+200B>nHello,world!<U+0022>;} TP]]
@endwbs

https://forum.plantuml.net/17434/multiline-tootlip-in-table-not-supported
@startuml
title
<#lightblue,#red>|=  Step  |=  Date  |=  Name  |=  Status  |=  Link  |
<#lightgreen>|  1.1  |  [[multiline.svg{multi line tooltip\\ntest} multiline label test]] | plantuml news |<#Navy><color:OrangeRed><b> Unknown  | [[https://plantuml.com/news{single line tooltip test} tooltip test]] |
end title
@enduml

@startuml
' Adapted from https://forum.plantuml.net/17434
title minimal example
file f [
[[{a\nb} tooltip test ]]

| [[{a\\nb} tooltip test on table ]] |

[[{a\nb\nprint("abc\n");} tooltip test ]]
[[{a\nb\n<code>print("abc\n");</code>} tooltip test ]]

[[{a\nb\nprint("abc\\n");} tooltip test ]]
[[{a\nb\n<code>print("abc\\n");</code>} tooltip test ]]

| [[{a\\nb\\n<code>print("abc\\n");</code>} tooltip test on table ]] |
]
@enduml



 */
public class link_URL_tooltip {

	protected File getJavaFile() {
		final String name = getClass().getName();
		final File f = new File("src/test/java/" + name.replace('.', '/') + ".java");
		return f;
	}

	@Test
	public void testExecute() throws IOException, InterruptedException {
		final File file = getJavaFile();
		final FileFormatOption options = new FileFormatOption(FileFormat.PNG);

		final File outputDirectory = new File("outputdev").getAbsoluteFile();
		outputDirectory.mkdirs();

		final SourceFileReader reader = new SourceFileReader(Defines.createWithFileName(file), file, outputDirectory,
				Arrays.asList("!pragma layout smetana"), "UTF-8", options);
		final List<GeneratedImage> list = reader.getGeneratedImages();

		assertEquals(5, list.size());

	}


}
