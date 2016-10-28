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

public class Main extends JFrame {

	private JPanel contentPane;

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
		setBounds(100, 100, 714, 629);
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu mnFil = new JMenu("Fil");
		menuBar.add(mnFil);
		
		JMenuItem mntmHentBeholdningsliste = new JMenuItem("Hent beholdningsliste");
		mnFil.add(mntmHentBeholdningsliste);
		
		JMenu mnRedigr = new JMenu("Redigér");
		menuBar.add(mnRedigr);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addComponent(tabbedPane, GroupLayout.DEFAULT_SIZE, 702, Short.MAX_VALUE)
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(Alignment.TRAILING, gl_contentPane.createSequentialGroup()
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
					.addComponent(tabbedPane, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
		);
		
		JPanel pnlInputList = new JPanel();
		tabbedPane.addTab("Bitmagasiner", null, pnlInputList, null);
		GroupLayout gl_pnlInputList = new GroupLayout(pnlInputList);
		gl_pnlInputList.setHorizontalGroup(
			gl_pnlInputList.createParallelGroup(Alignment.LEADING)
				.addGap(0, 697, Short.MAX_VALUE)
		);
		gl_pnlInputList.setVerticalGroup(
			gl_pnlInputList.createParallelGroup(Alignment.LEADING)
				.addGap(0, 552, Short.MAX_VALUE)
		);
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
