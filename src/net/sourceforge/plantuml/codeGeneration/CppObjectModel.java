package net.sourceforge.plantuml.codeGeneration;

import java.util.List;

public abstract class CppObjectModel {
    // 定義時のテキスト
    public abstract List<String> getDefinitionString();

    // 宣言時のテキスト
    public abstract List<String> getDeclarationString();

    // 使用時のテキスト
    public abstract List<String> getDependString();
}
