package com.cr.ui;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.border.MatteBorder;
import javax.swing.tree.DefaultMutableTreeNode;

import net.sf.json.JSONObject;

import java.awt.event.MouseListener;
import java.awt.event.MouseAdapter;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class ConfTabContent {
	private JPanel tabContentLeft;
	private JPanel tabContentRight;
	private double[] q;
	private LinkedHashMap<JLabel, JTextField> con;
	private int gridynum = 0;
	private JTextField configname;
	private JTextField configdes;
	// private JTextField configvalue;
	private GridBagLayout gridBagLayout;
	public static JSplitPane tabContent;
	private JButton saveButton;

//	private JPopupMenu addparam;

	private JComboBox selectopti;
	private JComboBox selectprob;

//	private JTextField paramname;
//	private JTextField paramvalue;

	private JButton saveConfig;

	private String filename;

	private int count = 0;

	private String type;

	public ConfTabContent(String filename, String type) {
		this.filename = filename;
		this.type = type;
		this.tabContent = createTab();
	}

	private JSplitPane createTab() {

		con = new LinkedHashMap<JLabel, JTextField>();
		JLabel config1 = new JLabel("Configuration Name");
		JLabel config2 = new JLabel("Configuration Description");
		JLabel config3 = new JLabel("Select Optimizer");
		JLabel config4 = new JLabel("Select Problem");
		JLabel runsetting = new JLabel("Run Settings:  ");
		q = new double[gridynum + 1];
		for (int i = 0; i < gridynum; i++) {
			q[i] = 0.0;
		}
		q[gridynum] = 1.0E-4;

		final JSplitPane splitPane = new JSplitPane();
		tabContentLeft = new JPanel();
		tabContentLeft.setBorder(new MatteBorder(10, 10, 10, 10, (Color) null));
		GridBagLayout gridBagLayout1 = new GridBagLayout();
		gridBagLayout1.columnWeights = new double[] { 2.0, 1.0, 1.0E-4 };
		gridBagLayout1.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 1.0E-4 };
		tabContentLeft.setLayout(gridBagLayout1);

		tabContentRight = new JPanel();
		tabContentRight
				.setBorder(new MatteBorder(10, 10, 10, 10, (Color) null));
		gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWeights = new double[] { 2.0, 1.0, 1.0E-4 };
		gridBagLayout.rowWeights = new double[] { 0.0, 1.0E-4 };
		tabContentRight.setLayout(gridBagLayout);

		configname = new JTextField();
		configdes = new JTextField();

		selectopti = new JComboBox();
		selectprob = new JComboBox();

		// splitPane.setResizeWeight(0.1);
		splitPane.setEnabled(false);
		splitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
		splitPane.setLeftComponent(tabContentLeft);

		// left panel
		int i = 0;
		{
			GridBagConstraints gbc = new GridBagConstraints();
			gbc.insets = new Insets(0, 0, 3, 3);
			gbc.fill = GridBagConstraints.HORIZONTAL;
			gbc.gridx = 0;
			gbc.gridy = 0;
			tabContentLeft.add(config1, gbc);
		}
		{
			GridBagConstraints gbc = new GridBagConstraints();
			gbc.insets = new Insets(0, 0, 3, 0);
			gbc.fill = GridBagConstraints.HORIZONTAL;
			gbc.gridx = 1;
			gbc.gridy = 0;
			tabContentLeft.add(configname, gbc);
		}

		{
			GridBagConstraints gbc = new GridBagConstraints();
			gbc.insets = new Insets(0, 0, 3, 3);
			gbc.fill = GridBagConstraints.HORIZONTAL;
			gbc.gridx = 0;
			gbc.gridy = 1;
			tabContentLeft.add(config2, gbc);
		}
		{
			GridBagConstraints gbc = new GridBagConstraints();
			gbc.insets = new Insets(0, 0, 3, 0);
			gbc.fill = GridBagConstraints.HORIZONTAL;
			gbc.gridx = 1;
			gbc.gridy = 1;
			tabContentLeft.add(configdes, gbc);
		}
		{
			GridBagConstraints gbc = new GridBagConstraints();
			gbc.insets = new Insets(0, 0, 3, 3);
			gbc.fill = GridBagConstraints.HORIZONTAL;
			gbc.gridx = 0;
			gbc.gridy = 2;
			tabContentLeft.add(config3, gbc);
		}
		{
			GridBagConstraints gbc = new GridBagConstraints();
			gbc.insets = new Insets(0, 0, 3, 0);
			gbc.fill = GridBagConstraints.HORIZONTAL;
			gbc.gridx = 1;
			gbc.gridy = 2;
			tabContentLeft.add(selectopti, gbc);
		}
		{
			GridBagConstraints gbc = new GridBagConstraints();
			gbc.insets = new Insets(0, 0, 3, 3);
			gbc.fill = GridBagConstraints.HORIZONTAL;
			gbc.gridx = 0;
			gbc.gridy = 3;
			tabContentLeft.add(config4, gbc);
		}
		{
			GridBagConstraints gbc = new GridBagConstraints();
			gbc.insets = new Insets(0, 0, 3, 0);
			gbc.fill = GridBagConstraints.HORIZONTAL;
			gbc.gridx = 1;
			gbc.gridy = 3;
			tabContentLeft.add(selectprob, gbc);
		}

		readProblems();
		readeOptimizers();
		
		

		// right panel
//		JButton btaddparam = new JButton("New Parameter");

//		addparam = new JPopupMenu();

//		JPanel p = new JPanel(new GridLayout(3, 2));
//		JLabel lab1 = new JLabel("Name:");
//		JLabel lab2 = new JLabel("Value:");
//		paramname = new JTextField();
//		paramvalue = new JTextField();
//		JButton btok = new JButton("OK");
//		JButton btcan = new JButton("Cancel");
//		p.add(lab1);
//		p.add(paramname);
//		p.add(lab2);
//		p.add(paramvalue);
//		p.add(btok);
//		p.add(btcan);

//		addparam.add(p);

//		btaddparam.addMouseListener(new MouseAdapter() {
//			public void mousePressed(MouseEvent event) {
//				triggerEvent(event);
//			}

//			public void mouseReleased(MouseEvent event) {
//				triggerEvent(event);
//			}

//			private void triggerEvent(MouseEvent event) {
//				if (event.isPopupTrigger()) {
//					addparam.show(event.getComponent(), event.getX(),
//							event.getY());
//				}
//			}
//		});

/*		btok.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				JLabel lab = new JLabel(paramname.getText().toString());
				JTextField value = new JTextField(paramvalue.getText()
						.toString());
				paramname.setText("");
				paramvalue.setText("");
				{
					GridBagConstraints gbc = new GridBagConstraints();
					gbc.insets = new Insets(0, 0, 3, 3);
					gbc.fill = GridBagConstraints.HORIZONTAL;
					gbc.gridx = 0;
					gbc.gridy = gridynum;
					tabContentRight.add(lab, gbc);
				}
				{
					GridBagConstraints gbc = new GridBagConstraints();
					gbc.insets = new Insets(0, 0, 3, 0);
					gbc.fill = GridBagConstraints.HORIZONTAL;
					gbc.gridx = 1;
					gbc.gridy = gridynum;
					tabContentRight.add(value, gbc);

				}

				con.put(lab, value);

				{
					GridBagConstraints gbc = new GridBagConstraints();
					gbc.insets = new Insets(0, 0, 3, 0);
					gbc.gridx = 1;
					gbc.anchor = GridBagConstraints.EAST;
					gbc.gridy = gridynum + 10;
					tabContentRight.add(saveConfig, gbc);
				}

				gridynum++;
				q = new double[gridynum + 1];
				for (int j = 0; j < gridynum; j++) {
					q[j] = 0.0;
				}
				q[gridynum] = 1.0E-4;
				gridBagLayout.rowWeights = q;
				tabContentRight.setLayout(gridBagLayout);

				JScrollPane scroll = new JScrollPane(tabContentRight);
				splitPane.setRightComponent(scroll);

				addparam.setVisible(false);
			}

		});

		btcan.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				paramname.setText("");
				paramvalue.setText("");
				addparam.setVisible(false);
			}
		});
*/
		JPanel ptemp = new JPanel();

		ptemp.add(runsetting);
//		ptemp.add(btaddparam);
		FlowLayout flowLayout = (FlowLayout) ptemp.getLayout();
		flowLayout.setAlignment(FlowLayout.LEFT);

		{
			GridBagConstraints gbc = new GridBagConstraints();
			gbc.insets = new Insets(0, 0, 3, 3);
			gbc.fill = GridBagConstraints.HORIZONTAL;
			gbc.gridx = 0;
			gbc.gridy = 0;
			tabContentRight.add(ptemp, gbc);
		}
	
		q = new double[]{0.0,0.0,0.0,0.0,1.0E-4};
		gridBagLayout.rowWeights = q;
		tabContentRight.setLayout(gridBagLayout);
		JScrollPane scroll1 = new JScrollPane(tabContentRight);
		splitPane.setRightComponent(scroll1);
		
		if(type.equals("write")) {
			gridynum++;
		String run = ""; 
		try {
			FileReader fr = new FileReader(Main.DEFAULT_RUN);
			BufferedReader br = new BufferedReader(fr);
			while(br.ready()) {
				run = br.readLine();
				JLabel lab = new JLabel(run.toString());
				GridBagConstraints gbc = new GridBagConstraints();
				gbc.insets = new Insets(0, 0, 3, 3);
				gbc.fill = GridBagConstraints.HORIZONTAL;
				gbc.gridx = 0;
				gbc.gridy = gridynum;
				tabContentRight.add(lab, gbc);
				JTextField value = new JTextField();
				GridBagConstraints gbc1 = new GridBagConstraints();
				gbc1.insets = new Insets(0, 0, 3, 0);
				gbc1.fill = GridBagConstraints.HORIZONTAL;
				gbc1.gridx = 1;
				gbc1.gridy = gridynum;
				tabContentRight.add(value, gbc1);
				gridynum++;
				con.put(lab, value);
				
			}
			br.close();
			fr.close();
		}catch(IOException e) {
			e.printStackTrace();
		}
		}
		
		saveConfig = new JButton("Save");

		{
			GridBagConstraints gbc = new GridBagConstraints();
			gbc.insets = new Insets(0, 0, 3, 0);
			gbc.anchor = GridBagConstraints.EAST;
			gbc.gridx = 1;
			gbc.gridy = gridynum + 10;
			tabContentRight.add(saveConfig, gbc);
		}

		saveConfig.addActionListener(configSave());

		gridynum++;
		q = new double[gridynum + 1];
		for (int j = 0; j < gridynum; j++) {
			q[j] = 0.0;
		}

		JScrollPane scroll = new JScrollPane(tabContentRight);
		splitPane.setRightComponent(scroll);

		if (type.equals("read")) {
			JSONObject json = new JSONObject();
			String sets = "";
			try {
				FileReader fr = new FileReader(
						"projects"+Main.SLASH + "project__"+filename
								+ ".json");
				BufferedReader br = new BufferedReader(fr);
				while (br.ready()) {
					sets += br.readLine();
				}
				br.close();
				fr.close();
			} catch (IOException e) {
				e.printStackTrace();
			}

			json = JSONObject.fromObject(sets);
			configname.setText(filename);
			configdes.setText(json.getString("description"));
			String problem = json.getString("problem_json");
//			String problem = temp.substring(0, temp.indexOf("_"));
			selectprob.setSelectedItem(problem);
			String optimizer = json.getString("optimizer_json");
//			String optimizer = temp.substring(0, temp.indexOf("_"));
			selectopti.setSelectedItem(optimizer);

			json.clear();

			// read run_parameters.json
			String run1 = "";
			try {
				FileReader fr = new FileReader("projects"+Main.SLASH+"run__default.json");
				BufferedReader br = new BufferedReader(fr);
				while (br.ready()) {
					run1 += br.readLine();

				}
				br.close();
				fr.close();
				json = JSONObject.fromObject(run1);
				// LinkedHashMap<JLabel,JTextField> map = new
				// LinkedHashMap<JLabel, JTextField>();
				for (int i1 = 0; i1 < json.size(); i1++) {
					JLabel lab = new JLabel(json.names().getString(i1));
					JTextField value = new JTextField(json.getString(lab
							.getText()));
					// map.put(lab, value);
					con.put(lab, value);

					GridBagConstraints gbc1 = new GridBagConstraints();
					gbc1.insets = new Insets(0, 0, 3, 3);
					// gbc1.anchor = GridBagConstraints.WEST;
					gbc1.fill = GridBagConstraints.HORIZONTAL;
					gbc1.gridx = 0;
					gbc1.gridy = gridynum;
					tabContentRight.add(lab, gbc1);

					GridBagConstraints gbc = new GridBagConstraints();
					gbc.insets = new Insets(0, 0, 3, 0);
					// gbc1.anchor = GridBagConstraints.WEST;
					gbc.fill = GridBagConstraints.HORIZONTAL;
					gbc.gridx = 1;
					gbc.gridy = gridynum;
					tabContentRight.add(value, gbc);

					GridBagConstraints gbc11 = new GridBagConstraints();
					gbc11.insets = new Insets(0, 0, 3, 0);
					gbc11.gridx = 1;
					gbc11.anchor = GridBagConstraints.EAST;
					gbc11.gridy = gridynum + 10;
					tabContentRight.add(saveConfig, gbc11);
					gridynum++;
				}

			} catch (IOException e) {
				e.printStackTrace();
			}
		
			
			
		}

		return splitPane;
	}

	private ActionListener configSave() {
		// TODO Auto-generated method stub
		ActionListener al = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				if (configname.getText().equals("")) {
					JOptionPane.showMessageDialog(null,
							"Please Input the Configuration Name", "Warning",
							JOptionPane.CLOSED_OPTION);
				} else {
					fileSave();
					initTree();
				}
			}

		};

		return al;
	}

	// read problems
	private void readProblems() {
		String line = "";
		try {
			FileReader fr = new FileReader(Main.PROBLEM);
			BufferedReader br = new BufferedReader(fr);
			while (br.ready()) {
				line = br.readLine();
				selectprob.addItem(line);
			}
			br.close();
			fr.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// read optimizers
	private void readeOptimizers() {
		String line = "";
		try {
			FileReader fr = new FileReader(Main.OPTIMIZER);
			BufferedReader br = new BufferedReader(fr);
			while (br.ready()) {
				line = br.readLine();
				selectopti.addItem(line);
			}
			br.close();
			fr.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// save project.json file and run_parameter.json file
	private void fileSave() {
		// project.json file Save
		JSONObject json = new JSONObject();
		json.put("description", configdes.getText().toString());
		String problem = (String) selectprob.getSelectedItem();
		String optimizer = (String) selectopti.getSelectedItem();
		json.put("problem_json", problem + "_opti.json");
		json.put("optimizer_json", optimizer + "_prob.json");
		json.put("run_json", configname.getText() + "_run_parameters.json");

		try {
			FileWriter fw = new FileWriter("projects"+Main.SLASH
					+ "project__" + configname.getText() + ".json");
			json.write(fw);
			fw.close();
			json.clear();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// run_parameter.json file Save
		Iterator iter = con.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry<JLabel, JTextField> entry = (Map.Entry<JLabel, JTextField>) iter
					.next();
			JLabel key = entry.getKey();
			JTextField val = entry.getValue();
			if(key.getText().equals("termination_max_evaluations")||key.getText().equals("seed")) {
				Integer a = Integer.valueOf(val.getText().toString());
				json.put(key.getText(), a);
			}
			else {
				json.put(key.getText(), val.getText());
			}
		}

		try {
			FileWriter fw = new FileWriter("projects"+Main.SLASH
					+"run__default.json");
			json.write(fw);
			fw.close();
			json.clear();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private void initTree() {
		int flag = 0;
		for (int i = 0; i < Window.coupling.getChildCount(); i++) {
			DefaultMutableTreeNode node = (DefaultMutableTreeNode) Window.coupling
					.getChildAt(i);
			if (node.toString().equals(configname.getText())) {
				flag = 1;
				break;
			}
		}

		if (flag == 0) {
			Window.coupling.insert(new DefaultMutableTreeNode(configname
					.getText().toString(), true), count);
			count++;
			Window.tree.updateUI();
			
			//add item in the project_coupling.txt
			try {
				FileWriter fww = new FileWriter(
						Main.PROJECT, true);
				fww.write("\n" + configname.getText());
				fww.flush();
				fww.close();
			}catch(IOException e) {
				e.printStackTrace();
			}
		}
	}
}
