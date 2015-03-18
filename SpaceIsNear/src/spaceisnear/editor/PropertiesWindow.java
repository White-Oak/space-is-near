package spaceisnear.editor;

import java.util.Collection;
import javax.swing.*;
import lombok.*;

/**
 *
 * @author White Oak
 */
public class PropertiesWindow {

    private final JFrame props = new JFrame();
    private JList propertiesList;
    private PropertiesPanel propertiesPanel;

    public void init() {
	props.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	props.setLocation(800, 200);
	propertiesPanel = new PropertiesPanel();
	props.add(propertiesPanel);
	propertiesList = propertiesPanel.getPropertiesList();
	propertiesList.setVisibleRowCount(8);
    }

    public void setListData(Property... propertys) {
	DefaultListModel<Property> model = (DefaultListModel<Property>) propertiesList.getModel();
	for (Property property : propertys) {
	    model.addElement(property);
	}
    }

    public void setListData(Collection<Property> propertys) {
	DefaultListModel<Property> model = (DefaultListModel<Property>) propertiesList.getModel();
	propertys.forEach(property -> model.addElement(property));
    }

    public void show() {
	props.pack();
	props.setVisible(true);
    }

    public boolean isFinished() {
	return propertiesPanel.isFinished();
    }

    public boolean isCancelled() {
	return propertiesPanel.isCancelled();
    }

    public Property[] getProperties() {
	DefaultListModel<Property> model = (DefaultListModel<Property>) propertiesList.getModel();
	Property[] propertys = new Property[model.size()];
	for (int i = 0; i < model.size(); i++) {
	    propertys[i] = model.get(i);
	}
	return propertys;
    }

    public void dispose() {
	props.dispose();
    }

    @Getter @Setter @RequiredArgsConstructor public static class Property {

	private final String name, value;

	@Override
	public String toString() {
	    return name + " : " + value;
	}

    }
}
