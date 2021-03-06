// Generated by delombok at Sun Dec 01 13:28:22 MSK 2013
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceisnear.game.objects;

import java.util.*;
import lombok.*;
import spaceisnear.abstracts.AbstractGameObject;
import spaceisnear.game.GameContext;
import spaceisnear.game.components.Component;

/**
 * @author LPzhelud
 */
@RequiredArgsConstructor public abstract class ClientGameObject extends AbstractGameObject {

    @Getter private int id = -1;
    @Getter @Setter private boolean destroyed = false;
    @NonNull @Getter private final GameObjectType type;
    @Getter private GameContext context;

    public void setId(int id) {
	if (this.id == -1) {
	    this.id = id;
	    getComponents().forEach((component) -> {
		component.setOwnerId(id);
	    });
	} else {
	    System.out.println("[WARNING]: Somebody tried to change id of " + id + " but failed.");
	}
    }

    public final synchronized void addComponents(Component... a) {
	for (Component component : a) {
	    component.setContext(context);
	}
	this.getComponents().addAll(Arrays.asList(a));
    }

    public void setContext(GameContext context) {
	this.context = context;
	getComponents().forEach(component -> component.setContext(context));
    }

    public boolean isAnimated() {
	boolean result = false;
	result = getComponents().stream()
		.map(component -> component.hasAnimation())
		.reduce(result, (accumulator, _item) -> accumulator | _item);
	return result;
    }

    @Override
    public final boolean isClient() {
	return true;
    }

}
