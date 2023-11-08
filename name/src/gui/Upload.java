package gui;

import java.awt.EventQueue;
import AST.GraphPrinter;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JButton;
import javax.swing.JTextField;

import dataAnalysis.*;
import preprocess.LexicalAnalysis;
import serialise.RunAnalysis;

import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JTextArea;
import java.awt.Color;

import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import java.awt.Button;

public class Upload {

	private JFrame frame;
	public static  File srcCode = null;
	private ArrayList<File> filesList;
	private ArrayList<String> fileNames;
	DataGraph graph = new DataGraph();
	double val = 0;
	int fileCount = 0;
	
	RunAnalysis run;
	LexicalAnalysis lex;
	ReportsUI reportUI;
	TaintedUI taint;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() { // <<BACKEND>>
			public void run() {
				try {
					Upload window = new Upload(srcCode);
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
			
		});
		
		
	}

	/**
	 * Create the application.
	 */
	public Upload(File code) {
		initialize(code);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize(File srcCode) {
		frame = new JFrame();
		frame.getContentPane().setBackground(new Color(204, 204, 255));
		frame.setBounds(100, 100, 590, 664);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(47, 220, 278, 336);
		frame.getContentPane().add(scrollPane);
		
		JTextArea txtBefore = new JTextArea();
		txtBefore.setEditable(false);
		txtBefore.setFont(new Font("Bookman Old Style", Font.ITALIC, 15));
		scrollPane.setViewportView(txtBefore);
		
		JButton btnOpenFile = new JButton("Select Files");
		btnOpenFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			
				 JFileChooser fileChooser = new JFileChooser();
				 fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
			        fileChooser.setMultiSelectionEnabled(true);
			        int returnValue = fileChooser.showOpenDialog(null);
			        filesList = new ArrayList<File>();
			        fileNames = new ArrayList<String>();
			        if (returnValue == JFileChooser.APPROVE_OPTION) {
			            File[] selectedFiles = fileChooser.getSelectedFiles();
			            
			            fileCount = selectedFiles.length;
			            for (File file : selectedFiles) {
			                System.out.println("Selected file: " + file.getName());
			                
			                txtBefore.append(file.getName());
			                txtBefore.append("\n");
			                txtBefore.append("\n");
			               filesList.add(file);
			               fileNames.add(file.getName());
			            }
			        }
			        
				 fileChooser.showOpenDialog(fileChooser);
				 fileChooser.setVisible(true);
			
				
				   // int result = fileChooser.showOpenDialog(btnOpenFile);
		/**trying to make singles*/		  // srcCode = fileChooser.getSelectedFile();
				 // lex = new LexicalAnalysis(srcCode);
				   // txtUpload.setText(srcCode.getAbsolutePath());
			}
		});
		btnOpenFile.setBounds(67, 134, 156, 23);
		frame.getContentPane().add(btnOpenFile);
		
		JLabel lblUploadLabel = new JLabel("Upload your source code");
		lblUploadLabel.setForeground(Color.DARK_GRAY);
		lblUploadLabel.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblUploadLabel.setBounds(47, 91, 214, 32);
		frame.getContentPane().add(lblUploadLabel);
		
		
		
		
		JLabel lblMASA = new JLabel("Mobile Application Security Analyser");
		lblMASA.setForeground(Color.BLACK);
		lblMASA.setFont(new Font("Bookman Old Style", Font.ITALIC, 25));
		lblMASA.setBounds(35, 21, 499, 59);
		frame.getContentPane().add(lblMASA);
		
		JLabel lblSrc = new JLabel("Uploaded Files");
		lblSrc.setFont(new Font("Trebuchet MS", Font.BOLD, 13));
		lblSrc.setBounds(67, 193, 95, 16);
		frame.getContentPane().add(lblSrc);
		
		JButton btnTaint = new JButton("Taint Analysis");
		btnTaint.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {JOptionPane.showMessageDialog(null, "Static tainted data analysis is a code analysis technique that identifies " + "\n"
					+ "how data flows through a program without executing it. It traces the sources and paths of user input (tainted data) to determine where it can " + "\n"
					+ "reach sensitive operations. This method is effective because it allows for the early detection of potential security risks, such as data leakage" + "\n"
					+ " or unauthorized access. By identifying and tracking tainted data, developers can implement appropriate security measures to prevent vulnerabilities" + "\n"
					+ " related to improper handling of user input. This proactive approach enhances the overall security posture of the application.","Tainted Data Info", JOptionPane.INFORMATION_MESSAGE);}
		});
		btnTaint.setBounds(364, 378, 117, 23);
		frame.getContentPane().add(btnTaint);
		
		JButton btnSQLInj = new JButton("SQL Injection");
		btnSQLInj.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(null, "Static SQL injection analysis is a code inspection technique that identifies vulnerabilities "+"\n"
						+ "in source code without executing the program. It analyzes the code's structure to pinpoint potential injection points " +"\n"
						+ "where untrusted data is concatenated into SQL queries. This method is effective because it can uncover vulnerabilities " +"\n"
						+ "early in the development process, enabling timely fixes and reducing security risks associated with SQL injection attacks." +"\n"
						+ " It provides a proactive approach to security, ensuring that applications are built with robust defenses against this common" +"\n"
						+ " type of attack","SQL Injection Info", JOptionPane.INFORMATION_MESSAGE);
			}
		});
		btnSQLInj.setBounds(364, 274, 123, 23);
		frame.getContentPane().add(btnSQLInj);
		
		
		JButton btnNewButton = new JButton("Analyse");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				 run = new RunAnalysis(filesList);
				val += run.calcTaintRatio();
				JOptionPane.showMessageDialog(null, "The analysis has been completed and the individual files have been created" +"\n"
				+ "To see a summary of the analysis, press the Report Summary button upon closing this message","Analysis Complete", JOptionPane.INFORMATION_MESSAGE);
			}
		});
		btnNewButton.setBounds(364, 491, 117, 23);
		frame.getContentPane().add(btnNewButton);
		
		JButton btnNewButton_1 = new JButton("Report Summary");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//presence of SQL injection vulnerability
				reportUI = new ReportsUI(val/fileCount,run.present());
				reportUI.setVisible(true);
				frame.dispose();
			}
		});
		btnNewButton_1.setForeground(new Color(64, 128, 128));
		btnNewButton_1.setBounds(364, 591, 147, 23);
		frame.getContentPane().add(btnNewButton_1);
		
	
		
		
	}
}
