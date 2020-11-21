package user11681.mshell.shell;

import org.joor.CompileOptions;
import org.joor.Reflect;
import user11681.mshell.MShell;

public class MEngine {
    private static final String SKELETON = "package user11681.mshell.shell; public class MShellExecutor {public static void execute() {%s}}";

    public static void tryExecute(final String code) {
        try {
            Reflect.compile("user11681.mshell.shell.MShellExecutor", String.format(SKELETON, code), new CompileOptions().options("-Xverify:none")).call("execute");
        } catch (final Throwable exception) {
            MShell.LOGGER.error("An error occured while attempting to execute code.", exception);
        }
    }
}
