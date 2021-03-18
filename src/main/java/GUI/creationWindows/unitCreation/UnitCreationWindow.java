package GUI.creationWindows.unitCreation;

import GUI.MainWindowGUI;
import hero.Enum.Edition;
import hero.Unit;
import javafx.beans.binding.BooleanBinding;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;


/**
 * The entire windows is divided in three screen (Parent):
 * First: user to enter Name and Game edition (and probably Profile Picture);
 * Second: user select with which method generate the Ability Score
 * Third: user reorder the  generated Ability Score (or just set whatever value they want)
 */

public class UnitCreationWindow  {
    public static final int WINDOW_SQUARE_DIMENSION = 400;
    public static final Insets STANDARD_MARGIN = new Insets(15);
    private static final Insets BOTTOM_BUTTON_MARGIN = new Insets(0, 10, 10, 0);

    private static Stage stage;
    private static String imgUrl;
    private static String name;
    private static Edition edition;
    private static int selectedMethod;
    private static int[] racialModArray;
    private static int[] abilityScoreArray;
    private static int pointBuyValue;
    private static int maximumHitPoint;
    private static boolean newUnit = false;

    //////////
    //SETTER//
    //////////

    public static void setImgUrl(String imgUrl) {
        UnitCreationWindow.imgUrl = imgUrl;
    }

    public static void setName(String name) {
        UnitCreationWindow.name = name;
    }

    public static void setEdition(Edition edition) {
        UnitCreationWindow.edition = edition;
    }

    public static void setSelectedMethod(int selectedMethod) {
        UnitCreationWindow.selectedMethod = selectedMethod;
    }

    public static void setRacialModArray(int[] racialModArray) {
        UnitCreationWindow.racialModArray = racialModArray;
    }

    public static void setAbilityScoreArray(int[] abilityScoreArray) {
        UnitCreationWindow.abilityScoreArray = abilityScoreArray;
    }

    public static void setPointBuyValue(int pointBuyValue) {
        UnitCreationWindow.pointBuyValue = pointBuyValue;
    }

    public static void setMaximumHitPoint(int maximumHitPoint) {
        UnitCreationWindow.maximumHitPoint = maximumHitPoint;
    }

    public static void setNewUnit(boolean newUnit) {
        UnitCreationWindow.newUnit = newUnit;
    }

    //////////
    //GETTER//
    //////////

    public static String getImgUrl() {return imgUrl; }

    public static String getName() { return name; }

    public static Edition getEdition() { return edition; }

    public static int getSelectedMethod() { return selectedMethod; }

    public static int[] getRacialModArray() { return racialModArray; }

    public static int[] getAbilityScoreArray() { return abilityScoreArray; }

    public static int getPointBuyValue() { return pointBuyValue; }

    public static int getMaximumHitPoint() { return maximumHitPoint; }

    public static boolean isNewUnit() { return newUnit;}


    public static void CreateWindow() {

        /////////////////////////
        //CENTRAL ELEMENT SETUP//
        /////////////////////////

        LinkedList<Parent_0_Base> windowList = new LinkedList<>();
        LinkedList<Parent> parentList = new LinkedList<>();

        //Populate windowList

        windowList.add(new Parent_1_NameEditionImage());
        windowList.add(new Parent_2_AS_Generation());
        windowList.add(new Parent_3_AS_Setup());
        windowList.add(new Parent_4_AS_Set());

        //Create a Parent for each window, add it to the parentList and set it invisible
        for (Parent_0_Base windowParentParent: windowList
             ) {
            Parent parent = windowParentParent.createParent();
            parentList.add(parent);
            parent.setVisible(false);
        }
        //Indicate the last position of the arrays
        int endArray = windowList.size()-1;

        //atomic because they are in a lambda expression; listIndex is already set at 0 for construction
        AtomicReference<Parent> currParent = new AtomicReference<>(parentList.getFirst());
        AtomicInteger listIndex = new AtomicInteger();
        currParent.get().setVisible(true);

        BorderPane mainPane = new BorderPane();

        //////////////////
        //BUTTON SECTION//
        //////////////////
        HBox buttonBox = new HBox(10);
        Button closeButton = new Button("Close");
        Button nextButton = new Button("Next >>");

        nextButton.disableProperty().bind(windowList.get(0).nextButtonDisableCondition());

        //box setup
        buttonBox.getChildren().addAll(closeButton, nextButton);
        buttonBox.setAlignment(Pos.CENTER_RIGHT);

        //Button Action
        closeButton.setOnAction(actionEvent -> {
                    //First Parent
                    if (listIndex.get() == 0) stage.close();
                    else {
                        listIndex.getAndDecrement();
                        currParent.get().setVisible(false); //set current Parent invisible
                        currParent.set(parentList.get(listIndex.get())); //get the new current Parent
                        currParent.get().setVisible(true); //set the new current Parent visible
                        mainPane.setCenter(currParent.get());
                        addBindCondition(nextButton, windowList.get(listIndex.get()).nextButtonDisableCondition());
                        if (listIndex.get() == 0) closeButton.setText("Close");
                        nextButton.setText("Next >>");
                    }
                }
                );

        nextButton.setOnAction(actionEvent -> {
            windowList.get(listIndex.get()).nextButtonPressed();
            //Last Parent
            if (listIndex.get() == endArray) {
                try {
                    createPG();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                stage.close();

            } else {
                listIndex.getAndIncrement();
                windowList.get(listIndex.get()).onLoad(); //call "onLoad" method to modify the window
                currParent.get().setVisible(false); //set current Parent invisible
                currParent.set(parentList.get(listIndex.get())); //get the new current Parent
                currParent.get().setVisible(true); //set the new current Parent visible
                mainPane.setCenter(currParent.get());

                addBindCondition(nextButton, windowList.get(listIndex.get()).nextButtonDisableCondition());
                //Change text on the button (always in "closeButton", if necessary in "nextButton")
                if (listIndex.get() == endArray) nextButton.setText("End");
                closeButton.setText("<< Back");
            }
        });

        ////////////////////
        //MAINPANE SECTION//
        ////////////////////

        //margin
        BorderPane.setMargin(buttonBox, BOTTOM_BUTTON_MARGIN);
        parentList.forEach(parent -> BorderPane.setMargin(parent, STANDARD_MARGIN));

        //setting
        mainPane.setBottom(buttonBox);
        mainPane.setCenter(currParent.get());


        Scene scene = new Scene(mainPane, WINDOW_SQUARE_DIMENSION, WINDOW_SQUARE_DIMENSION);
        scene.getStylesheets().add(MainWindowGUI.getCurrentTheme());
        stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("Hero Creation");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setResizable(false);
        stage.showAndWait();
    }

    /**
     * Remove all binding from the given Button, and add to it the given BooleanBinding
     * @param buttonToBind Button to modify
     * @param binding BooleanBinding to attach to the button
     */
    private static void addBindCondition(Button buttonToBind, BooleanBinding binding){
        buttonToBind.disableProperty().unbind();
        if (binding!=null) buttonToBind.disableProperty().bind(binding);
    }

    /**
     * Use the given data to create a new character
     * @throws IOException error with the File
     */
    private static void createPG() throws IOException {

        File unitFile = new File("data/" + name + ".json");

        if (!unitFile.createNewFile()){
            return;
        }
        //Load new Unit on exit
        newUnit = true;

        //Create a local copy of the profile image
        Path original = Paths.get(imgUrl);
        Path destination = Paths.get("images/" + name + ".png");
        Files.copy(original, destination);
        File pgImage = new File(destination.toString());

        //Json creation
        MainWindowGUI.saveCharacter(new Unit(goodPath(pgImage), name, edition, abilityScoreArray, maximumHitPoint), unitFile);
    }

    /**
     * Created mostly for Windows user, replace the "\" in the path with "/", making that compatible with the java File handler
     * @param file File with the interested path to be modified
     * @return the corrected parsed path, in a String form
     */
    private static String goodPath (File file){
        String oldPath = file.getPath();
        return  oldPath.replace(File.separatorChar, '/');
    }

}
