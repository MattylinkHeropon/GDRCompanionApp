package GUI.mainWindow.mainWindowComponent.centralPane;


import hero.Unit;
import hero.otherTracker.OtherTracker_Base;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

public class Tab_2_OtherTrackerPane {
    private static final int DESCRIPTION_COL = 0;
    private static final int TRACKER_COL = 1;
    private static final int DELETE_COL = 2;

    private static final GridPane TRACKER_GRID = buildGrid();

    private static Unit unit;


    private static GridPane buildGrid(){
        GridPane gridPane = new GridPane();
        gridPane.setVgap(5);

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

    public static GridPane getGridPane() {
        return TRACKER_GRID;
    }

    public static void setUnit(Unit pg) {
        unit = pg;
        redrawGrid();
    }

    /**
     * Add a tracker to the Unit, then create the GUI
     * @param tracker tracker to be added
     */
    public static void createTracker(OtherTracker_Base tracker){
        unit.getOtherTrackerArrayList().add(tracker);
        createTrackerGUI(tracker);
    }

    /**
     * Create a tracker GUI with the given specific
     * @param tracker Tracker associated with the GUI
     */
    private static void createTrackerGUI(OtherTracker_Base tracker) {
        Pane background = createBackground();
        Label descriptionLabel = new Label(tracker.getDescription());
        Node node = tracker.drawNode();
        Button deleteButton = createDeleteButton(tracker);

        drawRow(background, descriptionLabel, node, deleteButton);
    }

    /**
     * If we are creating an odd row, create a background as specified in the CSS
     * @return null, if a row is even, a background otherwise
     */
    private static Pane createBackground(){
        if (TRACKER_GRID.getRowCount()%2 == 0) return null;
        Pane pane = new Pane();
        pane.getStyleClass().add("otherTrackerBackground");
        return pane;
    }

    /**
     * Create and set up a DeleteButton for each row
     * @param tracker Tracker that will be eliminated when the button will be set on action
     * @return created Button
     */
    private static Button createDeleteButton(OtherTracker_Base tracker){
        Button button = new Button("X");
        button.setOnAction(actionEvent -> {
            unit.getOtherTrackerArrayList().remove(tracker);
            redrawGrid();
        });
        return button;
    }

    /**
     * Draw from the start the tracker grid
     */
    private static void redrawGrid() {
        TRACKER_GRID.getChildren().clear();
        if (unit.getOtherTrackerArrayList().isEmpty()) return;
        unit.getOtherTrackerArrayList().forEach(Tab_2_OtherTrackerPane::createTrackerGUI);
    }

    /**
     *  Draw a row with the given element
     * @param background Background of the row
     * @param label Label of the row
     * @param node Node of the row
     * @param deleteButton DeleteButton of the row
     */
    private static void drawRow(Pane background, Label label, Node node, Button deleteButton){
        int row = TRACKER_GRID.getRowCount();
        if (background != null) TRACKER_GRID.add(background, DESCRIPTION_COL, row, 3, 1);
        TRACKER_GRID.add(label, DESCRIPTION_COL, row);
        TRACKER_GRID.add(node, TRACKER_COL, row);
        TRACKER_GRID.add(deleteButton, DELETE_COL, row);
    }

}
