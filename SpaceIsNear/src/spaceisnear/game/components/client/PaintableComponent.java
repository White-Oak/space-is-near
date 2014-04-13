/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceisnear.game.components.client;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import spaceisnear.game.GameContext;
import spaceisnear.game.components.Component;
import spaceisnear.game.components.ComponentType;

public abstract class PaintableComponent extends Component {

    private final static OrthographicCamera camera = new OrthographicCamera();

    static {
	camera.setToOrtho(true);
    }

    protected PaintableComponent(ComponentType type) {
	super(type);
    }

    public int getX() {
	return getPosition().getX();
    }

    public int getY() {
	return getPosition().getY();
    }

    public int getDelayX() {
	return getPositionComponent().getDelayX();
    }

    public int getDelayY() {
	return getPositionComponent().getDelayY();
    }

    public abstract void paintComponent(SpriteBatch batch, int x, int y);

    public final void paint(SpriteBatch batch) {
	final GameContext name = (GameContext) getContext();
	if (name.getCameraMan().belongsToCamera(getPosition(), name)) {
	    int xto, yto;
//	    if (GameContext.TILE_WIDTH == 32) {
//		xto = (getX() << 5) - getDelayX();
//		yto = (getY() << 5) - getDelayY();
//	    } else {
	    xto = getX() * GameContext.TILE_WIDTH - getDelayX();
	    yto = getY() * GameContext.TILE_HEIGHT - getDelayY();
//	    }
	    paintComponent(batch, xto, yto);
	}
    }
}
