/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceisnear.game.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.Widget;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

/**
 *
 * @author White Oak
 */
public final class TextField extends Widget {

    private StringBuilder text = new StringBuilder();
    private int currentPosition;
    private final BitmapFont font = new BitmapFont(Gdx.files.classpath("default.fnt"), true);
    private Color textColor;
    private final InputListener inputListener;

    public TextField() {
	addListener(inputListener = new ClickListener() {
	    @Override
	    public void clicked(InputEvent event, float x, float y) {
//				if (getTapCount() > 1) setSelection(0, text.length());
	    }

	    @Override
	    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
		System.out.println("truee");
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
		return true;
	    }

	    @Override
	    public boolean keyTyped(InputEvent event, char character) {
		System.out.println("hey");
		if (font.containsCharacter(character)) {
		    addCharacter(character);
		}
		addCharacter(character);
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

    @Override
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
	renderer.begin(ShapeRenderer.ShapeType.FilledRectangle);
	renderer.setColor(Color.WHITE);
	final float y = Gdx.graphics.getHeight() - getY() - getPrefHeight();
	renderer.filledRect(getX(), y, getWidth(), getHeight());
	renderer.end();
	renderer.begin(ShapeRenderer.ShapeType.Line);
	renderer.setColor(Color.BLACK);
	renderer.line(getX(), y, getX() + getWidth(), y);
	renderer.end();
//	batch.setProjectionMatrix(camera.combined);
	batch.begin();
	font.setColor(Color.BLACK);
	font.draw(batch, "hey man", getX() + 10, y + 2);
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
