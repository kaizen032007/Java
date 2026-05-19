package com.sora.ui;

/**
 * Standard launcher class used to bypass JavaFX runtime module path verification checks
 * during startup when running outside packaged modules.
 */
public class Launch {
    public static void main(String[] args) {
        MainApp.main(args);
    }
}