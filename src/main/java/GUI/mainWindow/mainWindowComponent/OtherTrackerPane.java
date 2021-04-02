package GUI.mainWindow.mainWindowComponent;

import GUI.smallWindows.creationWindows.otherCounter.OtherTrackerOption;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.Random;

public class OtherTrackerPane {
    private static final int DESCRIPTION_COL = 0;
    private static final int TRACKER_COL = 1;
    private static final int DELETE_COL = 2;
    private static final double ROW_HEIGHT = 25;

    private static GridPane gridPane = null;

    private static final ArrayList<Label> labels = new ArrayList<>();
    private static final ArrayList<Node> nodes = new ArrayList<>();
    private static final ArrayList<Button> deleteButtons = new ArrayList<>();


    public static GridPane getGridPane() {
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
        return gridPane;
    }

    public static void createTracker (String description, OtherTrackerOption option, int backgroundWidth) {
        Label descriptionLabel = new Label(description);
        Node node = createNode(option);
        Button deleteButton = createDeleteButton(descriptionLabel, node, null);

        labels.add(descriptionLabel);
        nodes.add(node);
        deleteButtons.add(deleteButton);

        Rectangle background = getBackgroundRectangle(backgroundWidth);


        drawRow(background, descriptionLabel, node, deleteButton);

    }



    //Copied from OtherTrackerOption, for now is the only way to create an exact copy of a Node, since node.clone() is not supported
    private static Node createNode(OtherTrackerOption option){
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
    //TODO sistemare
    private static Button createDeleteButton(Label label, Node tracker, Rectangle background){
        Button button = new Button("X");
        button.setOnAction(actionEvent -> {
            if (background == null) gridPane.getChildren().removeAll(label, tracker, button);
            else gridPane.getChildren().removeAll(label, tracker, button, background);
        });
        return button;
    }

    private static void drawRow(Rectangle background, Label label, Node node, Button deleteButton){
        int row = gridPane.getRowCount();

        if (background != null) gridPane.add(background, DESCRIPTION_COL, row, 3, 1);
        gridPane.add(label, DESCRIPTION_COL, row);
        gridPane.add(node, TRACKER_COL, row);
        gridPane.add(deleteButton, DELETE_COL, row);

    }


    private static Rectangle getBackgroundRectangle(int backgroundWidth){
        if (gridPane.getRowCount()%2 != 0) return null;
        Rectangle rectangle = new Rectangle(backgroundWidth, ROW_HEIGHT);
        rectangle.getStyleClass().add("otherTrackerBackground");
        return rectangle;
    }




    public static void create_a_ton(int width){
        Random random = new Random();
        OtherTrackerOption[] options = OtherTrackerOption.values();

        for (int i = 0; i < 20; i++) {
            Label label = new Label("Create-a-ton n° " + (i+1));
            Node node = createNode(options[random.nextInt(options.length)]);

            Rectangle background = null;
            if (i%2 == 0) {
                background = new Rectangle(width, 20);
                background.setFill(Color.GREEN);
            }

            Button deleteButton = createDeleteButton(label, node, background);


            labels.add(label);
            nodes.add(node);
            deleteButtons.add(deleteButton);



            drawRow(background, label, node, deleteButton);
        }
    }


}
