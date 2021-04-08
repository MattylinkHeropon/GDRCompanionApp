package hero;

import hero.Enum.OtherTrackerOption;

public class OtherTracker {
    private final String description;
    private final OtherTrackerOption option;
    private int currSpinnerValue;
    private boolean currToggleBoxSelected;

    public OtherTracker(String description, OtherTrackerOption option) {
        this.description = description;
        this.option = option;
    }

    public String getDescription() {
        return description;
    }

    public OtherTrackerOption getOption() {
        return option;
    }

    public int getCurrSpinnerValue() {
        return currSpinnerValue;
    }

    public void setCurrSpinnerValue(int currSpinnerValue) {
        this.currSpinnerValue = currSpinnerValue;
    }

    public boolean isCurrToggleBoxSelected() {
        return currToggleBoxSelected;
    }

    public void setCurrToggleBoxSelected(boolean currToggleBoxSelected) {
        this.currToggleBoxSelected = currToggleBoxSelected;
    }
}
