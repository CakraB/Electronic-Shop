import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class Register extends JInternalFrame implements ActionListener {

	JTextField txtName, txtEmail;
	JPasswordField txtPassword;
	JComboBox<String> txtRole;
	JPanel pnlTitleRegister, pnlFormRegister, pnlRoleRegister, pnlBtnRegister;
	JButton btnRegister;

	String Role[] = { "admin", "user" };

	public void initComponent() {
		// Input
		txtName = new JTextField();
		txtEmail = new JTextField();
		txtPassword = new JPasswordField();
		txtRole = new JComboBox<String>(Role);

		// Panel
		pnlTitleRegister = new JPanel(new GridLayout(0, 1));
		pnlFormRegister = new JPanel(new GridLayout(4, 2));
		pnlRoleRegister = new JPanel(new GridLayout(1, 1));
		pnlBtnRegister = new JPanel(new GridLayout(0, 1));

		// Button
		btnRegister = new JButton("Register");

		// Panel Title
		pnlTitleRegister.add(new JLabel("Register"));

		// Panel Form
		pnlFormRegister.add(new JLabel("Name"));
		pnlFormRegister.add(txtName);
		pnlFormRegister.add(new JLabel("Email"));
		pnlFormRegister.add(txtEmail);
		pnlFormRegister.add(new JLabel("Password"));
		pnlFormRegister.add(txtPassword);
		pnlFormRegister.add(new JLabel("Role"));
		pnlFormRegister.add(pnlRoleRegister);

		pnlRoleRegister.add(txtRole);

		// Panel Button
		pnlBtnRegister.add(btnRegister);

		add(pnlTitleRegister, BorderLayout.NORTH);
		add(pnlFormRegister, BorderLayout.CENTER);
		add(pnlBtnRegister, BorderLayout.SOUTH);

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

	public void insertUserData() {

		String Name = txtName.getText();
		int lenName = Name.length();
		String Email = txtEmail.getText();
		String Password = String.valueOf(txtPassword.getPassword());
		int lenPassword = Password.length();
		String Role = txtRole.getSelectedItem().toString();

		try {
			openConnection();
			String query = "INSERT INTO USER(UserName, UserEmail, UserPassword, UserRole) VALUES(?,?,?,?)";
			ps = connect.prepareStatement(query);

			ps.setString(1, Name);
			ps.setString(2, Email);
			ps.setString(3, Password);
			ps.setString(4, Role);

			if (lenName < 5 || lenName > 30) {
				JOptionPane.showMessageDialog(this, "Name must be 5-30 characters", "Warning",
						JOptionPane.WARNING_MESSAGE);
			} else if (lenPassword < 5 || lenName > 20) {
				JOptionPane.showMessageDialog(this, "Password must be 5-30 characters", "Warning",
						JOptionPane.WARNING_MESSAGE);
			} else {
				JOptionPane.showMessageDialog(this, "Insert Success", "Success", JOptionPane.INFORMATION_MESSAGE);
				this.dispose();
			}

			ps.executeUpdate();
			closeConnection();
			txtName.setText("");
			txtEmail.setText("");
			txtPassword.setText("");

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	public Register() {
		initComponent();

		setTitle("Register");
		setBounds(450, 60, 50, 50);
		setVisible(true);
		setSize(500, 600);
		setClosable(true);
		setResizable(true);

		btnRegister.addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == btnRegister) {
			insertUserData();
		}
	}

}
