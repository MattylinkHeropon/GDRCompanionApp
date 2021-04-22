package hero.magic.spell;

public class Spell_DnD5e extends Spell_Base{
    private String schoolnLevel;
    private String duration;

    public Spell_DnD5e(String name, String component, String castingTime, int range, String description, String schoolnLevel, String duration) {
        super(name, component, castingTime, range, description);
        this.schoolnLevel = schoolnLevel;
        this.duration = duration;
    }

    public String getSchoolnLevel() {
        return schoolnLevel;
    }

    public void setSchoolnLevel(String schoolnLevel) {
        this.schoolnLevel = schoolnLevel;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }
}
