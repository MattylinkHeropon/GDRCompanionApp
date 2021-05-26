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

            //create rows
            for (int i = 0; i < totalNumberOfSpell.length; i++) {
                int numberOfSpell = totalNumberOfSpell[i];
                if (numberOfSpell != 0) classMask.getChildren().add(lvRow(i, numberOfSpell));
            }
            return classMask;
        }

    HBox lvRow(int spellLevel, int numberOfSpell){
        HBox hBox = new HBox(2);
        Label label = new Label("Lv." + spellLevel + ": ");
        hBox.getChildren().add(label);

        //remaining slot
        Spinner<Integer> remainingSpell = new Spinner<>(0, numberOfSpell, remainingNumberOfSpell[spellLevel]);
        remainingSpell.getEditor().textProperty().addListener((observableValue, oldValue, newValue) -> remainingNumberOfSpell[spellLevel] = Integer.parseInt(newValue));

        //total slot
        TextField totalSpell = new TextField(Integer.toString(totalNumberOfSpell[spellLevel]));
        totalSpell.setEditable(false);

        Label slashLabel = new Label("/");
        hBox.getChildren().addAll(remainingSpell, slashLabel, totalSpell);
        return hBox;
    }

    @Override
    public void resetSlot() {
        System.arraycopy(totalNumberOfSpell, 0, remainingNumberOfSpell, 0, totalNumberOfSpell.length);
    }

}