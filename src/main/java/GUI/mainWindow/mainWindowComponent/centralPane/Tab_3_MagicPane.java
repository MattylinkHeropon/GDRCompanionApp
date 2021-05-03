package GUI.mainWindow.mainWindowComponent.centralPane;

import GUI.mainWindow.MainWindowGUI;
import GUI.smallWindows.UpdateCasterClassWindow;
import hero.Unit;
import hero.magic.casterClass.Caster_Class_Base;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.util.ArrayList;

public class Tab_3_MagicPane {
    private static final GridPane CLASS_GRID = buildGrid();
    private static int column = 0;
    private static int row = 0;


    private static Unit unit;

    private static GridPane buildGrid(){
        GridPane gridPane = new GridPane();
        gridPane.setVgap(5);
        gridPane.setHgap(5);
        //Gridpane Constrain
        ColumnConstraints columnConstraint0 = new ColumnConstraints();
        columnConstraint0.setPercentWidth(50);
        ColumnConstraints columnConstraint1 = new ColumnConstraints();
        columnConstraint1.setPercentWidth(50);
        gridPane.getColumnConstraints().addAll(columnConstraint0, columnConstraint1);

        return gridPane;
    }

    public static GridPane getGridPane() {
        return CLASS_GRID;
    }

    public static void setUnit(Unit unit) {
        Tab_3_MagicPane.unit = unit;
        redrawGrid();
    }

    public static void addClass (Caster_Class_Base casterClass){
        unit.getCasterClassList().add(casterClass);
        drawCasterClass(casterClass.buildClassMask());
    }

    private static void drawCasterClass(VBox box){
        CLASS_GRID.add(box, column, row);
        if(column == 0) column++;
        else {
            column = 0;
            row++;
        }
    }

    public static void selectClass(boolean isDelete){
        if (CLASS_GRID.getChildren().isEmpty()) return;

        MainWindowGUI.lock();

        ArrayList<Button> closeButtonList = new ArrayList<>();

        for (Node node: CLASS_GRID.getChildren()
        ) {
            //Create button
            Button button = new Button("click to select a class");
            button.setTextFill(Color.WHITE);
            closeButtonList.add(button);

            //set dimension and Background
            button.setMinSize(node.getLayoutBounds().getWidth(), node.getLayoutBounds().getHeight());
            button.getStyleClass().add("remove-buff"); //TODO fare maschera personale

            //set constrain to add it to the grid:
            int col = GridPane.getColumnIndex(node);
            GridPane.setConstraints(button, col , GridPane.getRowIndex(node));

            //set behaviour
            button.setOnAction(actionEvent -> {
                //get the index of the selected casterClass
                int index = GridPane.getRowIndex(button)*2 + col;

                //If the function is called with the flag "isDelete" true, remove the element
                if (isDelete) unit.getCasterClassList().remove(index);
                else UpdateCasterClassWindow.createWindow(unit.getCasterClassList().get(index));
                redrawGrid();

                //Remove all button from the GridPane to "close" the function
                CLASS_GRID.getChildren().removeAll(closeButtonList);
                MainWindowGUI.unlock();
            });
        }

        //set every button on the grid, using the list to prevent ConcurrentModificationError
        for (Button button: closeButtonList
        ) {
            CLASS_GRID.getChildren().add(button);
        }
    }

    private static void redrawGrid(){
        CLASS_GRID.getChildren().clear();
        row = 0;
        column = 0;
        if (unit.getCasterClassList().isEmpty()) return;
        unit.getCasterClassList().stream().map(Caster_Class_Base::buildClassMask).forEach(Tab_3_MagicPane::drawCasterClass);
    }


    public static void reset() {
        unit.getCasterClassList().forEach(Caster_Class_Base::resetSlot);
        redrawGrid();
    }
}
