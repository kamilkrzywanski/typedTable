package org.krzywanski.table.providers;

import org.krzywanski.table.providers.TableWidthTool;

public class TableWidthProvider {

    private static TableWidthTool tool;

    public static TableWidthTool getInstance() {
        return tool;
    }

    public static void setProvider(TableWidthTool tableWidthTool){
        tool = tableWidthTool;
    }


}
