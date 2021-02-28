package GUI;

import GUI.creationWindows.buffHandler.BuffCreationWindow;
import GUI.creationWindows.unitCreation.UnitCreationWindow;
import GUI.mainWindow.StatPane;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import hero.Buff;
import hero.Unit;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;


public class MainGUI  extends Application {
    private Unit pg;
    private BorderPane root;
    private static GridPane buffGridPane; //Saved reference for easier access;
    private Stage stage;
    private Scene scene;

    //Constant Value
    private static final int BUFF_COL_INDEX = 0;
    private static final int DEBUFF_COL_INDEX = 1;
    private static final int STD_WIDTH = 800;
    private static final int STD_HEIGHT = 600;
    private static final int CENTRALPANE_ADJUST_TO_FIT = 35;

    //Used to serialize an Unit.
    private static final Type UNIT_TYPE = new TypeToken<Unit>() {}.getType();



    @Override
    public void start(Stage stageLocal) {
        stage = stageLocal;

        root = new BorderPane();
        MenuBar menuBar = buildMenuBar();
        root.setTop(menuBar);
        scene = new Scene(root, STD_WIDTH, STD_HEIGHT);
        stage.setScene(scene);
        stage.setTitle("GDR Companion App");
        stage.show();

    }

    private void loadGUI(){
        //clear root
        root.getChildren().removeAll(root.getLeft(), root.getRight());

        //////////////////
        //BUTTON SECTION//
        //////////////////


        //////////////////////
        //BORDERPANE SECTION//
        //////////////////////

            ///////
            //TOP//
            ///////

            /////////
            //RIGHT//
            /////////
            StatPane statPane = new StatPane(pg);
            VBox statNode = statPane.getCompleteBox();
            //setup
            root.setLeft(statNode);
            BorderPane.setMargin(statNode, new Insets(10));
            BorderPane.setAlignment(statNode, Pos.CENTER);

            //////////
            //CENTER//
            //////////

            TabPane centralPane = centralPaneCreation();
            centralPane.setMaxSize(STD_WIDTH - StatPane.GRID_WIDTH, STD_HEIGHT);
            root.setCenter(centralPane);

            ////////
            //LEFT//
            ////////

            //////////
            //BOTTOM//
            //////////

        /////////////////
        //SCENE SECTION//
        /////////////////

        stage.setScene(scene);

        stage.setTitle(pg.getName());
        stage.show();
    }



    ///////////
    //MENUBAR//
    ///////////

    /**
     * This Method contains all the Menu and their MenuItem.
     * When called, build a MenuBar with all the Menu and their Item.
     * The setOnActionMethod of the MenuItem will most likely call another method
     * @return the created Menu
     */

    private MenuBar buildMenuBar(){

        ///////////
        //1: FILE//
        ///////////
        Menu fileMenu = new Menu("File");

            //item 1: CreatePG
            MenuItem createPG = new MenuItem("New Character");
            createPG.setOnAction(actionEvent ->{
                UnitCreationWindow.CreateWindow();
                if (UnitCreationWindow.isNewUnit()){
                    UnitCreationWindow.setNewUnit(false);
                    loadCharacter_load(new File("data/" + UnitCreationWindow.getName() + ".json"));
                }

            } );

            //item 2: LoadPG
            MenuItem loadPG = new MenuItem("Load Character");
            loadPG.setOnAction(actionEvent -> loadCharacter_select());

            //item 3: savePG
            MenuItem savePG = new MenuItem("Update Character");
            savePG.setOnAction(actionEvent -> {
                try {
                    saveCharacter(pg, new File("data/" + pg.getName() + ".json"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

        fileMenu.getItems().addAll(createPG, loadPG, savePG);
    /*
    ------------------------------------------------------------------------------------------------------------------------
     */
        ///////////
        //2: BUFF//
        ///////////
        Menu buffMenu = new Menu("Buff");

            //Item 1: Add Buff
            MenuItem addBuff = new MenuItem("Add Buff");
            addBuff.setOnAction(actionEvent -> {
                BuffCreationWindow.createWindow();
                if (BuffCreationWindow.isConfirmPressed()){
                   addBuff(BuffCreationWindow.getBuff());
                }
            });

            //Item 2: Random Buff *10
            //ONLY USED FOR DEBUG REASON
            MenuItem randomBuff = new MenuItem("Random Buff * 10");
            randomBuff.setOnAction(actionEvent -> {
                for (int i = 1; i < 11; i++) {
                    addBuff(new Buff(i%2 == 0, "Random", i, Integer.toString(i)));
                }
            });

            //Item 3: Decrease Duration
            MenuItem decreaseDuration = new MenuItem("Decrease Duration");


            decreaseDuration.setOnAction(actionEvent -> {
                decreaseBuffDuration(pg.getBuffArrayList(), BUFF_COL_INDEX);
                decreaseBuffDuration(pg.getDebuffArrayList(), DEBUFF_COL_INDEX);
            });

            //Item 4: Remove Buff
            MenuItem removeBuff = new MenuItem("Remove Buff");
            removeBuff.setOnAction(actionEvent -> deleteBuff());
        buffMenu.getItems().addAll(addBuff, randomBuff, decreaseDuration, removeBuff);
    /*
    ------------------------------------------------------------------------------------------------------------------------
     */

        //END
        return new MenuBar(fileMenu, buffMenu);
    }



    //////////////////
    //MENUBAR METHOD//
    //////////////////

    ////////
    //FILE//
    ////////
    private void loadCharacter_select() {
        //First, create a fileChooser and let the user select a Unit
        FileChooser searchUnit = new FileChooser();
        searchUnit.setTitle("Select a character");
        searchUnit.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("json character", "*.json"));
        searchUnit.setInitialDirectory(new File("data"));
        File file = searchUnit.showOpenDialog(stage);
        loadCharacter_load(file);
    }

    //file selected: deserialization
    private void loadCharacter_load(File file){
        Gson gson = new Gson();
        FileReader fileReader = null;
        try {
            fileReader = new FileReader(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        assert fileReader != null;
        pg = gson.fromJson(fileReader, UNIT_TYPE);
        loadGUI();
        //Draw all the buff associated with the unit
        redrawColumn(BUFF_COL_INDEX, pg.getBuffArrayList());
        redrawColumn(DEBUFF_COL_INDEX, pg.getDebuffArrayList());


    }

    /**
     * Override the given File with the a set of data  of the unit in json format
     * @param unit Unit that will be saved in the file
     * @param file A file to be written
     * @throws IOException error with the file
     */
    public static void saveCharacter(Unit unit, File file) throws IOException {

        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        FileWriter fileWriter = new FileWriter(file);

        fileWriter.write(gson.toJson(unit, UNIT_TYPE));
        fileWriter.flush();
        fileWriter.close();
    }



    ////////
    //BUFF//
    ////////
    /**
     * If the list is not empty, decrease by one the duration of every buff of the list, and if a buff duration became
     * zero, remove it from the list.
     * At the end of dhe decrease process, **if the list was modified** NO, it does it always, remove every node from the buffGridPane with the
     * buffMask of the list. Then, if the new list is not empty, redraw every buffMask in the buffGridPane
     * @param buffList ArrayList with the Buff to be decreased
     * @param colIndex Index to represent the column of the buff in the GUI (0 Buff, 1 Debuff)
     */
    //Note: this method could be better, ideally shouldn't redraw the entire buff list every time it's modified
    private void decreaseBuffDuration(ArrayList<Buff> buffList, int colIndex){
        if (buffList.isEmpty()) return; //empty array, close

        //Analysis and removal
        buffList.forEach(Buff::decreaseDuration);
        buffList.removeIf(Buff -> Buff.getDuration() == 0);

        //ArrayList modified, update GridPane
        redrawColumn(colIndex, buffList);
    }

    /**
     * Delete a buff
     */
    private void deleteBuff(){
        if (buffGridPane.getChildren().isEmpty()) return;
        ArrayList<Button> closeButtonList = new ArrayList<>();

        //Set variable used to create the buttons
        Color closeButtonFill = Color.web("#808080", 0.5); //At the moment the color is Gray, 50% opacity
        BackgroundFill closeButtonBackground = new BackgroundFill(closeButtonFill, new CornerRadii(10), null);

        for (Node node: buffGridPane.getChildren()
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
                buffGridPane.getChildren().removeAll(closeButtonList);
            });

        }

        //set every button on the grid, using the list to prevent ConcurrentModificationError
        for (Button button: closeButtonList
             ) {
            buffGridPane.getChildren().add(button);
        }

        /*
         * TODO: QUI UN'IDEA SU COME IMPEDIRE LA CHIAMATA DI ALTRI METODI DURANTE L'ESEGUZIONE DI QUESTO, APPLICABILE ANCHE AD ALTRI NEL CASO.
         * Creare un lock globale, idealmente un bool, e fare in modo che quando una funzione è in eseguzione blocchi la chiamata delle altre (es con un if)
         * L'idea di base è quella di un semaforo, solo senza andare a toccare il multithreading, anche perché sarebbe complicato visto che nemmeno i metodi nativi
         * funzionano come vorrei
         */
    }


    ////////////////////
    //AUXILIARY METHOD//
    ////////////////////

    /**
     * Add the given Buff to the pg, then add it to the GUI
     * @param buff the buff instance that will be added to the pg and the GUI
     */
    private void addBuff(Buff buff){
        pg.addBuff(buff);
        drawBuffMask(buff);
    }

    /**
     * First, clear or Node in the GridPane from the selected column, then redraw the entire column using the given list
     * @param colIndex Index of the column to remove
     * @param list List used to repopulate the column
     */
    private void redrawColumn(int colIndex, ArrayList<Buff> list){
        buffGridPane.getChildren().removeIf(node -> GridPane.getColumnIndex(node) == colIndex);
        if (!list.isEmpty()) {
            list.forEach(this::drawBuffMask);
        }
    }

    /**
     * Create a mask from the given Buff and put it to the buffGridPane, while setting the correct constrain and margin
     * @param buff Buff instance to be added
     */

    private void drawBuffMask (Buff buff) {
        Insets buff_margin = new Insets(0,0,0,20);
        Insets debuff_margin = new Insets(0,20,0,0);
        int col, row;

        GridPane buffMask = buff.createBuffMask();

        if (buff.isBuff()) {
            col = BUFF_COL_INDEX;
            row = pg.getBuffArrayList().indexOf(buff);
            GridPane.setMargin(buffMask, buff_margin);
        }
        else {
            col = DEBUFF_COL_INDEX;
            row = pg.getDebuffArrayList().indexOf(buff);
            GridPane.setMargin(buffMask, debuff_margin);
        }

        GridPane.setConstraints(buffMask, col, row);
        buffGridPane.getChildren().add(buffMask);
    }

    /**
     * This function create the central Pane (a tab Pane), and add to it the first Tab, "buff".
     * This first tab will contain a ScrollPane, that contain a GridPane with the buff and debuff
     * @return the created TabPane
     */
    private TabPane centralPaneCreation() {
        //Buff Tab;
        Tab buffTab = new Tab("(De)Buff");

        //gridPane setup
        buffGridPane = new GridPane();
        buffGridPane.setHgap(11);
        buffGridPane.setVgap(5);
        buffGridPane.setMaxWidth(STD_WIDTH - StatPane.GRID_WIDTH - CENTRALPANE_ADJUST_TO_FIT);

        //GridPane constrain
        ColumnConstraints buffColumnConstrain = new ColumnConstraints();
        buffColumnConstrain.setPercentWidth(50);
        ColumnConstraints debuffColumnConstrain = new ColumnConstraints();
        debuffColumnConstrain.setPercentWidth(50);
        buffGridPane.getColumnConstraints().addAll(buffColumnConstrain, debuffColumnConstrain);

        //scrollPane setup
        ScrollPane buffScroll = new ScrollPane();
        buffScroll.setContent(buffGridPane);

        //buffTab setup
        buffTab.setContent(buffScroll);
        buffTab.setClosable(false);

        //END
        return new TabPane(buffTab);
    }


    public static void main(String[] args) {
        launch();
    }

}