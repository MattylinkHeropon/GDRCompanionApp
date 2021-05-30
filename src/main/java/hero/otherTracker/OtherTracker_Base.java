package hero.otherTracker;

import hero.Enum.OtherTrackerOption;
import javafx.scene.Node;

public abstract class OtherTracker_Base {
    protected final String description;
    protected Object currData;

    public OtherTracker_Base(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setCurrData(Object data){
        currData = data;
    }

    public Object getCurrData() {
        return currData;
    }

    public abstract Node drawNode();
}
