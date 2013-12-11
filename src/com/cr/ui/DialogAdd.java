package com.cr.ui;

import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.border.MatteBorder;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import net.sf.json.JSONObject;

public class DialogAdd extends JFrame {

	private JButton buttonAdd = new JButton("Add");
	private JButton buttonCan = new JButton("Cancel");
	private TabContent Tab;
	private static int count = 0;
	private JTree tree;
	private int flag = 0;

	LinkedHashMap<JLabel, JTextField> con;
	String filename;

	String getFileName() {
		return filename;
	}

	private void initTree(String name, String type) {

		if (type.equals("opti")) {
			Window.optimizer.insert(new DefaultMutableTreeNode(name.toString(),
					true), count);
			count++;
			Window.tree.updateUI();

			String tabtitle = name.toString();
			Tab = new TabContent(name, "optimizers");
			Window.m_tabPane.add(tabtitle, Tab.scrollright);
			Window.m_tabPane.setTabComponentAt(Window.count,
					new ButtonTabComponent(Window.m_tabPane));
			Window.m_tabPane.setSelectedIndex(Window.count);
			Window.count++;
		}

		else if (type.equals("prob")) {
			Window.problem.insert(new DefaultMutableTreeNode(name.toString(),
					true), count);
			count++;
			Window.tree.updateUI();

			String tabtitle = name.toString();
			Tab = new TabContent(name, "problems");
			Window.m_tabPane.add(tabtitle, Tab.scrollright);
			Window.m_tabPane.setTabComponentAt(Window.count,
					new ButtonTabComponent(Window.m_tabPane));
			Window.m_tabPane.setSelectedIndex(Window.count);
			Window.count++;
		}

	}

	DialogAdd(String type) {
		con = new LinkedHashMap<JLabel, JTextField>();

		// optimizer

		if (type == "opti") {
			JPanel p1 = new JPanel(new GridLayout(9, 2));

			try {
				String line = "";
				FileReader fr = new FileReader(
						Main.DEFAULT_OPTIMIZER);
				BufferedReader br = new BufferedReader(fr);
				while (br.ready()) {
					line = br.readLine();
					con.put(new JLabel(line), new JTextField());
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			setLocation(100, 150);
			setTitle("Add Optimization");
			Container c = getContentPane();
			setSize(350, 350);

			p1.setBorder(new MatteBorder(10, 10, 10, 10, (Icon) null));

			Iterator iter = con.entrySet().iterator();
			while (iter.hasNext()) {
				Map.Entry<JLabel, JTextField> entry = (Map.Entry<JLabel, JTextField>) iter
						.next();
				final JLabel key = entry.getKey();
				final JTextField val = entry.getValue();

				if (key.getText().equals("Path")) {
					JPanel temp = new JPanel();
					val.setColumns(12);
					temp.add(val);
					JButton choose = new JButton("browse");
					choose.setSize(5, 5);
					choose.addActionListener(new ActionListener() {

						@Override
						public void actionPerformed(ActionEvent e) {
							// TODO Auto-generated method stub
							JFileChooser fc = new JFileChooser("Open File");
							fc.showOpenDialog(null);
							String filepath = fc.getSelectedFile().getPath()
									.toString();
							val.setText(filepath);
						}
					});
					temp.add(choose);
					p1.add(key);
					p1.add(temp);
				} else {
					p1.add(key);
					p1.add(val);
				}
			}

			buttonAdd.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					JSONObject jSon = new JSONObject();
					Iterator iter = con.entrySet().iterator();
					while (iter.hasNext()) {
						Map.Entry<JLabel, JTextField> entry = (Map.Entry<JLabel, JTextField>) iter
								.next();
						JLabel key = entry.getKey();
						
						JTextField val = entry.getValue();

						if (key.getText().equals("Name")) {
							filename = val.getText().toString();
						}
						if (key.getText().equals("Description")) {
							String[] a = val.getText().split(" ");
							
						}

						if (key.getText().equals("Optimizer Function")) {
							key.setText("optimizer_fct");
						}


						if (val.getText().equals("")) {
							val.setText("0");
						}

						if(key.getText().equals("mu")||key.getText().equals("lambda")) {
							Integer a = Integer.valueOf(val.getText().toString());
							jSon.put(key.getText(), a);
						}
						else {
							jSon.put(key.getText(), val.getText());
						}

					}
					try {
						File temp = new File("optimizers"+Main.SLASH
								+ filename + ".json");
						boolean success = temp.createNewFile();
						if (success == true) {
							FileWriter fw = new FileWriter(
									"optimizers" + Main.SLASH + filename
											+ ".json");
							jSon.write(fw);
							fw.flush();
							fw.close();
							BufferedWriter fww = new BufferedWriter(
									new FileWriter(
											Main.OPTIMIZER,
											true));
							fww.write(filename.toString());
							fww.newLine();
							fww.flush();
							fww.close();
							initTree(filename, "opti");
						} else {
							JOptionPane.showMessageDialog(null,
									"Already have the Optimizer", "Warning",
									JOptionPane.CLOSED_OPTION);
						}
					} catch (IOException ex) {
						ex.printStackTrace();
					}

					setVisible(false);
				}
			});

			buttonCan.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					con.clear();
					setVisible(false);
				}
			});

			p1.add(buttonAdd);
			p1.add(buttonCan);

			c.add("Center", p1);

			setVisible(true);
		}

		// problem

		else if (type == "prob") {
			JPanel p1 = new JPanel(new GridLayout(10, 2));
			try {
				String line = "";
				FileReader fr = new FileReader(
						Main.DEFAULT_PROBLEM);
				BufferedReader br = new BufferedReader(fr);
				while (br.ready()) {
					line = br.readLine();
					con.put(new JLabel(line), new JTextField());
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			setLocation(100, 150);
			setTitle("Add Problem");
			Container c = getContentPane();
			setSize(350, 400);

			p1.setBorder(new MatteBorder(10, 10, 10, 10, (Icon) null));

			Iterator iter = con.entrySet().iterator();
			while (iter.hasNext()) {
				Map.Entry<JLabel, JTextField> entry = (Map.Entry<JLabel, JTextField>) iter
						.next();
				final JLabel key = entry.getKey();
				final JTextField val = entry.getValue();

				if (key.getText().equals("Path")) {
					JPanel temp = new JPanel();
					val.setColumns(12);
					temp.add(val);
					JButton choose = new JButton("browse");
//					choose.setSize(5, 5);
					choose.addActionListener(new ActionListener() {

						@Override
						public void actionPerformed(ActionEvent e) {
							// TODO Auto-generated method stub
							JFileChooser fc = new JFileChooser("Open File");
							fc.showOpenDialog(null);
							String filepath = fc.getSelectedFile().getPath()
									.toString();
							val.setText(filepath);
						}
					});
					temp.add(choose);
					p1.add(key);
					p1.add(temp);
				} else {
					p1.add(key);
					p1.add(val);
				}
			}

			buttonAdd.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					JSONObject jSon = new JSONObject();
					Iterator iter = con.entrySet().iterator();
					while (iter.hasNext()) {
						Map.Entry<JLabel, JTextField> entry = (Map.Entry<JLabel, JTextField>) iter
								.next();
						JLabel key = entry.getKey();
						JTextField val = entry.getValue();

						if (key.getText().equals("Name")) {
							filename = val.getText().toString();
						}
						
						if (key.getText().equals("Description")) {
							String[] a = val.getText().split(" ");
//							filename = a[0];
						}

						if (key.getText().equals("Problem Executable")) {
							key.setText("problem_exe");
						}

						if (key.getText().equals("input n")) {
							key.setText("N");
						}

						if (key.getText().equals("Lower Bound")) {
							key.setText("lowerbound");
						}

						if (key.getText().equals("Upper Bound")) {
							key.setText("upperbound");
						}

						if (key.getText().equals("Optimum")) {
							key.setText("x_opt");
						}

						if(key.getText().equals("dimension")) {
							Integer a = Integer.valueOf(val.getText().toString());
							jSon.put(key.getText(), a);
						}
						
						
						else {
							jSon.put(key.getText(), val.getText());
						}

					}
					try {
						File temp = new File("problems"+Main.SLASH
								+ filename + ".json");
						boolean success = temp.createNewFile();
						if (success == true) {
							FileWriter fw = new FileWriter(
									"problems"+Main.SLASH
									+ filename + ".json");
							jSon.write(fw);
							fw.flush();
							fw.close();
							BufferedWriter fww = new BufferedWriter(
									new FileWriter(
											Main.PROBLEM, true));
							fww.write(filename.toString());
							fww.newLine();
							fww.flush();
							fww.close();
							initTree(filename, "prob");
						} else {
							JOptionPane.showMessageDialog(null,
									"Already have the Problem", "Warning",
									JOptionPane.CLOSED_OPTION);
						}
					} catch (IOException ex) {
						ex.printStackTrace();
					}

					setVisible(false);
				}
			});

			buttonCan.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					con.clear();
					setVisible(false);
				}
			});

			p1.add(buttonAdd);
			p1.add(buttonCan);

			c.add("Center", p1);

			setVisible(true);
		}
	}
}
