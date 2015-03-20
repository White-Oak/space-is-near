package spaceisnear.game.components.server.context;

import java.io.Serializable;
import lombok.*;

/**
 *
 * @author White Oak
 */
@AllArgsConstructor @Getter @NoArgsConstructor(access = AccessLevel.PRIVATE) public class ServerContextSubMenu implements Serializable {

    private String[] actions;
    private int itemId;
    private int defaults;
}
