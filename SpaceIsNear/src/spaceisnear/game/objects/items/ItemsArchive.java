/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceisnear.game.objects.items;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import lombok.Getter;
import spaceisnear.abstracts.ItemsArchivable;
import spaceisnear.game.GameContext;

/**
 *
 * @author White Oak
 */
public class ItemsArchive extends ItemsArchivable {

    @Getter private final Texture sprites;
    @Getter private final TextureRegion[] regions;
    public static ItemsArchive itemsArchive;

    public ItemsArchive(ItemBundle[] bundles) {
	super(bundles);
	sprites = new Texture(Gdx.files.classpath("res").child("sprites.png"));
	TextureRegion[][] split = TextureRegion.split(sprites, GameContext.TILE_WIDTH, GameContext.TILE_HEIGHT);
	regions = new TextureRegion[split.length * split[0].length];
	int index = 0;
	for (TextureRegion[] textureRegions : split) {
	    for (TextureRegion textureRegion : textureRegions) {
		textureRegion.flip(false, true);
		regions[index++] = textureRegion;
	    }
	}
    }

    public TextureRegion getTextureRegion(int id) {
	return regions[id];
    }

    public StaticItem getNewItem(int id) {
	return new spaceisnear.game.objects.items.StaticItem(id);
    }

    public StaticItem clone(StaticItem item) {
	return getNewItem(item.getProperties().getId());
    }
}
