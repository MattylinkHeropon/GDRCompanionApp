package hero.magic.spell;

public class Spell_Path2e extends Spell_Base {
    private String trait;
    private String tradition;
    private String target;
    private String savingThrow;
    private String duration;
    private int level;

    public Spell_Path2e(String name, String component, String castingTime, int range, String description, String trait, String tradition, String target, String savingThrow, String duration, int level) {
        super(name, component, castingTime, range, description);
        this.trait = trait;
        this.tradition = tradition;
        this.target = target;
        this.savingThrow = savingThrow;
        this.duration = duration;
        this.level = level;
    }

    public String getTrait() {
        return trait;
    }

    public void setTrait(String trait) {
        this.trait = trait;
    }

    public String getTradition() {
        return tradition;
    }

    public void setTradition(String tradition) {
        this.tradition = tradition;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getSavingThrow() {
        return savingThrow;
    }

    public void setSavingThrow(String savingThrow) {
        this.savingThrow = savingThrow;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }
}
