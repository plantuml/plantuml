package net.sourceforge.plantuml.codeGeneration;

public class CppDefinitionTemplateProvider {

    public String getFieldTemplate() {
        return "{{type}} {{name}};";
    }

    public String getMethodTemplate() {
        return "{{type}} {{className}}::{{name}}({{args}});";
    }

    public String getClassTemplate() {
        return "class {{name}}\n{\n\t{{fields}}\n\n\t{{methods}}\n\n};\n";
    }
}
