package xxlx.oracle.apps.fnd.excelReader;


import java.util.ArrayList;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

public abstract class ReaderCallback {
	public void newSheet(Sheet sheet, int sheetNum){}
	public void newRow(Row row, int rowNum, ArrayList<?> castedCells){}
	public Object newCell(Cell cell, int cellNum, int rowNum, Object castedCell){ return castedCell;}
}

