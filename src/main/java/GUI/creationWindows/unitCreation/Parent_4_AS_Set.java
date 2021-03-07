package GUI.creationWindows.unitCreation;

import hero.AbilityScore_Generator;
import hero.Enum.AbilityScore;
import hero.Enum.Edition;
import hero.Unit;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class Parent_4_AS_Set implements Parent_0_Base {

    private final int[] finalAS = new int[6];
    private final TextField HPTextField = new TextField();
    private static final TextField pointBuyTextField = new TextField();
    private static final Label generatedDescriptionLabel = new Label("Here the ability score generated with the desired method");
    private static final Label assignableDescriptionLabel = new Label("Please input here your base Ability Scores");
    private static final Label HPDescriptionLabel = new Label("Please input here your maximum HP");
    private static final Label pointBuyRemainingLabel = new Label("Here your remaining point");

    private static int currStart;
    private static VBox finalBox;
    private static Edition edition;

    private final GridPane generatedGrid = createGrid(TextField.class.getName());
    private final GridPane assignableGrid = createGrid(TextField.class.getName());
    private final GridPane pointBuyGrid = createGrid(Spinner.class.getName());

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
        //Point Buy Case
        if (selectedMethod == 3)
        {
            edition = UnitCreationWindow.getEdition();
            //Modify Spinner to the correct value
            switch (edition){
                case DND_1E:
                case DND_2E:
                case DND_22E:
                case PATHFINDER_2E:
                    System.out.println("You shouldn't be here");
                    return;
                case DND_3E:
                case DND_35E:
                    currStart = 8;
                    spinnerInitializer(8, 18);
                    break;
                case DND_4E:
                    currStart = 10;
                    //TODO: ridurre di 2 il PB pool;
                    //Dire all'utente "ti ho ridotto il pool, sappi che puoi fare XYZ
                    spinnerInitializer(8, 18);
                    break;
                case DND_5E:
                    currStart = 8;
                    spinnerInitializer(8, 15);
                    break;
                case PATHFINDER_1E:
                    spinnerInitializer(7, 18);
                    break;
            }
            //Create graphic
            HBox pointBuyBox = new HBox(10);
            pointBuyTextField.setText(Integer.toString(UnitCreationWindow.getPointBuyValue()));
            pointBuyBox.getChildren().addAll(pointBuyRemainingLabel, pointBuyTextField);
            finalBox.getChildren().addAll(assignableDescriptionLabel, pointBuyGrid, pointBuyBox);
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
     * @return the main Vbox of the class (finalBox)
     */
    @Override
    public Parent createParent() {

        ///////////////////
        //GRAPHIC SECTION//
        ///////////////////
        //create Spinner and Listener for the pointBuyGrid



        //Set generatedGrid as uneditable
        generatedGrid.getChildren().forEach(node -> ((TextField) node).setEditable(false));


        //Initialize the final Vbox to return.
        finalBox = new VBox(50);
        VBox.setMargin(finalBox, UnitCreationWindow.STANDARD_MARGIN);

        return finalBox;
    }

    @Override
    public void nextButtonPressed() {
        for (int i = 0; i < 6; i++) {
            TextField textField;
            //case Point Buy
            if (UnitCreationWindow.getSelectedMethod() == 3)
                textField = ((Spinner<?>) pointBuyGrid.getChildren().get(i)).getEditor();
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
                    temp = new Spinner<Integer>(0 , 20 , 10);
                    ((Spinner<?>) temp).getEditor().textProperty().addListener((observableValue, oldValue, newValue) -> pointBuyChange(Integer.parseInt(oldValue), Integer.parseInt(newValue)));
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

    /**
     * Initialize every Spinner in pointBuyGrid, and assign to their TextField a Listener
     * @param minimum minimum value of a Spinner
     * @param maximum maximum value of a Spinner
     */
    private void spinnerInitializer(int minimum, int maximum) {
        for (int i = 0; i < 6; i++) {
            //Irrelevant Warning, for construction pointBuyGrid will always and only have Spinner as node
            @SuppressWarnings("unchecked")
            Spinner<Integer> spinner = ((Spinner<Integer>)pointBuyGrid.getChildren().get(i));
            SpinnerValueFactory.IntegerSpinnerValueFactory valueFactory = (SpinnerValueFactory.IntegerSpinnerValueFactory) spinner.getValueFactory();
            valueFactory.setMin(minimum);
            valueFactory.setMax(maximum);
            valueFactory.setValue(currStart);
        }
    }

    /**
     * Called when the value of a Spinner in pointBuyGrid is changed. Calculate the cost of the change, and add/subtract it from the pool of remaing point
     * @param oldValue value before the change
     * @param newValue value after the change
     */
    private static void pointBuyChange ( int oldValue, int newValue){
        int deltaPoint = 0;
        boolean increaseInValue = oldValue < newValue;
        switch (edition) {
            case DND_4E:
                deltaPoint = deltaCalculus_VariableAndSignSelector(increaseInValue, newValue <= currStart, oldValue, newValue);
                if ((increaseInValue && newValue == 16)||(!increaseInValue && oldValue == 16))
                    deltaPoint = 2 * Math.abs(deltaPoint)/deltaPoint ;
                break;
            case PATHFINDER_1E:
            case DND_5E:
                deltaPoint = deltaCalculus_VariableAndSignSelector(increaseInValue, newValue <= currStart, oldValue, newValue);
            break;
            case DND_3E:
            case DND_35E:
                if (increaseInValue) deltaPoint = (-1) * deltaCalculus_Value(oldValue);
                else deltaPoint = deltaCalculus_Value(newValue);
        }
        
        int currValue = Integer.parseInt(pointBuyTextField.getText());
        currValue = currValue + deltaPoint;
        pointBuyTextField.setText(Integer.toString(currValue));
    }

    /**
     * First method called to calculate the value of deltaPoint.
     * Call deltaCalculus_Value passing the correct variable, and decide the sign of the return value
     * @param increaseInValue decide the sign of the return value
     * @param belowBase decide which value pass to the subMethod
     * @param oldValue old value of the variable
     * @param newValue new value of the variable
     * @return the result of deltaCalculus_Value with the correct sing
     */
    private static int deltaCalculus_VariableAndSignSelector(boolean increaseInValue, boolean belowBase, int oldValue, int newValue){
        if (increaseInValue)
            if (belowBase) return (-1) * deltaCalculus_Value(oldValue);
            else return (-1) *  deltaCalculus_Value(newValue);
        else
            if(belowBase) return deltaCalculus_Value(newValue);
            else return deltaCalculus_Value(oldValue);
    }




    /**
     * Second method called to calculate the value od deltaPoint.
     * Calculate the |modifier| of the given AbilityScore (with a floor of 1) to determinate how many points we need to subtract from the pool
     * @param value Ability score whose modifier must be calculated
     * @return The calculated modifier or one, whichever is higher
     */
    private static int deltaCalculus_Value(int value){
        int cost = Math.abs(Unit.modCalculator(value));
        return Math.max(cost, 1);
    }

    //TODO: Controllare che il giocatore non proceda con meno di zero punti rimanenti col PB
    //TODO: Controllare che nella quarta edizione non ci sia più di un valore sotto base, e notificarlo all'utente
}

