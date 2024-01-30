package org.krzywanski.table.constraints;

/**
 * Deafults for typed table
 * Setters have public acces if you want change carret symbols.
 */
public class TypedTableDefaults {

    public static String CARRET_ASC_SYMBOL = " ▲";
    public static String CARRET_DESC_SYMBOL = " ▼";

    public static void setCarretAscSymbol(String carretAscSymbol) {
        CARRET_ASC_SYMBOL = carretAscSymbol;
    }

    public static void setCarretDescSymbol(String carretDescSymbol) {
        CARRET_DESC_SYMBOL = carretDescSymbol;
    }
}
