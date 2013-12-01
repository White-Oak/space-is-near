// Generated by delombok at Sun Dec 01 13:28:22 MSK 2013
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceisnear.game.components;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import spaceisnear.game.messages.Message;
import spaceisnear.Context;
import spaceisnear.game.GameContext;
import spaceisnear.game.objects.GameObject;

/**
 * @author LPzhelud
 */
public abstract class Component {
	
	private ArrayList<ComponentState> states = new ArrayList<>();
	private Context context = null;
	
	public abstract void processMessage(Message message);
	
	public void setContext(Context context) {
		if (this.context == null) {
			this.context = context;
		}
	}
	
	public static Component getInstance(ComponentStateBundle[] states, Class component) throws ClassNotFoundException {
		try {
			Component newInstance = (Component)component.newInstance();
			newInstance.states = new ArrayList<>();
			for (ComponentStateBundle state : states) {
				ComponentState componentState = state.getState();
				newInstance.states.add(componentState);
			}
			return newInstance;
		} catch (InstantiationException | IllegalAccessException ex) {
			Logger.getLogger(Component.class.getName()).log(Level.SEVERE, null, ex);
		}
		return null;
	}
	
	protected ComponentState getStateNamed(String name) {
		for (ComponentState componentState : states) {
			if (componentState.getName().equals(name)) {
				return componentState;
			}
		}
		return null;
	}
	
	protected Object getStateValueNamed(String name) {
		for (ComponentState componentState : states) {
			if (componentState.getName().equals(name)) {
				return componentState.getValue();
			}
		}
		return null;
	}
	
	protected GameObject getOwner() {
		return ((GameContext)getContext()).getObjects().get(getOwnerId());
	}
	
	protected int getOwnerId() {
		return (Integer)getStateValueNamed("owner");
	}
	
	public void setOwnerId(int id) {
		states.add(new ComponentState("owner", id));
	}
	
	protected void addState(ComponentState state) {
		states.add(state);
	}
	
	
	public ArrayList<ComponentState> getStates() {
		return this.states;
	}
	
	
	protected Context getContext() {
		return this.context;
	}
}