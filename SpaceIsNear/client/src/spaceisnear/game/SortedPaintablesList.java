package spaceisnear.game;

import java.util.*;
import spaceisnear.game.components.client.PaintableComponent;

/**
 *
 * @author White Oak
 */
public class SortedPaintablesList extends AbstractList<PaintableComponent> {

    private final ArrayList<PaintableComponent> internalList = new ArrayList<>();

    // Note that add(E e) in AbstractList is calling this one
    @Override
    public void add(int position, PaintableComponent e) {
	internalList.add(e);
	Collections.sort(internalList, (e1, e2) -> Integer.compare(e1.getZLayer(), e2.getZLayer()));
    }

    @Override
    public PaintableComponent get(int i) {
	return internalList.get(i);
    }

    @Override
    public int size() {
	return internalList.size();
    }

}
