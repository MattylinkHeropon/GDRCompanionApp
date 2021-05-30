package hero.otherTracker;

import javafx.scene.Node;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;

public class OtherTracker_ToggleBox extends OtherTracker_Base{


    public OtherTracker_ToggleBox(String description) {
        super(description);
    }

    @Override
    public Node drawNode() {
        if (currData == null) currData = true;
        ToggleButton toggleOn = new ToggleButton("ON");
        toggleOn.setOnAction(actionEvent -> this.setCurrData(true));
        ToggleButton toggleOff = new ToggleButton("OFF");
        toggleOff.setOnAction(actionEvent -> this.setCurrData(false));
        ToggleGroup toggleGroup = new ToggleGroup();
        toggleGroup.getToggles().addAll(toggleOn, toggleOff);
        if (getCurrData().equals(true)) toggleOn.setSelected(true);
        else toggleOff.setSelected(true);
        HBox toggleBox = new HBox();
        toggleBox.getChildren().addAll(toggleOn, toggleOff);
        return toggleBox;
    }
}
