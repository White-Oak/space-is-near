/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceisnear.game.bundles;

import lombok.AllArgsConstructor;
import lombok.Getter;
import spaceisnear.game.objects.GameObjectState;
import spaceisnear.game.objects.GameObjectType;

@AllArgsConstructor public class ObjectBundle extends Bundle {

    @Getter private GameObjectState state;
    @Getter private int objectID;
    @Getter private GameObjectType objectType;

    public ObjectBundle() {
    }
}
