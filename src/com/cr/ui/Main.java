package com.cr.ui;

import java.awt.EventQueue;

import javax.swing.UIManager;

public class Main {
	
	public static String os;
	public static String OPTIMIZER;
	public static String PROBLEM;
	public static String PROJECT;
	public static String SLASH;
	public static String DEFAULT_OPTIMIZER;
	public static String DEFAULT_PROBLEM;
	public static String DEFAULT_RUN;
	
	
	private static void getOS() {
		os = System.getProperty("os.name");
		if(os.equals("Linux")) {
			OPTIMIZER = "optimizers/optimizers.txt";
			PROBLEM = "problems/problems.txt";
			PROJECT = "projects/projects.txt";
			DEFAULT_OPTIMIZER = "Default/Default_opti.txt";
			DEFAULT_PROBLEM = "Default/Default_prob.txt";
			DEFAULT_RUN = "Default/Default_run.txt";
			SLASH = "/";
		}
		else if(os.equals("Windows 7")) {
			OPTIMIZER = "optimizers\\optimizers.txt";
			PROBLEM = "problems\\problems.txt";
			PROJECT = "projects\\projects.txt";
			DEFAULT_OPTIMIZER = "Default\\Default_opti.txt";
			DEFAULT_PROBLEM = "Default\\Default_prob.txt";
			DEFAULT_RUN = "Default\\Default_run.txt";
			SLASH = "\\";
		}
	}
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				
				try {
					getOS();
					UIManager.setLookAndFeel(UIManager
							.getSystemLookAndFeelClassName());
					Window window = new Window();
					window.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}
