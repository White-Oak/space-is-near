package spaceisnear.game.components.server.context;

import java.io.Serializable;
import lombok.*;

/**
 *
 * @author White Oak
 */
@AllArgsConstructor @Getter @Setter @NoArgsConstructor(access = AccessLevel.PRIVATE) public class ServerContextMenu implements Serializable {

    private ServerContextSubMenu[] subMenus;
}
