/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceisnear.game.layer;

import java.util.Arrays;
import java.util.List;
import spaceisnear.AbstractGameObject;
import spaceisnear.game.objects.GameObjectType;
import spaceisnear.game.objects.Position;
import spaceisnear.game.objects.items.ItemsArchive;
import spaceisnear.server.ServerContext;
import spaceisnear.server.objects.items.StaticItem;

/**
 *
 * @author White Oak
 */
public class AtmosphericLayer extends Layer {

    private final int[][] map;
    private final boolean[][] plitkas;
    /**
     * Two-bit variable where large bit is for top corners and little bit is for left corners.
     */
    private int tickState = 0b11;
    /**
     * Maximum pressure which one node can distribute over to other nodes.
     */
    private final static int MAX_CHANGE_OF_PRESSURE_PER_TICK = 8;
    public final static int PRESSURE_HARD_TO_BREATH = 60, PRESSURE_ENOUGH_TO_BREATH = 30;

    public AtmosphericLayer(int width, int height) {
	super(width, height);
	map = new int[width][height];
	plitkas = new boolean[width][height];
	for (int[] is : map) {
	    Arrays.fill(is, 100);
	}
    }

    private void tick() {
	//These are just four cycles to cycle every tick from different corner
	//The full cycle of corners is 
	//top left -> bottom right -> bottom left -> top right -> top left ->...
	if ((tickState & 0b10) != 0) {
	    for (int i = 0; i < getHeight(); i++) {
		if ((tickState & 0b1) != 0) {
		    //top left
		    for (int j = 0; j < getWidth(); j++) {
			processPressureIn(i, j);
		    }
		} else {
		    //top right
		    for (int j = getWidth() - 1; j > 0; j--) {
			processPressureIn(i, j);
		    }
		}
	    }
	} else {
	    for (int i = getHeight() - 1; i > 0; i--) {
		if ((tickState & 0b1) != 0) {
		    //bottom left
		    for (int j = 0; j < getWidth(); j++) {
			processPressureIn(i, j);
		    }
		} else {
		    //bottom right
		    for (int j = getWidth() - 1; j > 0; j--) {
			processPressureIn(i, j);
		    }
		}
	    }
	}
	tickState++;
	tickState &= 0b11;
    }

    private void processPressureIn(int x, int y) {
	int[] values = {
	    getPressure(x - 1, y), //left
	    getPressure(x, y - 1), //top
	    getPressure(x + 1, y), //right
	    getPressure(x, y + 1) //left
	};
	final int center = getPressure(x, y);
	if (center > 0) {
	    int sum = 0;
	    //Summing differences of pressures 
	    for (int i = 0; i < values.length; i++) {
		int j = values[i];
		if (j != -1 && j < center) {
		    //Adding difference between the center pressure and actual
		    sum += center - j;
		}
	    }
	    if (sum > 0) {
		//Setting each difference's part of total sum
		//i.e multiplier is what you need to multiply the sum on to get the difference
		float multipliers[] = new float[values.length];
		Arrays.fill(multipliers, -1f);
		for (int i = 0; i < values.length; i++) {
		    int j = values[i];
		    if (j != -1 && j < center) {
			multipliers[i] = (center - j) / (float) sum;
		    }
		}
		if (sum > MAX_CHANGE_OF_PRESSURE_PER_TICK) {
		    sum = MAX_CHANGE_OF_PRESSURE_PER_TICK;
		}
		if (sum > center) {
		    sum = center;
		}

		for (int i = 0; i < multipliers.length; i++) {
		    float f = multipliers[i];
		    if (f > 0) {
			values[i] += sum * f;
		    }
		}

		//Setting back the values
		setPressure(x - 1, y, values[0]); //left
		setPressure(x, y - 1, values[1]); //top
		setPressure(x + 1, y, values[2]); //right
		setPressure(x, y + 1, values[3]); //left
		setPressure(x, y, center - sum);//center
	    }
	}
    }

    public int getPressure(int x, int y) {
	try {
	    return map[x][y];
	} catch (ArrayIndexOutOfBoundsException e) {
	    return -1;
	}
    }

    public boolean isBreatheable(int x, int y) {
	return getPressure(x, y) > PRESSURE_ENOUGH_TO_BREATH;
    }

    private void setPressure(int x, int y, int value) {
	try {
	    map[x][y] = value;
	} catch (ArrayIndexOutOfBoundsException e) {
	}
    }

    public void setAirReacheable(int x, int y, boolean reacheable) {
	if (reacheable) {
	    if (getPressure(x, y) == -1) {
		//if wall is removed then the pressure in the node is calculated as mean value of surrounding nodes
		int[] values = {
		    getPressure(x - 1, y), //left
		    getPressure(x, y - 1), //top
		    getPressure(x + 1, y), //right
		    getPressure(x, y + 1) //left
		};
		int sum = 0;
		int amount = 0;
		for (int i = 0; i < values.length; i++) {
		    int j = values[i];
		    if (j != -1) {
			sum += j;
			amount++;
		    }
		}
		setPressure(x, y, amount > 0 ? sum / amount : -1);
	    }
	} else {
	    setPressure(x, y, -1);
	}
    }
//@working r71-78...

    /**
     * Checks if there are no plitka somewhere.
     *
     * @param context
     */
    private void checkHulls(ServerContext context) {
	for (boolean[] bs : plitkas) {
	    Arrays.fill(bs, false);
	}
	List<AbstractGameObject> objects = context.getObjects();
	for (AbstractGameObject abstractGameObject : objects) {
	    if (abstractGameObject.getType() == GameObjectType.ITEM) {
		StaticItem item = (StaticItem) abstractGameObject;
		if (item.getProperties().getId() == ItemsArchive.PLITKA_ID) {
		    Position position = abstractGameObject.getPosition();
		    plitkas[position.getX()][position.getY()] = true;
		}
	    }
	}
    }

    /**
     * Fills no-plitka areas with zero pressure.
     */
    private void recheckPressureForHulls() {
	for (int i = 0; i < plitkas.length; i++) {
	    boolean[] bs = plitkas[i];
	    for (int j = 0; j < bs.length; j++) {
		boolean b = bs[j];
		if (!b && getPressure(i, j) > 0) {
		    setPressure(i, j, 0);
		}
	    }
	}
    }

    private void processHulls(ServerContext context) {
	checkHulls(context);
	recheckPressureForHulls();
    }

    public void tickAtmosphere(ServerContext context) {
	processHulls(context);
	tick();
    }
}
