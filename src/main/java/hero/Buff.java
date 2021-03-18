package hero;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.geometry.Insets;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;


/**
 * possess all the characteristic of a (de)buff
 */
public class Buff {
    private final boolean isBuff;
    private final String caster;
    private int duration;
    private final String effect;
    private int maskHash;

    public Buff(boolean type, String caster, int duration, String effect) {
        this.isBuff = type;
        this.caster = caster;
        this.duration = duration;
        this.effect = effect;
    }

    //Getter, Setter and small modifier
    public boolean isBuff() {
        return isBuff;
    }

    public int getDuration() {
        return duration;
    }

    public int getMaskHash() {
        return maskHash;
    }

    public void decreaseDuration() {
        duration--;

    }

    //Mask Creation

    public GridPane createBuffMask(){

        BackgroundFill stdFill;
        //TODO: collegarla al CSS
        if (isBuff) stdFill = new BackgroundFill(Color.DARKGREEN, new CornerRadii(10), null);
        else  stdFill = new BackgroundFill(Color.DARKRED, new CornerRadii(10), null);

        //Create textField and TextArea
        TextField casterField = new TextField(caster);
        casterField.setEditable(false);

        TextField durationField = new TextField(Integer.toString(duration));
        durationField.setEditable(false);

        SimpleIntegerProperty test = new SimpleIntegerProperty();
        test.set(duration);


        TextArea effectArea = new TextArea(effect);
        effectArea.setEditable(false);
        effectArea.setWrapText(true);
        effectArea.setMaxHeight(100);

        //Create GridPane to return
        GridPane gridPane = new GridPane();
        gridPane.setVgap(10);
        gridPane.setHgap(10);

        //Set Column Size (70%-30%)
        ColumnConstraints column0 = new ColumnConstraints();
        column0.setPercentWidth(70);
        ColumnConstraints column1 = new ColumnConstraints();
        column1.setPercentWidth(30);

        gridPane.getColumnConstraints().addAll(column0, column1);


        //Set Element (order is: column, row, col_span, row_span)
        GridPane.setConstraints(casterField, 0, 0);
        GridPane.setConstraints(durationField,1,0);
        GridPane.setConstraints(effectArea, 0, 1, 2, 1);

        //set Margin (Clockwise order, starting from top)
        GridPane.setMargin(casterField, new Insets(10,0,0,10));
        GridPane.setMargin(durationField, new Insets(10,10,0,0));
        GridPane.setMargin(effectArea, new Insets(0,10,10,10));

        //Set Background
        gridPane.setBackground(new Background(stdFill));

        //Final setting and return
        gridPane.getChildren().addAll(casterField, durationField, effectArea);
        maskHash = gridPane.hashCode();
        return gridPane;
    }



}
