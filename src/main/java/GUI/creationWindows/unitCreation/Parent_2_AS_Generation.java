package GUI.creationWindows.unitCreation;

import hero.AbilityScore_Generator;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

class Parent_2_AS_Generation implements Parent_0_Base {

    //To return
    private final IntegerProperty currSelected = new SimpleIntegerProperty();

    @Override
    public void onLoad() {}

    @Override
    public Parent createParent() {

        ////////////////
        //TEXT SECTION//
        ////////////////

        Label descriptionLabel = new Label("Please, select with which method you want to generate your Ability Score");
        TextArea methodDescription = new TextArea();

        //setup
        descriptionLabel.setWrapText(true);
        methodDescription.setWrapText(true);
        methodDescription.setMaxWidth(UnitCreationWindow.WINDOW_SQUARE_DIMENSION /2.0);

        ///////////////////////
        //RADIOBUTTON SECTION//
        ///////////////////////
        //To return
        ToggleGroup radioGroup = new ToggleGroup();
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
            int finalI = i;
            temp.setOnAction(actionEvent -> {
                methodDescription.setText(AbilityScore_Generator.getMethodDescription()[finalI]);
                currSelected.set(finalI);
            }
            );
            temp.setToggleGroup(radioGroup);

            //add
            buttonBox.getChildren().add(temp);
        }
        //Set "Manual" as default
        Toggle manual = radioGroup.getToggles().get(i-1);
        manual.setSelected(true);
        currSelected.set(4); //set correct default value for autoselected
        methodDescription.setText(AbilityScore_Generator.getMethodDescription()[i-1]);


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
        UnitCreationWindow.setSelectedMethod(currSelected.get());
    }

    @Override
    public BooleanBinding nextButtonDisableCondition() {


        return Bindings.createBooleanBinding(()->
                currSelected.get() == 3 && invalidEdition(),
                currSelected
                );
    }

    private boolean invalidEdition(){
        switch (UnitCreationWindow.getEdition()) {
            case DND_1E:
            case DND_2E:
            case DND_22E:
            case PATHFINDER_2E:
                return true;
            case DND_3E:
            case DND_35E:
            case DND_4E:
            case DND_5E:
            case PATHFINDER_1E:
                return false;
        }
        return true;
    }
}
