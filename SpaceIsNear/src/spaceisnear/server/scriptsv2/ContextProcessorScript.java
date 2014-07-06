package spaceisnear.server.scriptsv2;

import spaceisnear.server.ServerContext;

public abstract class ContextProcessorScript extends ItemScript {

    private int chosen;

    public void init(ServerContext context, int chosen) {
	super.init(context);
	this.chosen = chosen;
    }

    protected int getChosen() {
	return chosen;
    }

    protected void interact() {
    }
}
