package xxlx.oracle.apps.fnd.excelReader;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

public abstract class excelHook {
	public void newSheet(Sheet sheet, int sheetNum){}
	public void newRow(Row row, int rowNum){}
	public void newCell(Cell cell, int cellNum, int rowNum){}
}
