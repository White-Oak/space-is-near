package spaceisnear.editor;

import java.util.Collection;
import javax.swing.*;

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

    public void setListData(ItemProperty... propertys) {
	DefaultListModel<ItemProperty> model = (DefaultListModel<ItemProperty>) propertiesList.getModel();
	for (ItemProperty property : propertys) {
	    model.addElement(property);
	}
    }

    public void setListData(Collection<ItemProperty> propertys) {
	DefaultListModel<ItemProperty> model = (DefaultListModel<ItemProperty>) propertiesList.getModel();
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

    public ItemProperty[] getProperties() {
	DefaultListModel<ItemProperty> model = (DefaultListModel<ItemProperty>) propertiesList.getModel();
	ItemProperty[] propertys = new ItemProperty[model.size()];
	for (int i = 0; i < model.size(); i++) {
	    propertys[i] = model.get(i);
	}
	return propertys;
    }

    public void dispose() {
	props.dispose();
    }

}
