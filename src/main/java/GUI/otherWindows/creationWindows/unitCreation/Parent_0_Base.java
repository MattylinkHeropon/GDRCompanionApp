package GUI.otherWindows.creationWindows.unitCreation;

import javafx.beans.binding.BooleanBinding;
import javafx.scene.Parent;

public interface Parent_0_Base {

    void onLoad();

    Parent createParent();

    void nextButtonPressed();
    //It's always the condition tha BLOCK the button
    BooleanBinding nextButtonDisableCondition();
}
