package spaceisnear.game.ui;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import lombok.Getter;
import lombok.Setter;

/**
 * Grid-like selection
 *
 * @author White Oak
 */
public class GridList extends UIElement {

    @Getter @Setter private int rows, columns;

    @Override
    public void paint(SpriteBatch batch) {
    }

}
