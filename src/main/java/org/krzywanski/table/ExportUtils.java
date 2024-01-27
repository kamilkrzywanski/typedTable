package org.krzywanski.table;


import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.krzywanski.table.constraints.ActionType;
import org.krzywanski.table.utils.FieldMock;

import javax.swing.table.TableColumn;
import java.io.File;
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
    /**
     * Export data to excel file with Poi
     * @param table - table with data
     * @param path - path to file
     * @throws IOException - exception
     */
    public static <T> void writeToExcell(TypedTable<T> table, Path path) throws IOException {

        Workbook wb = new XSSFWorkbook(); //Excell workbook
        Sheet sheet = wb.createSheet(); //WorkSheet
        Row row = sheet.createRow(2); //Row created at line 3

        Row headerRow = sheet.createRow(0); //Create row at line 0

        List<TableColumn> tableColumns = new ArrayList<>(table.columnCreator.getTableColumns().values());
        for (int headings = 0; headings < tableColumns.size(); headings++) { //For each column
            headerRow.createCell(headings).setCellValue(tableColumns.get(headings).getHeaderValue().toString());//Write column name
        }

        List<T> currentData = table.provider != null ? table.provider.getData(1000, 0, table.getSortColumns(), null, ActionType.EXPORT, table.extraParams ) : table.dataList;
        List<FieldMock> keyList = table.columnCreator.getFieldMocks();
        for (int rows = 0; rows < currentData.size(); rows++) { //For each table row
            for (int cols = 0; cols < keyList.size(); cols++) { //For each table column
                try {
                    Object o = keyList.get(cols).invoke(currentData.get(rows));
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

    /**
    * Return list of string arrays with data from table for csv export
     */
    private static List<String[]>  getCsvArray(TypedTable<?> table) {

        List<String[]> data = new ArrayList<>();

        List<TableColumn> tableColumns = new ArrayList<>(table.columnCreator.getTableColumns().values());

        String[] currentLine = new String[tableColumns.size()];
        for (int headings = 0; headings < tableColumns.size(); headings++) { //For each column
            currentLine[headings] = tableColumns.get(headings).getHeaderValue().toString();
        }

        data.add(currentLine);

        List<?> currentData = table.provider != null ? table.provider.getData(1000, 0, table.getSortColumns(), table.getSearchPhase(), ActionType.EXPORT, table.extraParams) : table.dataList;
        List<FieldMock> keyList = table.columnCreator.getFieldMocks();
        for (Object currentDatum : currentData) { //For each table row
            currentLine = new String[keyList.size()];
            for (int cols = 0; cols < keyList.size(); cols++) { //For each table column
                try {
                    Object o = keyList.get(cols).getPropertyDescriptor().getReadMethod().invoke(currentDatum);
                    currentLine[cols] = o != null ? o.toString() : ""; //Write value
                } catch (IllegalAccessException | InvocationTargetException e) {
                    throw new RuntimeException(e);
                }
            }
            data.add(currentLine);
        }
        return data;
    }

    /**
     * Export data to csv file
     * @param dataLines - list of string arrays with data
     * @param path - path to file
     * @throws IOException - exception
     */
    private static void exportToCSVFile(List<String[]> dataLines, Path path) throws IOException {
        File csvOutputFile = new File(path.toString() + ".csv");
        try (PrintWriter pw = new PrintWriter(csvOutputFile)) {
            dataLines.stream()
                    .map(ExportUtils::convertToCSV)
                    .forEach(pw::println);
        }
    }

    /**
     * Convert array of strings to csv line
     * @param data - array of strings
     * @return - csv line
     */
    private static String convertToCSV(String[] data) {
        return Stream.of(data)
                .map(ExportUtils::escapeSpecialCharacters)
                .collect(Collectors.joining(";"));
    }

    /**
     * Escape special characters form strings
     * @param data - string with special characters
     * @return - escaped strins
     */
    private static String escapeSpecialCharacters(String data) {
        String escapedData = data.replaceAll("\\R", " ");
        if (data.contains(",") || data.contains("\"") || data.contains("'")) {
            data = data.replace("\"", "\"\"");
            escapedData = "\"" + data + "\"";
        }
        return escapedData;
    }
}
