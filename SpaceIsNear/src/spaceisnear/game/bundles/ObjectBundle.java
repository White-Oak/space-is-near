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

    @Getter private final GameObjectState state;
    @Getter private final int objectID;
    @Getter private final GameObjectType objectType;
}
