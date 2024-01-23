package org.krzywanski.table;

import org.krzywanski.table.providers.TableWidthTool;

public class TableWidthProvider {

    private static TableWidthTool tool;

    protected static TableWidthTool getInstance() {
        return tool;
    }

    protected static void setProvider(TableWidthTool tableWidthTool){
        tool = tableWidthTool;
    }


}
