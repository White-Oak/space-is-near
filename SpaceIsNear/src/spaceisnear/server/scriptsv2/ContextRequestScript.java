package spaceisnear.server.scriptsv2;

import java.util.ArrayList;
import lombok.Getter;

public abstract class ContextRequestScript extends ItemScript {

    @Getter private final ArrayList<String> subMenu = new ArrayList<>();
    @Getter private int defaults;

    protected void addDefaultActions() {
	add("Learn");
	add("Pull");
	add("Take");
	defaults = 3;
    }

    protected void add(String close) {
	subMenu.add(close);
    }

}
