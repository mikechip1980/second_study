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
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
//import org.apache.poi.xssf.usermodel.XSSFWorkbook;
/**
 * Hello world!
 *
 */

public class ExcelReader 
{
	 private final Workbook wb;
	 private final ReaderCallback hook;
	 private HashMap<String,String> properites ;

	 private ExcelReader(Workbook wb,ReaderCallback hook) {
	     if (wb == null)
	         throw new NullPointerException("wb");
	     if (hook == null)
	         throw new NullPointerException("hook");
	     this.wb = wb;
	     this.hook =hook;
	     
	     properites = new HashMap<String,String> ();
	     properites.put("SET_ROW_ARRAY", "true");
	     properites.put("IGNORE_MIDDLE_NULLS", "false");
	 }
	 
	 public String getProperty(String name){
		 return properites.get(name)
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
	
	 @SuppressWarnings("rawtypes")
	private ArrayList setRowArray(Row row)
	 {
		 Logger.log("setRowArray");
		 return new ArrayList();
	 }
	 
	 private void readCells(Row row) {
		 Logger.log("readCells start");
		 int lastCellNum = 0;
		 Cell cell=null;
		 
		 if (row!=null) {
		 
			 	 lastCellNum=row.getLastCellNum();
		         for(int j = 0; j <= lastCellNum; j++) {
		             cell = row.getCell(j);
		             
		             if (cell==null){
		             Logger.log("calling Callback newRow ");
		              
		             }
		             
		             ArrayList<?> rowArray=null;
		             if ("true".equals(getProperty("SET_ROW_ARRAY"))) {
		            	 rowArray=setRowArray(row);
		             }
					 
					 try {
						 hook.newRow(row,j,rowArray);
					 }
					 catch (Exception e) {
						 handleHookException("newRow",e);
					 } 
		             
		         }
		 }
		 
		 Logger.log("readCells end");
	 }
	 
	 private void readRows(Sheet sheet) {
		 Logger.log("readRows start");
		 int lastRowNum = sheet.getLastRowNum();
         for(int j = 0; j <= lastRowNum; j++) {
             Row row = sheet.getRow(j);
             
             Logger.log("calling Callback newRow ");
             
             ArrayList<?> rowArray=null;
             if ("true".equals(getProperty("SET_ROW_ARRAY"))) {
            	 rowArray=setRowArray(row);
             }
			 
			 try {
				 hook.newRow(row,j,rowArray);
			 }
			 catch (Exception e) {
				 handleHookException("newRow",e);
			 } 
             
             readCells(row);
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
				 hook.newSheet(sheet, k);
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

	 
	public static void main( String[] args ) throws IOException
    {	String fileName="/home/mikechip/git/second_study/excelReader1/src/main/resource/short1.xlsx";
		
    	/*FileInputStream file=null;
        try { 
         file = new FileInputStream(fileName);
		}
		catch (FileNotFoundException e)
		{	if (file!=null) file.close();
			Logger.log(e.getMessage()+ " FileName:"+fileName);
		}*/
    Logger.log("Start");
		File file=null;
	    try { 
	     file = new File(fileName);
		}
		catch (NullPointerException e)
		{	
			Logger.logException(e,"FileName:"+fileName);
			throw new IOException("File name is empty",e);
		}
	    if (!file.exists()) {
	    	IOException e = new IOException("File does not exists");
	    	Logger.logException(e,"FileName:"+fileName);
			throw e;
	    }
	    
	    Logger.log("1");
	    
	    ExcelReader reader=null;
	    try {
			    StringWriter outWriter=new StringWriter();
				reader=ExcelReader.create(file, new ReaderCallbackTest1());
	    }
	    finally{
	    		if (reader!=null) reader.close();
	    }	
	    
	    
	    if (reader!=null) {
	    	Logger.log("Reader is not null");
	    }
    	
    }
}
