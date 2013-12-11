package com.cr.ui;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.border.MatteBorder;

import net.sf.json.JSONObject;

public class TabContent {

	private JPanel tabContent;

	private double[] q;
	private String tabName;
	private int gridynum = 0;
	private LinkedHashMap<JLabel, JTextField> con;
	private JLabel configname;
	private JTextField configvalue;
	private GridBagLayout gridBagLayout;
	public JScrollPane scrollright;
	private JButton saveButton;
//	private JButton removeButton;
	private String type;

	public TabContent(final String fileName, String type) {
		this.tabName = fileName;
		this.type = type;
		this.scrollright = createTab();
	}

	// Read the config files
	private void readConfig() {
		JSONObject jSon = new JSONObject();
		con = new LinkedHashMap<JLabel, JTextField>();

		String sets = "";
		try {
			System.out.println(type + Main.SLASH
					+ tabName + ".json");
			FileReader fr = new FileReader(type + Main.SLASH
					+ tabName + ".json");
			BufferedReader bf = new BufferedReader(fr);
			while (bf.ready()) {
				sets += bf.readLine();
			}
			bf.close();
			fr.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		jSon = JSONObject.fromObject(sets);
		if (!sets.equals("") && jSon.size() > 1) {

			// GridBagConstraints gbc = new GridBagConstraints();
			// JSONObject jSon = new JSONObject();

			for (int i = 0; i < jSon.size(); i++) {
				configname = new JLabel(jSon.names().getString(i));
				configvalue = new JTextField(jSon.getString(configname
						.getText()));

				con.put(configname, configvalue);
				GridBagConstraints gbc1 = new GridBagConstraints();
				gbc1.insets = new Insets(0, 0, 3, 3);
				// gbc1.anchor = GridBagConstraints.WEST;
				gbc1.fill = GridBagConstraints.HORIZONTAL;
				gbc1.gridx = 0;
				gbc1.gridy = gridynum;

				tabContent.add(configname, gbc1, gridynum);

				GridBagConstraints gb = new GridBagConstraints();
				gb.insets = new Insets(0, 0, 3, 0);
				gb.fill = GridBagConstraints.HORIZONTAL;
				gb.gridx = 1;
				gb.gridy = gridynum;

				tabContent.add(configvalue, gb, gridynum);

				gridynum++;

			}

		}

		q = new double[gridynum + 1];
		for (int j = 0; j < gridynum; j++) {
			q[j] = 0.0;
		}
		q[gridynum] = 1.0E-4;
		gridBagLayout.rowWeights = q;
		tabContent.setLayout(gridBagLayout);

		// rightpane.setLayout(new GridLayout(gridynum,2));
		// scrollright = new JScrollPane(tabContent);

	}

	private void saveConfig(String filename) {
		JSONObject jSon = new JSONObject();
		Iterator iter = con.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry<JLabel, JTextField> entry = (Map.Entry<JLabel, JTextField>) iter
					.next();
			JLabel key = entry.getKey();
			JTextField val = entry.getValue();
			jSon.put(key.getText(), val.getText());

		}
		try {
			FileWriter fw = new FileWriter(filename);
			jSon.write(fw);
			fw.flush();
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private JScrollPane createTab() {

		q = new double[gridynum + 1];
		for (int i = 0; i < gridynum; i++) {
			q[i] = 0.0;
		}
		q[gridynum] = 1.0E-4;

		tabContent = new JPanel();
		tabContent.setBorder(new MatteBorder(10, 10, 10, 10, (Color) null));
		gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWeights = new double[] { 2.0, 1.0, 1.0E-4 };
		// The number of the rows in the panel that shows tag and parameters is
		// restricted to be 12 .
		gridBagLayout.rowWeights = q;
		tabContent.setLayout(gridBagLayout);
		readConfig();

		JPanel ptemp = new JPanel();
		FlowLayout flowLayout = (FlowLayout) ptemp.getLayout();
		flowLayout.setAlignment(FlowLayout.LEFT);
		saveButton = new JButton("Save");
//		removeButton = new JButton("Remove");
		ptemp.add(saveButton);
//		ptemp.add(removeButton);
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.anchor = GridBagConstraints.EAST;
		gbc.insets = new Insets(0, 0, 3, 0);
		gbc.gridx = 1;
		gbc.gridy = gridynum;

		saveButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				saveConfig(type + Main.SLASH + tabName + ".json");
			}
		});

	

		tabContent.add(ptemp, gbc);

		JScrollPane scroll = new JScrollPane(tabContent);

		// System.out.println(gridynum);

		return scroll;
	}
}
