package GUI;

import GUI.creationWindows.BuffCreationWindow;
import GUI.creationWindows.unitCreation.UnitCreationWindow;
import GUI.mainWindowComponent.BuffPane;
import GUI.mainWindowComponent.StatPane;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import hero.Unit;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.lang.reflect.Type;

public class MainWindowGUI extends Application {
    private Unit pg;
    private BorderPane root;
    private static BuffPane buffPane;
    private Stage stage;
    private Scene scene;

    /*
    isLocked is checked every time a MenuItem is called.
    If true, prevent the execution of every other method in the MenuItem.setOnAction.
    Implemented because deleteBuff() doesn't create a new windows (no showAndWait() method), nor resolve instantly, but need an action from the user to resolve
     */



    //Constant Value
    public static final int BUFF_COL_INDEX = 0;
    public static final int DEBUFF_COL_INDEX = 1;
    private static final String LIGHT_THEME = "light_theme.css";
    private static final String DARK_THEME = "dark_theme.css";
    private static final int STD_WIDTH = 800;
    private static final int STD_HEIGHT = 600;
    private static final int CENTRALPANE_ADJUST_TO_FIT = 35;



    //Used to serialize an Unit.
    private static final Type UNIT_TYPE = new TypeToken<Unit>() {}.getType();

    //Variable called by Other
    public static boolean colorBlind = false;
    private static boolean isLocked = false;
    private static boolean isDarkTheme = true;


    public static void lock(){
        isLocked = true;
    }

    public static void unlock(){
        isLocked = false;
    }

    public static boolean isColorBlind() {
        return colorBlind;
    }

    public static String getCurrentTheme() {
        if (isDarkTheme) return DARK_THEME;
        else return LIGHT_THEME;
    }

    @Override
    public void start(Stage stageLocal) {
        stage = stageLocal;
        root = new BorderPane();

        MenuBar menuBar = buildMenuBar();
        root.setTop(menuBar);
        scene = new Scene(root, STD_WIDTH, STD_HEIGHT);
        scene.getStylesheets().add(getCurrentTheme());
        stage.setScene(scene);
        stage.setTitle("GDR Companion App");
        stage.setResizable(false);
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
                if(isLocked) return;
                UnitCreationWindow.CreateWindow();
                if (UnitCreationWindow.isNewUnit()){
                    UnitCreationWindow.setNewUnit(false);
                    loadCharacter_load(new File("data/" + UnitCreationWindow.getName() + ".json"));
                }

            } );

            //item 2: LoadPG
            MenuItem loadPG = new MenuItem("Load Character");
            loadPG.setOnAction(actionEvent -> {
                if (isLocked) return;
                loadCharacter_select();
            });

            //item 3: savePG
            MenuItem savePG = new MenuItem("Update Character");
            savePG.setOnAction(actionEvent -> {
                if (isLocked || pg == null) return;
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
                if (isLocked || pg == null) return;
                BuffCreationWindow.createWindow();
                if (BuffCreationWindow.isConfirmPressed()){
                    buffPane.addBuff(BuffCreationWindow.getBuff());
            }
            });

        //Item 2: Decrease Duration
        MenuItem decreaseDuration = new MenuItem("Decrease Duration");

            decreaseDuration.setOnAction(actionEvent -> {
                if (isLocked || pg == null) return;
                buffPane.decreaseBuffDuration(pg.getBuffArrayList(), BUFF_COL_INDEX);
                buffPane.decreaseBuffDuration(pg.getDebuffArrayList(), DEBUFF_COL_INDEX);
            });

            //Item 3: Remove Buff
            MenuItem removeBuff = new MenuItem("Remove Buff");
            removeBuff.setOnAction(actionEvent -> {
                if (isLocked || pg == null) return;
                buffPane.deleteBuff();
            });
        buffMenu.getItems().addAll(addBuff, decreaseDuration, removeBuff);


         /*
    ------------------------------------------------------------------------------------------------------------------------
     */
        ////////////
        //3: COLOR//
        ////////////
        Menu colorMenu = new Menu("Color");

            //Item 1: Theme Selector
            MenuItem themeSelector = new MenuItem("Switch theme");
            themeSelector.setOnAction( actionEvent -> {
                if(isLocked) return;
                scene.getStylesheets().remove(getCurrentTheme());
                isDarkTheme = !isDarkTheme;
                scene.getStylesheets().add(getCurrentTheme());

            });

            //Item 2: ColorBlindness selector
            MenuItem colorBlindness = new MenuItem("Switch colorblind mode");
            colorBlindness.setOnAction(actionEvent -> {
                if (isLocked) return;
                colorBlind = !colorBlind;
                redrawBuff();

            });

            colorMenu.getItems().addAll(themeSelector, colorBlindness);


    /*
    ------------------------------------------------------------------------------------------------------------------------
     */

        //END
        return new MenuBar(fileMenu, buffMenu, colorMenu);
    }

    //////////////////
    //MENUBAR METHOD//
    //////////////////

    ////////
    //FILE//
    ////////

    /**
     * Let the user select a Unit to load
     */
    private void loadCharacter_select() {
        //First, create a fileChooser and let the user select a Unit
        FileChooser searchUnit = new FileChooser();
        searchUnit.setTitle("Select a character");
        searchUnit.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("json character", "*.json"));
        searchUnit.setInitialDirectory(new File("data"));
        File file = searchUnit.showOpenDialog(stage);
        if (file == null) return;
        loadCharacter_load(file);
    }

    /**
     * Deserialize the given File into a Unit-type object
     * @param file File to be deserialized
     */
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
        redrawBuff();
    }

    /**
     * Redraw buff and debuff Column after a change
     */
    private void redrawBuff(){
        buffPane.redrawColumn(BUFF_COL_INDEX, pg.getBuffArrayList());
        buffPane.redrawColumn(DEBUFF_COL_INDEX, pg.getDebuffArrayList());
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

    ////////////////////
    //AUXILIARY METHOD//
    ////////////////////

    /**
     * This function create the central Pane (a tab Pane), and add to it the first Tab, "buff".
     * This first tab will contain a ScrollPane, that contain a GridPane with the buff and debuff
     * @return the created TabPane
     */
    private TabPane centralPaneCreation() {
        //Buff Tab;
        Tab buffTab = new Tab("(De)Buff");
        //TODO: Right now created based on the un-resizable property of the Scene
        buffPane = new BuffPane(STD_WIDTH - StatPane.GRID_WIDTH - CENTRALPANE_ADJUST_TO_FIT, pg);
        GridPane buffGridPane = buffPane.getBuffPane();

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