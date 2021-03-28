import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class Main extends JFrame implements ActionListener {

	// Menu
	JMenuBar menuBar;
	JMenu menuUser;
	JMenuItem itemLogin, itemRegister, itemExit;

	// InternalFrame
	JInternalFrame intLoginFrame = null;
	JInternalFrame intRegisterFrame = null;

	// Pane
	JDesktopPane desktopPane;

	public void initComponent() {

		// Menu
		menuBar = new JMenuBar();
		menuUser = new JMenu("User");

		itemLogin = new JMenuItem("Login");
		itemRegister = new JMenuItem("Register");
		itemExit = new JMenuItem("Exit");

		setJMenuBar(menuBar);
		menuBar.add(menuUser);

		menuUser.add(itemLogin);
		menuUser.add(itemRegister);
		menuUser.add(itemExit);
		
		// InternalFrame
		desktopPane = new JDesktopPane();
		desktopPane.setBackground(Color.LIGHT_GRAY);
		add(desktopPane);

	}

	public Main() {
		initComponent();
		
		setTitle("Electronic Shop");
		setSize(1400, 802);
		setVisible(true);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		itemLogin.addActionListener(this);
		itemRegister.addActionListener(this);
		itemExit.addActionListener(this);
	}

	public static void main(String[] args) {
		new Main();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == itemLogin) {
			if (intLoginFrame == null || intLoginFrame.isClosed() || intRegisterFrame.isClosed()) {
				
				intLoginFrame = new Login();
				desktopPane.add(intLoginFrame);
			}

		} else if (e.getSource() == itemRegister) {
			if (intRegisterFrame == null || intRegisterFrame.isClosed() || intLoginFrame.isClosed()) {

				intRegisterFrame = new Register();
				desktopPane.add(intRegisterFrame);

			}

		} else if (e.getSource() == itemExit) {

			System.exit(EXIT_ON_CLOSE);

		}
	}

}
