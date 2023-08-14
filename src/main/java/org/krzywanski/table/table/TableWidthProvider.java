package org.krzywanski.table.table;

public class TableWidthProvider {

    private static TableWidthTool tool;

    public TableWidthProvider(TableWidthTool tool){
        this.tool = tool;
    }


    protected static TableWidthTool getInstance() {
        return tool;
    }
}
