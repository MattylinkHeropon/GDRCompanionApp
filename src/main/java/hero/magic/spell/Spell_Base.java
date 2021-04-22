package hero.magic.spell;

public class Spell_Base {

    private String name;
    private String component;
    private String castingTime;
    private int range;
    private String description;

    public Spell_Base(String name, String component, String castingTime, int range, String description) {
        this.name = name;
        this.component = component;
        this.castingTime = castingTime;
        this.range = range;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getComponent() {
        return component;
    }

    public void setComponent(String component) {
        this.component = component;
    }

    public String getCastingTime() {
        return castingTime;
    }

    public void setCastingTime(String castingTime) {
        this.castingTime = castingTime;
    }

    public int getRange() {
        return range;
    }

    public void setRange(int range) {
        this.range = range;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
