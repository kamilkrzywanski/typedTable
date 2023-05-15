package org.krzywanski.table.procressor;

import org.krzywanski.table.table.WidthMockReader;
import org.krzywanski.table.table.WidthMockWriter;

public class TableWidthProvider {

    private WidthMockReader reader;
    private WidthMockWriter writer;
    private static TableWidthProvider instance;

    TableWidthProvider(WidthMockReader reader, WidthMockWriter writer){
        this.writer = writer;
        this.reader = reader;

        instance = this;
    }

    protected WidthMockReader getReader() {
        return reader;
    }

    protected WidthMockWriter getWriter() {
        return writer;
    }

    protected static TableWidthProvider getInstance() {
        return instance;
    }
}
