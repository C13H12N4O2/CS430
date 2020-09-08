import java.io.*;
import java.sql.*;
import java.util.Scanner;

class P2{
	// the host name of the server and the server instance name/id
	public static final String oracleServer = "dbs3.cs.umb.edu";
	public static final String oracleServerSid = "dbs3";
	public static void main(String args[]) {
		Connection conn = null;
		conn = getConnection();
		if (conn==null)
			System.exit(1);

		//now execute query
		Scanner input = new Scanner(System.in);
		try {
		  // Create statement object
		  Statement stmt = conn.createStatement();
		  while(true){
	 	    //get room number from user
		    System.out.print("Room Number = ");
		    int room_no=input.nextInt();
		    if (room_no==-1)
			break;
		    ResultSet rs = stmt.executeQuery("select name, rank from instructors"
			+ " where roomno="+room_no);
		    if (rs.next()){
		    	do{
		        	System.out.println(
					"Name = " + rs.getString("name")+
					", Rank = " + rs.getString("rank")
		        	);
		      	}while(rs.next());
	 	    }	
		    else
			System.out.println("No Records Retrieved");
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
