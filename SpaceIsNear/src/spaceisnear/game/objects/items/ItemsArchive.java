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
	int tilesX = sprites.getWidth() / GameContext.TILE_WIDTH;
	int tilesY = sprites.getHeight() / GameContext.TILE_HEIGHT;
	TextureRegion[][] split = TextureRegion.split(sprites, GameContext.TILE_WIDTH, GameContext.TILE_HEIGHT);
	regions = new TextureRegion[split.length * split[0].length];
	int index = 0;
	for (TextureRegion[] textureRegions : split) {
	    for (TextureRegion textureRegion : textureRegions) {
		regions[index++] = textureRegion;
	    }
	}
//	for (int i = 0; i < tilesY; i++) {
//	    for (int j = 0; j < tilesX; j++) {
//		regions[i * tilesX + j] = new TextureRegion(sprites,
//			j * GameContext.TILE_WIDTH, i * GameContext.TILE_HEIGHT,
//			GameContext.TILE_WIDTH, GameContext.TILE_HEIGHT);
//	    }
//	}
    }

    public TextureRegion getTextureRegion(int id) {
	return regions[id];
    }

    public spaceisnear.game.objects.items.StaticItem getNewItem(int id, GameContext serverContext) {
	return new spaceisnear.game.objects.items.StaticItem(serverContext, id);
    }
}
