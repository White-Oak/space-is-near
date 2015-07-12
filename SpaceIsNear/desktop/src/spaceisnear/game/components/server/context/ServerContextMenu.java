package spaceisnear.game.components.server.context;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;
import lombok.*;
import spaceisnear.game.GameContext;
import spaceisnear.game.messages.MessageActionChosen;
import spaceisnear.game.messages.MessageToSend;
import spaceisnear.game.ui.ActivationListener;
import spaceisnear.game.ui.context.ContextMenuItemable;

/**
 *
 * @author White Oak
 */
@AllArgsConstructor @Getter @Setter @NoArgsConstructor(access = AccessLevel.PRIVATE) public class ServerContextMenu implements Serializable {

    private ServerContextSubMenu[] subMenus;

    public List<List<ContextMenuItemable>> getLists() {
	return Arrays.stream(subMenus)
		.map(subMenu -> Arrays.stream(subMenu.getActions())
			.map(action -> (ContextMenuItemable) () -> action)
			.collect(Collectors.toList()))
		.collect(Collectors.toList());
    }

    public List<ActivationListener> getListeners(GameContext context) {
	List<ActivationListener> list = new ArrayList<>();
	for (int i = 0; i < subMenus.length; i++) {
	    final ServerContextSubMenu subMenu = subMenus[i];
	    final int fi = i;
	    final int itemId = subMenu.getItemId();
	    list.add(element -> {
		MessageActionChosen messageActionChosen;
		//@working check if this is not bugged
		messageActionChosen = new MessageActionChosen(fi, itemId);
		MessageToSend messageToSend = new MessageToSend(messageActionChosen);
		context.sendDirectedMessage(messageToSend);
		context.menuWantsToHide();
	    });
	}
	return list;
    }
}
