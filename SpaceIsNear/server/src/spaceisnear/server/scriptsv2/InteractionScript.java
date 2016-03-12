package spaceisnear.server.scriptsv2;

import lombok.Getter;
import spaceisnear.server.ServerContext;
import spaceisnear.server.objects.ServerGameObject;
import spaceisnear.server.objects.items.StaticItem;

public abstract class InteractionScript extends ItemScript {

    @Getter private ServerGameObject interactor;

    public final void init(ServerContext context, StaticItem item, ServerGameObject actor) {
	init(context, item);
	this.interactor = actor;
    }
}
