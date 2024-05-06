package demo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class QTMBusiness {
	Connection conn = null;
	Statement stmt = null;
	
	public void connectDB() {
		try {
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver") ;
            conn = DriverManager.getConnection("jdbc:sqlserver://localhost;encrypt=true;database=BankingDB;integratedSecurity=true"); ;
            
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	
	public int excuteDB(String sql) {
		int n=0;
		try {
			connectDB();
			stmt = conn.createStatement();
			n = stmt.executeUpdate(sql);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return n;
	}
}
