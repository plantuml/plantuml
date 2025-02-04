package net.sourceforge.plantuml.yaml.parser;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class YamlParserTest {

    @ParameterizedTest
    @ValueSource(strings = {
        "metadata:",
        "  name: vibrant:",
        "  type: \"dark\":"
    })
    void testSimple(String s) {
        YamlParser parser = new YamlParser(2);
        List<String> list = Arrays.asList(s);
        parser.parse(list);
    }
}
