package hero.Enum;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Spinner;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;

public enum OtherTrackerOption {
    SPINNER("Spinner", createSpinner()),
    TOGGLE_BUTTON("ToggleBox", createToggleBox());

    private final String name;
    private final Node node;

    OtherTrackerOption(String name, Node node) {
        this.name = name;
        this.node = node;
    }

    public String getName() {
        return name;
    }

    public Node getNode() {
        return node;
    }

    private static Node createSpinner(){
        return new Spinner<>(-100, 100, 0);
    }

    private static Node createToggleBox(){
        ToggleGroup toggleGroup = new ToggleGroup();
        ToggleButton toggleOn = new ToggleButton("ON");
        ToggleButton toggleOff = new ToggleButton("OFF");
        toggleGroup.getToggles().addAll(toggleOn, toggleOff);
        toggleOn.setSelected(true);
        HBox toggleBox = new HBox();
        toggleBox.getChildren().addAll(toggleOn, toggleOff);
        toggleBox.setAlignment(Pos.CENTER);
        return toggleBox;
    }

}
