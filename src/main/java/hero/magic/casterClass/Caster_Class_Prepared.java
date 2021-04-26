package hero.magic.casterClass;

import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class Caster_Class_Prepared extends Caster_Class_Base{


    public Caster_Class_Prepared(String className, int level, int[] spell) {
        super(className, level, spell);
    }

    @Override
    boolean setSpontaneousCaster() {
        return false;
    }

    @Override
    public VBox buildClassMask() {
        Label label = new Label("Nothing to see here");
        VBox box = new VBox();
        box.getChildren().add(label);
        return box;
    }
}
