package spaceisnear.server.objects.items.scripts;

/**
 *
 * @author White Oak
 */
public class ItemScriptBundle {

    public String name;
    public boolean contextMenuInit;
    public boolean messageProcessing;

    public boolean hasScript() {
	return contextMenuInit && messageProcessing;
    }
}
