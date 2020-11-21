package user11681.projectfabrok.plugin;

import com.sun.source.util.JavacTask;
import com.sun.source.util.Plugin;

public class ProjectFabrokPlugin implements Plugin {
    @Override
    public String getName() {
        return null;
    }

    @Override
    public void init(final JavacTask task, final String... args) {
        task.addTaskListener(new ProjectFabrokTaskListener());
    }
}
