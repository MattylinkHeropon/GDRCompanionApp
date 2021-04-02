package GUI.mainWindow.mainWindowComponent.centralPane;

import GUI.mainWindow.MainWindowGUI;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

public class OptionPane {

    public static GridPane buildOptionPane(){

        ////////////////////
        //GRIDPANE SECTION//
        ////////////////////

        GridPane gridPane = new GridPane();
        gridPane.setVgap(10);


        //Add children i: Column i1: Row
        gridPane.add(themeOption(), 0, 0);
        gridPane.add(colorBlindOption(),0,1);

        return gridPane;
    }

    private static HBox themeOption () {
        HBox box = new HBox(10);
        Label label = new Label("Select a theme: ");
        ToggleGroup toggleGroup = new ToggleGroup();

        ToggleButton lightToggle = new ToggleButton("Light");
        ToggleButton darkToggle = new ToggleButton("Dark");

        lightToggle.setOnAction(actionEvent -> MainWindowGUI.changeTheme(false));
        darkToggle.setOnAction(actionEvent -> MainWindowGUI.changeTheme(true));

        toggleGroup.getToggles().addAll(lightToggle, darkToggle);
        if (MainWindowGUI.isDarkThemeSelected()) darkToggle.setSelected(true);
        else lightToggle.setSelected(true);

        box.getChildren().addAll(label, lightToggle, darkToggle);
        return box;
    }

    private static HBox colorBlindOption(){
        HBox box = new HBox(10);
        Label label = new Label("Colorblind Mode: ");
        ToggleGroup toggleGroup = new ToggleGroup();

        ToggleButton toggleOn = new ToggleButton("ON");
        ToggleButton toggleOff = new ToggleButton("OFF");

        toggleOn.setOnAction(actionEvent -> MainWindowGUI.changeColorBlind(true));
        toggleOff.setOnAction(actionEvent -> MainWindowGUI.changeColorBlind(false));

        toggleGroup.getToggles().addAll(toggleOn, toggleOff);

        if (MainWindowGUI.isColorBlind()) toggleOn.setSelected(true);
        toggleOff.setSelected(true);

        box.getChildren().addAll(label, toggleOn, toggleOff);
        return box;
    }



}
