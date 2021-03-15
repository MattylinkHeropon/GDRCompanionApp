package GUI.mainWindowComponent;

import GUI.MainWindowGUI;
import hero.Buff;
import hero.Unit;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.util.ArrayList;

public class BuffPane {
    private final GridPane buffPane;
    private final Unit pg;


    public BuffPane(int width, Unit pg){
        this.pg = pg;

        //GridPane setup
        buffPane = new GridPane();
        buffPane.setHgap(11);
        buffPane.setVgap(5);
        buffPane.setMaxWidth(width);

        //GridPane constrain
        ColumnConstraints buffColumnConstrain = new ColumnConstraints();
        buffColumnConstrain.setPercentWidth(50);
        ColumnConstraints debuffColumnConstrain = new ColumnConstraints();
        debuffColumnConstrain.setPercentWidth(50);
        buffPane.getColumnConstraints().addAll(buffColumnConstrain, debuffColumnConstrain);
    }

    public GridPane getBuffPane() {
        return buffPane;
    }


    /**
     * Add the given Buff to the pg, then add it to the GUI
     * @param buff the buff instance that will be added to the pg and the GUI
     */
    public void addBuff(Buff buff){
        pg.addBuff(buff);
        drawBuffMask(buff);
    }

    /**
     * Delete a buff
     */
    public void deleteBuff(){
        if (buffPane.getChildren().isEmpty()) return;

        MainWindowGUI.lock();

        ArrayList<Button> closeButtonList = new ArrayList<>();

        //Set variable used to create the buttons
        Color closeButtonFill = Color.web("#808080", 0.5); //At the moment the color is Gray, 50% opacity
        BackgroundFill closeButtonBackground = new BackgroundFill(closeButtonFill, new CornerRadii(10), null);

        for (Node node: buffPane.getChildren()
        ) {
            //Create the button
            Button button = new Button("click to delete");
            button.setTextFill(Color.WHITE);
            closeButtonList.add(button);

            //set dimension and Background
            button.setMinSize(node.getLayoutBounds().getWidth(), node.getLayoutBounds().getHeight());
            button.setBackground(new Background(closeButtonBackground));

            //set constrain to add it to the grid:
            int col = GridPane.getColumnIndex(node);
            GridPane.setConstraints(button, col , GridPane.getRowIndex(node));
            //for the buff column, we need to shift the button 20 pixel to the right to match the Node below
            if (col == 0) GridPane.setMargin(button, new Insets(0,0,0,20));

            //set behaviour
            button.setOnAction(actionEvent -> {
                ArrayList<Buff> list;
                //Select the correct list from where we remove the BuffMask
                if (col == 0) list = pg.getBuffArrayList();
                else list = pg.getDebuffArrayList();
                list.removeIf(buff -> buff.getMaskHash() == node.hashCode());
                redrawColumn(col, list);

                //Remove all button from the GridPane to "close" the function
                buffPane.getChildren().removeAll(closeButtonList);
                MainWindowGUI.unlock();
            });

        }

        //set every button on the grid, using the list to prevent ConcurrentModificationError
        for (Button button: closeButtonList
        ) {
            buffPane.getChildren().add(button);
        }
    }

    /**
     * If the list is not empty, decrease by one the duration of every buff of the list, and if a buff duration became
     * zero, remove it from the list.
     * At the end of dhe decrease process, **if the list was modified** NO, it does it always, remove every node from the buffGridPane with the
     * buffMask of the list. Then, if the new list is not empty, redraw every buffMask in the buffGridPane
     * @param buffList ArrayList with the Buff to be decreased
     * @param colIndex Index to represent the column of the buff in the GUI (0 Buff, 1 Debuff)
     */
    //Note: this method could be better, ideally shouldn't redraw the entire buff list every time it's modified
    public void decreaseBuffDuration(ArrayList<Buff> buffList, int colIndex){
        if (buffList.isEmpty()) return; //empty array, close

        //Analysis and removal
        buffList.forEach(Buff::decreaseDuration);
        buffList.removeIf(Buff -> Buff.getDuration() == 0);

        //ArrayList modified, update GridPane
        redrawColumn(colIndex, buffList);
    }

    /**
     * First, clear or Node in the GridPane from the selected column, then redraw the entire column using the given list
     * @param colIndex Index of the column to remove
     * @param list List used to repopulate the column
     */
    public void redrawColumn(int colIndex, ArrayList<Buff> list){
        buffPane.getChildren().removeIf(node -> GridPane.getColumnIndex(node) == colIndex);
        if (!list.isEmpty()) {
            list.forEach(this::drawBuffMask);
        }
    }


    /**
     * Create a mask from the given Buff and put it to the buffGridPane, while setting the correct constrain and margin
     * @param buff Buff instance to be added
     */
    public void drawBuffMask (Buff buff) {
        Insets buff_margin = new Insets(0,0,0,20);
        Insets debuff_margin = new Insets(0,20,0,0);
        int col, row;

        GridPane buffMask = buff.createBuffMask();

        if (buff.isBuff()) {
            col = MainWindowGUI.BUFF_COL_INDEX;
            row = pg.getBuffArrayList().indexOf(buff);
            GridPane.setMargin(buffMask, buff_margin);
        }
        else {
            col = MainWindowGUI.DEBUFF_COL_INDEX;
            row = pg.getDebuffArrayList().indexOf(buff);
            GridPane.setMargin(buffMask, debuff_margin);
        }

        GridPane.setConstraints(buffMask, col, row);
        buffPane.getChildren().add(buffMask);
    }



}
