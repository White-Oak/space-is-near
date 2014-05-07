package spaceisnear.server.objects.items.scripts;

/**
 *
 * @author White Oak
 */
public class ItemScriptBundle {

    public String name;
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
