package spaceisnear.game.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import java.util.logging.Level;
import java.util.logging.Logger;
import lombok.*;

/**
 *
 * @author White Oak
 */
public abstract class UIElement extends Actor {

    protected static final BitmapFont font = new BitmapFont(Gdx.files.classpath("segoe_ui.fnt"), true);
    @Getter(AccessLevel.PROTECTED) private final static ShapeRenderer renderer = new ShapeRenderer();
    private final OrthographicCamera camera = new OrthographicCamera(1200, 600);
    @Setter private Hoverable hoverable;
    private boolean hovered;

    @Getter @Setter private ActivationListener activationListener;

    public UIElement() {
	camera.setToOrtho(true);
	camera.update();
	renderer.setProjectionMatrix(camera.combined);
	addListener(new ClickListener() {

	    @Override
	    public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
		hovered = true;
	    }

	    @Override
	    public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
		hovered = false;
	    }

	});
	new Thread(() -> {
	    while (true) {
		if (hoverable != null) {
		    hoverable.hoverAnimation(hovered);
		}
		try {
		    Thread.sleep(10L);
		} catch (InterruptedException ex) {
		    Logger.getLogger(UIElement.class.getName()).log(Level.SEVERE, null, ex);
		}
	    }
	}).start();
    }

    protected void activated() {
	if (activationListener != null) {
	    activationListener.componentActivated(this);
	}
    }

    public float getPrefHeight() {
	return 0;
    }

    public float getPrefWidth() {
	return 0;
    }

    @Override
    public float getWidth() {
	if (super.getWidth() == 0) {
	    setWidth(getPrefWidth());
	}
	return super.getWidth();
    }

    @Override
    public float getHeight() {
	if (super.getHeight() == 0) {
	    setHeight(getPrefHeight());
	}
	return super.getHeight();
    }

    @Override
    public final void draw(SpriteBatch batch, float parentAlpha) {
	batch.end();
	renderer.translate(getX(), getY(), 0);
	paint(batch);
	renderer.translate(-getX(), -getY(), 0);
	batch.begin();
    }

    public abstract void paint(SpriteBatch batch);

}
