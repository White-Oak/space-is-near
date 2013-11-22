/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceisnear.game.bundles;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@AllArgsConstructor public class JSONBundle extends Bundle {

    @Getter private String body;

    public JSONBundle() {
    }

    @Override
    public byte[] getBytes() {
	return body.getBytes();
    }
}
