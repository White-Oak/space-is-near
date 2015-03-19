package spaceisnear.game.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import java.util.ArrayList;
import java.util.List;
import lombok.Setter;

/**
 *
 * @author White Oak
 */
public class StatusBar extends UIElement {

    private final static int BAR_HEIGHT = (int) font.getLineHeight() + 10;
    @Setter private Color color = Color.WHITE;
    @Setter private Color textColor = Color.BLACK;
    private final List<CharSequence> statuses = new ArrayList<>();

    public void setStatus(int index, CharSequence status) {
	while (index >= statuses.size()) {
	    statuses.add("");
	}
	statuses.set(index, status);
    }

    @Override
    public void paint(SpriteBatch batch) {
	ShapeRenderer renderer = getRenderer();
	renderer.setColor(color);
	renderer.begin(ShapeRenderer.ShapeType.Filled);
	{
	    renderer.rect(0, 0, getWidth(), getHeight());
	}
	renderer.end();

	StringBuilder sb = new StringBuilder();
	statuses.forEach(status -> sb.append(status).append(" ; "));
	batch.begin();
	{
	    font.setColor(textColor);
	    font.draw(batch, sb, getX() + 10, getY() + 5);
	}
	batch.end();
    }

    @Override
    public float getPrefHeight() {
	return BAR_HEIGHT;
    }
}
