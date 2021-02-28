package GUI.creationWindows.unitCreation;

import hero.Enum.Edition;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;


class Parent_1_NameEditionImage implements Parent_0_Base {

    private String edition = null;
    private TextField nameTextField;
    private Image profileImage;
    private String imgUrl;
    @Override
    public void onLoad() {

    }

    /**
     * FIrst creation window. Let the user input the character's name, it's game edition and a profile image
     * while creating a Parent to contain all the necessary element
     * @return Created panel
     */
    @Override
    public Parent createParent(){

        ////////////////
        //TEXT SECTION//
        ////////////////

        Label introductionLabel = new Label("Welcome to the character creation window.\nPlease, enter your character's name and select the game edition");
        Label editionLabel = new Label("Edition: ");
        nameTextField = new TextField();
        Label currEdSelectedLabel = new Label("Selected Edition: ");
        TextField currEdSelected = new TextField();
        Label imageLabel = new Label("Select your profile image: ");


        //setup
        nameTextField.setPromptText("Enter your character's name here");
        currEdSelected.setEditable(false);

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
                 edition = editionEl.getAbbreviation();
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

        //Search setup
        imageSearchButton.setOnAction(actionEvent -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Select a image");
            fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("image file", "*.png", "*.jpg"));
            File temp = fileChooser.showOpenDialog(new Stage());
            imgUrl = temp.getAbsolutePath();
            profileImage = new Image(temp.toURI().toString());

            imageNode.setImage(profileImage);
        });

        ///////////////////
        //GRAPHIC SECTION//
        ///////////////////

        GridPane grid = new GridPane();
        grid.setHgap(1);
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
        UnitCreationWindow.setImgUrl(imgUrl);
    }
}
