package xxlx.oracle.apps.fnd.excelReader;

import java.io.File;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
/**
 * Hello world!
 *
 */

public class App 
{
	public static void main( String[] args )
    {	File file= new File("asdfasd");
    	Workbook wb = WorkbookFactory.create(file);
        System.out.println( "Hello World!" );
    }
}
