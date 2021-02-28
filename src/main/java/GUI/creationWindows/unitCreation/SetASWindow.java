package GUI.creationWindows.unitCreation;

import hero.AbilityScore_Generator;
import hero.Enum.AbilityScore;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class SetASWindow implements UnitCreationWindow_Parent {

    private final int[] finalAS = new int[6];

    private static final Label generatedDescriptionLabel = new Label("Here the ability score generated with the desired method");
    private static final Label assignableDescriptionLabel = new Label("Please input here your Ability Scores");
    private static final Label HPDescriptionLabel = new Label("Please input here your maximum HP");

    private static final int gridElement_MaxWidth = ((UnitCreationWindow.WINDOW_SQUARE_DIMENSION - 40)/3); //Formula: Windows dimension - margin (10+10 external, 10+10 between box), divided by 3 because three box per fow
    private static final int gridElement_Hgap = 10;
    private static final int gridElement_Vgap = 10;

    private VBox finalBox;

    private final GridPane generatedGrid = new GridPane();
    private final GridPane assignableGrid = new GridPane();
    private final TextField HPTextField = new TextField();


    //////////////////
    //PRIVATE METHOD//
    //////////////////

    /**
     * Populate generatedGrid with six uneditable TextField, in a 3*2 pattern
     */
    private void create_generatedGrid(){

        for (int row = 0; row < 2; row++) {
            for (int col = 0; col < 3; col++) {
                //textField section
                TextField temp = new TextField();
                temp.setMaxWidth(gridElement_MaxWidth);
                temp.setEditable(false);

                //constrains section
                GridPane.setConstraints(temp, col, row);

                //add section
                generatedGrid.getChildren().add(temp);
            }
        }
    }

    /**
     * Populate assignableGrid with six editable TextField, in a 3*2 pattern
     * Each TextField contains the abbreviation of the corresponding Ability Score as PromptText
     */
    private void create_assignableGrid(){
        int i = 0;
        for (int row = 0; row < 2; row++) {
            for (int col = 0; col < 3; col++) {
                TextField temp = new TextField();

                temp.setPromptText(AbilityScore.values()[i].getAbbreviation());
                i++;
                GridPane.setConstraints(temp, col, row);
                assignableGrid.getChildren().add(temp);
            }
        }
    }

    /**
     * Check the selected method to generate a set of Ability Score: if "manual" (4) il selected, end the execution,
     * else generate an array of int and use it to populate the GridPane.
     * Then, create a Vbox with a Label and the populated Grid, and add them to finalBox
     */
    private void populate_generatedGrid(){
        int[] generatedAS = null;

        switch (UnitCreationWindow.getSelectedMethod()) {
            case 0:
                generatedAS = AbilityScore_Generator.completeRandom();
                break;
            case 1:
                generatedAS = AbilityScore_Generator.classic();
                break;
            case 2:
                generatedAS = AbilityScore_Generator.standard();
                break;
            case 3:
                System.out.println("TODO");
                generatedAS = new int[]{0, 0, 0, 0, 0, 0};
                break;
            case 4: //"Manual" Case
                return;
        }

        for (int i = 0; i < 6; i++) {
            assert generatedAS != null;
            TextField temp = (TextField) generatedGrid.getChildren().get(i);

            temp.setText(Integer.toString(generatedAS[i]));
        }

        VBox vBox = new VBox();
        vBox.getChildren().addAll(generatedDescriptionLabel, generatedGrid);

        finalBox.getChildren().add(vBox);
    }

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
    public void onLoad(){
        //empty FinalBox
        if (!finalBox.getChildren().isEmpty())
            finalBox.getChildren().removeAll(finalBox.getChildren());

        populate_generatedGrid();

        VBox assignableBox = new VBox();
        assignableBox.getChildren().addAll(assignableDescriptionLabel, assignableGrid);

        //create HP box
        HBox HPBox = new HBox(10);
        HPTextField.setPromptText("HP");
        HPBox.getChildren().addAll(HPDescriptionLabel, HPTextField);


        finalBox.getChildren().addAll(assignableBox, HPBox);
    }

    /**
     * Generate the GridPane that will contain the data, and initialize a Vbox which will contain them, along with two label
     * @return the main Vbox of the class (finalBox)
     */
    @Override
    public Parent createParent(){

        ///////////////////
        //GRAPHIC SECTION//
        ///////////////////

        //generatedGrid gap setting
        generatedGrid.setHgap(gridElement_Hgap);
        generatedGrid.setVgap(gridElement_Vgap);

        //assignableGrid gap setting
        assignableGrid.setHgap(gridElement_Hgap);
        assignableGrid.setVgap(gridElement_Vgap);

        //generate the GridPane
        create_generatedGrid();
        create_assignableGrid();

        //Initialize the final Vbox to return. This box will only contain the "generatedBox" if it was created
        finalBox = new VBox(50);
        VBox.setMargin(finalBox, UnitCreationWindow.STANDARD_MARGIN);

        return finalBox;
    }

    @Override
    public void nextButtonPressed() {
        for (int i = 0; i < 6; i++) {
            TextField temp = (TextField) assignableGrid.getChildren().get(i);
            finalAS[i] = Integer.parseInt((temp.getText()));
        }
        UnitCreationWindow.setAbilityScoreArray(finalAS);
        UnitCreationWindow.setMaximumHitPoint(Integer.parseInt(HPTextField.getText()));
    }
}
