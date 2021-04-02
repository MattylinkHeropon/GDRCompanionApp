package GUI.mainWindow;

import GUI.mainWindow.mainWindowComponent.OptionPane;
import GUI.mainWindow.mainWindowComponent.OtherTrackerPane;
import GUI.smallWindows.creationWindows.BuffCreationWindow;
import GUI.smallWindows.creationWindows.otherCounter.OtherTrackerCreationWindow;
import GUI.smallWindows.creationWindows.unitCreation.UnitCreationWindow;
import GUI.mainWindow.mainWindowComponent.BuffPane;
import GUI.mainWindow.mainWindowComponent.StatPane;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import hero.Unit;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.lang.reflect.Type;

public class MainWindowGUI extends Application {
    private static Unit unit;
    private BorderPane root;
    private static BuffPane buffPane;
    private Stage stage;
    private static Scene scene;

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
    private static boolean colorBlind = false;
    private static boolean isLocked = false;
    private static boolean darkThemeSelected = true;


    public static void lock(){
        isLocked = true;
    }

    public static void unlock(){
        isLocked = false;
    }

    public static boolean isDarkThemeSelected() {
        return darkThemeSelected;
    }

    public static boolean isColorBlind() {
        return colorBlind;
    }

    public static String getCurrentTheme() {
        if (darkThemeSelected) return DARK_THEME;
        else return LIGHT_THEME;
    }

    @Override
    public void start(Stage stageLocal) {
        stage = stageLocal;
        root = new BorderPane();

        MenuBar menuBar = buildMenuBar();
        root.setTop(menuBar);
        VBox leftBox = new VBox();
        root.setLeft(leftBox);
        TabPane tabPane = buildCentralPane();
        root.setCenter(tabPane);


        scene = new Scene(root, STD_WIDTH, STD_HEIGHT);
        scene.getStylesheets().add(getCurrentTheme());
        stage.setScene(scene);
        stage.setTitle("GDR Companion App");
        stage.setResizable(false);
        stage.show();


        //TODO:Debug line
        //loadCharacter_load(new File("data/Charles.json"));
        printDimension("Root", root);
        printDimension("Root.Top",  root.getTop());
        printDimension("Root.Left",  root.getLeft());
        printDimension("Root.Center",  root.getCenter());
        /*
        printDimension("Root.Right", (Region) root.getRight());
        printDimension("Root.Bottom", (Region) root.getBottom());

         */
        }


    private static void printDimension( String text, Node node){
        System.out.println(text + ": " + (int) node.getBoundsInParent().getWidth() + " X " + (int) node.getBoundsInParent().getHeight() + " (" + node.getClass() + ")");
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
            StatPane statPane = new StatPane(unit);
            VBox statNode = statPane.getCompleteBox();
            //setup
            root.setLeft(statNode);
            BorderPane.setMargin(statNode, new Insets(10));
            BorderPane.setAlignment(statNode, Pos.CENTER);

            //////////
            //CENTER//
            //////////

            TabPane centralPane = buildCentralPane();
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

        stage.setTitle(unit.getName());
        stage.show();
    }

    ///////////
    //TABPANE//
    ///////////

    /**
     * This function create the central Pane (a tab Pane), and add to it the first Tab, "buff".
     * This first tab will contain a ScrollPane, that contain a GridPane with the buff and debuff
     * @return the created TabPane
     */
    private TabPane buildCentralPane() {
        int tabWidth = STD_WIDTH - StatPane.GRID_WIDTH - CENTRALPANE_ADJUST_TO_FIT;
        ///////////
        //1: BUFF//
        ///////////

        Tab buffTab = new Tab("(De)Buff");
        //TODO: Right now created based on the un-resizable property of the Scene
        buffPane = new BuffPane(tabWidth, unit);
        GridPane buffGridPane = buffPane.getBuffPane();

        //scrollPane setup
        ScrollPane buffScroll = new ScrollPane();
        buffScroll.setContent(buffGridPane);

        //buffTab setup
        buffTab.setContent(buffScroll);
        buffTab.setClosable(false);

        //
        //2: Other Counter//
        //

        Tab otherTab = new Tab("Other");
        GridPane otherPane = OtherTrackerPane.getGridPane();
        Button createTrackerButton = new Button("Create Tracker");
        createTrackerButton.setOnAction(actionEvent -> {
            OtherTrackerCreationWindow.createWindow();
            if (OtherTrackerCreationWindow.isConfirmPressed()){
                OtherTrackerPane.createTracker(OtherTrackerCreationWindow.getDescription(), OtherTrackerCreationWindow.getCurrOption(), tabWidth);
            }
        });

        Button testButton = new Button("Create-a-ton");
        testButton.setOnAction(actionEvent -> OtherTrackerPane.create_a_ton(tabWidth));



        otherTab.setClosable(false);
        otherTab.setDisable(false);
        VBox box = new VBox(10);
        box.getChildren().addAll(otherPane, createTrackerButton,testButton);

        otherTab.setContent(box);
        /*
        Sarà da sistemare, comunque qui permetto all'utente di salvare counter vari ed eventuali, come ad esempio buff attivi/disattivati
        Contatori di turni per creature ed evocazioni, e simili.
        Li devo fare statici legati al pg?
         */

        ////////////
        //3: MAGIC//
        ////////////

        Tab magicTab = new Tab("Magic");
        magicTab.setClosable(false);
        magicTab.setDisable(true);

        ////////////////
        //4: SPELLLIST//
        ////////////////

        Tab spellListTab = new Tab("Spell List");
        spellListTab.setClosable(false);
        spellListTab.setDisable(true);
        /////////////
        //X: OPTION//
        /////////////

        //TODO: keep it always at last position
        Tab optionTab = new Tab("Option");
        GridPane optionPane = OptionPane.buildOptionPane();
        optionTab.setContent(optionPane);
        optionTab.setClosable(false);

        ///////
        //END//
        ///////

        return new TabPane(buffTab, otherTab, magicTab, spellListTab, optionTab);
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
                if (isLocked || unit == null) return;
                try {
                    saveCharacter(unit, new File("data/" + unit.getName() + ".json"));
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
                if (isLocked || unit == null) return;
                BuffCreationWindow.createWindow();
                if (BuffCreationWindow.isConfirmPressed()){
                    buffPane.addBuff(BuffCreationWindow.getBuff());
            }
            });

            //Item 2: Decrease Duration
            MenuItem decreaseDuration = new MenuItem("Decrease Duration");

                decreaseDuration.setOnAction(actionEvent -> {
                    if (isLocked || unit == null) return;
                    buffPane.decreaseBuffDuration(unit.getBuffArrayList(), BUFF_COL_INDEX);
                    buffPane.decreaseBuffDuration(unit.getDebuffArrayList(), DEBUFF_COL_INDEX);
                });

            //Item 3: Remove Buff
            MenuItem removeBuff = new MenuItem("Remove Buff");
            removeBuff.setOnAction(actionEvent -> {
                if (isLocked || unit == null) return;
                buffPane.deleteBuff();
            });
        buffMenu.getItems().addAll(addBuff, decreaseDuration, removeBuff);

 /*
    ------------------------------------------------------------------------------------------------------------------------
     */
        ///////////////////
        //3: SPELLCASTING//
        ///////////////////

        Menu spellMenu = new Menu("Spell");

            //item 1: Set prepared Spellcasting
            MenuItem setPrepared = new MenuItem("Set Prepared Spellcasting");
            setPrepared.setOnAction(actionEvent -> {
                if (isLocked) return;
                unit.setSpontaneous(true);
                //TODO: Redraw/aggiungere tab preparata e Handler Spell se assente
            });

            //item 2: Set spontaneous Spellcasting
            MenuItem setSpontaneous = new MenuItem("Set Prepared Spellcasting");
            setSpontaneous.setOnAction(actionEvent -> {
                if (isLocked) return;
                unit.setPrepared(true);
                //TODO: Redraw/aggiungere tab Spontenea e Handler Spell se assente
            });

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
        unit = gson.fromJson(fileReader, UNIT_TYPE);
        loadGUI();
        //Draw all the buff associated with the unit
        redrawBuff();
    }

    /**
     * Redraw buff and debuff Column after a change
     */
    private static void redrawBuff(){
        buffPane.redrawColumn(BUFF_COL_INDEX, unit.getBuffArrayList());
        buffPane.redrawColumn(DEBUFF_COL_INDEX, unit.getDebuffArrayList());
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

    ////////////////
    //OTHER METHOD//
    ////////////////

    /**
     * Check if the selected theme is already set. If not, change the theme to the selected one.
     * Only binary for now, could be refactored to support multiple theme.
     * @param setDarkTheme the selected theme
     */
    public static void changeTheme (boolean setDarkTheme){
        if (darkThemeSelected == setDarkTheme) return;
        scene.getStylesheets().remove(getCurrentTheme());
        darkThemeSelected = !darkThemeSelected;
        scene.getStylesheets().add(getCurrentTheme());

    }

    /**
     * Check if the selected colorblind option is already set. If not, change the colorblind mode to the selected one.
     * * Only binary for now, could be refactored to support multiple colorblind mode.
     * @param setColorBlind the selected colorblind mode
     */
    public static void changeColorBlind (boolean setColorBlind){
        if (colorBlind == setColorBlind) return;
        colorBlind = !colorBlind;
        redrawBuff();
    }
    public static void main(String[] args) {
        launch();
    }

}