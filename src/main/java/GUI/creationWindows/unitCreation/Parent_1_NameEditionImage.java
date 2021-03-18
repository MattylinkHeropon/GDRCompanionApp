package GUI.creationWindows.unitCreation;

import hero.Enum.Edition;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.text.TextAlignment;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;


class Parent_1_NameEditionImage implements Parent_0_Base {

    //To return
    private Edition edition = null;
    private TextField nameTextField = null;
    private Image profileImage = null;
    private final StringProperty imgUrl = new SimpleStringProperty();

    //Used by Method
    private TextField currEdSelected;

    @Override
    public void onLoad() {}

    @Override
    public Parent createParent(){

        imgUrl.set("");

        ////////////////
        //TEXT SECTION//
        ////////////////
        Label introductionLabel = new Label("Welcome to the character creation window. Please, enter your character's name and select the game edition");
        Label editionLabel = new Label("Edition:");
        nameTextField = new TextField();
        Label currEdSelectedLabel = new Label("Selected Edition:");
        currEdSelected = new TextField();
        Label imageLabel = new Label("Select your profile image:");


        //setup
        nameTextField.setPromptText("Enter your character's name here:");
        currEdSelected.setEditable(false);

        imageLabel.setTextAlignment(TextAlignment.RIGHT);

        //Set every Label wrappable
        introductionLabel.setWrapText(true);
        editionLabel.setWrapText(true);
        currEdSelectedLabel.setWrapText(true);
        imageLabel.setWrapText(true);

        ////////////////
        //MENU SECTION//
        ////////////////

        MenuButton editionMenu = new MenuButton("Edition");

        //setup
        for (Edition editionEl : Edition.values()
        ) {
            MenuItem item = new MenuItem(editionEl.getFullName());
            editionMenu.getItems().add(item);
            item.setOnAction(actionEvent -> {
                 edition = editionEl;
                currEdSelected.setText(editionEl.getFullName());
            });
        }

        //////////////////////////
        //IMAGE RESEARCH SECTION//
        //////////////////////////
        Button imageSearchButton = new Button("search...");

        //ImageView SetUp
        ImageView imageNode = new ImageView();
        imageNode.setPreserveRatio(true);
        imageNode.setFitHeight(200);
        imageNode.setFitWidth(200);

        //Search setup
        imageSearchButton.setOnAction(actionEvent -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Select a image");
            fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("image file", "*.png", "*.jpg", "*.jpeg"));
            File temp = fileChooser.showOpenDialog(new Stage());
            imgUrl.set(temp.getAbsolutePath());
            profileImage = new Image(temp.toURI().toString());

            imageNode.setImage(profileImage);
        });

        ///////////////////
        //GRAPHIC SECTION//
        ///////////////////

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        //introductionLabel
        GridPane.setHalignment(introductionLabel, HPos.CENTER);
        GridPane.setConstraints(introductionLabel, 0, 0, 2, 1);

        //nameTextField
        GridPane.setHalignment(nameTextField, HPos.CENTER);
        GridPane.setConstraints(nameTextField, 0, 1, 2, 1);

        //edition
        GridPane.setHalignment(editionLabel, HPos.RIGHT);
        GridPane.setHalignment(currEdSelectedLabel, HPos.RIGHT);
        GridPane.setConstraints(editionLabel, 0, 2);
        GridPane.setConstraints(editionMenu, 1, 2);
        GridPane.setConstraints(currEdSelectedLabel, 0, 3);
        GridPane.setConstraints(currEdSelected,1, 3);

        //Search Image
        GridPane.setHalignment(imageLabel, HPos.RIGHT);
        GridPane.setHalignment(imageSearchButton, HPos.RIGHT);
        GridPane.setValignment(imageSearchButton, VPos.TOP);
        GridPane.setHalignment(imageNode, HPos.CENTER);
        GridPane.setConstraints(imageLabel, 0 ,4);
        GridPane.setConstraints(imageSearchButton,0,5);
        GridPane.setConstraints(imageNode, 1, 4, 1 ,2);


        grid.getChildren().addAll(introductionLabel, nameTextField, editionLabel, editionMenu, currEdSelectedLabel, currEdSelected, imageLabel, imageSearchButton, imageNode);

        return grid;
    }

    @Override
    public void nextButtonPressed(){
        UnitCreationWindow.setName(nameTextField.getText());
        UnitCreationWindow.setEdition(edition);
        UnitCreationWindow.setImgUrl(imgUrl.get());
    }

    @Override
    public BooleanBinding nextButtonDisableCondition() {

        return Bindings.createBooleanBinding(() ->
                checkInvalidName() || currEdSelected.getText().isEmpty() || imgUrl.get().isEmpty(),
                nameTextField.textProperty(), currEdSelected.textProperty(), imgUrl
        );
    }

    /**
     * Check if nameTextField is empty OR the name inside already exist in the "data" folder
     * @return True, if the field is empty or the name exist, false otherwise
     * TODO: A way to sign the user that the name is already taken. Maybe a red outline?
     */
    private boolean checkInvalidName(){
        if (nameTextField.getText().isEmpty()) return true;
        File dataFolder = new File("data");
        if (dataFolder.listFiles() == null) return false;
        File[] listOfFile = dataFolder.listFiles();
        assert listOfFile != null;
        for (File file: listOfFile
             ) {
            if (file.getName().equals(nameTextField.getText() + ".json")) return true;
        }
        return false;
    }
}
