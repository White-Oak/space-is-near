package spaceisnear.game.components.server.context;

import java.io.Serializable;
import lombok.Data;

/**
 *
 * @author White Oak
 */
@Data public class ServerContextMenu implements Serializable {

    private final ServerContextSubMenu[] subMenus;
}
