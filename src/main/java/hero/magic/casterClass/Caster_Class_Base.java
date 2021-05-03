package hero.magic.casterClass;

import hero.magic.spell.Spell_Base;
import javafx.scene.layout.VBox;

import java.util.ArrayList;


public abstract class Caster_Class_Base {
    protected final String className;
    protected int level;
    protected final ArrayList<Spell_Base> spellList = new ArrayList<>();
    protected final int[] totalNumberOfSpell = new int[10]; //At each index correspond the level of spell, so index 0 -> cantrip up to index 9 -> spell of 9th level


    public Caster_Class_Base(String className, int level, int[] spell) {
        this.className = className;
        this.level = level;
        System.arraycopy(spell, 0, totalNumberOfSpell, 0, totalNumberOfSpell.length);
    }

    public String getClassName() {
        return className;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public ArrayList<Spell_Base> getSpellList() {
        return spellList;
    }

    public int[] getTotalNumberOfSpell() {
        return totalNumberOfSpell;
    }

    public abstract VBox buildClassMask();

    public abstract void resetSlot();

}
