import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class Login extends JInternalFrame implements ActionListener {
	
	JTextField txtEmail;
	JPasswordField txtPassword;
	JPanel pnlTitleLogin, pnlFormLogin, pnlBtnLogin;
	JButton btnLogin;

	public void initComponent() {
		// Input
		txtEmail = new JTextField();
		txtPassword = new JPasswordField();

		// Panel
		pnlTitleLogin = new JPanel(new BorderLayout());
		pnlFormLogin = new JPanel(new GridLayout(2, 2, 5, 5));
		pnlBtnLogin = new JPanel(new BorderLayout());

		// Button Login
		btnLogin = new JButton("Login");

		// Panel Title
		pnlTitleLogin.add(new JLabel("Login"));

		// Panel Form
		pnlFormLogin.add(new JLabel("Email"));
		pnlFormLogin.add(txtEmail);
		pnlFormLogin.add(new JLabel("Password"));
		pnlFormLogin.add(txtPassword);

		// Panel Button
		pnlBtnLogin.add(btnLogin);

		add(pnlTitleLogin, BorderLayout.NORTH);
		add(pnlFormLogin, BorderLayout.CENTER);
		add(pnlBtnLogin, BorderLayout.SOUTH);
	}
	
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
	
	public void insertLoginData() {
		
		String Email = txtEmail.getText();
		String Password = String.valueOf(txtPassword.getPassword());
		
		try {
			openConnection();
			String query = "SELECT * FROM USER WHERE UserEmail =? AND UserPassword =?";
			ps = connect.prepareStatement(query);

			ps.setString(1, Email);
			ps.setString(2, Password);

			rs = ps.executeQuery();

			if (rs.next()) {
				String Name = rs.getString("UserName");
				String msg = "" + Name;
				msg += " \n";
				JOptionPane.showMessageDialog(this, "Welcome, " + msg, "Welcome", JOptionPane.INFORMATION_MESSAGE);

				this.dispose();
				String Role = rs.getString("UserRole");

				if (Role.equals("user")) {

					Home userHome = new Home();
					setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

				} else if (Role.equals("admin")) {

					Admin userAdmin = new Admin();
					setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

				}

			} else {
				JOptionPane.showMessageDialog(this, "Invalid Email/Password", "Warning", JOptionPane.WARNING_MESSAGE);
			}

			closeConnection();
			txtEmail.setText("");
			txtPassword.setText("");

		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, e.getMessage());
		}
	}

	public Login() {
		initComponent();

		setTitle("Login");
		setBounds(500,200,50,50);
		setVisible(true);
		setSize(400, 200);
		setClosable(true);
		setResizable(true);
		
		btnLogin.addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == btnLogin) {
			insertLoginData();
		}
	}

}
