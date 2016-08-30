package xxlx.oracle.apps.fnd.fileupload.server;

import java.util.ArrayList;
import xxlx.oracle.apps.fnd.excelReader.ReaderCallback;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;


public class ExcelReaderCallback  extends ReaderCallback {
		private void log (String message) {
			xxlx.oracle.apps.fnd.excelReader.Logger.log(message);
		}
	
		public void newSheet(Sheet sheet, int sheetNum){
			log("Sheet Hook");
		}
		public void newRow(Row row, int rowNum, ArrayList<?> castedCells){
			log("Row Hook");
			for (Object cell:castedCells) {
				if (cell!=null)
				  log(cell.getClass().getName()+" Value:"+ cell.toString());
				else log ("Value: null");	
			}
		}
		
		
	}

