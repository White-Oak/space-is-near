/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceisnear.game.ui;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.*;
import lombok.Getter;

/**
 *
 * @author White Oak
 */
public final class TextField extends UIElement {

    private StringBuilder text = new StringBuilder();
    private int currentPosition;
    private Color textColor;
    private final InputListener inputListener;
    @Getter private boolean focused;

    public TextField() {
	addCaptureListener(inputListener = new InputListener() {
	    @Override
	    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
		Stage stage = getStage();
		if (stage != null) {
		    stage.setKeyboardFocus(TextField.this);
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
		switch (keycode) {
		    case Input.Keys.ENTER:
			activated();
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
		return true;
	    }

	    @Override
	    public boolean keyTyped(InputEvent event, char character) {
		if (font.containsCharacter(character)) {
		    addCharacter(character);
		}
		return true;
	    }

	    @Override
	    public boolean keyUp(InputEvent event, int keycode) {
		return true;
	    }
	});
	setHeight(getPrefHeight());
	setWidth(getPrefWidth());
    }

    @Override
    public float getPrefHeight() {
	return font.getLineHeight() + 4;
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
    public void paint(SpriteBatch batch) {
	ShapeRenderer renderer = getRenderer();
	focused = getStage().getKeyboardFocus() == this;
	int start = 0;
	int end = text.length();
	int startingXText = 0;
	if (font.getBounds(text).width > getWidth() - WIDTH_PADDING * 2) {
	    end = text.length();
	    start = end - 1;
	    while (start > 0 && font.getBounds(text, start, end).width < getWidth() - WIDTH_PADDING * 2) {
		start--;
	    }
	    startingXText = (int) -font.getBounds(text, 0, start).width;
	}
	//
	renderer.setProjectionMatrix(batch.getProjectionMatrix());
	renderer.begin(ShapeRenderer.ShapeType.FilledRectangle);
	renderer.setColor(Color.WHITE);
	renderer.filledRect(0, 0, getWidth(), getHeight());
	renderer.end();
	renderer.begin(ShapeRenderer.ShapeType.Rectangle);
	renderer.setColor(Color.BLACK);
	renderer.rect(0, 0, getWidth() + 1, getHeight());
	renderer.end();
	//cursor
	renderer.begin(ShapeRenderer.ShapeType.Line);
	if (focused) {
	    final float x = 10 + font.getBounds(text, 0, currentPosition).width + startingXText;
	    renderer.line(x, 3, x, font.getLineHeight() + 2);
	    renderer.line(x + 1, 3, x + 1, font.getLineHeight() + 2);
	}
	renderer.end();
	batch.begin();
	font.setColor(Color.BLACK);
	font.draw(batch, text.subSequence(start, end), WIDTH_PADDING + getX(), 3 + getY());
	batch.end();
    }
    private static final int WIDTH_PADDING = 10;

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

    public void clear() {
	text.setLength(0);
    }
}
