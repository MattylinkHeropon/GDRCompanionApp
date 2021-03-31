package GUI.smallWindows.creationWindows;

import GUI.mainWindow.MainWindowGUI;
import hero.Buff;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.text.DecimalFormat;
import java.text.ParsePosition;


public class BuffCreationWindow  {

    //Variable to read from outside
    private static Stage stage;
    private static boolean confirmPressed;

    //Constant
    private static final int BUTTON_SPACING = 10;
    private static final int RADIO_SPACING = 20;
    private static final int SQUARE_DIMENSION = 300;


    //Variable read from multiple function
    private static final ToggleGroup radioGroup = new ToggleGroup();
    private static final TextField casterTextField = new TextField();
    private static final TextField durationTextField = new TextField();
    private static final TextArea descriptionTextArea = new TextArea();



    public static boolean isConfirmPressed() {
        return confirmPressed;
    }

    /**
     * Use the data to create a new Buff
     * @return the created Buff
     */

    public static Buff getBuff(){
        return new Buff(
                (boolean) radioGroup.getSelectedToggle().getUserData(),
                casterTextField.getText(),
                Integer.parseInt(durationTextField.getText()),
                descriptionTextArea.getText());
    }

    public static void createWindow() {

        ///////////////////////
        //RADIOBUTTON SECTION//
        ///////////////////////

        RadioButton buffRadio = new RadioButton("Buff");
        RadioButton debuffRadio = new RadioButton("Debuff");

        //Setup
        buffRadio.setToggleGroup(radioGroup);
        buffRadio.setUserData(true);
        buffRadio.setSelected(true);
        debuffRadio.setToggleGroup(radioGroup);
        debuffRadio.setUserData(false);


        ////////////////
        //TEXT SECTION//
        ////////////////

        //Clear text area
        casterTextField.clear();
        durationTextField.clear();
        descriptionTextArea.clear();

        //Set prompt text
        casterTextField.setPromptText("Caster Name");
        durationTextField.setPromptText("Duration in turn");
        descriptionTextArea.setPromptText("Describe your (de)buff");



         //Limit only number in durationTextField
         //https://stackoverflow.com/questions/31039449/java-8-u40-textformatter-javafx-to-restrict-user-input-only-for-decimal-number
        DecimalFormat format = new DecimalFormat( "#" );
        format.setMaximumIntegerDigits(0);
            durationTextField.setTextFormatter(
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


        //////////////////
        //BUTTON SECTION//
        //////////////////

        Button closeButton = new Button("Close");
        Button confirmButton = new Button("Confirm");

        //Confirm binding
        BooleanBinding confirmBind = Bindings.createBooleanBinding(() -> //Disabled if one is empty
             casterTextField.getText().isEmpty() || durationTextField.getText().isEmpty() || descriptionTextArea.getText().isEmpty(),
            casterTextField.textProperty(), durationTextField.textProperty(), descriptionTextArea.textProperty()
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


        ///////////////////
        //GRAPHIC SECTION//
        ///////////////////

        /*
        A borderPane will be the Root, with:
        a GridPane as a central element, with Radiobuttons and TextArea/field (s)
        a Hbox with Close and Confirm button as botton element
         */


            ////////////////////
            //GRIDPANE SECTION//
            ////////////////////

            //GridPane setup
            GridPane centralGrid = new GridPane();
            centralGrid.setVgap(10);
            centralGrid.setHgap(5);

            //Radiobutton Setup
            Label buffLabel = new Label("Type of buff: ");
            HBox radioBox = new HBox(RADIO_SPACING);
            radioBox.getChildren().addAll(buffRadio, debuffRadio);
            radioBox.setAlignment(Pos.CENTER_LEFT);
            GridPane.setHalignment(buffLabel, HPos.RIGHT);
            GridPane.setConstraints(buffLabel, 0, 0);
            GridPane.setConstraints(radioBox, 1, 0);
            centralGrid.getChildren().addAll(buffLabel, radioBox);

            //text setup
            GridPane.setConstraints(casterTextField, 0, 1);
            GridPane.setConstraints(durationTextField, 1, 1);
            centralGrid.getChildren().addAll(casterTextField, durationTextField);
            descriptionTextArea.setMaxWidth(SQUARE_DIMENSION);
            descriptionTextArea.setWrapText(true);
            centralGrid.add(descriptionTextArea, 0 , 2, 2, 1);


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

            Scene scene = new Scene(root, SQUARE_DIMENSION, SQUARE_DIMENSION);
            stage = new Stage();
            scene.getStylesheets().add(MainWindowGUI.getCurrentTheme());
            stage.setTitle("Buff Creation");
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.showAndWait();

    }


}
