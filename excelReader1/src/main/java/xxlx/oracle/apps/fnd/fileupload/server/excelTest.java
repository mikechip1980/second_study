package xxlx.oracle.apps.fnd.fileupload.server;

import java.io.File;
import java.io.IOException;

import xxlx.oracle.apps.fnd.excelReader.ExcelReader;
import xxlx.oracle.apps.fnd.excelReader.Logger;

public class excelTest {
	
	public static void main( String[] args ) throws IOException
    {	String fileName="c:/Temp/jar/poi_test.xlsx";
		
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
				reader=ExcelReader.create(file, new ExcelReaderCallback());
	    }
	    finally{
	    		if (reader!=null) reader.close();
	    }	
	    
	    
	    if (reader!=null) {
	    	reader.execute();
	   // 	Logger.log("Reader is not null");
	    }
    	
    }

}
