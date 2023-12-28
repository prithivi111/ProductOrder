import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConn {

	public static Connection getConnection() throws SQLException {
		Connection conn = null;
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			conn = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/productorder", "root", "Suraj123@");
			
		} catch (ClassNotFoundException e) {
					e.printStackTrace();
		}
		
		
		return conn;
		
	}
}
