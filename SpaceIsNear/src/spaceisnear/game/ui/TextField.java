/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceisnear.game.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.*;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author White Oak
 */
public final class TextField extends Actor {

    private StringBuilder text = new StringBuilder();
    private int currentPosition;
    private final BitmapFont font = new BitmapFont(Gdx.files.classpath("default.fnt"), true);
    private Color textColor;
    private final InputListener inputListener;
    @Setter private UIListener UIListener;
    @Getter @Setter private boolean focused;

    public TextField() {
	addCaptureListener(inputListener = new InputListener() {
	    @Override
	    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
		Stage stage = getStage();
		if (stage != null) {
		    stage.setKeyboardFocus(TextField.this);
		    setFocused(true);
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
			if (UIListener != null) {
			    UIListener.componentActivated(TextField.this);
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
	camera.setToOrtho(true);
	camera.update();
	renderer.setProjectionMatrix(camera.combined);
    }

    public float getPrefHeight() {
	return font.getLineHeight() + 4;
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

    private final ShapeRenderer renderer = new ShapeRenderer();
    private final OrthographicCamera camera = new OrthographicCamera(1200, 600);

    @Override
    public void draw(SpriteBatch batch, float parentAlpha) {
	batch.end();
	renderer.setProjectionMatrix(batch.getProjectionMatrix());
	renderer.begin(ShapeRenderer.ShapeType.FilledRectangle);
	renderer.setColor(Color.WHITE);
	float y = getY();
	renderer.filledRect(getX(), y, getWidth(), getHeight());
	renderer.end();
	renderer.begin(ShapeRenderer.ShapeType.Line);
	renderer.setColor(Color.BLACK);
	renderer.line(getX(), y, getX() + getWidth(), y);
	//cursor
	if (focused) {
	    final float x = getX() + 10 + font.getBounds(text).width;
	    renderer.line(x, y, x, y + font.getLineHeight());
	    renderer.line(x + 1, y, x + 1, y + font.getLineHeight());
	}
	renderer.end();
	batch.begin();
	font.setColor(Color.BLACK);
	font.draw(batch, text, getX() + 10, y + 2);
    }

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
