/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceisnear.game.components.inventory;

/**
 * Sizes of items.
 *
 * @author White Oak
 */
public enum Size {

    /**
     * Can be placed everywhere.
     */
    SMALL,
    /**
     * Cannnot be placed in small bags and pockets.
     */
    MEDIUM,
    /**
     * Cannnot be placed in all bags and pockets.
     */
    BIG,
    /**
     * Cannot be pickep up, only pulled.
     */
    TOO_BIG;
}
