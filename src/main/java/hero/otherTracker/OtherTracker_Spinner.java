package hero.otherTracker;

import javafx.scene.Node;
import javafx.scene.control.Spinner;

public class OtherTracker_Spinner extends OtherTracker_Base{

    public OtherTracker_Spinner(String description) {
        super(description);
    }

    @Override
    public Node drawNode() {
        if (currData == null) currData = 0.0;
        Spinner<Integer> spinner = new Spinner<>(-100, 100, (Double) this.getCurrData());
        spinner.getEditor().textProperty().addListener((observableValue, oldValue, newValue) -> this.setCurrData(Integer.parseInt(newValue)));
        return spinner;
    }

}
