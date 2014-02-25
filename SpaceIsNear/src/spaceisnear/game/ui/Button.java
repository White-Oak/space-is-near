package spaceisnear.game.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author White Oak
 */
public final class Button extends UIElement {

    @Getter @Setter private String label;
    private static final int WIDTH_PADDING = 5, HEIGHT_PADDING = 2;

    public Button(String label) {
	this.label = label;
	addListener(new InputListener() {

	    @Override
	    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
		activated();
		return false;
	    }
	});
	setHeight(getPrefHeight());
	setWidth(getPrefWidth());
    }

    @Override
    public void paint(SpriteBatch batch) {
	ShapeRenderer renderer = getRenderer();
	renderer.begin(ShapeRenderer.ShapeType.Rectangle);
	renderer.setColor(Color.BLACK);
	renderer.rect(0, 0, getWidth(), getHeight());
	renderer.end();
	batch.begin();
	font.setColor(Color.BLACK);
	font.draw(batch, label, getX() + WIDTH_PADDING, getY() + HEIGHT_PADDING);
	batch.end();
    }

    @Override
    public float getPrefWidth() {
	return font.getBounds(label).width + WIDTH_PADDING * 2;
    }

    @Override
    public float getPrefHeight() {
	return font.getLineHeight() + HEIGHT_PADDING * 2;
    }

}
