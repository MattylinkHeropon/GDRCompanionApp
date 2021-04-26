package hero.magic.casterClass;

import hero.magic.spell.Spell_Base;
import javafx.scene.layout.VBox;

import java.util.ArrayList;


public abstract class Caster_Class_Base {
    protected final String className;
    protected int level;
    protected final boolean spontaneousCaster;
    protected final ArrayList<Spell_Base> spellList = new ArrayList<>();
    protected final int[] numberOfSpell = new int[10]; //At each index correspond the level of spell, so index 0 -> cantrip up to index 9 -> spell of 9th level

    public Caster_Class_Base(String className, int level, int[] spell) {
        this.className = className;
        this.level = level;
        spontaneousCaster = setSpontaneousCaster();
        System.arraycopy(spell, 0, numberOfSpell, 0, numberOfSpell.length);
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

    public boolean isSpontaneousCaster() {
        return spontaneousCaster;
    }

    public ArrayList<Spell_Base> getSpellList() {
        return spellList;
    }

    public int[] getNumberOfSpell() {
        return numberOfSpell;
    }

    abstract boolean setSpontaneousCaster();

    public abstract VBox buildClassMask();
}
