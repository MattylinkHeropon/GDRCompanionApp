package GUI.mainWindow;

import hero.Enum.AbilityScore;
import hero.Unit;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import java.io.File;



/**
 * Vbox to visualize Profile Picture, AS and HP of the Unit
 */
public class StatPane {

    public static final int GRID_WIDTH = 150;

    private VBox completeBox;
    private final Unit pg;

    public StatPane(Unit pg) {
        this.pg = pg;
        buildNode();
    }

    public VBox getCompleteBox() {
        return completeBox;
    }


    private void buildNode() {

        /////////
        //IMAGE//
        /////////
        File imageFile = new File(pg.getImage());
        Image profileImage = new Image(imageFile.toURI().toString(), GRID_WIDTH, 0, true, true, true);
        ImageView profileView = new ImageView(profileImage);

        ////////
        //VBOX//
        ////////
        VBox box = new VBox(25);
        box.getChildren().addAll(profileView, buildGrid());
        completeBox = box;
    }

    private GridPane buildGrid() {

        ////////////////////
        //GRIDPANE SECTION//
        ////////////////////

        GridPane statGrid = new GridPane();
        statGrid.setMaxWidth(GRID_WIDTH);

        //gap setting
        statGrid.setHgap(10);
        statGrid.setVgap(5);

        //Column Constrain Setting
        ColumnConstraints column0 = new ColumnConstraints();
        ColumnConstraints column1 = new ColumnConstraints();
        ColumnConstraints column2 = new ColumnConstraints();
        column0.setPercentWidth(20);
        column1.setPercentWidth(40);
        column2.setPercentWidth(40);

        /////////////////
        //LABEL SECTION//
        /////////////////

        Label asLabel = new Label("Ability\nScore");
        Label modLabel = new Label("Mod");

        Label maxHPLabel = new Label("Max");
        Label currHPLabel = new Label("Current");
        Label HPLabel = new Label("HP:");

        //Label Constrain and Alignment
        GridPane.setConstraints(asLabel, 1, 0);
        GridPane.setHalignment(asLabel, HPos.CENTER);
        GridPane.setConstraints(modLabel, 2, 0);
        GridPane.setHalignment(modLabel, HPos.CENTER);


        //////////////////
        //GRIDPANE SETUP//
        //////////////////

        //Set first row
        statGrid.getColumnConstraints().addAll(column0, column1, column2);
        statGrid.getChildren().addAll(asLabel, modLabel);

        int i;

        //Generating Ability Score rows
        for (i = 0; i < pg.getAbility_score().length; i++) {

            //Create
            Label nameLabel = new Label(AbilityScore.values()[i].getAbbreviation() + ":");
            TextField asField = new TextField((Integer.toString(pg.getAbility_score()[i])));
            TextField modField = new TextField((Integer.toString(pg.getAbility_mod()[i])));

            //Graphic setup
            asField.setAlignment(Pos.CENTER);
            modField.setAlignment(Pos.CENTER);
            modField.setEditable(false);

            //Constraint
            GridPane.setConstraints(nameLabel, 0, i + 1);
            GridPane.setHalignment(nameLabel, HPos.RIGHT);
            GridPane.setConstraints(asField, 1, i + 1);
            GridPane.setConstraints(modField, 2, i + 1);

            //trying setting an update method:
            int finalI = i;
            asField.textProperty().addListener((observableValue, oldValue, newValue) -> {
                pg.setAbility_Score(finalI, Integer.parseInt(newValue));
                modField.setText((Integer.toString(pg.getAbility_mod()[finalI])));
            });

            statGrid.getChildren().addAll(nameLabel, asField, modField);
        }

        i = i + 2; //leave an empty space

        //HP section
        {
            //Create
            TextField maxHPField = new TextField(Integer.toString(pg.getMax_hp()));
            TextField currHPField = new TextField(Integer.toString(pg.getCurr_hp()));

            //listener to modify current and maximum HP values
            currHPField.textProperty().addListener((observableValue, oldValue, newValue) -> pg.setCurr_hp(Integer.parseInt(newValue)));
            maxHPField.textProperty().addListener((observableValue, oldValue, newValue) -> pg.setMax_hp(Integer.parseInt(newValue))); //TODO: creare metodo per sistemare i maxHP

            //Graphic setup
            currHPField.setAlignment(Pos.CENTER);
            maxHPField.setAlignment(Pos.CENTER);
            maxHPField.setEditable(false);

            //Constraint setup, top Label
            GridPane.setConstraints(currHPLabel, 1, i);
            GridPane.setConstraints(maxHPLabel, 2, i);
            GridPane.setHalignment(maxHPLabel, HPos.CENTER);
            GridPane.setHalignment(currHPLabel, HPos.CENTER);

            //Constraint setup, HP row
            GridPane.setConstraints(HPLabel, 0, i + 1);
            GridPane.setHalignment(HPLabel, HPos.RIGHT);
            GridPane.setConstraints(currHPField, 1, i + 1);
            GridPane.setConstraints(maxHPField, 2, i + 1);

            statGrid.getChildren().addAll(maxHPLabel, maxHPField, HPLabel, currHPLabel, currHPField);
        }

        return statGrid;
    }
}


