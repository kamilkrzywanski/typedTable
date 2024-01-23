package org.krzywanski.table;

public class TableWidthProvider {

    private static TableWidthTool tool;

    protected static TableWidthTool getInstance() {
        return tool;
    }

    protected static void setProvider(TableWidthTool tableWidthTool){
        tool = tableWidthTool;
    }


}
