package user11681.projectfabrok.plugin;

import com.sun.source.util.TaskEvent;
import com.sun.source.util.TaskListener;

public class ProjectFabrokTaskListener implements TaskListener {
    @Override
    public void finished(final TaskEvent event) {
        if (event.getKind() == TaskEvent.Kind.GENERATE) {
            event.getSourceFile().openOutputStream();
        }
    }
}
