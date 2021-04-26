package GUI.mainWindow;

import GUI.mainWindow.mainWindowComponent.StatPane;
import GUI.mainWindow.mainWindowComponent.centralPane.Tab_1_BuffPane;
import GUI.mainWindow.mainWindowComponent.centralPane.Tab_2_OtherTrackerPane;
import GUI.mainWindow.mainWindowComponent.centralPane.Tab_3_MagicPane;
import GUI.mainWindow.mainWindowComponent.centralPane.Tab_99_OptionPane;
import GUI.smallWindows.creationWindows.BuffCreationWindow;
import GUI.smallWindows.creationWindows.OtherTrackerCreationWindow;
import GUI.smallWindows.creationWindows.SpellClassCreationWindow;
import GUI.smallWindows.creationWindows.unitCreation.UnitCreationWindow;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import customGsonClass.CasterClassAdapter;
import hero.Buff;
import hero.Enum.OtherTrackerOption;
import hero.OtherTracker;
import hero.Unit;
import hero.magic.casterClass.Caster_Class_Base;
import hero.magic.casterClass.Caster_Class_Spontaneous;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainWindowGUI extends Application {

    public static final boolean DEBUG_ON  = true;


    private static Unit unit;
    private Stage stage;
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

        TabPane tabPane = new TabPane();
        GridPane temp = new GridPane();


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
        Button decreaseDuration = new Button("Decrease buff duration");
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

        //DEBUG Button 4
        if (DEBUG_ON){
            Button buffBomb = new Button("BUFF BOMB");
            tab_1_ButtonList.add(buffBomb);
            buffBomb.setOnAction(actionEvent -> {
                for (int i = 1; i < 11; i++) {
                    Tab_1_BuffPane.addBuff(new Buff(i%2 == 0, Integer.toString(i), i, Integer.toString(i)));
                }
            });
        }


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
        //DEBUG Button 2
        if (DEBUG_ON){
            Button oneOfEach = new Button("One of each counter");
            tab_2_ButtonList.add(oneOfEach);
            oneOfEach.setOnAction(actionEvent -> {
                for (OtherTrackerOption option: OtherTrackerOption.values()
                     ) {
                    OtherTracker tracker = new OtherTracker("TEST @ " + new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date()), option);

                    Tab_2_OtherTrackerPane.createTracker(tracker);
                }
            });
        }

        tabPane.getTabs().add(buildTab("Other Counter", trackerScroll, tab_2_ButtonList));

        ////////////
        //3: MAGIC//
        ////////////

        ScrollPane casterClassScroll = new ScrollPane();
        casterClassScroll.setContent(Tab_3_MagicPane.getGridPane());
        casterClassScroll.setFitToWidth(true);
        ArrayList<Button> tab_3_ButtonList = new ArrayList<>();

        //Button 1
        Button addClass = new Button("Add spellcasting class");
        tab_3_ButtonList.add(addClass);
        addClass.setOnAction(actionEvent -> {
            if (isLocked || unit == null) return;
            SpellClassCreationWindow.createWindow();
            if (SpellClassCreationWindow.isConfirmPressed()){
                Tab_3_MagicPane.addClass(SpellClassCreationWindow.getCaster_Class());
            }
        });

        //Button 2
        Button updateClass = new Button("Update spellcasting class");
        tab_3_ButtonList.add(updateClass);
        updateClass.setOnAction(actionEvent -> {
            Tab_3_MagicPane.updateClass();
        });

        //Button 3
        Button removeClass = new Button("Remove spellcasting class");
        tab_3_ButtonList.add(removeClass);
        removeClass.setOnAction(actionEvent -> {
            if (isLocked || unit == null) return;
            Tab_3_MagicPane.deleteClass();
        });

        //DEBUG
        if(DEBUG_ON){
            int[] shortArray = {1,1,1,0,0,0,0,0,0,0};
            int[] midArray = {4,4,4,4,4,0,0,0,0,0};
            int[] longArray = {9,9,9,9,9,9,9,9,9,9};

            Button classBomb = new Button("Bomb");
            tab_3_ButtonList.add(classBomb);
            classBomb.setOnAction(actionEvent -> {
                Tab_3_MagicPane.addClass(new Caster_Class_Spontaneous("short 1", 1, shortArray));
                Tab_3_MagicPane.addClass(new Caster_Class_Spontaneous("mid 1", 2, midArray));
                Tab_3_MagicPane.addClass(new Caster_Class_Spontaneous("long 1", 3, longArray));
                Tab_3_MagicPane.addClass(new Caster_Class_Spontaneous("short 2", 4, shortArray));
                Tab_3_MagicPane.addClass(new Caster_Class_Spontaneous("mid 2", 5, midArray));
                Tab_3_MagicPane.addClass(new Caster_Class_Spontaneous("long 2", 6, longArray));

            });


        }
        Tab magicTab = buildTab("Magic", casterClassScroll, tab_3_ButtonList);
        tabPane.getTabs().add(magicTab);

        /////////////////
        //4: SPELL LIST//
        /////////////////
        Tab spellListTab = buildTab("Spell list", temp, new ArrayList<>());
        spellListTab.setDisable(true);
        tabPane.getTabs().add(spellListTab);


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