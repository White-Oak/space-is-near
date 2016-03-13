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
    public boolean blockingLight;
    public int[] imageIds;
    public boolean stuckedByAddingFromScript;
    public String description;

    public Property[] defaultProperties;

    public int z;

    @Data public class Property {

	private String name;
	private Object value;

    }
}
