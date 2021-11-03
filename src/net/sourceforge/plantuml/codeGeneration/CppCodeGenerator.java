package net.sourceforge.plantuml.codeGeneration;

import java.io.*;
import java.util.List;

import net.sourceforge.plantuml.BlockUml;
import net.sourceforge.plantuml.core.Diagram;
import net.sourceforge.plantuml.classdiagram.ClassDiagram;
import net.sourceforge.plantuml.cucadiagram.ILeaf;
import net.sourceforge.plantuml.cucadiagram.entity.EntityImpl;
import net.sourceforge.plantuml.preproc.Defines;
import net.sourceforge.plantuml.sequencediagram.SequenceDiagram;
import net.sourceforge.plantuml.sequencediagram.Event;

public class CppCodeGenerator extends CodeGeneratorAbstract {

    public CppCodeGenerator(File file, Defines defines, List<String> config, String charsetName) throws IOException {
        super(file, defines, config, charsetName);
    }

    @Override
    public String generateCodeText() {
        for (final BlockUml b : builder.getBlockUmls()) {
            Diagram d = b.getDiagram();
            if (d instanceof SequenceDiagram) {
                SequenceDiagram diagram = SequenceDiagram.class.cast(b.getDiagram());
                return generateFromSequenceDiagram(diagram);
            } else if (d instanceof ClassDiagram) {
                ClassDiagram diagram = ClassDiagram.class.cast(b.getDiagram());
                return generateFromClassDiagram(diagram);
            }
        }
        return "hoge";
    }

    public String generateFromSequenceDiagram(SequenceDiagram diagram) {
        for (Event e : diagram.events()) {

        }
        return "1";

    }

    public String generateFromClassDiagram(ClassDiagram diagram) {
        String res = "";
        for (ILeaf l : diagram.getEntityFactory().leafs()) {
            if (l instanceof EntityImpl) {
                res += "class " + l.getCodeGetName() + "\n";
                res += "{\n";
                for (CharSequence s : l.getBodier().getRawBody()) {
                    res += s + "\n";
                }
                res += "}\n";
            }
        }
        return res;
    }
}
