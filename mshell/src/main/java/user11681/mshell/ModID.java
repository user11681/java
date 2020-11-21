package user11681.mshell;

public class ModID {
    public static final String ID = "mshell";
    public static final String NAME = "MShell";

    public static String key(final String name) {
        return String.format("key.%s.%s", ID, name);
    }
}
