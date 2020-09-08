import java.io.*;
import java.sql.*;
import java.util.Scanner;

class P3{
	// the host name of the server and the server instance name/id
	public static final String oracleServer = "dbs3.cs.umb.edu";
	public static final String oracleServerSid = "dbs3";
	public static void main(String args[]) {
		Connection conn = null;
		conn = getConnection();
		if (conn==null)
			System.exit(1);

		//inspect metadata
		Scanner input = new Scanner(System.in);
		try {
		  //retrieve info about tables matching input 
		  while(true){
	 	    //get table name from user
		    System.out.print("Search Key = ");
		    String filter=input.nextLine();
		    if (filter.equals("-1"))
			break;

			DatabaseMetaData md=conn.getMetaData();
			ResultSet trs=md.getTables(null,null,filter,null);
			if (trs.next()){
			    do{
				String tableName = trs.getString("TABLE_NAME");
				System.out.println("Table: " + tableName);
				ResultSet crs = md.getColumns(null,null,tableName, null);
				while (crs.next()) {
					System.out.println(
					  "COL_NAME="+crs.getString("COLUMN_NAME")+
					  ", TYPE="+crs.getString("TYPE_NAME")
					);
				}
			    }while(trs.next());
			}
			else
		            System.out.println("No Such Table");	
		  }
		} catch (SQLException e) {
			System.out.println ("ERROR OCCURRED");	
			e.printStackTrace();
		}
	}

	public static Connection getConnection(){

		// first we need to load the driver
		String jdbcDriver = "oracle.jdbc.OracleDriver";
		try {
			Class.forName(jdbcDriver); 
		} catch (Exception e) {
			e.printStackTrace();
		}

		// Get username and password
		Scanner input = new Scanner(System.in);
		System.out.print("Username:");
		String username = input.nextLine();
		System.out.print("Password:");
		//the following is used to mask the password
		Console console = System.console();
		String password = new String(console.readPassword()); 
		String connString = "jdbc:oracle:thin:@" + oracleServer + ":1521:"
				+ oracleServerSid;

		System.out.println("Connecting to the database...");
	
		Connection conn;
		// Connect to the database
		try{
			conn = DriverManager.getConnection(connString, username, password);
			System.out.println("Connection Successful");
		}
		catch(SQLException e){
			System.out.println("Connection ERROR");
			e.printStackTrace();	
			return null;
		}

		return conn;
	}
}
