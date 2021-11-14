package net.sourceforge.plantuml.codeGeneration;

import java.io.IOException;
import java.nio.file.*;

import net.sourceforge.plantuml.cucadiagram.Code;
import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.cucadiagram.entity.EntityImpl;

public class CppTemplateProvider {

    public String getHeader(EntityImpl impl) {
        Path file = Paths.get("h.tmpl");
        try {
            String text = Files.readString(file);
            text = text.replace("{{name}}", impl.getCodeGetName());
            Display fields = impl.getBodier().getFieldsToDisplay();
            String fieldsString = "";
            for (int i = 0; i < fields.size(); i++) {
                fieldsString += fields.get(i).toString() + ";";
                if (i + 1 < fields.size()) {
                    fieldsString += "\n";
                }
            }
            text = text.replace("{{fields}}", fieldsString);

            Display methods = impl.getBodier().getMethodsToDisplay();
            String methodsString = "";
            for (int i = 0; i < methods.size(); i++) {
                methodsString += methods.get(i).toString() + ";";
                if (i + 1 < methods.size()) {
                    methodsString += "\n";
                }
            }
            text = text.replace("{{methods}}", methodsString);
            return text;
        } catch (IOException e) {
            return "not found";
        }
    }

    public String getCpp(EntityImpl impl) {
        Path file = Paths.get("cpp.tmpl");
        try {
            Code namespace = impl.getNamespace();
            String text = Files.readString(file);
            text = text.replace("{{name}}", impl.getCodeGetName());
            Display fields = impl.getBodier().getFieldsToDisplay();
            String fieldsString = "";
            for (int i = 0; i < fields.size(); i++) {
                fieldsString += impl.getCodeGetName() + "::" + fields.get(i).toString() + ";";
                if (i + 1 < fields.size()) {
                    fieldsString += "\n\n";
                }
            }
            text = text.replace("{{fields}}", fieldsString);

            Display methods = impl.getBodier().getMethodsToDisplay();
            String methodsString = "";
            for (int i = 0; i < methods.size(); i++) {
                String[] str = methods.get(i).toString().split("\\s+");
                methodsString += str[0] + " " + impl.getCodeGetName() + "::" + str[1] + "\n{\n\n}";
                if (i + 1 < methods.size()) {
                    methodsString += "\n\n";
                }
            }
            text = text.replace("{{methods}}", methodsString);
            return text;
        } catch (IOException e) {
            return "not found";
        }
    }
}
