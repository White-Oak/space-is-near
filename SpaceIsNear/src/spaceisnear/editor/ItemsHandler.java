package spaceisnear.editor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import java.io.*;
import java.util.*;
import lombok.*;
import spaceisnear.abstracts.Context;
import spaceisnear.game.GameContext;
import spaceisnear.game.objects.items.*;

/**
 *
 * @author White Oak
 */
public class ItemsHandler {

    @Getter private ItemBundle[] bundles;
    @Getter private final Texture sprites;
    @Getter private final TextureRegion[] regions;
    @Getter private final ArrayList<Item> items = new ArrayList<>();
    @Getter private final ArrayList<MapAction> actions = new ArrayList<>();
    @Getter @Setter private MapAction currentAction;
    public final static ItemsHandler HANDLER = new ItemsHandler();
    private final HashMap<String, Integer> ids = new HashMap<>();

    public int getIdByName(String name) {
	return ids.get(name);
    }

    public ItemsHandler() {
	sprites = new Texture(Gdx.files.classpath("res").child("sprites.png"));
	int tilesX = sprites.getWidth() / GameContext.TILE_WIDTH;
	int tilesY = sprites.getHeight() / GameContext.TILE_HEIGHT;
	regions = new TextureRegion[tilesY * tilesX];
	for (int i = 0; i < tilesY; i++) {
	    for (int j = 0; j < tilesX; j++) {
		regions[i * tilesX + j] = new TextureRegion(sprites,
			j * GameContext.TILE_WIDTH, i * GameContext.TILE_HEIGHT,
			GameContext.TILE_WIDTH, GameContext.TILE_HEIGHT);
	    }
	}
	try {
	    bundles = ItemsReader.read();
	} catch (Exception ex) {
	    Context.LOG.log(ex);
	}
	for (int i = 0; i < bundles.length; i++) {
	    ItemBundle itemBundle = bundles[i];
	    ids.put(itemBundle.name, i);
	}
    }

    public TextureRegion getTextureRegion(int id) {
	return regions[bundles[id].imageIds[0]];
    }

    public void addCurrentAction() {
	currentAction.act(items);
	actions.add(currentAction);
	currentAction = null;
    }

    public void save() {
	FileHandle fh = Gdx.files.local("additems.txt");
	if (fh.exists()) {
	    fh.delete();
	}
	for (Item item : items) {
	    fh.writeString("addItem {" + bundles[item.getId()].name + ", " + item.getX() + ", " + item.getY() + "}" + System.lineSeparator(),
		    true);
	}
    }

    public void load() {
	FileHandle fh = Gdx.files.local("additems.txt");
	if (fh.exists()) {
	    InputStream read = fh.read();
	    Loader loader = new Loader();
	    try {
		loader.addItems(read);
	    } catch (IOException ex) {
		Context.LOG.log(ex);
	    }
	    items.clear();
	    actions.clear();
	    items.addAll(loader.getItems());
	}
    }
}
