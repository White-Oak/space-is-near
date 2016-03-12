package spaceisnear.editor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import java.io.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import lombok.*;
import spaceisnear.Utils;
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

    public String getName(int id) {
	return bundles[id].name;
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
		regions[i * tilesX + j].flip(false, true);
	    }
	}
	try {
	    bundles = ItemsReader.read();
	} catch (Exception ex) {
//	    Context.LOG.log(ex);
	    //@working
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
	try {
	    FileHandle fh = Gdx.files.local("additems.json");
	    File f = fh.file();
	    if (f.exists()) {
		f.delete();
	    }
	    f.createNewFile();
	    List<SaveLoadAction> slas = new LinkedList<>();
	    items.stream()
		    .sorted((item1, item2) -> item1.getId() - item2.getId())
		    .forEach(item -> slas.add(new SaveLoadAction(item)));
	    fh.writeString(Utils.GSON.toJson(slas), false);
	} catch (IOException ex) {
	    Logger.getLogger(ItemsHandler.class.getName()).log(Level.SEVERE, null, ex);
	}
    }

    public void load() {
	FileHandle fh = Gdx.files.local("additems.json");
	File f = fh.file();
	if (f.exists()) {
	    {
		SaveLoadAction[] slas = Utils.GSON.fromJson(fh.reader(), SaveLoadAction[].class);
		items.clear();
		actions.clear();
		for (SaveLoadAction sla : slas) {
		    final ItemProperty[] properties = sla.getProperties();
		    final Item item;
		    if (properties != null) {
			item = new Item(sla.getItemId(), sla.getX(), sla.getY(), Arrays.asList(properties));
		    } else {
			item = new Item(sla.getItemId(), sla.getX(), sla.getY());
		    }
		    items.add(item);
		}
	    }
	}
    }
}
