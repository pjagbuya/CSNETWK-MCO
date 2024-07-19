package org.example;

public class Paint {
    public static String paintTextOrange(String sText) {
        return "\033[1;38;5;202m" + sText + "\033[0m";
    }

    public static String paintTextCyan(String sText) {
        return "\033[1;36m" + sText + "\033[0m";
    }

    public static String paintTextRed(String sText) {
        return "\033[1;31m" + sText + "\033[0m";
    }
    
    public static String paintTextYellow(String sText) {
        return "\033[1;33m" + sText + "\033[0m";
    }

    public static String paintTextMagenta(String sText) {
        return "\033[1;35m" + sText + "\033[0m";
    }
    
    public static String paintTextGreen(String sText) {
        return "\033[1;32m" + sText + "\033[0m";
    }
    public static void printTextGreen(String sText) {
        System.out.println("\033[1;32m" + sText + "\033[0m");
    }
    public static void printTextOrange(String sText) {
        System.out.println("\033[1;32m" + sText + "\033[0m");
    }
}
