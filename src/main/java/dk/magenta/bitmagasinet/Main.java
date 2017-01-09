package dk.magenta.bitmagasinet;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowStateListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;

import org.bitrepository.common.utils.Base16Utils;

import dk.magenta.bitmagasinet.checksum.ChecksumIOHandler;
import dk.magenta.bitmagasinet.checksum.ClockBasedDateStrategy;
import dk.magenta.bitmagasinet.checksum.FileChecksum;
import dk.magenta.bitmagasinet.checksum.InvalidChecksumFileException;
import dk.magenta.bitmagasinet.comparators.ChecksumComparator;
import dk.magenta.bitmagasinet.comparators.ChecksumMatchComparator;
import dk.magenta.bitmagasinet.comparators.ChecksumType;
import dk.magenta.bitmagasinet.comparators.FilenameComparator;
import dk.magenta.bitmagasinet.configuration.ConfigurationHandler;
import dk.magenta.bitmagasinet.configuration.ConfigurationHandlerImpl;
import dk.magenta.bitmagasinet.configuration.ConfigurationIOHandler;
import dk.magenta.bitmagasinet.configuration.ConfigurationIOHandlerImpl;
import dk.magenta.bitmagasinet.configuration.InvalidArgumentException;
import dk.magenta.bitmagasinet.configuration.RepositoryConfiguration;
import dk.magenta.bitmagasinet.configuration.RepositoryConfigurationImpl;
import dk.magenta.bitmagasinet.remote.BitrepositoryConnectionResult;
import dk.magenta.bitmagasinet.remote.BitrepositoryConnector;
import dk.magenta.bitmagasinet.remote.BitrepositoryConnectorImpl;
import dk.magenta.bitmagasinet.remote.BitrepositoryConnectorRandomResultStub;
import dk.magenta.bitmagasinet.remote.ThreadStatus;
import dk.magenta.bitmagasinet.remote.ThreadStatusObserver;

public class Main extends JFrame implements ThreadStatusObserver, ProcessHandlerObserver {

	private JPanel contentPane;
	private JButton btnGetConfiguration;
	private JLabel lblCurrentConfiguration;
	private JPanel currentConfigurationPane;
	private JTable checksumTable;
	private JList<String> bitRepoList;

	private JTextField txtPathToSettingsFolder;
	private JTextField txtPathToCertificate;
	private JTextField txtCollectionId;
	private JTextField txtPillarId;
	private JTextField txtPathToLocalChecksumList;
	
	private ConfigurationHandler configurationHandler;
	private ConfigurationIOHandler configurationIOHandler;
	private ChecksumIOHandler checksumIOHandler;
	private BitrepositoryConnector bitrepositoryConnector;
	private ProcessHandlerImpl processHandler;
	
	private String repoName;
	private List<FileChecksum> fileChecksums;
	private Map<String, Comparator> comparatorMap;
	private DefaultListModel<String> bitRepoListModel;
	private DefaultTableModel checksumTableModel;
	private JProgressBar progressBar;
	private JLabel lblProgressBar;
	private JButton btnGetChecksums;
	private JButton btnSort;
	private JComboBox<String> sortDropDown;
	
	private DocumentListener documentListener;
	private JTextField txtPathToResultFile;
	private JLabel lblPathToResultFile;
	private JButton btnPathToResultFile;
	
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
		
		configurationHandler = new ConfigurationHandlerImpl();
		configurationIOHandler = new ConfigurationIOHandlerImpl(configurationHandler);
		checksumIOHandler = new ChecksumIOHandler(new ClockBasedDateStrategy());
		
		documentListener = getDocumentListener(); 
		
		bitRepoListModel = new DefaultListModel<String>();
		fileChecksums = new ArrayList<FileChecksum>();
		
		comparatorMap = new TreeMap<String, Comparator>();
		comparatorMap.put(Constants.FILENAME, new FilenameComparator());
		comparatorMap.put(Constants.MATCH, new ChecksumMatchComparator());
		comparatorMap.put(Constants.LOCAL_CHECKSUM, new ChecksumComparator(ChecksumType.LOCAL));
		comparatorMap.put(Constants.REMOTE_CHECKSUM, new ChecksumComparator(ChecksumType.REMOTE));

		initChecksumTableModel();
		
		initComponents();
		createEvents();
		
	}
	
	private void updateBitRepoListModel() {
		bitRepoListModel.clear();
		for (String name : configurationHandler.getRepositoryConfigurations().keySet()) {
			bitRepoListModel.addElement(name);
		}
	}

	private void clearRepositoryConfigurationFields() {
		txtPathToSettingsFolder.setText(null);
		txtPathToCertificate.setText(null);
		txtCollectionId.setText(null);
		txtPillarId.setText(null);
		txtPathToLocalChecksumList.setText(null);
	}
	
	private void initComponents() {

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				if (bitrepositoryConnector != null) {
					bitrepositoryConnector.closeMessageBus();
				}
				super.windowClosing(e);
				
			}
		});
		setBounds(100, 100, 1012, 642);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);

		// TODO: put into private method
		// Load repository configurations
		try {
			configurationHandler.getRepositoryConfigurationsFromFolder();
			if (!configurationHandler.getRepositoryConfigurations().isEmpty()) {
				updateBitRepoListModel();
			}
		} catch (IOException e) {
			JOptionPane.showMessageDialog(contentPane, e.getMessage());
			e.printStackTrace();
		} catch (InvalidArgumentException e) {
			JOptionPane.showMessageDialog(contentPane, e.getMessage());
			e.printStackTrace();
		}
		
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
		
		JPanel pnlBitrepositories = new JPanel();
		pnlBitrepositories.setName("");
		tabbedPane.addTab("Bitmagasiner", null, pnlBitrepositories, null);
		
		JLabel lblChooseRepo = new JLabel("Vælg Bitmagasin");
		lblChooseRepo.setFont(new Font("Dialog", Font.BOLD, 12));
		
		JScrollPane bitRepoScrollPane = new JScrollPane();
		
		btnGetConfiguration = new JButton("Hent konfiguration");
		if (bitRepoListModel.isEmpty()) {
			btnGetConfiguration.setEnabled(false);
		}
		btnGetConfiguration.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				repoName = (String) bitRepoList.getSelectedValue();
				lblCurrentConfiguration.setText("Konfiguration for " + repoName);
				setConfigurationPaneVisibility(true);
				
				RepositoryConfiguration repositoryConfiguration;
				try {
					repositoryConfiguration = configurationHandler.getRepositoryConfiguration(repoName);
					txtPathToSettingsFolder.setText(repositoryConfiguration.getPathToSettingsFiles().toString());
					txtPathToCertificate.setText(repositoryConfiguration.getPathToCertificate().toString());
					txtCollectionId.setText(repositoryConfiguration.getCollectionId());
					txtPillarId.setText(repositoryConfiguration.getPillarId());
					txtPathToLocalChecksumList.setText(repositoryConfiguration.getPathToChecksumList().toString());
					btnGetChecksums.setEnabled(true);
				} catch (InvalidArgumentException e) {
					// This should never happen
					e.printStackTrace();
				}
				
				
			}
		});
		
		currentConfigurationPane = new JPanel();
		currentConfigurationPane.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		currentConfigurationPane.setVisible(false);
		
		lblCurrentConfiguration = new JLabel("Konfiguration");
		lblCurrentConfiguration.setFont(new Font("Dialog", Font.BOLD, 12));
		lblCurrentConfiguration.setVisible(false);
		
		JButton btnAddNewRepo = new JButton("Tilføj ny");
		btnAddNewRepo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				repoName = JOptionPane.showInputDialog("Indtast navn på konfiguration").trim();
				if (configurationHandler.getRepositoryConfigurations().containsKey(repoName)) {
					JOptionPane.showMessageDialog(contentPane, "Der findes allerede en konfiguration med det navn");
					return;
				}
				lblCurrentConfiguration.setText("Konfiguration for " + repoName);
				clearRepositoryConfigurationFields();
				lblCurrentConfiguration.setVisible(true);
				currentConfigurationPane.setVisible(true);
			}
		});
		
		GroupLayout gl_pnlBitrepositories = new GroupLayout(pnlBitrepositories);
		gl_pnlBitrepositories.setHorizontalGroup(
			gl_pnlBitrepositories.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_pnlBitrepositories.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_pnlBitrepositories.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_pnlBitrepositories.createSequentialGroup()
							.addComponent(btnAddNewRepo)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(btnGetConfiguration))
						.addComponent(bitRepoScrollPane, GroupLayout.PREFERRED_SIZE, 263, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblChooseRepo))
					.addGap(18)
					.addGroup(gl_pnlBitrepositories.createParallelGroup(Alignment.LEADING)
						.addComponent(currentConfigurationPane, GroupLayout.DEFAULT_SIZE, 674, Short.MAX_VALUE)
						.addComponent(lblCurrentConfiguration))
					.addContainerGap())
		);
		gl_pnlBitrepositories.setVerticalGroup(
			gl_pnlBitrepositories.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_pnlBitrepositories.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_pnlBitrepositories.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblChooseRepo)
						.addComponent(lblCurrentConfiguration))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_pnlBitrepositories.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_pnlBitrepositories.createSequentialGroup()
							.addComponent(bitRepoScrollPane, GroupLayout.PREFERRED_SIZE, 106, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addGroup(gl_pnlBitrepositories.createParallelGroup(Alignment.BASELINE)
								.addComponent(btnAddNewRepo)
								.addComponent(btnGetConfiguration)))
						.addComponent(currentConfigurationPane, GroupLayout.DEFAULT_SIZE, 495, Short.MAX_VALUE))
					.addContainerGap())
		);
		
		JLabel lblStiTilRepositorysettingxml = new JLabel("Sti til mappe indeholdende RepositorySetting.xml og ReferenceSettings.xml");
		
		txtPathToSettingsFolder = new JTextField();
		txtPathToSettingsFolder.setColumns(10);
		txtPathToSettingsFolder.getDocument().addDocumentListener(documentListener);
		
		JLabel lblpathToCertificate = new JLabel("Sti til certifikat");
		
		txtPathToCertificate = new JTextField();
		txtPathToCertificate.setColumns(10);
		txtPathToCertificate.getDocument().addDocumentListener(documentListener);
		
		JLabel lblCollectionId = new JLabel("Samling (CollectionID)");
		
		txtCollectionId = new JTextField();
		txtCollectionId.setColumns(10);
		txtCollectionId.getDocument().addDocumentListener(documentListener);
		
		JLabel lblPillarId = new JLabel("Søjle (PillarID)");
		
		txtPillarId = new JTextField();
		txtPillarId.setColumns(10);
		txtPillarId.getDocument().addDocumentListener(documentListener);
		
		JLabel lblPathToLocalChecksumList = new JLabel("Sti til lokal kontrolsumsliste");
		
		txtPathToLocalChecksumList = new JTextField();
		txtPathToLocalChecksumList.setColumns(10);
		txtPathToLocalChecksumList.getDocument().addDocumentListener(documentListener);
		
		JButton btnSaveRepoConf = new JButton("Gem");
		btnSaveRepoConf.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					RepositoryConfiguration repositoryConfiguration = new RepositoryConfigurationImpl(repoName);
					repositoryConfiguration.setPathToSettingsFiles(Paths.get(txtPathToSettingsFolder.getText()));
					repositoryConfiguration.setPathToCertificate(Paths.get(txtPathToCertificate.getText()));
					repositoryConfiguration.setCollectionId(txtCollectionId.getText());
					repositoryConfiguration.setPillarId(txtPillarId.getText());
					repositoryConfiguration.setPathToChecksumList(Paths.get(txtPathToLocalChecksumList.getText()));
			
					configurationHandler.addRepositoryConfiguration(repositoryConfiguration);
					updateBitRepoListModel();
					bitRepoList.setSelectedValue(repoName, true);
					
					configurationIOHandler.writeRepositoryConfiguration(repositoryConfiguration);
					btnGetConfiguration.setEnabled(true);
					
					btnGetChecksums.setEnabled(true);

					JOptionPane.showMessageDialog(contentPane, "Konfiguration gemt");
				} catch (InvalidArgumentException | IOException e) {
					JOptionPane.showMessageDialog(
							contentPane, e.getMessage());
				}
			}
		});
		
		JButton btnRydFelter = new JButton("Ryd felter");
		btnRydFelter.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				clearRepositoryConfigurationFields();
			}
		});
		GroupLayout gl_currentConfigurationPane = new GroupLayout(currentConfigurationPane);
		gl_currentConfigurationPane.setHorizontalGroup(
			gl_currentConfigurationPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_currentConfigurationPane.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_currentConfigurationPane.createParallelGroup(Alignment.LEADING)
						.addComponent(txtPathToSettingsFolder, GroupLayout.DEFAULT_SIZE, 646, Short.MAX_VALUE)
						.addComponent(lblStiTilRepositorysettingxml)
						.addComponent(lblpathToCertificate)
						.addComponent(txtPathToCertificate, GroupLayout.DEFAULT_SIZE, 646, Short.MAX_VALUE)
						.addComponent(lblCollectionId)
						.addComponent(txtCollectionId, GroupLayout.DEFAULT_SIZE, 646, Short.MAX_VALUE)
						.addComponent(lblPillarId)
						.addComponent(txtPillarId, GroupLayout.DEFAULT_SIZE, 646, Short.MAX_VALUE)
						.addComponent(lblPathToLocalChecksumList)
						.addComponent(txtPathToLocalChecksumList, GroupLayout.DEFAULT_SIZE, 646, Short.MAX_VALUE)
						.addGroup(Alignment.TRAILING, gl_currentConfigurationPane.createSequentialGroup()
							.addComponent(btnRydFelter)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(btnSaveRepoConf)))
					.addContainerGap())
		);
		gl_currentConfigurationPane.setVerticalGroup(
			gl_currentConfigurationPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_currentConfigurationPane.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblStiTilRepositorysettingxml)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(txtPathToSettingsFolder, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addGap(18)
					.addComponent(lblpathToCertificate)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(txtPathToCertificate, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addGap(18)
					.addComponent(lblCollectionId)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(txtCollectionId, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addGap(18)
					.addComponent(lblPillarId)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(txtPillarId, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addGap(18)
					.addComponent(lblPathToLocalChecksumList)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(txtPathToLocalChecksumList, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addGap(18)
					.addGroup(gl_currentConfigurationPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(btnSaveRepoConf)
						.addComponent(btnRydFelter))
					.addContainerGap(138, Short.MAX_VALUE))
		);
		currentConfigurationPane.setLayout(gl_currentConfigurationPane);
		
		bitRepoList = new JList<String>();
		bitRepoList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		bitRepoList.setModel(bitRepoListModel);
		bitRepoList.setSelectedIndex(0);
		bitRepoScrollPane.setViewportView(bitRepoList);
		pnlBitrepositories.setLayout(gl_pnlBitrepositories);
		
		JPanel pnlChecksums = new JPanel();
		tabbedPane.addTab("Kontrolsummer", null, pnlChecksums, null);
		
		btnGetChecksums = new JButton("Hent kontrolsummer");
		// btnGetChecksums.setEnabled(false);
		btnGetChecksums.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				// Disable/hide buttons
				progressBar.setValue(0);
				sortDropDown.setEnabled(false);
				btnSort.setEnabled(false);
				setResultFileInputVisibility(false);
				
				// Clear table
				checksumTableModel.setRowCount(0);
				
				// Get checksum list from local file
				File checksumFile = new File(txtPathToLocalChecksumList.getText().trim());
				try {
					fileChecksums = checksumIOHandler.readChecksumList(checksumFile);
				} catch (IOException e) {
					JOptionPane.showMessageDialog(contentPane, "Der opstod en fejl under læsning af filen " + txtPathToLocalChecksumList.getText());
					e.printStackTrace();
				} catch (InvalidChecksumFileException e) {
					JOptionPane.showMessageDialog(contentPane, e.getMessage());
					e.printStackTrace();
				}
				
				// bitrepositoryConnector = new BitrepositoryConnectorRandomResultStub(fileChecksums.get(0), ThreadStatus.SUCCESS);
				try {
					bitrepositoryConnector = new BitrepositoryConnectorImpl(configurationHandler.getRepositoryConfiguration(repoName), 
							fileChecksums.get(0));
				} catch (InvalidArgumentException e) {
					e.printStackTrace();
				}
				processHandler = new ProcessHandlerImpl(fileChecksums, bitrepositoryConnector, true);
				bitrepositoryConnector.addObserver(processHandler);
				bitrepositoryConnector.addObserver(Main.this);
				
				processHandler.addObserver(Main.this);
				processHandler.processNext();
				
				lblProgressBar.setVisible(true);
				progressBar.setVisible(true);
				
			}
		});
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBackground(Color.WHITE);
		
		sortDropDown = new JComboBox();
		// sortDropDown.setEnabled(false);
		sortDropDown.setModel(new DefaultComboBoxModel(new String[] {Constants.FILENAME, Constants.MATCH, 
				Constants.LOCAL_CHECKSUM, Constants.REMOTE_CHECKSUM}));
		
		JLabel lblSortAfter = new JLabel("Sortér efter:");
		
		btnSort = new JButton("Sortér");
		// btnSort.setEnabled(false);
		btnSort.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String sortAfter = (String) sortDropDown.getSelectedItem();
				Collections.sort(processHandler.getProcessedFileChecksums(),comparatorMap.get(sortAfter));
				updateChecksumResultList();
			}
		});
		
		disableGetChecksums();
		
		progressBar = new JProgressBar();
		progressBar.setVisible(false);
		progressBar.setStringPainted(true);
		progressBar.setToolTipText("");
		
		lblProgressBar = new JLabel("Status for nedhentning");
		lblProgressBar.setVisible(false);
		lblProgressBar.setFont(new Font("Tahoma", Font.BOLD, 13));
		
		lblPathToResultFile = new JLabel("Angiv sti til mappe hvor resultatfilen skal gemmes:");
		lblPathToResultFile.setVisible(false);
		
		txtPathToResultFile = new JTextField();
		txtPathToResultFile.setVisible(false);
		txtPathToResultFile.setColumns(10);
		
		btnPathToResultFile = new JButton("Gem");
		btnPathToResultFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Path path = Paths.get(txtPathToResultFile.getText().trim());
				File file = path.toFile();
				if (!file.isDirectory()) {
					JOptionPane.showMessageDialog(contentPane, "Stien henviser ikke til en mappe");
					return;
				}
				if (!file.canWrite()) {
					JOptionPane.showMessageDialog(contentPane, "Kan ikke skrive til den angivne mappe!");
					return;
				}
				try {
					checksumIOHandler.writeResultFiles(path, processHandler.getProcessedFileChecksums(), 
							configurationHandler.getRepositoryConfiguration(repoName));
					JOptionPane.showMessageDialog(contentPane, "Filen er blevet gemt");
				} catch (IOException e) {
					JOptionPane.showConfirmDialog(contentPane, "Der opstod en fejl: " + e.getMessage());
					e.printStackTrace();
				} catch (InvalidArgumentException e) {
					JOptionPane.showConfirmDialog(contentPane, "Der opstod en fejl: " + e.getMessage());
					e.printStackTrace();
				}
			}
		});
		btnPathToResultFile.setVisible(false);
		
		GroupLayout gl_pnlChecksums = new GroupLayout(pnlChecksums);
		gl_pnlChecksums.setHorizontalGroup(
			gl_pnlChecksums.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_pnlChecksums.createSequentialGroup()
					.addGroup(gl_pnlChecksums.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_pnlChecksums.createSequentialGroup()
							.addContainerGap()
							.addGroup(gl_pnlChecksums.createParallelGroup(Alignment.LEADING)
								.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 955, Short.MAX_VALUE)
								.addGroup(gl_pnlChecksums.createSequentialGroup()
									.addComponent(lblSortAfter)
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(sortDropDown, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(btnSort)
									.addPreferredGap(ComponentPlacement.RELATED, 501, Short.MAX_VALUE)
									.addComponent(btnGetChecksums))
								.addGroup(gl_pnlChecksums.createSequentialGroup()
									.addComponent(lblPathToResultFile)
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(txtPathToResultFile, GroupLayout.PREFERRED_SIZE, 299, GroupLayout.PREFERRED_SIZE)
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(btnPathToResultFile))))
						.addGroup(gl_pnlChecksums.createSequentialGroup()
							.addGap(381)
							.addGroup(gl_pnlChecksums.createParallelGroup(Alignment.LEADING)
								.addComponent(lblProgressBar)
								.addComponent(progressBar, GroupLayout.PREFERRED_SIZE, 201, GroupLayout.PREFERRED_SIZE))))
					.addContainerGap())
		);
		gl_pnlChecksums.setVerticalGroup(
			gl_pnlChecksums.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_pnlChecksums.createSequentialGroup()
					.addGap(27)
					.addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 197, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_pnlChecksums.createParallelGroup(Alignment.BASELINE)
						.addComponent(btnGetChecksums)
						.addComponent(lblSortAfter)
						.addComponent(sortDropDown, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(btnSort))
					.addGap(59)
					.addComponent(lblProgressBar)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(progressBar, GroupLayout.PREFERRED_SIZE, 35, GroupLayout.PREFERRED_SIZE)
					.addGap(65)
					.addGroup(gl_pnlChecksums.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblPathToResultFile)
						.addComponent(txtPathToResultFile, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(btnPathToResultFile))
					.addContainerGap(90, Short.MAX_VALUE))
		);
		
		checksumTable = new JTable();
		checksumTable.setShowGrid(false);
		checksumTable.setBackground(Color.WHITE);
		checksumTable.setFillsViewportHeight(true);
		checksumTable.setModel(checksumTableModel);
		checksumTable.getColumnModel().getColumn(1).setMaxWidth(100);
		checksumTable.getColumnModel().getColumn(3).setMaxWidth(100);
		scrollPane.setViewportView(checksumTable);
		pnlChecksums.setLayout(gl_pnlChecksums);
		contentPane.setLayout(gl_contentPane);
		
	}

	private void setConfigurationPaneVisibility(boolean b) {
		lblCurrentConfiguration.setVisible(b);
		currentConfigurationPane.setVisible(b);
	}
	
	private void initChecksumTableModel() {
		checksumTableModel = new DefaultTableModel(new Object[][] {}, 
				new String[] {Constants.FILENAME, Constants.MATCH, Constants.LOCAL_CHECKSUM, "Salt", Constants.REMOTE_CHECKSUM});
	}
	
	private void createEvents() {
		// TODO Auto-generated method stub
		
	}
	
	private String convertBooleanToString(boolean b) {
		if (b) {
			return "Ja";
		} else {
			return "Nej";
		}
	}
	
	
	/**
	 * Callback from ProcessHandlerObserver
	 */
	@Override
	public void update(ProcessHandler processHandler) {
		sortDropDown.setEnabled(true);
		btnSort.setEnabled(true);
		
		setResultFileInputVisibility(true);
	}
	
	/**
	 * Callback from ThreadStatusObserver
	 */
	@Override
	public void update(BitrepositoryConnectionResult bitrepositoryConnectionResult) {
		updateChecksumResultList();
			
		FileChecksum fileChecksum = bitrepositoryConnectionResult.getFileChecksum();
		progressBar.setValue(processHandler.getProgressHandler().getProgress());
	}
	
	private void updateChecksumResultList() {
		checksumTableModel.setRowCount(0);
		
		for (int i = 0; i < processHandler.getProcessedFileChecksums().size(); i++) {
			FileChecksum fileChecksum = processHandler.getProcessedFileChecksums().get(i);
			String[] row = new String[] {fileChecksum.getFilename(), 
					convertBooleanToString(fileChecksum.checksumsMatch()),
					fileChecksum.getLocalChecksum(),
					Base16Utils.decodeBase16(fileChecksum.getSalt()),
					fileChecksum.getRemoteChecksum()};
			checksumTableModel.addRow(row);
		}
		
	}
	
	private DocumentListener getDocumentListener() {
		return new DocumentListener() {
			
			@Override
			public void removeUpdate(DocumentEvent e) {
				btnGetChecksums.setEnabled(false);
			}
			
			@Override
			public void insertUpdate(DocumentEvent e) {
				btnGetChecksums.setEnabled(false);
			}
			
			@Override
			public void changedUpdate(DocumentEvent e) {
				btnGetChecksums.setEnabled(false);
			}
		};
	}
	
	private void disableGetChecksums() {
		btnGetChecksums.setEnabled(false);
		sortDropDown.setEnabled(false);
		btnSort.setEnabled(false);
	}
	
	private void setResultFileInputVisibility(boolean b) {
		lblPathToResultFile.setVisible(b);
		txtPathToResultFile.setVisible(b);
		btnPathToResultFile.setVisible(b);
	}
	
	@Override
	public void messageBusErrorCallback() {
		JOptionPane.showMessageDialog(contentPane, "Der opstod et problem under lukning af forbindelsen til bitmagasinet");
	}
}
