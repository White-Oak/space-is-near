package spaceisnear.game.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author White Oak
 */
public final class Button extends UIElement implements Hoverable {

    @Getter @Setter private String label;
    private static final int WIDTH_PADDING = 20, HEIGHT_PADDING = 15;
    private Color color = new Color(0xdce0e1ff);
    volatile private Color currentColor = color.cpy();
    private Color finalColor = new Color(0xacb0b1ff);
    private boolean entered = true, animation;

    @Override
    public void setColor(Color color) {
	this.color = color;
	currentColor = color;
	finalColor = color.cpy().sub(new Color(0x30303000));
    }

    public Button(String label) {
	this.label = label;
	initOvers();
    }

    public void initOvers() {
	addListener(new ClickListener() {

	    @Override
	    public void clicked(InputEvent event, float x, float y) {
		activated();
	    }

	});
	setHoverable(this);
    }

    @Override
    public void hoverAnimation(boolean hovered) {
	if (hovered) {
	    if (!currentColor.equals(finalColor)) {
		currentColor.sub(new Color(0x08080800));
	    }
	} else {
	    if (!currentColor.equals(color)) {
		currentColor.add(new Color(0x08080800));
	    }
	}
    }

    @Override
    public void paint(SpriteBatch batch) {
	ShapeRenderer renderer = getRenderer();
//	renderer.begin(ShapeRenderer.ShapeType.Line);
//	renderer.setColor(Color.BLACK);
//	renderer.rect(0, 0, getWidth(), getHeight());
//	renderer.end();
	renderer.setColor(currentColor);
	renderer.begin(ShapeRenderer.ShapeType.Filled);
	renderer.rect(0, 0, getWidth(), getHeight());
	renderer.end();
	batch.begin();
	font.setColor(Color.BLACK);
	font.draw(batch, label, getX() + WIDTH_PADDING, getY() + HEIGHT_PADDING + 2);
	batch.end();
    }

    @Override
    public float getPrefWidth() {
	return (font.getBounds(label).width + WIDTH_PADDING * 2);
    }

    @Override
    public float getPrefHeight() {
	return (font.getLineHeight() + HEIGHT_PADDING * 2);
    }

}
