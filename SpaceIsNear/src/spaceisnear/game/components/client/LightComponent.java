package spaceisnear.game.components.client;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import spaceisnear.game.GameContext;
import spaceisnear.game.components.Component;
import spaceisnear.game.components.ComponentType;
import spaceisnear.game.messages.Message;
import spaceisnear.game.messages.MessageMoved;
import spaceisnear.game.messages.properties.MessagePropertySet;

public class LightComponent extends Component {

    private Body body;
    private static final PolygonShape BOX;

    static {
	BOX = new PolygonShape();
	BOX.setAsBox(0.5f, 0.5f);
    }

    public LightComponent() {
	super(ComponentType.LIGHT);
    }

    @Override
    public void processMessage(Message message) {
	switch (message.getMessageType()) {
	    case PROPERTY_SET:
		MessagePropertySet mps = (MessagePropertySet) message;
		GameContext gc = (GameContext) getContext();
		if (mps.getName().equals("blockingLight")) {
		    Object property = mps.getValue();
		    if (property != null) {
			if ((Boolean) property && body == null) {
			    // Create our body definition
			    BodyDef groundBodyDef = new BodyDef();
			    groundBodyDef.type = BodyDef.BodyType.StaticBody;
			    // Set its world position
			    groundBodyDef.position.set(new Vector2(getPosition().getX(), getPosition().getY()));

			    // Create a body from the defintion and add it to the world
			    body = gc.getWorld().createBody(groundBodyDef);

			    // Create a fixture from our polygon shape and add it to our ground body  
			    body.createFixture(BOX, 0.0f);
			}
		    } else {
			if (body != null) {
			    gc.getWorld().destroyBody(body);
			}
		    }
		}
		break;
	    case MOVED:
	    case TELEPORTED:
		if (body != null) {
		    MessageMoved mm = (MessageMoved) message;
		    body.setTransform(mm.getX() + 0.5f, mm.getY() + 0.5f, 0);
		}
		break;
	}
    }
}
