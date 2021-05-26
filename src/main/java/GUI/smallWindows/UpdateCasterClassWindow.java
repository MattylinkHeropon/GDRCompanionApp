package GUI.smallWindows;

import GUI.mainWindow.MainWindowGUI;
import hero.magic.casterClass.Caster_Class_Base;
import hero.magic.casterClass.Caster_Class_Prepared;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

public class UpdateCasterClassWindow {

    private static final int SQUARE_DIMENSION = 400;

    //This four are declared as class variable hoping that in a future where multiple language will be supported a simple reassignment of them doesn't break the code.
    private static final String BUTTON_NEXT  = "Next >>";
    private static final String BUTTON_BACK  = "Back <<";
    private static final String BUTTON_CONFIRM = "Confirm";
    private static final String BUTTON_CLOSE = "Close";

    public static void createWindow(Caster_Class_Base casterClassToUpdate){
       updateSpellNumber(casterClassToUpdate);
       if (casterClassToUpdate.getClass() == Caster_Class_Prepared.class) changePreparedSpell((Caster_Class_Prepared) casterClassToUpdate);
    }

    ///////////////////////
    //SPONTANEOUS SECTION//
    ///////////////////////

    /**
     * Create a window that let the user change the maximum number of spell per level of the given Caster Class
     * @param casterClassToUpdate class whose number of maximum spell will be updated
     */
    private static void updateSpellNumber(Caster_Class_Base casterClassToUpdate) {
        Stage stage = new Stage();

        //////////////////
        //GRIDPANE SETUP//
        //////////////////

        GridPane centralGrid = new GridPane();
        centralGrid.setHgap(5);
        centralGrid.setVgap(10);
        //column, row


        ///////////////
        //TOP SECTION//
        ///////////////

        Label lvLabel = new Label("Input here your level");
        centralGrid.add(lvLabel, 0, 0);
        Spinner<Integer> levelSpinner = new Spinner<>(0, 20, casterClassToUpdate.getLevel());
        centralGrid.add(levelSpinner, 1,0);

        //////////////////
        //MIDDLE SECTION//
        //////////////////
        Label numberOfSpellLabel = new Label("Input here the maximum number of spell for each level. By default, an empty slot means 0 spell of that level");
        numberOfSpellLabel.setWrapText(true);
        centralGrid.add(numberOfSpellLabel, 0, 1, 2 ,1);
        GridPane numberOfSpellGrid = createNumberOfSpellGrid(casterClassToUpdate.getTotalNumberOfSpell());
        centralGrid.add(numberOfSpellGrid, 0, 2, 2 ,1);

        //////////////////
        //BUTTON SECTION//
        //////////////////

        Button closeButton = new Button("Close");
        Button confirmButton = new Button("Confirm");
        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER_RIGHT);
        buttonBox.getChildren().addAll(closeButton, confirmButton);


        //Button Action
        closeButton.setOnAction(actionEvent -> stage.close());

        confirmButton.setOnAction(actionEvent -> {
            casterClassToUpdate.setLevel(levelSpinner.getValue());
            for (int i = 0; i < numberOfSpellGrid.getChildren().size(); i++) {
                @SuppressWarnings("unchecked")
                Spinner<Integer> spinner = (Spinner<Integer>) numberOfSpellGrid.getChildren().get(i);
                casterClassToUpdate.getTotalNumberOfSpell()[i] = spinner.getValue();
            }
            stage.close();
        });

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
        scene.getStylesheets().add(MainWindowGUI.getCurrentTheme());
        stage.setTitle("Update " + casterClassToUpdate.getClassName());
        stage.setScene(scene);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setResizable(false);
        stage.showAndWait();
    }

    /**
     * Generate a gridPane of Spinner to update the maximum number of spell of a given class
     * @param spellCounterArray array of integer used to set the current value of the spinner
     * @return a GridPane 2x5 of Spinner
     */
    private static GridPane createNumberOfSpellGrid(int[] spellCounterArray){
        GridPane pane = new GridPane();
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

        //Add elements in the grid:
        for (int row = 0; row < 2; row++) {
            for (int column = 0; column < 5; column++) {
                int index = column + row*5;
                Spinner<Integer> spinner = new Spinner<>(0, 30, spellCounterArray[index]);
                pane.add(spinner, column, row);
            }
        }
        return pane;
    }

/*
************************************************************************************************************************
*/

    ////////////////////
    //PREPARED SECTION//
    ////////////////////

    /**
     * Update the prepared spell of a caster class with Prepared spell
     * @param casterClassToUpdate class whose prepared spell will be updated
     */
    public static void changePreparedSpell(Caster_Class_Prepared casterClassToUpdate) {
        //Check if the user can cast at least 1 spell (checking if the sum of all available spell is 0). If not, close
        if (Arrays.stream(casterClassToUpdate.getTotalNumberOfSpell()).sum() == 0) return;

        Stage stage = new Stage();
        VBox mainBox = new VBox(10);

        ///////////////
        //TOP SECTION//
        ///////////////

        Label descriptionLabel = new Label("For each level, input here the name of the prepared spell(s)");
        descriptionLabel.setWrapText(true);
        mainBox.getChildren().add(descriptionLabel);

        //////////////////
        //MIDDLE SECTION//
        //////////////////

        //This variable are NOT class variable because they shouldn't be static
        GridPane[] gridArray = new GridPane[10];
        int firstIndex = -1;
        AtomicInteger currIndex = new AtomicInteger();
        int lastIndex = 0;
        //here we setup the arrayList of GridPane, then show the first GridPane.
        for (int i = 0; i < casterClassToUpdate.getTotalNumberOfSpell().length; i++) {
            if(casterClassToUpdate.getTotalNumberOfSpell()[i] == 0) gridArray[i] = null;
            else {
                gridArray[i] = createPreparedSpellGrid(casterClassToUpdate, i);
                if (firstIndex == -1) firstIndex = i; //if firstIndex is unedited, i is the position of the first valid element of the array. Save it.
                lastIndex = i; //This variable stop to be updated once all the subsequence element are null;
            }
        }
        //update CurrIndex to the first valid index, and get the first valid Grid;
       currIndex.set(firstIndex);
        mainBox.getChildren().add(gridArray[firstIndex]);

        //////////////////
        //BUTTON SECTION//
        //////////////////

        Button closeButton = new Button();
        Button confirmButton = new Button();
        //Button first name setting
        if (firstIndex == lastIndex) { //unique case, not handled by the renaming method: only 1 element
            closeButton.setText(BUTTON_CLOSE);
            confirmButton.setText(BUTTON_CONFIRM);
        } else buttonTextSet(confirmButton, closeButton, firstIndex, currIndex.get(), lastIndex);

        //copied to work under lambda expression
        int finalFirstIndex = firstIndex;
        int finalLastIndex = lastIndex;

        //Button set on action;
        {
            closeButton.setOnAction(actionEvent -> {
                if (closeButton.getText().equals(BUTTON_CLOSE)) stage.close();
                else {
                    currIndex.set(getPrevGridIndex(gridArray, currIndex.get()));
                    mainBox.getChildren().set(1, gridArray[currIndex.get()]);
                    buttonTextSet(confirmButton, closeButton, finalFirstIndex, currIndex.get(), finalLastIndex);
                }
            });

            confirmButton.setOnAction(actionEvent -> {
                if (confirmButton.getText().equals(BUTTON_CONFIRM)) {
                    setSpellName(gridArray, casterClassToUpdate.getSpellNameArray());
                    stage.close();
                } else {
                    currIndex.set(getNextGridIndex(gridArray, currIndex.get()));
                    mainBox.getChildren().set(1, gridArray[currIndex.get()]);
                    buttonTextSet(confirmButton, closeButton, finalFirstIndex, currIndex.get(), finalLastIndex);
                }
            });
        }
        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER_RIGHT);
        buttonBox.getChildren().addAll(closeButton, confirmButton);

        ////////////////
        //ROOT SECTION//
        ////////////////

        BorderPane root = new BorderPane();
        Insets centralMargin = new Insets(10);
        Insets bottomMargin = new Insets(0, 10, 10, 0);
        BorderPane.setMargin(mainBox, centralMargin);
        BorderPane.setMargin(buttonBox, bottomMargin);
        root.setCenter(mainBox);
        root.setBottom(buttonBox);

        //////////////////
        //WINDOW SECTION//
        //////////////////

        Scene scene = new Scene(root, SQUARE_DIMENSION, SQUARE_DIMENSION*2.0);
        scene.getStylesheets().add(MainWindowGUI.getCurrentTheme());
        stage.setTitle("Update " + casterClassToUpdate.getClassName());
        stage.setScene(scene);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setResizable(false);
        stage.showAndWait();
    }

    /**
     * Create the central grid of the prepared spell method for the given level, using the caster class to autofill some slot with already prepared spell of the given level
     * @param casterClassPrepared the given caster class
     * @param currLevel level of spell used to crate the grid
     * @return a complete gridPane with the prepared
     */
    private static GridPane createPreparedSpellGrid(Caster_Class_Prepared casterClassPrepared, int currLevel){
        //GridPane Setup
        GridPane centralGrid = new GridPane();
        centralGrid.setHgap(5);
        centralGrid.setVgap(10);

        //ColumnConstraints Setup
        ColumnConstraints columnConstraints0 = new ColumnConstraints();
        columnConstraints0.setPercentWidth(20);
        ColumnConstraints columnConstraints1 = new ColumnConstraints();
        columnConstraints1.setPercentWidth(80);
        centralGrid.getColumnConstraints().addAll(columnConstraints0, columnConstraints1);

        //column, row
        Label lvLabel = new Label("Lv." + currLevel + " spell:");
        centralGrid.add(lvLabel, 0, 0, 2, 1);

        for (int i = 1; i < casterClassPrepared.getTotalNumberOfSpell()[currLevel] + 1; i++) {
            Label label = new Label(Integer.toString(i));
            centralGrid.add(label, 0, i);
            TextField textField = new TextField();
            try{
                //Get the ArrayList of index "currLevel", and from that try to get the element at index "i-1". If out of bound, set null
                textField.setText(casterClassPrepared.getSpellNameArray()[currLevel].get(i-1));
            } catch (IndexOutOfBoundsException e) {
                textField.setText(null);
            }
            centralGrid.add(textField, 1, i);
        }

        return centralGrid;
    }

    /**
     * Set the name of the prepared spell in the grid to the spellNameArray of the CasterClass
     * @param gridArray array of grid with the names of the spells
     * @param spellNameArray arrayList of String where the name in gridArray will be saved
     */
    private static void setSpellName(GridPane[] gridArray, ArrayList<String>[] spellNameArray) {
        for (int i = 0; i < gridArray.length; i++) {
            ArrayList<String> currSpellNameArray = spellNameArray[i];
            GridPane currGrid = gridArray[i];

            if (currGrid != null) {
                currSpellNameArray.clear();

                for (int j = 2; j < currGrid.getChildren().size() + 1; j = j + 2) {
                    TextField selectedTextFie = (TextField) currGrid.getChildren().get(j);
                    currSpellNameArray.add(selectedTextFie.getText());
                }
            }
        }
    }

    /**
     * Set the correct text in the Confirm and Close button of changePreparedSpell, according to the element of gridArray and the current index
     * @param confirmButton confirm button to which the text will be changed
     * @param closeButton close button to which the text will be changed
     * @param firstIndex index of the first valid GridPane of GridArray
     * @param currIndex index of the current selected GridPane of GridArray
     * @param lastIndex index of the last valid GridPane of GridArray
     */
    private static void buttonTextSet(Button confirmButton, Button closeButton, int firstIndex, int currIndex, int lastIndex){

        if (currIndex == firstIndex){
            closeButton.setText(BUTTON_CLOSE);
            confirmButton.setText(BUTTON_NEXT);
        } else if (currIndex == lastIndex) {
            closeButton.setText(BUTTON_BACK);
            confirmButton.setText(BUTTON_CONFIRM);
        } else {
            closeButton.setText(BUTTON_BACK);
            confirmButton.setText(BUTTON_NEXT);
        }
    }

    /**
     * Get the index of the next valid (not null) GridPane of gridArray
     * @param arrayGrid Array to check
     * @param currIndex index of the current selected element
     * @return index of the next valid GridPane, or -1 if not present (case not handled, should be impossible to reach a state where the function return -1)
     */
    private static int getNextGridIndex(GridPane[] arrayGrid, int currIndex){
        currIndex++;
        for (; currIndex < arrayGrid.length; currIndex++) {
            if (arrayGrid[currIndex] != null) return currIndex;
        }
        return  -1;
    }

    /**
     * Get the index of the previous valid (not null) GridPane of gridArray
     * @param arrayGrid Array to check
     * @param currIndex index of the current selected element
     * @return index of the previous valid GridPane, or -1 if not present (case not handled, should be impossible to reach a state where the function return -1)
     */
    private static int getPrevGridIndex(GridPane[] arrayGrid, int currIndex){
        currIndex--;
        for (; currIndex > 0; currIndex--) {
            if (arrayGrid[currIndex] != null) return currIndex;
        }
        return -1;
    }
}
