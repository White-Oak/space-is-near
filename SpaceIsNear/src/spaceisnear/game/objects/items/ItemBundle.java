/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 *//*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceisnear.game.objects.items;

import lombok.Data;

/**
 *
 * @author White Oak
 */
public class ItemBundle {

    public String name;
    public Size size;
    public Type type;
    public boolean blockingPath;
    public boolean blockingAir;
    public int[] imageIds;
    public boolean stuckedByAddingFromScript;
    public String description;

    public Property[] defaultProperties; //script names reserved@Data

    @Data public class Property {

	private String name;
	private Object value;

    }
}
