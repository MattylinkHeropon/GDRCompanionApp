package GUI.smallWindows.creationWindows;


import GUI.mainWindow.MainWindowGUI;
import hero.Enum.OtherTrackerOption;
import hero.otherTracker.OtherTracker_Base;
import hero.otherTracker.OtherTracker_Spinner;
import hero.otherTracker.OtherTracker_ToggleBox;
import javafx.beans.binding.Bindings;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.RowConstraints;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class OtherTrackerCreationWindow {

    //Read from outside
    private static boolean confirmPressed;
    private static OtherTrackerOption currOption = null;
    private static final OtherTrackerOption[] options = OtherTrackerOption.values();
    private static String description;

    //Window Size value
    private static final int WIDTH = 400;
    private static double height = 0;

    //The following constants are set after analyzing a screen of the Grid with GridLines Visible, with WIDTH = 400. The measurement unit are Pixel (px)
    private static final double ROW_INTRO_HEIGHT = 34;
    private static final double ROW_PRE_SPINNER_HEIGHT = 24; //it's 10 less then ROW_INTRO because doesn't have the Insects
    private static final double ROW_SPINNER_HEIGHT = 15;
    private static final double ROW_BUTTON_HEIGHT = 40;
    private static final double GRID_VGAP = 10;

    public static boolean isConfirmPressed() {
        return confirmPressed;
    }

    public static OtherTracker_Base createTracker(){
        confirmPressed = false;
        switch (currOption){
            case SPINNER: return new OtherTracker_Spinner(description);
            case TOGGLE_BUTTON: return new OtherTracker_ToggleBox(description);
        }
        return null;
    }

    public static void createWindow(){
        //Value reset
        currOption = null;
        height = 0;

        //Margin
        Insets leftRightMargin = new Insets(0, 10, 0, 10);

        //Root Setup
        GridPane root = new GridPane();
        root.setVgap(GRID_VGAP);
        ColumnConstraints columnConstraint0 = new ColumnConstraints();
        columnConstraint0.setPercentWidth(50);
        ColumnConstraints columnConstraint1 = new ColumnConstraints();
        columnConstraint1.setPercentWidth(50);
        root.getColumnConstraints().addAll(columnConstraint0, columnConstraint1);

        //Top row
        Label introLabel = new Label("Please select what kind of counter you want, and input a descriptor");
        introLabel.setWrapText(true);
        GridPane.setMargin(introLabel, new Insets(GRID_VGAP,GRID_VGAP,0,GRID_VGAP));
        addRowConstrain(root, ROW_INTRO_HEIGHT);
        root.add(introLabel, 0, 0, 2, 1);
        height = height + ROW_INTRO_HEIGHT + GRID_VGAP;

        //left column: description
        TextField descriptionField = new TextField();
        descriptionField.setPromptText("Input here a description");
        GridPane.setMargin(descriptionField, leftRightMargin);
        addRowConstrain(root, ROW_PRE_SPINNER_HEIGHT);
        root.add(descriptionField, 0, 1);
        height = height + ROW_PRE_SPINNER_HEIGHT + GRID_VGAP;

        //left column: radioButton
        ToggleGroup radioGroup = new ToggleGroup();
        createRadioButton(radioGroup, root, leftRightMargin);

        //right column
        Label exampleLabel = new Label("Here an example of the selected tracker");
        exampleLabel.setWrapText(true);
        GridPane.setMargin(exampleLabel, leftRightMargin);
        root.add(exampleLabel, 1, 1);


        //bottom row
        HBox buttonBox = new HBox(10);
        Button closeButton = new Button("Close");
        Button confirmButton = new Button("Confirm");
        buttonBox.setAlignment(Pos.CENTER_RIGHT);
        buttonBox.getChildren().addAll(closeButton, confirmButton);
        GridPane.setMargin(buttonBox, new Insets(GRID_VGAP));
        root.add(buttonBox, 0, OtherTrackerOption.values().length + 3, 2, 1);
        height = height + ROW_BUTTON_HEIGHT + GRID_VGAP;

        //SetOnAction Button setup
        Stage stage = new Stage();
        closeButton.setOnAction(actionEvent -> {
            confirmPressed = false;
            stage.close();
        });

        confirmButton.setOnAction(actionEvent -> {
            confirmPressed = true;
            description = descriptionField.getText();
            stage.close();
        });


        //Bind confirmButton
        confirmButton.disableProperty().bind(Bindings.createBooleanBinding(()->
            descriptionField.getText().isEmpty(),
            descriptionField.textProperty()
        ));


        //////////////////
        //WINDOW SECTION//
        //////////////////

        Scene scene = new Scene(root, WIDTH, height);
        scene.getStylesheets().add(MainWindowGUI.getCurrentTheme());
        stage.setTitle("Other Counter Creation");
        stage.setScene(scene);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setResizable(false);
        stage.showAndWait();
    }

    /**
     * Add a rowConstrain of the given pixel to the Gridpane
     * @param root GridPane to which RowConstraints will be attached
     * @param rowIntroHeight Size of the RowConstraints
     */
    private static void addRowConstrain(GridPane root, double rowIntroHeight) {
        RowConstraints rowConstraints = new RowConstraints();
        rowConstraints.setMinHeight(rowIntroHeight);
        root.getRowConstraints().add(rowConstraints);
    }

    /**
     * Iterate through every OtherTrackerOption element and create a RadioButton linked to it.
     * During the creation, every RadioButton will be added to the left column of the grid, and when selected will change the example node on the right column.
     * This method also let the UI scale with every RadioButton added
     * @param radioGroup Group to witch attach every RadioButton
     * @param root GridPane where a RadioButton will be added
     * @param margin Insets of every RadioButton
     */
    private static void createRadioButton(ToggleGroup radioGroup, GridPane root, Insets margin) {
        int row = 2;
        for (OtherTrackerOption option: options
        ) {
            RadioButton button = new RadioButton(option.getName());
            radioGroup.getToggles().add(button);
            button.setOnAction(actionEvent -> changeNode(root, option));
            GridPane.setMargin(button, margin);
            root.add(button, 0, row);
            height = height + ROW_SPINNER_HEIGHT + GRID_VGAP;
            if (row == 2){ //set first node as default
                button.setSelected(true);
                changeNode(root, option);
            }
            row++;
        }
    }

    /**
     * Called when a RadioButton is selected:
     * create a correct node to represent the option associated to the selected RadioButton, and add it to the right column.
     * @param grid GridPane where the Node will be added.
     * @param toAdd Option associated to a RadioButton.
     */
    private static void changeNode(GridPane grid, OtherTrackerOption toAdd) {
        if (currOption != null) grid.getChildren().remove(currOption.getNode());
        currOption = toAdd;
        Node node = toAdd.getNode();
        GridPane.setHalignment(node, HPos.CENTER);
        GridPane.setValignment(node, VPos.CENTER);
        grid.add(node, 1, 2, 1, options.length);
    }
}
