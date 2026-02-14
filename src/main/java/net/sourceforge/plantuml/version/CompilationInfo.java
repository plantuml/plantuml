package net.sourceforge.plantuml.version;

public class CompilationInfo {
    
    public static final String VERSION = "$version$";

    public static final String COMMIT = "$git.commit.id$";

    // placeholder: will be replaced during build when filtering is enabled
    public static final long COMPILE_TIMESTAMP = 000L;

}
