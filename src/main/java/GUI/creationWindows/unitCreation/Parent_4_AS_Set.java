package GUI.creationWindows.unitCreation;

import hero.AbilityScore_Generator;
import hero.Enum.AbilityScore;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class Parent_4_AS_Set implements Parent_0_Base {

    private final int[] finalAS = new int[6];
    private final TextField HPTextField = new TextField();
    private final TextField pbTextField = new TextField();
    private static final Label generatedDescriptionLabel = new Label("Here the ability score generated with the desired method");
    private static final Label assignableDescriptionLabel = new Label("Please input here your base Ability Scores");
    private static final Label HPDescriptionLabel = new Label("Please input here your maximum HP");
    private static final Label pbRemaningLabel = new Label("Here your remaining point");

    private VBox finalBox;

    private final GridPane generatedGrid = createGrid(TextField.class.getName());
    private final GridPane assignableGrid = createGrid(TextField.class.getName());
    private final GridPane pbGrid = createGrid(Spinner.class.getName());

    ///////////////////
    //OVERRIDE METHOD//
    ///////////////////

    /**
     * First, if necessary, completely empty finalBox, removing all element
     * Then, if necessary, generate a set of ability score, put them in the TextBoxes of generatedGrid,
     * create a Vbox for generatedGrid with it's label, and add it to finalBox.
     * Repeat the process for assignableGrid, and then add a final row to set the HP of the character
     */
    @Override
    public void onLoad() {
        //empty FinalBox
        if (!finalBox.getChildren().isEmpty())
            finalBox.getChildren().removeAll(finalBox.getChildren());


        int selectedMethod = UnitCreationWindow.getSelectedMethod();
        if (selectedMethod == 3) //Point Buy Case
        {
            System.out.println("TEST SPINNER");
            pbHandler(0, 20, 10);
            HBox pbBox = new HBox(10);
            pbTextField.setText(Integer.toString(UnitCreationWindow.getPbValue()));
            pbBox.getChildren().addAll(pbRemaningLabel, pbTextField);
            finalBox.getChildren().addAll(assignableDescriptionLabel, pbGrid, pbBox);
        } else {
            populate_generatedGrid(selectedMethod);
            VBox assignableBox = new VBox();
            assignableBox.getChildren().addAll(assignableDescriptionLabel, assignableGrid);
            finalBox.getChildren().add(assignableBox);
        }


        //end if Else


        //create HP box
        HBox HPBox = new HBox(10);
        HPTextField.setPromptText("HP");
        HPBox.getChildren().addAll(HPDescriptionLabel, HPTextField);


        finalBox.getChildren().add(HPBox);
    }

    /**
     * Initialize the gridPane after the standard creation the Vbox which will contain them.
     *
     * @return the main Vbox of the class (finalBox)
     */
    @Override
    public Parent createParent() {

        ///////////////////
        //GRAPHIC SECTION//
        ///////////////////

        //Set generatedGrid as uneditable
        generatedGrid.getChildren().forEach(node -> ((TextField) node).setEditable(false));


        //Initialize the final Vbox to return.
        finalBox = new VBox(50);
        VBox.setMargin(finalBox, UnitCreationWindow.STANDARD_MARGIN);

        return finalBox;
    }

    @Override
    public void nextButtonPressed() {
        //int[] racialModArray = Parent_3_AS_Setup.getRacialModArray();
        for (int i = 0; i < 6; i++) {
            TextField textField;
            //case Point Buy
            if (UnitCreationWindow.getSelectedMethod() == 3)
                textField = ((Spinner<?>) pbGrid.getChildren().get(i)).getEditor();
                //Not point Buy
            else textField = (TextField) assignableGrid.getChildren().get(i);

            finalAS[i] = Integer.parseInt((textField.getText())) + UnitCreationWindow.getRacialModArray()[i];
        }
        UnitCreationWindow.setAbilityScoreArray(finalAS);
        UnitCreationWindow.setMaximumHitPoint(Integer.parseInt(HPTextField.getText()));
    }

    ////////////////
    //OTHER METHOD//
    ////////////////

    /**
     * Use the selected method to generate a set of Ability Score: if "manual" (4) il selected, end the execution,
     * else generate an array of int and use it to populate the GridPane.
     * Then, create a Vbox with a Label and the populated Grid, and add them to finalBox
     *
     * @param selectedMethod selected method for the Array Generation
     */
    private void populate_generatedGrid(int selectedMethod) {
        int[] generatedAS = null;

        //Get the correct Array of Ability Score
        switch (selectedMethod) {
            case 0:
                generatedAS = AbilityScore_Generator.completeRandom();
                break;
            case 1:
                generatedAS = AbilityScore_Generator.classic();
                break;
            case 2:
                generatedAS = AbilityScore_Generator.standard();
                break;
            case 4: //"Manual" Case
                return;
        }
        //set value
        for (int i = 0; i < 6; i++) {
            assert generatedAS != null;
            TextField temp = (TextField) generatedGrid.getChildren().get(i);

            temp.setText(Integer.toString(generatedAS[i]));
        }
        //Create the box
        VBox vBox = new VBox();
        vBox.getChildren().addAll(generatedDescriptionLabel, generatedGrid);

        //put it in the final box
        finalBox.getChildren().add(vBox);
    }

    /**
     * Create a 3*2 grid for the Ability Score, with set Hgap and Vgap
     * By default every element of the grid has a promp text with the abbreviation of the skill and is set as editable
     *
     * @return a properly formatted grid
     */
    public static GridPane createGrid(String typeOfField) {
        GridPane grid = new GridPane();
        //Formula: Windows dimension - margin (10+10 external, 10+10 between box), divided by 3 because three box per fow
        int gridElement_MaxWidth = ((UnitCreationWindow.WINDOW_SQUARE_DIMENSION - 40) / 3);
        int gridElement_Hgap = 10;
        int gridElement_Vgap = 10;
        int i = 0;
        Control temp;

        //Generate Grid
        for (int row = 0; row < 2; row++) {
            for (int col = 0; col < 3; col++) {

                //Select and create correct type of Field
                //This solution is a workaround because Switch case only accept constant Expression, which Class.class.getName() is not.
                //If an more cases are needed, value to convert all to a Switch case, and use an Enum to evaluate the case.
                //See here for detail: https://stackoverflow.com/questions/3827393/java-switch-statement-constant-expression-required-but-it-is-constant
                if (typeOfField.equals(Spinner.class.getName())) {
                    temp = new Spinner<Integer>();
                } else {
                    temp = new TextField();
                    ((TextField) temp).setPromptText(AbilityScore.values()[i].getAbbreviation());
                    i++;
                }

                //set bound

                temp.setMaxWidth(gridElement_MaxWidth);

                GridPane.setConstraints(temp, col, row);
                grid.getChildren().add(temp);
            }
        }

        //Set gap:
        grid.setHgap(gridElement_Hgap);
        grid.setVgap(gridElement_Vgap);


        return grid;
    }

    private void pbHandler(int minimum, int maximum, int start) {
        //Initialize Spinner
        pbGrid.getChildren().forEach(spinner -> ((Spinner<Integer>) spinner).setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(minimum, maximum, start)));

    }
}

