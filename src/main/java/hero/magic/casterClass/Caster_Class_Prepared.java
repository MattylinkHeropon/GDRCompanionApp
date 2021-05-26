package hero.magic.casterClass;


import GUI.smallWindows.UpdateCasterClassWindow;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.stream.IntStream;


public class Caster_Class_Prepared extends Caster_Class_Base{

    //Use of an hash table with separate chaining-like data structure for storing spell name
    private final ArrayList<String>[] spellNameArray = new ArrayList[10];

    public ArrayList<String>[] getSpellNameArray() {
        return spellNameArray;
    }

    public Caster_Class_Prepared(String className, int level, int[] spell) {
        super(className, level, spell);
        //initialize spellNameArray
        IntStream.range(0, spellNameArray.length).forEach(i -> spellNameArray[i] = new ArrayList<>());
        resetSlot();
    }

    @Override
    public void resetSlot() {
        UpdateCasterClassWindow.changePreparedSpell(this);
    }

    @Override
    public VBox buildClassMask() {
        VBox classMask = new VBox(5);

        Label classNameLabel = new Label(className + "(Lv: " + level + ")");
        classMask.getChildren().add(classNameLabel);

        //Textfield with the name of the current selected spell
        TextField currSelectedSpell = new TextField();
        currSelectedSpell.setEditable(false);
        currSelectedSpell.setPromptText("Last Spell selected here");
        //reset currSelectedSpell il we EXIT classMask
        classMask.hoverProperty().addListener((observableValue, oldValue, newValue) -> {
            if (!newValue) currSelectedSpell.clear();
        });
        classMask.getChildren().add(currSelectedSpell);

        //draw rows
        for (int i = 0; i < totalNumberOfSpell.length; i++) {
            int numberOfSpell = totalNumberOfSpell[i];
            if(numberOfSpell != 0) classMask.getChildren().add(lvRow(i, numberOfSpell, currSelectedSpell));
        }
        return classMask;
    }

    HBox lvRow(int spellLevel, int numberOfSpell, TextField textField) {
        HBox hBox = new HBox(2);
        ArrayList<String> currList = spellNameArray[spellLevel];

        Label lvClassLabel = new Label("Lv." + spellLevel + ": ");
        hBox.getChildren().add(lvClassLabel);

        for (int i = 0; i < numberOfSpell; i++) {
            RadioButton radioButton = new RadioButton();
            String text = currList.get(i);
            //Set textField field if we hover on the radiobutton. No resetting
            radioButton.hoverProperty().addListener((observableValue, oldValue, newValue) ->{
                    if (newValue) textField.setText(text);
            });
            hBox.getChildren().add(radioButton);
        }

        return hBox;
    }

}
