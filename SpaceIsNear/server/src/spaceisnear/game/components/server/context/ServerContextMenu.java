package spaceisnear.game.components.server.context;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;
import lombok.*;
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
}
