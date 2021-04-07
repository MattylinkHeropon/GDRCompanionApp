package GUI.mainWindow.mainWindowComponent.centralPane;

import GUI.smallWindows.creationWindows.otherCounter.OtherTrackerOption;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import java.util.ArrayList;

public class Tab_2_OtherTrackerPane {
    private static final int DESCRIPTION_COL = 0;
    private static final int TRACKER_COL = 1;
    private static final int DELETE_COL = 2;

    private final GridPane gridPane;

    private final ArrayList<Label> labels = new ArrayList<>();
    private final ArrayList<Node> nodes = new ArrayList<>();
    private final ArrayList<Button> deleteButtons = new ArrayList<>();

    public Tab_2_OtherTrackerPane(){
        gridPane = new GridPane();
        gridPane.setVgap(5);

        //GridPane constrain
        ColumnConstraints descriptionColumnConstrain = new ColumnConstraints();
        descriptionColumnConstrain.setPercentWidth(75);
        ColumnConstraints trackerColumnConstrain = new ColumnConstraints();
        trackerColumnConstrain.setPercentWidth(20);
        ColumnConstraints deleteColumnConstrain = new ColumnConstraints();
        deleteColumnConstrain.setPercentWidth(5);
        gridPane.getColumnConstraints().addAll(descriptionColumnConstrain, trackerColumnConstrain, deleteColumnConstrain);
    }

    public GridPane getGridPane() {
        return gridPane;
    }

    /**
     * Create a tracker with the given specific
     * @param description description of the tracker
     * @param option selected option for the tracker selection
     */
    public void createTracker (String description, OtherTrackerOption option) {
        Pane background = createBackground();
        Label descriptionLabel = new Label(description);
        Node node = createTracker(option);
        Button deleteButton = createDeleteButton(descriptionLabel, node);

        labels.add(descriptionLabel);
        nodes.add(node);
        deleteButtons.add(deleteButton);

        drawRow(background, descriptionLabel, node, deleteButton);
    }

    /**
     * If we are creating an odd row, create a background as specified in the CSS
     * @return null, if a row is even, a background otherwise
     */
    private Pane createBackground(){
        if (gridPane.getRowCount()%2 == 0) return null;
        Pane pane = new Pane();
        pane.getStyleClass().add("otherTrackerBackground");
        return pane;
    }

    /**
     * NOTE: (Almost) copied from OtherTrackerOption, for now is the only way to create an exact copy of a Node, since node.clone() is not supported.
     * Create a Node (tracker) associated to the given Option
     * @param option An Option with a Node associated
     * @return the created Node
     */
    private Node createTracker(OtherTrackerOption option){
        switch (option) {
            case SPINNER:
                return new Spinner<>(-100, 100, 0);
            case TOGGLE_BUTTON:
                ToggleGroup toggleGroup = new ToggleGroup();
                ToggleButton toggleOn = new ToggleButton("ON");
                ToggleButton toggleOff = new ToggleButton("OFF");
                toggleGroup.getToggles().addAll(toggleOn, toggleOff);
                toggleOn.setSelected(true);
                HBox toggleBox = new HBox();
                toggleBox.getChildren().addAll(toggleOn, toggleOff);
                return toggleBox;
        }
        return null;
    }

    /**
     * Create and set up a DeleteButton for each row
     * @param label Label of the same row of DeleteButton
     * @param tracker Tracker of the same row of DeleteButton
     * @return created Button
     */
    private Button createDeleteButton(Label label, Node tracker){
        Button button = new Button("X");
        button.setOnAction(actionEvent -> redrawGrid(label, tracker, button));
        return button;
    }

    /**
     * Called by a deleteButton. Empty the gridPane, delete the given node from the corresponding list, and redraw the remaining one
     * @param label Label to be eliminated
     * @param node Node to be eliminated
     * @param deleteButton deleteButton to be eliminated
     */
    private void redrawGrid(Label label, Node node, Button deleteButton) {
        gridPane.getChildren().clear();
        labels.remove(label);
        nodes.remove(node);
        deleteButtons.remove(deleteButton);

        if (labels.isEmpty()) return;

        for (Label currLabel: labels
             ) {
            int index = labels.indexOf(currLabel);
            Pane background = createBackground();
            drawRow(background, currLabel, nodes.get(index), deleteButtons.get(index));
        }
    }


    /**
     *  Draw a row with the given element
     * @param background Background of the row
     * @param label Label of the row
     * @param node Node of the row
     * @param deleteButton DeleteButton of the row
     */
    private void drawRow(Pane background, Label label, Node node, Button deleteButton){
        int row = gridPane.getRowCount();
        if (background != null) gridPane.add(background, DESCRIPTION_COL, row, 3, 1);
        gridPane.add(label, DESCRIPTION_COL, row);
        gridPane.add(node, TRACKER_COL, row);
        gridPane.add(deleteButton, DELETE_COL, row);
    }

}
