package spaceisnear.game.ui;

import com.badlogic.gdx.graphics.g2d.Batch;
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
    public void paint(Batch batch) {
    }

}
