package org.krzywanski.table.table;


import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.swing.table.TableColumn;
import java.beans.PropertyDescriptor;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Tool class for Excel export
 */
public class ExportUtils {

    public static void writeToExcell(TypedTable<?> table, Path path) throws FileNotFoundException, IOException {

        Workbook wb = new XSSFWorkbook(); //Excell workbook
        Sheet sheet = wb.createSheet(); //WorkSheet
        Row row = sheet.createRow(2); //Row created at line 3

        Row headerRow = sheet.createRow(0); //Create row at line 0

        List<TableColumn> tableColumns = new ArrayList<>(table.columnCreator.getTableColumns().values());
        for (int headings = 0; headings < tableColumns.size(); headings++) { //For each column
            headerRow.createCell(headings).setCellValue(tableColumns.get(headings).getHeaderValue().toString());//Write column name
        }

        List<?> currentData = table.provider != null ? table.provider.getData(1000, 0, null) : table.dataList;
        List<PropertyDescriptor> keyList = new ArrayList<PropertyDescriptor>(table.columnCreator.getTableColumns().keySet());
        for (int rows = 0; rows < currentData.size(); rows++) { //For each table row
            for (int cols = 0; cols < keyList.size(); cols++) { //For each table column
                try {
                    Object o = keyList.get(cols).getReadMethod().invoke(currentData.get(rows));
                    row.createCell(cols).setCellValue(o != null ? o.toString() : ""); //Write value
                } catch (IllegalAccessException | InvocationTargetException e) {
                    throw new RuntimeException(e);
                }
            }

            //Set the row to the next one in the sequence
            row = sheet.createRow((rows + 3));
        }
        wb.write(Files.newOutputStream(Paths.get(path.toString() + ".xlsx")));//Save the file
        wb.close();
    }


    public static void writeToCsv(TypedTable<?> table, Path path) throws IOException {
        exportToCSVFile(getCsvArray(table),path);
    }

    private static List<String[]>  getCsvArray(TypedTable<?> table) throws FileNotFoundException, IOException {

        List<String[]> data = new ArrayList<>();

        List<TableColumn> tableColumns = new ArrayList<>(table.columnCreator.getTableColumns().values());

        String[] currentLine = new String[tableColumns.size()];
        for (int headings = 0; headings < tableColumns.size(); headings++) { //For each column
            currentLine[headings] = tableColumns.get(headings).getHeaderValue().toString();
        }

        data.add(currentLine);

        List<?> currentData = table.provider != null ? table.provider.getData(1000, 0, null) : table.dataList;
        List<PropertyDescriptor> keyList = new ArrayList<PropertyDescriptor>(table.columnCreator.getTableColumns().keySet());
        for (int rows = 0; rows < currentData.size(); rows++) { //For each table row
            currentLine = new String[keyList.size()];
            for (int cols = 0; cols < keyList.size(); cols++) { //For each table column
                try {
                    Object o = keyList.get(cols).getReadMethod().invoke(currentData.get(rows));
                    currentLine[cols] = o != null ? o.toString() : ""; //Write value
                } catch (IllegalAccessException | InvocationTargetException e) {
                    throw new RuntimeException(e);
                }
            }
            data.add(currentLine);
        }
        return data;
    }

    private static void exportToCSVFile(List<String[]> dataLines, Path path) throws IOException {
        File csvOutputFile = new File(path.toString() + ".csv");
        try (PrintWriter pw = new PrintWriter(csvOutputFile)) {
            dataLines.stream()
                    .map(ExportUtils::convertToCSV)
                    .forEach(pw::println);
        }
    }

    private static String convertToCSV(String[] data) {
        return Stream.of(data)
                .map(ExportUtils::escapeSpecialCharacters)
                .collect(Collectors.joining(";"));
    }

    private static String escapeSpecialCharacters(String data) {
        String escapedData = data.replaceAll("\\R", " ");
        if (data.contains(",") || data.contains("\"") || data.contains("'")) {
            data = data.replace("\"", "\"\"");
            escapedData = "\"" + data + "\"";
        }
        return escapedData;
    }
}
