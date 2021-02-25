package hero.Enum;

/**
 * Enum with the name of Ability Score, each Ability has his index number in an array and the official abbrevation
 */
public enum AbilityScore {
    STRENGTH (0, "Str"),
    DEXTERITY (1, "Dex"),
    CONSTITUTION (2, "Con"),
    INTELLIGENCE (3, "Int"),
    WISDOM (4, "Wis"),
    CHARISMA (5, "Cha");


    private final int index;
    private final String abbreviation;

    AbilityScore (int index, String abbreviation){
        this.index = index;
        this.abbreviation = abbreviation;
    }

    public int getIndex() {
        return index;
    }

    public String getAbbreviation() {
        return abbreviation;
    }

}

