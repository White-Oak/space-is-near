package spaceisnear.game.components.server.scriptsv2;

/**
 *
 * @author White Oak
 */
public class ItemScriptBundle {

    public String scriptName;
    public boolean contextMenuInit;
    public boolean messageProcessing;
    public boolean interaction;
    public boolean contextMenuRequesting;
    public boolean contextMenuProccessing;
    public boolean time;

    public boolean hasScript() {
	return contextMenuInit || messageProcessing || interaction || contextMenuRequesting || contextMenuProccessing || time;
    }
}
