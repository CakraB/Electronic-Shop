import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
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

public class Home extends JFrame implements ActionListener, MouseListener {

	// Menu
	JMenuBar menuBar;
	JMenu menuUser;
	JMenuItem itemLogout, itemExit;
	
	// Components
	JTable tblProduct, tblCart;
	JScrollPane tblScrollProduct, tblScrollCart;
	JPanel pnlProduct, pnlForm, pnlButton, pnlTotal;
	JTextField txtProductID, txtProductName, txtProductPrice, txtProductQty;
	JButton btnAdd, btnCheckout;

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
		pnlForm = new JPanel(new GridLayout(5, 2, 5, 5));
		pnlButton = new JPanel(new GridLayout(0, 2, 5, 5));
		
		// TextField
		txtProductID = new JTextField();
		txtProductName = new JTextField();
		txtProductPrice = new JTextField();
		txtProductQty = new JTextField();

		btnAdd = new JButton("Add to Cart");
		btnCheckout = new JButton("Checkout Item");

		tblProduct = new JTable();
		tblScrollProduct = new JScrollPane(tblProduct);

		tblCart = new JTable();
		tblScrollCart = new JScrollPane(tblCart);
		
		// Panel Button
		pnlButton.add(btnAdd);
		pnlButton.add(btnCheckout);

		// Panel Form
		pnlForm.add(new JLabel("Product ID"));
		pnlForm.add(txtProductID);
		pnlForm.add(new JLabel("Product Name"));
		pnlForm.add(txtProductName);
		pnlForm.add(new JLabel("Product Price"));
		pnlForm.add(txtProductPrice);
		pnlForm.add(new JLabel("Product Quantity"));
		pnlForm.add(txtProductQty);
		pnlForm.add(new JLabel(""));
		pnlForm.add(pnlButton);

		pnlProduct.add(tblScrollProduct);
		pnlProduct.add(tblScrollCart);
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

	public void insertCart() {

		String Name = txtProductName.getText();
		int Price = Integer.parseInt(txtProductPrice.getText());
		int Qty = Integer.parseInt(txtProductQty.getText());

		try {
			openConnection();
			String query = "INSERT INTO CART(ProductName, ProductPrice, ProductQty) VALUES(?,?,?)";
			ps = connect.prepareStatement(query);
			ps.setString(1, Name);
			ps.setInt(2, Price);
			ps.setInt(3, Qty);
			ps.executeUpdate();
			closeConnection();
			
			txtProductID.setText("");
			txtProductName.setText("");
			txtProductPrice.setText("");
			txtProductQty.setText("");

			JOptionPane.showMessageDialog(this, "Insert to Cart Successfully", "Message", JOptionPane.INFORMATION_MESSAGE);

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	public void checkoutCart() {
		try {
			openConnection();
			String query = "DELETE FROM CART";
			ps = connect.prepareStatement(query);
			ps.executeUpdate();
			closeConnection();

			JOptionPane.showMessageDialog(this, "Thanks for Buying", "Message", JOptionPane.INFORMATION_MESSAGE);

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	public void loadCart() {
		try {
			openConnection();
			String query = "SELECT ProductName, ProductPrice, ProductQty FROM CART";
			ps = connect.prepareStatement(query);
			rs = ps.executeQuery();

			Vector<Vector<Object>> dataAll = new Vector<>();
			Vector<String> tableHeader = new Vector<>();
			tableHeader.add("Product Name");
			tableHeader.add("Product Price");
			tableHeader.add("Product Quantity");

			while (rs.next()) {
				Vector<Object> data = new Vector<>();

				String ProductName = rs.getString(1);
				int ProductPrice = rs.getInt(2);
				int ProductQty = rs.getInt(3);

				data.add(ProductName);
				data.add(ProductPrice);
				data.add(ProductQty);

				dataAll.add(data);
			}

			DefaultTableModel dtm = new DefaultTableModel(dataAll, tableHeader);
			tblCart.setModel(dtm);
			closeConnection();
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

	public Home() {
		initComponent();

		setTitle("Electronic Shop - Home");
		setSize(1400, 802);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
		
		itemLogout.addActionListener(this);
		itemExit.addActionListener(this);
		btnAdd.addActionListener(this);
		btnCheckout.addActionListener(this);
		tblProduct.addMouseListener(this);

		loadData();
		loadCart();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == btnAdd) {
			insertCart();
			loadData();
			loadCart();
		} else if (e.getSource() == btnCheckout) {
			checkoutCart();
			loadData();
			loadCart();
		} else if (e.getSource() == itemLogout) {
			this.dispose();
		} else if (e.getSource() == itemExit) {
			System.exit(EXIT_ON_CLOSE);
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		DefaultTableModel model = (DefaultTableModel) tblProduct.getModel();
		int index = tblProduct.getSelectedRow();
		txtProductID.setText(model.getValueAt(index, 0).toString());
		txtProductName.setText(model.getValueAt(index, 1).toString());
		txtProductPrice.setText(model.getValueAt(index, 2).toString());
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
	}

}
