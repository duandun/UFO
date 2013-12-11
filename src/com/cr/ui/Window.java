package com.cr.ui;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Point;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.JTree;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import net.sf.json.JSONObject;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

public class Window extends JFrame {

	/**
	 * 
	 */

	private JMenuBar menubar;
	private JMenuItem menuItemLoad, menuItemSave, menuItemSaveas,
			menuItemAbout, menuItemExit, menuItemAddOpti, menuItemAddProb,
			menuItemAddCoupling,menuItemExecute;
	private JMenu menuF, menuH;
	private JToolBar toolbar;
	public static JTabbedPane m_tabPane;
	private JTabbedPane taskPane;
	public static JTree tree;
	public static DefaultMutableTreeNode module;
	public static DefaultMutableTreeNode optimizer;
	public static DefaultMutableTreeNode problem;
	public static DefaultMutableTreeNode coupling;
	public static DefaultMutableTreeNode visualization;

	public static int count = 0;
	private JPanel groupToolBar;
	private JButton button_run;
	private JButton button_load;
	private JButton button_save;
	private JButton button_saveAs;
	private JButton button_showConfig;
	private JButton button_addTag;
	private JButton button_addOpti;
	private JButton button_addProb;
	private JTextField tagname;
	private JSplitPane contentPane;

	public static JSplitPane mainPane;

	private JSplitPane rightPane;

	private JFileChooser fc;

	private int flag = 0;
	private JTable table;

	private String tabtitle;

	private TabContent Tab;
	private ConfTabContent confTab;

	private JScrollPane panel;

	private JPopupMenu popupmenu;
	private JPopupMenu addOptiMenu;
	private JTextField optiName;
	private JPopupMenu addProbMenu;
	private JTextField probName;

	private JTextField pathName;

	/**
	 * Launch the application.
	 */
	

	/**
	 * Create the application.
	 */
	public Window() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */

	// Menu : File, Help .
	private void initMenu() {

		menubar = new JMenuBar();
		menuF = new JMenu("File");
		menuH = new JMenu("Help");
		menuItemLoad = new JMenuItem("Load", KeyEvent.VK_L);
		menuItemSave = new JMenuItem("Save", KeyEvent.VK_S);
		menuItemSaveas = new JMenuItem("Save As");
		menuItemExit = new JMenuItem("Exit");
		menuItemAbout = new JMenuItem("About");
		menuItemAddOpti = new JMenuItem("Add Optimizer");
		menuItemAddProb = new JMenuItem("Add Problem");
		menuItemAddCoupling = new JMenuItem("Create Project");
		menuItemExecute = new JMenuItem("Execute Project");

		
		menuItemLoad.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_L,
				ActionEvent.CTRL_MASK));
		menuItemSave.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,
				ActionEvent.CTRL_MASK));

		menuF.add(menuItemLoad);
		menuF.add(menuItemSave);
		menuF.add(menuItemSaveas);
		menuF.add(menuItemAddOpti);
		menuF.add(menuItemAddProb);
		menuF.add(menuItemAddCoupling);
		menuF.add(menuItemExecute);
		menuF.add(menuItemExit);

		menuH.add(menuItemAbout);

		menubar.add(menuF);
		menubar.add(menuH);

		
		
		menuItemLoad.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				fc = new JFileChooser("Open File");
				fc.showOpenDialog(null);
			}
		});

		menuItemExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});

		menuItemAbout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(null, "About Button", "About",
						JOptionPane.CLOSED_OPTION);
			}
		});

		menuItemAddOpti.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				DialogAdd dlg = new DialogAdd("opti");

			}
		});

		menuItemAddProb.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				DialogAdd dlg = new DialogAdd("prob");
			}
		});

		menuItemAddCoupling.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				ConfTabContent tt = new ConfTabContent("conf", "write");
				m_tabPane.add("conf_opti_prob", tt.tabContent);
				m_tabPane.setTabComponentAt(count, new ButtonTabComponent(
						m_tabPane));
				m_tabPane.setSelectedIndex(count);
				count++;
			}
		});
		
menuItemExecute.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				ProjectTabContent t = new ProjectTabContent();
				m_tabPane.add("Execute Project",t.tabContent);
				m_tabPane.setTabComponentAt(count, new ButtonTabComponent(
						m_tabPane));
				m_tabPane.setSelectedIndex(count);
				count++;
			}
		});

		setJMenuBar(menubar);
	}

	// Add Tag
	private void addTag(String name) {
		try {
			FileWriter fw = new FileWriter("configuration\\tagconfig.txt", true);
			fw.write("\n" + name);
			fw.flush();
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// Button : Run , Load , Save , Save As. Functions are not required.
	private void initButton() {

		button_run = new JButton("Run");
		button_load = new JButton("Load");
		button_save = new JButton("Save");
		button_saveAs = new JButton("Save As");
		button_showConfig = new JButton("Create Configuration");
		button_addTag = new JButton("Add Tag");
		button_addOpti = new JButton("Add Optimizer");
		button_addProb = new JButton("Add Problem");

		addOptiMenu = new JPopupMenu();
		JPanel p = new JPanel(new GridLayout(3, 2));
		JLabel lab = new JLabel("Name: ");
		JLabel path = new JLabel("Path: ");
		JPanel ptemp = new JPanel();

		JButton pChooser = new JButton("...");
		// pChooser.setSize(new Dimension(50,50));
		pChooser.setPreferredSize(new Dimension(20, 20));
		pChooser.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser fc = new JFileChooser("Open File");
				fc.showOpenDialog(null);
				String filepath = fc.getSelectedFile().getPath().toString();
				pathName.setText(filepath);
				addOptiMenu.setVisible(true);
			}
		});
		// pChooser.setBounds(4,10,10,10);
		pathName = new JTextField();
		pathName.setColumns(10);
		ptemp.add(pathName);
		ptemp.add(pChooser);

		optiName = new JTextField();
		// optiName.add(pChooser);
		optiName.setColumns(10);
		JButton opti_commit = new JButton("Add");
		JButton opti_cancel = new JButton("Cancel");
		p.add(lab);
		p.add(optiName);
		p.add(path);
		p.add(ptemp);
		p.add(opti_commit);
		p.add(opti_cancel);
		addOptiMenu.add(p);

		opti_commit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!optiName.getText().toString().equals("")) {
					try {

						File temp = new File("configuration\\" + "Op_"
								+ optiName.getText().toString() + ".json");
						boolean success = temp.createNewFile();
						if (success == true) {
							FileWriter fw = new FileWriter(
									"configuration\\optimizer.txt", true);
							fw.write("\n" + optiName.getText().toString());
							fw.flush();
							fw.close();

							FileWriter fr = new FileWriter("configuration\\Op_"
									+ optiName.getText().toString() + ".json");
							JSONObject jSon = new JSONObject();
							jSon.put("path", pathName.getText().toString());
							jSon.write(fr);
							fr.flush();
							fr.close();

							initTree();

							tabtitle = "Op_" + optiName.getText().toString();
							Tab = new TabContent(tabtitle, "optimizers");
							m_tabPane.add(tabtitle, Tab.scrollright);
							m_tabPane.setTabComponentAt(count,
									new ButtonTabComponent(m_tabPane));
							m_tabPane.setSelectedIndex(count);
							count++;
						}

						else {
							JOptionPane.showMessageDialog(null,
									"Already have the Optimizer", "Warning",
									JOptionPane.CLOSED_OPTION);
						}
					} catch (IOException ex) {
						ex.printStackTrace();
					}

					optiName.setText("");
					addOptiMenu.setVisible(false);
				}
			}
		});

		opti_cancel.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				optiName.setText("");
				addOptiMenu.setVisible(false);
			}
		});

		button_addOpti.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent event) {
				triggerEvent(event);
			}

			public void mouseReleased(MouseEvent event) {
				triggerEvent(event);
			}

			private void triggerEvent(MouseEvent event) {
				if (event.isPopupTrigger()) {
					addOptiMenu.show(event.getComponent(), event.getX(),
							event.getY());
				}
			}
		});

		addProbMenu = new JPopupMenu();
		p = new JPanel(new GridLayout(2, 2));
		lab = new JLabel("Name: ");
		probName = new JTextField();
		JButton prob_commmit = new JButton("Add");
		JButton prob_cancel = new JButton("Cancel");
		p.add(lab);
		p.add(probName);
		p.add(prob_commmit);
		p.add(prob_cancel);
		addProbMenu.add(p);

		prob_commmit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!probName.getText().toString().equals("")) {
					try {
						File temp = new File("configuration\\" + "Pr_"
								+ probName.getText().toString() + ".json");
						boolean success = temp.createNewFile();
						if (success == true) {
							FileWriter fw = new FileWriter(
									"configuration\\problem.txt", true);
							fw.write("\n" + probName.getText().toString());
							fw.flush();
							fw.close();

							initTree();

							tabtitle = "Pr_" + probName.getText().toString();
							Tab = new TabContent(tabtitle, "problems");
							m_tabPane.add(tabtitle, Tab.scrollright);
							m_tabPane.setTabComponentAt(count,
									new ButtonTabComponent(m_tabPane));
							m_tabPane.setSelectedIndex(count);
							count++;
						}

						else {
							JOptionPane.showMessageDialog(null,
									"Already have the Problem", "Warning",
									JOptionPane.CLOSED_OPTION);
						}
					} catch (IOException ex) {
						ex.printStackTrace();
					}
					probName.setText("");
					addProbMenu.setVisible(false);
				}
			}
		});

		prob_cancel.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				probName.setText("");
				addProbMenu.setVisible(false);
			}
		});

		button_addProb.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent event) {
				triggerEvent(event);
			}

			public void mouseReleased(MouseEvent event) {
				triggerEvent(event);
			}

			private void triggerEvent(MouseEvent event) {
				if (event.isPopupTrigger()) {
					addProbMenu.show(event.getComponent(), event.getX(),
							event.getY());
				}
			}
		});

		popupmenu = new JPopupMenu();
		p = new JPanel(new GridLayout(2, 2));
		lab = new JLabel("Tag Name: ");
		tagname = new JTextField();
		JButton tag_commit = new JButton("Add");
		JButton tag_cancel = new JButton("Cancel");
		p.add(lab);
		p.add(tagname);
		p.add(tag_commit);
		p.add(tag_cancel);
		popupmenu.add(p);

		tag_commit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (tagname.getText().toString() != "") {
					addTag(tagname.getText().toString());
					popupmenu.setVisible(false);
					tagname.setText("");
				}
			}
		});
		tag_cancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				popupmenu.setVisible(false);
				tagname.setText("");
			}
		});

		button_addTag.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent event) {
				triggerEvent(event);
			}

			public void mouseReleased(MouseEvent event) {
				triggerEvent(event);
			}

			private void triggerEvent(MouseEvent event) {
				if (event.isPopupTrigger()) {
					popupmenu.show(event.getComponent(), event.getX(),
							event.getY());
				}
			}
		});

		button_load.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				fc = new JFileChooser("Open File");
				fc.showOpenDialog(null);
			}
		});

		groupToolBar = new JPanel();
//		groupToolBar.add(button_run);
		groupToolBar.add(button_load);
		// groupToolBar.add(button_save);
		// groupToolBar.add(button_saveAs);
		// groupToolBar.add(button_showConfig);
		// groupToolBar.add(button_addTag);
		// groupToolBar.add(button_addOpti);
		// groupToolBar.add(button_addProb);

		FlowLayout flowLayout = (FlowLayout) groupToolBar.getLayout();
		flowLayout.setAlignment(FlowLayout.LEFT);
//		contentPane.setResizeWeight(0.1);
		contentPane.setEnabled(false);
		contentPane.setLeftComponent(groupToolBar);
	}

	// Tree in LeftPanel
	private void initTree() {

		module = new DefaultMutableTreeNode("Module", true);

		optimizer = new DefaultMutableTreeNode("Optimizer", true);
		problem = new DefaultMutableTreeNode("Problem", true);
		coupling = new DefaultMutableTreeNode("Project", true);
		/*
		 * visualization = new DefaultMutableTreeNode( "Visualization", true);
		 */
		module.add(optimizer);
		module.add(problem);
		DefaultMutableTreeNode node = new DefaultMutableTreeNode(
				"conf_opti_prob", true);
		coupling.add(node);
		module.add(coupling);
		// module.add(visualization);

		try {
			String line = "";
			FileReader fr = new FileReader(Main.OPTIMIZER);
			BufferedReader br = new BufferedReader(fr);
			while (br.ready()) {
				line = br.readLine();
				DefaultMutableTreeNode treeNode = new DefaultMutableTreeNode(
						line, true);
				optimizer.add(treeNode);
			}

			br.close();
			fr.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			String line = "";
			FileReader fr = new FileReader(Main.PROBLEM);
			BufferedReader br = new BufferedReader(fr);
			while (br.ready()) {
				line = br.readLine();
				DefaultMutableTreeNode treeNode = new DefaultMutableTreeNode(
						line, true);
				problem.add(treeNode);
			}

			br.close();
			fr.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			String line = "";
			FileReader fr = new FileReader(
					Main.PROJECT);
			BufferedReader br = new BufferedReader(fr);
			while (br.ready()) {
				line = br.readLine();
				DefaultMutableTreeNode treeNode = new DefaultMutableTreeNode(
						line, true);
				coupling.add(treeNode);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		// MouseListener
		MouseListener ml = new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				int selRow = tree.getRowForLocation(e.getX(), e.getY());
				TreePath selPath = tree.getPathForLocation(e.getX(), e.getY());

				if (selRow != -1 && selPath.getPathCount() > 2) {

					String type = new String();
					// double click on the tree
					if (e.getClickCount() == 2) {

						/*
						 * if (ButtonTabComponent.k > 0) { count = count -
						 * ButtonTabComponent.k; ButtonTabComponent.k = 0; }
						 */
						flag = 0;
						String name = new String();
						if (selPath.getParentPath().getLastPathComponent()
								.toString() == "Optimizer") {
							name = selPath.getLastPathComponent().toString();
							type = "optimizers";

						} else if (selPath.getParentPath()
								.getLastPathComponent().toString() == "Problem") {
							name = selPath.getLastPathComponent().toString();
							type = "problems";
						} else {
							name = selPath.getLastPathComponent().toString();
						}

						tabtitle = name;
						// If the tab is exist, then jump to it
						for (int i = 0; i < m_tabPane.getTabCount(); i++) {

							if (m_tabPane.getTitleAt(i).equals(tabtitle)) {
								m_tabPane.setSelectedIndex(i);
								flag = 1;
								break;
							}

						}
						// If it is a new tab, then create it
						if (flag == 0) {
							if (selPath.getParentPath().getLastPathComponent()
									.toString().equals("Project")) {
								confTab = new ConfTabContent(tabtitle, "read");
								m_tabPane.add(tabtitle, confTab.tabContent);
								m_tabPane.setTabComponentAt(count,
										new ButtonTabComponent(m_tabPane));
								m_tabPane.setSelectedIndex(count);
								count++;
							} else {
								Tab = new TabContent(tabtitle, type);
								m_tabPane.add(tabtitle, Tab.scrollright);
								m_tabPane.setTabComponentAt(count,
										new ButtonTabComponent(m_tabPane));
								m_tabPane.setSelectedIndex(count);
								count++;
							}
						}

					}
				}
			}
		};

		tree = new JTree(module);

		tree.addMouseListener(ml);

		mainPane.setLeftComponent(tree);

	}

	// RightPanel
	private void initRightPane() {

		rightPane = new JSplitPane();
		m_tabPane = new JTabbedPane();

		rightPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
		rightPane.setOneTouchExpandable(true);
		rightPane.setResizeWeight(0.9);
		mainPane.setRightComponent(rightPane);

		rightPane.setLeftComponent(m_tabPane);

		String[] columnNames = { "Task", "State", "Progress" };

		Object[][] data = {};
		taskPane = new JTabbedPane();
		table = new JTable(data, columnNames);
		table.setFillsViewportHeight(true);
		taskPane.add("Tasks", new JScrollPane(table));
		rightPane.setRightComponent(taskPane);
	}

	private void initialize() {

		// Frame
		setTitle("Uf4ot");
		setBounds(100, 100, 900, 560);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// ContentPane
		contentPane = new JSplitPane();
		contentPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
		setContentPane(contentPane);

		// MainPanel
		mainPane = new JSplitPane();
		mainPane.setResizeWeight(0.15);
		contentPane.setRightComponent(mainPane);

		initMenu();

		initButton();

		initTree();

		initRightPane();

		setFocusable(true);
	}

}
