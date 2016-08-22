package xxlx.oracle.apps.fnd.excelReader;

import java.util.ArrayList;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

public class ReaderCallbackTest1 extends ReaderCallback {
	public void newSheet(Sheet sheet, int sheetNum){
		Logger.log("Sheet Hook");
	}
	public void newRow(Row row, int rowNum, ArrayList<?> castedCells){
		Logger.log("Row Hook");
	}
	public void newCell(Cell cell, int cellNum, int rowNum){
		Logger.log("Cell Hook");
	}
}
