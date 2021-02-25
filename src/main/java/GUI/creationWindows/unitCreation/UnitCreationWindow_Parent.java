package GUI.creationWindows.unitCreation;

import javafx.scene.Parent;

public interface UnitCreationWindow_Parent {

    public void onLoad();

    public Parent createParent();

    public void nextButtonPressed();
}
