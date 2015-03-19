package spaceisnear.game.components.server.context;

import java.io.Serializable;
import lombok.Data;

/**
 *
 * @author White Oak
 */
@Data public class ServerContextSubMenu implements Serializable {

    private final String[] actions;
    private final int itemId;
    private final int defaults;
}
