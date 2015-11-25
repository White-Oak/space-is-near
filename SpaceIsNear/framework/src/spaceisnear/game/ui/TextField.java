/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceisnear.game.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.*;
import lombok.Getter;

/**
 *
 * @author White Oak
 */
public final class TextField extends UIElement implements Hoverable {

    private StringBuilder text = new StringBuilder();
    private int currentPosition;
    private Color textColor;
    @Getter private boolean focused;
    private static final int HEIGHT_PADDING = 2;
    private static final int WIDTH_PADDING = 10;
    private int keycode;
    private long lastTimeActed;
    private final static long DELTA_ACTED = 100L;
    private Color borderColor = new Color(0x0);
    volatile private Color currentColor = borderColor.cpy();
    private Color finalColor = new Color(0xff);

    @Override
    public void hoverAnimation(boolean hovered) {
	if (hovered) {
	    if (!currentColor.equals(finalColor)) {
		currentColor.add(new Color(0x10));
	    }
	} else if (!currentColor.equals(borderColor)) {
	    currentColor.sub(new Color(0x10));
	}
    }

    public void setBorderColor(Color color) {
	this.borderColor = color;
	currentColor = color;
	finalColor = color.cpy().sub(new Color(0x30303000));
    }

    public TextField() {
	this("");
    }

    public TextField(CharSequence text) {
	init();
	this.text.append(text);
    }

    private void init() {
	addListener(new InputListener() {
	    @Override
	    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
		System.out.println("EBAX!");
		Stage stage = getStage();
		if (stage != null) {
		    stage.setKeyboardFocus(TextField.this);
		}
		int currentX = WIDTH_PADDING;
		currentPosition = 0;
		while (currentX < x && currentPosition < text.length()) {
		    currentPosition++;
		    currentX += getLineWidth(text.subSequence(0, currentPosition));
		}
		return true;
	    }

	    @Override
	    public void touchDragged(InputEvent event, float x, float y, int pointer) {
//		super.touchDragged(event, x, y, pointer);
//		lastBlink = 0;
//		cursorOn = false;
//		setCursorPosition(x);
//		hasSelection = true;
	    }

	    @Override
	    public boolean keyDown(InputEvent event, int keycode) {
		TextField.this.keycode = keycode;
		lastTimeActed = 0;
		return true;
	    }

	    @Override
	    public boolean keyTyped(InputEvent event, char character) {
//		if (font.containsCharacter(character)) {
		addCharacter(character);
//		}
		return true;
	    }

	    @Override
	    public boolean keyUp(InputEvent event, int keycode) {
		TextField.this.keycode = 0;
		return true;
	    }
	});
	setHeight(getPrefHeight());
	setWidth(getPrefWidth());
	setHoverable(this);
    }

    public void checkKeys() {
	if (keycode != 0) {
	    if (System.currentTimeMillis() - lastTimeActed > DELTA_ACTED) {
		switch (keycode) {
		    case Input.Keys.ENTER:
			activated();
			keycode = 0;
			break;
		    case Input.Keys.BACKSPACE:
			removeCharacter();
			break;
		    case Input.Keys.LEFT:
			currentPosition--;
			if (currentPosition < 0) {
			    currentPosition = 0;
			}
			break;
		    case Input.Keys.RIGHT:
			currentPosition++;
			if (currentPosition > text.length()) {
			    currentPosition = text.length();
			}
			break;
		}
		lastTimeActed = System.currentTimeMillis();
	    }
	}
    }

    @Override
    public float getPrefHeight() {
	return font.getLineHeight() + HEIGHT_PADDING * 2;
    }

    @Override
    public float getPrefWidth() {
	return 200;
    }

    public void addCharacter(char c) {
	if (currentPosition >= text.length()) {
	    text.append(c);
	} else {
	    text.insert(currentPosition, c);
	}
	currentPosition++;
    }

    public void removeCharacter() {
	if (currentPosition > 0) {
	    text.deleteCharAt(currentPosition - 1);
	    currentPosition--;
	}
    }

    @Override
    public void paint(Batch batch) {
	checkKeys();
	ShapeRenderer renderer = getRenderer();
	focused = getStage().getKeyboardFocus() == this;
	int start = 0;
	int end = text.length();
	int startingXText = 0;
	if (getLineWidth(text) > getWidth() - WIDTH_PADDING * 2) {
	    end = text.length();
	    start = end - 1;
	    while (start > 0 && getLineWidth(text.substring(start, end)) < getWidth() - WIDTH_PADDING * 2) {
		start--;
	    }
	    startingXText = (int) -getLineWidth(text.substring(0, start));
	}
	//
	renderer.setProjectionMatrix(batch.getProjectionMatrix());
	renderer.begin(ShapeRenderer.ShapeType.Filled);
	renderer.setColor(backgroundColor);
	renderer.rect(0, 0, getWidth(), getHeight());
	renderer.end();
//	renderer.begin(ShapeRenderer.ShapeType.Line);
//	renderer.setColor(Color.WHITE);
//	renderer.rect(0, 0, getWidth(), getHeight());
//	renderer.end();\
	Gdx.gl.glEnable(GL20.GL_BLEND);
	Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
	renderer.begin(ShapeRenderer.ShapeType.Line);
	renderer.setColor(currentColor);
	renderer.rect(0, 0, getWidth() + 1, getHeight());
	renderer.end();
	Gdx.gl.glDisable(GL20.GL_BLEND);
	//cursor
	renderer.begin(ShapeRenderer.ShapeType.Line);
	font.setColor(Color.BLACK);
	if (focused) {
	    final float x = 10 + getLineWidth(text.substring(0, currentPosition)) + startingXText;
	    renderer.line(x, 3, x, font.getLineHeight() + 2);
	    renderer.line(x + 1, 3, x + 1, font.getLineHeight() + 2);
	}
	renderer.end();
	batch.begin();
	font.setColor(Color.BLACK);
	font.draw(batch, text.subSequence(start, end), WIDTH_PADDING + getX(), 3 + getY());
	batch.end();
    }
    private final Color backgroundColor = new Color(0xecf0f1ff);

    public void setTextColor(Color textColor) {
	this.textColor = textColor;
    }

    public String getText() {
	return text.toString();
    }

    public void setText(String string) {
	text = new StringBuilder(string);
	currentPosition = text.length();
    }

    @Override
    public void clear() {
	text.setLength(0);
    }
}
