package GUI.creationWindows.unitCreation;

import GUI.MainGUI;
import hero.Unit;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
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
    private static String edition;
    private static int selectedMethod;
    private static int[] abilityScoreArray;
    private static int maximumHitPoint;
    private static boolean newUnit = false;



    public static String getName() {
        return name;
    }

    public static void setName(String name) {
        UnitCreationWindow.name = name;
    }

    public static void setEdition(String edition) {
        UnitCreationWindow.edition = edition;
    }

    public static void setImgUrl(String imgUrl) {
        UnitCreationWindow.imgUrl = imgUrl;
    }

    public static void setSelectedMethod(int selectedMethod) {
        UnitCreationWindow.selectedMethod = selectedMethod;
    }

    public static int getSelectedMethod() {
        return selectedMethod;
    }

    public static void setAbilityScoreArray(int[] abilityScoreArray) {
        UnitCreationWindow.abilityScoreArray = abilityScoreArray;
    }

    public static void setMaximumHitPoint(int maximumHitPoint) {UnitCreationWindow.maximumHitPoint = maximumHitPoint; }

    public static boolean isNewUnit() {
        return newUnit;
    }

    public static void setNewUnit(boolean newUnit) {
        UnitCreationWindow.newUnit = newUnit;
    }

    public static void CreateWindow() {

        /////////////////////////
        //CENTRAL ELEMENT SETUP//
        /////////////////////////

        LinkedList<UnitCreationWindow_Parent> windowList = new LinkedList<>();
        LinkedList<Parent> parentList = new LinkedList<>();

        //Populate windowList
        windowList.add(new NameEditionImageWindow());
        windowList.add(new SelectASGenerationMethodWindow());
        windowList.add(new SetASWindow());

        //Create a Parent for each window, add it to the parentList and set it invisible
        for (UnitCreationWindow_Parent windowParentParent: windowList
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
        BorderPane.setMargin(parentList.get(0), STANDARD_MARGIN);
        BorderPane.setMargin(parentList.get(1), STANDARD_MARGIN);
        BorderPane.setMargin(parentList.get(2), STANDARD_MARGIN);
        //setting
        mainPane.setBottom(buttonBox);
        mainPane.setCenter(currParent.get());


        Scene mainScene = new Scene(mainPane, WINDOW_SQUARE_DIMENSION, WINDOW_SQUARE_DIMENSION);

        stage = new Stage();
        stage.setScene(mainScene);
        stage.setTitle("Hero Creation");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setResizable(false);
        stage.showAndWait();
    }

    private static void createPG() throws IOException {

        File unitFile = new File("data/" + name + ".json");

        if (!unitFile.createNewFile()){
            //TODO: error in caso di unitFile esistente
            System.out.println("File già presente");
            return;
        }
        //Load new Unit on exit
        newUnit = true;

        //Create a local copy of the profile image
        Path original = Paths.get(imgUrl);
        Path destination = Paths.get("images/" + name + ".jpg");
        Files.copy(original, destination);
        File pgImage = new File(destination.toString());

        //Json creation
        MainGUI.saveCharacter(new Unit(goodPath(pgImage), name, edition, abilityScoreArray, maximumHitPoint), unitFile);
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
