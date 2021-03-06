package spaceisnear.game.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
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
    private Color finalColor = new Color(0xacb0b1ff);

    @Override
    public void setColor(Color color) {
	this.color = color;
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
    public void hovered() {
	addAction(Actions.color(finalColor, 0.25f));
    }

    @Override
    public void unhovered() {
	addAction(Actions.color(color, 0.25f));
    }

    @Override
    public void paint(Batch batch) {
	ShapeRenderer renderer = getRenderer();
//	renderer.begin(ShapeRenderer.ShapeType.Line);
//	renderer.setColor(Color.BLACK);
//	renderer.rect(0, 0, getWidth(), getHeight());
//	renderer.end();
	renderer.setColor(getColor());
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
	return (getLineWidth(label) + WIDTH_PADDING * 2);
    }

    @Override
    public float getPrefHeight() {
	return (font.getLineHeight() + HEIGHT_PADDING * 2);
    }

}
