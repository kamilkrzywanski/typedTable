package org.krzywanski.table.table;

/**
 * Deafults for typed table
 * Setters have public acces if you want change carret symbols.
 */
public class TypedTableDefaults {

    protected static String CARRET_ASC_SYMBOL = " ▲";
    protected static String CARRET_DESC_SYMBOL = " ▼";

    public static void setCarretAscSymbol(String carretAscSymbol) {
        CARRET_ASC_SYMBOL = carretAscSymbol;
    }

    public static void setCarretDescSymbol(String carretDescSymbol) {
        CARRET_DESC_SYMBOL = carretDescSymbol;
    }
}
