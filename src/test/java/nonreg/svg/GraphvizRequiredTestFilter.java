package nonreg.svg;

import net.sourceforge.plantuml.dot.GraphvizRuntimeEnvironment;
import net.sourceforge.plantuml.dot.GraphvizUtils;
import org.junit.jupiter.api.extension.ConditionEvaluationResult;
import org.junit.jupiter.api.extension.ExecutionCondition;
import org.junit.jupiter.api.extension.ExtensionContext;

import java.io.File;

public class GraphvizRequiredTestFilter implements ExecutionCondition {
    @Override
    public ConditionEvaluationResult evaluateExecutionCondition(ExtensionContext context) {
        if (findDotExe()) {
            return ConditionEvaluationResult.enabled("Dot exe was found, so test can run");
        } else {
            return ConditionEvaluationResult.disabled("Dot exe was not found, so test will be skipped");
        }
    }

    @SuppressWarnings("CallToPrintStackTrace")
    private boolean findDotExe() {
        try {
            final File dotExeFile = GraphvizRuntimeEnvironment.getInstance().getDotExe();
            return dotExeFile.exists();
        } catch (Exception e) {
            System.err.println("Failed to find dot.exe as test prerequisite. Will skip the test.");
            e.printStackTrace();
            return false;
        }
    }
}
