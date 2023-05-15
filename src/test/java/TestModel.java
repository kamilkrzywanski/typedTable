import org.krzywanski.table.annot.TableColumn;

public class TestModel {


    @TableColumn()
    String columnA;

    String columnB;

    @TableColumn(label = "Test label")
    String columnC;

}
