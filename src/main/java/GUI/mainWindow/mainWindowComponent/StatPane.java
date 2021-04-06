package GUI.mainWindow.mainWindowComponent;

import hero.Enum.AbilityScore;
import hero.Unit;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.io.File;
import java.text.DecimalFormat;
import java.text.ParsePosition;
import java.util.ArrayList;


/**
 * Vbox to visualize Profile Picture, AS and HP of the Unit
 */
public class StatPane {

    private static final int STAT_ROW_INDEX_START = 1;
    private static final int HP_ROW_INDEX = 9;

    private static Double maxWidth;

    private static final ImageView profileView = new ImageView();
    private static final GridPane statGrid = new GridPane();

    private static final ArrayList<TextField> asTextFieldList = new ArrayList<>();
    private static final ArrayList<TextField> asModTextFieldList = new ArrayList<>();

    private static final TextField maxHPField = new TextField();
    private static final TextField currHPField = new TextField();


    public static VBox buildBox(Double maxWidth){

        StatPane.maxWidth = maxWidth;

        VBox mainBox = new VBox(25);
        mainBox.getChildren().addAll(profileView, statGrid);

        //////////////
        //TOP: IMAGE//
        //////////////

        //global variable

        ////////////////////
        //CENTER: GRIDPANE//
        ////////////////////

        statGrid.setMaxWidth(StatPane.maxWidth);

        //gap setting
        statGrid.setHgap(10);
        statGrid.setVgap(5);

        //Column Constrain Setting
        ColumnConstraints column0 = new ColumnConstraints();
            column0.setPercentWidth(20);
        ColumnConstraints column1 = new ColumnConstraints();
            column1.setPercentWidth(40);
        ColumnConstraints column2 = new ColumnConstraints();
            column2.setPercentWidth(40);

        statGrid.getColumnConstraints().addAll(column0, column1, column2);

        //Label
        Label asLabel = new Label("Ability\nScore");
        Label modLabel = new Label("Mod");

        Label maxHPLabel = new Label("Max");
        Label currHPLabel = new Label("Current");
        Label HPLabel = new Label("HP:");

        //Populate
            //Row 0: AS Label
            GridPane.setConstraints(asLabel, 1, 0);
            GridPane.setHalignment(asLabel, HPos.CENTER);
            GridPane.setConstraints(modLabel, 2, 0);
            GridPane.setHalignment(modLabel, HPos.CENTER);
            statGrid.getChildren().addAll(asLabel, modLabel);

            //Row 1 to 6: AS
            for (int i = STAT_ROW_INDEX_START; i < AbilityScore.values().length + 1; i++) {
                Label nameLabel = new Label(AbilityScore.values()[i-1].getAbbreviation() + ":");
                TextField asField = new TextField();
                asTextFieldList.add(asField);
                //Limit only number in asField
                //https://stackoverflow.com/questions/31039449/java-8-u40-textformatter-javafx-to-restrict-user-input-only-for-decimal-number
                {
                    DecimalFormat format = new DecimalFormat("#");
                    asField.setTextFormatter(
                            new TextFormatter<>(c ->
                            {
                                if (c.getControlNewText().isEmpty()) {
                                    return c;
                                }
                                ParsePosition parsePosition = new ParsePosition(0);
                                Object object = format.parse(c.getControlNewText(), parsePosition);

                                if (object == null || parsePosition.getIndex() < c.getControlNewText().length()) {
                                    return null;
                                } else {
                                    return c;
                                }
                            }));
                }

                TextField modField = new TextField();
                asModTextFieldList.add(modField);
                modField.setEditable(false);

                //Graphic setup
                asField.setAlignment(Pos.CENTER);
                modField.setAlignment(Pos.CENTER);
                modField.setEditable(false);

                //Constraint
                GridPane.setConstraints(nameLabel, 0, i);
                GridPane.setHalignment(nameLabel, HPos.RIGHT);
                GridPane.setConstraints(asField, 1, i);
                GridPane.setConstraints(modField, 2, i);

                statGrid.getChildren().addAll(nameLabel, asField, modField);
            }

            //Row 8: HP Label
            GridPane.setConstraints(currHPLabel, 1, HP_ROW_INDEX - 1);
            GridPane.setConstraints(maxHPLabel, 2, HP_ROW_INDEX - 1);
            GridPane.setHalignment(maxHPLabel, HPos.CENTER);
            GridPane.setHalignment(currHPLabel, HPos.CENTER);
            statGrid.getChildren().addAll(currHPLabel, maxHPLabel);

            //Row 9: HP Box

            //Limit only number in HPField
            //https://stackoverflow.com/questions/31039449/java-8-u40-textformatter-javafx-to-restrict-user-input-only-for-decimal-number
            {
                DecimalFormat format = new DecimalFormat("#");
                maxHPField.setTextFormatter(
                        new TextFormatter<>(c ->
                        {
                            if (c.getControlNewText().isEmpty()) {
                                return c;
                            }

                            ParsePosition parsePosition = new ParsePosition(0);
                            Object object = format.parse(c.getControlNewText(), parsePosition);

                            if (object == null || parsePosition.getIndex() < c.getControlNewText().length()) {
                                return null;
                            } else {
                                return c;
                            }
                        }));

                currHPField.setTextFormatter(
                        new TextFormatter<>(c ->
                        {
                            if (c.getControlNewText().isEmpty()) {
                                return c;
                            }

                            ParsePosition parsePosition = new ParsePosition(0);
                            Object object = format.parse(c.getControlNewText(), parsePosition);

                            if (object == null || parsePosition.getIndex() < c.getControlNewText().length()) {
                                return null;
                            } else {
                                return c;
                            }
                        }));
            }
            //Graphic setup
            currHPField.setAlignment(Pos.CENTER);
            maxHPField.setAlignment(Pos.CENTER);

            //Constraint setup
            GridPane.setConstraints(HPLabel, 0, HP_ROW_INDEX);
            GridPane.setHalignment(HPLabel, HPos.RIGHT);
            GridPane.setConstraints(currHPField, 1, HP_ROW_INDEX);
            GridPane.setConstraints(maxHPField, 2, HP_ROW_INDEX);
            statGrid.getChildren().addAll(HPLabel, currHPField, maxHPField);

            return mainBox;
    }


    public static void populateBox(Unit unit){

        //Set image
        File imageFile = new File(unit.getImage());
        Image profileImage = new Image(imageFile.toURI().toString(), maxWidth, 0, true, true, true);
        profileView.setImage(profileImage);

        //Set AS
        for (int i = 0; i < asModTextFieldList.size(); i++) {
            int finalI = i;
            TextField abilityScore = asModTextFieldList.get(i);
            TextField modField = asTextFieldList.get(i);

            abilityScore.setText(Integer.toString(unit.getAbility_score()[i]));
            modField.setText(Integer.toString(unit.getAbility_mod()[i]));

            abilityScore.textProperty().addListener((observableValue, oldValue, newValue) -> {
                unit.setAbility_Score(finalI, Integer.parseInt(newValue));
                modField.setText((Integer.toString(unit.getAbility_mod()[finalI])));
            });
        }

        //Set HP
        currHPField.setText(Integer.toString(unit.getCurr_hp()));
        maxHPField.setText(Integer.toString(unit.getMax_hp()));
        currHPField.textProperty().addListener((observableValue, oldValue, newValue) -> unit.setCurr_hp(Integer.parseInt(newValue)));
        maxHPField.textProperty().addListener((observableValue, oldValue, newValue) -> unit.setMax_hp(Integer.parseInt(newValue)));

    }
}


