import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Connect {

	Connection connect = null;
	ResultSet rs = null;
	PreparedStatement ps = null;

	public void openConnection() throws Exception {
		Class.forName("com.mysql.jdbc.Driver");
		connect = DriverManager.getConnection("jdbc:mysql://localhost/project_bad_kelas", "root", "");
	}

	public void closeConnection() throws Exception {
		ps.close();
		connect.close();
	}
}

