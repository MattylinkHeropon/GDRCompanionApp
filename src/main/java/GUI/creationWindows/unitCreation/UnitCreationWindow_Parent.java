package GUI.creationWindows.unitCreation;

import javafx.scene.Parent;

public interface UnitCreationWindow_Parent {

    void onLoad();

    Parent createParent();

    void nextButtonPressed();
}
