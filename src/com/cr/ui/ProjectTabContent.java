package com.cr.ui;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedHashMap;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.border.MatteBorder;

import net.sf.json.JSONObject;

public class ProjectTabContent {
	public static JSplitPane tabContent;
	private JButton runButton;
	private JComboBox selectProj;
	private JPanel tabLeft;
	private JPanel tabRight;
	private int gnum = 0;
	private LinkedHashMap<String,String> con;
	private String filename;

	public  ProjectTabContent() {
//		this.filename = ;
		this.tabContent = createTab();
	}
	
	private JSplitPane createTab() {
		
		con = new LinkedHashMap<String,String>();
		runButton = new JButton("Run");
		final JSplitPane tab = new JSplitPane();
		final JSplitPane tt = new JSplitPane();
		tt.setOrientation(JSplitPane.VERTICAL_SPLIT);
		tt.setEnabled(false);
		tt.setResizeWeight(0.5);
		tabRight = new JPanel();
		tabLeft = new JPanel();
		tabLeft.setBorder(new MatteBorder(10, 10, 10, 10, (Color) null));
		tabRight.setBorder(new MatteBorder(10, 10, 10, 10, (Color) null));
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWeights = new double[] { 2.0, 1.0, 1.0E-4 };
		gridBagLayout.rowWeights = new double[]{0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,1.0E-4};
		tabRight.setLayout(gridBagLayout);
		tabLeft.setLayout(gridBagLayout);
		tab.setOrientation(JSplitPane.VERTICAL_SPLIT);
		tab.setEnabled(false);
		selectProj = new JComboBox();
		readProject();
		tt.setLeftComponent(tabLeft);
		tt.setRightComponent(tabRight);
		
		selectProj.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {
				// TODO Auto-generated method stub
				tabRight.removeAll();
				gnum = 0;
				JSONObject json = new JSONObject();
				String sets = "";
				filename = (String) e.getItem();
				try {
					FileReader fr = new FileReader("projects" + Main.SLASH + "project__Ackley10D__CMAES_rodeolib.json");
					System.out.println(e.getItem());
					BufferedReader br = new BufferedReader(fr);
					while (br.ready()) {
						sets+=br.readLine();
					}
					br.close();
					fr.close();
					
				}catch(IOException e1) {
					e1.printStackTrace();
				}
//				int i = 0;
				json = JSONObject.fromObject(sets);
				 {
					for(int i = 0;i<json.size();i++) {
						String key = json.names().getString(i);
						String value = json.getString(key.toString());
						JLabel lab = new JLabel(key);
						JTextField val = new JTextField(json.getString(key.toString()));
						val.setEnabled(false);
						GridBagConstraints gbc = new GridBagConstraints();
						gbc.insets = new Insets(0, 0, 3, 3);
						gbc.fill = GridBagConstraints.HORIZONTAL;
						gbc.gridx = 0;
						gbc.gridy = gnum;
						tabLeft.add(lab, gbc);
						
						GridBagConstraints gbc1 = new GridBagConstraints();
						gbc1.insets = new Insets(0, 0, 3, 3);
						gbc1.fill = GridBagConstraints.HORIZONTAL;
						gbc1.gridx = 1;
						gbc1.gridy = gnum;
						tabLeft.add(val,gbc1);
						
						con.put(key, value);
						gnum++;
					}
				}
				
				 tt.setLeftComponent(tabLeft);
				 
					sets = "";
				try {
					FileReader fr = new FileReader("projects"+Main.SLASH + "run__default.json");
					BufferedReader br = new BufferedReader(fr);
					while(br.ready()) {
						sets+=br.readLine();
					}
					br.close();
					fr.close();
				}catch(IOException e1) {
					e1.printStackTrace();
				}
				
				json.clear();
				json = JSONObject.fromObject(sets);
				JLabel runsetting = new JLabel("Run Settings: ");
				GridBagConstraints gb = new GridBagConstraints();
				gb.insets = new Insets(3, 0, 5, 3);
				gb.fill = GridBagConstraints.HORIZONTAL;
				gb.gridx = 0;
				gb.gridy = gnum;
				tabRight.add(runsetting, gb);
				gnum++;
				for(int i = 0;i<json.size();i++) {
					String key = json.names().getString(i);
					String value = json.getString(key.toString());
					JLabel lab = new JLabel(key);
					JTextField val = new JTextField(json.getString(key.toString()));
					val.setEnabled(false);
					GridBagConstraints gbc = new GridBagConstraints();
					gbc.insets = new Insets(0, 0, 3, 3);
					gbc.fill = GridBagConstraints.HORIZONTAL;
					gbc.gridx = 0;
					gbc.gridy = gnum;
					tabRight.add(lab, gbc);
					
					GridBagConstraints gbc1 = new GridBagConstraints();
					gbc1.insets = new Insets(0, 0, 3, 0);
					gbc1.fill = GridBagConstraints.HORIZONTAL;
					gbc1.gridx = 1;
					gbc1.gridy = gnum;
					tabRight.add(val,gbc1);
					
					con.put(key, value);
					gnum++;
				}
				
				GridBagConstraints gbc = new GridBagConstraints();
				gbc.insets = new Insets(0, 0, 3, 3);
				gbc.anchor = GridBagConstraints.WEST;
				gbc.gridx = 0;
				gbc.gridy = gnum+10;
				JPanel temp = new JPanel();
				temp.add(runButton);
				tabRight.add(temp, gbc);
				tt.setRightComponent(tabRight);
			}
			
		});
		
		runButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				if(con.get("type").equals("MATLAB")) {
					if(Main.os.equals("Linux")) {
						String command = "./execute_matlab_project.sh /auto_mnt/appl/MATHWORKS_R2010B/bin/matlab "+ "project__"+filename + ".json";
						try {
							Process process = Runtime.getRuntime().exec(command); 
							
						}catch(IOException e1) {
							e1.printStackTrace();
						}
					}
					else if(Main.os.equals("Windows 7")) {
						String command = "matlab -nodisplay -nodesktop -nojvm -nosplash -r ufo_matlab_optimizer('projects/project__"+filename+".json')";
						try {
							Process process = Runtime.getRuntime().exec(command); 
						}catch(IOException ex) {
							ex.printStackTrace();
						}
					}
				}
			}
		});
		
		JPanel p = new JPanel();
		FlowLayout f = (FlowLayout)p.getLayout();
		f.setAlignment(FlowLayout.LEFT);
		p.add(selectProj);
		
		tab.setLeftComponent(p);
		
		tab.setRightComponent(tt);
		
		
		return tab;
	}
	
	private void readProject() {
		selectProj.addItem("");
		String line ="";
		try {
			FileReader fr = new FileReader("projects" + Main.SLASH + "projects.txt");
			BufferedReader br = new BufferedReader(fr);
			while(br.ready()) {
				line = br.readLine();
				selectProj.addItem(line);
			}
			br.close();
			fr.close();
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
	
}
