package user11681.mshell.shell;

import jdk.jshell.JShell;
import user11681.mshell.MShell;

public class MEngine {
    private static final String SKELETON = "package user11681.mshell.shell; public class MShellExecutor {public static void execute() {%s}}";
    private static final JShell shell = JShell.create();

    public static void tryExecute(final String code) {
        try {
            shell.eval(code);
        } catch (final Throwable exception) {
            MShell.LOGGER.error("An error occured while attempting to execute code.", exception);
        }
    }
}
