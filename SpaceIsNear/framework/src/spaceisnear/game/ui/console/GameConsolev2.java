package spaceisnear.game.ui.console;

import com.badlogic.gdx.scenes.scene2d.Stage;
import lombok.Getter;
import spaceisnear.game.ui.UIElement;

/**
 *
 * @author White Oak
 */
public class GameConsolev2 {

    @Getter private GameConsole console;
    private Stage consoleStage;

    public void init(Stage consoleStage) {
	this.consoleStage = consoleStage;
	initializeConsole(800, 0, 400, 600);
    }

    private void initializeConsole(int x, int y, int width, int height) {
	final spaceisnear.game.ui.TextField textField = new spaceisnear.game.ui.TextField();
	textField.setActivationListener(this::textFieldInConsole);
	textField.setBounds(x, y + height - textField.getPrefHeight(), width, textField.getPrefHeight());
	//
	console = new GameConsole(x, y, width, height, textField, consoleStage.getViewport());
	//
	consoleStage.addActor(console);
	consoleStage.addActor(textField);
    }

    public void draw() {
	consoleStage.draw();
	consoleStage.act();
    }

    private void textFieldInConsole(UIElement actor) {
	console.processInputedMessage();
	consoleStage.setKeyboardFocus(null);
    }
}
