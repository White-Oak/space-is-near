/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceisnear.editor;

import java.util.LinkedList;
import java.util.List;
import lombok.*;

/**
 *
 * @author White Oak
 */
@Data @RequiredArgsConstructor public class Item {

    @NonNull private int id;
    @NonNull private int x, y;
    private List<PropertiesWindow.Property> properties = new LinkedList<>();

    public boolean add(PropertiesWindow.Property e) {
	return properties.add(e);
    }

}
