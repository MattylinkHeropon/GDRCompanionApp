package hero.Enum;

/**
 * Enum with the name of Ability Score, each Ability has the official abbreviation
 */
public enum AbilityScore {
    STRENGTH("Str"),
    DEXTERITY("Dex"),
    CONSTITUTION("Con"),
    INTELLIGENCE("Int"),
    WISDOM("Wis"),
    CHARISMA("Cha");


    private final String abbreviation;

    AbilityScore(String abbreviation) {
        this.abbreviation = abbreviation;
    }

    public String getAbbreviation() {
        return abbreviation;
    }

}

