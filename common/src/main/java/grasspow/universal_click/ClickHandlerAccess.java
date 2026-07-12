package grasspow.universal_click;

public final class ClickHandlerAccess {

    private static ClickHandler handler;

    public static void set(ClickHandler handler) {
        ClickHandlerAccess.handler = handler;
    }

    public static ClickHandler get() {
        return handler;
    }

    private ClickHandlerAccess() {
    }

}
