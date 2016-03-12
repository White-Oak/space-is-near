/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceisnear.editor;

import java.util.LinkedList;
import java.util.List;
import lombok.*;

/**
 *
 * @author White Oak
 */
@Data @AllArgsConstructor public class Item {

    private int id;
    private int x, y;
    private List<ItemProperty> properties = new LinkedList<>();

    public Item(int id, int x, int y) {
	this.id = id;
	this.x = x;
	this.y = y;
    }

    public boolean add(ItemProperty e) {
	return properties.add(e);
    }

}
