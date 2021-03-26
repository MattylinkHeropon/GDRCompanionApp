open module GDRCompanionApp {
    //this error is caused by multi-platform support. Code compile without a problem, but I'm still looking how to resolve it
    requires javafx.controls;
    requires java.datatransfer;
    requires com.google.gson;

    exports GUI;
    exports hero;
    exports hero.Enum;

}