package spaceisnear.server.contexteditors;

import java.util.LinkedList;
import java.util.Queue;
import spaceisnear.server.ServerContext;

/**
 *
 * @author White Oak
 */
public abstract class ContextEditor {

    private final Queue<Action> actions = new LinkedList<>();

    public abstract void update(ServerContext context);

    public void makeChangesTo(ServerContext context) {
	while (!actions.isEmpty()) {
	    actions.poll().act(context);
	}
    }

    protected void addAction(Action action) {
	actions.add(action);
    }
}
