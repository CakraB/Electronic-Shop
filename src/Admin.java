import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

public class Admin extends JFrame implements ActionListener {

	// Menu
	JMenuBar menuBar;
	JMenu menuUser;
	JMenuItem itemLogout, itemExit;

	// Components
	JTable tblProduct;
	JScrollPane tblScroll;
	JPanel pnlProduct, pnlForm, pnlButton;
	JTextField txtProductID, txtProductName, txtProductPrice;
	JButton btnInsert, btnUpdate, btnDelete;

	public void initComponent() {
		
		// Menu
		menuBar = new JMenuBar();
		menuUser = new JMenu("User");
		itemLogout = new JMenuItem("Logout");
		itemExit = new JMenuItem("Exit");

		setJMenuBar(menuBar);

		menuBar.add(menuUser);
		menuUser.add(itemLogout);
		menuUser.add(itemExit);
		
		// Panel
		pnlProduct = new JPanel(new GridLayout(0, 2, 5, 5));
		pnlForm = new JPanel(new GridLayout(4, 2, 5, 5));
		pnlButton = new JPanel(new GridLayout(0, 3, 5, 5));
		
		// TextField
		txtProductID = new JTextField();
		txtProductName = new JTextField();
		txtProductPrice = new JTextField();
		
		// Button
		btnInsert = new JButton("Insert");
		btnDelete = new JButton("Delete");
		btnUpdate = new JButton("Update");

		tblProduct = new JTable();
		tblScroll = new JScrollPane(tblProduct);

		pnlButton.add(btnInsert);
		pnlButton.add(btnUpdate);
		pnlButton.add(btnDelete);
		
		// Panel Button
		pnlForm.add(new JLabel("Product ID"));
		pnlForm.add(txtProductID);
		pnlForm.add(new JLabel("Product Name"));
		pnlForm.add(txtProductName);
		pnlForm.add(new JLabel("Product Price"));
		pnlForm.add(txtProductPrice);
		pnlForm.add(new JLabel(""));
		pnlForm.add(pnlButton);

		pnlProduct.add(tblScroll);
		pnlProduct.add(pnlForm);

		add(pnlProduct);

	}

	Connection connect;
	ResultSet rs;
	PreparedStatement ps;

	public void openConnection() throws Exception {
		Class.forName("com.mysql.jdbc.Driver");
		connect = DriverManager.getConnection("jdbc:mysql://localhost/project_bad_kelas", "root", "");
	}

	public void closeConnection() throws Exception {
		ps.close();
		connect.close();
	}

	public void insertData() {

		String Name = txtProductName.getText();
		int Price = Integer.parseInt(txtProductPrice.getText());

		try {
			openConnection();
			String query = "INSERT INTO PRODUCT(ProductName, ProductPrice) VALUES(?,?)";
			ps = connect.prepareStatement(query);
			ps.setString(1, Name);
			ps.setInt(2, Price);
			ps.executeUpdate();
			closeConnection();

			txtProductName.setText("");
			txtProductPrice.setText("");

			JOptionPane.showMessageDialog(this, "Insert Successfully", "Message", JOptionPane.INFORMATION_MESSAGE);

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	public void deleteData() {

		int Id = Integer.parseInt(txtProductID.getText());

		try {
			openConnection();
			String query = "DELETE FROM PRODUCT WHERE ProductID = ?";
			ps = connect.prepareStatement(query);
			ps.setInt(1, Id);
			ps.executeUpdate();
			closeConnection();

			txtProductID.setText("");

			JOptionPane.showMessageDialog(this, "Delete Successfully", "Message", JOptionPane.INFORMATION_MESSAGE);

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	public void updateData() {

		String Name = txtProductName.getText();
		int Price = Integer.parseInt(txtProductPrice.getText());
		int Id = Integer.parseInt(txtProductID.getText());

		try {
			openConnection();
			String query = "UPDATE PRODUCT SET ProductName= ?, ProductPrice= ? WHERE ProductID= ?";
			ps = connect.prepareStatement(query);

			ps.setString(1, Name);
			ps.setInt(2, Price);
			ps.setInt(3, Id);

			ps.executeUpdate();
			closeConnection();

			txtProductID.setText("");
			txtProductName.setText("");
			txtProductPrice.setText("");

			JOptionPane.showMessageDialog(this, "Update Successfully", "Message", JOptionPane.INFORMATION_MESSAGE);

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	public void loadData() {
		try {
			openConnection();
			String query = "SELECT * FROM PRODUCT";
			ps = connect.prepareStatement(query);
			rs = ps.executeQuery();

			Vector<Vector<Object>> dataAll = new Vector<>();
			Vector<String> tableHeader = new Vector<>();
			tableHeader.add("Product ID");
			tableHeader.add("Product Name");
			tableHeader.add("Product Price");

			while (rs.next()) {
				Vector<Object> data = new Vector<>();

				int ProductID = rs.getInt(1);
				String ProductName = rs.getString(2);
				int ProductPrice = rs.getInt(3);

				data.add(ProductID);
				data.add(ProductName);
				data.add(ProductPrice);

				dataAll.add(data);
			}

			DefaultTableModel dtm = new DefaultTableModel(dataAll, tableHeader);
			tblProduct.setModel(dtm);
			closeConnection();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	public Admin() {
		initComponent();

		setTitle("Electronic Shop - Admin");
		setSize(1400, 802);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);

		itemLogout.addActionListener(this);
		itemExit.addActionListener(this);
		btnInsert.addActionListener(this);
		btnUpdate.addActionListener(this);
		btnDelete.addActionListener(this);

		loadData();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == btnInsert) {
			insertData();
			loadData();
		} else if (e.getSource() == btnUpdate) {
			updateData();
			loadData();
		} else if (e.getSource() == btnDelete) {
			deleteData();
			loadData();
		} else if (e.getSource() == itemLogout) {
			this.dispose();
		} else if (e.getSource() == itemExit) {
			System.exit(EXIT_ON_CLOSE);
		}
	}
}
