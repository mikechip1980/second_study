package xxlx.oracle.apps.fnd.fileupload.server;

import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import xxlx.oracle.apps.fnd.utils.server.DBConnectable;


public class OracleTest {

	private static final String DB_DRIVER = "oracle.jdbc.driver.OracleDriver";
	private static final String DB_CONNECTION = "jdbc:oracle:thin:@prd-ebs-db.luxoft.com:1530:CAFSDEV";
	private static final String DB_USER = "apps";
	private static final String DB_PASSWORD = "app4dev";
	
	
	public static void main(String[] argv) {

		try {

			//selectRecordsFromTable();
			//testDescribeTable();
			testInsertTable();

		} catch (SQLException e) {

			System.out.println(e.getMessage());

		}

	} 
	
	private static void testDescribeTable() throws SQLException {
		
		StatementHelper statementHelper= new StatementHelper("AP","AP_INVOICES_ALL");
		ArrayList<StatementHelper.DesribedColumn> descrTable= new ArrayList<StatementHelper.DesribedColumn>();
		JdbcStatement statement=new JdbcStatement();
		try {
			statement.init();
			descrTable=statementHelper.describeTable(statement);

		} catch (SQLException e) {
			e.printStackTrace();
		} 
		finally {
			statement.close();
		}
		
		for (StatementHelper.DesribedColumn descColumn:descrTable) {
			System.out.println(descColumn.name+" "+descColumn.type+" "+descColumn.length+descColumn.precision);
		}
	}
	
	private static void testInsertTable() throws SQLException {
		
		StatementHelper statementHelper= new StatementHelper("apps","xxlx_java_test");

		JdbcStatement statement=new JdbcStatement();
		ArrayList row = new ArrayList();
		//row.add(new Double(1));
		row.add(null);
		row.add(new java.util.Date());
		row.add("Hello");
		
		try {
			statement.init();
			statementHelper.insertRow(statement, row );
			statement.commit();
		} catch (SQLException e) {
			e.printStackTrace();
		} 
		finally {
			statement.close();
		}
		
		
	}

	private static void selectRecordsFromTable() throws SQLException {

		Connection dbConnection = null;
		PreparedStatement preparedStatement = null;

		String selectSQL = "SELECT object_id,object_type FROM dba_objects WHERE rownum<2";

		try {
			dbConnection = JdbcConnector.getDBConnection();
			preparedStatement = dbConnection.prepareStatement(selectSQL);
			//preparedStatement.setInt(1, 1001);

			// execute select SQL stetement
			ResultSet rs = preparedStatement.executeQuery();

			while (rs.next()) {

				Integer object_id = rs.getInt(1);
				String object_type = rs.getString(2);

				System.out.println("object_id : " + object_id);
				System.out.println("object_type : " + object_type);

			}

		} catch (SQLException e) {

			System.out.println(e.getMessage());

		} finally {

			if (preparedStatement != null) {
				preparedStatement.close();
			}

			if (dbConnection != null) {
				dbConnection.close();
			}

		}

	}


}