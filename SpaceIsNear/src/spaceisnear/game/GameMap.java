/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceisnear.game;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import spaceisnear.game.layer.TiledLayer;

/**
 *
 * @author LPzhelud
 */
@RequiredArgsConstructor public class GameMap {

    @Getter private final TiledLayer tiledLayer;
}
