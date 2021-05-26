package GUI.mainWindow;

import GUI.mainWindow.mainWindowComponent.StatPane;
import GUI.mainWindow.mainWindowComponent.centralPane.Tab_1_BuffPane;
import GUI.mainWindow.mainWindowComponent.centralPane.Tab_2_OtherTrackerPane;
import GUI.mainWindow.mainWindowComponent.centralPane.Tab_3_MagicPane;
import GUI.mainWindow.mainWindowComponent.centralPane.Tab_99_OptionPane;
import GUI.smallWindows.creationWindows.BuffCreationWindow;
import GUI.smallWindows.creationWindows.CasterClassCreationWindow;
import GUI.smallWindows.creationWindows.OtherTrackerCreationWindow;
import GUI.smallWindows.creationWindows.unitCreation.UnitCreationWindow;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import customGsonClass.CasterClassAdapter;
import hero.OtherTracker;
import hero.Unit;
import hero.magic.casterClass.Caster_Class_Base;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class MainWindowGUI extends Application {

    public static final boolean DEBUG_ON  = true;

    private Stage stage;
    private static Unit unit;
    private static Scene scene;
    private static final VBox buttonBox = new VBox(5);

    /*
    isLocked is checked every time a MenuItem is called.
    If true, prevent the execution of every other method in the MenuItem.setOnAction.
    Implemented because deleteBuff() doesn't create a new windows (no showAndWait() method), nor resolve instantly, but need an action from the user to resolve
     */

    //Constant Value
    private static final String LIGHT_THEME = "light_theme.css";
    private static final String DARK_THEME = "dark_theme.css";
    private static final int STD_WIDTH = 800;
    private static final int STD_HEIGHT = 600;

    //Used to serialize an Unit.
    private static final Type UNIT_TYPE = new TypeToken<Unit>() {}.getType();

    //Variable called by Other
    private static boolean colorBlind = false;
    private static boolean isLocked = false;
    private static boolean darkThemeSelected = true;

    //Getter and Setter
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
        BorderPane root = new BorderPane();

        //////////////
        //BUILD ROOT//
        //////////////

        //Top
        MenuBar menuBar = buildMenuBar();
        root.setTop(menuBar);

        //Left
        Double maxSize = 150.0;
        VBox leftBox = StatPane.buildBox(maxSize);
        leftBox.getChildren().add(buttonBox);
        BorderPane.setMargin(leftBox, new Insets(10));
        BorderPane.setAlignment(leftBox, Pos.CENTER);
        root.setLeft(leftBox);

        //Center
        TabPane tabPane = buildCentralPane();
        root.setCenter(tabPane);


        //scene setup
        scene = new Scene(root, STD_WIDTH, STD_HEIGHT);
        scene.getStylesheets().add(getCurrentTheme());
        stage.setScene(scene);
        stage.setTitle("GDR Companion App");
        //stage.setResizable(false);

        //Icon section
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        for (int i = 16; i < 513; i = i*2) {
            String pathName = "icon/icon_" + i + ".png";
            InputStream inputStream = classLoader.getResourceAsStream(pathName);
            if (inputStream != null)
            stage.getIcons().add(new Image(inputStream));
            else System.out.println("failed to load " + pathName);
        }


        stage.show();
        //TODO:Debug line
        if (DEBUG_ON){
            loadCharacter_load(new File("data/Path_Test.json"));
            printDimension("Root", root);
            printDimension("Root.Top",  root.getTop());
            printDimension("Root.Left",  root.getLeft());
            printDimension("Root.Center",  root.getCenter());
            printDimension("Root.Right",  root.getRight());
            printDimension("Root.Bottom",  root.getBottom());
        }


        }

    private static void printDimension( String text, Node node){
        if (node == null) return;
        System.out.println(text + ": " + (int) node.getBoundsInParent().getWidth() + " X " + (int) node.getBoundsInParent().getHeight() + " (" + node.getClass() + ")");
        }

    private void loadGUI(){
        stage.setScene(scene);

        stage.setTitle(unit.getName());
        stage.show();
    }

    ////////////
    //TAB PANE//
    ////////////

    private static TabPane buildCentralPane(){

        // FIXME: 25/05/2021 Known problem: if a button text is too long it won't wrap but extend the width of the left node, reducing the size of the central node
        TabPane tabPane = new TabPane();

        ///////////
        //1: BUFF//
        ///////////

        ScrollPane buffScroll = new ScrollPane();
        buffScroll.setContent(Tab_1_BuffPane.getGrid());
        buffScroll.setFitToWidth(true);
        ArrayList<Button> tab_1_ButtonList = new ArrayList<>();

        //Button 1
        Button addBuff = new Button("Add buff");
        tab_1_ButtonList.add(addBuff);
        addBuff.setOnAction(actionEvent -> {
            if (isLocked || unit == null) return;
            BuffCreationWindow.createWindow();
            if (BuffCreationWindow.isConfirmPressed()){
                Tab_1_BuffPane.addBuff(BuffCreationWindow.getBuff());
            }
        });
        //Button 2
        Button decreaseDuration = new Button("Decrease duration");
        tab_1_ButtonList.add(decreaseDuration);
        decreaseDuration.setOnAction(actionEvent -> {
            if (isLocked || unit == null) return;
            Tab_1_BuffPane.decreaseBuffDuration_Start();
        });
        //Button 3
        Button removeBuff = new Button("Remove buff");
        tab_1_ButtonList.add(removeBuff);
        removeBuff.setOnAction(actionEvent -> {
            if (isLocked || unit == null) return;
            Tab_1_BuffPane.deleteBuff();
        });

        tabPane.getTabs().add(buildTab("(De)Buff", buffScroll, tab_1_ButtonList));


        ////////////////////
        //2: OTHER COUNTER//
        ////////////////////
        ScrollPane trackerScroll = new ScrollPane();
        trackerScroll.setContent(Tab_2_OtherTrackerPane.getGridPane());
        trackerScroll.setFitToWidth(true);
        ArrayList<Button> tab_2_ButtonList = new ArrayList<>();

        //Button 1
        Button trackerCreator = new Button("Create tracker");
        tab_2_ButtonList.add(trackerCreator);
        trackerCreator.setOnAction(actionEvent -> {
            if (isLocked || unit == null) return;
            OtherTrackerCreationWindow.createWindow();
            if (OtherTrackerCreationWindow.isConfirmPressed()){
                OtherTracker tracker = new OtherTracker(OtherTrackerCreationWindow.getDescription(), OtherTrackerCreationWindow.getCurrOption());
                Tab_2_OtherTrackerPane.createTracker(tracker);
            }
        });

        tabPane.getTabs().add(buildTab("Other Counter", trackerScroll, tab_2_ButtonList));

        ////////////
        //3: MAGIC//
        ////////////

        ScrollPane casterClassScroll = new ScrollPane();
        casterClassScroll.setContent(Tab_3_MagicPane.getGridPane());
        casterClassScroll.setFitToWidth(true);
        ArrayList<Button> tab_3_ButtonList = new ArrayList<>();

        //Button 1
        Button addClass = new Button("Add class");
        tab_3_ButtonList.add(addClass);
        addClass.setOnAction(actionEvent -> {
            if (isLocked || unit == null) return;
            CasterClassCreationWindow.createWindow();
            if (CasterClassCreationWindow.isConfirmPressed()){
                Tab_3_MagicPane.addClass(CasterClassCreationWindow.getCaster_Class());
            }
        });
        //Button 2
        Button updateClass = new Button("Update class");
        tab_3_ButtonList.add(updateClass);
        updateClass.setOnAction(actionEvent -> {
            if (isLocked || unit == null) return;
            Tab_3_MagicPane.selectClass(false);
        });
        //Button 3
        Button removeClass = new Button("Remove class");
        tab_3_ButtonList.add(removeClass);
        removeClass.setOnAction(actionEvent -> {
            if (isLocked || unit == null) return;
            Tab_3_MagicPane.selectClass(true);
        });

        //Button 4
        Button resetSlot = new Button("Reset Slot");
        tab_3_ButtonList.add(resetSlot);
        resetSlot.setOnAction(actionEvent -> {
            if (isLocked || unit == null) return;
            Tab_3_MagicPane.reset();
        });

        Tab magicTab = buildTab("Magic", casterClassScroll, tab_3_ButtonList);
        tabPane.getTabs().add(magicTab);

        ////////////////
        //LAST: OPTION//
        ////////////////
        tabPane.getTabs().add(buildTab("Option", Tab_99_OptionPane.buildOptionPane(), new ArrayList<>()));

        return tabPane;
    }

    private static Tab buildTab(String name, Node node, List<Button> buttonList){
        Tab tab = new Tab(name);
        tab.setContent(node);
        tab.setClosable(false);
        tab.setOnSelectionChanged(event -> {
            buttonBox.getChildren().removeAll(buttonBox.getChildren());
            buttonList.forEach(button -> buttonBox.getChildren().add(button));
        });

        return tab;
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


        //END
        return new MenuBar(fileMenu);
    }

    ///////////////////////
    //FILE MENUBAR METHOD//
    ///////////////////////

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

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Caster_Class_Base.class, new CasterClassAdapter());
        Gson gson = gsonBuilder.create();

        FileReader fileReader = null;
        try {
            fileReader = new FileReader(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        assert fileReader != null;
        unit = gson.fromJson(fileReader, UNIT_TYPE);
        StatPane.populateBox(unit);
        loadGUI();
        //Load the character in every Tab
        Tab_1_BuffPane.setUnit(unit);
        Tab_2_OtherTrackerPane.setUnit(unit);
        Tab_3_MagicPane.setUnit(unit);

    }

    /**
     * Override the given File with the a set of data  of the unit in json format
     * @param unit Unit that will be saved in the file
     * @param file A file to be written
     * @throws IOException error with the file
     */
    public static void saveCharacter(Unit unit, File file) throws IOException {

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Caster_Class_Base.class, new CasterClassAdapter());

        Gson gson = gsonBuilder.setPrettyPrinting().create();
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
        if (unit != null) Tab_1_BuffPane.redrawBuff();
    }

    public static void main(String[] args) {
        launch();
    }
}