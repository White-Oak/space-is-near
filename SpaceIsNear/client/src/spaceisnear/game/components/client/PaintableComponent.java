/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceisnear.game.components.client;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import lombok.Getter;
import lombok.Setter;
import spaceisnear.game.GameContext;
import spaceisnear.game.components.Component;
import spaceisnear.game.components.ComponentType;
import spaceisnear.game.ui.Position;

public abstract class PaintableComponent extends Component {

    private final static OrthographicCamera camera = new OrthographicCamera();
    @Getter @Setter private boolean ownerDestroyed;
    @Getter @Setter private int zLayer;

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
	final Position position = getPosition();
	if (position != null) {
	    final GameContext context = (GameContext) getContext();
	    if (context.getEngine().getCore().getCameraMan().belongsToCamera(position, context.getEngine())) {
		int xto, yto;
		xto = getX() * GameContext.TILE_WIDTH - getDelayX();
		yto = getY() * GameContext.TILE_HEIGHT - getDelayY();
		paintComponent(batch, xto, yto);
	    }
	}
    }

    @Override
    public boolean isPaintable() {
	return true;
    }

}
