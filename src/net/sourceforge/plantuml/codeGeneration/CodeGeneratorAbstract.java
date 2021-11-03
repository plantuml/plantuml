/*
 * (C) Copyright 2021, sono8stream
 * This class is extention of original PlantUml.
 * Original Author:  sono8stream
 */

package net.sourceforge.plantuml.codeGeneration;

import java.io.*;
import java.nio.charset.Charset;
import java.util.List;

import net.sourceforge.plantuml.BlockUmlBuilder;
import net.sourceforge.plantuml.preproc.Defines;
import net.sourceforge.plantuml.preproc.FileWithSuffix;
import net.sourceforge.plantuml.utils.CharsetUtils;
import net.sourceforge.plantuml.security.SFile;

public abstract class CodeGeneratorAbstract {

    protected File file;

    protected final BlockUmlBuilder builder;

    public CodeGeneratorAbstract(File file, Defines defines, List<String> config, String charsetName)
            throws IOException {
        if (!file.exists()) {
            throw new IllegalArgumentException();
        }

        final Charset charset = CharsetUtils.charsetOrDefault(charsetName);

        this.file = file;
        this.builder = new BlockUmlBuilder(config, charset, defines, getReader(charset),
                SFile.fromFile(file.getAbsoluteFile().getParentFile()), FileWithSuffix.getFileName(file));
    }

    protected Reader getReader(Charset charset) throws FileNotFoundException, UnsupportedEncodingException {
        return new InputStreamReader(new BufferedInputStream(new FileInputStream(file)), charset);
    }

    public abstract String generateCodeText();
}
