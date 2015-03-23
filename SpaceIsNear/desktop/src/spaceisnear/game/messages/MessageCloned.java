package spaceisnear.game.messages;

import spaceisnear.game.GameContext;

@Deprecated public class MessageCloned extends Message implements NetworkableMessage {

    public int amount;

    public MessageCloned() {
	super(MessageType.CLONED);
    }

    @Override
    public void processForClient(GameContext context) {
//	AbstractGameObject get = context.getObjects().get(context.getObjects().size() - 1);
//	for (int i = 0; i < amount; i++) {
//	    StaticItem item = ItemsArchive.itemsArchive.clone((StaticItem) get);
////	    context.addObject(item);
//	    LoadingScreen.CURRENT_AMOUNT++;
//	}
    }

}
