package GUI.smallWindows;

import GUI.mainWindow.MainWindowGUI;
import hero.magic.casterClass.Caster_Class_Base;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class UpdateCasterClassWindow {

    private static final int SQUARE_DIMENSION = 400;

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

    public static void createWindow(Caster_Class_Base casterClassToUpdate){

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
        Label numberOfSpellLabel = new Label("TODO: pensare a che scriverci");
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


        ////////////////////////
        //BUTTON SET_ON_ACTION//
        ////////////////////////
        Stage stage = new Stage();
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
        stage.setTitle("Caster Class Update");
        stage.setScene(scene);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setResizable(false);
        stage.showAndWait();





    }
}
