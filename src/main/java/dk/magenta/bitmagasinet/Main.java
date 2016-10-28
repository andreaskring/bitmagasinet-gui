package dk.magenta.bitmagasinet;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JToolBar;
import javax.swing.JTabbedPane;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.UIManager;
import javax.swing.JSplitPane;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.ListSelectionModel;
import javax.swing.JList;
import javax.swing.AbstractListModel;
import javax.swing.border.TitledBorder;
import java.awt.Font;
import javax.swing.SwingConstants;
import javax.swing.JTextField;
import javax.swing.JPasswordField;

public class Main extends JFrame {

	private JPanel contentPane;
	private JTextField textField;
	private JTextField txtUsername;
	private JPasswordField pwdPwd;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Throwable e) {
			e.printStackTrace();
		}
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Main frame = new Main();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Main() {
		
		initComponents();
		createEvents();
		
	}

	private void initComponents() {

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1012, 642);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addComponent(tabbedPane, GroupLayout.DEFAULT_SIZE, 704, Short.MAX_VALUE)
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap()
					.addComponent(tabbedPane, GroupLayout.DEFAULT_SIZE, 607, Short.MAX_VALUE))
		);
		
		JPanel pnlInputList = new JPanel();
		pnlInputList.setName("");
		tabbedPane.addTab("Bitmagasiner", null, pnlInputList, null);
		
		JLabel lblVlgBitmagasin = new JLabel("Vælg Bitmagasin");
		lblVlgBitmagasin.setFont(new Font("Dialog", Font.BOLD, 12));
		
		JScrollPane scrollPane = new JScrollPane();
		
		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(null, "Indstillinger", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		GroupLayout gl_pnlInputList = new GroupLayout(pnlInputList);
		gl_pnlInputList.setHorizontalGroup(
			gl_pnlInputList.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_pnlInputList.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_pnlInputList.createParallelGroup(Alignment.LEADING)
						.addComponent(lblVlgBitmagasin)
						.addGroup(gl_pnlInputList.createSequentialGroup()
							.addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 263, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(panel, GroupLayout.PREFERRED_SIZE, 587, GroupLayout.PREFERRED_SIZE)))
					.addContainerGap(123, Short.MAX_VALUE))
		);
		gl_pnlInputList.setVerticalGroup(
			gl_pnlInputList.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_pnlInputList.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblVlgBitmagasin)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_pnlInputList.createParallelGroup(Alignment.LEADING)
						.addComponent(panel, GroupLayout.PREFERRED_SIZE, 449, GroupLayout.PREFERRED_SIZE)
						.addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 106, GroupLayout.PREFERRED_SIZE))
					.addContainerGap(111, Short.MAX_VALUE))
		);
		
		JLabel lblUrl = new JLabel("URL:");
		
		textField = new JTextField();
		textField.setColumns(10);
		
		JLabel lblUsername = new JLabel("Username:");
		
		JLabel lblPassword = new JLabel("Password:");
		
		txtUsername = new JTextField();
		txtUsername.setText("Username");
		txtUsername.setColumns(10);
		
		pwdPwd = new JPasswordField();
		pwdPwd.setText("Pwd");
		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel.createSequentialGroup()
							.addContainerGap()
							.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_panel.createSequentialGroup()
									.addComponent(lblUrl)
									.addPreferredGap(ComponentPlacement.UNRELATED)
									.addComponent(textField, GroupLayout.PREFERRED_SIZE, 276, GroupLayout.PREFERRED_SIZE))
								.addGroup(gl_panel.createSequentialGroup()
									.addGroup(gl_panel.createParallelGroup(Alignment.TRAILING)
										.addComponent(lblPassword)
										.addComponent(lblUsername))
									.addPreferredGap(ComponentPlacement.UNRELATED)
									.addComponent(txtUsername, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))))
						.addGroup(gl_panel.createSequentialGroup()
							.addGap(124)
							.addComponent(pwdPwd, GroupLayout.PREFERRED_SIZE, 110, GroupLayout.PREFERRED_SIZE)))
					.addContainerGap(239, Short.MAX_VALUE))
		);
		gl_panel.setVerticalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addGap(26)
					.addGroup(gl_panel.createParallelGroup(Alignment.TRAILING)
						.addComponent(textField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblUrl))
					.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel.createSequentialGroup()
							.addGap(42)
							.addComponent(lblUsername)
							.addGap(41)
							.addComponent(lblPassword))
						.addGroup(gl_panel.createSequentialGroup()
							.addGap(53)
							.addComponent(txtUsername, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
					.addGap(9)
					.addComponent(pwdPwd, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(241, Short.MAX_VALUE))
		);
		panel.setLayout(gl_panel);
		
		JList list = new JList();
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.setModel(new AbstractListModel() {
			String[] values = new String[] {"Statsbiblioteket", "Det Kongelige Bibliotek", "Bitmagasin no 1", "Prima Bitmagasin", "Bitmagasin no 7", "Superlageret", "Aarhus Universitet"};
			public int getSize() {
				return values.length;
			}
			public Object getElementAt(int index) {
				return values[index];
			}
		});
		list.setSelectedIndex(0);
		scrollPane.setViewportView(list);
		pnlInputList.setLayout(gl_pnlInputList);
		
		JPanel pnlChecksumList = new JPanel();
		tabbedPane.addTab("Kontrolsummer", null, pnlChecksumList, null);
		GroupLayout gl_pnlChecksumList = new GroupLayout(pnlChecksumList);
		gl_pnlChecksumList.setHorizontalGroup(
			gl_pnlChecksumList.createParallelGroup(Alignment.LEADING)
				.addGap(0, 697, Short.MAX_VALUE)
		);
		gl_pnlChecksumList.setVerticalGroup(
			gl_pnlChecksumList.createParallelGroup(Alignment.LEADING)
				.addGap(0, 552, Short.MAX_VALUE)
		);
		pnlChecksumList.setLayout(gl_pnlChecksumList);
		contentPane.setLayout(gl_contentPane);
		
	}

	
	private void createEvents() {
		// TODO Auto-generated method stub
		
	}
}
