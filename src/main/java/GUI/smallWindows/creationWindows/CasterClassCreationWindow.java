package GUI.smallWindows.creationWindows;

import GUI.mainWindow.MainWindowGUI;
import hero.magic.casterClass.Caster_Class_Base;
import hero.magic.casterClass.Caster_Class_Prepared;
import hero.magic.casterClass.Caster_Class_Spontaneous;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.text.DecimalFormat;
import java.text.ParsePosition;

//Mostly copied from BuffCreationWindow
public class CasterClassCreationWindow {

    //Variable to read from outside
    private static Stage stage;
    private static boolean confirmPressed;

    //Constant
    private static final int BUTTON_SPACING = 10;
    private static final int RADIO_SPACING = 20;
    private static final int SQUARE_DIMENSION = 400;


    //Variable read from multiple function
    private static final ToggleGroup radioGroup = new ToggleGroup();
    private static final TextField classNameTextField = new TextField();
    private static final TextField levelTextField = new TextField();
    private static final GridPane numberOfSpellGrid = createNumberOfSpellGrid();


    private static GridPane createNumberOfSpellGrid(){
        GridPane pane = new GridPane();
        DecimalFormat format = new DecimalFormat("#");
        format.setMaximumIntegerDigits(0);

        //Gap
        pane.setHgap(5);
        pane.setVgap(10);

        //The grid will be 2 row (50/50) and 5 column (20% each)
        for (int i = 0; i < 5; i++) {
            ColumnConstraints columnConstraints = new ColumnConstraints();
            columnConstraints.setPercentWidth(20);
            pane.getColumnConstraints().add(columnConstraints);
        }
        for (int i = 0; i < 2; i++) {
            RowConstraints rowConstraints = new RowConstraints();
            rowConstraints.setPercentHeight(50);
            pane.getRowConstraints().add(rowConstraints);
        }
        //Add elements in the grid: textfield limited to only number
        for (int row = 0; row < 2; row++) {
            for (int column = 0; column < 5; column++) {
                TextField textField = new TextField();
                textField.setPromptText("Lv. " + pane.getChildren().size());
                textField.setTextFormatter(
                        new TextFormatter<>(c ->
                        {
                            if (c.getControlNewText().isEmpty()) {
                                return c;
                            }

                            ParsePosition parsePosition = new ParsePosition(0);
                            Object object = format.parse(c.getControlNewText(), parsePosition);

                            if (object == null || parsePosition.getIndex() < c.getControlNewText().length()) {
                                return null;
                            } else {
                                return c;
                            }
                        }));
                pane.add(textField, column, row);
            }
        }

        return pane;
    }

    public static boolean isConfirmPressed() {
        return confirmPressed;
    }

    /**
     * Use the data to create a new Caster_Class_Base
     * @return the created Buff
     */
    public static Caster_Class_Base getCaster_Class(){
        int[] array = new int[10];

        for (Node node: numberOfSpellGrid.getChildren()
             ) {
            String valueString = ((TextField) node).getText();
            int value = 0;
            if (!valueString.isEmpty()) value = Integer.parseInt(valueString);
            array[numberOfSpellGrid.getChildren().indexOf(node)] = value;
        }

        //Spontaneous == true
        if ((boolean) radioGroup.getSelectedToggle().getUserData())
            return new Caster_Class_Spontaneous(
                    classNameTextField.getText(),
                    Integer.parseInt(levelTextField.getText()),
                    array
            );
        else //Spontaneous == false
            return new Caster_Class_Prepared(
                    classNameTextField.getText(),
                    Integer.parseInt(levelTextField.getText()),
                    array
            );

    }

    public static void createWindow() {

        ///////////////////////
        //RADIOBUTTON SECTION//
        ///////////////////////

        RadioButton spontaneousRadio = new RadioButton("Spontaneous");
        RadioButton preparedRadio = new RadioButton("Prepared");

        //Setup
        spontaneousRadio.setToggleGroup(radioGroup);
        spontaneousRadio.setUserData(true);
        spontaneousRadio.setSelected(true);
        preparedRadio.setToggleGroup(radioGroup);
        preparedRadio.setUserData(false);

        ////////////////
        //TEXT SECTION//
        ////////////////

        //Clear text area
        classNameTextField.clear();
        levelTextField.clear();

        //Set prompt text
        classNameTextField.setPromptText("Class name");
        levelTextField.setPromptText("Class level");


        //Limit only number in levelTextField
        //https://stackoverflow.com/questions/31039449/java-8-u40-textformatter-javafx-to-restrict-user-input-only-for-decimal-number
        DecimalFormat format = new DecimalFormat("#");
        format.setMaximumIntegerDigits(0);
        levelTextField.setTextFormatter(
                new TextFormatter<>(c ->
                {
                    if (c.getControlNewText().isEmpty()) {
                        return c;
                    }

                    ParsePosition parsePosition = new ParsePosition(0);
                    Object object = format.parse(c.getControlNewText(), parsePosition);

                    if (object == null || parsePosition.getIndex() < c.getControlNewText().length()) {
                        return null;
                    } else {
                        return c;
                    }
                }));


        //////////////////////
        //SPELL GRID SECTION//
        //////////////////////

        numberOfSpellGrid.getChildren().forEach(node -> ((TextField) node).clear());

        //////////////////
        //BUTTON SECTION//
        //////////////////

        Button closeButton = new Button("Close");
        Button confirmButton = new Button("Confirm");

        //Confirm binding
        BooleanBinding confirmBind = Bindings.createBooleanBinding(() -> //Disabled if one is empty
                classNameTextField.getText().isEmpty() || levelTextField.getText().isEmpty(),
                classNameTextField.textProperty(), levelTextField.textProperty()
        );

        confirmButton.disableProperty().bind(confirmBind);

        //SetOnAction setup
        closeButton.setOnAction(actionEvent -> {
            confirmPressed = false;
            stage.close();
        });

        confirmButton.setOnAction(actionEvent -> {
            confirmPressed = true;
            stage.close();
        });


/*
------------------------------------------------------------------------------------------------------------------------
 */
            ///////////////////
            //GRAPHIC SECTION//
            ///////////////////

        /*
        A borderPane will be the Root, with:
        a GridPane as a central element, with Radiobuttons and TextArea + Grid
        an Hbox with Close and Confirm button as bottom element
         */


            ////////////////////
            //GRIDPANE SECTION//
            ////////////////////

            //GridPane setup
            GridPane centralGrid = new GridPane();
            centralGrid.setVgap(10);
            centralGrid.setHgap(5);

            //Radiobutton Setup
            Label buffLabel = new Label("Type of spellcasting: ");
            HBox radioBox = new HBox(RADIO_SPACING);
            radioBox.getChildren().addAll(spontaneousRadio, preparedRadio);
            radioBox.setAlignment(Pos.CENTER_LEFT);
            GridPane.setHalignment(buffLabel, HPos.RIGHT);
            GridPane.setConstraints(buffLabel, 0, 0);
            GridPane.setConstraints(radioBox, 1, 0);
            centralGrid.getChildren().addAll(buffLabel, radioBox);

            //text setup
            GridPane.setConstraints(classNameTextField, 0, 1);
            GridPane.setConstraints(levelTextField, 1, 1);
            centralGrid.getChildren().addAll(classNameTextField, levelTextField);


            //GridPane section
            Label numberOfSpellLabel = new Label("Input here the number of spell you have at your current caster level. By default an empty slot is considered 0");
            numberOfSpellLabel.setWrapText(true);
            centralGrid.add(numberOfSpellLabel, 0, 2, 2, 1);
            centralGrid.add(numberOfSpellGrid, 0, 3, 2, 1);


            //////////////////
            //BOTTOM SECTION//
            //////////////////

            HBox buttonBox = new HBox(BUTTON_SPACING);
            buttonBox.setAlignment(Pos.CENTER_RIGHT);
            buttonBox.getChildren().addAll(closeButton, confirmButton);

            ////////////////
            //ROOT SECTION//
            ////////////////

            BorderPane root = new BorderPane();
            Insets centralMargin = new Insets(10);
            Insets bottomMargin = new Insets(0, 10, 10, 0);
            BorderPane.setMargin(centralGrid, centralMargin);
            BorderPane.setMargin(buttonBox, bottomMargin);
            root.setCenter(centralGrid);
            root.setBottom(buttonBox);

            //////////////////
            //WINDOW SECTION//
            //////////////////

            Scene scene = new Scene(root, SQUARE_DIMENSION, SQUARE_DIMENSION/2.0);
            stage = new Stage();
            scene.getStylesheets().add(MainWindowGUI.getCurrentTheme());
            stage.setTitle("Caster Class Creation");
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.showAndWait();

        }
}
