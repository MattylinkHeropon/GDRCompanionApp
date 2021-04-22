package hero.magic.casterClass;

import hero.magic.spell.Spell_Base;
import javafx.scene.layout.HBox;

import java.util.ArrayList;

public class Caster_Class implements Caster_Class_Base {
    private final String className;
    private int level;
    private final boolean spontaneousCaster;
    private final ArrayList<Spell_Base> spellList = new ArrayList<>();
    private final int[] numberOfSpell = new int[10]; //At each index correspond the level of spell, so index 0 -> cantrip up to index 9 -> spell of 9th level

    public Caster_Class(String className, int level, boolean spontaneousCaster, int[] spell) {
        this.className = className;
        this.level = level;
        this.spontaneousCaster = spontaneousCaster;
        System.arraycopy(spell, 0, numberOfSpell, 0, numberOfSpell.length);
    }

    public String getClassName() {
        return className;
    }

    public int getLevel() {
        return level;
    }

    @Override
    public void setLevel(int level) {
        this.level = level;
    }

    public boolean isSpontaneousCaster() {
        return spontaneousCaster;
    }

    public ArrayList<Spell_Base> getSpellList() {
        return spellList;
    }

    public int[] getNumberOfSpell() {
        return numberOfSpell;
    }

    public void buildMask(){

    }

    @Override
    public HBox buildClassMask() {
        return null;
    }
}
