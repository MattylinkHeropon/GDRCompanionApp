package hero.magic.casterClass;

import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class Caster_Class_Spontaneous extends Caster_Class_Base {

    public Caster_Class_Spontaneous(String className, int level, int[] spell) {
        super(className, level, spell);
    }

    @Override
    boolean setSpontaneousCaster() {
        return true;
    }

    @Override
    public VBox buildClassMask() {
        VBox classMask = new VBox(5);
        Label classNameLabel = new Label(className + "(Lv: " + level + ")");
        classMask.getChildren().add(classNameLabel);

        for (int i = 0; i < numberOfSpell.length; i++) {
            if (numberOfSpell[i] > 0) classMask.getChildren().add(lvRow(i, numberOfSpell[i]));
        }

        return classMask;
    }


    private HBox lvRow(int level, int number){
        HBox hBox = new HBox(2);
        Label label = new Label("Lv." + level + ": ");
        hBox.getChildren().add(label);
        for (int i = 0; i < number; i++) {
            hBox.getChildren().add(new RadioButton());
        }
        return hBox;
    }

}