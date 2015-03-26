package spaceisnear.server.scriptsv2;

import lombok.Getter;
import org.apache.commons.collections4.MapUtils;
import spaceisnear.game.messages.MessageAnimationChanged;
import spaceisnear.game.messages.MessageToSend;
import spaceisnear.server.ServerContext;
import spaceisnear.server.objects.items.StaticItem;

/**
 *
 * @author White Oak
 */
public abstract class ItemScript extends CustomScript {

    @Getter private StaticItem item;

    public final void init(ServerContext context, StaticItem item) {
	super.init(context);
	this.item = item;
    }

    protected boolean getBooleanPropertyOrFalse(String name) {
	return MapUtils.getBooleanValue(item.getVariableProperties().getStates(), name);
    }

    protected Object getProperty(String name) {
	return item.getVariableProperties().getProperty(name);
    }

    protected void setProperty(String name, Object value) {
	item.getVariableProperties().setProperty(name, value);
    }

    protected void setFullyPathable(boolean pathable) {
	boolean set = pathable;
	setProperty("blockingPath", String.valueOf(!set));
	setProperty("blockingAir", String.valueOf(!set));
	ServerContext context1 = context;
	context1.getObstacles().setReacheable(item.getPosition().getX(), item.getPosition().getY(), set);
	context1.getAtmosphere().setAirReacheable(item.getPosition().getX(), item.getPosition().getY(), set);
    }

    protected void sendAnimationQueueToRequestor(int[] queue, int i) {
	MessageAnimationChanged messageAnimationChanged;
	messageAnimationChanged = new MessageAnimationChanged(item.getId(), queue, i);
	MessageToSend messageToSend = new MessageToSend(messageAnimationChanged);
	context.sendDirectedMessage(messageToSend);
    }

    protected void registerForTimeMessage(int i) {
	TimeScript scriptFor = (TimeScript) context.getScriptsManager().getScriptFor(ScriptsManager.ScriptType.TIME,
		item.getProperties().getName());
	scriptFor.init(context, item);
	item.registerForOneShotTask(() -> scriptFor.script(), i);
    }

}
