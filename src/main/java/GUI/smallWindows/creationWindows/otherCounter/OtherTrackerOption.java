package GUI.smallWindows.creationWindows.otherCounter;

public enum OtherTrackerOption {
    SPINNER("Spinner"),
    TOGGLEBUTTON("ToggleBox")
/*
    ,
    A("a"),
    B("a"),
    C("a"),
    D("a"),
    E("a"),
    F("a"),
    G("a"),
    H("a"),
    I("a"),
    L("a"),
    M("a"),
    N("a"),
    O("a"),
    P("a"),
    Q("a"),
    R("a"),
    S("a"),
    T("a"),
    U("a"),
    V("a"),
    Z("a")
*/
    ;


    private final String name;

    OtherTrackerOption(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
