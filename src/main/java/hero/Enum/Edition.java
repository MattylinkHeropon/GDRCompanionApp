package hero.Enum;

/**
 * Full list with each edition of DnD and Pathfinder (not every edition is supported right now)
 */
public enum Edition {
    DND_1E("DnD", "DnD"),
    DND_2E("Advanced DnD", "ADnD"),
    DND_22E("Advanced DnD 2nd Edition", "ADnD 2e"),
    DND_3E("DnD 3rd Edition", "DnD 3e"),
    DND_35E("DnD v. 3.5", "DnD 3.5e"),
    DND_4E("DnD 4th Edition", "DnD 4e"),
    DND_5E("DnD 5th Edition", "DnD 5e"),
    PATHFINDER_1E("Pathfinder", "Path"),
    PATHFINDER_2E("Pathfinder 2nd Edition", "Path 2e");

    private final String fullName;
    private final String abbreviation;


    Edition(String fullName, String abbreviation) {
        this.fullName = fullName;
        this.abbreviation = abbreviation;
    }

    public String getFullName() {return fullName;}

    public String getAbbreviation() {return abbreviation; }
}


