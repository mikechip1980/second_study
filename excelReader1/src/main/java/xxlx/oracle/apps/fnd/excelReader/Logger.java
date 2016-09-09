package xxlx.oracle.apps.fnd.excelReader;

public class Logger {
  public static void log (String message) {
	//  System.out.println(message);
  }

  public static void logException (Exception E, String message) {
	  System.out.println("Error:"+E.getMessage());
	  System.out.println("Error: Additional info: "+message);
	  
  }

  public static void logException (Exception E) {

	  System.out.println("Error:"+E.getMessage());
  }
  
  
}
