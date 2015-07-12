package spaceisnear.server.objects.items;

import lombok.Getter;
import lombok.Setter;
import spaceisnear.game.components.ItemPropertiesComponent;
import spaceisnear.game.components.PositionComponent;
import spaceisnear.game.components.server.VariablePropertiesComponent;
import spaceisnear.game.objects.GameObjectType;
import spaceisnear.game.ui.Position;
import spaceisnear.server.ServerContext;
import spaceisnear.server.objects.ServerGameObject;

/**
 *
 * @author white_oak
 */
public class StaticItem extends ServerGameObject {

    @Getter private final ItemPropertiesComponent properties;
    @Getter private final VariablePropertiesComponent variableProperties;
    @Getter @Setter private int playerId;

    public StaticItem(ServerContext context, Position p, int itemId) {
	super(GameObjectType.ITEM, context);
	PositionComponent pc = new PositionComponent(p);
	properties = new ItemPropertiesComponent(itemId);
	variableProperties = new VariablePropertiesComponent();
	addComponents(pc, properties, variableProperties);
    }

    public StaticItem(ServerContext context, int itemId) {
	this(context, new Position(0, 0), itemId);
    }
}
