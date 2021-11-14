package net.sourceforge.plantuml.codeGeneration;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import net.sourceforge.plantuml.cucadiagram.Display;
import net.sourceforge.plantuml.cucadiagram.entity.EntityImpl;

public class CppClassDefinitionGenerator {
    public void generate(EntityImpl impl, String dirPath) {
        String headerStr = generateHeader(impl.getCodeGetName());
        String classStr = generateClass(impl);

        String res = headerStr + "\n\n" + classStr;
        String filePath = dirPath;
        if (filePath.charAt(filePath.length() - 1) != '/') {
            filePath += '/';
        }
        filePath += impl.getCodeGetName().replace('.', '/') + ".cpp";

        saveFile(res, filePath);
    }

    private String generateHeader(String className) {
        return "/*\n *\tby sono 2021.\n */";
    }

    private String generateClass(EntityImpl impl) {
        String rawClassName = impl.getCodeGetName();
        String[] splittedName = rawClassName.split("\\.");
        String className = splittedName[splittedName.length - 1];
        String res = "";
        for (int i = 0; i + 1 < splittedName.length; i++) {
            res += "namespace " + splittedName[i] + "\n{\n";
        }
        res += "class " + className + "\n{\n\n";
        res += generateFields(impl.getBodier().getFieldsToDisplay(), className, "\t");
        res += "\n\n";
        res += generateMethods(impl.getBodier().getMethodsToDisplay(), className, "\t");

        res += "\n\n};\n";

        for (int i = 0; i + 1 < splittedName.length; i++) {
            res += "}\n";
        }

        return res;
    }

    private String generateFields(Display fields, String className, String indentStr) {
        String res = "";
        for (int i = 0; i < fields.size(); i++) {
            // 【暫定】テンプレートなどで空白が生じるような場合は未対応
            // 空白で型と変数名を識別
            CharSequence hoge = fields.get(i);
            String[] fieldStrs = fields.get(i).toString().split(" ", 2);
            res += fieldStrs[0] + " " + className + "::" + fieldStrs[1] + ";";
            if (i + 1 < fields.size()) {
                res += "\n\n";
            }
        }

        return res;
    }

    private String generateMethods(Display methods, String className, String indentStr) {
        String res = "";
        for (int i = 0; i < methods.size(); i++) {
            // 【暫定】テンプレート、引数などで空白が生じるような場合は未対応
            // 空白で戻り値と関数名+引数を識別
            String[] fieldStrs = methods.get(i).toString().split(" ", 2);
            res += "//------------------------\n" + fieldStrs[0] + " " + className + "::" + fieldStrs[1] + "\n{\n\n}";
            if (i + 1 < methods.size()) {
                res += "\n\n";
            }
        }

        return res;
    }

    private void saveFile(String str, String filePath) {
        try {
            File file = new File(filePath);

            // ディレクトリを作成
            File dir = new File(file.getParent());
            dir.mkdir();

            FileWriter fw = new FileWriter(file);
            fw.write(str);
            fw.close();
        } catch (IOException e) {
            System.out.println(e);
        }
    }
}
