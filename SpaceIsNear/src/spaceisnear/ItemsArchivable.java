/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceisnear;

import spaceisnear.game.objects.items.ItemBundle;
import spaceisnear.game.objects.items.Size;
import spaceisnear.game.objects.items.Type;

/**
 *
 * @author White Oak
 */
public interface ItemsArchivable {

    public int getIdByName(String name);

    public boolean isBlockingAir(int id);

    public boolean isBlockingAir(String name);

    public boolean isBlockingPath(int id);

    public boolean isBlockingPath(String name);

    public Size getSize(int id);

    public Size getSize(String name);

    public String getName(int id);

    public String getName(String name);

    public Type getType(int id);

    public Type getType(String name);

    public int[] getImageIds(int id);

    public ItemBundle getBundle(int id);
}
