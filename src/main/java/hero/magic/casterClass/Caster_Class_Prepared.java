package hero.magic.casterClass;

import hero.magic.spell.Spell_Base;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.util.ArrayList;

public class Caster_Class_Prepared extends Caster_Class_Base{

    public Caster_Class_Prepared(String className, int level, int[] spell) {
        super(className, level, spell);
    }

    @Override
    public ArrayList<Spell_Base> getSpellList() {
        return super.getSpellList();
    }

    @Override
    public VBox buildClassMask() {
        Label label = new Label("Nothing to see here");
        VBox box = new VBox();
        box.getChildren().add(label);
        return box;
    }

    @Override
    public void resetSlot() {

    }

}
