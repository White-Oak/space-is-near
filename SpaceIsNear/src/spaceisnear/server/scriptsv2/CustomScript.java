package spaceisnear.server.scriptsv2;

import spaceisnear.server.ServerContext;

/**
 *
 * @author White Oak
 */
public abstract class CustomScript {

    protected ServerContext context;

    public void init(ServerContext context) {
	this.context = context;
    }

    public abstract void script();

}
