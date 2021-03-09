package GUI.creationWindows.unitCreation;

import javafx.beans.binding.BooleanBinding;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

public class Parent_3_AS_Setup implements Parent_0_Base{

    //To Return
    private final int[] racialModArray = new int[6];

    //Used by Method
    private final GridPane racialModGrid = Parent_4_AS_Set.createGrid(TextField.class.getName());
    private final TextField pointBuyTextField = new TextField();
    private VBox finalBox;

    //Label
    private static final Label racialModLabel = new Label("Please input here your racial modifier. They will be automatically added to your base Ability Score");
    private static final Label pointBuyLabel = new Label("Input here the number of point buy that you will be using to generate your base Ability Score");

    @Override
    public void onLoad() {
        pointBuyTextField.setText("0");
        //empty FinalBox
        if (!finalBox.getChildren().isEmpty())
            finalBox.getChildren().removeAll(finalBox.getChildren());

        //Draw racialModGrid
        VBox racialModVbox = new VBox(10);
        racialModVbox.getChildren().addAll(racialModLabel, racialModGrid);
        VBox pbVbox = new VBox(10);
        pbVbox.getChildren().addAll(pointBuyLabel, pointBuyTextField);

        finalBox.getChildren().add(racialModVbox);
        if (UnitCreationWindow.getSelectedMethod() == 3) //Point Buy Selected
            finalBox.getChildren().add(pbVbox);
    }

    @Override
    public Parent createParent() {
        //Set wrapText for the label
        racialModLabel.setWrapText(true);
        pointBuyLabel.setWrapText(true);

        //Initialize the final Vbox to return.
        finalBox = new VBox(50);
        VBox.setMargin(finalBox, UnitCreationWindow.STANDARD_MARGIN);

        return finalBox;
    }

    @Override
    public void nextButtonPressed() {
       //populate racialModArray
        for (int i = 0; i < 6; i++) {
            TextField temp = (TextField) racialModGrid.getChildren().get(i);
            if (temp.getText().isEmpty()) racialModArray[i] = 0;
                    else
                        racialModArray[i] = Integer.parseInt(temp.getText());
        }

    UnitCreationWindow.setRacialModArray(racialModArray);
    UnitCreationWindow.setPointBuyValue(Integer.parseInt(pointBuyTextField.getText()));
    }

    @Override
    public BooleanBinding nextButtonDisableCondition() {
        return null;
    }
}
