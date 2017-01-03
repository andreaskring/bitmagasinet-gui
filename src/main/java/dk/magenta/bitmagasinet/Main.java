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
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.border.LineBorder;
import java.awt.Color;

public class Main extends JFrame {

	private JPanel contentPane;
	private JButton btnGetConfiguration;
	private JLabel lblCurrentConfiguration;
	private JPanel currentConfigurationPane;
	private JList bitRepoList;

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
		
		JScrollPane bitRepoScrollPane = new JScrollPane();
		
		btnGetConfiguration = new JButton("Hent konfiguration");
		btnGetConfiguration.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String repositoryName = (String) bitRepoList.getSelectedValue();
				lblCurrentConfiguration.setText(repositoryName);
				lblCurrentConfiguration.setVisible(true);
				currentConfigurationPane.setVisible(true);

			}
		});
		
		currentConfigurationPane = new JPanel();
		currentConfigurationPane.setBorder(new LineBorder(new Color(0, 0, 0)));
		currentConfigurationPane.setVisible(false);
		
		lblCurrentConfiguration = new JLabel("Konfiguration for");
		lblCurrentConfiguration.setFont(new Font("Dialog", Font.BOLD, 12));
		lblCurrentConfiguration.setVisible(false);
		GroupLayout gl_pnlInputList = new GroupLayout(pnlInputList);
		gl_pnlInputList.setHorizontalGroup(
			gl_pnlInputList.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_pnlInputList.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_pnlInputList.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_pnlInputList.createParallelGroup(Alignment.TRAILING)
							.addComponent(btnGetConfiguration)
							.addComponent(bitRepoScrollPane, GroupLayout.PREFERRED_SIZE, 263, GroupLayout.PREFERRED_SIZE))
						.addComponent(lblVlgBitmagasin))
					.addGap(72)
					.addGroup(gl_pnlInputList.createParallelGroup(Alignment.LEADING)
						.addComponent(lblCurrentConfiguration)
						.addComponent(currentConfigurationPane, GroupLayout.PREFERRED_SIZE, 601, GroupLayout.PREFERRED_SIZE))
					.addGap(49))
		);
		gl_pnlInputList.setVerticalGroup(
			gl_pnlInputList.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_pnlInputList.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_pnlInputList.createParallelGroup(Alignment.TRAILING)
						.addComponent(lblCurrentConfiguration)
						.addComponent(lblVlgBitmagasin))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_pnlInputList.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_pnlInputList.createSequentialGroup()
							.addComponent(bitRepoScrollPane, GroupLayout.PREFERRED_SIZE, 106, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(btnGetConfiguration))
						.addComponent(currentConfigurationPane, GroupLayout.PREFERRED_SIZE, 298, GroupLayout.PREFERRED_SIZE))
					.addContainerGap(204, Short.MAX_VALUE))
		);
		GroupLayout gl_currentConfigurationPane = new GroupLayout(currentConfigurationPane);
		gl_currentConfigurationPane.setHorizontalGroup(
			gl_currentConfigurationPane.createParallelGroup(Alignment.LEADING)
				.addGap(0, 599, Short.MAX_VALUE)
		);
		gl_currentConfigurationPane.setVerticalGroup(
			gl_currentConfigurationPane.createParallelGroup(Alignment.LEADING)
				.addGap(0, 296, Short.MAX_VALUE)
		);
		currentConfigurationPane.setLayout(gl_currentConfigurationPane);
		
		bitRepoList = new JList();
		bitRepoList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		bitRepoList.setModel(new AbstractListModel() {
			String[] values = new String[] {"Statsbiblioteket", "Det Kongelige Bibliotek", "Bitmagasin no 1", "Prima Bitmagasin", "Bitmagasin no 7", "Superlageret", "Aarhus Universitet"};
			public int getSize() {
				return values.length;
			}
			public Object getElementAt(int index) {
				return values[index];
			}
		});
		bitRepoList.setSelectedIndex(0);
		bitRepoScrollPane.setViewportView(bitRepoList);
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
