/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceisnear.game.ui;

import lombok.Getter;

/**
 *
 * @author White Oak
 */
public class ContextMenuItem {

    @Getter private final String label;

    public ContextMenuItem(String label) {
	this.label = label;
    }
}
