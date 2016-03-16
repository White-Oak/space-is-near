package spaceisnear.server.objects;

import lombok.Getter;
import spaceisnear.game.components.*;
import spaceisnear.game.components.inventory.InventoryComponent;
import spaceisnear.game.components.server.*;
import spaceisnear.game.objects.GameObjectType;
import spaceisnear.game.ui.Position;
import spaceisnear.server.ServerContext;
import spaceisnear.server.objects.items.ItemAdder;
import spaceisnear.server.objects.items.ServerItemsArchive;
import spaceisnear.server.objects.items.StaticItem;

/**
 * @author LPzhelud
 */
public class Player extends ServerGameObject {

    @Getter private final HealthComponent healthComponent;

    public Player(ServerContext context) {
	super(GameObjectType.PLAYER, context);
	PositionComponent pc = new PositionComponent(0, 0);
	healthComponent = new HealthComponent();
	addComponents(pc, healthComponent, new BreathingComponent(),
		new InventoryComponent(), new PlayerControllableComponent(), new NameComponent(),
		new VariablePropertiesComponent(), new PlayerChunkObserverComponent());
    }

    public String getNickname() {
	return getNameComponent().getNickname();
    }

    public void setNickname(String nickname) {
	getNameComponent().setNickname(nickname);
    }

    private NameComponent getNameComponent() {
	for (Component component : getComponents()) {
	    if (component.getType() == ComponentType.NAME) {
		return (NameComponent) component;
	    }
	}
	return null;
    }

    public InventoryComponent getInventoryComponent() {
	for (Component component : getComponents()) {
	    if (component.getType() == ComponentType.INVENTORY) {
		return (InventoryComponent) component;
	    }
	}
	return null;
    }

    public void initPlayer(String nickname, String profession) {
	setNickname(nickname);
	getPosition().setX(1);
	getPosition().setY(1);
	//		list.add(getObjectMessaged(player));

	StaticItem ear = addToPlayerItem("ear_radio", "ear", this);
	//		list.add(getObjectMessaged(ear));

	StaticItem right_pocket = addToPlayerItem("pda", "right pocket", this);
	//		list.add(getObjectMessaged(right_pocket));

	StaticItem id = addToPlayerItem("id", "id", this);

	id.getVariableProperties().setProperty("name", nickname);
	id.getVariableProperties().setProperty("profession", profession);
	//		list.add(getObjectMessaged(id));
	//		return list;
    }

    private StaticItem addToPlayerItem(String itemName, String nameOfInventorySlot, Player player) {
	final ServerContext context = getContext();
	final ServerItemsArchive itemsArchive = ServerItemsArchive.ITEMS_ARCHIVE;
	int idByName = itemsArchive.getIdByName(itemName);
	StaticItem item = ItemAdder.addItem(new Position(-1, -1), idByName, context, null);
	item.setPlayerId(player.getId());
	player.getInventoryComponent().getSlots().get(nameOfInventorySlot).setItemId(item.getId());
	return item;
    }
}
