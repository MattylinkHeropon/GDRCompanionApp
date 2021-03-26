package hero.magic;

public class Spell_Path1eDnD3Xe extends Spell_Base{

    private String school;
    private String level;
    private String duration;
    private String target;
    private String area;
    private String savingThrow;
    private int spellResistance;

    public Spell_Path1eDnD3Xe(String name, String component, String castingTime, int range, String description, String school, String level, String duration, String target, String area, String savingThrow, int spellResistance) {
        super(name, component, castingTime, range, description);
        this.school = school;
        this.level = level;
        this.duration = duration;
        this.target = target;
        this.area = area;
        this.savingThrow = savingThrow;
        spellResistance = spellResistance;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getSavingThrow() {
        return savingThrow;
    }

    public void setSavingThrow(String savingThrow) {
        this.savingThrow = savingThrow;
    }

    public int getSpellResistance() {
        return spellResistance;
    }

    public void setSpellResistance(int spellResistance) {
        this.spellResistance = spellResistance;
    }
}
