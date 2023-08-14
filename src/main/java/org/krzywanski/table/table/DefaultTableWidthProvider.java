package org.krzywanski.table.table;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.LinkedHashMap;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

/**
 * Serialize widths of columns to separate files in User Local appdata
 * you can Override it with
 */
public class DefaultTableWidthProvider implements TableWidthTool {
    Path saveDirectory = Paths.get(System.getenv("LOCALAPPDATA"), "typedTable");

    @Override
    @SuppressWarnings("unchecked")
    public LinkedHashMap<String, Integer> getTable(String className) {

        try {
            InputStream f = null;
            f = Files.newInputStream(saveDirectory.resolve(className.replaceAll("[-+.^:,]", "") + ".properties"), StandardOpenOption.CREATE);
            ObjectInputStream oi = new ObjectInputStream(f);
            return (LinkedHashMap<String, Integer>) oi.readObject();
        } catch (IOException | ClassNotFoundException e) {
            Logger.getAnonymousLogger().log(new LogRecord(Level.INFO, e.toString()));
            return null;
        }

    }

    @Override
    public void updateColumns(String className, LinkedHashMap<String, Integer> columnns) {
        try {
            if (!Files.exists(saveDirectory)) Files.createDirectories(saveDirectory);

            OutputStream f = Files.newOutputStream(saveDirectory.resolve(className.replaceAll("[-+.^:,]", "") + ".properties"), StandardOpenOption.CREATE);
            ObjectOutputStream o = new ObjectOutputStream(f);
            o.writeObject(columnns);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
