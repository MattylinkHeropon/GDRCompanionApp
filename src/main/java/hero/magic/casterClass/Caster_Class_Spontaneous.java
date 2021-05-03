package hero.magic.casterClass;

import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class Caster_Class_Spontaneous extends Caster_Class_Base {
    protected final int[] remainingNumberOfSpell = new int[10];

    public Caster_Class_Spontaneous(String className, int level, int[] spell) {
        super(className, level, spell);
        System.arraycopy(spell, 0, remainingNumberOfSpell, 0, remainingNumberOfSpell.length);
    }

    @Override
    public VBox buildClassMask() {
        VBox classMask = new VBox(5);
        Label classNameLabel = new Label(className + "(Lv: " + level + ")");
        classMask.getChildren().add(classNameLabel);

        for (int i = 0; i < totalNumberOfSpell.length; i++) {
            if (totalNumberOfSpell[i] > 0) classMask.getChildren().add(lvRow(i, totalNumberOfSpell[i]));
        }
        return classMask;
    }
    private HBox lvRow(int level, int NumberOfSpell){
        HBox hBox = new HBox(2);
        Label label = new Label("Lv." + level + ": ");
        Label slashLabel = new Label("/");
        hBox.getChildren().add(label);

        //remaining
        Spinner<Integer> current = new Spinner<>(0, NumberOfSpell, remainingNumberOfSpell[level]);
        current.getEditor().textProperty().addListener((observableValue, oldValue, newValue) -> remainingNumberOfSpell[level] = Integer.parseInt(newValue));

        //total
        TextField maximum = new TextField(Integer.toString(totalNumberOfSpell[level]));
        maximum.setEditable(false);

        hBox.getChildren().addAll(current, slashLabel, maximum);
        return hBox;
    }

    @Override
    public void resetSlot() {
        System.arraycopy(totalNumberOfSpell, 0, remainingNumberOfSpell, 0, totalNumberOfSpell.length);
    }

}