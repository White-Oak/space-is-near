/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceisnear.editor;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

/**
 *
 * @author White Oak
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
	// TODO code application logic here
	LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
	cfg.title = "Space Editor";
//	cfg.width = 1000 + RightTab.TAB_WIDTH;
//	cfg.height = 800;
	cfg.width = 1920;
	cfg.height = 1080;
	cfg.fullscreen = true;
	cfg.vSyncEnabled = true;
	LwjglApplication lwjglApplication = new LwjglApplication(new spaceisnear.editor.Core(), cfg);
    }

}
