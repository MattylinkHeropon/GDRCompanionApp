package GUI.creationWindows.unitCreation;

import hero.AbilityScore_Generator;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

class SelectASGenerationMethodWindow implements UnitCreationWindow_Parent {

    private int selectedMethod;
    private static ToggleGroup radioGroup;


    @Override
    public void onLoad() {

    }

    /**
     * handle the selection of the method to generate 6 possible value for the Ability Score,
     * while creating a Parent to contain all the necessary element
     * @return Created panel
     */
    @Override
    public Parent createParent() {

        ////////////////
        //TEXT SECTION//
        ////////////////

        Label descriptionLabel = new Label("Please, select with which method you want to generate your Ability Score\n");
        TextArea methodDescription = new TextArea();

        //setup
        descriptionLabel.setWrapText(true);
        methodDescription.setWrapText(true);
        methodDescription.setMaxWidth(UnitCreationWindow.WINDOW_SQUARE_DIMENSION /2.0);

        ///////////////////////
        //RADIOBUTTON SECTION//
        ///////////////////////
        radioGroup = new ToggleGroup();
        VBox buttonBox = new VBox(10);

        int i;

        /*
        For each possible method of AS generation create a button;
        each button will have a label to indicate the associated method
        and as userdata an index to identify the method.
        Also, whenever a radioButton is selected, change the text in MethodDescription with the associated description of the generating method
         */
        for (i = 0; i < AbilityScore_Generator.getMethodName().length; i++) {
            //create
            RadioButton temp = new RadioButton(AbilityScore_Generator.getMethodName()[i]);

            //set
            temp.setUserData(i);
            temp.setOnAction(actionEvent -> methodDescription.setText(AbilityScore_Generator.getMethodDescription()[(int) temp.getUserData()]));
            temp.setToggleGroup(radioGroup);

            //add
            buttonBox.getChildren().add(temp);
        }
        //Set "Manual" as default
        Toggle manual = radioGroup.getToggles().get(i-1);
        manual.setSelected(true);
        methodDescription.setText(AbilityScore_Generator.getMethodDescription()[(int) manual.getUserData()]);

        ///////////////////
        //GRAPHIC SECTION//
        ///////////////////
        BorderPane mainBox = new BorderPane();

        BorderPane.setMargin(descriptionLabel, new Insets(0,0,10,0));

        mainBox.setTop(descriptionLabel);

        mainBox.setLeft(buttonBox);
        mainBox.setRight(methodDescription);

        return mainBox;

    }
    @Override
    public void nextButtonPressed(){
        UnitCreationWindow.setSelectedMethod((int) radioGroup.getSelectedToggle().getUserData());
    }
}
