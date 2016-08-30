package xxlx.oracle.apps.fnd.excelReader;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.poi.EncryptedDocumentException;
//import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

/**
 * Hello world!
 *
 */

public class ExcelReader 
{
	 private final Workbook wb;
	 private ReaderCallback sheetHook,rowHook,cellHook;
	 private HashMap<String,String> properites ;

	 private ExcelReader(Workbook wb,ReaderCallback hook) {
	     if (wb == null)
	         throw new NullPointerException("wb");
	     if (hook == null)
	         throw new NullPointerException("hook");
	     this.wb = wb;
	     this.sheetHook =hook;
	     this.rowHook=hook;
	     this.cellHook=hook;
	     
	     properites = new HashMap<String,String> ();
	     properites.put("SET_ROW_ARRAY", "true");
	     properites.put("IGNORE_MIDDLE_NULLS", "false");
	 }
	 
	 public String getProperty(String name){
		 return properites.get(name);
	 }
	 
	 public void setCellHook(ReaderCallback hook){
		 this.cellHook=hook;
	 }
	
	/**
	  * Creates a new converter to HTML for the given workbook.  This attempts to
	  * detect whether the input is XML (so it should create an {@link
	  * XSSFWorkbook} or not (so it should create an {@link HSSFWorkbook}).
	  *
	  * @param in     The input stream that has the workbook.
	  * @param output Where the HTML output will be written.
	  *
	  * @return An object for converting the workbook to HTML.
	  */
	 public static ExcelReader create(InputStream in, ReaderCallback hook)
	         throws IOException {
	     try {
	         Workbook wb = WorkbookFactory.create(in);
	         return create(wb,hook);
	     } catch (InvalidFormatException e){
	    	 Logger.logException(e);
	    	 throw new IllegalArgumentException("Cannot create workbook from stream", e);
	     }
	    	 catch (EncryptedDocumentException e) {
	    	 Logger.logException(e);
	    	 throw new IllegalArgumentException("Cannot oopen encriped stream", e);  
	    	   
	       }
	     }

	 public static ExcelReader create(File inputFile,ReaderCallback hook)
	         throws IOException {
	     try {
	         Workbook wb = WorkbookFactory.create(inputFile);
	         return create(wb, hook);
	     } catch (InvalidFormatException e){
	    	 Logger.logException(e);
	    	 throw new IllegalArgumentException("Cannot create workbook from file", e);
	     }
	    	 catch (EncryptedDocumentException e) {
	    	 Logger.logException(e);
	    	 throw new IllegalArgumentException("Cannot oopen encriped file", e);  
	    	   
	       }
	     }
	
	 public void close() {
		 if (wb!=null){
			 try {
				wb.close();
			} catch (IOException e) {
				Logger.logException(e);
			}
		 }
	 }
	 
	 private void handleHookException(String hookMethod,Exception e)
	 {
		 Logger.logException(e,hookMethod);
	 }
	
	 
	 public Object getCastedCell(Cell cell) {
		// Logger.log("getCastedCell start"); 
		 
		 if (cell==null) 
			 throw new IllegalArgumentException("Cell parameter is Null");
		 
		 switch (cell.getCellType()) {
         case Cell.CELL_TYPE_STRING:
        //	 Logger.log("String");
             return cell.getRichStringCellValue().getString();
            // break;
         case Cell.CELL_TYPE_NUMERIC:
             if (DateUtil.isCellDateFormatted(cell)) {
          //  	 Logger.log("Date");
                 return cell.getDateCellValue();
             } else {
        //    	 Logger.log("Number");
                 return cell.getNumericCellValue();
             }
          //   break;
         case Cell.CELL_TYPE_BOOLEAN:
        //	 Logger.log("Boolean");
             return cell.getBooleanCellValue();
         //    break;
         case Cell.CELL_TYPE_FORMULA:
        	// Logger.log("Formula");
             //System.out.println(cell.getCellFormula());
        	 return cell.getStringCellValue();
          //   break;
         case Cell.CELL_TYPE_BLANK:
        //	 Logger.log("Blank");
             return new Object();
          //   break;
         default:
        //	 Logger.log("Smth else");
             return new Object();
     }
		 
		// Logger.log("getCastedCell end");
	 }
	 
	 

	 
	 @SuppressWarnings("rawtypes")
	private ArrayList setRowArray(Row row)
	 {
		 Logger.log("setRowArray");
		 int lastCellNum = 0;
		 Cell cell=null;
		 ArrayList cellsList= new ArrayList();
		 if (row!=null) {
			 
		 	 lastCellNum=row.getLastCellNum();
	         for(int j = 0; j <= lastCellNum; j++) {
	        	 cell = row.getCell(j);
	        	 if (cell==null) 
	        		 cellsList.add(new Object());
	        	 else {
		             Object castedCell = getCastedCell(cell);
		             cellsList.add(castedCell);
	        	 }
	         }
	 }
		 
		 return cellsList;
	 }
	 
	 @SuppressWarnings("unchecked")
	private ArrayList readCells(Row row,int rowNum) {
		 Logger.log("readCells start");
		 int lastCellNum = 0;
		 Cell cell=null;
		 ArrayList result=null;
		 
		 if (row!=null) {
			 	
			 	 result=new ArrayList();
			 	 lastCellNum=row.getLastCellNum();
		         for(int j = 0; j <= lastCellNum; j++) {
		             cell = row.getCell(j,Row.RETURN_BLANK_AS_NULL);
		             
		             if (cell==null){
		            	 result.add(cell);
		             } else
		            	 
					 try {						 
						result.add( cellHook.newCell(cell,rowNum,j, getCastedCell(cell)));
					 }
					 catch (Exception e) {
						 handleHookException("newRow",e);
					 } 
		             
		         }
		 }
		 
		 Logger.log("readCells end");
		 return result;
	 } 
	 
	 private void readRows(Sheet sheet) {
		 Logger.log("readRows start");
		 int lastRowNum = sheet.getLastRowNum();
         for(int j = 0; j <= lastRowNum; j++) {
             Row row = sheet.getRow(j);
             
             Logger.log("calling Callback newRow ");
             
            /* ArrayList<?> rowArray=null;
             if ("true".equals(getProperty("SET_ROW_ARRAY"))) {
            	 rowArray=setRowArray(row);
             }*/
             ArrayList castedCells = readCells(row,j);
             
			 try {
				 rowHook.newRow(row,j,castedCells);
			 }
			 catch (Exception e) {
				 handleHookException("newRow",e);
			 } 
             
             
         } 
		 
		 Logger.log("readRows end");
	 }
	 
	 
	 private void readSheets() {
		 Logger.log("readSheets start");
		 for (int k = 0; k < wb.getNumberOfSheets(); k++){
			 Sheet sheet= wb.getSheetAt(k);
			 
			 Logger.log("current Sheet is "+k+" "+sheet.getSheetName());
			 Logger.log("calling Callback newSheet ");
			 
			 try {
				 sheetHook.newSheet(sheet, k);
			 }
			 catch (Exception e) {
				 handleHookException("newSheet",e);
			 } 
			 if(sheet.getPhysicalNumberOfRows() > 0) {
				 readRows(sheet);
			 }	 
			 
		 }
		 Logger.log("readSheets end"); 
	 }
	 
	 public void execute() {
		 Logger.log("Execute start");
		 readSheets();
		 Logger.log("Execute end");
	 }

	 /**
	  * Creates a new converter to HTML for the given workbook.
	  *
	  * @param wb     The workbook.
	  * @param output Where the HTML output will be written.
	  *
	  * @return An object for converting the workbook to HTML.
	  */
	 
	 
	 public static ExcelReader create(Workbook wb, ReaderCallback hook) {
	     return new ExcelReader(wb, hook);
	 }

	 
	
}
