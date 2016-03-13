package spaceisnear.editor;

import lombok.*;

/**
 *
 * @author White Oak
 */
@Getter
@Setter
@RequiredArgsConstructor
public class ItemProperty {
    private final String name;
    private final String value;

    @Override
    public String toString() {
	return name + " : " + value;
    }

}
