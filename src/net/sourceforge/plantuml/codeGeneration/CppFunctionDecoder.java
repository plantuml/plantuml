package net.sourceforge.plantuml.codeGeneration;

public class CppFunctionDecoder {
    private String name;
    private String retVal;
    private String args;

    public CppFunctionDecoder(String name, String retVal, String args) {
        this.name = name;
        this.retVal = retVal;
        this.args = args;
    }
}
