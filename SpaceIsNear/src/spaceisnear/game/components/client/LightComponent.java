package spaceisnear.game.components.client;

import box2dLight.PointLight;
import box2dLight.RayHandler;
import com.badlogic.gdx.graphics.Color;
import spaceisnear.game.components.Component;
import spaceisnear.game.components.ComponentType;
import spaceisnear.game.messages.Message;
import spaceisnear.game.messages.MessageMoved;
import spaceisnear.game.messages.properties.MessagePropertySet;

public class LightComponent extends Component {

    private final PointLight light;

    @Override
    public void processMessage(Message message) {
	switch (message.getMessageType()) {
	    case PROPERTY_SET:
		MessagePropertySet mps = (MessagePropertySet) message;
		if (light != null && mps.getName().equals("lightEnabled")) {
		    Object property = mps.getValue();
		    if (property != null) {
			if ((Boolean) property) {
			    light.setActive(true);
			} else {
			    light.setActive(false);
			}
		    }
		}
		break;
	    case MOVED:
		if (light != null) {
		    light.setPosition(getPosition().getX() + 0.5f, getPosition().getY() + 0.5f);
		}
		break;
	    case TELEPORTED:
		if (light != null) {
		    MessageMoved mm = (MessageMoved) message;
		    light.setPosition(mm.getX() + 0.5f, mm.getY() + 0.5f);
		}
		break;
	}
    }

    public LightComponent(LightProperty property, RayHandler rayHandler) {
	super(ComponentType.LIGHT);
	light = new PointLight(rayHandler, 64);
	light.setColor(property.color);
	light.setDistance(property.distance);
	light.setSoft(true);
	light.setSoftnessLenght(2.5f);
	light.setStaticLight(true);
    }

    public static class LightProperty {

	Color color;
	int distance;
    }
}
