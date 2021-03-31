package GUI.mainWindow.mainWindowComponent;

import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

public class OtherTrackerPane {
    private static final int DESCRIPTION_COL = 0;
    private static final int TRACKER_COL = 1;
    private static final int DELETE_COL = 2;

    private static GridPane gridPane = null;

    public static GridPane getGridPane() {
        gridPane = new GridPane();

        //GridPane constrain
        ColumnConstraints descriptionColumnConstrain = new ColumnConstraints();
        descriptionColumnConstrain.setPercentWidth(75);
        ColumnConstraints trackerColumnConstrain = new ColumnConstraints();
        trackerColumnConstrain.setPercentWidth(20);
        ColumnConstraints deleteColumnConstrain = new ColumnConstraints();
        deleteColumnConstrain.setPercentWidth(5);

        gridPane.getColumnConstraints().addAll(descriptionColumnConstrain, trackerColumnConstrain, deleteColumnConstrain);



        return gridPane;

    }

    public static void testPane (){
        String stdText = "Test n° ";

        Spinner<Integer> spinner = new Spinner<>(-100, 100, 0);
        spinner.setEditable(true);
        createNode(stdText + (gridPane.getRowCount() + 1) + ": Spinner", spinner);

        ToggleGroup toggleGroup = new ToggleGroup();
        ToggleButton toggleOn = new ToggleButton("ON");
        ToggleButton toggleOff = new ToggleButton("OFF");
        toggleGroup.getToggles().addAll(toggleOn, toggleOff);
        toggleOn.setSelected(true);
        HBox toggleBox = new HBox();
        toggleBox.getChildren().addAll(toggleOn, toggleOff);
        createNode(stdText + (gridPane.getRowCount() + 1) + ": ToggleButtons", toggleBox);
    }

    private static void createNode(String text, Node node){
        int row = gridPane.getRowCount();
        Label label = new Label(text);
        Button deleteButton = createDeleteButton(label, node);

        GridPane.setConstraints(label, DESCRIPTION_COL, row);
        GridPane.setConstraints(node, TRACKER_COL, row);
        GridPane.setConstraints(deleteButton, DELETE_COL, row);

        gridPane.getChildren().addAll(label, node, deleteButton);
        System.out.println("Creato nodo " + node.getClass().getName() + " alla riga " + row);
    }

    private static Button createDeleteButton(Node label, Node tracker){
        Button button = new Button("X");
        button.setOnAction(actionEvent -> gridPane.getChildren().removeAll(label, tracker, button));
        return button;
    }

}
