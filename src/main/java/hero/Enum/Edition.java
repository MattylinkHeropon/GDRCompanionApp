package hero.Enum;

/**
 * Full list with each edition of D&D and Pathfinder (not every edition is supported right now)
 */
public enum Edition {
    DND_1E("D&D", "D&D"),
    DND_2E("Advanced D&D", "AD&D"),
    DND_22E("Advanced D&D 2nd Edition", "AD&D 2E"),
    DND_3E("D&D 3rd Edition", "D&D 3E"),
    DND_35E("D&D v. 3.5", "D&D 3.5E"),
    DND4E("D&D 4th Edition", "D&D 4E"),
    DND5E("D&D 5th Edition", "D&D 5E"),
    PATHFINDER_1E("Pathfinder", "Path"),
    PATHFINDER_2E("Pathfinder 2nd Edition", "Path 2E");

    private final String fullName;
    private final String abbreviation;


    Edition(String fullName, String abbreviation) {
        this.fullName = fullName;
        this.abbreviation = abbreviation;
    }

    public String getFullName() {
        return fullName;
    }

    public String getAbbreviation() {
        return abbreviation;
    }
}


