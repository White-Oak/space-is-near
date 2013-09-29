/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceisnear.game.bundles;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor public class JSONBundle extends Bundle {

    @Getter private final String body;

    @Override
    public byte[] getBytes() {
	return body.getBytes();
    }
}
